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
 * Local interface for Enterprise Bean: MilestoneEJB
 */
public interface MilestoneEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * deleteMilestone
	 */
	public void deleteMilestone(java.lang.String MilestoneID)
		throws java.sql.SQLException;
	/**
	 * addMilestone
	 */
	public void addMilestone(
		int Project_ID,
		java.lang.String Name,
		int Complete,
		java.lang.String Base_start_date,
		java.lang.String Plan_start_date,
		java.lang.String Actual_start_date,
		java.lang.String Base_finish_date,
		java.lang.String Plan_finish_date,
		java.lang.String Actual_finish_date,
		int Base_effort,
		int Plan_effort,
		int Actual_effort,
		java.lang.String description)
		throws java.sql.SQLException;
	/**
	 * getMilestone
	 */
	public fpt.dashboard.ProjectManagementTran.ejb.MilestoneInfo getMilestone(
		int MilestoneID)
		throws java.sql.SQLException;
	/**
	 * updateMilestone
	 */
	public void updateMilestone(
		int Milestone_ID,
		java.lang.String Name,
		int Complete,
		java.lang.String Base_start_date,
		java.lang.String Plan_start_date,
		java.lang.String Actual_start_date,
		java.lang.String Base_finish_date,
		java.lang.String Plan_finish_date,
		java.lang.String Actual_finish_date,
		int Base_effort,
		int Plan_effort,
		int Actual_effort,
		java.lang.String description)
		throws java.sql.SQLException;
	/**
	 * getMilestoneList
	 */
	public java.util.Collection getMilestoneList(
		int ProjectID,
		int index,
		int orderby)
		throws java.sql.SQLException;
}
