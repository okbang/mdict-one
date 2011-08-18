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
 
 package fpt.dashboard.ProjectManagementTran.ejb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import fpt.dashboard.framework.connection.WSConnectionPooling;
public class MilestoneEJB implements SessionBean
{
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	private Connection con = null;
	
/**
 * method: public void getMilestoneList(int ProjectID, int index, int orderby)	throws SQLException
 * author: Nguyen Thai Son
 * date: August 20, 2002
 * description: this method is replaced for getlist() and nextMilestone() methods. 
 * It 's due to an error "ResultSetProxy is closed" at run-time
 * */		
	
	public Collection getMilestoneList(int ProjectID, int index, int orderby)	throws SQLException
	{
		ArrayList listResult = new ArrayList();
		Statement stmt = null;
		ResultSet rsResult = null;
		
		try
			{
			String strSQL =
				"SELECT Milestone_ID, Name, Complete, "
					+ "TO_CHAR(Base_start_date,'dd/mm/yy') AS Base_start_date, TO_CHAR(Plan_start_date,'dd/mm/yy') AS Plan_start_date,"
					+ "TO_CHAR(Actual_start_date,'dd/mm/yy') AS Actual_start_date, TO_CHAR(Base_Finish_date,'dd/mm/yy') AS Base_Finish_date, "
					+ "TO_CHAR(plan_finish_date,'dd/mm/yy') AS plan_finish_date,TO_CHAR(actual_finish_date,'dd/mm/yy') AS actual_finish_date,"
					+ "base_effort,plan_effort, actual_effort, description FROM Milestone "
					+ "WHERE Project_ID =  " + ProjectID;
			strSQL += " ORDER BY Milestone.Base_Finish_date";
			System.out.println("MilestoneEJB.getMilestoneList(): strSQL = " + strSQL);
			
			if (ds == null)		ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rsResult = stmt.executeQuery(strSQL);
			
			while (rsResult.next())
			{
				MilestoneInfo milestone = new MilestoneInfo();
				milestone.setID((rsResult.getString("Milestone_ID") == null) ? 0 : rsResult.getInt("Milestone_ID"));
				milestone.setName((rsResult.getString("Name") == null) ? "" : rsResult.getString("Name"));
				milestone.setComplete((rsResult.getString("Complete") == null) ? 0 : rsResult.getInt("Complete"));
				milestone.setBaseFinishDate((rsResult.getString("Base_Finish_date") == null) ? "" : rsResult.getString("Base_Finish_date"));
				milestone.setPlanFinishDate((rsResult.getString("plan_finish_date") == null) ? "" : rsResult.getString("plan_finish_date"));
				milestone.setActualFinishDate((rsResult.getString("actual_finish_date") == null) ? "" : rsResult.getString("actual_finish_date"));
				milestone.setBaseEffort((rsResult.getString("base_effort") == null) ? 0 : rsResult.getInt("base_effort"));
				milestone.setPlanEffort((rsResult.getString("plan_effort") == null) ? 0 : rsResult.getInt("plan_effort"));
				milestone.setActualEffort((rsResult.getString("actual_effort") == null) ? 0 : rsResult.getInt("actual_effort"));
				milestone.setDescription((rsResult.getString("description") == null) ? "" : rsResult.getString("description"));
				
				listResult.add(milestone);
			}
			rsResult.close();
			stmt.close();
			
			System.out.println("MilestoneEJB.getMilestoneList(): listResult.size() = " + listResult.size());
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in MilestoneEJB.getMilestoneList()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in MilestoneEJB.getMilestoneList()." + ex.getMessage());
		}
		finally
		{
			if (rsResult != null) rsResult.close();
			if (stmt != null) stmt.close();
			if (con != null) con.close();
		}
		
		return (Collection)listResult;
	}
	
