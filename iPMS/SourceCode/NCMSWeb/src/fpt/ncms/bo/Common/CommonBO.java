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
 * @(#)CommonBO.java 14-Mar-03
 */

package fpt.ncms.bo.Common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import fpt.ncms.bean.UserInfoBean;
import fpt.ncms.constant.JNDI;
import fpt.ncms.constant.NCMS;
import fpt.ncms.ejb.Common;
import fpt.ncms.ejb.CommonEJBLocal;
import fpt.ncms.ejb.CommonEJBLocalHome;
import fpt.ncms.ejb.CommonHome;
import fpt.ncms.framework.core.SessionInfoBaseBean;
import fpt.ncms.model.NCModel;
import fpt.ncms.util.StringUtil.StringMatrix;
import fpt.ncms.util.StringUtil.*;

/**
 * Class CommonBO
 * Bean Object for combo user, project, group
 * @version 1.0 14-Mar-03
 * @author
 */
public final class CommonBO {
//    private CommonHome homeCommon = null;
//    private Common objCommon = null;

	private CommonEJBLocalHome homeCommon = null; // HaiMM
	private CommonEJBLocal objCommon = null;

    private static final Logger logger = Logger.getLogger(
            CommonBO.class.getName());

    private static class StringComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((String) o1).compareTo((String) o2);
        }
/*        
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
*/
    }

    /**
     * CommonBO
     * Class constructor
     */
    public CommonBO() {
        getEJBHome();
        getEJBRemote();
    }

    /**
     * getEJBHome
     * Get home for EJB
     * @throws  NamingException
     * @throws  Exception
     */
    private void getEJBHome() {
        try {
            if (homeCommon == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.COMMON);
                homeCommon = (CommonEJBLocalHome)(objref);
            }
        }
        catch (NamingException ex) {
            System.out.println("CommonBO.getEJBHome() error! -- "
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
     */
    private void getEJBRemote() {
        try {
            if (objCommon == null) {
                objCommon = homeCommon.create();
            }
        }
        catch (Exception ex) {
            System.out.println("CommonBO.getEJBRemote() error! -- "
                    + ex.getMessage());
        }
    }

    /**
     * checkLogin
     * Validate login by user/pass
     * @param   strAccount - user name.
     * @param   strPassword - user password
     * @param   strLocation - login mode
     * @return  bean object that contains information about user
     */
    //modified by MinhPT
    //22-Dec-03
    //for add Call Log to NCMS
    public final UserInfoBean checkLogin(String strAccount,
            String strPassword, String strLocation) {
        UserInfoBean beanUserInfo = new UserInfoBean();
        int nLocation;
        try {
            nLocation = Integer.parseInt(strLocation);
            beanUserInfo.sessionInfoObject = objCommon.getUserInfo(strAccount,
                    strPassword, nLocation);
            String strHost = objCommon.getParameter("MAIL.SMTP.HOST");
            beanUserInfo.setSMTPHost(strHost);
            
            String strTypeOfView = "";
            if (beanUserInfo.sessionInfoObject != null) {
                beanUserInfo.setUserId(
                        beanUserInfo.sessionInfoObject.getUserID());
                beanUserInfo.setRole(
                        beanUserInfo.sessionInfoObject.getRole());
                beanUserInfo.setLoginName(
                        beanUserInfo.sessionInfoObject.getLoginName());
                beanUserInfo.setFullName(
                        beanUserInfo.sessionInfoObject.getFullname());
                beanUserInfo.setGroupName(
                        beanUserInfo.sessionInfoObject.getGroupName());
                String strRole = beanUserInfo.getRole();
//                if ("1".equals(strRole.substring(4, 5))){
//                    strTypeOfView = NCMS.ADMIN_VIEW;
//                }
//                else
                if (strRole.substring(4, 5).equals("1")) {
                    strTypeOfView = NCMS.PQA_VIEW;
                }
                else if (strRole.substring(1, 2).equals("1")) {
                    strTypeOfView = NCMS.REVIEWER_VIEW;
                }
                else if (strRole.substring(0, 1).equals("1")) {
                    strTypeOfView = NCMS.ASSIGNEE_VIEW;
                }
                else if (strRole.substring(6, 7).equals("1")) {
                    strTypeOfView = NCMS.CREATOR_VIEW;
                }
                beanUserInfo.setTypeOfView(strTypeOfView);
            }
            else {
                beanUserInfo.setLoginName(strAccount);
                beanUserInfo.setMessage(NCMS.MSG_INVALID_USER);
            }
        }
        catch (NumberFormatException ex) {
            beanUserInfo.setMessage(NCMS.MSG_INVALID_LOCATION);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in CommonBO.checkLogin(): "
                    + ex.toString());
        }
        return beanUserInfo;
    }

    /**
     * getUserList
     * Get list of user
     * @param   strUserRole - user's role
     * @param   nType - flag indicates type of combo
     * @param   nCombo - flag indicates the combo is Creator or Assignee
     * @return  list of assignees
     */
    //modified by Minh PT
    //15-Dec-03
    //for add NONE and ALL value
    public final StringMatrix getUserList(UserInfoBean beanUserInfo, int nType,
                                          int nCombo) {
        StringVector strRow = new StringVector(2); 
        StringMatrix smList = new StringMatrix();
        boolean b_IsListing = false;
        String strMininalRole = NCMS.ROLE_CREATOR;
        try {
            if ( nType == NCMS.CBO_ALL_VALUE){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_STRING);
                smList.addRow(strRow);
                b_IsListing = true;
            }
            else if(nType == NCMS.CBO_NONE_VALUE){
                strRow.setCell(0, NCMS.CBO_ITEM_NONE_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_NONE_STRING);
                smList.addRow(strRow);
                b_IsListing = true;
            }
            else if(nType == NCMS.CBO_BOTH_ALL_AND_NONE_VALUE ){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_STRING);
                smList.addRow(strRow);

                strRow.setCell(0, NCMS.CBO_ITEM_NONE_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_NONE_STRING);
                smList.addRow(strRow);
                b_IsListing = true;
            }
            // Use in Add new, Update, and status can change to Opened
            else if (nType == NCMS.CBO_EMPTY_VALUE && beanUserInfo.isOpenEnabled()) {
                strRow.setCell(0, ""); //Empty
                strRow.setCell(1, "");
                smList.addRow(strRow);
            }
            
            if (nCombo == NCMS.COMBO_ASSIGNEE) {
                strMininalRole = NCMS.ROLE_ASSIGNEE;
            }
            
            // In listring mode OR user is PQA/Reviewer: list all
            if ((b_IsListing) ||
                (beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA)) ||
                (beanUserInfo.getRoleName().equals(NCMS.ROLE_REVIEWER)))
            {
                ArrayList userList = objCommon.getUserList(
                            strMininalRole, beanUserInfo.getLocation());
                int nSize = userList.size() / 2;

                for (int nRow = 0; nRow < nSize; nRow++) {
                    strRow.setCell(0,userList.get(nRow * 2).toString());
                    strRow.setCell(1,userList.get(nRow * 2).toString()
                            + " - " + userList.get(nRow * 2 + 1).toString());
                    smList.addRow(strRow);
                }
            }
            // Add, update
            else if ((nType != NCMS.CBO_EMPTY_VALUE) &&
                     (nType != NCMS.CBO_OTHER_VALUE))
            {
                strRow.setCell(0, beanUserInfo.getLoginName());
                strRow.setCell(1, beanUserInfo.getLoginName());
                smList.addRow(strRow);
            }
            // Update, this NC assigned to other person
            else if (beanUserInfo.getCurrentNC() != null) {
                if (beanUserInfo.getCurrentNC().getAssignee() != null) {
                    strRow.setCell(0, beanUserInfo.getCurrentNC().getAssignee());
                    strRow.setCell(1, beanUserInfo.getCurrentNC().getAssignee());
                    smList.addRow(strRow);
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "CommonBO.getUserList(): " + ex.toString());
        }
        return smList;
    }

    /**
     * getGroupList
     * Get list of groups
     * @param   nType - flag indicates type of combo
     * @return  list of groups
     */
    public final StringMatrix getGroupList(int nType) {
        StringVector strRow = new StringVector(2); 
        StringMatrix smList = new StringMatrix();
        try {
            ArrayList groupList = objCommon.getGroupList();
            int nSize = groupList.size() / 2;

            if ( nType == NCMS.CBO_ALL_VALUE){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_VALUE);
                smList.addRow(strRow);
            }
            else if(nType == NCMS.CBO_NONE_VALUE){
                strRow.setCell(0, NCMS.CBO_ITEM_NONE_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_NONE_VALUE);
                smList.addRow(strRow);
            }
            else if(nType == NCMS.CBO_BOTH_ALL_AND_NONE_VALUE ){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_VALUE);
                smList.addRow(strRow);
                strRow.setCell(0, NCMS.CBO_ITEM_NONE_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_NONE_VALUE);
                smList.addRow(strRow);
            }
            else if(nType == NCMS.CBO_BOTH_ALL_AND_GENERAL_VALUE ){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_VALUE);
                smList.addRow(strRow);
                strRow.setCell(0, NCMS.CBO_ITEM_GENERAL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_GENERAL_VALUE);
                smList.addRow(strRow);
            }
            for (int nRow = 0; nRow < nSize; nRow++) {
                strRow.setCell(0,groupList.get(2 * nRow).toString());
                strRow.setCell(1,groupList.get(2 * nRow + 1).toString());
                smList.addRow(strRow);
            }
