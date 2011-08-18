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

/**
 * Contains the information about a metric, including norm and actual value
 * 
 * @author manu
 * Created on Mar 13, 2003
 */
public final class NormInfo {
    // Start : only used for PostMortem Report
    public static final int REQUIREMENT_COMPLETENESS = 1;
    public static final int CORRECTION_COST = 4;
    public static final int TRANSLATION_COST = 7;
    public static final int ACCEPTANCE_RATE = 8;
    // End : only used for PostMortem Report
        
    public String normName;
    public String normID;
    public int metricID; //reference to MetricInfo constants
    public String normUnit;
    public String cause;
    public String note;
    public double plannedValue = Double.NaN;
    public double actualValue = Double.NaN;
    public double lcl = Double.NaN;
    public double ucl = Double.NaN;
    
	// Add by HaiMM - Start
	public double usl = Double.NaN;
	public double lsl = Double.NaN;
	// Add by HaiMM - End
    
    public double average = Double.NaN;
    public double normDeviation = Double.NaN;
    public double planDeviation = Double.NaN;
    public int colorType;
    public boolean isOk = false;
    public java.sql.Date date = null;
    // The milestone that limit calculations up to milestone
    public boolean dateDefined = false;

    public final static NormInfo getNormByMetricID(int metricID,Vector normInfos) {
        //TODO normInfos should be HashMap instead of Vector, keys of HashMap elements are metricID
        NormInfo normInfo;
        for (int k = 0; k < normInfos.size(); k++) {
            normInfo = (NormInfo) normInfos.elementAt(k);
            if ((normInfo != null) && (normInfo.metricID == metricID)) {
                return normInfo;
            }
        }
        return null;
    }
}