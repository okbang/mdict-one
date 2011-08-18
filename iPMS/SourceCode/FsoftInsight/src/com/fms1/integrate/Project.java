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
 
 /*
 * Created on Jan 10, 2008
 * This class contain all necessary functions related to project to make FI
 * integrate with RMS2
 */
package com.fms1.integrate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.fms1.infoclass.AssignmentInfo;
import com.fms1.web.ServerHelper;

/**
 * @author trungtn
 *
 */
public class Project {
    /**
     * Get project status code by project status id from PROJECT_STATUS_CODE table
     * @param projectStatusId
     * @return
     */
    public static String getProjectStatusCodeById(int projectStatusId) {
        String statusCode = "";
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        String sql = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT code FROM rms_project_status WHERE status_id=?";
            preStm = conn.prepareStatement(sql);
            preStm.setInt(1, projectStatusId);
            rs = preStm.executeQuery();
            if (rs.next()) {
                statusCode = rs.getString("code");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, preStm, rs);
            return statusCode;
        }
    }

    /**
     * Get project position code by responsibility id from Responsibility table
     * @param responsibilityId
     * @return
     */
    public static String getProjectPositionCodeById(long responsibilityId) {
        String positionCode = "";
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        String sql = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT project_position_code FROM responsibility WHERE responsibility_id=?";
            preStm = conn.prepareStatement(sql);
            preStm.setLong(1, responsibilityId);
            rs = preStm.executeQuery();
            if (rs.next()) {
                positionCode = rs.getString("project_position_code");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, preStm, rs);
            return positionCode;
        }
    }

    /**
     * Get roles to be inserted into RMS_USER_ROLE table when add an assignment
     * @param assignment
     * @param positionCode
     * @return
     */
    private static Vector getRolesAddAssignment(AssignmentInfo assignment)
    {
        Vector roleList = new Vector();
        //------------------------------------------------------------------
        // When add assignment, should insert roles of user that mapped with
        // new added assignment position but exclude existed same roles of
        // user at project 
        //------------------------------------------------------------------
        String sql =
            // Selecte roles mapped with this position
            "SELECT role_id" 
            + " FROM   RESPONSIBILITY res, RMS_PROJECT_POSITION_ROLE ppr"
            + " WHERE  res.project_position_code = ppr.project_position_code"
            + " AND    res.responsibility_id=?"
            // Exclude existed roles of this user in this project
            + " MINUS"
            + " SELECT role_id"
            + " FROM   RMS_USER_ROLE"
            + " WHERE  PROJECT_ID=? AND developer_id=?";

        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setLong(1, assignment.responsibilityID);
            preStm.setLong(2, assignment.projectID);
            preStm.setLong(3, assignment.devID);
            rs = preStm.executeQuery();
            while (rs.next()) {
                roleList.add(new Integer(rs.getInt("ROLE_ID")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, preStm, rs);
            return roleList;
        }
    }
    
    /**
     * Update roles of user at project after assigned to project (for integrate
     * with RMS)
     * @param assignment
     * @param positionCode
     * @return Update result
     */
    public static boolean updateRoleByAddAssignment(AssignmentInfo assignment)
    {
        boolean isSucceed = true;
        Connection conn = null;
        PreparedStatement preStm = null;
        String sql = "INSERT INTO RMS_USER_ROLE (RMS_USER_ROLE_ID,"
            + " DEVELOPER_ID, PROJECT_ID, ROLE_ID) "
            + " VALUES ((SELECT NVL(MAX(RMS_USER_ROLE_ID) + 1, 1) FROM RMS_USER_ROLE),"
            + " ?, ?, ?)";
        
        try {
            Vector roleList = getRolesAddAssignment(assignment);

            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
            preStm = conn.prepareStatement(sql);
            int roleId;
            for (int i = 0; i < roleList.size(); i++) {
                roleId = ((Integer) roleList.get(i)).intValue();
                preStm.setLong(1, assignment.devID);
                preStm.setLong(2, assignment.projectID);
                preStm.setInt(3, roleId);
                if (preStm.executeUpdate() <= 0) {
                    throw new SQLException("----->updateRoleByAddAssignment()" +
                        "Cannot insert user role: devID=" + assignment.devID +
                        ";projectID=" + assignment.projectID +
                        ";roleId=" + roleId);
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch (SQLException e) {
            isSucceed = false;
            e.printStackTrace();
            try {
                conn.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        finally {
            ServerHelper.closeConnection(conn, preStm, null);
            return isSucceed;
        }
    }
    
    /**
     * Remove roles of user when delete assingment from project team. Program
     * should remove roles mapped with the deleting position but exclude roles
     * mapped with other assigned position of user in current project.
     * @param assignment
     * @return
     */
    public static boolean updateRoleByDeleteAssignment(AssignmentInfo assignment)
    {
        boolean isSucceed = true;
        String sqlRolesRemove =
            // Roles mapped with [assignment position] that being deleted.
            "SELECT role_id" 
            + " FROM   ASSIGNMENT ass, RESPONSIBILITY res, RMS_PROJECT_POSITION_ROLE ppr"
            + " WHERE  res.project_position_code = ppr.project_position_code"
            + " AND    ass.project_position_code = res.project_position_code"
            + " AND    ass.assignment_id=?"
            // Exclude roles of [user] of [posistion] of [other assignments] in the project
            + " MINUS"
            + " SELECT role_id"
            + " FROM   RMS_PROJECT_POSITION_ROLE ppr, "
            // Get all positions mapped with other assignments
            + " (SELECT DISTINCT project_position_code FROM assignment"
            + "   WHERE project_id=? AND developer_id=?"
            + "     AND assignment_id <> ?) apos"
            + " WHERE  ppr.project_position_code = apos.project_position_code"
            ;
        String sql = "DELETE RMS_USER_ROLE"
            + " WHERE  project_id=? AND developer_id=?"
            + " AND  role_id IN (" + sqlRolesRemove + ")"
            ;
        Connection conn = null;
        PreparedStatement preStm = null;
        try {
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
            preStm = conn.prepareStatement(sql);
            // sql. DELETE condition
            preStm.setLong(1, assignment.projectID);
            preStm.setLong(2, assignment.devID);
            
            // sqlRolesRemove. SELECT statement
            preStm.setLong(3, assignment.assID);
            // sqlRolesRemove. MINUS statement
            preStm.setLong(4, assignment.projectID);
            preStm.setLong(5, assignment.devID);
            preStm.setLong(6, assignment.assID);
            preStm.executeUpdate();
            conn.setAutoCommit(true);
            conn.commit();
        }
        catch (SQLException e) {
            isSucceed = false;
            e.printStackTrace();
            try {
                conn.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        finally {
            ServerHelper.closeConnection(conn, preStm, null);
            return isSucceed;
        }
    }
    
    /**
     * Update roles of user when update assingment from project team. First,
     * it should DELETE roles mapped with old position then INSERT roles mapped
     * with new updating position (if they are different).
     * @param assignment
     * @return
     */
    public static boolean updateRoleByUpdateAssignment(
        AssignmentInfo newAssignment, AssignmentInfo oldAssignment)
    {
        boolean isSucceed = true;
        // If position is changed then update role, otherwise ignore updating
        if (newAssignment.responsibilityID != oldAssignment.responsibilityID) {
            // First, remove roles mapped with old position
            isSucceed = updateRoleByDeleteAssignment(oldAssignment);
            // Then insert roles mapped with new position
            if (isSucceed) {
                isSucceed = updateRoleByAddAssignment(newAssignment);
            }
        }
        return isSucceed;
    }
}
