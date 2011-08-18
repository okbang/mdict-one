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
 * @Title:        MilestoneBO.java
 * @Description:  
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 12, 2002
 * @Modified date:
 */

import java.util.Collection;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

import fpt.dashboard.ProjectManagementTran.ejb.Milestone;
import fpt.dashboard.ProjectManagementTran.ejb.MilestoneEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.MilestoneEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.MilestoneHome;
import fpt.dashboard.ProjectManagementTran.ejb.MilestoneInfo;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboard;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboardHome;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetail;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailHome;
import fpt.dashboard.bean.UserInfoBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneAddBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneAllBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneListBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneUpdateBean;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.util.StringUtil.StringMatrix;
import fpt.dashboard.util.StringUtil.StringVector;

public class MilestoneBO
{
//	private MilestoneHome homeMilestone = null;
//	private Milestone objMilestone = null;
//	private ProjectDetailHome homeProject = null;
//	private ProjectDetail objProject = null;
	
// HaiMM ==============
	private MilestoneEJBLocalHome homeMilestone = null;
	private MilestoneEJBLocal objMilestone = null;

	private ProjectDetailEJBLocalHome homeProject = null;
	private ProjectDetailEJBLocal objProject = null;
// =====================
	private static Logger logger = Logger.getLogger(MilestoneBO.class.getName());
	
	private String projectName;
	private String projectCode;
	private String pr_start;
	private String pr_base_finish;
	private String pr_plan_finish;
	private String pr_actual_finish;
	
	//data variables
    private String strDateFrom = null;
    private String strDateTo = null;
    private String strStatus = null;
	
	public MilestoneBO()
	{
	
	}
	
