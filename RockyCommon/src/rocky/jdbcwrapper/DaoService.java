/**
 * DaoService.java
 * Copyright Rocky.
 */package rocky.jdbcwrapper;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import rocky.common.LogService;

/**
 * This class provide data access logic
 */
public class DaoService {
    /** String pattern of column name in the query template. It is ${column-name} */
    private static final String COLUMN_NAME_PATTERN = "[\\x24][\\{]([a-zA-Z0-9_]+)[\\}]";
    
    static Logger LOGGER = Logger.getLogger(DaoService.class);
    
    /** The connection to access to the database. */
    private Connection conn;
    
    private String jdbcDriver = null;
    private String jdbcUrl = null;
    private String jdbcUser = null;
    private String jdbcPasswd = null;
    
    private DataSource ds = null;

    /**
     * Create the instance of DaoService with JDBC configuration.
     * @param jdbcDriver
     * @param jdbcUrl
     * @param jdbcUser
     * @param jdbcPasswd
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public DaoService(String jdbcDriver, String jdbcUrl, String jdbcUser, String jdbcPasswd)
            throws ClassNotFoundException {
        this.jdbcDriver = jdbcDriver;
        this.jdbcUrl = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPasswd = jdbcPasswd;
        Class.forName(jdbcDriver);
    }
    
    public DaoService(DataSource ds) {
        this.ds = ds;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }
    
	public Connection getConnection() {
	    boolean createdSuccConn = false; 
	    final int RETRY_TIMES = 10;
	    
	    if (ds != null) {
	        try {
                conn = ds.getConnection();
            } catch (SQLException sqlex) {
                LOGGER.error(this.getClass(), sqlex);
            }
	    } else {

            for (int i = 1; i <= RETRY_TIMES && !createdSuccConn; i++) {
                try {
                    if (conn == null || conn.isClosed()) {
                        conn = null;
                        if (i > 1) {
                            LOGGER.debug("Retry getting the connection " + i);
                        }
                        conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPasswd);
                        createdSuccConn = true;
                    }
                } catch (SQLException sqlex) {
                    if (i == RETRY_TIMES) {
                        LOGGER.error(this.getClass(), sqlex);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException iex) {
                        LOGGER.error(this.getClass(), iex);
                    }
                }
            }
        }
	    if (conn != null) {
            try {

                conn.setAutoCommit(true);
            } catch (SQLException sqlex) {
                LOGGER.error(this.getClass(), sqlex);
            }
        }
		return conn;
	}
	
	public void close() {
	    if (conn != null) {
	        try {
                conn.close();
            } catch (SQLException sqlex) {
                LOGGER.error(this.getClass(), sqlex);
            }
	    }
	}

	public void commit() throws SQLException {
	    conn.commit();
	}

	public void rollback() throws SQLException {
	    conn.rollback();
	}

    public static PreparedStatement buildSQL(Connection conn, String sqlTemplate, Map<String, Object> mapValue)
            throws SQLException {
        Pattern pattern = Pattern.compile(COLUMN_NAME_PATTERN);
        Matcher matcher = pattern.matcher(sqlTemplate);
        StringBuffer sb = new StringBuffer();
        String key;
        // Mapping index and parameter
        Map<Integer, String> idxValueMap = new TreeMap<Integer, String>();
        int index = 1; // start with 1
        Object objVal;

        // Find column name
        while (matcher.find()) {
            // replace the column name pattern by question mark
            matcher.appendReplacement(sb, "?");
            key = matcher.group(1);
            objVal = mapValue.get(key);
            LOGGER.debug(key + "='" + objVal + "'");
            idxValueMap.put(index++, key);
        }
        // append the tail of the query template to the String buffer
        matcher.appendTail(sb);
        matcher.reset();

        final String sqlStatement = sb.toString(); // the standard query with question mark as parameters
        LOGGER.debug("JDBC sql='" + sqlStatement + "'");
        PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
        
        // the while loop can be replaced by for loop for performance
        index = 0;
        for (Object keyParam : idxValueMap.values()) {
            //key = idxValueMap.get(idxParam);
            objVal = mapValue.get(keyParam);
            LOGGER.debug("index=" + index + ";keyParam='" + keyParam + "';value='" + objVal + "'");
            if (objVal == null) {
                pstmt.setString(++index, null);
            } else {
                if (objVal instanceof Integer) {
                    pstmt.setInt(++index, (Integer) objVal);
                    //LOGGER.debug(key + "=" + (Integer) objVal);
                } else if (objVal instanceof java.util.Date) {
                    //java.sql.Date sqlDate = new java.sql.Date();
                    //pstmt.setDate(++index, sqlDate);
                    Timestamp timestamp = new Timestamp(((Date) objVal).getTime());
                    pstmt.setTimestamp(++index, timestamp);
                    //LOGGER.debug(key + "=" + sqlDate);
                } else if (objVal instanceof String) {
                    pstmt.setString(++index, objVal.toString());
                    //LOGGER.debug(key + "=" + objVal.toString());
                } else if (objVal instanceof Boolean) {
                    pstmt.setBoolean(++index, (Boolean)objVal);
                    //LOGGER.debug(key + "=" + (Boolean)objVal);
                } else { // unknown type of the value
                    throw new SQLException("Unknown type of the value '" + objVal + "'");
                }
            }

        }
        
        return pstmt;
    }
    
    public static final void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException sqlex) {
            // Do nothing
        }
    }

    public static final void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException sqlex) {
            // Do nothing
        }
    }
    
    public static final void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqlex) {
            // Do nothing
        }
    }
	/**
	 * Execute a query with a give connection.
	 * @param conn Database access connectivity.
	 * @param sqlTemplate The query
	 * @param mapValue the map table of column value
	 * @return ResultSet Result of the query
	 * @throws SQLException 
	 */
    /*
	public static ResultSet executeSelect(final Connection conn, final String sqlTemplate, final Map mapValue) throws SQLException {
	    PreparedStatement pstmt = DaoService.buildSQL(conn, sqlTemplate, mapValue);
	    return pstmt.executeQuery();
	}
	*/

    public static boolean testConnection(String jdbcDriver, String jdbcUrl, String jdbcUser, String jdbcPassswd) {
        boolean testConn = false;
        Connection conn = null;
        try {
            conn = new DaoService(jdbcDriver, jdbcUrl, jdbcUser, jdbcPassswd).getConnection();
            testConn = (conn != null);
        } catch (ClassNotFoundException cnfex) {
            LogService.logWarn(cnfex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlex) {
                    LogService.logWarn(sqlex);
                }
            }
        }
        return testConn;
    }

	/**
	 * Execute a single query.
	 * @param conn Database access connectivity.
	 * @param sqlTemplate The query
	 * @return ResultSet Result of the query
	 * @throws SQLException 
	 * @throws SQLException 
	 */
/*    
	public ResultSet executeSelect(final String sqlTemplate, final Map mapValue) throws SQLException {
	    Connection conn = getConnection();
	    PreparedStatement pstmt = null;
        try {
            pstmt = buildSQL(this.conn, sqlTemplate, mapValue);
            return pstmt.executeQuery();
        } catch (SQLException sqlex) {
            throw sqlex;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException sqlex) {
                throw sqlex;
            }
            try {
                conn.close();
            } catch (SQLException sqlex) {
                throw sqlex;
            }
        }
	}
*/	
}