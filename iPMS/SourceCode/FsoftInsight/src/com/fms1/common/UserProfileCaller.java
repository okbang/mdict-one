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
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Parser;
import sun.awt.WindowClosingListener;

import com.fms1.infoclass.AssignmentInfo;
import com.fms1.infoclass.CustomerInfo;
import com.fms1.infoclass.RolesInfo;
import com.fms1.infoclass.UserInfo;
import com.fms1.tools.LanguageChoose;
import com.fms1.web.*;
import java.util.Date;
import com.fms1.tools.*;

/**
 * User profile pages
 *
 */
public final class UserProfileCaller implements Constants {
	public static final void doLoginProcess(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			String userID = request.getParameter("loginName");
			String pass = request.getParameter("loginPass");
			final UserInfo userInfo = UserHelper.getUserNoCaseSensitive(userID);
			if ((userInfo != null) && (pass.equals(userInfo.Password.trim()))) {
				session.setAttribute("logged", "yes");
				userInfo.setUserRoleLogin(UserHelper.getUserLoginRole(userInfo.role));
				session.setAttribute("UserLoginInfo", userInfo);
				final String strLang = request.getParameter("cbolang");
				LanguageChoose objLang = null;
				if ("EN".equalsIgnoreCase(strLang)){
					objLang = new LanguageChoose(LanguageChoose.ENGLISH);
				}
				else if ("JA".equals(strLang)){
					objLang = new LanguageChoose(LanguageChoose.JAPANESE);
				}
				else {
					objLang = new LanguageChoose(LanguageChoose.ENGLISH); 
				}
				session.setAttribute("LanguageChoose", objLang);
				Roles.setUserRoles(request, response);
			}
			else
				Fms1Servlet.callPage("login.jsp?error=Invalid user name or password",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doShowAllPeople(final HttpServletRequest request, final HttpServletResponse response) {
		doSearchPeople(request, response);
	}
	public static final void doSearchPeople(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String searchName = request.getParameter("searchName");
			final String searchRule = request.getParameter("searchRule");
			Vector vtUserInfo = UserHelper.getUsersProfile(searchRule, searchName);
			session.setAttribute("userList", vtUserInfo);
			final Vector vtGroup = WorkUnit.getWUByType(RIGHT_GROUP);
			session.setAttribute("grpVector", vtGroup);
			Fms1Servlet.callPage(
				"UserProfiles.jsp?iPage=1"
					+ ((searchRule == null) ? "" : "&searchName=" + searchName + "&searchRule=" + searchRule),request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doUserDetail(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final long developerID;			
			final HttpSession session = request.getSession();
			
			if (Integer.parseInt(request.getParameter("reqType"))== Constants.VIEW_USER_PROFILE){
				UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");							
				developerID = userLoginInfo.developerID;				 			
			}else							
				developerID = Long.parseLong(request.getParameter("id").toString()); //todo see jsp

			final UserInfo userInfo = UserHelper.getUser(developerID);
			final Vector wuVector = WorkUnit.getWUList(null);
			final Vector rightVector1 = Roles.getRightOfUser(developerID);
			final Vector rightVector = new Vector();
			if (rightVector1 != null) {
				//rightVector = new Vector();
				for (int i = 0; i < rightVector1.size(); i++) {
					final RolesInfo roleInfo = (RolesInfo) rightVector1.elementAt(i);
					roleInfo.workunitName = WorkUnit.getWorkUnitInfo(roleInfo.workUnitID).workUnitName;
					rightVector.addElement(roleInfo);
				}
			}
			final Vector rgVector = RightGroup.getRightGroupVector();
			final Vector vtGroup = WorkUnit.getWUByType(RIGHT_GROUP);

			session.setAttribute("grpVector", vtGroup);
			session.setAttribute("rgList", rgVector);
			session.setAttribute("userInfo", userInfo);
			session.setAttribute("rightList", rightVector);
			session.setAttribute("wuList", wuVector);
			
			if (Integer.parseInt(request.getParameter("reqType"))== Constants.VIEW_USER_PROFILE){		
			
				Fms1Servlet.callPage("UserProfileViewOnly.jsp",request,response);			
			}else			
				Fms1Servlet.callPage("UserProfileView.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doUpdateUserProfile(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			final long developerID = userInfo.developerID;
			final String userAccount = request.getParameter("userID").toString().toUpperCase();
			final String userPassword = request.getParameter("userPassword");
			final String userEmail = request.getParameter("userEmail");
			final Date userStartDate = CommonTools.parseSQLDate(request.getParameter("userStartDate"));
			final Date userQuitDate =  CommonTools.parseSQLDate(request.getParameter("userQuitDate"));  
			final String userName = request.getParameter("userName");
			final String userDesignation = request.getParameter("userDesignation");
			final String userGroup = request.getParameter("userGroup");
			final String userStatus = request.getParameter("userStatus");
			final String toolsRole = request.getParameter("toolsRole");
			final String[] wuArray = request.getParameterValues("workUnit");
			final String[] rightArray = request.getParameterValues("rightGroup");
			UserInfo newUserInfo = new UserInfo(developerID, userAccount, userPassword, userEmail, userStartDate, userQuitDate,userName, 0, //todo
	userDesignation, toolsRole, userStatus, userGroup);
	UserHelper.updateUser(newUserInfo);
			//begin update right
			Roles.deleteAllRightOfUser(developerID);
			int i;
			for (i = 0; i < wuArray.length; i++) {
				if ((wuArray[i] == "-1") || (rightArray[i].equals("-1")))
					continue;
				final RolesInfo rightInfo = new RolesInfo(developerID, Long.parseLong(wuArray[i]), rightArray[i], "T");
				Roles.addRightOfUserByWorkUnit(rightInfo);
			}
			doShowAllPeople(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddNewUserProfile(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final String userID = request.getParameter("userID").toString().toUpperCase();
			final String userPassword = request.getParameter("userPassword");
			final String userName = request.getParameter("userName");
			final String userEmail = request.getParameter("userEmail");
			final Date userStartDate = CommonTools.parseSQLDate(request.getParameter("userStartDate"));
			final String userDesignation = request.getParameter("userDesignation");
			final String userGroup = request.getParameter("userGroup");
			final String userStatus = request.getParameter("userStatus");
			final String toolsRole = request.getParameter("toolsRole");
			final String[] wuArray = request.getParameterValues("workUnit");
			final String[] rightArray = request.getParameterValues("rightGroup");
			final UserInfo newUserInfo =
				new UserInfo(
					0,
					userID,
					userPassword,
					userEmail,
					userStartDate,
					null,
					userName,
					0,
					((userDesignation == null) ? "" : userDesignation),
					toolsRole,
					((userStatus == null) ? "" : userStatus),
					userGroup);
			final long newID = UserHelper.addUser(newUserInfo);
			if (newID == 0) {
				Fms1Servlet.callPage("UserProfileAddNew.jsp?error=You have choosen an existing userID",request,response);
				return;
			}
			Roles.deleteAllRightOfUser(newID);
			int i;
			for (i = 0; i < wuArray.length; i++) {
				if ((wuArray[i] == "-1") || (rightArray[i].equals("-1")))
					continue;
				final RolesInfo rightInfo = new RolesInfo(newID, Long.parseLong(wuArray[i]), rightArray[i], "T");
				Roles.addRightOfUserByWorkUnit(rightInfo);
			}
			doShowAllPeople(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteUserProfile(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final long developerID = Long.parseLong(request.getParameter("deleteID"));
			Roles.deleteAllRightOfUser(developerID);
			UserHelper.deleteUser(developerID);
			doShowAllPeople(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doUserChangePassword(
			final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();		
				UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
				final String userID = userLoginInfo.account;
				final String oldpass = request.getParameter("OldPassword").toString();
				final String newpass = request.getParameter("NewPassword").toString();			
								
				final UserInfo userInfo =UserHelper.getUserNoCaseSensitive(userID);							    								
				if (oldpass.equals(userInfo.Password)){																					
					UserHelper.ChangePassword(newpass,userInfo.developerID);					
					userLoginInfo.Password = newpass;
					session.setAttribute("UserLoginInfo", userLoginInfo);		
					request.setAttribute("closeMe","1");																	
		}
				else{													 
					request.setAttribute("errorChangePass","error");					
				}													
				Fms1Servlet.callPage("UserProfileViewOnly.jsp",request,response);						
			}
		catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	public static final void doFilterUserAssigned(final HttpServletRequest request, final HttpServletResponse response){
		final HttpSession session = request.getSession();
		String strAccountName = request.getParameter("Account");
		String strType = request.getParameter("Type");// type of searching: by Name or by Account
		strAccountName = ConvertString.toStandardizeString(strAccountName);
		long lProjectID = Long.parseLong((String)session.getAttribute("projectID"));
		Vector vtAccount = new Vector();
		try{
			vtAccount = Assignments.getAllUserAssignment(lProjectID, strAccountName, strType, ConvertString.adSearch_);
			String html = "";
			for (int i =0; i < vtAccount.size(); i++){
				AssignmentInfo userInfo = (AssignmentInfo) vtAccount.get(i);
				html += userInfo.account + "###" + userInfo.account + " - " + userInfo.devName + "(" + userInfo.groupName + ")" + "|";
			}
			PrintWriter out = response.getWriter();
			out.write(html);
		}
		catch (Exception e){
			e.printStackTrace();
		}		
	}
	/**
	 * implement: get all users whose account like strAccount. After that convert all accounts into string and send this
	 * string to Ajax, then Ajax convert that string into array and write to a DIV tag --> view pop up in .jsp page,
	 * @param request
	 * @param response
	 */
	public static final void doFilterUser(final HttpServletRequest request, final HttpServletResponse response){
		String strAccountName = request.getParameter("Account");
		String strType = request.getParameter("Type");
		String strGroup = request.getParameter("Group");
		String strSignature = request.getParameter("Signature");
		strAccountName = ConvertString.toStandardizeString(strAccountName);
		Vector vtAccount = new Vector();
		try{
			if (strSignature != null){
				int iProjectType = Integer.parseInt(strSignature);
				final HttpSession session = request.getSession();
				final long lProjectID = Long.parseLong((String) session.getAttribute("projectID"));
				vtAccount = Project.getDevListForSign(iProjectType, lProjectID, strAccountName, strType);
			}
			else{
				vtAccount = UserHelper.getUserToFillter("", strType.trim(), strAccountName, ConvertString.adSearch_);
			}
			String html = "";
			for (int i = 0; i < vtAccount.size(); i++){
				UserInfo userInfo = (UserInfo) vtAccount.get(i);
				html += userInfo.account + "###" + userInfo.account + " - " + userInfo.Name + "(" + userInfo.group + ")" + "|";
			}			

			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();			
			out.write(html);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static final void doFilterCustomer(final HttpServletRequest request, final HttpServletResponse response){
		String strAccountName = request.getParameter("Account");
		String strType = request.getParameter("Type");
		strAccountName = ConvertString.toStandardizeString(strAccountName);
		Vector vtCustomer = new Vector();
		try{
			vtCustomer = UserHelper.getCustomerToFillter(strType.trim(), strAccountName, ConvertString.adSearch_);
			
			String html = "";
			for (int i = 0; i < vtCustomer.size(); i++){
				CustomerInfo customerInfo = (CustomerInfo) vtCustomer.get(i);
				html += customerInfo.standardName + "|";
			}			

			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();			
			out.write(html);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * do format input string in order form: Surname straight in a column, 
	 * Middle name in a column, last name in a column.
	 * @param userInfo
	 * @return String
	 */
	public static final String doGetViewAccount(UserInfo userInfo){
		String strResult = userInfo.account;
		int iLength = userInfo.account.length();
		switch (iLength){
			case 3: strResult += "      - ";
			case 4: strResult += "     - ";
				break;
			case 5: strResult += "    - ";
				break;
			case 6: strResult += "   - ";
				break;
			case 7: strResult += "  - ";
				break;
			default:
				strResult += " - ";
				break;
		}
		strResult += userInfo.Name + "(" + userInfo.group + ")" + "|";
		return strResult;
	}
	/**
	 * 
	 * @param strAccountName
	 * @param strGroupName
	 * @return
	 */
	public static UserInfo checkUserFilter(HttpServletRequest request, String strAccountName, String strGroupName){
		UserInfo userInfo = null;
		try{
			Vector vtUserInfo = UserHelper.getUserToFillter(strGroupName, "Account", strAccountName, ConvertString.adCheck);
			if (vtUserInfo != null && vtUserInfo.size() > 0){
				userInfo = (UserInfo)vtUserInfo.elementAt(0);
			}
			else{
				vtUserInfo = UserHelper.getUserToFillter(strGroupName, "Name", strAccountName, ConvertString.adCheck);
				if (vtUserInfo != null && vtUserInfo.size() > 0){
					userInfo = (UserInfo)vtUserInfo.elementAt(0);
				}
			}
			return userInfo;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return userInfo;
		}
		finally{
			if (request != null && userInfo == null){
				request.setAttribute(StringConstants.FILLTER_USER_ERROR, "Account Error!");
			}
		}
	}
	public static AssignmentInfo checkUserAssignment(final HttpServletRequest request, String strAccountName, long lProjectID){
		AssignmentInfo assInfo = null;
		try{
			Vector vtResult = Assignments.getAllUserAssignment(lProjectID, strAccountName, "Account", ConvertString.adCheck);
			if(vtResult != null && vtResult.size() > 0){
				assInfo = (AssignmentInfo)vtResult.elementAt(0);
			}
			else{
				vtResult = Assignments.getAllUserAssignment(lProjectID, strAccountName, "Name", ConvertString.adCheck);
				if(vtResult != null && vtResult.size() > 0){
					assInfo = (AssignmentInfo)vtResult.elementAt(0);
				}
			}
			return assInfo;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return assInfo;
		}
		finally{
			if (request != null && assInfo == null){
				request.setAttribute(StringConstants.FILLTER_USER_ERROR, "Account Error!");
			}
		}
	}
}
