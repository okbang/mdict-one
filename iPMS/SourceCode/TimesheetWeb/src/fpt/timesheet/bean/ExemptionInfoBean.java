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
 
 package fpt.timesheet.bean;

import java.util.Calendar;
import java.util.Collection;

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.util.CommonFunction;

/**
 * @author HanhTN * 
 */
public class ExemptionInfoBean implements java.io.Serializable {

	private int intExemptionId 	  = 0;
	private int intType 		  = 0;
	private int intDeveloperId 	  = 0;
	private int intWeekDay 		  = 0;

	private String strWeekDay 	= null;
	private String strSunday 	= null;
	private String strMonday 	= null;
	private String strTuesday 	= null;
	private String strWednesday = null;
	private String strThursday 	= null;
	private String strFriday 	= null;
	private String strSaturday 	= null;

	private String strId 		= null;
	private String strDevId 	= null;
	private String strDevName 	= null;
	private String strDevAccount= null;
	private String strGroupName = null;
	private String strAccount 	= null;
	private String strWeekday 	= null;
	private String strFromDate 	= null;
	private String strToDate 	= null;
	private String strReason 	= null;
	private String strNote 		= null;
	private String strYear 		= null;
	private String strMessage	= "";

	//**************************************
	/**
	 * Method setId.
	 * @param strId
	 */
	public void setId(String strId)	{
		this.strId = strId;
	}
	
	/**
	 * Method getId.
	 * @return strId
	 */
	public String getId() {
		return strId;
	}

	/**
	 * Method getGroupName.
	 * @return String
	 */
	public String getGroupName() {
		return strGroupName;
	}

