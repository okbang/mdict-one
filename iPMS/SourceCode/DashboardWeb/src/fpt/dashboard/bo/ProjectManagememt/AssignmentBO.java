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

/**
 * @Title:        AssignmentBO.java
 * @Description:
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 12, 2002
 * @Modified date:
 */

import java.security.acl.Group;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

//import fpt.dashboard.ProjectManagementTran.ejb.Assign;
import fpt.dashboard.ProjectManagementTran.ejb.AssignEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.AssignEJBLocalHome;
//import fpt.dashboard.ProjectManagementTran.ejb.AssignHome;
//import fpt.dashboard.ProjectManagementTran.ejb.Assignment;
import fpt.dashboard.ProjectManagementTran.ejb.AssignmentEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.AssignmentEJBLocalHome;
//import fpt.dashboard.ProjectManagementTran.ejb.AssignmentHome;

import fpt.dashboard.ProjectManagementTran.ejb.AssignmentInfo;

//import fpt.dashboard.ProjectManagementTran.ejb.OtherAssignment;

import fpt.dashboard.ProjectManagementTran.ejb.OtherAssignmentEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.OtherAssignmentEJBLocalHome;

//import fpt.dashboard.ProjectManagementTran.ejb.OtherAssignmentHome;

import fpt.dashboard.bean.ProjectManagement.AssignmentAddBean;
import fpt.dashboard.bean.ProjectManagement.AssignmentListBean;
import fpt.dashboard.bean.ProjectManagement.AssignmentOtherBean;
import fpt.dashboard.bean.ResourceManagement.AssignmentDetailBean;
import fpt.dashboard.constant.DATA;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.util.StringUtil.StringMatrix;
import fpt.dashboard.util.StringUtil.StringVector;
import fpt.dashboard.util.DateUtil.DateUtil;

public class AssignmentBO {
//	private AssignHome homeAssign = null;
//	private Assign objAssign = null;
//	private AssignmentHome homeAssignment = null;
//	private Assignment objAssignment = null;
//
//	private OtherAssignmentHome homeOtherAssignment = null;
//	private OtherAssignment objOtherAssignment = null;

//  HaiMM ===============
	private AssignEJBLocalHome homeAssign = null;
	private AssignEJBLocal objAssign = null;
	
	private AssignmentEJBLocalHome homeAssignment = null;
	private AssignmentEJBLocal objAssignment = null;
	
	private OtherAssignmentEJBLocalHome homeOtherAssignment = null;
	private OtherAssignmentEJBLocal objOtherAssignment = null;
// =====================

	private static Logger logger =
		Logger.getLogger(AssignmentBO.class.getName());

	public AssignmentBO() {

	}

