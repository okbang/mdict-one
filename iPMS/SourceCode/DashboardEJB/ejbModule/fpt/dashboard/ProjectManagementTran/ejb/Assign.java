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
 *
 * Copyright 2001 FPT. All Rights Reserved.
 * Author: Duong Thanh Nhan
 *
 */
package fpt.dashboard.ProjectManagementTran.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.*;
import java.sql.*;

public interface Assign extends EJBObject {

	public Integer getProjectID() throws RemoteException;
	public Integer getAssignID() throws RemoteException;
	public String getProjectName() throws RemoteException;
	public String getProjectLeader() throws RemoteException;
	public String getProjectCode() throws RemoteException;
	public String getProjectStart() throws RemoteException;
	public String getProjectFinish() throws RemoteException;
	public String getDesc() throws RemoteException;
	public Integer getType() throws RemoteException;
	public Integer getStartWeek() throws RemoteException;
	public Integer getEndWeek() throws RemoteException;
	public int getUsage() throws RemoteException;
	public String getRes() throws RemoteException;
	public void getProjectInfor(Integer ProjectID)
		throws RemoteException, SQLException;

	public Integer getDeveloperID() throws RemoteException;
	public String getDeveloperName() throws RemoteException;
	public String getBegin() throws RemoteException;
	public String getEnd() throws RemoteException;

	public void setProjectID(Integer ID) throws RemoteException;
	public void setProjectName(String Name) throws RemoteException;
	public void setProjectLeader(String Name) throws RemoteException;
	public void setProjectCode(String Code) throws RemoteException;
	public void setProjectStart(String End) throws RemoteException;
	public void setProjectFinish(String End) throws RemoteException;
	public void setDesc(String Desc) throws RemoteException;
	public void setType(Integer Type) throws RemoteException;
	public void setUsage(int Usage) throws RemoteException;
	public void setRes(String res) throws RemoteException;

	public void setDeveloperID(Integer ID) throws RemoteException;
	public void setDeveloperName(String Name) throws RemoteException;

	public void setBegin(String Begin) throws RemoteException;
	public void setEnd(String End) throws RemoteException;

	public void insertRow(
		Integer ProjectID,
		Integer DeveloperID,
		String Begin,
		String End,
		Integer Type)
		throws RemoteException, SQLException;
	public void deleteRow(Integer AssignID)
		throws RemoteException, SQLException;
	public void loadRow(Integer AssignID) throws RemoteException, SQLException;
	public void loadOtherRow(Integer AssignID)
		throws RemoteException, SQLException;
	public void storeRow(Integer AssignID)
		throws RemoteException, SQLException;

	public Collection selectAllKey() throws RemoteException, SQLException;
	public Collection selectByDeveloper() throws RemoteException, SQLException;
	public Collection selectBussyDeveloper(
		String startPeriod,
		String endPeriod,
		String Condi)
		throws RemoteException, SQLException;
	public Collection selectFreeDeveloper(String strID, String Condi)
		throws RemoteException, SQLException;
	public Collection selectOtherByDeveloper()
		throws RemoteException, SQLException;

}
