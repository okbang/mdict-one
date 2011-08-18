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

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import fpt.dashboard.constant.DATA;

public class ProjectDetailBean
{
	//?
	private String strDoViewProjectDetail;
	// Chart
	private Collection requirementChartDataList = null;
	private Collection defectChartDataList = null;
	// Sub list
	private Collection MilestoneList = null;
	private Collection AssignmentList = null;
	private Collection IssueList = null;
	////
	private String id = "";
	private String code = "";
	private String name = "";
	private String leader = "";
	private String planStartDate = "";
	private String startDate = "";
	private String perComplete = "";
	private String description = "";
	private String scheduleStatus = "";
	private String effortStatus = "";
	private String planFinishDate = "";
	private String baseFinishDate = "";
	private String actualFinishDate = "";
	private String planEffort = "0";
	private String baseEffort = "0";
	private String actualEffort = "0";
	private String totalRequirement = "0";
	private String totalDefect = "0";
	private String totalWeightedDefect = "0";
	private String type = "";
	private String group = "";
	private String status = "";
	private String datafile = "";
	private String lastUpdate = "";
	private String Customer = "";
	private String cate = "";
	public ProjectDetailBean()
	{
	}
	///////////////////////////////////////////////////////////////////
	// Default from start for tranfer ProjectId
	public String getDoViewProjectDetail()
	{
		return strDoViewProjectDetail;
	}
	public void setDoViewProjectDetail(String str)
	{
		strDoViewProjectDetail = str;
	}
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
	public String getScheduleStatus()
	{
		String retVal = "NA";
        try {
            if ((scheduleStatus != null) && (! "".equals(scheduleStatus))) {
                float tmp = Float.parseFloat(scheduleStatus);
                float intCom = 0;
                if (! "".equals(perComplete)) {
                    intCom = Float.parseFloat(perComplete);
                }
                if (intCom > 0) {
                    retVal = String.valueOf(tmp * 100);
                    retVal = retVal.substring(0, retVal.indexOf(".") + 2) + "%";
                }
            }
        }
        catch (NumberFormatException e) {
        }
		return retVal;
	}
	public String getMajorScheduleStatus()
	{
		String txt = "Green"; // default value
		if (baseFinishDate.trim().length() == 0)
			return txt;
		int num1 = getBaseDuration();
		int num2 = this.getActualDuration();
		int num = 100 * (num1 - num2) / num1;
		if (num > -5)
			txt = "Green";
		if (num < -10)
			txt = "Red";
		if ((num > -10) && (num < -5))
			txt = "Yellow";
		return txt;
	}
	public int getPlanDuration()
	{
		if (planFinishDate.trim().length() > 0)
			if (planStartDate.trim().length() > 0){
				return dateDiff(planStartDate, planFinishDate);
			}
			else {
				return dateDiff(startDate, planFinishDate);
			}
		else
			return 0;
	}
	public int getBaseDuration()
	{
		if (baseFinishDate.trim().length() > 0)
			// return dateDiff(startDate, baseFinishDate);
			if (planStartDate.trim().length() > 0){
				return dateDiff(planStartDate, baseFinishDate);
			}
			else {
				return dateDiff(startDate, baseFinishDate);
			}
			
		else
			return 0;
	}
	public int getActualDuration()
	{
		int num = 0;
		if (this.startDate.length() < 5)
			return 0;
		Date sd = this.toDate(this.startDate);
		Date nd = new Date();
		if (actualFinishDate.trim().length() > 0)
			{
			Date ad = this.toDate(actualFinishDate);
			if (ad.before(nd))
				{
				num = this.dateDiff(sd, ad);
			}
			else
				{
				num = this.dateDiff(sd, nd);
			}
		}
		else
			{
			num = this.dateDiff(sd, nd);
		}
		return num;
	}
	public String getScheduleVariance()
	{
		int num1 = getBaseDuration();
		int num2 = getActualDuration();
		int num3 = getPlanDuration();
		if (num3 > num2)
			num2 = num3;
		if (num1 == 0)
			return "NA";
		if (num2 == 0)
			return "NA";
		int num = 100 * (num2 - num1) / num1;
		return Integer.toString(num) + " %";
	}
	public String getEffortVariance()
	{
		double num1 = 0;
		double num2 = 0;
		try
			{
			num1 = Double.parseDouble(getBaseEffort());
			num2 = Double.parseDouble(getActualEffort());
		}
		catch (Exception ex)
			{
			ex.toString();
			num1 = 0;
			num2 = 0;
		}
		if (num1 == 0)
			return "NA";
		if (num2 == 0)
			return "NA";
		double num = 100 * (num2 - num1) / num1;
		String txt = Double.toString(num);
		int pos = txt.indexOf(".");
		if ((pos > 0) && (txt.length() > (pos + 3)))
			txt = txt.substring(0, pos + 3);
		txt += " %";
		if (actualFinishDate.trim().length() > 0)
			return txt;
		if (num2 > num1)
			return txt;
		return "";
	}
	public String getId()
	{
		return id;
	}
	public void setType(String str)
	{
		type = str;
	}
	public String getType()
	{
		String tmp = "";
		if (type.equals("0"))
			{
			tmp = DATA.PROJECT_TYPE_EXTERNAL;
		}
		else if (type.equals("8"))
			{
			tmp = DATA.PROJECT_TYPE_INTERNAL;
		}
		else if (type.equals("9"))
			{
			tmp = DATA.PROJECT_TYPE_PUBLIC;
		}
		return tmp;
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
	public void setId(String i)
	{
		id = i;
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
	public String getPlanStart(){
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
		return planFinishDate;
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
			Double.parseDouble(strDbl);
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
			Double.parseDouble(strDbl);
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
			Double.parseDouble(strDbl);
		}
		catch (Exception ex)
			{
			actualEffort = "";
		}
	}
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
	private Date toDate(String s)
	{
		s = s.trim();
		int day;
		int month;
		int year;
		int pos;
		String wrd = "/";
		pos = s.indexOf(wrd);
		day = Integer.parseInt(s.substring(0, pos));
		s = s.substring((pos + 1), s.length()).trim();
		pos = s.indexOf(wrd);
		month = Integer.parseInt(s.substring(0, pos));
		s = s.substring((pos + 1), s.length()).trim();
		year = Integer.parseInt(s);
		if (year < 1000)
			year = year + 2000;
		Date dd = new Date(year - 1900, month - 1, day);
		return dd;
	}
	private int dateDiff(Date d1, Date d2)
	{
		long dd = (d2.getTime() - d1.getTime()) / (60 * 60 * 24 * 1000);
		return (int) dd;
	}
	private int dateDiff(String s1, String s2)
	{
		Date dd1 = this.toDate(s1);
		Date dd2 = this.toDate(s2);
		long vd = (dd2.getTime() - dd1.getTime()) / (60 * 60 * 24 * 1000);
		return (int) vd;
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
	// for query in javascript
	public String getDefectChartDataQueryList()
	{
		String strDefectChartDataList = "";
		Collection cList = this.getDefectChartData();
		if (cList == null)
			{
			return "";
		}
		Iterator itC = cList.iterator();
		char tt = 2;
		String mLine;
		String strTemp = "";
		StringTokenizer stkLine;
		String strDiff = String.valueOf(tt);
		String cValue = "";
		while (itC.hasNext())
			{
			mLine = (String) itC.next().toString();
			stkLine = new StringTokenizer(mLine, strDiff);
			if (stkLine.hasMoreElements())
				{
				strTemp = (String) stkLine.nextElement();
			}
			if (stkLine.hasMoreElements())
				{
				strTemp = (String) stkLine.nextElement();
				if (strTemp != null)
					if (strTemp.length() > 0)
						cValue = strTemp;
			}
			strDefectChartDataList += "dV=" + cValue + "&";
		}
		return strDefectChartDataList;
	}
	public String getRequirementChartDataQueryList()
	{
		String strDefectChartDataList = "";
		Collection cList = this.getRequirementChartData();
		if (cList == null)
			{
			return "";
		}
		Iterator itC = cList.iterator();
		char tt = 2;
		String mLine;
		String strTemp = "";
		StringTokenizer stkLine;
		String strDiff = String.valueOf(tt);
		String cValue = "";
		while (itC.hasNext())
			{
			mLine = (String) itC.next().toString();
			stkLine = new StringTokenizer(mLine, strDiff);
			if (stkLine.hasMoreElements())
				{
				strTemp = (String) stkLine.nextElement();
			}
			if (stkLine.hasMoreElements())
				{
				strTemp = (String) stkLine.nextElement();
				if (strTemp != null)
					if (strTemp.length() > 0)
						cValue = strTemp;
			}
			strDefectChartDataList += "rV=" + cValue + "&";
		}
		return strDefectChartDataList;
	}
	public void setRequirementChartData(Collection cl)
	{
		this.requirementChartDataList = cl;
	}
	public void setDefectChartData(Collection cl)
	{
		this.defectChartDataList = cl;
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
		String tmp="";
		//modified ny MinhPT 04-Nov-03
		//for avoid null pointer
		if (DATA.PROJECT_VALUE_STATUS_ONGOING.equals(status))
		{
			tmp = DATA.PROJECT_STATUS_ONGOING;
		}
		else if (DATA.PROJECT_VALUE_STATUS_CLOSED.equals(status))
		{
			tmp = DATA.PROJECT_STATUS_CLOSED;
		}
        else if (DATA.PROJECT_VALUE_STATUS_CANCELLED.equals(status)) {
            tmp = DATA.PROJECT_STATUS_CANCELLED;
        }
		else
		{
			tmp = DATA.PROJECT_STATUS_TENTATIVE;
		}
		return tmp;
	}
	public void setStatus(String str)
	{
		this.status = str;
	}
	
    public String getEffortStatus()
	{
		String retVal = "NA";
        try {
            if ((effortStatus != null) && (! "".equals(effortStatus))) {
                float tmp = Float.parseFloat(effortStatus);
                float intCom = 0;
            //modified ny MinhPT 04-Nov-03
            //for avoid null pointer
                if (!"".equals(perComplete)) {
                    intCom = Float.parseFloat(perComplete);
                }
                if (intCom > 0) {
                    retVal = String.valueOf(tmp * 100);
                    retVal = retVal.substring(0, retVal.indexOf(".") + 2) + "%";
                }
            }
        }
        catch (NumberFormatException e) {
        }
		return retVal;
	}
    
	public void setEffortStatus(String effortStatus)
	{
		this.effortStatus = effortStatus;
	}
	public String getLastUpdate()
	{
		if (lastUpdate.equals("") || (lastUpdate == null))
			{
			return "N/A";
		}
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}
	public String getCustomer()
	{
		return Customer;
	}
	public void setCustomer(String Customer)
	{
		this.Customer = Customer;
	}
	public String getCate()
	{
		String tmp = "";
		//modified by MinhPT 04-Nov-03 
		if (DATA.PROJECT_VALUE_CATEGORY_MAINTENANCE.equals(cate)) //DATA.PROJECT_CATEGORY_MAINTENANCE ="1"
		{
			tmp = DATA.PROJECT_CATEGORY_MAINTENANCE;
		}
		else if (DATA.PROJECT_VALUE_CATEGORY_DEVELOPMENT.equals(cate))//DATA.PROJECT_CATEGORY_DEVELOPMENT = "0"
		{
			tmp = DATA.PROJECT_CATEGORY_DEVELOPMENT;
		}
		else if (DATA.PROJECT_VALUE_CATEGORY_OTHERS.equals(cate))//DATA.PROJECT_CATEGORY_OTHERS = "2"
		{
			tmp = DATA.PROJECT_CATEGORY_OTHERS;
		}
		return tmp;
	}
	public void setCate(String cate)
	{
		this.cate = cate;
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