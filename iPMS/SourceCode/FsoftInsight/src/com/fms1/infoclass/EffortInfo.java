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
public final class EffortInfo {
	public static final int TOW_REVIEW=3;
	public static final int TOW_CREATE=2;                                            
	public static final int TOW_CORRECT =5;                                          
	public static final int TOW_TEST =4;                                                                                    
	public static final int TOW_STUDY  =1;                                           
	public static final int TOW_TRANSLATE    =6;                                     
	public static final int TOW_ENGINEERING_REVIEW    =65500;  
	
	
	public double projectID;
	//public String projectCode;
	
	public double budgetedEffort;
	public double plannedEffort;
	public double rePlannedEffort;
	public double actualEffort;
	public double effortUseage;
	public double effortDeviation;
	public double developementEffort;
	public double managementEffort;
	public double qualityEffort;
	public double correctionEffort;
	public double perDevelopementEffort;
	public double perManagementEffort;
	public double perQualityEffort;
	public double perCorrectionEffort;
	public double translationEffort;
	public double perTranslationEffort;
    public double calendarEffort = Double.NaN;
    public double effortEfficiency = Double.NaN;
}