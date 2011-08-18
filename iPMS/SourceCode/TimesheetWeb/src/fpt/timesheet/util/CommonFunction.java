/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package fpt.timesheet.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import fpt.timesheet.bean.Report.InquiryReportBean;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;

public class CommonFunction {
	private static Logger logger = Logger.getLogger(CommonFunction.class.getName());
    /**
     * Method defaultFromDate.
     * @return String
     */
    public static String defaultFromDate() {
        String strDate;
        java.util.Date dtDate = new java.util.Date();
        dtDate.setDate(dtDate.getDate() - 6);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        strDate = dateFormat.format(dtDate);
        return strDate;
    }

    /**
     * Method defaultToDate.
     * @return String
     */
    public static String defaultToDate() {
        String strDate;
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        strDate = formatter.format(new Date());
        return strDate;
    }

    /**
     * Method getStartDate.
     * @param nYear
     * @return Calendar
     */
    //Get default start date for weekly report
    public static Calendar getStartDate(int nYear) {
        Calendar cld = Calendar.getInstance();
        //It's previous year
        if (cld.get(Calendar.YEAR) > nYear) {
            cld.set(Calendar.YEAR, nYear);
            cld.set(Calendar.MONTH, Calendar.DECEMBER);
            cld.set(Calendar.DATE, 24);  // = (31 - 7 days)
            cld.add(Calendar.DATE, Calendar.SATURDAY - cld.get(Calendar.DAY_OF_WEEK));
        }
        //Else: it's current year
        //If current date is Saturday
        else if (cld.get(Calendar.DAY_OF_WEEK) >= Calendar.SATURDAY) {
            cld.add(Calendar.DATE, Calendar.SATURDAY - cld.get(Calendar.DAY_OF_WEEK));
            cld.add(Calendar.DATE, -7);
        }
        //Sunday to Friday
        else {
            cld.add(Calendar.DATE, Calendar.SATURDAY - cld.get(Calendar.DAY_OF_WEEK));
            cld.add(Calendar.DATE, -14);
        }

        return cld;
    }

