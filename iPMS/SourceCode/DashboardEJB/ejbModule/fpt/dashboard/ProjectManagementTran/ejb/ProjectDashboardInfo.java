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

import java.util.*;

public class ProjectDashboardInfo implements java.io.Serializable
{
	// Exposed methods
	private int ProjectID;
	private String Name;
	private String Code;
	private int per_complete;	
	private String dt_start;
	private String dt_base_finish;
	private String dt_plan_finish;
	private String dt_actual_finish;
	private int int_base_effort;
	private int int_plan_effort;
	private int int_actual_effort;
	private float schedule_status;
	private float effort_status;
    
	private double sOrder;
	private double eOrder;
	
	public ProjectDashboardInfo()
	{

	}
    public String getID()
    {
        return "" + ProjectID;
    }
    public int getProjectID()
    {
        return ProjectID;
    }
	public void setID(int nID)
	{
		ProjectID = nID;
	}
	
	public String getName()
	{
		return Name;
	}
	public void setName(String strData)
	{
		Name = strData;	
	}
	
	public String getCode()
	{
		return Code;
	}
	public void setCode(String strData)
	{
		Code = strData;
	}
		
	public int getPer_complete()
	{
		return per_complete;
	}
	public void setPer_complete(int nPer)
	{
		per_complete = nPer;
	}
	
	public String getStart_date()
	{
		return dt_start;
	}
	public void setStart_date(String strData)
	{
		dt_start = strData;
	}
	
	public String getBase_finish()
	{
		return dt_base_finish;
	}
	public void setBase_finish(String strData)
	{
		dt_base_finish = strData;
	}
	
	public String getPlan_finish()
	{
		return dt_plan_finish;
	}
	public void setPlan_finish(String strData)
	{
		dt_plan_finish = strData;
	}
	
	public String getActual_finish()
	{
		return dt_actual_finish;
	}
	public void setActual_finish(String strData)
	{
		dt_actual_finish = strData;
	}
	
	public String getBase_effort()
	{
		return "" + int_base_effort;
	}
	public void setBase_effort(int nBase)
	{
		int_base_effort = nBase;
	}
	
	public String getPlan_effort()
	{
		return "" + int_plan_effort;
	}
	public void setPlan_effort(int nPlan)
	{
		int_plan_effort = nPlan;
	}
	
	public String getActual_effort()
	{
		return "" + int_actual_effort;
	}
	public void setActual_effort(int nActual)
	{
		int_actual_effort = nActual;
	}
	
	public float getSchedule_status()
	{
		return schedule_status;
	}
	public void setSchedule_status(float nStatus)
	{
		schedule_status = nStatus;
	}
	
	public float getEffort_status()
	{
		return effort_status;
	}
	public void setEffort_status(float fStatus)
	{
		effort_status = fStatus;
	}
	
	public double getByOrder()
	{
		return this.sOrder;
	}
	public void setByOrder(double order)
	{
		this.sOrder  = order;
	}
	
	public double getOrDate()
	{
		return this.eOrder;
	}
	public void setOrDate(double order)
	{
		this.eOrder  = order;
	}
}

