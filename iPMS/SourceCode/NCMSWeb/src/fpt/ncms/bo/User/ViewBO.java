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
 * @(#)ViewBO.java 01-Apr-03
 */


package fpt.ncms.bo.User;

import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import fpt.ncms.bean.UserInfoBean;
import fpt.ncms.bean.ViewAddBean;
import fpt.ncms.constant.JNDI;

//import fpt.ncms.ejb.View;
//import fpt.ncms.ejb.ViewHome;
import fpt.ncms.ejb.ViewEJBLocal;
import fpt.ncms.ejb.ViewEJBLocalHome;

import fpt.ncms.util.CommonUtil.CommonUtil;
import fpt.ncms.util.StringUtil.StringMatrix;

/**
 * Class ViewBO
 * Bean object for manipulating with view
 * @version 1.0 01-Apr-03
 * @author
 */
public final class ViewBO {
//    private ViewHome homeView = null;
//    private View objView = null;

	private ViewEJBLocalHome homeView = null; // HaiMM
	private ViewEJBLocal objView = null; // HaiMM

    /**
     * ViewBO
     * Class constructor
     */
    public ViewBO() {
    }

    /**
     * getViewEJB
     * Get home and create View object
     * @throws  NamingException
     */
    private void getViewEJB() {
        try {
            if (homeView == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.VIEW);
                homeView = (ViewEJBLocalHome)(objref); // HaiMM
                if (objView == null) {
                    objView = homeView.create();
                }
            }
        }
        catch (NamingException ex) {
            System.out.println("ViewBO.getViewEJB() error! -- "
                    + ex.getMessage() + "---" + ex.getResolvedName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * getComboOrderBy
     * Set up combo order by
     * @return  combo order by
     */
    public static final StringMatrix getComboOrderBy() {
        StringMatrix smList = new StringMatrix(16, 1);
        smList.setCell(0, 0, "Assignee");
        smList.setCell(1, 0, "Code");
        smList.setCell(2, 0, "CreationDate");
        smList.setCell(3, 0, "Creator");
        smList.setCell(4, 0, "DetectedBy");
        smList.setCell(5, 0, "GroupName");
        smList.setCell(6, 0, "ISOClause");
        smList.setCell(7, 0, "KPA");
        smList.setCell(8, 0, "NCLevel");
        smList.setCell(9, 0, "NCType");
        smList.setCell(10, 0, "Process");
        smList.setCell(11, 0, "ProjectID");
        smList.setCell(12, 0, "Repeat");
        smList.setCell(13, 0, "Reviewer");
        smList.setCell(14, 0, "TypeOfAction");
        smList.setCell(15, 0, "TypeOfCause");
        return smList;
    }

    /**
     * getViewList
     * Get view list
     * @param   beanUserInfo - user's information
     * @return  list of view
     */
    public final StringMatrix getViewList(UserInfoBean beanUserInfo) {
        StringMatrix smList = null;
        try {
            getViewEJB();
            ArrayList arrList = objView.queryViewByAccount(
                    beanUserInfo.getLoginName());
            if (arrList.size() > 0) {
                int nSize = arrList.size() / 4;
                smList = new StringMatrix(nSize, 4);
                for (int nRow = 0; nRow < nSize; nRow++) {
                    for (int i = 0; i < 4; i++) {
                        smList.setCell(nRow, i, CommonUtil.correctHTMLError(
                                arrList.get(4 * nRow + i).toString()));
                    }
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in ViewBO.getViewList(): "
                    + ex.toString());
        }
        return smList;
    }

    /**
     * getViewByID
     * Get view by it's ID
     * @param   strViewID - view ident.number
     * @return  retrieved view information
     */
    public final ViewAddBean getViewByID(String strViewID) {
        ViewAddBean beanViewAdd = new ViewAddBean();
        try {
            getViewEJB();
            ArrayList arrList = objView.queryViewByID(strViewID);
            if (arrList.size() > 0) {
                beanViewAdd.setViewID(strViewID);
                beanViewAdd.setTitle(CommonUtil.correctHTMLError(
                        arrList.get(0).toString()));
                beanViewAdd.setField(arrList.get(1).toString());
                beanViewAdd.setOrderBy(arrList.get(2).toString());
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in ViewBO.getViewByID(): "
                    + ex.toString());
        }
        return beanViewAdd;
    }

    /**
     * addView
     * Add view to database
     * @param   beanUserInfo - user's information
     * @param   beanViewAdd - input information for inserting
     * @return  adding was successful or not
     */
    public final boolean addView(UserInfoBean beanUserInfo,
            ViewAddBean beanViewAdd) {
        boolean isSuccess = true;
        try {
            getViewEJB();
            if (objView.insertView(beanUserInfo.getLoginName(),
                    beanViewAdd.getTitle(), beanViewAdd.getField(),
                    beanViewAdd.getOrderBy()) <= 0) {
                isSuccess = false;
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in ViewBO.addView(): "
                    + ex.toString());
        }
        return isSuccess;
    }

    /**
     * updateView
     * Update view information
     * @param   beanUserInfo - user's information
     * @param   beanViewAdd - input information for updating
     * @return  adding was successful or not
     */
    public final boolean updateView(UserInfoBean beanUserInfo,
            ViewAddBean beanViewAdd) {
        boolean isSuccess = true;
        try {
            getViewEJB();
            if (objView.updateView(beanViewAdd.getViewID(),
                    beanViewAdd.getTitle(), beanViewAdd.getField(),
                    beanViewAdd.getOrderBy(), beanUserInfo.getLoginName())
                    <= 0) {
                isSuccess = false;
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in ViewBO.updateView(): "
                    + ex.toString());
        }
        return isSuccess;
    }

    /**
     * deleteView
     * Remove view
     * @param   strViewID - view ident.numbre
     * @return  number of removed records(1)
     */
    public final void deleteView(String strViewID) {
        try {
            getViewEJB();
            objView.deleteView(strViewID);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in ViewBO.deleteView(): "
                    + ex.toString());
        }
    }
}