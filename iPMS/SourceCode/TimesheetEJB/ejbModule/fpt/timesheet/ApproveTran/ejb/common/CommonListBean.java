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
 * Created on Nov 1, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fpt.timesheet.ApproveTran.ejb.common;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CommonListBean implements java.io.Serializable {

	private int intProjectId;
	private String strProjectCode;
	private String strPQAAccount;
	private String strPQAName;

	private String Key;
	private String strDevId;
	private String strAccount;
	private String strDevName;
	private String strGroupName;

	/**
	 * setKey
	 * Setter method for Key
	 * @param str - input data for setting
	 */
	public void setKey(String str) {
		this.Key = str;
	}

	/**
	 * getKey
	 * Getter method for Key
	 * @return Key
	 */
	public String getKey() {
		return this.Key;
	}
	
	/**
	 * @return intProjectId
	 */
	public int getProjectId() {
		return intProjectId;
	}
    
	/**
	 * @param intProjectId
	 */
	public void setProjectId(int intProjectId) {
		this.intProjectId = intProjectId;    	
	}
    
	/**
	 * @return strProjectCode
	 */
	public String getProjectCode() {
		return strProjectCode;
	}

	/**
	 * @param strProjectCode
	 */
	public void setProjectCode(String strProjectCode) {
		this.strProjectCode = strProjectCode;    	
	}
	
	/**
	 * @return strPQAAccount
	 */
	public String getPQAAccount() {
		return strPQAAccount;
	}

	/**
	 * @param strPQAAccount
	 */
	public void setPQAAccount(String strPQAAccount) {
		this.strPQAAccount = strPQAAccount;
	}
	
	/**
	 * @return strPQAName
	 */
	public String getPQAName() {
		return strPQAName;
	}

	/**
	 * @param strPQAName
	 */
	public void setPQAName(String strPQAName) {
		this.strPQAName = strPQAName;    	
	}	
	
	/**
	 * @return strDevId
	 */
	public String getDevId() {
		return strDevId;
	}

	/**
	 * @param strDevId
	 */
	public void setDevId(String strDevId) {
		this.strDevId = strDevId;
	}
	/**
	 * @return strDevName
	 */
	public String getDevName() {
		return strDevName;
	}

	/**
	 * @param strDevName
	 */
	public void setDevName(String strDevName) {
		this.strDevName = strDevName;
	}
	/**
	 * @return strAccount
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

	public String getGroupName() {
		return strGroupName;
	}

	/**
	 * @param strGroup
	 */
	public void setGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}

}
