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
 
 package fpt.timesheet.bean;

import javax.servlet.http.HttpSession;

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.framework.core.SessionInfoBaseBean;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

public class UserInfoBean {

    // User information
    private int nUserId;
    private String strRole;
    private String strLoginName = "";
    private String strFullName;
	private String strMessage = "";

	private String strGroupName = "";
	private String strTypeOfView = "";
	private String strStatus = "";
	private StringMatrix smtGroupList = null;

	public SessionInfoBaseBean sessionExtObject = null;
	public SessionInfoBaseBean sessionInfoObject = null;
	public static final String __SESSION_INFOMATION_OBJECT_NAME = "__SESSION_INFOMATION_OBJECT__";

	/**
	 * @see java.lang.Object#Object()
	 */
	public UserInfoBean() {
	}

    /**
     * Method toOut.
     */
    public void toOut() {
        String txt = "";
        txt += " The userID is " + this.nUserId;
        txt += " \n The Account is " + this.strLoginName;
        txt += " \n The Role is " + this.strRole;
        txt += " \n The Role Name is " + this.getRoleName();
    }

    /**
     * Method setUserId.
     * @param num
     */
    public void setUserId(int num) {
        nUserId = num;
    }

    /**
     * Method getUserId.
     * @return int
     */
    public int getUserId() {
        return nUserId;
    }

    /**
     * Method setLoginName.
     * @param str
     */
    public void setLoginName(String str) {
        strLoginName = str;
    }

    /**
     * Method getLoginName.
     * @return String
     */
    public String getLoginName() {
        return strLoginName;
    }

    /**
     * Method setFullName.
     * @param str
     */
    public void setFullName(String str) {
        strFullName = str;
    }

    /**
     * Method getFullName.
     * @return String
     */
    public String getFullName() {
        return strFullName;
    }

    /**
     * Method setRole.
     * @param role
     */
    public void setRole(String role) {
        strRole = role;
    }

    /**
     * Method getRole.
     * @return String
     */
    public String getRole() {
        return strRole;
    }

    /**
     * Method getRoleName.
     * @return String
     */
    public String getRoleName() {
        String strRoleName = "";
        if (!strRole.equals("")) {
            if (strRole.substring(0, 1).equals("1"))
                strRoleName = "Developer";
            if (strRole.substring(1, 2).equals("1"))
                strRoleName = "Project Manager"; //HaiMM  PL --> PM
            if (strRole.substring(2, 3).equals("1"))
                strRoleName = "Group Leader";
			if (strRole.substring(4, 5).equals("1"))
				strRoleName = "QA";
			if (strRole.substring(5, 6).equals("1"))
				strRoleName = "Manager";
			if (strRole.substring(6, 7).equals("1"))
				strRoleName = "External user";
            if (strRole.substring(8, 9).equals("1"))
                strRoleName = "Communicator";
        }
        return strRoleName;
    }

	/**
	 * @return getGroupList
	 */
	public StringMatrix getGroupList() {
		return smtGroupList;
	}

	/**
	 * Method setGroupList
	 * @param matrix
	 */
	public void setGroupList(StringMatrix matrix) {
		smtGroupList = matrix;
	}

	/**
	  * Method setGroupList.
	  */
	public void setGroupList() {
		CommonListBO cmlRef = new CommonListBO();
		this.smtGroupList = cmlRef.getGroupList();
	}

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
        if (data != null) {
			strGroupName = data;
        }
    }

    /**
     * Method getMessage.
     * @return String
     */
    public String getMessage() {
        if (strMessage == null)
            strMessage = "";
        return strMessage;
    }

    /**
     * Method setMessage.
     * @param inData
     */
    public void setMessage(String inData) {
        if (inData != null)
            strMessage = inData;
    }

    /**
     * Method getSessionInfoObject.
     * @param webSession
     * @return SessionInfoBaseBean
     */
    // this funtion use when synchronizing SessionInfoObject between BaseBean and Session..
	public static SessionInfoBaseBean getSessionInfoObject(HttpSession webSession) {
        SessionInfoBaseBean sessionInfoObject = (SessionInfoBaseBean) webSession.getAttribute(__SESSION_INFOMATION_OBJECT_NAME);
        if (sessionInfoObject == null) {
            sessionInfoObject = setSessionInfoObject(webSession, sessionInfoObject);
        }
        return sessionInfoObject;
    }

    /**
     * Method setSessionInfoObject.
     * @param webSession
     * @param sessionInfoObject
     * @return SessionInfoBaseBean
     */
    // this funtion use when finishing login..
    public static SessionInfoBaseBean setSessionInfoObject(HttpSession webSession, SessionInfoBaseBean sessionInfoObject) {
        SessionInfoBaseBean mySessionInfoObject = sessionInfoObject;
        if (mySessionInfoObject == null)
            mySessionInfoObject = new SessionInfoBaseBean();
        webSession.setAttribute(__SESSION_INFOMATION_OBJECT_NAME, mySessionInfoObject);
        return mySessionInfoObject;
    }

    /**
     * Method getTypeOfView.
     * @return String
     */
    public String getTypeOfView() {
        return this.strTypeOfView;
    }

    /**
     * Method setTypeOfView.
     * @param data
     */
    public void setTypeOfView(String data) {
        this.strTypeOfView = data;
    }

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
        if (data != null) {
			strStatus = data;
        }
    }
}