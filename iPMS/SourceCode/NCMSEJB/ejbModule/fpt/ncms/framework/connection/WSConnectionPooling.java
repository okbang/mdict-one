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


package fpt.ncms.framework.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import fpt.ncms.constant.JNDI;

/**
 * Class WSConnectionPooling
 * Use Websphere 4.0 Initial Context Factory interface for connection pooling
 * @version 1.0 12-Mar-03
 * @author
 */
public class WSConnectionPooling {
    /** Data Source */
    private static DataSource ds = null;
    /** JNDI string */
    private String strDbName = JNDI.DBNAME;

    /**
     * getDS
     * Look up for JNDI string and get a data source
     * @return  data source
     */
    public DataSource getDS() {
        try {
            Hashtable parms = new Hashtable();
            /*parms.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.ibm.websphere.naming.WsnInitialContextFactory");*/
            InitialContext ctx = new InitialContext(parms);
            ds = (DataSource)ctx.lookup(strDbName);
        }
        catch (Exception e) {
            System.out.println("Naming service exception: " + e.getMessage());
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Method releaseResource.
     * @param con Connection
     * @param stmt Statement
     * @param rs ResultSet
     * @param strMsg Message
     */
    public void releaseResource(Connection con, Statement stmt,
                                ResultSet rs, String strMsg) {
        try {
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
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }
    }

    /**
     * Method releaseResourceAndCommit.
     * @param con Connection
     * @param stmt Statement
     * @param rs ResultSet
     * @param strMsg Message
     */
    public void releaseResourceAndCommit(Connection con, Statement stmt,
                                         ResultSet rs, String strMsg) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.commit();
                con.setAutoCommit(true);
                con.close();
            }
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }
    }
}