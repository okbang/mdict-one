package rocky.poi;

import java.io.InputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rocky.common.CommonUtil;
import junit.framework.TestCase;

public class PoiUtilTest extends TestCase {
    XSSFWorkbook workbook = null;
    protected void setUp() throws Exception {
        InputStream fileTemplate = CommonUtil.loadResource("/SampleExcel.xlsx");
        workbook = new XSSFWorkbook(fileTemplate);
    }

    protected void tearDown() throws Exception {
        
    }

    public void testGetValueHSSFSheetString() {
        XSSFSheet sheet = workbook.getSheet("sheet1");
        assertEquals("Exist data 1", PoiUtil.getValue(sheet, "B2"));
    }

    /**
     * 
     * Check types of data.
     */
    public void testGetManyTypesOfData() {
        XSSFSheet sheet = workbook.getSheet("Data");
        assertEquals(1.0, PoiUtil.getValue(sheet, "B1"));
        assertEquals(0.25, PoiUtil.getValue(sheet, "B2"));
        assertEquals(0.6, PoiUtil.getValue(sheet, "B3"));
        assertEquals("Test", PoiUtil.getValue(sheet, "B4"));
        assertEquals(0.1, PoiUtil.getValue(sheet, "B5"));
        assertEquals("AB", PoiUtil.getValue(sheet, "B6"));
        
        Date date1 = (Date) PoiUtil.getValue(sheet, "B7");
        assertEquals("01-31", date1);
        assertEquals("01-31", PoiUtil.getValue(sheet, "B8"));
        assertEquals("01-31", PoiUtil.getValue(sheet, "B9"));
        
    }
}
