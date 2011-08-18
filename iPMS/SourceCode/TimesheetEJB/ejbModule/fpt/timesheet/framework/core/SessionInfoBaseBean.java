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
 
 /**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package fpt.timesheet.framework.core;

import java.io.Serializable;

public class SessionInfoBaseBean implements Serializable {
    private int nUserID = 0x00;
    private String strLoginName = "";
    private String strLoginPassword = "";
    private String strFullname = "";
    private String strRole = "";

    /**
     * Method getUserID.
     * @return int
     */
    public int getUserID() {
        return nUserID;
    }

    /**
     * Method setUserID.
     * @param inData
     */
    public void setUserID(int inData) {
        nUserID = inData;
    }

    /**
     * Method getLoginName.
     * @return String
     */
    public String getLoginName() {
        if (strLoginName == null)
            strLoginName = "";
        return strLoginName;
    }

    /**
     * Method setLoginName.
     * @param inData
     */
    public void setLoginName(String inData) {
        if (inData != null)
            strLoginName = inData;
    }

    /**
     * Method getLoginPassword.
     * @return String
     */
    public String getLoginPassword() {
        if (strLoginPassword == null)
            strLoginPassword = "";
        return strLoginPassword;
    }

    /**
     * Method setLoginPassword.
     * @param inData
     */
    public void setLoginPassword(String inData) {
        if (inData != null)
            strLoginPassword = inData;
    }

    /**
     * Method getFullname.
     * @return String
     */
    public String getFullname() {
        if (strFullname == null)
            strFullname = "";
        return strFullname;
    }

    /**
     * Method setFullname.
     * @param inData
     */
    public void setFullname(String inData) {
        if (inData != null)
            strFullname = inData;
    }

    /**
     * Method getRole.
     * @return String
     */
    public String getRole() {
        if (strRole == null)
            strRole = "";
        return strRole;
    }

    /**
     * Method setRole.
     * @param inData
     */
    public void setRole(String inData) {
        if (inData != null)
            strRole = inData;
    }

    //user group
    private String strGroupName = "";

    /**
     * Method getGroupName.
     * @return String
     */
    public String getGroupName() {
        return strGroupName;
    }

    /**
     * Method setGroupName.
     * @param data
     */
    public void setGroupName(String data) {
        if (data != null)
            strGroupName = data;
    }

    //user status
    private String strStatus = "";

    /**
     * Method getStatus.
     * @return String
     */
    public String getStatus() {
        return strStatus;
    }

    /**
     * Method setStatus.
     * @param data
     */
    public void setStatus(String data) {
        if (data != null)
            strStatus = data;
    }
}