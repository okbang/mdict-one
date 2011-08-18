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
 
 package com.fms1.web;
import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Database connection pooling 
 */
public final class ServerHelper {
	private static ServerHelper _me = null;
	/** A cached copy of the data source */
	private DataSource _ds;
	//be carefull to use instance() instead of constructor
	private ServerHelper(){}
	/**
	 * Answer the default connection for the application
	 */
	public final Connection getConnection() {
		try {
			if (_ds == null) {
				_ds = getDataSource();
				if (_ds == null)
					throw new RuntimeException("No data sources");
			}
			return _ds.getConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    /**
     * Answer the default data source for the application.
     * @return null if you have problems.
     */
    private static DataSource getDataSource() {
        try {
            final Context context = new InitialContext();
            
			DataSource specificDataSource = (DataSource) context.lookup("java:jdbc/fms1");
			
			// For Websphere
            /*DataSource specificDataSource =
                (DataSource) 
                 javax.rmi.PortableRemoteObject.narrow(
                    context.lookup("jdbc/fms1"),
                    DataSource.class);*/
                 
            return specificDataSource;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * returns existing instance of object (or creates one on the first call),
     * that are share for all users 
     */
    public static ServerHelper instance() {
        if (_me == null) {
            _me = new ServerHelper();
        }
        return _me;
    }
    
	public final static void closeConnection(Connection conn, PreparedStatement prepStmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (prepStmt != null)
				prepStmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public final static void closeConnection(Connection conn, Statement prepStmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (prepStmt != null)
				prepStmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
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
        String sql = null;
        ResultSet rs = null;
        sql = "SELECT " + strSeqName + ".NEXTVAL FROM dual";
        try {
            conn = ServerHelper.instance().getConnection();
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
            ServerHelper.closeConnection(conn, stm, rs);
        }
        return nResult;
    }
}
