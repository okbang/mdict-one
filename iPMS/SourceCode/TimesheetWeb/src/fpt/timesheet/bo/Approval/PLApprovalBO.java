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
 
 package fpt.timesheet.bo.Approval;

import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import fpt.timesheet.ApproveTran.ejb.approve.ApproveEJBLocal;
import fpt.timesheet.ApproveTran.ejb.approve.ApproveEJBLocalHome;
import fpt.timesheet.ApproveTran.ejb.approve.ApproveModel;
//import fpt.timesheet.ApproveTran.ejb.approve.ApproveHome;
//import fpt.timesheet.ApproveTran.ejb.approve.ApproveRemote;

import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.bean.Approval.PLListBean;
import fpt.timesheet.bean.Approval.PLUpdateBean;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;

public class PLApprovalBO implements DefinitionList {
    private ApproveEJBLocalHome homeApprove = null; 
    private ApproveEJBLocal objApprove = null;

    private static Logger logger = Logger.getLogger(PLApprovalBO.class.getName());

    /**
     * @see java.lang.Object#Object()
     */
    public PLApprovalBO() {
    }

    /**
     * Method getApproveEJB.
     * @throws NamingException
     */
    private void getApproveEJB() throws NamingException {
        try {
            if (homeApprove == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.APPROVE);

//              homeApprove = (ApproveHome) javax.rmi.PortableRemoteObject.narrow(objref, ApproveHome.class);
				homeApprove = (ApproveEJBLocalHome)(objref);
                if (objApprove == null)
                    objApprove = homeApprove.create();
            }
        }
        catch (NamingException ex) {
            logger.debug("PLApprovalBO.getApproveEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
            throw ex;
        }
        catch (Exception ex) {
            logger.debug("PLApprovalBO.getApproveEJB() error! -- " + ex.getMessage());
            ex.printStackTrace();
        }
    } //getApproveEJB

    /**
     * Get user timesheet leaded by a PL.
     * @author  Nguyen Thai Son.
     * @version  November 19, 2002.
     * @param   beanPLList PLListBean: input data
     * @param   beanUserInfo UserInfoBean: user information
     * @param   nType: 0 - called from PLList, 1 - called from GLList
     * @exception   Exception    If an exception occurred.
     */
    public PLListBean getPLList(UserInfoBean beanUserInfo, PLListBean beanPLList) throws Exception {
        try {
            getApproveEJB();

            String strListingName = beanPLList.getListingName();
            int nDevId = beanUserInfo.getUserId();

            String sProject = beanPLList.getProject();
            int nStatus = beanPLList.getStatus();

            String strFromDate = beanPLList.getFromDate();
            String strToDate = beanPLList.getToDate();
            String strAccount = beanPLList.getAccount();

            int nSortby = beanPLList.getSortby();
            int nCurrentPage = beanPLList.getCurrentPage();

            ArrayList dbList = objApprove.getTimesheetList(strListingName, nDevId, sProject, nStatus, strFromDate, strToDate, strAccount, nSortby, nCurrentPage);

            StringMatrix mtxList = new StringMatrix();
            StringVector vecItem = new StringVector(12);

            Iterator it = dbList.iterator();
            while (it.hasNext()) {
                ApproveModel modelApprove = (ApproveModel) it.next();

                //Add to user view
                vecItem.setCell(0, Integer.toString(modelApprove.getId()));
                vecItem.setCell(1, modelApprove.getProject());
                vecItem.setCell(2, modelApprove.getAccount());

                vecItem.setCell(3, modelApprove.getDate());
                vecItem.setCell(4, modelApprove.getDescription());
                vecItem.setCell(5, modelApprove.getDuration() + "");

                vecItem.setCell(6, modelApprove.getProcess() + "");
                vecItem.setCell(7, modelApprove.getType() + "");
                vecItem.setCell(8, modelApprove.getProduct() + "");

                vecItem.setCell(9, modelApprove.getLeader());
                vecItem.setCell(10, modelApprove.getStatus() + "");
                vecItem.setCell(11, modelApprove.getComment());

                mtxList.addRow(vecItem);
            }
            int nTotalPage = objApprove.getTotalPage();
            int nTotalTimesheet = objApprove.getTotalTimesheet();
            beanPLList.setTimesheetList(mtxList);
            beanPLList.setTotalPage(nTotalPage);
            beanPLList.setTotalTimesheet(nTotalTimesheet);
            //added by MinhPT 03Oct13
            //for check nCurrentPage > (nTotalPage-1)
            if (nCurrentPage > (nTotalPage - 1))
                beanPLList.setCurrentPage(0);
            else
                beanPLList.setCurrentPage(nCurrentPage);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in PLApprovalBO.getPLList(): " + ex.toString());
            logger.error(ex);
        }

        return beanPLList;
    }


