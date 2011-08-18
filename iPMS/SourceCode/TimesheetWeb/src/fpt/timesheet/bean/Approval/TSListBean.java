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

import java.util.Collection;

import fpt.timesheet.framework.util.DateUtil.DateUtil;

public class TSListBean {

    private int intSearchProjectID 		= 0;
    private int intSearchStatus 		= 1;
    private int intSearchSortBy 		= 1;
	private int intCurrentPage 			= 0;
	private int intTotalPage 			= 0;
	private int intTotalTimesheet 		= 0;
	private int intRejectedTimesheets 	= 0;

    private String strSearchFromDate 	= null;
    private String strSearchToDate 		= null;
    private Collection arrListing 		= null;
    private String strSelectedTS 		= "";

    /**
     * Method getSearchProjectID.
     * @return intSearchProjectID
     */
    public int getSearchProjectID() {
        return intSearchProjectID;
    }

    /**
     * Method setSearchProjectID.
     * @param intSearchProjectID
     */
    public void setSearchProjectID(int intSearchProjectID) {
        this.intSearchProjectID = intSearchProjectID;
    }

    /**
     * Method getSearchStatus.
     * @return intSearchStatus
     */
    public int getSearchStatus() {
        return intSearchStatus;
    }

    /**
     * Method setSearchStatus.
     * @param intSearchStatus
     */
    public void setSearchStatus(int intSearchStatus) {
        this.intSearchStatus = intSearchStatus;
    }

    /**
     * Method getSearchSortBy.
     * @return intSearchSortBy
     */
    public int getSearchSortBy() {
        return intSearchSortBy;
    }

    /**
     * Method setSearchSortBy.
     * @param intSearchSortBy
     */
    public void setSearchSortBy(int intSearchSortBy) {
        this.intSearchSortBy = intSearchSortBy;
    }

    /**
     * Method getSearchFromDate.
     * @return strSearchFromDate
     */
    public String getSearchFromDate() {
        if (strSearchFromDate == null) {
            strSearchFromDate = DateUtil.getCurrentDate(-6);
        }
        return strSearchFromDate;
    }

    /**
     * Method setSearchFromDate.
     * @param strSearchFromDate
     */
    public void setSearchFromDate(String strSearchFromDate) {
        if (strSearchFromDate != null) {
            this.strSearchFromDate = strSearchFromDate;
            strSearchFromDate.trim();
        }
    }

    /**
     * Method getSearchToDate.
     * @return strSearchToDate
     */
    public String getSearchToDate() {
        if (strSearchToDate == null) {
            strSearchToDate = DateUtil.getCurrentDate(0);
        }
        return strSearchToDate;
    }

    /**
     * Method setSearchToDate.
     * @param strSearchToDate
     */
    public void setSearchToDate(String strSearchToDate) {
        if (strSearchToDate != null) {
			this.strSearchToDate = strSearchToDate;
			strSearchToDate.trim();
        }
    }

    /**
     * Method getRejectedTimesheets.
     * @return intRejectedTimesheets
     */
    public int getRejectedTimesheets() {
        return intRejectedTimesheets;
    }

    /**
     * Method setRejectedTimesheets.
     * @param intRejectedTimesheets
     */
    public void setRejectedTimesheets(int intRejectedTimesheets) {
        this.intRejectedTimesheets = intRejectedTimesheets;
    }

    /**
     * Method getTimesheetList.
     * @return arrListing
     */
    public Collection getTimesheetList() {
        return arrListing;
    }

    /**
     * Method setTimesheetList.
     * @param arrListing
     */
    public void setTimesheetList(Collection arrListing) {
        this.arrListing = arrListing;
    }

    /**
     * Method getSelectedTS.
     * @return strSelectedTS
     */
    public String getSelectedTS() {
        return strSelectedTS;
    }

    /**
     * Method setSelectedTS.
     * @param strSelectedTS
     */
    public void setSelectedTS(String strSelectedTS) {
        this.strSelectedTS = strSelectedTS;
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