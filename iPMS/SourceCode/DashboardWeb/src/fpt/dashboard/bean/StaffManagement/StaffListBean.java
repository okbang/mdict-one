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
 
 package fpt.dashboard.bean.StaffManagement;

import fpt.dashboard.util.StringUtil.StringMatrix;

public class StaffListBean
{
    int nDoViewOrEditDeveloper;
    StringMatrix DeveloperList = null;
    String[] arrDelete = null;
    String[] arrGroup = null;
    String selectedGroup;
    ////////
    private String status = null;
    private String strSortBy = null;
    private String strDirection = null;
    private String strAccount = null;
    private String strName = null;
    private int n_PageNumber = 0;
    private int n_TotalRecords = 0;
    
    private StringMatrix StatusList = null;
    ////////
    public int getDoViewOrEditDeveloper()
    {
        return nDoViewOrEditDeveloper;
    }
    public void setDoViewOrEditDeveloper(int inData)
    {
        nDoViewOrEditDeveloper = inData;
    }
    public String getSelectedGroup()
    {
        return selectedGroup;
    }
    public void setSelectedGroup(String selectedGroup)
    {
        this.selectedGroup = selectedGroup;
    }
    ///////////////////
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public StringMatrix getStatusList()
    {
        return StatusList;
    }
    public void setStatusList(StringMatrix StatusList)
    {
        this.StatusList = StatusList;
    }
    //////////////////
    public StringMatrix getDeveloperList()
    {
        return DeveloperList;
    }
    public void setDeveloperList(StringMatrix DeveloperList)
    {
        this.DeveloperList = DeveloperList;
    }
    public void setArrGroup(String[] arrGroup)
    {
        this.arrGroup = arrGroup;
    }
    public String[] getArrGroup()
    {
        return arrGroup;
    }
    public String[] getArrDelete()
    {
        return arrDelete;
    }
    public void setArrDelete(String[] arrDelete)
    {
        this.arrDelete = arrDelete;
    }
    /**
     * @return
     */
    public String getDirection() {
        return strDirection;
    }

    /**
     * @return
     */
    public String getSortBy() {
        return strSortBy;
    }

    /**
     * @param string
     */
    public void setDirection(String string) {
        strDirection = string;
    }

    /**
     * @param string
     */
    public void setSortBy(String string) {
        strSortBy = string;
    }

    /**
     * @return
     */
    public String getAccount() {
        return strAccount;
    }

    /**
     * @return
     */
    public String getName() {
        return strName;
    }

    /**
     * @param string
     */
    public void setAccount(String string) {
        strAccount = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        strName = string;
    }

    /**
     * @return
     */
    public int getPageNumber() {
        return n_PageNumber;
    }

    /**
     * @param i
     */
    public void setPageNumber(int i) {
        n_PageNumber = i;
    }

    /**
     * @return
     */
    public int getTotalRecords() {
        return n_TotalRecords;
    }

    /**
     * @param i
     */
    public void setTotalRecords(int i) {
        n_TotalRecords = i;
    }

}