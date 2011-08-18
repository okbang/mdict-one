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
 
 package fpt.timesheet.bean.Report;

import java.util.ArrayList;

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.bo.ComboBox.ProjectComboBO;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.util.CommonFunction;

public class SummaryReportBean implements DefinitionList {

    // Report Type..
    public static final int REPORTTYPE_SUMMARY 		= 0x00;
    public static final int REPORTTYPE_PROCESS_TOW 	= 0x01;
    public static final int REPORTTYPE_PRODUCT_TOW 	= 0x02;
    public static final int REPORTTYPE_KPA_TOW 		= 0x03;
    public static final int REPORTTYPE_ACCOUNT_DATE = 0x04;
    
    // Status..
    public static final int STATUS_UNAPPROVED 		= 0x00;
    public static final int STATUS_PLGL 			= 0x01;
    public static final int STATUS_QA 				= 0x02;
    public static final int STATUS_ALL 				= 0x03;
    
    // Attributes..    
	private int intProjectID = 0x00;
	private int intStatus = STATUS_UNAPPROVED;
	private int intReportType = REPORTTYPE_SUMMARY;
	private int intProjectStatus = Timesheet.PROJECT_STATUS_ONGOING;

    private String strFromDate = null;
    private String strToDate = null;	
	private String strGroup = "All";    // Default all groups
	private String strProjectIDs = "";  //contain project IDs separated by commas
	private String strProjectType = "";
	private String strAccount = "";

    private StringMatrix smtReport = null;
    private StringMatrix smtProjectList = null;    
    private StringMatrix smtProjectStatusList = null;
    private StringMatrix smtGroupList = null;
    private ArrayList m_ProjectSummary = null;

    /**
     * Method getProject.
     * @return intProjectID
     */
    public int getProject() {
        return intProjectID;
    }

    /**
     * Method setProject.
     * @param intProjectID
     */
    public void setProject(int intProjectID) {
        this.intProjectID = intProjectID;
    }

    /**
     * Method getFromDate.
     * @return strFromDate
     */
    public String getFromDate() {
        if (strFromDate == null) {
            strFromDate = CommonFunction.defaultFromDate();
        }
        return strFromDate;
    }

    /**
     * Method setFromDate.
     * @param strFromDate
     */
    public void setFromDate(String strFromDate) {
        if (strFromDate != null) {
            this.strFromDate = strFromDate;
            strFromDate.trim();
        }
    }

    /**
     * Method getToDate.
     * @return strToDate
     */
    public String getToDate() {
        if (strToDate == null) {
            strToDate = CommonFunction.defaultToDate();
        }
        return strToDate;
    }

    /**
     * Method setToDate.
     * @param strToDate
     */
    public void setToDate(String strToDate) {
        if (strToDate != null) {
			this.strToDate = strToDate;
			strToDate.trim();
        }
    }

    /**
     * Method getStatus.
     * @return intStatus
     */
    public int getStatus() {
        return intStatus;
    }

    /**
     * Method setStatus.
     * @param intStatus
     */
    public void setStatus(int intStatus) {
        this.intStatus = intStatus;
    }

    /**
     * Method getReportType.
     * @return intReportType
     */
    public int getReportType() {
        return intReportType;
    }

    /**
     * Method setReportType.
     * @param intReportType
     */
    public void setReportType(int intReportType) {
        this.intReportType = intReportType;
    }

    /**
     * Method setProjectList.
     * @param strRole
     * @param intUserID
     */
    public void setProjectList(String strRole, int intUserID) {
        ProjectComboBO pc = new ProjectComboBO();
        //Thaison - November 29, 2002
        //External user can view all projects in his group only
        if (strRole.substring(6, 7).equals("1") &&
            strRole.substring(0, 1).equals("0")) {
            this.smtProjectList = pc.getProjectComboList(strRole, intUserID,
                                                         EXTERNAL_PROJECT_TYPE,
                                                         LIST_ALL_PROJECT);
        }
        else {
            this.smtProjectList = pc.getProjectComboList(strRole, intUserID,
                                                         RP_PROJECT_TYPE,
                                                         LIST_ALL_PROJECT);
        }
        if (this.strProjectIDs.length() < 2)
            this.strProjectIDs = this.smtProjectList.getCell(0, 0);
    }

    //Added by Tu Ngoc Trung, 2003-11-26
    /**
     * Method setProjectList.
     * @param strRole
     * @param intUserID
     * @param intPageType
     */
    public void setProjectList(String strRole, int intUserID, int intPageType) {
        ProjectComboBO pc = new ProjectComboBO();
        this.smtProjectList = pc.getProjectComboList(strRole, intUserID,
                                                     intPageType,
                                                     LIST_ALL_PROJECT);
        if (this.strProjectIDs.length() < 2) {
            this.strProjectIDs = this.smtProjectList.getCell(0, 0);
        }
    }

