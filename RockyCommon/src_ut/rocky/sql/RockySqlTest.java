package rocky.sql;

import junit.framework.TestCase;

public class RockySqlTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBuildJdbcUrl() {
        
        String urlPattern = "jdbc:oracle:thin:@$serverName:$port:$databaseName";
        String serverName = "localhost";
        int port = 1521;
        String databaseName = "HUB";
        String jdbcUrl = RockySql.buildJdbcUrl(urlPattern, serverName, port, databaseName);
        
        assertEquals("jdbc:oracle:thin:@localhost:1521:HUB", jdbcUrl);
    }

}
