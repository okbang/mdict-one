package rocky.export;

import java.util.List;

import junit.framework.TestCase;

public class SqlConfigurationUtilTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParse() {
        List<ReportItemConfigurationInfo> rptItemCfgInfoList = SqlConfigurationUtil.parse("/ExportSQL.properties");
        assertEquals(3, rptItemCfgInfoList.size());
        
        ReportItemConfigurationInfo rptItemCfgInfo = rptItemCfgInfoList.get(0);
        
        assertEquals("RPT01", rptItemCfgInfo.getId());
        assertEquals("Daily_Today", rptItemCfgInfo.getName());
        
        String expectedSQL = "SELECT (Dir.PARENTDIR + '/' + Dir.DIRNAME ) as PATH, VssFile.FileName, Version.Modifier as Modifier, Version.MODIFIED as LastModified, LOC, Comment, Version.Version, Version.Label"
        		+ " FROM Version, VssFile, Dir, VssDB"
        		+ " WHERE Version.DirId = (VssFile.DIRID) AND (Version.FILENAME = VssFile.FILENAME) AND (VSSFILE.DIRID = Dir.DIRID) AND ( VssDB.VssId = Dir.VssId)"
        		+ " AND VssDB.VssPath LIKE '%Group_3%'"
        		+ " AND Dir.ParentDir LIKE '%NEXTBUILD%'"
        		+ " AND VERSION.VERSION = (SELECT MAX(VERSION) FROM VERSION WHERE Version.FILENAME = VssFile.FILENAME)"
        		+ " AND CONVERT(VARCHAR(10), GetDate(),102) = CONVERT(VARCHAR(10), Version.MODIFIED,102)";
        assertEquals(expectedSQL, rptItemCfgInfo.getQuery());
        //System.out.println(rptItemCfgInfo.getQuery());
    }
    
    public void testParse02() {
        List<ReportItemConfigurationInfo> rptItemCfgInfoList = SqlConfigurationUtil.parse("/ExportSQL.properties");
        assertEquals(3, rptItemCfgInfoList.size());
        
        ReportItemConfigurationInfo rptItemCfgInfo = rptItemCfgInfoList.get(2);
        
        assertEquals("RPT03", rptItemCfgInfo.getId());
        assertEquals("Summary_UTD", rptItemCfgInfo.getName());
        
        System.out.println(rptItemCfgInfo.getQuery());
    }

}