	/**
	 * Method setGroupName.
	 * @param strGroupName
	 */
	public void setGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}

	/**
	 * Method getAccount
	 * @return strAccount
	 */
	public String getAccount() {
		return strAccount;
	}

	/**
	 * Method setAccount.
	 * @param strAccount
	 */
	public void setAccount(String strAccount) {
		this.strAccount = strAccount;
	}

	/**
	 * Method getDevAccount
	 * @return strDevAccount
	 */
	public String getDevAccount() {
		return strDevAccount;
	}

	/**
	 * Method setDevAccount.
	 * @param strDevAccount
	 */
	public void setDevAccount(String strDevAccount) {
		this.strDevAccount = strDevAccount;
	}

	/**
	 * Method getGroupName.
	 * @return String
	 */
	public String getDevName() {
		return strDevName;
	}

	/**
	 * Method setGroupName.
	 * @param strGroupName
	 */
	public void setDevName(String strDevName) {
		this.strDevName = strDevName;
	}

	/**
	 * Method getDevId.
	 * @return strDevId
	 */
	public String getDevId() {
		return strDevId;
	}

	/**
	 * Method setDevId.
	 * @param strDevId
	 */
	public void setDevId(String strDevId) {
		this.strDevId = strDevId;
	}

	/**
	 * Method getExemptionId.
	 * @return intExemptionId
	 */
	public int getExemptionId() {
		return intExemptionId;
	}

	/**
	 * Method setExemptionId.
	 * @param intExemptionId
	 */
	public void setExemptionId(int intExemptionId) {
		this.intExemptionId = intExemptionId;
	}

	/**
	 * Method getDeveloperId.
	 * @return intDeveloperId
	 */
	public int getDeveloperId() {
		return intDeveloperId;
	}

	/**
	 * Method setDeveloperId.
	 * @param intDeveloperId
	 */
	public void setDeveloperId(int intDeveloperId) {
		this.intDeveloperId = intDeveloperId;
	}

	/**
	 * Method getType.
	 * @return intType
	 */
	public int getType() {
		return intType;
	}

	/**
	 * Method setType.
	 * @param intType
	 */
	public void setType(int intType) {
		this.intType = intType;
	}

	/**
	 * Method getYear.
	 * @return strYear
	 */
	public String getYear() {
		return strYear;
	}

	/**
	 * Method setYear.
	 * @param intData
	 */
	public void setYear(String intData) {
		Integer intYear = new Integer(Calendar.getInstance().get(Calendar.YEAR));
		if ((intData == null) || (intData.length() <= 0)) {
			strYear = intYear.toString();
		}
		else if (Integer.parseInt(intData) < TIMESHEET.TS_START_YEAR) {
			strYear = intYear.toString();
		}
		else {
			strYear = intData;
		}
	}

	/**
	 * Method getWeekDay.
	 * @return strWeekDay
	 */
	public String getWeekDay() {
		return strWeekDay;
	}
	
	/**
	 * Method setWeekDay.
	 * @param strWeekDay
	 */
	public void setWeekDay(String strWeekDay) {
		this.strWeekDay = strWeekDay;
	}

	/**
	 * Method getSunday.
	 * @return strSunday
	 */
	public String getSunday() {
		return strSunday;
	}

	/**
	 * Method setSunday.
	 * @param strSunday
	 */
	public void setSunday(String strSunday) {
		this.strSunday = strSunday;
	}

	/**
	 * Method getMonday.
	 * @return strMonday
	 */
	public String getMonday() {
		return strMonday;
	}

	/**
	 * Method setMonday.
	 * @param strMonday
	 */
	public void setMonday(String strMonday) {
		this.strMonday = strMonday;
	}

	/**
	 * Method getTuesday.
	 * @return strTuesday
	 */
	public String getTuesday() {
		return strTuesday;
	}

	/**
	 * Method setTuesday.
	 * @param strTuesday
	 */
	public void setTuesday(String strTuesday) {
		this.strTuesday = strTuesday;
	}

	/**
	 * Method getWednesday.
	 * @return strWednesday
	 */
	public String getWednesday() {
		return strWednesday;
	}

	/**
	 * Method setWednesday.
	 * @param strWednesday
	 */
	public void setWednesday(String strWednesday) {
		this.strWednesday = strWednesday;
	}

	/**
	 * Method getThursday.
	 * @return strThursday
	 */
	public String getThursday() {
		return strThursday;
	}

	/**
	 * Method setThursday.
	 * @param strThursday
	 */
	public void setThursday(String strThursday) {
		this.strThursday = strThursday;
	}

	/**
	 * Method getFriday.
	 * @return strFriday
	 */
	public String getFriday() {
		return strFriday;
	}

	/**
	 * Method setFriday.
	 * @param strFriday
	 */
	public void setFriday(String strFriday) {
		this.strFriday = strFriday;
	}

	/**
	 * Method getSaturday.
	 * @return strSaturday
	 */
	public String getSaturday() {
		return strSaturday;
	}

	/**
	 * Method setSaturday.
	 * @param strSaturday
	 */
	public void setSaturday(String strSaturday) {
		this.strSaturday = strSaturday;
	}

	/**
	 * Method setWeekDayCalendar.
	 * @param strWeekDay
	 */
	public void setWeekDayCalendar(String strWeekDay) {
	    if (strWeekDay.equals("Sunday")) 
			intWeekDay = Calendar.SUNDAY;
	    else if (strWeekDay.equals("Monday")) 
			intWeekDay = Calendar.MONDAY;
	    else if (strWeekDay.equals("Tuesday")) 
			intWeekDay = Calendar.TUESDAY;
	    else if (strWeekDay.equals("Wednesday")) 
			intWeekDay = Calendar.WEDNESDAY;
	    else if (strWeekDay.equals("Thursday")) 
			intWeekDay = Calendar.THURSDAY;
	    else if (strWeekDay.equals("Friday")) 
			intWeekDay = Calendar.FRIDAY;
	    else if (strWeekDay.equals("Saturday")) 
			intWeekDay = Calendar.SATURDAY;
	    else throw new IllegalArgumentException("bad weekday: " + strWeekDay);
	}

	/**
	 * Method getFromDate.
	 * @return String
	 */    
	public String getFromDate() {
		return strFromDate;
	}

	/**
	 * Method setFromDate.
	 * @param intData
	 */
	public void setFromDate(String intData) {
		if (intData != null) {
			strFromDate = intData.trim();
		}
	}

	/**
	 * Method getToDate.
	 * @return String
	 */
	public String getToDate() {
		return strToDate;
	}

	/**
	 * Method setToDate.
	 * @param intData
	 */
	public void setToDate(String intData) {
		if (intData != null) {
			strToDate = intData.trim();
		}
	}

	/**
	* Method getSearchFromDate.
	* @return strFromDate
	*/    
	public String getSearchFromDate() {
		if (strFromDate == null) {
		    this.setYear(this.strYear);
		    strFromDate = CommonFunction.getWeeklyFromDate(Integer.parseInt(strYear));
	    }
	    return strFromDate;
	}
	
	/**
	* Method setSearchFromDate.
	* @param intData
	*/
	public void setSearchFromDate(String intData) {
		if (intData != null) {
			  strFromDate = intData.trim();
		}
	}
	
	/**
	* Method getSearchToDate.
	* @return String
	*/
	public String getSearchToDate() {
		if (strToDate == null) {
		    this.setYear(this.strYear);
		    strToDate = CommonFunction.getWeeklyToDate(Integer.parseInt(strYear));
	    }
	    return strToDate;
	}

	/**
	* Method setSearchToDate.
	* @param intData
	*/
	public void setSearchToDate(String intData) {
		if (intData != null) {
			  strToDate = intData.trim();
		}
	}

	/**
	 * Method getReason.
	 * @return strReason
	 */
	public String getReason() {
		return strReason;
	}

	/**
	 * Method setReason.
	 * @param strReason
	 */
	public void setReason(String strReason) {
		this.strReason = strReason;
	}

	/**
	 * Method getNote.
	 * @return strNote
	 */
	public String getNote() {
		return strNote;
	}

	/**
	 * Method setNote.
	 * @param strNote
	 */
	public void setNote(String strNote) {
		this.strNote = strNote;
	}

	/**
	 * Method getMessage.
	 * @return strMessage
	 */
	public String getMessage() {
		return strMessage;
	}

	/**
	 * Method setMessage.
	 * @param strMessage
	 */
	public void setMessage(String strMessage) {
		this.strMessage = strMessage;
	}

}