    //Added by Tu Ngoc Trung, 2004-06-26
    /**
     * Method setProjectList.
     * @param strRole
     * @param intUserID
     * @param intPageType
     * @param intProjectStatus
     */
    public void setProjectList(String strRole, int intUserID, int intPageType, int intProjectStatus) {
        ProjectComboBO pc = new ProjectComboBO();
        this.smtProjectList = pc.getProjectComboList(strRole, intUserID,
                                                     intPageType,
                                                     intProjectStatus);
        if (this.strProjectIDs.length() < 2) {
            this.strProjectIDs = this.smtProjectList.getCell(0, 0);
        }
    }

    /**
     * Method getProjectList.
     * @return smtProjectList
     */
    public StringMatrix getProjectList() {
        return smtProjectList;
    }

    /**
     * Method setArrayOfProjectIDs.
     * @param strProjectIDs
     */
    public void setArrayOfProjectIDs(String strProjectIDs) {
        this.strProjectIDs = strProjectIDs;
    }

    /**
     * Method getArrayOfProjectIDs.
     * @return strProjectIDs
     */
    public String getArrayOfProjectIDs() {
        return strProjectIDs;
    }

    /**
     * Method getReportList.
     * @return smtReport
     */
    public StringMatrix getReportList() {
        return smtReport;
    }

    /**
     * Method setReportList.
     * @param smtReport
     */
    public void setReportList(StringMatrix smtReport) {
        this.smtReport = smtReport;
    }

    /**
     * Method getProjectType.
     * @return strProjectType
     */
    public String getProjectType() {
        return strProjectType;
    }

    /**
     * Method setProjectType.
     * @param strProjectType
     */
    public void setProjectType(String strProjectType) {
        this.strProjectType = strProjectType;
    }

    /**
     * Returns the Account.
     * @return strAccount
     */
    public String getAccount() {
        return strAccount;
    }

    /**
     * Sets the Account.
     * @param strAccount
     */
    public void setAccount(String strAccount) {
        this.strAccount = strAccount;
    }

    /**
     * Method getProjectStatus.
     * @return intProjectStatus
     */
    public int getProjectStatus() {
        return intProjectStatus;
    }

    /**
     * Method setProjectStatus.
     * @param intProjectStatus
     */
    public void setProjectStatus(int intProjectStatus) {
        this.intProjectStatus = intProjectStatus;
    }

    /**
     * Method getProjectStatusList.
     * @return smtProjectStatusList
     */
    public StringMatrix getProjectStatusList() {
        return smtProjectStatusList;
    }

    /**
     * Method setProjectStatusList.
     * @param smtProjectStatusList
     */
    public void setProjectStatusList(StringMatrix smtProjectStatusList) {
        this.smtProjectStatusList = smtProjectStatusList;
    }

    /**
     * Method setProjectStatusList.
     * @param strRole
     * @param intUserID
     */
    public void setProjectStatusList(String strRole, int intUserID) {
        ProjectComboBO pc = new ProjectComboBO();
        this.smtProjectStatusList = pc.getProjectStatusComboList(strRole, intUserID);
    }

    /**
     * Method getGroupList.
     * @return smtGroupList
     */
    public StringMatrix getGroupList() {
        return smtGroupList;
    }

    /**
     * Method setGroupList.
     * @param smtGroupList
     */
    public void setGroupList(StringMatrix smtGroupList) {
        this.smtGroupList = smtGroupList;
    }

    /**
     * Method generateGroupList.
     */
    public void generateGroupList() {
        CommonListBO cmlRef = new CommonListBO();
        this.smtGroupList = cmlRef.getGroupList();
    }
    
    /**
     * Method getGroup.
     * @return strGroup
     */
    public String getGroup() {
        return strGroup;
    }

    /**
     * Method setGroup.
     * @param strGroup
     */
    public void setGroup(String strGroup) {
        this.strGroup = strGroup;
    }

    /**
     * Method getProjectSummary.
     * @return m_ProjectSummary
     */
    public ArrayList getProjectSummary() {
        return m_ProjectSummary;
    }

    /**
     * Method setProjectSummary.
     * @param m_ProjectSummary
     */
    public void setProjectSummary(ArrayList m_ProjectSummary) {
        this.m_ProjectSummary = m_ProjectSummary;
    }

}