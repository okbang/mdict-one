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
 
 package com.fms1.common;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fms1.tools.*;
import com.fms1.infoclass.*;
import com.fms1.web.Parameters;
import com.fms1.web.Fms1Servlet;

/**
 * Parameter menu pages
 *
 */

public final class ParamCaller {
    public static boolean checkAddTail = true;
	public static final void methodListCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		final Vector methodList = Param.getEstimationMethodList();
		session.setAttribute("methodList", methodList);
		Fms1Servlet.callPage("methodList.jsp",request,response);
	}
	public static final void methodAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final EstimationMethodInfo methodInfo = new EstimationMethodInfo();
		methodInfo.name = request.getParameter("methodName");
		methodInfo.note = request.getParameter("methodDesc");
		Param.addEstimationMethod(methodInfo);
		methodListCaller(request, response);
	}
	public static final void methodUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String temp = request.getParameter("methodID");
			Vector methodList = (Vector) session.getAttribute("methodList");
			int methodID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
				return;
			}
			else {
				methodID = Integer.parseInt(temp);
				if ((methodID > methodList.size() - 1) || (methodID < 0)) {
					Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
					return;
				}
			}
			final EstimationMethodInfo methodInfo = (EstimationMethodInfo) methodList.elementAt(methodID);
			methodInfo.name = request.getParameter("methodName");
			methodInfo.note = request.getParameter("methodDesc");
			Param.setEstimationMethod(methodInfo);
			methodListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void methodDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String temp = request.getParameter("methodID");
			Vector methodList = (Vector) session.getAttribute("methodList");
			int methodID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
				return;
			}
			else {
				methodID = Integer.parseInt(temp);
				if ((methodID > methodList.size() - 1) || (methodID < 0)) {
					Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
					return;
				}
			}
			final EstimationMethodInfo methodInfo = (EstimationMethodInfo) methodList.elementAt(methodID);
			Param.delEstimationMethod(methodInfo.methodID);
			methodListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void bizDomainCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		final Vector bizdomainList = Param.getBizDomainList();
		session.setAttribute("bizdomainList", bizdomainList);
		Fms1Servlet.callPage("bizdomainList.jsp",request,response);
	}
	public static final void bizDomainUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String temp = request.getParameter("bizdomainID");
			Vector bizdomainList = (Vector) session.getAttribute("bizdomainList");
			int bizdomainID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
				return;
			}
			else {
				bizdomainID = Integer.parseInt(temp);
				if ((bizdomainID > bizdomainList.size() - 1) || (bizdomainID < 0)) {
					Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
					return;
				}
			}
			final BizDomainInfo info = (BizDomainInfo) bizdomainList.elementAt(bizdomainID);
			info.name = request.getParameter("bizdomainName");
            info.domainStatus =Byte.parseByte(request.getParameter("selStatus"));
            Param.setBizDomain(info);
			bizDomainCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void bizDomainDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String temp = request.getParameter("bizdomainID");
			Vector bizdomainList = (Vector) session.getAttribute("bizdomainList");
			int bizdomainID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
				return;
			}
			else {
				bizdomainID = Integer.parseInt(temp);
				if ((bizdomainID > bizdomainList.size() - 1) || (bizdomainID < 0)) {
					Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
					return;
				}
			}
			final BizDomainInfo info = (BizDomainInfo) bizdomainList.elementAt(bizdomainID);
			Param.delBizDomain(info.domainID);
			bizDomainCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void bizDomainAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final BizDomainInfo info = new BizDomainInfo();
		info.name = request.getParameter("bizdomainName");
		Param.addBizDomain(info);
		bizDomainCaller(request, response);
	}
	public static final void appTypeCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		final Vector apptypeList = Param.getAppTypeList();
		session.setAttribute("apptypeList", apptypeList);
		Fms1Servlet.callPage("apptypeList.jsp",request,response);
	}
	public static final void appTypeUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String temp = request.getParameter("apptypeID");
			Vector apptypeList = (Vector) session.getAttribute("apptypeList");
			int apptypeID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
				return;
			}
			else {
				apptypeID = Integer.parseInt(temp);
				if ((apptypeID > apptypeList.size() - 1) || (apptypeID < 0)) {
					Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
					return;
				}
			}
			final AppTypeInfo info = (AppTypeInfo) apptypeList.elementAt(apptypeID);
			info.name = request.getParameter("apptypeName");
            info.appStatus = Byte.parseByte(request.getParameter("selStatus"));
			Param.setAppType(info);
			appTypeCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void appTypeDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String temp = request.getParameter("apptypeID");
			Vector apptypeList = (Vector) session.getAttribute("apptypeList");
			int apptypeID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
				return;
			}
			else {
				apptypeID = Integer.parseInt(temp);
				if ((apptypeID > apptypeList.size() - 1) || (apptypeID < 0)) {
					Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
					return;
				}
			}
			final AppTypeInfo info = (AppTypeInfo) apptypeList.elementAt(apptypeID);
			if(Param.delAppType(info.apptypeID) == false){
				Fms1Servlet.callPage("error.jsp?error=Can not delete this application type because there is at least one project plan using this item",request,response);
			}
			appTypeCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void appTypeAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final AppTypeInfo info = new AppTypeInfo();
			info.name = request.getParameter("apptypeName");
			Param.addAppType(info);
			appTypeCaller(request, response);
		}
        catch (Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * @ provide a list of Contract Type contracttypeList.jsp
	 * @author: PhuNT
	 */

	public static final void contractTypeCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		final Vector contracttypeList = Param.getContractTypeList();

		session.setAttribute("contracttypeList", contracttypeList);
		Fms1Servlet.callPage("contracttypeList.jsp", request, response);
	}

	public static final void contractTypeAddnewCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final ContractTypeInfo info = new ContractTypeInfo();
			info.contracttypeName = request.getParameter("contracttypeName");
			info.contracttypeDescription = request.getParameter("contracttypeDescription");
			Param.addContractType(info);
			contractTypeCaller(request, response);
		} 
        catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void contractTypeUpdateCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();

		try {
			String temp = request.getParameter("contracttypeID");
			Vector contracttypeList = (Vector) session.getAttribute("contracttypeList");
			int contracttypeID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage(
					"error.jsp?error=Bad parameters",
					request,
					response);
				return;
			}
            else {
				contracttypeID = Integer.parseInt(temp);
				if ((contracttypeID > contracttypeList.size() - 1)
					|| (contracttypeID < 0)) {
					Fms1Servlet.callPage(
						"error.jsp?error=Bad parameters",
						request,
						response);
					return;
				}
			}
			final ContractTypeInfo info =
				(ContractTypeInfo) contracttypeList.elementAt(contracttypeID);
			info.contracttypeName = request.getParameter("contracttypeName");
			info.contracttypeDescription =
				request.getParameter("contracttypeDescription");
			info.contracttypeStatus =
				Byte.parseByte(request.getParameter("selStatus"));
			Param.setContractType(info);
			contractTypeCaller(request, response);
		}
        catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void contractTypeDeleteCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();

		try {
			String temp = request.getParameter("contracttypeID");
			Vector contracttypeList = (Vector) session.getAttribute("contracttypeList");
			int contracttypeID;
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage(
					"error.jsp?error=Bad parameters",
					request,
					response);
				return;
			} else {
                contracttypeID = Integer.parseInt(temp);
				if ((contracttypeID > contracttypeList.size() - 1) || (contracttypeID < 0)) {
					Fms1Servlet.callPage(
						"error.jsp?error=Bad parameters",
						request,
						response);
					return;
				}
			}
			final ContractTypeInfo info = (ContractTypeInfo) contracttypeList.elementAt(contracttypeID);
			Param.delContractType(info.contracttypeID);
            contractTypeCaller(request, response);
		}
        catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * provide a list of process tailoring for tailoringList.jsp
     * @author: Ngadtt
	 */
    
	public static final void proTailoringAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final ProTailoringInfo info = new ProTailoringInfo();
		info.Tailoring_per = request.getParameter("permission");
		info.Applicable_Cri = request.getParameter("applicable");
		info.ProcessID=CommonTools.parseInt(request.getParameter("processId"));
        info.tailLyfeCycle = Byte.parseByte(request.getParameter("selAddLyfeCycle"));
        checkAddTail = false;
        Param.addProTailoring(info);
		proTailoringSearchCaller(request, response);
        checkAddTail = true;
	}
	public static final void proTailoringSearchCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		String strProcess = request.getParameter("cboProcess");
		String isAll = request.getParameter("isAll");
      	long iProcessId = CommonTools.parseLong(strProcess);
		final Vector proTailoringList = Param.getProTailoringList(iProcessId);
        final Vector vtProcess = TailoringInfo.getProcessList();
		if(isAll!=null)
		{
			if (isAll.equalsIgnoreCase("")) isAll = "0";
		}
		else
		{
			isAll = "0";
		}
		session.setAttribute("isAll", isAll);
		session.setAttribute("vtProcess", vtProcess);
		session.setAttribute("strProcess", strProcess);
	    session.setAttribute("ProTailoringList",proTailoringList );
		Fms1Servlet.callPage("ProcessTailoringList.jsp",request,response);
	}
	public static final void tailRefCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strWuID = (String) request.getParameter("cboGroup");
			String strProcess = request.getParameter("cboProcess");
			//long lWuID = Parameters.FSOFT_WU; // FSOFT by default
			long lWuID = -1; // Blank page by default
			if (strWuID != null)
				lWuID = Long.parseLong(strWuID);
			
			String strWhere = "";
			String strWhereDev = "";
			
			if (strProcess != null) {
				if (!strProcess.equalsIgnoreCase("0")) {
					strWhere = strWhere + " AND TAIL.process_tail_id = " + strProcess + " ";
					
					strWhereDev = strWhereDev + " AND c.category = (SELECT name FROM process where process_id = " + strProcess + " ) ";
				}
			}
			
			Vector vtOccuredTail = Assets.getTailDevRefList(lWuID, strWhereDev, strWhere);
			
			final Vector vtProcess = Project.getProcessList();
			session.setAttribute("vtProcess", vtProcess);
			session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("strProcess", strProcess);
			session.setAttribute("vtOccuredTail", vtOccuredTail);
			Fms1Servlet.callPage("tailReferList.jsp", request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void proTailoringCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			Vector proTailoringList = Param.getProTailoringList();
            boolean b = false;
            for (int i=0; i<proTailoringList.size(); i++ ) {
                ProTailoringInfo proTail = (ProTailoringInfo) proTailoringList.get(i);
                if (proTail.tailLyfeCycle == -2) {
                    b = true;
                    proTail.tailLyfeCycle = 3;
                    Param.setProTailoring(proTail);
                }
            }
            if (b) {
                proTailoringList = Param.getProTailoringList();
            }
			final Vector vtProcess = TailoringInfo.getProcessList();
			
			session.setAttribute("isAll", "0");
			session.setAttribute("vtProcess", vtProcess);
			session.setAttribute("strProcess","0");
			session.setAttribute("ProTailoringList",proTailoringList );
			Fms1Servlet.callPage("ProcessTailoringList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void proTailoringUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
            Vector proTailoringList = (Vector) session.getAttribute("ProTailoringList");
			final Vector vtProcess = TailoringInfo.getProcessList();
			session.setAttribute("vtProcess", vtProcess);
			int tailID = CommonTools.parseInt(request.getParameter("tailID"));
			final ProTailoringInfo  info = (ProTailoringInfo) proTailoringList.elementAt(tailID);
			info.Tailoring_per = request.getParameter("permission");
			info.Applicable_Cri = request.getParameter("applicable");
			info.ProcessID = Integer.parseInt(request.getParameter("processId"));
            info.tailStatus = Byte.parseByte(request.getParameter("updateselStatus"));
            info.tailLyfeCycle = Byte.parseByte(request.getParameter("updateselLyfeCycle"));
			Param.setProTailoring(info);
            proTailoringCaller(request, response);
        }
		catch (Exception e) {
		  e.printStackTrace();
        }
    }
		
	public static final void proTailoringDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
        final HttpSession session = request.getSession();
		try {
            Vector proTailoringList = (Vector)session.getAttribute("ProTailoringList");
			int tailID;
			boolean blsuccess = false;
				
			tailID = CommonTools.parseInt(request.getParameter("tailID"));
			final ProTailoringInfo info = (ProTailoringInfo) proTailoringList.elementAt(tailID);
				
			blsuccess = Param.delProTailoring(info.TailoringID);
			String strsuccess = "0";
    		if (blsuccess == false) {
                strsuccess = "0";
    			final Vector proTailoringListExisted = Param.getproTailoringListExisted(info.TailoringID);
                session.setAttribute("tailoringexisted", proTailoringListExisted);
    			Fms1Servlet.callPage("TailoringExisted.jsp",request,response);
            }
    		else {
                strsuccess = "1";
                proTailoringSearchCaller(request, response);
            }
										
        }
		catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * provide a list of conversions for conversionList.jsp
	 * @author: Hoang My Duc
	 */
	public static final void conversionListInitCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		session.setAttribute("conversionBookmark", "0");
		session.setAttribute("searchedLangName", "");
		conversionListCaller(request, response);
	}
	public static final void conversionListByNameCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		conversionListCaller(request, response);
	}
	public static final void conversionListCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			Vector finalTable = new Vector();
			final java.util.Date lastUpdate = Parameters.convLastUpd;
			String convLastUpdate = CommonTools.dateFormat(lastUpdate);
			String searchedName = request.getParameter("searchedLangName");
			final Vector conversionList = Param.getConversionList(searchedName);
			if (searchedName == null)
				searchedName = "";
			else
				searchedName = "&searchedName=" + searchedName.trim();
			final Vector methodList = Param.getEstimationMethodList();
			int rowSize = methodList.size() + 3;
			//one for language name , one for unit, plus one for each method,plus one for note
			String[] convTableRow = new String[rowSize+1];
			//1st row contains titles
			convTableRow[0] = "Language name";
			convTableRow[1] = "Unit";
			convTableRow[2] = "Note";
			for (int i = 3; i < rowSize; i++) {
				final EstimationMethodInfo methodInfo = (EstimationMethodInfo) methodList.elementAt(i - 3);
				convTableRow[i] = methodInfo.name;
			}
			//Hieunv
			convTableRow[rowSize] = "Common Used";
			finalTable.add(convTableRow);
			//other rows
			int maxSize = conversionList.size();
			int i = 0;
			long mem; //to know when we are processing a new row
			ConversionInfo convInfo = null;
			if (maxSize != 0)
				convInfo = (ConversionInfo) conversionList.elementAt(0);
			while (i < maxSize) {
				mem = convInfo.languageID;
				final String[] convTableRowTemp = new String[rowSize + 2];
				//last cell will include languageID for update/delete
				convTableRowTemp[0] = convInfo.language;
				convTableRowTemp[1] = convInfo.sizeUnit;
				convTableRowTemp[2] = (convInfo.note == null) ? "N/A" : convInfo.note;
				convTableRowTemp[rowSize] = (convInfo.getCommonUsed() == 1)?"Yes":"No";
				convTableRowTemp[rowSize+1] = Long.toString(convInfo.languageID);
				//initialize all columns to N/A
				for (int j = 3; j < rowSize; j++)
					convTableRowTemp[j] = "N/A";
				for (;(i < maxSize); i++) {
					convInfo = (ConversionInfo) conversionList.elementAt(i);
					if (convInfo.languageID != mem) {
						finalTable.add(convTableRowTemp);
						break;
					}
					//put in the good column
					int k = 0;
					for (; k < methodList.size(); k++) {
						final EstimationMethodInfo methodInfo = (EstimationMethodInfo) methodList.elementAt(k);
						if (methodInfo.methodID == convInfo.methodID)
							break;
					}
					convTableRowTemp[k + 3] = CommonTools.formatDouble(convInfo.sloc);
					if (i == maxSize - 1) {
						finalTable.add(convTableRowTemp);
					}
				}
			}
			session.setAttribute("methodList", methodList);
			session.setAttribute("conversionList", finalTable);
			Fms1Servlet.callPage("conversionList.jsp?convLastUpdate=" + convLastUpdate + searchedName,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void conversionAddnewCaller(
		final HttpServletRequest request,
		final HttpServletResponse response, Fms1Servlet servlet) {
		final HttpSession session = request.getSession();
		try {
			ConversionInfo conversionInfo = new ConversionInfo();
			final java.util.Date today = new java.util.Date();
			final java.sql.Date sToday = new java.sql.Date(today.getTime());
			conversionInfo.languageID = -1;
			conversionInfo.language = request.getParameter("languageName");
			conversionInfo.note = request.getParameter("note");
			conversionInfo.sizeUnit = request.getParameter("unit");
			int iCommonUsed = (request.getParameter("chkCommonUsed") != null)?1:0;
			conversionInfo.setCommonUsed(iCommonUsed);
			conversionInfo.lastUpdate = sToday;
			conversionInfo = Param.addConversion(conversionInfo);
			final Vector methodList = (Vector) session.getAttribute("methodList");
			final String[] estMethod = request.getParameterValues("estMethod");
			for (int i = 0; i < methodList.size(); i++) {
				final EstimationMethodInfo methodInfo = (EstimationMethodInfo) methodList.elementAt(i);
				conversionInfo.methodID = methodInfo.methodID;
				conversionInfo.lastUpdate = null;
				if (!estMethod[i].equals(""))
					conversionInfo.sloc = Double.parseDouble(estMethod[i]);
				else
					conversionInfo.sloc = Double.NaN;
				Param.addConversion(conversionInfo);
			}
			doCheckSaveJavaScript(servlet, conversionInfo.sizeUnit);
			conversionListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void conversionUpdateCaller(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Fms1Servlet servlet) {
		final HttpSession session = request.getSession();
		try {
			final ConversionInfo conversionInfo = new ConversionInfo();
			final LanguageInfo languageInfo = new LanguageInfo();
			final java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
			String temp = request.getParameter("languageNo");
			int languageNo;
			final Vector conversionList = (Vector) session.getAttribute("conversionList");
			//check for bad parameters
			if ((temp == null) || (!ConvertString.isNumber(temp))) {
				Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
				return;
			}
			else {
				languageNo = Integer.parseInt(temp);
				if ((languageNo >= conversionList.size()) || (languageNo < 1)) {
					Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
					return;
				}
			}
			//get the table ID
			String[] languageDetail = (String[]) conversionList.elementAt(languageNo);
			languageInfo.languageID = Integer.parseInt(languageDetail[languageDetail.length-1]);
			languageInfo.name = request.getParameter("languageName");
			languageInfo.note = request.getParameter("note");
			languageInfo.sizeUnit = request.getParameter("unit");
			languageInfo.isrelevant = 0;
			if (request.getParameter("chkCommonUsed") != null){
				languageInfo.isrelevant = 1;
			}
			languageInfo.lastUpdate = today;
			if (!Param.setLanguage(languageInfo)) {
				Fms1Servlet.callPage(
					"conversionUpdate.jsp?languageNo="
						+ languageNo
						+ "&error=The language name already exists, please choose another one.",request,response);
				return;
			}
			//gets the mapping column/id
			final Vector methodList = (Vector) session.getAttribute("methodList");
			final String[] estMethod = request.getParameterValues("estMethod");
			for (int i = 0; i < methodList.size(); i++) {
				conversionInfo.languageID = languageInfo.languageID;
				final EstimationMethodInfo methodInfo = (EstimationMethodInfo) methodList.elementAt(i);
				conversionInfo.methodID = methodInfo.methodID;
				if (estMethod[i].trim().equals(""))
					conversionInfo.sloc = Double.NaN;
				else
					conversionInfo.sloc = Double.parseDouble(estMethod[i]);
				if (!Param.setConversion(conversionInfo))
					System.err.println("ParamCaller.conversionUpdateCaller Error: Cannot update conversion");
			}
			doCheckSaveJavaScript(servlet, languageInfo.sizeUnit);
			conversionListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static final void conversionDeleteCaller(
    	final HttpServletRequest request,
    	final HttpServletResponse response,
    	final Fms1Servlet servlet) {
        final HttpSession session = request.getSession();
        try {
            final int languageNo = Integer.parseInt(request.getParameter("languageNo"));
            String strSizeUnit = request.getParameter("sizeUnit");
            //get the table ID
            final Vector conversionList = (Vector)session.getAttribute("conversionList");
            String[] languageDetail = (String[])conversionList.elementAt(languageNo);
            final int languageID = Integer.parseInt(languageDetail[languageDetail.length - 1]);
            Vector existingModules = WorkProduct.getModuleByLanguage(languageID);
            if (existingModules.size() == 0){
				Param.delConversionByLanguage(languageID);
				doCheckSaveJavaScript(servlet, strSizeUnit);
            }
            else {
                request.setAttribute("error",existingModules);
            }
            conversionListCaller(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static final void distributionRateInitCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			ProjectInfo prjInf = Project.getProjectInfo(Long.parseLong((String)session.getAttribute("projectID")));
			java.sql.Date date = (prjInf.getStartDate() == null)
                                 ? new java.sql.Date(new java.util.Date().getTime())
                                 : prjInf.getStartDate();
			//EFFORT DIST BY PROCESS
            Vector effortDistributionByProcess = new Vector();
			for (int j = 0; j < ProjectInfo.lifecycles.length; j++) {
				final Vector effortProcList =
					Norms.getEffortDstrByProc(Parameters.FSOFT_WU, ProjectInfo.lifecycles[j], date);
                effortDistributionByProcess.addElement(effortProcList);
			}
            request.setAttribute("EffortProcList", effortDistributionByProcess);
			//EFFORT DIST BY STAGE
            request.setAttribute("EffortStageList", Norms.getEffortDstrByStg(prjInf));
			//DEFECT DIST BY ORIGIN
            Vector defectDistributionByOrigin = new Vector();
			for (int j = 0; j < ProjectInfo.lifecycles.length; j++) {
				final Vector defectPrcList =
					Norms.getDefectDstrByPrc(Parameters.FSOFT_WU, ProjectInfo.lifecycles[j], date);
                defectDistributionByOrigin.addElement(defectPrcList);
			}
            request.setAttribute("DefectProcList", defectDistributionByOrigin);
			Vector completenessList = Param.getCompletenessRateList(prjInf);
            session.setAttribute("CompletenessList", completenessList);
			Fms1Servlet.callPage("dstrRateList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void complRateUpdateCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final String[] complValue = request.getParameterValues("complValue");
			long projectID = Long.parseLong((String) session.getAttribute("projectID"));
            
			for (int j = 0; j <  RequirementInfo.statusList.length; j++) {
				final CompletenessRateInfo cri = new CompletenessRateInfo();
				cri.projectID = projectID;
				cri.statusID = RequirementInfo.statusList[j];
				cri.value = Double.parseDouble(complValue[j]);
				Param.setCompletenessRate(cri);
			}
			distributionRateInitCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void doCheckSaveJavaScript(Fms1Servlet servlet, String strUnit){
		try{
			if ("LOC".equals(strUnit.toUpperCase())){
				doSaveJavaScript(servlet, "languages.js");
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * do save all: common languages, all languages to a file
	 * @param strPath
	 * @param strFileName
	 */
	public static void doSaveJavaScript(Fms1Servlet servlet, String strFileName){
		String strPath = servlet.getServletContext().getRealPath("/jscript/").toString();
		Vector vtLanguage = Param.getLanguageList();
		long lArraySize = vtLanguage.size();
		LanguageInfo languageInfo = new LanguageInfo();
		try{
			File file =  new File(strPath, strFileName);
			if (file.exists()){
				file.delete();
			}
			PrintWriter outJavaSciptWrite = new PrintWriter(new FileWriter(file.getAbsolutePath(), true));
			StringBuffer strBuffer = new StringBuffer("");
			strBuffer.append("//Common languages (used for products and product LOC);\n");
			outJavaSciptWrite.println(strBuffer.toString());
			strBuffer.setLength(0);
			CommonTools.doCreateScriptArray(strBuffer, "common_lang", 1, 0);
			outJavaSciptWrite.println(strBuffer.toString());
			for (int i = 0, j = 0; i < lArraySize; i++){
				languageInfo = (LanguageInfo)vtLanguage.elementAt(i);
				if (languageInfo.isrelevant == 1){
					strBuffer.setLength(0);
					CommonTools.doCreateScriptArray(strBuffer,"common_lang", 2, j);
					CommonTools.doSetScriptArray(strBuffer, "common_lang", 2, j, 0, Integer.toString(languageInfo.languageID) + "; ");
					CommonTools.doSetScriptArray(strBuffer, "common_lang", 2, j, 1,"\"" + languageInfo.name + "\";");
					j++;
					outJavaSciptWrite.println(strBuffer.toString());
				}
			}
			strBuffer.setLength(0);
			strBuffer.append("//All languages (used for products and product LOC);\n");
			outJavaSciptWrite.println(strBuffer.toString());
			strBuffer.setLength(0);
			CommonTools.doCreateScriptArray(strBuffer, "all_lang", 1, 0);
			outJavaSciptWrite.println(strBuffer.toString());
			for (int i = 0; i < lArraySize; i++){
				strBuffer.setLength(0);
				languageInfo = (LanguageInfo)vtLanguage.elementAt(i);
				CommonTools.doCreateScriptArray(strBuffer, "all_lang", 2, i);
				CommonTools.doSetScriptArray(strBuffer, "all_lang", 2, i, 0, Integer.toString(languageInfo.languageID) + "; ");
				CommonTools.doSetScriptArray(strBuffer, "all_lang", 2, i, 1, "\"" + languageInfo.name + "\";");
				outJavaSciptWrite.println(strBuffer.toString());
			}
			strBuffer.setLength(0);
			outJavaSciptWrite.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}
}