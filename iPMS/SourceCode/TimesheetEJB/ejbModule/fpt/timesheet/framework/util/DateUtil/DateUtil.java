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
 
 /*******************************************************************************
 * File:        DateUtil.java
 * Author:      Nguyen Thai Son - FPT Software
 * Description: Date utilities
 *              This util will do following tasks:
 *              - format the current date
 - get the number of dates in a month/year
 - get the number of weeks in a month/year
 - get date integer from a full date value (in format "dd/mm/yyyy")
 - get month integer from a full date value (in format "dd/mm/yyyy")
 - get year integer from a full date value (in format "dd/mm/yyyy")
 * Revisions:   2002.01.07 - Nguyen Thai Son - First written
 * Copyright:   Copyright (c) FPT Software. All rights reserved.
 /******************************************************************************/


package fpt.timesheet.framework.util.DateUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private String m_strDate;
    private String m_strTime;
	private Calendar m_CreateDate = Calendar.getInstance();
	public static final String DATE_FORMAT = "dd-MMM-yy";
	private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
	private DecimalFormat decimalFormatter = new DecimalFormat("00");

    //Constructor
    public DateUtil() {

    }

    /**************************************************************************
     Function: public void formatDate()
     Description: format the current date and time.
     Parameters:
     Return: void
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/
    public void formatDate(String strFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
        Date dtCurrentTime = new Date();
        String strDateTime = formatter.format(dtCurrentTime);
        int nPos = strDateTime.indexOf(' ');
        m_strDate = strDateTime.substring(0, nPos);
        m_strTime = strDateTime.substring(nPos + 1, strDateTime.length());
    }



    /////////////////////GET-SET methods////////////////////////////
    /**************************************************************************
     Function: public String getDate()
     Description: return the current date.
     Parameters:
     Return: String
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/
    public String getDate() {
        return this.m_strDate;
    }


    /**************************************************************************
     Function: public String getTime()
     Description: return the current time.
     Parameters:
     Return: String
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/
    public String getTime() {
        return this.m_strTime;
    }


    /**************************************************************************
     Function: public static int getNumDate(int nMonth,int nYear)
     Description: get the number of dates in month/year
     Parameters:
     - nMonth[in]: month
     - nYear[in]: year
     Return: int
     Author: Duong Thanh Nhan
     Created date: 2001
     **************************************************************************/

    public static int getNumDate(int nMonth, int nYear) {
        if (nMonth == 4 || nMonth == 6 || nMonth == 9 || nMonth == 11) {
            return 30;
        }

        if (nMonth == 2) {
            // leap year
            if ((nYear % 4 == 0) && (nYear % 100 != 0)) {
                return 29;
            }
            else
                return 28;
        }

        return 31;
    }


    /**************************************************************************
     Function: public static int getNumWeek(int nMonth,int nYear)
     Description: get the number of weeks in month/year
     Parameters:
     - nMonth[in]: month
     - nYear[in]: year
     Return: int
     Author: Duong Thanh Nhan
     Created date: 2001
     **************************************************************************/
    public static int getNumWeek(int nMonth, int nYear) {
        int tmp = 0;
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 6; i++) {
            calendar.set(nYear, nMonth + i, getNumDate(nMonth + i, nYear));
            if (nMonth > 12) {
                nMonth = 1;
                nYear++;
            }
            tmp = calendar.get(Calendar.WEEK_OF_MONTH);
        }
        return tmp;
    }


    /**************************************************************************
     Function: public static int getDateFromString(String str)
     Description: get date integer from a full date string
     Parameters:
     - str[in]: a date string has format dd/mm/yy (or dd/mm/yyyy)
     Return: int
     Author: Duong Thanh Nhan
     Created date: 2001
     **************************************************************************/

    public static int getDateFromString(String str) {
        String strDay, strMonth, strYear;
        int nMonth, nDay, nYear, nPos1, nPos2;

        nPos1 = str.indexOf("/");
        if (nPos1 < 0)
            return -1;

        //Get the position of the backslash "/" character for DAY
        nPos2 = str.lastIndexOf("/");
        if (nPos2 < 0)
            return -1;

        //Checking the duplicate position of the backslash character
        if (nPos1 == nPos2)
            return -1;

        //Breaking the string into 3 parts of DAY, MONTH, YEAR
        strDay = str.substring(0, nPos1);
        strMonth = str.substring(nPos1 + 1, nPos2);

        //Rounding MONTH & DAY by adding 0
        if (strMonth.length() < 2)
            strMonth = "0" + strMonth;
        if (strDay.length() < 2)
            strDay = "0" + strDay;
        strYear = str.substring(nPos2 + 1);

        return Integer.parseInt(strDay, 10);
    }


    /**************************************************************************
     Function: public static int getMonthFromString(String str)
     Description: get month integer from a full date string
     Parameters:
     - str[in]: a date string has format dd/mm/yy (or dd/mm/yyyy)
     Return: int
     Author: Duong Thanh Nhan
     Created date: 2001
     **************************************************************************/

    public static int getMonthFromString(String str) {
        String strDay, strMonth, strYear;
        int nMonth, nDay, nYear, nPos1, nPos2;

        nPos1 = str.indexOf("/");
        if (nPos1 < 0)
            return -1;

        //Get the position of the backslash "/" character for DAY
        nPos2 = str.lastIndexOf("/");
        if (nPos2 < 0)
            return -1;

        //Checking the duplicate position of the backslash character
        if (nPos1 == nPos2)
            return -1;

        //Breaking the string into 3 parts of DAY, MONTH, YEAR
        strDay = str.substring(0, nPos1);
        strMonth = str.substring(nPos1 + 1, nPos2);

        //Rounding MONTH & DAY by adding 0
        if (strMonth.length() < 2)
            strMonth = "0" + strMonth;
        if (strDay.length() < 2)
            strDay = "0" + strDay;
        strYear = str.substring(nPos2 + 1);
        return Integer.parseInt(strMonth, 10);
    }


    /**************************************************************************
     Function: public static int getYearFromString(String str)
     Description: get year integer from a full date string
     Parameters:
     - str[in]: a date string has format dd/mm/yy (or dd/mm/yyyy)
     Return: int
     Author: Duong Thanh Nhan
     Created date: 2001
     **************************************************************************/

    public static int getYearFromString(String str) {
        String strDay, strMonth, strYear;
        int nMonth, nDay, nYear, nPos1, nPos2;

        nPos1 = str.indexOf("/");
        if (nPos1 < 0)
            return -1;

        //Get the position of the backslash "/" character for DAY
        nPos2 = str.lastIndexOf("/");
        if (nPos2 < 0)
            return -1;

        //Checking the duplicate position of the backslash character
        if (nPos1 == nPos2)
            return -1;

        //Breaking the string into 3 parts of DAY, MONTH, YEAR
        strDay = str.substring(0, nPos1);
        strMonth = str.substring(nPos1 + 1, nPos2);

        //Rounding MONTH & DAY by adding 0
        if (strMonth.length() < 2)
            strMonth = "0" + strMonth;
        if (strDay.length() < 2)
            strDay = "0" + strDay;
        strYear = str.substring(nPos2 + 1);
        return Integer.parseInt(strYear, 10);
    }

    /**
     * Method getCurrentDate.
     * @param nDelta
     * @return String
     */
    public static String getCurrentDate(int nDelta) {
        String strDate = "";
        java.util.Date dtDate = new java.util.Date();
        dtDate.setDate(dtDate.getDate() + nDelta);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        strDate = dateFormat.format(dtDate);
        return strDate;
    }

    /**
     * Method getCurrentTime 
	 * @return strTime
	 */
	public static String getCurrentTime() {
		String strTime = "";
		java.util.Date dtDate = new java.util.Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		strTime = timeFormat.format(dtDate);
		return strTime;
	}

	/**
	 * Method getCurrentTime
	 * @param date
	 * @return timeFormat
	 */
	public static String getCurrentTime(java.util.Date date) {
		if (date == null)
			return "N/A";
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			return timeFormat.format(date);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "N/A";
		}
	}
	
	/**
	 * Method printDiff2Dates
	 * A sound simple question. 
	 * What is the date difference between these two date/time 
	 * 12-31-2002 23:59:59 and 01-01-2003 00:00:01? 
	 * The real time difference is only 2 seconds. 
	 * However, the date/month/year differences are all ones.
	 * The week difference is zero.
	 * The answer will also be different when you are in a different time zone. 
	 * The different dates in London, UK does not mean different dates in Beijing, China. 
	 * @param sdate1 --> "12-31-2002 23:59:59"
	 * @param sdate2 --> "01-01-2003 00:00:01"
	 * @param fmt --> "MM-dd-yyyy HH:mm:ss"
	 * @param tz --> TimeZone.getTimeZone("GMT+08:00")
	 */
	public static void printDiff2Dates(String sdate1, String sdate2, String fmt, TimeZone tz)	{
		SimpleDateFormat df = new SimpleDateFormat(fmt);

		Date date1  = null;
		Date date2  = null;

		try {
			date1 = df.parse(sdate1);
			date2 = df.parse(sdate2);
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}

		Calendar cal1 = null; 
		Calendar cal2 = null;

		if (tz == null)	{
			cal1=Calendar.getInstance();
			cal2=Calendar.getInstance();
		}
		else {
			cal1=Calendar.getInstance(tz); 
			cal2=Calendar.getInstance(tz); 
		}

		// different date might have different offset
		cal1.setTime(date1);
		long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);

		cal2.setTime(date2);
		long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);

		// Use integer calculation, truncate the decimals
		int hr1   = (int)(ldate1/3600000); //60*60*1000
		int hr2   = (int)(ldate2/3600000);

		int days1 = (int)hr1/24;
		int days2 = (int)hr2/24;

		int dateDiff  = days2 - days1;
		int weekOffset = (cal2.get(Calendar.DAY_OF_WEEK) - cal1.get(Calendar.DAY_OF_WEEK))<0 ? 1 : 0;
		int weekDiff  = dateDiff/7 + weekOffset;
		int yearDiff  = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
		int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);

		System.out.println();
		System.out.println("DateTime 1: " + sdate1);
		System.out.println("DateTime 2: " + sdate2);

		System.out.println("Date difference : " + dateDiff);
		System.out.println("Week difference : " + weekDiff);
		System.out.println("Month difference: " + monthDiff);
		System.out.println("Year difference : " + yearDiff);        
	}
}