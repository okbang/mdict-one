package rockycommon.samples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;

import rocky.common.CommonUtil;
import rocky.poi.PoiUtil;

public class WriteToExcel {
    public static void main(String args[]) {
        Integer retCode = null;
        InputStream fileTemplate;
        try {
            fileTemplate = CommonUtil.loadResource("/ExcelTemplate.xls");
            int currCol = 1;
            int startRow = 1;
            int endRow = 5;
            int targetCol = 2;
            String outFilePath = "WriteToExcel_output.xls";
            
            retCode = fillData(fileTemplate, "Sheet1", currCol, targetCol, startRow, endRow, outFilePath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Return code:" + retCode);
        
        // fillData(is, sheetName, colName, colData, startRow, endRow, outFilePath)
    }

    public static int fillData(InputStream is, String sheetName, int currColIdx, int colData, int startRow, int endRow,
            String outFilePath) throws IOException {
        int errCd = 0;
        HSSFWorkbook workbook = new HSSFWorkbook(is);

        HSSFSheet sheet = workbook.getSheet(sheetName);

        // Check error
        if (sheet == null) {
            errCd = 1;
        }

        // if no error
        String name;
        if (errCd == 0) {
            HSSFRow row;
            HSSFCell nameCell;
            String account;
            for (int idxRow = startRow; idxRow <= endRow; idxRow++) {
                row = HSSFCellUtil.getRow(idxRow, sheet);
                nameCell = HSSFCellUtil.getCell(row, currColIdx);
                if (nameCell != null) {
                    name = nameCell.getRichStringCellValue().getString();
                    System.out.println("Current data at (" + idxRow + "," + currColIdx + ":" + name);
                    PoiUtil.setContent(row, (short) (colData), "New Data " + idxRow);
                }
            }

            // out to file
            workbook.write(new FileOutputStream(outFilePath));
        }

        return errCd;
    }
}
