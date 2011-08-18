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
 * Created on Mar 9, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.fms1.common;

/**
 * @author thimb
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.sql.*;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.*;
import com.fms1.infoclass.group.*;

public class Dar {   
    /**
     * Used for get Dar_Plan info 
     * @param prjID 
     * @return    
     * Created on Mar 03, 2006
     * 
     */
    public static final Vector getDarPlan(final long prjID) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        Vector vt = null;
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            sql = "SELECT DEVELOPER.account as DOER, DP.DAR_PLAN_ID, DP.dar_item,DP.plan_date,DP.actual_date,DP.dar_cause" 
                  +" FROM DAR_PLAN DP, DEVELOPER"
                  +" WHERE PROJECT_ID = " + prjID + " and DP.devloper_id = DEVELOPER.developer_id" 
                  +" ORDER BY DP.DAR_ITEM";             
            rs = stm.executeQuery(sql);             
            if (rs != null) {                   
                vt = new Vector();
                while (rs.next()) {                     
                    DARPlanInfo info = new DARPlanInfo();   
                    info.darPlanID = rs.getInt("DAR_PLAN_ID");                          
                    info.doer = rs.getString("DOER");
                    info.darItem = rs.getString("DAR_ITEM");
                    info.planDate = rs.getDate("PLAN_DATE");
                    info.actualDate =  rs.getDate("ACTUAL_DATE");                        
                    if (info.actualDate == null) {
                        info.onTime = "N/A";
                    } else if (info.planDate.compareTo(info.actualDate)>=0) {
                        info.onTime = "Yes";
                    } else if (info.planDate.compareTo(info.actualDate)<0) {
                        info.onTime = "No";
                    }
                    info.darCause = (rs.getString("DAR_CAUSE") == null ? "" : rs.getString("DAR_CAUSE"));                                                                                   
                    vt.add(info);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    
    /**
     * add Data entered to database
     * @param darPlanInfo
     * @param prjID
     * @return     
     * Created on Mar 03, 2006
     */
    public static boolean addDarPlan(DARPlanInfo darPlanInfo, long prjID) {
        Connection conn = null;
        PreparedStatement stm = null;       
        String sql = null;
        try {
            conn = ServerHelper.instance().getConnection();             
            sql = "INSERT INTO DAR_PLAN VALUES ((SELECT NVL(MAX(DAR_PLAN_ID)+1,1) FROM DAR_PLAN), ?, ?, ?, ?, ?, ?)";
            stm = conn.prepareStatement(sql);               
            stm.setLong(1, prjID);                  
            stm.setLong(2, Long.parseLong(darPlanInfo.doer));       
            stm.setString(3, darPlanInfo.darItem);                              
            stm.setDate(4,(Date) darPlanInfo.planDate);             
            stm.setDate(5, (Date) darPlanInfo.actualDate);  
            stm.setString(6, darPlanInfo.darCause);                                 
            if (stm.executeUpdate() != 1)
                return false;                   
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
        }
    }

    /**
	 * @param darPlanID
	 */
	public static void deleteDarPlan(long darPlanID) {
            Db.delete(darPlanID,"DAR_PLAN_ID","DAR_PLAN");
    }   
    
    /**
     * Update data on page Quanlity to database 
     * @param darPlanInfo
     * @param prjID
     * @return    
     * Created on Mar 03, 2006
     */
    public static boolean updateDarPlan(DARPlanInfo darPlanInfo, long prjID ) {
        Connection conn = null;
        PreparedStatement stm = null;           
        String sql = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "UPDATE DAR_PLAN SET PROJECT_ID = ?, DEVLOPER_ID = ?, DAR_ITEM = ?,"
                 +"  PLAN_DATE = ?, ACTUAL_DATE = ?, DAR_CAUSE = ? WHERE DAR_PLAN_ID = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjID);
            stm.setLong(2, Long.parseLong(darPlanInfo.doer));
            stm.setString(3, darPlanInfo.darItem);
            stm.setDate(4, (Date) darPlanInfo.planDate);
            stm.setDate(5, (Date) darPlanInfo.actualDate);
            stm.setString(6, darPlanInfo.darCause);
            stm.setLong(7, darPlanInfo.darPlanID);                                  
            if (stm.executeUpdate() != 1)
                return false;
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
        }
    }
    
    /**
     * Update data on Mileston and Post motern to database
     * @param darPlanInfo
     * @return
     * @author Thimb
     * Created on Mar 03, 2006
     */
    public static boolean updateRepDarPlan(DARPlanInfo darPlanInfo) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "UPDATE DAR_PLAN SET ACTUAL_DATE = ?, DAR_CAUSE = ? WHERE DAR_PLAN_ID = ?";
            stm = conn.prepareStatement(sql);
            stm.setDate(1,(Date) darPlanInfo.actualDate);
            stm.setString(2, darPlanInfo.darCause);
            stm.setLong(3, darPlanInfo.darPlanID);
            if (stm.executeUpdate() != 1)
                return false;
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
        }
    }
}    
        
