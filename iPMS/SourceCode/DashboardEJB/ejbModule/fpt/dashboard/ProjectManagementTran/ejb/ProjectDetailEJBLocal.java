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
 
 package fpt.dashboard.ProjectManagementTran.ejb;
/**
 * Local interface for Enterprise Bean: ProjectDetailEJB
 */
public interface ProjectDetailEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * listGroup
	 */
	public java.util.Collection listGroup() throws java.sql.SQLException;
	/**
	 * listLeader
	 */
	public java.util.Collection listLeader() throws java.sql.SQLException;
	/**
	 * listAll
	 */
	public java.util.Collection listAll(
		java.lang.String group,
		java.lang.String status,
		java.lang.String type,
		java.lang.String cate)
		throws java.sql.SQLException;
	/**
	 * listAssigned
	 */
	public java.util.Collection listAssigned(
		java.lang.String group,
		java.lang.String status,
		java.lang.String type,
		java.lang.String cate,
		java.lang.String strDeveloperID)
		throws java.sql.SQLException;
	/**
	 * getHeader
	 */
	public java.util.Collection getHeader(java.lang.String pId)
		throws java.sql.SQLException;
	/**
	 * getContent
	 */
	public java.util.Collection getContent(java.lang.String pId)
		throws java.sql.SQLException;
	/**
	 * getAssignments
	 */
	public java.util.Collection getAssignments(java.lang.String pId)
		throws java.sql.SQLException;
	/**
	 * getMilestones
	 */
	public java.util.Collection getMilestones(java.lang.String pId)
		throws java.sql.SQLException;
	/**
	 * getIssue
	 */
	public java.util.Collection getIssue(java.lang.String pId)
		throws java.sql.SQLException;
	/**
	 * getIssueContent
	 */
	public java.lang.String getIssueContent(java.lang.String iId)
		throws java.sql.SQLException;
	/**
	 * getMilestoneList
	 */
	public java.util.Collection getMilestoneList(
		java.lang.String strComplete,
		java.lang.String fromDate,
		java.lang.String toDate,
		java.lang.String strUserGroup)
		throws java.sql.SQLException;
	/**
	 * getRequirementChartData
	 */
	public java.util.Collection getRequirementChartData(java.lang.String pId)
		throws java.sql.SQLException;
	/**
	 * getDefectChartData
	 */
	public java.util.Collection getDefectChartData(java.lang.String pId)
		throws java.sql.SQLException;
	/**
	 * updateProject
	 */
	public boolean updateProject(
		java.lang.String pId,
		java.lang.String code,
		java.lang.String name,
		java.lang.String leader,
		java.lang.String startDate,
		java.lang.String perComplete,
		java.lang.String baseFinishDate,
		java.lang.String planFinishDate,
		java.lang.String baseEffort,
		java.lang.String planEffort,
		java.lang.String actualEffort,
		java.lang.String type,
		java.lang.String group,
		java.lang.String status,
		java.lang.String cate,
		java.lang.String customer)
		throws java.sql.SQLException;
	/**
	 * closeProject
	 */
	public void closeProject(
		java.lang.String pId,
		java.lang.String actualFinishDate,
		java.lang.String status,
		java.lang.String desc)
		throws java.sql.SQLException;
	/**
	 * addProject
	 */
	public boolean addProject(
		java.lang.String code,
		java.lang.String name,
		java.lang.String leader,
		java.lang.String startDate,
		java.lang.String perComplete,
		java.lang.String baseFinishDate,
		java.lang.String planFinishDate,
		java.lang.String baseEffort,
		java.lang.String planEffort,
		java.lang.String actualEffort,
		java.lang.String type,
		java.lang.String group,
		java.lang.String status,
		java.lang.String customer,
		java.lang.String cate)
		throws java.sql.SQLException;
	/**
	 * delProject
	 */
	public void delProject(java.lang.String pId) throws java.sql.SQLException;
	/**
	 * addProjectIssue
	 */
	public void addProjectIssue(
		java.lang.String projectId,
		java.lang.String description,
		java.lang.String startDate,
		java.lang.String endDate)
		throws java.sql.SQLException;
	/**
	 * updateProjectIssue
	 */
	public void updateProjectIssue(
		java.lang.String iId,
		java.lang.String projectId,
		java.lang.String description,
		java.lang.String startDate,
		java.lang.String endDate)
		throws java.sql.SQLException;
	/**
	 * delProjectIssue
	 */
	public void delProjectIssue(java.lang.String iId)
		throws java.sql.SQLException;
	/**
	 * getOngoingList
	 */
	public java.util.Collection getOngoingList(
		java.util.Calendar cal_From,
		java.util.Calendar cal_To,
		java.lang.String strGroupName)
		throws java.sql.SQLException;
}
