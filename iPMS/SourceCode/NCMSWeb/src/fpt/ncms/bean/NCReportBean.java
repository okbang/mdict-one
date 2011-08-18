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
 * @(#)NCReportBean.java 29-Mar-03
 */


package fpt.ncms.bean;

import fpt.ncms.util.StringUtil.StringMatrix;
import fpt.ncms.constant.NCMS;

/**
 * Class NCReportBean
 * Bean object for storing NC report information
 * @version 1.0 29-Mar-03
 * @author
 */
public class NCReportBean {
    /** Report list */
    private StringMatrix arrReport = null;
    /** Report grouping condition (GroupName/ProjectID) */
    private String strGroupBy = "GroupName";
    /** Date to query from */
    private String strFromDate = "";
    /** Date to query to */
    private String strToDate = "";
    private int n_TypeOfCause = 0;
    
    private boolean b_CheckedNC = true;
    private boolean b_CheckedOB = true;
    private boolean b_CheckedCC = true;
	private boolean b_CheckedPB = true;

    /** Combo group by*/
    private StringMatrix smComboGroupBy;
    private StringMatrix smComboTypeOfCause;

    /**
     * getReport
     * Get report list
     * @return  report
     */
    public final StringMatrix getReport() {
        return arrReport;
    }

    /**
     * setReport
     * Set report list
     * @param   inData - report list
     */
    public final void setReport(StringMatrix inData) {
        arrReport = inData;
    }

    /**
     * getGroupBy
     * Get grouping condition
     * @return  grouping condition
     */
    public final String getGroupBy() {
        return strGroupBy;
    }

    /**
     * setGroupBy
     * Set grouping condition
     * @param   inData - grouping condition
     */
    public final void setGroupBy(String inData) {
        strGroupBy = inData;
    }

    /**
     * getFromDate
     * Get from date
     * @return  from date
     */
    public final String getFromDate() {
        return strFromDate;
    }

    /**
     * setFromDate
     * Set from date
     * @param   inData - from date
     */
    public final void setFromDate(String inData) {
        strFromDate = inData;
    }

    /**
     * getToDate
     * Get to date
     * @return  to date
     */
    public final String getToDate() {
        return strToDate;
    }

    /**
     * setToDate
     * Set to date
     * @param   inData - to date
     */
    public final void setToDate(String inData) {
        strToDate = inData;
    }

    /**
     * getComboGroupBy
     * Get combo group by
     * @return  combo content
     */
    public final StringMatrix getComboGroupBy() {
        return smComboGroupBy;
    }

    /**
     * setComboGroupBy
     * Set combo group by
     * @param   inData - input content
     */
    public final void setComboGroupBy(StringMatrix inData) {
        smComboGroupBy = inData;
    }
    /**
     * Returns the checkedCC.
     * @return boolean
     */
    public boolean isCheckedCC() {
        return b_CheckedCC;
    }
    
	/**
	 * Returns the checkedPB.
	 * @return boolean
	 */
	public boolean isCheckedPB() {
		return b_CheckedPB;
	}

    /**
     * Returns the checkedNC.
     * @return boolean
     */
    public boolean isCheckedNC() {
        return b_CheckedNC;
    }

    /**
     * Returns the checkedOB.
     * @return boolean
     */
    public boolean isCheckedOB() {
        return b_CheckedOB;
    }

    /**
     * Sets the checkedCC.
     * @param checkedCC The checkedCC to set
     */
    public void setCheckedCC(boolean checkedCC) {
        b_CheckedCC = checkedCC;
    }
    
	/**
	 * Sets the checkedPB.
	 * @param checkedPB The checkedCC to set
	 */
	public void setCheckedPB(boolean checkedPB) {
		b_CheckedPB = checkedPB;
	}

    /**
     * Sets the checkedNC.
     * @param checkedNC The checkedNC to set
     */
    public void setCheckedNC(boolean checkedNC) {
        b_CheckedNC = checkedNC;
    }

    /**
     * Sets the checkedOB.
     * @param checkedOB The checkedOB to set
     */
    public void setCheckedOB(boolean checkedOB) {
        b_CheckedOB = checkedOB;
    }
    
    public String getNCTypes() {
        String strNCTypes = "";
        if (isCheckedNC()) {
            strNCTypes = strNCTypes + NCMS.NCTYPE_NC + ",";
        }
        if (isCheckedOB()) {
            strNCTypes = strNCTypes + NCMS.NCTYPE_OB + ",";
        }
        if (isCheckedCC()) {
            strNCTypes = strNCTypes + NCMS.NCTYPE_CC + ",";
        }
		if (isCheckedPB()) {
			strNCTypes = strNCTypes + NCMS.NCTYPE_PB + ",";
		}
        if (strNCTypes.length() > 0) {
            strNCTypes = strNCTypes.substring(0, strNCTypes.length() - 1);
        }
        
        return strNCTypes;
    }
    
    /**
     * Returns the typeOfCause.
     * @return int
     */
    public int getTypeOfCause() {
        return n_TypeOfCause;
    }

    /**
     * Sets the typeOfCause.
     * @param typeOfCause The typeOfCause to set
     */
    public void setTypeOfCause(int typeOfCause) {
        n_TypeOfCause = typeOfCause;
    }

    /**
     * getComboGroupBy
     * Get combo Type Of Cause
     * @return  combo content
     */
    public final StringMatrix getComboTypeOfCause() {
        return smComboTypeOfCause;
    }

    /**
     * setComboGroupBy
     * Set combo Type Of Cause
     * @param   inData - input content
     */
    public final void setComboTypeOfCause(StringMatrix inData) {
        smComboTypeOfCause = inData;
    }
}