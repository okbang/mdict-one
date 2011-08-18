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
 * @(#)View.java 03-Apr-03
 */


package fpt.ncms.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

/**
 * Remote interface for Enterprise Bean: View
 */
public interface View extends javax.ejb.EJBObject {

    /**
     * queryViewByID
     * Retrieve view information by its ident.number
     * @param   strViewID - view ident.number
     * @return  view information
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList queryViewByID(String strViewID)
            throws RemoteException, SQLException;

    /**
     * queryViewByAccount
     * Retrieve view information by user's account
     * @param   strAccount - user's account
     * @return  view information
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList queryViewByAccount(String strAccount)
            throws RemoteException, SQLException;

    /**
     * insertView
     * Add new view to database
     * @param   strAccount - user's account
     * @param   strTitle - view title
     * @param   strFields - field names
     * @param   strOrderBy - sorting condition
     * @return  -1: view with such title already exists
     * @return  other: inserting was successful
     * @throws  RemoteException
     * @throws  SQLException
     */
    public int insertView(String strAccount, String strTitle, String strFields,
            String strOrderBy) throws RemoteException, SQLException;

    /**
     * deleteView
     * Remove a view from database
     * @param   strViewID - view ident.number
     * @return  number of removed records (1)
     * @throws  RemoteException
     * @throws  SQLException
     */
    public int deleteView(String strViewID)
            throws RemoteException, SQLException;

    /**
     * updateView
     * Update view
     * @param   strViewID - view ident.number
     * @param   strTitle - view title
     * @param   strFields - field names
     * @param   strOrderBy - sorting condition
     * @param   strAccount - user's account
     * @return  -1: view with such title already exists
     * @return  other: updating was successful
     * @throws  RemoteException
     * @throws  SQLException
     */
    public int updateView(String strViewID, String strTitle, String strFields,
            String strOrderBy, String strAccount)
            throws RemoteException, SQLException;
}