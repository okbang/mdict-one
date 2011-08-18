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
 * @Title:        ProjectBO.java
 * @Description:  
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 24, 2002
 * @Modified date:
 */

import java.util.Collection;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetail;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailHome;
import fpt.dashboard.bean.UserInfoBean;
import fpt.dashboard.bean.ProjectManagement.ProjectAddBean;
import fpt.dashboard.bean.ProjectManagement.ProjectCloseBean;
import fpt.dashboard.bean.ProjectManagement.ProjectDetailBean;
import fpt.dashboard.bean.ProjectManagement.ProjectInfoBean;
import fpt.dashboard.bean.ProjectManagement.ProjectListBean;
import fpt.dashboard.bean.ProjectManagement.ProjectListModelBean;
import fpt.dashboard.bean.ProjectManagement.ProjectUpdateBean;
import fpt.dashboard.bo.ComboBox.ComboBO;
import fpt.dashboard.constant.DATA;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.util.CommonUtil.CommonUtil;
import fpt.dashboard.util.StringUtil.StringMatrix;

public class ProjectBO
{
//	private ProjectDetailHome homeProject = null;
//	private ProjectDetail objProject = null;
	
// HaiMM =============
	private ProjectDetailEJBLocalHome homeProject = null;
	private ProjectDetailEJBLocal objProject = null;
// ===================

	private static Logger logger = Logger.getLogger(ProjectBO.class.getName());
	
	public ProjectBO()
	{
	
	}
	
