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
 * @(#)NCBO.java 17-Mar-03
 */

package fpt.ncms.bo.User;

import fpt.ncms.bean.NCAddBean;
import fpt.ncms.bean.NCListBean;
import fpt.ncms.bean.NCListPersonalBean;
import fpt.ncms.bean.NCReportBean;
import fpt.ncms.bean.NCReportPivotBean;
import fpt.ncms.bean.UserInfoBean;

import fpt.ncms.constant.JNDI;
import fpt.ncms.constant.NCMS;

import fpt.ncms.ejb.NC;
import fpt.ncms.ejb.NCEJBLocal;
import fpt.ncms.ejb.NCEJBLocalHome;
import fpt.ncms.ejb.NCHome;
import fpt.ncms.model.NCModel;

import fpt.ncms.util.CommonUtil.CommonUtil;
import fpt.ncms.util.StringUtil.StringMatrix;

import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Class NCBO
 * Bean object for manipulating with NC
 * @version 1.0 17-Mar-03
 * @author
 */
public final class NCBO {
//    private NCHome homeNC = null;
//    private NC objNC = null;
	private NCEJBLocalHome homeNC = null; // HaiMM
	private NCEJBLocal objNC = null;


    /**
     * NCBO
     * Class constructor
     */
    public NCBO() {
        getNCEJB();
    }

    /**
     * getNCEJB
     * Get home and create NC object
     * @throws  NamingException
     */
    private void getNCEJB() {
        try {
            if (homeNC == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.NC);
                homeNC = (NCEJBLocalHome)(objref);

                if (objNC == null) {
                    objNC = homeNC.create();
                }
            }
        }
        catch (NamingException ex) {
            System.out.println("NCBO.getNCEJB() error! -- " + ex.getMessage() +
                    "---" + ex.getResolvedName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * getNCList
     * Get list of NC
     * @param   beanUserInfo - user's information
     * @param   beanNCList - input condition for NC list
     * @return  list of NC
     * @throws  Exception
     */
    public final NCListBean getNCList(UserInfoBean beanUserInfo,
            NCListBean beanNCList) {
        try {
            //String strFilter = "";

            //String strNCType = beanNCList.getCurrentNCType();

            /*if ("".equals(strNCType)) {
                strFilter += (" AND (NC.NCType IN (" + strNCType + ")) ");
            }*/

            beanNCList.setTotal(objNC.getNumByCriteria(
                    beanUserInfo.getLoginName(), beanUserInfo.getRoleName(),
                    beanNCList.getCondition(),
                    beanUserInfo.getLocation(),
                    beanNCList.getCurrentFromDate(),
                    beanNCList.getCurrentToDate()));
            // Client selected page
            int nSelectedPage = beanNCList.getNumPage();
            // Number of pages when apply filter
            int nTotalPages = (beanNCList.getTotal() + NCMS.NUM_PER_PAGE - 1) / NCMS.NUM_PER_PAGE;
            // If selected page > number of pages => reset page to 1
            int nPage = (nTotalPages >= nSelectedPage) ? nSelectedPage : 1;
            beanNCList.setNumPage(nPage);
            
            ArrayList arrList = objNC.queryByCriteria(beanUserInfo.getLoginName(),
                    beanUserInfo.getRoleName(), beanNCList.getFields(),
                    beanNCList.getCondition(),
                    beanNCList.getOrderBy(), beanNCList.getDirection(),
                    ((nPage - 1) * NCMS.NUM_PER_PAGE) + 1,
                    nPage * NCMS.NUM_PER_PAGE,
                    beanNCList.getCurrentFromDate(),
                    beanNCList.getCurrentToDate(),
                    beanUserInfo.getLocation());
            int nFields = beanNCList.getNumFields();
            int nRows = arrList.size() / nFields;
            StringMatrix smList = new StringMatrix(nRows, nFields);

            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nFields; j++) {
                    smList.setCell(i, j,CommonUtil.correctErrorChar(
                            CommonUtil.correctHTMLError(arrList.get((nFields * i) +
                            j).toString()),'\n',"<br style='mso-data-placement:same-cell;'/>"));
                }
            }

            beanNCList.setNCList(smList);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return beanNCList;
    }

    /**
     * getNCListPersonal
     * Get list of NC
     * @param   beanUserInfo - user's information
     * @param   beanNCListPersonal - input condition for NC list
     * @return  list of NC
     * @throws  Exception
     */
    public final NCListPersonalBean getNCListPersonal(
            UserInfoBean beanUserInfo, NCListPersonalBean beanNCListPersonal) {
        try {
            beanNCListPersonal.setTotal(objNC.getNumByCriteria(
                    beanUserInfo.getLoginName(), beanUserInfo.getRoleName(),
                    beanNCListPersonal.getCondition(),
                    beanUserInfo.getLocation(),
                    beanNCListPersonal.getCurrentFromDate(),
                    beanNCListPersonal.getCurrentToDate()));
            // Client selected page
            int nSelectedPage = beanNCListPersonal.getNumPage();
            // Number of pages when apply filter
            int nTotalPages = (beanNCListPersonal.getTotal() + NCMS.NUM_PER_PAGE - 1) / NCMS.NUM_PER_PAGE;
            // If selected page > number of pages => reset page to 1
            int nPage = (nTotalPages >= nSelectedPage) ? nSelectedPage : 1;
            beanNCListPersonal.setNumPage(nPage);

            ArrayList arrList = objNC.queryByCriteria(beanUserInfo.getLoginName(),
                    beanUserInfo.getRoleName(), beanNCListPersonal.getFields(),
                    beanNCListPersonal.getCondition(),
                    beanNCListPersonal.getOrderBy(), beanNCListPersonal.getDirection(),
                    ((nPage - 1) * NCMS.NUM_PER_PAGE) + 1,
                    nPage * NCMS.NUM_PER_PAGE,
                    beanNCListPersonal.getCurrentFromDate(),
                    beanNCListPersonal.getCurrentToDate(),
                    beanUserInfo.getLocation());
            int nFields = beanNCListPersonal.getNumFields();
            int nRows = arrList.size() / nFields;
            StringMatrix smList = new StringMatrix(nRows, nFields);

            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nFields; j++) {
                    smList.setCell(i, j,
                            CommonUtil.correctHTMLError(arrList.get((nFields * i) +
                            j).toString()));
                }
            }

            beanNCListPersonal.setNCList(smList);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return beanNCListPersonal;
    }

    /**
     * getNCListExport
     * Get list of NC for export
     * @param   beanUserInfo - user's information
     * @param   beanNCList - input condition for NC list
     * @return  list of NC for export
     * @throws  Exception
     */
    public final NCListBean getNCListExport(UserInfoBean beanUserInfo,
            NCListBean beanNCList) {
        try {
            int nTotal = objNC.getNumByCriteria(beanUserInfo.getLoginName(),
                    beanUserInfo.getRoleName(), beanNCList.getCondition(),
                    beanUserInfo.getLocation(),
                    beanNCList.getCurrentFromDate(),
                    beanNCList.getCurrentToDate());
            ArrayList arrList = objNC.queryByCriteria(beanUserInfo.getLoginName(),
                    beanUserInfo.getRoleName(), beanNCList.getFields(),
                    beanNCList.getCondition(),
                    beanNCList.getOrderBy(), beanNCList.getDirection(),
                    1, nTotal,
                    beanNCList.getCurrentFromDate(),
                    beanNCList.getCurrentToDate(),
                    beanUserInfo.getLocation());
            int nFields = beanNCList.getNumFields();
            int nRows = arrList.size() / nFields;
            StringMatrix smList = new StringMatrix(nRows, nFields);

            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nFields; j++) {
					smList.setCell(i, j,CommonUtil.correctErrorChar(
							CommonUtil.correctHTMLError(arrList.get((nFields * i) +
							j).toString()),'\n',"<br style='mso-data-placement:same-cell;'/>"));
                }
            }

            beanNCList.setNCList(smList);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return beanNCList;
    }

    /**
     * addNC
     * Add new NC
     * @param   modelNC - NC information
     * @return  indicator if adding was successful or not
     * @throws  Exception
     */
    public final int addNC(NCModel modelNC, int nLocation) throws Exception{
        int nRetVal = -1;

        try {
            nRetVal = objNC.addNC(modelNC, nLocation);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return nRetVal;
    }

    /**
     * updateNC
     * Update NC
     * @param   modelNC - NC information
     * @return  number of updated record
     * @throws  Exception
     */
    public final int updateNC(NCModel modelNC, UserInfoBean beanUserInfo)
            throws Exception
    {
        int retVal = -1;
        try {
            retVal = objNC.updateNC(modelNC, beanUserInfo.getLoginName(),
                                    beanUserInfo.getLocation());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return retVal;
    }

    /**
     * getNCDetail
     * Get NC information
     * @param   strNCID - ID of NC to retrieve from database
     * @param   beanNCAdd - NC information
     * @return  required NC information
     * @throws  Exception
     */
    public final NCAddBean getNCDetail(String strNCID, NCAddBean beanNCAdd,
                                       int nLocation) {
        try {
            NCModel modelNC = objNC.queryByID(strNCID, nLocation);
            String strHistory = objNC.getNCHistory(strNCID, nLocation);
            strHistory =
                    CommonUtil.correctErrorChar(strHistory, '\r', "<br>");
            strHistory = strHistory.replace('\n', ' ');
            beanNCAdd.setNCModel(modelNC);
            beanNCAdd.setHistory(strHistory);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return beanNCAdd;
    }

    /**
     * addNC
     * Add new NC
     * @param   modelNC - NC information
     * @return  indicator if delete is successful or not
     * @throws  Exception
     */
    public final int deleteNC(NCModel modelNC, int nLocation) throws Exception{
        int nRetVal = -1;
        try {
            nRetVal = objNC.deleteNC(modelNC, nLocation);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return nRetVal;
    }

    /**
     * getNCReport
     * Get report
     * @param   beanNCReport - input condition for report
     * @return  report
     * @throws  Exception
     */
    public final NCReportBean getNCReport(NCReportBean beanNCReport,
                                          UserInfoBean beanUserInfo) {
        try {
            ArrayList arrList = objNC.queryForReport(
                    beanNCReport.getFromDate(), beanNCReport.getToDate(),
                    beanNCReport.getGroupBy(), beanUserInfo.getLocation(),
                    beanNCReport.getNCTypes(), beanNCReport.getTypeOfCause());
            int nCols = 9;
            int nRows = arrList.size() / nCols;
            StringMatrix smList = null;
            if(nRows>0){
                smList = new StringMatrix(nRows + 1, nCols);
            }
            else
            {
                smList = new StringMatrix(nRows, nCols);

            }

            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nCols; j++) {
                    smList.setCell(i, j, arrList.get((i * nCols) + j).toString());
                }
            }

            if(nRows>0){
                String strTotalList = "All";
                smList.setCell(nRows, 0, strTotalList);
                ArrayList arrTotalList = objNC.queryForReportAll(
                        beanNCReport.getFromDate(), beanNCReport.getToDate(),
                        beanUserInfo.getLocation(),
                        beanNCReport.getNCTypes(),
                        beanNCReport.getTypeOfCause());
                for(int j=1; j<nCols; j++){
                    smList.setCell(nRows, j, arrTotalList.get(j - 1).toString());
                }
            }
            beanNCReport.setReport(smList);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return beanNCReport;
    }

    /**
     * getNCReportPivot
     * Get pivot report
     * @param   beanNCReport - input condition for pivot report
     * @return  pivot report data
     * @throws  Exception
     */
    public final NCReportPivotBean getNCReportPivot(
            NCReportPivotBean beanNCReportPivot, UserInfoBean beanUserInfo) {
        try {
            ArrayList arrList = objNC.queryForPivotReport(
                            beanNCReportPivot.getFromDate(),
                            beanNCReportPivot.getToDate(),
                            beanNCReportPivot.getGroupBy(),
                            beanNCReportPivot.getReportType(),
                            beanUserInfo.getLocation(),
                            beanNCReportPivot.getNCTypes(),
                            beanNCReportPivot.getTypeOfCause());
            int arrNum[] = (int[]) arrList.get(0);
            ArrayList arrConstant = (ArrayList) arrList.get(1);
            ArrayList arrData = (ArrayList) arrList.get(2);
            int nRows = arrNum[0];
            int nCols = arrNum[1];
            StringMatrix smList = new StringMatrix(nRows + 1, nCols);

            for (int i = 0; i < nCols; i++) {
                smList.setCell(0, i, (String) arrConstant.get(i));
            }
            
            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nCols; j++) {
                    smList.setCell(i+1, j,
                        CommonUtil.correctHTMLError(
                            arrData.get((i * nCols) + j).toString()));
                }
            }
            beanNCReportPivot.setReport(smList);
            beanNCReportPivot.setNumReportField(nCols);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return beanNCReportPivot;
    }
}