	public void deleteMilestone(String MilestoneID) throws SQLException
	{
		Statement stmt = null;
		try
			{
			String strSQL = "DELETE FROM Milestone where Milestone_ID in (" + MilestoneID + ")";
			
			if (ds == null)		ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			stmt.executeQuery(strSQL);
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in MilestoneEJB.deleteMilestone(). " + ex.getMessage());
			ex.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println("Exception occurs in MilestoneEJB.deleteMilestone(). " + e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (stmt != null) stmt.close();
			if (con != null) con.close();
		}
	}
		
	public int getMaxPage(int ProjectID) throws SQLException
	{
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try
		{
			String strSQL = "SELECT COUNT(*) FROM Milestone WHERE Project_ID = ?";
			
			if (ds == null)		ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);
			prepStmt.setInt(1, ProjectID);
			rs = prepStmt.executeQuery();
			if (rs.next())
				{
				int max = rs.getInt(1) / 10;
				if (max * 10 < rs.getInt(1))
					{
					return max + 1;
				}
				else
					{
					return max;
				}
			}
			else
				{
				return 0;
			}
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in MilestoneEJB.getMaxPage(). " + ex.getMessage());
			ex.printStackTrace();
			return 0;
		}
		catch (Exception e)
		{
			System.out.println("Exception occurs in MilestoneEJB.getMaxPage(). " + e.toString());
			e.printStackTrace();
			return 0;
		}
		finally
		{
			if (rs != null) 	rs.close();
			if (prepStmt != null)	prepStmt.close();
			if (con != null) con.close();
		}
	}
	public void addMilestone(
		int Project_ID,
		String Name,
		int Complete,
		String Base_start_date,
		String Plan_start_date,
		String Actual_start_date,
		String Base_finish_date,
		String Plan_finish_date,
		String Actual_finish_date,
		int Base_effort,
		int Plan_effort,
		int Actual_effort,
		String description)
		throws SQLException
	{
		Statement stmt = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try
			{
			long milestoneId = conPool.getNextSeq("milestone_seq");
			String strSQL = null;
			if (ds == null)		ds = conPool.getDS();
			con = ds.getConnection();
			strSQL =
				"INSERT INTO Milestone (Milestone_ID, Project_ID, Name, Complete, Base_start_date, Plan_start_date, Actual_start_date, Base_finish_date, Plan_finish_date, Actual_finish_date,Base_effort, Plan_effort,Actual_effort, description)"
					+ " values(?,?,?,?,TO_DATE(?,'dd/mm/yy'),TO_DATE(?,'dd/mm/yy'),TO_DATE(?,'dd/mm/yy'),TO_DATE(?,'dd/mm/yy'),TO_DATE(?,'dd/mm/yy'),TO_DATE(?,'dd/mm/yy'),?,?,?,?)";
			
			prepStmt = con.prepareStatement(strSQL);
			// prepStmt.setInt(1, max_id);
			prepStmt.setLong(1, milestoneId);
			prepStmt.setInt(2, Project_ID);
			prepStmt.setString(3, Name);
			prepStmt.setInt(4, Complete);
			prepStmt.setString(5, Base_start_date);
			prepStmt.setString(6, Plan_start_date);
			prepStmt.setString(7, Actual_start_date);
			prepStmt.setString(8, Base_finish_date);
			prepStmt.setString(9, Plan_finish_date);
			prepStmt.setString(10, Actual_finish_date);
			prepStmt.setInt(11, Base_effort);
			prepStmt.setInt(12, Plan_effort);
			prepStmt.setInt(13, Actual_effort);
			prepStmt.setString(14, description);
			prepStmt.executeQuery();

			rs.close();
			stmt.close();
			prepStmt.close();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			if (rs != null) rs.close();
			if (stmt != null)	stmt.close();
			if (prepStmt != null)	prepStmt.close();
			if (con != null) con.close();
		}
	}
/**
 * method: public MilestoneInfo getMilestone(int MilestoneID) throws SQLException
 * author: Nguyen Thai Son
 * date: August 30, 2002
 * description: this method is replaced for getMilestone() which returns a boolean value. 
 * It 's due to an error "ResultSetProxy is closed" at run-time
 * */		