    /**
     * Method getWeeklyFromDate.
     * Get default From Date of current year
     * @return String
     */
    public static String getWeeklyFromDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        Calendar cld = getStartDate(Calendar.getInstance().get(Calendar.YEAR));
        return formatter.format(cld.getTime());
    }

    /**
     * Method getWeeklyFromDate.
     * Get From Date of specific year
     * @param nYear
     * @return String
     */
    public static String getWeeklyFromDate(int nYear) {
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        Calendar cld = getStartDate(nYear);
        return formatter.format(cld.getTime());
    }

    /**
     * Method getWeeklyToDate.
     * @param nYear
     * @return String
     */
    public static String getWeeklyToDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        Calendar cld = getStartDate(Calendar.getInstance().get(Calendar.YEAR));
        cld.add(Calendar.DATE, 6);
        return formatter.format(cld.getTime());
    }

    /**
     * Method getWeeklyToDate.
     * @param nYear
     * @return String
     */
    public static String getWeeklyToDate(int nYear) {
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        Calendar cld = getStartDate(nYear);
        cld.add(Calendar.DATE, 6);
        return formatter.format(cld.getTime());
    }

    /**
     * Method getCurrentDate.
     * @return String
     */
    public static String getCurrentDate() {
        String strDate;
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        strDate = formatter.format(new Date());
        return strDate;
    }

    /**
     * Method getYearsListing.
     * @return int[]
     */
    //Get list of years from current year downto default start year
    public static int[] getYearsListing() {
        int[] arrYears = null;
        Calendar cld = Calendar.getInstance();
        if (cld.get(Calendar.YEAR) < TIMESHEET.TS_START_YEAR) {
            return arrYears;
        }

        arrYears = new int[cld.get(Calendar.YEAR) - TIMESHEET.TS_START_YEAR + 1];
        int nIndex = 0;
        for (int nYear = cld.get(Calendar.YEAR); nYear >= TIMESHEET.TS_START_YEAR; nYear--) {
            arrYears[nIndex] = nYear;
            nIndex++;
        }

        return arrYears;
    }

    /**
     * Method getWeeksListing.
     * @param nYear
     * @return StringMatrix
     */
    //Get all weeks of specified year
    public static StringMatrix getWeeksListing(int nYear) {
        return getWeeksListing(new Integer(nYear).toString());
    }

    /**
     * Method getWeeksListing.
     * @param strYear
     * @return StringMatrix
     */
    //Get all weeks of specified year
    public static StringMatrix getWeeksListing(String strYear) {
        StringMatrix mtxWeeks = new StringMatrix();
        if ((strYear != null) && (strYear.length() > 0)) {
            try {
                // strYear is not a valid year
                if (Integer.parseInt(strYear) < TIMESHEET.TS_START_YEAR) {
                    return mtxWeeks;
                }
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                return mtxWeeks;
            }
        }
        else {
            return mtxWeeks;
        }
        ArrayList alsWeeks = getWeeksArray(Integer.parseInt(strYear));
        Calendar cldSaturday;
        Calendar cldFriday;
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESHEET.DATE_FORMAT);
        StringVector svRow = new StringVector(2);

        Iterator itr = alsWeeks.iterator();
        while (itr.hasNext()) {
            cldSaturday = (Calendar) itr.next();
            cldFriday = (Calendar) cldSaturday.clone();
            cldFriday.add(Calendar.DATE, 6);

            String strSaturday = formatter.format(cldSaturday.getTime());
            String strFriday = formatter.format(cldFriday.getTime());
            svRow.setCell(0, strSaturday);
            svRow.setCell(1, strFriday);
            mtxWeeks.addRow(svRow);
        }

        return mtxWeeks;
    }

    /**
     * Method getWeeksArray.
     * @param nYear
     * @return ArrayList
     */
    //Get list of weeks in specified year
    public static ArrayList getWeeksArray(int nYear) {
        ArrayList alsWeeks = new ArrayList();

        Calendar cldFirst = Calendar.getInstance();
        Calendar cldLast = Calendar.getInstance();

        cldFirst.set(Calendar.YEAR, nYear);
        cldFirst.set(Calendar.MONTH, Calendar.JANUARY);
        cldFirst.set(Calendar.DATE, 1);
        // First Saturday of the year
        cldFirst.add(Calendar.DATE, Calendar.SATURDAY - cldFirst.get(Calendar.DAY_OF_WEEK));
        // Fix when the first Saturday is 07/01 but not 01/01
        if (cldFirst.get(Calendar.DATE) == 7) {
            cldFirst.add(Calendar.DATE, -7);
        }

        // If this week is first week of year then get previous year
        cldLast.add(Calendar.DATE, -7);
        if (cldLast.before(cldFirst)) {
            nYear--;
            cldFirst.set(Calendar.YEAR, nYear);
            cldFirst.set(Calendar.MONTH, Calendar.JANUARY);
            cldFirst.set(Calendar.DATE, 1);
            cldFirst.add(Calendar.DATE, Calendar.SATURDAY - cldFirst.get(Calendar.DAY_OF_WEEK));
        }
        cldLast.add(Calendar.DATE, 7);
        // nYear has changed to previous year (first week of year)
        if (cldLast.get(Calendar.YEAR) > nYear) {
            cldLast.set(Calendar.YEAR, nYear);
            cldLast.set(Calendar.MONTH, Calendar.DECEMBER);
            cldLast.set(Calendar.DATE, 24);  // = (31 - 7 days)
            // Last Saturday of year
            cldLast.add(Calendar.DATE, Calendar.SATURDAY - cldLast.get(Calendar.DAY_OF_WEEK));
        }
        // Else: it's current year
        // This day is Saturday
        else if (cldLast.get(Calendar.DAY_OF_WEEK) >= Calendar.SATURDAY) {
            cldLast.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            cldLast.add(Calendar.DATE, -7);
        }
        else {
            cldLast.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            cldLast.add(Calendar.DATE, -14);
        }
        // Generate list of Saturdays
        Calendar cld = (Calendar) cldLast.clone();
        while (!cld.before(cldFirst)) {
            alsWeeks.add(cld.clone());
            cld.add(Calendar.DATE, -7);
        }
        return alsWeeks;
    }

	/**
	 * Method doGenerateFile
	 * @param strPrefix
	 * @param strExt
	 * @return
	 */
	public static String doGenerateFile(String strPrefix, String strExt) {
		String strMillis = String.valueOf(System.currentTimeMillis());
		String strFileName = strPrefix + strMillis + strExt;
		return strFileName;
	}

	/**
	 * Zip Inquiry Data
	 * Method doZipInquiry 
	 */
	public static void doZipInquiry(InquiryReportBean beanInquiryReport) {
		// Create a buffer for reading the files
		byte[] buf = new byte[65536];

		try {
			// Create the ZIP file
			//Clock milli seconds
            CommonFunction.checkFolderExistCreateNew(InquiryReportBean.getRealPath());
			String strOutFileName = doGenerateFile("ZipInquiryTS_", ".zip");
			String strZipFile = InquiryReportBean.getRealPath() + strOutFileName;
			beanInquiryReport.setZipFile(strZipFile);
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(beanInquiryReport.getZipFile()));
			FileInputStream in = new FileInputStream(beanInquiryReport.getExcelFile());

            // Set compression level to highest level (9).
            out.setLevel(9);
            // Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry("InquiryTimesheet.xls"));

			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			// Complete the entry
			out.closeEntry();
			in.close();
			out.close();
		}
		catch (IOException e) {
		}
		catch (Exception ex) {
			logger.debug("@HanhTN -- ReportBO -- doZipInquiry" + ex.toString());
			ex.printStackTrace();
		}
	}
    /**
     * Check folder is existed, if not, create new folder
     * @param strPath
     */
    public static void checkFolderExistCreateNew(String strPath) {
        java.io.File sysFile = new java.io.File(strPath);
        if (!sysFile.exists()) {
            sysFile.mkdirs();
        }
    }
	/**
	 * Read file into byte array with limitted size nMaxSize
	 * @author trungtn (Jan-05)
	 * @param strFilePath
	 * @param nMaxSize
	 * @return
	 * @throws IOException
	 */
	public static byte[] getFileData(String strFilePath, int nMaxSize)
									throws IOException {
		byte[] arrData = null;
		try {
			java.io.File file = new java.io.File(strFilePath);
			InputStream is = new FileInputStream(file);
			// Get the size of the file
			long length = file.length();
			// File is too large
			if (length > nMaxSize) {
				throw new IOException("File size exceeded.");
			}
			// Create the byte array to hold the data
			arrData = new byte[(int)length];

			// Begin read file
			int offset = 0;
			int numRead = 0;
			while ((offset < arrData.length) && (numRead=is.read(arrData, offset, arrData.length-offset)) >= 0) {
				offset += numRead;
			}
			// Ensure all the bytes have been read in
			if (offset < arrData.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			// Close the input stream and return bytes
			is.close();
		}
		catch (IOException e) {
			logger.error(e);
			throw e;
		}
		return arrData;
	}

	/**
	 * Delete file from real path
	 * @param strFileName
	 * @return
	 */
	public static boolean deleteFile(String strFileName) {
		boolean bResult = false;
		if (strFileName != null) {
			try {
				logger.debug("removing file:" + strFileName);
				java.io.File attFile = new java.io.File(strFileName);
				if (attFile.delete()) {
					bResult = true;
				}
				else {
					logger.error("Cannot remove file:" + strFileName);
				}
			}
			catch (Exception e) {
				logger.error("Cannot remove file :"  + strFileName + ", " +
								   e.getMessage());
			}
		}
		return bResult;
	}
	
}