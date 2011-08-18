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
 * @(#)Common.java 12-Mar-03
 */


package fpt.ncms.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

import fpt.ncms.framework.core.SessionInfoBaseBean;

/**
 * Remote interface for Enterprise Bean: Common
 */
public interface Common extends javax.ejb.EJBObject {

    /**
     * getUserInfo
     * Check user by account and password
     * @param   strAccount - account name
     * @param   strPassword - password
     * @param   nLocation - login mode (user/admin)
     * @return  information about user
     * @throws  RemoteException
     * @throws  SQLException
     */
    public SessionInfoBaseBean getUserInfo(String strAccount,
            String strPassword, int nLocation)
            throws RemoteException, SQLException;

    /**
     * getUserInfo
     * Get user information by account
     * @param   strAccount - account name
     * @return  information about user
     * @throws  RemoteException
     * @throws  SQLException
     */
    public SessionInfoBaseBean getUserInfo(String strAccount)
            throws RemoteException, SQLException;

    /**
     * getUserList
     * Get list of users
     * @param   strRole - role of user to list
     * @param   nLocation - system that get user list
     * @return  list of users
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList getUserList(String strRole, int nLocation)
            throws RemoteException, SQLException;

    /**
     * getGroupList
     * Get list of group names
     * @return  list of group names
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList getGroupList() throws RemoteException, SQLException;

    /**
     * getProjectList
     * Get list of project names
     * @param   strGroup - name of group listed projects related to
     * @return  list of project names
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList getProjectList(String strGroup, int nType, int nLocation)
            throws RemoteException, SQLException;

    /**
     * getParameter
     * Get value of a parameter from Parameters table
     * @param   strParameter - parameter name
     * @return  parameter value
     * @throws  RemoteException
     * @throws  SQLException
     */
    public String getParameter(String strParameter) throws RemoteException, SQLException;

    /**
     * getMailList
     * Get list of mail addresses of requested group
     * @param   nRequestToID - requested group
     * @return  list of project names
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList getMailList(int nRequestToID) throws RemoteException, SQLException;
}