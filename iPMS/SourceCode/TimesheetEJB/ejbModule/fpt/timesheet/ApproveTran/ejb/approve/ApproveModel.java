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
 * @(#)ApproveModel.java 23-April-03
 */

package fpt.timesheet.ApproveTran.ejb.approve;

/**
 * Class ApproveModel
 * Description: Approvinf model.
 * @version 1.0 23-April-03
 * @author
 */
public class ApproveModel implements java.io.Serializable {

    /** timesheet ident.number */
    private int nTimesheetId;
    /** project code */
    private String strProject;
    /** account of timesheet record */
    private String strAccount;
    /** timesheet date */
    private String strDate;
    /** timesheet description */
    private String strDescription;
    /** timesheet duration */
    private float fDuration;

    /** process ident.number */
    private int nProcess;
    /** type of work */
    private int nType;
    /** type of work product */
    private int nProduct;

    /** KPA */
    private int nKpa;

    /** name of QA who approved this timesheet */
    private String strQA;
    /** name of leader who approved this timesheet */
    private String strLeader;

    /** status of timesheet record */
    private int nStatus;
    /** added comment */
    String comment;

    /**
     * ApproveModel
     * Class constructor
     */
    public ApproveModel() {
        this.nTimesheetId = 0;
        this.strProject = "";
        this.strAccount = "";

        this.strDate = "";
        this.strDescription = "";
        this.fDuration = 0;

        this.nProcess = 0;
        this.nType = 0;
        this.nProduct = 0;

        this.strLeader = "";
        this.nStatus = 1; // "UnApproved";

        this.nKpa = 0;
        this.strQA = "";
    }

    /**
     * setId
     * Setter method for nTimesheetId
     * @param num - input value for setting
     */
    public void setId(int num) {
        this.nTimesheetId = num;
    }

    /**
     * getId
     * Getter method fot nTimesheetId
     * @return nTimesheetId
     */
    public int getId() {
        return this.nTimesheetId;
    }

    /**
     * setProject
     * Setter method for strProject
     * @param str - input data for setting
     */
    public void setProject(String str) {
        this.strProject = str;
    }

    /**
     * getProject
     * Getter method for strProject
     * @return strProject
     */
    public String getProject() {
        return this.strProject;
    }

    /**
     * setAccount
     * Setter method for strAccount
     * @param str - input data for setting
     */
    public void setAccount(String str) {
        this.strAccount = str;
    }

    /**
     * getAccount
     * Getter method for strAccount
     * @return strAccount
     */
    public String getAccount() {
        return this.strAccount;
    }

    /**
     * setDate
     * Setter method for strDate
     * @param str - input data for setting
     */
    public void setDate(String str) {
        this.strDate = str;
    }

    /**
     * getDate
     * Getter method for strDate
     * @return strDate
     */
    public String getDate() {
        return this.strDate;
    }

    /**
     * setDescription
     * Setter method for strDescription
     * @param str - input data for setting
     */
    public void setDescription(String str) {
        this.strDescription = str;
    }

    /**
     * getDescription
     * Getter method for strDescription
     * @return strDescription
     */
    public String getDescription() {
        return this.strDescription;
    }

    /**
     * setDuration
     * Setter method for fDuration
     * @param num - input data for setting
     */
    public void setDuration(float num) {
        this.fDuration = num;
    }

    /**
     * getDuration
     * Getter method for fDuration
     * @return fDuration
     */
    public float getDuration() {
        return this.fDuration;
    }

    /**
     * setProcess
     * Setter method for nProcess
     * @param num - input data for setting
     */
    public void setProcess(int num) {
        this.nProcess = num;
    }

    /**
     * getProcess
     * Getter method for nProcess
     * @return nProcess
     */
    public int getProcess() {
        return this.nProcess;
    }

    /**
     * setType
     * Setter method for nType
     * @param num - input data for setting
     */
    public void setType(int num) {
        this.nType = num;
    }

    /**
     * getType
     * Getter method for nType
     * @return nType
     */
    public int getType() {
        return this.nType;
    }

    /**
     * setProduct
     * Setter method for nProduct
     * @param num - input data for setting
     */
    public void setProduct(int num) {
        this.nProduct = num;
    }

    /**
     * getProduct
     * Getter method for nProduct
     * @return nProduct
     */
    public int getProduct() {
        return this.nProduct;
    }

    /**
     * setKpa
     * Setter method for nKpa
     * @param num - input data for setting
     */
    public void setKpa(int num) {
        this.nKpa = num;
    }

    /**
     * getKpa
     * Getter method for nKpa
     * @return nKpa
     */
    public int getKpa() {
        return this.nKpa;
    }

    /**
     * setLeader
     * Setter method for strLeader
     * @param str - input data for setting
     */
    public void setLeader(String str) {
        this.strLeader = str;
    }

    /**
     * getLeader
     * Getter method for strLeader
     * @return strLeader
     */
    public String getLeader() {
        return this.strLeader;
    }

    /**
     * setQA
     * Setter method for strQA
     * @param str - input data for setting
     */
    public void setQA(String str) {
        this.strQA = str;
    }

    /**
     * getQA
     * Getter method for strQA
     * @return strQA
     */
    public String getQA() {
        return this.strQA;
    }

    /**
     * setStatus
     * Setter method for nStatus
     * @param num - input data for setting
     */
    public void setStatus(int num) {
        this.nStatus = num;
    }

    /**
     * getStatus
     * Getter method for nStatus
     * @return nStatus
     */
    public int getStatus() {
        return this.nStatus;
    }

    /**
     * getComment
     * Getter method for comment
     * @return comemnt
     */
    public String getComment() {
        return comment;
    }

    /**
     * setComment
     * Setter method for comment
     * @param comment - input data for setting
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}