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
 * Created on Oct 7, 2004
 * Contain summary report for projects
 */
package fpt.timesheet.framework.util.CommonUtil;

import java.io.Serializable;

/**
 * @author trungtn
 *
 */
public class ProjectSummaryRow implements Serializable {
    private String strProjectCode;
    private float fDevelopment;
    private float fQuality;
    private float fManagement;
    private float fTotal;  // Total = Development + Quality + Management
    private float fTranslation;
    private float fCorrection;
    // Number represent with 2 digit after period
    //private static final float N_ROUND = 100; 
    
    /*
     * Round all numbers with format 0.00 and calculate Development
    public void roundNumbers() {
        fManagement = Math.round(fManagement * N_ROUND) / N_ROUND;
        fQuality = Math.round(fQuality * N_ROUND) / N_ROUND;
        fTotal = Math.round(fTotal * N_ROUND) / N_ROUND;
        fDevelopment = fTotal - fManagement - fQuality;
        fDevelopment = Math.round(fDevelopment * N_ROUND) / N_ROUND;
        
        fTranslation = Math.round(fTranslation * N_ROUND) / N_ROUND;
        fCorrection = Math.round(fCorrection * N_ROUND) / N_ROUND;
    }
     */
    
    /**
     * @return
     */
    public float getCorrection() {
        return fCorrection;
    }

    /**
     * @return
     */
    public float getDevelopment() {
        return fDevelopment;
    }

    /**
     * @return
     */
    public float getManagement() {
        return fManagement;
    }

    /**
     * @return
     */
    public float getQuality() {
        return fQuality;
    }

    /**
     * @return
     */
    public float getTotal() {
        return fTotal;
    }

    /**
     * @return
     */
    public float getTranslation() {
        return fTranslation;
    }

    /**
     * @param f
     */
    public void setCorrection(float f) {
        fCorrection = f;
    }

    /**
     * @param f
     */
    public void setDevelopment(float f) {
        fDevelopment = f;
    }

    /**
     * @param f
     */
    public void setManagement(float f) {
        fManagement = f;
    }

    /**
     * @param f
     */
    public void setQuality(float f) {
        fQuality = f;
    }

    /**
     * @param f
     */
    public void setTotal(float f) {
        fTotal = f;
    }

    /**
     * @param f
     */
    public void setTranslation(float f) {
        fTranslation = f;
    }

    /**
     * @return
     */
    public String getProjectCode() {
        return strProjectCode;
    }

    /**
     * @param string
     */
    public void setProjectCode(String string) {
        strProjectCode = string;
    }

}
