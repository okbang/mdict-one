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
 
 package com.fms1.tools;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Parameters;
import com.fms1.web.StringConstants;
import java.text.SimpleDateFormat;
/**
 * Various kinds of functions, especially formatting and parsing data
 *
 */
public final class CommonTools {
	public final static DecimalFormat decForm = new DecimalFormat("###.##");
	public final static SimpleDateFormat dateForm = new SimpleDateFormat("dd-MMM-yy");
	public final static SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat dfLong = new SimpleDateFormat("dd-MMM-yyyy");

	
    public static String stringReplace(
        final String mystring, final String replaceWhat, final String replaceBy)
    {
		try {
			if ((mystring == null) || (replaceWhat == null) ||
                (replaceBy == null))
            {
                return null;
            }
			final StringTokenizer strToken =
                new StringTokenizer(mystring, replaceWhat);
			final StringBuffer newString = new StringBuffer();
			boolean first = !mystring.startsWith(replaceWhat);
			while (strToken.hasMoreElements()) {
				if (first)
					first = false;
				else
					newString.append(replaceBy);
				newString.append(strToken.nextToken());
			}
			if (mystring.endsWith(replaceWhat))
				newString.append(replaceBy);
			return newString.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	public static String formatDouble(final double metric) {
		try {
			String tmp;
			if (!Double.isNaN(metric) && !Double.isInfinite(metric)) {
				tmp = decForm.format(metric);
				if (tmp.equals("-0"))
					tmp = "0";
				return tmp;
			}
			else
				return "N/A";
		}
		catch (Exception e) {
			return "N/A";
		}
		
	}
	/**
     * Format number, both Integer and Double number
     * @param metric
     * @param isDouble
     * @return
     */
    public static String formatNumber(
        final double metric, final boolean isDouble)
    {
		if (!Double.isNaN(metric)) {
			DecimalFormat decFormat;
			if (isDouble) {
				decFormat = new DecimalFormat("0.00");
			}
			else {
				decFormat = new DecimalFormat("0");
			}
			decFormat.setDecimalSeparatorAlwaysShown(isDouble);
			return decFormat.format(metric);
		}
		else {
			return "N/A";
		}
	}
	/**
     * Format double value, used for update forms. If the number is NaN then it
     * should display as blank in update pages
     * @param metric
     * @return
     */
    public static String updateDouble(final double metric) {
		if (!Double.isNaN(metric))
			return decForm.format(metric);
		else
			return "";
	}
    /**
     * Convert number to 0 if it's NaN, otherwise keep its value
     * @param number
     * @return
     */
    public static double nanToZero(double number) {
        if (Double.isNaN(number)) {
            return 0;
        }
        else {
            return number;
        }
    }

    public static final double parseDouble(String myDouble) {
        try {
            return Double.parseDouble(myDouble);
        }
        catch (Exception e) {
            return Double.NaN;
        }
    }
	public static final float parseFloat(String myFloat) {
		try {
			return Float.parseFloat(myFloat);
		}
		catch (Exception e) {
			return 0;
		}
	}
    public static final long parseLong(String myInt) {
        try {
            return Long.parseLong(myInt);
        }
        catch (Exception e) {
            return 0;
        }
    }
    public static final int parseInt(String myInt) {
        try {
            return Integer.parseInt(myInt);
        }
        catch (Exception e) {
            return 0;
        }
    }
    /**
     * Add two doubles, if both of them are NaN then the total is NaN, if one of
     * them is NaN then set it to 0 then add them. Otherwise, add them normally
     * @param number1
     * @param number2
     * @return
     */
    public static final double addDouble(
        final double number1, final double number2)
    {
        double total = Double.NaN;
        if (! Double.isNaN(number1) || ! Double.isNaN(number2)) {
            total = (Double.isNaN(number1) ? 0 : number1) +
                    (Double.isNaN(number2) ? 0 : number2);
        }
        return total;
    }
    public static final float addFloat(final float number1, final float number2){
    	float total = Float.NaN;
    	if (! Float.isNaN(number1) || ! Float.isNaN(number2)){
    		total = (Float.isNaN(number1) ? 0 : number1) + 
    				(Float.isNaN(number2) ? 0 : number2);
    	}
    	return total;
    }
    public static final double metricDeviation(double planned, double replanned, double actual) {
    	if (!Double.isNaN(replanned)) {
            planned = replanned;
        }
        if (!Double.isNaN(planned) && (planned != 0) && (!Double.isNaN(actual))) {
            return ((actual - planned) * 100d / planned);
        }
        else
            return Double.NaN;
    }

	public static String formatString(final String string) {
		if (string == null)
			return "N/A";
		if (string.equals(""))
			return "N/A";
		return string;
	}
	public static String updateString(final String string) {
		return (string == null) ? "" : string;
	}
    /**
     * Numbers of days between two dates
     * @param fromDate
     * @param toDate
     * @return
     */
	public static final double dateDiff(final Date fromDate, final Date toDate) {
        double days = 0;
		try {
			final long t1 = fromDate.getTime();
			final long t2 = toDate.getTime();
			final long elapsems = t2 - t1; // in milliseconds
            days = elapsems / 86400000;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        finally {
            return days;
        }
	}
    /**
     * Numbers of days between two dates
     * @param fromDate
     * @param toDate
     * @return
     */
    public static final int daysBetween(final Date fromDate, final Date toDate) {
        return (int) Math.floor(dateDiff(fromDate, toDate));
    }
    /**
     * Calculate recurrent of specified day of week (example SUNDAY) between two dates
     * Used to count numbers of SUNDAY, SATURDAY to subtract effort of
     *      off days (SUNDAY), half-day (SATURDAY)
     * @param fromDate
     * @param toDate
     * @param weekDay (Calendar.SUNDAY, Calendar.MONDAY,...)=>valid from 1 to 7
     * @return
     * @see CommonTools.getActualCalendarEffort(Vector)
     */
    public static final int recurrentBetween(final Date fromDate,
        final Date toDate, final int weekDay)
    {
        int weekdaysFound = 0;
        int days = (int) Math.floor(dateDiff(fromDate, toDate)) + 1;
        if (weekDay >= 1 && weekDay <= 7 && days > 0) {
            // Get day of week of fromDate, example Wednesday, Friday,...
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            int weekDayOfFromDate = cal.get(Calendar.DAY_OF_WEEK);
            // Numbers of days between week day of fromDate and the next weekDay
            // Example if weekDay is Sunday(1) and fromDate is Thursday (5) then
            // numbers of day between Thursday(5) and next Sunday(1) is 3 days
            int scrollDays = (7 - weekDayOfFromDate + weekDay) % 7;
            // Add 6 days in oder to get corrected math function of div/mod
            int collapsedDays = days + 6;
            weekdaysFound = (collapsedDays - scrollDays) / 7;

            /* Please see following example of Jan-2007:
             ---------------------------
               1   2   3   4   5   6   7
             Sun Mon Tue Wed Thu Fri Sat
             --------------------------- 
                   1   2   3   4   5   6
               7   8   9  10 *11  12  13
              14  15  16  17  18  19  20
              21  22  23  24  25  26  27
              28  29  30 *31
             --------------------------- 
              We want to count numbers of Sundays(1) between 11-Jan and 31-Jan
              => From 11-Jan (Thu) to 31-Jan, we see there are 3 Sundays (14,21,28-Jan)
              days = 31 - 11 + 1 = 21
              weekDay = 1 (Sun)
              weekDayOfFromDate = 5 (Thu)
              scrollDays = (7 - 5 + 1) % 7 = 3 (that means from Thu to next Sun is 3 days)
              collapsedDays = 21 + 6 = 27
              weekdaysFound = (27 - 3) / 7 = 3 (that means 3 Sundays between 11-Jan and 31-Jan)
              (You can check more samples with JUnit class of FI_UnitTest project)
            */
        }
        return weekdaysFound;
    }
    
    /**
     * Get numbers of workings day between two dates (fromDate <= toDate)
     * Current working time rule of FSOFT: exclude Sunday and half of Saturday
     * @param fromDate
     * @param toDate
     * @return
     */
    public static double getWorkingDays(final Date fromDate, final Date toDate) {
        double workingDays = Double.NaN;
        if ((fromDate != null) && (toDate != null)) {
            double days = Math.floor(dateDiff(fromDate, toDate)) + 1;
            if (days > 0) {
                double sundays =
                    recurrentBetween(fromDate, toDate, Calendar.SUNDAY);
                double saturdays =
                    recurrentBetween(fromDate, toDate, Calendar.SATURDAY);
                workingDays = days - sundays - saturdays / 2;
            }
        }
        return workingDays;
    }
    
    /**
     * Get ratio of duration up to milestone when compare with the whole
     * duration between fromDate, toDate. If milestone is beyond toDate then
     * return 1
     * @param fromDate
     * @param toDate
     * @param milestone
     * @return
     */
    public static double getMilestoneRatio(final Date fromDate,
        final Date toDate, final Date milestone)
    {
        double ratio = Double.NaN;
        if ((fromDate != null) && (toDate != null) && (milestone != null)) {
            double totalDays = getWorkingDays(fromDate, toDate);
            double milestoneDays = getWorkingDays(fromDate, milestone);
            if (totalDays > 0) {
                if (milestoneDays == 0) { // fromDate and milestone are Sundays
                    ratio = 0;
                }
                else if (Double.isNaN(milestoneDays)) { // milestone before fromDate
                    ratio = 0;
                }
                else if (totalDays > milestoneDays) { // fromDate <= milestone < toDate
                    ratio = milestoneDays / totalDays;
                }
                else { // milestone >= toDate
                    ratio = 1;
                }
            }
        }
        return ratio;
    }
    
	public static final Vector getMondayList(final Date startDate, final Date endDate) {
		Vector mondayList = new Vector();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(startDate);
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
			cal.add(Calendar.DAY_OF_WEEK, 1);
		final Date firstMonday = cal.getTime();
		long weekDuration = 7 * 24 * 60 * 60 * 1000;
		long dateMili = firstMonday.getTime();
		while (dateMili <= endDate.getTime()) {
			final Date theMonday = new Date(dateMili);
			mondayList.add(theMonday);
			dateMili += weekDuration;
		}
		return mondayList;
	}
	public static final String dateFormat(java.util.Date date) {
		if (date == null) {
            return "N/A";
		}
		try {
			return dateForm.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "N/A";
		}
	}
    /**
     * Return string that represent date in long format (dd-MMM-yyyy)
     * @param date
     * @return
     */
    public static String dateFormatLong(java.util.Date date) {
        if (date == null) {
            return null;
        }
        else {
            return dfLong.format(date);
        }
    }
	public static final String dateFormat(double longDate) {
		if (longDate < 0 || Double.isNaN(longDate))
			return "N/A";
		Date date = new Date((long) longDate);
		return dateFormat(date);
	}
	public static final String dateUpdate(java.util.Date date) {
		String retVal = dateFormat(date);
		return (retVal.equals("N/A")) ? "" : retVal;
	}
	public static final String dateUpdate(double longDate) {
		if (longDate < 0 || Double.isNaN(longDate))
			return "";
		Date date = new Date((long) longDate);
		return dateFormat(date);
	}
	public static final String timeFormat(java.util.Date date) {
		if (date == null)
			return "N/A";
		try {
			return timeForm.format(date);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "N/A";
		}
	}
	public static final java.sql.Date toSQLDate(java.util.Date value) {
		return ((value == null) ? null : new java.sql.Date(value.getTime()));
	}
	public static final java.util.Date parseDate(String date) {
		try {
			return dateForm.parse(date);
		}
		catch (Exception e) {
			return null;
		}
	}
	public static final java.sql.Date parseSQLDate(String date) {
		try {
			return new java.sql.Date(dateForm.parse(date).getTime());
		}
		catch (Exception e) {
			return null;
		}
	}
	public static final String parseAllForImportFromExcelFile(String s) {
		return s.equals("null")? "N/A":s;
	}
	public static final String getMonth(final int month) {
		String value = null;
		switch (month) {
			case 1 :
				value = "Jan";
				break;
			case 2 :
				value = "Feb";
				break;
			case 3 :
				value = "Mar";
				break;
			case 4 :
				value = "Apr";
				break;
			case 5 :
				value = "May";
				break;
			case 6 :
				value = "Jun";
				break;
			case 7 :
				value = "Jul";
				break;
			case 8 :
				value = "Aug";
				break;
			case 9 :
				value = "Sep";
				break;
			case 10 :
				value = "Oct";
				break;
			case 11 :
				value = "Nov";
				break;
			case 12 :
				value = "Dec";
				break;
			default :
				value = "Jan";
				break;
		}
		return value;
	}
	public final static String getNoDay(int curMonth, int curYear) {
		Calendar cal = Calendar.getInstance();
		cal.set(curYear, curMonth - 1, 1);
		int nNumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return String.valueOf(nNumDay);
	}
	public final static int getNoWorkingDay(int curDay, int curMonth, int curYear) {
		Calendar cal = Calendar.getInstance();
		cal.set(curYear, curMonth - 1, curDay);
		int nNumWorkingDay;
		// Still need to refine
		int nNumWeek = cal.get(Calendar.WEEK_OF_MONTH) + 1;
		nNumWorkingDay = 1;
		if (curDay > nNumWeek)
			nNumWorkingDay = curDay - nNumWeek;
		return nNumWorkingDay;
	}
	public final static String getMnuFunc(final javax.servlet.http.HttpSession session) {
		return getMnuFunc(session, "");
	}
	public final static String getMnuFunc(final javax.servlet.http.HttpSession session, String param) {
		int nWorkUnitType = Integer.parseInt((String) session.getAttribute("workUnitType"));
		int wuID = Integer.parseInt((String) session.getAttribute("workUnitID"));
		String strMenuType;
		if (wuID == Parameters.SQA_WU || wuID == Parameters.PQA_WU)
			strMenuType = "loadSQAMenu(" + param + ");";
		else if (nWorkUnitType == Constants.RIGHT_ORGANIZATION)
			strMenuType = "loadOrgMenu(" + param + ");";
		else if (nWorkUnitType == Constants.RIGHT_GROUP)
			strMenuType = "loadGrpMenu(" + param + ");";
		else
			strMenuType = "loadPrjMenu(" + param + ");";
		return strMenuType;
	}
	public final static boolean findString(String searched, String keyword) {
		if (searched == null || keyword == null || keyword.length() == 0)
			return false;
		return (searched.toUpperCase().indexOf(keyword.toUpperCase()) >= 0);
	}
	public final static java.sql.Date getTodayMidnight() {
		final Calendar cal = new GregorianCalendar();
		//reset to midight
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTime().getTime());
	}
	/** 
	 * Sort Date
	 * Author:Ngadtt
	 * @param endDate
	 */
	public final static void sortDate(Date arrSortDate[]) {
		Date temp;
		for (int i = 0; i < arrSortDate.length; i++) {
			for (int j = i + 1; j < arrSortDate.length; j++) {
				if (arrSortDate[i].compareTo(arrSortDate[j]) == 1) {
					temp = arrSortDate[i];
					arrSortDate[i] = arrSortDate[j];
					arrSortDate[j] = temp;
				}
			}
		}
	}
	/** 
	 * Sort integer decreasing
	 * Author:Ngadtt
	 * @param endDate
     * @deprecated Do not use manual created class, java.util.Collections
     * class have many method that manipulate arrays, collections like
     * sorting, searching,...
	 */
	public final static void IntDecreaseSort(int arrSortInt[]) {
		int inttemp;
		for (int i = 0; i < arrSortInt.length; i++) {
			for (int j = i + 1; j < arrSortInt.length; j++) {
				if (arrSortInt[i] < arrSortInt[j]) {
					inttemp = arrSortInt[i];
					arrSortInt[i] = arrSortInt[j];
					arrSortInt[j] = inttemp;
				}
			}
		}
	}
	/** 
	 * Sort integer increasing
	 * Author:Ngadtt
	 * @param endDate
     * @deprecated Do not use manual created class, java.util.Collections
     * class have many method that manipulate arrays, collections like
     * sorting, searching,...
	 */
	public final static void IntIncreaseSort(int arrSortInt[]) {
		int inttemp;
		for (int i = 0; i < arrSortInt.length; i++) {
			for (int j = i + 1; j < arrSortInt.length; j++) {
				if (arrSortInt[i] > arrSortInt[j]) {
					inttemp = arrSortInt[i];
					arrSortInt[i] = arrSortInt[j];
					arrSortInt[j] = inttemp;
				}
			}
		}
	}
	public final static void setJustBeforeMidnight(java.sql.Date endDate) {
		final Calendar cal = new GregorianCalendar();
		cal.setTime(endDate);
		//reset to midight
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		endDate.setTime(cal.getTime().getTime());
	}
	/**
	 * The vector must be uniform
	 * 
	 */
	public static final int TYPE_INT = 0;
	public static final int TYPE_DATE = 1;
	public static final int TYPE_LONG = 2;
	public static final int TYPE_STRING = 3;
    /** 
     * @deprecated Do not use manual created class, java.util.Collections
     * class have many method that manipulate arrays, collections like
     * sorting, searching,...
     */
	public final static void sortVector(Vector sortMe) {
		// sort by date
		boolean isFound = true;
		int size = sortMe.size();
		Object obj, objPrev, objTmp;
		double prevVal, val;
		String strprevVal, strval;
		int type = TYPE_INT;
		try {
			if (size > 0) {
				obj = sortMe.elementAt(0);
				if (obj instanceof Date)
					type = TYPE_DATE;
				else if (obj instanceof Integer)
					type = TYPE_INT;
				else if (obj instanceof String)
					type = TYPE_STRING;
				while (isFound) {
					prevVal = 0;
					val = 0;
					strval = "";
					strprevVal = "";
					isFound = false;
					for (int i = 1; i < size; i++) {
						objPrev = sortMe.elementAt(i - 1);
						obj = sortMe.elementAt(i);
						switch (type) {
							case TYPE_INT :
								val = ((Integer) obj).doubleValue();
								prevVal = ((Integer) objPrev).doubleValue();
								break;
							case TYPE_DATE :
								val = (double) ((Date) obj).getTime();
								prevVal = (double) ((Date) objPrev).getTime();
								break;
							case TYPE_STRING :
								strval = (String) obj;
								strprevVal = (String) objPrev;
								break;
						}
						//prevDate = (taskPrev.rePlanDate == null) ? taskPrev.planDate.getTime() : taskPrev.rePlanDate.getTime();
						//date = (task.rePlanDate == null) ? task.planDate.getTime() : task.rePlanDate.getTime();
						if ((type == TYPE_STRING && strval.compareTo(strprevVal) < 0) || (type != TYPE_STRING && (val < prevVal))) {
							objTmp = obj;
							sortMe.setElementAt(objPrev, i);
							sortMe.setElementAt(objTmp, i - 1);
							isFound = true;
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
    /** 
     * @deprecated Do not use manual created class, java.util.Collections
     * class have many method that manipulate arrays, collections like
     * sorting, searching,...
     */
	public final static void sortVector(Vector sortMe, String fieldName) {
		// sort by date
		boolean isFound = true;
		int size = sortMe.size();
		Object obj, objPrev, objTmp;
		Object prevField, field;
		double prevVal, val;
		String strprevVal, strval;
		int type = TYPE_INT;
		try {
			if (size > 0) {
				obj = sortMe.elementAt(0);
				Field thefield = (obj.getClass()).getField(fieldName);
				if (thefield.get(obj) instanceof Date)
					type = TYPE_DATE;
				else if (thefield.get(obj) instanceof Integer)
					type = TYPE_INT;
				else if (thefield.get(obj) instanceof String)
					type = TYPE_STRING;
				while (isFound) {
					prevVal = 0;
					val = 0;
					strval = "";
					strprevVal = "";
					isFound = false;
					for (int i = 1; i < size; i++) {
						objPrev = sortMe.elementAt(i - 1);
						obj = sortMe.elementAt(i);
						field = thefield.get(obj);
						prevField = thefield.get(objPrev);
						switch (type) {
							case TYPE_INT :
								val = ((Integer) field).doubleValue();
								prevVal = ((Integer) prevField).doubleValue();
								break;
							case TYPE_DATE :
								val = (double) ((Date) field).getTime();
								prevVal = (double) ((Date) prevField).getTime();
								break;
							case TYPE_STRING :
								strval = String.valueOf(field);
								strprevVal = String.valueOf(prevField);
								break;
						}
						//prevDate = (taskPrev.rePlanDate == null) ? taskPrev.planDate.getTime() : taskPrev.rePlanDate.getTime();
						//date = (task.rePlanDate == null) ? task.planDate.getTime() : task.rePlanDate.getTime();
						if ((type == TYPE_STRING && strval.compareTo(strprevVal) < 0) || (type != TYPE_STRING && (val < prevVal))) {
							objTmp = obj;
							sortMe.setElementAt(objPrev, i);
							sortMe.setElementAt(objTmp, i - 1);
							isFound = true;
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	public final static Object findVector(Vector sortMe, String fieldName,int value) {
			// sort by date

			int size = sortMe.size();
			Object obj;
			Object field;
			double  val;
			try {
				Field thefield = (sortMe.elementAt(0).getClass()).getField(fieldName);
				val = 0;
				for (int i = 0; i < size; i++) {
					obj = sortMe.elementAt(i);
					field = thefield.get(obj);
					val = ((Integer) field).doubleValue();
					if (val==value) {
						return obj;
					}
				}
				return null;
				
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	/**
	 * The vector must contain at least one item
	 * @return an object that must be casted to the expected array type
	 */
	public final static Object vectorToArray(Vector sortMe, String fieldName) {
		return vectorToArray(sortMe, fieldName, false);
	}
	/**
		 * @return distinct values
		 */
	public final static Object vectorToArrayDistinct(Vector sortMe, String fieldName) {
		return vectorToArray(sortMe, fieldName, true);
	}
	private final static Object vectorToArray(Vector sortMe, String fieldName, boolean distinct) {
		int size = sortMe.size();
		Object obj;
		Object retVal = null;
		Object field;
		int type = TYPE_INT;		
		try {
			
			if (size > 0) {
				obj = sortMe.elementAt(0);
				
				Field thefield = (obj.getClass()).getField(fieldName);
				
                if (thefield.get(obj) instanceof Date) {
					type = TYPE_DATE;
					retVal = new Date[size];
				}
				else if (thefield.get(obj) instanceof Integer) {
					type = TYPE_INT;					
					retVal = new int[size];
				}
				else if (thefield.get(obj) instanceof Long) {
					type = TYPE_LONG;
					retVal = new long[size];
				}
				
                int intVal;
				int intValPrev = 0;
				long longVal;
				long longValPrev = 0;
				int j = 0;
				
				for (int i = 0; i < size; i++) {
					obj = sortMe.elementAt(i);
					field = thefield.get(obj);
					switch (type) {
						case TYPE_INT :
							intVal = ((Integer) field).intValue();
							if (distinct) {
								if (i != 0 && intValPrev != intVal) {
									((int[]) retVal)[j++] = intVal;
								}
								intValPrev = intVal;
							}
							else
								 ((int[]) retVal)[i] = intVal;
							break;
						case TYPE_DATE :
							 ((Date[]) retVal)[i] = (Date) field;
							break;
						case TYPE_LONG :
						
							longVal = ((Long) field).longValue();
							if (distinct) {
								if (i != 0 && longValPrev != longVal) {
									((long[]) retVal)[j++] = longVal;
								}
								longValPrev = longVal;
							}
							else
								 ((long[]) retVal)[i] = longVal;
							break;
					}
				}
				if (distinct) {
					Object uniques = null;
					switch (type) {
						case TYPE_INT :
							uniques = new int[j];
							break;
						case TYPE_DATE :
							uniques = new Date[j];
							break;
						case TYPE_LONG :
							uniques = new long[j];
							break;
					}
					System.arraycopy(retVal, 0, uniques, 0, j);
					return uniques;
				}
			}
			return retVal;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void clone(Object objSource, Object objDest) {
		Field[] fld = objSource.getClass().getFields();
		int actionSize = fld.length;
		try {
			for (int i = 0; i < actionSize; i++) {
				fld[i].setAccessible(true);
				fld[i].set(objDest, fld[i].get(objSource));
			}
		}
		catch (Exception e) {
			//display a lot of shit in debug mode e.printStackTrace();
		}
	}
    public static void totals(double [] vals,double [] nuVals) {
        try {
            for (int i = 0; i < nuVals.length; i++) {
                if (!Double.isNaN(nuVals[i])){
                    if (Double.isNaN(vals[i]))
                        vals[i]=0;
                    vals[i]+=nuVals[i];
                }
                    
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     *scans the array and return the index of value founds 
     *similar to java.utils.Arrays.binarySearch except that the arrays doesnt need to be sorted
     */
    public static int arrayScan(int [] arraySearched,int valueSearched) {
        return arrayScan(arrayToArray(arraySearched),(double) valueSearched);
    }
    public static int arrayScan(double [] arraySearched,double valueSearched) {

        for (int i = 0; i < arraySearched.length; i++) {
            if (arraySearched[i]==valueSearched)
                return i;
        }
        return -1;

    }
    /**
     * 
     * @return sums of array values
     */
    public static double[] arrayToArray(int [] array) {
        double [] retval=new double [array.length] ;
        for (int i=0;i<retval.length;i++){
            retval[i]=(double)array[i];
        }
        return retval;
    }
    public static double arraySum(double [] array) {
        double sum=Double.NaN;
        for (int i = 0; i < array.length; i++) {
            if (!Double.isNaN(array[i])){
                if (Double.isNaN(sum))
                    sum=0d;
                sum+=array[i];
            }
                
        }
        return sum;

    }
    public static double arraySum(double [] array, int from, int to) {
        double sum=Double.NaN;
        if (from > to) {
            int tmp = from;
            from = to;
            to = tmp;
        }
        if (from >= 0) {
            for (int i = from; ((i <= to) && (i < array.length)); i++) {
                if (!Double.isNaN(array[i])){
                    if (Double.isNaN(sum))
                        sum=0d;
                    sum+=array[i];
                }
                
            }
        }
        return sum;
    }
    /**
     * Round each element before calculate sum value
     * @param array
     * @return
     */
    public static double arrayRoundSum(double [] array) {
        double sum=Double.NaN;
        for (int i = 0; i < array.length; i++) {
            if (!Double.isNaN(array[i])){
                if (Double.isNaN(sum))
                    sum=0d;
                sum+=Math.round(array[i]);
            }
                
        }
        return sum;
    }
    /**
     *  0:Developer<BR>
        1:Tester<BR>
        2:Project Leader<BR>
        3:Project Director<BR>
        4:Graphic Designer<BR>
        5:External<BR>
        6:Onsite Coordinator<BR>
        7:SQA<BR>
        8:PQA<BR>
        9:Communicator<BR>      
        role for converting: Developer = {Developer,Communicator}<BR>
                             Project Leader = {Project Leader,Project Director}<BR>
                             Tester/SQA = {Tester,SQA,PQA}<BR>
     * @param response
     * @return
     * @author huynh2
     */
    public static final String getDefectPermission(long response) {
        // developer
        if (response == 0 || response == 9 || response==6 || response==4 ) {
            return "1000000000";
        }
        // project leader
        else if (response == 2 || response == 3) {
            return "1110000000";
        }
        else if (response == 5) {
        	return "0000001000";
        }
        // tester/sqa;
        else if (response == 1 || response == 7 || response == 8) {
            return "1100000000";
        } else {
            return "0000000000";
        }
    }
    /* 
     * HuyNH2 add function     
     * dd-MMM-yy
     */
    public static final boolean dateCompare(String startDate,String endDate,Date compareDate){     
        SimpleDateFormat dateForm = new SimpleDateFormat("dd-MMM-yyyy");      
        boolean result = false;    
        Date start;
        Date end;
        try{
            if(startDate == null || startDate.equals("")){
                start = dateForm.parse("01-Jan-1900");
            }else{
                start = dateForm.parse(startDate);
            }
            if(endDate == null || endDate.equals("")){
                end = dateForm.parse("31-Dec-9999");                
            }else{
                end = dateForm.parse(endDate);
            }            
            if((startDate == null || startDate.equals("")) && (endDate == null || endDate.equals(""))){
                result = true;
            }
            else{
                if (!compareDate.equals(null)){                
                    result = (compareDate.before(end) && compareDate.after(start))||compareDate.equals(end)||compareDate.equals(start);
                }else{
                    result = false;
                }
            }                                  
        }catch(Exception ex){
            result = false;
        }        
        finally{        
            return result;            
        }
    }
	/**
	 * 
	 * @param strField: a field in a Table of Database
	 * @param strValue: value of data put into above field
	 * @param typeQuery: user search data(use clause LIKE) or check data (use =) 
	 * @return
	 */
	public static String doCreateSQLCondition(String strField, String strValue, int typeQuery){
		String strCondition = "";
		if (typeQuery == ConvertString.adSearch_){
			strValue = "'%" + ConvertString.toSql(strValue.trim().toUpperCase(), ConvertString.adText) + "%'";
			strCondition += " AND " + strField + " LIKE " + strValue;
		}
		else if (typeQuery == ConvertString.adCheck){
			strValue = "'" + ConvertString.toSql(strValue.trim().toUpperCase(), ConvertString.adText) + "'";
			strCondition += " AND " + strField + "=" + strValue;
		}
		return strCondition;
	}
	public static String doCreateSQLCondition(String strField, Object value){
		String strCondition = "";
		strCondition += " AND " + strField + "=" + value;
		return strCondition;
	}
	public static String doCreateSQLPrepareStatement(String strField){
		String strCondition = "";
		strCondition += " AND " + strField + "=?";
		return strCondition;
	}
	public static String doCreateSQLPrepareStament(String strField, int typeQuery){
		String strCondition = "";
		strCondition += " AND " + strField;
		if (typeQuery == ConvertString.adSearch_){
			strCondition += " LIKE '%?%'";
		}
		else{
			strCondition += "='?'";
		}
		return strCondition;
	}
	/**
	 * do: declare a new array:
	 * iDemention = 1: create a new array: atrArrName = new Array(); row = 0;
	 * iDemention = 2: create a new array of strArrName[row] = new Array() 
	 * @param strBuffer
	 * @param strArrName
	 * @param iDemention
	 * @param row
	 */
	public static void  doCreateScriptArray(StringBuffer strBuffer, String strArrName, int iDemention, int iRow){
		
		switch (iDemention){
			case 1:
				strBuffer.append("var " + strArrName + " = new Array();\n");
				break;
			case 2:
				strBuffer.append(strArrName + "[");
				strBuffer.append(iRow);
				strBuffer.append("] = new Array(); ");
				break;
    	}
    }
	public static void doSetScriptArray(StringBuffer strBuffer, String strArrName, int iDemention, int iRow, int iCol, String value){
		switch (iDemention){
			case 1:
				strBuffer.append(strArrName + "[");
				strBuffer.append(iRow);
				strBuffer.append("]=");
				strBuffer.append(value);
				break;
			case 2:
				strBuffer.append(strArrName + "[");
				strBuffer.append(iRow);
				strBuffer.append("][");
				strBuffer.append(iCol);
				strBuffer.append("]=");
				strBuffer.append(value);
				break;
		}
	}
	public static String doGenerateFile(String strPrefix, String fileExtend){
		String strFileName = "";
		try{
			String strMillis = String.valueOf(System.currentTimeMillis());
			strFileName = strPrefix + strMillis + fileExtend;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			return strFileName;
		}
	}
	public static String getRealPath(Fms1Servlet servlet){
		String realPath = "";
		try {
			org.apache.log4j.PropertyConfigurator.configure(servlet.getServletContext().getRealPath("/log4j.properties"));
			realPath = servlet.getServletContext().getRealPath(StringConstants.DIRECTORY_DOWNLOAD);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			return realPath;
		}
	}
	public static boolean doDeleteFile(String fileName){
		boolean result = false;
		try {
			File file = new File(fileName);
			if (file.delete()){
				result = true;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			return result;
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
	public static byte[] getFileData(String strFilePath, int nMaxSize){
		byte[] arrData = null;
		try {
			File file = new File(strFilePath);
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
			while ((offset < arrData.length) && (numRead = is.read(arrData, offset, arrData.length-offset)) >= 0) {
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
			e.printStackTrace();
		}
		finally {
			return arrData;
		}
	}
	/**
	 * Get the input day in the next year
	 * @author hieunv1
	 * @since 12/12/2007
	 * @param inputDate
	 * @return
	 */
	public static java.sql.Date getDateForNextYear(java.sql.Date inputDate){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH)+1;
			int year = cal.get(Calendar.YEAR) + 1;

			String date = "" + day + "-" + getMonth(month) + "-" + year;
			return parseSQLDate(date);
		}
		catch (Exception ex){
			ex.printStackTrace();
			return inputDate;
		}
	}
	/**
	 * Get the input date in previous year
	 * @author hieunv1
	 * @since 12/12/2007
	 * @param inputDate
	 * @return
	 */
	public static java.sql.Date getDateForPreviousYear(java.sql.Date inputDate){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH)+1;
			int year = cal.get(Calendar.YEAR) - 1;
			String date = "" + day + "-" + getMonth(month) + "-" + year;
			return parseSQLDate(date);
		}
		catch (Exception ex){
			ex.printStackTrace();
			return inputDate;
		}
	}
	public static Date convertStringToDate(String mask,String inputDate){
		try{
			SimpleDateFormat df = new SimpleDateFormat(mask);
			return df.parse(inputDate);
		}
		catch (Exception ex){
			return null;
		}
	}	
}