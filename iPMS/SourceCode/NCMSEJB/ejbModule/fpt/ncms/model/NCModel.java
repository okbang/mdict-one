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
 
 package fpt.ncms.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import fpt.ncms.constant.NCMS;

/**
 * @author trungtn
 * @since 2003-December-27
 */
public class NCModel extends Object implements Serializable, Cloneable {
    /*
     * Integer values:
     *      <= 0: Null value
     * (Repeat value:
     *      < 0: Null value) */
    private int n_NCID;
    private int n_NCLevel;
    private String strProjectID;
    private int n_NCType;
    private int n_DetectedBy;
    private String strCode;
    private String strDescription;
    private String strCreator;
    private Calendar m_CreateDate = Calendar.getInstance();
    private int n_Status;
    private int n_TypeOfCause;
    private String strCause;
    private int n_Process;
    private String strImpact;
    private int n_TypeOfAction;
    private String strCPAction;
    private String strAssignee;
    private Calendar m_DeadLine = null;
    private int n_Repeat = -1;
	private String strEffectOfChange;
    private String strNote;
    private Calendar m_ClosureDate = null;
    private String strReviewer;
    private int n_KPA;
    private int n_ISOClause;
    private String strGroupName;
    private Calendar m_ReviewDate = null;
    private int n_Priority;
	private int prjStatus;
    
    // Date format
    //private String strDateFormat = NCMS.DATE_FORMAT;
    private SimpleDateFormat formatter = new SimpleDateFormat(NCMS.DATE_FORMAT);
    private DecimalFormat decimalFormatter = new DecimalFormat("00");
        
    /**
     * Returns a copy of object
     * @return NCModel
     */
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Returns the closureDate.
     * @return Calendar
     */
    public Calendar getClosureDate() {
        return m_ClosureDate;
    }

    /**
     * Returns the closureDate.
     * @return String
     */
    public String getClosureDateString() {
        return this.format(m_ClosureDate);
    }

    /**
     * Returns the time string of closureDate.
     * @return String
     */
    public String getClosureTime() {
        if (m_ClosureDate != null) {
            return decimalFormatter.format(m_ClosureDate.get(Calendar.HOUR_OF_DAY)) +
                   ":" +
                   decimalFormatter.format(m_ClosureDate.get(Calendar.MINUTE));
        }
        return null;
    }

    /**
     * Returns the createDate.
     * @return Calendar
     */
    public Calendar getCreateDate() {
        return m_CreateDate;
    }

    /**
     * Returns the createDate.
     * @return String
     */
    public String getCreateDateString() {
        return this.format(m_CreateDate);
    }

    /**
     * Returns the time string of CreateDate.
     * @return String
     */
    public String getCreateTime() {
        if (m_CreateDate != null) {
            return decimalFormatter.format(m_CreateDate.get(Calendar.HOUR_OF_DAY)) +
                   ":" +
                   decimalFormatter.format(m_CreateDate.get(Calendar.MINUTE));
        }
        return null;
    }

    /**
     * Returns the deadLine.
     * @return Calendar
     */
    public Calendar getDeadLine() {
        return m_DeadLine;
    }

    /**
     * Returns the deadLine.
     * @return String
     */
    public String getDeadLineString() {
        return this.format(m_DeadLine);
    }

    /**
     * Returns the time string of Deadline.
     * @return String
     */
    public String getDeadLineTime() {
        if (m_DeadLine != null) {
            return decimalFormatter.format(m_DeadLine.get(Calendar.HOUR_OF_DAY)) +
                   ":" +
                   decimalFormatter.format(m_DeadLine.get(Calendar.MINUTE));
        }
        return null;
    }

    /**
     * Returns the reviewDate.
     * @return Calendar
     */
    public Calendar getReviewDate() {
        return m_ReviewDate;
    }

    /**
     * Returns the reviewDate.
     * @return String
     */
    public String getReviewDateString() {
        return this.format(m_ReviewDate);
    }

    /**
     * Returns the time string of Review Date.
     * @return String
     */
    public String getReviewTime() {
        if (m_ReviewDate != null) {
            return decimalFormatter.format(m_ReviewDate.get(Calendar.HOUR_OF_DAY)) +
                   ":" +
                   decimalFormatter.format(m_ReviewDate.get(Calendar.MINUTE));
        }
        return null;
    }

