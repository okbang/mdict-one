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

public class MappingDetailBean {

    //This class is also used for adding timesheets
    private StringMatrix smtProcessList = null;
    private StringMatrix smtWorkProductList = null;
    private StringMatrix smtCurrentWorkProductList = null;
    private StringMatrix smtMappingList = null;  //All mapping

	private int intSavingResult = 0;
	private int arrNumberOfRelateWP[];
	private String strCurrentProcessID = DATA.PROCESS_NOTHING;
	private String strCurrentProcessName = "";
	private String strCurrentState = "Updating";

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
     * Method setWorkProductList.
     * @param smtWorkProductList
     */
    public void setWorkProductList(StringMatrix smtWorkProductList) {
        this.smtWorkProductList = smtWorkProductList;
    }

    /**
     * Method getWorkProductList.
     * @return smtWorkProductList
     */
    public StringMatrix getWorkProductList() {
        return smtWorkProductList;
    }

    /**
     * Method setCurrentWorkProductList.
     * @param smInData
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
     * Method setMappingList.
     * @param smInData
     */
    public void setMappingList(StringMatrix smtInData) {
        smtMappingList = smtInData;
        if (smtProcessList != null) {
            int intLength = smtProcessList.getNumberOfRows();
            arrNumberOfRelateWP = new int[intLength];
            for (int i = 0; i < intLength; i++) {
                String strProcessID = smtProcessList.getCell(i, 0); //strProcessID != null must be confirmed
                for (int j = 0; j < smtMappingList.getNumberOfRows(); j++) {
                    if (strProcessID.equals(smtMappingList.getCell(j, 0))) {
                        arrNumberOfRelateWP[i]++;
                    }
                }
            }
        }
    }

    /**
     * Method getRelativeMapping.
     * @return arrNumberOfRelateWP
     */
    public int[] getRelativeMapping() {
        return arrNumberOfRelateWP;
    }

    /**
     * Method getMappingList.
     * @return smtMappingList
     */
    public StringMatrix getMappingList() {
        return smtMappingList;
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

    /**
     * Method setSavingResult.
     * @param intSavingResult
     */
    public void setSavingResult(int intSavingResult) {
        this.intSavingResult = intSavingResult;
    }

    /**
     * Method getSavingResult.
     * @return intSavingResult
     */
    public int getSavingResult() {
        return intSavingResult;
    }

    /**
     * Method setCurrentState.
     * @param strCurrentState
     */
    public void setCurrentState(String strCurrentState) {
        this.strCurrentState = strCurrentState;
    }

    /**
     * Method getCurrentState.
     * @return strCurrentState
     */
    public String getCurrentState() {
        return strCurrentState;
    }

}
