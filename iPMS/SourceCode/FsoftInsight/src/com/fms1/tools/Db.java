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
 
 package com.fms1.tools;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.fms1.web.Parameters;
import com.fms1.web.ServerHelper;

import oracle.sql.CLOB;//The class12.jar from Oracle client install must be included in the library files
import oracle.sql.BLOB;
/**
 * Database related functions
 * @author manu
 * @date Jun 25, 2004
 */
public class Db {
	public static boolean oracleInit = oracleInit();   // ???

	public static final String getClob(ResultSet rs, String colName) {
		Clob clob = null;
		try {
			clob = rs.getClob(colName);
			if ((clob != null) && (clob.length() > 0)) {
                return (clob.getSubString(1, (int) clob.length()));
			}
			else {
                return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    public static final byte[] getBlob(ResultSet rs, String colName) {
        Blob bl = null;
        try {
            bl = rs.getBlob(colName);
            if ((bl != null) && (bl.length() > 0)) {
                return (bl.getBytes(1,(int)bl.length()));
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static final double getDouble(ResultSet rs, int colNum) {
		try {
			if (rs.getBigDecimal(colNum) == null ) {
                return Double.NaN;
			}
			else {
                return rs.getDouble(colNum);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}
    public static final double getDouble(ResultSet rs, String colName) {
		try {
			if (rs.getBigDecimal(colName) == null ) {
                return Double.NaN;
			}
			else {
                return rs.getDouble(colName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}
    public static final void setDouble(PreparedStatement prepStm, int index, double value) {
		try {
			if (Double.isNaN(value)||Double.isInfinite(value)) {
                prepStm.setNull(index, Types.DOUBLE);
			}
			else {
                prepStm.setDouble(index, value);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Update BLOB data using standard JDBC functions.
     * Please note, before using this function, should provide reference to
     * this function by idValue of idColumn (primary key mostly).
     * If want to insert BLOB data, first INSERT into the table an EMPTY_BLOB()
     * then provide references to this function.
     * This function is not tested, only wrteClob() was tested. If error found
     * then please rewrite the same as writeClob() function: update EMPTY LOB
     * first then use new EMPTY LOB reference for update.
     * @param tableName
     * @param columnName
     * @param idColumn Primary key column
     * @param data
     * @return
     */
    public static boolean writeBlob(String tableName, String lobColumnName,
        String idColumn, String idValue, byte[] data)
    {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        oracle.sql.BLOB oraBlob;
        boolean bResult = true;
        try {
            // Should lock row before trying to update LOB data
            String sqlLockRow =
                "SELECT " + lobColumnName + " FROM " + tableName +
                    " WHERE " + idColumn + " = ? FOR UPDATE";
            String sqlUpdate = 
                "UPDATE " + tableName + " SET " + lobColumnName + " = ? " +
                    "WHERE " + idColumn + " = ?";
            String sqlSetEmpty = "UPDATE " + tableName + " SET " +
                    lobColumnName + " = EMPTY_BLOB() " +
                    " WHERE " + idColumn + " = ?";
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sqlLockRow);
            pstm.setString(1, idValue);
            rs = pstm.executeQuery();
            if (rs.next()) {
                //Use oracle.sql.BLOB because java.sql.Blob lacks setBytes()
                //JDBC3 java.sql.Blob adds the method setBytes(int,byte[])
                //Oracle JDBC uses the method putBytes(int,byte[])
                oraBlob = (oracle.sql.BLOB) rs.getBlob(1);
                // The record is existed but LOB data is null, not empty!!!                
                // =>Update to EMPTY LOB first before using putString() function
                if (oraBlob == null) {
                    // Close old locking
                    rs.close();
                    // Set EMPTY LOB for the LOB column
                    pstm = conn.prepareStatement(sqlSetEmpty);
                    pstm.setString(1, idValue);
                    pstm.executeUpdate();
                    // Open row again with just updated EMPTY LOB
                    pstm = conn.prepareStatement(sqlLockRow);
                    pstm.setString(1, idValue);
                    rs = pstm.executeQuery();
                    // Then provide new references of just updated EMPTY LOB
                    if (rs.next()) {
                        oraBlob = (oracle.sql.BLOB) rs.getBlob(1);
                    }
                }
                // Get data from byte array (binary data)
                oraBlob.putBytes(1, data);
                pstm = conn.prepareStatement(sqlUpdate);    // Update content
                pstm.setBlob(1, oraBlob);
                pstm.setString(2, idValue);
                pstm.executeUpdate();
                pstm.close();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch (Exception e) {
            bResult = false;
            e.printStackTrace();
            try {
                conn.rollback();
            }
            catch (SQLException sqe) {
                sqe.printStackTrace();
            }
        }
        finally {
            ServerHelper.closeConnection(conn, pstm, rs);
            return bResult;
        }
    }
    
    /**
     * Update CLOB data using standard JDBC functions.
     * Please note, before using this function, should provide reference to
     * this function by idValue of idColumn (primary key mostly).
     * If want to insert BLOB data, first INSERT into the table an EMPTY_CLOB()
     * then provide references to this function 
     * @param tableName
     * @param columnName
     * @param idColumn Primary key column
     * @param data
     * @return
     */
    public static boolean writeClob(String tableName, String lobColumnName,
        String idColumn, String idValue, String data)
    {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        oracle.sql.CLOB oraClob = null;
        boolean bResult = true;
        try {
            // Reset to empty LOB first to avoid putString() function issue:
            // when update text shorter than old data, the remain text of
            // old data is still stayed!!!
            String sqlSetEmpty = "UPDATE " + tableName + " SET " +
                    lobColumnName + " = EMPTY_CLOB() " +
                    " WHERE " + idColumn + " = ?";
            // Should lock row before trying to update LOB data
            String sqlLockRow = "SELECT " + lobColumnName +
                    " FROM " + tableName +
                    " WHERE " + idColumn + " = ? FOR UPDATE";
            String sqlUpdate = "UPDATE " + tableName + " SET " +
                    lobColumnName + " = ? " +
                    " WHERE " + idColumn + " = ?";
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
            // Set EMPTY first
            pstm = conn.prepareStatement(sqlSetEmpty);
            pstm.setString(1, idValue);
            pstm.executeUpdate();
            // Then open for update
            pstm = conn.prepareStatement(sqlLockRow);
            pstm.setString(1, idValue);
            rs = pstm.executeQuery();
            if (rs.next()) {
                oraClob = (oracle.sql.CLOB) rs.getClob(1);
                // Get data from string (text data)
                oraClob.putString(1, data);
                pstm = conn.prepareStatement(sqlUpdate);    // Update content
                pstm.setClob(1, oraClob);
                pstm.setString(2, idValue);
                pstm.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch (Exception e) {
            bResult = false;
            e.printStackTrace();
            try {
                conn.rollback();
            }
            catch (SQLException sqe) {
                sqe.printStackTrace();
            }
        }
        finally {
            ServerHelper.closeConnection(conn, pstm, rs);
            return bResult;
        }
    }

    /**
     * @deprecated Please use writeClob() function instead.
	 * needed for CLOB manipulation
	 * don't forget to commit after the transaction
	 */
	public static final void setCLOB(PreparedStatement prepStm, int index, String value) {
		try {
			prepStm.setClob(index, createOracleCLOB( prepStm.getConnection(), value));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * @d eprecated Please use writeBlob() function instead.
     */
    public static final void setBLOB(PreparedStatement prepStm, int index, byte [] value) {
		try {
			prepStm.setBlob(index, createOracleBLOB( prepStm.getConnection(), value));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * @deprecated Because the setCLOB() and setBLOB() functions are deprecated.
     */
	public static final Connection getOracleConn() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(Parameters.connectionString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return conn;
		}
	}
	/**
     * @deprecated Because the setCLOB() and setBLOB() functions are deprecated.<BR><BR>
	 * the connection MUST be obtained from Oracle driver using getOracleConn(),
	 * in order to create the Oracle CLOB
	*/
	public static CLOB createOracleCLOB(Connection conn) {
		return createOracleCLOB( conn, null);
	}
    /**
     * @deprecated Because the setCLOB() and setBLOB() functions are deprecated.
     */
	public static CLOB createOracleCLOB(Connection conn, String val) {
		CLOB clob = null;
		try {
			clob = CLOB.createTemporary(conn, true, CLOB.DURATION_CALL);
			if (val != null) {
                clob.putString(1, val);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return clob;
		}
	}
    /**
     * @deprecated Because the setCLOB() and setBLOB() functions are deprecated.
     */
	public static BLOB createOracleBLOB(Connection conn, byte [] val) {
		BLOB clob = null;
		try {
			clob = BLOB.createTemporary(conn, true, BLOB.DURATION_CALL);
			if (val != null) {
                clob.putBytes(1, val);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return clob;
		}
	}
	//Oracle Driver specific code
	public static boolean oracleInit() {		
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver()); 
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * Delete one row from a table
     * @param:id
     * @return boolean
     */
    public static boolean delete(final long id, final String colname, final String table) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        boolean bl = true;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            sql = "DELETE " + table + " WHERE " + colname + " = " + id;
            stm.executeQuery(sql);
        }
        catch (SQLException e) {
            bl = false;
            e.printStackTrace();
            System.err.println(sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
            return bl;
        }
    }
    
	public static boolean deleteBatch(final String idList, final String colname, final String table) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "DELETE " + table + " WHERE " + colname + " in " + idList;
			stm.executeQuery(sql);
		}
		catch (SQLException e) {
			bl = false;
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return bl;
		}
	}
    
	public static boolean update(final long id, final String colname, final String table, final String colUpdate, final String newValue) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "UPDATE " + table + " SET "+ colUpdate + "="+ newValue + " WHERE " + colname + " = " + id;
			stm.executeQuery(sql);
		}
		catch (SQLException e) {
			bl = false;
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return bl;
		}
	}
	
	public static boolean updateBatch(final String idList, final String colname, final String table, final String colUpdate, final String newValue) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "UPDATE " + table + " SET "+ colUpdate + "="+ newValue + " WHERE " + colname + " IN (" + idList + ")";
			stm.executeQuery(sql);
		}
		catch (SQLException e) {
			bl = false;
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return bl;
		}
	}
    
    // HuyNH2 add for project archived
    public static boolean checkProjectHaveArchive(final long prjID){
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        int countItem = 0;
        boolean returnValue = false;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT COUNT (*) numproject FROM project_archive WHERE project_id = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjID);
            rs = stm.executeQuery();
            while (rs.next()) {
                countItem = rs.getInt("numproject");
            }
            if (countItem > 0) {
                returnValue = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return returnValue;
        }
    }
    public static boolean checkProjecIsArchive(final long prjID){
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        int countItem = 0;
        boolean returnValue = false;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT COUNT (*) numproject FROM project  WHERE project_id = ? AND archive_status = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjID);
            stm.setDouble(2, 4);
            rs = stm.executeQuery();
            while (rs.next()) {
                countItem = rs.getInt(1);
            }
            if(countItem > 0){
                returnValue = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return returnValue;
        }
    }
    // end HuyNH2
}