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
 
 package fpt.timesheet.bean.Approval;

public class ApproveModelBean {

    private int intTimesheetId;
	private int intProcessId;
	private int intTypeId;
	private int intProductId;
	private int intKpaId;
	private int intStatus;
	private float fltDuration;

    private String strProject; // Code
    private String strAccount;
    private String strDate;
    private String strDescription;
    private String strLeader;
    private String strQA;
    private String strComment = "";

    /**
     * @see java.lang.Object#Object()
     */
    public ApproveModelBean() {
        this.intTimesheetId = 0;
        this.strProject = "";
        this.strAccount = "";
        this.strDate = "";
        this.strDescription = "";
        this.fltDuration = 0;
        this.intProcessId = 0;
        this.intTypeId = 0;
        this.intProductId = 0;
        this.intKpaId = 0;
        this.strLeader = "";
        this.strQA = "";
        this.intStatus = 1;
    }

    /**
     * Method ApproveModelBean.
     * @param intTimesheetId
     * @param strProject
     * @param strAccount
     * @param strDate
     * @param strDescription
     * @param fltDuration
     * @param intProcessId
     * @param intTypeId
     * @param intProductId
     * @param intKpaId
     * @param strQA
     * @param intStatus
     */
    //QA
    public ApproveModelBean(int intTimesheetId, String strProject, String strAccount, String strDate, String strDescription, 
    						float fltDuration, int intProcessId, int intTypeId, int intProductId, int intKpaId, String strQA, int intStatus) {
        this.intTimesheetId = intTimesheetId;
        this.strProject = strProject;
        this.strAccount = strAccount;
        this.strDate = strDate;
        this.strDescription = strDescription;
        this.fltDuration = fltDuration;
        this.intProcessId = intProcessId;
        this.intTypeId = intTypeId;
        this.intProductId = intProductId;
        this.intKpaId = intKpaId;
        this.strQA = strQA;
        this.intStatus = intStatus;
    }

    /**
     * Method ApproveModelBean.
     * @param intTimesheetId
     * @param strProject
     * @param strAccount
     * @param strDate
     * @param strDescription
     * @param fltDuration
     * @param intProcessId
     * @param intTypeId
     * @param intProductId
     * @param intKpaId
     * @param strLeader
     * @param intStatus
     */
    // Leader
    public ApproveModelBean(int intTimesheetId, String strProject, String strAccount, String strDate, String strDescription, 
    						float fltDuration, int intProcessId, int intTypeId, int intProductId, String strLeader, int intStatus) {
        this.intTimesheetId = intTimesheetId;
        this.strProject = strProject;
        this.strAccount = strAccount;
        this.strDate = strDate;
        this.strDescription = strDescription;
        this.fltDuration = fltDuration;
        this.intProcessId = intProcessId;
        this.intTypeId = intTypeId;
        this.intProductId = intProductId;
        this.strLeader = strLeader;
        this.intStatus = intStatus;
    }

    /**
     * Method getId.
     * @return intTimesheetId
     */
    public int getId() {
        return intTimesheetId;
    }

    /**
     * Method getProject.
     * @return strProject
     */
    public String getProject() {
        return strProject;
    }

    /**
     * Method getAccount.
     * @return strAccount
     */
    public String getAccount() {
        return strAccount;
    }

    /**
     * Method getDate.
     * @return strDate
     */
    public String getDate() {
        return strDate;
    }

    /**
     * Method getDescription.
     * @return strDescription
     */
    public String getDescription() {
        return strDescription;
    }

    /**
     * Method getDuration.
     * @return fltDuration
     */
    public float getDuration() {
        return fltDuration;
    }

    /**
     * Method getProcess.
     * @return intProcessId
     */
    public int getProcess() {
        return intProcessId;
    }

    /**
     * Method getType.
     * @return intTypeId
     */
    public int getType() {
        return intTypeId;
    }

    /**
     * Method getProduct.
     * @return intProductId
     */
    public int getProduct() {
        return intProductId;
    }

    /**
     * Method getKpa.
     * @return intKpaId
     */
    public int getKpa() {
        return intKpaId;
    }

    /**
     * Method getLeader.
     * @return strLeader
     */
    public String getLeader() {
        return strLeader;
    }

    /**
     * Method getQA.
     * @return strQA
     */
    public String getQA() {
        return strQA;
    }

    /**
     * Method getStatus.
     * @return intStatus
     */
    public int getStatus() {
        return intStatus;
    }

    /**
     * Method getComment.
     * @return strComment
     */
    public String getComment() {
        return strComment;
    }

    /**
     * Method setComment.
     * @param strComment
     */
    public void setComment(String strComment) {
        this.strComment = strComment;
    }
}