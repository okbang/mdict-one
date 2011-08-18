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

import fpt.dashboard.util.StringUtil.StringMatrix;

public class AssignmentListBean
{
	StringMatrix AssignList = null;
	String strLeader = "";
	String ProjectFinish = "";
	String ProjectStart = "";
	
	private String strProjectID = "";
	private String strProjectCode = "";
	private String ProjectName = "";
	String[] arrDelete = null;
	String[] arrBegin = null;
	String[] arrEnd = null;
	String[] arrType = null;
	String[] arrUsage = null;
	String[] arrRes = null;
    private ArrayList m_ResponseList = null;
    
	public StringMatrix getAssignList()
	{
		return AssignList;
	}
	public void setAssignList(StringMatrix AssignList)
	{
		this.AssignList = AssignList;
	}
	public void setStrLeader(String strLeader)
	{
		this.strLeader = strLeader;
	}
	public String getStrLeader()
	{
		return strLeader;
	}
	public String getProjectFinish()
	{
		return ProjectFinish;
	}
	public void setProjectFinish(String ProjectFinish)
	{
		this.ProjectFinish = ProjectFinish;
	}
	public void setProjectStart(String ProjectStart)
	{
		this.ProjectStart = ProjectStart;
	}
	public String getProjectStart()
	{
		return ProjectStart;
	}
	public void setProjectID(String position)
	{
		strProjectID = position;
	}
	public String getProjectID()
	{
		return strProjectID;
	}
	public void setProjectCode(String project)
	{
		strProjectCode = project;
	}
	public String getProjectCode()
	{
		return strProjectCode;
	}
	public String getProjectName()
	{
		return ProjectName;
	}
	public void setProjectName(String ProjectName)
	{
		this.ProjectName = ProjectName;
	}
	public String[] getArrDelete()
	{
		return arrDelete;
	}
	public void setArrDelete(String[] arrDelete)
	{
		this.arrDelete = arrDelete;
	}
	public String[] getArrBegin()
	{
		return arrBegin;
	}
	public void setArrBegin(String[] arrBegin)
	{
		this.arrBegin = arrBegin;
	}
	public void setArrEnd(String[] arrEnd)
	{
		this.arrEnd = arrEnd;
	}
	public String[] getArrEnd()
	{
		return arrEnd;
	}
	public String[] getArrType()
	{
		return arrType;
	}
	public void setArrType(String[] arrType)
	{
		this.arrType = arrType;
	}
	public String[] getArrUsage()
	{
		return arrUsage;
	}
	public void setArrUsage(String[] arrUsage)
	{
		this.arrUsage = arrUsage;
	}
	public String[] getArrRes()
	{
		return arrRes;
	}
	public void setArrRes(String[] arrRes)
	{
		this.arrRes = arrRes;
	}
    /**
     * @return
     */
    public ArrayList getResponseList() {
        return m_ResponseList;
    }

    /**
     * @param list
     */
    public void setResponseList(ArrayList list) {
        m_ResponseList = list;
    }

}