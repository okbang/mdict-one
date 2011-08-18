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

package fpt.dms.framework.util.DateUtil;

import java.util.Date;
import java.util.StringTokenizer;

public class CalendarUtil extends Object implements java.io.Serializable
{
	public static final int MONTH = java.util.Calendar.MONTH;
	public static final int DATE = java.util.Calendar.DATE;
	public static final int YEAR = java.util.Calendar.YEAR;

	private int m_nMonth;
	private int m_nDay;
    private int m_nYear;
    
    //Added by: Tu Ngoc Trung, 2003-11-3
    private int m_nHour;
    private int m_nMinute;
    private int m_nSecond;


    //*********** private methods *******************
    private CalendarUtil(int year, int month, int day)
    {
        this.m_nMonth = month;
        this.m_nDay= day;
        this.m_nYear = year;
    }//CalendarUtil
    
    //Added by: Tu Ngoc Trung, 2003-11-3
    private CalendarUtil(int year, int month, int day,
                          int hour, int minute, int second)
    {
        this.m_nMonth = month;
        this.m_nDay= day;
        this.m_nYear = year;
        this.m_nHour = hour;
        this.m_nMinute= minute;
        this.m_nSecond = second;
    }//CalendarUtil
    
    
	/**************************************************************************
	Function: public static CalendarUtil getInstance()
	Description: get the current instance of Calendar object.
	Parameters:
	Return: CalendarUtil
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public static CalendarUtil getInstance()
	{
		java.util.Calendar c = java.util.Calendar.getInstance();
		int m = c.get(java.util.Calendar.MONTH);
		int d = c.get(java.util.Calendar.DATE);
        int y = c.get(java.util.Calendar.YEAR);
        
        //Added by: Tu Ngoc Trung, 2003-11-3
        int h = c.get(java.util.Calendar.HOUR);
        int min = c.get(java.util.Calendar.MINUTE);
        int s = c.get(java.util.Calendar.SECOND);
		//return new CalendarUtil(y,m,d);
        return new CalendarUtil(y,m,d, h,min,s);
	}

    /**************************************************************************
    Function: public void set(int year, int month, int day)
    Description: sets this object to a given year, month, day.
    Parameters:
        - year[in]: year {nnnn}
        - month[in]: month {0..11}
        - day[in]: day {1..31}
    Return: void
    Author: Duong Thanh Nhan
    Created date: 2001
    **************************************************************************/
    public void set(int year, int month, int day)
    {
        this.m_nMonth = month;
        this.m_nDay = day;
        this.m_nYear = year;
    }

    /**************************************************************************
    Function: public void set(int year, int month, int day
                              int hour, int minute, int second)
    Description: sets this object to a given year, month, day, hour, minute, second
    Parameters:
        - year[in]:  year {nnnn}
        - month[in]: month {0..11}
        - day[in]:   day {1..31}
        - hour[in]:  hour {0..23}
        - minute[in]:day {0..59}
        - second[in]:day {0..59}
    Return: void
    Author: Tu Ngoc Trung
    Created date: 2003-11-03
    **************************************************************************/
    public void set(int year, int month, int day,
                     int hour, int minute, int second)
    {
        this.m_nMonth = month;
        this.m_nDay = day;
        this.m_nYear = year;
        this.m_nHour = hour;
        this.m_nMinute= minute;
        this.m_nSecond = second;
    }