//            strRow.setCell(0, NCMS.GENERAL_VALUE);
//            strRow.setCell(1, NCMS.GENERAL_VALUE);
//            smList.addRow(strRow);
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in CommonBO.getGroupList(): "
                    + ex.toString());
        }
        return smList;
    }

    /**
     * getProjectList
     * Get list of projects
     * @param   nType - flag indicates type of combo
     * @return  list of projects
     */
    public final StringMatrix getProjectList(int nTypeCombo, int nTypeProjectList, int nLocation) {
        StringVector strRow = new StringVector(3); 
        StringMatrix smList = new StringMatrix();
        try {
            ArrayList projectList = objCommon.getProjectList("",nTypeProjectList, nLocation);
            int nSize = projectList.size() / 3;
            if ( nTypeCombo == NCMS.CBO_ALL_VALUE){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_STRING);
				strRow.setCell(2, NCMS.CBO_ITEM_ALL_STRING);
                smList.addRow(strRow);
            }
            else if(nTypeCombo == NCMS.CBO_NONE_VALUE){
                strRow.setCell(0, NCMS.CBO_ITEM_NONE_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_NONE_STRING);
				strRow.setCell(2, NCMS.CBO_ITEM_NONE_STRING);
                smList.addRow(strRow);
            }
            else if(nTypeCombo == NCMS.CBO_BOTH_ALL_AND_NONE_VALUE ){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_STRING);
				strRow.setCell(2, NCMS.CBO_ITEM_ALL_STRING);
                smList.addRow(strRow);
                strRow.setCell(0, NCMS.CBO_ITEM_NONE_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_NONE_STRING);
				strRow.setCell(2, NCMS.CBO_ITEM_NONE_STRING);
                smList.addRow(strRow);
            }
            else if(nTypeCombo == NCMS.CBO_BOTH_ALL_AND_GENERAL_VALUE ){
                strRow.setCell(0, NCMS.CBO_ITEM_ALL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_ALL_VALUE);
				strRow.setCell(2, NCMS.CBO_ITEM_ALL_STRING);
                smList.addRow(strRow);
                strRow.setCell(0, NCMS.CBO_ITEM_GENERAL_STRING);
                strRow.setCell(1, NCMS.CBO_ITEM_GENERAL_VALUE);
				strRow.setCell(2, NCMS.CBO_ITEM_ALL_STRING);
                smList.addRow(strRow);
            }
            
            for (int nRow = 0; nRow < nSize; nRow++) {
                strRow.setCell(0,projectList.get(3 * nRow).toString());
                strRow.setCell(1,projectList.get(3 * nRow + 1).toString());
				strRow.setCell(2,projectList.get(3 * nRow + 2).toString());
                smList.addRow(strRow);
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurs in "
                    + "CommonBO.getProjectList(): " + ex.toString());
        }
        return smList;
    }
	/**
	 * Method getStatusString
	 * Get project status title by project status value
	 * @param nStatus
	 * @return strStatus
	 */
	private String getStatusString(int nStatus) {
		String strStatus = null;
		switch (nStatus) {
			case NCMS.PROJECT_STATUS_ONGOING:
				strStatus = NCMS.PROJECT_STATUS_ONGOING_STR;
				break;
			case NCMS.PROJECT_STATUS_CLOSED:
				strStatus = NCMS.PROJECT_STATUS_CLOSED_STR;
				break;
			case NCMS.PROJECT_STATUS_CANCELLED:
				strStatus = NCMS.PROJECT_STATUS_CANCELLED_STR;
				break;
			case NCMS.PROJECT_STATUS_TENTATIVE:
				strStatus = NCMS.PROJECT_STATUS_TENTATIVE_STR;
				break;
			case NCMS.PROJECT_STATUS_ALL:
			strStatus = NCMS.PROJECT_STATUS_ALL_STR;
			break;	
			default:
				strStatus = "";
		}
		return strStatus;
	}   
	/**
	 * getProjectStatusList
	 * Get list of projects Status
	 * @param   nLocation
	 * @return  list of projects Status
	 */
	public final StringMatrix getProjectStatusList() {
		StringVector strRow = new StringVector(2); 
		StringMatrix smList = new StringMatrix();
		try {
			ArrayList projectStatusList = objCommon.getProjectStatusList();
			int nSize = projectStatusList.size();
			for (int i = 0; i < nSize; i++) {
				strRow.setCell(0,projectStatusList.get(i).toString());
				strRow.setCell(1,getStatusString(Integer.parseInt(projectStatusList.get(i).toString())));
				smList.addRow(strRow);
			}
		}
		catch (Exception ex) {
			System.out.println("Exception occurs in "
					+ "CommonBO.getProjectStatusList(): " + ex.toString());
		}
		return smList;
	}
    /**
     * Append new string to a tree set
     * @param list
     * @param str
     */
    private void addString(String str, TreeSet tree)
    {
        if ((tree != null) && (str != null)) {
            tree.add(str);
        }
    }
    /**
     * Append new string list to a tree set
     * @param list
     * @param str
     */
    private void addStringList(Collection list, TreeSet tree)
    {
        if ((list != null) && (tree != null)) {
            Iterator itr = list.iterator();
            while (itr.hasNext()) {
                String str = (String) itr.next();
                if (str != null) {
                    tree.add(str);
                }
            }
        }
    }
    /**
     * Set recipient addresses from list of mail addresses
     * @param beanUserInfo User information
     * @param message Mail message to be sent
     * @param mailList Recipitents list
     */
    private void setRecipitents(UserInfoBean beanUserInfo, 
                                javax.mail.internet.MimeMessage message,
                                TreeSet mailList)
    {
        try {
            // Remove current user from recipient addresses list
            mailList.remove(beanUserInfo.sessionInfoObject.getEmail());
            Iterator itr = mailList.iterator();
            while (itr.hasNext()) {
                String strAddress = (String) itr.next();
                if ((strAddress != null) && (!"".equals(strAddress.trim()))) {
                    message.addRecipient(javax.mail.Message.RecipientType.TO, 
                            new javax.mail.internet.InternetAddress(strAddress));
                }
            }
        }
        catch (Exception e) {
        }
    }
    /**
     * Notify when changes were applied to Calls
     * @param request
     */
    public void notifyCallChanged(UserInfoBean beanUserInfo,
                                  String strAction,
                                  NCModel callOld, NCModel callNew)
    {
        StringComparator cmp = new StringComparator();
        TreeSet mailList = new TreeSet(cmp);
        try {
            ArrayList listCallOld = objCommon.getMailList(callOld.getTypeOfCause());
            ArrayList listCallNew = objCommon.getMailList(callNew.getTypeOfCause());
            // Send mail to new requested group and previous requested group
            addStringList(listCallOld, mailList);
            addStringList(listCallNew, mailList);
            
            // Get old assignee information
            SessionInfoBaseBean assigneeInfo = objCommon.getUserInfo(callOld.getAssignee());
            SessionInfoBaseBean creatorInfo = objCommon.getUserInfo(callOld.getCreator());
            String strCreatorEmail = null;
            String strOldAssigneeEmail = null;
            String strNewAssigneeEmail = null;
            if (creatorInfo != null) {
                strCreatorEmail = creatorInfo.getEmail();
            }
            if (assigneeInfo != null) {
                strOldAssigneeEmail = assigneeInfo.getEmail();
            }
            // Get new assignee information
            assigneeInfo = objCommon.getUserInfo(callNew.getAssignee());
            if (assigneeInfo != null) {
                strNewAssigneeEmail = assigneeInfo.getEmail();
            }
            
            // Get properties
            Properties props = System.getProperties();
            // Setup mail server
            props.put("mail.smtp.host", beanUserInfo.getSMTPHost());
            // Get a mail session
            javax.mail.Session mailSession =
                    javax.mail.Session.getDefaultInstance(props, null);
            // Create mail message
            javax.mail.internet.MimeMessage message =
                    new javax.mail.internet.MimeMessage(mailSession);
            message.setFrom(new javax.mail.internet.InternetAddress(
                                    beanUserInfo.sessionInfoObject.getEmail()));
            
            String strMessage = "";
            // Save a new call
            if (NCMS.CALL_LOG_SAVE_NEW.equals(strAction)) {
                message.setSubject("New call logged");
                strMessage = "Message from Call Log System\n" +
                        "A new call was logged:\n" + callNew.getDescription();
                // Include implementer to recipient addresses
                if (callNew.getStatus() == NCMS.NC_STATUS_ASSIGNED) {
                    addString(strNewAssigneeEmail, mailList);
                }
            }
            // Update a call
            else if (NCMS.CALL_LOG_SAVE_UPDATE.equals(strAction)) {
                message.setSubject("Call information changed");
                strMessage = "Message from Call Log System\n" +
                             "Call #" + callOld.getNCID() +
                             " was modified by ";
                strMessage += beanUserInfo.getFullName() + ":\n";
                
                // Status changed
                if (callOld.getStatus() != callNew.getStatus()) {
                    String strOldStatus = "Unknown", strNewStatus = "Unknown";
                    // Temporary remove Requested group from recipients list
                    mailList.clear();
                    
                    // Re-Open => alert to Creator + previous implementer
                    if (callNew.getStatus() == NCMS.NC_STATUS_OPENED) {
                        strNewStatus = "Opened";
                        addString(strCreatorEmail, mailList);
                        addString(strOldAssigneeEmail, mailList);
                    }
                    // Assigned => alert to implementer
                    else if (callNew.getStatus() == NCMS.NC_STATUS_ASSIGNED) {
                        strNewStatus = "Assigned";
                        addString(strNewAssigneeEmail, mailList);
                    }
                    // Pending => alert to requested group
                    else if (callNew.getStatus() == NCMS.NC_STATUS_PENDING) {
                        strNewStatus = "Pending";
                        addStringList(listCallOld, mailList);
                        addStringList(listCallNew, mailList);
                    }
                    // Close => alert to creator + previous implementer
                    else if (callNew.getStatus() == NCMS.NC_STATUS_CLOSED) {
                        strNewStatus = "Closed";
                        addString(strCreatorEmail, mailList);
                        addString(strOldAssigneeEmail, mailList);
                    }
                    // Cancelled => alert to creator
                    else if (callNew.getStatus() == NCMS.NC_STATUS_CANCELLED) {
                        strNewStatus = "Cancelled";
                        addString(strCreatorEmail, mailList);
                        // previous implementor
                        //addString(strOldAssigneeEmail, mailList);
                    }
                    
                    if (callOld.getStatus() == NCMS.NC_STATUS_OPENED) {
                        strOldStatus = "Opened";
                    }
                    else if (callOld.getStatus() == NCMS.NC_STATUS_ASSIGNED) {
                        strOldStatus = "Assigned";
                    }
                    else if (callOld.getStatus() == NCMS.NC_STATUS_PENDING) {
                        strOldStatus = "Pending";
                    }
                    else if (callOld.getStatus() == NCMS.NC_STATUS_CLOSED) {
                        strOldStatus = "Closed";
                    }
                    else if (callOld.getStatus() == NCMS.NC_STATUS_CANCELLED) {
                        strOldStatus = "Cancelled";
                    }
                    strMessage += " - Status changed from " + strOldStatus;
                    strMessage += " to " + strNewStatus + "\n";
                }
                // Assigned to other implementer
                else if (!callNew.getAssignee().equals(callOld.getAssignee())) {
                    addString(strOldAssigneeEmail, mailList);
                    addString(strNewAssigneeEmail, mailList);
                }
                
            }
            // Delete a call
            else if (NCMS.CALL_LOG_DELETE.equals(strAction)) {
                message.setSubject("Call removed");
                strMessage = "Call #" + callOld.getNCID() + " was removed by " +
                        beanUserInfo.getFullName() + ":\n" +
                        callOld.getDescription();
            }
            
            // ----------------Test result
//            mailList.remove(beanUserInfo.sessionInfoObject.getEmail());
//            Iterator itr = mailList.iterator();
//            strMessage += "\n\nRecipient addresses:";
//            while (itr.hasNext()) {
//                String strAddress = (String) itr.next();
//                strMessage += "\n" + strAddress;
//            }
//            
//            // Temporary clear list for test, do not send mail alert to truly addresses now
//            mailList.clear();
//            addStringList(listCallOld, mailList);
//            addStringList(listCallNew, mailList);
            // --------    
            
            message.setText(strMessage);
            if (mailList.size() > 0) {
                this.setRecipitents(beanUserInfo, message, mailList);
                javax.mail.Transport.send(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}