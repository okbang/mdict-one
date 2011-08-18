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

import java.text.SimpleDateFormat;
import java.util.*;

import fpt.dashboard.constant.DB;

public class AssignmentInfo implements java.io.Serializable
{
	private int nProjectID = 0;
	private String strProjectName = "";
	private String strDevName = "";
	private String strBeginDate = "";
	private String strEndDate = "";
	private int nUsage = 0;
	
    private int n_DeveloperID;
    private String strDeveloperAccount;
    private int n_Type;
    private Calendar cal_BeginCalendar;
    private Calendar cal_EndCalendar;

	public AssignmentInfo()
	{
        cal_BeginCalendar = Calendar.getInstance();
        cal_EndCalendar = Calendar.getInstance();
	}
	public int getProjectID()
	{
		return this.nProjectID;
	}
	public void setProjectID(int data)
	{
		this.nProjectID = data;
	}
	
	public String getProjectName()
	{
		return this.strProjectName;
	}
	public void setProjectName(String data)
	{
		this.strProjectName = data;
	}
	
	public String getDeveloperName()
	{
		return this.strDevName;
	}
	public void setDeveloperName(String data)
	{
		this.strDevName = data;
	}
	
	public String getBeginDate()
	{
		return this.strBeginDate;
	}
	public void setBeginDate(String data)
	{
		this.strBeginDate = data;
	}
	
	public String getEndDate()
	{
		return this.strEndDate;
	}
	public void setEndDate(String data)
	{
		this.strEndDate = data;
	}
	
	public int getUsage()
	{
		return nUsage;
	}
	public void setUsage(int data)
	{
		nUsage = data;
	}
    /**
     * @return
     */
    public Calendar getBeginCalendar() {
        return cal_BeginCalendar;
    }

    /**
     * @return
     */
    public Calendar getEndCalendar() {
        return cal_EndCalendar;
    }

    /**
     * @return
     */
    public int getDeveloperID() {
        return n_DeveloperID;
    }

    /**
     * @return
     */
    public int getType() {
        return n_Type;
    }

    /**
     * @param calendar
     */
    public void setBeginCalendar(Calendar calendar) {
        cal_BeginCalendar = calendar;
    }

    /**
     * @param date
     */
    public void setBeginCalendar(java.sql.Date date) {
        if (date != null) {
            cal_BeginCalendar.setTime(date);
        }
    }

    /**
     * @param strDate
     */
    public void setBeginCalendar(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DB.DATE_FORMAT);
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                cal_BeginCalendar.setTime(formatter.parse(strDate));
            }
        }
        catch (Exception e) {
        }
    }

    /**
     * @param calendar
     */
    public void setEndCalendar(Calendar calendar) {
        cal_EndCalendar = calendar;
    }

    /**
     * @param date
     */
    public void setEndCalendar(Date date) {
        if (date != null) {
            cal_EndCalendar.setTime(date);
        }
    }

    /**
     * @param date
     */
    public void setEndCalendar(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DB.DATE_FORMAT);
        try {
            if ((strDate != null) && (strDate.length() > 0)) {
                cal_EndCalendar.setTime(formatter.parse(strDate));
            }
        }
        catch (Exception e) {
        }
    }

    /**
     * @param i
     */
    public void setDeveloperID(int i) {
        n_DeveloperID = i;
    }

    /**
     * @param i
     */
    public void setType(int i) {
        n_Type = i;
    }

    /**
     * @return
     */
    public String getDeveloperAccount() {
        return strDeveloperAccount;
    }

    /**
     * @param string
     */
    public void setDeveloperAccount(String string) {
        strDeveloperAccount = string;
    }

    /**
     * @return
     */
    public String getTooltip() {
        String strTooltip;
        String strType;
        String strUsage = "";
        SimpleDateFormat formatter = new SimpleDateFormat(DB.DATE_FORMAT);
        
        switch (n_Type) {
            case DB.ASSIGNMENT_TYPE_ONSITE:
                strType = "Onsite";
                break;
            case DB.ASSIGNMENT_TYPE_ALLOCATED:
                strType = "Allocated";
                break;
            case DB.ASSIGNMENT_TYPE_TENTATIVE:
                strType = "Tentative";
                break;
            case DB.ASSIGNMENT_TYPE_TRAINNING:
                strType = "Trainning";
                break;
            case DB.ASSIGNMENT_TYPE_OFF:
                strType = "Off";
                break;
            default:
                strType = "";
        }
        if (nUsage > 0) {
            strUsage = " Usage:" + nUsage + "%";
        }
        strTooltip = strDeveloperAccount + " - " + strType +
                " From " + formatter.format(cal_BeginCalendar.getTime()) +
                " To " + formatter.format(cal_EndCalendar.getTime()) +
                strUsage;
        return strTooltip;
    }
}
