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
 
 package fpt.dashboard.bean;
public class UserInfoBean
{
    private String strUserName = "";
    private String strPassword = "";
    private String strGroup = "";
    private String strAccount = "";
    private String strRole = "";
    private String strPosition = "0000000000";
    private String strPositionName = "";
    private String strProjectCode = "";
    private String strDateLogin = "";
    private String strProjectID = "";
    private String strProjectName = "";
    private String SRole = "";
    private String strTypeOfView = "ViewProjectList";
    private String strDeveloperId = "";
    
    public String getTypeOfView()
    {
        return strTypeOfView;
    }
    public void setTypeOfView(String strTypeOfView)
    {
        this.strTypeOfView = strTypeOfView;
    }
    public String getPositionName()
    {
        strPositionName = "";
        if (!strPosition.equals(""))
            {
            if (strPosition.substring(0, 1).equals("1")) //Developer
                //strPositionName += "Developer " + " | ";
                strPositionName = "Developer";
            if (strPosition.substring(1, 2).equals("1"))
                //strPositionName += "Tester" + " | ";
                strPositionName = "Tester";
            if (strPosition.substring(2, 3).equals("1"))
                //strPositionName = "Project Leader" + " | ";
                strPositionName = "Project Leader";
            //if (!strPositionName.equals(""))
            //  strPositionName = strPositionName.substring(0,strPositionName.length()-2);
            System.out.println(strPositionName);
        }
        return strPositionName;
    }
    public void setUserName(String username)
    {
        strUserName = username;
    }
    public String getUserName()
    {
        return strUserName;
    }
    public void setPassword(String pass)
    {
        strPassword = pass;
    }
    public String getPassword()
    {
        return strPassword;
    }
    public void setGroupName(String group)
    {
        strGroup = group;
    }
    public String getGroupName()
    {
        return strGroup;
    }
    public void setAccount(String account)
    {
        strAccount = account;
    }
    public String getAccount()
    {
        return strAccount;
    }
    public void setRole(String role)
    {
        strRole = role;
    }
    public String getRole()
    {
        return strRole;
    }
    public void setPosition(String position)
    {
        strPosition = position;
    }
    public String getPosition()
    {
        return strPosition;
    }
    public void setProjectID(String position)
    {
        strProjectID = position;
    }
    public String getProjectID()
    {
        return strProjectID;
    }
    public void setProjectCode(String project)
    {
        strProjectCode = project;
    }
    public String getProjectCode()
    {
        return strProjectCode;
    }
    public void setDateLogin(String date)
    {
        strDateLogin = date;
    }
    public String getDateLogin()
    {
        return strDateLogin;
    }
    public String getStrProjectName()
    {
        return strProjectName;
    }
    public void setStrProjectName(String strProjectName)
    {
        this.strProjectName = strProjectName;
    }
    public String getSRole()
    {
        return SRole;
    }
    public void setSRole(String SRole)
    {
        this.SRole = SRole;
    }
    public UserInfoBean()
    {
    }
    private String strMessage = "";
    public String getMessage()
    {
        return strMessage;
    }
    public void setMessage(String message)
    {
        strMessage = message;
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