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

public final class SkillInfo {
	public long projectSkillId;
	public long assignmentId;
	public String fullName;
	public String account;
	public String projectRole;
	public long processId;
	public String process;
	public String skill;
	public int point;
	public String skillComment;

	public SkillInfo() {
		projectSkillId = 0;
		assignmentId = 0;
		fullName = "";
		projectRole = "";
		processId = 0;
		process = "";
		skill = "";
		point = 0;
		skillComment = "";
		account = "";
	}
	
	public SkillInfo(
		long psId,
		long assId,
		String name,
		String account,
		String projectR,
		long pID,
		String pro,
		String sk,
		int p,
		String comm) 
	{
		projectSkillId = psId;
		assignmentId = assId;
		fullName = name;
		account = account;
		projectRole = projectR;
		processId = pID;
		process = pro;
		skill = sk;
		point = p;
		skillComment = comm;
	}
}