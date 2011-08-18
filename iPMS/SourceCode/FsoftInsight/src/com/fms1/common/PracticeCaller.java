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
import com.fms1.web.Language;
/**
 * Practice and lesson pages
 *
 */
public final class PracticeCaller {
	public static final void doGetPracticeList(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			final Vector vtAss = Assets.getPracticeList(prjID);
			session.setAttribute("practiceVector", vtAss);
			Vector vtWP = WorkProduct.getProcessList();
			if (vtWP == null) {
				vtWP = new Vector();
			}
			session.setAttribute("processVector", vtWP);
			Fms1Servlet.callPage("practice.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doGetPractice(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int vtID = -1;
			vtID = Integer.parseInt(request.getParameter("vtID"));
			if (vtID == -1)
				return;
			final Vector vt = (Vector) session.getAttribute("practiceVector");
			final PracticeInfo pracInfo = (PracticeInfo) vt.get(vtID);
			session.setAttribute("practiceInfo", pracInfo);
			Fms1Servlet.callPage("practiceDetails.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddPractice(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			String practice = request.getParameter("txtPractice").trim();
			String scenario = request.getParameter("txtScenario").trim();
			final String category = request.getParameter("cmbCategory").trim();
			final String type = request.getParameter("cmbType").trim();
			final PracticeInfo pracInfo = new PracticeInfo();
			if (scenario == null || scenario.compareTo("") == 0) {
				scenario = "N/A";
			}
			if (practice == null || practice.compareTo("") == 0) {
				practice = "N/A";
			}
			pracInfo.projectId = Integer.parseInt(prjID);
			pracInfo.practiceId = 0; //Integer.parseInt(practID);
			pracInfo.scenario = scenario;
			pracInfo.practice = practice;
			pracInfo.category = category;
			pracInfo.type = Integer.parseInt(type);
			if (Assets.addPractice(pracInfo)) {
				doGetPracticeList(request, response);
				return;
			}
			else {
				final Language lang = (Language) session.getAttribute("lang");
				session.setAttribute("practiceError", lang.errAddPractice);
				Fms1Servlet.callPage("practiceAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUppdatePractice(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();

			String practice = request.getParameter("txtPractice");
			String scenario = request.getParameter("txtScenario");
			final String category = request.getParameter("cmbCategory");
			final String type = request.getParameter("cmbType");
			final PracticeInfo pracInfo = (PracticeInfo) session.getAttribute("practiceInfo");
			if (practice == null || scenario == null || category == null || type == null) {
				return;
			}
			if (scenario.compareTo("") == 0) {
				scenario = "N/A";
			}
			if (practice.compareTo("") == 0) {
				practice = "N/A";
			}
			pracInfo.scenario = scenario.trim();
			pracInfo.practice = practice.trim();
			pracInfo.type = Integer.parseInt(type);
			pracInfo.category = category.trim();
			if (Assets.updatePractice(pracInfo)) {
				doGetPracticeList(request, response);
				return;
			}
			else {
				Fms1Servlet.callPage("practiceUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeletePractice(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final PracticeInfo pracInfo = (PracticeInfo) session.getAttribute("practiceInfo");
			if (pracInfo != null) {
				if (!Assets.deletePractice(pracInfo.practiceId)) {}
					//System.out.println("Can't delete practice");
			}
//			else
//				System.out.println("Delete must be called from lessons.view mode");
			doGetPracticeList(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}