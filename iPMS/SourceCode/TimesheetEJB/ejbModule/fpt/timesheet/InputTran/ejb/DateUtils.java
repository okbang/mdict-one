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
 
 //***************************************************************************
// DateUtils.java
//
// Created by	: Thaison
// Created date	: Dec 25, 2001
//***************************************************************************

package fpt.timesheet.InputTran.ejb;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class DateUtils {

	/**
	 * Method DateToString
	 * @param d
	 * @param formatter
	 * @return String
	 */
	public static String DateToString(Date d, String formatter) {
        if (d == null) {
            return null;
        }
        else {
            SimpleDateFormat fm = new SimpleDateFormat(formatter);
            String dateString;
            dateString = fm.format(d);
            return dateString;
        }
    }

	/**
	 * Method StringToDate
	 * @param myString
	 * @param formatter
	 * @return
	 */
	public static Date StringToDate(String myString, String formatter) {
        if (myString == null) {
            return null;
        }
        else {
            SimpleDateFormat fm = new SimpleDateFormat(formatter);
            ParsePosition pos = new ParsePosition(0);
            fm.setLenient(false);
            Date d = fm.parse(myString, pos);
            return d;
        }
    }

	/**
	 * Method ReplaceChar
	 * Replace a char ' with a couple of '
	 * @param str
	 * @return String
	 */
	public static String ReplaceChar(String str) {
        int intI = 0;
        String strTemp = "",strSub = "";
        if (str != null) {
            for (intI = 0; intI < str.length(); intI++) {
                strSub = str.substring(intI, intI + 1);
                strTemp = strTemp + str.substring(intI, intI + 1);
                if (strSub.equals("'"))
                    strTemp = strTemp + "'";
            }
        }
        return strTemp;
    }

	/**
	 * Method getCurrentDate
	 * @return
	 * @throws RemoteException
	 */
	public static String getCurrentDate() throws RemoteException {
        return getCurrentDate(0);
    }

	/**
	 * Method getCurrentDate
	 * @param nDelta
	 * @return
	 * @throws RemoteException
	 */
	public static String getCurrentDate(int nDelta) throws RemoteException {
        java.util.Date currentDate = new java.util.Date();
        currentDate.setDate(currentDate.getDate() + nDelta);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String dateString = dateFormat.format(currentDate);

        return dateString;
    }

	/**
	 * Method GetNextDate
	 * @param dateValue
	 * @return
	 */
	public static String GetNextDate(String dateValue) {
        //dateValue Format: mm/dd/yy
        String strResult = "";

        int n1 = 0, n2 = 0;
        n1 = dateValue.indexOf("/");
        n2 = dateValue.lastIndexOf("/");

        int nMonth = Integer.parseInt(dateValue.substring(0, n1), 10);
        int nDay = Integer.parseInt(dateValue.substring(n1 + 1, n2), 10);
        int nYear = Integer.parseInt(dateValue.substring(n2 + 1), 10);

        //Get Next Day
        int nNextDay, nNextMonth, nNextYear;

        if (nMonth != 2) {
            if (nDay < 30) {
                nNextDay = nDay + 1;
                nNextMonth = nMonth;
                nNextYear = nYear;
            }
            else if (nDay == 30) {
                if (nMonth == 1 || nMonth == 3 || nMonth == 5 || nMonth == 7 || nMonth == 8 || nMonth == 10 || nMonth == 12) {
                    nNextDay = nDay + 1;
                    nNextMonth = nMonth;
                    nNextYear = nYear;
                }
                else {
                    nNextDay = 1;
                    nNextMonth = nMonth + 1;
                    nNextYear = nYear;
                }
            }
            else	//nDay = 31
            {
                if (nMonth < 12) {
                    nNextDay = 1;
                    nNextMonth = nMonth + 1;
                    nNextYear = nYear;
                }
                else {
                    nNextDay = 1;
                    nNextMonth = 1;
                    nNextYear = nYear + 1;
                }
            }
        }
        else	//nMonth = 2
        {
            if (nDay < 28) {
                nNextDay = nDay + 1;
                nNextMonth = nMonth;
                nNextYear = nYear;
            }
            else if (nDay == 28) {
                int temp = nYear % 4;
                if ((nYear % 4 == 0))	//la nam Nhuan
                {
                    nNextDay = nDay + 1;
                    nNextMonth = nMonth;
                    nNextYear = nYear;
                }
                else	//la nam Thuong
                {
                    nNextDay = 1;
                    nNextMonth = nMonth + 1;
                    nNextYear = nYear;
                }
            }
            else	//nDay = 29
            {
                nNextDay = 1;
                nNextMonth = nMonth + 1;
                nNextYear = nYear;
            }
        }

        //Formating the next date
        String sNextDay = (nNextDay > 9) ? Integer.toString(nNextDay) : ("0" + Integer.toString(nNextDay));
        String sNextMonth = (nNextMonth > 9) ? Integer.toString(nNextMonth) : ("0" + Integer.toString(nNextMonth));
        String sNextYear = (nNextYear > 9) ? Integer.toString(nNextYear) : ("0" + Integer.toString(nNextYear));

        strResult = sNextMonth + "/" + sNextDay + "/" + sNextYear;
        // System.out.println("Next day:"+strResult);

        return strResult;
    }	//end function

	/**
	 * Method isFirstDateSooner
	 * @param dateString1
	 * @param dateString2
	 * @return boolean
	 */
	public static boolean isFirstDateSooner( String dateString1, String dateString2 ) {

		//System.out.println("@HanhTN -- Current Date: mm/dd/yy = " + dateString1);
		//System.out.println("@HanhTN -- Log  TS Date: mm/dd/yy = " + dateString2);
		//System.out.println("-------------------------------------------");

	    //public static final String FORMAT = "yyyy/MM/dd HH:mm:ss";
	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
	    Calendar cal1;
	    Calendar cal2;
	    Date date1 = null;
	    Date date2 = null;

	    try {
		    date1 = sdf.parse(dateString1);
		    date2 = sdf.parse(dateString2);
	    }
	    catch (ParseException pe) {
	   		pe.printStackTrace();
	    }

	    cal1 = Calendar.getInstance();
	    cal2 = Calendar.getInstance();

	    cal1.setTime(date1);
	    cal2.setTime(date2);

	    return cal1.before(cal2);
    }

	/**
	 * Method compareDate
	 * Compare two dates.
	 * <PRE>
	 * if date1 lthan date2 = -1
	 * if date1   =   date2 = 0
	 * if date1 gthan date2 = 1
	 * </PRE>
	 *
	 * @param date1 is a Java Date object.
	 * @param date2 is a Java Date object.
	 */
	public static int compareDate(Date date1, Date date2) {
	   if (date2.after(date1)) {
		  return (-1);
	   }
	   if (date1.after(date2)) {
		  return (1);
	   }
	   return(0);
	}

	/**
	 * Method isDate1BeforeDate2
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDate1BeforeDate2( String date1, String date2 )
	{
		//System.out.println("@HanhTN -- Current Date: mm/dd/yy = " + date1);
		//System.out.println("@HanhTN -- Log  TS Date: mm/dd/yy = " + date2);
		//System.out.println("-------------------------------------------");

		if ( date1.compareTo(date2) < 0 )  // date1 is before date2
		{
			return true;
		}
		else
		{
			return false;  // date1 is equal to or after the date 2
		}
	}

	/**
	 * Method CompareDate
	 * Purpose: Compare  sStartDate to sEndDate
	 * Input:   sStartDate and sEndDate are dates to compare
	 * 		    sStartDate and sEndDate in MM/dd/yy format
	 * 
	 * Output: -1 if sStartDate is before sEndDate
	 *		    0 if sStartDate is equal to sEndDate
	 *		    1 if sStartDate is after sEndDate
	 * @param sStartDate
	 * @param sEndDate
	 * @return
	 */
	public static int CompareDate(String sStartDate, String sEndDate) {

        int startDay, startMonth, startYear, endMonth, endDay, endYear;
        int n1 = sStartDate.indexOf("/");
        int n2 = sStartDate.lastIndexOf("/");
        int n3 = sEndDate.indexOf("/");
        int n4 = sEndDate.lastIndexOf("/");

        startMonth = Integer.parseInt(sStartDate.substring(0, n1), 10);
        startDay = Integer.parseInt(sStartDate.substring(n1 + 1, n2), 10);
        startYear = Integer.parseInt(sStartDate.substring(n2 + 1), 10);

        endMonth = Integer.parseInt(sEndDate.substring(0, n3), 10);
        endDay = Integer.parseInt(sEndDate.substring(n3 + 1, n4), 10);
        endYear = Integer.parseInt(sEndDate.substring(n4 + 1), 10);

        //System.out.println("In CompareDate(): Start Date: mm/dd/yy = " + startDay +"/"+startMonth+"/"+startYear);
        //System.out.println("In CompareDate(): End   Date: mm/dd/yy = " + endDay +"/"+endMonth+"/"+endYear);
	    //System.out.println("-------------------------------------------");

        if (startYear > endYear)
            return 1;
        if (startYear < endYear)
            return -1;
        // Now startYear == endYear
        if (startMonth > endMonth)
            return 1;
        if (startMonth < endMonth)
            return -1;
        // Now startMonth == endMonth
        if (startDay > endDay)
            return 1;
        if (startDay < endDay)
            return -1;
        return 0;
    } // compareDate

	/**
	 * Method IsSunday
	 * @param strDate
	 * @return boolean
	 */
	public static boolean IsSunday(String strDate) {
        java.util.Date dtDay = StringToDate(strDate, "MM/dd/yy");
        int nDay = dtDay.getDay();
        if (nDay == 0)	//Sunday
        {
            //System.out.println(strDate + " is Sunday!");
            return true;
        }
        return false;
    }

	/**
	 * Method getDateFromString
	 * str format is MM/dd/yy 
	 * @param str
	 * @return int 
	 */
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
        strMonth = str.substring(0, nPos1);
        strDay = str.substring(nPos1 + 1, nPos2);

        //Rounding MONTH & DAY by adding 0
        if (strMonth.length() < 2)
            strMonth = "0" + strMonth;
        if (strDay.length() < 2)
            strDay = "0" + strDay;
        strYear = str.substring(nPos2 + 1);

        return Integer.parseInt(strDay, 10);
        //nYear = parseInt(strYear,10);
    }

	/**
	 * Method getMonthFromString
	 * str format is mm/dd/yy
	 * @param str
	 * @return int
	 */
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
        strMonth = str.substring(0, nPos1);
        strDay = str.substring(nPos1 + 1, nPos2);

        //Rounding MONTH & DAY by adding 0
        if (strMonth.length() < 2)
            strMonth = "0" + strMonth;
        if (strDay.length() < 2)
            strDay = "0" + strDay;
        strYear = str.substring(nPos2 + 1);
        return Integer.parseInt(strMonth, 10);
    }

	/**
	 * Method getYearFromString
	 * str format is mm/dd/yy
	 * @param str
	 * @return int
	 */
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
        strMonth = str.substring(0, nPos1);
        strDay = str.substring(nPos1 + 1, nPos2);

        //Rounding MONTH & DAY by adding 0
        if (strMonth.length() < 2)
            strMonth = "0" + strMonth;
        if (strDay.length() < 2)
            strDay = "0" + strDay;
        strYear = str.substring(nPos2 + 1);
        return Integer.parseInt(strYear, 10);
    }

    /**
     * Method getNumOfDates
	 * @param strFromDate
	 * @param strToDate
	 * @return int
	 */
	public static int getNumOfDates(String strFromDate, String strToDate) {

        int nTotal = 0;
        boolean bOK = true;
        String strDate = strFromDate;
        while (bOK) {
            if (CompareDate(strDate, strToDate) == 1)
                bOK = false;	//FromDate > ToDate
            else {
                //Is Sunday
                //if (DateUtils.IsSunday(strDate))
                //	strDate = GetNextDate(strDate);
                nTotal++;
                strDate = GetNextDate(strDate);
            }
        }
        return nTotal;
    }

    /**
     * Method getArrayOfMonths
	 * @param strFrom
	 * @param strTo
	 * @return int[]
	 */
	public static int[] getArrayOfMonths(String strFrom, String strTo) {

        int[] arrResult = null;
        int nTotal = getNumOfDates(strFrom, strTo);
        arrResult = new int[nTotal];

        String strDate = strFrom;
        for (int i = 0; i < nTotal; i++) {
            int nMonth = getMonthFromString(strDate);
            arrResult[i] = nMonth;
            strDate = GetNextDate(strDate);
        }

        //System.out.println("In DateUtils: nMonth of FromDate = " + arrResult[0]);
        //System.out.println("In DateUtils: nMonth of ToDate = " + arrResult[nTotal-1]);

        return arrResult;
    }

    /**
     * Method getArrayOfFullDates
	 * @param strFrom
	 * @param strTo
	 * @return String[]
	 */
	public static String[] getArrayOfFullDates(String strFrom, String strTo) {
        String[] arrResult = null;
        int nTotal = getNumOfDates(strFrom, strTo);
        arrResult = new String[nTotal];
        boolean bOK = true;
        String strDate = strFrom;
        int i = 0;
        while (bOK) {
            if (CompareDate(strDate, strTo) == 1)
                bOK = false;	//FromDate > ToDate
            else {
                //Is Sunday
                //if (IsSunday(strDate))
                //	strDate = GetNextDate(strDate);
                arrResult[i] = strDate;
                strDate = GetNextDate(strDate);
                i++;
            }
        }
        return arrResult;
    }

    /**
     * Method getArrayOfWorkingDates
	 * @param strFrom
	 * @param strTo
	 * @return String[]
	 */
	public static String[] getArrayOfWorkingDates(String strFrom, String strTo) {
        int intNumberOfDates;
        Vector vec = new Vector();
        String[] arrWorkingDates = null;

        String[] arrDates = getArrayOfFullDates(strFrom, strTo);
        for (int i = 0; i < arrDates.length; i++) {
            if (!DateUtils.IsSunday(arrDates[i])) {
                vec.addElement(arrDates[i]);
            }
        }
        intNumberOfDates = vec.size();
        //String[] arrWorkingDates = (String[])vec.toArray();
        arrWorkingDates = new String[intNumberOfDates];
        vec.copyInto(arrWorkingDates);

        return arrWorkingDates;
    }
    
    /**
     * Method getArrayOfDates
	 * @param strFrom
	 * @param strTo
	 * @return int[]
	 */
	public static int[] getArrayOfDates(String strFrom, String strTo) {
        int[] arrResult = null;
        int nTotal = getNumOfDates(strFrom, strTo);
        arrResult = new int[nTotal];

        String strDate = strFrom;
        for (int i = 0; i < nTotal; i++) {
            int nDate = getDateFromString(strDate);
            if (IsSunday(strDate))
                nDate = nDate + 100;
            arrResult[i] = nDate;
            strDate = GetNextDate(strDate);
        }
        return arrResult;
    }

}