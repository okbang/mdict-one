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
 * @(#)WSConnectionPooling.java 12-Mar-03
 */

package fpt.timesheet.framework.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fpt.timesheet.constant.Timesheet;

/**
 * Class WSConnectionPooling
 * Use Websphere 4.0 Initial Context Factory interface for connection pooling
 * @version 1.0 12-Mar-03
 * @author
 */
public class WSConnectionPooling {
    private static Logger logger = Logger.getLogger(WSConnectionPooling.class);
    private static DataSource ds = null;
    //private String strDbName = Timesheet.DB_NAME;

    /**
     * Method getDS.
     * @return DataSource
     */
    public DataSource getDS() {
        try {
            Hashtable parms = new Hashtable();
            /*parms.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");*/
            InitialContext ctx = new InitialContext(parms);
            ds = (DataSource) ctx.lookup(Timesheet.DB_NAME);
        }
        catch (Exception e) {
            logger.error("Naming service exception: " + e.getMessage());
            //e.printStackTrace();
        }
        return ds;
    }

    /**
     * Method releaseResource.
     * @param con
     * @param stmt
     * @param rs
     * @param strMsg
     */
    public void releaseResource(Connection con, Statement stmt, ResultSet rs, String strMsg) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e.getMessage());
        }

        try {
            if (con != null) {
                con.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e.getMessage());
        }
    }

    /**
     * Method releaseResourceAndCommit.
     * @param con
     * @param stmt
     * @param rs
     * @param strMsg
     */
    public void releaseResourceAndCommit(Connection con, Statement stmt, ResultSet rs, String strMsg) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e.getMessage());
        }

        try {
            if (con != null) {
                con.commit();
                con.setAutoCommit(true);
                con.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e.getMessage());
        }
    }

	/**
	 * Get next sequence number from database (for add new record)
	 * @param strSeqName
	 * @return
	 */
	public static final long getNextSeq(String strSeqName)
				throws SQLException, javax.naming.NamingException {
		long nResult = -1;
		Connection conn = null;
		PreparedStatement stm = null;
		WSConnectionPooling conpool = new WSConnectionPooling();
		String sql = null;
		ResultSet rs = null;
		sql = "SELECT " + strSeqName + ".NEXTVAL FROM dual";
		try {
			conn = conpool.getDS().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			if (rs.next()) {
				nResult = rs.getLong(1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			conpool.releaseResource(conn, stm, rs, "");
		}
		return nResult;
	}

}
