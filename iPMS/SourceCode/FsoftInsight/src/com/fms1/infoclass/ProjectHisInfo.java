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
import com.fms1.web.Constants;

public final class ProjectHisInfo {
	
	public long projectHisId;
	public long projectId;
	public java.sql.Date eventDate;
	public String events;
	public String comments;
	public String link;
		
	private static String root="Fms1Servlet?reqType=";
	public static final String MILESTONELINK =root+Constants.SCHE_STAGE_GET_LIST;
	public static final String DELIVERYLINK =root+Constants.WO_DELIVERABLE_GET_LIST;
	public static final String HISLINK = root+Constants.UPDATE_HIS;
	public static final String NOLINK ="nolink";
	
	public ProjectHisInfo() {
		projectHisId = 0;
		projectId = 0;
		eventDate = null;
		events = "";
		comments = "";
		link = "";
	}
	
	public ProjectHisInfo(
		long prHisId,
		long prId,
		java.sql.Date eD,
		String ev,
		String cm,
		String l) 
	{
		projectHisId = prHisId;
		projectId = prId;
		eventDate = eD;
		events = ev;
		comments = cm;
		link = l;
	}
}
