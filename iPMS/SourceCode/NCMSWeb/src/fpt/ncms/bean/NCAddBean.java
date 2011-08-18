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
 * @(#)NCAddBean.java 19-Mar-03
 */


package fpt.ncms.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fpt.ncms.constant.NCMS;
import fpt.ncms.model.NCModel;
import fpt.ncms.util.StringUtil.StringMatrix;

/**
 * Class NCAddBean
 * Bean object for storing NC information
 * @version 1.0 19-Mar-03
 * @author
 */
public class NCAddBean {
    /*
    private String strNCID = "";
    private String strNCLevel = null;
    /** Default id. for super project General 
    private String strProjectID = "General";
    private String strNCType = null;
    private String strDetectedBy = null;
    private String strCode = null;
    private String strDescription = null;
    private String strCreator = null;
    private String strCreationDate = null;
    /** Default status for newly added NC is Opened 
    private String strStatus = NCMS.NC_OPENED;
    private String strTypeOfCause = null;
    private String strCause = null;
    private String strProcess = null;
    private String strImpact = null;
    private String strTypeOfAction = null;
    private String strCPAction = null;
    private String strAssignee = null;
    private String strDeadLine = null;
    private String strRepeat = NCMS.NC_NONREPEAT;
    private String strNote = null;
    private String strClosureDate = null;
    private String strReviewer = null;
    private String strKPA = null;
    private String strISOClause = null;
    /** Default group is General 
    private String strGroupName = "General";
    */
    
    /** Flag for field enable */
    //private String strFieldEnable = "000000000000000000000000";
    /** Tab index for form control */
    private int nTabIndex = 0;
    /** relation between user role andNC status */
//    private int nViewNC = NCMS.CREATOR_ADD;

    /** combo list */
    private StringMatrix smComboAssignee;
    private StringMatrix smComboTypeOfAction;
    private StringMatrix smComboStatus;
    private StringMatrix smComboLevel;
    private StringMatrix smComboProcess;
    private StringMatrix smComboGroup;
    private StringMatrix smComboDetectedBy;
    private StringMatrix smComboProject;
    private StringMatrix smComboTypeOfNC;
    private StringMatrix smComboISOClause;
    private StringMatrix smComboKPA;
    private StringMatrix smComboTypeOfCause;
    private StringMatrix smComboPriority;

    private NCModel m_NCModel;
    private boolean b_DBError;      // Previous DB access is error
    // Format to get current date string
    private SimpleDateFormat formatter = new SimpleDateFormat(NCMS.DATE_FORMAT);
    private String strHistory = "";
    
    /*
     * getNCID
     * Get NC ident.number
     * @return  NC ident.number
     
    public final String getNCID() {
        return strNCID;
    }

    /**
     * setNCID
     * Set NC ident.numbers
     * @param   inData - NC ident.number
     
    public final void setNCID(String inData) {
        strNCID = inData;
    }

    /**
     * getNCLevel
     * Get NC level
     * @return  NC level
     
    public final String getNCLevel() {
        return (strNCLevel == null ? "" : strNCLevel);
    }

    /**
     * setNCLevel
     * Set NC level
     * @param   inData - NC level
     
    public final void setNCLevel(String inData) {
        if ((inData != null) && (!"".equals(inData))) {
            strNCLevel = inData;
        }
    }

    /**
     * getProjectID
     * Get Project ID
     * @return  Project ID
     
    public final String getProjectID() {
        return strProjectID;
    }

    /**
     * setProjectID
     * Set Project ID
     * @param   inData - Project ID
     
    public final void setProjectID(String inData) {
        if (inData != null) {
            strProjectID = inData;
        }
    }

    /**
     * getNCType
     * Get NC type
     * @return  NC type
     
    public final String getNCType() {
        return (strNCType == null ? "" : strNCType);
    }

    /**
     * setNCType
     * set NC type
     * @param   inData - NC type
     
    public final void setNCType(String inData) {
//        strNCType = ((inData == null) || ("".equals(inData))) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strNCType = inData;
        }
    }

    /**
     * getDetectedBy
     * Get Detected By
     * @return  Detected By
     
    public final String getDetectedBy() {
        return (strDetectedBy == null ? "" : strDetectedBy);
    }

    /**
     * setDetectedBy
     * Set Detected By
     * @param   inData - Detected By
     
    public final void setDetectedBy(String inData) {
//        strDetectedBy = ((inData == null) || ("".equals(inData)))
//                ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strDetectedBy = inData;
        }
    }

    /**
     * getCode
     * Get code
     * @return  code
     
    public final String getCode() {
        return (strCode == null) ? "" : strCode;
    }

    /**
     * setCode
     * Set code
     * @param   inData - code
     
    public final void setCode(String inData) {
        if ((inData != null) && (!"".equals(inData))) {
            strCode = inData;
        }
//        strCode = ((inData == null) || ("".equals(inData))) ? null : inData;
    }

    /**
     * getDescription
     * Get description
     * @return  description
     
    public final String getDescription() {
        return (strDescription == null ? "" : strDescription);
    }

    /**
     * setDescription
     * Set description
     * @param   inData - description
     
    public final void setDescription(String inData) {
        strDescription = ((inData == null) || ("".equals(inData)))
                ? null : inData;
    }

    /**
     * getCreator
     * Get creator
     * @return  creator
     
    public final String getCreator() {
        return (strCreator == null ? "" : strCreator);
    }

    /**
     * setCreator
     * Set creator
     * @param   inData - creator
     
    public final void setCreator(String inData) {
//        strCreator = ((inData == null) || ("".equals(inData))) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strCreator = inData;
        }
    }

    /**
     * getCreationDate
     * Get creation date
     * @return  creation date
     
    public final String getCreationDate() {
        return (strCreationDate == null ? "" : strCreationDate);
    }

    /**
     * setCreationDate
     * Set creation date
     * @paraminData  - creation date
     
    public final void setCreationDate(String inData) {
//        strCreationDate = ((inData == null) || ("".equals(inData)))
//                ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strCreationDate = inData;
        }
    }

    /**
     * getStatus
     * Get status
     * @return  status
     
    public final String getStatus() {
        return strStatus;
    }

    /**
     * setStatus
     * Set status
     * @param   inData - status
     
    public final void setStatus(String inData) {
        if (inData != null) {
            strStatus = inData;
        }
    }

    /**
     * getTyepOfCause
     * Get type of cause
     * @return  type of cause
     
    public final String getTypeOfCause() {
        return (strTypeOfCause == null ? "" : strTypeOfCause);
    }

    /**
     * setTypeOfCause
     * Set type of cause
     * @param   inData - type of cause
     
    public final void setTypeOfCause(String inData) {
//        strTypeOfCause = ((inData == null) || ("".equals(inData)))
//                ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strTypeOfCause = inData;
        }
    }

    /**
     * getCause
     * Get cause
     * @return  cause
     
    public final String getCause() {
        return (strCause == null ? "" : strCause);
    }

    /**
     * setCause
     * Set cause
     * @param   inData  - cause
     
    public final void setCause(String inData) {
//        strCause = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strCause = inData;
        }
    }

    /**
     * getProcess
     * Get process
     * @return  process
     
    public final String getProcess() {
        return (strProcess == null ? "" : strProcess);
    }

    /**
     * setProcess
     * Set process
     * @param   inData - process
     
    public final void setProcess(String inData) {
//        strProcess = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strProcess = inData;
        }
    }

    /**
     * getImpact
     * Get impact
     * @return  impact
     
    public final String getImpact() {
        return (strImpact == null ? "" : strImpact);
    }

    /**
     * setImpact
     * Set impact
     * @param   inData - impact
     
    public final void setImpact(String inData) {
//        strImpact = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strImpact = inData;
        }
    }

    /**
     * getTypeOfAction
     * Get type of action
     * @return  type of action
     
    public final String getTypeOfAction() {
        return (strTypeOfAction == null ? "" : strTypeOfAction);
    }

    /**
     * setTypeOfAction
     * Set type of action
     * @param   inData - type of action
     
    public final void setTypeOfAction(String inData) {
        if ((inData != null) && (!"".equals(inData))) {
            strTypeOfAction = inData;
        }
//        strTypeOfAction = ("".equals(inData) || (inData == null))
//                ? null : inData;
    }

    /**
     * getCPAction
     * Get CP Action
     * @return  CP Action
     
    public final String getCPAction() {
        return (strCPAction == null ? "" : strCPAction);
    }

    /**
     * setCPAction
     * Set CP Action
     * @param   inData - CP Action
     
    public final void setCPAction(String inData) {
//        strCPAction = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strCPAction = inData;
        }
    }

    /**
     * getAssignee
     * Get assignee
     * @return  assignee
     
    public final String getAssignee() {
        return (strAssignee == null ? "" : strAssignee);
    }

    /**
     * setAssignee
     * Set assignee
     * @param   inData - assignee
     
    public final void setAssignee(String inData) {
//        strAssignee = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strAssignee = inData;
        }
    }

    /**
     * getDeadLine
     * Get deadline
     * @return  deadline
     
    public final String getDeadLine() {
        return (strDeadLine == null ? "" : strDeadLine);
    }

    /**
     * setDeadLine
     * Set deadline
     * @param   inData  - deadline
     
    public final void setDeadLine(String inData) {
//        strDeadLine = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strDeadLine = inData;
        }
    }

    /**
     * getRepeat
     * Get repeat
     * @return  repeat
     
    public final String getRepeat() {
        return strRepeat;
    }

    /**
     * setRepeat
     * Set repeat
     * @param   inData - repeat
     
    public final void setRepeat(String inData) {
//        strRepeat = inData;
        if ((inData != null) && (!"".equals(inData))) {
            strRepeat = inData;
        }
    }

    /**
     * getNote
     * Get note
     * @return  note
     
    public final String getNote() {
        return (strNote == null ? "" : strNote);
    }

    /**
     * setNote
     * Set note
     * @param   inData - note
     
    public final void setNote(String inData) {
//        strNote = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strNote = inData;
        }
    }

    /**
     * getClosureDate
     * Get closure date
     * @return  closure date
     
    public final String getClosureDate() {
        return (strClosureDate == null ? "" : strClosureDate);
    }

    /**
     * setClosureDate
     * Set closure date
     * @param   inData - closure date
     
    public final void setClosureDate(String inData) {
//        strClosureDate = ("".equals(inData) || (inData == null))
//                ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strClosureDate = inData;
        }
    }

    /**
     * getReviewer
     * Get reviewer
     * @return  reviewer
     
    public final String getReviewer() {
        return (strReviewer == null ? "" : strReviewer);
    }

    /**
     * setReviewer
     * Set reviewer
     * @param   inData - reviewer
     
    public final void setReviewer(String inData) {
//        strReviewer = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strReviewer = inData;
        }
    }

    /**
     * getKPA
     * Get KPA
     * @return  KPA
     
    public final String getKPA() {
        return (strKPA == null ? "" : strKPA);
    }

    /**
     * setKPA
     * Set KPA
     * @param   inData - KPA
     
    public final void setKPA(String inData) {
//        strKPA = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strKPA = inData;
        }
    }

    /**
     * getISOClause
     * Get ISO Clause
     * @return  ISO Clause
     
    public final String getISOClause() {
        return (strISOClause == null ? "" : strISOClause);
    }

    /**
     * setISOClause
     * Set ISO Clause
     * @param   inData - ISO Clause
     
    public final void setISOClause(String inData) {
//        strISOClause = ("".equals(inData) || (inData == null)) ? null : inData;
        if ((inData != null) && (!"".equals(inData))) {
            strISOClause = inData;
        }
    }

    /**
     * getGroupName
     * Get group name
     * @return  group name
     
    public final String getGroupName() {
        return strGroupName;
    }

    /**
     * setGroupName
     * Set group name
     * @param   inData - group name
     
    public final void setGroupName(String inData) {
        if ((inData != null) && (!"".equals(inData))) {
            strGroupName = inData;
        }
//        strGroupName = inData;
    }
*/
    /*
     * getFieldEnable
     * Get field enable flag
     * @return  field enable flag
    public final String getFieldEnable() {
        return strFieldEnable;
    }

    /**
     * setFieldEnable
     * Set field enable flag
     * @param   inData - field enable flag
    public final void setFieldEnable(String inData) {
        strFieldEnable = inData;
    }
     */

    /*
     * getViewNC
     * Get relation between NC status and user's role
     * @return  relation
    public final int getViewNC() {
        return nViewNC;
    }

    /**
     * setViewNC
     * Set relation between NC status and user's role
     * @param   inData - view number
    public final void setViewNC(int inData) {
        nViewNC = inData;
    }
     */

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
     * Get combo type of action
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
        return smComboISOClause;
    }

    /**
     * setComboISOClause
     * Set combo ISO Clause
     * @param   inData - combo ISO Clause
     */
    public final void setComboISOClause(StringMatrix inData) {
        smComboISOClause = inData;
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
     * getComboPriority
     * Get combo Priority
     * @return  combo Priority
     */
    public final StringMatrix getComboPriority() {
        return smComboPriority;
    }

    /**
     * setComboPriority
     * Set combo Priority
     * @param   inData - combo Priority
     */
    public final void setComboPriority(StringMatrix inData) {
        smComboPriority = inData;
    }

    /*
     * visibility
     * Make visibility and add tab index for form control
     * @param   nPosition - control position
     * @param   nType - type of control:
     *          (1 - radio button or select option, 0 - other)
     * @return  visibility string
    public final String visibility(int nPosition, int nType) {
        String strVisibility;
        if (strFieldEnable.charAt(nPosition - 1) == '1') {
            if (nType == 0) {
                strVisibility = "readonly style=\"BACKGROUND-COLOR: #eeebf7\"";
            }
            else {
                strVisibility = "disabled style=\"BACKGROUND-COLOR: "
                        + "#eeebf7;color:#000000\"";
            }
        }
        else {
            nTabIndex++;
            strVisibility = "tabindex=\"" + nTabIndex + "\"";
        }
        return strVisibility;
    }
     */
    
    /**
     * Returns the nCModel.
     * @return NCModel
     */
    public NCModel getNCModel() {
        return m_NCModel;
    }

    /**
     * Sets the nCModel.
     * @param nCModel The nCModel to set
     */
    public void setNCModel(NCModel ncModel) {
        m_NCModel = ncModel;
    }

    /**
     * Returns the DBError.
     * @return boolean
     */
    public boolean isDBError() {
        return b_DBError;
    }

    /**
     * Sets the DBError.
     * @param dbError The DBError to set
     */
    public void setDBError(boolean dbError) {
        b_DBError = dbError;
    }

    /**
     * Returns the date string.
     * @return Current date
     */
    public String getCurrentDate() {
        return formatter.format(Calendar.getInstance().getTime());
    }

    /**
     * Returns the current time in HH:mm.
     * @return Current time
     */
    public String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        java.text.DecimalFormat formatter =
                new java.text.DecimalFormat("00");
        
        return formatter.format(cal.get(Calendar.HOUR_OF_DAY)) +
               ":" +
               formatter.format(cal.get(Calendar.MINUTE));
    }

    /**
     * Returns the history.
     * @return String
     */
    public String getHistory() {
        return strHistory;
    }

    /**
     * Sets the history.
     * @param history The history to set
     */
    public void setHistory(String history) {
        strHistory = history;
    }

}