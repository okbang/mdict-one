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
 * @(#)NCListBean.java 17-Mar-03
 */


package fpt.ncms.bean;

import java.util.StringTokenizer;

import fpt.ncms.model.NCModel;
import fpt.ncms.util.StringUtil.StringMatrix;
import fpt.ncms.constant.NCMS;

/**
 * Class NCListBean
 * Bean object for storing list of NC
 * @version 1.0 17-Mar-03
 * @author
 */
public class NCListBean {
    /** list of NC retrived from databased */
    private StringMatrix arrNCList = null;
    /** sorting condition */
    private String strOrderBy = "CreationDate";
    // Direction:
    // 0 - descending
    // 1 - ascending
    // Default is descending by created date
    private int n_Direction = 0;
    /**
     * to avoid complication with retrieving field name and value,
     * always let field 'Status' on the first place
     */
    private String strFields
            = "Status,NCID,Description,GroupName,ProjectID,Assignee,DeadLine,CreationDate,Code,Repeat,TypeOfCause";
    private String strCondition = "";
    /** default page is the first page, no doubt */
    private int nNumPage = 1;
    /** number of record */
    private int nTotal = 0;
    /** number of column to list on screen, not rows */
    private int nNumFields = 0;

    private StringMatrix smComboCreator;
    private StringMatrix smComboReviewer;
    private StringMatrix smComboAssignee;
    private StringMatrix smComboTypeOfAction;
    private StringMatrix smComboStatus;
    private StringMatrix smComboLevel;
    private StringMatrix smComboProcess;
    private StringMatrix smComboGroup;
    private StringMatrix smComboDetectedBy;
    private StringMatrix smComboProject;
	private StringMatrix smComboProjectStatus;
    private StringMatrix smComboTypeOfNC;
    private StringMatrix smComboISOCause;
    private StringMatrix smComboKPA;
    private StringMatrix smComboTypeOfCause;
    private StringMatrix smComboPriority;
    
    private NCModel m_NCModel;  // Collects old search parameters
    private String strCurrentFromDate = "";
    private String strCurrentToDate = "";
    /*
    private String strCurrentCreator = "";
    private String strCurrentAssignee = "";
    private String strCurrentReviewer = "";
    private String strCurrentLevel = "";
    private String strCurrentTypeOfNC = "";

    private String strCurrentGroup = "";
    private String strCurrentProject = "";
    private String strCurrentProcess = "";
    private String strCurrentDetectedBy = "";
    private String strCurrentStatus = "";

    private String strCurrentKPA = "";
    private String strCurrentISOCause = "";
    private String strCurrentTypeOfCause = "";
    private String strCurrentTypeOfAction = "";


    public void setCurrentCreator(String strCreator) {
        strCurrentCreator = strCreator;
    }

    public String getCurrentCreator() {
        return strCurrentCreator;
    }

    public void setCurrentAssignee(String strAssignee) {
        this.strCurrentAssignee = strAssignee;
    }

    public String getCurrentAssignee() {
        return strCurrentAssignee;
    }
    public void setCurrentReviewer(String strReviewer) {
        strCurrentReviewer = strReviewer;
    }

    public String getCurrentReviewer() {
        return strCurrentReviewer;
    }

    public void setCurrentLevel(String strLevel) {
        strCurrentLevel = strLevel;
    }

    public String getCurrentLevel() {
        return strCurrentLevel;
    }

    public void setCurrentTypeOfNC(String strTypeOfNC) {
        strCurrentTypeOfNC = strTypeOfNC;
    }

    public String getCurrentTypeOfNC() {
        return strCurrentTypeOfNC;
    }

    public void setCurrentProcess(String strProcess) {
        strCurrentProcess = strProcess;
    }

    public String getCurrentProcess() {
        return strCurrentProcess;
    }
    public void setCurrentProject(String strProject) {
        strCurrentProject = strProject;
    }

    public String getCurrentProject() {
        return strCurrentProject;
    }
    public void setCurrentDetectedBy(String strDetectedBy) {
        strCurrentDetectedBy = strDetectedBy;
    }

    public String getCurrentDetectedBy() {
        return strCurrentDetectedBy;
    }
    public void setCurrentKPA(String strKPA) {
        strCurrentKPA = strKPA;
    }

    public String getCurrentKPA() {
        return strCurrentKPA;
    }
    public void setCurrentTypeOfCause(String strTypeOfCause) {
        strCurrentTypeOfCause = strTypeOfCause;
    }

    public String getCurrentTypeOfCause() {
        return strCurrentTypeOfCause;
    }
    public void setCurrentTypeOfAction(String strTypeOfAction) {
        strCurrentTypeOfAction = strTypeOfAction;
    }

    public String getCurrentTypeOfAction() {
        return strCurrentTypeOfAction;
    }
    public void setCurrentISOClause(String strISOCause) {
        strCurrentISOCause = strISOCause;
    }

    public String getCurrentISOClause() {
        return strCurrentISOCause;
    }
    public void setCurrentGroup(String strGroup) {
        strCurrentGroup = strGroup;
    }

    public String getCurrentGroup() {
        return strCurrentGroup;
    }

    public void setCurrentStatus(String strStatus) {
        strCurrentStatus = strStatus;
    }

    public String getCurrentStatus() {
        return strCurrentStatus;
    }
    */
    
    /**
     * getNCList
     * Get list of NC
     * @return  list of NC
     */
    public final StringMatrix getNCList() {
        return arrNCList;
    }

    /**
     * setNCList
     * Set list of NC
     * @param   inData  list of NC
     */
    public final void setNCList(StringMatrix inData) {
        arrNCList = inData;
    }

    /**
     * getOrderBy
     * Get sorting condition
     * @return  sort condition
     */
    public final String getOrderBy() {
        return strOrderBy;
    }

    /**
     * setOrderBy
     * Set sorting condition
     * @param   inData  sorting condition
     */
    public final void setOrderBy(String inData) {
        strOrderBy = inData;
    }

    /**
     * getNumPage
     * Get number of current page
     * @return  number of current page
     */
    public final int getNumPage() {
        return nNumPage;
    }

    /**
     * setNumPage
     * Set number of current page
     * @param   inData  number of page
     */
    public final void setNumPage(int inData) {
        nNumPage = inData;
    }

    /**
     * getFields
     * Get field names
     * @return  field names
     */
    public final String getFields() {
        return strFields;
    }

    /**
     * setFields
     * Set field names
     * @param   inData  field names
     */
    public final void setFields(String inData) {
        strFields = inData;
    }

    /**
     * getCondition
     * Get condition
     * @return  condition
     */
    public final String getCondition() {
        return strCondition;
    }

    /**
     * setCondition
     * Set condition
     * @param   inData  condition
     */
    public void setCondition(String inData) {
        strCondition = inData;
    }

    /**
     * getNumFields
     * Get number of fields
     * @return  number of fields
     */
    public final int getNumFields() {
        final StringTokenizer sk = new StringTokenizer(strFields, ",");
        return sk.countTokens();
    }

    /**
     * getTotal
     * Get number of records
     * @return  number of records
     */
    public final int getTotal() {
        return nTotal;
    }

    /**
     * setTotal
     * Set number of records
     * @param   inData of records
     */
    public final void setTotal(int inData) {
        nTotal = inData;
    }

    /**
     * getComboCreator
     * Get combo creator
     * @return  combo creator
     */
    public final StringMatrix getComboCreator() {
        return smComboCreator;
    }

    /**
     * setComboCreator
     * Set combo creator
     * @param   inData - combo creator
     */
    public final void setComboCreator(StringMatrix inData) {
        smComboCreator = inData;
    }

    /**
     * getComboReviewer
     * Get combo reviewer
     * @return  combo reviewer
     */
    public final StringMatrix getComboReviewer() {
        return smComboReviewer;
    }

    /**
     * setComboReviewer
     * Set combo reviewer
     * @param   inData - combo reviewer
     */
    public final void setComboReviewer(StringMatrix inData) {
        smComboReviewer = inData;
    }

    /**
     * getComboAssignee
     * Get combo assignee
     * @return  combo assignee
     */
    public final StringMatrix getComboAssignee() {
        return smComboAssignee;
    }

    /**
     * setComboAssignee
     * Set combo assignee
     * @param   inData - combo assignee
     */
    public final void setComboAssignee(StringMatrix inData) {
        smComboAssignee = inData;
    }

    /**
     * getComboTypeOfAction
     * Gcombo type of action
     * @return  combo type of action
     */
    public final StringMatrix getComboTypeOfAction() {
        return smComboTypeOfAction;
    }

    /**
     * setComboTypeOfAction
     * Set combo type of action
     * @param   inData - combo type of action
     */
    public final void setComboTypeOfAction(StringMatrix inData) {
        smComboTypeOfAction = inData;
    }

    /**
     * getComboStatus
     * Get combo status
     * @return  combo status
     */
    public final StringMatrix getComboStatus() {
        return smComboStatus;
    }

    /**
     * setComboStatus
     * Set combo status
     * @param   inData - combo status
     */
    public final void setComboStatus(StringMatrix inData) {
        smComboStatus = inData;
    }

    /**
     * getComboLevel
     * Get combo level
     * @return  combo level
     */
    public final StringMatrix getComboLevel() {
        return smComboLevel;
    }

    /**
     * setComboLevel
     * Set combo level
     * @param   inData - combo level
     */
    public final void setComboLevel(StringMatrix inData) {
        smComboLevel = inData;
    }

    /**
     * getComboProcess
     * Get combo process
     * @return  combo process
     */
    public final StringMatrix getComboProcess() {
        return smComboProcess;
    }

    /**
     * setComboProcess
     * Set combo process
     * @param   inData - combo process
     */
    public final void setComboProcess(StringMatrix inData) {
        smComboProcess = inData;
    }

    /**
     * getComboGroup
     * Get combo group
     * @return  combo group
     */
    public final StringMatrix getComboGroup() {
        return smComboGroup;
    }

    /**
     * setComboGroup
     * Set combo group
     * @param   inData - combo group
     */
    public final void setComboGroup(StringMatrix inData) {
        smComboGroup = inData;
    }

    /**
     * getComboDetectedBy
     * Get combo detected by
     * @return  combo detected by
     */
    public final StringMatrix getComboDetectedBy() {
        return smComboDetectedBy;
    }

    /**
     * setComboDetectedBy
     * Set combo detected by
     * @param   inData - combo detected by
     */
    public final void setComboDetectedBy(StringMatrix inData) {
        smComboDetectedBy = inData;
    }

    /**
     * getComboProject
     * Get combo project
     * @return  combo project
     */
    public final StringMatrix getComboProject() {
        return smComboProject;
    }

    /**
     * setComboProject
     * Set combo project
     * @param   inData - combo project
     */
    public final void setComboProject(StringMatrix inData) {
        smComboProject = inData;
    }
    
	/**
	 * setComboProjectStatus
	 * Set combo project Status
	 * @param   inData - combo project Status
	 */
	public final void setComboProjectStatus(StringMatrix inData) {
		smComboProjectStatus = inData;
	}
	/**
	 * getComboProjectStatus
	 * Get combo project
	 * @return  combo project
	 */
	public final StringMatrix getComboProjectStatus() {
		return smComboProjectStatus;
	}
    /**
     * getComboTypeOfNC
     * Get combo type of NC
     * @return  combo type of NC
     */
    public final StringMatrix getComboTypeOfNC() {
        return smComboTypeOfNC;
    }

    /**
     * setComboTypeOfNC
     * Set combo type of NC
     * @param   inData - combo type of NC
     */
    public final void setComboTypeOfNC(StringMatrix inData) {
        smComboTypeOfNC = inData;
    }

    /**
     * getComboISOClause
     * Get combo ISO Clause
     * @return  combo ISO Clause
     */
    public final StringMatrix getComboISOClause() {
        return smComboISOCause;
    }

    /**
     * setComboISOClause
     * Set combo ISO Clause
     * @param   inData - combo ISO Clause
     */
    public final void setComboISOClause(StringMatrix inData) {
        smComboISOCause = inData;
    }

    /**
     * getComboKPA
     * Get combo KPA
     * @return  combo KPA
     */
    public final StringMatrix getComboKPA() {
        return smComboKPA;
    }

    /**
     * setComboKPA
     * Set combo KPA
     * @param   inData - combo KPA
     */
    public final void setComboKPA(StringMatrix inData) {
        smComboKPA = inData;
    }

    /**
     * getComboTypeOfCause
     * Get combo type of cause
     * @return  combo type of cause
     */
    public final StringMatrix getComboTypeOfCause() {
        return smComboTypeOfCause;
    }

    /**
     * setComboTypeOfCause
     * Set combo type of cause
     * @param   inData - combo type of cause
     */
    public final void setComboTypeOfCause(StringMatrix inData) {
        smComboTypeOfCause = inData;
    }

    /**
     * getComboTypeOfCause
     * Get combo type of cause
     * @return  combo type of cause
     */
    public final StringMatrix getComboPriority() {
        return smComboPriority;
    }

    /**
     * setComboTypeOfCause
     * Set combo type of cause
     * @param   inData - combo type of cause
     */
    public final void setComboPriority(StringMatrix inData) {
        smComboPriority = inData;
    }

    private StringMatrix smCurrentSearchCondition = null;
    public void setCurrentSearchCondition(StringMatrix smInput) {
        smCurrentSearchCondition = smInput;
    }

    public StringMatrix getCurrentSearchCondition() {
        return smCurrentSearchCondition;
    }

    public String getSelected(String strFieldName, int nCol, String key) {
        String strResult = "";
        if (smCurrentSearchCondition == null) {
            return strResult;
        }
        int nRow = smCurrentSearchCondition.indexOf(strFieldName, 0);
        if (nRow != -1) {
            if (key != null) {
                if (key.equals(smCurrentSearchCondition.getCell(nRow, nCol))) {
                    strResult = " SELECTED ";
                }
            }
        }
        return strResult;
    }

    public String getFieldValue(String strFieldName) {
        if (smCurrentSearchCondition == null) {
            return "";
        }
        int nRow = smCurrentSearchCondition.indexOf(strFieldName, 0);
        if (nRow == -1) {
            return "";
        }
        else {
            return smCurrentSearchCondition.getCell(nRow, 1);
        }
    }
    
    /**
     * Returns the NCModel.
     * @return NCModel
     */
    public NCModel getNCModel() {
        return m_NCModel;
    }

    /**
     * Sets the NCModel.
     * @param ncModel The NCModel to set
     */
    public void setNCModel(NCModel ncModel) {
        m_NCModel = ncModel;
    }

    /**
     * Returns the currentFromDate.
     * @return String
     */
    public String getCurrentFromDate() {
        return strCurrentFromDate;
    }

    /**
     * Returns the currentToDate.
     * @return String
     */
    public String getCurrentToDate() {
        return strCurrentToDate;
    }

    /**
     * Sets the currentFromDate.
     * @param currentFromDate The currentFromDate to set
     */
    public void setCurrentFromDate(String currentFromDate) {
        strCurrentFromDate = currentFromDate;
    }

    /**
     * Sets the currentToDate.
     * @param currentToDate The currentToDate to set
     */
    public void setCurrentToDate(String currentToDate) {
        strCurrentToDate = currentToDate;
    }

    /**
     * @return
     */
    public int getDirection() {
        return n_Direction;
    }

    /**
     * @param i
     */
    public void setDirection(int i) {
        n_Direction = i;
    }

}