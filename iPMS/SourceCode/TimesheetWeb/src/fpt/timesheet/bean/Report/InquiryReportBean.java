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

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.bo.ComboBox.ProjectComboBO;
import fpt.timesheet.constant.DATA;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.util.CommonFunction;

public class InquiryReportBean implements DefinitionList {

    // For Search Form
    private StringMatrix mtxProjectList = null;
    private StringMatrix mtxGroupList = null;
    private StringMatrix mtxStatusList = null;
    private StringMatrix mtxSortbyList = null;

    // For Select to Search
    private int intProject;
    private int intStatus;
	private int intSortby;

	// Paging
	private int intCurrentPage = 0;
	private int intTotalPage = 0;
	private int intTotalTimesheet = 0;

    private String strGroup;
    private String strFromDate;
    private String strToDate;
    private String strAccount;
    private String strApprover;
    private String[] arrTimesheet = null;
    private String strListingName = DATA.VIEW_REPORT_INQUIRY;
	private String strProjectID = "";       //contain project IDs separated by commas

    // For Display
    private StringMatrix mtxTimesheetList = null;

	private String strExcelFile = null; // Original file name uploaded from client
	private String strZipFile = null; // Temporary file
//	private String strZipPath = null;
	private static String strRealPath = null;

	public static String getRealPath() {
		return strRealPath;
	}

	public static void setRealPath(String string) {
		if (strRealPath == null) {
			strRealPath = string;
		}
	}

	/**
	 * @return
	 */
	public String getExcelFile() {
		return strExcelFile;
	}

	/**
	 * @return
	 */
	public String getZipFile() {
		return strZipFile;
	}

	/**
	 * @return
	 */
//	public String getZipPath() {
//		return strZipPath;
//	}

	/**
	 * @param string
	 */
	public void setExcelFile(String string) {
		strExcelFile = string;
	}

	/**
	 * @param string
	 */
	public void setZipFile(String string) {
		strZipFile = string;
	}

	/**
	 * @param string
	 */
//	public void setZipPath(String string) {
//		strZipPath = string;
//	}

    // Contructor
    public InquiryReportBean() {
        this.contructList();
        this.intProject = 0;
        this.strGroup = "All";
        this.strAccount = "";
        this.strApprover = "";
    }

    /**
     * Method setProject.
     * @param intProject
     */
    public void setProject(int intProject) {
        this.intProject = intProject;
    }

    /**
     * Method getProject.
     * @return intProject
     */
    public int getProject() {
        return intProject;
    }

    /**
     * Method setProjectID.
     * @param strProjectID
     */
    public void setProjectID(String strProjectID) {
        this.strProjectID = strProjectID;
    }

    /**
     * Method getProjectID.
     * @return strProjectID
     */
    public String getProjectID() {
        return strProjectID;
    }

    /**
     * Method setGroup.
     * @param strGroup
     */
    public void setGroup(String strGroup) {
        this.strGroup = strGroup;
    }

    /**
     * Method getGroup.
     * @return strGroup
     */
    public String getGroup() {
        return strGroup;
    }

    /**
     * Method setAccount.
     * @param strAccount
     */
    public void setAccount(String strAccount) {
        this.strAccount = strAccount;
    }

    /**
     * Method getAccount.
     * @return strAccount
     */
    public String getAccount() {
        return strAccount;
    }

    /**
     * Method setApprover.
     * @param strApprover
     */
    public void setApprover(String strApprover) {
        this.strApprover = strApprover;
    }

    /**
     * Method getApprover.
     * @return strApprover
     */
    public String getApprover() {
        return strApprover;
    }

    /**
     * Method getGroupList.
     * @return mtxGroupList
     */
    public StringMatrix getGroupList() {
        return mtxGroupList;
    }

    /**
     * Method setGroupList.
     * @param List
     * @return boolean
     */
    public boolean setGroupList(StringMatrix List) {
        if (List != null) {
            this.mtxGroupList = new StringMatrix(List.toString());
            return true;
        }
        return false;
    }

