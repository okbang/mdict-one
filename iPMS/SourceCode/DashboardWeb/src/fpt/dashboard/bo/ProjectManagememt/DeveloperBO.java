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
 
 package fpt.dashboard.bo.ProjectManagememt;

import java.security.acl.Group;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

import fpt.dashboard.DeveloperManagementTran.ejb.Developer;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocal;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocalHome;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperHome;
import fpt.dashboard.InfoClass.DeveloperInfo;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.bean.ProjectManagement.AssignmentAddBean;
import fpt.dashboard.bean.StaffManagement.StaffAddBean;
import fpt.dashboard.bean.StaffManagement.StaffListBean;
import fpt.dashboard.bean.StaffManagement.StaffUpdateBean;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.util.CommonUtil.CommonUtil;
import fpt.dashboard.util.StringUtil.StringMatrix;
import fpt.dashboard.util.StringUtil.StringVector;

public class DeveloperBO {
//	private DeveloperHome homeDeveloper = null;
//	private Developer objDeveloper = null;

//  HaiMM ==========
	private DeveloperEJBLocalHome homeDeveloper = null;
	private DeveloperEJBLocal objDeveloper = null;
//==================
	private static Logger logger =
		Logger.getLogger(DeveloperBO.class.getName());

	public DeveloperBO() {

	}

