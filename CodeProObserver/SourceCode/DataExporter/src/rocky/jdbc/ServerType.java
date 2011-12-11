package rocky.jdbc;

public class ServerType {
    private static final String[] TYPENAMES = {"Oracle", "MSSQL", "MySQL", "HSQL"};
    public static final int ORACLE = 0;
    public static final int MSSQL = 1;
    public static final int MYSQL = 2;
    public static final int HSQL = 3;
    
    public static String getTypeName(int type) {
        
        return (type < TYPENAMES.length) ? TYPENAMES[type] : null;
    }
}
