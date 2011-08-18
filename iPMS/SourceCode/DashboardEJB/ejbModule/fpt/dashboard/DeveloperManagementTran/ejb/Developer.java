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

package fpt.dashboard.DeveloperManagementTran.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBObject;

public interface Developer extends EJBObject {

    public String getName() throws RemoteException;
    public String getDesign() throws RemoteException;
    public String getGroup() throws RemoteException;
    public String getRole() throws RemoteException;
    public String getPass() throws RemoteException;
    public String getAcc() throws RemoteException;
    public String getStatus() throws RemoteException;
    public String getEmail() throws RemoteException;
    public String getStartDate() throws RemoteException;
    public String getQuitDate() throws RemoteException;

    public void setName(String name) throws RemoteException;
    public void setDesign(String design) throws RemoteException;
    public void setGroup(String group) throws RemoteException;
    public void setRole(String role) throws RemoteException;
    public void setPass(String pass) throws RemoteException;
    public void setAcc(String acc) throws RemoteException;
    public void setStatus(String status) throws RemoteException;
    public void setEmail(String email) throws RemoteException;
    public void setStartDate(String strDate) throws RemoteException;
    public void setQuitDate(String strDate) throws RemoteException;

    public int insertRow(String name, String design, String group, String role, String pass, String acc, String strStatus, String strEmail, String StartDate)
        throws RemoteException, SQLException;
    public void deleteRow(Integer key) throws RemoteException, SQLException;
    public void loadRow(Integer key) throws RemoteException, SQLException;
    public int storeRow(Integer key) throws RemoteException, SQLException;

    public Collection selectAllKey(String condi, String strStatus, String strSortBy, String strDirection) throws RemoteException, SQLException;
    public Collection selectGroup() throws RemoteException, SQLException;

	public int countDev(String strGroup, String strStatus) throws RemoteException, SQLException;
    //public int countDevelopers(String strGroup) throws RemoteException, SQLException;
    public ArrayList getAccountList() throws RemoteException, SQLException, Exception;
    public ArrayList getDeveloperList(String strSortBy, String strDirection, int nPage) throws RemoteException, SQLException, Exception;
    public int getDeveloperNumber() throws RemoteException, SQLException, Exception;
}