	//**************** HELPER Methods ****************************************
	//************************Developer EJB***********************
	//EJB Bean Specific methods ...
	private void getEJBHome() throws NamingException {
		try {
			if (homeDeveloper == null) {
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.DEVELOPER);
				homeDeveloper =(DeveloperEJBLocalHome)(ref); // HaiMM
			}
		} catch (NamingException ex) {
			logger.error(
				"NamingException occurs in DeveloperBO.getEJBHome(). "
					+ ex.getResolvedName());
			throw ex;
		}
	} //getEJBHome
	private DeveloperEJBLocal getEJBRemote() throws Exception { // HaiMM
		try {
			objDeveloper = (DeveloperEJBLocal) homeDeveloper.create(); // HaiMM
			return objDeveloper;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	} //getEJBRemote

	/**
	 * Get all assigned users in project
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public AssignmentAddBean getAssignmentList() throws Exception {
		AssignmentAddBean beanAssignmentAdd = new AssignmentAddBean();

		try {
			getEJBHome();
			getEJBRemote();
            
            // Get groups list
            Collection cGroup = objDeveloper.selectGroup();
            StringMatrix smGroup = new StringMatrix(cGroup.size() + 1, 1);
            smGroup.setCell(0, 0, fpt.dashboard.constant.DATA.GROUP_ALL);
            Iterator itr = cGroup.iterator();
            for (int i = 1; itr.hasNext(); i++) {
                smGroup.setCell(i, 0, (String) itr.next());
            }
            beanAssignmentAdd.setGroupList(smGroup);
            // End: Get groups list
            
			Collection cAllKey = objDeveloper.selectAllKey(Constants.ALL, "-4", "account", null);
			Integer[] DeveloperID = null;

			if (cAllKey == null || cAllKey.isEmpty()) {
				logger.debug("No record found.");
			}
			Object[] oAllKey = cAllKey.toArray();
			DeveloperID = new Integer[oAllKey.length];
			for (int i = 0; i < oAllKey.length; i++) {
				Integer iTemp = (Integer) oAllKey[i];
				DeveloperID[i] = iTemp;
			}
			StringMatrix smResult = new StringMatrix();
			for (int i = 0; i < DeveloperID.length; i++) {
				objDeveloper.loadRow(DeveloperID[i]);
				StringVector svDev = new StringVector(3);
				svDev.setCell(0, DeveloperID[i].toString());
                svDev.setCell(
                    1,
                    CommonUtil.correctHTMLError(objDeveloper.getAcc()) + " - " +
                    CommonUtil.correctHTMLError(objDeveloper.getName()));
//                svDev.setCell(
//                    2,
//                    CommonUtil.correctHTMLError(objDeveloper.getName()));
                svDev.setCell(
                    2,
                    CommonUtil.correctHTMLError(objDeveloper.getGroup()));
				smResult.addRow(svDev);
			}
			beanAssignmentAdd.setDevList(smResult);
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in DeveloperBO.getAssignmentList(): "
					+ ex.toString());
			logger.error(ex);
		}

		////////////////////////////////////////////////////////////
		return beanAssignmentAdd;
	}

	/**
	 * Get a list of staff
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @param   strGroup    String: user group
	 * @param   strSelectedStatus   String: user status
	 * @exception   Exception    If an exception occurred.
	 */
	public StaffListBean getStaffList(StaffListBean beanStaffList) throws Exception {
		//Status List - hard code.
		StringMatrix statusList = new StringMatrix(6, 2);
        // Display all users
        statusList.setCell(0, 0, Constants.VALUE_STATUS_ALL);
        statusList.setCell(0, 1, Constants.STATUS_ALL);
        // Display staffs & collaborators
        statusList.setCell(1, 0, Constants.VALUE_STATUS_WORKING);
        statusList.setCell(1, 1, Constants.STATUS_WORKING);
		//value for combo box
		statusList.setCell(2, 0, Constants.VALUE_STAFF); //value for combo box
		statusList.setCell(2, 1, Constants.STAFF);
		statusList.setCell(3, 0, Constants.VALUE_COLLABORATOR);
		statusList.setCell(3, 1, Constants.COLLABORATOR);
		statusList.setCell(4, 0, Constants.VALUE_EXTERNAL);
		statusList.setCell(4, 1, Constants.EXTERNAL_VIEWER);
		statusList.setCell(5, 0, Constants.VALUE_OUTPLACED);
		statusList.setCell(5, 1, Constants.OUTPLACED);
		beanStaffList.setStatusList(statusList);
        
        String strGroup = beanStaffList.getSelectedGroup();
        String strStatus = beanStaffList.getStatus();
        String strSortBy = beanStaffList.getSortBy();
        String strDirection = beanStaffList.getDirection();
        String strAccount = CommonUtil.stringConvert(beanStaffList.getAccount());   // Replace the ' character
        String strName = CommonUtil.stringConvert(beanStaffList.getName()); // Replace the ' character
        int nPage = beanStaffList.getPageNumber();
        
		try {
			if ((strGroup == null) || ("".equals(strGroup))) {
				strGroup = Constants.GROUP_ALL;
			}
			beanStaffList.setSelectedGroup(strGroup);

            //set default as STAFF
			if ((strStatus == null) || ("".equals(strStatus))) {
                strStatus = Constants.VALUE_STAFF;
			}
			beanStaffList.setStatus(strStatus);

			getEJBHome();
			getEJBRemote();

			////////////////////////////////////////////////////////////////////////
			Collection cGroup = objDeveloper.selectGroup();

			Object[] oGroup = cGroup.toArray();
			String[] Group = new String[oGroup.length + 1];
			Group[0] = Constants.GROUP_ALL;
			for (int i = 0; i < oGroup.length; i++) {
				String iTemp = (String) oGroup[i];
				Group[i + 1] = iTemp;
			}
			beanStaffList.setArrGroup(Group);
			
            StringMatrix smResult = new StringMatrix();
            objDeveloper.setAcc(strAccount);
            objDeveloper.setName(strName);
            objDeveloper.setGroup(strGroup);
            objDeveloper.setStatus(strStatus);
            beanStaffList.setTotalRecords(objDeveloper.getDeveloperNumber());   // Get number of developers
            ArrayList arrDeveloper = objDeveloper.getDeveloperList(strSortBy, strDirection, nPage);
            Iterator itr = arrDeveloper.iterator();
            DeveloperInfo dev;
            while (itr.hasNext()) {
                dev = (DeveloperInfo) itr.next();
                StringVector tmpVector = new StringVector(7);
                tmpVector.setCell(0, Integer.toString(dev.getDeveloperID()));
                tmpVector.setCell(
                    1,
                    CommonUtil.correctHTMLError(dev.getName()));
                tmpVector.setCell(
                    2,
                    CommonUtil.correctHTMLError(dev.getDesignation()));
                tmpVector.setCell(3, dev.getGroupName());
                //tmpVector.setCell(4, dev.getRoleName());  // Temporary not use this value in Listing
                tmpVector.setCell(5, dev.getStatusName());
                tmpVector.setCell(6, dev.getAccount());
                smResult.addRow(tmpVector);
            }
//            Collection cAllKey =
//				objDeveloper.selectAllKey(strGroup, strStatus, strSortBy, strDirection);
//
//			Integer[] DeveloperID = null;
//			Object[] oAllKey = cAllKey.toArray();
//			DeveloperID = new Integer[oAllKey.length];
//			for (int i = 0; i < oAllKey.length; i++) {
//				Integer iTemp = (Integer) oAllKey[i];
//				DeveloperID[i] = iTemp;
//			}
//			StringMatrix smResult = new StringMatrix();
//			for (int i = 0; i < DeveloperID.length; i++) {
//				objDeveloper.loadRow(DeveloperID[i]);
//
//				StringVector tmpVector = new StringVector(7);
//				tmpVector.setCell(0, DeveloperID[i].toString());
//				tmpVector.setCell(
//					1,
//					CommonUtil.correctHTMLError(objDeveloper.getName()));
//				tmpVector.setCell(
//					2,
//					CommonUtil.correctHTMLError(objDeveloper.getDesign()));
//				tmpVector.setCell(3, objDeveloper.getGroup());
//
//				String strRole = "0000000000";
//				String role = objDeveloper.getRole();
//				if ("1000000000".equals(role))
//					strRole = Constants.DEVELOPER;
//				else if ("1100000000".equals(role))
//					strRole = Constants.PROJECT_LEADER;
//				else if ("1110000000".equals(role))
//					strRole = Constants.GROUP_LEADER;
//				else if ("1111000000".equals(role))
//					strRole = Constants.FIST;
//				else if ("1000100000".equals(role))
//					strRole = Constants.PQA;
//				else if ("1111110000".equals(role))
//					strRole = Constants.MANAGER;
//				else if ("0000001000".equals(role))
//					strRole = Constants.EXTERNAL_PL;
//				//external user of project level
//				else if ("0000001100".equals(role))
//					strRole = Constants.EXTERNAL_GL;
//				//external user of group level
//				else
//					strRole = Constants.UNKNOWN; //role.equals("0000000000")
//				tmpVector.setCell(4, strRole);
//
//				int nStatus =
//					Integer.parseInt(
//						(objDeveloper.getStatus() == null
//							|| "".equals(objDeveloper.getStatus()))
//							? "0"
//							: objDeveloper.getStatus());
//				String strStatusTitle = "";
//				switch (nStatus) {
//					case 0 :
//                        strStatusTitle = "";
//						break;
//					case 1 :
//                        strStatusTitle = Constants.STAFF;
//						break;
//					case 2 :
//                        strStatusTitle = Constants.COLLABORATOR;
//						break;
//					case 3 :
//                        strStatusTitle = Constants.EXTERNAL_VIEWER;
//						break;
//					case 4 :
//                        strStatusTitle = Constants.OUTPLACED;
//						break;
//				}
//
//				tmpVector.setCell(5, strStatusTitle); //Thaison - 04 Sep 2002
//                tmpVector.setCell(6, objDeveloper.getAcc());
//
//				smResult.addRow(tmpVector);
//			}

			beanStaffList.setDeveloperList(smResult);
		} //end try
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in DeveloperBO.getStaffList(): "
					+ ex.toString());
			ex.printStackTrace();
			logger.error(ex);
		}

		///////////////////////////////////////////////////////////////////
		return beanStaffList;
	}

	/**
	 * Preparing data to add new staff
	 * @author  Nguyen Thai Son.
	 * @version  November 13, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public StaffAddBean createStaffAddForm() throws Exception {
		StaffAddBean beanStaffAdd = new StaffAddBean();

		//Status List - hard code.
		StringMatrix statusList = new StringMatrix(3, 2);
		statusList.setCell(0, 0, Constants.VALUE_STAFF); //value for combo box
		statusList.setCell(0, 1, Constants.STAFF);
		statusList.setCell(1, 0, Constants.VALUE_COLLABORATOR);
		statusList.setCell(1, 1, Constants.COLLABORATOR);
		statusList.setCell(2, 0, Constants.VALUE_EXTERNAL);
		statusList.setCell(2, 1, Constants.EXTERNAL_VIEWER);
		// Does not allow to create a new quit user
        //statusList.setCell(3, 0, Constants.VALUE_OUTPLACED);
		//statusList.setCell(3, 1, Constants.OUTPLACED);
		beanStaffAdd.setStatusList(statusList);

		try {
			getEJBHome();
			getEJBRemote();

			Collection cGroup = objDeveloper.selectGroup();
			Object[] oGroup = cGroup.toArray();
			String[] Group = new String[oGroup.length];
			for (int i = 0; i < oGroup.length; i++) {
				String iTemp = (String) oGroup[i];
				Group[i] = iTemp;
			}
			beanStaffAdd.setArrGroup(Group);
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in DeveloperBO.createStaffAddForm(): "
					+ ex.toString());
			logger.error(ex);
		}

		//////////////////////////////////////////////////////////////////////////////
		return beanStaffAdd;
	}

	/**
	 * Save a new staff into DB
	 * @author  Nguyen Thai Son.
	 * @version  November 13, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public int saveStaff(StaffAddBean beanStaffAdd) throws Exception {
		int nResult = 0;
		try {
			getEJBHome();
			getEJBRemote();
			ArrayList arrDevList = objDeveloper.getAccountList();
			if (arrDevList.indexOf(beanStaffAdd.getAccount().toUpperCase()) != -1) {
				return -1;
			}
			String strStatus =
				(beanStaffAdd.getStatus() != null || !"".equals(beanStaffAdd.getStatus()))
					? beanStaffAdd.getStatus()
					: "0";
			nResult =
				objDeveloper.insertRow(
					beanStaffAdd.getName(),
					beanStaffAdd.getDesignation(),
					beanStaffAdd.getGroup(),
					beanStaffAdd.getRole(),
					beanStaffAdd.getPassword(),
					beanStaffAdd.getAccount().toUpperCase(),
					strStatus,
                    beanStaffAdd.getEmail(),
                    beanStaffAdd.getStartDate());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in DeveloperBO.saveStaff(): "
					+ ex.toString());
			logger.error(ex);
		}
		return nResult;
	}

	/**
	 * Delete staff
	 * @author  Nguyen Thai Son.
	 * @version  November 13, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public int deleteStaff(String[] arrID) throws Exception {
		int nResult = -1;
		try {
			getEJBHome();
			getEJBRemote();

			for (int i = 0; i < arrID.length; i++) {
				Integer tmp = new Integer(arrID[i]);
				objDeveloper.deleteRow(tmp);
			}
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in DeveloperBO.deleteStaff(): "
					+ ex.toString());
			logger.error(ex);
		}

		return nResult;
	}

	/**
	 * Get staff information to update
	 * @author  Nguyen Thai Son.
	 * @version  November 13, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public StaffUpdateBean getStaffInfo(String strID) throws Exception {
		StaffUpdateBean beanStaffUpdate = new StaffUpdateBean();

		try {
			getEJBHome();
			getEJBRemote();

			Collection cGroup = objDeveloper.selectGroup();
			Object[] oGroup = cGroup.toArray();
			String[] Group = new String[oGroup.length];
			for (int i = 0; i < oGroup.length; i++) {
				String iTemp = (String) oGroup[i];
				Group[i] = iTemp;
			}
			beanStaffUpdate.setArrGroup(Group);
			Integer nDevID = new Integer(strID);

			objDeveloper.loadRow(nDevID);

			beanStaffUpdate.setDevID(strID);
			beanStaffUpdate.setName(objDeveloper.getName());
			beanStaffUpdate.setDesignation(objDeveloper.getDesign());
			beanStaffUpdate.setGroup(objDeveloper.getGroup());

			String strRole = "0000000000";
			String role = objDeveloper.getRole();

			if (role.equals("1000000000"))
				strRole = Constants.DEVELOPER;
			else if (role.equals("1100000000"))
				strRole = Constants.PROJECT_LEADER;
			else if (role.equals("1110000000"))
				strRole = Constants.GROUP_LEADER;
			else if (role.equals("1111000000"))
				strRole = Constants.FIST;
			else if (role.equals("1000100000"))
				strRole = Constants.PQA;
			else if (role.equals("1111110000"))
				strRole = Constants.MANAGER;
			else if (role.equals("0000001000"))
                // external user of project level
				strRole = Constants.EXTERNAL_PL;
			else if (role.equals("0000001100"))
                // external user of group level
                strRole = Constants.EXTERNAL_GL;
            else if (role.equals("0000000010"))
                // communicator
                strRole = Constants.COMMUNICATOR;
			else
				strRole = Constants.UNKNOWN; //role.equals("0000000000")

			beanStaffUpdate.setRole(strRole);
			beanStaffUpdate.setPassword(objDeveloper.getPass());
			beanStaffUpdate.setAccount(objDeveloper.getAcc());
            beanStaffUpdate.setStatus(objDeveloper.getStatus());
            beanStaffUpdate.setEmail(objDeveloper.getEmail());
            beanStaffUpdate.setStartDate(objDeveloper.getStartDate());
            beanStaffUpdate.setQuitDate(objDeveloper.getQuitDate());
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in DeveloperBO.createStaffAddForm(): "
					+ ex.toString());
			logger.error(ex);
		}

		//Status List - hard code.
		StringMatrix statusList = new StringMatrix(4, 2);
		statusList.setCell(0, 0, Constants.VALUE_STAFF); //value for combo box
		statusList.setCell(0, 1, Constants.STAFF);
		statusList.setCell(1, 0, Constants.VALUE_COLLABORATOR);
		//value for combo box
		statusList.setCell(1, 1, Constants.COLLABORATOR);
		statusList.setCell(2, 0, Constants.VALUE_EXTERNAL);
		//value for combo box
		statusList.setCell(2, 1, Constants.EXTERNAL_VIEWER);
		statusList.setCell(3, 0, Constants.VALUE_OUTPLACED);
		//value for combo box
		statusList.setCell(3, 1, Constants.OUTPLACED);
		beanStaffUpdate.setStatusList(statusList);

		//////////////////////////////////////////////////////////////////////////////
		return beanStaffUpdate;
	}

	/**
	 * Update the current staff into DB
	 * @author  Nguyen Thai Son.
	 * @version  November 13, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public int updateStaff(StaffUpdateBean beanStaffUpdate) throws Exception {
		int nResult = -1;
		try {
			getEJBHome();
			getEJBRemote();

			String strStatus =
				"".equals(beanStaffUpdate.getStatus())
					? "0"
					: beanStaffUpdate.getStatus();

			objDeveloper.setName(beanStaffUpdate.getName());
			objDeveloper.setDesign(beanStaffUpdate.getDesignation());
			objDeveloper.setRole(beanStaffUpdate.getRole());
			objDeveloper.setAcc(beanStaffUpdate.getAccount().toUpperCase());
			//objDeveloper.setPass(beanStaffUpdate.getPassword());
			objDeveloper.setGroup(beanStaffUpdate.getGroup());
			objDeveloper.setStatus(strStatus);
            objDeveloper.setEmail(beanStaffUpdate.getEmail());
            objDeveloper.setStartDate(beanStaffUpdate.getStartDate());
            objDeveloper.setQuitDate(beanStaffUpdate.getQuitDate());
			Integer nKey = new Integer(beanStaffUpdate.getDevID());
			objDeveloper.storeRow(nKey);
            // All updates successful
            nResult = 1;
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in DeveloperBO.saveStaff(): "
					+ ex.toString());
			logger.error(ex);
		}

		return nResult;
	}
}