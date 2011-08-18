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
 * Created on Apr 6, 2004
 *
 */
package fpt.dashboard.bo.ResourceManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

import fpt.dashboard.DeveloperManagementTran.ejb.Developer;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocal;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocalHome;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperHome;
import fpt.dashboard.ProjectManagementTran.ejb.Assignment;
import fpt.dashboard.ProjectManagementTran.ejb.AssignmentEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.AssignmentEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.AssignmentHome;
import fpt.dashboard.ProjectManagementTran.ejb.AssignmentInfo;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboardInfo;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetail;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailHome;
import fpt.dashboard.bean.ResourceManagement.ResourceProjectBean;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.util.CommonUtil.InfoList;
import fpt.dashboard.util.DateUtil.CalendarUtil;
import fpt.dashboard.constant.DB;

/**
 * @author trungtn
 *
 */
public class ResourceProjectBO {
    private static Logger logger =
        Logger.getLogger(ResourceWeekBO.class.getName());

//    private AssignmentHome assignmentHome = null;
//    private Assignment assignment = null;
//    private ProjectDetailHome projectDetailHome = null;
//    private ProjectDetail projectDetail = null;
//    private DeveloperHome developerHome = null;
//    private Developer developer = null;
	
// HaiMM =============
	private AssignmentEJBLocalHome assignmentHome = null;
	private AssignmentEJBLocal assignment = null;
	
	private ProjectDetailEJBLocalHome projectDetailHome = null;
	private ProjectDetailEJBLocal projectDetail = null;
	
	private DeveloperEJBLocalHome developerHome = null;
	private DeveloperEJBLocal developer = null;
//====================

    /*
    private static class IntegerComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((Integer)o1).intValue() - ((Integer)o2).intValue();
        }
        
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
    */
    //EJB Bean Specific methods ...
    private void getEJB() throws NamingException {
        try {
            if (projectDetailHome == null) {
                Context ic = new InitialContext();
                Object ref = ic.lookup(JNDINaming.PROJECT_DETAIL);
                projectDetailHome = (ProjectDetailEJBLocalHome)(ref);
                projectDetail = (ProjectDetailEJBLocal) projectDetailHome.create();
            }
            if (developerHome == null) {
                Context ic = new InitialContext();
                Object ref = ic.lookup(JNDINaming.DEVELOPER);
                developerHome = (DeveloperEJBLocalHome)(ref);
                developer = (DeveloperEJBLocal) developerHome.create();
            }
            if (assignmentHome == null) {
                Context ic = new InitialContext();
                Object ref = ic.lookup(JNDINaming.ASSIGNMENT_DETAIL);
                assignmentHome = (AssignmentEJBLocalHome)(ref);
                assignment = (AssignmentEJBLocal) assignmentHome.create();
            }
        } catch (NamingException ex) {
            logger.debug(
                "NamingException occurs in ResourceProjectBO.getEJB(). "
                    + ex.getResolvedName());
            throw ex;
        } catch (Exception e) {
            logger.debug(e.toString());
            e.printStackTrace();
        }
    } //getEJB

    /**
     * Get weekly resource of all projects of a group
     * @param   beanResourceProject
     * @exception   Exception    If an exception occurred.
     */
    public ResourceProjectBean getProjectResource(
                ResourceProjectBean beanResourceProject) throws Exception
    {
        try {
            getEJB();
            // Set group combo box
            beanResourceProject.setGroupsList((ArrayList) developer.selectGroup());
            // Add selection of all groups
            beanResourceProject.getGroupsList().add(0, "All");
            
            beanResourceProject.setProjectsList((ArrayList)
                    projectDetail.getOngoingList(beanResourceProject.getFromCalendar(),
                                                 beanResourceProject.getToCalendar(),
                                                 beanResourceProject.getGroupName()));
            beanResourceProject.setAssignmentsList((ArrayList)
                    assignment.getAssignmentList(beanResourceProject.getFromCalendar(),
                                                 beanResourceProject.getToCalendar(),
                                                 beanResourceProject.getGroupName()));
            beanResourceProject.setGroupDevelopers(
                    developer.countDev(beanResourceProject.getGroupName(),
                                       Integer.toString(DB.DEVELOPER_SATUS_STAFF)));
            // Generate report
            getReport(beanResourceProject);
        }
        catch (Exception e) {
            throw e;
        }
        return beanResourceProject;
    }

