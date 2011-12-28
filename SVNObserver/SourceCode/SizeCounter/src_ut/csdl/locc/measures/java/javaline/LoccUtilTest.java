package csdl.locc.measures.java.javaline;

import junit.framework.TestCase;
import csdl.locc.measures.java.parser.javacc.TokenMgrError;

public class LoccUtilTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testCountLOC() {
        try {
            new LoccUtil().countLOC("D:/Temp/Constants.java", "UTF-8");
        } catch (TokenMgrError tmex) {
            new LoccUtil().countLOC("D:/Temp/Constants.java", null);
            // tmex.printStackTrace();
        }
    }

}