    /**
     * Returns the detectedBy.
     * @return int
     */
    public int getDetectedBy() {
        return n_DetectedBy;
    }

    /**
     * Returns the iSOClause.
     * @return int
     */
    public int getISOClause() {
        return n_ISOClause;
    }

    /**
     * Returns the kPA.
     * @return int
     */
    public int getKPA() {
        return n_KPA;
    }

    /**
     * Returns the nCID.
     * @return int
     */
    public int getNCID() {
        return n_NCID;
    }

    /**
     * Returns the nCLevel.
     * @return int
     */
    public int getNCLevel() {
        return n_NCLevel;
    }

    /**
     * Returns the nCType.
     * @return int
     */
    public int getNCType() {
        return n_NCType;
    }

    /**
     * Returns the process.
     * @return int
     */
    public int getProcess() {
        return n_Process;
    }

    /**
     * Returns the repeat.
     * @return int
     */
    public int getRepeat() {
        return n_Repeat;
    }

    /**
     * Returns the status.
     * @return int
     */
    public int getStatus() {
        return n_Status;
    }

    /**
     * Returns the typeOfAction.
     * @return int
     */
    public int getTypeOfAction() {
        return n_TypeOfAction;
    }

    /**
     * Returns the typeOfCause.
     * @return int
     */
    public int getTypeOfCause() {
        return n_TypeOfCause;
    }

    /**
     * Returns the assignee.
     * @return String
     */
    public String getAssignee() {
        return strAssignee;
    }

    /**
     * Returns the cause.
     * @return String
     */
    public String getCause() {
        return strCause;
    }

    /**
     * Returns the code.
     * @return String
     */
    public String getCode() {
        return strCode;
    }

    /**
     * Returns the cPAction.
     * @return String
     */
    public String getCPAction() {
        return strCPAction;
    }

    /**
     * Returns the creator.
     * @return String
     */
    public String getCreator() {
        return strCreator;
    }

    /**
     * Returns the description.
     * @return String
     */
    public String getDescription() {
        return strDescription;
    }

    /**
     * Returns the groupName.
     * @return String
     */
    public String getGroupName() {
        return strGroupName;
    }

    /**
     * Returns the impact.
     * @return String
     */
    public String getImpact() {
        return strImpact;
    }

	/**
	 * Returns the effect of change.
	 * @return String
	 */
	public String getEffectOfChange() {
		return strEffectOfChange;
	}
    
    /**
     * Returns the note.
     * @return String
     */
    public String getNote() {
        return strNote;
    }

    /**
     * Returns the projectID.
     * @return String
     */
    public String getProjectID() {
        return strProjectID;
    }

    /**
     * Returns the viewer.
     * @return String
     */
    public String getReviewer() {
        return strReviewer;
    }

    /**
     * Sets the closureDate.
     * @param closureDate The closureDate to set
     */
    public void setClosureDate(Calendar closureDate) {
        m_ClosureDate = closureDate;
    }

    /**
     * Sets the closureDate.
     * @param closureDate The closureDate to set
     */
    public void setClosureDate(Date closureDate) {
        if (closureDate != null) {
            if (m_ClosureDate == null) {
                m_ClosureDate = Calendar.getInstance();
            }
            m_ClosureDate.setTime(closureDate);
        }
        else {
            m_ClosureDate = null;
        }
    }

    /**
     * Sets the closureDate.
     * @param strClosureDate The closureDate to set
     */
    public void setClosureDate(String strClosureDate) {
        if (this.parseDate(strClosureDate) != null) {
            if (m_ClosureDate == null) {
                m_ClosureDate = Calendar.getInstance();
            }
            m_ClosureDate.setTime(this.parseDate(strClosureDate));
        }
        else {
            m_ClosureDate = null;
        }
    }

    /**
     * Sets the closureTime.
     * @param strTime The Time to set
     */
    public void setClosureTime(String strTime) {
        setTime(m_ClosureDate, strTime);
    }

    /**
     * Sets the createDate.
     * @param createDate The createDate to set
     */
    public void setCreateDate(Calendar createDate) {
        m_CreateDate = createDate;
    }

    /**
     * Sets the createDate.
     * @param createDate The createDate to set
     */
    public void setCreateDate(Date createDate) {
        if (createDate != null) {
            if (m_CreateDate == null) {
                m_CreateDate = Calendar.getInstance();
            }
            m_CreateDate.setTime(createDate);
        }
        else {
            m_CreateDate = null;
        }
    }

