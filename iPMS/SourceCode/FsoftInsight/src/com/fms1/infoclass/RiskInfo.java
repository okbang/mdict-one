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

import com.fms1.tools.CommonTools;
public final class RiskInfo {
	public long riskID = 0;		
	public String categoryName = "";
	public long sourceID = 0;	
	public int type = 0;
	public String condition = null;
	//LAMNT3 - 20081020
	public int project_category = 0;
	public String sourceName = "";
	public String contigency = "";
	
	// Added by HaiMM -- Start ------------
	
	// Maximum Mitigation/Contingency.    
	public static final int LINES_MAX = 5;
	// Minimum Mitigation/Contingency.
	public static final int LINES_MIN_DISPLAY = 3;
	// Additional blank lines will be added bellow current Mitigation/Contingency.
	public static final int LINES_PLUS = 2;
	
	// For insert/update/delete mitigation or contingency
	public static final int DML_INSERT = 1;
	public static final int DML_UPDATE = 2;
	public static final int DML_DELETE = 3;
	
	// Added by HaiMM -- End ----------------
	
	public String consequence = null;
	public String threshold = null;
	public double probability = 0;
	public double impact = 0;
	public int priority = 0;
	public int riskPriority = 0;
	public int unit = 0;
	public int estimatedImpact = 0;	
	public String mitigation = null;
	public String mitigationBenefit = null;
	public String mitigationActual = null;
	public String mitigationCost = null;
	public String contingencyPlan = null;
	public String triggerName = null;
	public long developerID = 0;
	public Date assessmentDate = null;
	public Date lastUpdatedDate = null;
	public Date planEndDate = null;
	public Date actualEndDate = null;
	public int riskStatus = 0;
	public int status = 0;
	public String actualRiskScenario = null;
	public String actualAction = null;
	public String actualImpact = null;
	public int unplanned = 0;
	public long projectID = 0;
	public String projectCode = "";
	public String groupName = "";
	public int projectType = -1;
	public java.sql.Date start_date = null;
	public String plannedImpact = null;
	public double exposure = 0;
	public String developerAcc = null;
	//actual impact
	public  String[] imp ;
	public  String[] est ;
	public  String[] unt ;
	//planned impact
	public  String[] pimp ;
	public  String[] pest ;
	public  String[] punt ;
	public String developerName;
	public int processId = 0;
	public boolean baselined= false;
	public static String [] impactTypes={
		"",
		"Schedule",
		"Effort",
		"Finance",
		"Team",
		"Timeliness",
		"Requirement completion",
		"Leakage",
		"Customer satisfaction",
		"Correction Cost",
		"Other"
	};
	public static String [] impactUnits={
		"",
		"%",
		"day",
		"month",
		"person.day",
		"person.month",
		"$",
		"#"
	};
	public static String [] alfa={"a","b","c","d","e","f","g","h","i","j","k"};
	public RiskInfo() {
		riskID = 0;
		condition = "";
		consequence = "";
		probability = 0;
		impact = 0;
		unit = 0;
		estimatedImpact = 0;
		plannedImpact = "";
		mitigation = "";
		contingencyPlan = "";
		triggerName = "";
		developerID = 0;
		assessmentDate = null;
		status = 0;
		actualRiskScenario = "";
		actualAction = "";
		actualImpact = "";
		unplanned = 0;
		exposure = 0;
		projectID = 0;
		projectCode = "";
		projectType = 0;
		start_date = null;
		processId = 0;
	}
	public void parseImpact(){
		if (actualImpact!=null && actualImpact.length()>0){
			imp = parseImpactType(actualImpact);
			unt = parseImpactUnit(actualImpact);
			est = parseImpactValue(actualImpact);
		}
		if (plannedImpact!=null && plannedImpact.length()>0){
			pimp = parseImpactType(plannedImpact);
			punt = parseImpactUnit(plannedImpact);
			pest = parseImpactValue(plannedImpact);
		}
	}

	String [] parseImpactValue(String impact) {
		String [] imp = new String[3];
		String impactType;
		String na="N/A";
		for (int i = 0; i < imp.length; i++) {
			impactType = impact.substring(i*16+2, i*16+16);
			if (impactType.substring(0,1).equals("-")) 
				imp[i] = na;
			else //nasty, remove the 0000 at the beginning
				imp[i] = CommonTools.formatDouble(CommonTools.parseDouble(impactType));
		}
		return imp;
	}

	String [] parseImpactUnit(String impact) {
		String imp[] = new String[3];
		String impactType;
		String na="N/A";
		for (int i = 0; i < imp.length; i++) {
			impactType = impact.substring(i*16+1, i*16+2);
			imp[i]=na;
			for (int j=1;j<impactUnits.length;j++){
				if (impactType.equals(alfa[j])){ 
					imp[i] = impactUnits[j];
					break;
				}
			}
		}
		return imp;
	}

	String [] parseImpactType(String impact) {
		String imp[] = new String[3];
		String impactType;
		String na="N/A";
		for (int i = 0; i < imp.length; i++) {
			impactType = impact.substring(i*16, i*16+1);
			imp[i]=na;
			for (int j=1;j<impactTypes.length;j++){
				if (impactType.equals(alfa[j])) {
					imp[i] = impactTypes[j];
					break;
				}
			}
		}
		return imp;
	}
}