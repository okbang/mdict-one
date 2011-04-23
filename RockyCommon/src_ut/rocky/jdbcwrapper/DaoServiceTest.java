package rocky.jdbcwrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class DaoServiceTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetConnection() {
        fail("Not yet implemented");
    }

    public void testBuildSQL() {
        int i = 0;
        try {
            for (; i < Integer.MAX_VALUE; i++) {
                runBuildSQL();
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        System.out.println("Performed " + i + " queries");
    }
    
    private void runBuildSQL() throws ClassNotFoundException, SQLException {
        String driver = "org.hsqldb.jdbcDriver";
        String url = "jdbc:hsqldb:hsql://localhost/RockyCommonTest";
        String user = "sa";
        String passwd = "";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT MAX(Version) FROM Version WHERE DirId = 0 AND FileName= 'Test.txt'";
        
        try {
            DaoService dao = new DaoService(driver, url, user, passwd);
            conn = dao.getConnection();
            stmt = conn.createStatement();
            stmt.executeQuery(selectQuery);
        } finally {
            DaoService.close(rs);
            DaoService.close(stmt);
            DaoService.close(conn);
        }
        
        
    }

}