	//**************** HELPER Methods ****************************************
	//************************Assign EJB***********************
	//EJB Bean Specific methods ...
	private void getEJBHome() throws NamingException {
		try {
			if (homeAssign == null) {
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.ASSIGNMENT);
				homeAssign =(AssignEJBLocalHome)(ref); // HaiMM
			}
		} catch (NamingException ex) {
			logger.error(
				"NamingException occurs in AssignmentBO.getEJBHome(). "
					+ ex.getResolvedName());
			throw ex;
		}
	} //getEJBHome
	private AssignEJBLocal getEJBRemote() throws Exception { //HaiMM: from Assign --> AssignEJBLocal
		try {
			objAssign = (AssignEJBLocal) homeAssign.create(); // HaiMM
			return objAssign;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	} //getEJBRemote

	//AssignmentEJB
	private void getAssignmentEJB() throws Exception {
		try {
			if (homeAssignment == null) {
				Context ic = new InitialContext();
				java.lang.Object objref =
					ic.lookup(JNDINaming.ASSIGNMENT_DETAIL);
				homeAssignment = (AssignmentEJBLocalHome)(objref);
				objAssignment = homeAssignment.create();
			}
		} catch (Exception e) {
			throw new Exception(
				"Exception occurs in AssignmentBO.getAssignmentEJB(). "
					+ e.toString());
		}
	} //getAssignmentEJB

	//OtherAssignmentEJB
	private void getOtherAssignmentEJB() throws Exception {
		try {
			if (homeOtherAssignment == null) {
				Context ic = new InitialContext();
				java.lang.Object objref =
					ic.lookup(JNDINaming.OTHER_ASSIGNMENT);
				homeOtherAssignment =(OtherAssignmentEJBLocalHome)(objref);
				objOtherAssignment = homeOtherAssignment.create();
			}
		} catch (Exception e) {
			throw new Exception(
				"Exception occurs in AssignmentBO.getOtherAssignmentEJB(). "
					+ e.toString());
		}
	} //OtherAssignmentEJB

	/**
	 * Get all assigned users in project
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @param	nProjectID int: project ID
	 * @exception   Exception    If an exception occurred.
	 */
	public AssignmentListBean getAssignmentList(int nProjectID)
		throws Exception {
		AssignmentListBean beanAssignmentList = new AssignmentListBean();

		try {
			getEJBHome();
			getEJBRemote();

			StringMatrix smResult = new StringMatrix();
			Integer ProjectID = new Integer(nProjectID);

			objAssign.setProjectID(ProjectID);
			Collection cAllKey = objAssign.selectAllKey();
			Object[] oAllKey = cAllKey.toArray();
			Integer[] AssignID = new Integer[oAllKey.length];
			for (int i = 0; i < oAllKey.length; i++) {
				Integer iTemp = (Integer) oAllKey[i];
				AssignID[i] = iTemp;
			}
			for (int i = 0; i < AssignID.length; i++) {
				objAssign.loadRow(AssignID[i]);
				StringVector tmpVector = new StringVector(7);
				tmpVector.setCell(0, AssignID[i].toString());
				tmpVector.setCell(1, objAssign.getDeveloperName());
				tmpVector.setCell(2, objAssign.getBegin());
				tmpVector.setCell(3, objAssign.getEnd());
				tmpVector.setCell(4, objAssign.getType().toString());
				tmpVector.setCell(5, String.valueOf(objAssign.getUsage()));
				tmpVector.setCell(6, objAssign.getRes());
				smResult.addRow(tmpVector);
			}
			objAssign.getProjectInfor(ProjectID);

			beanAssignmentList.setProjectID(Integer.toString(nProjectID));
			beanAssignmentList.setProjectCode(objAssign.getProjectCode());
			beanAssignmentList.setProjectName(objAssign.getProjectName());
			beanAssignmentList.setProjectStart(objAssign.getProjectStart());
			beanAssignmentList.setProjectFinish(objAssign.getProjectFinish());
			beanAssignmentList.setStrLeader(objAssign.getProjectLeader());
			beanAssignmentList.setAssignList(smResult);
		} // end try
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.getAssignmentList(): "
					+ ex.toString());
			logger.error(ex);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		return beanAssignmentList;
	}

	/**
	 * Update a list of assigned users into database
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @param	beanAssignmentList AssignmentListBean: a list needs to be updated.
	 * @exception   Exception    If an exception occurred.
	 */
	public int updateAssignment(AssignmentListBean beanAssignmentList)
		throws Exception {
		int nResult = -1;
		try {
			getEJBHome();
			getEJBRemote();

			Integer ProjectID = new Integer(beanAssignmentList.getProjectID());
			String[] arrBegin = beanAssignmentList.getArrBegin();
			String[] arrEnd = beanAssignmentList.getArrEnd();
			String[] arrType = beanAssignmentList.getArrType();
			String[] arrUsage = beanAssignmentList.getArrUsage();
			String[] arrRes = beanAssignmentList.getArrRes();

			objAssign.setProjectID(ProjectID);
			Collection cAllKey = objAssign.selectAllKey();
			Object[] oAllKey = cAllKey.toArray();
			Integer[] AssignID = new Integer[oAllKey.length];
			for (int i = 0; i < oAllKey.length; i++) {
				Integer iTemp = (Integer) oAllKey[i];
				AssignID[i] = iTemp;
			}
			for (int i = 0; i < AssignID.length; i++) {
				objAssign.setBegin(arrBegin[i]);
				objAssign.setEnd(arrEnd[i]);
				objAssign.setType(new Integer(arrType[i]));
				objAssign.setUsage(Integer.parseInt(arrUsage[i]));
				objAssign.setRes(arrRes[i]);
				objAssign.storeRow(AssignID[i]);
			}
		} // end try
		catch (NumberFormatException ex) {
			logger.debug("strProjectID = " + beanAssignmentList.getProjectID());
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.updateAssignment(): "
					+ ex.toString());
			logger.error(ex);
		}
		return nResult;
	}

	/**
	 * Delete assigned user(s)
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @param	arrID String[]: a list of user IDs
	 * @exception   Exception    If an exception occurred.
	 */
	public int deleteAssignment(String[] arrID) throws Exception {
		int nResult = -1;
		try {
			getEJBHome();
			getEJBRemote();

			for (int i = 0; i < arrID.length; i++) {
				Integer tmp = new Integer(arrID[i]);
				objAssign.deleteRow(tmp);
			}
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.deleteAssignment(): "
					+ ex.toString());
			logger.error(ex);
		}

		return nResult;
	}

	/**
	 * Save new assigned user(s)
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @param	beanAssignmentAdd AssignmentAddBean
	 * @exception   Exception    If an exception occurred.
	 */
	public int saveAssignment(AssignmentAddBean beanAssignmentAdd)
		throws Exception {
		int nResult = -1;
		try {
			getEJBHome();
			getEJBRemote();

			Integer ProjectID = new Integer(beanAssignmentAdd.getProjectID());
			String status = beanAssignmentAdd.getSelectDevID();

			StringTokenizer stnrSelID = null;
			stnrSelID = new StringTokenizer(status, ",");
			while (stnrSelID.hasMoreTokens()) {
				String strCurID = stnrSelID.nextToken();
				String endDate;
				if (beanAssignmentAdd.getProjectEnd() != null) {
					endDate = beanAssignmentAdd.getProjectEnd();
				} else {
					endDate = beanAssignmentAdd.getProjectStart();
				}
				objAssign.insertRow(
					ProjectID,
					new Integer(strCurID),
					beanAssignmentAdd.getProjectStart(),
					endDate,
					new Integer("2"));
			} //end while
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.deleteAssignment(): "
					+ ex.toString());
			logger.error(ex);
		}

		return nResult;
	}

	/**
	 * Get assignment detail
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @param	nProjectID int: project ID
	 * @exception   Exception    If an exception occurred.
	 */
	public AssignmentDetailBean getAssignmentDetail(AssignmentDetailBean beanAssignmentDetail)
		throws Exception {
		try {
			getAssignmentEJB();

			int developerID =
				Integer.parseInt(beanAssignmentDetail.getDeveloperID());
			String from = beanAssignmentDetail.getFrom();
			String to = beanAssignmentDetail.getTo();

			Collection listResult =
				objAssignment.getAssignmentList(developerID, from, to);
			java.lang.Object[] arrResult = listResult.toArray();
			StringMatrix smResult = new StringMatrix();
			for (int nIndex = 0; nIndex < arrResult.length; nIndex++) {
				AssignmentInfo info = (AssignmentInfo) arrResult[nIndex];
				String tempName = info.getProjectName();
				if (!tempName.startsWith("Daily_")) {
					StringVector tmpVector = new StringVector(4);
					tmpVector.setCell(0, info.getProjectName());
					tmpVector.setCell(1, info.getBeginDate());
					tmpVector.setCell(2, info.getEndDate());
					tmpVector.setCell(3, String.valueOf(info.getUsage()));
					beanAssignmentDetail.setDeveloperName(
						info.getDeveloperName());
					smResult.addRow(tmpVector);
				}
			}
			beanAssignmentDetail.setSMAssign(smResult);
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.getAssignmentDetail(): "
					+ ex.toString());
			logger.error(ex);
		}
		return beanAssignmentDetail;
	}

	/**
	 * Get other assignment
	 * @author  Nguyen Thai Son.
	 * @version  November 12, 2002.
	 * @param	nProjectID int: project ID
	 * @exception   Exception    If an exception occurred.
	 */
	public AssignmentOtherBean getOtherAssignment(AssignmentOtherBean beanOtherAssignment)
		throws Exception {

		String strGroup = "All";
		String strFrom = "";
		String strTo = "";
		Date date = null;
		String year1 = "";
		String year2 = "";
		String month1 = "";
		String month2 = "";
		String day1 = "";
		String day2 = "";
		int ngay1 = 0;
		int ngay2 = 0;
		date = new Date();
		String duyear1 = new Integer(date.getYear() + 1900).toString();
		year1 = duyear1.substring(2, 4);

		month1 = new Integer(date.getMonth() + 1).toString();
		day1 = new Integer(date.getDate()).toString();
		if (month1.length() < 2) {
			month1 = "0" + month1;
		}
		if (day1.length() < 2) {
			day1 = "0" + day1;
		}

		Date tempd = new Date();
		Date date1 =
			new Date(tempd.getYear(), tempd.getMonth(), tempd.getDate() + 15);

		String duyear2 = new Integer(date1.getYear() + 1900).toString();
		year2 = duyear2.substring(2, 4);

		month2 = new Integer(date1.getMonth() + 1).toString();
		day2 = new Integer(date1.getDate()).toString();
		if (month2.length() < 2) {
			month2 = "0" + month2;
		}
		if (day2.length() < 2) {
			day2 = "0" + day2;
		}

		strFrom = day1 + "/" + month1 + "/" + year1;
		strTo = day2 + "/" + month2 + "/" + year2;
		if ((beanOtherAssignment.getSelectGroup() != null)
			&& (!(beanOtherAssignment.getSelectGroup().equals("")))) {
			strGroup = beanOtherAssignment.getSelectGroup();
		}
		if ((beanOtherAssignment.getFrom() != null)
			&& (!beanOtherAssignment.getFrom().equals(""))) {
			strFrom = beanOtherAssignment.getFrom();
		}
		if ((beanOtherAssignment.getTo() != null)
			&& (!beanOtherAssignment.getTo().equals(""))) {
			strTo = beanOtherAssignment.getTo();
		}

		try {
			getOtherAssignmentEJB();

			StringMatrix smResult = new StringMatrix();
			Collection cAllKey = objOtherAssignment.getGroup();
			Object[] oAllKey = cAllKey.toArray();
			String[] Group = new String[oAllKey.length];
			for (int i = 0; i < oAllKey.length; i++) {
				String iTemp = (String) oAllKey[i];
				Group[i] = iTemp;
			}

			logger.debug("Group.length = " + Group.length);

			beanOtherAssignment.setGroup(Group);

			Collection cl =
				objOtherAssignment.getListnew(strGroup, strFrom, strTo);
			Iterator IT = cl.iterator();
			while (IT.hasNext()) {
				StringVector tmpVector = new StringVector(7);
				Integer id_number = (Integer) IT.next();
				tmpVector.setCell(0, String.valueOf(id_number));
				String name = (String) IT.next();
				tmpVector.setCell(1, name);
				String from_date = (String) IT.next();
				tmpVector.setCell(2, from_date);
				String end_date = (String) IT.next();
				tmpVector.setCell(3, end_date);
				Integer type_number = (Integer) IT.next();
				int so = type_number.intValue();
				String chuoi;
				switch (so) {
					case 1 :
						chuoi = "Onsite";
						break;
					case 2 :
						chuoi = "Allocated";
						break;
					case 3 :
						chuoi = "Tentative";
						break;
					case 4 :
						chuoi = "Training";
						break;
					case 5 :
						chuoi = "Off";
						break;
					default :
						chuoi = "";
				}
				tmpVector.setCell(4, chuoi);
				tmpVector.setCell(5, (String) IT.next());
				String description = (String) IT.next();
				tmpVector.setCell(6, description);
				//logger.info(description);
				smResult.addRow(tmpVector);
			}

			logger.debug(
				"smResult.getNumberOfRows() = " + smResult.getNumberOfRows());

			beanOtherAssignment.setAssignList(smResult);
			beanOtherAssignment.setSelectGroup(strGroup);
			beanOtherAssignment.setFrom(strFrom);
			beanOtherAssignment.setTo(strTo);
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.getOtherAssignment(): "
					+ ex.toString());
			logger.error(ex);
		}

		return beanOtherAssignment;
	}

	/**
	 * Save other assignment
	 * @author  Nguyen Thai Son.
	 * @version  November 14, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public int saveOtherAssignment(AssignmentAddBean beanAssignmentAdd)
		throws Exception {
		int nResult = -1;

		try {
			getOtherAssignmentEJB();

			int nID = Integer.parseInt(beanAssignmentAdd.getSelectDevID());
			String strFrom = beanAssignmentAdd.getFrom();
			String strTo = beanAssignmentAdd.getTo();
			int nType = Integer.parseInt(beanAssignmentAdd.getType());
			String strUsage = beanAssignmentAdd.getUsage();
			String strDescription = beanAssignmentAdd.getDesc();

			objOtherAssignment.addAssignment(
				nID,
				strFrom,
				strTo,
				nType,
				strDescription,
				strUsage);
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.saveOtherAssignment(): "
					+ ex.toString());
			logger.error(ex);
		}

		return nResult;
	}

	/**
	 * Delete other assignment
	 * @author  Nguyen Thai Son.
	 * @version  November 14, 2002.
	 * @exception   Exception    If an exception occurred.
	 */
	public int deleteOtherAssignment(String[] arrID) throws Exception {
		int nResult = -1;

		try {
			getOtherAssignmentEJB();

			for (int i = 0; i < arrID.length; i++) {
				objOtherAssignment.delete(arrID[i]);
			}
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.deleteOtherAssignment(): "
					+ ex.toString());
			logger.error(ex);
		}

		return nResult;
	}
	//ThaiLH
	public int checkAssign(String strID, String strFrom, String strTo) {
		int nResult = 0;
		try {
			getOtherAssignmentEJB();
			Collection dateList = objOtherAssignment.getOffTime(strID);
			Iterator IT = dateList.iterator();
			while (IT.hasNext()) {
				String from = (String) IT.next();
				String to = (String) IT.next();
                
                /* Replaced by TrungTN, error when add same account in separated time,
                 * example assign ANA off from 01-Oct-06 to xx-Oct-06, then not
                 * allow assign ANA off from 01-Nov-06 to xx-Nov-06
                 * => incorected, should allow
                 * 
				if ((DateUtil.CompareDate(strFrom, from) < 1)
					&& (DateUtil.CompareDate(from, strTo) < 0)) {
					nResult = -1;
				}
				if ((DateUtil.CompareDate(strFrom, to) < 0)
					&& (DateUtil.CompareDate(to, strTo) < 1)) {
					nResult = -1;
				}
				if ((DateUtil.CompareDate(from, strFrom) < 1)
					&& (DateUtil.CompareDate(strTo, to) < 1)) {
					nResult = -1;
				}
				if ((DateUtil.CompareDate(strFrom, from) < 1)
					&& (DateUtil.CompareDate(to, strTo) < 1)) {
					nResult = -1;
				}*/

                /* If the assignment is separated from existed assignment */
                if ((DateUtil.CompareDate(strFrom, to) <= 0) &&
                        (DateUtil.CompareDate(strTo, from) >= 0)) {
                    nResult = -1;
                    break;
                }
			}
		} catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in AssignmentBO.saveOtherAssignment(): "
					+ ex.toString());
			logger.error(ex);
		}

		return nResult;
	}
	//EndOfThaiLH
}