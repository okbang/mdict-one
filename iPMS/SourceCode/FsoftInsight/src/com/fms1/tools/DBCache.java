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
 * Created on Aug 12, 2005
 */
package com.fms1.tools;

import java.sql.*;
import java.util.Vector;

import com.fms1.infoclass.DBCacheInfo;
import com.fms1.web.ServerHelper;

/**
 * @author Manu
 *
 * Stores & retrieve values from DB
 */
public class DBCache {
    
    public static final void set(int type, String key,long key2, double value) {
        PreparedStatement prepStmt = null;
        String sql = null;
        String sqlUpdate = null;
        Connection conn = null;
        try {
            sql = "INSERT INTO FMSCACHE (VALUE,TYPE, KEY,KEY2) VALUES (?,?,?,?)";
            sqlUpdate = "UPDATE FMSCACHE SET VALUE=? WHERE TYPE=? AND KEY=? AND KEY2=?";
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sqlUpdate);
            Db.setDouble(prepStmt,1, value);
            prepStmt.setInt(2,type);
            prepStmt.setString(3,key);
            prepStmt.setLong(4,key2);

            if (prepStmt.executeUpdate()<1) {
                prepStmt.close();
                prepStmt=conn.prepareStatement(sql);
                Db.setDouble(prepStmt,1, value);
                prepStmt.setInt(2,type);
                prepStmt.setString(3,key);
                prepStmt.setLong(4,key2);
                prepStmt.executeUpdate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt,null);
        }
    }
    /**
     * 
     * @return vector of DBCacheInfo
     */
    public static final void setPCB(long workunitID,int lifecycleID,Date date,int metricConstant,long projectId,double value){
        set(DBCacheInfo.TYPE_PCB, workunitID+"/"+lifecycleID+"/"+CommonTools.dateFormat(date)+"/"+metricConstant,projectId,value);
    }
    public static final Vector getPCB(long workunitID,int lifecycleID,Date date,int metricConstant){
        return get(DBCacheInfo.TYPE_PCB, workunitID+"/"+lifecycleID+"/"+CommonTools.dateFormat(date)+"/"+metricConstant);
    }
    /**
     * 
     * @return vector of DBCacheInfo
     */
    public static final Vector get(int type, String key){
        PreparedStatement prepStmt = null;
        String sql = null;
        Connection conn = null;
        ResultSet rs=null;
        Vector retVal=new Vector();
        try {
            sql = "SELECT KEY2,VALUE FROM FMSCACHE WHERE TYPE=? AND KEY=?";
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setInt(1,type);
            prepStmt.setString(2,key);
            
            rs=prepStmt.executeQuery();
            DBCacheInfo inf;
            while (rs.next()){
                inf=new DBCacheInfo ();
                inf.type=type;
                inf.key=key;
                inf.key2=rs.getLong("KEY2");
                inf.value=Db.getDouble(rs,"VALUE");
                retVal.add(inf);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt,rs);
            return retVal;
        }
    }

}
