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
 
 /*
 * Created on Oct 14, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fpt.timesheet.Exemption.ejb;

/**
 * @author HanhTN
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExemptionBean implements java.io.Serializable {

	private int intExemptionId = 0;
	private int intDeveloperId = 0;
	private int intType = 0;
	private String strWeekDay = null;
	private String strGroup = null;
	private String strAccount = null;
	private String strFullName = null;
	private String strFromDate = null;
	private String strToDate = null;
	private String strReason = null;
	private String strNote = null;
	
	private String strBegindate = null;
	private String strOccurdate = null;


	/**
	 * @return strOccurdate 
	 */
	public String getOccurdate () {
		return strOccurdate;
	}
	
	/**
	 * @param strOccurdate
	 */
	public void setOccurdate (String strOccurdate) {
		this.strOccurdate = strOccurdate;
	}

	/**
	 * @return strBegindate
	 */
	public String getBegindate() {
		return strBegindate;
	}
	
	/**
	 * @param strBegindate
	 */
	public void setBegindate(String strBegindate) {
		this.strBegindate = strBegindate;
	}

	/**
	 * @return strGroup
	 */
	public String getGroup() {
		return strGroup;
	}
	
	/**
	 * @param strGroup
	 */
	public void setGroup(String strGroup) {
		this.strGroup = strGroup;
	}

	/**
	 * @return strGroup
	 */
	public String getAccount() {
		return strAccount;
	}
	
	/**
	 * @param strGroup
	 */
	public void setAccount(String strAccount) {
		this.strAccount = strAccount;
	}

	/**
	 * @return strGroup
	 */
	public String getFullName() {
		return strFullName;
	}
	
	/**
	 * @param strGroup
	 */
	public void setFullName(String strFullName) {
		this.strFullName = strFullName;
	}

	/**
	 * @return intExemptionId
	 */
	public int getExemptionId() {
		return intExemptionId;
	}

	/**
	 * @param intExemptionId
	 */
	public void setExemptionId(int intExemptionId) {
		this.intExemptionId = intExemptionId;
	}

	/**
	 * @return intDeveloperId
	 */
	public int getDeveloperId() {
		return intDeveloperId;
	}

	/**
	 * @param intDeveloperId
	 */
	public void setDeveloperId(int intDeveloperId) {
		this.intDeveloperId = intDeveloperId;
	}

	/**
	 * @return intType
	 */
	public int getType() {
		return intType;
	}

	/**
	 * @param intType
	 */
	public void setType(int intType) {
		this.intType = intType;
	}

	/**
	 * Method getWeekDay
	 * @return intWeekDay
	 */
	public String getWeekDay() {
		return strWeekDay;
	}
	
	public String getWeekDayList() {
		String strResult = "";
		if (strWeekDay != null) {
			for (int j = 0; j < strWeekDay.length(); j++) {
				if (strWeekDay.charAt(j) == '1') {
					strResult += "Sunday, ";
				}
				else if (strWeekDay.charAt(j) == '2') {
					strResult += "Monday, ";
				}
				else if (strWeekDay.charAt(j) == '3') {
					strResult += "Tuesday, ";
				}
				else if (strWeekDay.charAt(j) == '4') {
					strResult += "Wednesday, ";
				}
				else if (strWeekDay.charAt(j) == '5') {
					strResult += "Thursday, ";
				}
				else if (strWeekDay.charAt(j) == '6') {
					strResult += "Friday, ";
				}
				else if (strWeekDay.charAt(j) == '7') {
					strResult += "Saturday, ";
				}
			}
			if (strResult.length() > 0) {
				strResult = strResult.substring(0, strResult.length()-2);
			}
		}
		return strResult;
	}

	/**
	 * Method setDay
	 * @param strWeekDay
	 */
	public void setWeekDay(String strWeekDay) {
		this.strWeekDay = strWeekDay;
	}

	/**
	 * @return strFromDate
	 */
	public String getFromDate() {
		return strFromDate;
	}

	/**
	 * @param strFromDate
	 */
	public void setFromDate(String strFromDate) {
		this.strFromDate = strFromDate;
	}
	
	/**
	 * @return strToDate
	 */
	public String getToDate() {
		return strToDate;
	}

	/**
	 * @param strToDate
	 */
	public void setToDate(String strToDate) {
		this.strToDate = strToDate;
	}
	
	/**
	 * @return strReason
	 */
	public String getReason() {
		return strReason;
	}

	/**
	 * @param strReason
	 */
	public void setReason(String strReason) {
		this.strReason = strReason;
	}

	/**
	 * @return strNote
	 */
	public String getNote() {
		return strNote;
	}

	/**
	 * @param strNote
	 */
	public void setNote(String strNote) {
		this.strNote = strNote;
	}

}
