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
 
 package com.fms1.tools;
/**
 * Called by JSPs, returns HTML code
 * used to display the metrics with the appropriate color
 * @author manu
 * @date on Jul 16, 2003
 */
public class Color {
	
	/**
	 * HIGHBAD and HIGHGOOD are passed to the function as 'colorType'.
	 * 'colorType' is also defined in the table METRIC_DES , column 'COLOR_TYPE'.
	 * HIGHBAD meaning that a high value of the metric is considered bad ,
	 * and will get the 'bad' red color
	 */
	public static final int NOCOLOR=0;
	// good or bad metrics
	public static final int GOODMETRIC=1;
	public static final int BADMETRIC=2;
	//colorType
	public static final int HIGHBAD=2;
	public static final int HIGHGOOD=1;
	
	private static final String good = "<font color = \"#0000ff\">"; // blue
	private static final String bad = "<font color = \"#ff0000\">";  // red
	public static final String colorByNorm(String colorMe,double metricValue, double lcl, double ucl, int colorType) {
		return setColor(getColor(metricValue, lcl, ucl, colorType), colorMe);
	}
	public static final String setColor(int isGood, String colorMe) {
		switch(isGood){
			case NOCOLOR:
				return colorMe;
			case GOODMETRIC:
				return good+colorMe+"</font>";
			case BADMETRIC:
				return bad+colorMe+"</font>";
			default:	
				return colorMe;
		}
	}
	public static final int getColor(double metricValue, double lcl, double ucl, int colorType) {
		if (!Double.isNaN(metricValue)) {
			if ((ucl != Double.NaN) && (metricValue > ucl)) {
				if (colorType == HIGHGOOD)
					return GOODMETRIC;
				else if (colorType == HIGHBAD)
					return BADMETRIC;
			}
			else if ((lcl != Double.NaN) && (metricValue < lcl)) {
				if (colorType == HIGHGOOD)
					return BADMETRIC;
				else if (colorType == HIGHBAD)
					return GOODMETRIC;
			}
		}
		return NOCOLOR;
	}
}