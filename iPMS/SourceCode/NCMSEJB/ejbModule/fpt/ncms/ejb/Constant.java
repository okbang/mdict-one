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
 * @(#)Constant.java 12-Mar-03
 */


package fpt.ncms.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

/**
 * Remote interface for Enterprise Bean: Constant
 * @version 1.0 12-Mar-03
 * @author
 */
public interface Constant extends javax.ejb.EJBObject {

    /**
     * queryConstantByID
     * Retrieve constant information by ident.numbers
     * @param   strConstantID - list of constant ident.numbers
     * @return  list of constant information
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList queryConstantByID(String strConstantID)
            throws RemoteException, SQLException;

    /**
     * queryConstantByType
     * List constants by its type
     * @param   strConstantType - type of Constant
     * @return  list of constants of this type
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList queryConstantByType(String strConstantType, int nUsage)
            throws RemoteException, SQLException;

    /**
     * queryConstant
     * List constants by certain condition
     * @param   strConstantType - sorting condition
     * @param   nFromRow - lower row number limit
     * @param   nToRow - upper row number limit
     * @return  list of constants
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList queryConstant(String strConstantType, int nUsage,
            int nFromRow, int nToRow) throws RemoteException, SQLException;

    /**
     * getNumByType
     * Get number of constant by its type
     * @param   strConstantType - sorting condition
     * @return  number of constants
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList queryConstantType(int nUsage, boolean isShowAll,
                                       String strExcludeConstants)
            throws RemoteException, SQLException;

    /**
     * queryConstantType
     * List all constant types
     * @return  list of constant types
     * @throws  RemoteException
     * @throws  SQL Exception
     */
    public int getNumByType(String strConstantType, int nUsage, boolean isShowAll)
            throws RemoteException, SQLException;

    /**
     * deleteConstant
     * Delete constant by ident.number
     * @param   strConstantID - constant ident.number
     * @return  -1: constant is linked to some NC or constant doesn't exist
     * @return  others: constant was deleted successfully
     * @throws  RemoteException
     * @throws  SQLException
     */
    public int deleteConstant(String strConstantID)
            throws RemoteException, SQLException;

    /**
     * insertConstant
     * Insert new constant to database
     * @param   strConstantType - type of inserted constant
     * @param   strDescription - value of inserted constant
     * @return  -2: parameters are wrong
     * @return  -1: constant already exists
     * @return  others: inserting was successful
     * @throws  RemoteException
     * @throws  SQLException
     */
    public int insertConstant(String strConstantType, String strDescription,
            int nUsage) throws RemoteException, SQLException;

    /**
     * updateConstant
     * Update value of constant
     * @param   strDescription - value of updated constant
     * @param   strConstantID - ident.number of updated constant
     * @return  -2: parameters are wrong
     * @return  -1: constant of same type and same value already exists
     * @return  others: updating was successful
     * @throws  RemoteException
     * @throws  SQLException
     */
    public int updateConstant(String strDescription, String strConstantID,
                              int nUsage) throws RemoteException, SQLException;
}