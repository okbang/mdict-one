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

import com.fms1.tools.CommonTools;

public final class StandardMetricInfo {

    /**
    * Get all info for Standard Metric
    */
    private float targetedValue;
    private float actualValue;
    private String note;
    
    // Add by HaiMM - Start
	private float uslValue;
	private float lslValue;
	// Add by HaiMM - End
    
    public StandardMetricInfo() {
        uslValue = Float.NaN;
		lslValue = Float.NaN;
        targetedValue = Float.NaN;
		actualValue = Float.NaN;
        note = null;
    }
	
	//	Add by HaiMM - Start
	public float getUslValue() {
		return this.uslValue;
	}
	public void setUslValue(String strUslValue) {
		if (strUslValue == null || "".equals(strUslValue)) {
			this.uslValue = Float.NaN;
		} else {
			this.uslValue = Float.parseFloat(strUslValue);
		}
	}
	
	public float getLslValue() {
		return this.lslValue;
	}
	public void setLslValue(String strLslValue) {
		if (strLslValue == null || "".equals(strLslValue)) {
			this.lslValue = Float.NaN;
		} else {
			this.lslValue = Float.parseFloat(strLslValue);
		}
	}
	// Add by HaiMM - End
	
    /**
    * Get and Show All Properties
    */
    public float getTargetedValue() {
        return this.targetedValue;
    }
    public void setTargetedValue(String strTargetedValue) {
        if (strTargetedValue == null || "".equals(strTargetedValue)) {
            this.targetedValue = Float.NaN;
        } else {
            this.targetedValue = Float.parseFloat(strTargetedValue);
        }
    }
    
    public float getActualValue() {
        return this.actualValue;
    }
    // public void setActualValue(float floatActualValue) {
    //    this.actualValue = floatActualValue;
    // }
    public void setActualValue(String strActualValue) {
        if (strActualValue == null || "".equals(strActualValue)) {
            this.actualValue = Float.NaN;
        } else {
            this.actualValue = Float.parseFloat(strActualValue);
        }
    }
    
    public String getNote() {
        return this.note;
    }
    public void setNote(String strNote) {
        if (strNote == null) {
            this.note = "";
        } else {
            this.note = strNote;
        }
    }
}