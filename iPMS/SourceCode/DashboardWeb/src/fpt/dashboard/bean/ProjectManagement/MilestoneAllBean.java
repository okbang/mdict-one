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

public class MilestoneAllBean
{
    private String strProjectID = null;
    private String strProjectCode = null;
    private String strDateFrom = null;
    private String strDateTo = null;
    private String strStatus = "0"; // incompleted milestones will be showed by default
    private StringMatrix MilestoneList = null; //main table
    ////////////////////////////////////////////////////////////////////////
    //SET-GET METHODS
    public String getStatus()
    {
        return strStatus;
    }
    public void setStatus(String data)
    {
        if (data != null)
            strStatus = data;
    }
    public String getDateFrom()
    {
        return strDateFrom;
    }
    public void setDateFrom(String data)
    {
        if (data != null)
            strDateFrom = data;
    }
    public String getDateTo()
    {
        return strDateTo;
    }
    public void setDateTo(String data)
    {
        if (data != null)
            strDateTo = data;
    }
    public StringMatrix getMilestoneList()
    {
        return MilestoneList;
    }
    public void setMilestoneList(StringMatrix list)
    {
        this.MilestoneList = list;
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
}