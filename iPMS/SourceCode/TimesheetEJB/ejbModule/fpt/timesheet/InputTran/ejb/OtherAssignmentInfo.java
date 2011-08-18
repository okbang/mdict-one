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
 * Created on Sep 29, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fpt.timesheet.InputTran.ejb;

import java.util.Date;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OtherAssignmentInfo implements java.io.Serializable {
	
	private int intDevId;
	private String strGroupName;
	private String strFullName;
	private String strAccount;
	private String strOaFromDate;
	private String strOaToDate;	
	private Date dtFromDate;
	private Date dtToDate;

	/**
	 * @return intDevId
	 */
	public int getDevId() {
		return intDevId;
	}

	/**
	 * @param intDevId
	 */
	public void setDevId(int intDevId) {
		this.intDevId = intDevId;
	}

	/**
	 * @return strGroupName
	 */
	public String getGroupName() {
		return strGroupName;
	}

	/**
	 * @param strGroupName
	 */
	public void setGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}

	/**
	 * @return strFullName
	 */
	public String getFullName() {
		return strFullName;
	}

	/**
	 * @param strFullName
	 */
	public void setFullName(String strFullName) {
		this.strFullName = strFullName;
	}
	
	/**
	 * @return strAccount
	 */
	public String getAccount() {
		return strAccount;
	}

	/**
	 * @param strAccount
	 */
	public void setAccount(String strAccount) {
		this.strAccount = strAccount;
	}
	
	/**
	 * @return fromDate
	 */
	public Date getFromDate() {
		return dtFromDate;
	}

	/**
	 * @param fromDate
	 */
	public void setFromDate(Date dtFromDate) {
		this.dtFromDate = dtFromDate;
	}
	
	/**
	 * @return endDate
	 */
	public Date getToDate() {
		return dtToDate;
	}
		

	/**
	 * @param endDate
	 */
	public void setToDate(Date dtToDate) {
		this.dtToDate = dtToDate;
	}
	/**
	 * @return fromDate
	 */
	public String getOaFromDate() {
		return strOaFromDate;
	}

	/**
	 * @param fromDate
	 */
	public void setOaFromDate(String strOaFromDate) {
		this.strOaFromDate = strOaFromDate;
	}
	
	/**
	 * @return endDate
	 */
	public String getOaToDate() {
		return strOaToDate;
	}
		

	/**
	 * @param endDate
	 */
	public void setOaToDate(String strOaToDate) {
		this.strOaToDate = strOaToDate;
	}
}
