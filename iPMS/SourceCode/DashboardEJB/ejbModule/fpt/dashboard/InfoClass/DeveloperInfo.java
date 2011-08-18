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
 * Created on Sep 7, 2004
 *
 */
package fpt.dashboard.InfoClass;

import fpt.dashboard.constant.*;

import java.io.Serializable;

/**
 * @author trungtn
 *
 */
public class DeveloperInfo implements Serializable {
    private int n_DeveloperID;
    private String strAccount;
    private String strName;
    private String strGroupName;
    private String strDesignation;
    private String strRole;
    private String strPassword;
    private int n_Status;
    private String strEmail;
    /**
     * @return
     */
    public int getDeveloperID() {
        return n_DeveloperID;
    }

    /**
     * @return
     */
    public int getStatus() {
        return n_Status;
    }

    /**
     * Get status name by status value
     * @return
     */
    public String getStatusName() {
        switch (n_Status) {
            case DB.DEVELOPER_SATUS_STAFF:
                return DB.DEVELOPER_SATUS_NAME_STAFF;
            case DB.DEVELOPER_SATUS_COLLABORATOR:
                return DB.DEVELOPER_SATUS_NAME_COLLABORATOR;
            case DB.DEVELOPER_SATUS_EXTERNAL:
                return DB.DEVELOPER_SATUS_NAME_EXTERNAL;
            case DB.DEVELOPER_SATUS_OFF:
                return DB.DEVELOPER_SATUS_NAME_OFF;
            default:
                return DB.DEVELOPER_SATUS_NAME_NA;
        }
    }

    /**
     * @return
     */
    public String getAccount() {
        return strAccount;
    }

    /**
     * @return
     */
    public String getDesignation() {
        return strDesignation;
    }

    /**
     * @return
     */
    public String getEmail() {
        return strEmail;
    }

    /**
     * @return
     */
    public String getGroupName() {
        return strGroupName;
    }

    /**
     * @return
     */
    public String getName() {
        return strName;
    }

    /**
     * @return
     */
    public String getPassword() {
        return strPassword;
    }

    /**
     * @return
     */
    public String getRole() {
        return strRole;
    }

    /**
     * @return
     */
    public String getRoleName() {
        if (DATA.ROLE_DEVELOPER.equals(strRole)) {
            return DATA.ROLENAME_DEVELOPER;
        }
        else if (DATA.ROLE_PROJECTLEADER.equals(strRole)) {
            return DATA.ROLENAME_PROJECTLEADER;
        }
        else if (DATA.ROLE_GROUPLEADER.equals(strRole)) {
            return DATA.ROLENAME_GROUPLEADER;
        }
        else if (DATA.ROLE_PQA.equals(strRole)) {
            return DATA.ROLENAME_PQA;
        }
        else if (DATA.ROLE_MANAGER.equals(strRole)) {
            return DATA.ROLENAME_MANAGER;
        }
        else if (DATA.ROLE_EXTERNAL_GL.equals(strRole)) {
            return DATA.ROLENAME_EXTERNAL_GL;
        }
        else if (DATA.ROLE_EXTERNAL_PL.equals(strRole)) {
            return DATA.ROLENAME_EXTERNAL_PL;
        }
        else if (DATA.ROLE_COMMUNICATOR.equals(strRole)) {
            return DATA.ROLENAME_COMMUNICATOR;
        }
        else {
            return DATA.ROLENAME_UNKNOWN;
        }
    }

    /**
     * @param i
     */
    public void setDeveloperID(int i) {
        n_DeveloperID = i;
    }

    /**
     * @param i
     */
    public void setStatus(int i) {
        n_Status = i;
    }

    /**
     * @param string
     */
    public void setAccount(String string) {
        strAccount = string;
    }

    /**
     * @param string
     */
    public void setDesignation(String string) {
        strDesignation = string;
    }

    /**
     * @param string
     */
    public void setEmail(String string) {
        strEmail = string;
    }

    /**
     * @param string
     */
    public void setGroupName(String string) {
        strGroupName = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        strName = string;
    }

    /**
     * @param string
     */
    public void setPassword(String string) {
        strPassword = string;
    }

    /**
     * @param string
     */
    public void setRole(String string) {
        strRole = string;
    }

}
