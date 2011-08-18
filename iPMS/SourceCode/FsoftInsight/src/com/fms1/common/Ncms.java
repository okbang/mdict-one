/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package com.fms1.common;

import java.sql.*;
import java.util.Vector;

import com.fms1.infoclass.AssignmentInfo;
import com.fms1.infoclass.ConformityNCInfo;
import com.fms1.infoclass.NCInfo;
import com.fms1.infoclass.ResponsibilityInfo;
import com.fms1.infoclass.TaskInfo;
import com.fms1.tools.CommonTools;
import com.fms1.tools.Table;
import com.fms1.web.Parameters;
import com.fms1.web.ServerHelper;
import com.fms1.infoclass.*;

/**
 * NCMS/call log interface
 * @author manu
 *
 */
public class Ncms {
	public static String CALL_LOG_PREFIX="Call log: ";
	public static int getCustoComplaintsCount(long projectID, Date endDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		int getCustoComplaintsCount = 0;
		try {

			sql =
				"SELECT "
					+ "COUNT(N.NCID) S_NC "
					+ "FROM "
					+ "NC N,PROJECT P "
					+ "WHERE "
					+ "N.STATUS <>6 AND "
					+ "N.NCTYPE BETWEEN 23 AND 26 AND "
					+ "N.PROJECTID =P.CODE AND "
					+ "P.PROJECT_ID=?"
					
					+ ((endDate==null)?"":" and N.CREATIONDATE<?");
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			if (endDate!=null)
				prepStmt.setDate(2, endDate);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				getCustoComplaintsCount = rs.getInt("S_NC");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return getCustoComplaintsCount;
		}
	}
	public static int getOverdueNCsObsCount(long projectID, Date endDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;

		int getOverdueNCsObsCount = 0;
		try {
			sql =
				"SELECT NCID, DEADLINE, CLOSUREDATE "
					+ "FROM "
					+ "NC N,PROJECT P "
					+ "WHERE "
					+ "N.STATUS <>6 AND "
					+ "N.NCTYPE IN (21,22) AND "
					+ "N.PROJECTID =P.CODE AND "
					+ "N.CREATIONDATE >= TO_DATE ('10/01/2008', 'mm/dd/yyyy') AND "
					+ "P.PROJECT_ID=? "
					
					+ ((endDate==null)?"":" and N.CREATIONDATE<?");
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			if (endDate!=null)
				prepStmt.setDate(2, endDate);
			rs = prepStmt.executeQuery();
			java.util.Date now = new java.util.Date();
			String currentDate;
			currentDate = CommonTools.dateFormat(now);
			while (rs.next()) {
				if(rs.getDate("CLOSUREDATE") != null){
					if(rs.getDate("CLOSUREDATE").compareTo(rs.getDate("DEADLINE")) > 0){
						getOverdueNCsObsCount++; 
					}
				}
				else{
					if(CommonTools.parseDate(currentDate).compareTo(rs.getDate("DEADLINE")) > 0){
						getOverdueNCsObsCount++; 
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return getOverdueNCsObsCount;
		}
	}
	/**
	* get conformityNC
	* @param: projectID
	* @return: array of comformityNC information
	*/
	public static final ConformityNCInfo[] getConfNCDefect(final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		ConformityNCInfo[] myArray = null;
		try {
			vt = new Vector();
			
			final String[] strArr = new String[4];
			strArr[0] = "Quality control";
			strArr[1] = "Internal audit";
			strArr[2] = "External audit";
			strArr[3] = "Total";
			myArray = new ConformityNCInfo[4];
			for (int i = 0; i < 4; i++) {
				myArray[i] = new ConformityNCInfo(strArr[i]);
			}
			sql =
				"SELECT COUNT(NCID) NUM,DETECTEDBY,NCTYPE"
					+ " FROM NC, PROJECT"
					+ " WHERE TRIM(NC.PROJECTID) = TRIM(PROJECT.CODE)"
					+ " AND PROJECT.PROJECT_ID = ?"
					+ " AND DETECTEDBY IN (28, 29, 30)"
					+ " AND NC.STATUS <>6  "
					+ " AND NCTYPE IN (21,22)"
					+ " GROUP BY DETECTEDBY, NCTYPE";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			rs = stm.executeQuery();
			int num = 0;
			int i = 0;
			int j = 0;
			while (rs.next()) {
				num = rs.getInt("NUM");
				i = rs.getInt("DETECTEDBY") - 28;
				j = rs.getInt("NCTYPE") - 21;
				if (j == 0)
					myArray[i].numNC += num;
				else 
					myArray[i].numOb += num;
			}
			for (i = 0; i < 3; i++) {
				myArray[3].numNC += myArray[i].numNC;
				myArray[3].numOb += myArray[i].numOb;
			}
			for (i = 0; i < myArray.length; i++) {
				vt.add(myArray[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return myArray;
		}
	}
	public static double getNCCount(long projectID, java.sql.Date endDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		double getNCCount = 0;
		try {
			sql =
				" SELECT COUNT(*) NUMBER_OF_NC FROM NC,PROJECT P "
					+ " WHERE NCTYPE IN (21,22)"
					+ " AND NC.STATUS <>6  "
					+ " AND  NC.PROJECTID =P.CODE"
					+ " AND P.PROJECT_ID=?"
					+ ((endDate != null) ? " AND CREATIONDATE <= ?" : "");
					
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			if (endDate != null)
				prepStmt.setDate(2, endDate);
			rs = prepStmt.executeQuery();
			if (rs.next()) 
				getNCCount = rs.getInt("NUMBER_OF_NC");
				
			final Vector vtOtherAct = QualityObjective.getOtherActivityList(projectID);
			OtherActInfo oaInfo;
			int test = 0;
			for(int i=0;i<vtOtherAct.size();i++){  
				oaInfo=(OtherActInfo)vtOtherAct.get(i);
				if(oaInfo.qcActivity == 42 && oaInfo.status == 1){
					test++;
				}
			}
			if(getNCCount ==0 && test ==0){
				getNCCount = Double.NaN;
			}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return getNCCount;
		}
	}
	public static double getCPTime(long projectID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		double getCPTime = Double.NaN;
		try {

			sql = " SELECT AVG(CLOSUREDATE - CREATIONDATE) C_P FROM NC,PROJECT P WHERE NCTYPE IN (21,22)"
			+ " AND NC.STATUS <>6  "
			+ " AND NC.PROJECTID =P.CODE"
			+ " AND P.PROJECT_ID=? AND CLOSUREDATE IS NOT NULL";
				conn = ServerHelper.instance().getConnection();
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setLong(1, projectID);
				rs = prepStmt.executeQuery();
				if (rs.next())
					getCPTime = rs.getDouble("C_P");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return getCPTime;
		}
	}
	/**
	 * needs vector of NCInfo
	 * Return value in Days
	 */
	public static double getCPTime(Vector closedNCs) {
		NCInfo inf;
		double retVal=Double.NaN;
		double sum=0;
		double size=closedNCs.size();
		for (int i=0;i<size;i++){
			inf=(NCInfo)closedNCs.elementAt(i);
			sum +=CommonTools.dateDiff(inf.creationDate,inf.closureDate);
		}
		if (size!=0)
			retVal=sum/size;
		return retVal;
	}
	public static double getRepeatedNC(Vector createdNCs) {
		NCInfo inf;
		double retVal=Double.NaN;
		double sum=0;
		double size=createdNCs.size();
		for (int i=0;i<size;i++){
			inf=(NCInfo)createdNCs.elementAt(i);
			if (inf.repeat==1)
				sum ++;
		}
		if (size!=0)
			retVal=sum*100d/size;
		return retVal;
	}
	public static double getNCTimeliness(Vector closedNCs) {
		NCInfo inf;
		double retVal=Double.NaN;
		double sum=0;
		double size=closedNCs.size();
		for (int i=0;i<size;i++){
			inf=(NCInfo)closedNCs.elementAt(i);
			if (inf.deadline !=null && inf.closureDate.getTime()<=inf.deadline.getTime())
				sum ++;
		}
		if (size!=0)
			retVal=sum*100d/size;
		return retVal;
	}
	public static double getResponseTimeResult(double dblResponseTimeResult) {
		try {			
			int numGroup = WorkUnit.getNumSupportGroup();						
			if (numGroup > 0) {								
				if (dblResponseTimeResult == 0) {
					dblResponseTimeResult = -15;
				}
				else if (dblResponseTimeResult > 24) {
					dblResponseTimeResult = 0;
				}
//				else if (dblResponseTimeResult <= 0) {
//					dblResponseTimeResult = 25;
//				}
				else if (dblResponseTimeResult <= 8) {
					dblResponseTimeResult = 25;
				}
				else {
					dblResponseTimeResult = 5 + ((24-dblResponseTimeResult)*20)/16;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return dblResponseTimeResult;
		}
	}
	public static double getSQAResponseTime(Date startDate,Date endDate) {
		return getResponseTime( startDate, endDate,"SQA");
	}
	public static double getPQAResponseTime(Date startDate,Date endDate) {
		return getResponseTime( startDate, endDate,"PQA");
	}
	public static double getResponseTime(Date startDate,Date endDate,String group) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		double retVal = Double.NaN;
		try {
			sql =
				"SELECT AVG(NVL(reviewdate,sysdate)-creationdate)*24"//oracle calcs in days
					+ " FROM CALL,NCCONSTANT"
					+ " WHERE CALL.typeofcause= NCCONSTANT.constantid"
					+ " AND NCCONSTANT.description=?"
					+ " AND NCCONSTANT.type='TypeOfCause'"
					+ " AND creationdate >=? AND creationdate <=? "
					+ " AND STATUS <> 6";//not cancelled
				conn = ServerHelper.instance().getConnection();
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setString(1, group);
				prepStmt.setDate(2, startDate);
				prepStmt.setDate(3, endDate);
				rs = prepStmt.executeQuery();
				if (rs.next())
					retVal = rs.getDouble(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return retVal;
		}
	}
	
	public static double getPQAFixTime(Date startDate,Date endDate) {
			return getFixTime(startDate,endDate, "PQA");
	}
	public static double getFixTime(Date startDate,Date endDate,String group) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		double retVal = Double.NaN;
		try {
			sql =
				"SELECT AVG(closuredate-creationdate)"
					+ " FROM CALL,NCCONSTANT"
					+ " WHERE CALL.typeofcause= NCCONSTANT.constantid"
					+ " AND NCCONSTANT.description=?"
					+ " AND NCCONSTANT.type='TypeOfCause'"
					+ " AND closuredate >=? AND TO_DATE(closuredate) <=? "
					+ " AND STATUS <> 6";//not cancelled
				conn = ServerHelper.instance().getConnection();
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setString(1, group);
				prepStmt.setDate(2, startDate);
				prepStmt.setDate(3, endDate);
				rs = prepStmt.executeQuery();
				if (rs.next())
					retVal = rs.getDouble(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return retVal;
		}
	}
	public static Vector getPQACallLogs(Date startDate,Date endDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector ret = new Vector();
		TaskInfo task;
		Table rolesTable=new Table();
		Vector roles;
		AssignmentInfo assinf;
		try {
			String constraint=(startDate==null)?"":"AND CALL.creationdate >=? ";
			sql =
				"SELECT CALL.*,WORKUNIT.PARENTWORKUNITID,developer.developer_id,WORKUNIT.type,WORKUNIT.tableid "
				+" FROM CALL,NCCONSTANT,WORKUNIT,developer"
					+ " WHERE CALL.typeofcause= NCCONSTANT.constantid"
					+ " AND NCCONSTANT.description='PQA'"
					+ " AND NCCONSTANT.type='TypeOfCause'"
					+ " AND TO_DATE(CALL.creationdate) <=? "+constraint
					+ " AND CALL.STATUS <> 6"
					+ " AND CALL.assignee=developer.account(+)"
					+" AND WORKUNIT.workunitname(+)=CALL.projectid ";
				conn = ServerHelper.instance().getConnection();
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setDate(1, endDate);
				if (startDate!=null)
					prepStmt.setDate(2, startDate);
				rs = prepStmt.executeQuery();
				int status;
				int type;
				while (rs.next()){
					task=new TaskInfo();
					task.planDate=rs.getDate("deadline");
					if (task.planDate==null)
						task.planDate=new Date(rs.getDate("creationdate").getTime()+5*24*3600000);
					task.actualDate=rs.getDate("closuredate");
					task.desc=CALL_LOG_PREFIX+rs.getString("DESCRIPTION");
					
					type=rs.getInt("type");
					if (type==2){
						task.prjID=rs.getLong("tableid");
						task.grpName=rs.getString("GROUPNAME");
						task.prjCode=rs.getString("projectid");
						task.parentwuID=rs.getLong("PARENTWORKUNITID");
					}
					else if(type==1){
						task.grpName=rs.getString("projectid");
						task.parentwuID=rs.getLong("tableid");
					}
					else if(type==0){//general
						task.grpName=rs.getString("GROUPNAME");
					}
					status=rs.getInt("STATUS");
					if (status==9)
						task.status=(int)TaskInfo.STATUS_PASS;
					else if(status==6)
						task.status=(int)TaskInfo.STATUS_CANCELLED;
					else
						task.status=(int)TaskInfo.STATUS_PENDING;
					task.type=TaskInfo.CONTROL;	
					task.typeID=TaskInfo.getTypeID(TaskInfo.CONTROL);
					task.note=rs.getString("NOTE");
					if (task.prjID>0){
						roles =(Vector)rolesTable.get(task.prjID);
						if (roles==null){
							roles=Assignments.getProjectRole(task.prjID,ResponsibilityInfo.ROLE_PQA);
							rolesTable.add(task.prjID,roles);
						}
						assinf=Assignments.getProjectRoleAtDate(roles,task.planDate);
						if(assinf!=null){
							task.assignedTo=assinf.devID;
							task.assignedToStr=assinf.account;
						}
						else{
							task.assignedToStr=rs.getString("assignee");
							task.assignedTo=rs.getLong("developer_id");
						}
					}
					else{
						task.assignedToStr=rs.getString("assignee");
						task.assignedTo=rs.getLong("developer_id");
					}

					ret.add(task);
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return ret;
		}
	}
	/**
	 * returns vector of TaskInfo
	 */
	public static Vector getNCTasks(Date startDate,Date endDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector ret = new Vector();
		TaskInfo task,task2;
		int days=5;//the review action is 5 days after creation date
		try {
			//only assigned and closed Ncs
			String constraint=(startDate==null)?"":"AND (creationdate+"+days+" >=? OR DEADLINE >=?)";
			sql =
				"select tabletest.*,developer.developer_id from (SELECT NC.*,"+Parameters.FSOFT_WU+" as WUID, 0 as PARENTWU FROM NC WHERE nclevel=18 "
				+ " union SELECT NC.*,WORKUNIT.workunitid,0 FROM NC, groups,WORKUNIT WHERE nclevel=19 and  groups.groupname=nc.groupname and WORKUNIT.TABLEID=groups.group_id AND WORKUNIT.type=1"
				+ " union SELECT NC.*,PROJECT.project_id,WORKUNIT.PARENTWORKUNITID FROM NC, PROJECT,WORKUNIT WHERE nclevel=20 and  PROJECT.code=nc.projectid AND WORKUNIT.TABLEID=PROJECT.project_id AND WORKUNIT.type=2 "
				+ " ) tabletest,developer where "
				+ " tabletest.STATUS <>6 AND NCTYPE IN (21,22,23) AND (creationdate-"+days+" <=? OR DEADLINE <=?) "+constraint+" AND tabletest.assignee=developer.account(+)";
				conn = ServerHelper.instance().getConnection();
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setDate(1, endDate);
				prepStmt.setDate(2, endDate);
				if (startDate!=null){
					prepStmt.setDate(3, startDate);
					prepStmt.setDate(4, startDate);
				}
				rs = prepStmt.executeQuery();
				int level,status;
				Table rolesTable=new Table();
				Vector roles=null;

				int NCType;
				while (rs.next()){
					task=new TaskInfo();
					level=rs.getInt("NCLEVEL");
					task.type=TaskInfo.CONTROL;	
					task.typeID=TaskInfo.getTypeID(TaskInfo.CONTROL);
					task.note=rs.getString("NOTE");
					if (level==18){//fsoft
						task.grpName=Parameters.ORGNAME;
						task.wuID=rs.getLong("WUID");
						task.assignedToStr=rs.getString("assignee");
						task.assignedTo=rs.getLong("developer_id");
					}
					else if(level==19){//GRoup
						task.grpName=rs.getString("GROUPNAME");
						task.wuID=rs.getLong("WUID");
						task.assignedToStr=rs.getString("assignee");
						task.assignedTo=rs.getLong("developer_id");
					}
					else if(level==20){//project
						task.grpName=rs.getString("GROUPNAME");
						task.prjCode=rs.getString("projectid");
						task.prjID=rs.getLong("WUID");
						task.parentwuID=rs.getLong("PARENTWU");
						
						roles =(Vector)rolesTable.get(task.prjID);
						if (roles==null){
							roles=Assignments.getProjectRole(task.prjID,ResponsibilityInfo.ROLE_PQA);
							rolesTable.add(task.prjID,roles);
						}

					}
					NCType=rs.getInt("NCType");
					//21 NC
					//22 OB
					//23 CC
					status=rs.getInt("STATUS");
					// 7:assigned
					// 9:closed
					// 8:pending
					// 5:opened
					task.planDate=new Date(rs.getDate("creationdate").getTime()+days*24*3600000);
					if (task.planDate.compareTo(endDate)<=0 && (startDate==null || task.planDate.compareTo(startDate)>=0)){
						task.actualDate=rs.getDate("reviewdate");
						task.desc=(NCType==23 ?"Review customer complaint: ":"Review C&P action: ")+rs.getString("description");
						task.status=(int)((status==5)? TaskInfo.STATUS_PENDING:TaskInfo.STATUS_PASS);
						if (level==20)
							setAssignment(roles,task);
						
						ret.add(task);
					}
					if (status==9||status==8){
						task2=task.copy();
						task2.planDate=rs.getDate("deadline");
						if (task2.planDate.compareTo(endDate)<=0 && (startDate==null || task2.planDate.compareTo(startDate)>=0)){
							task2.actualDate=rs.getDate("closuredate");
							task2.desc=(NCType==23 ?"Follow up customer complaint: ":"Follow up C&P action: ")+rs.getString("CPACTION");
							task2.status=(int)((status==8)? TaskInfo.STATUS_PENDING:TaskInfo.STATUS_PASS);
							if (level==20)
								setAssignment(roles,task2);
							ret.add(task2);
						}
					}
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return ret;
		}
	}
	private static void setAssignment(Vector roles,TaskInfo task){
		AssignmentInfo assinf=Assignments.getProjectRoleAtDate(roles,task.planDate);
		if (assinf!=null){
			task.assignedTo=assinf.devID;
			task.assignedToStr=assinf.account;
		}
	}
	public static Vector getClosedNCs(Date startDate,Date endDate) {
		return getNCs( startDate, endDate, 0);
	}
	public static Vector getCreatedNCs(Date startDate,Date endDate) {
		return getNCs( startDate, endDate, 1);
	}
	public static Vector getNCs(Date startDate,Date endDate,int type) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector ret = new Vector();
		try {
			String strType = (type == 0) ? "CLOSUREDATE" : "CREATIONDATE";
			String constraint = (startDate == null) ? "" : "AND " + strType + " >=? ";
			sql = "SELECT NC.* FROM NC WHERE STATUS <> 6 AND NCTYPE IN (21,22) AND "+strType+" <=? " + constraint;
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate(1, endDate);
			if (startDate != null)
				prepStmt.setDate(2, startDate);
			rs = prepStmt.executeQuery();
			NCInfo nc;
			while (rs.next()) {
				nc = new NCInfo();
				nc.NCid = rs.getLong("NCid");
				nc.NCLevel = rs.getInt("NCLevel");
				nc.projectID = rs.getString("projectID");
				nc.NCType = rs.getInt("NCType");
				nc.deadline = rs.getDate("Deadline");
				nc.repeat = rs.getInt("Repeat");
				nc.closureDate = rs.getDate("ClosureDate");
				nc.creationDate= rs.getDate("creationDate");
				nc.groupName = rs.getString("GroupName");
				ret.add(nc);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return ret;
		}
	}
	public static int getNCCount(Date startDate,Date endDate){
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		int ret=0 ;
		try {
			String constraint = (startDate == null) ? "" : "AND CREATIONDATE >=? ";
			sql = "SELECT Count(*) FROM NC WHERE STATUS <> 6 AND NCTYPE IN (21,22) AND creationdate <=? " + constraint;
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate(1, endDate);
			if (startDate != null)
				prepStmt.setDate(2, startDate);
			rs = prepStmt.executeQuery();

			if(rs.next()) 
				ret = rs.getInt(1);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return ret;
		}
	
	
	}

}
