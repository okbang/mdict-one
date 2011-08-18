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
import com.fms1.tools.ReportMonth;
import com.fms1.tools.CommonTools;
import com.fms1.tools.Db;
import com.fms1.web.*;
/**
 * Finance logic
 * Author: HaiMM
 * 06-Feb-09
 */
public final class Finance {
	
	public static final Vector getDurationList(ProjectInfo pinf) {
		Vector durationList = new Vector();
		

		ReportMonth repMonth = new ReportMonth(pinf.getPlanStartDate());
		
		// Get first month (including 4 object for each week of month)
		for (int i=1;i<=4;i++) {
			DurationInfo dInf = new DurationInfo();
			dInf.month = repMonth.getMonth();
			dInf.year = repMonth.getYear();
			dInf.name = "W" + i + " - " + CommonTools.getMonth(repMonth.getMonth());
			dInf.week = i;

			durationList.add(dInf);
		}
		
		// Get next months
		int duration = getDurationByMonth(pinf);
		ReportMonth rMonth = repMonth;
		for (int j=1;j<=duration;j++) {
			rMonth = rMonth.getNextMonth();

			for (int i=1;i<=4;i++) {
				DurationInfo dInf = new DurationInfo();
				dInf.month = rMonth.getMonth();
				dInf.year = rMonth.getYear();
				dInf.name = "W" + i + " - " + CommonTools.getMonth(rMonth.getMonth());
				dInf.week = i;
				durationList.add(dInf);
			}
		}
		
		return durationList;
	}
	
	public static final int getDurationByMonth(ProjectInfo pinf) {
		int firstMonth = 0;
		int lastMonth = 0;
		
		int duration = -1;
		
		// Get first month
		ReportMonth repFirstMonth = new ReportMonth(pinf.getPlanStartDate());
		firstMonth = repFirstMonth.getMonth();
		
		// Get last month		
		ReportMonth repLastMonth = new ReportMonth(pinf.getPlannedFinishDate());
		lastMonth = repLastMonth.getMonth();
		
		duration = (lastMonth > firstMonth) ? (lastMonth - firstMonth) : (12 - firstMonth + lastMonth);
		
		return duration;
	}
	
	public static final Vector getFinancePlan(long prjId,Vector durationList) {
		DurationInfo durationInf=null;
		Vector financeList=new Vector();
		
		FinanceInfo financeInf, financeInfPlan;
		Vector financeNameList = Finance.getPlanFinanceName(prjId);
		Vector financePlanList = Finance.getPlanFinanceList(prjId);
		
		for (int i=0;i<durationList.size();i++){
			durationInf=(DurationInfo)durationList.elementAt(i);
			for (int j=0;j<financeNameList.size();j++){
				financeInf= new FinanceInfo();
				financeInf.week=durationInf.week;
				financeInf.month=durationInf.month;
				financeInf.year=durationInf.year;

				FinanceTotalInfo fTotalInf = new FinanceTotalInfo();
				fTotalInf = (FinanceTotalInfo)financeNameList.get(j);
				
				financeInf.financeName=fTotalInf.financeName;
				financeInf.note = fTotalInf.note;
				
				financeInfPlan = new FinanceInfo();
				financeInfPlan = FinanceInfo.getPlan(financePlanList, fTotalInf.financeName + StringConstants.pattern + fTotalInf.note, 
											durationInf.week, durationInf.month, durationInf.year);
				if (financeInfPlan != null)
				financeInf.plannedValue = financeInfPlan.plannedValue;
				
				financeList.add(financeInf);
			}
		}
		return financeList;
	}
	
	public static final Vector getPlanFinanceName(long projectId){
		final Vector resultVector = new Vector();
		final Vector tokenString = new Vector();
		String token = "";
		String financeNote = "";
		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try{
			sql = "SELECT distinct finance_name FROM finance_plan " +
				  "WHERE PROJECT_ID = ? " +
				  "ORDER BY finance_name";            
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectId);
			rs = stm.executeQuery();
			FinanceTotalInfo financeTotalInf;
			while(rs.next()){
				financeTotalInf=new FinanceTotalInfo();
				financeNote = rs.getString("finance_name");		
				StringTokenizer st =
								new StringTokenizer(financeNote, StringConstants.pattern);
				while (st.hasMoreElements())
				{
					token = st.nextToken().toString();
					tokenString.add(token);
				}
				if(rs.getString("finance_name")!="" && rs.getString("finance_name") != null && tokenString.size() == 2){
					financeTotalInf.setFinanceName((String)tokenString.elementAt(0));
					financeTotalInf.setNote((String)tokenString.elementAt(1));
				}
				resultVector.add(financeTotalInf);
				// reset the vector tokenString
				tokenString.removeElementAt(0);
				tokenString.removeElementAt(0);
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

	public static final Vector getPlanFinanceList(long projectid)  {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		Vector result = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM finance_plan"
				+" WHERE project_id=?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectid);
			rs=prepStmt.executeQuery();
			FinanceInfo inf;

			while (rs.next()){
				inf = new FinanceInfo();
				inf.financeName=rs.getString("finance_name");
				inf.plannedValue = Db.getDouble(rs,"plan_value");
				inf.week=rs.getInt("week");
				inf.month=rs.getInt("month");
				inf.year=rs.getInt("year");
//				inf.note=rs.getString("note");
								
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
	
	public static boolean delFinanceDB(String financeName){
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "DELETE finance_plan WHERE finance_name = " + "'" + financeName + "'";
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
	
	public static final void updateFinancePlan(Vector financePlanList, long pid) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmtInsert = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE finance_plan SET " 
				+ "plan_value = ?"
				+ " WHERE project_id = ? AND week=? AND month=? AND year=? AND finance_name=?";
			prepStmt = conn.prepareStatement(sql);
		
			sql = "Insert INTO finance_plan ("
					+ " plan_value "
					+" ,project_id,week, month, year,finance_name)" 
									+ " VALUES(?,?,?,?,?,?)";
			prepStmtInsert= conn.prepareStatement(sql);
		
			FinanceInfo inf;
			for (int k = 0; k < financePlanList.size(); k++) {
				inf = (FinanceInfo) financePlanList.elementAt(k);
				String fName = inf.financeName;
				
				Db.setDouble(prepStmt,1,inf.plannedValue);
				prepStmt.setLong(2,pid);
				prepStmt.setInt(3,inf.week);
				prepStmt.setInt(4,inf.month);
				prepStmt.setInt(5,inf.year);
				prepStmt.setString(6,fName);
			
				if (prepStmt.executeUpdate()<1){
					Db.setDouble(prepStmtInsert,1,inf.plannedValue);
					prepStmtInsert.setLong(2,pid);
					prepStmtInsert.setInt(3,inf.week);
					prepStmtInsert.setInt(4,inf.month);
					prepStmtInsert.setInt(5,inf.year);
					prepStmtInsert.setString(6,fName);
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
	
	public static final void updateFinanceNote(Vector vFinaneNameNote, Vector financePlanTotalList, long pid) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE finance_plan SET " 
				+ "finance_name = ?"
				+ " WHERE project_id = ? AND finance_name=?";
			prepStmt = conn.prepareStatement(sql);
		
			FinanceTotalInfo inf;
			for (int k = 0; k < financePlanTotalList.size(); k++) {
				inf=(FinanceTotalInfo)financePlanTotalList.elementAt(k);

				prepStmt.setString(1,(String)vFinaneNameNote.elementAt(k));
				prepStmt.setLong(2,pid);
				prepStmt.setString(3,inf.financeName + StringConstants.pattern + inf.note);
			
				prepStmt.executeUpdate();
				
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
}
