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
 
 package fpt.dashboard.combo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fpt.dashboard.framework.connection.WSConnectionPooling;

/**
 * Bean implementation class for Enterprise Bean: ConstantComboEJB<br>
 * Get data for combo boxes
 * @author trungtn
 * @since  2004-Oct-06<br>
 */
public class ConstantComboEJB implements javax.ejb.SessionBean {
    private final String CLASS_NAME = this.getClass().getName();
    private WSConnectionPooling conPool = new WSConnectionPooling();

    private javax.ejb.SessionContext mySessionCtx;
    /**
     * getSessionContext
     */
    public javax.ejb.SessionContext getSessionContext() {
        return mySessionCtx;
    }
    /**
     * setSessionContext
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) {
        mySessionCtx = ctx;
    }
    /**
     * ejbCreate
     */
    public void ejbCreate() throws javax.ejb.CreateException {
    }
    /**
     * ejbActivate
     */
    public void ejbActivate() {
    }
    /**
     * ejbPassivate
     */
    public void ejbPassivate() {
    }
    /**
     * ejbRemove
     */
    public void ejbRemove() {
    }
    
    /**
     * Get response from responsibility table represented in ConstantRow
     * @return ArrayList
     * @throws SQLException
     */
    public ArrayList getResponse() throws SQLException {
        ArrayList arlResult = new ArrayList();
        Statement stmt = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            String strSql = "SELECT responsibility_id, name FROM responsibility";
            con = conPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSql);
            while (rs.next()) {
                ConstantRow row = new ConstantRow();
                row.setID(rs.getInt("responsibility_id"));
                row.setTitle(rs.getString("name"));
                arlResult.add(row);
            }
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            conPool.releaseResource(con, stmt, rs, CLASS_NAME + ".getResponsibilities(): ");
        }
        return arlResult;
    }
}
