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
 
 package fpt.timesheet.bo.Exemption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import fpt.timesheet.ApproveTran.ejb.common.CommonListBean;
//import fpt.timesheet.Exemption.ejb.Exemption;
//import fpt.timesheet.Exemption.ejb.ExemptionHome;
import fpt.timesheet.Exemption.ejb.ExemptionBean;
import fpt.timesheet.Exemption.ejb.ExemptionEJBLocal;
import fpt.timesheet.Exemption.ejb.ExemptionEJBLocalHome;
import fpt.timesheet.bean.ExemptionInfoBean;
import fpt.timesheet.bean.ExemptionListBean;
import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;

import fpt.timesheet.constant.TIMESHEET;

public class ExemptionBO {

	private static Logger logger = Logger.getLogger(ExemptionBO.class.getName());
	private String strClassName = "ExemptionBO";

	private ExemptionEJBLocalHome homeExemption = null; 
	private ExemptionEJBLocal objExemption = null;
	/**
	 * @see java.lang.Object#Object()
	 */
	public ExemptionBO() {
	}

	/**
	 * Method getInputEJB.
	 * @throws NamingException
	 */
	private void getExemptionEJB() throws NamingException {
		try {
			if (homeExemption == null) {
				Context ic = new InitialContext();
				java.lang.Object objref = ic.lookup(JNDI.EXEMPTION);

//				homeExemption = (ExemptionHome) javax.rmi.PortableRemoteObject.narrow(objref, ExemptionHome.class);
				homeExemption = (ExemptionEJBLocalHome)(objref);
				
				if (objExemption == null) {
					objExemption = homeExemption.create();
				}
			}
		}
		catch (NamingException ex) {
			logger.debug(strClassName + ".getExemptionEJB() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			throw ex;
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".getExemptionEJB() error! -- " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Method getDeveloperList
	 * @return mtxList
	 */
	public StringMatrix getDeveloperList() throws Exception {
		StringMatrix smtDevList = new StringMatrix();
		Collection colDevList = null;

		try {
			getExemptionEJB();
			colDevList = objExemption.getDeveloperListEJB();

			if (colDevList != null && ! colDevList.isEmpty()) {
				Iterator it = colDevList.iterator();
				while (it.hasNext()) {
					CommonListBean beanCommonList = (CommonListBean) it.next();
					StringVector vecDevList = new StringVector(4);

					vecDevList.setCell(0, beanCommonList.getDevId());
					vecDevList.setCell(1, beanCommonList.getAccount());
					vecDevList.setCell(2, beanCommonList.getDevName());
					vecDevList.setCell(3, beanCommonList.getGroupName());
					smtDevList.addRow(vecDevList);
				}
			}
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".getDeveloperList() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".getDeveloperList(): " + ex.toString());
			logger.error(ex);
		}
		return smtDevList;
	}

	/**
	 * Method getExemptionListBO
	 * Call getExemptionListEJB() to list of Timesheet Exemption cases
	 * @param beanUserInfo
	 * @param beanExemptionInfo
	 * @return ExemptionInfoBean
	 * @throws Exception
	 */
	public ExemptionListBean getExemptionListBO(UserInfoBean beanUserInfo, ExemptionListBean beanExemptionList, ExemptionInfoBean beanExemptionInfo) throws Exception {
		try {
			getExemptionEJB();

			Collection colExemption = null;
			String strMessage = null;
			String strGroupName = beanExemptionInfo.getGroupName();
			String strAccount = beanExemptionInfo.getDevAccount();
			String strName = beanExemptionInfo.getDevName();
			String strSearchFromDate = beanExemptionInfo.getSearchFromDate();
			String strSearchToDate = beanExemptionInfo.getSearchToDate();
			int intType = beanExemptionInfo.getType();
			int intCurrentPage = beanExemptionList.getCurrentPage();

			colExemption = objExemption.getExemptionListEJB(strGroupName, strAccount, strName, intType, strSearchFromDate, strSearchToDate, intCurrentPage);
			beanExemptionList.setExemptionList(colExemption);

			int intTotalPage = objExemption.getTotalPage();
			int intTotalExemption = objExemption.getTotalExemption();
			beanExemptionList.setTotalPage(intTotalPage);
			beanExemptionList.setTotalExemption(intTotalExemption);

			if (intCurrentPage > (intTotalPage - 1)) {
				beanExemptionList.setCurrentPage(0);
			}
			else {
				beanExemptionList.setCurrentPage(intCurrentPage);
			}
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".getExemptionListBO() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".getExemptionListBO(): " + ex.toString());
			logger.error(ex);
		}
		return beanExemptionList;
	}

	/**
	 * Method getExistedAccountBO
	 * @param beanExemptionInfo
	 * @return String
	 * @throws Exception
	 */
	public String getExistedAccountBO(ExemptionInfoBean beanExemptionInfo) throws Exception {
		String strMessage = null;

		try {
			getExemptionEJB();
			String strAccount = beanExemptionInfo.getDevAccount();
			String strName = beanExemptionInfo.getDevName();

			Collection colHasExistedAccount = null;
			colHasExistedAccount = objExemption.hasExistedAccount(strAccount, strName);

			if (colHasExistedAccount.isEmpty()) {
				if (!strAccount.equals("")) {
					strMessage = "Have not user "+strAccount+" in database. Please enter another !";
				}
				else if (!strName.equals("")) {
					strMessage = "Have not user "+strName+" in database. Please enter another !";
				}
				else if (!strAccount.equals("") && !strName.equals("")) {
					strMessage = "Have not user "+strAccount+" in database. Please enter another !";
				}
			}
		}
		catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".getExistedAccountBO() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".getExistedAccountBO(): "	+ ex.toString());
			ex.printStackTrace();
			logger.error(ex);
		}
		return strMessage;
	}

	/**
	 * Method deleteExemptionBO
	 * Call deleteExemptionEJB() to delete an exemption case
	 * @param arrExemptionId
	 * @return nResult
	 * @throws Exception
	 */
	public int deleteExemptionBO(String[] arrExemptionId) throws Exception {
		int nResult = -1;
		try {
			getExemptionEJB();

			for (int i = 0; i < arrExemptionId.length; i++) {
				int id = objExemption.getExemptionNewId(arrExemptionId[i]);
				objExemption.deleteExemptionEJB(arrExemptionId[i]);
				
				// Added by HaiMM ========================
				boolean isExits = false;
				Collection colExemptionList = objExemption.getDummyMigrationByIdEJB();
				
				ExemptionBean beanExemption = new ExemptionBean();
				beanExemption.setExemptionId(Integer.parseInt(arrExemptionId[i]));
				
				if (colExemptionList != null && !colExemptionList.isEmpty()) {
					Iterator it = colExemptionList.iterator();
					while (it.hasNext()) {
						ExemptionBean beanExemption_Tmp = (ExemptionBean) it.next();
						if (beanExemption_Tmp.getExemptionId() == beanExemption.getExemptionId()) {
							isExits = true;
							break;
						}
					}
				}
				if (isExits) {
					if (id>0)
						objExemption.updateDummyMigrationEJB(beanExemption.getExemptionId(), TIMESHEET.DELETE1);
					else objExemption.updateDummyMigrationEJB(beanExemption.getExemptionId(), TIMESHEET.DELETE); 
				} else {
					if (id>0)
						objExemption.addDummyExemptionEJB(beanExemption.getExemptionId(),TIMESHEET.DELETE1);
					else objExemption.addDummyExemptionEJB(beanExemption.getExemptionId(),TIMESHEET.DELETE); 					
				}
				// End ====================================
			}
		}
		catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".deleteExemptionBO() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".deleteExemptionBO(): " + ex.toString());
			logger.error(ex);
		}
		return nResult;
	}

	/**
	 * Method addExemptionFormBO
	 * Call addExemptionEJB() to add an exemption case
	 * @param beanExemptionInfo
	 * @return strMessage
	 * @throws Exception
	 */
	public String addExemptionFormBO(ExemptionInfoBean beanExemptionInfo) throws Exception {
		String strMessage = null;

		try {
			getExemptionEJB();

			int intExemptionId = beanExemptionInfo.getExemptionId();
			int intDevId = beanExemptionInfo.getDeveloperId();
			int intType = beanExemptionInfo.getType();

			String strGroup = beanExemptionInfo.getGroupName();
			String strSunday = beanExemptionInfo.getSunday();
			String strMonday = beanExemptionInfo.getMonday();
			String strTuesday = beanExemptionInfo.getTuesday();
			String strWednesday = beanExemptionInfo.getWednesday();
			String strThursday = beanExemptionInfo.getThursday();
			String strFriday = beanExemptionInfo.getFriday();
			String strSaturday = beanExemptionInfo.getSaturday();
			String strWeekDay = strSunday+strMonday+strTuesday+strWednesday+strThursday+strFriday+strSaturday;

			String strFromDate = beanExemptionInfo.getFromDate();
			String strToDate = beanExemptionInfo.getToDate();
			String strReason = beanExemptionInfo.getReason();
			String strNote = beanExemptionInfo.getNote();

			Collection colHasExistedExemption = null;
			colHasExistedExemption = objExemption.hasExistedExemption(intExemptionId, intDevId, intType, strFromDate, strToDate);

			if (colHasExistedExemption!= null && !colHasExistedExemption.isEmpty()) {
				Iterator it = colHasExistedExemption.iterator();
				while (it.hasNext()) {
					ExemptionBean beanExemption = (ExemptionBean) it.next();
					if (beanExemption.getType() == 1) {
						strMessage = "This user has had Permanent's type. Please update it if you want to have another type !";
						break;
					}
					else if (beanExemption.getType() == 2) {
						strMessage = "This user has had Temporary's type from "+beanExemption.getFromDate()+ " to "+beanExemption.getToDate()+". Please enter another !";
						break;
					}
					else if (beanExemption.getType() == 3) {
						strMessage = "This user has had Weekly type from "+beanExemption.getFromDate()+ " to "+beanExemption.getToDate()+". Please enter another !";
						break;
					}
				}
			}
			else if (intDevId == 1) {
				strMessage = "Have not user at Group "+strGroup+". Please enter another !";	
			}
			else {
				objExemption.addExemptionEJB(intDevId, intType, strWeekDay, strFromDate, strToDate, strReason, strNote);
				strMessage = "";
			}
		}
		catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".addExemptionFormBO() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".addExemptionFormBO(): "	+ ex.toString());
			ex.printStackTrace();
			logger.error(ex);
		}
		return strMessage;
	}

