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
import java.util.Vector;

public class TailoringInfo {
	public int projectID;
	public int process_tailID;
	public String projectCode;
	public java.sql.Date start_date;
	public String modification;
	public int action;
	public String reason;
	final int type=1;
	public String category;
	public String note;

	public TailoringInfo() {
		projectID = 0;
		process_tailID=0;
		projectCode = "";
		modification = "";
		action = 0;
		reason = "";
		category = "";
		note = "";
	}
	public TailoringInfo(int process_tailID,
		int prjID,
		String prjCode,
		String mod,
		int act,
		String reason,
		String category,
		String note) {
	    this.process_tailID=process_tailID;
		this.projectID = prjID;
		this.projectCode = prjCode;
		this.modification = mod;
		this.action = act;
		this.reason = reason;
		this.category = category;
		this.note = note;
	}
	public static final Vector getProcessList() {
		Vector retval=new Vector();
		ProcessInfo info = new ProcessInfo();
        info.processId = ProcessInfo.GENERAL;
        info.name ="General";
        info.metricConstant=ProcessInfo.getMetric(info.processId);
        retval.add(info);
        info = new ProcessInfo();
		info.processId = ProcessInfo.FSOFT_SLC;
		info.name ="FSOFT SLC";
		info.metricConstant=ProcessInfo.getMetric(info.processId);
		retval.add(info);
		retval.addAll(ProcessInfo.processList);
		return retval;
	}
}