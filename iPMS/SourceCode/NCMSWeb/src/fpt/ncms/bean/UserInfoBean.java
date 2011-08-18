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
 * @(#)UserInfoBean.java 13-Mar-03
 */


package fpt.ncms.bean;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import fpt.ncms.constant.NCMS;
import fpt.ncms.framework.core.SessionInfoBaseBean;
import fpt.ncms.model.NCModel;

/**
 * Class UserInfoBean
 * Bean for storing user's information
 * @version 1.0 13-Mar-03
 * @author
 */
public final class UserInfoBean {
    private int nUserId;
    private String strRole;
    private String strLoginName = "";
    private String strFullName;
    private String strGroupName = "";
    private String strStatus = "";
    private int n_Location;
    /** Message to deliver on screen */
    private String strMessage = "";
    /** Indicator for type of view due to NC */
    private String strTypeOfView = "";
    /** NC listing mode (system default/personal) */
    private String strListingMode = NCMS.SYSTEM_MODE;
    /** NC type list */
    private ArrayList alNCTypeList;
    /** session object */
    public SessionInfoBaseBean sessionInfoObject = null;
    public static final String __SESSION_INFORMATION_OBJECT_NAME =
            "__SESSION_INFORMATION_OBJECT__";
    
    // This user can change this NC or not
    private boolean b_ModifyEnabled = true;
    // This NC can change status to Open or not
    private boolean b_OpenEnabled = true;
    // This NC can change status to Assigned or not
    private boolean b_AssignEnabled = true;
    // Current working NC
    private NCModel m_currentNC = null;
    // SMTP Host
    private String strSMTPHost = null;
    
    /**
     * UserInfoBean
     * Class constructor
     */
    public UserInfoBean() {
    }

    /**
     * setUserID
     * Set user's identify number
     * @param   num - user's identify number
     */
    public final void setUserId(int num) {
        nUserId = num;
    }

    /**
     * getUserID
     * Get user's identify number
     * @return  user's identify number
     */
    public final int getUserId() {
        return nUserId;
    }

    /**
     * setLoginName
     * Set user's login name
     * @param   str - user's login name
     */
    public final void setLoginName(String str) {
        strLoginName = str;
    }

    /**
     * getLoginName
     * Get user's login name
     * @return  user's login name
     */
    public final String getLoginName() {
        return strLoginName;
    }

    /**
     * setFullName
     * Set user's name
     * @param   str - user's full name
     */
    public final void setFullName(String str) {
        strFullName = str;
    }

    /**
     * getFullName
     * Get user's name
     * @return  user's full name
     */
    public final String getFullName() {
        return strFullName;
    }

    /**
     * setRole
     * Set user's role
     * @param   role - user's role
     */
    public final void setRole(String role) {
        strRole = role;
    }

    /**
     * getRole
     * Get user's role
     * @return  user's role
     */
    public final String getRole() {
        return strRole;
    }

    /**
     * getRoleName
     * Get user's role description
     * @return  description of user role
     */
    public final String getRoleName() {
        String strRoleName = "";
        if (strRole != null) {
            if (strRole.length() > 8) {
                if ("1".equals(strRole.substring(4, 5))) {//PQA
                    strRoleName = NCMS.ROLE_PQA;
                }
                else if ("1".equals(strRole.substring(1, 2))) {//Project leader
                    strRoleName = NCMS.ROLE_REVIEWER;
                }
                else if ("1".equals(strRole.substring(0, 1))) {//Developer
                    strRoleName = NCMS.ROLE_ASSIGNEE;
                }
                else if ("1".equals(strRole.substring(6, 7))) {//External user
                    strRoleName = NCMS.ROLE_CREATOR;
                }
            }
        }
        return strRoleName;
    }

    /**
     * getGroupName
     * Get user's group
     * @return  user's group
     */
    public final String getGroupName() {
        return strGroupName;
    }

    /**
     * setGroupName
     * Set user's group
     * @param   data - user's group
     */
    public final void setGroupName(String data) {
        if (data != null) {
            strGroupName = data;
        }
    }

    /**
     * getMessage
     * Get message to deliver
     * @return  Message to deliver
     */
    public final String getMessage() {
        return (strMessage == null ? "" : strMessage);
    }

    /**
     * setMessage
     * Set message to deliver
     * @param   inData - message to deliver
     */
    public final void setMessage(String inData) {
        if (inData != null) {
            strMessage = inData;
        }
    }

    /**
     * getSessionInfoObject
     * Get user's information stored on session
     * @param   session object
     * @return  bean that contains user's information
     */
    static public SessionInfoBaseBean getSessionInfoObject(
            HttpSession webSession) {
        SessionInfoBaseBean sessionInfoObject =
                (SessionInfoBaseBean)webSession.getAttribute(
                        __SESSION_INFORMATION_OBJECT_NAME);
        if (sessionInfoObject == null) {
            sessionInfoObject = setSessionInfoObject(
                    webSession, sessionInfoObject);
        }
        return sessionInfoObject;
    }

    /**
     * setSessionInfoObject
     * Set user's information stored on session
     * @param   webSession - session object
     * @param   sessionInfoObject - bean that contains user's information
     * @return  bean that contains user's information
     */
    static public SessionInfoBaseBean setSessionInfoObject(
            HttpSession webSession, SessionInfoBaseBean sessionInfoObject) {
        SessionInfoBaseBean mySessionInfoObject = sessionInfoObject;
        if (mySessionInfoObject == null) {
            mySessionInfoObject = new SessionInfoBaseBean();
        }
        webSession.setAttribute(__SESSION_INFORMATION_OBJECT_NAME,
                mySessionInfoObject);
        return mySessionInfoObject;
    }

    /**
     * getTypeOfView
     * Get type of view used by user
     * @return  type of view used by user
     */
    public final String getTypeOfView() {
        return this.strTypeOfView;
    }

    /**
     * setTypeOfView
     * Set type of view used by user
     * @param   data - type of view used by user
     */
    public final void setTypeOfView(String data) {
        this.strTypeOfView = data;
    }

    /**
     * getStatus
     * Get user's status
     * @return  user's status
     */
    public final String getStatus() {
        return strStatus;
    }

    /**
     * setStatus
     * Set user's status
     * @param   data - user's status
     */
    public final void setStatus(String data) {
        if (data != null) {
            strStatus = data;
        }
    }

    /**
     * getListingMode
     * Get mode user's using
     * @return  mode
     */
    public final String getListingMode() {
        return strListingMode;
    }

    /**
     * setListingMode
     * Set mode user's using
     * @param   inData - mode
     */
    public final void setListingMode(String inData) {
        if (!"".equals(inData) && (inData != null)) {
            strListingMode = inData;
        }
    }
    /**
     * Returns the location.
     * @return int
     */
    public int getLocation() {
        return n_Location;
    }

    /**
     * Sets the location.
     * @param location The location to set
     */
    public void setLocation(int location) {
        n_Location = location;
    }

    /**
     * Returns the modifiable.
     * @return boolean
     */
    public boolean isModifyEnabled() {
        return b_ModifyEnabled;
    }

    /**
     * Sets the modifiable.
     * @param modifiable The modifiable to set
     */
    public void setModifyEnabled(boolean modifyEnabled) {
        b_ModifyEnabled = modifyEnabled;
    }

    /**
     * Returns the openEnabled.
     * @return boolean
     */
    public boolean isOpenEnabled() {
        return b_OpenEnabled;
    }

    /**
     * Sets the openEnabled.
     * @param openEnabled The openEnabled to set
     */
    public void setOpenEnabled(boolean openEnabled) {
        b_OpenEnabled = openEnabled;
    }

    /**
     * Returns the assignEnabled.
     * @return boolean
     */
    public boolean isAssignEnabled() {
        return b_AssignEnabled;
    }

    /**
     * Sets the assignEnabled.
     * @param assignEnabled The assignEnabled to set
     */
    public void setAssignEnabled(boolean assignEnabled) {
        b_AssignEnabled = assignEnabled;
    }

    /**
     * Returns the currentNC.
     * @return NCModel
     */
    public NCModel getCurrentNC() {
        return m_currentNC;
    }

    /**
     * Sets the currentNC.
     * @param currentNC The currentNC to set
     */
    public void setCurrentNC(NCModel currentNC) {
        m_currentNC = currentNC;
    }

    /**
     * @return
     */
    public String getSMTPHost() {
        return strSMTPHost;
    }

    /**
     * @param string
     */
    public void setSMTPHost(String string) {
        strSMTPHost = string;
    }

 }