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
public class AssignmentAddBean
{
	StringMatrix DevList = null;
    StringMatrix m_GroupList = null;
    String strSelectedGroup = fpt.dashboard.constant.DATA.GROUP_ALL;
	String Type = null;
	String From = "";
	String To = "";
	String Desc = "";
	String Devname = "";
	String selectDevID = "";
	String strUsage = "";
	String strMessage = "";
    ArrayList m_ResponseList;
	//ThaiLH
	public void setMessage(String message)
	{
		strMessage = message;
	}
	public String getMessage()
	{
		return strMessage;
	}
	//EndOfThaiLH
	public StringMatrix getDevList()
	{
		return DevList;
	}
	public void setDevList(StringMatrix DevList)
	{
		this.DevList = DevList;
	}
	public String getSelectDevID()
	{
		return selectDevID;
	}
	public void setSelectDevID(String selectDevID)
	{
		this.selectDevID = selectDevID;
	}
	public String getDesc()
	{
		return Desc;
	}
	public void setDesc(String Desc)
	{
		this.Desc = Desc;
	}
	public String getFrom()
	{
		return From;
	}
	public void setFrom(String From)
	{
		this.From = From;
	}
	public String getTo()
	{
		return To;
	}
	public void setTo(String To)
	{
		this.To = To;
	}
	public String getDevname()
	{
		return Devname;
	}
	public void setDevname(String Devname)
	{
		this.Devname = Devname;
	}
	public String getType()
	{
		return Type;
	}
	public void setType(String Type)
	{
		this.Type = Type;
	}
	public String getUsage()
	{
		return strUsage;
	}
	public void setUsage(String usage)
	{
		this.strUsage = usage;
	}
	/////////////////////////////////////////////////////////
	
	private String strProjectID = "";
	private String ProjectFinish = "";
	private String ProjectStart = "";
	
	public String getProjectEnd()
	{
		return ProjectFinish;
	}
	public void setProjectEnd(String ProjectFinish)
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
    /**
     * @return
     */
    public StringMatrix getGroupList() {
        return m_GroupList;
    }

    /**
     * @param matrix
     */
    public void setGroupList(StringMatrix matrix) {
        m_GroupList = matrix;
    }

    /**
     * @return
     */
    public String getSelectedGroup() {
        return strSelectedGroup;
    }

    /**
     * @param string
     */
    public void setSelectedGroup(String string) {
        strSelectedGroup = string;
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