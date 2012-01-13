package rocky.poi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiUtil {
    final static Logger LOG = Logger.getLogger("PoiUtil");

    public static HSSFWorkbook loadFile(InputStream is) throws FileNotFoundException, IOException {
        HSSFWorkbook wk = null;
        POIFSFileSystem pfs = null;
        // create a Poi File from FileInputStream
        pfs = new POIFSFileSystem(is);
        // create workbook
        wk = new HSSFWorkbook(pfs);

        return wk;
    }

    public static XSSFWorkbook loadFileX(InputStream is) throws FileNotFoundException, IOException {
        XSSFWorkbook wk = null;
        // create workbook
        wk = new XSSFWorkbook(is);

        return wk;
    }

    public static HSSFCell setContent(HSSFRow row, short colIdx, Object value) {
        HSSFCell cell = row.getCell(colIdx);
        if (cell == null) {
            cell = row.createCell(colIdx);
        }

        if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Double) {
            cell.setCellValue(((Double) value).doubleValue());
        } else if (value instanceof Integer) {
            cell.setCellValue(((Integer) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(new HSSFRichTextString(value.toString()));
        }

        return cell;

    }
    public static HSSFCell setContent(HSSFSheet sheet, int rowIdx, short colIdx, Object value) {
        HSSFRow row = sheet.getRow(rowIdx);
        if (row == null) {
            row = sheet.createRow(rowIdx);
        }
        HSSFCell cell = row.getCell(colIdx);
        if (cell == null) {
            cell = row.createCell(colIdx);
        }

        if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Double) {
            cell.setCellValue(((Double) value).doubleValue());
        } else if (value instanceof Integer) {
            cell.setCellValue(((Integer) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(new HSSFRichTextString(value.toString()));
        }

        return cell;
    }

    @Deprecated
    public static Object getValue(HSSFSheet sheet, int rowIdx, short colIdx) {
        HSSFRow row = sheet.getRow(rowIdx);
        Object retValue;
        if (row == null) {
            row = sheet.createRow(rowIdx);
        }

        return getValue(row, colIdx);
    }

    public static Object getValue(HSSFSheet sheet, int rowIdx, int colIdx) {
        HSSFRow row = sheet.getRow(rowIdx);
        Object retValue;
        if (row == null) {
            row = sheet.createRow(rowIdx);
        }

        return getValue(row, colIdx);
    }

    /**
     * Get value of cell.
     * @param sheet
     * @param cellAddr A1, C27 style cell references
     * @return
     */
    public static Object getValue(HSSFSheet sheet, String cellAddr) {
        CellReference cellRef = new CellReference(cellAddr);
        HSSFCell cell = sheet.getRow(cellRef.getRow()).getCell(cellRef.getCol());
        return getValue(cell);
    }

    public static Object getValue(XSSFSheet sheet, String cellAddr) {
        CellReference cellRef = new CellReference(cellAddr);
        XSSFCell cell = sheet.getRow(cellRef.getRow()).getCell(cellRef.getCol());
        return getValue(cell);
    }

    /**
     * Get value of given column of the row.
     * @param row
     * @param colIdx
     * @return Warning: for date format cell, the Double value can be returned.
     */
    public static Object getValue(HSSFRow row, int colIdx) {
        Object retValue = null;
        HSSFCell cell = row.getCell(colIdx);

        return getValue(cell);
    }

    public static Object getValue(XSSFRow row, int colIdx) {
        Object retValue = null;
        XSSFCell cell = row.getCell(colIdx);

        return getValue(cell);
    }

    @Deprecated
    public static Object getValue(HSSFRow row, short colIdx) {
        Object retValue = null;
        HSSFCell cell = row.getCell(colIdx);
        return getValue(cell);
    }

    private static Object getValue(HSSFCell cell) {
        Object retValue = null;
        String strVal;

        if (cell != null) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_FORMULA :
                    // Try to get double value
                    try {
                        retValue = cell.getNumericCellValue();
                        break;
                    } catch (java.lang.IllegalStateException nfex) {
                        // Not a double value
                        //nfex.printStackTrace();
                        retValue = cell.getRichStringCellValue().toString();
                    }
                    // Try to get Date value
                    try {
                        retValue = cell.getDateCellValue();
                    } catch (Exception ex) {
                        retValue = cell.getRichStringCellValue().toString();
                    }
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC :
                    retValue = cell.getNumericCellValue();
                    break;
                case HSSFCell.CELL_TYPE_STRING :
                    retValue = cell.getRichStringCellValue().toString();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN :
                    retValue = cell.getBooleanCellValue();
                    break;

                case HSSFCell.CELL_TYPE_ERROR :
                    LOG.debug("Error (" + cell.getRowIndex() + "," + cell.getColumnIndex() + ")"
                            + cell.getErrorCellValue());
                    retValue = "#N/A";
                    break;
                default :
                    try {
                        retValue = cell.getDateCellValue();
                    } catch (Exception ex) {
                        retValue = cell.getRichStringCellValue().toString();
                    }
            }
        }

        return retValue;
    }

    private static Object getValue(XSSFCell cell) {
        Object retValue = null;
        String strVal;

        if (cell != null) {

            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_FORMULA :
                    // Try to get double value
                    try {
                        retValue = cell.getNumericCellValue();
                        break;
                    } catch (java.lang.IllegalStateException nfex) {
                        // Not a double value
                        //nfex.printStackTrace();
                        retValue = cell.getRichStringCellValue().toString();
                    }
                    // Try to get Date value
                    try {
                        retValue = cell.getDateCellValue();
                    } catch (Exception ex) {
                        retValue = cell.getRichStringCellValue().toString();
                    }
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC :
                    retValue = cell.getNumericCellValue();
                    break;
                case HSSFCell.CELL_TYPE_STRING :
                    retValue = cell.getRichStringCellValue().toString();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN :
                    retValue = cell.getBooleanCellValue();
                    break;

                case HSSFCell.CELL_TYPE_ERROR :
                    LOG.debug("Error (" + cell.getRowIndex() + "," + cell.getColumnIndex() + ")"
                            + cell.getErrorCellValue());
                    retValue = "#N/A";
                    break;
                default :
                    try {
                        retValue = cell.getDateCellValue();
                    } catch (Exception ex) {
                        retValue = cell.getRichStringCellValue().toString();
                    }
            }
        }

        return retValue;
    }

    public static void writeExcelFile(HSSFWorkbook wb, String filename) throws IOException {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
        } finally {
            fileOut.close();
        }
    }
}
