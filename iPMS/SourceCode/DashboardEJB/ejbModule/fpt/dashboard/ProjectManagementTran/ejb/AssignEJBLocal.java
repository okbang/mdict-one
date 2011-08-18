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
 
 package fpt.dashboard.ProjectManagementTran.ejb;
/**
 * Local interface for Enterprise Bean: AssignEJB
 */
public interface AssignEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * getProjectID
	 */
	public java.lang.Integer getProjectID();
	/**
	 * getAssignID
	 */
	public java.lang.Integer getAssignID();
	/**
	 * getProjectName
	 */
	public java.lang.String getProjectName();
	/**
	 * getProjectLeader
	 */
	public java.lang.String getProjectLeader();
	/**
	 * getProjectCode
	 */
	public java.lang.String getProjectCode();
	/**
	 * getProjectStart
	 */
	public java.lang.String getProjectStart();
	/**
	 * getProjectFinish
	 */
	public java.lang.String getProjectFinish();
	/**
	 * getDesc
	 */
	public java.lang.String getDesc();
	/**
	 * getType
	 */
	public java.lang.Integer getType();
	/**
	 * getStartWeek
	 */
	public java.lang.Integer getStartWeek();
	/**
	 * getEndWeek
	 */
	public java.lang.Integer getEndWeek();
	/**
	 * getUsage
	 */
	public int getUsage();
	/**
	 * getRes
	 */
	public java.lang.String getRes();
	/**
	 * getProjectInfor
	 */
	public void getProjectInfor(java.lang.Integer ProjectID)
		throws java.sql.SQLException;
	/**
	 * getDeveloperID
	 */
	public java.lang.Integer getDeveloperID();
	/**
	 * getDeveloperName
	 */
	public java.lang.String getDeveloperName();
	/**
	 * getBegin
	 */
	public java.lang.String getBegin();
	/**
	 * getEnd
	 */
	public java.lang.String getEnd();
	/**
	 * setProjectID
	 */
	public void setProjectID(java.lang.Integer ID);
	/**
	 * setProjectName
	 */
	public void setProjectName(java.lang.String Name);
	/**
	 * setProjectLeader
	 */
	public void setProjectLeader(java.lang.String Name);
	/**
	 * setProjectCode
	 */
	public void setProjectCode(java.lang.String Code);
	/**
	 * setProjectStart
	 */
	public void setProjectStart(java.lang.String End);
	/**
	 * setProjectFinish
	 */
	public void setProjectFinish(java.lang.String End);
	/**
	 * setDesc
	 */
	public void setDesc(java.lang.String Desc);
	/**
	 * setType
	 */
	public void setType(java.lang.Integer Type);
	/**
	 * setUsage
	 */
	public void setUsage(int Usage);
	/**
	 * setRes
	 */
	public void setRes(java.lang.String res);
	/**
	 * setDeveloperID
	 */
	public void setDeveloperID(java.lang.Integer ID);
	/**
	 * setDeveloperName
	 */
	public void setDeveloperName(java.lang.String Name);
	/**
	 * setBegin
	 */
	public void setBegin(java.lang.String Begin);
	/**
	 * setEnd
	 */
	public void setEnd(java.lang.String End);
	/**
	 * insertRow
	 */
	public void insertRow(
		java.lang.Integer ProjectID,
		java.lang.Integer DeveloperID,
		java.lang.String Begin,
		java.lang.String End,
		java.lang.Integer Type)
		throws java.sql.SQLException;
	/**
	 * deleteRow
	 */
	public void deleteRow(java.lang.Integer AssignID)
		throws java.sql.SQLException;
	/**
	 * loadRow
	 */
	public void loadRow(java.lang.Integer AssignID)
		throws java.sql.SQLException;
	/**
	 * loadOtherRow
	 */
	public void loadOtherRow(java.lang.Integer AssignID)
		throws java.sql.SQLException;
	/**
	 * storeRow
	 */
	public void storeRow(java.lang.Integer AssignID)
		throws java.sql.SQLException;
	/**
	 * selectAllKey
	 */
	public java.util.Collection selectAllKey() throws java.sql.SQLException;
	/**
	 * selectByDeveloper
	 */
	public java.util.Collection selectByDeveloper()
		throws java.sql.SQLException;
	/**
	 * selectBussyDeveloper
	 */
	public java.util.Collection selectBussyDeveloper(
		java.lang.String startPeriod,
		java.lang.String endPeriod,
		java.lang.String Condi)
		throws java.sql.SQLException;
	/**
	 * selectFreeDeveloper
	 */
	public java.util.Collection selectFreeDeveloper(
		java.lang.String strID,
		java.lang.String Condi)
		throws java.sql.SQLException;
	/**
	 * selectOtherByDeveloper
	 */
	public java.util.Collection selectOtherByDeveloper()
		throws java.sql.SQLException;
}
