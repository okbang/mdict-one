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
public final class ModuleInfo {
	public static final int STATUS_PENDING=1;
	public static final int STATUS_ACCEPTED=2;
	public static final int STATUS_REJECTED=3;
	public static final int STATUS_CANCELLED=4;
	public static final int STATUS_OTHER=5;
	public static final int STATUS_PROGRESS=6;
    public static final int SIZE_TYPE_ESTIM_METHOD=1;
    public static final int SIZE_TYPE_LANGUAGE=0;
	public static final String [] statusNames={"","Pending","Accepted","Rejected","Cancelled","Other","In progress"};
		public long moduleID = 0;
	public String name = "N/A";
	public String wpName = "N/A";
	public String conductor = "N/A";
	public String reviewer = "N/A";
	public String approver = "N/A";
	public String language = "N/A";
	public String stage = "N/A";
	public double deviation = Double.NaN;
	public String isReview = "N/A";
	public String isTest = "N/A";
	public String isRelease = "N/A";
	public boolean isDel = false;
	public boolean isNormal = true;
	public int projectID;
	public int wpID;
	public int plannedDefect;
	public int rePlannedDefect;
	public Date plannedReviewDate;
	public Date plannedTestEndDate;
	public Date plannedReleaseDate;
	public Date rePlannedReleaseDate;
	public Date thePlanReleaseDate;
	public Date actualReviewDate;
	public Date actualReleaseDate;
	public Date actualTestEndDate;
	public double actualSize;
	public int sizeType;
	public int isDeliverable;
	public String deliveryLocation;
	public String note;
	public int status;
	public int baselineStatus;
	public String baselineNote;
	public String baseline;
	public double actualEffort;//for review
	public final String getStatusName(){
		try{
			return statusNames[status];
		}
		catch(Exception e){
			return "";
		}
	}
}
