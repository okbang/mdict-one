package rocky.sql;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import rocky.common.CommonUtil;
import rocky.common.Constant;

/**
 * This class provides utilities for the query processing.
 * @author Le Ngoc Thach
 *
 */
public class RockySql {
    // Define constants for Server Types
    public final static int ST_HSQL = 0;
    public final static int ST_MSSQL = 1;
    public final static int ST_ORACLE = 2;
    public final static int ST_MYSQL = 3;
    
    public static final String[] SERVE_TYPE_NAMES = {
        "HSQL",
        "MSSQL",
        "Oracle",
        "MySQL"
    };
    
    final static String[] JDBC_URL_PATTERNS = {
        "jdbc:hsqldb:hsql://$serverName/$databaseName", // HSQL
        "jdbc:sqlserver://$serverName:$port;databaseName=$databaseName", // MSSQL
        "jdbc:oracle:thin:@$serverName:$port:$databaseName", // Oracle
        "jdbc:mysql://$serverName:$port/$databaseName" // MySQL
    };
    
    public static final String[] JDBC_DRIVERS = {
        "org.hsqldb.jdbcDriver", // HSQL
        "com.microsoft.sqlserver.jdbc.SQLServerDriver", // MSSQL
        "oracle.jdbc.driver.OracleDriver", // Oracle
        "com.mysql.jdbc.Driver", // MySQL
    };
    
    private Properties props; 
    
    /**
     * Create an instance of RockySql base on resource file.
     * @param baseName File <baseName>.properties is lookup in CLASSPATH 
     */
    public RockySql(Properties props) {
        this.props = props;
    }
    
    /**
     * Get the query by adding subsequence parts.
     * Ex: SQL.RPT01
     * @param prefixKey
     * @return
     */
//    public String getCombineSql(String prefixKey) {
//        String key;
//        /** Contains sorted keys. */
//        Map<String, String> sortedMap = new TreeMap<String, String>();
//        
//        /** Contains combined queries. */
//        StringBuffer sbSql = new StringBuffer();
//        
//        for (Enumeration e = props.keys(); e.hasMoreElements();) {
//            key = (String)e.nextElement();
//            if (key.startsWith(prefixKey)) {
//                sortedMap.put(key, key);
//            }
//        }
//        
//        for (Iterator<String> it = sortedMap.keySet().iterator(); it.hasNext();) {
//            key = it.next();
//            sbSql.append(props.getProperty(key));
//        }
//        
//        return sbSql.toString();
//    }
    
    public Map<String, String> getQueryMap(String prefixKey) {
        String key;
        /** Contains sorted keys. */
        Map<String, String> sortedMap = new TreeMap<String, String>();
        
        /** Contains combined queries. */
        StringBuffer sbSql = new StringBuffer();
        
        for (Enumeration e = props.keys(); e.hasMoreElements();) {
            key = (String)e.nextElement();
            if (key.startsWith(prefixKey)) {
                sortedMap.put(key, (String) props.get(key));
            }
        }
        
        return sortedMap;
    }

    /**
     * Get the query by adding subsequence parts.
     * Ex: SQL.RPT01
     * @param prefixKey Contains sorted pair value (key, value)
     * @param sortedMap
     * @return
     */
    public static String getCombineSql(String prefixKey, Map<String, String> sortedMap) {
        String key;
        
        /** Contains combined queries. */
        StringBuffer sbSql = new StringBuffer();
        
        for (Iterator<String> it = sortedMap.keySet().iterator(); it.hasNext();) {
            key = it.next();
            // Add a space at the end of sql part.
            sbSql.append(sortedMap.get(key)).append(Constant.SPACE);
        }
        
        return sbSql.toString();
    }

    /**
     * Parse the jdbc url pattern.
     * @param urlPattern format: XXX$varnameXXX.
     * Example of Oracle URL pattern: jdbc:oracle:thin:@$serverName:$port:$databaseName
     * @param serverName
     * @param port
     * @param databaseName
     * @return
     */
    public static String buildJdbcUrl(String urlPattern, String serverName, int port, String databaseName) {
        final String varPattern = "[\\x24]([a-zA-Z0-9_.]+)";
        Map mapValue = new HashMap();
        
        mapValue.put("serverName", serverName);
        mapValue.put("port", port);
        mapValue.put("databaseName", databaseName);
        
        return CommonUtil.formatPattern(urlPattern, varPattern, mapValue);
    }
    
    public static String getJdbcUrlPattern(int serverType) {
        return ((0 <= serverType) && (serverType < JDBC_URL_PATTERNS.length)) ? JDBC_URL_PATTERNS[serverType] : null;
    }

    public static String getJdbcDriver(int serverType) {
        return ((0 <= serverType) && (serverType < JDBC_DRIVERS.length)) ? JDBC_DRIVERS[serverType] : null;
    }
}
