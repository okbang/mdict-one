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
 
 package com.fms1.infoclass.group;
import com.fms1.tools.Color;
/**
 * Contains the information about a metric to be displayed in PCB report
 * @author manu
 * Created on oct 07, 2003
 */
public final class PCBMetricInfo {
		public String metricID;
		public String name;
		public String unit;
		//previous period
		public double prevLCL=Double.NaN;
		public double prevAvg=Double.NaN;
		public double prevUCL=Double.NaN;
		//actual period
		public double LCL=Double.NaN;
		public double actualAvg=Double.NaN;
		public double UCL=Double.NaN;
		//norm for actual period
		String normWUName=null; //from wich workunit comes the norm
		public double normLCL=Double.NaN;
		public double normValue=Double.NaN;
		public double normUCL=Double.NaN;
		
		
		public double deviation=Double.NaN;
		public int colorType=Color.NOCOLOR;
		public int metricConstant;
		public boolean isMetricGroup=false;//used to display metric groups in PCB metric page
		
		//for PCB detail
		/* all ProjectXXX arrays should all be the same length 
		 * and values should be in the same order as in 
		 * the PCBGalInfo.projectList*/
		public double [] projectValues;
		public String [] projectComments;
		public double [] timeValues;
		public String [] timeLabels;
		public double K=Double.NaN;
		public double sigma=Double.NaN;
		public double stDev=Double.NaN;
		public String definition;
		public String suggestion;
		public String analysis;
}
