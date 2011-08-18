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
 
 package fpt.timesheet.bo.Mapping;


import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

//import fpt.timesheet.InputTran.ejb.Input;
//import fpt.timesheet.InputTran.ejb.InputHome;
import fpt.timesheet.InputTran.ejb.InputEJBLocal;
import fpt.timesheet.InputTran.ejb.InputEJBLocalHome;
import fpt.timesheet.bean.Mapping.MappingDetailBean;
import fpt.timesheet.bean.Mapping.MappingListBean;
import fpt.timesheet.bean.Mapping.MappingSaveBean;
import fpt.timesheet.constant.DATA;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

/**
 * @author thailh
 */
public class MappingBO {

    private InputEJBLocalHome homeInput = null;
    private InputEJBLocal objInput = null;

    private static Logger logger = Logger.getLogger(MappingBO.class.getName());

    /**
     * @see java.lang.Object#Object()
     */
    public MappingBO() {
    }

    /**
     * Method getInputEJB.
     * @throws NamingException
     */
    private void getInputEJB() throws NamingException {
        try {
            if (homeInput == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.INPUT);

//              homeInput = (InputHome) javax.rmi.PortableRemoteObject.narrow(objref, InputHome.class);
				homeInput = (InputEJBLocalHome)(objref);
                if (objInput == null)
                    objInput = homeInput.create();
            }
        }
        catch (NamingException ex) {
            logger.debug("MappingBO.getInputEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
            throw ex;
        }
        catch (Exception ex) {
            logger.debug("MappingBO.getInputEJB() error! -- " + ex.getMessage());
        }
    }