    /**
     * Sets the createDate.
     * @param strCreateDate The createDate to set
     */
    public void setCreateDate(String strCreateDate) {
        if (m_CreateDate == null) {
            m_CreateDate = Calendar.getInstance();
        }
        m_CreateDate.setTime(this.parseDate(strCreateDate));
    }

    /**
     * Sets the Create time.
     * @param strTime The Time to set
     */
    public void setCreateTime(String strTime) {
        setTime(m_CreateDate, strTime);
    }

    /**
     * Sets the deadLine.
     * @param deadLine The deadLine to set
     */
    public void setDeadLine(Calendar deadLine) {
        m_DeadLine = deadLine;
    }

    /**
     * Sets the deadLine.
     * @param deadLine The deadLine to set
     */
    public void setDeadLine(Date deadLine) {
        if (deadLine != null) {
            if (m_DeadLine == null) {
                m_DeadLine = Calendar.getInstance();
            }
            m_DeadLine.setTime(deadLine);
        }
        else {
            m_DeadLine = null;
        }
    }

    /**
     * Sets the deadLine.
     * @param strDeadLine The deadLine to set
     */
    public void setDeadLine(String strDeadLine) {
        if (this.parseDate(strDeadLine) != null) {
            if (m_DeadLine == null) {
                m_DeadLine = Calendar.getInstance();
            }
            m_DeadLine.setTime(this.parseDate(strDeadLine));
        }
        else {
            m_DeadLine = null;
        }
    }

    /**
     * Sets the DeadLine time.
     * @param strTime The Time to set
     */
    public void setDeadLineTime(String strTime) {
        setTime(m_DeadLine, strTime);
    }

    /**
     * Sets the reviewDate.
     * @param reviewDate The reviewDate to set
     */
    public void setReviewDate(Calendar reviewDate) {
        m_ReviewDate = reviewDate;
    }

    /**
     * Sets the reviewDate.
     * @param reviewDate The reviewDate to set
     */
    public void setReviewDate(Date reviewDate) {
        if (reviewDate != null) {
            if (m_ReviewDate == null) {
                m_ReviewDate = Calendar.getInstance();
            }
            m_ReviewDate.setTime(reviewDate);
        }
        else {
            m_ReviewDate = null;
        }
    }

    /**
     * Sets the reviewDate.
     * @param strReviewDate The reviewDate to set
     */
    public void setReviewDate(String strReviewDate) {
        if (this.parseDate(strReviewDate) != null) {
            if (m_ReviewDate == null) {
                m_ReviewDate = Calendar.getInstance();
            }
            m_ReviewDate.setTime(this.parseDate(strReviewDate));
        }
        else {
            m_ReviewDate = null;
        }
    }

    /**
     * Sets the Review time.
     * @param strTime The Time to set
     */
    public void setReviewTime(String strTime) {
        setTime(m_ReviewDate, strTime);
    }

    /**
     * Sets the detectedBy.
     * @param detectedBy The detectedBy to set
     */
    public void setDetectedBy(int detectedBy) {
        n_DetectedBy = detectedBy;
    }

    /**
     * Sets the detectedBy.
     * @param strData The detectedBy to set
     */
    public void setDetectedBy(String strData) {
        n_DetectedBy = this.parseInt(strData);
    }

    /**
     * Sets the iSOClause.
     * @param iSOClause The iSOClause to set
     */
    public void setISOClause(int nISOClause) {
        n_ISOClause = nISOClause;
    }

    /**
     * Sets the iSOClause.
     * @param strData The iSOClause to set
     */
    public void setISOClause(String strData) {
        n_ISOClause = this.parseInt(strData);
    }

    /**
     * Sets the kPA.
     * @param kPA The kPA to set
     */
    public void setKPA(int nKPA) {
        n_KPA = nKPA;
    }

    /**
     * Sets the kPA.
     * @param strData The kPA to set
     */
    public void setKPA(String strData) {
        n_KPA = this.parseInt(strData);
    }

    /**
     * Sets the nCID.
     * @param nCID The nCID to set
     */
    public void setNCID(int nNCID) {
        n_NCID = nNCID;
    }

    /**
     * Sets the nCID.
     * @param strData The nCID to set
     * @throws NumberFormatException 
     */
    public void setNCID(String strData) throws NumberFormatException {
        n_NCID = Integer.parseInt(strData);
    }

