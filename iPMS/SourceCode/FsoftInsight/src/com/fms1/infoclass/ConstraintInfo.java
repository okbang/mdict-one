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
public final class ConstraintInfo {
	public long constraintID;
	public String description;
	public String note; // landd add
	public int isConstraint;	 
	public int type;
	public long prjPlanID;
	public ConstraintInfo(
		long id,
		String descrp,
		String noteParam,
		int isCon,
		int tp,
		long prjID) {
		constraintID = id;
		description = descrp;
		note = noteParam;	// landd add
		isConstraint = isCon;
		type = tp;
		prjPlanID = prjID;
	}
	public ConstraintInfo() {
		constraintID = 0;
		description = "";
		note = "";	// landd add
		isConstraint = 0;
		type = 0;
		prjPlanID = 0;
	}
	public String GetNameOfType()
	{
		switch (type)
		{
			case 0:
				return "Business expertise";
			case 1:
				return "Customer problem";
			case 2:
				return "Customer relationship";
			case 3:
				return "Environment";
			case 4:
				return	"Estimation";
			case 5:
				return	"Geography";
			case 6:
				return	"Requirement";
			case 7:
				return	"Resource";
			case 8:
				return	"Schedule";
			case 9:
				return	"Technical";
			case 10:
				return	"Project management";
			case 11:
				return	"Quality management";
		}
		return "unknown";
	}
}