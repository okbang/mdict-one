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
 * @(#)ConstantBO.java 19-Mar-03
 */


package fpt.ncms.bo.Common;

import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import fpt.ncms.bean.ConstantAddBean;
import fpt.ncms.bean.ConstantListBean;
import fpt.ncms.bean.UserInfoBean;
import fpt.ncms.constant.JNDI;
import fpt.ncms.constant.NCMS;
import fpt.ncms.ejb.Constant;
import fpt.ncms.ejb.ConstantEJBLocal;
import fpt.ncms.ejb.ConstantEJBLocalHome;
import fpt.ncms.ejb.ConstantHome;
import fpt.ncms.model.NCModel;
import fpt.ncms.util.CommonUtil.CommonUtil;
import fpt.ncms.util.StringUtil.StringMatrix;
import fpt.ncms.util.StringUtil.*;

/**
 * Class ConstantBO
 * Bean Object for various NC combo
 * @version 1.0 19-Mar-03
 * @author
 */
public final class ConstantBO {
//    private ConstantHome homeConstant = null;
//    private Constant objConstant = null;

	private ConstantEJBLocalHome homeConstant = null; // HaiMM
	private ConstantEJBLocal objConstant = null;

    private int n_Usage = 0; // 0-NCMS, 1-CallLog;  Default is 0-NCMS
    private String strExcludeConstants = "";

    /**
     * ConstantBO
     * Class constructor
     */
    public ConstantBO() {
    }

    /*
     * getEJBHome
     * Get EJB home
     * @throws  NamingException
     * @throws  Exception
    private void getEJBHome() {
        try {
            if (homeConstant == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.CONSTANT);
                homeConstant =
                        (ConstantHome)javax.rmi.PortableRemoteObject.narrow(
                                objref, ConstantHome.class);
            }
        }
        catch (NamingException ex) {
            System.out.println("ConstantBO.getEJBHome() error! -- "
                    + ex.getMessage() + "---" + ex.getResolvedName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * getEJBRemote
     * Get remote EJB
     * @throws  Exception
    private void getEJBRemote() {
        try {
            if (objConstant == null) {
                objConstant = homeConstant.create();
            }
        }
        catch (Exception ex) {
            System.out.println("ConstantBO.getEJBRemote() error! -- "
                    + ex.getMessage());
        }
    }
*/

    /**
     * getEJB
     * Get remote EJB
     * @throws  NamingException
     * @throws  Exception
     */
    private void getEJB() {
        try {
            if (homeConstant == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.CONSTANT);
                homeConstant = (ConstantEJBLocalHome)(objref);
                if (objConstant == null) {
                    objConstant = homeConstant.create();
                }
            }
        }
        catch (NamingException ex) {
            System.out.println("ConstantBO.getEJB() error! -- "
                    + ex.getMessage() + "---" + ex.getResolvedName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * convertToStrMatrix
     * Convert array list to 2-dimensional string matrix for combo box
     * @param   arrList  - array list to convert
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  string matrix
     */
    //modified by Minh PT
    //15-Dec-03
    //for add NONE and ALL value
    private StringMatrix convertToStrMatrix(ArrayList arrList,
            int nType) {
        StringVector strRow = new StringVector(2); 
        int nSize = arrList.size() / 2;
        int i = 0;
        StringMatrix smList = smList = new StringMatrix();

        if ( nType == NCMS.CBO_ALL_VALUE){
            strRow.setCell(0, NCMS.CBO_ITEM_ALL_VALUE);
            strRow.setCell(1, NCMS.CBO_ITEM_ALL_STRING);
            smList.addRow(strRow);

        }
        else if(nType == NCMS.CBO_NONE_VALUE){
            strRow.setCell(0, NCMS.CBO_ITEM_NONE_VALUE);
            strRow.setCell(1, NCMS.CBO_ITEM_NONE_STRING);
            smList.addRow(strRow);
        }
        else if(nType == NCMS.CBO_BOTH_ALL_AND_NONE_VALUE ){
            strRow.setCell(0, NCMS.CBO_ITEM_ALL_VALUE);
            strRow.setCell(1, NCMS.CBO_ITEM_ALL_STRING);
            smList.addRow(strRow);
            strRow.setCell(0, NCMS.CBO_ITEM_NONE_VALUE);
            strRow.setCell(1, NCMS.CBO_ITEM_NONE_STRING);
            smList.addRow(strRow);
        }
        else if(nType == NCMS.CBO_EMPTY_VALUE){
            strRow.setCell(0, NCMS.CBO_ITEM_NONE_VALUE);
            strRow.setCell(1, "");
            smList.addRow(strRow);
        }

        for (int nRow = 0; nRow < nSize; nRow++) {
            strRow.setCell(0,CommonUtil.correctHTMLError(
                            arrList.get(2 * nRow).toString()));
            strRow.setCell(1,CommonUtil.correctHTMLError(
                            arrList.get(2 * nRow + 1).toString()));
            smList.addRow(strRow);
        }
        return smList;
    }

    /**
     * getTypeOfActionList
     * Get list of actions
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of actions
     */
    public final StringMatrix getTypeOfActionList(int nType) {
        ArrayList typeOfActionList = new ArrayList();
        try {
            getEJB();
            typeOfActionList = objConstant.queryConstantByType("TypeOfAction", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getTypeOfActionList(): " + ex.toString());
        }
        return convertToStrMatrix(typeOfActionList, nType);
    }

    /*
     * getStatusList
     * Get list of status
     * @param   nViewNC - number of view
     * @return  list of status
    public static final StringMatrix getStatusList(int nViewNC) {
        StringMatrix smList = null;
        switch (nViewNC) {
            case NCMS.CREATOR_ADD:
                smList = new StringMatrix(1, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
                smList.setCell(0, 1, "Opened");
                break;
            case NCMS.REVIEWER_ADD:
                smList = new StringMatrix(2, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
                smList.setCell(0, 1, "Opened");
                smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
                smList.setCell(1, 1, "Assigned");
                break;
            case NCMS.REVIEWER_ANALYSED:
                smList = new StringMatrix(3, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
                smList.setCell(0, 1, "Opened");
                smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
                smList.setCell(1, 1, "Assigned");
                smList.setCell(2, 0, Integer.toString(NCMS.NC_STATUS_CANCELLED));
                smList.setCell(2, 1, "Cancelled");
                break;
            case NCMS.CREATOR_UPDATE:
                smList = new StringMatrix(1, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
                smList.setCell(0, 1, "Opened");
                break;
            case NCMS.ASSIGNEE_CORRECT:
                smList = new StringMatrix(2, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
                smList.setCell(0, 1, "Assigned");
                smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_PENDING));
                smList.setCell(1, 1, "Pending");
                break;
            case NCMS.PQA_REVIEW:
                smList = new StringMatrix(3, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_PENDING));
                smList.setCell(0, 1, "Pending");
                smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
                smList.setCell(1, 1, "Opened");
                smList.setCell(2, 0, Integer.toString(NCMS.NC_STATUS_CLOSED));
                smList.setCell(2, 1, "Closed");
                break;
            case NCMS.VIEW_ONLY:
                smList = new StringMatrix(5, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
                smList.setCell(0, 1, "Opened");
                smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_CANCELLED));
                smList.setCell(1, 1, "Cancelled");
                smList.setCell(2, 0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
                smList.setCell(2, 1, "Assigned");
                smList.setCell(3, 0, Integer.toString(NCMS.NC_STATUS_PENDING));
                smList.setCell(3, 1, "Pending");
                smList.setCell(4, 0, Integer.toString(NCMS.NC_STATUS_CLOSED));
                smList.setCell(4, 1, "Closed");
                break;
            //added by MinhPT
            //12-Dec-03
            //for Add All Status to combo Status 
            case NCMS.ALL_STATUS:
                smList = new StringMatrix(6, 2);
                smList.setCell(0, 0, NCMS.STR_ALL_VALUE);
                smList.setCell(0, 1, "(All)");
                smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
                smList.setCell(1, 1, "Opened");
                smList.setCell(2, 0, Integer.toString(NCMS.NC_STATUS_CANCELLED));
                smList.setCell(2, 1, "Cancelled");
                smList.setCell(3, 0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
                smList.setCell(3, 1, "Assigned");
                smList.setCell(4, 0, Integer.toString(NCMS.NC_STATUS_PENDING));
                smList.setCell(4, 1, "Pending");
                smList.setCell(5, 0, Integer.toString(NCMS.NC_STATUS_CLOSED));
                smList.setCell(5, 1, "Closed");
                break;
            case NCMS.PQA_REVIEW_ASSIGNED:
                smList = new StringMatrix(2, 2);
                smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
                smList.setCell(0, 1, "Assigned");
                smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_PENDING));
                smList.setCell(1, 1, "Pending");
                break;
        }
        return smList;
    }
     */

    /**
     * getStatusList
     * Get list of status
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of status
     */
    public final StringMatrix getStatusList(int nComboType,
                                            UserInfoBean beanUserInfo,
                                            NCModel currentNC) {
        StringMatrix smList = new StringMatrix();
        // User can update informations by default
        beanUserInfo.setModifyEnabled(true);
        // Statuses that user can change to
        beanUserInfo.setOpenEnabled(false);
        beanUserInfo.setAssignEnabled(false);
        
        // Add new
        if (currentNC.getStatus() == NCMS.NC_STATUS_NEW) {
            smList.addRow(getStatusItem(NCMS.NC_STATUS_OPENED, beanUserInfo));
            // Revewers, PQA can assign also
            if (beanUserInfo.getRoleName().equals(NCMS.ROLE_REVIEWER) ||
                beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA))
            {
                smList.addRow(getStatusItem(NCMS.NC_STATUS_ASSIGNED, beanUserInfo));
            }
        }
        // Update information
        else if (currentNC.getStatus() == NCMS.NC_STATUS_OPENED) {
            smList.addRow(getStatusItem(NCMS.NC_STATUS_OPENED, beanUserInfo));
            // Reviewer can assign task to user
            if (beanUserInfo.getRoleName().equals(NCMS.ROLE_REVIEWER) ||
                beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA))
            {
                smList.addRow(getStatusItem(NCMS.NC_STATUS_ASSIGNED, beanUserInfo));
				smList.addRow(getStatusItem(NCMS.NC_STATUS_CANCELLED, beanUserInfo));
            }
        }
        else if (currentNC.getStatus() == NCMS.NC_STATUS_ASSIGNED) {
            smList.addRow(getStatusItem(NCMS.NC_STATUS_ASSIGNED, beanUserInfo));
            // Assignee can set status to pending
            if (beanUserInfo.getLoginName().equals(currentNC.getAssignee()) ||
                beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA))
            {
                smList.addRow(getStatusItem(NCMS.NC_STATUS_PENDING, beanUserInfo));
            }
            // Other users not have permission to modify it
            else if (beanUserInfo.getRoleName().equals(NCMS.ROLE_CREATOR) ||
                     beanUserInfo.getRoleName().equals(NCMS.ROLE_ASSIGNEE))
            {
                beanUserInfo.setModifyEnabled(false);
            }
            
            // PQA can set status to Closed/Cancelled directly
            if (beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA)) {
                smList.addRow(getStatusItem(NCMS.NC_STATUS_CLOSED, beanUserInfo));
                // Call log: Admin can cancel an assigned call
                if (beanUserInfo.getLocation() == NCMS.USER_CALL) {
                    smList.addRow(getStatusItem(NCMS.NC_STATUS_CANCELLED, beanUserInfo));
                }
            }
            // Call log: Reviewer can set status to Cancelled directly
            if ((beanUserInfo.getRoleName().equals(NCMS.ROLE_REVIEWER)) &&
                (beanUserInfo.getLocation() == NCMS.USER_CALL))
            {
                smList.addRow(getStatusItem(NCMS.NC_STATUS_CANCELLED, beanUserInfo));
            }
        }
        else if (currentNC.getStatus() == NCMS.NC_STATUS_PENDING) {
            smList.addRow(getStatusItem(NCMS.NC_STATUS_PENDING, beanUserInfo));
            if (beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA)) {
                smList.addRow(getStatusItem(NCMS.NC_STATUS_CLOSED, beanUserInfo));
                smList.addRow(getStatusItem(NCMS.NC_STATUS_OPENED, beanUserInfo));
            }
            // Only PQA can process with Pending status, others can't change it
            else {
                beanUserInfo.setModifyEnabled(false);
            }
        }
		else if (currentNC.getStatus() == NCMS.NC_STATUS_CANCELLED) {
			smList.addRow(getStatusItem(NCMS.NC_STATUS_CANCELLED, beanUserInfo));
			if (beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA)) {
					smList.addRow(getStatusItem(NCMS.NC_STATUS_OPENED, beanUserInfo));
				}
		}
        // Cannot modify records has Closed, Cancelled
        else if (currentNC.getStatus() == NCMS.NC_STATUS_CLOSED) {
            smList.addRow(getStatusItem(NCMS.NC_STATUS_CLOSED, beanUserInfo));
            beanUserInfo.setModifyEnabled(false);
        }        
        
        // Listing
        else if (currentNC.getStatus() == NCMS.NC_STATUS_ALL) {
            // Currently, only (All) status is used for Listing function,
            // other functions Add new/Update, the status is mandatory
            if (nComboType == NCMS.CBO_ALL_VALUE) {
                smList.addRow(getStatusItem(
                        NCMS.NC_STATUS_ALL, beanUserInfo));
            }
            smList.addRow(getStatusItem(NCMS.NC_STATUS_OPENED, beanUserInfo));
            smList.addRow(getStatusItem(NCMS.NC_STATUS_ASSIGNED, beanUserInfo));
            smList.addRow(getStatusItem(NCMS.NC_STATUS_PENDING, beanUserInfo));
            smList.addRow(getStatusItem(NCMS.NC_STATUS_CLOSED, beanUserInfo));
            smList.addRow(getStatusItem(NCMS.NC_STATUS_CANCELLED, beanUserInfo));
        }
        return smList;
    }

    /**
     * getStatusItem
     * Get a status item
     * @param   nStatus Status
     * @return  Status information
     */
    private final StringVector getStatusItem(int nStatus, UserInfoBean beanUserInfo) {
        StringVector svItem = new StringVector(2);
        switch (nStatus) {
            case NCMS.NC_STATUS_OPENED:
                svItem.setCell(0, Integer.toString(NCMS.NC_STATUS_OPENED));
                svItem.setCell(1, "Opened");
                beanUserInfo.setOpenEnabled(true);
                break;
            case NCMS.NC_STATUS_ASSIGNED:
                svItem.setCell(0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
                svItem.setCell(1, "Assigned");
                beanUserInfo.setAssignEnabled(true);
                break;
            case NCMS.NC_STATUS_PENDING:
                svItem.setCell(0, Integer.toString(NCMS.NC_STATUS_PENDING));
                svItem.setCell(1, "Pending");
                break;
            case NCMS.NC_STATUS_CLOSED:
                svItem.setCell(0, Integer.toString(NCMS.NC_STATUS_CLOSED));
                svItem.setCell(1, "Closed");
                break;
            case NCMS.NC_STATUS_CANCELLED:
                svItem.setCell(0, Integer.toString(NCMS.NC_STATUS_CANCELLED));
                svItem.setCell(1, "Cancelled");
                break;
            
            case NCMS.NC_STATUS_ALL:
                svItem.setCell(0, NCMS.CBO_ITEM_ALL_VALUE);
                svItem.setCell(1, NCMS.CBO_ITEM_ALL_STRING);
                break;
        }
        return svItem;
    }

    /*
     * getStatusList
     * Get list of status
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of status
    public final StringMatrix getStatusList(int nType) {
        if (nType == 0) {   // Add new
            StringMatrix smList = new StringMatrix(2, 2);
            smList.setCell(0, 0, Integer.toString(NCMS.NC_STATUS_OPENED));
            smList.setCell(0, 1, "Opened");
            smList.setCell(1, 0, Integer.toString(NCMS.NC_STATUS_ASSIGNED));
            smList.setCell(1, 1, "Assigned");
            return smList;
        }
        else {
            ArrayList statusList = null;
            try {
                getEJB();
                //statusList = objConstant.queryConstantByType("Status", n_Usage);
                // Currently status is common for all systems
                statusList = objConstant.queryConstantByType("Status", 0);
            }
            catch (Exception ex) {
                System.out.println("Exception occurs in "
                        + "ConstantBO.getStatusList(): " + ex.toString());
            }
            return convertToStrMatrix(statusList, nType);
        }
    }
     */

    /**
     * getLevelList
     * Get list of levels
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of levels
     */
    public final StringMatrix getLevelList(int nType) {
        ArrayList levelList = new ArrayList();
        try {
            getEJB();
            levelList = objConstant.queryConstantByType("NCLevel", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getLevelList(): " + ex.toString());
        }
        return convertToStrMatrix(levelList, nType);
    }

    /**
     * getProcessList
     * Get list of process
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of process
     */
    public final StringMatrix getProcessList(int nType) {
        ArrayList processList = new ArrayList();
        try {
            getEJB();
            processList = objConstant.queryConstantByType("Process", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getProcessList(): " + ex.toString());
        }
        return convertToStrMatrix(processList, nType);
    }

    /**
     * getDetectedByList
     * Get list of detected by
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of detected by
     */
    public final StringMatrix getDetectedByList(int nType) {
        ArrayList detectedByList = new ArrayList();
        try {
            getEJB();
            detectedByList = objConstant.queryConstantByType("DetectedBy", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getDetectedByList(): " + ex.toString());
        }
        return convertToStrMatrix(detectedByList, nType);
    }

    /**
     * getTypeOfNCList
     * Get list of type of NC
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of type of NC
     */
    public final StringMatrix getTypeOfNCList(int nType) {
        ArrayList typeOfNCList = new ArrayList();
        try {
            getEJB();
            // NCMS: usage = 0
            // Call: usage = 1 for all Call system (PQA, Network,...)
            typeOfNCList = objConstant.queryConstantByType("NCType",
                        (n_Usage == 0) ? 0 : 1);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getTypeOfNCList(): " + ex.toString());
        }
        return convertToStrMatrix(typeOfNCList, nType);
    }

    /**
     * getISOClauseList
     * Get list of ISO clause
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of ISO clause
     */
    public final StringMatrix getISOClauseList(int nType) {
        ArrayList isoClauseList = new ArrayList();
        try {
            getEJB();
            isoClauseList = objConstant.queryConstantByType("ISOClause", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getTypeOfActionList(): " + ex.toString());
        }
        return convertToStrMatrix(isoClauseList, nType);
    }

    /**
     * getKPAList
     * Get list of KPA
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of KPA
     */
    public final StringMatrix getKPAList(int nType) {
        ArrayList kpaList = new ArrayList();
        try {
            getEJB();
            kpaList = objConstant.queryConstantByType("KPA", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in ConstantBO.getKPAList(): "
                    + ex.toString());
        }
        return convertToStrMatrix(kpaList, nType);
    }

    /**
     * getTypeOfCauseList
     * Get list of type of cause
     * @param   hasBlank - flag indicates 'reserved' blank space for combo
     * @return  list of type of cause
     */
    public final StringMatrix getTypeOfCauseList(int nType) {
        ArrayList typeOfCauseList = new ArrayList();
        try {
            getEJB();
            typeOfCauseList = objConstant.queryConstantByType("TypeOfCause", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getTypeOfCauseList(): " + ex.toString());
        }
        return convertToStrMatrix(typeOfCauseList, nType);
    }

    /**
     * getPriorityList
     * Get list of Priority
     * @param   nType - flag indicates 'reserved' blank space for combo
     * @return  list of Priority
     */
    public final StringMatrix getPriorityList(int nType) {
        ArrayList priorityList = new ArrayList();
        try {
            getEJB();
            priorityList = objConstant.queryConstantByType("Repeat", n_Usage);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getPriorityList(): " + ex.toString());
        }
        return convertToStrMatrix(priorityList, nType);
    }

    /**
     * getGroupByList
     * Get list of group by condition
     * @return  list of group by condition
     */
    public static final StringMatrix getGroupByList() {
        StringMatrix smList = new StringMatrix(2, 2);
        smList.setCell(0, 0, "GroupName");
        smList.setCell(0, 1, "Group");
        smList.setCell(1, 0, "ProjectID");
        smList.setCell(1, 1, "Project");
        return smList;
    }

    /**
     * getReportFieldList
     * Get number of field names for pivot report
     * @param   strReportType - type of report
     * @return  number of field names for pivot report
     */
    public final int getNumReportField(String strReportType) {
        int nRetVal = 0;
        try {
            getEJB();
            // True: show all contant types
            nRetVal = objConstant.getNumByType(strReportType, n_Usage, true) + 3;
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getNumReportField(): " + ex.toString());
        }
        return nRetVal;
    }

    /**
     * getConstantTypeList
     * Get list of constant types
     * @param   hasTypeAll - list will contain type 'All' or not
     * @param   isShowAll - fill all constants OR only manageable constants
     * @return  list of constant types
     */
    public final StringMatrix getConstantTypeList(boolean hasTypeAll,
                                                  boolean isShowAll) {
        StringMatrix smList = null;
        try {
            getEJB();
            ArrayList arrList = objConstant.queryConstantType(
                                n_Usage, isShowAll, strExcludeConstants);
            int nSize = arrList.size();
            int i = hasTypeAll ? 1 : 0;
            smList = new StringMatrix(nSize + i, 2);
            if (hasTypeAll) {
                smList.setCell(0, 0, "All");
                smList.setCell(0, 1, "All");
            }
            
            // NCMS
            if (n_Usage == 0) {
                for (int nRow = 0; nRow < nSize; nRow++) {
                    smList.setCell(nRow + i, 0, CommonUtil.correctHTMLError(
                            arrList.get(nRow).toString()));
                    smList.setCell(nRow + i, 1, CommonUtil.correctHTMLError(
                            arrList.get(nRow).toString()));
                }
            }
            // Call
            else {//if (n_Usage == 1) {
                for (int nRow = 0; nRow < nSize; nRow++) {
                    smList.setCell(nRow + i, 0, CommonUtil.correctHTMLError(
                            arrList.get(nRow).toString()));
                    if ("TypeOfCause".equalsIgnoreCase(arrList.get(nRow).toString())) {
                        smList.setCell(nRow + i, 1, "RequestTo");
                    }
                    else if ("TypeOfAction".equalsIgnoreCase(arrList.get(nRow).toString())) {
                        smList.setCell(nRow + i, 1, "TypeOfSolution");
                    }
                    else if ("Repeat".equalsIgnoreCase(arrList.get(nRow).toString())) {
                        smList.setCell(nRow + i, 1, "Priority");
                    }
                    else {
                        smList.setCell(nRow + i, 1, CommonUtil.correctHTMLError(
                                arrList.get(nRow).toString()));
                    }
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getConstantTypeList(): " + ex.toString());
        }
        return smList;
    }

    /**
     * getConstantList
     * Get list of constants
     * @param   beanConstantList - input condition for constant list
     * @return  list of constants
     */
    public final ConstantListBean getConstantList(
            ConstantListBean beanConstantList, boolean isShowAll) {
        try {
            getEJB();
            int nPage = beanConstantList.getNumPage();
            ArrayList arrList = objConstant.queryConstant(
                    beanConstantList.getSortBy(),
                    n_Usage,
                    (nPage - 1) * NCMS.NUM_CONST_PER_PAGE + 1,
                    nPage * NCMS.NUM_CONST_PER_PAGE);
            int nSize = arrList.size() / 3;
            StringMatrix smList = new StringMatrix(nSize, 3);
            for (int nRow = 0; nRow < nSize; nRow++) {
                for (int i = 0; i < 3; i++) {
                    smList.setCell(nRow, i, CommonUtil.correctHTMLError(
                            arrList.get(3 * nRow + i).toString()));
                }
            }
            beanConstantList.setConstantList(smList);
            beanConstantList.setComboSortBy(getConstantTypeList(true, isShowAll));
            beanConstantList.setTotal(objConstant.getNumByType(
                    beanConstantList.getSortBy(), n_Usage, isShowAll));
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getConstantList(): " + ex.toString());
        }
        return beanConstantList;
    }

    /**
     * addConstant
     * Add new constants to database
     * @param   beanConstantAdd - input parameters
     * @return  adding was successful or not
     */
    public final boolean addConstant(ConstantAddBean beanConstantAdd) {
        boolean isSuccess = true;
        try {
            getEJB();
            int nRows = beanConstantAdd.getConstantList().getNumberOfRows();
            for (int i = 0; i < nRows; i++) {
                if (objConstant.insertConstant(
                        beanConstantAdd.getConstantList().getCell(i, 1),
                        beanConstantAdd.getConstantList().getCell(i, 0),
                        n_Usage) <= 0)
                {
                    isSuccess = false;
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in ConstantBO.addConstant(): "
                    + ex.toString());
        }
        return isSuccess;
    }

    /**
     * deleteConstant
     * Remove constants from database
     * @param   beanConstantAdd - input parameters
     * @return  deleting was successful or not
     */
    public final boolean deleteConstant(ConstantAddBean beanConstantAdd) {
        boolean isSuccess = true;
        try {
            getEJB();
            int nRows = beanConstantAdd.getConstantList().getNumberOfRows();
            for (int i = 0; i < nRows; i++) {
                if (objConstant.deleteConstant(
                        beanConstantAdd.getConstantList().getCell(i, 0)) <= 0) {
                    isSuccess = false;
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.deleteConstant(): " + ex.toString());
        }
        return isSuccess;
    }

    /**
     * updateConstant
     * Update constants
     * @param   beanConstantAdd - input parameters
     * @return  updating was successful or not
     */
    public final boolean updateConstant(ConstantAddBean beanConstantAdd) {
        boolean isSuccess = true;
        try {
            getEJB();
            int nRows = beanConstantAdd.getConstantList().getNumberOfRows();
            for (int i = 0; i < nRows; i++) {
                if (objConstant.updateConstant(
                        beanConstantAdd.getConstantList().getCell(i, 1),
                        beanConstantAdd.getConstantList().getCell(i, 0),
                        n_Usage) <= 0) {
                    isSuccess = false;
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.updateConstant(): " + ex.toString());
        }
        return isSuccess;
    }

    /**
     * getConstantByID
     * Get list of constants by their ident.numbers
     * @param   strConstantID - input parameter
     * @return  list of constants
     */
    public final StringMatrix getConstantByID(String strConstantID) {
        StringMatrix smList = null;
        try {
            getEJB();
            ArrayList arrList
                    = objConstant.queryConstantByID(strConstantID);
            int nSize = arrList.size() / 3;
            smList = new StringMatrix(nSize, 3);
            for (int nRow = 0; nRow < nSize; nRow++) {
                for (int i = 0; i < 3; i++) {
                    smList.setCell(nRow, i, CommonUtil.correctHTMLError(
                            arrList.get(3 * nRow + i).toString()));
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "ConstantBO.getConstantByID(): " + ex.toString());
        }
        return smList;
    }
    /**
     * Returns the usage.
     * @return int
     */
    public int getUsage() {
        return n_Usage;
    }

    /**
     * Sets the usage.
     * @param usage The usage to set
     */
    public void setUsage(int usage) {
        n_Usage = usage;
    }

    /**
     * Returns the excludeConstants.
     * @return String
     */
    public String getExcludeConstants() {
        return strExcludeConstants;
    }

    /**
     * Sets the excludeConstants.
     * @param excludeConstants The excludeConstants to set
     */
    public void setExcludeConstants(String excludeConstants) {
        strExcludeConstants = excludeConstants;
    }

}