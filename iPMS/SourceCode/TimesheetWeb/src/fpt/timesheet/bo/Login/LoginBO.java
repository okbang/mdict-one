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
 
 package fpt.timesheet.bo.Login;

import java.util.Collection;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

//import fpt.timesheet.LoginTran.ejb.Login;
import fpt.timesheet.LoginTran.ejb.LoginEJBLocal;
import fpt.timesheet.LoginTran.ejb.LoginEJBLocalHome;
//import fpt.timesheet.LoginTran.ejb.LoginHome;
import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.constant.DATA;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.core.SessionInfoBaseBean;

public class LoginBO {

	private LoginEJBLocal objLogin = null; 
    private LoginEJBLocalHome homeLogin = null;
	
	private String strClassName = "LoginEJB";
    private static Logger logger = Logger.getLogger(LoginBO.class.getName());

    /**
     * Method Constructor
     */
    public LoginBO() {
    }

    /**
     * Method getLoginEJB.
     * @throws NamingException
     */
    private void getLoginEJB() throws NamingException {
        try {
            if (homeLogin == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.LOGIN);

//              homeLogin = (LoginHome) javax.rmi.PortableRemoteObject.narrow(objref, LoginHome.class);
				homeLogin = (LoginEJBLocalHome)(objref);
                if (objLogin == null)
                    objLogin = homeLogin.create();
            }
        }
        catch (NamingException ex) {
            logger.debug("LoginBO.getLoginEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
            throw ex;
        }
        catch (Exception ex) {
            logger.debug("LoginBO.getLoginEJB() error! -- " + ex.getMessage());
            ex.printStackTrace();
        }
    }

	private boolean validLoginRole(String strRole, int intLocation) {
		if (strRole.length() < 5) {
			return false;
		}
		// Timesheet
		if (intLocation == 0) {
			if (strRole.substring(0, 1).equals("1") ||	// FMS user
                strRole.substring(8, 9).equals("1") ||  // Communicator at Group and Project
				strRole.substring(6, 7).equals("1") ) { // External User at Group and Project
				return true;
			}
		}
		// Approve external project/Approve internal project
		else if  ((intLocation == 1) || (intLocation == 3)) {
			// GL/PL
			if (strRole.substring(1, 2).equals("1") || 
				strRole.substring(2, 3).equals("1") ) {
				return true;
			}
		}
		// QA approve/Administration
		else if  ((intLocation == 2) || (intLocation == 4)) {
			if (strRole.substring(4, 5).equals("1")) {
				return true;
			}
		}
		// All user except external user
		else if (intLocation == 5) {
			if (strRole.substring(0, 1).equals("1") ||
				strRole.substring(1, 2).equals("1") ||
				strRole.substring(2, 3).equals("1") ||
				strRole.substring(3, 4).equals("1") ||
				strRole.substring(4, 5).equals("1") ||
				strRole.substring(5, 6).equals("1") ||
                strRole.substring(8, 9).equals("1")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method getTypeOfView
	 * @param beanUserInfo
	 * @param intLocation
	 */
	private void getTypeOfView(UserInfoBean beanUserInfo, int intLocation) {
		String strTypeOfView = "";

		switch (intLocation) {
			case 0: // Input Timesheet..
				{
					strTypeOfView = DATA.VIEW_TS_LIST;
					break;
				}
			case 1: // Approved By Project Leaders..
				{
					strTypeOfView = DATA.VIEW_PL_LIST;
					break;
				}
			case 2: // Approved By Quality Assurance..
				{
					strTypeOfView = DATA.VIEW_QA_LIST;
					break;
				}
			case 3: // Approved for Other Type Projects..
				{
					strTypeOfView = DATA.VIEW_GL_LIST;
					break;
				}
			case 4:
				{
					strTypeOfView = DATA.VIEW_MAPPING_LIST;
					break;
				}
			case 5:
				{
					strTypeOfView = DATA.VIEW_EXEMPTION;
					break;
				}
		}
		beanUserInfo.setTypeOfView(strTypeOfView);
	}

	/**
	 * Method setUserInfo
	 * @param beanUserInfo
	 * @param strAccount
	 * @param intLocation
	 * @param it
	 */
	private void setUserInfo(UserInfoBean beanUserInfo, String strAccount, int intLocation, Iterator it) {
		SessionInfoBaseBean beanInfo = (SessionInfoBaseBean) it.next();
		String strRole = beanInfo.getRole();
		if (validLoginRole(strRole, intLocation)) {
			beanUserInfo.setUserId(beanInfo.getUserID());
			beanUserInfo.setRole(beanInfo.getRole());
			beanUserInfo.setLoginName(beanInfo.getLoginName());
			beanUserInfo.setFullName(beanInfo.getFullname());
			beanUserInfo.setGroupName(beanInfo.getGroupName());
			String strStatus = beanInfo.getStatus();
			beanUserInfo.setStatus("".equals(strStatus) ? DATA.VALUE_STAFF : strStatus);
			getTypeOfView(beanUserInfo, intLocation);
		}
		else {
			intLocation = -1;
			beanUserInfo.setLoginName(strAccount);
			beanUserInfo.setMessage(TIMESHEET.MSG_INVALID_USER);
		}
	}

    /**
     * Method checkLogin
	 * @param strAccount
	 * @param strPassword
	 * @param strLocation
	 * @return beanUserInfo
	 */
	public UserInfoBean checkLogin(String strAccount, String strPassword, String strLocation) {
        UserInfoBean beanUserInfo = new UserInfoBean();
        int intLocation = 0;
		Collection colExternalUserInfo = null;
		Collection colUserInfo = null;
        Collection colCommunicatorUserInfo = null;

        try {
            intLocation = Integer.parseInt(strLocation);
            getLoginEJB();

            //Get External User Profile
			colExternalUserInfo = objLogin.getExternalUserInfo(strAccount, strPassword);
            if (colExternalUserInfo != null && !colExternalUserInfo.isEmpty()) {
				Iterator it = colExternalUserInfo.iterator();
				while(it.hasNext()) {
					setUserInfo(beanUserInfo, strAccount, intLocation, it);
				}
			}
            else {
                //Get Communicator User Profile
                colCommunicatorUserInfo = objLogin.getCommunicatorUserInfo(strAccount, strPassword);
                if (colCommunicatorUserInfo != null & !colCommunicatorUserInfo.isEmpty()) {
                    Iterator it = colCommunicatorUserInfo.iterator();
                    while(it.hasNext()) {
                        setUserInfo(beanUserInfo, strAccount, intLocation, it);
                    }
                }
                //Get User Profile
                else {
                    colUserInfo = objLogin.getUserInfo(strAccount, strPassword);
                    if (colUserInfo != null && !colUserInfo.isEmpty()) {
                        Iterator it = colUserInfo.iterator();
                        while(it.hasNext()) {
                            setUserInfo(beanUserInfo, strAccount, intLocation, it);
                        }
                    }
                }
            }
        }
        catch (NumberFormatException ex) {
            intLocation = -1;
            beanUserInfo.setMessage(TIMESHEET.MSG_INVALID_LOCATION);
            logger.debug("NumberFormatException occurs: strLocation = " + strLocation);
        }
        catch (Exception ex) {
            logger.error("Exception occurs in LoginBO.checkLogin(): " + ex.toString());
        }
        return beanUserInfo;
    }
}