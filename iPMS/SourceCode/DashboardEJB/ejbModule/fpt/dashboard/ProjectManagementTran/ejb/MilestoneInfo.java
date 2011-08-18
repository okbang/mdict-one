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
import java.util.*;
public class MilestoneInfo implements java.io.Serializable
{
	private int nID = 0;
	private String strName = "";
	private int nComplete = 0;
	
	private String strBaseStartDate = "";
	private String strPlanStartDate = "";
	private String strActualStartDate = "";
	
	private String strBaseFinishDate = "";
	private String strPlanFinishDate = "";
	private String strActualFinishDate = "";
	private String strStatus = "";
	
	private int nBaseEffort = 0;
	private int nPlanEffort = 0;
	private int nActualEffort = 0;
	
	
	private String strDescription = "";
	
	//Project information
	private int nProjectID = 0;
	private String strProjectCode;
	private String strProjectName;
	
	///////////////////////////////////////////////////////////////////////////
	//Project database
	//SET-GET METHODS
	public void setID(int id)
	{
			nID = id;
	}
	public int getID()
	{
		return nID;
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
	public void setComplete(int complete)
	{
			nComplete = complete;
	}
	public int getComplete()
	{
		return nComplete;
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
	
	///////////////
	public void setBaseStartDate(String data)
	{
		if (data != null)
			strBaseStartDate = data;
	}
	public String getBaseStartDate()
	{
		return strBaseStartDate;
	}
	public void setPlanStartDate(String data)
	{
		if (data != null)
			strPlanStartDate = data;
	}
	public String getPlanStartDate()
	{
		return strPlanStartDate;
	}
	public void setActualStartDate(String data)
	{
		if (data != null)
			strActualStartDate = data;
	}
	public String getActualStartDate()
	{
		return strActualStartDate;
	}
	///////////////
	public void setBaseEffort(int data)
	{
			nBaseEffort = data;
	}
	public int getBaseEffort()
	{
		return nBaseEffort;
	}
	public void setPlanEffort(int data)
	{
			nPlanEffort = data;
	}
	public int getPlanEffort()
	{
		return nPlanEffort;
	}
	public void setActualEffort(int data)
	{
			nActualEffort = data;
	}
	public int getActualEffort()
	{
		return nActualEffort;
	}
	public void setDescription(String data)
	{
		if (data != null)
			strDescription = data;
	}
	public String getDescription()
	{
		return strDescription;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public int getProjectID()
	{
		return this.nProjectID;
	}
	public void setProjectID(int nID)
	{
		this.nProjectID = nID;
	}
	
	public String getProjectCode()
	{
		return this.strProjectCode;
	}
	public void setProjectCode(String data)
	{
		this.strProjectCode = data;
	}
	
	public String getProjectName()
	{
		return this.strProjectName;
	}
	public void setProjectName(String data)
	{
		this.strProjectName = data;
	}
	
	public void setStatus(String data)
	{
		if (data != null)
			strStatus = data;
	}
	public String getStatus()
	{
		return strStatus;
	}

}