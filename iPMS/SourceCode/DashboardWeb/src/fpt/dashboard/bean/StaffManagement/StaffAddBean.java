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
 
 package fpt.dashboard.bean.StaffManagement;

import fpt.dashboard.util.StringUtil.StringMatrix;

public class StaffAddBean implements java.io.Serializable
{
    private String[] arrGroup = null;
    private String DevID = "";
    private String Name = "";
    private String Designation = "";
    private String Role = "0000000000";
    private String Account = "";
    private String Password = "";
    private String Email = "";
    private String StartDate = "";//Calendar.getInstance();
    private String Group = "";
    private String strStatus;
    private StringMatrix StatusList = null;
    public String getAccount()
    {
        return Account;
    }
    public String[] getArrGroup()
    {
        return arrGroup;
    }
    public String getDesignation()
    {
        return Designation;
    }
    public String getGroup()
    {
        return Group;
    }
    public String getName()
    {
        return Name;
    }
    public String getPassword()
    {
        return Password;
    }
    public String getRole()
    {
        return Role;
    }
    public void setRole(String Role)
    {
        this.Role = Role;
    }
    public void setPassword(String Password)
    {
        this.Password = Password;
    }
    public void setName(String Name)
    {
        this.Name = Name;
    }
    public void setGroup(String Group)
    {
        this.Group = Group;
    }
    public void setDesignation(String Designation)
    {
        this.Designation = Designation;
    }
    public void setArrGroup(String[] arrGroup)
    {
        this.arrGroup = arrGroup;
    }
    public void setAccount(String Account)
    {
        this.Account = Account;
    }
    public String getDevID()
    {
        return DevID;
    }
    public void setDevID(String DevID)
    {
        this.DevID = DevID;
    }
    public String getStatus()
    {
        return this.strStatus;
    }
    public void setStatus(String data)
    {
        this.strStatus = data;
    }
    public StringMatrix getStatusList()
    {
        return StatusList;
    }
    public void setStatusList(StringMatrix StatusList)
    {
        this.StatusList = StatusList;
    }
    private String strMessage = "";
    public String getMessage()
    {
        return strMessage;
    }
    public void setMessage(String strMsg)
    {
        this.strMessage = strMsg;
    }
    /**
     * @return
     */
    public String getEmail() {
        return Email;
    }

    /**
     * @param string
     */
    public void setEmail(String string) {
        Email = string;
    }

    /**
     * @return
     */
    public String getStartDate() {
        return StartDate;
    }

    /**
     * @param string
     */
    public void setStartDate(String string) {
        StartDate = string;
    }

}