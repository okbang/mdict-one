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
 
 /*
 * Created on Jun 17, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.fms1.infoclass.group;

import java.util.Vector;

import com.fms1.infoclass.RiskContingencyInfo;
import com.fms1.infoclass.RiskInfo;
import com.fms1.infoclass.RiskMitigationInfo;

/**
 * @author TienHM08
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FullRiskInfo {
	private RiskInfo riskInfo;
	private Vector riskMitigationInfo;
	private Vector riskContingencyInfo;
	
	/**
	 * @return
	 */
	public Vector getRiskContingencyInfo() {
		return riskContingencyInfo;
	}

	/**
	 * @return
	 */
	public RiskInfo getRiskInfo() {
		return riskInfo;
	}

	/**
	 * @return
	 */
	public Vector getRiskMitigationInfo() {
		return riskMitigationInfo;
	}

	/**
	 * @param vector
	 */
	public void setRiskContingencyInfo(Vector vector) {
		riskContingencyInfo = vector;
	
	}

	/**
	 * @param info
	 */
	public void setRiskInfo(RiskInfo info) {
		riskInfo = info;
	}

	/**
	 * @param vector
	 */
	public void setRiskMitigationInfo(Vector vector) {
		riskMitigationInfo = vector;
	}

}
