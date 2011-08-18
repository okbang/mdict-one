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
 
 /**
 *
 * Copyright 2001 FPT. All Rights Reserved.
 * Author: Duong Thanh Nhan
 *  Updated: Nguyen Thai Son
 *
 */
package fpt.dashboard.DeveloperManagementTran.ejb;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import fpt.dashboard.framework.connection.*;
import fpt.dashboard.framework.util.SqlUtil;
import fpt.dashboard.InfoClass.DeveloperInfo;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.constant.*;

public class DeveloperEJB implements SessionBean {
	private Integer id;
	private String Name;
	private String Design;
	private String Groups;
	private String Role = "0000000000";
    private String Pass;
    private String Email;
	private String Acc;
    private String strStatus;
    private String strStartDate;
    private String strQuitDate;

	private SessionContext context;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	private Connection con = null;

	public DeveloperEJB() {
	}

	public String getName() {
		return Name;
	}
	public String getDesign() {
		return Design;
	}
	public String getGroup() {
		return Groups;
	}
	public String getRole() {
		return Role;
	}
    public String getPass() {
        return Pass;
    }
    public String getEmail() {
        return Email;
    }
	public String getAcc() {
		return Acc;
	}
    public String getStatus() {
        return this.strStatus;
    }
    public String getStartDate() {
        return this.strStartDate;
    }
    public String getQuitDate() {
        return this.strQuitDate;
    }

	public void setName(String name) {
		this.Name = name;
	}
	public void setDesign(String design) {
		this.Design = design;
	}
	public void setGroup(String group) {
		this.Groups = group;
	}
    public void setPass(String pass) {
        this.Pass = pass;
    }
    public void setEmail(String email) {
        this.Email = email;
    }
	public void setAcc(String acc) {
		this.Acc = acc;
	}
	public void setRole(String role) {
		this.Role = role;
	}
    public void setStatus(String status) {
        this.strStatus = status;
    }
    public void setStartDate(String strDate) {
        this.strStartDate = strDate;
    }
    public void setQuitDate(String strDate) {
        this.strQuitDate = strDate;
    }