    /**
     * Generate report informations follow by projects and assignments
     * @param   beanResourceProject
     * @exception   Exception    If an exception occurred.
     */
    private void getReport(ResourceProjectBean beanResourceProject)
                    throws Exception
    {
        // TrungTN comment on 31-May-2005: break this function into smaller functions?
        try {
            // List of ProjectDashboardInfo
            ArrayList arrProjects = beanResourceProject.getProjectsList();
            // List of Calendar
            ArrayList arrWeeks = beanResourceProject.getWeeksList();
            // List of AssignmentInfo
            ArrayList arrAssignments = beanResourceProject.getAssignmentsList();
            InfoList report = new InfoList();
            InfoList listProject;
            InfoList listWeek;
            ProjectDashboardInfo projectInfo;
            AssignmentInfo assignmentInfo;
            Calendar calFrom;
            Calendar calTo;
            CalendarUtil calUtil = new CalendarUtil();
            // Total developers of group of all project for each week
            int[] arr_GroupSum = beanResourceProject.getGroupSum();
            int[] arr_GroupAllocated = beanResourceProject.getGroupAllocated();
            int[] arr_GroupTentative = beanResourceProject.getGroupTentative();
            int nMaxDevelopers = 0;

            // Create counters of weekly report for this group
            //IntegerComparator intCmp = new IntegerComparator();
            TreeSet weekDevelopers = new TreeSet(); //(intCmp);
            TreeSet[] developerSet = new TreeSet[arrWeeks.size()];
            TreeSet[] allocatedSet = new TreeSet[arrWeeks.size()];
            TreeSet[] tentativeSet = new TreeSet[arrWeeks.size()];
            for (int i = 0; i < developerSet.length; i++) {
                developerSet[i] = new TreeSet();    //(intCmp);
                allocatedSet[i] = new TreeSet();    //(intCmp);
                tentativeSet[i] = new TreeSet();    //(intCmp);
            }
            
            int nStart;
            for (int iProject = 0; iProject < arrProjects.size(); iProject++) {
                listProject = new InfoList();
                // A project node contains ProjectDashboardInfo, and children (for several weeks)
                projectInfo = (ProjectDashboardInfo) arrProjects.get(iProject);
                listProject.setInfo(projectInfo);
                for (int iWeek = 0; iWeek < arrWeeks.size(); iWeek++) {
                    calFrom = (Calendar) arrWeeks.get(iWeek);
                    calTo = (Calendar) calFrom.clone();
                    // Go to Saturday (weekend)
                    calTo.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    // Last week in week list may have lower than 7 days
                    // => SATURDAY of last week is in next month
                    if (calTo.get(Calendar.MONTH) > calFrom.get(Calendar.MONTH)) {
                        calTo.set(Calendar.DATE, 1);
                        calTo.add(Calendar.DATE, -1);
                    }
                    listWeek = new InfoList();
                    
                    nStart = 0;
                    // Go to assignments of project, please note that assignments list
                    // is sorted by projectID first, then assignment type (descending)
                    // and developerID. All assignments of one project are sorted continuously
                    while (nStart < arrAssignments.size()) {
                        assignmentInfo = (AssignmentInfo) arrAssignments.get(nStart);
                        if (assignmentInfo.getProjectID() == projectInfo.getProjectID()) {
                            break;
                        }
                        nStart++;
                    }
                    
                    weekDevelopers.clear();
                    // Fetch informations
                    while (nStart < arrAssignments.size()) {
                        assignmentInfo = (AssignmentInfo) arrAssignments.get(nStart);
                        // Stop at other project assignments
                        if (assignmentInfo.getProjectID() != projectInfo.getProjectID()) {
                            break;
                        }
                        if (!assignmentInfo.getBeginCalendar().after(calTo) &&
                            (!assignmentInfo.getEndCalendar().before(calFrom) ||
                             assignmentInfo.getEndCalendar() == null))
                        {
                            //listWeek.add(assignmentInfo);
                            // Map to index of assignment in assignments list
                            listWeek.add(new Integer(nStart));
                            Integer intDeveloperID = new Integer(assignmentInfo.getDeveloperID());
                            weekDevelopers.add(intDeveloperID);
                            
                            // Only calculate Allocated and Tentative assignments only
                            if ((assignmentInfo.getType() == DB.ASSIGNMENT_TYPE_ALLOCATED) ||
                                (assignmentInfo.getType() == DB.ASSIGNMENT_TYPE_TENTATIVE))
                            {   // Increase developers count of group of this week
                                developerSet[iWeek].add(intDeveloperID);
                                if (assignmentInfo.getType() == DB.ASSIGNMENT_TYPE_ALLOCATED) {
                                    allocatedSet[iWeek].add(intDeveloperID);
                                    // Remove from tentative list if new developer is added to allocated list,
                                    tentativeSet[iWeek].remove(intDeveloperID);
                                }
                                else if ( (assignmentInfo.getType() == DB.ASSIGNMENT_TYPE_TENTATIVE) &&
                                          (!allocatedSet[iWeek].contains(intDeveloperID)))
                                {
                                    tentativeSet[iWeek].add(intDeveloperID);
                                }
                            }
                        }
                        
                        nStart++;
                    }
                    arr_GroupSum[iWeek] = developerSet[iWeek].size();
                    if (arr_GroupSum[iWeek] > nMaxDevelopers) {
                        nMaxDevelopers = arr_GroupSum[iWeek];
                    }
                    arr_GroupAllocated[iWeek] = allocatedSet[iWeek].size();
                    arr_GroupTentative[iWeek] = tentativeSet[iWeek].size();
                    
                    listWeek.setInfo(new Integer(weekDevelopers.size()));
                    listProject.add(listWeek);
                }
                
                report.add(listProject);
            }
            
            beanResourceProject.setReportList(report);
            beanResourceProject.setMaxDevelopers(nMaxDevelopers);
        }
        catch(Exception e) {
            throw e;
        }
    }
}
