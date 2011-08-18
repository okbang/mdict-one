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
public final class ProjectPointInfo {
	public long milestoneID = 0;
	public long prjID = 0;
	public String stageName = null;
	public double ProjectSize = 0;
	public double EffortEff = 0;
	public double CusSatisfaction = 0;
	public double AcceptanceOfDeliverable = 0;
	public double ReqCompleteness = 0;
	public double Timeliness = 0;
	public double Leakage = 0;
	public double CorrectionCost = 0;
	public double ProjectNC = 0;
	public double WOPoint = 0;
	public double ANPoint = 0;
	public double PPPoint = 0;
	public double PRPoint = 0;
	public double PrestigePoint = 0;
	public double CusPoint = 0;
	public double ProjectPoint = 0;
	public String ProjectEval = null;
	public double EffortEfficiency = 0;
	public double OverdueNCsObsCount = 0;
	public double OverdueNCsObsPoint = 0;
	
	public ProjectPointInfo() {
		ProjectSize  = 5;
		CusSatisfaction = 1;
		AcceptanceOfDeliverable = 1;
		ReqCompleteness = 1;
		Timeliness  = 1;
		Leakage  = 1;
		CorrectionCost = 1;
		ProjectNC = 1;
		WOPoint = 1;
		ANPoint = 1;
		PPPoint = 1;
		PRPoint = 1;
		PrestigePoint = 0;
		CusPoint = 0;
		ProjectPoint = 12;
		ProjectEval = "Good";
		stageName = "Initiation";
		milestoneID = 1;
		EffortEfficiency = 0;
		OverdueNCsObsCount = 0;
		OverdueNCsObsPoint = 0;
	}
	 
}