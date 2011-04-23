package rocky.poi;

import java.io.InputStream;

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

}
