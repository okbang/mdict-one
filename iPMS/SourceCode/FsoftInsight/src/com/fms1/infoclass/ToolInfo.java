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
import java.io.Serializable;
import java.sql.Date;
public final class ToolInfo implements Serializable {
	
	public long toolID;
	public long prjID;
	public String name;
	public String purpose;
	public String source;
	public String description;
	public String status;
	public Date dueD;
	public Date actualD;
	public String note;
	public long tool_type;
	public String expected_available_stage;
	public String actual_available_stage;
	
	public ToolInfo()
	{
		toolID = 0;
		prjID = 0;
		name = "";
		purpose = "";
		source = "";
		description = "";
		status = "";
		dueD = null;
		actualD = null;
		note = "";
		tool_type = 0;
		expected_available_stage = "";
		actual_available_stage = "";
	}
	
	public ToolInfo(
		long tID,
		long pID,
		String n,
		String pur,
		String sour,
		String desc,
		String sta,
		Date dD,
		Date aD,
		String nte,
		long t,
		String _expected_available_stage,
		String _actual_available_stage)
	{
		toolID = tID;
		prjID = pID;
		name = n;
		purpose = pur;
		source = sour;
		description = desc;
		status = sta;
		dueD = dD;
		actualD = aD;
		note = nte;
		tool_type = t;
		expected_available_stage = _expected_available_stage;
		actual_available_stage = _actual_available_stage;
	}
	
	public ToolInfo(
		long tID,
		long pID,
		String n,
		String pur,
		String sour,
		String desc,
		String sta,
		Date dD,
		Date aD,
		String nte,
		long t)
	{
		toolID = tID;
		prjID = pID;
		name = n;
		purpose = pur;
		source = sour;
		description = desc;
		status = sta;
		dueD = dD;
		actualD = aD;
		note = nte;
		tool_type = t;
		
	}
}