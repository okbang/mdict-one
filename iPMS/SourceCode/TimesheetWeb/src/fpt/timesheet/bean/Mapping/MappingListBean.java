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
 
 package fpt.timesheet.bean.Mapping;

import fpt.timesheet.constant.DATA;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

public class MappingListBean {

    private StringMatrix smtProcessList = null;
    private StringMatrix smtCurrentWorkProductList = null;
    private String strCurrentProcessID = DATA.PROCESS_NOTHING;
    private String strCurrentProcessName = "";

    /**
     * Method setProcessList.
     * @param smtProcessList
     */
    public void setProcessList(StringMatrix smtProcessList) {
        this.smtProcessList = smtProcessList;
    }

    /**
     * Method getProcessList.
     * @return smtProcessList
     */
    public StringMatrix getProcessList() {
        return smtProcessList;
    }

    /**
     * Method setCurrentWorkProductList.
     * @param smtCurrentWorkProductList
     */
    public void setCurrentWorkProductList(StringMatrix smtCurrentWorkProductList) {
        this.smtCurrentWorkProductList = smtCurrentWorkProductList;
    }

    /**
     * Method getCurrentWorkProductList.
     * @return smtCurrentWorkProductList
     */
    public StringMatrix getCurrentWorkProductList() {
        return smtCurrentWorkProductList;
    }

    /**
     * Method setCurrentProcessID.
     * @param strCurrentProcessID
     */
    public void setCurrentProcessID(String strCurrentProcessID) {
        this.strCurrentProcessID = strCurrentProcessID;
    }

    /**
     * Method getCurrentProcessID.
     * @return strCurrentProcessID
     */
    public String getCurrentProcessID() {
        return strCurrentProcessID;
    }

    /**
     * Method setCurrentProcessName.
     * @param strCurrentProcessName
     */
    public void setCurrentProcessName(String strCurrentProcessName) {
        this.strCurrentProcessName = strCurrentProcessName;
    }

    /**
     * Method getCurrentProcessName.
     * @return strCurrentProcessName
     */
    public String getCurrentProcessName() {
        return strCurrentProcessName;
    }

}
