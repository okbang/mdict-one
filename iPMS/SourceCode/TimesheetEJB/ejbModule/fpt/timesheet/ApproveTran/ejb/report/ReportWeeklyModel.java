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
 
 /**
 * Description  :  Timesheet <p>
 * Author       :  TrangTK
 * Created date :  Jan 10, 2002
 */

package fpt.timesheet.ApproveTran.ejb.report;

import java.io.Serializable;

public class ReportWeeklyModel implements Serializable {

    private String strKey;
	private String strValue;
	private String strLeader;

	private String strTitle;
	private String strProjectCode;
	private String strPQAAccount;
	private String strPQAName;

	private int intProjectId;
	private double dblTotalEffort;
	private double dblUnapprovedEffort;
	private double dblRatioEffort;

    /**
     * Method ReportWeeklyModel.
	 * @param strKey
	 * @param strValue
	 * @param strLeader
	 */	
	public ReportWeeklyModel() {
	}

	/**
	 * @param strKey
	 * @param strValue
	 * @param strLeader
	 */
	public ReportWeeklyModel(String strKey, String strValue, String strLeader) {
		this.strKey = strKey;
		this.strValue = strValue;
		this.strLeader = strLeader;
	}

	/**
	 * Method ReportWeeklyModel.
	 * @param strKey
	 * @param strValue
	 * @param strLeader
	 * @param strEffort
	 */
    public ReportWeeklyModel(String strKey, String strLeader, double dblUnapprovedEffort, String strValue) {
        this.strKey = strKey;
		this.strLeader = strLeader;
        this.dblUnapprovedEffort = dblUnapprovedEffort;
		this.strValue = strValue;
    }

	/**
	 * @param strKey
	 * @param strLeader
	 * @param strTitle
	 * @param strValue
	 */
	public ReportWeeklyModel(String strKey, String strLeader, String strTitle, String strValue) {
		this.strKey = strKey;
		this.strLeader = strLeader;
		this.strTitle = strTitle;
		this.strValue = strValue;
	}

    /**
     * Method getKey.
     * @return String
     */
    public String getKey() {
        return this.strKey;
    }

    /**
	 * @param strKey
	 */
	public void setKey(String strKey) {
    	this.strKey = strKey; 
    }

    /**
     * Method getValue.
     * @return String
     */
    public String getValue() {
        return this.strValue;
    }

    /**
	 * @param strValue
	 */
	public void setValue(String strValue) {
    	this.strValue = strValue;
    }

    /**
     * Method getLeader.
     * @return String
     */
    public String getLeader() {
        return this.strLeader;
    }
    
    /**
	 * @param strLeader
	 */
	public void setLeader(String strLeader) {
    	this.strLeader = strLeader;
    }
    
	
	/**
	 * @return dblUnapprovedEffort
	 */
	public double getUnapprovedEffort() {
		return this.dblUnapprovedEffort;
	}    
	
	/**
	 * @param dblUnapprovedEffort
	 */
	public void setUnapprovedEffort(double dblUnapprovedEffort) {
		this.dblUnapprovedEffort = dblUnapprovedEffort;
	}
	
	/**
	 * @return dblTotalEffort
	 */
	public double getTotalEffort() {
		return this.dblTotalEffort;
	}	
	
	/**
	 * @param dblTotalEffort
	 */
	public void setTotalEffort(double dblTotalEffort) {
		this.dblTotalEffort = dblTotalEffort;
	}
	
	/**
	 * @return dblRatio
	 */
	public double getRatioEffort() {
		return this.dblRatioEffort;
	}
	
	/**
	 * @param dblRatio
	 */
	public void setRatioEffort(double dblRatioEffort) {
		this.dblRatioEffort = dblRatioEffort;
	}
	
	/**
	 * @return intProjectId
	 */
	public int getProjectId() {
		return this.intProjectId;
	}
	
	/**
	 * @param intProjectId
	 */
	public void setProjectId(int intProjectId) {
		this.intProjectId = intProjectId;
	}
	
	/**
	 * @return strProjectCode
	 */
	public String getProjectCode() {
		return strProjectCode;
	}

	/**
	 * @param strProjectCode
	 */
	public void setProjectCode(String strProjectCode) {
		this.strProjectCode = strProjectCode;
	}

	/**
	 * @return strPQAAccount
	 */
	public String getPQAAccount() {
		return strPQAAccount;
	}

	/**
	 * @param strPQAAccount
	 */
	public void setPQAAccount(String strPQAAccount) {
		this.strPQAAccount = strPQAAccount;
	}

	/**
	 * @return strPQAName
	 */
	public String getPQAName() {
		return strPQAName;
	}

	/**
	 * @param strPQAName
	 */
	public void setPQAName(String strPQAName) {
		this.strPQAName = strPQAName;    	
	}
	
}