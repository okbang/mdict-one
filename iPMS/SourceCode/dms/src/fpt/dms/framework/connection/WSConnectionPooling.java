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
 
 package fpt.dms.framework.connection;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class WSConnectionPooling {
	private static DataSource ds = null;
	private static final String JNDI_NAME = "java:jdbc/DMSDB";
	private static Logger logger = Logger.getLogger(WSConnectionPooling.class.getName());
	
    /**
     * Method getDS.
     * @return DataSource
     */
	public DataSource getDS() throws javax.naming.NamingException {
        //if (ds == null) {
            try {
                // Note the new Initial Context Factory interface
                // available in Version 4.0
                Hashtable parms = new Hashtable();
                /*parms.put(Context.INITIAL_CONTEXT_FACTORY,
                        "com.ibm.websphere.naming.WsnInitialContextFactory");*/
                InitialContext ctx = new InitialContext(parms);
                // Perform a naming service lookup to get the DataSource object.
                ds = (DataSource)ctx.lookup(JNDI_NAME);                
            } 
            catch (javax.naming.NamingException e) {
            	e.printStackTrace();
                logger.error("Naming service exception: " + e);
                throw e;
            }
        //}
		return ds;		
	}

    /**
     * Method releaseResource.
     * @param con Connection
     * @param stmt Statement or PrepareStatement
     * @param rs ResultSet
     * @param strMsg Message
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
     * @param con Connection
     * @param stmt Statement or PreparedStatement
     * @param rs ResultSet
     * @param strMsg Message
     */
    public void releaseResourceAndCommit(Connection con, Statement stmt, ResultSet rs, String strMsg) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e + ":" + e.getMessage());
        }
        
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e + ":" + e.getMessage());
            //e.printStackTrace();
        }
        
        try {
            if (con != null) {
                con.commit();
                con.setAutoCommit(true);
                con.close();
            }
        }
        catch (Exception e) {
            logger.error(strMsg + " " + e + ":" + e.getMessage());
        }
    }
    
    /**
     * Get next sequence number from database (for add new record)
     * @param strSeqName
     * @return
     */
    public long getNextSeq(String strSeqName)
                throws SQLException, javax.naming.NamingException {
        long nResult = -1;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT " + strSeqName + ".NEXTVAL FROM dual");
            if (rs.next()) {
                nResult = rs.getLong(1);
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        catch (javax.naming.NamingException e) {
            logger.error(e);
        }
        finally {
            releaseResourceAndCommit(con, stmt, rs,
                    "WSConnectionPooling.getSeqNext() : ");
        }
        return nResult;
    }
}

