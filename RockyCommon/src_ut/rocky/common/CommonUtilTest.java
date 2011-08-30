package rocky.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class CommonUtilTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetExtension0001() {
        String ext = CommonUtil.getExtension("D:/Test/abc.java");
        assertEquals("java", ext);
        
    }

    public void testGetExtension00002() {
        String ext = CommonUtil.getExtension("abc.java");
        assertEquals("java", ext);
        
        ext = CommonUtil.getExtension("");
        assertEquals("", ext);
        
        ext = CommonUtil.getExtension("abc");
        assertEquals(null, ext);
        
    }
    public void testFormatPattern01() {
        String strPattern = "Today is ${CURRENT_DATE.yyyyMMdd}";
        String strValue =CommonUtil.formatPattern(strPattern);
        
        System.out.println(strValue);
        
        strPattern = "Today is ${TMP}";
        strValue = CommonUtil.formatPattern(strPattern);
        System.out.println(strValue);
        
        strPattern = "Nothing";
        strValue = CommonUtil.formatPattern(strPattern);
        
        assertEquals(strPattern, strValue);
    }
    
    public void testFormatPattern02() {
        String strPattern = "D:/FSOFT/RAI/InitialTraining/DEV/InitialTraining/Utility/JRockyReport/Wip/Project/ExportedData/DailyReport_${CURRENT_DATE.yyyyMMdd}.xls";
        String strValue =CommonUtil.formatPattern(strPattern);
        
        System.out.println(strValue);
    }
    
    public void testFormatPattern03() {
        String strPattern = "${CURRENT_DATE.yyyyMMdd}.xls";
        String strValue =CommonUtil.formatPattern(strPattern);
        
        System.out.println(strValue);
    }
    
    public void testFormatPattern10() {
        String strPattern = "$ServerName:1234";
        String varPattern = "[\\x24]([a-zA-Z0-9_.]+)";
        Map mapValue = new HashMap();
        mapValue.put("ServerName", "localhost");
        String strValue =CommonUtil.formatPattern(strPattern, varPattern, mapValue);
        
        assertEquals("localhost:1234", strValue);
    }
    
    public void testGetContent1() {
        String resourcePath = "/build.properties";
        String encoding = "utf-8";
        String strValue = "";
        try {
            strValue = CommonUtil.getContent(resourcePath, encoding);
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        System.out.println(strValue);
    }    
}