    /**
     * PL approves timesheet in his project
     * @author  Nguyen Thai Son.
     * @version  November 20, 2002.
     * @param   beanPLList PLListBean: input data
     * @param   beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public int approveByPL(UserInfoBean beanUserInfo, PLListBean beanPLList) throws Exception {
        int nResult = -1;

        String strApprover = beanUserInfo.getLoginName();
        String strListingName = beanPLList.getListingName();
        String[] arrIdList = beanPLList.getArrIdList();
        String[] arrComment = beanPLList.getArrComment();
        String strAction = beanPLList.getUpdateAction();

        int nStatus = 2;
        if (TIMESHEET.PL_APPROVE.equalsIgnoreCase(strAction) || TIMESHEET.GL_APPROVE.equalsIgnoreCase(strAction))
            nStatus = LD_APPROVE_STATUS;
        else if (TIMESHEET.PL_REJECT.equalsIgnoreCase(strAction) || TIMESHEET.GL_REJECT.equalsIgnoreCase(strAction))
            nStatus = LD_REJECT_STATUS;

        try {
            getApproveEJB();

            objApprove.changeStatus(strListingName, nStatus, arrIdList, arrComment, strApprover);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in PLApprovalBO.approveByPL(): " + ex.toString());
            logger.error(ex);
        }

        return nResult;
    }

    /**
     * Get PLUpdate info to display a form.
     * @author  Nguyen Thai Son.
     * @version  November 20, 2002.
     * @param   beanPLList PLListBean: input data
     * @exception   Exception    If an exception occurred.
     */
    public PLUpdateBean getPLUpdateInfo(String[] arrID, String strTypeOfView) throws Exception {
        PLUpdateBean beanPLUpdate = new PLUpdateBean();
        int nSortby = 1;

        try {
            getApproveEJB();

            String strIdList = "";
            for (int i = 0; i < arrID.length; i++)
                strIdList += arrID[i] + ",";
            strIdList = strIdList.substring(0, strIdList.length() - 1);

            ArrayList dbList = objApprove.getTimesheetList(strTypeOfView, strIdList, nSortby);
            Iterator it = dbList.iterator();

            StringMatrix mtxList = new StringMatrix();
            StringVector vecItem = new StringVector(9);

            while (it.hasNext()) {
                ApproveModel apmData = (ApproveModel) it.next();

                vecItem.setCell(0, apmData.getId() + "");
                vecItem.setCell(1, apmData.getProject());
                vecItem.setCell(2, apmData.getAccount());

                vecItem.setCell(3, apmData.getDate());
                vecItem.setCell(4, apmData.getDescription());
                vecItem.setCell(5, apmData.getDuration() + "");

                vecItem.setCell(6, apmData.getProcess() + "");
                vecItem.setCell(7, apmData.getType() + "");
                vecItem.setCell(8, apmData.getProduct() + "");

                mtxList.addRow(vecItem);
            }

            beanPLUpdate.setTimesheetList(mtxList);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in PLApprovalBO.approveByPL(): " + ex.toString());
            logger.error(ex);
        }

        return beanPLUpdate;
    }

    /**
     * Approve and update timesheet by PL
     * @author  Nguyen Thai Son.
     * @version  November 20, 2002.
     * @param   beanPLUpdate PLUpdateBean: input data
     * @param   beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public int approveAndUpdateByPL(UserInfoBean beanUserInfo, PLUpdateBean beanPLUpdate) throws Exception {
        int nResult = -1;

        String strApprover = beanUserInfo.getLoginName();
        //String strAction = beanPLUpdate.getAction();
        StringMatrix mtxList = beanPLUpdate.getUpdateList();

        int maxrows = mtxList.getNumberOfRows();
        if (maxrows < 1) {
            return nResult;
        }

        String[] sId = new String[maxrows];
        String[] sProcess = new String[maxrows];
        String[] sType = new String[maxrows];
        String[] sProduct = new String[maxrows];
        String[] sDuration = new String[maxrows];
        String[] sDescription = new String[maxrows];
        String[] sDate = new String[maxrows];

        int i = 0;
        while (i < maxrows) {
            sId[i] = mtxList.getCell(i, 0);
            sProcess[i] = mtxList.getCell(i, 1);
            sType[i] = mtxList.getCell(i, 2);
            sProduct[i] = mtxList.getCell(i, 3);
            sDuration[i] = mtxList.getCell(i, 4);
            sDescription[i] = mtxList.getCell(i, 5);
            sDate[i] = mtxList.getCell(i, 6);

            i++;
        }

        try {
            getApproveEJB();
            objApprove.LDcorrect(strApprover, sId, sProcess, sType, sProduct, sDuration, sDescription, sDate);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in PLApprovalBO.approveAndUpdateByPL(): " + ex.toString());
            logger.error(ex);
        }

        return nResult;
    }
}