	public void ejbCreate() throws CreateException {
		try {
			makeConnection();
		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	public void ejbRemove() {
		System.out.println("DeveloperEJB.ejbRemove() called.");
		try {
			if (con != null)
				con.close();
		} catch (Exception ex) {
			throw new EJBException(
				"ejbRemove SQLException: " + ex.getMessage());
		}
	}
	public void setSessionContext(SessionContext context) {
		this.context = context;
	}
	public void ejbActivate() {
		try {
			makeConnection();
		} catch (Exception ex) {
			throw new EJBException("ejbActivate Exception: " + ex.getMessage());
		}
	}
	public void ejbPassivate() {
		try {
			if (con != null)
				con.close();
		} catch (SQLException ex) {
			throw new EJBException(
				"ejbPassivate Exception: " + ex.getMessage());
		}
	}
	/*********************** Database Routines *************************/
	private void makeConnection() throws NamingException, SQLException {
		System.out.println("DeveloperEJB.makeConnection() called.");
		ds = conPool.getDS();
		con = ds.getConnection();
	}
	public int insertRow(String name,
                        String design,
                        String group,
                        String role,
                        String pass,
                        String acc,
                        String strStatus,
                        String strEmail,
                        String strStartDate)
                        throws SQLException {
		String insertStatement =
			"insert into developer(name, designation,group_name,role,password,account,status,email,begin_date)" +
            " values(?,?,?,?,?,?,?,?,to_date(?,'dd/mm/yy'))";
		PreparedStatement prepStmt = null;
		int retVal = 0;
		try {
//			ArrayList arrDevList = getDeveloperList();
//			if (arrDevList.indexOf(acc.toUpperCase()) != -1) {
//				System.out.println("Account already exists");
//				return -1;
//			}
            con = ds.getConnection();
			prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setString(1, name);
			prepStmt.setString(2, design);
			prepStmt.setString(3, group);
			prepStmt.setString(4, role);
			prepStmt.setString(5, pass);
			prepStmt.setString(6, acc);
            prepStmt.setString(7, strStatus);
            prepStmt.setString(8, strEmail);
            prepStmt.setString(9, strStartDate);

			retVal = prepStmt.executeUpdate();
		} catch (SQLException ex) {
			System.out.println(
				"SQLException occurs in DeveloperEJB.insertRow()."
					+ ex.getMessage());
			throw ex;
		} finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
			return retVal;
		}
	}
	public void deleteRow(Integer id) throws SQLException {
		String deleteStatement =
			"delete from developer where developer_id = ? ";
		PreparedStatement prepStmt = null;
		try {
			con = ds.getConnection();
			prepStmt = con.prepareStatement(deleteStatement);
			prepStmt.setInt(1, id.intValue());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException ex) {
			System.out.println(
				"SQLException occurs in DeveloperEJB.insertRow()."
					+ ex.getMessage());
			throw ex;
		} finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}
	public void loadRow(Integer key) throws SQLException {
		String selectStatement =
			"select name,designation,group_name,role,password,account, status,email," +
            " to_char(begin_date,'dd/mm/yy'),to_char(quit_date,'dd/mm/yy')"
				+ " from developer where developer_id = ? ";

		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			//          System.out.println("DeveloperEJB.loadRow(): connection is closed = " + con.isClosed());
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, key.intValue());
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				this.Name = rs.getString(1);
				this.Design = rs.getString(2);
				this.Groups = rs.getString(3);
				this.Role = rs.getString(4);
				this.Pass = rs.getString(5);
				this.Acc = rs.getString(6);
                this.strStatus = rs.getString(7);
                this.Email = rs.getString(8);
                this.strStartDate = rs.getString(9);
                this.strQuitDate = rs.getString(10);

				this.id = key;
				rs.close();
				prepStmt.close();
			} else {
				prepStmt.close();
				throw new NoSuchEntityException(
					"Row for id " + id + " not found in database.");
			}
		} catch (SQLException ex) {
			System.out.println(
				"SQLException occurs in DeveloperEJB.loadRow()."
					+ ex.getMessage());
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}
	public int storeRow(Integer key) throws SQLException {
		String updateStatement =
			"update developer set name      =  ? ,"
				+ "designation  =  ? , "
				+ "group_name   =  ? , "
				+ "role         =  ? , "
				+ "password     =  ? , "
				+ "account      =  ? , "
                + "status       =  ? , "
                + "email        =  ? , "
                + "begin_date   =  to_date(?,'dd/mm/yy') , "
                + "quit_date    =  to_date(?,'dd/mm/yy')   "
				+ "where developer_id = ?";
		PreparedStatement prepStmt = null;
        int nUpdated = -1;
		try {
			con = ds.getConnection();
			prepStmt = con.prepareStatement(updateStatement);

			prepStmt.setString(1, Name);
			prepStmt.setString(2, Design);
			prepStmt.setString(3, Groups);
			prepStmt.setString(4, Role);
			prepStmt.setString(5, Pass);
			prepStmt.setString(6, Acc);
            prepStmt.setString(7, strStatus);
            prepStmt.setString(8, Email);
            prepStmt.setString(9, strStartDate);
            prepStmt.setString(10, strQuitDate);
            prepStmt.setInt(11, key.intValue());

            nUpdated = prepStmt.executeUpdate();
			prepStmt.close();
			if (nUpdated == 0) {
				throw new EJBException("Storing row for id " + id + " failed.");
			}
		} catch (SQLException ex) {
			System.out.println(
				"SQLException occurs in DeveloperEJB.storeRow()."
					+ ex.getMessage());
			throw ex;
		}
        finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
            return nUpdated;
        }
	}

	/**
	 * Method: public Collection selectAllKey(String condi, String strStatus) throws SQLException
	 * Author: Nguyen Thai Son
	 * Date: September 05, 2002
	 * Description: Add status field to query constraint
	 * */
	public Collection selectAllKey(String strGroup, String strStatus, String strSortBy, String strDirection)
		throws SQLException {
		ArrayList alSelectedID = new ArrayList();
		StringBuffer strSQL = new StringBuffer();

		strSQL.append("SELECT developer_id FROM Developer");
        strSQL.append(" WHERE 1=1");
        if (!Constants.GROUP_ALL.equals(strGroup)) {
            strSQL.append(" AND upper(group_name) = '" + strGroup.toUpperCase() + "'");
        }
        
        // Not selected all status
        // Not selected non-outplaced users (staff/collaborator/external)
        // Not selected working users (staff/collaborator)
        if ((!Constants.VALUE_STATUS_ALL.equals(strStatus)) &&
            (!"-4".equals(strStatus)) &&
            (!Constants.VALUE_STATUS_WORKING.equals(strStatus)))
        {
                strSQL.append(" AND status = " + strStatus);
        }
        // select all except outplaced
        else if ("-4".equals(strStatus)) {
            strSQL.append(" AND status <> 4");
        }
        // staff/collaborator
        else if (Constants.VALUE_STATUS_WORKING.equals(strStatus)) {
            strSQL.append(" AND status IN (").append(Constants.VALUE_STATUS_WORKING).append(")");
        }
        
        //Display sorted lists
        String strAscDesc = "";
        if ((strDirection != null) && (strDirection.length() > 0)) {
            if (strDirection.equals("0")) {
                strAscDesc = " DESC";
            }
            else if (strDirection.equals("1")) {
                strAscDesc = " ASC";
            }
        }
        if ((strSortBy != null) && (! "".equals(strSortBy))) {
            strSQL.append(" ORDER BY ").append(strSortBy).append(strAscDesc);
            if (strSortBy.equalsIgnoreCase("Group_Name")) {
                strSQL.append(",account");
            }
            else if (strSortBy.equalsIgnoreCase("Status") || strSortBy.equalsIgnoreCase("Designation")) {
                strSQL.append(",group_name,account");
            }
        }
        // Default sort by account
        else {
            strSQL.append(" ORDER BY account");
        }
        
        if (DB.DEBUG) {
            System.out.println(
                "DeveloperEJB.selectAllKey(): strSQL = " + strSQL.toString());
        }

		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL.toString());
			while (rs.next()) {
				Integer tid = new Integer(rs.getInt(1));
				alSelectedID.add(tid);
			}
			rs.close();
			stmt.close();
			System.out.println(
				"DeveloperEJB.selectAllKey(): alSelectedID.size() = "
					+ alSelectedID.size());
		} catch (SQLException ex) {
			System.out.println(
				"SQLException occurs in DeveloperEJB.selectAllKey()."
					+ ex.getMessage());
			throw ex;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (Exception ex) {
				System.out.println(
					"Exception occurs in DeveloperEJB.selectAllKey(): "
						+ ex.toString());
				ex.printStackTrace();
			}
		}
		return alSelectedID;
	}
	public Collection selectGroup() throws SQLException {
		ArrayList listResult = new ArrayList();
		String selectStatement = "select groupname FROM groups,workunit" +
            " where groups.group_id=workunit.tableid" +
            " and workunit.type=? and workunit.parentworkunitid=?" +
            " ORDER BY groupname";
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
            prepStmt.setInt(1, DB.WORKUNIT_TYPE_GROUP);
            prepStmt.setInt(2, DB.WORKUNIT_FSOFT);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				String gname = new String(rs.getString(1));
				listResult.add(gname);
			}
