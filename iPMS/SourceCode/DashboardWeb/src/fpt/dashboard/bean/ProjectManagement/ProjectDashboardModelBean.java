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
 
 package fpt.dashboard.bean.ProjectManagement;
public class ProjectDashboardModelBean
{
	private String strID = null;
	private String strCode = null;
	private String strName = null;
	private String strComplete = null;
	private String strScheduleStatus = null;
	private String strEffortStatus = null;
	private String strStartDate = null;
	private String strBaseFinishDate = null;
	private String strPlanFinishDate = null;
	private String strActualFinishDate = null;
	private String strBaseEffort = null;
	private String strPlanEffort = null;
	private String strActualEffort = null;
	private double listOrder;
	//SET-GET METHODS
	////////////////////////////////////////////////////////////////////////////
	public String getID()
	{
		return strID;
	}
	public void setID(String data)
	{
		if (data != null)
			strID = data;
	}
	public void setCode(String code)
	{
		if (code != null)
			strCode = code;
	}
	public String getCode()
	{
		return strCode;
	}
	public void setName(String name)
	{
		if (name != null)
			strName = name;
	}
	public String getName()
	{
		return strName;
	}
	public void setComplete(String complete)
	{
		if (complete != null)
			strComplete = complete;
	}
	public String getComplete()
	{
		return strComplete;
	}
	public void setScheduleStatus(String status)
	{
		if (status != null)
			strScheduleStatus = status;
	}
	public String getScheduleStatus()
	{
		return strScheduleStatus;
	}
	public void setEffortStatus(String effort)
	{
		if (effort != null)
			strEffortStatus = effort;
	}
	public String getEffortStatus()
	{
		return strEffortStatus;
	}
	public void setStartDate(String data)
	{
		if (data != null)
			strStartDate = data;
	}
	public String getStartDate()
	{
		return strStartDate;
	}
	public void setBaseFinishDate(String data)
	{
		if (data != null)
			strBaseFinishDate = data;
	}
	public String getBaseFinishDate()
	{
		return strBaseFinishDate;
	}
	public void setPlanFinishDate(String data)
	{
		if (data != null)
			strPlanFinishDate = data;
	}
	public String getPlanFinishDate()
	{
		return strPlanFinishDate;
	}
	public void setActualFinishDate(String data)
	{
		if (data != null)
			strActualFinishDate = data;
	}
	public String getActualFinishDate()
	{
		return strActualFinishDate;
	}
	public void setBaseEffort(String data)
	{
		if (data != null)
			strBaseEffort = data;
	}
	public String getBaseEffort()
	{
		return strBaseEffort;
	}
	public void setPlanEffort(String data)
	{
		if (data != null)
			strPlanEffort = data;
	}
	public String getPlanEffort()
	{
		return strPlanEffort;
	}
	public void setActualEffort(String data)
	{
		if (data != null)
			strActualEffort = data;
	}
	public String getActualEffort()
	{
		return strActualEffort;
	}
	public double getListOrder()
	{
		return listOrder;
	}
	public void setListOrder(double listOrder)
	{
		this.listOrder = listOrder;
	}
}