	public MilestoneInfo getMilestone(int MilestoneID) throws SQLException{
		MilestoneInfo milestone = new MilestoneInfo();
		Statement stmt = null;
		ResultSet rsResult = null;
		try{
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("SELECT Milestone_ID, Name, Complete, TO_CHAR(Base_start_date,'dd/mm/yy') as Base_start_date, TO_CHAR(Plan_start_date,'dd/mm/yy') as Plan_start_date,");
			strSQL.append("TO_CHAR(Actual_start_date,'dd/mm/yy') as Actual_start_date,TO_CHAR(Base_Finish_date,'dd/mm/yy') as Base_Finish_date,TO_CHAR(plan_finish_date,'dd/mm/yy') as plan_finish_date,TO_CHAR(actual_finish_date,'dd/mm/yy') as actual_finish_date,");
			strSQL.append("base_effort,plan_effort, actual_effort, description FROM Milestone ");
			strSQL.append("WHERE Milestone_ID = " + MilestoneID);
			if (ds == null)		ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rsResult = stmt.executeQuery(strSQL.toString());
			if (rsResult.next()){
				milestone.setID(MilestoneID);
				milestone.setName((rsResult.getString("Name") == null) ? "" : rsResult.getString("Name"));
				milestone.setComplete((rsResult.getString("Complete") == null) ? 0 : rsResult.getInt("Complete"));
				milestone.setBaseStartDate((rsResult.getString("Base_start_date") == null) ? "" : rsResult.getString("Base_start_date"));
				milestone.setPlanStartDate((rsResult.getString("Plan_start_date") == null) ? "" : rsResult.getString("Plan_start_date"));
				milestone.setActualStartDate((rsResult.getString("Actual_start_date") == null) ? "" : rsResult.getString("Actual_start_date"));

				milestone.setBaseFinishDate((rsResult.getString("Base_Finish_date") == null) ? "" : rsResult.getString("Base_Finish_date"));
				milestone.setPlanFinishDate((rsResult.getString("plan_finish_date") == null) ? "" : rsResult.getString("plan_finish_date"));
				milestone.setActualFinishDate((rsResult.getString("actual_finish_date") == null) ? "" : rsResult.getString("actual_finish_date"));

				milestone.setBaseEffort((rsResult.getString("base_effort") == null) ? 0 : rsResult.getInt("base_effort"));
				milestone.setPlanEffort((rsResult.getString("plan_effort") == null) ? 0 : rsResult.getInt("plan_effort"));
				milestone.setActualEffort((rsResult.getString("actual_effort") == null) ? 0 : rsResult.getInt("actual_effort"));

				milestone.setDescription((rsResult.getString("description") == null) ? "" : rsResult.getString("description"));
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println("Exception occurs in MilestoneEJB.getMilestone(). " + e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (rsResult != null)		rsResult.close();
			if (stmt != null)	stmt.close();
			if (con != null)	con.close();
		}
		
		return milestone;
	}

	public void updateMilestone(
		int Milestone_ID,
		String Name,
		int Complete,
		String Base_start_date,
		String Plan_start_date,
		String Actual_start_date,
		String Base_finish_date,
		String Plan_finish_date,
		String Actual_finish_date,
		int Base_effort,
		int Plan_effort,
		int Actual_effort,
		String description)
		throws SQLException
	{
		PreparedStatement prepStmt = null;
		
		try
			{
			String strSQL =
				"UPDATE Milestone set Name = ?, Complete = ?,"
					+ "Base_start_date = TO_DATE(?,'dd/mm/yy'), Plan_start_date = TO_DATE(?,'dd/mm/yy'), Actual_start_date = TO_DATE(?,'dd/mm/yy'),"
					+ "Base_finish_date = TO_DATE(?,'dd/mm/yy'), Plan_finish_date = TO_DATE(?,'dd/mm/yy'), Actual_finish_date = TO_DATE(?,'dd/mm/yy'),"
					+ "Base_effort = ?, Plan_effort = ?, Actual_effort = ?, description = ? WHERE Milestone_ID = ?";
			System.out.println(strSQL);
			
			if (ds == null)		ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);
			prepStmt.setString(1, Name);
			prepStmt.setInt(2, Complete);
			prepStmt.setString(3, Base_start_date);
			prepStmt.setString(4, Plan_start_date);
			prepStmt.setString(5, Actual_start_date);
			prepStmt.setString(6, Base_finish_date);
			prepStmt.setString(7, Plan_finish_date);
			prepStmt.setString(8, Actual_finish_date);
			prepStmt.setInt(9, Base_effort);
			prepStmt.setInt(10, Plan_effort);
			prepStmt.setInt(11, Actual_effort);
			prepStmt.setString(12, description);
			prepStmt.setInt(13, Milestone_ID);
			prepStmt.executeQuery();
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			if (prepStmt != null)	prepStmt.close();
			if (con != null)	con.close();
		}
	}
	public MilestoneEJB()
	{
	}
	public void ejbCreate() throws javax.ejb.CreateException {
		try{
			ds = conPool.getDS();
			con = ds.getConnection();
		}
		catch (Exception e){
			throw new javax.ejb.CreateException(e.getMessage());
		}
	}
	public void ejbRemove()
	{
		try{
			if (con != null)	con.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
	public void ejbActivate()
	{
	}
	public void ejbPassivate()
	{
	}
	public void setSessionContext(SessionContext sc)
	{
	}
}