    /**
     * Sets the nCLevel.
     * @param nCLevel The nCLevel to set
     */
    public void setNCLevel(int nNCLevel) {
        n_NCLevel = nNCLevel;
    }

    /**
     * Sets the nCLevel.
     * @param strData The nCLevel to set
     * @throws NumberFormatException Beacause this value cannot nullable
     */
    public void setNCLevel(String strData) throws NumberFormatException {
        n_NCLevel = this.parseInt(strData);
    }

    /**
     * Sets the nCType.
     * @param nCType The nCType to set
     */
    public void setNCType(int nNCType) {
        n_NCType = nNCType;
    }

    /**
     * Sets the nCType.
     * @param nCType The nCType to set
     * @throws NumberFormatException 
     */
    public void setNCType(String strData) throws NumberFormatException {
        n_NCType = this.parseInt(strData);
    }

    /**
     * Sets the process.
     * @param process The process to set
     */
    public void setProcess(int process) {
        n_Process = process;
    }

    /**
     * Sets the process.
     * @param process The process to set
     */
    public void setProcess(String strData) {
        n_Process = this.parseInt(strData);
    }

    /**
     * Sets the repeat.
     * @param repeat The repeat to set
     */
    public void setRepeat(int repeat) {
        n_Repeat = repeat;
    }

    /**
     * Sets the repeat.
     * @param repeat The repeat to set
     */
    public void setRepeat(String strData) {
        try {
            if ((strData != null) && (!"".equals(strData))) {
                n_Repeat = Integer.parseInt(strData);
            }
            else {
                n_Repeat = -1;
            }
        }
        catch (Exception e) {
            n_Repeat = -1;
        }
    }

    /**
     * Sets the status.
     * @param status The status to set
     */
    public void setStatus(int status) {
        n_Status = status;
    }

    /**
     * Sets the status.
     * @param status The status to set
     */
    public void setStatus(String strData) {
        n_Status = this.parseInt(strData);
    }

    /**
     * Sets the typeOfAction.
     * @param typeOfAction The typeOfAction to set
     */
    public void setTypeOfAction(int typeOfAction) {
        n_TypeOfAction = typeOfAction;
    }

    /**
     * Sets the typeOfAction.
     * @param typeOfAction The typeOfAction to set
     */
    public void setTypeOfAction(String strData) {
        n_TypeOfAction = this.parseInt(strData);
    }

    /**
     * Sets the typeOfCause.
     * @param typeOfCause The typeOfCause to set
     */
    public void setTypeOfCause(int typeOfCause) {
        n_TypeOfCause = typeOfCause;
    }

    /**
     * Sets the typeOfCause.
     * @param typeOfCause The typeOfCause to set
     */
    public void setTypeOfCause(String strData) {
        n_TypeOfCause = this.parseInt(strData);
    }

    /**
     * Sets the assignee.
     * @param assignee The assignee to set
     */
    public void setAssignee(String assignee) {
        strAssignee = assignee;
    }

    /**
     * Sets the cause.
     * @param cause The cause to set
     */
    public void setCause(String cause) {
        strCause = cause;
    }

    /**
     * Sets the code.
     * @param code The code to set
     */
    public void setCode(String code) {
        strCode = code;
    }

    /**
     * Sets the cPAction.
     * @param cPAction The cPAction to set
     */
    public void setCPAction(String CPAction) {
        strCPAction = CPAction;
    }

    /**
     * Sets the creator.
     * @param creator The creator to set
     */
    public void setCreator(String creator) {
        strCreator = creator;
    }

    /**
     * Sets the description.
     * @param description The description to set
     */
    public void setDescription(String description) {
        strDescription = description;
    }

    /**
     * Sets the groupName.
     * @param groupName The groupName to set
     */
    public void setGroupName(String groupName) {
        strGroupName = groupName;
    }

    /**
     * Sets the impact.
     * @param impact The impact to set
     */
    public void setImpact(String impact) {
        strImpact = impact;
    }

	/**
	 * Sets the effect of change.
	 * @param effectofchange The effectofchange to set
	 */
	public void setEffectOfChange(String effectofchange) {
		strEffectOfChange = effectofchange;
	}
		
    /**
     * Sets the note.
     * @param note The note to set
     */
    public void setNote(String note) {
        strNote = note;
    }

