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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

//import fpt.timesheet.ApproveTran.ejb.approve.ApproveHome;
//import fpt.timesheet.ApproveTran.ejb.approve.ApproveRemote;
import fpt.timesheet.ApproveTran.ejb.approve.ApproveEJBLocal;
import fpt.timesheet.ApproveTran.ejb.approve.ApproveEJBLocalHome;
import fpt.timesheet.ApproveTran.ejb.approve.ApproveModel;

import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.bean.Approval.QAListBean;
import fpt.timesheet.bean.Approval.QAUpdateBean;
import fpt.timesheet.bean.Mapping.MappingDetailBean;
import fpt.timesheet.constant.DATA;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;

public class QAApprovalBO implements DefinitionList {
    private ApproveEJBLocalHome homeApprove = null;  
    private ApproveEJBLocal objApprove = null;

    private static Logger logger = Logger.getLogger(QAApprovalBO.class.getName());

    /**
     * @see java.lang.Object#Object()
     */
    public QAApprovalBO() {
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
            logger.debug("QAApprovalBO.getApproveEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
            throw ex;
        }
        catch (Exception ex) {
            logger.debug("QAApprovalBO.getApproveEJB() error! -- " + ex.getMessage());
            ex.printStackTrace();
        }
    } //getApproveEJB

    /**
     * Get user timesheet leaded by all PLs.
     * @author  Nguyen Thai Son.
     * @version  November 20, 2002.
     * @param   beanQAList QAListBean: input data
     * @param   beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public QAListBean getQAList(UserInfoBean beanUserInfo, QAListBean beanQAList) throws Exception {
        try {
            getApproveEJB();

            String strListingName = beanQAList.getListingName();
            int nDevId = beanUserInfo.getUserId();

            String sProject = beanQAList.getProject();
            int nStatus = beanQAList.getStatus();

            String strFromDate = beanQAList.getFromDate();
            String strToDate = beanQAList.getToDate();
            String strAccount = beanQAList.getAccount();

            int nSortby = beanQAList.getSortby();
            int nCurrentPage = beanQAList.getCurrentPage();

            ArrayList dbList = objApprove.getTimesheetList(strListingName, nDevId, sProject, nStatus, strFromDate, strToDate, strAccount, nSortby, nCurrentPage);

            StringMatrix mtxList = new StringMatrix();
            StringVector vecItem = new StringVector(13);

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
                vecItem.setCell(9, modelApprove.getKpa() + "");

                vecItem.setCell(10, modelApprove.getQA());
                vecItem.setCell(11, modelApprove.getStatus() + "");
                vecItem.setCell(12, modelApprove.getComment());

                mtxList.addRow(vecItem);
            }
            int nTotalPage = objApprove.getTotalPage();
            int nTotalTimesheet = objApprove.getTotalTimesheet();

            beanQAList.setTimesheetList(mtxList);
            beanQAList.setTotalPage(nTotalPage);
            beanQAList.setTotalTimesheet(nTotalTimesheet);
            //added by MinhPT 03Oct13
            //for check nCurrentPage > (nTotalPage-1)
            if (nCurrentPage > (nTotalPage - 1))
                beanQAList.setCurrentPage(0);
            else
                beanQAList.setCurrentPage(nCurrentPage);

        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in QAApprovalBO.getQAList(): " + ex.toString());
            logger.error(ex);
        }

        return beanQAList;
    }


    /**
     * PL approves timesheet in his project
     * @author  Nguyen Thai Son.
     * @version  November 20, 2002.
     * @param   beanQAList QAListBean: input data
     * @param   beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public int approveByQA(UserInfoBean beanUserInfo, QAListBean beanQAList) throws Exception {
        int nResult = -1;

        String strApprover = beanUserInfo.getLoginName();
        String strListingName = beanQAList.getListingName();
        String[] arrIdList = beanQAList.getArrIdList();
        String[] arrComment = beanQAList.getArrComment();
        String[] arrKPA = beanQAList.getArrKPA_ID();
        String strAction = beanQAList.getUpdateAction();

        int nStatus = 2;
        if (TIMESHEET.QA_APPROVE.equalsIgnoreCase(strAction))
            nStatus = QA_APPROVE_STATUS;
        else if (TIMESHEET.QA_REJECT.equalsIgnoreCase(strAction))
            nStatus = QA_REJECT_STATUS;

        try {
            getApproveEJB();
            // When QA approve, map empty KPA by Process-KPA mapping automatically
            if (TIMESHEET.QA_APPROVE.equals(strAction)) {
                objApprove.changeStatus(strListingName, nStatus, arrIdList, arrComment, arrKPA, strApprover);
            }
            else {  // Not map KPA if not an Approve action
                objApprove.changeStatus(strListingName, nStatus, arrIdList, arrComment, strApprover);
            }
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in QAApprovalBO.approveByQA(): " + ex.toString());
            logger.error(ex);
        }

        return nResult;
    }

    /**
     * Get QAUpdate info to display a form.
     * @author  Nguyen Thai Son.
     * @version  November 20, 2002.
     * @exception   Exception    If an exception occurred.
     */
    public QAUpdateBean getQAUpdateInfo(String[] arrID) throws Exception {
        QAUpdateBean beanQAUpdate = new QAUpdateBean();
        int nSortby = 1;

        try {
            getApproveEJB();

            String strIdList = "";
            for (int i = 0; i < arrID.length; i++)
                strIdList += arrID[i] + ",";
            strIdList = strIdList.substring(0, strIdList.length() - 1);

            ArrayList dbList = objApprove.getTimesheetList(DATA.VIEW_QA_LIST, strIdList, nSortby);
            Iterator it = dbList.iterator();

            StringMatrix mtxList = new StringMatrix();
            StringVector vecItem = new StringVector(11);

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
                vecItem.setCell(9, apmData.getKpa() + "");

                vecItem.setCell(10, apmData.getStatus() + "");

                mtxList.addRow(vecItem);
            }

            beanQAUpdate.setTimesheetList(mtxList);

            beanQAUpdate.setKindPage(1);
            //Controller.setFromScreen("QAListing");
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in QAApprovalBO.getQAUpdateInfo(): " + ex.toString());
            logger.error(ex);
        }

