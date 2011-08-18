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
 
 package fpt.dashboard.bean.ResourceManagement;

import java.util.ArrayList;
import java.util.Calendar;

import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.util.CommonUtil.InfoList;
import fpt.dashboard.util.DateUtil.CalendarUtil;

public class ResourceProjectBean
{
    // Values from input values
    private String strGroupName;
    private int n_FromYear;
    private int n_FromMonth;
    private int n_ToYear;
    private int n_ToMonth;
    private Calendar cal_FromCalendar;
    private Calendar cal_ToCalendar;
    
    private ArrayList m_GroupsList;     // Combo box
    private int n_groupDevelopers;
    private int[] arr_GroupSum;
    private int[] arr_GroupAllocated;
    private int[] arr_GroupTentative;
    private int n_maxDevelopers;
    
    private ArrayList m_WeeksList;      // List of Calendar objects
    private ArrayList m_ProjectsList;   // Running projects of group
    private ArrayList m_AssignmentsList;// Assignments between a time period of group
    private ArrayList m_MonthsList;     // List of Calendar objects
    private InfoList m_ReportList;      // Report list
    
    private int n_FirstWeekDays;
    private int n_LastWeekDays;
    
    public ResourceProjectBean() {
        // Default range of month is 6 months from current month
        Calendar cal = Calendar.getInstance();
        n_FromYear = cal.get(Calendar.YEAR);
        n_FromMonth = cal.get(Calendar.MONTH) + 1;
        cal.add(Calendar.MONTH, 5);
        n_ToYear = cal.get(Calendar.YEAR);
        n_ToMonth = cal.get(Calendar.MONTH) + 1;
        
        m_GroupsList = new ArrayList();
        m_WeeksList = new ArrayList();
        m_ProjectsList = new ArrayList();
        strGroupName = Constants.ALL;
    }

    /**
     * @return
     */
    public int getFirstWeekDays() {
        return n_FirstWeekDays;
    }

    /**
     * @return
     */
    public int getFromMonth() {
        return n_FromMonth;
    }

    /**
     * @return
     */
    public int getFromYear() {
        return n_FromYear;
    }

    /**
     * @return
     */
    public int getLastWeekDays() {
        return n_LastWeekDays;
    }

    /**
     * @return
     */
    public int getToMonth() {
        return n_ToMonth;
    }

    /**
     * @return
     */
    public int getToYear() {
        return n_ToYear;
    }

    /**
     * @return
     */
    public String getGroupName() {
        return strGroupName;
    }

    /**
     * @param i
     */
    public void setFirstWeekDays(int i) {
        n_FirstWeekDays = i;
    }

    /**
     * @param i
     */
    public void setFromMonth(int i) {
        n_FromMonth = i;
    }

    /**
     * @param i
     */
    public void setFromYear(int i) {
        n_FromYear = i;
    }

    /**
     * @param i
     */
    public void setLastWeekDays(int i) {
        n_LastWeekDays = i;
    }

    /**
     * @param i
     */
    public void setToMonth(int i) {
        n_ToMonth = i;
    }

    /**
     * @param i
     */
    public void setToYear(int i) {
        n_ToYear = i;
    }

    /**
     * @param string
     */
    public void setGroupName(String string) {
        strGroupName = string;
    }
    /**
     * @return
     */
    public ArrayList getAssignmentsList() {
        return m_AssignmentsList;
    }

    /**
     * @return
     */
    public ArrayList getGroupsList() {
        return m_GroupsList;
    }

    /**
     * @return
     */
    public ArrayList getProjectsList() {
        return m_ProjectsList;
    }

    /**
     * @return
     */
    public ArrayList getWeeksList() {
        return m_WeeksList;
    }

    /**
     * @param list
     */
    public void setAssignmentsList(ArrayList list) {
        m_AssignmentsList = list;
    }

    /**
     * @param list
     */
    public void setGroupsList(ArrayList list) {
        m_GroupsList = list;
    }

    /**
     * @param list
     */
    public void setProjectsList(ArrayList list) {
        m_ProjectsList = list;
    }

