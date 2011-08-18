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

import com.fms1.tools.Color;
/**
 * @author manu
 * Created on Jul 17, 2003
 */
public final class PlanRCRInfo {
	public long metricConstant;
	public double plannedValue = Double.NaN;//RCR
	public double actual = 0;
	public double forecasted = 0;
	public double norm = Double.NaN;
	public int processid;
	public long stage;//when it was planned for
	public long milestone;//when it was planned
	public int color =Color.NOCOLOR;
    public double plannedEffort = Double.NaN;
    public static final PlanRCRInfo getPlan(Vector list, int processID,long stage){
        return getPlan( list,  processID, stage,-1);
    }
    public static final PlanRCRInfo getPlan(Vector list, int processID,long stage, long milestone){
    	PlanRCRInfo rcrInf;
    	//PlanRCRInfo retVal;
    	for (int k=0;k<list.size();k++){
    		rcrInf=(PlanRCRInfo)list.elementAt(k);
    		if (rcrInf.processid == processID && rcrInf.stage==stage && (milestone==-1 || rcrInf.milestone==milestone))
    			return rcrInf;
    	}
    	return null;
    }
}
