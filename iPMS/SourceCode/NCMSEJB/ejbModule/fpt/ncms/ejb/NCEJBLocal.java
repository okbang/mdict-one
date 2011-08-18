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
 
 package fpt.ncms.ejb;
/**
 * Local interface for Enterprise Bean: NCEJB
 */
public interface NCEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * queryByID
	 */
	public fpt.ncms.model.NCModel queryByID(
		java.lang.String strNCID,
		int nLocation)
		throws java.sql.SQLException;
	/**
	 * queryByCriteria
	 */
	public java.util.ArrayList queryByCriteria(
		java.lang.String strUser,
		java.lang.String strRole,
		java.lang.String strFields,
		java.lang.String strCondition,
		java.lang.String strOrderBy,
		int nDirection,
		int nFromRow,
		int nToRow,
		java.lang.String strFromDate,
		java.lang.String strToDate,
		int nUsage)
		throws java.sql.SQLException;
	/**
	 * getNumByCriteria
	 */
	public int getNumByCriteria(
		java.lang.String strUser,
		java.lang.String strRole,
		java.lang.String strCondition,
		int nLocation,
		java.lang.String strFromDate,
		java.lang.String strToDate)
		throws java.sql.SQLException;
	/**
	 * addNC
	 */
	public int addNC(fpt.ncms.model.NCModel modelNC, int nLocation)
		throws java.sql.SQLException, java.lang.Exception;
	/**
	 * updateNC
	 */
	public int updateNC(
		fpt.ncms.model.NCModel modelNC,
		java.lang.String strUser,
		int nLocation)
		throws java.sql.SQLException, java.lang.Exception;
	/**
	 * deleteNC
	 */
	public int deleteNC(fpt.ncms.model.NCModel modelNC, int nLocation)
		throws java.sql.SQLException, java.lang.Exception;
	/**
	 * queryForReport
	 */
	public java.util.ArrayList queryForReport(
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strGroupBy,
		int nUsage,
		java.lang.String strNCTypes,
		int nTypeOfCause)
		throws java.sql.SQLException;
	/**
	 * queryForReportAll
	 */
	public java.util.ArrayList queryForReportAll(
		java.lang.String strFromDate,
		java.lang.String strToDate,
		int nUsage,
		java.lang.String strNCTypes,
		int nTypeOfCause)
		throws java.sql.SQLException;
	/**
	 * queryForPivotReport
	 */
	public java.util.ArrayList queryForPivotReport(
		java.lang.String strFromDate,
		java.lang.String strToDate,
		java.lang.String strGroupBy,
		java.lang.String strType,
		int nUsage,
		java.lang.String strNCTypes,
		int nTypeOfCause)
		throws java.sql.SQLException;
	/**
	 * getNCHistory
	 */
	public java.lang.String getNCHistory(java.lang.String strID, int nLocation)
		throws java.sql.SQLException;
}
