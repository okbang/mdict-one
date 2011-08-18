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
 
 package com.fms1.infoclass.group;


import java.sql.Date;

/**
 * 
 * @author Nguyen Van Hieu
 *@version 1.0 20/Oct/2007
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HumanResourceInfo{
	private String 	projectGroup;	// name of group which has this project
	private String 	userGroup;		// Name of group which user belong to.
	private String 	projectStatusName;	// status of Project: 0:On-going; 1:Closed; 2: Cancelled; 3: Tentative
	private String 	projectCode;	// Code of Project
	private long 	projectId;
	private String 	userAccount;	// Account of User
	private double 	workingTime;	// percentage of time which user assigned to project
	private Date 	beginAssignment;// day which user begin assigning to Project
	private Date 	endAssignment;	// day which user end assigning to Project
	private String 	userRole;		// Role of user
	private double	userCalendarEffort; // Calendar effort of this user;
	private int		userBy;
	private int		status;			// project status in Integer type
	private int		projectType;
	private String	fromDate = null;
	private String	toDate = null;
	private String  fileName;

	public HumanResourceInfo(){
		this.projectGroup = "";
		this.userGroup = "";
		this.projectCode = "";
		this.projectStatusName = "";
		this.userAccount = "";
	}
	/**
	 * set value for Project group
	 * @param strProjectGroup
	 */
	public void setProjectGroup(String strProjectGroup){
		if (strProjectGroup == null){
			this.projectGroup = "";
		}
		else {
			this.projectGroup = strProjectGroup.trim();
		}
	}
	/**
	 * get information of Project group
	 * @return String
	 */
	public String getProjectGroup(){
		return this.projectGroup;
	}
	/**
	 * set value for User Group
	 * @param strUserGroup
	 */
	public void setUserGroup(String strUserGroup){
		if (strUserGroup == null) {
			this.userGroup = "";
		}
		else {
			this.userGroup = strUserGroup.trim();
		}
	}
	public String getUserGroup(){
		return this.userGroup;
	}
	
	public void setProjectStatusName(int iStatus){
		try{
			switch (iStatus){
				case 0:
					this.projectStatusName = "On-going";
					break;
				case 1:
					this.projectStatusName = "Closed";
					break;
				case 2:
					this.projectStatusName = "Cancelled";
					break;
				case 3:
					this.projectStatusName = "Tentative";
					break;
				case 4:
					this.projectStatusName = "";
					break;
				default:
					this.projectStatusName = "N/A";
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public String getProjectStatusName(){
		return this.projectStatusName;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	public int getStatus(){
		return this.status;
	}
	public void setProjectType(int  projectType){
		this.projectType = projectType;
	}
	public int getProjectType(){
		return this.projectType;
	}
	public void setProjectCode(String strProjectCode){
		if (strProjectCode == null){
			this.projectCode = "N/A";
		}
		else{
			this.projectCode = strProjectCode.trim();
		}
	}
	public String getProjectCode(){
		return this.projectCode;
	}
	
	public void setUserAccount(String strAccount){
		if (strAccount == null){
			this.userAccount = "";
		}
		else {
			this.userAccount = strAccount.trim();
		}
	}
	public String getUserAccount(){
		return this.userAccount;
	}
	
	public void setWorkingTime(double lWorkingTime){
		this.workingTime = lWorkingTime;		
	}
	public double getWorkingTime(){
		return this.workingTime;
	}
	
	public void setBeginAssingment(Date beginDate){
		this.beginAssignment =  beginDate;
	}
	public Date getBeginAssignment(){
		return this.beginAssignment;
	}

	public void setEndAssingment(Date endDate){
		this.endAssignment =  endDate;
	}
	public Date getEndAssignment(){
		return this.endAssignment;
	}
	public void setUserRole(int role){
		switch (role){
			case 0:
				this.userRole = "Developer";
				break;
			case 1:
				this.userRole = "Tester";
				break;
			case 2:
				this.userRole = "Project Technical Leader";
				break;
			case 3:
				this.userRole = "Project Manage";
				break;
			case 4:
				this.userRole = "Graphic Designe";
				break;
			case 5:
				this.userRole = "External";
				break;
			case 6:
				this.userRole = "Onsite Coordinator";
				break;
			case 7:
				this.userRole = "SQA";
				break;
			case 8:
				this.userRole = "PQA";
				break;
			case 9:
				this.userRole = "Communicator";
				break;
			default: 
				this.userRole = "N/A";
		}
	}
	public void setUserRole(String strRole){
		if (strRole == null){
			this.userRole = "N/A";
		}
		else {
			this.userRole = strRole.trim();
		}
	}
	public String getUserRole(){
		return this.userRole;
	}
	public void setUserCalendarEffort(double lCalendarEffort){
		this.userCalendarEffort = lCalendarEffort;
	}
	public double getUserCalendarEffort(){
		return this.userCalendarEffort;
	}
	public void setUserBy(int userBy){
		this.userBy = userBy;
	}
	public int getUserBy(){
		return this.userBy;
	}
	public void setProjectId(long projectId){
		this.projectId = projectId;
	}
	public long getProjectId(){
		return this.projectId;
	}
	public void setFromDate(String fromDate){
		this.fromDate =  fromDate;
	}
	public String getFromDate(){
		return this.fromDate;
	}
	public void setToDate(String toDate){
		this.toDate =  toDate;
	}
	public String getToDate(){
		return this.toDate;
	}
	public void setFileName(String fileName){
		if (fileName == null){
			this.fileName = "";
		}
		else {
			this.fileName = fileName.trim();
		}
	}
	public String getFileName(){
		return this.fileName;
	}
}