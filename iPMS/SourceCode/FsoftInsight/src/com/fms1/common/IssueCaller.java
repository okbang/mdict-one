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
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import com.fms1.html.*;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.StringConstants;
/**
 * Issue pages
 */
public final class IssueCaller {
	public static final void issueListCaller2(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		final long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
		final Vector vtProcess = Project.getProcessList();
		Vector issueList = Issues.getIssueListByWorkUnit(workUnitID);
		session.setAttribute("vtProcess", vtProcess);
		session.setAttribute("issueList", issueList);
		session.setAttribute("issueUserList", UserHelper.getAllUsers());
	}
	public static final void issueListCaller(final HttpServletRequest request, final HttpServletResponse response) {
		issueListCaller2(request,response);
		Fms1Servlet.callPage("issue.jsp",request,response);
	}
	public static final void issueUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try{
			IssueInfo newIssue = parseIssue(request, response);
			UserInfo userInfo = new UserInfo();
			userInfo = UserProfileCaller.checkUserFilter(request, newIssue.owner, null);
			if (userInfo == null){
				request.setAttribute("IssueInfo", newIssue);
				Fms1Servlet.callPage("issueUpdate.jsp", request, response);
			}
			else{
				// avoid situation that when user type UserName but did not press Search button
				newIssue.owner = userInfo.account;
				Vector issueList = (Vector)session.getAttribute("issueList");
				int vtID = Integer.parseInt(request.getParameter("vtID"));
				IssueInfo issue = (IssueInfo)issueList.elementAt(vtID);
				newIssue.issueID = issue.issueID;
				Issues.updateIssue(newIssue);
				issueListCaller(request, response);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	public static final IssueInfo parseIssue(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		final IssueInfo newIssue = new IssueInfo();
		try {
			newIssue.description = request.getParameter("txtDescription");
			newIssue.statusID = Integer.parseInt(request.getParameter("cboStatus"));
			newIssue.priorityID=Integer.parseInt(request.getParameter("cboPriority"));
			newIssue.typeID= Integer.parseInt(request.getParameter("cboType"));
			newIssue.creator = request.getParameter("txtCreator");
			newIssue.owner = request.getParameter("strAccountName");
			
			newIssue.startDate = CommonTools.parseSQLDate(request.getParameter("txtStartDate"));
			newIssue.dueDate = CommonTools.parseSQLDate(request.getParameter("txtDueDate"));
			String clsdDate = request.getParameter("txtClosedDate");
			if ((clsdDate == null)|| ("".equals(clsdDate)))
				newIssue.closeDate = null;
			else
				newIssue.closeDate = CommonTools.parseSQLDate(request.getParameter("txtClosedDate"));
				
			newIssue.comment  = request.getParameter("txtComment");
						
			newIssue.reference = request.getParameter("txtReference").trim();
			
			newIssue.workUnitID= Long.parseLong((String)session.getAttribute("workUnitID"));
			newIssue.processId = Integer.parseInt(request.getParameter("cboProcess"));
			newIssue.wuID = WUCombo.parse(request,WUCombo.MODE_UPDATE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			return newIssue;
		}
	}
	public static final void issueAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final IssueInfo newIssue = parseIssue(request, response);
		UserInfo userInfo = UserProfileCaller.checkUserFilter(request, newIssue.owner, null);
		if (userInfo == null){
			request.setAttribute("IssueInfo", newIssue);
			Fms1Servlet.callPage("issueAddnew.jsp", request, response);
		}
		else{
			// avoid situation that when user type UserName but did not press checkName button
			newIssue.owner = userInfo.account;
			Issues.addIssue(newIssue);
			issueListCaller(request, response);
		}
	}
	public static final void issueAddPrepCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		//called from tasks
		if (session.getAttribute("issueUserList")==null)
			session.setAttribute("issueUserList", UserHelper.getAllUsers());
		if (session.getAttribute("vtProcess")==null)
			session.setAttribute("vtProcess", Project.getProcessList());	
		Fms1Servlet.callPage("issueAddnew.jsp",request,response);
	}
	public static final void issueDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector issueList = (Vector)session.getAttribute("issueList");
			int vtID=Integer.parseInt(request.getParameter("vtID"));
			IssueInfo issue= (IssueInfo)issueList.elementAt(vtID);
			Issues.deleteIssue(issue.issueID);
			issueListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doImportIssue(final HttpServletRequest request,final HttpServletResponse response) throws BiffException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
		
			try {
				FileItem formFileItem = processFormField(request, upload);
				InputStream inStream = formFileItem.getInputStream();
				readImportFile(request, inStream);
				IssueCaller.issueListCaller(request, response);
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
		
		try{
			Sheet sheet0 = workbook.getSheet(0);
			if(!sheet0.getCell(1, 66).getContents().trim().equalsIgnoreCase("Import Issues")){
				session.setAttribute("ImportFail","fail");
				return;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			session.setAttribute("ImportFail","fail");
			return;
		}
		
		Sheet sheet = workbook.getSheet(1);
		//Vector IssueList = new Vector();
		int[] added = new int[50];
		for(int i=0;i<50;i++){
			added[i] = -1;
		}
		int k = 0;
		UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
		String srtName = "";
		if (userLoginInfo != null){
			srtName = userLoginInfo.Name;
		}
		
		java.util.Date currentDate = new java.util.Date();
		String sDate = CommonTools.dateFormat(currentDate);
		
		for(int i=1;i<51;i++){
			IssueInfo issueInfo = new IssueInfo();
			NumberCell check = (NumberCell) sheet.getCell(0, i); 
			
			if(check.getValue() > 0 ){
				issueInfo.description = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(1, i).getContents());
				issueInfo.priorityID = Integer.parseInt(sheet.getCell(2, i).getContents()); 
				issueInfo.statusID = Integer.parseInt(sheet.getCell(3, i).getContents());
				issueInfo.typeID = Integer.parseInt(sheet.getCell(4, i).getContents());
				issueInfo.processId = Integer.parseInt(sheet.getCell(5, i).getContents());
				issueInfo.owner = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(6, i).getContents().toUpperCase());
				issueInfo.dueDate = CommonTools.parseSQLDate(sheet.getCell(7, i).getContents());
				issueInfo.comment = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(8, i).getContents());
				issueInfo.reference = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(9, i).getContents());
		    	
				issueInfo.workUnitID = Long.parseLong((String)session.getAttribute("workUnitID"));
				issueInfo.creator = srtName;
				issueInfo.startDate = CommonTools.parseSQLDate(sDate);
				issueInfo.wuID = WUCombo.parse(request,WUCombo.MODE_UPDATE);
				
				if(Issues.addIssue(issueInfo)==true){
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
}
