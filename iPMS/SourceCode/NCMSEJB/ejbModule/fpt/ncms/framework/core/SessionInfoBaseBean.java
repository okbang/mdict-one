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
 * @(#)SessionInfoBase.java 13-Mar-03
 */


package fpt.ncms.framework.core;

import java.io.Serializable;

/**
 * Class SessionInfoBaseBean
 * Bean for storing user's information
 * @version 1.0 13-Mar-03
 * @author
 */
public class SessionInfoBaseBean implements Serializable {
    private int nUserID = 0x00;
    private String strLoginName = "";
    private String strLoginPassword = "";
    private String strFullname = "";
    private String strRole = "";
    private String strGroupName = "";
    private String strStatus = "";
    private String strEmail = "";

    /**
     * getUserID
     * get user's identify number
     * @return  user's identify number
     */
    public int getUserID() {
        return nUserID;
    }

    /**
     * setUserID
     * Set user's identify number
     * @param   inData - user's identify number
     */
    public void setUserID(int inData) {
        nUserID = inData;
    }

    /**
     * getLoginName
     * Get user's login name
     * @return  user's login name
     */
    public String getLoginName() {
        if (strLoginName == null) {
            strLoginName = "";
        }
        return strLoginName;
    }

    /**
     * setLoginName
     * Set user's login name
     * @param   inData - user's login name
     */
    public void setLoginName(String inData) {
        if (inData != null) {
            strLoginName = inData;
        }
    }

    /**
     * getLoginPassword
     * Get user's password
     * @return  user's password
     */
    public String getLoginPassword() {
        if (strLoginPassword == null) {
            strLoginPassword = "";
        }
        return strLoginPassword;
    }

    /**
     * setLoginPassword
     * Set user's password
     * @param   inData - user's password
     */
    public void setLoginPassword(String inData) {
        if (inData != null) {
            strLoginPassword = inData;
        }
    }

    /**
     * getFullname
     * Get user's full name
     * @return  user's full name
     */
    public String getFullname() {
        if (strFullname == null) {
            strFullname = "";
        }
        return strFullname;
    }

    /**
     * setFullname
     * Set user's full name
     * @param   inData - user's full name
     */
    public void setFullname(String inData) {
        if (inData != null) {
            strFullname = inData;
        }
    }

    /**
     * getRole
     * Get user's role
     * @return  user's role
     */
    public String getRole() {
        if (strRole == null) {
            strRole = "";
        }
        return strRole;
    }

    /**
     * setRole
     * Set user's role
     * @param   inData - user's role
     */
    public void setRole(String inData) {
        if (inData != null) {
            strRole = inData;
        }
    }

    /**
     * getGroupName
     * Get user's group
     * @return  user's group
     */
    public String getGroupName() {
        return strGroupName;
    }

    /**
     * setGroupName
     * Set user's group
     * @param   inData - user's group
     */
    public void setGroupName(String inData) {
        if (inData != null) {
            strGroupName = inData;
        }
    }

    /**
     * getStatus
     * Get user's status
     * @return  user's status
     */
    public String getStatus() {
        return strStatus;
    }

    /**
     * setStatus
     * Set user's status
     * @param   inData - user's status
     */
    public void setStatus(String inData) {
        if (inData != null) {
            strStatus = inData;
        }
    }
    /**
     * @return
     */
    public String getEmail() {
        return strEmail;
    }

    /**
     * @param string
     */
    public void setEmail(String string) {
        strEmail = string;
    }

}