//			System.out.println(
//				"DeveloperEJB.selectGroup(): listResult.size() = "
//					+ listResult.size());
			rs.close();
			prepStmt.close();
		} // end try
		catch (SQLException ex) {
			System.out.println(
				"SQLException occurs in DeveloperEJB.selectGroup()."
					+ ex.getMessage());
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
		return listResult;
	}
    public int countDev(String strGroup, String strStatus)
        throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();
        int retVal = 0;

        strSQL.append("SELECT COUNT(developer_id) FROM Developer");

        if ((!Constants.ALL.equals(strGroup)) && (!"0".equals(strStatus))) {
            strSQL.append(
                " WHERE upper(group_name) = '" + strGroup.toUpperCase() + "'");
            strSQL.append(" AND status = '" + strStatus.toUpperCase() + "'");
            strSQL.append(" AND role<>'0000000000'");
        } else if (!Constants.ALL.equals(strGroup)) {
            strSQL.append(
                " WHERE upper(group_name) = '" + strGroup.toUpperCase() + "'");
            strSQL.append(" AND role<>'0000000000'");
        } else if (!"0".equals(strStatus)) { //not all status
            strSQL.append(" WHERE status = '" + strStatus + "'");
            strSQL.append(" AND role<>'0000000000'");
        }
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL.toString());
            while (rs.next()) {
                retVal = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(
                "SQLException occurs in DeveloperEJB.countDev()."
                    + ex.getMessage());
            throw ex;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                System.out.println(
                    "Exception occurs in DeveloperEJB.countDev(): "
                        + ex.toString());
                ex.printStackTrace();
            }
        }
        return retVal;
    }
    
    /*
     * Get number of active developers of a group
     * @param strGroup
     * @param strStatus
     * @return
     * @throws SQLException
    public int countDevelopers(String strGroup) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();
        int retVal = 0;

        strSQL.append("SELECT COUNT(developer_id) FROM Developer");
        strSQL.append(" WHERE upper(group_name) = '" + strGroup.toUpperCase() + "'");
        strSQL.append(" AND role<>'0000000000'");
        strSQL.append(" AND status IN (1,2)");  // Active developers only
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL.toString());
            if (rs.next()) {
                retVal = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(
                "SQLException occurs in DeveloperEJB.countDevelopers()."
                    + ex.getMessage());
            throw ex;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                System.out.println(
                    "Exception occurs in DeveloperEJB.countDevelopers(): "
                        + ex.toString());
                ex.printStackTrace();
            }
        }
        return retVal;
    }
     */
    
    public ArrayList getAccountList() throws SQLException, Exception {
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList arrResult = new ArrayList();

        String strSQL = "SELECT account FROM developer ORDER BY account";
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL);
            while (rs.next()) {
                arrResult.add(rs.getString("account").toUpperCase());
            }
        } catch (SQLException ex) {
            System.out.println(
                "SQLException occurs in DeveloperEJB.getDeveloperList()."
                    + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            System.out.println(
                "Exception occurs in DeveloperEJB.getDeveloperList()."
                    + ex.getMessage());
            throw ex;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();
        }
        return arrResult;
    }
    
    /**
     * Get developers number, condition parameters (group,status,account,name) are set via setters
     * @return int Number of develolpers
     * @throws SQLException
     * @throws Exception
     */
    public int getDeveloperNumber() throws SQLException, Exception {
        Statement stmt = null;
        ResultSet rs = null;
        int nResult = 0;

        StringBuffer strSQL = new StringBuffer();
        strSQL.append("SELECT count(developer_id) FROM developer WHERE 1=1");
        if (!Constants.GROUP_ALL.equals(this.Groups)) {
            strSQL.append(" AND upper(group_name) = '" + this.Groups.toUpperCase() + "'");
        }
        // Not selected all status
        // Not selected non-outplaced users (staff/collaborator/external)
        // Not selected working users (staff/collaborator)
        if ((!Constants.VALUE_STATUS_ALL.equals(this.strStatus)) &&
            (!"-4".equals(this.strStatus)) &&
            (!Constants.VALUE_STATUS_WORKING.equals(this.strStatus)))
        {
                strSQL.append(" AND status = " + this.strStatus);
        }
        // select all except outplaced
        else if ("-4".equals(this.strStatus)) {
            strSQL.append(" AND status <> 4");
        }
        // staff/collaborator
        else if (Constants.VALUE_STATUS_WORKING.equals(this.strStatus)) {
            strSQL.append(" AND status IN (").append(Constants.VALUE_STATUS_WORKING).append(")");
        }
        
        // Specified parameters: Account, name
        if ((this.Acc != null) && (! "".equals(this.Acc))) {
            strSQL.append(" AND upper(account)='" + this.Acc.toUpperCase() + "'");
        }
        if ((this.Name != null) && (! "".equals(this.Name))) {
            strSQL.append(" AND upper(name) LIKE '%" + this.Name.toUpperCase() + "%'");
        }
        // End: Specified parameters
        
        if (DB.DEBUG) {
            System.out.println("DeveloperEJB.getDeveloperNumber()  strSQL=" + strSQL);
        }
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL.toString());
            if (rs.next()) {
                nResult = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(
                "SQLException occurs in DeveloperEJB.getDeveloperList()."
                    + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            System.out.println(
                "Exception occurs in DeveloperEJB.getDeveloperList()."
                    + ex.getMessage());
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return nResult;
    }

    /**
     * Select developers list, other condition parameters (group,status,account,name) are set via setters
     * @param strSortBy
     * @param strDirection
     * @param nPage Page number <BR>
     * Negative number: get entire records <BR>
     * Positive number: jump to this page number of the listing
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public ArrayList getDeveloperList(String strSortBy, String strDirection, int nPage) throws SQLException, Exception {
        Statement stmt = null;
        ResultSet rs = null;
        int nFetches = 0;
        int nUBound = DB.PAGE_SIZE_STAFF;
        ArrayList arrResult = new ArrayList();

        StringBuffer strSQL = new StringBuffer();
        strSQL.append("SELECT developer_id,name,designation,group_name,role,password,account,status,email" +
                        " FROM developer" +
                        " WHERE 1=1");
        if (!Constants.GROUP_ALL.equals(this.Groups)) {
            strSQL.append(" AND upper(group_name) = '" + this.Groups.toUpperCase() + "'");
        }
        // Not selected all status
        // Not selected non-outplaced users (staff/collaborator/external)
        // Not selected working users (staff/collaborator)
        if ((!Constants.VALUE_STATUS_ALL.equals(this.strStatus)) &&
            (!"-4".equals(this.strStatus)) &&
            (!Constants.VALUE_STATUS_WORKING.equals(this.strStatus)))
        {
                strSQL.append(" AND status = " + this.strStatus);
        }
        // select all except outplaced
        else if ("-4".equals(this.strStatus)) {
            strSQL.append(" AND status <> 4");
        }
        // staff/collaborator
        else if (Constants.VALUE_STATUS_WORKING.equals(this.strStatus)) {
            strSQL.append(" AND status IN (").append(Constants.VALUE_STATUS_WORKING).append(")");
        }
        
        // Specified parameters: Account, name
        if ((this.Acc != null) && (! "".equals(this.Acc))) {
            strSQL.append(" AND upper(account)='" + this.Acc.toUpperCase() + "'");
        }
        if ((this.Name != null) && (! "".equals(this.Name))) {
            strSQL.append(" AND upper(name) LIKE '%" + this.Name.toUpperCase() + "%'");
        }
        // End: Specified parameters

        //Display sorted lists
        String strAscDesc = "";
        if ((strDirection != null) && (strDirection.length() > 0)) {
            if (strDirection.equals("0")) {
                strAscDesc = " DESC";
            }
            else if (strDirection.equals("1")) {
                strAscDesc = " ASC";
            }
        }
        if ((strSortBy != null) && (! "".equals(strSortBy))) {
            strSQL.append(" ORDER BY ").append(strSortBy).append(strAscDesc);
            if (strSortBy.equalsIgnoreCase("Group_Name")) {
                strSQL.append(",account");
            }
            else if (strSortBy.equalsIgnoreCase("Status") || strSortBy.equalsIgnoreCase("Designation")) {
                strSQL.append(",group_name,account");
            }
        }
        // Default sort by account
        else {
            strSQL.append(" ORDER BY account");
        }

        if (DB.DEBUG) {
            System.out.println("DeveloperEJB.getDeveloperList()  strSQL=" + strSQL);
        }
        try {
            con = ds.getConnection();
            // Open a scrollable and read only cursor
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(strSQL.toString());
            // Jump to starting fetch position
            int nCount = SqlUtil.setFetchPosition(rs, nPage, DB.PAGE_SIZE_STAFF);
            // nPage < 0 mean that get entire records, nCount is total number of records
            if (nPage < 0) {
                nUBound = nCount;
            }
            // Fetch until last records or reach max records per page
            while (rs.next() && (nFetches < nUBound)) {
                DeveloperInfo dev = new DeveloperInfo();
                dev.setDeveloperID(rs.getInt("developer_id"));
                dev.setAccount(rs.getString("account"));
                dev.setName(rs.getString("name"));
                dev.setGroupName(rs.getString("group_name"));
                dev.setStatus(rs.getInt("status"));
                dev.setRole(rs.getString("role"));
                dev.setDesignation(rs.getString("designation"));
                arrResult.add(dev);
                nFetches++;
            }
        } catch (SQLException ex) {
            System.out.println(
                "SQLException occurs in DeveloperEJB.getDeveloperList()."
                    + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            System.out.println(
                "Exception occurs in DeveloperEJB.getDeveloperList()."
                    + ex.getMessage());
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return arrResult;
    }
} // DeveloperEJB