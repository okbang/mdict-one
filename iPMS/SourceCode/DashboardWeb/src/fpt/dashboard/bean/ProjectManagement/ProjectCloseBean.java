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

public class ProjectCloseBean
{
	private StringMatrix StatusList = null;
	//////////////////////////////////////////
	private String strProjectID = "";
	private String status = "";
	private String actualFinishdate = "";
	private String Desc = "";
	
	
	public void setProjectID(String id)
	{
		strProjectID = id;
	}
	public String getProjectID()
	{
		return strProjectID;
	}
	public String getActualFinishDate()
	{
		return actualFinishdate;
	}
	public String getDescription()
	{
		return Desc;
	}
	public String getStatus()
	{
		return status;
	}
	public StringMatrix getStatusList()
	{
		return StatusList;
	}
	public void setActualFinishDate(String actualFinishdate)
	{
		this.actualFinishdate = actualFinishdate;
	}
	public void setDescription(String Desc)
	{
		this.Desc = Desc;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public void setStatusList(StringMatrix StatusList)
	{
		this.StatusList = StatusList;
	}
}