	//**************** HELPER Methods ****************************************
	//************************Milestone EJB***********************
	//EJB Bean Specific methods ...
	private void getEJBHome() throws NamingException
	{
		try
		{
			if (homeMilestone == null)
			{
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.MILESTONE);
				homeMilestone = (MilestoneEJBLocalHome)(ref); // HaiMM
			}
		}
		catch (NamingException ex)
		{
			logger.error("NamingException occurs in MilestoneBO.getEJBHome(). " + ex.getResolvedName());	
			throw ex;
		}
	} //getEJBHome
	private MilestoneEJBLocal getEJBRemote() throws Exception // HaiMM
	{
		try
		{
			objMilestone = (MilestoneEJBLocal)homeMilestone.create(); // HaiMM
			return objMilestone;
		}
		catch (Exception e)
		{
			logger.error(e.toString());
			return null;
		}
	} //getEJBRemote
	
	
	//EJB Bean Specific methods ...
	private void getProjectDetailEJB() throws Exception
	{
		try
		{
			if (homeProject == null)
			{
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.PROJECT_DETAIL);
				homeProject = (ProjectDetailEJBLocalHome)(ref);
				objProject = (ProjectDetailEJBLocal)homeProject.create();
			}
		}
		catch (Exception ex)
		{
			logger.error("Exception occurs in MilestoneBO.getProjectDetailEJB(). " + ex.getMessage());	
			throw ex;
		}
	} //getEJBHome
	
	/**
	 * Get all project's milestones.
	 * @author  Nguyen Thai Son.
	 * @version  November 11, 2002.
	 * @param	nProjectID int: project ID
	 * @exception   Exception    If an exception occurred.
	 */	
	public MilestoneListBean getMilestoneList(int nProjectID) throws Exception
	{
		MilestoneListBean beanMilestoneList = new MilestoneListBean();
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
			int orderby = 0;
			int page = 0;
			
			StringMatrix smMilestoneList = new StringMatrix();
			StringVector vecMilestone = new StringVector(10);
			
			Collection listMilestone = objMilestone.getMilestoneList(nProjectID, (page - 1) * 10, orderby);
			java.lang.Object[] arrMilestone = listMilestone.toArray();
			for (int nIndex = 0; nIndex < arrMilestone.length; nIndex++)
			{
				MilestoneInfo milestone = (MilestoneInfo) arrMilestone[nIndex];
				
				vecMilestone.setCell(0, Integer.toString(milestone.getID()));	//milestone ID
				vecMilestone.setCell(1, milestone.getName());	//milestone name
				int i = milestone.getComplete();
				vecMilestone.setCell(2, (i == 0) ? "No" : "Yes");	//complete?
				vecMilestone.setCell(3, milestone.getBaseFinishDate());	//base finish date
				vecMilestone.setCell(4, milestone.getPlanFinishDate());	//plan finish date
				vecMilestone.setCell(5, milestone.getActualFinishDate());	//actual finish date
				vecMilestone.setCell(6, Integer.toString(milestone.getBaseEffort()));		//base effort
				vecMilestone.setCell(7, Integer.toString(milestone.getPlanEffort()));		//plan effort
				vecMilestone.setCell(8, Integer.toString(milestone.getActualEffort()));		//actual effort
				vecMilestone.setCell(9, milestone.getDescription());	//description
				
				smMilestoneList.addRow(vecMilestone);
			}// end for
			
			beanMilestoneList.setMilestoneList(smMilestoneList);
			
			//Get Project Information...
			if (getProjectInfo(nProjectID))
			{
				beanMilestoneList.setProjectID(Integer.toString(nProjectID));
    			beanMilestoneList.setProjectCode(projectCode);
    			beanMilestoneList.setProjectName(projectName);
    			beanMilestoneList.setProjectStartDate(pr_start);
    			beanMilestoneList.setProjectBaseFinishDate(pr_base_finish);
    			beanMilestoneList.setProjectPlanFinishDate(pr_plan_finish);
    			beanMilestoneList.setProjectActualFinishDate(pr_actual_finish);
			}
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in MilestoneBO.getMilestoneList(): " + ex.toString());
			logger.error(ex);
		}
		
		///////////////////////////////////////////////////////////////////////////////////////
		return beanMilestoneList;
	}
	
	/**
	 * Delete milestone
	 * @author  Nguyen Thai Son
	 * @version November 12, 2002
	 * @param	strIDs String[]:	an array of milestone IDs
	 * @exception   Exception    If an exception occurred.
	 */
	public int deleteMilestone(String[] arrIDs) throws Exception
	{
		int nResult = -1;
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
            String strDelete = "";
            for(int i=0; i<arrIDs.length;  i++)
            {
				strDelete = strDelete + arrIDs[i] + ",";
            }
            strDelete = strDelete.substring(0, strDelete.length() - 1);
            objMilestone.deleteMilestone(strDelete);
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ProjectBO.deleteMilestone(): " + ex.toString());
			logger.error(ex);
		}
		
		return nResult;
	}
	
	/**
	 * Get project information
	 * @author  Nguyen Thai Son.
	 * @version  November 11, 2002.
	 * @param	nProjectID int: project ID
	 * @exception   Exception    If an exception occurred.
	 */	
	private boolean getProjectInfo(int nProjectID) throws Exception, NamingException
	{
		boolean bResult = true;
		
		try
		{
			Context ic = new InitialContext();
			java.lang.Object objref = ic.lookup(JNDINaming.PROJECT_DASHBOARD);

			ProjectDashboardHome homeDashboard = 
				(ProjectDashboardHome) javax.rmi.PortableRemoteObject.narrow(objref, ProjectDashboardHome.class);

			ProjectDashboard remoteDashboard = (ProjectDashboard) homeDashboard.create();
			remoteDashboard.getProjectInfo(nProjectID);

			projectName = remoteDashboard.getName();
			projectCode = remoteDashboard.getCode();
			pr_start = (remoteDashboard.getStart_date() != null) ? remoteDashboard.getStart_date() : "";
			pr_base_finish = (remoteDashboard.getBase_finish() != null) ? remoteDashboard.getBase_finish() : "";
			pr_plan_finish = (remoteDashboard.getPlan_finish() != null) ? remoteDashboard.getPlan_finish() : "";
			pr_actual_finish = (remoteDashboard.getActual_finish() != null) ? remoteDashboard.getActual_finish() : "";
			
			if (projectCode.equals(""))	bResult = false;
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			throw new Exception("Caught an unexpected exception in MilestoneBO.getProjectInfo: " + ex.toString());
		}
		
		return bResult;
	}
	
	/**
	 * Get all project's milestones.
	 * @author  Nguyen Thai Son.
	 * @version  November 11, 2002.
	 * @param	strMilestoneID int: project ID
	 * @exception   Exception    If an exception occurred.
	 */	
	public MilestoneUpdateBean getMilestoneInfo(String strMilestoneID) throws Exception
	{
		MilestoneUpdateBean beanMilestoneUpdate = new MilestoneUpdateBean();
		int nMilestoneID = 0;
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
			nMilestoneID = Integer.parseInt(strMilestoneID);
			
			MilestoneInfo milestone = objMilestone.getMilestone(nMilestoneID);

            beanMilestoneUpdate.setID(strMilestoneID);
    		beanMilestoneUpdate.setName(milestone.getName());
    		beanMilestoneUpdate.setComplete(milestone.getComplete());
    		beanMilestoneUpdate.setBaseFinishDate(milestone.getBaseFinishDate());
    		beanMilestoneUpdate.setPlanFinishDate(milestone.getPlanFinishDate());
    		beanMilestoneUpdate.setActualFinishDate(milestone.getActualFinishDate());
    		beanMilestoneUpdate.setDescription(milestone.getDescription());
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (NumberFormatException ex)
		{
			nMilestoneID = 0;
			logger.debug("NumberFormatException occurs in MilestoneBO.getMilestoneInfo(): strMilestoneID = " + strMilestoneID);
		}
		catch (Exception ex)
		{
			throw new Exception("Caught an unexpected exception in MilestoneBO.getMilestoneInfo: " + ex.toString());
		}
		
		return beanMilestoneUpdate;
	}
	
	/**
	 * Add new a milestone
	 * @author  Nguyen Thai Son
	 * @version November 12, 2002
	 * @param	strIDs String[]:	an array of milestone IDs
	 * @exception   Exception    If an exception occurred.
	 */
	public int addMilestone(MilestoneAddBean beanMilestone) throws Exception
	{
		int nResult = -1;
		try
		{
			getEJBHome();
			getEJBRemote();
			
            int nProjectID = Integer.parseInt(beanMilestone.getProjectID());
            
            String strName = beanMilestone.getName();
            int nComplete = beanMilestone.getComplete();
            String strBaseFinishDate = beanMilestone.getBaseFinishDate();
            String strPlanFinishDate = beanMilestone.getPlanFinishDate();
            String strActualFinishDate = beanMilestone.getActualFinishDate();
            String strDescription = beanMilestone.getDescription();

            String strBaseStartDate = "";
            String strPlanStartDate = "";
            String strActualStartDate = "";
            int nBaseEffort = 0;
            int nPlanEffort = 0;
            int nActualEffort = 0;
            
            objMilestone.addMilestone(nProjectID, strName,
                                  nComplete, strBaseStartDate,
                                  strPlanStartDate, strActualStartDate, strBaseFinishDate,
                                  strPlanFinishDate, strActualFinishDate, nBaseEffort,
                                  nPlanEffort, nActualEffort, strDescription);
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (NumberFormatException ex)
		{
			logger.debug("NumberFormatException occurs in MilestoneBO.addMilestone(): beanMilestone.getProjectID() = " + beanMilestone.getProjectID());
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ProjectBO.addMilestone(): " + ex.toString());
			logger.error(ex);
		}
		return nResult;
	}
	
	/**
	 * Update a milestone
	 * @author  Nguyen Thai Son
	 * @version November 12, 2002
	 * @param	strIDs String[]:	an array of milestone IDs
	 * @exception   Exception    If an exception occurred.
	 */
	public int updateMilestone(MilestoneUpdateBean beanMilestone) throws Exception
	{
		int nResult = -1;
		try
		{
			getEJBHome();
			getEJBRemote();
			
			int nID = Integer.parseInt(beanMilestone.getID());
            
            String strName = beanMilestone.getName();
            int nComplete = beanMilestone.getComplete();
            String strBaseFinishDate = beanMilestone.getBaseFinishDate();
            String strPlanFinishDate = beanMilestone.getPlanFinishDate();
            String strActualFinishDate = beanMilestone.getActualFinishDate();
            String strDescription = beanMilestone.getDescription();

            String strBaseStartDate = "";
            String strPlanStartDate = "";
            String strActualStartDate = "";
            int nBaseEffort = 0;
            int nPlanEffort = 0;
            int nActualEffort = 0;
            
            objMilestone.updateMilestone(nID, strName,
                                  nComplete, strBaseStartDate,
                                  strPlanStartDate, strActualStartDate, strBaseFinishDate,
                                  strPlanFinishDate, strActualFinishDate, nBaseEffort,
                                  nPlanEffort, nActualEffort, strDescription);
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (NumberFormatException ex)
		{
			logger.debug("NumberFormatException occurs in MilestoneBO.updateMilestone(): beanMilestone.getID() = " + beanMilestone.getID());
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in MilestoneBO.updateMilestone(): " + ex.toString());
			logger.error(ex);
		}
		return nResult;
	}
	
	/**
	 * Get complete/incomplete milestone
	 * @author  Nguyen Thai Son
	 * @version November 14, 2002
	 * @exception   Exception    If an exception occurred.
	 */
	public MilestoneAllBean getAllMilestone(MilestoneAllBean beanMilestoneAll, UserInfoBean beanUserInfo) throws Exception
	{
		strDateFrom = beanMilestoneAll.getDateFrom();
		strDateTo = beanMilestoneAll.getDateTo();
		strStatus = beanMilestoneAll.getStatus();
		String strUserGroup = beanUserInfo.getGroupName();

		String strRole = beanUserInfo.getSRole();	//formatted as "XXXXXXXXXX"
		if (strRole.charAt(4) == '1')		//PQA
			strUserGroup = "All";	//PQA can view all groups
		
		if (strDateFrom == null || strDateTo == null || strStatus == null)
		{
              //Get the default date
			getDefaultValues();
			beanMilestoneAll.setDateFrom(strDateFrom);
			beanMilestoneAll.setDateTo(strDateTo);
			beanMilestoneAll.setStatus(strStatus);
		}
		
		try
		{
			getProjectDetailEJB();
			
			Collection collResult = (Collection)objProject.getMilestoneList(strStatus,strDateFrom, strDateTo, strUserGroup);
			java.lang.Object[] arrResult = collResult.toArray();
			
			StringMatrix milestoneList = new StringMatrix();
			StringVector vecMilestone = new StringVector(7);
			
			for (int i = 0; i < arrResult.length; i++)
			{
				MilestoneInfo milestone = (MilestoneInfo)arrResult[i];
				
				vecMilestone.setCell(0, milestone.getProjectCode());
				vecMilestone.setCell(1, milestone.getName());
				vecMilestone.setCell(2, milestone.getBaseFinishDate());
				vecMilestone.setCell(3, milestone.getPlanFinishDate());
				vecMilestone.setCell(4, milestone.getStatus());
				vecMilestone.setCell(5, milestone.getDescription());
				vecMilestone.setCell(6, Integer.toString(milestone.getProjectID()));
				
				milestoneList.addRow(vecMilestone);
			}
    		beanMilestoneAll.setMilestoneList(milestoneList);
		}// end try
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in MilestoneBO.getAllMilestone(): " + ex.toString());
			logger.error(ex);
		}
		
		return beanMilestoneAll;
	}
	
	private boolean getDefaultValues()
	{
		java.util.Date fd = new java.util.Date();
		String day = Integer.toString(fd.getDate());
		String month = Integer.toString(fd.getMonth() + 1);
		String fullyear = Integer.toString(fd.getYear()+1900);
		String year = fullyear.trim().substring(2,4);
		strDateFrom = day+"/"+month+"/"+ year;

		java.util.Date tempd = new java.util.Date();
		java.util.Date td = new java.util.Date(tempd.getYear(),tempd.getMonth(),tempd.getDate() + 14);

		day = Integer.toString(td.getDate());
		month = Integer.toString(td.getMonth() + 1);
		fullyear = Integer.toString(td.getYear()+1900);
		year = fullyear.trim().substring(2,4);
		strDateTo = day+"/"+month+"/"+ year;

		return true;
	}
}