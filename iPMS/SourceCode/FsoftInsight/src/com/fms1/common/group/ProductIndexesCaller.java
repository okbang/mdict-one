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
import com.fms1.infoclass.*;
import com.fms1.tools.ReportMonth;
import com.fms1.web.*;
import com.fms1.common.*;
import java.util.*;
/**
 * Product indexes page logic
 * @author Phuong
 */
public class ProductIndexesCaller {
	/**
	 * Calls detail index drill and forward to approriate page
	 */
	public static void doGetIndexDrill(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			int nMetricID = Integer.parseInt((String)request.getParameter("metricID"));
			
			if (nMetricID!=MetricDescInfo.DELIVERY_LIST) {
				HttpSession session = request.getSession();
				String strMonth = (String)request.getParameter("month");
				String strYear = (String)request.getParameter("year");
				ReportMonth actualMonth = (ReportMonth)session.getAttribute("reportMonth");
				if ((strMonth == null) || (strYear == null)) {
					if (actualMonth == null) {
						actualMonth = new ReportMonth();
					}
				}
				else {
					actualMonth = new ReportMonth(Integer.parseInt(strMonth), Integer.parseInt(strYear));
				}
	
				String strWorkUnitID = (String)request.getParameter("workUnitID");
				long nWorkUnitID;
				WorkUnitInfo workUnitInfo;
				if (strWorkUnitID == null) {
					// refresh, stay in the same level
					nWorkUnitID = Long.parseLong((String)session.getAttribute("workUnitID"));
					workUnitInfo = WorkUnit.getWorkUnitInfo(nWorkUnitID);
				}
				else {
					// cross-tab jumping
					nWorkUnitID = Long.parseLong(strWorkUnitID);
					workUnitInfo=WorkUnitCaller.setWorkUnitHome(request,nWorkUnitID);
				}
				int nType = Integer.parseInt((String)session.getAttribute("workUnitType"));
				
				if (nType != WorkUnitInfo.TYPE_ORGANIZATION) {
					Vector vtRightForPage = RightForPage.getRightForPage(
							WorkUnitCaller.getRightGroupID(request, workUnitInfo.parentWorkUnitID));
					String strPageName = (nType == WorkUnitInfo.TYPE_PROJECT) ? "Group product"
							: "Organization product";
					if (Security.securiCheck(strPageName, vtRightForPage) > 1) {
						session.setAttribute("parentWorkUnitID",
								String.valueOf(workUnitInfo.parentWorkUnitID));
					}
				}
	
				if (nType == WorkUnitInfo.TYPE_PROJECT) {
					// jumping to project level, open 'Summary metrics' page for project
					NormCaller.doGetNormTable(request, response);
				}
				else {
					MetricDescInfo metricDescInfo = Metrics.getMetricDesc(nMetricID);
					session.setAttribute("metricID", String.valueOf(nMetricID));
					session.setAttribute("metricDescInfo", metricDescInfo);
					session.setAttribute("reportMonth", actualMonth);
					if ((nMetricID == MetricDescInfo.IN_PROGRESS_PROJECTS)
							&& (nType == WorkUnitInfo.TYPE_GROUP)) {
						Vector vtInProgressProjects =
								ProductIndexes.getInProgressProjects(nWorkUnitID, actualMonth);
						session.setAttribute("inProgressProjectList", vtInProgressProjects);
						Fms1Servlet.callPage("Group/ProjectList.jsp",request,response);
					}
					else {
						Vector alIndexDrill =
								ProductIndexes.getIndexDrill(nWorkUnitID, actualMonth, nMetricID);
						session.setAttribute("indexDrill", alIndexDrill);
						Fms1Servlet.callPage("Group/ProductDetail.jsp",request,response);
					}
				}
			}
			else {
				request.getSession().setAttribute("listCustomer",Requirement.getCustomerList());
				String check = "0" ; 
				request.getSession().setAttribute("checkDelivery",check);
				Fms1Servlet.callPage("delilveryList.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls product indexes and forward to approriate page
	 */
	public static void doGetProductIndexes(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			String strMonth = (String)request.getParameter("month");
			String strYear = (String)request.getParameter("year");
			ReportMonth actualMonth = (ReportMonth)session.getAttribute("reportMonth");
			if ((strMonth == null) || (strYear == null)) {
				if (actualMonth == null) {
					actualMonth = new ReportMonth();
				}
			}
			else {
				actualMonth = new ReportMonth(Integer.parseInt(strMonth), Integer.parseInt(strYear));
			}
			long nWorkUnitID = Long.parseLong((String)session.getAttribute("workUnitID"));
			ArrayList alProdInd = ProductIndexes.getProductIndexes(nWorkUnitID, actualMonth);
			double dSCI = ProductIndexes.getSCI(alProdInd);
			session.setAttribute("productIndexes", alProdInd);
			session.setAttribute("SCI", String.valueOf(dSCI));
			session.setAttribute("reportMonth", actualMonth);
			Fms1Servlet.callPage("Group/Product.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
