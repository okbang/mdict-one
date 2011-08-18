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
 * Local interface for Enterprise Bean: ProjectDashboardEJB
 */
public interface ProjectDashboardEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * getID
	 */
	public java.lang.String getID();
	/**
	 * getName
	 */
	public java.lang.String getName();
	/**
	 * getCode
	 */
	public java.lang.String getCode();
	/**
	 * getDescription
	 */
	public java.lang.String getDescription();
	/**
	 * getEffort_status
	 */
	public float getEffort_status();
	/**
	 * getSchedule_status
	 */
	public float getSchedule_status();
	/**
	 * updateProject
	 */
	public void updateProject(
		int per_complete,
		java.lang.String dt_plan_finish,
		java.lang.String dt_actual_finish,
		int int_plan_effort,
		int int_actual_effort,
		java.lang.String txt_description,
		int totalRequirement,
		int commitedRequirement,
		int designedRequirement,
		int codedRequirement,
		int testedRequirement,
		int deployedRequirement,
		int acceptedRequirement,
		int totalDefect,
		int totalWeightedDefect,
		int fatalPendingDefect,
		int seriousPendingDefect,
		int mediumPendingDefect,
		int cosmeticPendingDefect,
		int totalFatalDefect,
		int totalSeriousDefect,
		int totalMediumDefect,
		int totalCosmeticDefect)
		throws java.sql.SQLException;
	/**
	 * getStart_date
	 */
	public java.lang.String getStart_date();
	/**
	 * getBase_finish
	 */
	public java.lang.String getBase_finish();
	/**
	 * getPlan_finish
	 */
	public java.lang.String getPlan_finish();
	/**
	 * getActual_finish
	 */
	public java.lang.String getActual_finish();
	/**
	 * getBase_effort
	 */
	public int getBase_effort();
	/**
	 * getPlan_effort
	 */
	public int getPlan_effort();
	/**
	 * getActual_effort
	 */
	public int getActual_effort();
	/**
	 * getPer_complete
	 */
	public int getPer_complete();
	/**
	 * getGroup
	 */
	public java.lang.String[] getGroup() throws java.sql.SQLException;
	/**
	 * getTotalRequirement
	 */
	public int getTotalRequirement();
	/**
	 * getCommittedRequirement
	 */
	public int getCommittedRequirement();
	/**
	 * getDesignedRequirement
	 */
	public int getDesignedRequirement();
	/**
	 * getCodedRequirement
	 */
	public int getCodedRequirement();
	/**
	 * getTestedRequirement
	 */
	public int getTestedRequirement();
	/**
	 * getDeployedRequirement
	 */
	public int getDeployedRequirement();
	/**
	 * getAcceptedRequirement
	 */
	public int getAcceptedRequirement();
	/**
	 * getTotalDefect
	 */
	public int getTotalDefect();
	/**
	 * getTotalWeightedDefect
	 */
	public int getTotalWeightedDefect();
	/**
	 * getFatalPendingDefect
	 */
	public int getFatalPendingDefect();
	/**
	 * getSeriousPendingDefect
	 */
	public int getSeriousPendingDefect();
	/**
	 * getMediumPendingDefect
	 */
	public int getMediumPendingDefect();
	/**
	 * getCosmeticPendingDefect
	 */
	public int getCosmeticPendingDefect();
	/**
	 * getTotalFatalDefect
	 */
	public int getTotalFatalDefect();
	/**
	 * getTotalSeriousDefect
	 */
	public int getTotalSeriousDefect();
	/**
	 * getTotalMediumDefect
	 */
	public int getTotalMediumDefect();
	/**
	 * getTotalCosmeticDefect
	 */
	public int getTotalCosmeticDefect();
	/**
	 * getProjectInfo
	 */
	public void getProjectInfo(int ProjectID) throws java.sql.SQLException;
	/**
	 * setProjectID
	 */
	public void setProjectID(int value);
	/**
	 * getProjectDashboard
	 */
	public java.util.Collection getProjectDashboard(
		java.lang.String strType,
		java.lang.String strGroup,
		java.lang.String strStatus,
		java.lang.String strCategory,
		java.lang.String strDeveloperId,
		int nOrderBy)
		throws java.sql.SQLException;
}
