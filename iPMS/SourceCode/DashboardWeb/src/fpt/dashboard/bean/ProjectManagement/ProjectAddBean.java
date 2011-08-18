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

import java.util.List;

import fpt.dashboard.util.StringUtil.StringMatrix;

public class ProjectAddBean
{
	private StringMatrix ProjectList = null;
	private StringMatrix GroupList = null;
	private StringMatrix TypeList = null;
	private StringMatrix LeaderList = null;
	private StringMatrix StatusList = null;
	private StringMatrix CateList = null;
	private String cate;
	//////////////////////////////////////////
	private String pId = "";
	private String code = "";
	private String name = "";
	private String leader = "";
	private String startDate = " ";
	private String perComplete = "0";
	private String description = "";
	private String planFinishDate = " ";
	private String baseFinishDate = " ";
	private String actualFinishDate = " ";
	private String planEffort = "";
	private String baseEffort = "";
	private String actualEffort = "";
	private String type = "1";
	private String group = "";
	String status = "";
	String customer = "";
	public ProjectAddBean()
	{
	}
	public void setId(String str)
	{
		pId = str;
	}
	public String getId()
	{
		return pId;
	}
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
	public String getCode()
	{
		return code;
	}
	public void setCode(String str)
	{
		code = str;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String str)
	{
		name = str;
	}
	public String getLeader()
	{
		return leader;
	}
	public void setLeader(String str)
	{
		leader = str.toUpperCase();
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
	//////////////////////////////////////////////////////////
	// Group List
	public boolean setGroupList(StringMatrix List)
	{
		if (List != null)
			{
			this.GroupList = new StringMatrix(List.toString());
			return true;
		}
		return false;
	}
	public StringMatrix getGroupList()
	{
		return this.GroupList;
	}
	// Type List
	public boolean setTypeList(StringMatrix List)
	{
		if (List != null)
			{
			this.TypeList = new StringMatrix(List.toString());
			return true;
		}
		return false;
	}
	public StringMatrix getTypeList()
	{
		return this.TypeList;
	}
	// Leader List
	public boolean setLeaderList(StringMatrix List)
	{
		if (List != null)
			{
			this.LeaderList = new StringMatrix(List.toString());
			return true;
		}
		return false;
	}
	public StringMatrix getLeaderList()
	{
		return this.LeaderList;
	}
	public StringMatrix getStatusList()
	{
		return StatusList;
	}
	public void setStatusList(StringMatrix StatusList)
	{
		this.StatusList = StatusList;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public StringMatrix getCateList()
	{
		return CateList;
	}
	public void setCateList(StringMatrix CateList)
	{
		this.CateList = CateList;
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
}