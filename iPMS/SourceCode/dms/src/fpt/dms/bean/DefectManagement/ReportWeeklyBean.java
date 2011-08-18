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
 
 package fpt.dms.bean.DefectManagement;

import fpt.dms.framework.util.StringUtil.StringMatrix;

public class ReportWeeklyBean
{
	private int nProjectID;
	private StringMatrix WeeklyReportList = null;
	private int nSubReport = 0;
	private StringMatrix stmSubReport = null;
	////////////////////////////////////////////////////////////////////////////////
	public void setProjectID(int position)
	{
		nProjectID = position;
	}
	public int getProjectID()
	{
		return nProjectID;
	}
	public StringMatrix getWeeklyReportList()
	{
		return WeeklyReportList;
	}
	public void setWeeklyReportList(StringMatrix WeeklyReportList)
	{
		this.WeeklyReportList = WeeklyReportList;
	}
	//Thaison - Sep 30, 2002
	public int getSubReportType()
	{
		return this.nSubReport;
	}
	public void setSubReportType(int nType)
	{
		this.nSubReport = nType;
	}
	public StringMatrix getSubSummaryReport()
	{
		return stmSubReport;
	}
	public void setSubSummaryReport(StringMatrix data)
	{
		this.stmSubReport = data;
	}
}