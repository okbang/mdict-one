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
public final class TrainingInfo {
	public long trainingID;
	public long prjID;
	public String description;
	public String duration;
	public String participant;
	public String waiver;
	public Date startD;
	public Date endD;
	public Date actualEndD;
	public String topic;
	public int verifyBy;
	public TrainingInfo() {
		trainingID = 0;
		prjID = 0;
		description = "";
		participant = "";
		duration = "";
		waiver = "";
		startD = null;
		endD = null;
		actualEndD = null;
		topic = "";
	}
	/**
	 * Mapping between table values (integer)
	 * and real values (String) for Verify by field
	 */
	public String getVerifyBy() {
		switch (verifyBy) {
			case 0 :
				return "N/A";
			case 1 :
				return "Quiz";
			case 2 :
				return "Examination";
			case 3 :
				return "Report";
			case 4 :
				return "Presentation";
			case 5 :
				return "Case study";
			case 6 :
				return "Prototype";
			case 7 :
				return "Other";
			default :
				return "N/A";
		}
	}
	public void setVerifyBy(String verfy) {
		if (verfy != null)
			verfy = verfy.trim();
		else
			return;
		if (verfy.equals("N/A"))
			verifyBy = 0;
		else if (verfy.equals("Quiz"))
			verifyBy = 1;
		else if (verfy.equals("Examination"))
			verifyBy = 2;
		else if (verfy.equals("Report"))
			verifyBy = 3;
		else if (verfy.equals("Presentation"))
			verifyBy = 4;
		else if (verfy.equals("Case study"))
			verifyBy = 5;
		else if (verfy.equals("Prototype"))
			verifyBy = 6;
		else if (verfy.equals("Other"))
			verifyBy = 7;
		else
			verifyBy = 0;
		return;
	}
}
