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
public final class TailoringDeviationInfo  {
	public long tailoringID;
	public long process_tail_ID;
	public int projectID;
	public String projectCode;
	public java.sql.Date start_date;
	public String modification;
	public int action;
	public String reason;
	public int type;
	public String category;
	public String note;
	// Add by HaiMM
	public String groupName;
   public TailoringDeviationInfo() {
		tailoringID = 0;
		projectID = 0;
		process_tail_ID=0;
		projectCode = "";
		modification = "";
		action = 0;
		reason = "";
		type = 0;
		category = "";
		note = "";
	}
	public TailoringDeviationInfo(long process_tail_ID,
		long tailoringID,
		int prjID,
		String prjCode,
		String mod,
		int act,
		String reason,
		int type,
		String category,
		String note) {
		this.process_tail_ID=process_tail_ID;
		this.tailoringID = tailoringID;
		this.projectID = prjID;
		this.projectCode = prjCode;
		this.modification = mod;
		this.action = act;
		this.reason = reason;
		this.type = type;
		this.category = category;
		this.note = note;        
	}
}
