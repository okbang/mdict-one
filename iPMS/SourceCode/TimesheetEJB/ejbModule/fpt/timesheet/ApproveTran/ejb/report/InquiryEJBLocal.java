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
 
 package fpt.timesheet.ApproveTran.ejb.report;
/**
 * Local interface for Enterprise Bean: InquiryEJB
 */
public interface InquiryEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * ViewTimesheet
	 */
	public java.util.Collection ViewTimesheet(
		java.lang.String strAccount,
		java.lang.String strApprover,
		int nProjectID,
		java.lang.String arrProjectID,
		int nStatus,
		java.lang.String strDateFrom,
		java.lang.String strDateTo,
		int nSort,
		java.lang.String strGroup,
		int nCurrentPage,
		boolean bIsForExport)
		throws java.sql.SQLException;
	/**
	 * getTotalPage
	 */
	public int getTotalPage();
	/**
	 * getTotalTimesheet
	 */
	public int getTotalTimesheet();
	/**
	 * getSummaryReportData
	 */
	public java.util.Collection getSummaryReportData(
		int nProjectID,
		java.lang.String arrProjectIDs,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		int nStatus,
		int type,
		java.lang.String strDeveloperID)
		throws java.sql.SQLException;
	/**
	 * getPendingReportData
	 */
	public java.util.Collection getPendingReportData(
		java.lang.String arrProjectIDs,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strGroup)
		throws java.sql.SQLException;
	/**
	 * getUnreviewedPMEJB
	 */
	public java.util.Collection getUnreviewedPMEJB(
		java.lang.String strGroup,
		int intProjectID,
		java.lang.String arrProjectIDs,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strLogDateTime,
		int intProjectStatus)
		throws java.sql.SQLException;
	/**
	 * getUnreviewedQAEJB
	 */
	public java.util.Collection getUnreviewedQAEJB(
		java.lang.String strGroup,
		java.lang.String strPQAName,
		int intProjectID,
		java.lang.String arrProjectIDs,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strLogDateTime,
		int intProjectStatus)
		throws java.sql.SQLException;
	/**
	 * getPQAList
	 */
	public java.util.Collection getPQAList(
		int intProjectID,
		java.lang.String arrProjectIDs,
		java.lang.String strPQAName)
		throws java.sql.SQLException;
	/**
	 * getProjectDistribution
	 */
	public java.util.ArrayList getProjectDistribution(
		int nProjectID,
		java.lang.String arrProjectIDs,
		int nStatus,
		java.lang.String strFrom,
		java.lang.String strTo,
		int nReportType,
		int type,
		java.lang.String strDeveloperID)
		throws java.sql.SQLException;
	/**
	 * getProjectSummary
	 */
	public java.util.ArrayList getProjectSummary(
		int nProjectID,
		java.lang.String arrProjectIDs,
		int nStatus,
		java.lang.String strFrom,
		java.lang.String strTo,
		int nReportType,
		int type,
		java.lang.String strDeveloperID)
		throws java.sql.SQLException;
}
