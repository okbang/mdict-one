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

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fms1.infoclass.*;
import com.fms1.web.Fms1Servlet;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import sun.rmi.transport.proxy.HttpReceiveSocket;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Iterator;

/**
 * Requirement pages
 *
 */

public final class RequirementCaller implements com.fms1.web.Constants {

	public static final int REQUIREMENT_SHOULD_BE_UPDATED =10000;
	/**
	 * provide a list of requirements for requirementList.jsp
	 * @author: Hoang My Duc
	 */
	public static final void requirementListInitCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		requirementListCaller(request, response);
	}
	public static final void requirementListCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {						
			HttpSession session = request.getSession();
			int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
			ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
			request.setAttribute("projectInfo", projectInfo);

			Vector deliverableList = WorkProduct.getDeliverableList(workUnitID);
			request.setAttribute("deliverableList", deliverableList);

			Vector requirementList = Requirement.getRequirementList(projectInfo);
			request.setAttribute("requirementList", requirementList);

			Fms1Servlet.callPage("requirementList.jsp",request,response);	
            		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * View detail requirement information.
	 * @param request
	 * @param response
	 */
	public static final void requirementDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
			ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
			Vector requirementList = Requirement.getRequirementList(projectInfo);
			request.setAttribute("requirementList", requirementList);
			Fms1Servlet.callPage("requirementView.jsp", request, response);	
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public static final void requirementStatus(HttpServletRequest request, HttpServletResponse response){
		try{	
			HttpSession session = request.getSession();
			int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
			ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
			Vector requirementList = Requirement.getRequirementList(projectInfo);
			request.setAttribute("requirementList", requirementList);
			Fms1Servlet.callPage("requirementStatus.jsp", request, response);	
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
    public static final void requirementGraphCaller(final HttpServletRequest request, final HttpServletResponse response) {
        HttpSession session = request.getSession();
        int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
		ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
        Vector reqList = Requirement.getRequirementList(projectInfo);
        //must call that function before the below function, for some unknow reason the reqlist is updated by the following functions
        request.setAttribute("reqHdrInfo",Requirement.getRequirementInfo(reqList));
        Vector stageList = Schedule.getStageList(projectInfo);
        if (stageList.size() > 0) {
            StageInfo stageInf = null;
            int currStage = 0;
            for (int i = 0; i < stageList.size(); i++) {
                stageInf = (StageInfo)stageList.elementAt(i);
                currStage = i;
                if (stageInf.aEndD == null) {
                    break;
                }
            }
            Vector processEffortByStages;
            boolean hasBeenReplanned = Requirement.checkPlanRCR(stageInf.milestoneID);
            if (!hasBeenReplanned)
                processEffortByStages = Effort.getProcessEffortByStage(projectInfo, stageList, currStage - 1);
            else
                processEffortByStages = Effort.getProcessEffortByStage(projectInfo, stageList);
            
            Vector rcrProc = Requirement.getRCRByProcess(projectInfo, processEffortByStages, reqList, stageList,hasBeenReplanned);
            request.setAttribute("RCRByStageInfos", Requirement.getRCRByStage(projectInfo, stageList, reqList, rcrProc, hasBeenReplanned));
        }
        request.setAttribute("stageList", stageList);
       	
		// HaiMM: CR - Add Requirement section
		final ReqChangesInfo reqInfo = Requirement.getChangesRequirement(workUnitID);
		session.setAttribute("ReqChangesInfo", reqInfo);
       	
        Fms1Servlet.callPage("requirementGraph.jsp", request, response);
    }
	public static final void requirementByGroup(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
			ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
			final long deliverableId = Integer.parseInt(request.getParameter("deliverable"));
			final Vector requirementList;
			if (deliverableId != 0) {
				requirementList =
				Requirement.getRequirementListByDeliverable(projectInfo, deliverableId);
			}
			else {
				requirementList = Requirement.getRequirementList(projectInfo);
			}
			Vector deliverableList = WorkProduct.getDeliverableList(workUnitID);
			request.setAttribute("deliverableList", deliverableList);
			request.setAttribute("projectInfo", projectInfo);
			request.setAttribute("deliverableId", String.valueOf(deliverableId));
			request.setAttribute("requirementList", requirementList);
			Fms1Servlet.callPage("requirementList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void requirementAddnewPrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
			final Vector deliverableList = WorkProduct.getDeliverableList(workUnitID);
			request.setAttribute("deliverableList", deliverableList);

			final ScheduleHeaderInfo schHdrInfo = Schedule.getSchHeader(workUnitID);
			request.setAttribute("schHdrInfo", schHdrInfo);

			ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
			request.setAttribute("projectInfo", projectInfo);

			final Vector projectList = Project.getProjectList();
			request.setAttribute("prjList", projectList);

			Fms1Servlet.callPage("requirementAddnew.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doImportRequirement(final HttpServletRequest request,final HttpServletResponse response) throws BiffException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
	
			try {
				FileItem formFileItem = processFormFieldTeam(request, upload);
				InputStream inStream = formFileItem.getInputStream();
				readImportFileTeam(request, inStream);
				requirementListCaller(request, response);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
	}
	private static final FileItem processFormFieldTeam(HttpServletRequest request, ServletFileUpload upload) throws FileUploadException {
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			FileItem item = null;
			while (iter.hasNext()) {
				FileItem fileItem = (FileItem) iter.next();

				if (fileItem.isFormField()) {
				//	processFormField(item);
				} else {
					item = fileItem;
				}
			}
			return item;
		}
	private static final void readImportFileTeam(HttpServletRequest request, InputStream inStream) throws BiffException, IOException {
		final HttpSession session = request.getSession();
		Workbook workbook = Workbook.getWorkbook(inStream);

		Sheet sheet0 = workbook.getSheet(0);
		try{
			if(!sheet0.getCell(1, 68).getContents().trim().equalsIgnoreCase("Import Requirement")){
				session.setAttribute("ImportFail","fail");
				return;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			session.setAttribute("ImportFail","fail");
			return;
		}	

		Sheet sheet = workbook.getSheet(1);
		for(int i=1;i<51;i++){
			long id = Long.parseLong(sheet.getCell(2, i).getContents());
			if(id ==-1){
				session.setAttribute("ImportFail","fail");
				return; 
			}
		}
		
		  int[] added = new int[50];
		  for(int i=0;i<50;i++){
			  added[i] = 0;
		  }
		  
		  long projectIDinSection = Long.parseLong((String) session.getAttribute("projectID"));
		  long projectIDinExcell = Long.parseLong(sheet.getCell(23, 1).getContents());
		for(int i = 1 ; i < 51 ; i++){
			int test = Integer.parseInt(sheet.getCell(2, i).getContents());
			if(test != 0){
				if(projectIDinSection != projectIDinExcell){
				session.setAttribute("ImportFail","fail");
				return;
			  }
			}
		}
				  
		  int k=0;
		  for(int i = 1 ; i < 51 ; i++){
			  final ModuleInfo moduleInfo = new ModuleInfo();
			  NumberCell check = (NumberCell) sheet.getCell(0, i);
	
			  if(check.getValue() > 0 ){
				final RequirementInfo reqInfo = new RequirementInfo();
				reqInfo.name = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(1, i).getContents());
				reqInfo.moduleID = Integer.parseInt(sheet.getCell(2, i).getContents());
				reqInfo.type = CommonTools.parseInt(sheet.getCell(3, i).getContents());
				reqInfo.size = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(4, i).getContents()) != null ? Integer.parseInt(sheet.getCell(4, i).getContents()) : 0;
				
				java.util.Date status[] = new java.util.Date[8];
				final int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
				final ScheduleHeaderInfo schHdrInfo = Schedule.getSchHeader(workUnitID);
				Date prjStartDate = schHdrInfo.aStartD;
				if (prjStartDate == null){
					prjStartDate = new Date();
				}
				status[0] = prjStartDate;
				reqInfo.committedDate =CommonTools.parseSQLDate(sheet.getCell(5, i).getContents());
				status[1] = reqInfo.committedDate;
				reqInfo.designedDate =CommonTools.parseSQLDate(sheet.getCell(6, i).getContents());
				status[2] = reqInfo.designedDate;
				reqInfo.codedDate = CommonTools.parseSQLDate(sheet.getCell(7, i).getContents());
				status[3] = reqInfo.codedDate;
				reqInfo.testedDate = CommonTools.parseSQLDate(sheet.getCell(8, i).getContents());
				status[4] = reqInfo.testedDate;
				reqInfo.deployedDate =CommonTools.parseSQLDate(sheet.getCell(9, i).getContents());
				status[5] = reqInfo.deployedDate;
				reqInfo.acceptedDate =CommonTools.parseSQLDate(sheet.getCell(10, i).getContents());
				status[6] = reqInfo.acceptedDate;
				reqInfo.cancelledDate =CommonTools.parseSQLDate(sheet.getCell(11, i).getContents());
				status[7] = new Date();
				
				if(checkDate(status) == false){
					added[k] = -i;
					k++;
					session.setAttribute("error","Status date must be between previous status dates (or project start date "+CommonTools.dateFormat(prjStartDate)+") and next status dates (or today)");
					continue;
				}
				
				if (reqInfo.cancelledDate != null) {
					if (((status[0]).compareTo(reqInfo.cancelledDate) < 0) ||
						(reqInfo.cancelledDate.compareTo(status[7]) < 0)) {
							session.setAttribute("errorCancel","Cancelled date must be between project start date ("+CommonTools.dateFormat(prjStartDate)+") and today!");
							added[k] = -i;
							k++;
						continue;
					}
				}
				
				reqInfo.requirementSection = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(12, i).getContents());
				reqInfo.detailDesign = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(13, i).getContents());
				reqInfo.codeModuleName = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(14, i).getContents()) != null ? sheet.getCell(14, i).getContents() : "";
				reqInfo.testCase = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(15, i).getContents());
				reqInfo.releaseNote = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(16, i).getContents());
				if(!sheet.getCell(17, i).getContents().equals("") && (reqInfo.type != 1)){
					try{
						reqInfo.effort = Double.parseDouble(sheet.getCell(17, i).getContents());
					}catch(NumberFormatException e){
						reqInfo.effort = -1;
					}
				}
				if(!sheet.getCell(18, i).getContents().equals("?")&& (reqInfo.type != 1)){
					try{
						reqInfo.elapsedDay = (float)Double.parseDouble(sheet.getCell(18, i).getContents());
					}catch(NumberFormatException e){
						reqInfo.elapsedDay = -1;
					}
				}
				reqInfo.prevPrjID =CommonTools.parseInt(sheet.getCell(19, i).getContents());
				reqInfo.receivedDate =CommonTools.parseSQLDate(sheet.getCell(20, i).getContents());
				reqInfo.responseDate =CommonTools.parseSQLDate(sheet.getCell(21, i).getContents());
				reqInfo.projectID = Integer.parseInt((String) session.getAttribute("projectID"));
				final java.util.Date uDate = new java.util.Date();
				reqInfo.createDate = new java.sql.Date(uDate.getTime());
				  
				  if(Requirement.addRequirement(reqInfo)==true){
					  added[k] = i;
				  }else{
					  added[k] = -i;
				  }
				  k++;
			  }
		  }
		  session.setAttribute("AddedRecord",added);				
		  session.setAttribute("Imported","true");
	}
	
	public static final void addNewCustomer(final HttpServletRequest request,final HttpServletResponse response){
		try{
			String[] sName = request.getParameterValues("sName");
			String[] fName = request.getParameterValues("fName");
			String[] OG = request.getParameterValues("OGS");
			String[] note = request.getParameterValues("note");
			CustomerInfo data = new CustomerInfo();
			String duplicate = "";
			for (int i=0;i<sName.length;i++) {
				if (!sName[i].equals("")) {
					data.standardName = sName[i].trim();
					data.fullName = fName[i].trim();
					data.ofOGs = OG[i].trim();
					data.note = note[i].trim();
					if (Requirement.checkCustomerName(data.standardName)==true)
						Requirement.addCustomer(data);
					else {
						if (duplicate.equals("")) duplicate = data.standardName ; 
						else duplicate = duplicate + " , " + data.standardName ;
					}
					   
				}
				
			}
			if (duplicate.equals("")) duplicate = "1";
			request.getSession().setAttribute("addNewValue",duplicate);
			request.getSession().setAttribute("nameSearch","");
			request.getSession().setAttribute("listOgSearch","");
			doLoadCustomerPage1(request,response);	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static final void doUpdateCustomer(final HttpServletRequest request,final HttpServletResponse response){
		try{
			String duplicateUpdateCustomer = "0";
			
			String[] cusId = request.getParameterValues("customerId");
			String[] sName = request.getParameterValues("sName");
			String[] fName = request.getParameterValues("fName");
			String[] OG = request.getParameterValues("OGS");
			String[] note = request.getParameterValues("note");
			Vector data = Requirement.getCustomerList();
			Vector newData = new Vector();
			String line = "";
			for (int i=0;i<data.size();i++) {
				CustomerInfo temp = (CustomerInfo)data.get(i);
				int check1 = 0  ; 
				for (int j=0;j<sName.length;j++){
					if (temp.cusID == Long.parseLong(cusId[j])) {
						temp.standardName = sName[j].trim();
						temp.fullName = fName[j].trim();
						temp.ofOGs = OG[j].trim();
						temp.note = note[j].trim();
						temp.ofOBs = String.valueOf(j+1);
						check1 = 1 ;
						break; 
					}
				}
				if (check1 ==  0 ) temp.ofOBs = "-1";
				newData.add(temp);
			}
			for (int i=0;i<newData.size();i++){
				CustomerInfo temp1 = (CustomerInfo)newData.get(i);
				int count=0;
				for (int j=0;j<newData.size();j++){
					CustomerInfo temp2 = (CustomerInfo)newData.get(j);
					if (temp1.standardName.equals(temp2.standardName)) {
						count++; 
					}
				}
				if (count>=2) {
					duplicateUpdateCustomer = "1";
					if (!temp1.ofOBs.equals("-1"))
						line = line+"  "+temp1.ofOBs;
				}
			}
			if (duplicateUpdateCustomer.equalsIgnoreCase("0")) {
				for (int i=0;i<sName.length;i++) {
					CustomerInfo temp3 = new CustomerInfo();
					temp3.cusID = Long.parseLong(cusId[i].trim());
					temp3.standardName = sName[i].trim();
					temp3.fullName = fName[i].trim();
					temp3.ofOGs = OG[i].trim();
					temp3.note = note[i].trim();
					Requirement.updateCustomer(temp3);
				}
				doLoadCustomerPage(request,response);
			}
			else {
				Vector data1 = new Vector();
				for (int i=0;i<sName.length;i++) {
					CustomerInfo temp3 = new CustomerInfo();
					temp3.cusID = Long.parseLong(cusId[i].trim());
					temp3.standardName = sName[i].trim();
					temp3.fullName = fName[i].trim();
					temp3.ofOGs = OG[i].trim();
					temp3.note = note[i].trim();
					data1.add(temp3);
				}
				request.getSession().setAttribute("customerUpdateList",data1);
				request.getSession().setAttribute("duplicateUpdateCustomer",duplicateUpdateCustomer);
				request.getSession().setAttribute("line",line);
				Fms1Servlet.callPage("updateCustomer.jsp", request, response);	
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static final void updateCustomer(final HttpServletRequest request,final HttpServletResponse response){
		try{
			String value = request.getParameter("value");
			StringTokenizer token = new StringTokenizer(value.trim(),",");
			Vector data = new Vector();
			while (token.hasMoreElements()) {
				data.add(Requirement.getCustomer(Long.parseLong(token.nextToken().toString())));
			}
			String duplicateUpdateCustomer = "0";
			request.getSession().setAttribute("customerUpdateList",data);
			request.getSession().setAttribute("duplicateUpdateCustomer",duplicateUpdateCustomer);
			Fms1Servlet.callPage("updateCustomer.jsp", request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static final void doLoadCustomerPage(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String addNewValue = "1";
			Vector loadCustomerVector = new Vector();
			loadCustomerVector = Requirement.getCustomerList();
			Vector OgsList = Requirement.getListOGS();
			session.setAttribute("addNewValue",addNewValue);
			request.getSession().setAttribute("nameSearch","");
			request.getSession().setAttribute("listOgSearch","");
			session.setAttribute("OgsList",OgsList);
			session.setAttribute("loadCustomerVector",loadCustomerVector);
			Fms1Servlet.callPage("customer.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doSearchCustomer (final HttpServletRequest request, final HttpServletResponse response){
		try{
			String addNewValue = "1";
			request.getSession().setAttribute("addNewValue",addNewValue);
			Vector loadCustomerVector = new Vector();
			String nameSearch = (String)request.getParameter("nameSearch");;
			String listOgSearch = (String)request.getParameter("listOgSearch");
			request.getSession().setAttribute("nameSearch",nameSearch);
			request.getSession().setAttribute("listOgSearch",listOgSearch);
			loadCustomerVector = Requirement.getCustomerListBySearch(nameSearch);
			Vector newLoadCustomerVector = new Vector();
			if (!listOgSearch.trim().equals("")) { 
				/*StringTokenizer st = new StringTokenizer(listOgSearch,",") ;
				while (st.hasMoreTokens()) {
					String temp = st.nextToken().trim();
					if (!temp.equals("")) {
						for (int i=0;i<loadCustomerVector.size();i++){
							String temp1 = ((CustomerInfo)loadCustomerVector.get(i)).ofOGs;
							if (temp1!=null&&(!temp1.trim().equals(""))) {
								StringTokenizer temp2 = new StringTokenizer(temp1,",");
								while(temp2.hasMoreTokens()){
									String temp3 = temp2.nextToken().trim();
									if (temp3.equals(temp)) { 
										newLoadCustomerVector.add(loadCustomerVector.get(i));
										break;
									}
								}
							}
						}
					}
				}*/
				for (int i=0;i<loadCustomerVector.size();i++){
					String temp1 = ((CustomerInfo)loadCustomerVector.get(i)).ofOGs;
					if (temp1!=null&&(!temp1.trim().equals(""))) {
						StringTokenizer temp2 = new StringTokenizer(temp1,",");
						while(temp2.hasMoreTokens()){
							String temp3 = temp2.nextToken().trim();
							StringTokenizer st = new StringTokenizer(listOgSearch,",") ;
							int check = 0 ;
							while (st.hasMoreTokens()) {
								String temp = st.nextToken().trim(); 
								if (temp.equalsIgnoreCase(temp3)) {
									newLoadCustomerVector.add(loadCustomerVector.get(i));
									check = 1; 
									break;
								}
							}			
							if (check==1) break;
						}
					}
				}
				request.getSession().setAttribute("loadCustomerVector",newLoadCustomerVector);
			}
			else {
				request.getSession().setAttribute("loadCustomerVector",loadCustomerVector);	
			}
			Fms1Servlet.callPage("customer.jsp", request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static final void doLoadCustomerPage1(
			final HttpServletRequest request,
			final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			Vector loadCustomerVector = new Vector();
			loadCustomerVector = Requirement.getCustomerList();
			session.setAttribute("loadCustomerVector",loadCustomerVector);
			Fms1Servlet.callPage("customer.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doImportCustomer(final HttpServletRequest request,final HttpServletResponse response) throws BiffException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				FileItem formFileItem = processFormField(request, upload);
				InputStream inStream = formFileItem.getInputStream();
				readImportFile(request, inStream);
				
				Fms1Servlet.callPage("customer.jsp", request, response);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
	}
	private static final FileItem processFormField(HttpServletRequest request, ServletFileUpload upload) throws FileUploadException {
		List items = upload.parseRequest(request);
		Iterator iter = items.iterator();
		FileItem item = null;
		while (iter.hasNext()) {
			FileItem fileItem = (FileItem) iter.next();

			if (fileItem.isFormField()) {
			//	processFormField(item);
			} else {
				item = fileItem;
			}
		}
		return item;
	}
	
	private static final void readImportFile(HttpServletRequest request, InputStream inStream) throws BiffException, IOException {
		final HttpSession session = request.getSession();
		Workbook workbook = Workbook.getWorkbook(inStream);
		session.setAttribute("addNewValue","1");
		request.getSession().setAttribute("nameSearch","");
		request.getSession().setAttribute("listOgSearch","");
		Sheet sheet0 = workbook.getSheet(0);
		try{
			if(!sheet0.getCell(0, 0).getContents().trim().equalsIgnoreCase("FSOFT Customer Names")){
				session.setAttribute("ImportFail","fail");
				return;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			session.setAttribute("ImportFail","fail");
			return;
		}catch(Exception e){
			session.setAttribute("ImportFail","fail");
		}
		
		CustomerInfo customerInfo;
		Vector customerVector = new Vector();
		int i = 5;
	  	while(true){
		  	try{
				NumberCell check = (NumberCell) sheet0.getCell(0, i);
				if(sheet0.getCell(2, i).getContents().trim().equals("")){
					break;
				}
		  	}catch(Exception e){
		  		break;
		  	}
			
			//Insert code here
			customerInfo = new CustomerInfo();
			customerInfo.fullName = sheet0.getCell(1, i).getContents().trim();
			customerInfo.standardName = sheet0.getCell(2, i).getContents().trim();
			customerInfo.ofOGs = sheet0.getCell(3, i).getContents().trim();
			customerInfo.note = sheet0.getCell(4, i).getContents().trim();
			customerVector.add(customerInfo);
			
	  		i++;
		}
		int test = 0;

		Vector importSuccessCustomerList = new Vector();
		Vector duplicateCustomerList = new Vector();
		for (int j = 0; j < customerVector.size(); j++) {
			customerInfo = new CustomerInfo();
			customerInfo = (CustomerInfo)customerVector.elementAt(j);
			if(Requirement.checkCustomerName(customerInfo.standardName)==true){
				test++;
				if(Requirement.addCustomer(customerInfo) == false){
					session.setAttribute("ImportFail","fail");
					break;
				}
				importSuccessCustomerList.add(customerInfo);
			}else{
				duplicateCustomerList.add(customerInfo);
			}
		}
		if(importSuccessCustomerList.size()>0){
			session.setAttribute("importSuccessCustomerList",importSuccessCustomerList);
		}
		if(duplicateCustomerList.size()>0){
			session.setAttribute("duplicateCustomerList",duplicateCustomerList);
		}
		if(test ==0){
			session.setAttribute("ImportFail","fail");
			return;
		}
		session.setAttribute("ImportSuccess","success");
		session.setAttribute("customerVector",customerVector);
		
	}
		
	public static final boolean checkDate(Date[] status){
		for (int i = 1; i < 7; i++) {
			if (status[i] != null) {
				boolean b = true;
				for (int j = i - 1; j >= 0; j--) {
					if ((status[j] != null) && (status[j].compareTo(status[i]) > 0)) {
						b = false;
						break;
					}
				}
				if (b)
					for (int j = i + 1; j < 8; j++) {
						if ((status[j] != null) && (status[i].compareTo(status[j]) > 0)) {
							b = false;
							break;
						}
					}
				if (!b) {
					return false;
				}
			}
		}
		return true;
	}
	public static final void requirementAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final RequirementInfo reqInfo = new RequirementInfo();
			reqInfo.name = request.getParameter("name");
			reqInfo.moduleID = Integer.parseInt(request.getParameter("deliverable"));
			reqInfo.type = CommonTools.parseInt(request.getParameter("type"));
			reqInfo.size = request.getParameter("size") != null ? Integer.parseInt(request.getParameter("size")) : 0;
			final String[] status = request.getParameterValues("status");
            reqInfo.committedDate =CommonTools.parseSQLDate(status[1]);
            reqInfo.designedDate =CommonTools.parseSQLDate(status[2]);
            reqInfo.codedDate = CommonTools.parseSQLDate(status[3]);
            reqInfo.testedDate = CommonTools.parseSQLDate(status[4]);
            reqInfo.deployedDate =CommonTools.parseSQLDate(status[5]);
            reqInfo.acceptedDate =CommonTools.parseSQLDate(status[6]);
			reqInfo.cancelledDate =CommonTools.parseSQLDate(request.getParameter("cancelled"));
			reqInfo.requirementSection = request.getParameter("srs");
			reqInfo.detailDesign = request.getParameter("dd");
			reqInfo.codeModuleName =
				request.getParameter("codeModule") != null ? request.getParameter("codeModule") : "";
			reqInfo.testCase = request.getParameter("testCase");
			reqInfo.releaseNote = request.getParameter("releaseNote");
			reqInfo.effort =
				!request.getParameter("effort").equals("")
					&& (reqInfo.type != 1) ? Double.parseDouble(request.getParameter("effort")) : -1;
			reqInfo.elapsedDay =
				!request.getParameter("elapsedDays").equals("")
					&& (reqInfo.type != 1) ? (float) Double.parseDouble(request.getParameter("elapsedDays")) : -1;
			reqInfo.prevPrjID =CommonTools.parseInt(request.getParameter("prevPrjID"));
            reqInfo.receivedDate =CommonTools.parseSQLDate(request.getParameter("receivedDate"));
            reqInfo.responseDate =CommonTools.parseSQLDate(request.getParameter("responsedDate"));
			reqInfo.projectID = Integer.parseInt((String) session.getAttribute("projectID"));
			final java.util.Date uDate = new java.util.Date();
			reqInfo.createDate = new java.sql.Date(uDate.getTime());
			Requirement.addRequirement(reqInfo);
			requirementListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void requirementUpdatePrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			String reqID = request.getParameter("requirementID");
			final int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
			final Vector deliverableList = WorkProduct.getDeliverableList(workUnitID);
			final ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
			final ScheduleHeaderInfo schHdrInfo = Schedule.getSchHeader(workUnitID);
			final Vector moduleList = WorkProduct.getModuleList(workUnitID);
			final Vector projectList = Project.getProjectList();
			Vector requirementList = Requirement.getRequirementList(projectInfo);
			request.setAttribute("requirementList", requirementList);
			request.setAttribute("deliverableList", deliverableList);
			request.setAttribute("generalInfo", projectInfo);
			request.setAttribute("schHdrInfo", schHdrInfo);
			request.setAttribute("moduleList", moduleList);
			request.setAttribute("prjList", projectList);
			Fms1Servlet.callPage("requirementUpdate.jsp?requirementID="+reqID, request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void requirementBatchUpdatePrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final int workUnitID = Integer.parseInt(session.getAttribute("projectID").toString());
			long deliverableId = 0;
			final Vector requirementList;

			ProjectInfo projectInfo = Project.getProjectInfo(workUnitID);
			request.setAttribute("projectInfo", projectInfo);
			if (request.getParameter("deliverable") != null){
				deliverableId = Long.parseLong(request.getParameter("deliverable"));
			}
			if (deliverableId != 0) {
				requirementList =
				Requirement.getRequirementListByDeliverable(projectInfo, deliverableId);
			}
			else {
				requirementList = Requirement.getRequirementList(projectInfo);
			}
			request.setAttribute("requirementList", requirementList);

			final Vector deliverableList = WorkProduct.getDeliverableList(workUnitID);
			request.setAttribute("deliverableList", deliverableList);
			Fms1Servlet.callPage("requirementBatchEdit.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void requirementUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final RequirementInfo reqInfo = new RequirementInfo();
			reqInfo.name = request.getParameter("name");
			reqInfo.moduleID = Integer.parseInt(request.getParameter("deliverable"));
			reqInfo.type = request.getParameter("type") != null ? Integer.parseInt(request.getParameter("type")) : 0;
			reqInfo.size = request.getParameter("size") != null ? Integer.parseInt(request.getParameter("size")) : 0;
			final String[] status = request.getParameterValues("status");
			reqInfo.committedDate =CommonTools.parseSQLDate(status[1]);
			reqInfo.designedDate =CommonTools.parseSQLDate(status[2]);
			reqInfo.codedDate = CommonTools.parseSQLDate(status[3]);
			reqInfo.testedDate = CommonTools.parseSQLDate(status[4]);
			reqInfo.deployedDate =CommonTools.parseSQLDate(status[5]);
			reqInfo.acceptedDate =CommonTools.parseSQLDate(status[6]);
			reqInfo.cancelledDate =CommonTools.parseSQLDate(request.getParameter("cancelled"));
			reqInfo.requirementSection = request.getParameter("srs");
			reqInfo.detailDesign = request.getParameter("dd");
			reqInfo.codeModuleName =
				request.getParameter("codeModule") != null ? request.getParameter("codeModule") : "";
			reqInfo.testCase = request.getParameter("testCase");
			reqInfo.releaseNote = request.getParameter("releaseNote");
			reqInfo.effort =
				(!request.getParameter("effort").equals(""))
					&& (reqInfo.type != 1) ? Double.parseDouble(request.getParameter("effort")) : -1;
			reqInfo.elapsedDay =
				!request.getParameter("elapsedDays").equals("")
					&& (reqInfo.type != 1) ? (float) Double.parseDouble(request.getParameter("elapsedDays")) : -1;
			reqInfo.prevPrjID =
				(request.getParameter("prevPrjID") != null) ? Integer.parseInt(request.getParameter("prevPrjID")) : 0;
			reqInfo.receivedDate =CommonTools.parseSQLDate(request.getParameter("receivedDate"));
			reqInfo.responseDate =CommonTools.parseSQLDate(request.getParameter("responsedDate"));
			reqInfo.projectID = Integer.parseInt((String) session.getAttribute("projectID"));
			reqInfo.requirementID = Integer.parseInt(request.getParameter("requirementID"));
			Requirement.setRequirement(reqInfo);
			requirementListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	public static final void requirementDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final int requirementID = Integer.parseInt(request.getParameter("requirementID"));
		Requirement.delRequirement(requirementID);
		requirementListCaller(request, response);

	}
	/**
	 * AUTHOR: LINHDD !
	 */
	public static final void requirementUpdateStatus(final HttpServletRequest request, final HttpServletResponse response) {
		try {					
			RequirementInfo reqInfo = new RequirementInfo();			
			final SimpleDateFormat dateForm = new SimpleDateFormat("dd-MMM-yy");
			String[] arrRequirementId = request.getParameterValues("requirementID");
			String[] arrRequirementName = request.getParameterValues("requirementName");
			String[] arrCommitted = request.getParameterValues("Committed");
			String[] arrDesigned = request.getParameterValues("Designed");
			String[] arrCoded = request.getParameterValues("Coded");
			String[] arrTested = request.getParameterValues("Tested");
			String[] arrDeployed = request.getParameterValues("Deployed");
			String[] arrAccepted = request.getParameterValues("Accepted");
			String[] arrCancelled = request.getParameterValues("Cancelled");
			int noReq, i, j, k;
			noReq = arrRequirementId.length;
			String[] arrCommittedValue = new String[noReq];
			String[] arrDesignedValue = new String[noReq];
			String[] arrCodedValue = new String[noReq];
			String[] arrTestedValue = new String[noReq];
			String[] arrDeployedValue = new String[noReq];
			String[] arrAcceptedValue = new String[noReq];
			String[] arrCancelledValue = new String[noReq];
			for (i = 0; i < noReq; i++) {
				arrCommittedValue[i] = "";
				arrDesignedValue[i] = "";
				arrCodedValue[i] = "";
				arrTestedValue[i] = "";
				arrDeployedValue[i] = "";
				arrAcceptedValue[i] = "";
				arrCancelledValue[i] = "";
			}
									
			// greater than 10000 mean: value has been updated
			if (arrCommitted != null) {
				for (i = 0; i < arrCommitted.length; i++) {
					if (Integer.parseInt(arrCommitted[i]) >= REQUIREMENT_SHOULD_BE_UPDATED) {										
						j = Integer.parseInt(arrCommitted[i]) / (7 * REQUIREMENT_SHOULD_BE_UPDATED);
						arrCommittedValue[j] = "X";						
					}
					else {						
						k = Integer.parseInt(arrCommitted[i]);						
						if (k == 0) {
							j = 0;
						}
						else {
							j = k / 7;
						}
						arrCommittedValue[j] = "XX";
					}
				}
			}						
			if (arrDesigned != null) {
				for (i = 0; i < arrDesigned.length; i++) {
					if (Integer.parseInt(arrDesigned[i]) >= REQUIREMENT_SHOULD_BE_UPDATED) {
						j = ((Integer.parseInt(arrDesigned[i]) / REQUIREMENT_SHOULD_BE_UPDATED) - 1) / 7;
						arrDesignedValue[j] = "X";
					}
					else {
						k = Integer.parseInt(arrDesigned[i]);
						if (k == 0) {
							j = 0;
						}
						else {
							j = (k - 1) / 7;
						}
						arrDesignedValue[j] = "XX";
					}
				}
			}

			if (arrCoded != null) {
				for (i = 0; i < arrCoded.length; i++) {
					if (Integer.parseInt(arrCoded[i]) >= REQUIREMENT_SHOULD_BE_UPDATED) {
						j = ((Integer.parseInt(arrCoded[i]) / REQUIREMENT_SHOULD_BE_UPDATED) - 2) / 7;
						arrCodedValue[j] = "X";
					}
					else {
						k = Integer.parseInt(arrCoded[i]);
						if (k == 0) {
							j = 0;
						}
						else {
							j = (k - 2) / 7;
						}
						// j = (Integer.parseInt(arrCoded[i]) - 2) / 7;
						arrCodedValue[j] = "XX";
					}
				}
			}			
			if (arrTested != null) {
				for (i = 0; i < arrTested.length; i++) {
					if (Integer.parseInt(arrTested[i]) >= REQUIREMENT_SHOULD_BE_UPDATED) {
						j = ((Integer.parseInt(arrTested[i]) / REQUIREMENT_SHOULD_BE_UPDATED) - 3) / 7;
						arrTestedValue[j] = "X";
					}
					else {
						k = Integer.parseInt(arrTested[i]);
						if (k == 0) {
							j = 0;
						}
						else {
							j = (k - 3) / 7;
						}
						// j = (Integer.parseInt(arrTested[i]) - 3) / 7;
						arrTestedValue[j] = "XX";
					}
				}
			}
			if (arrDeployed != null) {
				for (i = 0; i < arrDeployed.length; i++) {
					if (Integer.parseInt(arrDeployed[i]) >= REQUIREMENT_SHOULD_BE_UPDATED) {
						j = ((Integer.parseInt(arrDeployed[i]) / REQUIREMENT_SHOULD_BE_UPDATED) - 4) / 7;
						arrDeployedValue[j] = "X";
					}
					else {
						k = Integer.parseInt(arrDeployed[i]);
						if (k == 0) {
							j = 0;
						}
						else {
							j = (k - 4) / 7;
						}
						//j = (Integer.parseInt(arrDeployed[i]) - 4) / 7;
						arrDeployedValue[j] = "XX";
					}
				}
			}
			if (arrAccepted != null) {
				for (i = 0; i < arrAccepted.length; i++) {
					if (Integer.parseInt(arrAccepted[i]) >= REQUIREMENT_SHOULD_BE_UPDATED) {
						j = ((Integer.parseInt(arrAccepted[i]) / REQUIREMENT_SHOULD_BE_UPDATED) - 5) / 7;
						arrAcceptedValue[j] = "X";
					}
					else {
						k = Integer.parseInt(arrAccepted[i]);
						if (k == 0) {
							j = 0;
						}
						else {
							j = (k - 5) / 7;
						}
						//j = (Integer.parseInt(arrAccepted[i]) - 5) / 7;
						arrAcceptedValue[j] = "XX";
					}
				}
			}
			if (arrCancelled != null) {
				for (i = 0; i < arrCancelled.length; i++) {
					if (Integer.parseInt(arrCancelled[i]) >= REQUIREMENT_SHOULD_BE_UPDATED) {
						j = ((Integer.parseInt(arrCancelled[i]) / REQUIREMENT_SHOULD_BE_UPDATED) - 6) / 7;
						arrCancelledValue[j] = "X";
					}
					else {
						k = Integer.parseInt(arrCancelled[i]);
						if (k == 0) {
							j = 0;
						}
						else {
							j = (k - 6) / 7;
						}
						//j = (Integer.parseInt(arrCancelled[i]) - 6) / 7;
						arrCancelledValue[j] = "XX";
					}
				}
			}						
			Date now = new Date();
			String currentDate;
			currentDate = CommonTools.dateFormat(now);
			if (arrRequirementId != null) {
				for (i = 0; i < noReq; i++) {
					if (arrRequirementId[i].length() > 0) {
						reqInfo.requirementID = Integer.parseInt(arrRequirementId[i]);
						RequirementInfo reqInfoOldOne =  Requirement.getRequirementById(reqInfo.requirementID);
						if (arrRequirementName[i].length() > 0) {
							reqInfo.name = arrRequirementName[i];
							if (arrCommittedValue[i].equalsIgnoreCase("X")) {
								reqInfo.committedDate = new java.sql.Date(dateForm.parse(currentDate).getTime());
							}
							else {
								if (arrCommittedValue[i].equalsIgnoreCase("XX")) {
									reqInfo.committedDate = reqInfoOldOne.committedDate;
								}
								else
									reqInfo.committedDate = null;
							}
							if (arrDesignedValue[i].equalsIgnoreCase("X")) {
								reqInfo.designedDate = new java.sql.Date(dateForm.parse(currentDate).getTime());
							}
							else {
								if (arrDesignedValue[i].equalsIgnoreCase("XX")) {
									reqInfo.designedDate = reqInfoOldOne.designedDate;
								}
								else
									reqInfo.designedDate = null;
							}
							if (arrCodedValue[i].equalsIgnoreCase("X")) {
								reqInfo.codedDate = new java.sql.Date(dateForm.parse(currentDate).getTime());
							}
							else {
								if (arrCodedValue[i].equalsIgnoreCase("XX")) {
									reqInfo.codedDate = reqInfoOldOne.codedDate;
								}
								else
									reqInfo.codedDate = null;
							}
							if (arrTestedValue[i].equalsIgnoreCase("X")) {
								reqInfo.testedDate = new java.sql.Date(dateForm.parse(currentDate).getTime());
							}
							else {
								if (arrTestedValue[i].equalsIgnoreCase("XX")) {
									reqInfo.testedDate = reqInfoOldOne.testedDate;
								}
								else
									reqInfo.testedDate = null;
							}
							if (arrDeployedValue[i].equalsIgnoreCase("X")) {
								reqInfo.deployedDate = new java.sql.Date(dateForm.parse(currentDate).getTime());
							}
							else {
								if (arrDeployedValue[i].equalsIgnoreCase("XX")) {
									reqInfo.deployedDate = reqInfoOldOne.deployedDate;
								}
								else
									reqInfo.deployedDate = null;
							}
							if (arrAcceptedValue[i].equalsIgnoreCase("X")) {
								reqInfo.acceptedDate = new java.sql.Date(dateForm.parse(currentDate).getTime());
							}
							else {
								if (arrAcceptedValue[i].equalsIgnoreCase("XX")) {
									reqInfo.acceptedDate = reqInfoOldOne.acceptedDate;
								}
								else
									reqInfo.acceptedDate = null;
							}
							if (arrCancelledValue[i].equalsIgnoreCase("X")) {
								reqInfo.cancelledDate = new java.sql.Date(dateForm.parse(currentDate).getTime());
							}
							else {
								if (arrCancelledValue[i].equalsIgnoreCase("XX")) {
									reqInfo.cancelledDate = reqInfoOldOne.cancelledDate;
								}
								else
									reqInfo.cancelledDate = null;
							}
							Requirement.setRequirementStatus(reqInfo);
						}
					}
				}
			}			
			requirementListCaller(request, response);						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void requirementBatchUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			RequirementBatchUpdate reqBatchUpdate = new RequirementBatchUpdate();
			reqBatchUpdate.setRequirementId(request.getParameterValues("requirementID"));
			reqBatchUpdate.setDeliverable(request.getParameterValues("deliverable"));
			reqBatchUpdate.setRequirementType(request.getParameterValues("type"));
			reqBatchUpdate.setRequirementSize(request.getParameterValues("size"));
			reqBatchUpdate.setRequirementSection(request.getParameterValues("srs"));
			reqBatchUpdate.setDesignSection(request.getParameterValues("dd"));
			reqBatchUpdate.setCodeModule(request.getParameterValues("codeModule"));
			reqBatchUpdate.setTestCaseSection(request.getParameterValues("testCase"));
			reqBatchUpdate.setReleaseNote(request.getParameterValues("releaseNote"));
			if (reqBatchUpdate.getRequirementId() != null){
				Requirement.requirementBatchUpdate(reqBatchUpdate);
			}
			requirementListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void getRCRForMilestone(final HttpServletRequest request, final HttpServletResponse response,long milestoneID) {
		final HttpSession session = request.getSession();
		Vector stageList = (Vector) session.getAttribute("stageList");
		StageInfo stageInf;
		int i = 0;
		for (; i < stageList.size(); i++) {
			stageInf = (StageInfo) stageList.elementAt(i);
			if (stageInf.milestoneID == milestoneID) {
				break;
			}
		}
		if (i>0)
			getBatchRePlan2(request,response,i);
		else
			getBatchPlan2(request,response);
	
	}
	
	public static final void getBatchPlan(final HttpServletRequest request, final HttpServletResponse response) {
		getBatchPlan2( request,response);
		Fms1Servlet.callPage("requirementDetailBatchPlan.jsp", request, response);
	}
	public static final void getBatchPlan2(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long pid = Long.parseLong((String) session.getAttribute("projectID"));
		final ProjectInfo pinf = Project.getProjectInfo(pid);
		Vector stageList=Schedule.getStageList(pinf);
		Vector planRCRList = Requirement.getPlanRCR(pinf, stageList);
		boolean oneIsClosed = false;
		StageInfo stageInf;
		for (int i = 0; i < stageList.size(); i++) {
			stageInf = (StageInfo) stageList.elementAt(i);
			if (stageInf.aEndD != null) {
				oneIsClosed = true;
				break;
			}
		}
		request.setAttribute("oneClosed", oneIsClosed ? "1" : "0");
		session.setAttribute("stageList", stageList);
		session.setAttribute("planRCRList", planRCRList);
		session.setAttribute("replan", "");
	}

	// Add by HaiMM
	public static final void getEstEffort(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long pid = Long.parseLong((String) session.getAttribute("projectID"));
		final ProjectInfo pinf = Project.getProjectInfo(pid);

		Vector stageList=Schedule.getStageList(pinf);
		Vector estEffortList = Requirement.getEstEffortPlan(pid, stageList);
		
		// get all Process including calculate Total - Start
		Vector processNameList = Requirement.getEstProcessName(pid);
		Vector estEffortTotalList = new Vector();
		
		EstEffortInfo estEffInf;
		String processName = "";
		double totalPd;
		double sumTotal = 0;
		
		for (int i = 0; i < processNameList.size(); i++) {
			EstEffortTotalInfo estEffTotalInf = new EstEffortTotalInfo();
			processName=(String)processNameList.elementAt(i);
			totalPd = 0;
			for (int j = 0; j < estEffortList.size(); j++) {
				estEffInf=(EstEffortInfo)estEffortList.elementAt(j);
				if (estEffInf.processName.equals(processName)) {
					totalPd += estEffInf.plannedValue;
				}
			}
			sumTotal += totalPd;
			estEffTotalInf.setProcessName(processName);
			estEffTotalInf.setTotalPd(totalPd);
			
			estEffortTotalList.add(estEffTotalInf);
		}
		// get all Process including calculate Total - End
		
		boolean oneIsClosed = false;
		StageInfo stageInf;
		for (int i = 0; i < stageList.size(); i++) {
			stageInf = (StageInfo) stageList.elementAt(i);
			if (stageInf.aEndD != null) {
				oneIsClosed = true;
				break;
			}
		}
		request.setAttribute("oneClosed", oneIsClosed ? "1" : "0");
		session.setAttribute("stageList", stageList);
		session.setAttribute("estEffortList", estEffortList);
//		session.setAttribute("processNameList", processNameList);
		session.setAttribute("estEffortTotalList", estEffortTotalList);
		session.setAttribute("replan", "");
		session.setAttribute("sumTotal", Double.toString(sumTotal));
		Fms1Servlet.callPage("estEffProcess.jsp", request, response);
	}
	


	public static final void getBatchRePlan(final HttpServletRequest request, final HttpServletResponse response) {
		getBatchRePlan2(request,response,-1);
		Fms1Servlet.callPage("requirementDetailBatchPlan.jsp",request,response);
	}
	public static final void getBatchRePlan2(final HttpServletRequest request, final HttpServletResponse response,int vectorID) {
		final HttpSession session = request.getSession();
		long pid=Long.parseLong((String)session.getAttribute("projectID"));
		ProjectInfo pinf = Project.getProjectInfo(pid);
		Vector stageList=(Vector)session.getAttribute("stageList");
		Vector planRCRList=Requirement.getRePlanRCR(pinf,stageList,vectorID);
		session.setAttribute("planRCRList",planRCRList);
		session.setAttribute("replan","1");
	}
	public static final void updateBatchPlan(final HttpServletRequest request, final HttpServletResponse response) {
			final HttpSession session = request.getSession();
			Vector planRCRList=(Vector)session.getAttribute("planRCRList");
			PlanRCRInfo inf;
			int k=0;
			String param;
			Vector plan=new Vector();
			for (k=0;k<planRCRList.size();k++){
				inf=(PlanRCRInfo)planRCRList.elementAt(k);
				param=request.getParameter("plan"+k);
				if (param !=null){
				    if ("".equals(param))
						inf.plannedValue=0;
					else
						inf.plannedValue=CommonTools.parseDouble(param);
					plan.add(inf)	;	
				}
				
			}
			Requirement.updatePlanRCR(plan); 
			//removing "update" parameter would be simple but don't know how to do
			request.setAttribute("updated","1");
		Fms1Servlet.callPage("requirementDetailBatchPlan.jsp",request,response);
	}
	
	// Add by HaiMM
	public static final void updateEstEffort(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		Vector estEffortList=(Vector)session.getAttribute("estEffortList");
		long pid=Long.parseLong((String)session.getAttribute("projectID"));
		EstEffortInfo inf;
		int k=0;
		String param;
		Vector plan=new Vector();
		for (k=0;k<estEffortList.size();k++){
			inf=(EstEffortInfo)estEffortList.elementAt(k);
			param=request.getParameter("plan"+k);
			if (param !=null){
				if ("".equals(param))
					inf.plannedValue=0;
				else
					inf.plannedValue=CommonTools.parseDouble(param);
					plan.add(inf);	
			}
		}
		Requirement.updateEstEffort(plan, pid);

		getEstEffort(request, response);
	}

	// Add by HaiMM
	public static final void estEffortAddCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long pid=Long.parseLong((String)session.getAttribute("projectID"));
		Vector stageList=Schedule.getStageList(pid);
		StageInfo stageInf;
		
		int k=0;
		String param;
		String[] process;
		Vector plan=new Vector();
		
		int maxRow = 10; // Fixed max Row (sync with maxRow of estEffProcessAdd.jsp)
		process = request.getParameterValues("process");		
		for (k=0;k<maxRow;k++){
			if (process[k]!= null && !process[k].equalsIgnoreCase("")) {
				for (int j=0;j<stageList.size();j++ ) {
					EstEffortInfo inf = new EstEffortInfo();
					inf.processName = process[k].trim();
					stageInf = (StageInfo)stageList.elementAt(j);
					inf.milestoneId = stageInf.milestoneID;
					param = request.getParameter("plan"+k+j);
					if (param !=null){
						if ("".equals(param))
							inf.plannedValue=0;
						else
							inf.plannedValue=CommonTools.parseDouble(param);
						
						plan.add(inf);
					}
				}
			}
		}
		
		Requirement.updateEstEffort(plan, pid);
		
		// Get data and forward to list page
		getEstEffort(request, response);

	}

	// Add by HaiMM
	public static final void deleteEstEffort(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		Vector estEffortList=(Vector)session.getAttribute("estEffortList");
		int elementIdx=Integer.parseInt((String)request.getParameter("elementIdx"));
		
		EstEffortInfo inf;
		inf = (EstEffortInfo)estEffortList.elementAt(elementIdx);
		if (inf != null) {
			boolean isSaved = Requirement.delEstEffort(inf.processName);
			if (isSaved) {
				getEstEffort(request, response);
			}
		} else {
			Fms1Servlet.callPage("error.jsp?error=Internal server error !",request,response);
			return;
		}
	}

	public static final void getRequirementPlan(final HttpServletRequest request, final HttpServletResponse response) {
	
			final HttpSession session = request.getSession();
			long pid=Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo pinf = Project.getProjectInfo(pid);
			Vector stageList=Schedule.getStageList(pinf);	
            if (stageList.size()==0){
                Fms1Servlet.callPage("error.jsp?error=Please define at least one stage to access this page",request,response);
                return;
            }
            
            Vector reqList= Requirement.getRequirementList(pinf);
			StageInfo stageInf=null;
			boolean oneIsClosed=false;
            int currStage=0;
			for (int i=0;i<stageList.size();i++){
				stageInf=(StageInfo)stageList.elementAt(i);
                currStage=i;
				if (stageInf.aEndD!=null){
					oneIsClosed=true;
					
				}
                else{
                    break;
                }
			}
            Vector processEffortByStages; 
            boolean hasBeenReplanned=Requirement.checkPlanRCR(stageInf.milestoneID);
        	
        	if (!hasBeenReplanned)
                processEffortByStages = Effort.getProcessEffortByStage(pinf,stageList,currStage-1);
            else						
            	processEffortByStages =  Effort.getProcessEffortByStage(pinf,stageList);
            
            Vector rcrProc=Requirement.getRCRByProcess(pinf,processEffortByStages,reqList, stageList,hasBeenReplanned);
            
			session.setAttribute("stageList",stageList);
			session.setAttribute("RCRByStageInfos",Requirement.getRCRByStage(pinf,stageList,reqList,rcrProc, hasBeenReplanned));
            session.setAttribute("RCRByProcessInfos",rcrProc);
			request.setAttribute("oneClosed",oneIsClosed?"1":"0");
			Fms1Servlet.callPage("requirementPlan.jsp",request,response);			
	}
    public static final void updateProcessPlan(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession();
        Vector rCRByProcessInfos = (Vector)session.getAttribute("RCRByProcessInfos");
        RCRByProcessInfo rCRByProcessInfo;
        for (int i = 0; i < rCRByProcessInfos.size(); i++) {
            rCRByProcessInfo = (RCRByProcessInfo)rCRByProcessInfos.elementAt(i);
            rCRByProcessInfo.plan = CommonTools.parseDouble(request.getParameter("plan" + i));
        }
        Effort.updateProcessRCRs(rCRByProcessInfos);
        getRequirementPlan(request, response);
    }
    
	public static final void estEffortAddPreCaller(
		final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final long projectId = Long.parseLong((String) session.getAttribute("projectID"));
			
			Vector stageList=Schedule.getStageList(projectId);


			session.setAttribute("stageList", stageList);
			
			Fms1Servlet.callPage("estEffProcessAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