  	/**************************************************************************
	Function: public void set(int target, int value)
	Description: set the MONTH or YEAR or DATE.
	Parameters:
		- target[in]: target = 2, or 1, or 5
		- value[in]: an integer value for the specified target
					 (for MONTH, use 0..11)
	Return: void
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public void set(int target, int value)
	{
		switch (target)
		{
      //MONTH=2
			case java.util.Calendar.MONTH :
				this.m_nMonth = value;
				break;
      //YEAR=1
			case java.util.Calendar.YEAR :
				this.m_nYear = value;
				break;
      //DATE=5
			case java.util.Calendar.DATE :
				this.m_nDay = value;
				break;
      //Added by: Tu Ngoc Trung
      //HOUR = 10
            case java.util.Calendar.HOUR :
                this.m_nHour = value;
                break;
      //MINUTE = 12
            case java.util.Calendar.MINUTE :
                this.m_nMinute = value;
                break;
      //SECOND = 13
            case java.util.Calendar.SECOND :
                this.m_nSecond = value;
                break;
		}
	}

  	/**************************************************************************
	Function: public void setTime(java.util.Date date)
	Description:
	Parameters:
		- date[in]: a date value

	Return: void
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/
	public void setTime(java.util.Date date)
	{
		  java.util.Calendar c = java.util.Calendar.getInstance();
		  c.setTime(date);
		  this.m_nDay = c.get(java.util.Calendar.DATE);
		  this.m_nMonth = c.get(java.util.Calendar.MONTH);
		  this.m_nYear = c.get(java.util.Calendar.YEAR);
	}

  	/**************************************************************************
	public void setCalendarTime(java.util.Calendar cal)
	Description:
	Parameters:
		- cal[in]: a calendar value

	Return: void
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/
	public void setCalendarTime(java.util.Calendar cal)
	{
		/**
         * Modified by: Tu Ngoc Trung
         * Date: 2003-11-03
        java.util.Calendar c = java.util.Calendar.getInstance();
    	c.set(cal.get(java.util.Calendar.YEAR),
                  cal.get(java.util.Calendar.MONTH),
                  cal.get(java.util.Calendar.DATE));
        this.m_nDay = cal.get(java.util.Calendar.DATE);
		this.m_nMonth = cal.get(java.util.Calendar.MONTH);
		this.m_nYear = cal.get(java.util.Calendar.YEAR);
        */
        
