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

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.sql.*;
import javax.sql.*;
import java.util.Collection;
import javax.naming.NamingException;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;

public interface ProjectDashboard extends EJBObject
{

  	public String getID()throws RemoteException;
  	public String getName()throws RemoteException;
	public String getCode()throws RemoteException;
	public String getDescription()throws RemoteException;
	public float getEffort_status()throws RemoteException;
	public float getSchedule_status()throws RemoteException;
	public void updateProject(
							int 		per_complete,
							String 		dt_plan_finish,
							String 		dt_actual_finish,
							int 		int_plan_effort,
							int 		int_actual_effort,
							String 		txt_description,
							int 		totalRequirement,
							int 		commitedRequirement,
							int 		designedRequirement,
							int 		codedRequirement,
							int 		testedRequirement,
							int 		deployedRequirement	,
							int 		acceptedRequirement	,
							int 		totalDefect,
							int 		totalWeightedDefect	,
							int 		fatalPendingDefect,
							int 		seriousPendingDefect,
							int 		mediumPendingDefect	,
							int 		cosmeticPendingDefect,
							int 		totalFatalDefect,
							int 		totalSeriousDefect	,
							int 		totalMediumDefect	,
							int 		totalCosmeticDefect

			) throws SQLException, RemoteException;

	public String getStart_date() throws RemoteException;
	public String getBase_finish()throws RemoteException;
	public String getPlan_finish() throws RemoteException;
	public String getActual_finish() throws RemoteException;


	public int getBase_effort()throws RemoteException;
	public int getPlan_effort() throws RemoteException;
	public int getActual_effort() throws RemoteException;
	public int getPer_complete() throws RemoteException;

	public String[] getGroup() throws RemoteException, SQLException;

	public int getTotalRequirement() throws RemoteException;

	public int getCommittedRequirement() throws RemoteException;

	public int getDesignedRequirement() throws RemoteException;

	public int getCodedRequirement()throws RemoteException;

	public int getTestedRequirement()throws RemoteException;

	public int getDeployedRequirement() throws RemoteException;

	public int getAcceptedRequirement() throws RemoteException;

	public int getTotalDefect() throws RemoteException;

	public int getTotalWeightedDefect() throws RemoteException;

	public int getFatalPendingDefect() throws RemoteException;

	public int getSeriousPendingDefect() throws RemoteException;

	public int getMediumPendingDefect()throws RemoteException;

	public int getCosmeticPendingDefect() throws RemoteException;

	public int getTotalFatalDefect() throws RemoteException;

	public int getTotalSeriousDefect() throws RemoteException;
	public int getTotalMediumDefect() throws RemoteException;

	public int getTotalCosmeticDefect()throws RemoteException;

  //Thaison added here (13 Nov 01 - 10.30am)
  public void getProjectInfo(int ProjectID) throws RemoteException, SQLException;
  //Thaison added here (19 Nov 2001 - 9.30pm)
  public void setProjectID(int value) throws RemoteException;
  
    public Collection getProjectDashboard(
        String strType,
        String strGroup,
        String strStatus,
        String strCategory,
        String strDeveloperId,
        int nOrderBy)
        throws SQLException, RemoteException;
}
