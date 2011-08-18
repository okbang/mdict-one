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


import java.rmi.RemoteException;
import java.sql.*;
import javax.sql.*;
import java.util.*;

import javax.ejb.*;
import javax.naming.*;

import fpt.dashboard.constant.DB;
import fpt.dashboard.framework.connection.*;


public class AssignmentEJB implements SessionBean 
{
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	private Connection con = null;

	public AssignmentEJB() 
	{
	}

	public void ejbCreate() throws CreateException
	{
		try {
			makeConnection();
		} catch (Exception e) {
			throw new CreateException(e.getMessage());
		}
	}

    public void ejbRemove() 
    {
        try
        {
             if (con != null)   con.close();
        } catch (Exception e) 
        {
            System.out.println("AssignmentEJB.ejbRemove() error: " + e.toString());
        }

    }
    public void ejbActivate()
    {
        try
        {
            makeConnection();
        }
        catch (Exception e)
        {
            throw new EJBException ("Could not close connection. "+e.toString());
        }
    }
    public void ejbPassivate()
    {
        try
        {
             if (con != null)   con.close();
        }
        catch (Exception e)
        {
            throw new EJBException ("Could not close connection. "+e.toString());
        }
    }
    public void setSessionContext(SessionContext sc) {  }

	private void makeConnection() throws SQLException
	{
		try
		{
		      ds = conPool.getDS();
		      con = ds.getConnection();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
            e.printStackTrace();
		}
	}

/**
 * method: public Collection getAssignmentList(int DeveloperID,String dt_Start,String dt_Finish) throws SQLException
 * author: Nguyen Thai Son
 * date: August 21, 2002
 * description: this method is replaced for getlist() and nextAssignment() methods. 
 * It 's due to an error "ResultSetProxy is closed" at run-time
 * */	
	public Collection getAssignmentList(int DeveloperID,String dt_Start,String dt_Finish) throws SQLException
	{
		ArrayList listResult = new ArrayList();
		PreparedStatement prepStmt = null;
		ResultSet rsResult = null;
		try
		{
			String strSQL = "SELECT a.Project_ID as ProjectID, d.Name as developername, p.Name as projectName, TO_CHAR(a.begin_date,'dd/mm/yy') AS begin_date, TO_CHAR(a.end_date,'dd/mm/yy') As end_date,a.usage " +
							"FROM Project p, Developer d, Assignment a " +
							"WHERE a.Project_ID = p.Project_ID " +
							"AND a.Developer_ID = ? " +
							"AND a.Developer_ID = d.Developer_ID " +
							"AND ((TO_DATE(? ,'dd/mm/yyyy' ) between a.begin_date and a.end_date ) or (a.begin_date between TO_DATE(? ,'dd/mm/yyyy') and TO_DATE(? ,'dd/mm/yyyy'))) "+
							"UNION " + 
							"SELECT a.developer_id,d.name AS developername,'Other Assignment' AS projectname, " + 
							"TO_CHAR (a.from_date,'dd/mm/yyyy') AS begin_date, " + 
							"TO_CHAR (a.end_date,'dd/mm/yyyy') AS end_date, a.usage " + 
							"FROM other_assignment a, developer d " + 
							"where d.developer_id = a.developer_id " + 
			  				"AND a.developer_id = ? " +
							"AND ((TO_DATE(? ,'dd/mm/yyyy' ) between a.from_date and a.end_date ) or (a.from_date between TO_DATE(? ,'dd/mm/yyyy') and TO_DATE(? ,'dd/mm/yyyy'))) ";
			
			if (ds == null)		ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);
			prepStmt.setInt(1, DeveloperID);
			prepStmt.setString(2, dt_Start);
			prepStmt.setString(3, dt_Start);
            prepStmt.setString(4, dt_Finish);
			prepStmt.setInt(5, DeveloperID);
			prepStmt.setString(6, dt_Start);
			prepStmt.setString(7, dt_Start);
			prepStmt.setString(8, dt_Finish);
			rsResult = prepStmt.executeQuery();
			while (rsResult.next())
			{
				AssignmentInfo assign = new AssignmentInfo();
				assign.setProjectID((rsResult.getString("ProjectID") == null) ? 0 : rsResult.getInt("ProjectID"));
				assign.setProjectName((rsResult.getString("projectName") == null) ? "" : rsResult.getString("projectName"));
				assign.setDeveloperName((rsResult.getString("developername") == null) ? "" : rsResult.getString("developername"));
				assign.setBeginDate((rsResult.getString("begin_date") == null) ? "" : rsResult.getString("begin_date"));
				assign.setEndDate((rsResult.getString("end_date") == null) ? "" : rsResult.getString("end_date"));
				assign.setUsage((rsResult.getString("usage") == null) ? 0 : rsResult.getInt("usage"));
				
				listResult.add(assign);
			}
			
			System.out.println("AssignmentEJB.getAssignmentList(): listResult.size() = " + listResult.size());			
		}
		catch (SQLException ex)
			{
			System.out.println(
				"SQLException occurs in AssignmentEJB.getAssignmentList(). " + ex.getMessage());
			ex.printStackTrace();
		}
		catch (Exception ex)
			{
			System.out.println(
				"Exception occurs in AssignmentEJB.getAssignmentList(). " + ex.getMessage());
			ex.printStackTrace();
		}
		finally
		{
			if (rsResult != null) rsResult.close();
			if (prepStmt != null)	prepStmt.close();
			if (con != null)	con.close();
		}
		return (Collection)listResult;
	}
	
    /**
     * Select assignments of a group in a time period
     * @param n_FromMonth
     * @param n_FromYear
     * @param n_ToMonth
     * @param n_ToYear
     * @param strGroupName
     * @return
     * @throws SQLException
     */
    public Collection getAssignmentList(Calendar cal_From, Calendar cal_To,
                                        String strGroupName) throws SQLException
    {
        ArrayList arrResult = new ArrayList();
        String strFrom = (cal_From.get(Calendar.MONTH) + 1) + "/" + cal_From.get(Calendar.YEAR);
        String strTo = (cal_To.get(Calendar.MONTH) + 1) + "/" + cal_To.get(Calendar.YEAR);
        
        String selectStatement =
           "select A.assignment_id, A.project_id, A.developer_id, A.type, A.begin_date, A.end_date, A.usage," +
                "P.code AS project_code, D.account AS developer_account, d.name AS developer_name" +
                " from assignment A, developer D, project P" +
                " where A.project_id = P.project_id AND A.developer_id = D.developer_id" +
                " AND not((P.code like 'Daily_G%') and (P.group_name like 'G%'))" +
                " AND A.begin_date <= To_date(?, 'mm/yy') AND A.end_date >= To_date(?, 'mm/yy')" +
                " AND D.status<>3 " + " AND D.status<>4 ";
        if (!strGroupName.equals(Constants.ALL)) {
            selectStatement += //" AND P.group_name = '" + strGroupName + "'" +
                               " AND D.group_name = '" + strGroupName + "'";
        }
        selectStatement += " ORDER BY A.project_id, A.type DESC, A.developer_id";
        
        if (DB.DEBUG) {
            System.out.println(
                "getAssignmentList - selectStatement:" + selectStatement);
        }
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(selectStatement);
            prepStmt.setString(1, strTo);
            prepStmt.setString(2, strFrom);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                AssignmentInfo assignment = new AssignmentInfo();
                assignment.setProjectID(rs.getInt("project_id"));
                assignment.setProjectName(rs.getString("project_code"));
                assignment.setDeveloperID(rs.getInt("developer_id"));
                assignment.setDeveloperAccount(rs.getString("developer_account"));
                assignment.setDeveloperName(rs.getString("developer_name"));
                assignment.setType(rs.getInt("type"));
                assignment.setBeginCalendar(rs.getDate("begin_date"));
                assignment.setEndCalendar(rs.getDate("end_date"));
                assignment.setUsage(rs.getInt("usage"));
                arrResult.add(assignment);
            }
            rs.close();
            prepStmt.close();
        }
        catch (SQLException ex) {
            System.out.println(
                "SQLException occurs in AssignEJB.selectAssignments(). "
                    + ex.getMessage());
            ex.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println(
                "Exception occurs in AssignEJB.selectAssignments(). "
                    + ex.getMessage());
            ex.printStackTrace();
        }
        finally {
            if (rs != null)
                rs.close();
            if (prepStmt != null)
                prepStmt.close();
            if (con != null)
                con.close();
        }
        return arrResult;
    }
}