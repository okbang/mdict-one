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
 
 package com.fms1.infoclass;
import java.sql.Date;
public final class IssueInfo {
	public int issueID;
	public long workUnitID;
	public String projectCode;
	public Date start_date = null;
	public String description;
	public int statusID;
	public int priorityID;
	public int typeID;
	public String owner;
	public Date startDate;
	public Date dueDate;
	public String comment;
	public Date closeDate;
	public String reference;
	public String statusName;
	public String priorityName;
	public String typeName;
	public String creator; // create variable
	public String ownerName;
	public long riskID;
	public int processId;
	public int vectorID;//system use
	//for PQA
	public long wuID;//reference wu
	public long parentwuID;//reference wu
	public String wuName;
	
	public static String [] statusFlds={"Open","Cancel","Closed","In progress"};
	public static String [] priorityFlds={"Low","Medium","High","Urgent"};
	public static String [] typeFlds={"Organization","Customer","Resource","Operation","Others"};
	public IssueInfo() {
		workUnitID = 0;
		projectCode = "";
		start_date = null;
		description = "";
		statusID = 0;
		priorityID = 0;
		typeID = 0;
		owner = "";
		startDate = null;
		dueDate = null;
		comment = "";
		closeDate = null;
		reference = "";
		statusName = "";
		priorityName = "";
		typeName = "";
		creator = "";
		processId = 0;
	}
	public final String getStatusName() {
		return statusFlds[statusID];
	}
	public final String getPriorityName() {
		return priorityFlds[priorityID];
	}
	public final String getTypeName() {
		return typeFlds[typeID];
	}

}
