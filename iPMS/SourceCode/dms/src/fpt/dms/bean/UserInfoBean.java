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
 
 package fpt.dms.bean;

import fpt.dms.constant.DMS;

/**
 * Title:        Project
 * Description:
 * Copyright:    Copyright (C) 2001 Cogita FPTSoft
 * Company:      FPT Corporation
 * @author
 * @version 1.0.0
 */

public class UserInfoBean {

    private String strUserName = "";
    private String strPassword = "";
    private String strGroup = "";
    private String strAccount = "";
    private String strRole = "";
    private String strPosition = "0000000000";
    private String strPositionName = "";
    private String strProjectCode = "";
    private String strDateLogin = "";
    private String strDeveloperId = "";
    private int nProjectID = 0;
    private int nViewDefectID = 0;
    private int nKindOfView = 0; //0: AllDefect 1:Private Defect
    int SelectedDefect = 0;
    private String Direction = "";
    private String strCurrentStatus = DMS.PROJECT_VALUE_STATUS_ONGOING;

    //Thaison - Sep 16, 2002
    private String strRootPath = null;
    private String strExportedFileName = null;

    //Tu Ngoc Trung, 2003-Dec-12
    private boolean bDeveloper;
    private boolean bTester;
    private boolean bProjectLeader;
	private boolean bExternalUser;

	/**
	 * Constructor UserInfoBean()
	 */
	public UserInfoBean() {
	}

    public void setKindOfView(int kindview) {
        nKindOfView = kindview;
    }
    public int getKindOfView() {
        return nKindOfView;
    }
    public String getPositionName() {
        strPositionName = "";
        if (!strPosition.equals("")) {
            if (strPosition.substring(0, 1).equals("1"))
                strPositionName = "Developer";
            if (strPosition.substring(1, 2).equals("1"))
                strPositionName = "Tester";
            if (strPosition.substring(2, 3).equals("1"))
                strPositionName = "Project Leader";
//			if (strRole.substring(4, 5).equals("1"))
//				strPositionName = "Quality Assurance";
			if (strPosition.substring(6, 7).equals("1"))
				strPositionName = "External User";
        }
        return strPositionName;
    }

    public void setUserName(String username) {
        strUserName = username;
    }

    public String getUserName() {
        return strUserName;
    }

    public void setPassword(String pass) {
        strPassword = pass;
    }

    public String getPassword() {
        return strPassword;
    }

    public void setGroupName(String group) {
        strGroup = group;
    }

    public String getGroupName() {
        return strGroup;
    }

    public void setAccount(String account) {
        strAccount = account;
    }

    public String getAccount() {
        return strAccount;
    }

    public void setRole(String role) {
        strRole = role;
    }

    public String getRole() {
        return strRole;
    }

    public void setPosition(String position) {
        strPosition = position;
        if (strPosition.length() > 0) {
        	//Developer
            if (strPosition.substring(0, 1).equals("1")) {
                bDeveloper = true;
            }
            else {
                bDeveloper = false;
            }
            //Tester
            if (strPosition.substring(1, 2).equals("1")) {
                bTester = true;
            }
            else {
                bTester = false;
            }
            //Project Leader
            if (strPosition.substring(2, 3).equals("1")) {
                bProjectLeader = true;
            }
            else {
                bProjectLeader = false;
            }
            //External User
			if (strPosition.substring(6, 7).equals("1")) {
				bExternalUser = true;
			}
			else {
				bExternalUser = false;
			}
        }
    }

    public String getPosition() {
        return strPosition;
    }

    public void setProjectID(int position) {
        nProjectID = position;
    }

    public int getProjectID() {
        return nProjectID;
    }

    public void setProjectCode(String project) {
        strProjectCode = project;
    }

    public String getProjectCode() {
        return strProjectCode;
    }

    public void setDateLogin(String date) {
        strDateLogin = date;
    }

    public String getDateLogin() {
        return strDateLogin;
    }

    public void setViewDefectID(int id) {
        nViewDefectID = id;
    }

    public int getViewDefectID() {
        return nViewDefectID;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String Direction) {
        this.Direction = Direction;
    }

    public int getSelectedDefect() {
        return SelectedDefect;
    }

    public void setSelectedDefect(int SelectedDefect) {
        this.SelectedDefect = SelectedDefect;
    }

    //Thaison - Sep 16, 2002
    public String getRootPath() {
        return this.strRootPath;
    }
    public void setRootPath(String data) {
        this.strRootPath = data;
    }
    public String getExportedFileName() {
        return this.strExportedFileName;
    }
    public void setExportedFileName(String data) {
        this.strExportedFileName = data;
    }

    String strTypeOfView = DMS.VIEW_ALL_DEFECTS;
    public String getTypeOfView() {
        return strTypeOfView;
    }
    public void setTypeOfView(String strTypeOfView) {
        this.strTypeOfView = strTypeOfView;
    }

    public void setCurrentStatus(String strCurrentStatus) {
        this.strCurrentStatus = strCurrentStatus;
    }

    public String getCurrentStatus() {
        return this.strCurrentStatus;
    }
    
    /**
     * Returns the developer flag.
     * @return boolean
     */
    public boolean isDeveloper() {
        return bDeveloper;
    }

    /**
     * Returns the projectLeader flag.
     * @return boolean
     */
    public boolean isProjectLeader() {
        return bProjectLeader;
    }

    /**
     * Returns the tester flag.
     * @return boolean
     */
    public boolean isTester() {
        return bTester;
    }
	
	/**
	 * Returns the externalUser flag.
	 * @return boolean
	 */
	public boolean isExternalUser() {
		return bExternalUser;
	}

	/**
	 * check Uer have permisstion AssignUser
	 * @return
	 */
	public boolean isAssignUserHandler(){
		try {
			if(getPosition().substring(2,3).equals("1")){
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			return false;
		}
	}

	/**
	 * check permission for Setup Enviroment
	 * @return
	 */
	public boolean isSetupEnvironment() {
		try {
			if(getRole().substring(4,5).equals("1")) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			return false;
		}
	}

	/**
	 * check permission for Setup ModuleAreaHandler/
	 * @return
	 */
	public boolean isSetupModuleAreaHandler(){
		try {
			if(getPosition().substring(2,3).equals("1")) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			return false;
		}
	}
	/**
	 * chec permission for WorkProduct Size Handler
	 * @return
	 */
	public boolean isWorkProductSizeHandler(){
		try {
			if(getPosition().substring(2,3).equals("1")) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			return false;
		}
	}
	/**
	 * check have permission for plan Defect Handler
	 * @return
	 */
	public boolean isPlannedDefectHandler(){
		try {
			if(getPosition().substring(2,3).equals("1")) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception ex) {
			return false;
		}
	}

    /**
     * Sets the developer.
     * @param boolean The developer flag to set
     */
    public void setDeveloper(boolean bInput) {
        bDeveloper = bInput;
    }

    /**
     * Sets the projectLeader.
     * @param projectLeader The projectLeader flag to set
     */
    public void setProjectLeader(boolean bInput) {
        bProjectLeader = bInput;
    }

    /**
     * Sets the tester.
     * @param tester The tester flag to set
     */
    public void setTester(boolean bInput) {
        bTester = bInput;
    }

    /**
     * @return
     */
    public String getDeveloperId() {
        return strDeveloperId;
    }

    /**
     * @param string
     */
    public void setDeveloperId(String string) {
        strDeveloperId = string;
    }
}