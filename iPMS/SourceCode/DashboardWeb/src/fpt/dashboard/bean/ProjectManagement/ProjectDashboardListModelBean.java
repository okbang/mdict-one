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

public class ProjectDashboardListModelBean
{
	//Retreive Key = UseRule=-1 => All,
	private int UseRule = -1;
	//Collection of Project
	private ArrayList ModelObjectsList = null;
	private int NumberOfElements = 0;
	String status = "0";
	//GROUP COMBO
	private String strSelectedGroup = null;
	private ArrayList arrGroupList = null;
	private int NumberOfGroup = 0;
	int orderBy = 0;
	//////////////////////////////////////////////////////////////////
	public ProjectDashboardListModelBean()
	{
	}
	public void setUserRule(int data)
	{
		this.UseRule = data;
	}
	public int getUseRule()
	{
		return this.UseRule;
	}
	public int getNumberOfElements()
	{
		if (ModelObjectsList == null)
			return 0;
		return ModelObjectsList.size();
	}
	public boolean addProject(ProjectDashboardModelBean data)
	{
		if (ModelObjectsList == null)
			ModelObjectsList = new ArrayList();
		return ModelObjectsList.add(data);
	}
	public ProjectDashboardModelBean getProject(int index)
	{
		if (index < ModelObjectsList.size())
			return (ProjectDashboardModelBean) ModelObjectsList.get(index);
		else
			return null;
	}
	public String getSelectedGroup()
	{
		return strSelectedGroup;
	}
	public void setSelectedGroup(String group)
	{
		if (group != null)
			strSelectedGroup = group;
	}
	public String getGroup(int index)
	{
		if (index < arrGroupList.size())
			return (String) arrGroupList.get(index);
		else
			return null;
	}
	public boolean addGroup(String group)
	{
		if (arrGroupList == null)
			arrGroupList = new ArrayList();
		return arrGroupList.add(group);
	}
	public int getNumberOfGroups()
	{
		if (arrGroupList == null)
			return 0;
		return arrGroupList.size();
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public int getOrderBy()
	{
		return orderBy;
	}
	public void setOrderBy(int orderBy)
	{
		this.orderBy = orderBy;
	}
}