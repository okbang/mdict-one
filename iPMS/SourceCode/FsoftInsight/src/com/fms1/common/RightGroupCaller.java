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
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.Fms1Servlet;
/**
 * Role pages
 * @author NgaHT
 */
public final class RightGroupCaller {
	public final static void doGetRightGroupList(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final Vector result = RightGroup.getRightGroupVector();
			final HttpSession session = request.getSession();
			session.setAttribute("getRightGroupVector", result);
			Fms1Servlet.callPage("rightGroup.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doGetRightForPage(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String id = ConvertString.toSql(request.getParameter("rightGroupID"), ConvertString.adText);
			final RightGroupInfor rgInfor = RightGroup.getRightGroup(id);
            final String id1 =  request.getParameter("rightGroupID");
			final Vector vt = RightForPage.getRightForPage(id1);
			session.setAttribute("getRightForPageVector", vt);
			session.setAttribute("getRightGroup", rgInfor);
			Fms1Servlet.callPage("rightGroupDetail.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrepAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final Vector result = Page.getPageVector();
			final HttpSession session = request.getSession();
			session.setAttribute("getPageVector", result);
			Fms1Servlet.callPage("rightGroupAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddnewRightGroup(final HttpServletRequest request, final HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			final String rightGroupID = ConvertString.toSql(request.getParameter("rightGroupName").trim(), ConvertString.adText);
			final String mngLevel = request.getParameter("mngLevel");
			String description = ConvertString.toSql(request.getParameter("description").trim(), ConvertString.adText);
			if (description == null)
				description = "N/A";
			final RightGroupInfor rgInfor = new RightGroupInfor(rightGroupID, mngLevel, description);
			//Insert right into RightGroup
			if (RightGroup.addRightGroup(rgInfor)) {
				Vector pageVector = (Vector) session.getAttribute("getPageVector");
				if (pageVector == null) {
					Fms1Servlet.callPage("rightGroupAdd.jsp?error=1",request,response);
					return;
				}
				int mode;
				//insert right into right for pages
				for (int i = 0; i < pageVector.size(); i++) {
					PageInfor pageInfor = (PageInfor) pageVector.elementAt(i);
					final String modeString =
						request.getParameter(ConvertString.replace(pageInfor.name.trim(), " ", "_"));
					if (modeString == null)
						continue;
					mode = Integer.parseInt(modeString);
					RightForPage.addnewRightForPage(rightGroupID, pageInfor.pageID, mode);
				}
				doGetRightGroupList(request, response);
			}
			else {
				Fms1Servlet.callPage("rightGroupAdd.jsp?error=1",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdateRightGroup(final HttpServletRequest request, final HttpServletResponse response) {
		try {

			HttpSession session = request.getSession();
			Vector pageVector = (Vector) session.getAttribute("getRightForPageVector");
			//update RightForPage
			final String rightGroupID = ConvertString.toSql(request.getParameter("rightGroupName"), ConvertString.adText).trim();
			for (int i = 0; i < pageVector.size(); i++) {
				RightForPageInfor rfpInfor = (RightForPageInfor) pageVector.elementAt(i);
				final String modeString =
					request.getParameter(ConvertString.replace(rfpInfor.pageName2.trim(), " ", "_"));
				if (modeString == null)
					continue;
				final int mode = Integer.parseInt(modeString);
                //System.out.print(rightGroupID +":" +  rfpInfor.pageID +":" + mode +"\n");
				RightForPage.uppdateRightForPage(rightGroupID, rfpInfor.pageID, mode);
			}
			//update RightGroup
			String description = ConvertString.toSql(request.getParameter("description").trim(), ConvertString.adText);
			final String mngLevel = request.getParameter("mngLevel");
			if (description == null)
				description = "N/A";
			final RightGroupInfor rgInfor = new RightGroupInfor(rightGroupID, mngLevel, description);
			//check update result
			if (RightGroup.updateRightGroup(rgInfor, rightGroupID)) {
				doGetRightGroupList(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteRightGroup(final HttpServletRequest request, final HttpServletResponse response) {
		final ConvertString util = new ConvertString();
		try {
			final String rightGroupID = util.toSql(request.getParameter("rightGroupName").trim(), util.adText);
			RightGroup.deleteRightGroup(rightGroupID);
			doGetRightGroupList(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}