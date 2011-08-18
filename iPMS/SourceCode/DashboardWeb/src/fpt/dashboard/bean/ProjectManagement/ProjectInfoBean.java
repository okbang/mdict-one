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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectInfoBean
{
	////////////////////////////////////////////////////////
	//Collection of Project
	private ArrayList ModelObjectsList = null;
	private int NumberOfElements = 0;
	//Collection of Group
	private ArrayList ModelGroupObjectsList = null;
	private int NumberOfGroupElements = 0;
	//Collection of Leader
	private ArrayList ModelLeaderObjectsList = null;
	private int NumberOfLeaderElements = 0;
	/////////////////////////////////////////////////////////
	// Chart
	private Collection requirementChartDataList = null;
	private Collection defectChartDataList = null;
	// Sub list
	private Collection MilestoneList = null;
	private Collection AssignmentList = null;
	private Collection IssueList = null;
	////////////////////////
	private String ProjectId = "";
	private String ProjectCode = "";
	private String ProjectName = "";
	private String ProjectLeader = "";
	private String lastUpdate = "";
	/////////////////////////////
	private String type = "";
	private String group = "";
	private String status = "";
	private String datafile = "";
	////////////////////////////////////////////
	private String planStartDate = "";
	private String startDate = "";
	private String perComplete = "";
	private String description = "";
	private String scheduleStatus = "NA";
	private String planFinishDate = "";
	private String baseFinishDate = "";
	private String actualFinishDate = "";
	private String planEffort = "";
	private String baseEffort = "";
	private String actualEffort = "";
	private String effortStatus = "NA";
	private String totalRequirement;
	private String totalDefect;
	private String totalWeightedDefect;
	String customer = "";
	String cate = "";
	public ProjectInfoBean()
	{
	}
	public void setProjectId(String pId)
	{
		this.ProjectId = pId;
	}
	public String getProjectId()
	{
		return this.ProjectId;
	}
	public void setProjectCode(String code)
	{
		this.ProjectCode = code;
	}
	public String getProjectCode()
	{
		return this.ProjectCode;
	}
	public void setProjectName(String name)
	{
		this.ProjectName = name;
	}
	public String getProjectName()
	{
		return this.ProjectName;
	}
	public void setProjectLeader(String Leader)
	{
		this.ProjectLeader = Leader.toUpperCase();
	}
	public String getProjectLeader()
	{
		return this.ProjectLeader;
	}
	//////////////////////////////
	public void setType(String str)
	{
		type = str;
	}
	public String getType()
	{
		return type;
	}
	public void setGroup(String str)
	{
		group = str;
	}
	public String getGroup()
	{
		return group;
	}
	public void setDescription(String str)
	{
		description = str;
	}
	public String getDescription()
	{
		return description;
	}
	public String getPlanStartDate(){
		return planStartDate;
	}
	public void setPlanStartDate(String str){
		planStartDate = str;
	}
	public String getStartDate()
	{
		return startDate;
	}
	public void setStartDate(String str)
	{
		startDate = str;
	}
	public String getPerComplete()
	{
		return perComplete;
	}
	public void setPerComplete(String str)
	{
		perComplete = str;
	}
	public String getFinishDate()
	{
		return baseFinishDate;
	}
	public String getBaseFinishDate()
	{
		return baseFinishDate;
	}
	public String getPlanFinishDate()
	{
		return planFinishDate;
	}
	public String getActualFinishDate()
	{
		return actualFinishDate;
	}
	public void setActualFinishDate(String str)
	{
		actualFinishDate = str;
	}
	public String getBaseEffort()
	{
		return baseEffort;
	}
	public String getPlanEffort()
	{
		return planEffort;
	}
	public String getActualEffort()
	{
		return actualEffort;
	}
	public void setBaseFinishDate(String str)
	{
		baseFinishDate = str;
	}
	public void setPlanFinishDate(String str)
	{
		planFinishDate = str;
	}
	public void setBaseEffort(String strDbl)
	{
		try
			{
			baseEffort = strDbl;
			double temp = Double.parseDouble(strDbl);
		}
		catch (Exception ex)
			{
			baseEffort = "";
		}
	}
	public void setPlanEffort(String strDbl)
	{
		try
			{
			planEffort = strDbl;
			double temp = Double.parseDouble(strDbl);
		}
		catch (Exception ex)
			{
			planEffort = "";
		}
	}
	public void setActualEffort(String strDbl)
	{
		try
			{
			actualEffort = strDbl;
			double temp = Double.parseDouble(strDbl);
		}
		catch (Exception ex)
			{
			actualEffort = "";
		}
	}
	/////////////////////////////////////////////
	public String getTotalRequirement()
	{
		return totalRequirement;
	}
	public String getTotalDefect()
	{
		return totalDefect;
	}
	public String getTotalWeightedDefect()
	{
		return totalWeightedDefect;
	}
	public String getScheduleStatus()
	{
		return scheduleStatus;
	}
	//////////////////////////////////
	public void setTotalRequirement(String str)
	{
		this.totalRequirement = str;
	}
	public void setTotalDefect(String str)
	{
		this.totalDefect = str;
	}
	public void setTotalWeightedDefect(String str)
	{
		this.totalWeightedDefect = str;
	}
	public void setScheduleStatus(String str)
	{
		this.scheduleStatus = str;
	}
	/////////////////////////////////
	public Collection getRequirementChartData()
	{
		return this.requirementChartDataList;
	}
	public Collection getDefectChartData()
	{
		return this.defectChartDataList;
	}
	public void setRequirementChartData(Collection cl)
	{
		this.requirementChartDataList = cl;
	}
	public void setDefectChartData(Collection cl)
	{
		this.defectChartDataList = cl;
	}
	//////////////////////////////////////
	// Milestone List
	public boolean setMilestoneList(Collection List)
	{
		if (List != null)
			{
			this.MilestoneList = List;
			return true;
		}
		return false;
	}
	public Collection getMilestoneList()
	{
		return this.MilestoneList;
	}
	// Assignment List
	public boolean setAssignmentList(Collection List)
	{
		if (List != null)
			{
			this.AssignmentList = List;
			return true;
		}
		return false;
	}
	public Collection getAssignmentList()
	{
		return this.AssignmentList;
	}
	// Issue List
	public boolean setIssueList(Collection List)
	{
		if (List != null)
			{
			this.IssueList = List;
			return true;
		}
		return false;
	}
	public Collection getIssueList()
	{
		return this.IssueList;
	}
	////////////////////////////////////////////////////////
	public int getNumberOfElements()
	{
		if (ModelObjectsList == null)
			return 0;
		return ModelObjectsList.size();
	}
	public int getNumberOfGroupElements()
	{
		if (ModelGroupObjectsList == null)
			return 0;
		return ModelGroupObjectsList.size();
	}
	public int getNumberOfLeaderElements()
	{
		if (ModelLeaderObjectsList == null)
			return 0;
		return ModelLeaderObjectsList.size();
	}
	///////////////////////////////////
	public boolean addProject(ProjectInfoBean data)
	{
		if (ModelObjectsList == null)
			ModelObjectsList = new ArrayList();
		return ModelObjectsList.add(data);
	}
	public ProjectInfoBean getProject(int index)
	{
		if (index < ModelObjectsList.size())
			return (ProjectInfoBean) ModelObjectsList.get(index);
		else
			return null;
	}
	///////////////////////////////////
	public boolean addGroupItem(String data)
	{
		if (ModelGroupObjectsList == null)
			ModelGroupObjectsList = new ArrayList();
		return ModelGroupObjectsList.add(data);
	}
	public String getGroupItem(int index)
	{
		if (index < ModelGroupObjectsList.size())
			return (String) ModelGroupObjectsList.get(index);
		else
			return null;
	}
	//////////////////////////////////////////////
	public String getLeaderItem(int index)
	{
		if (index < ModelLeaderObjectsList.size())
			return (String) ModelLeaderObjectsList.get(index);
		else
			return null;
	}
	public boolean addLeaderItem(String data)
	{
		if (ModelLeaderObjectsList == null)
			ModelLeaderObjectsList = new ArrayList();
		return ModelLeaderObjectsList.add(data);
	}
	public String getDatafile()
	{
		return datafile;
	}
	public void setDatafile(String datafile)
	{
		this.datafile = datafile;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String str)
	{
		this.status = str;
	}
	public String getEffortStatus()
	{
		return effortStatus;
	}
	public void setEffortStatus(String effortStatus)
	{
		this.effortStatus = effortStatus;
	}
	public String getLastUpdate()
	{
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}
	public String getCate()
	{
		return cate;
	}
	public void setCate(String cate)
	{
		this.cate = cate;
	}
	public String getCustomer()
	{
		return customer;
	}
	public void setCustomer(String customer)
	{
		this.customer = customer;
	}
	private String strTotalPending = null;
	public String getTotalPending()
	{
		return this.strTotalPending;
	}
	public void setTotalPending(String data)
	{
		this.strTotalPending = data;
	}
}