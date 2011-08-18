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
 
 //TrangTK
//Dec 18, 2001

package fpt.timesheet.ApproveTran.ejb.common;


public class ProjectComboModel implements java.io.Serializable {

	private String ID;
	private String Code;
	private String Name;
	private String Group;
	private String Status;
	
	//Modified by HaiMM
	private String Startdate;
	private String PlanStartdate;

	/**
	 * @see java.lang.Object#Object()
	 */
	public ProjectComboModel() {
		this.ID = "";
		this.Code = "";
		this.Name = "";
		this.Group = "";
		this.Status = "";
		this.Startdate = "";
		this.PlanStartdate = "";
	}

	/**
	 * Method ProjectComboModel.
	 * @param strID
	 * @param strCode
	 * @param strName
	 */
	public ProjectComboModel(String strID, String strCode, String strName) {
		this.ID = strID;
		this.Code = strCode;
		this.Name = strName;
	}

	/**
	 * Method ProjectComboModel.
	 * @param strID
	 * @param strCode
	 * @param strName
	 */
	public ProjectComboModel(String strID, String strCode, String strName, String strGroup, String strStatus) {
		this.ID = strID;
		this.Code = strCode;
		this.Name = strName;
		this.Group = strGroup;
		this.Status = strStatus;
	}

	/**
	 * Method ProjectComboModel.
	 * @param strID
	 * @param strCode
	 * @param strName
	 * @param strGroup
	 * @param strStatus
	 * @param strStartdate
	 * @param strPlanStartdate
	 */
	public ProjectComboModel(String strID, String strCode, String strName, String strGroup, String strStatus, String strStartdate, String strPlanStartdate) {
		this.ID = strID;
		this.Code = strCode;
		this.Name = strName;
		this.Group = strGroup;
		this.Status = strStatus;
		this.Startdate = strStartdate;
		this.PlanStartdate = strPlanStartdate;
	}

	/**
	 * Method setCode.
	 * @param str
	 */
	public void setCode(String str) {
		this.Code = str;
	}

	/**
	 * Method getCode.
	 * @return String
	 */
	public String getCode() {
		return this.Code;
	}

	/**
	 * Method setName.
	 * @param str
	 */
	public void setName(String str) {
		this.Name = str;
	}

	/**
	 * Method getName.
	 * @return String
	 */
	public String getName() {
		return this.Name;
	}

	/**
	 * Method setID.
	 * @param str
	 */
	public void setID(String str) {
		this.ID = str;
	}

	/**
	 * Method getID.
	 * @return String
	 */
	public String getID() {
		return this.ID;
	}
	/**
	 * @return
	 */
	public String getGroup() {
		return Group;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @return
	 */
	public String getStartdate() {
		return Startdate;
	}

	/**
	 * @return
	 */
	public String getPlanStartdate() {
		return PlanStartdate;
	}

	/**
	 * @param string
	 */
	public void setGroup(String string) {
		Group = string;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		Status = string;
	}

	/**
	 * @param string
	 */
	public void setStartdate(String string) {
		Startdate = string;
	}
	
	/**
	 * @param string
	 */
	public void setPlanStartdate(String string) {
		PlanStartdate = string;
	}

}