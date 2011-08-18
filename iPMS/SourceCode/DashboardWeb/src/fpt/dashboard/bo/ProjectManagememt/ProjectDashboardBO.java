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
 
 package fpt.dashboard.bo.ProjectManagememt;

/**
 * @Title:        ProjectDashboardBO.java
 * @Description:
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 10, 2002
 */

import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboard;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboardEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboardEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboardHome;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboardInfo;
import fpt.dashboard.bean.ProjectManagement.ProjectDashboardBean;
import fpt.dashboard.bean.ProjectManagement.ProjectDashboardListModelBean;
import fpt.dashboard.bean.ProjectManagement.ProjectDashboardModelBean;
import fpt.dashboard.constant.DATA;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.util.StringUtil.StringMatrix;

public class ProjectDashboardBO
{
//    private ProjectDashboardHome homeProjectDB = null;
//    private ProjectDashboard objProjectDB = null;

// HaiMM ==========
	private ProjectDashboardEJBLocalHome homeProjectDB = null;
	private ProjectDashboardEJBLocal objProjectDB = null;
// ================

    private static Logger logger = Logger.getLogger(ProjectDashboardBO.class.getName());

    public ProjectDashboardBO()
    {

    }

    //**************** HELPER Methods *****************
    // EJB Bean Project methods ...
    private void getEJBHome() throws NamingException
    {
        try
        {
            if (homeProjectDB == null)
            {
                Context ic = new InitialContext();
                Object ref = ic.lookup(JNDINaming.PROJECT_DASHBOARD);
                homeProjectDB = (ProjectDashboardEJBLocalHome)(ref);
            }
        }
        catch (NamingException ex)
        {
            logger.error("NamingException occurs in ProjectDashboardBO.getEJBHome(). " + ex.getResolvedName());
            throw ex;
        }
    } //getEJBHome

    private ProjectDashboardEJBLocal getEJBRemote() throws Exception
    {
        objProjectDB = (ProjectDashboardEJBLocal) homeProjectDB.create();
        return objProjectDB;
    } //getEJBRemote

