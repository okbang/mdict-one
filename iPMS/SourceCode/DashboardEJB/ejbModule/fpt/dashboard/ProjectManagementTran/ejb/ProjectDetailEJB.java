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
 
 // August 30, 2001.
package fpt.dashboard.ProjectManagementTran.ejb;
import javax.ejb.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import java.util.*;

import fpt.dashboard.constant.DB;
import fpt.dashboard.framework.connection.*;
import fpt.dashboard.framework.util.SqlUtil;

public class ProjectDetailEJB implements SessionBean
{
    final private boolean DEBUG_MODE = true;
    private final String strClassName = this.getClass().getName();
    private DataSource ds = null;
    private Connection con = null;
    private WSConnectionPooling conPool = new WSConnectionPooling();
    //***************************************************************
    //  purpose:  get list of group from Database - Ap_group table.
    //***************************************************************
    public Collection listGroup() throws SQLException
    {
        ArrayList vList = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            //String txtLine = "";
            String selectStatement = "select groupname FROM groups,workunit" +
                " where groups.group_id=workunit.tableid" +
                " and workunit.type=" + DB.WORKUNIT_TYPE_GROUP +
                " and workunit.parentworkunitid=" + DB.WORKUNIT_FSOFT +
                " ORDER BY groupname";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(selectStatement);
            while (rs.next()) {
//                txtLine = rs.getString(1).trim();
//                vList.add(txtLine);
                vList.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) {
            showError(ex);
        }
        finally
        {
            if (rs != null)     rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }

        return vList;
    }
    public Collection listLeader() throws SQLException
    {
        ArrayList vList = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;

        try
            {
            String txtLine = "";
            String selectStatement =
                " select account from developer where (substr(role,2,1) ='1') Order by account";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(selectStatement);
            while (rs.next())
                {
                txtLine = rs.getString(1).trim();
                vList.add(txtLine);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null)     rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return vList;
    }
    public Collection listAll(
        String strGroup,
        String strStatus,
        String strType,
        String strCate)
        throws SQLException {
        ArrayList vList = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String txtLine = "";
            String selectGroup = "";
            //String selectType = "";
            String selectStatus = "";
            String selectCate = "";            
            String selectStatement = "SELECT Project_ID FROM Project";
			String strNotArchiveStatus = " archive_status <> 4 ";
			
            if (strGroup.trim().length() > 0) {
                if (!(Constants.GROUP_ALL.equals(strGroup))) {
                    selectGroup += " Group_Name='" + strGroup + "'";
                }
            }
            if (strStatus.trim().length() > 0) {
                if (Integer.parseInt(strStatus) != -1)
                    selectStatus += " Status ='" + strStatus + "'";
                else
                    selectStatus += " Status in ('0','1','2','3')";
            }
            //added by MinhPT 
            //for select in category
            if (strCate.trim().length() > 0) {
                if (Integer.parseInt(strCate) != -1)
                    selectCate += " Category ='" + strCate + "'";
                else {
                    // 0,1 - 2.
                    selectCate += " Category in ('0','1','2')";
                }
            }
            if (selectGroup.trim().length() > 0) {
                selectStatement += " Where ";
                selectStatement += selectGroup;
                if (selectStatus.trim().length() > 0) {
                    selectStatement += " And ";
                    selectStatement += selectStatus;
                }
            } else if (selectStatus.trim().length() > 0) {
                selectStatement += " Where ";
                selectStatement += selectStatus;
            }
            if (!selectCate.equals("")) {
                selectStatement += " AND ";
                selectStatement += selectCate;
            }
            if (Integer.parseInt(strType) != -1) {
                selectStatement += " And type=" + strType;
            }
            selectStatement += " AND ";
            selectStatement += strNotArchiveStatus;
            selectStatement += " Order by Code";
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(selectStatement);
	 		// System.out.println("DatabaseSQL- Get Project List:------ " + selectStatement);
            while (rs.next()) {
                txtLine = rs.getString(1).trim();
                vList.add(txtLine);
            }

            rs.close();
            stmt.close();
            // System.out.println(
            // "ProjectDetailEJB.listAll(): vList.size() = " + vList.size());
        } catch (SQLException ex) {
            showError(ex);
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();
        }

        return vList;
    }

    /**
     * Get all project IDs that a developer assigned to
     * @param group
     * @param status
     * @param type
     * @param cate
     * @param strDeveloperID
     * @return
     * @throws SQLException
     */
    public Collection listAssigned(
        String strGroup,
        String strStatus,
        String strType,
        String strCate,
        String strDeveloperID)
        throws SQLException 
    {
        ArrayList arrID = new ArrayList();
        Connection con = null;
        Statement stmt = null;
		String strNotArchiveStatus = " archive_status <> 4 ";
        ResultSet rs = null;
        
        try {
            String strSql = "SELECT Project_ID FROM Project WHERE 1=1";
            // filter by Group
            if ((strGroup != null) && (strGroup.trim().length() > 0) &&
                (!(Constants.GROUP_ALL.equals(strGroup))))
            {
                strSql += " AND Group_Name='" + strGroup + "'";
            }
            // filter by Status
            if ((strStatus != null) && (strStatus.trim().length() > 0)) {
                if (Integer.parseInt(strStatus) != -1) {
                    strSql += " AND Status ='" + strStatus + "'";
                }
                else {
                    strSql += " AND Status in ('0','1','2','3')";
                }
            }
			strSql += " AND ";
			strSql += strNotArchiveStatus;
            //for select in category
            if (strCate.trim().length() > 0) {
                if (Integer.parseInt(strCate) != -1) {
                    strSql += " AND Category ='" + strCate + "'";
                }
                else {
                    // 0,1 - 2.
                    strSql += " AND Category in ('0','1','2')";
                }
            }
            // Filter by type
            if ((strType != null) && (Integer.parseInt(strType) != -1)) {
                strSql += " AND type=" + strType;
            }
            // Get projects list that user assigned in
            if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
                strSql += " AND project_id IN(" +
                        "SELECT project_id FROM assignment WHERE developer_id=" +
                        strDeveloperID + ")";
            }
            strSql += " ORDER BY code";
            System.out.println("ProjectDetailEJB.listAssigned(): " + strSql);

            con = conPool.getConnection();    // Get connection from datasource
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSql);
            while (rs.next()) {
                arrID.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            //showError(ex);
        } finally {
            conPool.releaseResource(con, stmt, rs, "ProjectDetailEJB.listAssigned(): ");
        }
        