        return beanQAUpdate;
    }

    /**
     * Update and approve timesheet by QA
     * @author  Nguyen Thai Son.
     * @version  November 20, 2002.
     * @param   beanQAUpdate QAUpdateBean: input data
     * @param   beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public int updateAndApproveByQA(UserInfoBean beanUserInfo, QAUpdateBean beanQAUpdate) throws Exception {
        int nResult = -1;

        String strApprover = beanUserInfo.getLoginName();
        String strAction = beanQAUpdate.getAction();
        StringMatrix mtxList = beanQAUpdate.getUpdateList();

        int nStatus = 99;

        if (TIMESHEET.QA_UPDATE_AND_APPROVE.equalsIgnoreCase(strAction)) {
            nStatus = QA_APPROVE_STATUS;
        }

        int maxrows = mtxList.getNumberOfRows();
        if (maxrows < 1)
            return nResult;

        String[] sId = new String[maxrows];
        String[] sProcess = new String[maxrows];
        String[] sType = new String[maxrows];
        String[] sProduct = new String[maxrows];
        String[] sKpa = new String[maxrows];
        String[] sDate = new String[maxrows];

        int i = 0;
        while (i < maxrows) {
            sId[i] = mtxList.getCell(i, 0);
            sProcess[i] = mtxList.getCell(i, 1);
            sType[i] = mtxList.getCell(i, 2);
            sProduct[i] = mtxList.getCell(i, 3);
            sKpa[i] = mtxList.getCell(i, 4);
            sDate[i] = mtxList.getCell(i, 5);
            i++;
        }

        try {
            getApproveEJB();
            objApprove.QAcorrect(strApprover, nStatus, sId, sType, sProcess, sProduct, sKpa, sDate);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in QAApprovalBO.approveAndUpdateByQA(): " + ex.toString());
            logger.error(ex);
        }

        return nResult;
    }

    /**
     * Save Mapping as JavaScript
     * @author MinhPT
     * @param   beanQAUpdate MappingDetailBean: input data
     * @param   path String                   : input data
     * @exception   Exception    If an exception occurred.
     */
    public void saveJavaScript(MappingDetailBean beanMappingDetail, String path) {
        StringMatrix smProcessList = beanMappingDetail.getProcessList();
        StringMatrix smWorkProductList = beanMappingDetail.getWorkProductList();
        StringMatrix smAllMapping = beanMappingDetail.getMappingList();

        int[] arrNumRelative = beanMappingDetail.getRelativeMapping();
        int nNumberOfProcess = smProcessList.getNumberOfRows();
        //int nNumberOfProductList = smWorkProductList.getNumberOfRows();
        int nNumberOfMapping = smAllMapping.getNumberOfRows();
        //create the temp.js
        try {
            File file = new File(path, TIMESHEET.PR_WP_JS_NAME);
            if (file.exists()) {
                file.delete();
            }
            PrintWriter outJavaScript = new PrintWriter(new FileWriter(file.getAbsolutePath(), true));

            StringBuffer buffer = new StringBuffer("");
            buffer.append("var nNumberOfProcess = ");
            buffer.append(nNumberOfProcess);
            buffer.append(";\n");

            buffer.append("var arrNumRelative = new Array(");
            buffer.append(nNumberOfProcess);
            buffer.append(");\n");

            buffer.append("var arrProcessID = new Array(");
            buffer.append(nNumberOfProcess);
            buffer.append(");\n");

            buffer.append("var arrWPID = new Array(");
            buffer.append(nNumberOfProcess);
            buffer.append(");\n");

            buffer.append("var arrWPName = new Array(");
            buffer.append(nNumberOfProcess);
            buffer.append(");\n");

            outJavaScript.println(buffer.toString());

            for (int i = 0; i < nNumberOfProcess; i++) {
                buffer.setLength(0);

                buffer.append("arrNumRelative[");
                buffer.append(i);
                buffer.append("] = ");
                buffer.append(arrNumRelative[i]);
                buffer.append(";\n");

                buffer.append("arrProcessID[");
                buffer.append(i);
                buffer.append("] = \"");
                buffer.append(smProcessList.getCell(i, 0));
                buffer.append("\";\n");
                outJavaScript.println(buffer.toString());
            }
            for (int i = 0; i < nNumberOfProcess; i++) {
                buffer.setLength(0);

                buffer.append("arrWPID[");
                buffer.append(i);
                buffer.append("] = new Array(");
                buffer.append(arrNumRelative[i]);
                buffer.append(");\n");

                buffer.append("arrWPName[");
                buffer.append(i);
                buffer.append("] = new Array(");
                buffer.append(arrNumRelative[i]);
                buffer.append(");\n");

                outJavaScript.println(buffer.toString());

                /*outJavaScript.println("arrWPID[" + i + "] = new Array(" + arrNumRelative[i] + ");");
                outJavaScript.println("arrWPName[" + i + "] = new Array(" + arrNumRelative[i] + ");");*/
                String[] arrCurrentWPID = new String[arrNumRelative[i]];
                String[] arrCurrentWPName = new String[arrNumRelative[i]];
                int nCol = 0;
                for (int l = 0; l < nNumberOfMapping; l++) {
                    if (smProcessList.getCell(i, 0).equals(smAllMapping.getCell(l, 0))) {
                        arrCurrentWPID[nCol] = smAllMapping.getCell(l, 1);
                        int nWPIndex = smWorkProductList.indexOf(arrCurrentWPID[nCol], 0);
                        arrCurrentWPName[nCol] = smWorkProductList.getCell(nWPIndex, 1);
                        nCol++;
                    }
                }

                for (int j = 0; j < arrNumRelative[i]; j++) {
                    buffer.setLength(0);

                    buffer.append("arrWPID[");
                    buffer.append(i);
                    buffer.append("][");
                    buffer.append(j);
                    buffer.append("] = \"");
                    buffer.append(arrCurrentWPID[j]);
                    buffer.append("\";\n");

                    buffer.append("arrWPName[");
                    buffer.append(i);
                    buffer.append("][");
                    buffer.append(j);
                    buffer.append("] = \"");
                    buffer.append(arrCurrentWPName[j]);
                    buffer.append("\";\n");
                    outJavaScript.println(buffer.toString());
                    /*outJavaScript.println("arrWPID[" + i + "][" + j + "] = \"" + arrCurrentWPID[j] + "\";");
                    outJavaScript.println("arrWPName[" + i + "][" + j + "] = \"" + arrCurrentWPName[j] + "\";");*/
                }
            }
            buffer.setLength(0);
            outJavaScript.close();
        }
        catch (IOException e) {
        }

    }

}