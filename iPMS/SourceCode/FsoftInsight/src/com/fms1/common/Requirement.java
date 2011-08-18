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
import java.util.StringTokenizer;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.tools.Color;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.tools.Db;
import com.fms1.web.*;
/**
 * Requirements logic
 *
 */
public final class Requirement {
	
	public static final int NUMBER_OF_ROW_ADDABLE = 10;
	
	public static final Vector getRequirementList(ProjectInfo pinf, java.sql.Date date) {
		return getRequirementList(pinf, date, 0);
	}
	public static final Vector getRequirementListByDeliverable(
        ProjectInfo pinf,
		final long deliverableId) {
		return getRequirementList(pinf, new java.sql.Date(0), deliverableId);
	}
	/**
	 * 
	 * @return vector of RequirementInfo
	 */
	public static final Vector getRequirementList(ProjectInfo pinf) {
		return getRequirementList(pinf, null, 0);
	}
	/**
	 * 
	 * @return vector of RequirementInfo
	 */
	public static final Vector getRequirementList(
        ProjectInfo pinf,
		java.sql.Date date,
		final long deliverableId) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {
			String delivConstraint = (deliverableId > 0) ? " AND M.MODULE_ID =" + deliverableId : "";
			conn = ServerHelper.instance().getConnection();
			// get the table of completeness rates
			//get the list of requirements
			sql =
					"SELECT "
					+ "R.PROJECT_ID, "
					+ "R.REQUIREMENT_ID, "
					+ "R.PREV_PROJECT_ID, "
					+ "R.MODULE_ID, "
					+ "R.TYPE, "
					+ "R.REQ_SIZE, "
					+ "R.SRS, "
					+ "R.DD, "
					+ "R.TESTCASE, "
					+ "R.RELEASE_NOTE, "
					+ "R.EFFORT, "
					+ "R.ELAPSED_DAY, "
					+ "R.RECEIVED_DATE, "
					+ "R.RESPONSE_DATE, "
					+ "R.PROJECT_ID, "
					+ "R.CREATE_DATE, "
					+ "R.COMMITTED_DATE, "
					+ "R.DESIGNED_DATE, "
					+ "R.CODED_DATE, "
					+ "R.TESTED_DATE, "
					+ "R.DEPLOYED_DATE, "
					+ "R.ACCEPTED_DATE, "
					+ "R.CANCELLED_DATE, "
					+ "R.REQUIREMENT, "
					+ "R.CODE_MODULE, "
					+ "R.RESPONSE_DATE - R.RECEIVED_DATE RESPONSE_TIME, "
					+ "P.CODE PCODE,"
					+ "M.NAME MNAME "
					+ "FROM "
					+ "REQUIREMENTS R, MODULE M,PROJECT P "
					+ "WHERE "
					+ "R.PROJECT_ID = ? "
					+ "AND P.PROJECT_ID (+) = R.PREV_PROJECT_ID "
					+ "AND M.MODULE_ID(+)=R.MODULE_ID"
					+ delivConstraint
					+ " ORDER BY M.NAME DESC, R.REQUIREMENT ASC";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pinf.getProjectId());
			rs = prepStmt.executeQuery();
            RequirementInfo info ;
			while (rs.next()) {
				info = new RequirementInfo();
				info.requirementID=rs.getInt("REQUIREMENT_ID");
				info.projectID = rs.getInt("PROJECT_ID");
				info.prevPrjID = rs.getInt("PREV_PROJECT_ID");
				info.prevPrjName = rs.getString("PCODE");
				info.moduleID = rs.getInt("MODULE_ID");
				info.moduleName = rs.getString("MNAME");
				info.type = rs.getInt("TYPE");
				info.size = rs.getInt("REQ_SIZE");
				info.requirementSection = rs.getString("SRS");
				info.detailDesign = rs.getString("DD");
				info.testCase = rs.getString("TESTCASE");
				info.releaseNote = rs.getString("RELEASE_NOTE");
				info.effort = rs.getDouble("EFFORT");
				info.elapsedDay = rs.getFloat("ELAPSED_DAY");
				info.receivedDate = rs.getDate("RECEIVED_DATE");
				info.responseDate = rs.getDate("RESPONSE_DATE");
				info.projectID = rs.getInt("PROJECT_ID");
				info.createDate = rs.getDate("CREATE_DATE");
				info.committedDate = rs.getDate("COMMITTED_DATE");
				info.designedDate = rs.getDate("DESIGNED_DATE");
				info.codedDate = rs.getDate("CODED_DATE");
				info.testedDate = rs.getDate("TESTED_DATE");
				info.deployedDate = rs.getDate("DEPLOYED_DATE");
				info.acceptedDate = rs.getDate("ACCEPTED_DATE");
				info.cancelledDate = rs.getDate("CANCELLED_DATE");
				info.name = rs.getString("REQUIREMENT");
				info.codeModuleName = rs.getString("CODE_MODULE");
				info.responseTime = rs.getInt("RESPONSE_TIME");
				list.addElement(info);
			}            
            getActualRCRByDateHistory(pinf,list,date);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}
	public static final ReqChangesInfo getChangesRequirement(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ReqChangesInfo reqInfo = null;
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			sql = "SELECT A.REQ_CHANGES_ID," 
				       + "A.REQ_CHANGES_DESC," 
				       + "A.REQ_LOG_LOCATION,"
       				   + "A.REQ_CREATOR," 
       				   + "A.REQ_REVIEWER," 
       				   + "A.REQ_APROVER"
  				 +" FROM REQ_CHANGES_MNG A" 
  				 +" WHERE A.REQ_PROJECT_ID = ?";
					
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			
			if (rs.next()) {
				ReqChangesInfo info = new ReqChangesInfo();				
				info.reqChangesID 	= rs.getLong("REQ_CHANGES_ID");
				info.reqChangesDesc = rs.getString("REQ_CHANGES_DESC");
				info.reqLogLocation = rs.getString("REQ_LOG_LOCATION");
				info.reqCreator 	= rs.getString("REQ_CREATOR");
				info.reqReviewer 	= rs.getString("REQ_REVIEWER");
				info.reqApprover 	= rs.getString("REQ_APROVER");
				reqInfo = info;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return reqInfo;
		}
	}
	
	public static final int doAddReqChanges(final ReqChangesInfo reqInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		
		try {
			conn = ServerHelper.instance().getConnection();
			sql =  " INSERT INTO REQ_CHANGES_MNG("
				    + "REQ_CHANGES_ID, "
					+ "REQ_PROJECT_ID,"
				   	+ "REQ_CHANGES_DESC, "				    
				    + "REQ_LOG_LOCATION, "
				    + "REQ_CREATOR, "
				    + "REQ_REVIEWER, "
				    + "REQ_APROVER)"
				 + " VALUES(NVL((SELECT MAX(REQ_CHANGES_ID)+1 FROM REQ_CHANGES_MNG),1),"+ prjID+",?,?,?,?,?)";
			stm = conn.prepareStatement(sql);				
			stm.setString(1, reqInfo.reqChangesDesc);				
			stm.setString(2, reqInfo.reqLogLocation);
			stm.setString(3, reqInfo.reqCreator);
			stm.setString(4, reqInfo.reqReviewer);
			stm.setString(5, reqInfo.reqApprover);
			stm.executeUpdate();
			conn.commit();
			
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
	 
		return iRet;
	}
	
	public static final int doUpdateReqChanges(final ReqChangesInfo reqInfo, final long prjID) {
		// landd start
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();			
			sql =  " UPDATE REQ_CHANGES_MNG R "
					+ "SET R.REQ_CHANGES_DESC = ?"				    
					+ "  , R.REQ_LOG_LOCATION = ?"
					+ "  , R.REQ_CREATOR = ?"
					+ "  , R.REQ_REVIEWER = ?"
					+ "  , R.REQ_APROVER = ?"
				 + " WHERE R.REQ_CHANGES_ID = ?";
			stm = conn.prepareStatement(sql);				
			stm.setString(1, reqInfo.reqChangesDesc);				
			stm.setString(2, reqInfo.reqLogLocation);
			stm.setString(3, reqInfo.reqCreator);
			stm.setString(4, reqInfo.reqReviewer);
			stm.setString(5, reqInfo.reqApprover);
			stm.setLong(6, reqInfo.reqChangesID);
			
			stm.executeUpdate();
			conn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			iRet = 1;
		} finally {
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final boolean doDeletePLReqChanges(final long reqID) {
			return Db.delete(reqID, "REQ_CHANGES_ID", "REQ_CHANGES_MNG");
	}
	
	/** add new requirement
	 * @author Hoang My Duc
	 */
	public static final boolean addRequirement(final RequirementInfo reqInfo) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean result = false;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO  REQUIREMENTS "
					+ "(REQUIREMENT_ID,PREV_PROJECT_ID , "
					+ "MODULE_ID , "
					+ "TYPE , "
					+ "REQ_SIZE, "
					+ "SRS , "
					+ "DD , "
					+ "TESTCASE , "
					+ "RELEASE_NOTE , "
					+ "EFFORT, "
					+ "ELAPSED_DAY , "
					+ "RECEIVED_DATE , "
					+ "RESPONSE_DATE , "
					+ "PROJECT_ID , "
					+ "CREATE_DATE, "
					+ "COMMITTED_DATE , "
					+ "DESIGNED_DATE , "
					+ "CODED_DATE , "
					+ "TESTED_DATE , "
					+ "DEPLOYED_DATE, "
					+ "ACCEPTED_DATE , "
					+ "CANCELLED_DATE , "
					+ "REQUIREMENT , "
					+ "CODE_MODULE) "
					+ "VALUES "
					+ "((SELECT NVL(MAX(REQUIREMENT_ID)+1,1) FROM REQUIREMENTS),?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?) ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, reqInfo.prevPrjID);
			prepStmt.setInt(2, reqInfo.moduleID);
			prepStmt.setInt(3, reqInfo.type);
			prepStmt.setInt(4, reqInfo.size);
			prepStmt.setString(5, reqInfo.requirementSection);
			prepStmt.setString(6, reqInfo.detailDesign);
			prepStmt.setString(7, reqInfo.testCase);
			prepStmt.setString(8, reqInfo.releaseNote);
			prepStmt.setDouble(9, reqInfo.effort);
			prepStmt.setFloat(10, reqInfo.elapsedDay);
			prepStmt.setDate(11, reqInfo.receivedDate);
			prepStmt.setDate(12, reqInfo.responseDate);
			prepStmt.setInt(13, reqInfo.projectID);
			prepStmt.setDate(14, reqInfo.createDate);
			prepStmt.setDate(15, reqInfo.committedDate);
			prepStmt.setDate(16, reqInfo.designedDate);
			prepStmt.setDate(17, reqInfo.codedDate);
			prepStmt.setDate(18, reqInfo.testedDate);
			prepStmt.setDate(19, reqInfo.deployedDate);
			prepStmt.setDate(20, reqInfo.acceptedDate);
			prepStmt.setDate(21, reqInfo.cancelledDate);
			prepStmt.setString(22, reqInfo.name);
			prepStmt.setString(23, reqInfo.codeModuleName);
			prepStmt.executeUpdate();
			result = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
			return result;
		}
	}
	
	/** add new customer
	 * @author LamNT3
	 */
	public static final boolean addCustomer(final CustomerInfo customerInfo) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean result = false;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO  CUSTOMER "
					+ "(CUSTOMER_ID, CUS_NAME , "
					+ "CUS_DESCRIPTION , "
					+ "CUS_NOTE , "
					+ "OG) "
					+ "VALUES "
					+ "((SELECT NVL(MAX(CUSTOMER_ID)+1,1) FROM CUSTOMER),?, ?, ?, ?) ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, customerInfo.standardName);
			prepStmt.setString(2, customerInfo.fullName);
			prepStmt.setString(3, customerInfo.note);
			prepStmt.setString(4, customerInfo.ofOGs);
			prepStmt.executeUpdate();
			result = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		catch (Exception ex){
			ex.printStackTrace();
			result = false;
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
			return result;
		}
	}
	public static final Vector getCustomerList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		Vector cusVector = new Vector();
		Vector returnVector = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			sql = "SELECT * FROM CUSTOMER order by og,cus_name";
				
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
		
			while (rs.next()) {
				CustomerInfo info = new CustomerInfo();				
				info.cusID 	= rs.getLong("customer_id");
				info.fullName = rs.getString("cus_description");
				info.standardName = rs.getString("cus_name");
				info.note 	= rs.getString("cus_note");
				info.ofOGs 	= rs.getString("og");
				cusVector.add(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			for (int i=0;i<cusVector.size();i++){
				CustomerInfo temp = (CustomerInfo)cusVector.get(i);
				if (temp.ofOGs==null||temp.ofOGs.equals(""))
					returnVector.add(temp);
			}
			for (int i=0;i<cusVector.size();i++){
				CustomerInfo temp = (CustomerInfo)cusVector.get(i);
				if (temp.ofOGs!=null&&(!temp.ofOGs.equals("")))
					returnVector.add(temp);
			}
			return returnVector;
		}
	}
	public static final Vector getCustomerListBySearch(String sName) {
			Connection conn = null;
			PreparedStatement prepStmt = null;
			String sql = null;
			Vector cusVector = new Vector();
			Vector returnVector = new Vector();
			ResultSet rs = null;
			try {			
				conn = ServerHelper.instance().getConnection();			
				sql = "SELECT * FROM CUSTOMER WHERE upper(cus_name) like '%"+sName.toUpperCase()+"%' order by og, cus_name";
				
				prepStmt = conn.prepareStatement(sql);
				rs = prepStmt.executeQuery();
		
				while (rs.next()) {
					CustomerInfo info = new CustomerInfo();				
					info.cusID 	= rs.getLong("customer_id");
					info.fullName = rs.getString("cus_description");
					info.standardName = rs.getString("cus_name");
					info.note 	= rs.getString("cus_note");
					info.ofOGs 	= rs.getString("og");
					cusVector.add(info);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, prepStmt, rs);
				for (int i=0;i<cusVector.size();i++){
					CustomerInfo temp = (CustomerInfo)cusVector.get(i);
					if (temp.ofOGs==null||temp.ofOGs.equals(""))
						returnVector.add(temp);
				}
				for (int i=0;i<cusVector.size();i++){
					CustomerInfo temp = (CustomerInfo)cusVector.get(i);
					if (temp.ofOGs!=null&&(!temp.ofOGs.equals("")))
						returnVector.add(temp);
				}
				return returnVector;
			}
		}
	public static final void updateCustomer(CustomerInfo customerInfo) {
			Connection conn = null;
			PreparedStatement prepStmt = null;
			String sql = null;
			Vector cusVector = new Vector();
			ResultSet rs = null;
			try {			
				conn = ServerHelper.instance().getConnection();			
				sql = "update customer set cus_name = '"+customerInfo.standardName+"' , cus_description = '"+customerInfo.fullName+"' , cus_note = '"+customerInfo.note+"' , og = '"+customerInfo.ofOGs+"' where customer_id = " + customerInfo.cusID;
				
				prepStmt = conn.prepareStatement(sql);
				rs = prepStmt.executeQuery();
		
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, prepStmt, rs);
			}
		}
	public static final CustomerInfo getCustomer(long cusId) {
			Connection conn = null;
			PreparedStatement prepStmt = null;
			String sql = null;
			CustomerInfo cus = new CustomerInfo();
			ResultSet rs = null;
			try {			
				conn = ServerHelper.instance().getConnection();			
				sql = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = " + cusId ;
				
				prepStmt = conn.prepareStatement(sql);
				rs = prepStmt.executeQuery();
		
				while (rs.next()) {				
					cus.cusID 	= rs.getLong("customer_id");
					cus.fullName = rs.getString("cus_description");
					cus.standardName = rs.getString("cus_name");
					cus.note 	= rs.getString("cus_note");
					cus.ofOGs 	= rs.getString("og");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, prepStmt, rs);
				return cus;
			}
		}
	public static final boolean checkCustomerName(String cusName) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		Vector cusVector = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			sql = "SELECT * FROM CUSTOMER WHERE UPPER(trim(CUS_NAME))=?";
			
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, cusName.trim().toUpperCase());
			rs = prepStmt.executeQuery();
	
			if (rs.next()) {
				return false;
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
		
	/** update requirement
	 * @author Hoang My Duc
	 */
	public static final boolean setRequirement(final RequirementInfo reqInfo) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean result = false;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE  REQUIREMENTS R "
					+ "SET "
					+ "  R.PREV_PROJECT_ID = ? "
					+ ", R.MODULE_ID = ? "
					+ ", R.TYPE = ? "
					+ ", R.REQ_SIZE = ? "
					+ ", R.SRS = ? "
					+ ", R.DD = ? "
					+ ", R.TESTCASE = ? "
					+ ", R.RELEASE_NOTE = ? "
					+ ", R.EFFORT = ? "
					+ ", R.ELAPSED_DAY = ? "
					+ ", R.RECEIVED_DATE = ? "
					+ ", R.RESPONSE_DATE = ? "
					+ ", R.PROJECT_ID = ? "
					+ ", R.CREATE_DATE = ? "
					+ ", R.COMMITTED_DATE = ? "
					+ ", R.DESIGNED_DATE = ? "
					+ ", R.CODED_DATE = ? "
					+ ", R.TESTED_DATE = ? "
					+ ", R.DEPLOYED_DATE = ? "
					+ ", R.ACCEPTED_DATE = ? "
					+ ", R.CANCELLED_DATE = ? "
					+ ", R.REQUIREMENT = ? "
					+ ", R.CODE_MODULE = ? "
					+ "WHERE "
					+ "  R.REQUIREMENT_ID = ? ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, reqInfo.prevPrjID);
			prepStmt.setInt(2, reqInfo.moduleID);
			prepStmt.setInt(3, reqInfo.type);
			prepStmt.setInt(4, reqInfo.size);
			prepStmt.setString(5, reqInfo.requirementSection);
			prepStmt.setString(6, reqInfo.detailDesign);
			prepStmt.setString(7, reqInfo.testCase);
			prepStmt.setString(8, reqInfo.releaseNote);
			prepStmt.setDouble(9, reqInfo.effort);
			prepStmt.setFloat(10, reqInfo.elapsedDay);
			prepStmt.setDate(11, reqInfo.receivedDate);
			prepStmt.setDate(12, reqInfo.responseDate);
			prepStmt.setInt(13, reqInfo.projectID);
			prepStmt.setDate(14, reqInfo.createDate);
			prepStmt.setDate(15, reqInfo.committedDate);
			prepStmt.setDate(16, reqInfo.designedDate);
			prepStmt.setDate(17, reqInfo.codedDate);
			prepStmt.setDate(18, reqInfo.testedDate);
			prepStmt.setDate(19, reqInfo.deployedDate);
			prepStmt.setDate(20, reqInfo.acceptedDate);
			prepStmt.setDate(21, reqInfo.cancelledDate);
			prepStmt.setString(22, reqInfo.name);
			prepStmt.setString(23, reqInfo.codeModuleName);
			prepStmt.setInt(24, reqInfo.requirementID);
			prepStmt.executeUpdate();
			result = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
		}
		return result;
	}
	/** delete requirement
	 * @author Hoang My Duc
	 */
	public static final boolean delRequirement(final int reqID) {

           return Db.delete(reqID,"REQUIREMENT_ID","REQUIREMENTS");

	}
	// Please note that, in this function, we do not calculate the completenessRate, so it is null 
	// in RequirementInfo obj
	public static final RequirementInfo getRequirementById(final int requirementID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final RequirementInfo info = new RequirementInfo();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT "
					+ "R.PROJECT_ID, "
					+ "R.REQUIREMENT_ID, "
					+ "R.PREV_PROJECT_ID, "
					+ "R.MODULE_ID, "
					+ "R.TYPE, "
					+ "R.REQ_SIZE, "
					+ "R.SRS, "
					+ "R.DD, "
					+ "R.TESTCASE, "
					+ "R.RELEASE_NOTE, "
					+ "R.CODE_MODULE, "
					+ "R.EFFORT, "
					+ "R.ELAPSED_DAY, "
					+ "R.RECEIVED_DATE, "
					+ "R.RESPONSE_DATE, "
					+ "R.PROJECT_ID, "
					+ "R.CREATE_DATE, "
					+ "R.COMMITTED_DATE, "
					+ "R.DESIGNED_DATE, "
					+ "R.CODED_DATE, "
					+ "R.TESTED_DATE, "
					+ "R.DEPLOYED_DATE, "
					+ "R.ACCEPTED_DATE, "
					+ "R.CANCELLED_DATE, "
					+ "R.REQUIREMENT, "
					+ "R.RESPONSE_DATE - R.RECEIVED_DATE RESPONSE_TIME "
					+ "FROM "
					+ "REQUIREMENTS R "
					+ "WHERE "
					+ "R.REQUIREMENT_ID = ? ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, requirementID);
			final ResultSet rs = prepStmt.executeQuery();
			if (rs != null)
				if (rs.next()) {
					info.projectID = rs.getInt("PROJECT_ID");
					info.prevPrjID = rs.getInt("PREV_PROJECT_ID");
					prepStmt = conn.prepareStatement("SELECT CODE, NAME FROM PROJECT WHERE PROJECT_ID = ?");
					prepStmt.setInt(1, info.prevPrjID);
					ResultSet rs1 = prepStmt.executeQuery();
					info.prevPrjName = null;
					if (rs1 != null)
						if (rs1.next())
							info.prevPrjName = rs1.getString("CODE");
					info.moduleID = rs.getInt("MODULE_ID");
					prepStmt = conn.prepareStatement("SELECT NAME FROM MODULE WHERE MODULE_ID = ?");
					prepStmt.setInt(1, info.moduleID);
					rs1 = prepStmt.executeQuery();
					info.moduleName = null;
					if (rs1 != null)
						if (rs1.next())
							info.moduleName = rs1.getString("NAME");
					info.type = rs.getInt("TYPE");
					info.size = rs.getInt("REQ_SIZE");
					info.requirementSection = rs.getString("SRS");
					info.detailDesign = rs.getString("DD");
					info.testCase = rs.getString("TESTCASE");
					info.releaseNote = rs.getString("RELEASE_NOTE");
					info.codeModuleName = rs.getString("CODE_MODULE");
					info.effort = rs.getDouble("EFFORT");
					info.elapsedDay = rs.getFloat("ELAPSED_DAY");
					info.receivedDate = rs.getDate("RECEIVED_DATE");
					info.responseDate = rs.getDate("RESPONSE_DATE");
					info.projectID = rs.getInt("PROJECT_ID");
					info.createDate = rs.getDate("CREATE_DATE");
					info.committedDate = rs.getDate("COMMITTED_DATE");
					info.designedDate = rs.getDate("DESIGNED_DATE");
					info.codedDate = rs.getDate("CODED_DATE");
					info.testedDate = rs.getDate("TESTED_DATE");
					info.deployedDate = rs.getDate("DEPLOYED_DATE");
					info.acceptedDate = rs.getDate("ACCEPTED_DATE");
					info.cancelledDate = rs.getDate("CANCELLED_DATE");
					info.name = rs.getString("REQUIREMENT");
					info.responseTime = rs.getInt("RESPONSE_TIME");
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
		}
		return info;
	}
	public static final boolean setRequirementStatus(final RequirementInfo reqInfo) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean result = false;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE  REQUIREMENTS R "
					+ "SET "
					+ "  R.REQUIREMENT = ? "
					+ ", R.COMMITTED_DATE = ? "
					+ ", R.DESIGNED_DATE = ? "
					+ ", R.CODED_DATE = ? "
					+ ", R.TESTED_DATE = ? "
					+ ", R.DEPLOYED_DATE = ? "
					+ ", R.ACCEPTED_DATE = ? "
					+ ", R.CANCELLED_DATE = ? "
					+ "WHERE "
					+ "  R.REQUIREMENT_ID = ? ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, reqInfo.name);
			prepStmt.setDate(2, reqInfo.committedDate);
			prepStmt.setDate(3, reqInfo.designedDate);
			prepStmt.setDate(4, reqInfo.codedDate);
			prepStmt.setDate(5, reqInfo.testedDate);
			prepStmt.setDate(6, reqInfo.deployedDate);
			prepStmt.setDate(7, reqInfo.acceptedDate);
			prepStmt.setDate(8, reqInfo.cancelledDate);
			prepStmt.setInt(9, reqInfo.requirementID);
			prepStmt.executeUpdate();
			result = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
		}
		return result;
	}
	public static final boolean requirementBatchUpdate(final RequirementBatchUpdate reBatchUpdate){
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try{
			sql =
				"UPDATE  REQUIREMENTS R "
					+ "SET "
					+ "  R.MODULE_ID = ? "
					+ ", R.TYPE = ? "
					+ ", R.REQ_SIZE = ? "
					+ ", R.SRS = ? "
					+ ", R.DD = ? "
					+ ", R.TESTCASE = ? "
					+ ", R.RELEASE_NOTE = ? "
					+ ", R.CODE_MODULE = ? "
					+ "WHERE "
					+ "  R.REQUIREMENT_ID = ? ";
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			prepStmt = conn.prepareStatement(sql);

			for (int i = 0; i < reBatchUpdate.getRequirementId().length; i++){
				if (reBatchUpdate.getRequirementId()[i].length() > 0){
					prepStmt.setInt(1, Integer.parseInt(reBatchUpdate.getDeliverable()[i]));
					prepStmt.setInt(2, Integer.parseInt(reBatchUpdate.getRequirementType()[i]));
					prepStmt.setInt(3, Integer.parseInt(reBatchUpdate.getRequirementSize()[i]));
					prepStmt.setString(4, reBatchUpdate.getRequirementSection()[i]);
					prepStmt.setString(5, reBatchUpdate.getDesignSection()[i]);
					prepStmt.setString(6, reBatchUpdate.getTestCaseSection()[i]);
					prepStmt.setString(7, reBatchUpdate.getReleaseNote()[i]);
					prepStmt.setString(8, reBatchUpdate.getCodeModule()[i]);
					prepStmt.setInt(9, Integer.parseInt(reBatchUpdate.getRequirementId()[i]));
					prepStmt.executeUpdate();
				}
			}
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return false;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		finally{
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	/*
	public static final boolean requirementBatchUpdate(final RequirementInfo reqInfo) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean result = false;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE  REQUIREMENTS R "
					+ "SET "
					+ "  R.MODULE_ID = ? "
					+ ", R.TYPE = ? "
					+ ", R.REQ_SIZE = ? "
					+ ", R.SRS = ? "
					+ ", R.DD = ? "
					+ ", R.TESTCASE = ? "
					+ ", R.RELEASE_NOTE = ? "
					+ ", R.CODE_MODULE = ? "
					+ "WHERE "
					+ "  R.REQUIREMENT_ID = ? ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, reqInfo.moduleID);
			prepStmt.setInt(2, reqInfo.type);
			prepStmt.setInt(3, reqInfo.size);
			prepStmt.setString(4, reqInfo.requirementSection);
			prepStmt.setString(5, reqInfo.detailDesign);
			prepStmt.setString(6, reqInfo.testCase);
			prepStmt.setString(7, reqInfo.releaseNote);
			prepStmt.setString(8, reqInfo.codeModuleName);
			prepStmt.setInt(9, reqInfo.requirementID);
			prepStmt.executeUpdate();
			result = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
			return result;
		}
		
	}
	*/
	/**
	 * get from DB
	 */
	public static final Vector getDBPlanRCR(long projectid)  {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		Vector result = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT rc.* FROM PLANS_PROCESS_STAGE rc,MILESTONE"
				+" WHERE MILESTONE.milestone_id=rc.stageid"
				+" AND MILESTONE.project_id=?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectid);
			rs=prepStmt.executeQuery();
			PlanRCRInfo inf;

			while (rs.next()){
				inf=new PlanRCRInfo();
				inf.processid=rs.getInt("PROCESSID");
				inf.plannedValue = Db.getDouble(rs,"PLAN_RCR");
                inf.plannedEffort= Db.getDouble(rs,"PLAN_EFFORT");
                //inf.rePlannedEffort= Db.getDouble(rs,"REPLAN_EFFORT");
				inf.stage=rs.getLong("STAGEID");
				inf.milestone=rs.getLong("MILESTONEID");
				
				result.add(inf);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return result;
		}
		
	}
	
	// Add by HaiMM
	public static final Vector getEstEffortList(long projectid)  {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		Vector result = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM est_effort"
				+" WHERE project_id=?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectid);
			rs=prepStmt.executeQuery();
			EstEffortInfo inf;

			while (rs.next()){
				inf = new EstEffortInfo();
				inf.processName=rs.getString("process_name");
				inf.plannedValue = Db.getDouble(rs,"plan_value");
				inf.milestoneId=rs.getLong("milestone_id");
				
				result.add(inf);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return result;
		}
		
	}
	
    public static final boolean checkPlanRCR(long milestoneID){
        return checkPlanProcessStage(milestoneID,false);
    }
	public static final boolean checkPlanProcessStage(long milestoneID,boolean checkEffortInstead)  {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT count(*) valcount FROM PLANS_PROCESS_STAGE rc"
				+" WHERE rc.MILESTONEID=? and "
                +(checkEffortInstead?" PLAN_EFFORT":"PLAN_RCR" ) +" is not null";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, milestoneID);
			rs=prepStmt.executeQuery();
			return (rs.next() && rs.getInt("valcount")>0);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
		}
		
	}
    public static final void updatePlanRCR(Vector planRCRInfos) {
        updateStageProcess( planRCRInfos,0);
    }

        /**
         * @param updateType 0-> Update RCR only, 1->Effort only
         */
	public static final void updateStageProcess(Vector planRCRInfos,int updateType) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmtInsert = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE PLANS_PROCESS_STAGE SET" 
			+ (updateType==0?" PLAN_RCR =?": " PLAN_EFFORT =?")
			+ " WHERE STAGEID = ? AND MILESTONEID=? AND PROCESSID=?";
			prepStmt = conn.prepareStatement(sql);
			sql = "Insert INTO PLANS_PROCESS_STAGE ("
                + (updateType==0?" PLAN_RCR ": " PLAN_EFFORT")
                +" ,STAGEID ,MILESTONEID,PROCESSID)" 
						+ " VALUES(?,?,?,?)";
			prepStmtInsert= conn.prepareStatement(sql);
			PlanRCRInfo inf;
			for (int k = 0; k < planRCRInfos.size(); k++) {
				inf = (PlanRCRInfo) planRCRInfos.elementAt(k);
				
				Db.setDouble(prepStmt,1,updateType==0?inf.plannedValue:inf.plannedEffort);
				prepStmt.setLong(2,inf.stage);
				prepStmt.setLong(3,inf.milestone);
				prepStmt.setLong(4,inf.processid);
				
				if (prepStmt.executeUpdate()<1){
					Db.setDouble(prepStmtInsert,1,updateType==0?inf.plannedValue:inf.plannedEffort);
					prepStmtInsert.setLong(2,inf.stage);
					prepStmtInsert.setLong(3,inf.milestone);
					prepStmtInsert.setLong(4,inf.processid);
					prepStmtInsert.executeUpdate();
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	
	// Add by HaiMM
	public static final void updateEstEffort(Vector planEstEffList, long pid) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmtInsert = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE est_effort SET " 
				+ "plan_value = ?"
				+ " WHERE project_id = ? AND milestone_id=? AND process_name=?";
			prepStmt = conn.prepareStatement(sql);
			
			sql = "Insert INTO est_effort ("
					+ " plan_value "
					+" ,project_id ,milestone_id,process_name)" 
									+ " VALUES(?,?,?,?)";
			prepStmtInsert= conn.prepareStatement(sql);
			
			EstEffortInfo inf;
			for (int k = 0; k < planEstEffList.size(); k++) {
				inf = (EstEffortInfo) planEstEffList.elementAt(k);
				
				Db.setDouble(prepStmt,1,inf.plannedValue);
				prepStmt.setLong(2,pid);
				prepStmt.setLong(3,inf.milestoneId);
				prepStmt.setString(4,inf.processName);
				
				if (prepStmt.executeUpdate()<1){
					Db.setDouble(prepStmtInsert,1,inf.plannedValue);
					prepStmtInsert.setLong(2,pid);
					prepStmtInsert.setLong(3,inf.milestoneId);
					prepStmtInsert.setString(4,inf.processName);
					prepStmtInsert.executeUpdate();
				}
				conn.commit();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	
	// Add by HaiMM
	 public static final void addEstEffort(Vector planEstEffList, long pid) {
		 String sql = null;
		 Connection conn = null;
		 PreparedStatement prepStmtInsert = null;
		 try {
			 conn = ServerHelper.instance().getConnection();
			 conn.setAutoCommit(false);
			 sql = "Insert INTO est_effort ("
					 + " plan_value "
					 +" ,project_id ,milestone_id,process_name)" 
									 + " VALUES(?,?,?,?)";
			 prepStmtInsert= conn.prepareStatement(sql);
			
			 EstEffortInfo inf;
			 for (int k = 0; k < planEstEffList.size(); k++) {
				 inf = (EstEffortInfo) planEstEffList.elementAt(k);
				 Db.setDouble(prepStmtInsert,1,inf.plannedValue);
				 prepStmtInsert.setLong(2,pid);
				 prepStmtInsert.setLong(3,inf.milestoneId);
				 prepStmtInsert.setString(4,inf.processName);
				 prepStmtInsert.executeUpdate();
				
			 }

			conn.commit();
			conn.setAutoCommit(true);

		 }
		 catch (Exception e) {
			 e.printStackTrace();
		 }
		 finally {
			 ServerHelper.closeConnection(conn, prepStmtInsert, null);
		 }
	 }
	/*
	public static final void baselineRCRandEffort(ProjectInfo pinf,Vector stageList, int vtID) {
		StageInfo currentStage=(StageInfo )stageList.elementAt(vtID);
//		if values have been planned during this stage, we don't need to baseline
		if (checkPlanRCR(currentStage.milestoneID))
			return;
		PlanRCRInfo rcrInf;
		if (vtID==0){
			Vector planRCRList=getPlanRCR(pinf, stageList);
			for (int k=0;k<planRCRList.size();k++){
				rcrInf=(PlanRCRInfo)planRCRList.elementAt(k);
				rcrInf.plannedValue=rcrInf.norm;
			}
			updatePlanRCR(planRCRList);
		}
		else {
			Vector planRCRList=getRePlanRCR(pinf, stageList);
			StageInfo stinf;
			Vector updated=new Vector();
			for (int i=vtID;i<stageList.size();i++){
				stinf=(StageInfo )stageList.elementAt(i);
				for (int k=0;k<planRCRList.size();k++){
					rcrInf=(PlanRCRInfo)planRCRList.elementAt(k);
					if(rcrInf.stage==stinf.milestoneID){
						rcrInf.plannedValue=rcrInf.forecasted;
						updated.add(rcrInf);
					}
					
				}
			}
			
			updatePlanRCR(updated);
		}
        if (Effort.checkPlanEffortProcess(currentStage.milestoneID))
            return;
        Vector updateEff=new Vector();
        ProcessEffortByStageInfo inf;
        PlanRCRInfo upd;
        Vector processEffortByStages = Effort.getProcessEffortByStage(pinf,stageList);
        for (int i=0; i < processEffortByStages.size(); i++) {
                inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(i);
                if (inf.isOpen){
                    for (int j=0;j<ProcessInfo.trackedProcessId.length;j++){
                        upd=new PlanRCRInfo();
                        upd.milestone=currentStage.milestoneID;
                        upd.stage=inf.stageid;
                        upd.processid=ProcessInfo.trackedProcessId[j];
                        upd.plannedEffort=inf.forecast[j];
                        updateEff.add(upd);
                    }
                }
        }

        Effort.updatePlanEffortStageProcess(updateEff);
		
	}
	*/
	/**

	 * @return vector of PlanRCRInfo
	 */
	public static final Vector getPlanRCR(ProjectInfo projectInfo,Vector stageList) {
		Vector norms = Norms.getNormList(projectInfo,RequirementInfo.getRCRMetricConstants());
		Vector plan = Requirement.getDBPlanRCR(projectInfo.getProjectId()); 
		StageInfo stageInf=null;
		StageInfo stageInfCurrent=null;
		Vector planRCRList=new Vector();
		PlanRCRInfo rcrInf,rcrInfPlan;
		NormInfo normInfo;

		//the plan vals are set in the 1st stage only
		stageInfCurrent=(StageInfo)stageList.elementAt(0);

		int [] stdStageCount=Schedule.getStdStageCount(stageList);
		
		//merge the plan and norm data
		int metricid;
		for (int i=0;i<stageList.size();i++){
			stageInf=(StageInfo)stageList.elementAt(i);
			for (int j=0;j<ProcessInfo.RCRProcesses.length;j++){
				rcrInf= new PlanRCRInfo();
				rcrInf.stage=stageInf.milestoneID;
				rcrInf.processid=ProcessInfo.RCRProcesses[j];
				//planning is done during first stage
				rcrInf.milestone=stageInfCurrent.milestoneID;
				metricid=MetricDescInfo.getRCRMetricConstant((int)rcrInf.processid,stageInf.StandardStage);
				//merge with norms
				normInfo=NormInfo.getNormByMetricID(metricid,norms);
				if (normInfo!=null && stageInf.StandardStage>=1)
					rcrInf.norm=normInfo.average/(double)stdStageCount[stageInf.StandardStage-1];

				//	merge with plan
				rcrInfPlan=PlanRCRInfo.getPlan(plan,rcrInf.processid,rcrInf.stage,stageInfCurrent.milestoneID);
				if (rcrInfPlan!=null)
					rcrInf.plannedValue=rcrInfPlan.plannedValue;

				//System.out.println("rcrInfPlan.plannedValue " + rcrInfPlan.plannedValue);
				planRCRList.add(rcrInf);
			}
		}
		return planRCRList;
	}
    /**
     * 
     * @return vector of PlanRCRInfo
     */
	public static final Vector getRePlanRCR(ProjectInfo pinf,Vector stageList) {
		return getRePlanRCR(pinf,stageList, -1);
	}
    /**
     * 
     * @return vector of PlanRCRInfo
     */
	public static final Vector getRePlanRCR(ProjectInfo pinf, Vector stageList, int vectorID) {
		Vector reqs = Requirement.getRequirementList(pinf);
		Vector plan = Requirement.getDBPlanRCR(pinf.getProjectId());
		StageInfo stageInf = null;
		StageInfo stageInfCurrent = null;
		StageInfo stageInfPrevious = null;
		int currentStageID = -1;
		Vector planRCRList = new Vector();
		PlanRCRInfo rcrInf;
		PlanRCRInfo rcrInfPlan;
		PlanRCRInfo rcrInfPlanOld;
		//NormInfo normInfo;
		//boolean replanFound = false;
		if (vectorID < 0)			//get actual stage (vector is sorted by pend date same order as aend date)
			for (int i = 0; i < stageList.size(); i++) {
				stageInfCurrent = (StageInfo) stageList.elementAt(i);
                if (i>0)
                    stageInfPrevious = (StageInfo) stageList.elementAt(i - 1);
                currentStageID = i;
				if (stageInfCurrent.aEndD == null) 
					break;

			}
		else {
			stageInfCurrent = (StageInfo) stageList.elementAt(vectorID);
			stageInfPrevious = (StageInfo) stageList.elementAt(vectorID - 1);
			currentStageID = vectorID;
		}
		for (int i = 0; i < stageList.size(); i++) {
			stageInf = (StageInfo) stageList.elementAt(i);
			for (int j = 0; j < ProcessInfo.RCRProcesses.length; j++) {
				rcrInf = new PlanRCRInfo();
				rcrInf.stage = stageInf.milestoneID;
				rcrInf.processid = ProcessInfo.RCRProcesses[j];
				rcrInf.milestone = stageInfCurrent.milestoneID;
				//	merge with current plan, or old plan for closed stages
				rcrInfPlan = PlanRCRInfo.getPlan(plan, rcrInf.processid, rcrInf.stage, (i>=currentStageID)?stageInfCurrent.milestoneID:rcrInf.stage);
				if (rcrInfPlan != null)
					rcrInf.plannedValue = rcrInfPlan.plannedValue;
				//actual
				if (i<currentStageID) {
					rcrInf.actual = getActualRCRByProcess(reqs, rcrInf.processid, stageInf.actualBeginDate, stageInf.aEndD);
				}
				planRCRList.add(rcrInf);
			}
		}
		//Forecasted
		double sumActual = 0;
		double remainActual = 0;
		double remainPrevPlan = 0;
		for (int j = 0; j < ProcessInfo.RCRProcesses.length; j++) {
			sumActual = 0;
			remainPrevPlan = 0;
			for (int k = 0; k < planRCRList.size(); k++) {
				rcrInf = (PlanRCRInfo) planRCRList.elementAt(k);
				if (ProcessInfo.RCRProcesses[j] == rcrInf.processid)
					sumActual += rcrInf.actual;
			}
			for (int i = currentStageID; i < stageList.size(); i++) {
				stageInf = (StageInfo) stageList.elementAt(i);
				rcrInfPlanOld = PlanRCRInfo.getPlan(plan, ProcessInfo.RCRProcesses[j], stageInf.milestoneID, stageInfPrevious.milestoneID);
				if (rcrInfPlanOld!=null && !Double.isNaN(rcrInfPlanOld.plannedValue))
					remainPrevPlan += rcrInfPlanOld.plannedValue;
			}
			remainActual = 100 - sumActual;
            //all future  stages are forecasted
			for (int i = currentStageID; i < stageList.size(); i++) {
				stageInf = (StageInfo) stageList.elementAt(i);
				rcrInf = PlanRCRInfo.getPlan(planRCRList, ProcessInfo.RCRProcesses[j], stageInf.milestoneID);
				rcrInfPlanOld = PlanRCRInfo.getPlan(plan, ProcessInfo.RCRProcesses[j], stageInf.milestoneID, stageInfPrevious.milestoneID);
				if (rcrInfPlanOld != null && !Double.isNaN(rcrInfPlanOld.plannedValue))
					rcrInf.forecasted = rcrInfPlanOld.plannedValue;
			}
			if (remainPrevPlan < remainActual) {
				rcrInf = PlanRCRInfo.getPlan(planRCRList, ProcessInfo.RCRProcesses[j], stageInfCurrent.milestoneID);
				rcrInf.color = Color.BADMETRIC;
				rcrInfPlanOld = PlanRCRInfo.getPlan(plan, ProcessInfo.RCRProcesses[j], stageInfCurrent.milestoneID, stageInfPrevious.milestoneID);
				rcrInf.forecasted =
					((rcrInfPlanOld == null || Double.isNaN(rcrInfPlanOld.plannedValue)) ? 0 : rcrInfPlanOld.plannedValue)
						+ remainActual
						- remainPrevPlan;
			}
			else if (remainPrevPlan > remainActual) {
				//remainPlan = 100 - sumReplan;
				for (int i = currentStageID; i < stageList.size(); i++) {
					stageInf = (StageInfo) stageList.elementAt(i);
					rcrInf = PlanRCRInfo.getPlan(planRCRList, ProcessInfo.RCRProcesses[j], stageInf.milestoneID);
					rcrInfPlanOld = PlanRCRInfo.getPlan(plan, ProcessInfo.RCRProcesses[j], stageInf.milestoneID, stageInfPrevious.milestoneID);
					rcrInf.color = Color.GOODMETRIC;
					if (rcrInfPlanOld != null && !Double.isNaN(rcrInfPlanOld.plannedValue))
						rcrInf.forecasted = rcrInfPlanOld.plannedValue * (remainActual / remainPrevPlan);
				}
			}
		}
		return planRCRList;
	}
/*
	public static final double getActualRCRByProcess(Vector reqs, int processID, Date sdate,Date edate) {
		RequirementInfo info;
		long sMili=sdate.getTime();
		long eMili=edate.getTime();
		int sum=0;
		int total=0;
		//boolean [] grid= new boolean [6];
		boolean found;
		Date date=null;
	try
	{
		if (reqs != null)
		{
			for (int i = 0; i < reqs.size(); i++) {
				info = (RequirementInfo) reqs.elementAt(i);
				System.out.println("Begin shit " + info.requirementID);
				if (info.cancelledDate == null) {
					found = false;
					date =null;
					switch (RequirementInfo.getProcessMapping(processID)) {
						case RequirementInfo.STATUS_ACCEPTED :
							date = info.acceptedDate;
							break;
						case RequirementInfo.STATUS_DEPLOYED :
							date = info.deployedDate;
							break;
						case RequirementInfo.STATUS_TESTED :
							date = info.testedDate;
							break;
						case RequirementInfo.STATUS_CODED :
							date = info.codedDate;
							break;
						case RequirementInfo.STATUS_DESIGNED :
							date = info.designedDate;
							break;
						case RequirementInfo.STATUS_COMMITTED :
							date = info.committedDate;
							break;
					}
					found = (date != null && date.getTime() <= eMili && date.getTime() >= sMili);
					total += info.size;
					if (found)
						sum += info.size;
				}
			}
		}
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	finally
	{
		return (total==0)?0:(double)sum*100d/(double)total;
	}
	}
	*/
	public static final double getActualRCRByProcess(Vector reqs, int processID, Date sdate,Date edate) {
		int sum=0;
		int total=0;
		boolean found;
		Date date=null;

		if (sdate == null)
			sdate =new Date(0);
		if (edate == null)
			edate =new Date(0);

		long sMili=sdate.getTime();
		long eMili=edate.getTime();

		for (int i = 0; i < reqs.size(); i++) {
			RequirementInfo info = (RequirementInfo) reqs.elementAt(i);
			if (info.cancelledDate == null) {
				found = false;
				date =null;
				switch (RequirementInfo.getProcessMapping(processID)) {
					case RequirementInfo.STATUS_ACCEPTED :
						date = info.acceptedDate;
						break;
					case RequirementInfo.STATUS_DEPLOYED :
						date = info.deployedDate;
						break;
					case RequirementInfo.STATUS_TESTED :
						date = info.testedDate;
						break;
					case RequirementInfo.STATUS_CODED :
						date = info.codedDate;
						break;
					case RequirementInfo.STATUS_DESIGNED :
						date = info.designedDate;
						break;
					case RequirementInfo.STATUS_COMMITTED :
						date = info.committedDate;
						break;
				}
				found = (date != null && date.getTime() <= eMili && date.getTime() >= sMili);
				total += info.size;
				if (found)
					sum += info.size;
			}
		}
		return (total==0)?0:(double)sum*100d/(double)total;
	}
/**
 * 
 * also set the status of reqs
 */
    public static final double getActualRCR(ProjectInfo pinf, Vector reqs){
        return getActualRCRByDate(pinf, reqs, null,false);
    }
    public static final double getActualRCRByDateFinal(ProjectInfo pinf, Vector reqs, Date edate) {
        return getActualRCRByDate(pinf, reqs, edate,true);
    }
	public static final double getActualRCRByDateHistory(ProjectInfo pinf, Vector reqs, Date edate) {
        return getActualRCRByDate(pinf, reqs, edate,false);
        
    }
    private static final double getActualRCRByDate(ProjectInfo pinf, Vector reqs, Date edate,boolean isFinal) {
		RequirementInfo info;
		long milisec = 0;
        boolean dateDefined = ((edate != null) && (edate.getTime() != 0));
		double sum = 0;
		int total = 0;
		
        if (dateDefined)
                milisec = edate.getTime();
        final Vector completeness = Param.getCompletenessRateList(pinf);
        boolean statusSet;
		for (int i = 0; i < reqs.size(); i++) {
			info = (RequirementInfo) reqs.elementAt(i);
			statusSet=true;
            if (info.cancelledDate != null && (!dateDefined ||isFinal|| info.cancelledDate.getTime() <= milisec)) //||isFinal
                info.statusID = RequirementInfo.STATUS_CANCELLED;
            else if(info.acceptedDate != null && (!dateDefined || info.acceptedDate.getTime() <= milisec))
				info.statusID = RequirementInfo.STATUS_ACCEPTED;
			else if (info.deployedDate != null && (!dateDefined || info.deployedDate.getTime() <= milisec))
				info.statusID = RequirementInfo.STATUS_DEPLOYED;
			else if (info.testedDate != null && (!dateDefined || info.testedDate.getTime() <= milisec))
				info.statusID = RequirementInfo.STATUS_TESTED;
			else if (info.codedDate != null && (!dateDefined || info.codedDate.getTime() <= milisec))
				info.statusID = RequirementInfo.STATUS_CODED;
			else if (info.designedDate != null && (!dateDefined || info.designedDate.getTime() <= milisec))
				info.statusID = RequirementInfo.STATUS_DESIGNED;
			else if (info.committedDate != null && (!dateDefined ||info.committedDate.getTime() <= milisec))//||isFinal
				info.statusID = RequirementInfo.STATUS_COMMITTED;
			else
			{
				statusSet = false;
			}
			//find completeness rate
			info.completenessRate = 0;
			CompletenessRateInfo completenessRateinfo;
			if (statusSet)
			for (int j = 0; j < completeness.size(); j++) {
				completenessRateinfo  =	(CompletenessRateInfo) completeness.elementAt(j);
				if (info.statusID==completenessRateinfo.statusID) {
					info.completenessRate = completenessRateinfo.value;
                    total+=info.size;
                    sum+=info.completenessRate*info.size;
					break;
				}
			}
			
            info.statusName = RequirementInfo.getStatusName(info.statusID);
		}

		return (total==0)?0:(double)sum/(double)total;
	}

	public static final Vector getRCRByStage(ProjectInfo pinf,Vector stageList, Vector reqList,Vector planRCRProcess,boolean hasBeenReplanned){
		Vector result=new Vector();
		//norms
		Vector normRefInfos= Norms.getEffortDstrByStg(pinf);
		int [] stdStageCount=Schedule.getStdStageCount(stageList);
	    Vector planRCRInfos= getDBPlanRCR(pinf.getProjectId());
		
		//actual
		PlanRCRInfo planInf;
		StageInfo stinf;
		RCRByStageInfo inf;
        RCRByProcessInfo rcrProc;
        
        double actualPrev=0;
		double totalReplan=0;
        long firstStage=0;
        long curStage=-1;		
        int isStage=0;        
		for (int i=0;i<stageList.size();i++){
			inf=new RCRByStageInfo();
			stinf=(StageInfo)stageList.elementAt(i);
									            
            if (i==0)
                firstStage=stinf.milestoneID;                   			                
				    
            if (curStage<0 && stinf.aEndD==null){            	                
				curStage=stinf.milestoneID;				
				if (i>1)				
					curStage=((StageInfo)stageList.elementAt(i-1)).milestoneID;				
								                       
				isStage=i;			       
            }
            			
			                          
			inf.stageName=stinf.stage;
			if (stinf.StandardStage>0)
				inf.norm =((NormRefInfo)normRefInfos.elementAt(stinf.StandardStage-1)).value/(double)stdStageCount[stinf.StandardStage-1];
			if (stinf.aEndD!=null){
				/*
				RequirementInfo reqHdrInfo = getRequirementInfo(reqList,stinf.aEndD);//-actualPrev;
				inf.actual=reqHdrInfo.sumSizeCompletion -actualPrev;
                actualPrev+=inf.actual; 
				*/
				inf.actual=getActualRCRByDateFinal(pinf,reqList,stinf.aEndD)-actualPrev;
				actualPrev+=inf.actual; 
            }
            
								
			if (i==0 && inf.actual!=0)
				inf.rePlan=inf.actual;//Double.NaN;
                        
            for (int j=0;j<ProcessInfo.RCRProcesses.length;j++){
                //Plan
                planInf=PlanRCRInfo.getPlan(planRCRInfos,ProcessInfo.RCRProcesses[j],stinf.milestoneID,firstStage);
                rcrProc=(RCRByProcessInfo)planRCRProcess.elementAt(j);					
                if (planInf!=null  && planInf.plannedValue>0)
                    inf.plan+=planInf.plannedValue*rcrProc.plan/100d;					
                
                //Replan
                if (i>0){					             	
                    planInf=PlanRCRInfo.getPlan(planRCRInfos,ProcessInfo.RCRProcesses[j],stinf.milestoneID,stinf.aEndD==null ?curStage :stinf.milestoneID); //stinf.aEndD==null ?curStage :stinf.milestoneID                    
                    if (planInf!=null && planInf.plannedValue>0){                    	
                        inf.rePlan+=planInf.plannedValue*rcrProc.rePlan/100d;                                                
                    }
                    
                }
            }
			
			if (!Double.isNaN(inf.rePlan))
				totalReplan+=inf.rePlan;
						
			/*
			if (totalReplan > 100)
			{
				inf.rePlan = inf.rePlan - (totalReplan - 100);
				totalReplan = 100;
			}		
			
			inf.deviation=CommonTools.metricDeviation(inf.plan,inf.rePlan,inf.actual);	
			*/
			
			result.add(inf);
			
		
		}
				
		//Added by BinhNT
		double rePlanfirstStage;
		inf = (RCRByStageInfo)result.elementAt(0);
		rePlanfirstStage=inf.rePlan;
		
		for (int i = 0; i < result.size(); i++) {
			inf = (RCRByStageInfo)result.elementAt(i);						
			if (hasBeenReplanned){			
				if (totalReplan != 0)					
					if (i==0)
						inf.rePlan =inf.actual;
					else	
						inf.rePlan = inf.rePlan * (100d-rePlanfirstStage)/(totalReplan-rePlanfirstStage);				
				else
					inf.rePlan = Double.NaN;       
			}else{
				if (isStage<=1){ //Is stage 2				
					inf.rePlan = Double.NaN;					
				}else{				
					if (totalReplan != 0)			
						if (i==0)
							inf.rePlan =inf.actual;
						else	
							inf.rePlan = inf.rePlan * (100d-rePlanfirstStage)/(totalReplan-rePlanfirstStage);					
					else
						inf.rePlan = Double.NaN;
				}				
				
			}
			
			inf.deviation=CommonTools.metricDeviation(inf.plan,inf.rePlan,inf.actual);
										
		}		
				
		return result;
	}
    /**
     * 
     * @return vector of RCRByProcessInfo
     * 
     */
    public static final Vector getRCRByProcess(ProjectInfo pinf, Vector processEffortByStages,Vector reqList, Vector stageList,boolean hasBeenReplanned){
        Vector result=new Vector();
        //norms & Plan eff by proc
        Vector planEffort= Effort.getPlanProcessEffortAndRCR(pinf);
        Vector normInfos= Norms.getEffortDstrByProcRCR(pinf);
        
        //actual
        ProcessEffortInfo processEffortInfo;
        RCRByProcessInfo inf;
        int mappingID;
        ProcessEffortByStageInfo processEffortByStage;
        NormInfo ninf;
        double superTotal=0;		
		int currentStageID = 0;		
		
		//is current stage already replanned           
					
		StageInfo stageInfCurrent = null;
		StageInfo stageInfPrev = null;
		
		for (int i = 0; i < stageList.size(); i++) {
			stageInfCurrent = (StageInfo)stageList.elementAt(i);			
			currentStageID=i;
			if (i > 0)
				stageInfPrev = (StageInfo)stageList.elementAt(i - 1);
			if (stageInfCurrent.aEndD == null) {
				break;
			}
		}			
			
        for (int i=0;i<ProcessInfo.RCRProcesses.length;i++){
            inf=new RCRByProcessInfo();
            inf.projectID = pinf.getProjectId();
            inf.processID = ProcessInfo.RCRProcesses[i];
            inf.processName = ProcessInfo.getProcessName(inf.processID);
            //Norm
            ninf=NormInfo.getNormByMetricID(MetricDescInfo.completionMetrics[i],normInfos);
            if (ninf!=null)
                inf.norm=ninf.average; 
           //Plan
            for (int j=0;j<planEffort.size();j++){
                processEffortInfo=(ProcessEffortInfo)planEffort.elementAt(j);
                if (processEffortInfo.processID==ProcessInfo.RCRProcesses[i]){
                    inf.plan=processEffortInfo.estimatedRCR;
                    break;
                }
            }
            if(Double.isNaN(inf.plan))
                inf.plan=inf.norm;
            //Replan
			mappingID=CommonTools.arrayScan(ProcessInfo.trackedProcessId,ProcessInfo.RCRProcesses[i]);
			for (int j = 0; j < processEffortByStages.size(); j++) {								
				processEffortByStage = (ProcessEffortByStageInfo)processEffortByStages.elementAt(j);					
				
				if (currentStageID>1 ||(currentStageID==1 && hasBeenReplanned)){			
					if (processEffortByStage.isOpen && !Double.isNaN(processEffortByStage.forecast[mappingID])){
						if (Double.isNaN(inf.rePlan))
							inf.rePlan=0;																			
							inf.rePlan+=processEffortByStage.forecast[mappingID];
							superTotal+=processEffortByStage.forecast[mappingID];					
					}
					else if (!processEffortByStage.isOpen){
						if (Double.isNaN(inf.rePlan))
							inf.rePlan=0;
						inf.rePlan+=processEffortByStage.actual[mappingID];
						superTotal+=processEffortByStage.actual[mappingID];
					}						
				}else{
					inf.rePlan=Double.NaN;
					superTotal=Double.NaN;
				}						 				
			}	
			
						  
            //Actual
            RequirementInfo rinf=getRequirementInfo(reqList);
            switch (ProcessInfo.RCRProcesses[i]) {
                case ProcessInfo.REQUIREMENT :
                    inf.actual = (double)rinf.sumSizeCommitted ;
                    break;
                case ProcessInfo.DESIGN :
                    inf.actual = (double)rinf.sumSizeDesigned;
                    break;
                case ProcessInfo.CODING :
                    inf.actual = (double)rinf.sumSizeCoded ;
                    break;
                case ProcessInfo.TEST :
                    inf.actual = (double)rinf.sumSizeTested ;
                    break;
                case ProcessInfo.DEPLOYMENT :
                    inf.actual = (double)rinf.sumSizeDeployed ;
                    break;
                case ProcessInfo.CUSTOMER_SUPPORT :
                    inf.actual = (double)rinf.sumSizeAccepted ;
                    break;
            }
            if (rinf.sumSizeNotCancelled!=0)
                inf.actual*=100d/rinf.sumSizeCommitted;
            
            result.add(inf);
        }
        
        for (int i = 0; i < result.size(); i++) {
            inf = (RCRByProcessInfo)result.elementAt(i);
            if (superTotal != 0)
                inf.rePlan = inf.rePlan * 100d / superTotal;
            else
                inf.rePlan = Double.NaN;
                
            inf.actual *= (Double.isNaN(inf.rePlan) ? inf.plan : inf.rePlan) / 100d;
            inf.deviation = CommonTools.metricDeviation(inf.plan, inf.rePlan, inf.actual);
																							
        }
        return result;
    }
	public static final RequirementInfo getRequirementInfo(Vector requirementList, final java.sql.Date date) {
		final RequirementInfo reqHdrInfo = new RequirementInfo();
		try {
			double sumSizeXRate = 0;
			double sumSizeChangeRequest = 0;
			double avgResponseTime = 0;
			double nResponseTime = 0;
			for (int i = 0; i < requirementList.size(); i++) {
				RequirementInfo reqInfo = (RequirementInfo) requirementList.elementAt(i);
				if ((reqInfo.receivedDate != null) && (reqInfo.responseDate != null)) {
					nResponseTime++;
					avgResponseTime += reqInfo.responseTime;
				}
				boolean cancelIsDefined = (reqInfo.cancelledDate != null);
                if (!cancelIsDefined)
					reqHdrInfo.sumSizeNotCancelled += reqInfo.size;
				boolean dateIsDefined = ((date != null) && (date.getTime() != 0));
				//date not defined
				if (!dateIsDefined) {
					if (!cancelIsDefined && (reqInfo.committedDate != null)) {
						sumSizeXRate += reqInfo.size * reqInfo.completenessRate;
						if ((reqInfo.type == 2) && (reqInfo.committedDate != null))
							sumSizeChangeRequest += reqInfo.size;
						
						if ((reqInfo.committedDate != null))
							reqHdrInfo.sumSizeCommitted += reqInfo.size;
						if (reqInfo.designedDate != null)
							reqHdrInfo.sumSizeDesigned += reqInfo.size;
						if (reqInfo.codedDate != null)
							reqHdrInfo.sumSizeCoded += reqInfo.size;
						if (reqInfo.testedDate != null)
							reqHdrInfo.sumSizeTested += reqInfo.size;
						if ((reqInfo.deployedDate != null))
							reqHdrInfo.sumSizeDeployed += reqInfo.size;
						if ((reqInfo.acceptedDate != null))
							reqHdrInfo.sumSizeAccepted += reqInfo.size;
						if (
							(reqInfo.acceptedDate != null) || 
							(reqInfo.deployedDate != null)
                            )
							reqHdrInfo.sumSizeDeployedOrAccepted += reqInfo.size;
					}
				}
				//date defined
				else if (	((cancelIsDefined && (reqInfo.cancelledDate.getTime() > date.getTime())) || (!cancelIsDefined)) &&
							(reqInfo.committedDate != null &&(reqInfo.committedDate.compareTo(date) <= 0))
						) {
					sumSizeXRate += reqInfo.size * reqInfo.completenessRate;
					if (reqInfo.type == 2)
						sumSizeChangeRequest += reqInfo.size;
					reqHdrInfo.sumSizeCommitted += reqInfo.size;
					if ((reqInfo.designedDate != null) && (reqInfo.designedDate.compareTo(date) <= 0))
						reqHdrInfo.sumSizeDesigned += reqInfo.size;
					if ((reqInfo.codedDate != null) && (reqInfo.codedDate.compareTo(date) <= 0))
						reqHdrInfo.sumSizeCoded += reqInfo.size;
					if ((reqInfo.testedDate != null) && (reqInfo.testedDate.compareTo(date) <= 0))
						reqHdrInfo.sumSizeTested += reqInfo.size;
					if ((reqInfo.deployedDate != null) && (reqInfo.deployedDate.compareTo(date) <= 0))
						reqHdrInfo.sumSizeDeployed += reqInfo.size;
					if ((reqInfo.acceptedDate != null) && (reqInfo.acceptedDate.compareTo(date) <= 0))
						reqHdrInfo.sumSizeAccepted += reqInfo.size;
					if (
						(((reqInfo.acceptedDate != null) && (reqInfo.acceptedDate.compareTo(date) <= 0))
						|| ((reqInfo.deployedDate != null) && (reqInfo.deployedDate.compareTo(date) <= 0)))
						)
						reqHdrInfo.sumSizeDeployedOrAccepted += reqInfo.size;
				}
			}
			if (reqHdrInfo.sumSizeCommitted != 0) {
                
				reqHdrInfo.sumSizeCompletion = sumSizeXRate / (double)reqHdrInfo.sumSizeCommitted;
				reqHdrInfo.sumSizeStability = sumSizeChangeRequest * 100d / (double) reqHdrInfo.sumSizeCommitted;
			}
			if (nResponseTime != 0) {
				avgResponseTime = avgResponseTime / nResponseTime;
				reqHdrInfo.avgResponseTime = avgResponseTime;
			}
            //System.out.println("reqHdrInfo.sumSizeCompletion "+reqHdrInfo.sumSizeCompletion);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return reqHdrInfo;
	}

	public static final RequirementInfo getRequirementInfo(Vector requirementList) {
		return Requirement.getRequirementInfo(requirementList, null);
	}
	
	// Add by HaiMM
	public static final Vector getEstEffortPlan(long prjId,Vector stageList) {
		StageInfo stageInf=null;
		Vector estEffortList=new Vector();
		
		EstEffortInfo estEffInf, estEffInfPlan;
		Vector processName = Requirement.getEstProcessName(prjId);
		Vector estEffList = Requirement.getEstEffortList(prjId);
		
		for (int i=0;i<stageList.size();i++){
			stageInf=(StageInfo)stageList.elementAt(i);
			for (int j=0;j<processName.size();j++){
				estEffInf= new EstEffortInfo();
				estEffInf.milestoneId=stageInf.milestoneID;
				
				estEffInf.processName=(String)processName.elementAt(j);
				estEffInfPlan = new EstEffortInfo();
				estEffInfPlan = EstEffortInfo.getPlan(estEffList, (String)processName.elementAt(j), stageInf.milestoneID);
				if (estEffInfPlan != null)
				estEffInf.plannedValue = estEffInfPlan.plannedValue;
				
				estEffortList.add(estEffInf);
			}
		}
		return estEffortList;
	}
	
	// Add by HaiMM
	public static final Vector getEstProcessName(long projectId){
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try{
			sql = "SELECT distinct process_name FROM est_effort " +
				  "WHERE PROJECT_ID = ? " +
				  "ORDER BY process_name";            
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectId);
			rs = stm.executeQuery();
			while(rs.next()){
				if(rs.getString("process_name")!="" && rs.getString("process_name") != null){
					resultVector.addElement(rs.getString("process_name").trim());
				}
			}
			return resultVector;
		}
		catch(Exception e){
			e.printStackTrace();
			return resultVector;
		}
		finally{
			ServerHelper.closeConnection(conn, stm, rs);
		}
		
	}
	
	public static boolean delEstEffort(String processName){
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "DELETE est_effort WHERE process_name = " + "'" + processName + "'";
			stm.executeQuery(sql);
		}
		catch (SQLException e) {
			bl = false;
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return bl;
		}
	}
	public static final String makeCondition(String right){
		try{
			if (right.equalsIgnoreCase("")) return "";
			String value = " and( ";
			int flag = 0 ; 
			StringTokenizer st = new StringTokenizer(right,",");
			while(st.hasMoreTokens()) {

				String temp = st.nextToken();
				if (!temp.trim().equals("")) {
					String temp1="";
					if (temp.indexOf("'")!=-1){
						for(int k=0;k<temp.length();k++) {
							if (temp.charAt(k)!='\'')
								temp1 = temp1 + temp.charAt(k);
							else
								temp1 = temp1 + temp.charAt(k) + '\'';
						}
						temp = temp1 ; 
					}
				}
				if (flag==0){
					if (!temp.trim().equals("")) {
						if (temp.trim().equalsIgnoreCase("Hitachi Joho")) {
							value = value + "(pr.customer LIKE 'Hitachi Joho%') OR (pr.customer LIKE 'Hitachi Ltd%') OR (pr.customer LIKE 'Hitachi Medical Corporation%') OR (pr.customer LIKE 'HITACHI%')";
						}
						else if (temp.trim().equalsIgnoreCase("Kyocera Mita")) {
							value = value + "(pr.customer LIKE 'Kyocera Corporation in Japan%') OR (pr.customer LIKE 'Kyocera Mita%') OR (pr.customer LIKE 'Kyocera Mobile%') OR (pr.customer LIKE 'Kyocera-Mita%')";
						}
						else value = value + "(pr.customer LIKE '"+temp.trim()+"%')";
						flag++;
					}
				}
				else {
					if (!temp.trim().equals("")) {
						if (temp.trim().equalsIgnoreCase("Hitachi Joho")) {
							value = value + " OR (pr.customer LIKE 'Hitachi Joho%') OR (pr.customer LIKE 'Hitachi Ltd%') OR (pr.customer LIKE 'Hitachi Medical Corporation%') OR (pr.customer LIKE 'HITACHI%')";
						}
						else if (temp.trim().equalsIgnoreCase("Kyocera Mita")) {
							value = value + " OR (pr.customer LIKE 'Kyocera Corporation in Japan%') OR (pr.customer LIKE 'Kyocera Mita%') OR (pr.customer LIKE 'Kyocera Mobile%') OR (pr.customer LIKE 'Kyocera-Mita%')";
						}
						else value = value + " OR (pr.customer LIKE '"+temp.trim()+"%')";
					}
				}
			}
			value = value+ " )";
			return value;
		}
		catch(Exception e){
			return "";
		}
	}
	public static final Vector getListDelivery(String fromDate,String toDate,String right){
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		String dateCondition = "";
		
		if (fromDate.equals("")&&toDate.equals("")) dateCondition = "";
		else if ((!fromDate.equals(""))&&toDate.equals("")) dateCondition = "AND NVL(md.replanned_release_date, md.planned_release_date) >= TO_DATE ('"+fromDate+"', 'dd-MON-yy')";
		else if ((!toDate.equals(""))&&fromDate.equals("")) dateCondition = "AND NVL(md.replanned_release_date, md.planned_release_date) <= TO_DATE ('"+toDate+"', 'dd-MON-yy')";
		else  dateCondition = "AND NVL(md.replanned_release_date, md.planned_release_date) <= TO_DATE ('"+toDate+"', 'dd-MON-yy') AND NVL(md.replanned_release_date, md.planned_release_date) >= TO_DATE ('"+fromDate+"', 'dd-MON-yy')";
		
		try{
			sql = "SELECT   pr.code AS project_code, pr.group_name, pr.customer, md.NAME AS deliverable, "+
         		  "TO_CHAR (md.planned_release_date, "+
                  "'dd/MM/yyyy') AS first_committed_date, "+
         		  "TO_CHAR (md.replanned_release_date, 'dd/MM/yyyy') AS re_committed_date, "+
         		  "TO_CHAR (md.actual_release_date, 'dd/MM/yyyy') AS actual_date, "+
         		  "DECODE (md.status, "+
                 	"1, 'Pending', "+
                 	"2, 'Accepted', "+
                 	"3, 'Rejected', "+
                 	"4, 'Cancelled', "+
                 	"5, 'Others', "+
                 	"6, 'InProgress' "+
                  	") AS status, md.note "+
    			"FROM module md, project pr "+
   				"WHERE pr.project_id = md.project_id "+
     			"AND md.status = '6' "+ dateCondition + //New condition
     			"AND md.is_deliverable = '1' "+
     			"AND md.project_id IN ( "+
            	"SELECT pr.project_id "+
                "FROM project pr, project_plan pl "+
             	"WHERE status = 0  "+//0-On-going, 1-Closed
                "AND code NOT LIKE 'Daily%' "+
                "AND NAME NOT LIKE 'Daily%' "+
                "AND pr.project_id = pl.project_id "+ makeCondition(right) + 
                " ) "+
				"ORDER BY customer, project_code, first_committed_date";            
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while(rs.next()){
				Delivery temp = new Delivery();
				temp.setProjectCode(rs.getString("PROJECT_CODE"));
				temp.setCustomerName(rs.getString("CUSTOMER"));
				temp.setDeliverable(rs.getString("DELIVERABLE"));
				temp.setFirstCommitDate(rs.getString("FIRST_COMMITTED_DATE"));
				temp.setReCommitDate(rs.getString("RE_COMMITTED_DATE"));
				temp.setActualDate(rs.getString("ACTUAL_DATE"));
				temp.setStatus(rs.getString("STATUS"));
				temp.setNote(rs.getString("NOTE"));
				resultVector.add(temp);
			}
			return resultVector;
		}
		catch(Exception e){
			e.printStackTrace();
			return resultVector;
		}
		finally{
			ServerHelper.closeConnection(conn, stm, rs);
		}
		
	}
	public static Vector getListOGS(){
			Connection conn = null;
			Statement stm = null;
			String sql = null;
		    ResultSet rs = null;
		    Vector data = new Vector();
			try {
				conn = ServerHelper.instance().getConnection();
				stm = conn.createStatement();
				//sql = "select unit_name from rms_unit where unit_type_code = 'GROUP' order by unit_name ";
				sql = "select groupname from groups  order by groupname ";
				rs = stm.executeQuery(sql);
				while(rs.next()) {
					data.add(rs.getString("groupname"));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, stm, rs);
				return data;
			}
		}
}
