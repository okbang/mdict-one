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
 * File:        CalendarUtil.java
 * Author:      Duong Thanh Nhan - FPT Software
 * Description: Represents a calendar
 *              This util will do following tasks:
 *              - format the current date
 * Revisions:   2001 	   - Duong Thanh Nhan - First written
 *				2002.01.07 - Nguyen Thai Son - Modified
 * Copyright:   Copyright (c) FPT Software. All rights reserved.
 /******************************************************************************/

package fpt.timesheet.framework.util.DateUtil;

import java.util.Date;
import java.util.StringTokenizer;

public class CalendarUtil extends Object implements java.io.Serializable {
    public static final int MONTH = java.util.Calendar.MONTH;
    public static final int DATE = java.util.Calendar.DATE;
    public static final int YEAR = java.util.Calendar.YEAR;

    private int m_nMonth;
    private int m_nDay;
    private int m_nYear;


    /**
     * Method CalendarUtil.
     * @param year
     * @param month
     * @param day
     */
    private CalendarUtil(int year, int month, int day) {
        this.m_nMonth = month;
        this.m_nDay = day;
        this.m_nYear = year;
    } //CalendarUtil

    /**
     * Method getInstance.
     * get the current instance of Calendar object.
     * @return CalendarUtil
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public static CalendarUtil getInstance() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        int m = c.get(java.util.Calendar.MONTH);
        int d = c.get(java.util.Calendar.DATE);
        int y = c.get(java.util.Calendar.YEAR);
        return new CalendarUtil(y, m, d);
    }

    /**
     * Method set.
     * sets this object to a given year, month, day.
     * @param year
     * @param month
     * @param day
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public void set(int year, int month, int day) {
        this.m_nMonth = month;
        this.m_nDay = day;
        this.m_nYear = year;
    }

    /**
     * Method set.
     * set the MONTH or YEAR or DATE.
     * @param target
     * @param value
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public void set(int target, int value) {
        switch (target) {
            //MONTH=2
            case java.util.Calendar.MONTH:
                this.m_nMonth = value;
                break;
                //YEAR=1
            case java.util.Calendar.YEAR:
                this.m_nYear = value;
                break;
                //DATE=5
            case java.util.Calendar.DATE:
                this.m_nDay = value;
                break;
        }
    }

    /**
     * Method setTime.
     * @param date
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public void setTime(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        this.m_nDay = c.get(java.util.Calendar.DATE);
        this.m_nMonth = c.get(java.util.Calendar.MONTH);
        this.m_nYear = c.get(java.util.Calendar.YEAR);
    }

    /**
     * Method setCalendarTime.
     * @param cal
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public void setCalendarTime(java.util.Calendar cal) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DATE));
        this.m_nDay = cal.get(java.util.Calendar.DATE);
        this.m_nMonth = cal.get(java.util.Calendar.MONTH);
        this.m_nYear = cal.get(java.util.Calendar.YEAR);
    }//setCalendarTime

    /**
     * Method setFullDateString.
     * @param mmddyyyy
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public void setFullDateString(String mmddyyyy) {
        StringTokenizer token = new StringTokenizer(mmddyyyy, "/");
        int mm = -1;
        int dd = -1;
        int yyyy = -1;
        if (token.hasMoreTokens()) {
            mm = Integer.parseInt(token.nextToken());
            mm--;
        }
        if (token.hasMoreTokens()) {
            dd = Integer.parseInt(token.nextToken());
        }
        if (token.hasMoreTokens()) {
            yyyy = Integer.parseInt(token.nextToken());
        }

        java.util.Calendar dt = java.util.Calendar.getInstance();
        dt.set(yyyy, mm, dd);
        this.m_nDay = dt.get(java.util.Calendar.DATE);
        this.m_nMonth = dt.get(java.util.Calendar.MONTH);
        this.m_nYear = dt.get(java.util.Calendar.YEAR);
    }//setFullDateString


    /**
     * Method setCloudscapeDateString.
     * set a string with yyyymmdd format into the Cloudscape date
     * @param yyyymmdd A date string that formatted as yyyymmdd
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public void setCloudscapeDateString(String yyyymmdd) {
        StringTokenizer token = new StringTokenizer(yyyymmdd, "-");
        int mm = -1;
        int yyyy = -1;
        int dd = -1;
        if (token.hasMoreTokens()) {
            yyyy = Integer.parseInt(token.nextToken());
        }
        if (token.hasMoreTokens()) {
            mm = Integer.parseInt(token.nextToken());
            mm--;
        }
        if (token.hasMoreTokens()) {
            dd = Integer.parseInt(token.nextToken());
        }

        java.util.Calendar dt = java.util.Calendar.getInstance();
        dt.set(yyyy, mm, dd);
        this.m_nDay = dt.get(java.util.Calendar.DATE);
        this.m_nMonth = dt.get(java.util.Calendar.MONTH);
        this.m_nYear = dt.get(java.util.Calendar.YEAR);
    }//setCloudscapeDateString


    /**
     * Method getMonth.
     * @return int
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public int getMonth() {
        return m_nMonth;
    }//getMonth

    /**
     * Method getDay.
     * @return int
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public int getDay() {
        return m_nDay;
    }//getDay

    /**
     * Method getYear.
     * @return int
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public int getYear() {
        return m_nYear;
    }//getYear


    /**
     * Method getTime.
     * get time of the current calendar
     * @return Date
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public Date getTime() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(m_nYear, m_nMonth, m_nDay);
        return c.getTime();
    }//getTime


    /**
     * Method get.
     * get the current month, year, or day
     * @param target
     * @return int
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public int get(int target) {
        switch (target) {
            //MONTH=2
            case java.util.Calendar.MONTH:
                return this.m_nMonth;
                //YEAR=1
            case java.util.Calendar.YEAR:
                return this.m_nYear;
                //DATE=5
            case java.util.Calendar.DATE:
                return this.m_nDay;
            default:
                return -1;
        }
    }//get


    /**
     * Method getFullDateString.
     * get the date encoded in the format  mm/dd/yyyy {mm = 1..12}
     * @return String String in format mm/dd/yyyy
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public String getFullDateString() {
        return ((m_nMonth + 1 < 10) ? "0" : "") + (m_nMonth + 1) + "/" + ((m_nDay < 10) ? "0" : "") + m_nDay + "/" + ((m_nYear < 10) ? "0" : "") + m_nYear;
    }//getFullDateString

    /**
     * Method getCloudscapeDateString.
     * get the Cloudscape date string of the current date
     * @return String String in format "yyyy-mm-dd"
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public String getCloudscapeDateString() {
        return m_nYear + "-" + ((m_nMonth + 1 < 10) ? "0" : "") + (m_nMonth + 1) + "-" + ((m_nDay < 10) ? "0" : "") + m_nDay;
    }//getCloudscapeDateString


    /**
     * Method getCalendarTime.
     * get the Calendar object of the current date
     * @return Calendar
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public java.util.Calendar getCalendarTime() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(this.m_nYear, this.m_nMonth, this.m_nDay);
        return c;
    }//getCalendarTime


    /**
     * Method IsBeforeToday.
     * returns true if the date passed as parameter with format
     * (int day, int month, int year) is BEFORE today's date.
     * @param day input date
     * @param month input month
     * @param year input year
     * @return boolean
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public static boolean IsBeforeToday(int day, int month, int year) {
        java.util.Calendar today = java.util.Calendar.getInstance();
        java.util.Calendar dt = java.util.Calendar.getInstance();
        dt.set(year, month, day);
        if (dt.before(today))
            return true;
        return false;
    }//IsBeforeToday


    /**
     * Method IsAfterToday.
     * returns true if the date passed as parameter
     * with format (int day, int month, int year) is AFTER today's date.
     * @param day input date
     * @param month input month
     * @param year input year
     * @return boolean
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public static boolean IsAfterToday(int day, int month, int year) {
        java.util.Calendar today = java.util.Calendar.getInstance();
        java.util.Calendar dt = java.util.Calendar.getInstance();
        dt.set(year, month, day);
        if (dt.after(today))
            return true;
        return false;
    }//IsAfterToday


    /**
     * Method clear.
     * clear the current date
     * @author: Duong Thanh Nhan
     * @since 2001
     */
    public void clear() {
        this.m_nDay = -1;
        this.m_nMonth = -1;
        this.m_nYear = -1;
    }//clear

    /*
        public String toString()
        {
            return "[Year=" + m_nYear + ", Month=" + m_nMonth + ", Day=" + m_nDay + "]";
        }//toString
    */

}//class