	// Added by HaiMM ================
	public String addDummyExemptionFormBO(ExemptionInfoBean beanExemptionInfo) throws Exception {
		String strMessage = null;

		try {
			getExemptionEJB();

			int intDevId = beanExemptionInfo.getDeveloperId();
			
			ExemptionBean beanExemption = objExemption.getDummyExemptionByIdEJB(intDevId);
			objExemption.addDummyExemptionEJB(beanExemption.getExemptionId(), TIMESHEET.INSERT);
			objExemption.updateDummyExemptionEJB(beanExemption.getExemptionId());	
		
		}
		catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".addDummyExemptionFormBO() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".addDummyExemptionFormBO(): "	+ ex.toString());
			ex.printStackTrace();
			logger.error(ex);
		}
		return strMessage;
	}
	// End ===========================
	/**
	 * Method updateExemptionFormBO
	 * Call updateExemptionEJB() to update an exemption case
	 * @param beanExemptionInfo
	 * @return strMessage
	 * @throws Exception
	 */
	public String updateExemptionFormBO(ExemptionInfoBean beanExemptionInfo) throws Exception {
		String strMessage = null;

		try {
			getExemptionEJB();

			int intExemptionId = beanExemptionInfo.getExemptionId();
			int intDevId = beanExemptionInfo.getDeveloperId();
			int intType = beanExemptionInfo.getType();

			String strSunday = beanExemptionInfo.getSunday();
			String strMonday = beanExemptionInfo.getMonday();
			String strTuesday = beanExemptionInfo.getTuesday();
			String strWednesday = beanExemptionInfo.getWednesday();
			String strThursday = beanExemptionInfo.getThursday();
			String strFriday = beanExemptionInfo.getFriday();
			String strSaturday = beanExemptionInfo.getSaturday();
			String strWeekDay = strSunday+strMonday+strTuesday+strWednesday+strThursday+strFriday+strSaturday;

			String strFromDate = beanExemptionInfo.getFromDate();
			String strToDate = beanExemptionInfo.getToDate();
			String strReason = beanExemptionInfo.getReason();
			String strNote = beanExemptionInfo.getNote();

			Collection colHasExistedUpdateExemption = null;
			colHasExistedUpdateExemption = objExemption.hasExistedExemption(intExemptionId, intDevId, intType, strFromDate, strToDate);

			if (colHasExistedUpdateExemption!= null && !colHasExistedUpdateExemption.isEmpty()) {
				Iterator it = colHasExistedUpdateExemption.iterator();
				while (it.hasNext()) {
					ExemptionBean beanExemption = (ExemptionBean) it.next();
					if (beanExemption.getType() == 1) {
						strMessage = "This user has had Permanent's type. Please update it if you want to have another type !";
						break;
					}
					else if (beanExemption.getType() == 2) {
						strMessage = "This user has had Temporary's type from "+beanExemption.getFromDate()+ " to "+beanExemption.getToDate()+". Please enter another !";
						break;
					}
					else if (beanExemption.getType() == 3) {
						strMessage = "This user has had Weekly type from "+beanExemption.getFromDate()+ " to "+beanExemption.getToDate()+". Please enter another !";
						break;
					}
				}
			}
			else {
				objExemption.updateExemptionEJB(intExemptionId, intType, strWeekDay, strFromDate, strToDate, strReason, strNote);
				strMessage = "";
				// Added by HaiMM ========================
				boolean isExits = false;
				Collection colExemptionList = objExemption.getDummyMigrationByIdEJB();
				
				ExemptionBean beanExemption = new ExemptionBean();
				beanExemption.setExemptionId(intExemptionId);
				
				if (colExemptionList != null && !colExemptionList.isEmpty()) {
					Iterator it = colExemptionList.iterator();
					while (it.hasNext()) {
						ExemptionBean beanExemption_Tmp = (ExemptionBean) it.next();
						if (beanExemption_Tmp.getExemptionId() == beanExemption.getExemptionId()) {
							isExits = true;
							break;
						}
					}
				}
				if (isExits) {
					objExemption.updateDummyMigrationEJB(beanExemption.getExemptionId(), TIMESHEET.UPDATE);
				} else {
					objExemption.addDummyExemptionEJB(beanExemption.getExemptionId(),TIMESHEET.UPDATE);					
				}
				// End ====================================				
			}
		}
		catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".updateExemptionFormBO() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".updateExemptionFormBO(): "	+ ex.toString());
			logger.error(ex);
		}
		return strMessage;
	}

	/**
	 * Method getExemptionByIdBO
	 * Call getExemptionByIdEJB() to get an exemption case by exemptionId field
	 * @param intExemptionId
	 * @return beanExemptionInfo
	 * @throws Exception
	 */

	public ExemptionInfoBean getExemptionByIdBO(String strExemptionId) throws Exception {
		ExemptionInfoBean beanExemptionInfo = new ExemptionInfoBean();
		int intExemptionId = 0;
		try {
			getExemptionEJB();
			
			intExemptionId = Integer.parseInt(strExemptionId);
			ExemptionBean beanExemption = objExemption.getExemptionByIdEJB(intExemptionId);

			beanExemptionInfo.setId(strExemptionId);
			beanExemptionInfo.setGroupName(beanExemption.getGroup());
			beanExemptionInfo.setAccount(beanExemption.getAccount());
			beanExemptionInfo.setDevName(beanExemption.getFullName());
			beanExemptionInfo.setDeveloperId(beanExemption.getDeveloperId());
			beanExemptionInfo.setType(beanExemption.getType());
			beanExemptionInfo.setWeekDay(beanExemption.getWeekDay());
			beanExemptionInfo.setFromDate(beanExemption.getFromDate());
			beanExemptionInfo.setToDate(beanExemption.getToDate());
			beanExemptionInfo.setReason(beanExemption.getReason());
			beanExemptionInfo.setNote(beanExemption.getNote());

			// Translate weekday string into separated week days
			if (beanExemptionInfo.getType() == 3) {
				if (beanExemptionInfo.getWeekDay() != null) {
					String strWeekDay = beanExemptionInfo.getWeekDay();
					for (int i = 0; i < strWeekDay.length(); i++) {
						if (strWeekDay.charAt(i) == '1') {
							beanExemptionInfo.setSunday("1");
						}
						else if (strWeekDay.charAt(i) == '2') {
							beanExemptionInfo.setMonday("2");
						}
						else if (strWeekDay.charAt(i) == '3') {
							beanExemptionInfo.setTuesday("3");
						}
						else if (strWeekDay.charAt(i) == '4') {
							beanExemptionInfo.setWednesday("4");
						}
						else if (strWeekDay.charAt(i) == '5') {
							beanExemptionInfo.setThursday("5");
						}
						else if (strWeekDay.charAt(i) == '6') {
							beanExemptionInfo.setFriday("6");
						}
						else if (strWeekDay.charAt(i) == '7') {
							beanExemptionInfo.setSaturday("7");
						}
					}
				}
			}
		}
		catch (NumberFormatException ex) {
			logger.debug(ex.getMessage());
		}
		catch (javax.naming.NamingException ex) {
			logger.debug(strClassName + ".getExemptionByIdBO() error! -- " + ex.getMessage() + " Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug(strClassName + ".getExemptionByIdBO(): "	+ ex.toString());
			logger.error(ex);
		}
		return beanExemptionInfo;
	}
}