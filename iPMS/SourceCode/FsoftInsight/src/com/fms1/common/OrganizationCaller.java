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
import javax.servlet.http.*;
import com.fms1.tools.*;
import com.fms1.web.*;
import com.fms1.infoclass.*;
/**
 * Organization pages
 */
public final class OrganizationCaller {
	
	public static final void getOrganization(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long pid = Long.parseLong((String) session.getAttribute("projectID"));
		final ProjectInfo pinf = Project.getProjectInfo(pid);

		Vector durationList = Finance.getDurationList(pinf);
		
		Vector orgPlanList = Organization.getOrgPlan(pid, durationList);
		
		Vector teamList = Assignments.getWOTeamList(pid, null, null);
		
		Vector assAvailableList = Organization.getAssAvailable(pid, teamList); // New
	
	
		/* Comment by crazy CR :)
		// get all Organization including calculate Total - Start
		Vector orgPlanTotalList = new Vector();
		
		long assId = 0;
		OrgInfo orgInf;
		
		double totalPd = 0;
		double sumTotal = 0;
	
		for (int i=0;i<teamList.size();i++){
			AssignmentInfo assInf = new AssignmentInfo();
			assInf=(AssignmentInfo)teamList.elementAt(i);
			assId=assInf.assID;
			totalPd = 0;
			for (int j = 0; j < orgPlanList.size(); j++) {
				orgInf=(OrgInfo)orgPlanList.elementAt(j);
				if (orgInf.assID == assId) {
					if (!Double.isNaN(orgInf.plannedValue)) {
						totalPd += orgInf.plannedValue;
					}
				}
			}
			sumTotal += totalPd;
			assInf.total = totalPd*5.5/100; // follow Calendar Effor (eff = 5.5pd/week)
		
			orgPlanTotalList.add(assInf);
		}
		
		// New
		Vector orgAssAvailableList = new Vector();
		OrgAssAvailableInfo orgAssInf;
		sumTotal = 0;
		for (int k = 0; k < assAvailableList.size(); k++) {
			orgAssInf=(OrgAssAvailableInfo)assAvailableList.elementAt(k);
			totalPd = 0;
			for (int j = 0; j < orgPlanTotalList.size(); j++) {
				AssignmentInfo assInf = new AssignmentInfo();
				assInf=(AssignmentInfo)teamList.elementAt(j);
				if (orgAssInf.role.equals(assInf.roleID) && orgAssInf.devName.equals(assInf.devName)) {
					if (!Double.isNaN(assInf.total)) {
						totalPd += assInf.total*100/5.5;
					}
				}
			}
			sumTotal += totalPd;
			orgAssInf.total = totalPd*5.5/100;
			
			orgAssAvailableList.add(orgAssInf);
		}
		
		sumTotal = sumTotal*5.5/100;
		
		// get all Organization including calculate Total - End
		
		*/
		
		//	Add Total for CR
		Vector orgTotalList = Assignments.getOrgTotalValueList(pid);
		double sumTotal = 0;
		
		Vector orgAssAvailableList = new Vector();
		OrgAssAvailableInfo orgAssInf;
		OrgTotalInfo orgTotalInfo;
		sumTotal = 0;
		for (int k = 0; k < assAvailableList.size(); k++) {
			orgAssInf=(OrgAssAvailableInfo)assAvailableList.elementAt(k);
			for (int j = 0; j < orgTotalList.size(); j++) {
				orgTotalInfo = (OrgTotalInfo)orgTotalList.elementAt(j);
				if (orgTotalInfo.assID == orgAssInf.assID) {
					orgAssInf.total = orgTotalInfo.totalValue;
					break;
				}
			}
			sumTotal += Double.isNaN(orgAssInf.total) ? 0 : orgAssInf.total;
			
			orgAssAvailableList.add(orgAssInf);
		}
		
	
		boolean oneIsClosed = false;
	
		request.setAttribute("oneClosed", oneIsClosed ? "1" : "0");
		session.setAttribute("durationList", durationList);
		session.setAttribute("orgPlanList", orgPlanList);
//		session.setAttribute("orgPlanTotalList", orgPlanTotalList);
		session.setAttribute("orgAssAvailableList", orgAssAvailableList);
		session.setAttribute("replan", "");
		session.setAttribute("sumTotal", Double.toString(sumTotal));
		Fms1Servlet.callPage("orgPlanView.jsp", request, response);
	}
	
	public static final void getListDelivery(final HttpServletRequest request, final HttpServletResponse response) {
		try{
			String fromDate = (String)request.getParameter("dateSearch");
			String toDate = (String)request.getParameter("dateSearch1");
			
			String left = request.getParameter("leftCustomer");
			String right = request.getParameter("rightCustomer");
			request.getSession().setAttribute("leftCustomer",left);
			request.getSession().setAttribute("rightCustomer",right);
			request.getSession().setAttribute("dateCustomerList",fromDate);
			request.getSession().setAttribute("dateCustomerList1",toDate);
			request.getSession().setAttribute("checkDelivery","1");
			Vector data = (Vector) Requirement.getListDelivery(fromDate,toDate,right);
			request.getSession().setAttribute("deliveryList",data);
			Fms1Servlet.callPage("delilveryList.jsp", request, response);	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
		
	public static final void updateOrgPlan(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		Vector orgPlanList=(Vector)session.getAttribute("orgPlanList");
		long pid=Long.parseLong((String)session.getAttribute("projectID"));
		OrgInfo inf;
		int k=0;
		String param;
		Vector plan=new Vector();
		for (k=0;k<orgPlanList.size();k++){
			inf=(OrgInfo)orgPlanList.elementAt(k);
			param=request.getParameter("plan"+k);
			if (param !=null && !"".equals(param)){
				inf.plannedValue=CommonTools.parseDouble(param);
				plan.add(inf);
			}
		}
		Organization.deleteOrgPlan(pid);
		Organization.updateOrgPlan(plan, pid);
		
		// Add for crazy CR :)
		Vector orgAssAvailableList=(Vector)session.getAttribute("orgAssAvailableList");
		OrgAssAvailableInfo orgTotalInfo;
		int i=0;
		String totalParam;
		Vector totalPlanList = new Vector();
		
		for (i=0;i<orgAssAvailableList.size();i++){
			orgTotalInfo=(OrgAssAvailableInfo)orgAssAvailableList.elementAt(i);
			totalParam=request.getParameter("totalPlan"+i);
			if (totalParam !=null && !"".equals(totalParam)){
				orgTotalInfo.total=CommonTools.parseDouble(totalParam);
				totalPlanList.add(orgTotalInfo);
			}
		}
		
		Organization.deleteTotalPlan(pid);
		Organization.insertTotalPlan(totalPlanList, pid);
		
		// Return list page
		getOrganization(request, response);
	}
	
}