        return arrID;
    }
    
    public Collection getHeader(String pId) throws SQLException
    {
        ArrayList vList = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;

        try
            {
            String txtLine = "";
            String sqlCommand = "SELECT code,name,leader,category"              //+ "("
        //  + "SELECT DISTINCT COUNT(project_id) FROM milestone"
        //  + " WHERE project_id =" + pId
        //  + ")"
    		+ " FROM Project" + " WHERE Project_ID =" + pId;

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            if (rs.next())
            {
                txtLine = rs.getString(1).trim();
                vList.add(txtLine);
                txtLine = rs.getString(2).trim();
                vList.add(txtLine);
                if (rs.getString(3) != null)
                    {
                    txtLine = rs.getString(3).trim();
                    vList.add(txtLine);
                }
                else
                    {
                    vList.add("");
                }
                if (rs.getString(4) != null)
                    {
                    txtLine = rs.getString(4).trim();
                    vList.add(txtLine);
                }
                else
                    {
                    vList.add("");
                }
                //System.out.println ("Cate2"+rs.getString(4).trim());
                // Check for has child.
                /* Trang - no require now !
                 - dat vao mot bien boolean : true/false de chi ra the
                 project hasMilestone --> has function + attributes to allocate this

                txtLine = rs.getString(4).trim();
                vList.add(txtLine);
                */
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getHeader(). " + ex.getMessage());
        }
        catch (Exception ex)
        {
            showError(ex);
        }

        finally
        {
            if (rs != null)     rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return vList;
    }
    /**
     * Get Project Infomation which have Id is ProjectID
     * @param projectId
     * @return Collection
     */
    public Collection getContent(String projectId) throws SQLException{
    	ArrayList vList = new ArrayList();
    	Statement stm = null;
    	ResultSet rs = null;
    	String sql = null;
    	try{
    		sql = "SELECT CODE, NAME, LEADER, TO_CHAR(PLAN_START_DATE, 'dd/mm/yy') as PLAN_START_DATE, TO_CHAR(START_DATE,'dd/mm/yy') AS START_DATE, PER_COMPLETE"
    			+ ", TO_CHAR(BASE_FINISH_DATE, 'dd/mm/yy') AS BASE_FINISH_DATE, TO_CHAR(PLAN_FINISH_DATE, 'dd/mm/yy') AS PLAN_FINISH_DATE"
    			+ ", TO_CHAR(ACTUAL_FINISH_DATE, 'dd/mm/yy') AS ACTUAL_FINISH_DATE, BASE_EFFORT, PLAN_EFFORT, ACTUAL_EFFORT"
    			+ ", DESCRIPTION, STATUS, GROUP_NAME, TOTALREQUIREMENT, TOTALDEFECT, TOTALWEIGHTEDDEFECT"
    			+ ", SCHEDULE_STATUS, EFFORT_STATUS, TYPE, TO_CHAR(LAST_UPDATE, 'dd/mm/yy') AS LAST_UPDATE, CATEGORY, CUSTOMER"
    			+ ", (COSMETICPENDINGDEFECT + MEDIUMPENDINGDEFECT*3 + SERIOUSPENDINGDEFECT*5 + FATALPENDINGDEFECT*10) AS TOTALWEIGHTEDPENDING"
    			+ " FROM PROJECT"
    			+ " WHERE PROJECT_ID = " + projectId;
    		
    		if (ds == null){
    			ds = conPool.getDS();
    		}
    		con = ds.getConnection();
    		stm = con.createStatement();
    		rs = stm.executeQuery(sql);
    		if (rs.next()){
    			vList.add(rs.getString("CODE"));
    			vList.add(rs.getString("NAME"));
    			if (rs.getString("LEADER") != null){
					vList.add(rs.getString("LEADER"));
    			}
    			else{
					vList.add("N/A");
    			}
    			
				if (rs.getString("PLAN_START_DATE") != null){
					vList.add(rs.getString("PLAN_START_DATE"));
				}
				else {
					vList.add("");
				}
    			if (rs.getString("START_DATE") != null){
    				vList.add(rs.getString("START_DATE"));
    			}
    			else {
    				vList.add(""); 
    			}
    			if (rs.getString("PER_COMPLETE") != null){
    				vList.add(rs.getString("PER_COMPLETE"));
    			}
    			else {
    				vList.add("");
    			}
				if (rs.getString("BASE_FINISH_DATE") != null){
					vList.add(rs.getString("BASE_FINISH_DATE"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("PLAN_FINISH_DATE") != null){
					vList.add(rs.getString("PLAN_FINISH_DATE"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("ACTUAL_FINISH_DATE") != null){
					vList.add(rs.getString("ACTUAL_FINISH_DATE"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("BASE_EFFORT") != null){
					vList.add(rs.getString("BASE_EFFORT"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("PLAN_EFFORT") != null){
					vList.add(rs.getString("PLAN_EFFORT"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("ACTUAL_EFFORT") != null){
					vList.add(rs.getString("ACTUAL_EFFORT"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("DESCRIPTION") != null){
					vList.add(rs.getString("DESCRIPTION"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("STATUS") != null){
					vList.add(rs.getString("STATUS"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("GROUP_NAME") != null){
					vList.add(rs.getString("GROUP_NAME"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("TOTALREQUIREMENT") != null){
					vList.add(rs.getString("TOTALREQUIREMENT"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("TOTALDEFECT") != null){
					vList.add(rs.getString("TOTALDEFECT"));
				}
				else {
					vList.add("0");
				}
				if (rs.getString("TOTALWEIGHTEDDEFECT") != null){
					vList.add(rs.getString("TOTALWEIGHTEDDEFECT"));
				}
				else {
					vList.add("0");
				}
				if (rs.getString("SCHEDULE_STATUS") != null){
					vList.add(rs.getString("SCHEDULE_STATUS"));
				}
				else {
					vList.add("0");
				}
				if (rs.getString("EFFORT_STATUS") != null){
					vList.add(rs.getString("EFFORT_STATUS"));
				}
				else {
					vList.add("0");
				}
				if (rs.getString("TYPE") != null){
					vList.add(rs.getString("TYPE"));
				}
				else {
					vList.add("0");
				}
				if (rs.getString("LAST_UPDATE") != null){
					vList.add(rs.getString("LAST_UPDATE"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("CATEGORY") != null){
					vList.add(rs.getString("CATEGORY"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("CUSTOMER") != null){
					vList.add(rs.getString("CUSTOMER"));
				}
				else {
					vList.add("");
				}
				if (rs.getString("TOTALWEIGHTEDPENDING") != null){
					vList.add(rs.getString("TOTALWEIGHTEDPENDING"));
				}
				else {
					vList.add("");
				}
    		}
    		return vList;
    	}
    	catch (Exception ex){
    		ex.printStackTrace();
    		return null;
    	}
    	finally{
    		if (rs != null){
    			rs.close();
    		}
    		if (stm != null){
    			stm.close();
    		}
    		if (con != null){
    			con.close();
    		}
    	}
    }
    /*
     * Never used locally so hide it
     * 
    private String getScheduleStatus(String pId) throws SQLException
    {
        String txtLine = "Green";
        Statement stmt = null;
        ResultSet rs = null;

        try
            {
            // get Schedule Status from Miliestone information
            String sqlCommand =
                "SELECT (base_finish_date-base_start_date) as base_duration,(actual_finish_date-actual_start_date) as actual_duration  FROM "
                    + "(SELECT base_start_date, base_finish_date, actual_start_date, actual_finish_date "
                    + "from milestone "
                    + "where project_id ="
                    + pId
                    + " "
                    + "and base_start_date is not NULL "
                    + "and actual_start_date is not NULL "
                    + "and base_finish_date is not NULL "
                    + "and actual_finish_date is not NULL "
                    + "UNION "
                    + "Select base_start_date, base_finish_date, NVL(actual_start_date,base_start_date),NVL(actual_start_date,Trunc(sysdate)) "
                    + "from milestone "
                    + "where project_id ="
                    + pId
                    + " "
                    + "and base_start_date is not NULL "
                    + "and base_finish_date is not NULL "
                    + "and actual_finish_date is NULL "
                    + "and base_finish_date<=trunc(Sysdate) "
                    + "order by base_start_date DESC) "
                    + "Where rownum = 1";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            if (rs.next())
                {
                int base_duration = rs.getInt(1);
                int actual_duration = rs.getInt(2);
                if (base_duration == 0)
                    {
                    txtLine = "Green";
                }
                else
                    {
                    int num = 100 * (actual_duration - base_duration) / base_duration;
                    if (num > 10)
                        txtLine = "Red";
                    else if (num > 5)
                        txtLine = "Green";
                    else
                        txtLine = "Yellow";
                }
            }
            rs.close();
            stmt.close();
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null)     rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return txtLine;
    }
    */
    public void ejbLoad()
    {
        //
    }
    public void ejbCreate() throws CreateException
    {
        try
            {
            makeConnection();
        }
        catch (Exception ex)
            {
            throw new CreateException(ex.getMessage());
        }
    }
    public boolean addProject(
        String code,
        String name,
        String leader,
        String startDate,
        String perComplete,
        String baseFinishDate,
        String planFinishDate,
        String baseEffort,
        String planEffort,
        String actualEffort,
        String type,
        String group,
        String status,
        String customer,
        String cate) throws SQLException
    {
        try
            {
           if( insertRow(
                code,
                name,
                leader,
                startDate,
                perComplete,
                baseFinishDate,
                planFinishDate,
                baseEffort,
                planEffort,
                actualEffort,
                type,
                group,
                status,
                customer,
                cate)){
					return true;	
                }
                
             return false;
        }
        catch (SQLException ex)
            {
            //showError(ex);
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex)
            {
            //showError(ex);
            ex.printStackTrace();
            return false;
            // throw new SQLException(ex.getMessage());
        }
    }
    public void delProject(String pId) throws SQLException
    {
        try
            {
            deleteRow(pId);
        }
        catch (Exception ex)
            {
            showError(ex);
        }
    }
	public boolean updateProject(
		String pId,
		String code,
		String name,
		String leader,
		String startDate,
		String perComplete,
		String baseFinishDate,
		String planFinishDate,
		String baseEffort,
		String planEffort,
		String actualEffort,
		String type,
		String group,
		String status,
		String cate,
		String customer) throws SQLException{
			Connection conn = null;
			PreparedStatement preStm = null;
			String sql = null;
			try{
				final long parentWorkUnitID = getWUIDByGroupName(group);
				conn = conPool.getConnection();
				long projectId = Long.parseLong(pId);
				sql = "UPDATE PROJECT SET "
					+ "CODE=?"
					+ ", NAME=?"
					+ ", Leader=?"
					+ ", Start_date = to_date('" + startDate + "','dd/mm/yy')"
					+ ", Per_complete=?"
					+ ", Base_Finish_date=to_date('" + baseFinishDate + "','dd/mm/yy')"
					+ ", Plan_Finish_date=to_date('" + planFinishDate + "','dd/mm/yy')"
					+ ", Base_Effort=?"
					+ ", Plan_Effort=?"
					+ ", Actual_Effort=?"
					+ ", Type=?"
					+ ", Group_Name=?"
					+ ", status=?"
					+ ", category=?"
					+ ", Customer=?"
					+" WHERE Project_ID= ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, code);
				preStm.setString(2, name);
				preStm.setString(3, leader);
				preStm.setString(4,perComplete);
				preStm.setString(5, baseEffort);
				preStm.setString(6, planEffort);
				preStm.setString(7, actualEffort);
				preStm.setString(8, type);
				preStm.setString(9, group);
				preStm.setString(10, status);
				preStm.setString(11, cate);
				preStm.setString(12, customer);
				preStm.setLong(13, projectId);
				preStm.executeUpdate();

				con = ds.getConnection();
				sql =
					"UPDATE WorkUnit SET workunitname=?, PARENTWORKUNITID =?"
					+ " WHERE type=2" +
					" AND tableid=?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, code);
				preStm.setLong(2, parentWorkUnitID);
				preStm.setLong(3, projectId);
				preStm.executeUpdate();
				
				return true;
			}
			catch (Exception ex){
				ex.printStackTrace();
				return false;
			}
			finally{
				conPool.releaseResource(con, preStm, null, strClassName + ".updateProject(): ");
			}
		}

    /*
     * 
    public boolean updateProject(
        String pId,
        String code,
        String name,
        String leader,
        String startDate,
        String perComplete,
        String baseFinishDate,
        String planFinishDate,
        String baseEffort,
        String planEffort,
        String actualEffort,
        String type,
        String group,
        String status,
        String cate,
        String customer) throws SQLException
    {
        Connection con = null;
        Statement stmt = null;
        
        try
        {
			con = conPool.getConnection();
			
            String updateStatement = "UPDATE Project SET " +
                "code='" + code + "'," +
                "Name='" + name + "'," +
                "Leader='" + leader + "'," +
                "Start_date=to_date('" + startDate + "','dd/mm/yy')," +
                "Per_complete='" + perComplete + "'," +
                "Base_Finish_date=to_date('" + baseFinishDate + "','dd/mm/yy')," +
                "Plan_Finish_date=to_date('" + planFinishDate + "','dd/mm/yy')," +
                "Base_Effort='" + baseEffort + "'," +
                "Plan_Effort='" + planEffort + "'," +
                "Actual_Effort='" + actualEffort + "'," +
                "Type='" + type + "'," +
                "Group_Name='" + group + "'," +
                "status='" + status + "'," +
                "category='" + cate + "'," +
                "Customer='" + customer + "'" +
                " WHERE Project_ID=" + pId;

            // Update work unit name due to project code changing
            String strUpdUnit =
                "UPDATE WorkUnit SET workunitname='" + code + "'" +
                " WHERE type=2" +
                " AND tableid='" + pId + "' AND workunitname<>'" + code + "'";
            if (DB.DEBUG) {
                System.out.println(strClassName + " updateProject(): " + updateStatement);
                System.out.println(strClassName + " updateWorkUnit(): " + strUpdUnit);
            }
            
			stmt = con.createStatement();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            stmt.addBatch(updateStatement);
            stmt.addBatch(strUpdUnit);
            stmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
            return true;
            //con.commit();  // Not needed in EJB container mode
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            con.rollback();
            return false;
        }
        catch (Exception ex) {
        	ex.printStackTrace();
        	con.rollback();
        	return false;
        }
        finally {
            conPool.releaseResource(con, stmt, null, strClassName + ".updateProject(): ");
        }
    }
   */
/*
    public void updateProject(
        String pId,
        String code,
        String name,
        String leader,
        String startDate,
        String perComplete,
        String baseFinishDate,
        String planFinishDate,
        String baseEffort,
        String planEffort,
        String actualEffort,
        String type,
        String group,
        String status,
        String cate,
        String customer) throws SQLException
    {
        PreparedStatement prepStmt = null;

        try
        {
            String updateStatement =
                " Update Project "
                    + " Set code = ?,"
                    + " Name = ? ,"
                    + " Leader = ?,"
                    + " Start_date = to_date(?,'dd/mm/yy'),"
                    + " Per_complete = ?,"
                    + " Base_Finish_date = to_date(?,'dd/mm/yy'),"
                    + " Plan_Finish_date = to_date(?,'dd/mm/yy'),"
                    + " Base_Effort = ?,"
                    + " Plan_Effort = ?,"
                    + " Actual_Effort = ?,"
                    + " Type = ?,"
                    + " Group_Name = ?, "
                    + " status = ?, "
                    + " category = ?, "
                    + " Customer = ? "
                    + " Where Project_ID = ?";

            // Update work unit name due to project code changing
            String strUpdUnit =
                "UPDATE WorkUnit SET workunitname=?" +
                " WHERE type=2 AND tableid=? AND workunitname<>?";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement(updateStatement);

            prepStmt.setString(1, code);
            prepStmt.setString(2, name);
            prepStmt.setString(3, leader);
            prepStmt.setString(4, startDate);
            prepStmt.setString(5, perComplete);
            prepStmt.setString(6, baseFinishDate);
            prepStmt.setString(7, planFinishDate);
            prepStmt.setString(8, baseEffort);
            prepStmt.setString(9, planEffort);
            prepStmt.setString(10, actualEffort);
            prepStmt.setString(11, type);
            prepStmt.setString(12, group);
            prepStmt.setString(13, status);
            prepStmt.setString(14, cate);
            prepStmt.setString(15, customer);
            prepStmt.setString(16, pId);
            int rowCount = prepStmt.executeUpdate();
            prepStmt.close();
            if (rowCount == 0) {
                System.err.println("Update Project id " + pId + " failed.");
            }
            else {
                prepStmt = con.prepareStatement(strUpdUnit);
                prepStmt.setString(1, code);
                prepStmt.setString(2, pId);
                prepStmt.setString(3, code);
                prepStmt.executeUpdate();
                prepStmt.close();
            }
            con.commit();
        }
        catch (SQLException ex) {
            System.err.println("SQLException occurs in ProjectDetailEJB.updateProject()." + ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            System.err.println("Exception occurs in ProjectDetailEJB.updateProject()." + ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, null, strClassName + ".updateProject(): ");
        }
    }
*/
        public void closeProject(String pId, String actualFinishDate, String status,
            String desc) throws SQLException {
        PreparedStatement prepStmt = null;

        try {
            String updateStatement =
                "update project set "
                    + ("1".equals(status) ? " actual_Finish_date = to_date('" 
                    + actualFinishDate + "','dd/mm/yy')," : " actual_Finish_date=null,")
                    + " status=" + status + ","
                    + " description='" + desc + "'"
                    + " Where Project_ID=" + pId;
            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(updateStatement);
            int rowCount = prepStmt.executeUpdate();
            prepStmt.close();
            if (rowCount == 0)
                {
                System.out.println("Update Project id " + pId + " failed.");
            }
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.closeProject()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (prepStmt != null)   prepStmt.close();
            if (con != null) con.close();
        }
    }
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
        catch (Exception ex)
            {
            throw new EJBException(ex.getMessage());
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
    }
    public void setSessionContext(SessionContext context)
    {
    }
    public ProjectDetailEJB()
    {
    }

    /////////////////////////////////////////////////////////////////
    private void makeConnection() throws NamingException, SQLException
    {
        ds = conPool.getDS();
        con = ds.getConnection();
    }
    private boolean insertRow(
        String code,
        String name,
        String leader,
        String startDate,
        String perComplete,
        String baseFinishDate,
        String planFinishDate,
        String baseEffort,
        String planEffort,
        String actualEffort,
        String type,
        String group,
        String status,
        String customer,
        String cate)
        throws SQLException
    {
		String insertStatement = null;

        PreparedStatement prepStmt = null;

        try
        {
			final long parentWorkUnitID = getWUIDByGroupName(group);
			if (ds == null){
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			final long project_id = conPool.getNextSeq("project_seq");
			final long workUnitId = conPool.getNextSeq("WORKUNIT_SEQ");
			insertStatement =
								"INSERT INTO WORKUNIT (WORKUNITID,WORKUNITNAME, TYPE, PARENTWORKUNITID, TABLEID) "
								+ " VALUES (?,?,2,?,?"
								+ ")";
			// workUnit type = 2 because workUnit is project
			prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setLong(1, workUnitId);
			prepStmt.setString(2, code);
			prepStmt.setLong(3, parentWorkUnitID);
			prepStmt.setLong(4, project_id);
			prepStmt.executeUpdate();
			prepStmt.close();
			
	        if (ds == null){
	        	ds = conPool.getDS();
	       	}
	        con = ds.getConnection();
			insertStatement = 
				" Insert into Project (Project_Id,Code,Name,Leader,Start_date,Per_complete,"
					+ " Base_Finish_date,Plan_Finish_date,"
					+ " Base_Effort,Plan_Effort,Actual_Effort,Type, Group_Name,status,schedule_status,effort_status,customer,category)"
					+ " Values (?,?,?,?,to_date(?,'dd/mm/yy'),?,"
					+ " to_date(?,'dd/mm/yy'),to_date(?,'dd/mm/yy'),?,?,?,?,?,?,'0','0',?,?)";

			prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setLong(1, project_id);
	        prepStmt.setString(2, code);
	        prepStmt.setString(3, name);
	        prepStmt.setString(4, leader);
	        prepStmt.setString(5, startDate);
	        prepStmt.setString(6, perComplete);
	        prepStmt.setString(7, baseFinishDate);
	        prepStmt.setString(8, planFinishDate);
	        prepStmt.setString(9, baseEffort);
	        prepStmt.setString(10, planEffort);
	        prepStmt.setString(11, actualEffort);
	        prepStmt.setString(12, type);
	        prepStmt.setString(13, group);
	        prepStmt.setString(14, status);
	        prepStmt.setString(15, customer);
	        prepStmt.setString(16, cate);
	        System.out.println("ProjectDetailEJB.insertRow(): category = " + cate);
	        prepStmt.executeUpdate();
			prepStmt.close();
			
			return true;
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        	return false;
        }
        finally
        {
            if (prepStmt != null)   prepStmt.close();
            if (con != null)    con.close();
        }
    }
    /////////////////////////////
    private void deleteRow(String pId) throws SQLException
    {
        String sqlCommand = "DELETE FROM Project WHERE Project_ID =" + pId;
        Statement stmt = null;

        try
        {
            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sqlCommand);
            stmt.close();
            sqlCommand = "DELETE FROM WORKUNIT WHERE TABLEID =" + pId;
            stmt = con.createStatement();
            stmt.executeUpdate(sqlCommand);
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.deleteRow()." + ex.getMessage());
        }
        finally
        {
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
    }
    
    /**
     * Get assignments of a project
     * @param pId
     * @return
     * @throws SQLException
     */
    public Collection getAssignments(String pId) throws SQLException
    {
        ArrayList a = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // 2004-Oct-06: Response title get from Responsibility table
            String sqlCommand =
                " Select D.Name,to_char(A.Begin_date,'dd/mm/yy'), to_char(A.End_date,'dd/mm/yy'),type,usage, r.name as response " +
                " From  Developer D, Assignment A, responsibility R " +
                " Where D.Developer_ID = A.Developer_ID" +
                        " AND A.response=R.responsibility_id" +
                        " And A.Project_ID =" + pId +
                        " Order by A.Begin_date, D.Name";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            String Null_Date_Str = "00000000";
            char tt = 2;
            String strDiff = String.valueOf(tt);
            String txtName = "";
            String txtBdate = "";
            String txtEdate = "";
            String txtLine = "";
            String txtres = " ";
            while (rs.next())
                {
                txtLine = "";
                txtBdate = Null_Date_Str;
                txtEdate = Null_Date_Str;
                txtName = rs.getString(1);
                if (rs.getString(2) != null)
                    txtBdate = rs.getString(2);
                if (rs.getString(3) != null)
                    txtEdate = rs.getString(3);
                if (rs.getString(6) != null)
                    txtres = rs.getString(6);
                
/*                if (txtres.equals("0")) {
                    txtres = "Developer";
                }
                else if (txtres.equals("1")) {
                    txtres = "Tester";
                }
                else if (txtres.equals("2")) {
                    txtres = "Project Leader";
                }
                else if (txtres.equals("3")) {
                    txtres = "Project Director";
                }
                else if (txtres.equals("4")) {
                    txtres = "Graphic Designer";
                }
                else if (txtres.equals("5")) {
                    txtres = "External";
                }
                else if (txtres.equals("6")) {
                    txtres = "Onsite Cordinator";
                }
                else if (txtres.equals("7")) {
                    txtres = "SQA/PQA";
                }
                else {
                    txtres = " ";
                }*/
                
                txtLine =
                    txtName
                        + strDiff
                        + rs.getString(4)
                        + strDiff
                        + rs.getString(5)
                        + strDiff
                        + txtres
                        + strDiff
                        + txtBdate
                        + strDiff
                        + txtEdate;
                a.add(txtLine);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getAssignments()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null) rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return a;
    }
    public Collection getMilestones(String pId) throws SQLException
    {
        ArrayList a = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try
            {
            String sqlCommand =
                " Select M.Name, M.Complete, NVL ( PLAN_FINISH_DATE, BASE_FINISH_DATE) PLANNED_FINISH_DATE, "
                    + " to_char(M.Base_finish_date,'dd/mm/yy'),to_char(M.Plan_finish_date,'dd/mm/yy'), to_char(M.Actual_finish_date,'dd/mm/yy') "
                    + " From MileStone M "
                    + " Where M.Project_ID ="
                    + pId
                    + " Order by  PLANNED_FINISH_DATE";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            String Null_Date_Str = "00000000";
            String txtName = "";
            String txtComplete = "";
            String fBdate = "";
            String fPdate = "";
            String fAdate = "";
            String txtLine = "";
            int count = 0;
            while (rs.next()){
                fBdate = Null_Date_Str;
                fPdate = Null_Date_Str;
                fAdate = Null_Date_Str;
                txtName = rs.getString(1).trim();
                txtComplete = rs.getString(2).trim();
                if (rs.getString(4) != null)
                    fBdate = rs.getString(4).trim();
                if (rs.getString(5) != null)
                    fPdate = rs.getString(5).trim();
                if (rs.getString(6) != null)
                    fAdate = rs.getString(6).trim();
				txtLine = txtName + txtComplete + fBdate + fPdate + fAdate;
                a.add(txtLine);
                count++;
            }
			rs.close();
			stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getMilestones()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null) rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return a;
    }
    public String getIssueContent(String iId) throws SQLException
    {
        String txtLine = "";
        Statement stmt = null;
        ResultSet rs = null;

        try
            {
            String sqlCommand =
                " Select Project_Id, Description,"
                    + " to_char(Startdate,'dd/mm/yy'),to_char(Enddate,'dd/mm/yy') "
                    + " From OpenIssue "
                    + " Where OpenIssue_Id ="
                    + iId
                    + " Order by  Startdate";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            String projectId = "";
            String description = "";
            String Sdate = "";
            String Edate = "";
            char tt = 2;
            String strDiff = String.valueOf(tt);
            if (rs.next())
                {
                projectId = rs.getString(1).trim();
                description = rs.getString(2).trim();
                if (rs.getString(3) != null)
                    Sdate = rs.getString(3).trim();
                if (rs.getString(4) != null)
                    Edate = rs.getString(4).trim();
                txtLine = projectId + strDiff + description + strDiff + Sdate + strDiff + Edate;
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getIssueContent()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null) rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return txtLine;
    }
    public Collection getIssue(String pId) throws SQLException
    {
        ArrayList a = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;

        try
            {
            String sqlCommand =
                " Select OpenIssue_Id, Description,"
                    + " to_char(Startdate,'dd/mm/yy'),to_char(Enddate,'dd/mm/yy') "
                    + " From OpenIssue "
                    + " Where Project_ID ="
                    + pId
                    + " Order by  Startdate";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            String issueId = "";
            String description = "";
            String Sdate = "";
            String Edate = "";
            String txtLine = "";
            char tt = 2;
            String strDiff = String.valueOf(tt);
            while (rs.next())
                {
                Sdate = " ";
                Edate = " ";
                issueId = rs.getString(1).trim();
                description = rs.getString(2).trim();
                if (rs.getString(3) != null)
                    Sdate = rs.getString(3).trim();
                if (rs.getString(4) != null)
                    Edate = rs.getString(4).trim();
                txtLine = issueId + strDiff + description + strDiff + Sdate + strDiff + Edate;
                a.add(txtLine);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getIssue()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null) rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return a;
    }

/**
 * method: public Collection getMilestoneList(String fromDate, String toDate) throws SQLException
 * modified by: Nguyen Thai Son
 * date: September 03, 2002
 * description: Using MilestoneInfo object instead of TokenString for storing data
 * */
    public Collection getMilestoneList(String strComplete, String fromDate, String toDate, String strUserGroup) throws SQLException
    {
        ArrayList a = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            StringBuffer sqlCommand = new StringBuffer();
            sqlCommand.append("SELECT P.project_ID, P.Name as PROJECTNAME, M.Name as MILESTONENAME, M.complete, to_char(M.Base_finish_date,'dd/mm/yy') as BASEDATE, ");
            sqlCommand.append(" to_char(M.Plan_finish_date,'dd/mm/yy') AS PLANDATE, M.description, P.code");
            sqlCommand.append(" FROM MileStone M, Project P");
            if ("-1".equals(strComplete) )/*show all milestones*/
            {
                sqlCommand.append(" WHERE (P.Project_ID = M.Project_ID)");
            }
            else
            {
                if ("0".equals(strComplete)) {// incomplete
                    sqlCommand.append(" WHERE (P.Project_ID = M.Project_ID) AND (M.Complete = " + strComplete + ")");
                }
                else {// complete milestone, closed projects aren't counted
                    sqlCommand.append(" WHERE (P.Project_ID = M.Project_ID) AND (P.status <> 1) AND (M.Complete = " + strComplete + ")");
                }
            }
            if (!Constants.ALL.equals(strUserGroup))
                sqlCommand.append(" AND P.group_name LIKE '" + strUserGroup + "'");

            
//            sqlCommand.append(" AND (((M.Plan_finish_date >= to_date('" + fromDate + "','dd/mm/yy' ))");
//            sqlCommand.append(" AND    (M.Plan_finish_date <= to_date('" + toDate + "','dd/mm/yy' ))");
            sqlCommand.append(" AND ((1=1 ").append(SqlUtil.genDateConstraint("M.Plan_finish_date", fromDate, toDate));
            sqlCommand.append(" AND    (M.Plan_finish_date is not NULL))");
//            sqlCommand.append(" OR ((M.Base_finish_date >= to_date('" + fromDate + "','dd/mm/yy' ))");
//            sqlCommand.append(" AND (M.Base_finish_date <= to_date('" + toDate + "','dd/mm/yy' ))");
            sqlCommand.append(" OR (1=1 ").append(SqlUtil.genDateConstraint("M.Base_finish_date", fromDate, toDate));
            sqlCommand.append(" AND (M.Plan_finish_date is NULL)))");
            sqlCommand.append(" ORDER BY M.Project_ID");

            System.out.println("ProjectDetailEJB.getMilestoneList(): SQL = " + sqlCommand.toString());

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand.toString());
            while (rs.next())
            {
                MilestoneInfo milestone = new MilestoneInfo();
                milestone.setProjectID(rs.getInt("project_ID"));
                milestone.setProjectName(rs.getString("PROJECTNAME"));
                milestone.setName(rs.getString("MILESTONENAME"));
                milestone.setBaseFinishDate((rs.getString("BASEDATE") == null) ? "" : rs.getString("BASEDATE"));
                milestone.setPlanFinishDate((rs.getString("PLANDATE") == null) ? "" : rs.getString("PLANDATE"));
                milestone.setDescription((rs.getString("DESCRIPTION") == null) ? "" : rs.getString("DESCRIPTION"));
                milestone.setProjectCode(rs.getString("CODE"));
                milestone.setStatus(rs.getString("COMPLETE"));
                a.add(milestone);
            }

            System.out.println("ProjectDetailEJB.getMilestoneList(): a.size() = " + a.size());

            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getMilestoneList()." + ex.getMessage());
            ex.printStackTrace();
        }
        catch (Exception ex)
        {
            showError(ex);
            ex.printStackTrace();
        }
        finally
        {
            if (rs != null) rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return a;
    }

    public Collection getRequirementChartData(String pId) throws SQLException
    {
        ArrayList a = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;

        try
            {
            String sqlCommand =
                "select acceptedRequirement,"
                    + " deployedRequirement,"
                    + " testedRequirement,"
                    + " codedRequirement,"
                    + " designedRequirement,"
                    + " committedRequirement,"
                    + " totalRequirement"
                    + " from project where project_id="
                    + pId;

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            String cName = "";
            String cValue = "";
            String txtLine = "";
            char tt = 2;
            String strDiff = String.valueOf(tt);
            if (rs.next())
                {
                cName = "Accepted";
                cValue = "0";
                if (rs.getString(1) != null)
                    cValue = rs.getString(1).trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
                cName = "Deployed";
                cValue = "0";
                if (rs.getString(2) != null)
                    cValue = rs.getString(2).trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
                cName = "Tested";
                cValue = "0";
                if (rs.getString(3) != null)
                    cValue = rs.getString(3).trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
                cName = "Coded";
                cValue = "0";
                if (rs.getString(4) != null)
                    cValue = rs.getString(4).trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
                cName = "Designed";
                cValue = "0";
                if (rs.getString(5) != null)
                    cValue = rs.getString(5).trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
                cName = "Commited";
                cValue = "0";
                if (rs.getString(6) != null)
                    cValue = rs.getString(6).trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
                cName = "Total";
                cValue = "0";
                if (rs.getString(7) != null)
                    cValue = rs.getString(7).trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getRequirementChartData()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null) rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return a;
    }

    public Collection getDefectChartData(String pId) throws SQLException
    {
        ArrayList a = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;

        try
        {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("SELECT (totalCosmeticDefect - cosmeticPendingDefect) AS CosmeticClosed, ");
            strSQL.append(" cosmeticPendingDefect AS CosmeticPending ,");
            strSQL.append(" (totalMediumDefect - mediumPendingDefect) AS MediumClosed, ");
            strSQL.append(" mediumPendingDefect AS MediumPending ,");
            strSQL.append(" (totalSeriousDefect - seriousPendingDefect) AS SeriousClosed, ");
            strSQL.append(" seriousPendingDefect AS SeriousPending ,");
            strSQL.append(" (totalFatalDefect - fatalPendingDefect) AS FatalClosed, ");
            strSQL.append(" fatalPendingDefect AS FatalPending ,");
            strSQL.append(" (totalCosmeticDefect + totalMediumDefect + totalSeriousDefect + totalFatalDefect - cosmeticPendingDefect - seriousPendingDefect - mediumPendingDefect - fatalPendingDefect) AS TotalClosed, ");
            strSQL.append(" (cosmeticPendingDefect + seriousPendingDefect + mediumPendingDefect + fatalPendingDefect) AS TotalPending");
            strSQL.append(" FROM project");
            strSQL.append(" WHERE project_id = " + pId);

            System.out.println("EJB.ProjectDetailEJB.getDefectChartData(): SQL = " + strSQL.toString());

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL.toString());
            String cName = "";
            String cValue = "";
            String txtLine = "";
            char tt = 2;
            String strDiff = String.valueOf(tt);
            if (rs.next())
            {
                cName = "Cosmetric";
                cValue = "0";
                if (rs.getString(1) != null)                    cValue = rs.getString("CosmeticClosed").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Cosmetric2";
                cValue = "0";
                if (rs.getString(2) != null)                    cValue = rs.getString("CosmeticPending").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Medium";
                cValue = "0";
                if (rs.getString(3) != null)                    cValue = rs.getString("MediumClosed").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Medium2";
                cValue = "0";
                if (rs.getString(4) != null)                    cValue = rs.getString("MediumPending").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Serious";
                cValue = "0";
                if (rs.getString(5) != null)                    cValue = rs.getString("SeriousClosed").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Serious2";
                cValue = "0";
                if (rs.getString(6) != null)                    cValue = rs.getString("SeriousPending").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Fatal";
                cValue = "0";
                if (rs.getString(7) != null)                    cValue = rs.getString("FatalClosed").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Fatal2";
                cValue = "0";
                if (rs.getString(8) != null)                    cValue = rs.getString("FatalPending").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Total";
                cValue = "0";
                if (rs.getString(9) != null)                    cValue = rs.getString("TotalClosed").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);

                cName = "Total2";
                cValue = "0";
                if (rs.getString(10) != null)                   cValue = rs.getString("TotalPending").trim();
                txtLine = cName + strDiff + cValue;
                a.add(txtLine);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.getDefectChartData()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (rs != null) rs.close();
            if (stmt != null)   stmt.close();
            if (con != null)    con.close();
        }
        return a;
    }
    //////////////////////////////////////////////////////////////////////////////
    // Under Contruction area - for new function, new idea
    //////////////////////////////////////////////////////////////////////////////
    private void showError(Exception ex)
    {
        if (DEBUG_MODE)
            {
            System.out.println("  Error: " + ex.toString());
        }
    }
    
    /*
     * Never used locally so hide it
     * 
    private void showMirror(String msg)
    {
        if (DEBUG_MODE)
        {
            System.out.println("  Message: " + msg);
        }
    }
    */
    //////////////////////////////////////////////////////////////////////////////
    // Issue
    //////////////////////////////////////////////////////////////////////////////
    public void addProjectIssue(
        String projectId,
        String description,
        String startDate,
        String endDate) throws SQLException
    {
        try
            {
            addIssue(projectId, description, startDate, endDate);
        }
        catch (Exception ex)
            {
            showError(ex);
        }
    }
    public void delProjectIssue(String iId) throws SQLException
    {
        try
            {
            deleteIssue(iId);
        }
        catch (Exception ex)
            {
            showError(ex);
        }
    }
    public void updateProjectIssue(
        String iId,
        String projectId,
        String description,
        String startDate,
        String endDate) throws SQLException
    {
        PreparedStatement prepStmt = null;

        try
            {
            String sqlCommand =
                " Update OpenIssue"
                    + " Set Project_Id = ?,Description = ?,"
                    + " Startdate = to_date(?,'dd/mm/yy'),"
                    + " Enddate = to_date(?,'dd/mm/yy')"
                    + " Where OpenIssue_ID = ?";

            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(sqlCommand);
            System.out.println(sqlCommand);
            prepStmt.setString(1, projectId);
            prepStmt.setString(2, description);
            prepStmt.setString(3, startDate);
            prepStmt.setString(4, endDate);
            prepStmt.setString(5, iId);
            int rowCount = prepStmt.executeUpdate();
            prepStmt.close();
            if (rowCount == 0)
                {
                System.out.println("Storing row for id " + iId + " failed.");
            }
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.updateProjectIssue()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (prepStmt != null)   prepStmt.close();
            if (con != null)    con.close();
        }
    }
    ///////////////////////////////////
    private void addIssue(
        String projectId,
        String description,
        String startDate,
        String endDate)
        throws SQLException
    {
        String sqlCommand =
            " Insert into OpenIssue (Project_Id,Description,StartDate, EndDate)"
                + " Values ( ?,?, to_date(?,'dd/mm/yy'), to_date(?,'dd/mm/yy') )";

        PreparedStatement prepStmt = null;

        try
        {
            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
        prepStmt = con.prepareStatement(sqlCommand);
        prepStmt.setString(1, projectId);
        prepStmt.setString(2, description);
        prepStmt.setString(3, startDate);
        prepStmt.setString(4, endDate);
        prepStmt.executeUpdate();
        prepStmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.addIssue()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (prepStmt != null) prepStmt.close();
            if (con != null)    con.close();

        }
    }
    /////////////////////////////
    private void deleteIssue(String iId) throws SQLException
    {
        String sqlCommand = "DELETE FROM OpenIssue WHERE OpenIssue_ID =" + iId;
        Statement stmt = null;

        try
        {
            if (ds == null)     ds = conPool.getDS();
            con = ds.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate(sqlCommand);
        stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException occurs in ProjectDetailEJB.deleteIssue()." + ex.getMessage());
        }
        catch (Exception ex)
            {
            showError(ex);
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (con != null)    con.close();
        }
    }

    /**
     * Get on-going projects in a time period
     * @param cal_From
     * @param cal_To
     * @param strGroupName
     * @return
     * @throws SQLException
     */
    public Collection getOngoingList(Calendar cal_From, Calendar cal_To,
                                     String strGroupName) throws SQLException
    {
        ArrayList arrResult = new ArrayList();
        String strFrom = (cal_From.get(Calendar.MONTH) + 1) + "/" + cal_From.get(Calendar.YEAR);
        String strTo = (cal_To.get(Calendar.MONTH) + 1) + "/" + cal_To.get(Calendar.YEAR);
        
        String strSQL =
                "SELECT project_id, code, name" +
                " FROM project" +
                " WHERE type <> 9" +  // MISC project
                " AND not((code like 'Daily_G%') and (group_name like 'G%'))" +
                " AND start_date <= To_date(?, 'mm/yy')" +
                " AND ((actual_finish_date IS NULL) OR (actual_finish_date >= To_date(?, 'mm/yy')))";
        if (!strGroupName.equals(Constants.ALL)) {
            strSQL += " AND group_name = '" + strGroupName + "'";
        }
        strSQL += " ORDER BY project_id";
        
        if (DB.DEBUG) {
            System.out.println("ProjectDetailEJB.getOngoingList() - strSQL:" + strSQL);
        }
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL);
            prepStmt.setString(1, strTo);
            prepStmt.setString(2, strFrom);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                ProjectDashboardInfo projectInfo = new ProjectDashboardInfo();
                projectInfo.setID(rs.getInt("project_id"));
                projectInfo.setCode(rs.getString("code"));
                projectInfo.setName(rs.getString("name"));
                arrResult.add(projectInfo);
            }
            rs.close();
            prepStmt.close();
            
            if (!strGroupName.equals(Constants.ALL)) {
                // Select projects of other groups which developers of this group is assigned to
                strSQL ="SELECT distinct P.project_id, P.code, P.name, P.group_name" +
                        " FROM project P, assignment A, developer D" +
                        " WHERE P.type <> 9" +  // MISC project
                        " AND not((P.code like 'Daily_G%') and (P.group_name like 'G%'))" +
                        " AND P.start_date <= To_date(?, 'mm/yy')" +
                        " AND ((P.actual_finish_date IS NULL) OR (P.actual_finish_date >= To_date(?, 'mm/yy')))" +
                        " AND P.project_id = A.project_id" +
                        " AND D.developer_id = A.developer_id" +
                        " AND A.begin_date <= To_date(?, 'mm/yy')" +
                        " AND A.end_date >= To_date(?, 'mm/yy')" +
                        " AND D.group_name = ?" +
                        " AND P.group_name <> ?" +
                        " ORDER BY P.group_name, P.code";
                if (DB.DEBUG) {
                    System.out.println("ProjectDetailEJB.getOngoingList() - strSQL:" + strSQL);
                }
                prepStmt = con.prepareStatement(strSQL);
                prepStmt.setString(1, strTo);
                prepStmt.setString(2, strFrom);
                prepStmt.setString(3, strTo);
                prepStmt.setString(4, strFrom);
                prepStmt.setString(5, strGroupName);
                prepStmt.setString(6, strGroupName);
                rs = prepStmt.executeQuery();
                while (rs.next()) {
                    ProjectDashboardInfo projectInfo = new ProjectDashboardInfo();
                    String strGroupOther = rs.getString("group_name");
                    projectInfo.setID(rs.getInt("project_id"));
                    projectInfo.setCode(strGroupOther + "/" + rs.getString("code"));
                    projectInfo.setName(strGroupOther + "/" + rs.getString("name"));
                    arrResult.add(projectInfo);
                }
                rs.close();
                prepStmt.close();
            }
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
	/**
	 * Get WorkUnitID by GroupName
	 * @param groupName
	 * @return
	 */
	public final long getWUIDByGroupName(final String groupName) throws SQLException{
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		long workUnitId = 0;
		try{
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			
			sql = "SELECT WORKUNITID FROM WORKUNIT WHERE upper(WORKUNITNAME)=upper('" + groupName + "')";
			stm = con.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next()){
				workUnitId = rs.getLong("WORKUNITID");			
			}
			return workUnitId;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		finally{
			if (rs != null){
				rs.close();
			}
			if (stm != null){
				stm.close();
			}
			if (con != null){
				con.close();
			}
		}
	}
} // ProjectDetailEJB