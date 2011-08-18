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

public class ProjectListBean
{
	private String strDoViewProjectDetail;
	// For display
	private StringMatrix ProjectList = null;
	private StringMatrix GroupList = null;
	private StringMatrix TypeList = null;
	private StringMatrix StatusList = null;
	private StringMatrix CateList = null;
	// For select
	private String[] SelectedProjectList = null;
	private String group;
	private String type;
	private String status;
	private String cate;
	public void setGroup(String str)
	{
		this.group = str;
	}
	public String getGroup()
	{
		return this.group;
	}
	public void setType(String str)
	{
		this.type = str;
	}
	public String getType()
	{
		return this.type;
	}
	// Project List
	public StringMatrix getProjectList()
	{
		return this.ProjectList;
	}
	public boolean setProjectList(StringMatrix List)
	{
		if (List != null)
			{
			this.ProjectList = new StringMatrix(List.toString());
			return true;
		}
		return false;
	}
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
	///////////////////////////////////////////////////////////////////
	// Desription :
	public String[] getSelectedProjectList()
	{
		return SelectedProjectList;
	}
	public void setSelectedProjectList(String[] str)
	{
		if (str != null)
			this.SelectedProjectList = str;
	}
	///////////////////////////////////////////////////////////////////
	// Default from start
	public String getDoViewProjectDetail()
	{
		return strDoViewProjectDetail;
	}
	public void setDoViewProjectDetail(String str)
	{
		strDoViewProjectDetail = str;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public StringMatrix getStatusList()
	{
		return StatusList;
	}
	public void setStatusList(StringMatrix StatusList)
	{
		this.StatusList = StatusList;
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
}