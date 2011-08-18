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
 * @(#)NCReportPivotBean.java 04-Apr-03
 */


package fpt.ncms.bean;

import java.util.ArrayList;

import fpt.ncms.util.StringUtil.StringMatrix;

/**
 * Class NCReportPivotBean
 * Bean object for storing NC report pivot information
 * @version 1.0 04-Apr-03
 * @author
 */
public final class NCReportPivotBean extends NCReportBean {
    /** Report type */
    private String strReportType = "DetectedBy";
    /** Number of report fields */
    private int nNumReportField = 3;

    /** Combo report type */
    private StringMatrix smComboReportType;

    /**
     * getReportType
     * Get report type
     * @return  report type
     */
    public final String getReportType() {
        return strReportType;
    }

    /**
     * setReportType
     * Set report type
     * @param   inData  - input type
     */
    public final void setReportType(String inData) {
        strReportType = inData;
    }

    /**
     * getComboReportType
     * Get combo report type
     * @return  combo content
     */
    public final StringMatrix getComboReportType() {
        return smComboReportType;
    }

    /**
     * setComboReportType
     * Set combo report type
     * @param   inData - input content
     */
    public final void setComboReportType(StringMatrix inData) {
        smComboReportType = inData;
    }

    /**
     * getNumReportField
     * Get number of report fields
     * @return  number of report fields
     */
    public final int getNumReportField() {
        return nNumReportField;
    }

    /**
     * setNumReportField
     * Set number of report fields
     * @param   inData - number of report fields
     */
    public final void setNumReportField(int inData) {
        nNumReportField = inData;
    }
}