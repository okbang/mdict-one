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


package fpt.dms.framework.util.DateUtil;

import fpt.dms.constant.DMS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
	private String m_strDate;
	private String m_strTime;


	//Constructor
	public DateUtil()
	{

	}

	/**************************************************************************
	Function: public void formatDate()
	Description: format the current date and time.
	Parameters:
	Return: void
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/
	public void formatDate(String strFormat)
	{
	    SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
	    Date dtCurrentTime = new Date();
	    String strDateTime = formatter.format(dtCurrentTime);
	    int nPos = strDateTime.indexOf(' ');
	    m_strDate = strDateTime.substring(0,nPos);
	    m_strTime = strDateTime.substring(nPos + 1,strDateTime.length());
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
  	public String getDate()
  	{
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
  	public String getTime()
  	{
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

  	public static int getNumDate(int nMonth,int nYear)
	{
		if (nMonth==4 || nMonth==6 || nMonth==9 || nMonth==11 )
		{
			return 30;
		}

		if (nMonth==2)
		{
			// leap year
			if ( (nYear % 4 == 0) && (nYear % 100 != 0))
			{
				 return 29;
			}
			else  return 28;
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
  	public static int getNumWeek(int nMonth,int nYear)
  	{
		int tmp=0;
		Calendar calendar = Calendar.getInstance();

		for(int i=0;i<6;i++)
		{
			calendar.set(nYear,nMonth+i,getNumDate(nMonth+i,nYear));
			if(nMonth>12)
			{
				nMonth=1;
				nYear++;
			}
			tmp=calendar.get(Calendar.WEEK_OF_MONTH);
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

	public static int getDateFromString(String str)
	{
		String strDay, strMonth, strYear;
		int nMonth, nDay, nYear, nPos1, nPos2;

		nPos1 = str.indexOf( "/" );
		if ( nPos1 < 0 ) return -1;

		//Get the position of the backslash "/" character for DAY
		nPos2 = str.lastIndexOf( "/" );
		if ( nPos2 < 0 ) return -1;

		//Checking the duplicate position of the backslash character
		if ( nPos1 == nPos2 ) return -1;

		//Breaking the string into 3 parts of DAY, MONTH, YEAR
		strDay = str.substring( 0, nPos1  );
		strMonth = str.substring( nPos1 + 1, nPos2 );

		//Rounding MONTH & DAY by adding 0
		if (strMonth.length() < 2) strMonth = "0" + strMonth;
		if (strDay.length() < 2) strDay = "0" + strDay;
		strYear = str.substring( nPos2 + 1 );


		return Integer.parseInt(strDay,10);
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

	public static int getMonthFromString(String str)
	{
		String strDay, strMonth, strYear;
	    int nMonth, nDay, nYear, nPos1, nPos2;

	    nPos1 = str.indexOf( "/" );
	    if ( nPos1 < 0 ) return -1;

	    //Get the position of the backslash "/" character for DAY
	    nPos2 = str.lastIndexOf( "/" );
	    if ( nPos2 < 0 ) return -1;

	    //Checking the duplicate position of the backslash character
	    if ( nPos1 == nPos2 ) return -1;

	    //Breaking the string into 3 parts of DAY, MONTH, YEAR
	    strDay = str.substring( 0, nPos1  );
	    strMonth = str.substring( nPos1 + 1, nPos2 );

	    //Rounding MONTH & DAY by adding 0
	    if (strMonth.length() < 2) strMonth = "0" + strMonth;
	    if (strDay.length() < 2) strDay = "0" + strDay;
	    strYear = str.substring( nPos2 + 1 );
	    return Integer.parseInt(strMonth,10);
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

	public static int getYearFromString(String str)
	{
	    String strDay, strMonth, strYear;
	    int nMonth, nDay, nYear, nPos1, nPos2;

	    nPos1 = str.indexOf( "/" );
	    if ( nPos1 < 0 ) return -1;

	    //Get the position of the backslash "/" character for DAY
	    nPos2 = str.lastIndexOf( "/" );
	    if ( nPos2 < 0 ) return -1;

	    //Checking the duplicate position of the backslash character
	    if ( nPos1 == nPos2 ) return -1;

	    //Breaking the string into 3 parts of DAY, MONTH, YEAR
	    strDay = str.substring( 0, nPos1  );
	    strMonth = str.substring( nPos1 + 1, nPos2 );

	    //Rounding MONTH & DAY by adding 0
	    if (strMonth.length() < 2) strMonth = "0" + strMonth;
	    if (strDay.length() < 2) strDay = "0" + strDay;
	    strYear = str.substring( nPos2 + 1 );
	    return Integer.parseInt(strYear,10);
	}
	
	public static String getCurrentDate()
    {
            java.util.Date currentDate = new java.util.Date();
            currentDate.setDate(currentDate.getDate());
            SimpleDateFormat dateFormat	= new SimpleDateFormat ("MM/dd/yy");
            String strDate = dateFormat.format(currentDate);
            return strDate;
    }
    public static String getCurrentDate(int nDelta)
	{
          java.util.Date currentDate = new java.util.Date();
          currentDate.setDate(currentDate.getDate()+nDelta);
          SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yy");
          String dateString = dateFormat.format(currentDate);
          return dateString;
	}
    
 /** 
 * Purpose: Compare  sStartDate to sEndDate
 * Input: sStartDate and sEndDate are dates to compare
 * 		  	 sStartDate and sEndDate in MM/dd/yy format
 * Output: -1 if sStartDate is before sEndDate
 *			   0 if sStartDate is equal to sEndDate
 *			   1 if sStartDate is after sEndDate
 */    
    public static int CompareDate(String sStartDate , String sEndDate)
	
	{
		int startDay, startMonth, startYear, endMonth, endDay, endYear;
		int n1 = sStartDate.indexOf( "/" );
		int n2 = sStartDate.lastIndexOf( "/" );
		int n3 = sEndDate.indexOf( "/" );
		int n4 = sEndDate.lastIndexOf( "/" );

		startMonth = Integer.parseInt( sStartDate.substring( 0, n1  ),10);
		startDay = Integer.parseInt( sStartDate.substring( n1 + 1, n2 ),10);
		startYear = Integer.parseInt( sStartDate.substring( n2+1 ),10);

		endMonth = Integer.parseInt( sEndDate.substring( 0, n3  ),10);
		endDay = Integer.parseInt( sEndDate.substring( n3+1, n4 ),10);
		endYear = Integer.parseInt( sEndDate.substring( n4+1 ),10);
		
		if ( startYear > endYear ) return 1;
		if ( startYear < endYear ) return -1;
		// Now startYear == endYear
		if ( startMonth > endMonth ) return 1;
		if ( startMonth < endMonth ) return -1;
		// Now startMonth == endMonth
		if ( startDay > endDay ) return 1;
		if ( startDay < endDay ) return -1;
		return 0;
	} // compareDate
	
	
	public static String getDateBeginOfWeek()
    {
            java.util.Date currentDate = new java.util.Date();
            currentDate.setDate(currentDate.getDate());
            int nDay = currentDate.getDay();
            switch (nDay)
            {
                case 0: //Sunday
                  break;
                case 1: //Monday
                  currentDate.setDate(currentDate.getDate()-1);
                  break;
                case 2: //Tuesday
                  currentDate.setDate(currentDate.getDate()-2);
                  break;
                case 3: //Wed
                  currentDate.setDate(currentDate.getDate()-3);
                  break;
                case 4: //Thurs
                  currentDate.setDate(currentDate.getDate()-4);
                  break;
                case 5: //Fri
                  currentDate.setDate(currentDate.getDate()-5);
                  break;
                case 6: //Sat
                  currentDate.setDate(currentDate.getDate()-6);
                  break;

            }

            SimpleDateFormat dateFormat	= new SimpleDateFormat (DMS.DATE_FORMAT);
            String strDate = dateFormat.format(currentDate);
            return strDate;
    }
}