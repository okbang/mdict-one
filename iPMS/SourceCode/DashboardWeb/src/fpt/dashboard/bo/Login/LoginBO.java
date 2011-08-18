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
 
 package fpt.dashboard.bo.Login;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

//import fpt.dashboard.Login.DashboardLogin;
//import fpt.dashboard.Login.DashboardLoginHome;
import fpt.dashboard.Login.DashboardLoginEJBLocal;
import fpt.dashboard.Login.DashboardLoginEJBLocalHome;

import fpt.dashboard.bean.UserInfoBean;
import fpt.dashboard.constant.JNDINaming;
public class LoginBO
{
	//member variables
//	private DashboardLogin homeLogin = null;
//	private DashboardLogin objLogin = null;
	
	//HaiMM
	private DashboardLoginEJBLocalHome homeLogin = null;
	private DashboardLoginEJBLocal objLogin = null;
	
	private static Logger logger = Logger.getLogger(LoginBO.class.getName());
	
	public LoginBO()
	{
	}
	//EJB Bean Specific methods ...
	private void getEJBHome() throws NamingException
	{
		try
		{
			Context ic = new InitialContext();
			java.lang.Object objref = ic.lookup(JNDINaming.LOGIN);
			logger.info(objref.getClass().getName());
			// HaiMM
			//homeLogin = (DashboardLoginHome) javax.rmi.PortableRemoteObject.narrow(objref, DashboardLoginHome.class);
			homeLogin = (DashboardLoginEJBLocalHome) (objref);
		}
		catch (NamingException ex)
		{
			logger.error("LoginBO.getEJBHome() error! -- "+ ex.getMessage() + "---" + ex.getResolvedName());
			logger.debug("LoginBO.getEJBHome() error! -- "+ ex.getMessage() + "---" + ex.getResolvedName());
		}
		catch (Exception ex)
		{
			logger.debug("LoginBO.getEJBHome() error! -- " + ex.getMessage());
			ex.printStackTrace();
		}
	} //getEJBHome
	private DashboardLoginEJBLocal getEJBRemote() throws Exception // HaiMM - from DashboardLogin to DashboardLoginEJBLocal 
	{
		try
		{
			objLogin = (DashboardLoginEJBLocal)homeLogin.create(); // HaiMM
			
			logger.debug("After getting objLogin.");
		}
		catch (Exception ex)
		{
			logger.error("LoginBO.getEJBRemote() error! -- " + ex.getMessage());
			logger.debug("LoginBO.getEJBRemote() error! -- " + ex.getMessage());
		}
		return objLogin;
	} //getEJBRemote
	
	
	/**
		 * Validate login by user/pass 
		 * @author  Nguyen Thai Son
		 * @version  24 October, 2002
		 * @param   strAccount String: user name.
		 * @param   strPassword String: user password
		 */
	public UserInfoBean checkLogin(String strAccount, String strPassword)
	{
		UserInfoBean beanUserInfo = new UserInfoBean();
		
		int per = -1;
		String groupName = "";
		String strRole = "";
		String strDeveloperId = "";
		try
		{
			getEJBHome();
			objLogin = getEJBRemote();
			
			per = objLogin.CheckLogin(strAccount, strPassword);
			groupName = objLogin.getGroup();
			strRole = objLogin.getSRole();
            strDeveloperId = objLogin.getDeveloperId();
		}
		catch (Exception ex)
		{
			logger.error("Exception occurs in LoginBO.checkLogin(): " + ex.toString());
		}
		
		if (per > 0) //Login Successfull:
		{
			//Set value for outView:
			beanUserInfo.setAccount(strAccount);
			beanUserInfo.setGroupName(groupName);
			beanUserInfo.setRole(String.valueOf(per));
			beanUserInfo.setSRole(strRole);
            beanUserInfo.setDeveloperId(strDeveloperId);
		}
		else //Login Fail
		{
			beanUserInfo.setAccount(strAccount);
			beanUserInfo.setMessage("You have no permission to access Dashboard.");
		}
		
		return beanUserInfo;
	}
}