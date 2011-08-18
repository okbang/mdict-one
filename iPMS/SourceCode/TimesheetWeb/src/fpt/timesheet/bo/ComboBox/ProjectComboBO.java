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
 
 package fpt.timesheet.bo.ComboBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

import fpt.timesheet.ApproveTran.ejb.common.ProjectComboEJB;
import fpt.timesheet.ApproveTran.ejb.common.ProjectComboEJBLocal;
import fpt.timesheet.ApproveTran.ejb.common.ProjectComboEJBLocalHome;
//import fpt.timesheet.ApproveTran.ejb.common.ProjectComboHome;
//import fpt.timesheet.ApproveTran.ejb.common.ProjectComboRemote;
import fpt.timesheet.ApproveTran.ejb.common.ProjectComboModel;

import fpt.timesheet.constant.JNDI;
import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

public class ProjectComboBO {
	private static Logger logger = Logger.getLogger(ProjectComboBO.class.getName());
	public ProjectComboBO() {
	}

	/**
	 * @param arrList
	 * @return
	 */
	private StringMatrix ConvertToStrMatrix(ArrayList arrList) {
		int nSize = arrList.size() + 1; // TrangTK
		StringMatrix mtxList = new StringMatrix(nSize, 7); // Modified by HaiMM: 4 --> 6
		Iterator it = arrList.iterator();
		//String strCode = "";
		//String strName = "";
		String strAllValue = "";
		StringBuffer strTemp = new StringBuffer(); // Modified by HaiMM
		// All project - TrangTK
		int i = 0;
		mtxList.setCell(i, 0, "0");
		mtxList.setCell(i, 1, "All");
		mtxList.setCell(i, 2, "All");
		i++;
		while (it.hasNext()) {
			ProjectComboModel clmData = (ProjectComboModel) it.next();
			mtxList.setCell(i, 0, clmData.getID());
			// TrangTK
			mtxList.setCell(i, 1, clmData.getCode());
			mtxList.setCell(i, 2, clmData.getName());
			mtxList.setCell(i, 3, clmData.getGroup());
			mtxList.setCell(i, 4, clmData.getStatus());
			mtxList.setCell(i, 5, clmData.getStartdate()); //Modified by HaiMM
			mtxList.setCell(i, 6, clmData.getPlanStartdate()); //Modified by HaiMM
			strTemp.append(clmData.getID() + ",");  //Modified by HaiMM
			i++;
		}
		strAllValue = strTemp.toString();  //Modified by HaiMM
		if (strAllValue.length() > 1) {
			strAllValue = strAllValue.substring(0, strAllValue.length() - 1);
			mtxList.setCell(0, 0, strAllValue);
		}        
		return mtxList;
	}

	/**
	 * @param arrList
	 * @return
	 */
	private StringMatrix StatusListToStrMatrix(ArrayList arrList) {
		int nSize = arrList.size() + 1;
		StringMatrix mtxList = new StringMatrix(nSize, 2);
		Iterator it = arrList.iterator();
//		String strAllValue = ""; -- by HaiMM
		int i = 0;
		mtxList.setCell(i, 0, Integer.toString(Timesheet.PROJECT_STATUS_ALL));
		mtxList.setCell(i, 1, Timesheet.PROJECT_STATUS_ALL_STR);
		i++;
		while (it.hasNext()) {
			ProjectComboModel clmData = (ProjectComboModel) it.next();
			mtxList.setCell(i, 0, clmData.getID());
			mtxList.setCell(i, 1, clmData.getName());
//			strAllValue += clmData.getID() + ","; -- by HaiMM
			i++;
		}
		return mtxList;
	}

	/**
	 * @param strRole
	 * @param nDeveloperID
	 * @param nPageType
	 * @param nProjectStatus
	 * @return
	 */
	public StringMatrix getProjectComboList(String strRole, int nDeveloperID, int nPageType, int nProjectStatus) {
		ArrayList ProjectList = new ArrayList();
		ProjectComboEJBLocalHome ejbHome;
		ProjectComboEJBLocal ejbRemote;
		ProjectComboModel clmData;
		try {
			Context ic = new InitialContext();
			java.lang.Object objref = ic.lookup(JNDI.PROJECT_COMBO);
			ejbHome = (ProjectComboEJBLocalHome)(objref);
			ejbRemote = ejbHome.create();
			Collection projectList = ejbRemote.getProjectList(strRole, nDeveloperID, nPageType, nProjectStatus);
			Iterator it = projectList.iterator();
			while (it.hasNext()) {
				clmData = (ProjectComboModel) it.next();
				ProjectList.add(clmData);
			}
			// release resource
			ejbRemote = null;
		}
		catch (Exception re) {
			logger.error("Couldn't locate Definition, Exception is " + re.getMessage());
		}
		return ConvertToStrMatrix(ProjectList);
	}

	/**
	 * @param strRole
	 * @param nDeveloperID
	 * @param nPageType
	 * @param nProjectStatus
	 * @return
	 */
	public StringMatrix getProjectComboList(String strRole, int intDeveloperID, int intProjectStatus) {
		ArrayList ProjectList = new ArrayList();
		ProjectComboEJBLocalHome ejbHome;
		ProjectComboEJBLocal ejbRemote;
		ProjectComboModel clmData;
		
		try {
			Context ic = new InitialContext();
			java.lang.Object objref = ic.lookup(JNDI.PROJECT_COMBO);
//			ejbHome = (ProjectComboEJBLocalHome) PortableRemoteObject.narrow(objref, ProjectComboHome.class);
			ejbHome = (ProjectComboEJBLocalHome)(objref);
			ejbRemote = ejbHome.create();
			Collection projectList = ejbRemote.getProjectList(strRole, intDeveloperID, intProjectStatus);
			Iterator it = projectList.iterator();
			while (it.hasNext()) {
				clmData = (ProjectComboModel) it.next();
				ProjectList.add(clmData);
			}
			// release resource
			ejbRemote = null;
		}
		catch (Exception re) {
			logger.error("Couldn't locate Definition, Exception is " + re.getMessage());
		}
		return ConvertToStrMatrix(ProjectList);
	}

	/**
	 * @param strRole
	 * @param nDeveloperID
	 * @return
	 */
	public StringMatrix getProjectStatusComboList(String strRole, int nDeveloperID) {
		ArrayList ProjectStatusList = new ArrayList();
		ProjectComboEJBLocalHome ejbHome;
		ProjectComboEJBLocal ejbRemote;
		ProjectComboModel clmData;
		try {
			Context ic = new InitialContext();
			java.lang.Object objref = ic.lookup(JNDI.PROJECT_COMBO);
//			ejbHome = (ProjectComboHome) PortableRemoteObject.narrow(objref, ProjectComboHome.class);
			ejbHome = (ProjectComboEJBLocalHome)(objref);
			ejbRemote = ejbHome.create();
			Collection projectList = ejbRemote.getProjectStatusList(strRole, nDeveloperID);
			Iterator it = projectList.iterator();
			while (it.hasNext()) {
				clmData = (ProjectComboModel) it.next();
				ProjectStatusList.add(clmData);
			}
			// release resource
			ejbRemote = null;
		}
		catch (Exception re) {
			re.printStackTrace();
		}
		return StatusListToStrMatrix(ProjectStatusList);
	}    
    
}