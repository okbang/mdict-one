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
 
 package com.fms1.common.group;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.infoclass.group.*;
import java.util.*;
import com.fms1.tools.*;
import com.fms1.web.Fms1Servlet;
/**
 * plan page logic
 * @author manu
 * 
 */
public class PlanCaller {
	public static final void loadPlanning(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String error) {
		try {
			final HttpSession session = request.getSession();
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			String strYear = request.getParameter("txtYear");
			String strVersion = request.getParameter("txtVersion");
			String section = request.getParameter("section");
			int year;
			float version = 0;
			if (strYear != null && strVersion != null) {
				//called after selecting combos
				year = Integer.parseInt(strYear);
				version = Float.parseFloat(strVersion);
			}
			else {
				Object temp = session.getAttribute("planningInfo");
				if (temp != null) {
					//reuse the parameters from the previous consultation
					PlanningInfo planningInfo = (PlanningInfo) temp;
					year = planningInfo.year;
					version = planningInfo.version;
				}
				else {
					//called first time from the menu
					Calendar cal = new GregorianCalendar();
					cal.setTime(new java.util.Date());
					year = cal.get(Calendar.YEAR);
				}
			}
			//get version 2 by default
			PlanningInfo planningInfo;
			if (version != 0)
				planningInfo = Plans.getPlanning(workUnitID, year, version, section);
			else {
				//get latest plan
				planningInfo = Plans.getPlanning(workUnitID, year, 1.1f, section);
				//if plan not found
				if (planningInfo.planningID == 0)
					planningInfo = Plans.getPlanning(workUnitID, year, 1f, section);
			}
			session.setAttribute("planningInfo", planningInfo);
			Fms1Servlet.callPage("Group/planning.jsp?error=" + error,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void savePlanning(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			PlanningInfo planningInfo = (PlanningInfo) session.getAttribute("planningInfo");
			planningInfo.year = Integer.parseInt(request.getParameter("txtYear"));
			planningInfo.version = Float.parseFloat(request.getParameter("txtVersion"));
			planningInfo.lastUpdate = new java.sql.Date(new java.util.Date().getTime());
			planningInfo.planningID =
				Plans.getPlanningID(planningInfo.workUnit, planningInfo.year, planningInfo.version);
			PlanningInfo.Row row;
			for (int i = 0; i < planningInfo.rows.size(); i++) {
				row = (PlanningInfo.Row) planningInfo.rows.elementAt(i);
				if (row.groupID != -2) {
					for (int j = 0; j < 12; j++) {
						row.values[j] =
							CommonTools.parseDouble(
								request.getParameter("val" + CommonTools.getMonth(j + 1) + Integer.toString(i)));
						row.assumption = request.getParameter("ass" + i);
					}
					row.yearTotal=CommonTools.parseDouble(request.getParameter("yeartotal" +i));
				}

			}
			String error =
				(Plans.savePlanning(planningInfo)) ? "Planning saved successfully" : "Error when saving plan";
			loadPlanning(request, response, error);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
