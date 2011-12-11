package rocky.export;

import junit.framework.TestCase;

public class Rdb2ExcelTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testMain() {
        String[] args = {
                "-conf=/AliasConfig.xml",
                "-a=CodeReportCenter",
                "-r=/ExportHSQL.properties",
                "-t=/template/ReportChart-Template.xls",
                "-o=CodeReport_${CURRENT_DATE.yyyyMMddHHmmss}.xls"
        };
        Rdb2Excel.main(args);
    }

    /**
     * 
     */
    public void testMain02() {
        String[] args = {
                "-conf=/AliasConfig02.xml",
                "-a=CodeReportCenter",
                "-r=/ExportDRCenterMSSQL02.properties",
                "-t=/template/DRReport-Template02.xls",
                "-o=Report_${CURRENT_DATE.yyyyMMddHHmmss}.xls"
        };
        Rdb2Excel.main(args);
    }
    
    /**
     * 
     */
    public void testMain03_ProgNews() {
        String[] args = {
                "-conf=/prognews/AliasConfigProgNews.xml",
                "-a=SVNReport",
                "-r=/fuojt-k2/ExportProgNewsHSQL_HAIS.IEM.properties",
                "-t=/templates/Template_CodeReport02.xls",
                "-o=CodeReport_${CURRENT_DATE.yyyyMMddHHmmss}.xls"
        };
        Rdb2Excel.main(args);
    }
    
    /**
     * 
     */
    public void testMain04_SVNReport() {
        String[] args = {
                "-conf=/prognews/AliasConfigProgNews.xml",
                "-a=SVNReport",
                "-r=/fuojt-k2/ExportProgNewsMSSQL_DocSearch.properties",
                "-t=/templates/Template_CodeReport01.xls",
                "-o=CodeReport_${CURRENT_DATE.yyyyMMddHHmmss}.xls"
        };
        Rdb2Excel.main(args);
    }
    
    /**
     * 
     */
    public void testMain05_SVNReport_HSQL() {
        String[] args = {
                "-conf=/prognews/AliasConfigProgNews.xml",
                "-a=SVNReport",
                "-r=/fuojt-k2/ExportProgNewsHSQL_HAIS.IEM.properties",
                "-t=/templates/Template_CodeReport01.xls",
                "-o=CodeReport_${CURRENT_DATE.yyyyMMddHHmmss}.xls"
        };
        Rdb2Excel.main(args);
    }
    
    public void testJPAconfig() {
        String[] args = {
        		"-conftype=jpa",
                "-conf=/persistence.xml",
                "-r=/fuojt-k2/ExportProgNewsHSQL_HAIS.IEM.properties",
                "-t=/templates/Template_CodeReport01.xls",
                "-o=CodeReport_${CURRENT_DATE.yyyyMMddHHmmss}.xls"
        };
        Rdb2Excel.main(args);
    }
}
