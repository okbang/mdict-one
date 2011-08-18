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

//import java.util.List;

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

public class QAUpdateBean implements DefinitionList {
    // for syncronize the name
    private StringMatrix mtxProcessList = null;
    private StringMatrix mtxTypeList = null;
    private StringMatrix mtxProductList = null;
    private StringMatrix mtxKpaList = null;
    int kindPage = 0;
    // For Display
    private StringMatrix mtxTimesheetList = null;
    // Store update data
    private String strAction;
    private StringMatrix mtxUpdateList = null; // 6, num

    /**
     * Method getUpdateList.
     * @return StringMatrix
     */
    public StringMatrix getUpdateList() {
        return this.mtxUpdateList;
    }

    /**
     * Method setUpdateList.
     * @param List
     * @return boolean
     */
    public boolean setUpdateList(StringMatrix List) {
        if (List != null) {
            this.mtxUpdateList = new StringMatrix(List.toString());
            return true;
        }
        return false;
    }
    /**
     * @see java.lang.Object#Object()
     */
    // Contructor
    public QAUpdateBean() {
        this.strAction = "";
        CommonListBO cmlRef = new CommonListBO();
        this.mtxProcessList = cmlRef.getProcessList();
        this.mtxTypeList = cmlRef.getTypeOfWorkList();
        this.mtxProductList = cmlRef.getProductList();
        this.mtxKpaList = cmlRef.getKpaList();
    }
    /**
     * Method setAction.
     * @param str
     */
    // update action
    public void setAction(String str) {
        this.strAction = str;
    }

    /**
     * Method getAction.
     * @return String
     */
    public String getAction() {
        return this.strAction;
    }
    /**
     * Method getProcessList.
     * @return StringMatrix
     */
    // Process List
    public StringMatrix getProcessList() {
        return this.mtxProcessList;
    }
    /**
     * Method getTypeList.
     * @return StringMatrix
     */
    // Type List
    public StringMatrix getTypeList() {
        return this.mtxTypeList;
    }
    /**
     * Method getProductList.
     * @return StringMatrix
     */
    // Product List
    public StringMatrix getProductList() {
        return this.mtxProductList;
    }
    /**
     * Method getKpaList.
     * @return StringMatrix
     */
    // Kpa List
    public StringMatrix getKpaList() {
        return this.mtxKpaList;
    }
    /**
     * Method getTimesheetList.
     * @return StringMatrix
     */
    /////////////////////////////////////////////
    public StringMatrix getTimesheetList() {
        return this.mtxTimesheetList;
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
     * Method getKindPage.
     * @return int
     */
    public int getKindPage() {
        return kindPage;
    }

    /**
     * Method setKindPage.
     * @param kindPage
     */
    public void setKindPage(int kindPage) {
        this.kindPage = kindPage;
    }
}