    /**
     * Get project dashboard
     * @author  Nguyen Thai Son.
     * @version  November 11, 2002.
     * @param   strGroup String:    group name
     * @param   strStatus String:   project status (on-going, closed,...)
     * @param   strOrderBy String
     * @exception   Exception    If an exception occurred.
     */
	public ProjectDashboardBean getProjectDashboard(
		String strGroup,
		String strType,
		String strStatus,
        String strCategory,
        String strDeveloperId,
		int nOrderBy)
		throws Exception {
		ProjectDashboardBean beanProjectDashboard = new ProjectDashboardBean();
		ProjectDashboardListModelBean beanProjectDashboardModel =
			new ProjectDashboardListModelBean();

		//////////////////////////////////////////////////////////////////////////////////////////////////////
		//STEP 1 - Hard-code for static combo boxes
		StringMatrix statusList = new StringMatrix(5, 2);
		statusList.setCell(0, 0, DATA.PROJECT_VALUE_STATUS_ALL);
		statusList.setCell(0, 1, DATA.PROJECT_STATUS_ALL);
		statusList.setCell(1, 0, DATA.PROJECT_VALUE_STATUS_ONGOING);
		statusList.setCell(1, 1, DATA.PROJECT_STATUS_ONGOING);
		statusList.setCell(2, 0, DATA.PROJECT_VALUE_STATUS_CLOSED);
		statusList.setCell(2, 1, DATA.PROJECT_STATUS_CLOSED);
        statusList.setCell(3, 0, DATA.PROJECT_VALUE_STATUS_CANCELLED);
        statusList.setCell(3, 1, DATA.PROJECT_STATUS_CANCELLED);
        statusList.setCell(4, 0, DATA.PROJECT_VALUE_STATUS_TENTATIVE);
        statusList.setCell(4, 1, DATA.PROJECT_STATUS_TENTATIVE);
		beanProjectDashboard.setStatusList(statusList);
		beanProjectDashboard.setSelectStatus(strStatus);

		//ThaiLH
		//Type List 
		StringMatrix typeList = new StringMatrix(4, 2);
		typeList.setCell(0, 0, DATA.PROJECT_ALL);
		typeList.setCell(0, 1, DATA.PROJECT_TYPE_ALL);
		typeList.setCell(1, 0, DATA.PROJECT_EXTERNAL);
		typeList.setCell(1, 1, DATA.PROJECT_TYPE_EXTERNAL);
		typeList.setCell(2, 0, DATA.PROJECT_INTERNAL);
		typeList.setCell(2, 1, DATA.PROJECT_TYPE_INTERNAL);
		typeList.setCell(3, 0, DATA.PROJECT_PUBLIC);
		typeList.setCell(3, 1, DATA.PROJECT_TYPE_PUBLIC);

		beanProjectDashboard.setTypeList(typeList);

		beanProjectDashboard.setType(strType);
        //added by MinhPT        
        //09-Dec-03
        //for create category list
        //Category List - hard code.
        StringMatrix cateList = new StringMatrix(4, 2);
        cateList.setCell(0, 0, DATA.PROJECT_VALUE_CATEGORY_ALL);
        cateList.setCell(0, 1, DATA.PROJECT_CATEGORY_ALL);
        cateList.setCell(1, 0, DATA.PROJECT_VALUE_CATEGORY_DEVELOPMENT);
        cateList.setCell(1, 1, DATA.PROJECT_CATEGORY_DEVELOPMENT);
        cateList.setCell(2, 0, DATA.PROJECT_VALUE_CATEGORY_MAINTENANCE);
        cateList.setCell(2, 1, DATA.PROJECT_CATEGORY_MAINTENANCE);
        cateList.setCell(3, 0, DATA.PROJECT_VALUE_CATEGORY_OTHERS);
        cateList.setCell(3, 1, DATA.PROJECT_CATEGORY_OTHERS);
        beanProjectDashboard.setCategoryList(cateList);


		//End of ThaiLH

		StringMatrix orderList = new StringMatrix(5, 2);
		orderList.setCell(0, 0, "0");
		orderList.setCell(0, 1, "Status");
		orderList.setCell(1, 0, "1");
		orderList.setCell(1, 1, "Project Code");
		orderList.setCell(2, 0, "2");
		orderList.setCell(2, 1, "Project Name");
		orderList.setCell(3, 0, "3");
		orderList.setCell(3, 1, "Start Date");
		orderList.setCell(4, 0, "4");
		orderList.setCell(4, 1, "Base Finish");
		beanProjectDashboard.setOrderList(orderList);
		beanProjectDashboard.setOrderBy(nOrderBy);

		//data variables
		int type = 0;
		int orderBy = 0;
		double sOrder = 0;
		double eOrder = 0;
		String group = "";
		String status = "";
		String complete = "";
		String schedule_status;
		String effort_status;
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		try {
			//STEP 2 - Get Home and Remote interfaces
			getEJBHome();
			getEJBRemote();

			//STEP 3 -
			//Data for filter
			if (strGroup != null) {
				if (!strGroup.equals(Constants.GROUP_ALL) && (!strGroup.equals("")))
					group = strGroup;
				else
					group = Constants.GROUP_ALL;
			} else {
				group = Constants.GROUP_ALL;
			}
			orderBy = nOrderBy;

			//FILL GROUP COMBOBOX
			String strAll = Constants.GROUP_ALL;
			beanProjectDashboardModel.addGroup(strAll); //First is Select All
			String[] groupList = getGroupList();

			if (groupList != null) {
				for (int i = 0; i < groupList.length; i++) {
					beanProjectDashboardModel.addGroup(groupList[i]);
				}
			}
			beanProjectDashboard.setGroupName(group);

			status = strStatus;

			//FILL PROJECT DASHBOARD LIST******************
			Collection listProjects =
				objProjectDB.getProjectDashboard(
					strType,
					group,
					status,
                    strCategory,
                    strDeveloperId,
					orderBy);
			java.lang.Object[] arrProjects = listProjects.toArray();

			for (int j = 0; j < arrProjects.length; j++) {
				ProjectDashboardInfo project =
					(ProjectDashboardInfo) arrProjects[j];

				ProjectDashboardModelBean projectModel =
					new ProjectDashboardModelBean();
				projectModel.setID(project.getID()); //ID
				projectModel.setCode(project.getCode()); //CODE
				projectModel.setName(project.getName()); //NAME

				int i = 0;
				//PER COMPLETE
				i = project.getPer_complete();
				if (i == 0)
					complete =
						"<nobr><IMG src = \"images/0.bmp\"> " + i + "%</nobr>";
				else if (i > 0 && i <= 25)
					complete =
						"<nobr><IMG src = \"images/1.bmp\"> " + i + "%</nobr>";
				else if (i > 25 && i < 75)
					complete =
						"<nobr><IMG src = \"images/2.bmp\"> " + i + "%</nobr>";
				else if (i >= 75 && i < 100)
					complete =
						"<nobr><IMG src = \"images/3.bmp\"> " + i + "%</nobr>";
				else if (i == 100)
					complete =
						"<nobr><IMG src = \"images/4.bmp\"> " + i + "%</nobr>";
				projectModel.setComplete(complete); //COMPLETE

				//SCHEDULE STATUS
				float fi = project.getSchedule_status();
				if (fi < 0.8) {
					schedule_status = "<IMG src = \"images/x.gif\">";
					sOrder = 1;
				} else if (fi >= 0.8 && fi <= 1.1) {
					schedule_status = "<IMG src = \"images/v.gif\">";
					sOrder = 2;
				} else {
					schedule_status = "<IMG src = \"images/d.gif\">";
					sOrder = 3;
				}
				projectModel.setScheduleStatus(schedule_status);
				//SCHEDULE STATUS

				//EFFORT STATUS
				if (project.getPer_complete() > 0) {
					fi = project.getEffort_status();
					if (fi < 0.8) {
						effort_status = "<IMG src = \"images/x.gif\">";
						eOrder = 1;
					} else if (fi >= 0.8 && fi < 1.1) {
						effort_status = "<IMG src = \"images/v.gif\">";
						eOrder = 2;
					} else {
						effort_status = "<IMG src = \"images/d.gif\">";
						eOrder = 3;
					}
				} else {
					effort_status = "";
					eOrder = 1.2;
					sOrder = 1.2;
				}
				projectModel.setEffortStatus(effort_status); //EFFORT STATUS
				projectModel.setStartDate(project.getStart_date());
				//START DATE
				projectModel.setBaseFinishDate(project.getBase_finish());
				//BASE FINISH DATE
				projectModel.setPlanFinishDate(project.getPlan_finish());
				//PLAN FINISH DATE
				projectModel.setActualFinishDate(project.getActual_finish());
				//ACTUAL FINISH DATE
				projectModel.setBaseEffort(project.getBase_effort());
				//BASE EFFORT
				projectModel.setPlanEffort(project.getPlan_effort());
				//PLAN EFFORT
				projectModel.setActualEffort(project.getActual_effort());
				//ACTUAL EFFORT
				projectModel.setListOrder(sOrder * eOrder);

				beanProjectDashboardModel.addProject(projectModel);
			} //end for

			////////////////////////////////////////////////////////////////
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (ClassCastException ex) {
			logger.debug(
				"ClassCastException occurs in ProjectDashoardBO.getProjectDashboard(): "
					+ ex.toString());
			ex.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in ProjectDashoardBO.getProjectDashboard(): "
					+ ex.toString());
			logger.error(ex);
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//STEP 4 - Put all object into user-view bean
		//Group Combo
		int nNumOfGroups = beanProjectDashboardModel.getNumberOfGroups();
		StringMatrix groupList = new StringMatrix(nNumOfGroups, 2);
		for (int i = 0; i < nNumOfGroups; i++) {
			groupList.setCell(i, 0, beanProjectDashboardModel.getGroup(i));
		}
		beanProjectDashboard.setGroupList(groupList);

		//Project List
		int nNumberOfProjects = beanProjectDashboardModel.getNumberOfElements();
		StringMatrix projectList = new StringMatrix(nNumberOfProjects, 14);

		for (int i = 0; i < nNumberOfProjects; i++) {
			projectList.setCell(
				i,
				0,
				beanProjectDashboardModel.getProject(i).getID());
			projectList.setCell(
				i,
				1,
				beanProjectDashboardModel.getProject(i).getCode());
			projectList.setCell(
				i,
				2,
				beanProjectDashboardModel.getProject(i).getName());
			projectList.setCell(
				i,
				3,
				beanProjectDashboardModel.getProject(i).getComplete());
			projectList.setCell(
				i,
				4,
				beanProjectDashboardModel.getProject(i).getScheduleStatus());
			projectList.setCell(
				i,
				5,
				beanProjectDashboardModel.getProject(i).getEffortStatus());
			projectList.setCell(
				i,
				6,
				beanProjectDashboardModel.getProject(i).getStartDate());
			projectList.setCell(
				i,
				7,
				beanProjectDashboardModel.getProject(i).getBaseFinishDate());
			projectList.setCell(
				i,
				8,
				beanProjectDashboardModel.getProject(i).getPlanFinishDate());
			projectList.setCell(
				i,
				9,
				beanProjectDashboardModel.getProject(i).getActualFinishDate());
			if ("0"
				.equals(
					beanProjectDashboardModel.getProject(i).getBaseEffort())) {
				projectList.setCell(i, 10, "");
			} else {
				projectList.setCell(
					i,
					10,
					beanProjectDashboardModel.getProject(i).getBaseEffort());
			}
			if ("0"
				.equals(
					beanProjectDashboardModel.getProject(i).getPlanEffort())) {
				projectList.setCell(i, 11, "");
			} else {
				projectList.setCell(
					i,
					11,
					beanProjectDashboardModel.getProject(i).getPlanEffort());
			}
			if ("0"
				.equals(
					beanProjectDashboardModel
						.getProject(i)
						.getActualEffort())) {
				projectList.setCell(i, 12, "");
			} else {
				projectList.setCell(
					i,
					12,
					beanProjectDashboardModel.getProject(i).getActualEffort());
			}
			projectList.setCell(
				i,
				13,
				String.valueOf(
					beanProjectDashboardModel.getProject(i).getListOrder()));
		}
		beanProjectDashboardModel.setOrderBy(nOrderBy);

		if (beanProjectDashboardModel.getOrderBy() == 0) {
			projectList.sortByFloatColumn(13);
		}
		beanProjectDashboard.setProjectDashboardList(projectList);
		///////////////////////////////////////////////////////////////////////////////////////////////////

		return beanProjectDashboard;
	}

    private String[] getGroupList() throws Exception
    {
        String[] arrResult = null;
        try
        {
            arrResult = objProjectDB.getGroup();
        }
        catch (Exception e)
        {
            logger.error("Exception occurs in ProjectDashboardBO.getGroupList(). " + e.toString());
            throw e;
        }
        return arrResult;
    }
}