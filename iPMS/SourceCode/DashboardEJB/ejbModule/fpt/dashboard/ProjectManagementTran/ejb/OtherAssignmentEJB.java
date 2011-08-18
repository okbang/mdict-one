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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import fpt.dashboard.constant.*;
import fpt.dashboard.framework.connection.WSConnectionPooling;
public class OtherAssignmentEJB implements SessionBean
{
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	private Connection con = null;

	public void ejbCreate() throws CreateException
	{
		try
		{
			makeConnection();
		}
		catch (Exception e)
		{
			throw new CreateException(e.getMessage());
		}
	} //enf of ejbCreate method
	public void ejbRemove()
	{
		try
		{
			if (con != null) con.close();
		}
		catch (Exception ex)
		{
			throw new EJBException(ex.getMessage());
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
			throw new EJBException(e.getMessage());
		}
	}
	public void ejbPassivate()
	{
		try
		{
			if (con != null) con.close();
		}
		catch (Exception ex)
		{
			throw new EJBException(ex.getMessage());
		}
	} //end of ejbPassivate method
	public void setSessionContext(SessionContext rc)
	{

	}
	private void makeConnection() throws NamingException, SQLException
	{
		if (con == null)
		{
			try
			{
				System.out.println("OtherAssignmentEJB.makeConnection(): get JNDI connection.");
				ds = conPool.getDS();
				con = ds.getConnection();
			}
			catch (Exception e)
				{
				System.out.println("Exception in OtherAssignmentEJB: makeConnection(): " + e.toString());
			}
		}
		else
		{
			System.out.println("connection exists");
		}
	}
	public void addAssignment(
		int Developer_ID,
		String From_date,
		String To_date,
		int Type,
		String Description,
		String usage)
		throws SQLException
	{
		String chuoi = "";
		PreparedStatement prepStmt = null;

		try
			{
			chuoi =
				"Insert into Other_Assignment(Developer_ID, From_date, End_date,Type, Description,usage ) Values(?,TO_DATE(?, 'dd/mm/yy'),TO_DATE(?, 'dd/mm/yy'), ?,?,?)";

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(chuoi);
			prepStmt.setInt(1, Developer_ID);
			prepStmt.setString(2, From_date);
			prepStmt.setString(3, To_date);
			prepStmt.setInt(4, Type);
			prepStmt.setString(5, Description);
			prepStmt.setInt(6, Integer.parseInt(usage));
			prepStmt.executeQuery();
			prepStmt.close();
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in OtherAssignmentEJB.addAssignment()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in OtherAssignmentEJB.addAssignment()." + ex.getMessage());
		}
		finally
		{
			if (prepStmt != null) prepStmt.close();
			if (con != null)	con.close();
		}
	}
	public Collection getDeveloper() throws SQLException
	{
		Collection Cl = (Collection) new ArrayList();
		String chuoi = "";
		String temp = "";
		Integer so;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			chuoi = "select Developer_id, name from developer order by name";

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(chuoi);
			while (rs.next())
			{
				temp = "";
				so = new Integer(rs.getInt(1));
				Cl.add(so);
				temp = rs.getString(2);
				Cl.add(temp);
			}

			rs.close();
			stmt.close();
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in OtherAssignmentEJB.getDeveloper()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in OtherAssignmentEJB.getDeveloper()." + ex.getMessage());
		}
		finally
		{
			if (rs != null)	rs.close();
			if (stmt != null) stmt.close();
			if (con != null)	con.close();
		}
		return Cl;
	} //end of method
	public Collection getList() throws SQLException
	{
		// this method return a List other_assignment record
		Collection cl = (Collection) new ArrayList();
		String sql = "";
		Integer number;

		Statement stmt = null;
		ResultSet rs = null;
		try
			{
			sql =
				"select other_assignment.OA_ID, developer.Name, TO_Char(other_assignment.From_date,'dd/mm/yy'), To_char(other_assignment.End_date,'dd/mm/yy'), other_assignment.Type,other_assignment.usage from other_assignment, developer where developer.Developer_ID=other_assignment.Developer_ID";

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				number = new Integer(rs.getInt(1));
				cl.add(number);
				cl.add(rs.getString(2));
				cl.add(rs.getString(3));
				cl.add(rs.getString(4));
				number = new Integer(rs.getInt(5));
				cl.add(number);
			}

			rs.close();
			stmt.close();
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in OtherAssignmentEJB.getList()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in OtherAssignmentEJB.getList()." + ex.getMessage());
		}
		finally
		{
			if (rs != null)	rs.close();
			if (stmt != null) stmt.close();
			if (con != null)	con.close();
		}
		return cl;
	}
	public void delete(String so) throws SQLException
	{
		Integer so_ng;
		int so_int = 0;
		PreparedStatement pre = null;

		try
			{
			String sql = "delete from other_assignment where OA_ID=?";

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			pre = con.prepareStatement(sql);
			so_ng = new Integer(so);
			so_int = so_ng.intValue();
			pre.setInt(1, so_int);
			pre.executeUpdate();
			pre.close();
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in OtherAssignmentEJB.delete()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in OtherAssignmentEJB.delete()." + ex.getMessage());
		}
		finally
		{
			if (pre != null) pre.close();
			if (con != null)	con.close();
		}
	}
	public Collection getGroup() throws SQLException
	{
		Collection cl = (Collection) new ArrayList();
		String sql = "select groupname FROM groups,workunit" +
            " where groups.group_id=workunit.tableid" +
            " and workunit.type=" + DB.WORKUNIT_TYPE_GROUP +
            " and workunit.parentworkunitid=" + DB.WORKUNIT_FSOFT +
            " ORDER BY groupname";
		String group = "";

		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				group = rs.getString(1);
				cl.add(group);
			}
			stmt.close();
			rs.close();
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in OtherAssignmentEJB.getGroup()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in OtherAssignmentEJB.getGroup()." + ex.getMessage());
		}
		finally
		{
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (con != null)	con.close();
		}
		return cl;
	}
	public Collection getListnew(String Group, String From_date, String To_date)
		throws SQLException
	{
		Collection cl = (Collection) new ArrayList();
		String sql = "";
		Integer number;
		PreparedStatement preStm = null;
		ResultSet rs = null;

		try
		{

			if (!Group.equals(Constants.ALL))
				{
				sql =
					"select other_assignment.OA_ID, developer.Name,"
						+ " TO_Char(other_assignment.From_date,'dd/mm/yy'),"
						+ " To_char(other_assignment.End_date,'dd/mm/yy'),"
						+ " other_assignment.Type,other_assignment.usage,other_assignment.description"
						+ " from other_assignment, developer"
						+ " where developer.Developer_ID=other_assignment.Developer_ID"
						+ " AND (developer.group_name=?)"
						+ " AND"
						+ "("
						+ "("
						+ "(other_assignment.from_date>=To_date(?,'dd/mm/yy'))"
						+ "AND"
						+ "(other_assignment.from_date<=To_date(?,'dd/mm/yy'))"
						+ ")"
						+ "OR"
						+ "("
						+ "(other_assignment.end_date>=To_date(?,'dd/mm/yy'))"
						+ "AND "
						+ "(other_assignment.end_date<= To_date(?,'dd/mm/yy'))"
						+ ")"
						+ "OR"
						+ "("
						+ "(other_assignment.from_date<=To_date(?,'dd/mm/yy'))"
						+ "AND "
						+ "(other_assignment.end_date>= To_date(?,'dd/mm/yy'))"
						+ ")"
						+ ") ORDER BY  other_assignment.From_date";

				if (ds == null) ds = conPool.getDS();
				con = ds.getConnection();
				preStm = con.prepareStatement(sql);
				preStm.setString(1, Group);
				preStm.setString(2, From_date);
				preStm.setString(3, To_date);
				preStm.setString(4, From_date);
				preStm.setString(5, To_date);
				preStm.setString(6, From_date);
				preStm.setString(7, To_date);
			}
			else
				{
				sql =
					"select other_assignment.OA_ID, developer.Name,"
						+ " TO_Char(other_assignment.From_date,'dd/mm/yy') ,"
						+ " To_char(other_assignment.End_date,'dd/mm/yy'),"
						+ " other_assignment.Type,other_assignment.usage,other_assignment.description"
						+ " from other_assignment, developer"
						+ " where developer.Developer_ID=other_assignment.Developer_ID"
						+ " AND"
						+ "("
						+ "("
						+ "(other_assignment.from_date>=To_date(?,'dd/mm/yy'))"
						+ "AND"
						+ "(other_assignment.from_date<=To_date(?,'dd/mm/yy'))"
						+ ")"
						+ "OR"
						+ "("
						+ "(other_assignment.end_date>=To_date(?,'dd/mm/yy'))"
						+ "AND "
						+ "(other_assignment.end_date<= To_date(?,'dd/mm/yy'))"
						+ ")"
						+ "OR"
						+ "("
						+ "(other_assignment.from_date<=To_date(?,'dd/mm/yy'))"
						+ "AND "
						+ "(other_assignment.end_date>= To_date(?,'dd/mm/yy'))"
						+ ")"
						+ ") ORDER BY  other_assignment.From_date";

				if (ds == null) ds = conPool.getDS();
				con = ds.getConnection();
				preStm = con.prepareStatement(sql);

				preStm.setString(1, From_date);
				preStm.setString(2, To_date);
				preStm.setString(3, From_date);
				preStm.setString(4, To_date);
				preStm.setString(5, From_date);
				preStm.setString(6, To_date);
			} //else if

			rs = preStm.executeQuery();
			while (rs.next())
				{
				number = new Integer(rs.getInt(1));
				cl.add(number);
				cl.add(rs.getString(2));
				cl.add(rs.getString(3));
				cl.add(rs.getString(4));
				number = new Integer(rs.getInt(5));
				cl.add(number);
				cl.add(String.valueOf(rs.getInt(6)));
				cl.add(rs.getString(7));
			}

			rs.close();
			preStm.close();
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in OtherAssignmentEJB.getListnew()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in OtherAssignmentEJB.getListnew()." + ex.getMessage());
		}
		finally
		{
			if (rs != null) rs.close();
			if (preStm != null) preStm.close();
			if (con != null)	con.close();
		}
		return cl;
	}
	
	//ThaiLH
	public Collection getOffTime(String strID) throws SQLException{

		Collection listResult = (Collection) new ArrayList();
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String strSQL = "select TO_Char(other_assignment.From_date,'dd/mm/yy'),"
				+ " TO_Char(other_assignment.End_date,'dd/mm/yy') "
				+ " from other_assignment "
				+ " where developer_id = '"
				+ strID
				+ "' and type = '5'";
		try{
			
			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			preStm = con.prepareStatement(strSQL);
	
			rs = preStm.executeQuery();
			while (rs.next())
			{
				listResult.add(rs.getString(1));
				listResult.add(rs.getString(2));
			}
			rs.close();
			preStm.close();
		}	
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in OtherAssignmentEJB.getOffTime()." + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurs in OtherAssignmentEJB.getOffTime()." + ex.getMessage());
		}
		finally
		{
			if (rs != null) rs.close();
			if (preStm != null) preStm.close();
			if (con != null)	con.close();
		}
		return (Collection) listResult;		
	}
	//EndOfThaiLH
} //end of class