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
public class MilestoneUpdateBean
{
	//Project properties
	private String strProjectID = null;
	private String strProjectName = null;
	private String strProjectCode = null;
	private String strProjectStartDate = null;
	private String strProjectBaseFinishDate = null;
	private String strProjectPlanFinishDate = null;
	private String strProjectActualFinishDate = null;
	//Milestone properties
	private String strID = null;
	private String strName = null;
	private int nComplete = -1;
	private String strBaseFinishDate = null;
	private String strPlanFinishDate = null;
	private String strActualFinishDate = null;
	private String strDescription = null;
	///////////////////////////////////////////////////////////////////////
	//***************SET-GET MOTHODS**************************************
	public String getID()
	{
		return strID;
	}
	public void setID(String data)
	{
		if (data != null)
			strID = data;
	}
	public String getName()
	{
		return strName;
	}
	public void setName(String data)
	{
		if (data != null)
			strName = data;
	}
	public int getComplete()
	{
		return nComplete;
	}
	public void setComplete(int data)
	{
		nComplete = data;
	}
	public String getBaseFinishDate()
	{
		return strBaseFinishDate;
	}
	public void setBaseFinishDate(String data)
	{
		if (data != null)
			strBaseFinishDate = data;
	}
	public String getPlanFinishDate()
	{
		return strPlanFinishDate;
	}
	public void setPlanFinishDate(String data)
	{
		if (data != null)
			strPlanFinishDate = data;
	}
	public String getActualFinishDate()
	{
		return strActualFinishDate;
	}
	public void setActualFinishDate(String data)
	{
		if (data != null)
			strActualFinishDate = data;
	}
	public String getDescription()
	{
		return strDescription;
	}
	public void setDescription(String data)
	{
		if (data != null)
			strDescription = data;
	}
	/////////////////////////////////////////////////////////////////////////
	//PROEJCT METHODS
	//ID
	public String getProjectID()
	{
		return strProjectID;
	}
	public void setProjectID(String data)
	{
		if (data != null)
			{
			strProjectID = data;
		}
	}
	//CODE
	public String getProjectCode()
	{
		return strProjectCode;
	}
	public void setProjectCode(String data)
	{
		if (data != null)
			strProjectCode = data;
	}
	//NAME
	public String getProjectName()
	{
		return strProjectName;
	}
	public void setProjectName(String data)
	{
		if (data != null)
			strProjectName = data;
	}
	//START DATE
	public String getProjectStartDate()
	{
		return strProjectStartDate;
	}
	public void setProjectStartDate(String data)
	{
		if (data != null)
			strProjectStartDate = data;
	}
	//BASE FINISH DATE
	public String getProjectBaseFinishDate()
	{
		return strProjectBaseFinishDate;
	}
	public void setProjectBaseFinishDate(String data)
	{
		if (data != null)
			strProjectBaseFinishDate = data;
	}
	//PLAN FINISH DATE
	public String getProjectPlanFinishDate()
	{
		return strProjectPlanFinishDate;
	}
	public void setProjectPlanFinishDate(String data)
	{
		if (data != null)
			strProjectPlanFinishDate = data;
	}
	//ACTUAL FINISH DATE
	public String getProjectActualFinishDate()
	{
		return strProjectActualFinishDate;
	}
	public void setProjectActualFinishDate(String data)
	{
		if (data != null)
			strProjectActualFinishDate = data;
	}
}