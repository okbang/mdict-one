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
 
 package fpt.timesheet.ApproveTran.ejb.approve;
/**
 * Local interface for Enterprise Bean: ApproveEJB
 */
public interface ApproveEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * getTimesheetList
	 */
	public java.util.ArrayList getTimesheetList(
		java.lang.String strListingName,
		int nDevId,
		java.lang.String sProjectIdList,
		int nStatus,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strAccount,
		int nSortBy,
		int nCurrentPage)
		throws java.sql.SQLException;
	/**
	 * getTimesheetList
	 */
	public java.util.ArrayList getTimesheetList(
		java.lang.String strListingName,
		java.lang.String strIdList,
		int nSortBy)
		throws java.sql.SQLException;
	/**
	 * changeStatus
	 */
	public void changeStatus(
		java.lang.String strListingName,
		int nStatus,
		java.lang.String[] arrIdList,
		java.lang.String[] arrComment,
		java.lang.String strApprover)
		throws java.sql.SQLException;
	/**
	 * changeStatus
	 */
	public void changeStatus(
		java.lang.String strListingName,
		int nStatus,
		java.lang.String[] arrIdList,
		java.lang.String[] arrComment,
		java.lang.String[] arrKPA,
		java.lang.String strApprover)
		throws java.sql.SQLException;
	/**
	 * LDcorrect
	 */
	public void LDcorrect(
		java.lang.String strApprover,
		java.lang.String[] sId,
		java.lang.String[] sProcess,
		java.lang.String[] sType,
		java.lang.String[] sProduct,
		java.lang.String[] sDuration,
		java.lang.String[] sDescription,
		java.lang.String[] sDate)
		throws java.sql.SQLException;
	/**
	 * QAcorrect
	 */
	public void QAcorrect(
		java.lang.String strApprover,
		int nStatus,
		java.lang.String[] sId,
		java.lang.String[] sType,
		java.lang.String[] sProcess,
		java.lang.String[] sProduct,
		java.lang.String[] sKpa,
		java.lang.String[] sDate)
		throws java.sql.SQLException;
	/**
	 * getTotalPage
	 */
	public int getTotalPage();
	/**
	 * getTotalTimesheet
	 */
	public int getTotalTimesheet();
}
