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
 
 
// Tran Khanh Trang
// August 20, 2001

package fpt.dashboard.ProjectManagementTran.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.*;
import java.sql.SQLException;


public interface ProjectDetail extends EJBObject {

	public Collection listGroup() throws RemoteException, SQLException;
	public Collection listLeader() throws RemoteException, SQLException;

    public Collection listAll(String group, String status, String type, String cate) throws RemoteException, SQLException;
    public Collection listAssigned(
        String group,
        String status,
        String type,
        String cate,
        String strDeveloperID)
        throws RemoteException, SQLException;
	
    public Collection getHeader(String pId) throws RemoteException, SQLException;
	public Collection getContent(String pId) throws RemoteException, SQLException;
	public Collection getAssignments(String pId) throws RemoteException, SQLException;
	public Collection getMilestones(String pId) throws RemoteException, SQLException;
	public Collection getIssue(String pId) throws RemoteException, SQLException;
	public String getIssueContent(String iId) throws RemoteException, SQLException;

	//////////////

	public Collection getMilestoneList(String strComplete,String fromDate, String toDate, String strUserGroup) throws RemoteException, SQLException;

	//////////////
	public Collection getRequirementChartData(String pId) throws RemoteException, SQLException;
	public Collection getDefectChartData(String pId) throws RemoteException, SQLException;


	//////////////
	public boolean updateProject(String pId,String code,
							String name,
							String leader,
							String startDate,
							String perComplete,
							String baseFinishDate,
							String planFinishDate,
                            String baseEffort,
                            String planEffort,
                            String actualEffort,
							String type,
							String group,
                                                        String status,
                                                        String cate,
                                                        String customer
			) throws RemoteException, SQLException;
    public void closeProject(String pId,
			String actualFinishDate,
			String status,
			String desc
			) throws RemoteException, SQLException;


    public boolean addProject(String code,
							String name,
							String leader,
							String startDate,
							String perComplete,
							String baseFinishDate,
							String planFinishDate,
							String baseEffort,
							String planEffort,
							String actualEffort,
							String type,
							String group,
                            String status,
                            String customer,
                            String cate
							) throws RemoteException, SQLException;

    public void delProject(String pId) throws RemoteException, SQLException;


////////////////////////

    public void addProjectIssue(String projectId,
    								String description,
									String startDate,
									String endDate ) throws RemoteException, SQLException;

	public void updateProjectIssue(String iId,
									String projectId,
									String description,
									String startDate,
									String endDate ) throws RemoteException, SQLException;

    public void delProjectIssue(String iId) throws RemoteException, SQLException;

    public Collection getOngoingList(Calendar cal_From, Calendar cal_To,
                                     String strGroupName)
                                     throws RemoteException, SQLException;
}
