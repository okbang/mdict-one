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
 
 package fpt.timesheet.Exemption.ejb;
/**
 * Local interface for Enterprise Bean: ExemptionEJB
 */
public interface ExemptionEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * getCurrentPage
	 */
	public int getCurrentPage();
	/**
	 * getTotalPage
	 */
	public int getTotalPage();
	/**
	 * getTotalExemption
	 */
	public int getTotalExemption();
	/**
	 * hasExistedExemption
	 */
	public java.util.Collection hasExistedExemption(
		int intExemptionId,
		int intDevId,
		int intType,
		java.lang.String strSearchFromDate,
		java.lang.String strSearchToDate)
		throws java.sql.SQLException;
	/**
	 * hasExistedAccount
	 */
	public java.util.Collection hasExistedAccount(
		java.lang.String strAccount,
		java.lang.String strName)
		throws java.sql.SQLException;
	/**
	 * getDeveloperListEJB
	 */
	public java.util.Collection getDeveloperListEJB()
		throws java.sql.SQLException;
	/**
	 * getExemptionListEJB
	 */
	public java.util.Collection getExemptionListEJB(
		java.lang.String strGroupName,
		java.lang.String strAccount,
		java.lang.String strName,
		int intType,
		java.lang.String strSearchFromDate,
		java.lang.String strSearchToDate,
		int intCurPage)
		throws java.sql.SQLException;
	/**
	 * getExemptionByIdEJB
	 */
	public fpt.timesheet.Exemption.ejb.ExemptionBean getExemptionByIdEJB(
		int intExemptionId)
		throws java.sql.SQLException;
		
	// Added by HaiMM
	public fpt.timesheet.Exemption.ejb.ExemptionBean getDummyExemptionByIdEJB(
			int intExemptionId)
			throws java.sql.SQLException;
	
	public void addDummyExemptionEJB(
			int intExemptionId, String status)
			throws java.sql.SQLException;
			
	public void updateDummyExemptionEJB(
				int intExemptionId)
				throws java.sql.SQLException;
				
	public java.util.Collection getDummyMigrationByIdEJB()
				throws java.sql.SQLException;
	
	public void updateDummyMigrationEJB(
				int intExemptionId, String status)
				throws java.sql.SQLException;
			
	/**
	 * addExemptionEJB
	 */
	public void addExemptionEJB(
		int intDevId,
		int intType,
		java.lang.String strWeekDay,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strReason,
		java.lang.String strNote)
		throws java.sql.SQLException;
	/**
	 * updateExemptionEJB
	 */
	public void updateExemptionEJB(
		int intExemptionId,
		int intType,
		java.lang.String strWeekDay,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strReason,
		java.lang.String strNote)
		throws java.sql.SQLException;
		
	
	/**
	 * deleteExemptionEJB
	 */
	public void deleteExemptionEJB(java.lang.String strExemptionId)
		throws java.sql.SQLException;
	/**
	 * getTimesheetExemptionEJB
	 */
	public int getExemptionNewId(String strExemptionId) throws java.sql.SQLException;
	
	public java.util.Vector getTimesheetExemptionEJB(
		java.lang.String strGroup,
		java.lang.String strSearchFromDate,
		java.lang.String strSearchToDate)
		throws java.sql.SQLException;
	/**
	 * getTrackingReportEJB
	 */
	public java.util.Collection getTrackingReportEJB(
		fpt.timesheet.vo.TrackingByProjectForm formRequest)
		throws java.sql.SQLException;
	/**
	 * getArrAssignment
	 */
	public java.util.Collection getArrAssignment(
		java.lang.String strGroup,
		int intProjectID,
		java.lang.String arrProjectIDs,
		int intProjectStatus,
		java.lang.String strFromDate,
		java.lang.String strToDate)
		throws java.sql.SQLException;
	/**
	 * getVctAssignment
	 */
	public java.util.Vector getVctAssignment() throws java.sql.SQLException;
}