    /**
     * Method getStatusList.
     * @return mtxStatusList
     */
    public StringMatrix getStatusList() {
        return mtxStatusList;
    }

    /**
     * Method setStatusList.
     * @param List
     * @return boolean
     */
    public boolean setStatusList(StringMatrix List) {
        if (List != null) {
            this.mtxStatusList = new StringMatrix(List.toString());
            return true;
        }
        return false;
    }

    /**
     * Method getSortbyList.
     * @return StringMatrix
     */
    public StringMatrix getSortbyList() {
        return this.mtxSortbyList;
    }

    /**
     * Method setSortbyList.
     * @param List
     * @return boolean
     */
    public boolean setSortbyList(StringMatrix List) {
        if (List != null) {
            this.mtxSortbyList = new StringMatrix(List.toString());
            return true;
        }
        return false;
    }

    /**
     * Method setStatus.
     * @param intStatus
     */
    public void setStatus(int intStatus) {
        this.intStatus = intStatus;
    }

    /**
     * Method getStatus.
     * @return intStatus
     */
    public int getStatus() {
        return intStatus;
    }

    /**
     * Method setSortby.
     * @param intSortby
     */
    public void setSortby(int intSortby) {
        this.intSortby = intSortby;
    }

    /**
     * Method getSortby.
     * @return intSortby
     */
    public int getSortby() {
        return intSortby;
    }

    /**
     * Method setFromDate.
     * @param strFromDate
     */
    public void setFromDate(String strFromDate) {
        if (strFromDate != null)
            this.strFromDate = strFromDate;
    }

    /**
     * Method getFromDate.
     * @return strFromDate
     */
    public String getFromDate() {
        if (this.strFromDate == null)
            this.strFromDate = CommonFunction.defaultFromDate();
        return this.strFromDate;
    }

    /**
     * Method setToDate.
     * @param strToDate
     */
    public void setToDate(String strToDate) {
        if (strToDate != null)
            this.strToDate = strToDate;
    }

    /**
     * Method getToDate.
     * @return strToDate
     */
    public String getToDate() {
        if (this.strToDate == null)
            this.strToDate = CommonFunction.defaultToDate();
        return this.strToDate;
    }

    /**
     * Method setListingName.
     * @param strListingName
     */
    public void setListingName(String strListingName) {
        this.strListingName = strListingName;
    }

    /**
     * Method getListingName.
     * @return strListingName
     */
    public String getListingName() {
        return strListingName;
    }

    /**
     * Method getProjectList.
     * @return mtxProjectList
     */
    public StringMatrix getProjectList() {
        return mtxProjectList;
    }

    /**
     * Method setProjectList.
     * @param List
     * @return boolean
     */
    public boolean setProjectList(StringMatrix List) {
        if (List != null) {
            this.mtxProjectList = new StringMatrix(List.toString());
            return true;
        }
        return false;
    }

    /**
     * Method getTimesheetList.
     * @return mtxTimesheetList
     */
    public StringMatrix getTimesheetList() {
        return mtxTimesheetList;
    }

    /**
     * Method setTimesheetList.
     * @param List
     * @return boolean
     */
    public boolean setTimesheetList(StringMatrix List) {
        if (List != null) {
            this.mtxTimesheetList = new StringMatrix(List.toString());
            return true;
        }
        return false;
    }

    /**
     * Method setProjectList.
     * @param strRole
     * @param intUserID
     */
    public void setProjectList(String strRole, int intUserID) {
        ProjectComboBO pc = new ProjectComboBO();
        this.mtxProjectList = pc.getProjectComboList(strRole, intUserID, RP_PROJECT_TYPE, LIST_ALL_PROJECT);

        //Thaison - November 29, 2002
        //External user can view all projects in his group only
        if (strRole.substring(6, 7).equals("1") && strRole.substring(0, 1).equals("0"))
            this.mtxProjectList = pc.getProjectComboList(strRole, intUserID, EXTERNAL_PROJECT_TYPE, LIST_ALL_PROJECT);
        if (this.strProjectID.length() < 2)
            this.strProjectID = this.mtxProjectList.getCell(0, 0);
    }

