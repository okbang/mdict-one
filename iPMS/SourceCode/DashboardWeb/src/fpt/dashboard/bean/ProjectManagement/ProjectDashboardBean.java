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

import fpt.dashboard.util.StringUtil.StringMatrix;

public class ProjectDashboardBean
{
	//Fields in the Project Dashboard Form
	private StringMatrix ProjectDBList = null; //main table
	private StringMatrix StatusList = null;
	private StringMatrix orderList = null;
    private StringMatrix typeList = null;            //ThaiLH
    private StringMatrix smCategoryList = null;   //minhPT
	private String strCategory = "";
    private String strSelectedProject = "";
	private String selectStatus = "";
	private String strSelectedGroup = null; //Selected group
	private StringMatrix lstGroup = null; //Group combo
    
	private int orderBy = 0;
	private String strType;					//ThaiLH

	///////////////////////////////////////////////////////////
	//GET-SET methods
	public String getDoViewProjectDetail()
	{
		return strSelectedProject;
	}
	public StringMatrix getProjectDashboardList()
	{
		return ProjectDBList;
	}
	public void setProjectDashboardList(StringMatrix list)
	{
		this.ProjectDBList = list;
	}
	public void setGroupName(String group)
	{
		if (group != null)
			this.strSelectedGroup = group;
	}
	public String getGroupName()
	{
		return strSelectedGroup;
	}
	//ThaiLH
	public void setType(String str)
	{
		this.strType = str;
	}
	public String getType()
	{
		return this.strType;
	}

	//ThaiLH
	public StringMatrix getGroupList()
	{
		return lstGroup;
	}
	public void setGroupList(StringMatrix list)
	{
		if (list != null)
			this.lstGroup = list;
	}
	//ThaiLH	
    public StringMatrix getTypeList()
    {
        return typeList;
    }
	
	public String getSelectStatus()
	{
		return selectStatus;
	}
	public void setSelectStatus(String selectStatus)
	{
		this.selectStatus = selectStatus;
	}
	public StringMatrix getStatusList()
	{
		return StatusList;
	}
	public void setStatusList(StringMatrix StatusList)
	{
		this.StatusList = StatusList;
	}
	public int getOrderBy()
	{
		return orderBy;
	}
	public void setOrderBy(int orderBy)
	{
		this.orderBy = orderBy;
	}
	public StringMatrix getOrderList()
	{
		return orderList;
	}
	public void setOrderList(StringMatrix orderList)
	{
		this.orderList = orderList;
	}
	public void setTypeList(StringMatrix typeList) {
		if (typeList != null)
            this.typeList = typeList;

	}
    public StringMatrix getCategoryList()
    {
        return this.smCategoryList;
    }
    public void setCategoryList(StringMatrix smCategory)
    {
        this.smCategoryList = smCategory;
    }
    public String getCategory()
    {
        return this.strCategory;
    }
    public void setCategory(String strCate)
    {
        this.strCategory = strCate;
    }

}