/**
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package rocky.sizecounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import rocky.common.CommonUtil;
import rocky.common.PropertiesManager;
/**
 * @author linh
 * 
 */
public class SizeCounterUtil {
    static final Logger log = Logger.getLogger("ISizeCounter");
    /**
     * Count Word's number of page from input directory.
     * 
     * @param filePath
     *            .
     * @return page
     */
    public int countPage(String filePath) {
        FileInputStream is = null;
        int page = 0;
        try {
            is = new FileInputStream(filePath);
            // When file is .DOC
            if (CommonUtil.getExtension(filePath).equals("doc")) {
                HWPFDocument doc = new HWPFDocument(is);
                page = doc.getDocProperties().getCPg();
            }
            // When file is .DOCX
            else if (CommonUtil.getExtension(filePath).equals("docx")) {
                XWPFDocument doc = new XWPFDocument(is);
                XWPFWordExtractor ex = new XWPFWordExtractor(doc);
                page = ex.getExtendedProperties().getUnderlyingProperties().getPages();
            }
        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
            log.warn("File " + getFileName(filePath) + " not found", ex);
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            log.warn("Invalid when reading file.", ex);
        } catch (Exception ex) {
            // TODO: handle exception
            log.warn("Can not count file " + new File(filePath).getName(), ex);
        }
        return page;
    }
    /**
     * Count excel's number of sheet from input directory.
     * 
     * @param filePath
     * @return sheet
     */
    public int countSheet(String filePath) {
        FileInputStream fis;
        int sheet = 0;
        try {
            fis = new FileInputStream(filePath);
            if (CommonUtil.getExtension(filePath).equals("xls")) {
                HSSFWorkbook doc = new HSSFWorkbook(fis);
                sheet = doc.getNumberOfSheets();
            } else if (CommonUtil.getExtension(filePath).equals("xlsx")) {
                XSSFWorkbook doc = new XSSFWorkbook(fis);
                sheet = doc.getNumberOfSheets();
            }
        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            log.warn("Invalid when reading file.", ex);
        } catch (Exception e) {
            // TODO: handle exception
            log.warn("Can not count file " + new File(filePath).getName(), e);
        }
        return sheet;
    }
    /**
     * 
     * @param filePath
     * @return slide
     */
    public int countSlide(String filePath) {
        FileInputStream fis;
        int slide = 0;
        try {
            fis = new FileInputStream(filePath);
            if (CommonUtil.getExtension(filePath).equals("ppt")) {
                HSLFSlideShow show = new HSLFSlideShow(fis);
                slide = show.getDocumentSummaryInformation().getSlideCount();
            } else if (CommonUtil.getExtension(filePath).equals("pptx")) {
                XSLFSlideShow show = new XSLFSlideShow(filePath);
                XSLFPowerPointExtractor ex = new XSLFPowerPointExtractor(show);
                slide = ex.getExtendedProperties().getUnderlyingProperties().getSlides();
            }
        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (OpenXML4JException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (XmlException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            log.warn("Can not count file " + new File(filePath).getName(), e);
        }
        return slide;
    }
    /**
     * getNmTC.
     * 
     * @param filePath
     *            .
     * @return 0
     */
    public int getNmTC(String filePath) {
        FileInputStream fis;
        int tmp = 0, nmtc = 0;
        try {
            fis = new FileInputStream(filePath);
            int i = 0;
            if (CommonUtil.getExtension(filePath).equals("xls")) {
                try {
                    HSSFWorkbook doc = new HSSFWorkbook(fis);
                    HSSFSheet sheet = doc.getSheet("Test Report");
                    for (i = 0; i <= sheet.getLastRowNum(); i++) {
                        if (sheet.getRow(i).getCell(2).getStringCellValue().equals("Sub total")) {
                            tmp = i;
                        }
                    }
                    nmtc = (int) sheet.getRow(tmp).getCell(9).getNumericCellValue();
                } catch (Exception e) {
                    log.warn("Can not count number of UTC in file: " + getFileName(filePath), e);
                }

            } else if (CommonUtil.getExtension(filePath).equals("xlsx")) {
                try {
                    XSSFWorkbook doc = new XSSFWorkbook(fis);
                    XSSFSheet sheet = doc.getSheet("Test Report");
                    for (i = 0; i <= sheet.getLastRowNum(); i++) {
                        if (sheet.getRow(i).getCell(2).getStringCellValue().equals("Sub total")) {
                            tmp = i;
                        }
                    }
                    nmtc = (int) sheet.getRow(tmp).getCell(9).getNumericCellValue();
                } catch (Exception e) {
                    log.warn("Can not count number of UTC in file: " + getFileName(filePath), e);
                }
            }
        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        return nmtc;
    }
    /**
     * get file which is supported from ApplicationResources.properties.
     * 
     * @param proName
     *            .
     * @return extArray
     */
    public ArrayList<String> getResources(String proName) {
        ArrayList<String> extArray = new ArrayList<String>();
        PropertiesManager promanager = null;
        try {
            promanager = new PropertiesManager("/ApplicationResources.properties");

            String fileType = promanager.getProperty(proName);

            String[] exts = fileType.split(" | ");

            for (String ext : exts) {
                extArray.add(ext);
            }
        } catch (Exception ex) {
            log.error("Load configuration file ApplicationResources.properties", ex);
        }
        return extArray;
    }
    /**
     * checkFileType function.
     * 
     * @param proName
     *            .
     * @param extFile
     *            .
     * @return boolean
     */
    public boolean checkFileType(String proName, String extFile) {
        String ext = null;
        ArrayList<String> proArray = new ArrayList<String>();
        proArray = getResources(proName);
        // check file's extent is in Properties
        if (extFile == null) {
            return false;
        }

        int idxLastDot = extFile.lastIndexOf(".");
        if (idxLastDot > -1) {
            ext = extFile.substring(idxLastDot + 1); // get extension without dot
        } else {
            ext = extFile;
        }
        return proArray.contains(ext);
    }
    /**
     * get file's name from filePath without extent.
     * 
     * @param filePath
     *            .
     * @return filename
     */
    public String getFileName(String filePath) {
        File file = new File(filePath);
        // get file's extent
        String ext = CommonUtil.getExtension(filePath);
        // get full file name with extent
        String fullname = file.getName();
        // get file name without extent
        String filename = fullname.replace("." + ext, "");
        return filename;
    }
    /**
     * check file is unit test or not.
     * 
     * @param filename
     *            .
     * @return true if is unit test
     */
    public boolean checkIsUnitTestFile(String filePath) {
        String filename = getFileName(filePath);
        ArrayList<String> unitTestFiles = getResources("UnitTestFiles");
        for (String string : unitTestFiles) {
            if (filename.contains(string))
                return true;
        }
        return false;
    }
}
