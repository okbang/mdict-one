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
 
 package fpt.dashboard.DeveloperManagementTran.ejb;
/**
 * Local interface for Enterprise Bean: DeveloperEJB
 */
public interface DeveloperEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * getName
	 */
	public java.lang.String getName();
	/**
	 * getDesign
	 */
	public java.lang.String getDesign();
	/**
	 * getGroup
	 */
	public java.lang.String getGroup();
	/**
	 * getRole
	 */
	public java.lang.String getRole();
	/**
	 * getPass
	 */
	public java.lang.String getPass();
	/**
	 * getAcc
	 */
	public java.lang.String getAcc();
	/**
	 * getStatus
	 */
	public java.lang.String getStatus();
	/**
	 * getEmail
	 */
	public java.lang.String getEmail();
	/**
	 * getStartDate
	 */
	public java.lang.String getStartDate();
	/**
	 * getQuitDate
	 */
	public java.lang.String getQuitDate();
	/**
	 * setName
	 */
	public void setName(java.lang.String name);
	/**
	 * setDesign
	 */
	public void setDesign(java.lang.String design);
	/**
	 * setGroup
	 */
	public void setGroup(java.lang.String group);
	/**
	 * setRole
	 */
	public void setRole(java.lang.String role);
	/**
	 * setPass
	 */
	public void setPass(java.lang.String pass);
	/**
	 * setAcc
	 */
	public void setAcc(java.lang.String acc);
	/**
	 * setStatus
	 */
	public void setStatus(java.lang.String status);
	/**
	 * setEmail
	 */
	public void setEmail(java.lang.String email);
	/**
	 * setStartDate
	 */
	public void setStartDate(java.lang.String strDate);
	/**
	 * setQuitDate
	 */
	public void setQuitDate(java.lang.String strDate);
	/**
	 * insertRow
	 */
	public int insertRow(
		java.lang.String name,
		java.lang.String design,
		java.lang.String group,
		java.lang.String role,
		java.lang.String pass,
		java.lang.String acc,
		java.lang.String strStatus,
		java.lang.String strEmail,
		java.lang.String StartDate)
		throws java.sql.SQLException;
	/**
	 * deleteRow
	 */
	public void deleteRow(java.lang.Integer key) throws java.sql.SQLException;
	/**
	 * loadRow
	 */
	public void loadRow(java.lang.Integer key) throws java.sql.SQLException;
	/**
	 * storeRow
	 */
	public int storeRow(java.lang.Integer key) throws java.sql.SQLException;
	/**
	 * selectAllKey
	 */
	public java.util.Collection selectAllKey(
		java.lang.String condi,
		java.lang.String strStatus,
		java.lang.String strSortBy,
		java.lang.String strDirection)
		throws java.sql.SQLException;
	/**
	 * selectGroup
	 */
	public java.util.Collection selectGroup() throws java.sql.SQLException;
	/**
	 * countDev
	 */
	public int countDev(java.lang.String strGroup, java.lang.String strStatus)
		throws java.sql.SQLException;
	/**
	 * getAccountList
	 */
	public java.util.ArrayList getAccountList()
		throws java.sql.SQLException, java.lang.Exception;
	/**
	 * getDeveloperList
	 */
	public java.util.ArrayList getDeveloperList(
		java.lang.String strSortBy,
		java.lang.String strDirection,
		int nPage)
		throws java.sql.SQLException, java.lang.Exception;
	/**
	 * getDeveloperNumber
	 */
	public int getDeveloperNumber()
		throws java.sql.SQLException, java.lang.Exception;
}
