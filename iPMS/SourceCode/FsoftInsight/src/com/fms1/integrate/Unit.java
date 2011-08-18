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
 * Created on Jan 11, 2008
 * This class contain all necessary functions related to unit to make FI
 * integrate with RMS2
 *
 */
package com.fms1.integrate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fms1.web.ServerHelper;

/**
 * @author trungtn
 *
 */
public class Unit {
    public static final String OPERATION_GROUP = "GROUP";
	public static final String BA_GROUP = "BA_TYPE";
	public static final String BRANCH = "BRANCH";
    
    /**
     * Get unit id by group name, this is not WorkUnitId of WorkUnit table that
     * used by old FI, this is the unit id of RMS_Unit table
     * @param projectStatusId
     * @return
     */
    public static int getUnitIdByGroupName(String groupName) {
        int unitId = 0;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        String sql = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT unit_id FROM Rms_unit WHERE unit_name=?"
                + " AND (unit_type_code=? OR unit_type_code=?)"
                + " union (select UNIT_ID FROM Rms_unit "
                + " where unit_name=? AND (unit_type_code<>? OR unit_type_code<>?))";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, groupName);
            preStm.setString(2, OPERATION_GROUP);
			preStm.setString(3, BA_GROUP);
			preStm.setString(4, groupName);
			preStm.setString(5, OPERATION_GROUP);
			preStm.setString(6, BA_GROUP);
            rs = preStm.executeQuery();
            if (rs.next()) {
                unitId = rs.getInt("unit_id");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, preStm, rs);
            return unitId;
        }
    }

}