    /**
     * Method getMappingList.
     * @param beanMappingList
     * @return MappingListBean
     */
    public MappingListBean getMappingList(MappingListBean beanMappingList) {

        try {
            getInputEJB();

            //Getting the process list

            ArrayList alProcessList = objInput.getProcessList();
            int nRows = alProcessList.size() / 2;
            StringMatrix smProcessList = new StringMatrix(nRows, 2);
            Iterator itProcessList = alProcessList.iterator();
            String strItemID;
            String strItemName;
            int i = 0;
            while (itProcessList.hasNext()) {
                strItemID = itProcessList.next().toString().trim();
                smProcessList.setCell(i, 0, strItemID);
                strItemName = itProcessList.next().toString().trim();
                smProcessList.setCell(i, 1, strItemName);
                i++;
            }

            if (nRows > 0) {
                if (DATA.PROCESS_NOTHING.equals(beanMappingList.getCurrentProcessID())) {
                    beanMappingList.setCurrentProcessID(smProcessList.getCell(0, 0));
                }
            }

            beanMappingList.setProcessList(smProcessList);
            //CurrentProcessID
            int nIndex = smProcessList.indexOf(beanMappingList.getCurrentProcessID(), 0);
            if (nIndex >= 0) {
                beanMappingList.setCurrentProcessName(smProcessList.getCell(nIndex, 1));
            }

            //Getting the current WorkProduct list
            ArrayList alCurrentWorkProduct = objInput.getMappingList(beanMappingList.getCurrentProcessID());
            nRows = alCurrentWorkProduct.size() / 2;
            StringMatrix smCurrentWorkProductList = new StringMatrix(nRows, 2);
            Iterator itCurrentWorkProductList = alCurrentWorkProduct.iterator();

            i = 0;
            while (itCurrentWorkProductList.hasNext()) {
                strItemID = itCurrentWorkProductList.next().toString().trim();
                smCurrentWorkProductList.setCell(i, 0, strItemID);
                strItemName = itCurrentWorkProductList.next().toString().trim();
                smCurrentWorkProductList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingList.setCurrentWorkProductList(smCurrentWorkProductList);

        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in MappingBO.getMappingList(): " + ex.toString());
            logger.error(ex);
        }

        return beanMappingList;
    }

    /**
     * Method getMappingAddForm.
     * @param beanMappingDetail
     * @return MappingDetailBean
     */
    public MappingDetailBean getMappingAddForm(MappingDetailBean beanMappingDetail) {
        try {
            getInputEJB();

            //Getting WorkProduct list

            ArrayList alWorkProductList = objInput.getWorkProductList();
            int nRows = alWorkProductList.size() / 2;
            StringMatrix smWorkProductList = new StringMatrix(nRows, 2);
            Iterator itWorkProductList = alWorkProductList.iterator();
            String strItemID;
            String strItemName;
            int i = 0;
            while (itWorkProductList.hasNext()) {
                strItemID = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 0, strItemID);
                strItemName = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingDetail.setWorkProductList(smWorkProductList);

            //Getting the current workproduct list
            StringMatrix smCurrentWorkProductList = new StringMatrix(0, 2); //not null
            beanMappingDetail.setCurrentWorkProductList(smCurrentWorkProductList);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in MappingBO.getMappingAddForm(): " + ex.toString());
            logger.error(ex);
        }

        return beanMappingDetail;
    }

    /**
     * Method getMappingUpdateForm.
     * @param beanMappingDetail
     * @return MappingDetailBean
     */
    public MappingDetailBean getMappingUpdateForm(MappingDetailBean beanMappingDetail) {

        try {
            getInputEJB();

            //Getting WorkProduct list

            ArrayList alWorkProductList = objInput.getWorkProductList();
            int nRows = alWorkProductList.size() / 2;
            StringMatrix smWorkProductList = new StringMatrix(nRows, 2);
            Iterator itWorkProductList = alWorkProductList.iterator();
            String strItemID;
            String strItemName;
            int i = 0;
            while (itWorkProductList.hasNext()) {
                strItemID = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 0, strItemID);
                strItemName = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingDetail.setWorkProductList(smWorkProductList);

            //Getting the current workproduct list
            ArrayList alCurrentWorkProductList = objInput.getMappingList(beanMappingDetail.getCurrentProcessID());
            nRows = alCurrentWorkProductList.size() / 2;
            StringMatrix smCurrentWorkProductList = new StringMatrix(nRows, 2);
            Iterator itCurrentWorkProductList = alCurrentWorkProductList.iterator();
            i = 0;
            while (itCurrentWorkProductList.hasNext()) {
                strItemID = itCurrentWorkProductList.next().toString().trim();
                smCurrentWorkProductList.setCell(i, 0, strItemID);
                strItemName = itCurrentWorkProductList.next().toString().trim();
                smCurrentWorkProductList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingDetail.setCurrentWorkProductList(smCurrentWorkProductList);

        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in MappingBO.getMappingUpdateForm(): " + ex.toString());
            logger.error(ex);
        }

        return beanMappingDetail;
    }

    /**
     * Method saveDetailForm.
     * @param beanMappingSave
     * @param strCurrentMappingState
     * @return int
     */
    public int saveDetailForm(MappingSaveBean beanMappingSave, String strCurrentMappingState) {

        try {
            getInputEJB();
            if ("Adding".equals(strCurrentMappingState)) {
                if (objInput.checkValidateWorkProduct(beanMappingSave.getCurrentProcessID(), beanMappingSave.getCurrentWorkProductListID()) == 0) {
                    objInput.addWorkProduct(beanMappingSave.getCurrentProcessID(), beanMappingSave.getCurrentWorkProductListID());
                }
                else {
                    return -1;
                }
            }
            else {
                ArrayList alEmptyList = new ArrayList();
                objInput.deleteWorkProduct(beanMappingSave.getCurrentProcessID(), alEmptyList);
                objInput.addWorkProduct(beanMappingSave.getCurrentProcessID(), beanMappingSave.getCurrentWorkProductListID());
            }

        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in MappingBO.saveDetailForm(): " + ex.toString());
            logger.error(ex);
        }

        return 0; //Saving successfull

    }


    /**
     * Method deleteMapping.
     * @param strProcessID
     * @param alCurrentWorkProductIDList
     */
    public void deleteMapping(String strProcessID, ArrayList alCurrentWorkProductIDList) {

        try {
            getInputEJB();
            objInput.deleteWorkProduct(strProcessID, alCurrentWorkProductIDList);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in MappingBO.deleteMapping(): " + ex.toString());
            logger.error(ex);
        }

    }

    /**
     * Method getMappingForUse.
     * @return MappingDetailBean
     */
    public MappingDetailBean getMappingForUse() {

        MappingDetailBean beanMappingDetail = new MappingDetailBean();

        try {
            getInputEJB();

            //Getting WorkProduct list

            ArrayList alProcessList = objInput.getProcessList();
            int nRows = alProcessList.size() / 2;
            StringMatrix smProcessList = new StringMatrix(nRows, 2);
            Iterator itProcessList = alProcessList.iterator();
            String strItemID;
            String strItemName;
            int i = 0;
            while (itProcessList.hasNext()) {
                strItemID = itProcessList.next().toString().trim();
                smProcessList.setCell(i, 0, strItemID);
                strItemName = itProcessList.next().toString().trim();
                smProcessList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingDetail.setProcessList(smProcessList);

            ArrayList alWorkProductList = objInput.getWorkProductList();
            nRows = alWorkProductList.size() / 2;
            StringMatrix smWorkProductList = new StringMatrix(nRows, 2);
            Iterator itWorkProductList = alWorkProductList.iterator();
            i = 0;
            while (itWorkProductList.hasNext()) {
                strItemID = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 0, strItemID);
                strItemName = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingDetail.setWorkProductList(smWorkProductList);

            ArrayList alAllMapping = objInput.getAllMapping();
            nRows = alAllMapping.size() / 2;
            StringMatrix smAllMapping = new StringMatrix(nRows, 2);
            Iterator itAllMapping = alAllMapping.iterator();
            i = 0;
            String strProcessID;
            String strWPID;
            while (itAllMapping.hasNext()) {
                strProcessID = itAllMapping.next().toString().trim();
                smAllMapping.setCell(i, 0, strProcessID);
                strWPID = itAllMapping.next().toString().trim();
                smAllMapping.setCell(i, 1, strWPID);
                i++;
            }
            beanMappingDetail.setMappingList(smAllMapping);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in MappingBO.getMappingUpdateForm(): " + ex.toString());
            logger.error(ex);
        }

        return beanMappingDetail;
    }

    /**
     * Method getMappingForSaveJavaScript.
     * @param beanMappingDetail
     * @return MappingDetailBean
     */
    public MappingDetailBean getMappingForSaveJavaScript(MappingDetailBean beanMappingDetail) {
        try {
            getInputEJB();
            //getProcesList
            ArrayList alProcessList = objInput.getProcessList();
            int nRows = alProcessList.size() / 2;
            StringMatrix smProcessList = new StringMatrix(nRows, 2);
            Iterator itProcessList = alProcessList.iterator();
            String strItemID;
            String strItemName;
            int i = 0;
            while (itProcessList.hasNext()) {
                strItemID = itProcessList.next().toString().trim();
                smProcessList.setCell(i, 0, strItemID);
                strItemName = itProcessList.next().toString().trim();
                smProcessList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingDetail.setProcessList(smProcessList);
            //Getting WorkProduct list
            ArrayList alWorkProductList = objInput.getWorkProductList();
            nRows = alWorkProductList.size() / 2;
            StringMatrix smWorkProductList = new StringMatrix(nRows, 2);
            Iterator itWorkProductList = alWorkProductList.iterator();
            strItemID = "";
            strItemName = "";
            i = 0;
            while (itWorkProductList.hasNext()) {
                strItemID = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 0, strItemID);
                strItemName = itWorkProductList.next().toString().trim();
                smWorkProductList.setCell(i, 1, strItemName);
                i++;
            }
            beanMappingDetail.setWorkProductList(smWorkProductList);
            //get all Mapping
            ArrayList alAllMapping = objInput.getAllMapping();
            nRows = alAllMapping.size() / 2;
            StringMatrix smAllMapping = new StringMatrix(nRows, 2);
            Iterator itAllMapping = alAllMapping.iterator();
            i = 0;
            String strProcessID;
            String strWPID;
            while (itAllMapping.hasNext()) {
                strProcessID = itAllMapping.next().toString().trim();
                smAllMapping.setCell(i, 0, strProcessID);
                strWPID = itAllMapping.next().toString().trim();
                smAllMapping.setCell(i, 1, strWPID);
                i++;
            }
            beanMappingDetail.setMappingList(smAllMapping);

        }

        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in MappingBO.getMappingUpdateForm(): " + ex.toString());
            logger.error(ex);
        }

        return beanMappingDetail;
    }

}
