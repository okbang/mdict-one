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

import java.util.Arrays;

public class ProcessEffortByStageInfo {
    public long stageid;
	public String stage_name="";
// all arrays are mapped with process array
    static int temp=ProcessInfo.trackedProcessId.length;
    public double [] norm=new double [temp];
    public double [] planned=new double [temp];
    public double [] rePlanned=new double [temp];
    public double [] rePlannedLatest=new double [temp];
    public double [] actual=new double [temp];
	public double [] effortDeviation=new double [temp];
	public double [] plannedRCR=new double [temp];
    public double [] actualRCR=new double [temp];
	public double [] rcrDeviation=new double [temp];
    public double [] forecast=new double [temp];
    public double totalForecast=0;
    public double totalPlannedPrev=0;
    public boolean isOpen=false;
    public ProcessEffortByStageInfo(){
        Arrays.fill(norm,Double.NaN);
        Arrays.fill(planned,Double.NaN);
        Arrays.fill(rePlanned,Double.NaN);
        Arrays.fill(actual,0);
		Arrays.fill(effortDeviation,0);
		Arrays.fill(rcrDeviation,0);
        Arrays.fill(forecast,Double.NaN);
        Arrays.fill(rePlannedLatest,Double.NaN);
    }
}
