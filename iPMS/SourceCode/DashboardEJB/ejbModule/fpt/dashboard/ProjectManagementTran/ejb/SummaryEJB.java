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
import fpt.dashboard.framework.connection.*;
public class SummaryEJB implements SessionBean
{
	private DataSource ds = null;
	private Connection con = null;
	private ResultSet rs = null;
	private WSConnectionPooling conPool = new WSConnectionPooling();

/**
 * method: public Collection getSummaryReport(int month, int year) throws Exception
 * author: Nguyen Thai Son
 * date: August 20, 2002
 * description: this method is replaced for getSummary() and nextSummary() methods.
 * It 's due to an error "ResultSetProxy is closed" at run-time
 * 13-Mar-06, Trungtn: Change summary page:
 *     - Separate project allocates and non-project allocates
 *     - Doesn't display external viewers
 * */

	public Collection getSummaryReport(int month, int year, String strGroupType) throws Exception
	{
		ArrayList listResult = new ArrayList();
		PreparedStatement prepStmt = null;
		ResultSet rsResult = null;
        String strGroupTypeSql = "";
        if ("1".equals(strGroupType)) {    // Operation group
            strGroupTypeSql = " AND isoperationgroup=1 ";
        }
        else if ("0".equals(strGroupType)) {    // Support group
            strGroupTypeSql = " AND isoperationgroup IS NULL ";
        }

		int numDate = getNumDate(month, year);
		String startPeriod = "01/" + String.valueOf(month) + "/" + String.valueOf(year);
		String endPeriod =
			String.valueOf(numDate)
				+ "/"
				+ String.valueOf(month)
				+ "/"
				+ String.valueOf(year);
		String strSQL = "SELECT group_name, type, COUNT(d.developer_id) AS c FROM ";
		strSQL += "( ";
		strSQL += "SELECT developer_ID, Min(type) AS type FROM ";
		strSQL += "( ";
		strSQL += "(SELECT d.developer_ID as developer_ID, 7 AS type  ";
		strSQL += "FROM Developer d WHERE (d.role<>'0000000000') AND (d.status IN (1,2))) ";
		strSQL += "UNION ";
		strSQL += "( ";
		strSQL += "SELECT d.Developer_ID as developer_ID, Min(a.type) AS type ";
		strSQL += "FROM Developer d, Assignment a ";
		strSQL += "WHERE (d.Developer_ID = a.Developer_ID) AND (d.role<>'0000000000') AND (d.status IN (1,2))";
		strSQL += "AND ((TO_DATE(? ,'dd/mm/yy' ) between a.begin_date and a.end_date )" +
                        " or (a.begin_date between TO_DATE(? ,'dd/mm/yy') and TO_DATE(? ,'dd/mm/yy'))) ";
		//strSQL += "AND (TO_CHAR(a.begin_date,'MM') <= ?) ";
		//strSQL += "AND (TO_CHAR(a.end_date,'MM') >= ?) ";
		//strSQL += "AND (TO_CHAR(a.end_date,'YY') = " + year + ") ";
		strSQL += " GROUP BY d.developer_id) ";
		strSQL += "UNION ";
		strSQL += "( ";
		// Separate non-project allocates (other assignments) with project allocates:
        // If assigned from Other Assignment then transport to new type (2-Allocated to 7-Other Allocated)
        strSQL += "SELECT d.Developer_ID as developer_ID," +
                    "Min(CASE WHEN o.type=2 THEN 6 ELSE o.type END) AS type ";
		strSQL += "FROM Developer d, Other_Assignment o ";
		strSQL += "WHERE (d.Developer_ID = o.Developer_ID)AND (d.role<>'0000000000') AND (d.status IN (1,2)) ";
		strSQL += "AND ((TO_DATE(? ,'dd/mm/yy' ) between o.from_date and o.end_date )" +
                        " or (o.from_date between TO_DATE(? ,'dd/mm/yy') and TO_DATE(? ,'dd/mm/yy'))) ";
		//strSQL += "AND (TO_CHAR(o.from_date,'MM') <= ?) ";
		//strSQL += "AND (TO_CHAR(o.end_date,'MM') >= ?) ";
		//strSQL += "AND (TO_CHAR(o.end_date,'YY') = " + year + ") ";
		strSQL += "GROUP BY d.developer_id) ";
		strSQL += ") ";
		//strSQL += "GROUP BY developer_ID, group_name ";
        strSQL += " GROUP BY developer_id) Summary, developer d, Groups G";
        strSQL += " WHERE Summary.developer_id=d.developer_id AND d.group_name=g.groupname";
        strSQL += strGroupTypeSql;
		strSQL += " GROUP BY group_name, type ";
		strSQL += " ORDER BY group_name, type ";
		try
		{
			System.out.println("SummaryEJB.getSummaryReport(): startPeriod = " + startPeriod + "   endPeriod = " + endPeriod);
			System.out.println("SummaryEJB.getSummaryReport(): strSQL = " + strSQL);

			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);
			prepStmt.setString(1, startPeriod);
			prepStmt.setString(2, startPeriod);
			prepStmt.setString(3, endPeriod);
			prepStmt.setString(4, startPeriod);
			prepStmt.setString(5, startPeriod);
			prepStmt.setString(6, endPeriod);
			rsResult = prepStmt.executeQuery();

			while (rsResult.next())
			{
				SummaryInfo summary = new SummaryInfo();

				summary.setGroupName(rsResult.getString("group_name"));
				summary.setType((rsResult.getString("type") == null) ? 0 : rsResult.getInt("type"));
				summary.setCount((rsResult.getString("c") == null) ? 0 : rsResult.getInt("c"));

				listResult.add(summary);
			}
			rsResult.close();
			prepStmt.close();

			System.out.println("SummaryEJB.getSummaryReport(): listResult.size() = " + listResult.size());
		}
		catch (SQLException ex)
		{
			System.out.println("SQLException occurs in SummaryEJB.getSummaryReport(). " + ex.getMessage());
			throw new SQLException("SummaryEJB.getSummaryReport(): " + ex.toString());
		}
		catch (Exception e)
		{
			throw new Exception("SummaryEJB.getSummaryReport(): " + e.toString());
		}
		finally
		{
			if (rsResult != null)		rsResult.close();
			if (prepStmt != null)	prepStmt.close();
			if (con != null)			con.close();
            return (Collection) listResult;
		}
	}
	public String getGroup()
	{
		try
			{
			return rs.getString("group_name");
		}
		catch (Exception e)
			{
			return "";
		}
	}
	public int getCount()
	{
		try
			{
			return rs.getInt("c");
		}
		catch (Exception e)
			{
			return 0;
		}
	}
	public int getType()
	{
		try
			{
			return rs.getInt("type");
		}
		catch (Exception e)
			{
			return 0;
		}
	}
	public SummaryEJB()
	{
	}
	public void ejbCreate() throws CreateException
	{
		try {
			ds = conPool.getDS();
			con = ds.getConnection();
		} catch (Exception e) {
			throw new CreateException(e.getMessage());
		}
	}
	public void ejbRemove()
	{
		try
		{
			if (con != null) con.close();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	public void ejbActivate() throws RemoteException
	{
		try
		{
			ds = conPool.getDS();
			con = ds.getConnection();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	public void ejbPassivate()
	{
		try
		{
			if (con != null) con.close();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	public void setSessionContext(SessionContext sc)
	{
	}
	public int getNumDate(int nMonth, int nYear)
	{
		if (nMonth == 4 || nMonth == 6 || nMonth == 9 || nMonth == 11)
			{
			return 30;
		}
		if (nMonth == 2)
			{
			// leap year
			if ((nYear % 4 == 0) && (nYear % 100 != 0))
				{
				return 29;
			}
			else
				return 28;
		}
		return 31;
	}
}