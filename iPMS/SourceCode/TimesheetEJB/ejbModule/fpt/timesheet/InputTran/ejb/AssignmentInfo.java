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
 * Created on Aug 28, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fpt.timesheet.InputTran.ejb;

/**
 * @author HanhTN
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AssignmentInfo implements java.io.Serializable {	
	
	public int intDeveloperID;
	public int intProjectID;
	public String strDeveloperName;
	public String strAccount;
	public String strProjectCode;

	public AssignmentInfo() {
	}

	/**
	 * @return intDeveloperID
	 */
	public int getDeveloperID() {
		return intDeveloperID;
	}
	
	/**
	 * @param intDeveloperID
	 */
	public void setDeveloperID(int intDeveloperID) {
		this.intDeveloperID = intDeveloperID;
	}

	/**
	 * @return intProjectID
	 */
	public int getProjectID() {
		return intProjectID;
	}

	/**
	 * @param intProjectID
	 */
	public void setProjectID(int intProjectID) {
		this.intProjectID = intProjectID;
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
	 * @return strDeveloperName
	 */
	public String getDeveloperName() {
		return strDeveloperName;
	}

	/**
	 * @param strDeveloperName
	 */
	public void setDeveloperName(String strDeveloperName) {
		this.strDeveloperName = strDeveloperName;
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

}
