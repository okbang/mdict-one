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

import java.text.DecimalFormat;
import java.util.Calendar;

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.bo.ComboBox.ProjectComboBO;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.util.DateUtil.DateUtil;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.util.CommonFunction;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WeeklyReportBean implements DefinitionList {

    public static final String STR_CONCATTER 			= " to ";

	//Attributes
	private int intProjectID = 0x00;
	private int intReportType = 0;	
	private int[] arrYear = null;
	private int intProjectStatus = Timesheet.PROJECT_STATUS_ALL;

	//private String strGroup = "All";    // Default all groups
	private String strGroup;
	private String strPQAName;
	private String strSearchFromDate	 = null;
	private String strSearchToDate 		 = null;
    private String strFromDate			 = null;
    private String strToDate 			 = null;
    private String strFromTo 			 = null;
    private String strYear 				 = null;
    private String strLogDate	 		 = null;
    private String strCurrentTime		 = null;
	private String strDeveloperId 		 = null;
	private String strProjectCode 		 = null;
	private String strFullName 	 		 = null;
	private String strAccount 	  		 = null;
	private String strLackTimeSheetTitle = null;
	private String strLackTimeSheetList  = null;
	private String strProjectIDs = "";  //contain project IDs separated by commas

	private StringMatrix smtPQANameList  = null;
	private StringMatrix smtGroupList 	 = null;
	private StringMatrix smtProjectList  = null;
    private StringMatrix smtReportList 	 = null;
    private StringMatrix smtWeeks 		 = null;

//    private StringMatrix smtLackTimeSheetList = null;
	private StringMatrix smtProjectStatusList = null;

	private Calendar m_CreateDate = Calendar.getInstance();
	private DecimalFormat decimalFormatter = new DecimalFormat("00");

    /**
     * @see java.lang.Object#Object()
     */
    public WeeklyReportBean() {
        //strGroup = "All";
        arrYear = CommonFunction.getYearsListing();
    }

	/**
	 * Method getProject.
	 * @return intProjectID
	 */
	public int getProject() {
		return intProjectID;
	}

	/**
	 * Method setProject.
	 * @param intProjectID
	 */
	public void setProject(int intProjectID) {
		this.intProjectID = intProjectID;
	}

	/**
	 * Method getProjectStatus
	 * @return intProjectStatus
	 */
	public int getProjectStatus() {
		return intProjectStatus;
	}

	/**
	 * Method setProjectStatus
	 * @param intProjectStatus
	 */
	public void setProjectStatus(int intProjectStatus) {
		this.intProjectStatus = intProjectStatus;
	}

	/**
	 * Method getProjectStatusList
	 * @return smtProjectStatusList
	 */
	public StringMatrix getProjectStatusList() {
		return smtProjectStatusList;
	}

	/**
	 * Method setProjectStatusList
	 * @param smtProjectStatusList
	 */
	public void setProjectStatusList(StringMatrix smtProjectStatusList) {
		this.smtProjectStatusList = smtProjectStatusList;
	}

	/**
	 * Method setProjectStatusList
	 * Get project status list
	 * @param strRole
	 * @param intUserId
	 */
	public void setProjectStatusList(String strRole, int intUserId) {
		ProjectComboBO pc = new ProjectComboBO();
		this.smtProjectStatusList = pc.getProjectStatusComboList(strRole, intUserId);
	}

	/**
	 * Method getLackTimeSheetTitle
	 * @return strLackTimeSheetTitle
	 */
	public String getLackTimeSheetTitle() {
		return strLackTimeSheetTitle;
	}
		
	/**
	 * Method setLackTimeSheetTitle
	 * @param strLackTimeSheetTitle
	 */
	public void setLackTimeSheetTitle(String strLackTimeSheetTitle) {
		this.strLackTimeSheetTitle = strLackTimeSheetTitle;
	}

	/**
	 * Method getLackTimeSheetList
	 * @return strLackTimeSheetList
	 */
	public String getLackTimeSheetList() {
		return strLackTimeSheetList;
	}

	/**
	 * Method setLackTimeSheetList
	 * @param strLackTimeSheetList
	 */
	public void setLackTimeSheetList(String strLackTimeSheetList) {
		this.strLackTimeSheetList = strLackTimeSheetList;
	}

	/**
	 * Method getFullName
	 * @return strFullName
	 */
	public String getFullName() {
		return strFullName;
	}

	/**
	 * Method setFullName
	 * @param strFullName
	 */
	public void setFullName(String strFullName) {
		this.strFullName = strFullName;
	}
	
	/**
	 * Method getAccount
	 * @return strAccount
	 */
	public String getAccount() {
		return strAccount;
	}

	/**
	 * Method setAccount
	 * @param strAccount
	 */
	public void setAccount(String strAccount) {
		this.strAccount = strAccount;
	}

	/**
	 * Method getDeveloperId
	 * @return strDeveloperId
	 */
	public String getDeveloperId() {
		return strDeveloperId;
	}

	/**
	 * Method setDeveloperId
	 * @param strDeveloperId
	 */
	public void setDeveloperId(String strDeveloperId) {
		this.strDeveloperId = strDeveloperId;
	}

	/**
	 * Method getProjectCode
	 * @return strProjectCode
	 */
	public String getProjectCode() {
		return strProjectCode;
	}

	/**
	 * Method setProjectCode
	 * @param strProjectCode
	 */
	public void setProjectCode(String strProjectCode) {
		this.strProjectCode = strProjectCode;
	}	
		
	/**
     * Method getSearchFromDate.
     * @return String
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
        }
    }

    /**
     * Method getLogDate 
	 * @return strLogDate
	 */
	public String getLogDate() {
    	if (strLogDate == null) {
    		strLogDate = DateUtil.getCurrentDate(0);
    	}
    	return strLogDate;
    }

    /**
     * Method setLogDate
	 * @param strLogDate
	 */
	public void setLogDate(String strLogDate) {
    	if (strLogDate != null) {
			this.strLogDate = strLogDate;
    	}
    }

	/**
	 * Method getCreateTime
	 * @return
	 */
	public String getLogTime() {
		if (m_CreateDate != null) {
			return decimalFormatter.format(m_CreateDate.get(Calendar.HOUR_OF_DAY)) +
				   ":" +
				   decimalFormatter.format(m_CreateDate.get(Calendar.MINUTE));
		}
		return null;
	}

	/**
	 * Method setCreateTime
	 * Sets the Create time.
	 * @param strTime The Time to set
	 */
	public void setLogTime(String strTime) {
		setCalendarTime(m_CreateDate, strTime);
	}

	/**
	 * Sets Time to calendar variable.
	 * @param cal The Calendar
	 * @param strTime The Time to set (format: HH:mm)
	 */
	private void setCalendarTime(Calendar cal, String strTime) {
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

	/**
	 * Method getCurrentTime
	 * @return strCurrentTime
	 */
	public String getCurrentTime() {
		if (strCurrentTime == null) {
			strCurrentTime = DateUtil.getCurrentTime();
		}
		return strCurrentTime;
	}

	/**
	 * Method setCurrentTime
	 * @param inData
	 */
	public void setCurrentTime(String strCurrentTime) {
		if (strCurrentTime != null) {
			this.strCurrentTime = strCurrentTime;
		}
	}

    /**
     * Method getFromDate.
     * @return String
     */
    public String getFromDate() {
        if (strFromDate == null) {
            this.setYear(this.strYear);
            strFromDate = CommonFunction.getWeeklyFromDate(Integer.parseInt(strYear));
        }
        return strFromDate;
    }

    /**
     * Method setFromDate.
     * @param intData
     */
    public void setFromDate(String intData) {
        if (intData != null) {
            strFromDate = intData.trim();
        }
    }

    /**
     * Method getToDate.
     * @return strToDate
     */
    public String getToDate() {
        if (strToDate == null) {
            this.setYear(this.strYear);
            strToDate = CommonFunction.getWeeklyToDate(Integer.parseInt(strYear));
        }
        return strToDate;
    }

    /**
     * Method setToDate.
     * @param strToDate
     */
    public void setToDate(String strToDate) {
        if (strToDate != null) {
			strToDate = strToDate.trim();
        }
    }

    /**
     * Method getFromTo.
     * @return strFromTo
     */
    public String getFromTo() {
        if (strFromTo == null) {
            strFromTo = this.getFromDate() + STR_CONCATTER + this.getToDate();
        }
        return strFromTo;
    }

	/**
     * Method setFromTo.
     * Set From Date - To Date string (MM/dd/yy ... MM/dd/yy)
     * From Date must begin at first position To Date is the last string
     * @param strIn_FromTo
     */
    public void setFromTo(String strIn_FromTo) {
        int nDateLength = TIMESHEET.DATE_FORMAT.length();
        if ((strIn_FromTo != null) && (strIn_FromTo.length() > 2 * nDateLength)) {
            int nFromToLength = strIn_FromTo.length();

            this.strFromTo = strIn_FromTo;
            this.strFromDate = strIn_FromTo.substring(0, nDateLength);
            this.strToDate = strIn_FromTo.substring(nFromToLength - nDateLength, nFromToLength);
        }
        else {
            this.strFromTo = getFromDate() + STR_CONCATTER + getToDate();
        }
    }

    /**
     * Method getYear.
     * @return String
     */
    public String getYear() {
        return strYear;
    }

    /**
     * Method setYear.
     * @param intData
     */
    public void setYear(String intData) {
        Integer intYear = new Integer(Calendar.getInstance().get(Calendar.YEAR));
        if ((intData == null) || (intData.length() <= 0)) {
            strYear = intYear.toString();
        }
        else if (Integer.parseInt(intData) < TIMESHEET.TS_START_YEAR) {
            strYear = intYear.toString();
        }
        else {
            strYear = intData;
        }
    }

    /**
     * Method getYearsListing.
     * @return int[]
     */
    public int[] getYearsListing() {
        return this.arrYear;
    }

    /**
     * Method setYearsListing.
     * @param arrYearsList
     */
    public void setYearsListing(int[] arrYearsList) {
        this.arrYear = arrYearsList;
    }

    /**
     * Method getWeeksList.
     * @return StringMatrix
     */
    public StringMatrix getWeeksList() {
        return this.smtWeeks;
    }

    /**
     * Method setWeeksList.
     * @param strInYear
     */
    public void setWeeksList(String strInYear) {
        String strOldYear = this.getYear();
        //Already have weeks list or not
        if ((strInYear != this.getYear()) || (smtWeeks == null)) {
            this.setYear(strInYear);
            this.smtWeeks = CommonFunction.getWeeksListing(this.getYear());

            //Reset From Date and To Date if year was changed
            if (strOldYear != this.getYear()) {
                this.setFromTo(smtWeeks.getCell(0, 0) + STR_CONCATTER + smtWeeks.getCell(0, 1));
            }
        }
    }

    /**
     * Method getGroup.
     * @return strGroup
     */
    public String getGroup() {
        return strGroup;
    }

    /**
     * Method setGroup.
     * @param strGroup
     */
    public void setGroup(String strGroup) {
        if (strGroup != null) {
            this.strGroup = strGroup;
        }
    }

	/**
	 * Method getGroupList
	 * @return smtGroupList
	 */
	public StringMatrix getGroupList() {
		return smtGroupList;
	}

	/**
	 * Method setGroupList
	 * @param smtGroupList
	 */
	public void setGroupList(StringMatrix smtGroupList) {
		this.smtGroupList = smtGroupList;
	}

	/**
	 * Method setGroupList
	 */
	public void setGroupList() {
	    CommonListBO boCommonList = new CommonListBO();
		this.smtGroupList = boCommonList.getGroupList();
	}

	public String getPQAName() {
		return strPQAName;
	}

	/**
	 * Method setPQAName.
	 * @param strPQAName
	 */
	public void setPQAName(String strPQAName) {
		if (strPQAName != null) {
			this.strPQAName = strPQAName;
		}
	}

	/**
	 * Method getPQANameList
	 * @return smtPQANameList
	 */
	public StringMatrix getPQANameList() {
		return this.smtPQANameList;
	}

	/**
	 * Method setPQANameList
	 * @param smtPQANameList
	 */
	public void setPQANameList(StringMatrix smtPQANameList) {
		this.smtPQANameList = smtPQANameList;
	}

	/**
	  * Method setPQANameList. 
	  */
	public void setPQANameList() throws Exception {
		CommonListBO cmlBO = new CommonListBO();
		this.smtPQANameList = cmlBO.getPQANameList();
	}

    /**
     * Method getReportType.
     * @return intReportType
     */
    public int getReportType() {
        return intReportType;
    }

    /**
     * Method setReportType.
     * @param intData
     */
    public void setReportType(int intData) {
        intReportType = intData;
    }

    /**
     * Method getReportList.
     * @return smtReportList
     */
    public StringMatrix getReportList() {
        return smtReportList;
    }

    /**
     * Method setReportList.
     * @param List
     * @return boolean
     */
    public boolean setReportList(StringMatrix List) {
        if (List != null) {
            this.smtReportList = new StringMatrix(List.toString());
            return true;
        }
        return false;
    }

	/**
	 * Method getProjectList.
	 * @return smtProjectList
	 */
	public StringMatrix getProjectList() {
		return smtProjectList;
	}

	/**
	 * Method setProjectList.
	 * @param strRole
	 * @param intUserId
	 */
	public void setProjectList(String strRole, int intUserId, int intStatus) {
		ProjectComboBO boProjectCombo = new ProjectComboBO();
		this.smtProjectList = boProjectCombo.getProjectComboList(strRole, intUserId, intStatus);

		if (this.strProjectIDs.length() < 2) {
			this.strProjectIDs = this.smtProjectList.getCell(0, 0);
		}
	}

	/**
	  * Method setProjectList.
	  * @param strRole
	  * @param intUserId
	  * @param intPageType
	*/
	public void setProjectList(String strRole, int intUserId, int intPageType, int intProjectStatus) {
		ProjectComboBO pc = new ProjectComboBO();
		this.smtProjectList = pc.getProjectComboList(strRole, intUserId, intPageType, intProjectStatus);

		if (this.strProjectIDs.length() < 2) {
			this.strProjectIDs = this.smtProjectList.getCell(0, 0);
		}
	}

	/**
	  * Method setProjectList.
	  * @param strRole
	  * @param intUserId
	  * @param intPageType
	  */
	public void setProjectListAll(String strRole, int intUserId, int intPageType) {
	    ProjectComboBO pc = new ProjectComboBO();
		this.smtProjectList = pc.getProjectComboList(strRole, intUserId, intPageType, LIST_ALL_PROJECT);

		if (this.strProjectIDs.length() < 2) {
			this.strProjectIDs = this.smtProjectList.getCell(0, 0);
		}
	}

    /**
     * Method setArrayOfProjectIDs.
     * @param strProjectIDs
     */
    public void setArrayOfProjectIDs(String strProjectIDs) {
        this.strProjectIDs = strProjectIDs;
    }

    /**
     * Method getArrayOfProjectIDs.
     * @return String
     */
    public String getArrayOfProjectIDs() {
        return this.strProjectIDs;
    }
}