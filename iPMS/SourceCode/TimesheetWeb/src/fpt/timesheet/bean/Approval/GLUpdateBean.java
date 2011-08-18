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

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

public class GLUpdateBean implements DefinitionList {

    // For Syncronize the name
    private StringMatrix mtxProcessList = null;
    private StringMatrix mtxTypeList = null;
    private StringMatrix mtxProductList = null;

    // For Display
    private StringMatrix mtxTimesheetList = null;

    // Store Update data
    private String strAction;
    private StringMatrix mtxUpdateList = null; // 6, num

	public GLUpdateBean() {
		this.strAction = "";
		CommonListBO cmlRef = new CommonListBO();
		this.mtxProcessList = cmlRef.getProcessList();
		this.mtxTypeList = cmlRef.getTypeOfWorkList();
		this.mtxProductList = cmlRef.getProductList();
	}

    /**
     * Method getUpdateList.
     * @return mtxUpdateList
     */
    public StringMatrix getUpdateList() {
        return mtxUpdateList;
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
     * Method setAction.
     * @param strAction
     */
    public void setAction(String strAction) {
        this.strAction = strAction;
    }

    /**
     * Method getAction.
     * @return strAction
     */
    public String getAction() {
        return strAction;
    }

    /**
     * Method getProcessList.
     * @return mtxProcessList
     */
    public StringMatrix getProcessList() {
        return mtxProcessList;
    }

    /**
     * Method getTypeList.
     * @return mtxTypeList
     */
    public StringMatrix getTypeList() {
        return mtxTypeList;
    }

    /**
     * Method getProductList.
     * @return mtxProductList
     */
    public StringMatrix getProductList() {
        return mtxProductList;
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

}