    /**
     * @param list
     */
    public void setWeeksList(ArrayList list) {
        m_WeeksList = list;
        if (list.size() > 0) {
            // Number of days of first week and last week
            Calendar cal = (Calendar) list.get(0);
            cal_FromCalendar = (Calendar) cal.clone();
            cal = (Calendar) list.get(list.size() - 1);
            cal_ToCalendar = (Calendar) cal.clone();
            n_FirstWeekDays = cal_FromCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            n_LastWeekDays = cal_ToCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            arr_GroupSum = new int[m_WeeksList.size()];
            arr_GroupAllocated = new int[m_WeeksList.size()];
            arr_GroupTentative = new int[m_WeeksList.size()];
        }
    }

    /**
     * Get weeks list between date period (after month/year values is setted),
     * then get months list
     */
    public void genWeeksList() {
        try {
            CalendarUtil calUtil = new CalendarUtil(); 
            m_WeeksList = calUtil.getWeeksArray(n_FromMonth, n_FromYear, n_ToMonth, n_ToYear);
            if (m_WeeksList.size() > 0) {
                // Number of days of first week and last week
                Calendar cal = (Calendar) m_WeeksList.get(0);
                cal_FromCalendar = (Calendar) cal.clone();
                cal = (Calendar) m_WeeksList.get(m_WeeksList.size() - 1);
                cal_ToCalendar = (Calendar) cal.clone();
                n_FirstWeekDays = cal_FromCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
                
                n_LastWeekDays = cal_ToCalendar.get(Calendar.DATE);
                // Go to last day of month
                cal_ToCalendar.add(Calendar.MONTH, 1);
                cal_ToCalendar.set(Calendar.DATE, 1);
                cal_ToCalendar.add(Calendar.DATE, -1);
                // Get number of days of last week
                n_LastWeekDays = cal_ToCalendar.get(Calendar.DATE) - n_LastWeekDays;
                arr_GroupSum = new int[m_WeeksList.size()];
                arr_GroupAllocated = new int[m_WeeksList.size()];
                arr_GroupTentative = new int[m_WeeksList.size()];
            }
            m_MonthsList = calUtil.getMonthsArray(n_FromMonth, n_FromYear, n_ToMonth, n_ToYear);
        }
        catch (Exception e) {
        }
    }

    /*
     * Get months list between date period (after month/year values is setted)
    public void genMonthsList() {
        try {
            CalendarUtil calUtil = new CalendarUtil(); 
            m_MonthsList = calUtil.getMonthsArray(n_FromMonth, n_FromYear, n_ToMonth, n_ToYear);
        }
        catch (Exception e) {
        }
    }
     */

    /**
     * @return
     */
    public ArrayList getMonthsList() {
        return m_MonthsList;
    }

    /**
     * @return
     */
    public InfoList getReportList() {
        return m_ReportList;
    }

    /**
     * @return
     */
    public int getGroupDevelopers() {
        return n_groupDevelopers;
    }

    /**
     * @param list
     */
    public void setMonthsList(ArrayList list) {
        m_MonthsList = list;
    }

    /**
     * @param list
     */
    public void setReportList(InfoList list) {
        m_ReportList = list;
    }

    /**
     * @param i
     */
    public void setGroupDevelopers(int i) {
        n_groupDevelopers = i;
    }

    /**
     * @return
     */
    public Calendar getFromCalendar() {
        return cal_FromCalendar;
    }

    /**
     * @return
     */
    public Calendar getToCalendar() {
        return cal_ToCalendar;
    }

    /**
     * @param calendar
     */
    public void setFromCalendar(Calendar calendar) {
        cal_FromCalendar = calendar;
    }

    /**
     * @param calendar
     */
    public void setToCalendar(Calendar calendar) {
        cal_ToCalendar = calendar;
    }

    /**
     * @return
     */
    public int[] getGroupSum() {
        return arr_GroupSum;
    }

    /**
     * @param is
     */
    public void setGroupSum(int[] is) {
        arr_GroupSum = is;
    }

    /**
     * @return
     */
    public int getMaxDevelopers() {
        return n_maxDevelopers;
    }

    /**
     * @param i
     */
    public void setMaxDevelopers(int i) {
        n_maxDevelopers = i;
    }

    /**
     * @return
     */
    public int[] getGroupAllocated() {
        return arr_GroupAllocated;
    }

    /**
     * @return
     */
    public int[] getGroupTentative() {
        return arr_GroupTentative;
    }

    /**
     * @param is
     */
    public void setGroupAllocated(int[] is) {
        arr_GroupAllocated = is;
    }

    /**
     * @param is
     */
    public void setGroupTentative(int[] is) {
        arr_GroupTentative = is;
    }

}