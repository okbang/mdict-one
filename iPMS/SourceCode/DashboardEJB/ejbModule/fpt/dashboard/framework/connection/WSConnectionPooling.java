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
 
 package fpt.dashboard.framework.connection;

import java.util.*;
import javax.naming.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.sql.DataSource;
//import com.ibm.db2.jdbc.app.stdext.javax.sql.DataSource;

public class WSConnectionPooling 
{
	private static DataSource ds = null;
	//private final static String strDbName = "java:comp/env/jdbc/DashBoardDB";
	private final static String strDbName = "java:jdbc/DashBoardDB";
	
	public DataSource getDS() 
	{
		try 
		{
			// Note the new Initial Context Factory interface
			// available in Version 4.0
			Hashtable parms = new Hashtable();
			//parms.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
			InitialContext ctx = new InitialContext(parms);

			// Perform a naming service lookup to get the DataSource object.
			ds = (DataSource)ctx.lookup(strDbName);
			System.out.println("DataSource = " + ds);
		} 
		catch (Exception e) 
		{
			System.out.println("Naming service exception: " + e.getMessage());
//			e.printStackTrace();
		}
		
		return ds;		
	}
    
    /**
     * Get connection from datasource
     * @param ds
     * @param con
     */
    public Connection getConnection() {
        Connection con = null;
        try {
            if (ds == null) {
                ds = getDS();
            }
            con = ds.getConnection();
            System.out.println("Connection = " + con);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return con;
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
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }

        try {
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
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }

        try {
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

    /**
     * Method releaseResourceAndRollback.
     * @param con Connection
     * @param stmt Statement
     * @param rs ResultSet
     * @param strMsg Message
     */
    public void releaseResourceAndRollback(Connection con, Statement stmt,
                                         ResultSet rs, String strMsg) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }

        try {
            if (con != null) {
                con.rollback();
                //con.setAutoCommit(true);
                con.close();
            }
        }
        catch (Exception e) {
            System.out.println(strMsg + " " + e.getMessage());
        }
    }
	/**
	 * Get next sequence number from database (for add new record)
	 * @param strSeqName
	 * @return
	 */
	public final long getNextSeq(String strSeqName){
		long nResult = -1;
		Connection conn = null;
		PreparedStatement stm = null;
		WSConnectionPooling WS = new WSConnectionPooling();
		String sql = null;
		ResultSet rs = null;
		sql = "SELECT " + strSeqName + ".NEXTVAL FROM dual";
		try {
			conn = WS.getConnection();
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
			WS.releaseResource(conn, stm, rs,"");
		}
		return nResult;
	}
    
    
    
}