        this.set(cal.get(java.util.Calendar.YEAR),
                  cal.get(java.util.Calendar.MONTH),
                  cal.get(java.util.Calendar.DATE),
                  cal.get(java.util.Calendar.HOUR),
                  cal.get(java.util.Calendar.MINUTE),
                  cal.get(java.util.Calendar.SECOND));
	}//setCalendarTime


  	/**************************************************************************
	Funstion: public void setFullDateString(String mmddyyyy)
	Description:
	Parameters:
		- mmddyyyy[in]: a date string that formatted as mmddyyyy
						(where "mm" in range 1..12)

	Return: void
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public void setFullDateString(String mmddyyyy)
	{
    	StringTokenizer token = new StringTokenizer(mmddyyyy,"/");
		int mm = -1;
		int dd = -1;
		int yyyy = -1;
		if (token.hasMoreTokens())
		{
			mm = Integer.parseInt(token.nextToken());
			mm--;
		}
		if (token.hasMoreTokens())
		{
			dd = Integer.parseInt(token.nextToken());
		}
		if (token.hasMoreTokens())
		{
			yyyy = Integer.parseInt(token.nextToken());
		}

		java.util.Calendar dt = java.util.Calendar.getInstance();
		dt.set(yyyy,mm,dd);
		this.m_nDay = dt.get(java.util.Calendar.DATE);
		this.m_nMonth = dt.get(java.util.Calendar.MONTH);
		this.m_nYear = dt.get(java.util.Calendar.YEAR);
	}//setFullDateString


	/**************************************************************************
	Function: public void setCloudscapeDateString(String yyyymmdd)
	Description: set a string with yyyymmdd format into the Cloudscape date
	Parameters:
		- yyyymmdd[in]: a date string that formatted as yyyymmdd
						(where "mm" in range 1..12)

	Return: void
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/
	public void setCloudscapeDateString(String yyyymmdd)
	{
		StringTokenizer token = new StringTokenizer(yyyymmdd,"-");
		int mm = -1;
		int yyyy = -1;
		int dd = -1;
		if (token.hasMoreTokens())
		{
			yyyy = Integer.parseInt(token.nextToken());
		}
		if (token.hasMoreTokens())
		{
			mm = Integer.parseInt(token.nextToken());
			mm--;
		}
		if (token.hasMoreTokens())
		{
			dd = Integer.parseInt(token.nextToken());
		}

		java.util.Calendar dt = java.util.Calendar.getInstance();
		dt.set(yyyy,mm,dd);
		this.m_nDay = dt.get(java.util.Calendar.DATE);
		this.m_nMonth = dt.get(java.util.Calendar.MONTH);
		this.m_nYear = dt.get(java.util.Calendar.YEAR);
	}//setCloudscapeDateString


  	/***************************GET methods***************************************
	Description: return the day, month (0..11), year.
	Parameters: void
	Return: int
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public int getMonth()
	{
		return m_nMonth;
	}//getMonth

	public int getDay()
	{
		return m_nDay;
	}//getDay

	public int getYear()
	{
		return m_nYear;
	}//getYear
    
    //Added by: Tu Ngoc Trung, 2003-11-03
    public int getHour()
    {
        return m_nHour;
    }//getHour

    public int getMinute()
    {
        return m_nMinute;
    }//getMinute

    public int getSecond()
    {
        return m_nSecond;
    }//getSecond


  	/**************************************************************************
	Function: public Date getTime()
	Description: get time of the current calendar
	Parameters: void
	Return: java.util.Date
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public Date getTime()
	{
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(m_nYear,m_nMonth,m_nDay, m_nHour,m_nMinute,m_nSecond);
		return c.getTime();
	}//getTime


	/**************************************************************************
	Function: public int get(int target)
	Description: get the current month, year, or day
	Parameters:
		- target[in]: is either 1=YEAR, 2=MONTH, 5=DATE
	Return: int
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public int get(int target)
	{
		switch (target)
		{
			//MONTH=2
			case java.util.Calendar.MONTH : return this.m_nMonth;
			//YEAR=1
			case java.util.Calendar.YEAR : return this.m_nYear;
			//DATE=5
			case java.util.Calendar.DATE : return this.m_nDay;
			default: return -1;
		}
	}//get


	/**************************************************************************
	Function: public String getFullDateString()
	Description: get the date encoded in the format  mm/dd/yyyy {mm = 1..12}
	Parameters: void
	Return: String in format mm/dd/yyyy
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public String getFullDateString()
	{
		return ((m_nMonth + 1 < 10)? "0" : "") + (m_nMonth + 1) + "/" + ((m_nDay < 10)? "0" : "") + m_nDay + "/" + ((m_nYear < 10)? "0" : "") + m_nYear;
	}//getFullDateString


	/**************************************************************************
	Function: public String getCloudscapeDateString()
	Description: get the Cloudscape date string of the current date
	Parameters: void
	Return: String in format "yyyy-mm-dd"
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public String getCloudscapeDateString()
	{
		return m_nYear + "-" + ((m_nMonth + 1 < 10)? "0" : "") + (m_nMonth + 1) + "-" + ((m_nDay < 10)? "0" : "") + m_nDay;
	}//getCloudscapeDateString


 	/**************************************************************************
	Function: public java.util.Calendar getCalendarTime()
	Description: get the Calendar object of the current date
	Parameters: void
	Return: a java.util.Calendar object.
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

 	public java.util.Calendar getCalendarTime()
 	{
		java.util.Calendar c = java.util.Calendar.getInstance();
    	c.set(this.m_nYear,this.m_nMonth,this.m_nDay,
              this.m_nHour, this.m_nMinute, this.m_nSecond);
    	return c;
	}//getCalendarTime


	/**************************************************************************
	Function: public static boolean IsBeforeToday(int day, int month, int year)
	Description: returns true if the date passed as parameter
				 with format (int day, int month, int year) is BEFORE today's date.
	Parameters:
		- day[in]: input date
		- month[in]: input month
		- year[in]: input year
	Return: boolean.
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public static boolean IsBeforeToday(int day, int month, int year)
	{
		java.util.Calendar today = java.util.Calendar.getInstance();
		java.util.Calendar dt = java.util.Calendar.getInstance();
		dt.set(year,month,day);
		if (dt.before(today)) return true;
		return false;
	}//IsBeforeToday



	/**************************************************************************
	Function: public static boolean IsAfterToday(int day, int month, int year)
	Description: returns true if the date passed as parameter
   				 with format (int day, int month, int year) is AFTER today's date.
	Parameters:
		- day[in]: input date
		- month[in]: input month
		- year[in]: input year
	Return: boolean.
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public static boolean IsAfterToday(int day, int month, int year)
	{
		java.util.Calendar today = java.util.Calendar.getInstance();
		java.util.Calendar dt = java.util.Calendar.getInstance();
		dt.set(year,month,day);
		if (dt.after(today)) return true;
		return false;
	}//IsAfterToday


	/**************************************************************************
	Function: public void clear()
	Description: clear the current date
	Parameters: void
	Return: void.
	Author: Duong Thanh Nhan
	Created date: 2001
	**************************************************************************/

	public void clear()
	{
		this.m_nDay = -1;
		this.m_nMonth = -1;
		this.m_nYear = -1;
        
        this.m_nHour = -1;
        this.m_nMinute = -1;
        this.m_nSecond = -1;
	}//clear

/*
	public String toString()
	{
		return "[Year=" + m_nYear + ", Month=" + m_nMonth + ", Day=" + m_nDay + "]";
	}//toString
*/

}//class