    /**
     * Method setProjectList.
     * @param strRole
     * @param intUserID
     * @param intPageType
     */
    //Added by Tu Ngoc Trung, 2003-11-26
    public void setProjectList(String strRole, int intUserID, int intPageType) {
        ProjectComboBO pc = new ProjectComboBO();
        this.mtxProjectList = pc.getProjectComboList(strRole, intUserID, intPageType, LIST_ALL_PROJECT);
        if (this.strProjectID.length() < 2) {
            this.strProjectID = this.mtxProjectList.getCell(0, 0);
        }
    }

    /**
     * Method setGroupList.
     */
    public void setGroupList() {
        CommonListBO cmlRef = new CommonListBO();
        this.mtxGroupList = cmlRef.getGroupList();
    }

    /**
     * Method contructList.
     */
    private void contructList() {
        // Status List
        this.mtxStatusList = new StringMatrix(6, 2);
        this.mtxStatusList.setCell(0, 0, "0");
        this.mtxStatusList.setCell(0, 1, "All");
        this.mtxStatusList.setCell(1, 0, "1");
        this.mtxStatusList.setCell(1, 1, "Unapproved");
        this.mtxStatusList.setCell(2, 0, "2");
        this.mtxStatusList.setCell(2, 1, "PL Approved");
        this.mtxStatusList.setCell(3, 0, "3");
        this.mtxStatusList.setCell(3, 1, "Misc Approved");
        this.mtxStatusList.setCell(4, 0, "4");
        this.mtxStatusList.setCell(4, 1, "QA Approved");
        this.mtxStatusList.setCell(5, 0, "5");
        this.mtxStatusList.setCell(5, 1, "Rejected");
        this.intStatus = 1;
        // Sortby List
        this.mtxSortbyList = new StringMatrix(6, 2);
        this.mtxSortbyList.setCell(0, 0, "1");
        this.mtxSortbyList.setCell(0, 1, "Project");
        this.mtxSortbyList.setCell(1, 0, "2");
        this.mtxSortbyList.setCell(1, 1, "Account");
        this.mtxSortbyList.setCell(2, 0, "3");
        this.mtxSortbyList.setCell(2, 1, "Report Date");
        this.mtxSortbyList.setCell(3, 0, "4");
        this.mtxSortbyList.setCell(3, 1, "Process");
        this.mtxSortbyList.setCell(4, 0, "5");
        this.mtxSortbyList.setCell(4, 1, "Type");
        this.mtxSortbyList.setCell(5, 0, "6");
        this.mtxSortbyList.setCell(5, 1, "Product");
        this.intSortby = 1;
    }

    /**
     * Method getArrTimesheet.
     * @return arrTimesheet
     */
    public String[] getArrTimesheet() {
        return arrTimesheet;
    }

    /**
     * Method setArrTimesheet.
     * @param arrTimesheet
     */
    public void setArrTimesheet(String[] arrTimesheet) {
        this.arrTimesheet = arrTimesheet;
    }

    /**
     * Method getCurrentPage.
     * @return intCurrentPage
     */
    public int getCurrentPage() {
        return intCurrentPage;
    }

    /**
     * Method setCurrentPage.
     * @param intCurrentPage
     */
    public void setCurrentPage(int intCurrentPage) {
        this.intCurrentPage = intCurrentPage;
    }

    /**
     * Method getTotalPage.
     * @return intTotalPage
     */
    public int getTotalPage() {
        return intTotalPage;
    }

    /**
     * Method setTotalPage.
     * @param intTotalPage
     */
    public void setTotalPage(int intTotalPage) {
        this.intTotalPage = intTotalPage;
    }

    /**
     * Method getTotalTimesheet.
     * @return intTotalTimesheet
     */
    public int getTotalTimesheet() {
        return intTotalTimesheet;
    }

    /**
     * Method setTotalTimesheet.
     * @param intTotalTimesheet
     */
    public void setTotalTimesheet(int intTotalTimesheet) {
        this.intTotalTimesheet = intTotalTimesheet;
    }

}