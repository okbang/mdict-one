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
import java.util.*;

public interface Milestone extends EJBObject
{
/*	
	public void getlist(int ProjectID, int index, int orderby)		throws RemoteException, SQLException;
	public boolean nextMilestone() throws RemoteException, SQLException;
	public int getID() throws RemoteException;
	public String getName() throws RemoteException;
	public int getcomplete() throws RemoteException;
	public String getBase_Start_date() throws RemoteException;
	public String getPlan_Start_date() throws RemoteException;
	public String getActual_Start_date() throws RemoteException;
	public String getBase_finish_date() throws RemoteException;
	public String getPlan_finish_date() throws RemoteException;
	public String getActual_finish_date() throws RemoteException;
	public int getBase_effort() throws RemoteException;
	public int getPlan_effort() throws RemoteException;
	public int getActual_effort() throws RemoteException;
	public int getMaxPage(int ProjectID) throws RemoteException, SQLException;
	public String getDescription() throws RemoteException;
*/	
	public void deleteMilestone(String MilestoneID)
		throws RemoteException, SQLException;
	public void addMilestone(
		int Project_ID,
		String Name,
		int Complete,
		String Base_start_date,
		String Plan_start_date,
		String Actual_start_date,
		String Base_finish_date,
		String Plan_finish_date,
		String Actual_finish_date,
		int Base_effort,
		int Plan_effort,
		int Actual_effort,
		String description)
		throws RemoteException, SQLException;
	public MilestoneInfo getMilestone(int MilestoneID) throws SQLException, RemoteException;
	public void updateMilestone(
		int Milestone_ID,
		String Name,
		int Complete,
		String Base_start_date,
		String Plan_start_date,
		String Actual_start_date,
		String Base_finish_date,
		String Plan_finish_date,
		String Actual_finish_date,
		int Base_effort,
		int Plan_effort,
		int Actual_effort,
		String description)
		throws RemoteException, SQLException;
		
	
	public Collection getMilestoneList(int ProjectID, int index, int orderby)	throws RemoteException, SQLException;
}