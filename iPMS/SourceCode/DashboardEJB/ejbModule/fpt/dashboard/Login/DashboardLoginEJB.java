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
 
 package fpt.dashboard.Login;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import fpt.dashboard.framework.connection.WSConnectionPooling;

/**
 * Bean implementation class for Enterprise Bean: LoginEJB
 */
public class DashboardLoginEJB implements javax.ejb.SessionBean
{
    private WSConnectionPooling conPool = new WSConnectionPooling();
    private DataSource ds = null;
    private Connection con = null;

    private javax.ejb.SessionContext mySessionCtx;

    private String groupName = "";
    private String SRole = "";
    private String strDeveloperId = "";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * getSessionContext
     */
    public javax.ejb.SessionContext getSessionContext()
    {
        return mySessionCtx;
    }
    /**
     * setSessionContext
     */
    public void setSessionContext(javax.ejb.SessionContext ctx)
    {
        mySessionCtx = ctx;
    }
    /**
     * ejbActivate
     */
    public void ejbActivate()
    {
        if (con == null)
        {
            try
            {
                ds = conPool.getDS();
                con = ds.getConnection();
            }
            catch (Exception e)
            {
                System.out.println("Exception in LoginEJB: ejbActivate(): " + e.toString());
            }
        }
        else
            {
            System.out.println("connection exists");
        }
    }
    /**
     * ejbCreate
     */
    public void ejbCreate() throws javax.ejb.CreateException
    {
        if (con == null)
        {
            try
            {
                ds = conPool.getDS();
                con = ds.getConnection();
            }
            catch (Exception e)
            {
                System.out.println("Exception in LoginEJB: ejbCreate(): " + e.toString());
            }
        }
        else
            {
            System.out.println("connection exists");
        }
    }
    /**
     * ejbPassivate
     */
    public void ejbPassivate() throws RemoteException
    {
        try
        {
            if (con != null) con.close();
        }
        catch (Exception e)
            {
            throw new RemoteException("LoginEJB.ejbPassivate() error: " + e.toString());
        }
    }
    /**
     * ejbRemove
     */
    public void ejbRemove() throws RemoteException
    {
        try
        {
            if (con != null) con.close();
        }
        catch (Exception e)
            {
            throw new RemoteException("LoginEJB.ejbRemove() error: " + e.toString());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int CheckLogin(String strAccount, String strPassword)    throws SQLException
    {
        int per = 0;

        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try
        {
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();

            StringBuffer strSQL = new StringBuffer();
            strSQL.append("SELECT role, group_name,developer_id");
            strSQL.append(" FROM Developer");
            strSQL.append(" WHERE UPPER(account) = ?");
            strSQL.append(" AND password = ?");
            strSQL.append(" AND status <> 4");

            prepStmt = this.con.prepareStatement(strSQL.toString());
            prepStmt.setString(1, strAccount.toUpperCase());
            prepStmt.setString(2, strPassword);
            rs = prepStmt.executeQuery();

            String role = "";
            if (rs.next())
            {
                role = rs.getString(1);
                if (role.trim().charAt(1) == '1')   //Project Leader
                    per = 1;
                if (role.trim().charAt(4) == '1')   //PQA
                    per = 2;

                int i = 0;
                for (i = 0; i < role.length(); i++)
                {
                    if (role.charAt(i) == '1')
                        break;
                }
                if (i == 10)
                {
                    per = 3;
                }

                if (role.trim().charAt(6) == '1')   //external user of project level
                    per = 5;

                if (role.trim().charAt(7) == '1')   //external user of group level
                    per = 4;


                SRole = role;
                groupName = rs.getString(2);
                strDeveloperId = rs.getString("developer_id");
            }
            else
            {
                per = 0;
            }

            rs.close();
            prepStmt.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        finally
        {
            if (rs != null) rs.close();
            if (prepStmt != null)   prepStmt.close();
            if (con != null)    con.close();
        }
        return per;
    }
    public String getGroup() {
        return groupName;
    }
    public String getSRole() {
        return SRole;
    }
    public void setSRole(String SRole) {
        this.SRole = SRole;
    }
    public String getDeveloperId() {
        return strDeveloperId;
    }
}