    /**
     * Sets the projectID.
     * @param projectID The projectID to set
     */
    public void setProjectID(String projectID) {
        strProjectID = projectID;
    }

    /**
     * Sets the viewer.
     * @param viewer The viewer to set
     */
    public void setReviewer(String reviewer) {
        strReviewer = reviewer;
    }

    /**
     * Method parseInt.
     * @param strData String to be parsed
     * @return int
     */
    private int parseInt(String strData) {
        try {
            if ((strData != null) && (!"".equals(strData))) {
                return Integer.parseInt(strData);
            }
            else {
                return 0;
            }
        }
        catch (Exception e) {
            return 0;
        }
    }

    /**
     * Method format.
     * @param datData Date to be formated
     * @return Date
     */
    private String format(Calendar calData) {
        String strResult = null;
        try {
            if (calData != null) {
                strResult = formatter.format(calData.getTime());
            }
        }
        catch (Exception e) {
            strResult = null;
        }
        return strResult;
    }

    /**
     * Method format.
     * @param datData Date to be formated
     * @param strFormat The format string specified.
     * @return Date
     */
    private String format(Calendar calData, String strFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
        String strResult = null;
        try {
            if (calData != null) {
                strResult = formatter.format(calData.getTime());
            }
        }
        catch (Exception e) {
            strResult = null;
        }
        return strResult;
    }

    /**
     * Method parseDate.
     * @param strData The string to be parsed
     * @return Date
     */
    private Date parseDate(String strData) {
        Date date = null;
        try {
            if ( (strData != null) && (!"".equals(strData)) ) {
                // Use the class's formatter
                date = formatter.parse(strData);
            }
        }
        catch (Exception e) {
            date = null;
        }
        return date;
    }

    /**
     * Sets Time to calendar variable.
     * @param cal The Calendar
     * @param strTime The Time to set (format: HH:mm)
     */
    private void setTime(Calendar cal, String strTime) {
        try {
            if ((cal != null) && (strTime != null)) {
                int nHour = 0;
                int nMinute = 0;
                int nPos = strTime.indexOf(":");
                // ":" string is not presents or at first position
                if (nPos <= 0) {
                    return;
                }
                else {
                    String strHour = strTime.substring(0, nPos);
                    String strMinute = strTime.substring(nPos + 1);
                    if ((strHour == null) || (strMinute == null) ||
                        (strHour.length() <= 0) || (strMinute.length() <= 0))
                    {
                        return;
                    }
                    nHour = Integer.parseInt(strHour);
                    nMinute = Integer.parseInt(strMinute);
                    if ((nHour < 0) || (nHour > 23) ||
                        (nMinute < 0) || (nMinute > 59))
                    {
                        return;
                    }
                    
                    cal.set(Calendar.HOUR_OF_DAY, nHour);
                    cal.set(Calendar.MINUTE, nMinute);
                }
            }
        }
        catch (NumberFormatException e) {
        }
    }

    /*
     * Method parseDate.
     * @param strData The string to be parsed
     * @param strFormat The format string specified
     * @return Date
    private Date parseDate(String strData, String strFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
        Date date = null;
        try {
            if ( (strData != null) && (!"".equals(strData)) ) {
                // Use the new formatter
                date = formatter.parse(strData);
            }
        }
        catch (Exception e) {
            date = null;
        }
        return date;
    }
    
    /*
     * Returns the dateFormat.
     * @return String
    public String getDateFormat() {
        return strDateFormat;
    }

    /**
     * Sets the dateFormat.
     * @param dateFormat The dateFormat to set
    public void setDateFormat(String dateFormat) {
        strDateFormat = dateFormat;
        formatter.applyPattern(strDateFormat);
    }
     */

    /**
     * Returns the priority.
     * @return int
     */
    public int getPriority() {
        return n_Priority;
    }
    
    /**
     * Sets the priority.
     * @param priority The priority to set
     */
    public void setPriority(int priority) {
        n_Priority = priority;
    }
    
	//added by LamNT3
	public int getPrjStatus() {
		return prjStatus;
	}
	
	public void setPrjStatus(int prjStatus) {
		this.prjStatus = prjStatus;
	}
		
	public void setPrjStatus(String strData) {
		this.prjStatus = this.parseInt(strData);
	}	
    /**
     * Sets the priority.
     * @param priority The priority to set
     */
    public void setPriority(String strData) {
        n_Priority = this.parseInt(strData);
    }

}