	//**************** HELPER Methods *****************
	// EJB Bean Project methods ...
	private void getEJBHome() throws NamingException
	{
		try
		{
			if (homeProject == null)
			{
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.PROJECT_DETAIL);
				homeProject = (ProjectDetailEJBLocalHome)(ref);
			}
		}
		catch (NamingException ex)
		{
			logger.error("NamingException occurs in ProjectBO.getEJBHome(). " + ex.getResolvedName());	
			throw ex;
		}
	} //getEJBHome
	
	private ProjectDetailEJBLocal getEJBRemote() throws Exception
	{
		objProject = (ProjectDetailEJBLocal) homeProject.create();
		return objProject;
	} //getEJBRemote
	
	/**
	 * Get all projects.
	 * @author  Nguyen Thai Son.
	 * @version  24 October, 2002.
	 * @param	strGroup String:	group
	 * @param	strType String:	project type (external, internal,...)
	 * @param	strStatus String:	project status (on-going, closed,...)
	 * @param	strCategory String:	project categoy
	 * @param	beanUserInfo UserInfoBean: user information
	 * @exception   Exception    If an exception occurred.
	 */	
	public ProjectListBean getProjectList(
		String strGroup,
		String strType,
		String strStatus,
		String strCategory,
		UserInfoBean beanUserInfo)
		throws Exception {
		ProjectListBean beanProjectList = new ProjectListBean();
		ProjectListModelBean beanProjectListModel = new ProjectListModelBean();

		////////////////////////////////////////////////////////////////////////////////////////////
		//Type List - hard code.
		StringMatrix typeList = new StringMatrix(4, 2);
		typeList.setCell(0, 0, DATA.PROJECT_ALL);
		typeList.setCell(0, 1, DATA.PROJECT_TYPE_ALL);
		typeList.setCell(1, 0, DATA.PROJECT_EXTERNAL);
		typeList.setCell(1, 1, DATA.PROJECT_TYPE_EXTERNAL);
		typeList.setCell(2, 0, DATA.PROJECT_INTERNAL);
		typeList.setCell(2, 1, DATA.PROJECT_TYPE_INTERNAL);
		typeList.setCell(3, 0, DATA.PROJECT_PUBLIC);
		typeList.setCell(3, 1, DATA.PROJECT_TYPE_PUBLIC);
		beanProjectList.setTypeList(typeList);

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
		beanProjectList.setCateList(cateList);

		//Status List - hard code.
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
		beanProjectList.setStatusList(statusList);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		String group = "";
		try {
			getEJBHome();
			getEJBRemote();

			if (strGroup != null) {
				if (!strGroup.equals(Constants.GROUP_ALL) && (!strGroup.equals("")))
					group = strGroup;
				else
					group = Constants.GROUP_ALL;
			} else {
				group = Constants.GROUP_ALL;
			}
			String strAll = Constants.GROUP_ALL;
			beanProjectListModel.addGroupItem(strAll);

			Collection collGroup = objProject.listGroup();
			Iterator itG = collGroup.iterator();
			String gItem = "";
			while (itG.hasNext()) {
				gItem = itG.next().toString().trim();
				beanProjectListModel.addGroupItem(gItem);
			}
            
            Collection collProjectList = null;
            // External at project level AND project leader only show his assigned projects
            if (DATA.ROLE_EXTERNAL_PL.equals(beanUserInfo.getSRole()) ||
                DATA.ROLE_PROJECTLEADER.equals(beanUserInfo.getSRole()))
            {
                collProjectList =
                    objProject.listAssigned(strGroup, strStatus, strType,
                            strCategory, beanUserInfo.getDeveloperId());
            }
            else {
                collProjectList =
                    objProject.listAll(strGroup, strStatus, strType, strCategory);
            }
			if (collProjectList == null || collProjectList.isEmpty()) {
				logger.info(
					"ProjectBO.getProjectList(): No Project found.");
			}
			Iterator it = collProjectList.iterator();
			String id = "";
			String code = "";
			String name = "";
			String leader = "";
			String cate = "";
			while (it.hasNext()) {
				id = it.next().toString().trim();
				if (id.length() > 0) {
					Collection ch = objProject.getHeader(id);
					Iterator itH = ch.iterator();
					if (itH.hasNext())
						code = itH.next().toString();
					else
						code = "";
					if (itH.hasNext())
						name = itH.next().toString();
					else
						name = "";
					if (itH.hasNext())
						leader = itH.next().toString();
					else
						leader = "";
					if (itH.hasNext())
						cate = itH.next().toString();
					else
						cate = "";
					ProjectInfoBean project = new ProjectInfoBean();
					project.setProjectId(id);
					project.setProjectCode(code);
					project.setProjectName(name);
					project.setProjectLeader(leader);

					if (Integer.parseInt(cate) == 0)
						project.setCate(DATA.PROJECT_CATEGORY_DEVELOPMENT);
					else
						project.setCate(DATA.PROJECT_CATEGORY_MAINTENANCE);

					// add new project to Model View
					beanProjectListModel.addProject(project);
				}
			}
			// release resource
			objProject = null;
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in ProjectBO.getProjectList(): "
					+ ex.toString());
			logger.error(ex);
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////
		//Map Model To BaseBean Properties
		//Group List
		StringMatrix groupList =
			new StringMatrix(
				beanProjectListModel.getNumberOfGroupElements(),
				1);

		for (int i = 0;
			i < beanProjectListModel.getNumberOfGroupElements();
			i++) {
			groupList.setCell(i, 0, beanProjectListModel.getGroupItem(i));
		}
		beanProjectList.setGroupList(groupList);

		//Project List
		StringMatrix projectList =
			new StringMatrix(beanProjectListModel.getNumberOfElements(), 5);

		for (int i = 0; i < beanProjectListModel.getNumberOfElements(); i++) {
			projectList.setCell(
				i,
				0,
				beanProjectListModel.getProject(i).getProjectId());
			projectList.setCell(
				i,
				1,
				beanProjectListModel.getProject(i).getProjectCode());
			projectList.setCell(
				i,
				2,
				beanProjectListModel.getProject(i).getProjectName());
			projectList.setCell(
				i,
				3,
				beanProjectListModel.getProject(i).getProjectLeader());
			projectList.setCell(
				i,
				4,
				beanProjectListModel.getProject(i).getCate());
		}

		beanProjectList.setProjectList(projectList);

		beanProjectList.setGroup(strGroup);
		beanProjectList.setType(strType);
		beanProjectList.setStatus(strStatus);
		beanProjectList.setCate(strCategory);

		return beanProjectList;
	}
	
	/**
	 * Create an adding form to add a project
	 * @author  Nguyen Thai Son
	 * @version November 09, 2002
	 * @exception   Exception    If an exception occurred.
	 */
	public ProjectAddBean createProjectAddForm() throws Exception
	{
		ProjectAddBean beanProjectAdd = new ProjectAddBean();
		
		ComboBO boCombo = new ComboBO();
		ProjectInfoBean beanProjectInfo = new ProjectInfoBean();
		
		//STEP 2 - Using BO to get data
		//Group combo box
		Collection collGroup = boCombo.getGroupList();
		Iterator itG = collGroup.iterator();
		String gItem = "";

		while (itG.hasNext())
		{
			gItem = itG.next().toString().trim();
			beanProjectInfo.addGroupItem(gItem);
		}
		
		//Leader combo box
		Collection collLeader = boCombo.getLeaderList();
		Iterator itL = collLeader.iterator();
		String lItem = "";

		while(itL.hasNext())
		{
			lItem = itL.next().toString().trim();
			beanProjectInfo.addLeaderItem(lItem);
		}
		
		//STEP 3 - Preparing to display web
		//Type List - hard code.
	    StringMatrix typeList =  new StringMatrix(2, 2);
	    typeList.setCell(0,0, "0");
	    typeList.setCell(0,1, DATA.PROJECT_TYPE_EXTERNAL);
	    typeList.setCell(1,0, "8");
	    typeList.setCell(1,1, DATA.PROJECT_TYPE_INTERNAL);
	    beanProjectAdd.setTypeList(typeList);
	   //Category List - hard code.
	    StringMatrix cateList =  new StringMatrix(3, 2);
	    cateList.setCell(0,0, DATA.PROJECT_VALUE_CATEGORY_DEVELOPMENT);
	    cateList.setCell(0,1, DATA.PROJECT_CATEGORY_DEVELOPMENT);
	    cateList.setCell(1,0, DATA.PROJECT_VALUE_CATEGORY_MAINTENANCE);
	    cateList.setCell(1,1, DATA.PROJECT_CATEGORY_MAINTENANCE);
	    cateList.setCell(2,0, DATA.PROJECT_VALUE_CATEGORY_OTHERS);
	    cateList.setCell(2,1, DATA.PROJECT_CATEGORY_OTHERS);
	    beanProjectAdd.setCateList(cateList);


	    //Status List - hard code.
	    StringMatrix statusList =  new StringMatrix(2, 2);
	    statusList.setCell(0,0, "0");
	    statusList.setCell(0,1, DATA.PROJECT_STATUS_ONGOING);
	    statusList.setCell(1,0, "3");
	    statusList.setCell(1,1, DATA.PROJECT_STATUS_TENTATIVE);
	    beanProjectAdd.setStatusList(statusList);

	    //Group List
	    StringMatrix groupList = new StringMatrix(beanProjectInfo.getNumberOfGroupElements(),1);

	    for (int i=0;i<beanProjectInfo.getNumberOfGroupElements();i++)
	    {
			groupList.setCell(i,0,beanProjectInfo.getGroupItem(i));
	    }

	    beanProjectAdd.setGroupList(groupList);

	    //Leader List
	    StringMatrix leaderList = new StringMatrix(beanProjectInfo.getNumberOfLeaderElements(),1);

	    for (int i=0; i < beanProjectInfo.getNumberOfLeaderElements();i++)
	    {
			leaderList.setCell(i, 0, beanProjectInfo.getLeaderItem(i));
	    }
	    beanProjectAdd.setLeaderList(leaderList);
		
		///////////////////////////////////////////////////////////////////////////////
		return beanProjectAdd;
	}
	
	/**
	 * Delete project
	 * @author  Nguyen Thai Son
	 * @version November 09, 2002
	 * @param	strIDs String[]:	an array of project IDs
	 * @exception   Exception    If an exception occurred.
	 */
	public int deleteProject(String[] strIDs) throws Exception
	{
		int nResult = -1;
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
			String strProjectID = "";
			for(int i=0; i<strIDs.length; i++)
			{
				strProjectID = strIDs[i].toString().trim();
				if (strProjectID.length() > 0)
					objProject.delProject(strProjectID);
			}	
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ProjectBO.deleteProject(): " + ex.toString());
			logger.error(ex);
		}
		
		return nResult;
	}
	
	/**
	 * Save a project
	 * @author  Nguyen Thai Son
	 * @version November 09, 2002
	 * @param	beanNewProject ProjectAddBean:	a new project need to be saved
	 * @exception   Exception    If an exception occurred.
	 */
	public int saveProject(ProjectAddBean beanNewProject) throws Exception
	{
		int nResult = -1;
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
			String code = beanNewProject.getCode();
            String name = beanNewProject.getName();
            String leader = beanNewProject.getLeader();

            String startDate = beanNewProject.getStartDate().trim();
            String perComplete = beanNewProject.getPerComplete();

            String baseFinishDate = beanNewProject.getBaseFinishDate().trim();
            String planFinishDate = beanNewProject.getPlanFinishDate().trim();
            //String actualFinishDate = beanNewProject.getActualFinishDate().trim();

            String type = beanNewProject.getType();
            String group = beanNewProject.getGroup();
            String status = beanNewProject.getStatus();

            String  baseEffort = beanNewProject.getBaseEffort().trim();
            String  planEffort = beanNewProject.getPlanEffort().trim();
            String  actualEffort = beanNewProject.getActualEffort().trim();
//            logger.info(beanNewProject.getCustomer());
//            logger.info(beanNewProject.getCate());
            String customer = "";
            if(beanNewProject.getCustomer()!=null){
               customer = beanNewProject.getCustomer().trim();
            }

            String cate = beanNewProject.getCate().trim();

            // This cause Effort values is set to 0 when input blank value => incorrect
//            double _baseEffort = 0;
//            double _planEffort = 0;
//            double _actualEffort = 0;
//            float _perCom=0;
//
//           try
//           {
//                  _baseEffort = Double.parseDouble(baseEffort);
//           }catch(Exception ex){
//                  _baseEffort = 0;
//           }
//           try{
//                  _perCom = Float.parseFloat(perComplete);
//           }catch(Exception ex){
//                  perComplete = "0";
//           }
//        
//           try{
//                  _planEffort = Double.parseDouble(planEffort);
//           }catch(Exception ex){
//                  _planEffort = 0;
//           }
//        
//           try{
//                  _actualEffort = Double.parseDouble(actualEffort);
//           }catch(Exception ex){
//                  _actualEffort = 0;
//           }
//
			if( objProject.addProject(code, name,leader.toUpperCase(),startDate,perComplete,
									 baseFinishDate,planFinishDate,
									 baseEffort,planEffort,actualEffort,type,group,status,customer,cate)){
										nResult = 1;						 	
									 }
            // Created without exception
		}
		catch (javax.naming.NamingException e){
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex){
			//logger.debug("Exception occurs in ProjectBO.saveProject(): " + ex.toString());
			logger.error(ex);
		}
		finally{
			return nResult;
		}
	}
	
	/**
	 * Delete project
	 * @author  Nguyen Thai Son
	 * @version November 09, 2002
	 * @param	strProjectID String:	a project ID
	 * @exception   Exception    If an exception occurred.
	 */
	public ProjectDetailBean getProjectDetail(String strProjectID) throws Exception
	{
		ProjectInfoBean beanProjectInfo = new ProjectInfoBean();
		ProjectDetailBean beanProjectDetail = new ProjectDetailBean();
		try
		{
			getEJBHome();
			getEJBRemote();
			
			Collection collProject = objProject.getContent(strProjectID);

            if(collProject == null || collProject.isEmpty())
            {
				logger.info("No Project found...");
				return null;
            }

            String code = "";
            String name = "";
            String leader = "";
			String planStartDate = "";
            String startDate = "";
            String perComplete = "";
            String description = "";

            String scheduleStatus = "NA";

            String planFinishDate = "";
            String baseFinishDate = "";
            String actualFinishDate = "";

            String planEffort = "";
            String baseEffort = "";
            String actualEffort = "";
            String effortStatus="";

            String totalRequirement = "";
            String totalDefect = "";
            String totalWeightedDefect = "";

            String type = "";
            String group = "";
            String status="";
            String datafile="";
            String lastUpdate="";
            String customer="";
            String cate="";
            
            String strTotalPending = "";		//Thaison - Oct 18, 2002

            Iterator itC = collProject.iterator();
            if (itC.hasNext()) code = itC.next().toString();
            if (itC.hasNext()) name = itC.next().toString();
            if (itC.hasNext()) leader = itC.next().toString();
			if (itC.hasNext()) planStartDate = itC.next().toString();
            if (itC.hasNext()) startDate = itC.next().toString();
            if (itC.hasNext()) perComplete = itC.next().toString();

            if (itC.hasNext()) baseFinishDate = itC.next().toString();
            if (itC.hasNext()) planFinishDate = itC.next().toString();
            if (itC.hasNext()) actualFinishDate = itC.next().toString();

            if (itC.hasNext()) baseEffort = itC.next().toString();
            if (itC.hasNext()) planEffort = itC.next().toString();

            //modified by MinhPT 04-Nov-03
            //for avoid null poiter planEffort
//            if (planEffort.equalsIgnoreCase("0")) planEffort = "";
            if ("0".equalsIgnoreCase(planEffort)) planEffort = "";

            if (itC.hasNext())actualEffort = itC.next().toString();
            //for avoid null poiter actualEffort
            if ("0".equalsIgnoreCase(actualEffort)) actualEffort = "";

            if (itC.hasNext()) description = itC.next().toString();
            if (itC.hasNext()) status = itC.next().toString();
            if (itC.hasNext()) group = itC.next().toString();

            if (itC.hasNext())  totalRequirement = itC.next().toString();
            if (itC.hasNext())  totalDefect = itC.next().toString();
            if (itC.hasNext())  totalWeightedDefect = itC.next().toString();
            
            if (itC.hasNext()) scheduleStatus = itC.next().toString();
            if (itC.hasNext()) effortStatus = itC.next().toString();
            if (itC.hasNext()) type = itC.next().toString();
            if (itC.hasNext()) lastUpdate = itC.next().toString();
            if (itC.hasNext()) cate = itC.next().toString();
            if (itC.hasNext()) customer = itC.next().toString();
			
			if (itC.hasNext()) strTotalPending = itC.next().toString();		//Thaison - Oct 18, 2002
			logger.info("modelhandlers.GetProjectHandler: strTotalPending = " + strTotalPending);

            beanProjectInfo.setProjectId(strProjectID);
            beanProjectInfo.setProjectCode(code);
            beanProjectInfo.setProjectName(name);
            beanProjectInfo.setProjectLeader(leader);

			beanProjectInfo.setStartDate(startDate);
			beanProjectInfo.setPlanStartDate(planStartDate);
            beanProjectInfo.setPerComplete(perComplete);
            beanProjectInfo.setDescription(description);

            beanProjectInfo.setScheduleStatus(scheduleStatus);

            beanProjectInfo.setPlanFinishDate(planFinishDate);
            beanProjectInfo.setBaseFinishDate(baseFinishDate);
            beanProjectInfo.setActualFinishDate(actualFinishDate);

            beanProjectInfo.setPlanEffort(planEffort);
            beanProjectInfo.setBaseEffort(baseEffort);
            beanProjectInfo.setActualEffort(actualEffort);

            beanProjectInfo.setTotalRequirement(totalRequirement);
            beanProjectInfo.setTotalDefect(totalDefect);
            beanProjectInfo.setTotalWeightedDefect(totalWeightedDefect);

            beanProjectInfo.setType(type);
            beanProjectInfo.setGroup(group);
            beanProjectInfo.setStatus(status);
            beanProjectInfo.setCate(cate);
            beanProjectInfo.setCustomer(customer);
            beanProjectInfo.setEffortStatus(effortStatus);
            beanProjectInfo.setLastUpdate(lastUpdate);
            
            beanProjectInfo.setTotalPending(strTotalPending);	//Thaison - Oct 18, 2002
          ///////////////////
          // Get chart data

            Collection defectChartCollection = objProject.getDefectChartData(strProjectID);
            beanProjectInfo.setDefectChartData(defectChartCollection);

            Collection requirementChartCollection = objProject.getRequirementChartData(strProjectID);
            beanProjectInfo.setRequirementChartData(requirementChartCollection);

          ///////////////////
          // Get Milestone
            Collection milestoneCollection = objProject.getMilestones(strProjectID);
            beanProjectInfo.setMilestoneList(milestoneCollection);

          ///////////////////
          // Get Assignment
            Collection assignmentCollection = objProject.getAssignments(strProjectID);
            beanProjectInfo.setAssignmentList(assignmentCollection);

          ///////////////////
          // Get Issue
            Collection issueCollection = objProject.getIssue(strProjectID);
            beanProjectInfo.setIssueList(issueCollection);

		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ProjectBO.getProjectDetail(): " + ex.toString());
			logger.error(ex);
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		beanProjectDetail.setId(strProjectID);
    	beanProjectDetail.setCode(beanProjectInfo.getProjectCode());
	    beanProjectDetail.setName(beanProjectInfo.getProjectName());
	    beanProjectDetail.setLeader(beanProjectInfo.getProjectLeader());
//	    beanProjectDetail.setStrProjectName(beanProjectInfo.getProjectName());

	    beanProjectDetail.setStartDate(beanProjectInfo.getStartDate());
	    beanProjectDetail.setPlanStartDate(beanProjectInfo.getPlanStartDate());
	    beanProjectDetail.setPerComplete(beanProjectInfo.getPerComplete());
	    beanProjectDetail.setDescription(beanProjectInfo.getDescription());

	    beanProjectDetail.setScheduleStatus(beanProjectInfo.getScheduleStatus());
	    beanProjectDetail.setEffortStatus(beanProjectInfo.getEffortStatus());

	    beanProjectDetail.setPlanFinishDate(beanProjectInfo.getPlanFinishDate());
	    beanProjectDetail.setBaseFinishDate(beanProjectInfo.getBaseFinishDate());
	    beanProjectDetail.setActualFinishDate(beanProjectInfo.getActualFinishDate());

	    beanProjectDetail.setPlanEffort(beanProjectInfo.getPlanEffort());
	    beanProjectDetail.setBaseEffort(beanProjectInfo.getBaseEffort());
	    beanProjectDetail.setActualEffort(beanProjectInfo.getActualEffort());

	    beanProjectDetail.setTotalRequirement(beanProjectInfo.getTotalRequirement());
	    beanProjectDetail.setTotalDefect(beanProjectInfo.getTotalDefect());
	    beanProjectDetail.setTotalWeightedDefect(beanProjectInfo.getTotalWeightedDefect());

	    beanProjectDetail.setType(beanProjectInfo.getType());
	    beanProjectDetail.setGroup(beanProjectInfo.getGroup());
	    beanProjectDetail.setStatus(beanProjectInfo.getStatus());
	    beanProjectDetail.setCate(beanProjectInfo.getCate());
	    beanProjectDetail.setCustomer(beanProjectInfo.getCustomer());

	    beanProjectDetail.setTotalPending(beanProjectInfo.getTotalPending());

	    beanProjectDetail.setDefectChartData(beanProjectInfo.getDefectChartData());
	    beanProjectDetail.setRequirementChartData(beanProjectInfo.getRequirementChartData());

	    beanProjectDetail.setMilestoneList(beanProjectInfo.getMilestoneList());
	    beanProjectDetail.setAssignmentList(beanProjectInfo.getAssignmentList());
	    beanProjectDetail.setIssueList(beanProjectInfo.getIssueList());
	    beanProjectDetail.setLastUpdate(beanProjectInfo.getLastUpdate());

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		return beanProjectDetail;
	}
	
	/**
	 * Create an updating form to update a project
	 * @author  Nguyen Thai Son
	 * @version November 09, 2002
	 * @exception   Exception    If an exception occurred.
	 */
	public ProjectUpdateBean createProjectUpdateForm(String strProjectID) throws Exception
	{
		ProjectUpdateBean beanProjectUpdate = new ProjectUpdateBean();
		ProjectInfoBean beanProjectInfo = new ProjectInfoBean();
		
		//Type List - hard code.
	    StringMatrix typeList =  new StringMatrix(2, 2);
	    typeList.setCell(0,0, "0");
	    typeList.setCell(0,1, DATA.PROJECT_TYPE_EXTERNAL);
	    typeList.setCell(1,0, "8");
	    typeList.setCell(1,1, DATA.PROJECT_TYPE_INTERNAL);
	    beanProjectUpdate.setTypeList(typeList);
	   //Category List - hard code.
	    StringMatrix cateList =  new StringMatrix(3, 2);
	    cateList.setCell(0,0,DATA.PROJECT_VALUE_CATEGORY_DEVELOPMENT);
	    cateList.setCell(0,1, DATA.PROJECT_CATEGORY_DEVELOPMENT);
	    cateList.setCell(1,0,DATA.PROJECT_VALUE_CATEGORY_MAINTENANCE);
	    cateList.setCell(1,1, DATA.PROJECT_CATEGORY_MAINTENANCE);
	    cateList.setCell(2,0,DATA.PROJECT_VALUE_CATEGORY_OTHERS);
	    cateList.setCell(2,1, DATA.PROJECT_CATEGORY_OTHERS);
	    beanProjectUpdate.setCateList(cateList);

	    //Status List - hard code.
	    StringMatrix statusList =  new StringMatrix(2, 2);
	    statusList.setCell(0,0, "0");
	    statusList.setCell(0,1, DATA.PROJECT_STATUS_ONGOING);
	    statusList.setCell(1,0, "3");
	    statusList.setCell(1,1, DATA.PROJECT_STATUS_TENTATIVE);
	    beanProjectUpdate.setStatusList(statusList);
	    ///////////////////////////////////////////////////////////////////////////////
	    
		try
		{
			getEJBHome();
			getEJBRemote();    
			
			// list group of Project
            Collection collGroup = objProject.listGroup();

            Iterator itG = collGroup.iterator();
            String gItem = "";

            while(itG.hasNext())
            {
            	gItem = itG.next().toString().trim();
            	beanProjectInfo.addGroupItem(gItem);
            }

            // list Leader
            Collection collLeader = objProject.listLeader();
            Iterator itL = collLeader.iterator();
            String lItem = "";

            while(itL.hasNext())
            {
				lItem = itL.next().toString().trim();
				beanProjectInfo.addLeaderItem(lItem);
            }
            
            // Get content
            Collection collProject = objProject.getContent(strProjectID);

            if(collProject == null || collProject.isEmpty())
            {
              logger.info("No Project found...");
            }

            String code = "";
            String name = "";
            String leader = "";
			String planStartDate = "";
            String startDate = "";
            String perComplete = "";
            String description = "";

            String scheduleStatus = "NA";

            String planFinishDate = "";
            String baseFinishDate = "";
            String actualFinishDate = "";

            String planEffort = "";
            String baseEffort = "";
            String actualEffort = "";
            String effortStatus="";

            String totalRequirement = "";
            String totalDefect = "";
            String totalWeightedDefect = "";

            String type = "";
            String group = "";
            String status="";
            String cate="";
            String customer="";
            String lastupdate="";

            Iterator itC = collProject.iterator();
			int ik=0;

            if (itC.hasNext()) code = itC.next().toString();
            if (itC.hasNext()) name = itC.next().toString();
            if (itC.hasNext()) leader = itC.next().toString();

			if (itC.hasNext()) planStartDate = itC.next().toString();
            if (itC.hasNext()) startDate = itC.next().toString();
            if (itC.hasNext()) perComplete = itC.next().toString();

            if (itC.hasNext()) baseFinishDate = itC.next().toString();
            if (itC.hasNext()) planFinishDate = itC.next().toString();
            if (itC.hasNext()) actualFinishDate = itC.next().toString();
            if (itC.hasNext()) baseEffort = itC.next().toString();
            if (itC.hasNext()) planEffort = itC.next().toString();
            //modified by MinhPT 04-Nov-03
            //for catch null pointer planEffort?
            //if (planEffort.equalsIgnoreCase("0")) planEffort = "";
            if ("0".equalsIgnoreCase(planEffort)) planEffort = "";
            if (itC.hasNext())actualEffort = itC.next().toString();

            //for catch null pointer planEffort?
            //if (planEffort.equalsIgnoreCase("0")) planEffort = "";
            if ("0".equalsIgnoreCase(actualEffort)) actualEffort = "";

            if (itC.hasNext()) description = itC.next().toString();
            if (itC.hasNext()) status = itC.next().toString();
            if (itC.hasNext()) group = itC.next().toString();

            if (itC.hasNext())  totalRequirement = itC.next().toString();
            if (itC.hasNext())  totalDefect = itC.next().toString();
            if (itC.hasNext())  totalWeightedDefect = itC.next().toString();

            if (itC.hasNext()) scheduleStatus = itC.next().toString();
            if (itC.hasNext()) effortStatus = itC.next().toString();
            if (itC.hasNext()) type = itC.next().toString();
            if (itC.hasNext()) lastupdate = itC.next().toString();
            if (itC.hasNext()) cate = itC.next().toString();
            if (itC.hasNext()) customer = itC.next().toString();


            beanProjectInfo.setProjectId(strProjectID);
            beanProjectInfo.setProjectCode(code);
            beanProjectInfo.setProjectName(name);
            beanProjectInfo.setProjectLeader(leader);

			beanProjectInfo.setPlanStartDate(planStartDate);
            beanProjectInfo.setStartDate(startDate);
            beanProjectInfo.setPerComplete(perComplete);
            beanProjectInfo.setDescription(description);

            beanProjectInfo.setScheduleStatus(scheduleStatus);

            beanProjectInfo.setPlanFinishDate(planFinishDate);
            beanProjectInfo.setBaseFinishDate(baseFinishDate);
            beanProjectInfo.setActualFinishDate(actualFinishDate);

            beanProjectInfo.setPlanEffort(planEffort);
            beanProjectInfo.setBaseEffort(baseEffort);
            beanProjectInfo.setActualEffort(actualEffort);

            beanProjectInfo.setTotalRequirement(totalRequirement);
            beanProjectInfo.setTotalDefect(totalDefect);
            beanProjectInfo.setTotalWeightedDefect(totalWeightedDefect);

            beanProjectInfo.setType(type);
            beanProjectInfo.setGroup(group);
            beanProjectInfo.setStatus(status);
            beanProjectInfo.setCate(cate);
            beanProjectInfo.setCustomer(customer);
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ProjectBO.createProjectUpdateForm(): " + ex.toString());
			logger.error(ex);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Group List
    	StringMatrix groupList =
		  new StringMatrix(beanProjectInfo.getNumberOfGroupElements(),1);

	    for (int i=0;i<beanProjectInfo.getNumberOfGroupElements();i++){
	      groupList.setCell(i,0,beanProjectInfo.getGroupItem(i));
	    }
	    beanProjectUpdate.setGroupList(groupList);

	    //Leader List
	    StringMatrix leaderList =
		  new StringMatrix(beanProjectInfo.getNumberOfLeaderElements(),1);

	    for (int i=0;i<beanProjectInfo.getNumberOfLeaderElements();i++){
	      leaderList.setCell(i,0,beanProjectInfo.getLeaderItem(i));
	    }
	    beanProjectUpdate.setLeaderList(leaderList);

	    //

	    beanProjectUpdate.setId(strProjectID);
	    beanProjectUpdate.setCode(beanProjectInfo.getProjectCode());
	    beanProjectUpdate.setName(beanProjectInfo.getProjectName());
	    beanProjectUpdate.setLeader(beanProjectInfo.getProjectLeader());

		beanProjectUpdate.setPlanStartDate(beanProjectInfo.getPlanStartDate());
	    beanProjectUpdate.setStartDate(beanProjectInfo.getStartDate());
	    beanProjectUpdate.setPerComplete(beanProjectInfo.getPerComplete());
	    beanProjectUpdate.setDescription(beanProjectInfo.getDescription());

	    beanProjectUpdate.setPlanFinishDate(beanProjectInfo.getPlanFinishDate());
	    beanProjectUpdate.setBaseFinishDate(beanProjectInfo.getBaseFinishDate());
	    beanProjectUpdate.setActualFinishDate(beanProjectInfo.getActualFinishDate());

	    beanProjectUpdate.setPlanEffort(beanProjectInfo.getPlanEffort());
	    beanProjectUpdate.setBaseEffort(beanProjectInfo.getBaseEffort());
	    beanProjectUpdate.setActualEffort(beanProjectInfo.getActualEffort());

	    beanProjectUpdate.setType(beanProjectInfo.getType());
	    beanProjectUpdate.setGroup(beanProjectInfo.getGroup());
	    beanProjectUpdate.setStatus(beanProjectInfo.getStatus());
	    beanProjectUpdate.setCate(beanProjectInfo.getCate());
	    beanProjectUpdate.setCustomer(beanProjectInfo.getCustomer());

		////////////////////////////////////////////////////////////////////////////////////////
		return beanProjectUpdate;
	}
	
	/**
	 * Update a project into database
	 * @author  Nguyen Thai Son
	 * @version November 09, 2002
	 * @param	beanNewProject ProjectAddBean:	a new project need to be saved
	 * @exception   Exception    If an exception occurred.
	 */
	public int updateProject(ProjectUpdateBean beanProject) throws Exception
	{
		int nResult = -1;
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
			String pId = beanProject.getId();
            String code = CommonUtil.stringConvert(beanProject.getCode());
            String name = CommonUtil.stringConvert(beanProject.getName());
            String leader = beanProject.getLeader();

            String startDate = beanProject.getStartDate();
            String perComplete = beanProject.getPerComplete();
            String baseFinishDate = beanProject.getBaseFinishDate();
            String planFinishDate = beanProject.getPlanFinishDate();
            //String actualFinishDate = beanProject.getActualFinishDate();

            String baseEffort = beanProject.getBaseEffort();
            String planEffort = beanProject.getPlanEffort();
            String actualEffort = beanProject.getActualEffort();

            String type = beanProject.getType();
            String group = beanProject.getGroup();
            String status=beanProject.getStatus();
            String cate=beanProject.getCate();
            String customer=CommonUtil.stringConvert(beanProject.getCustomer());
            
            // This cause Effort values is set to 0 when input blank value => incorrect
//            double _baseEffort = 0;
//            double _planEffort = 0;
//            double _actualEffort = 0;
//
//            if (baseEffort.length()>0){
//                    _baseEffort = Double.parseDouble(baseEffort);
//            }
//
//            if (planEffort.length()>0){
//                    _planEffort = Double.parseDouble(planEffort);
//            }
//
//            if (actualEffort.length()>0){
//                    _actualEffort = Double.parseDouble(actualEffort);
//            }

            if (objProject.updateProject(pId,code,name,leader,startDate,perComplete,baseFinishDate.trim(),
                            planFinishDate.trim(),baseEffort,planEffort,actualEffort,type,group,status,cate,customer)){
								nResult = 1;                	
                            }
            // Updated without exception
            
		} catch (javax.naming.NamingException e){
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex){
            logger.debug("Exception occurs in ProjectBO.saveProject(): " + ex.toString());
            logger.error(ex);
        }
        finally{
			return nResult;
        }
	}
	
	/**
	 * Close a project
	 * @author  Nguyen Thai Son
	 * @version November 09, 2002
	 * @param	beanProjectClose ProjectCloseBean:	a project needs to be closed.
	 * @exception   Exception    If an exception occurred.
	 */
	public int closeProject(ProjectCloseBean beanProjectClose) throws Exception
	{
		int nResult = -1;
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
			String strProjectID = beanProjectClose.getProjectID();
            String actualFinishDate = beanProjectClose.getActualFinishDate();
            String status = beanProjectClose.getStatus();
            String desc = beanProjectClose.getDescription();

            objProject.closeProject(strProjectID, actualFinishDate.trim(), status, desc);
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ProjectBO.saveProject(): " + ex.toString());
			logger.error(ex);
		}
		
		return nResult;
	}

    public void reopenProject(String strProjectID) throws Exception {
        try {
            getEJBHome();
            getEJBRemote();
            
            objProject.closeProject(strProjectID, "", DATA.PROJECT_VALUE_STATUS_ONGOING, "");
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in ProjectBO.saveProject(): " + ex.toString());
            logger.error(ex);
        }
   }
}