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
 
 package fpt.dashboard.bo.ComboBox;

/**
 * @Title:        ComboBO.java
 * @Description:  
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 24, 2002
 * @Modified date:
 */

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetail;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.ProjectDetailHome;
import fpt.dashboard.combo.ConstantCombo;
import fpt.dashboard.combo.ConstantComboEJBLocal;
import fpt.dashboard.combo.ConstantComboEJBLocalHome;
import fpt.dashboard.combo.ConstantComboHome;
import fpt.dashboard.constant.JNDINaming;

public class ComboBO
{
//	private ProjectDetailHome homeProject = null;
//	private ProjectDetail objProject = null;
//	
//	private ConstantComboHome homeCombo = null;
//  private ConstantCombo remoteCombo = null;

//  HaiMM ================    
	private ProjectDetailEJBLocalHome homeProject = null;
	private ProjectDetailEJBLocal objProject = null;
	
	private ConstantComboEJBLocalHome homeCombo = null;
	private ConstantComboEJBLocal remoteCombo = null;

//  =======================

	private static Logger logger = Logger.getLogger(ComboBO.class.getName());
	
	public ComboBO()
	{
	
	}
	
	//**************** HELPER Methods *****************
	// EJB Bean Project methods ...
	private void getEJBHome() throws NamingException
	{
		try
		{
			if (homeProject == null)
			{
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.PROJECT_DETAIL);
				homeProject = (ProjectDetailEJBLocalHome)(ref); // HaiMM
			}
		}
		catch (NamingException ex)
		{
			logger.error("NamingException occurs in ComboBO.getEJBHome(). " + ex.getResolvedName());	
			throw ex;
		}
	} //getEJBHome
	
	private ProjectDetailEJBLocal getEJBRemote() throws Exception // HaiMM
	{ 
		objProject = (ProjectDetailEJBLocal) homeProject.create(); // HaiMM
		return objProject;
	} //getEJBRemote
	
    /**
     * Get ConstantCombo home,remote
     * @throws RemoteException
     * @throws CreateException
     */
    private void initConstantComboEJB()
            throws NamingException, RemoteException, CreateException {
        try {
            Context ic = new InitialContext();
            Object ref = ic.lookup(JNDINaming.CONSTANT_COMBO);
            homeCombo = (ConstantComboEJBLocalHome) (ref);
            remoteCombo = homeCombo.create();
        }
        catch (NamingException ex) {
            logger.error("NamingException occurs in ComboBO.getConstantComboEJB(). " + ex.getResolvedName());    
            throw ex;
        }
        catch (CreateException ex) {
            logger.error("CreateException occurs in ComboBO.getConstantComboEJB(). " +
                    ex.getMessage());  
            throw ex;
        }
    }
	
    /**
	 * Get all groups.
	 * @author  Nguyen Thai Son.
	 * @version  09 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public Collection getGroupList() throws Exception
	{
		Collection collResult = null;
		
		try
		{
			getEJBHome();
			getEJBRemote();
			
			collResult = objProject.listGroup();
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ComboBO.getGroupList(): " + ex.toString());
			logger.error(ex);
		}
		
		return collResult;
	}
	
	/**
	 * Get all leaders.
	 * @author  Nguyen Thai Son.
	 * @version  09 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
    public Collection getLeaderList() throws Exception
    {
        Collection collResult = null;
        
        try
        {
            getEJBHome();
            getEJBRemote();
            
            collResult = objProject.listLeader();
        }
        catch (javax.naming.NamingException e)
        {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            logger.debug("Exception occurs in ComboBO.getLeaderList(): " + ex.toString());
            logger.error(ex);
        }
        
        return collResult;
    }

    /**
     * Get response from responsibility table represented in ConstantRow
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList getResponseList() throws Exception
    {
        ArrayList arlResponse = null;
        
        try {
            initConstantComboEJB();
            arlResponse = remoteCombo.getResponse();
        }
        catch (NamingException ex) {
            logger.error("NamingException occurs in ComboBO.getConstantComboEJB(). " + ex.getResolvedName());    
        }
        catch (RemoteException ex) {
            logger.error("RemoteException occurs in ComboBO.getConstantComboEJB(). " +
                    ex.getMessage());  
        }
        catch (CreateException ex) {
            logger.error("CreateException occurs in ComboBO.getConstantComboEJB(). " +
                    ex.getMessage());  
        }
        
        return arlResponse;
    }
}