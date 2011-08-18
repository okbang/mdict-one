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
import java.sql.Array;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.*;
import com.fms1.tools.*;
import com.fms1.web.*;
import com.fms1.infoclass.*;
/**
 * Finance pages
 */
public final class FinanCaller {
	public static final void doGetFinanList(final HttpServletRequest request, final HttpServletResponse response){
		doGetFinanList(request, response, "");
	}
	public static final void doGetFinanList(final HttpServletRequest request, final HttpServletResponse response, String index) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Integer.parseInt(prjIDstr);
			session.setAttribute("caller", String.valueOf(Constants.FINAN_CALLER));
			final Vector vtFinan = Project.getFinanList(prjID);
			session.setAttribute("finanVector", vtFinan);
			final Vector vtCost = Project.getCostList(prjID);
			session.setAttribute("costVector", vtCost);
			final CostTotalInfo costTotalInfo = Project.getCostTotal(prjID);
			session.setAttribute("costTotalInfo", costTotalInfo);
			Fms1Servlet.callPage("finan.jsp"+index,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doGetFinan(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String strFinanID = request.getParameter("finanID");
			if (strFinanID == null)
				return;
			final int trainID = Integer.parseInt(strFinanID);
			final FinancialInfo finanInfo = Project.getFinan(trainID);
			final HttpSession session = request.getSession();
			session.setAttribute("finanInfo", finanInfo);
			Fms1Servlet.callPage("finanDetails.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static final void doAddFinan(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			addFinan(request, response);
			doGetFinanList(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUppdateFinan(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			updateFinan(request, response);
			doGetFinanList(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteFinan(final HttpServletRequest request, final HttpServletResponse response) {
		deleteFinan(request, response);
		doGetFinanList(request, response);
	}
	public static final void deleteFinan(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String trainIDstr = request.getParameter("txtFinanID");
			final int trainID = Integer.parseInt(trainIDstr);
			final HttpSession session = request.getSession();
			Project.deleteFinan(trainID);
			final String prjID = (String) session.getAttribute("projectID");
			String vtIDstr = request.getParameter("vtIDstr");
			Vector finanVt = (Vector) session.getAttribute("finanVector");
			int vtID = Integer.parseInt(vtIDstr);
			FinancialInfo oldInfo = (FinancialInfo) finanVt.get(vtID);
			ProjectPlanCaller.addChangeAuto(
				Long.parseLong(prjID),
				Constants.ACTION_DELETE,
				Constants.PL_FINANCE,
				"Financial plan list>Item>" + oldInfo.item,
				null,
				null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doGetCost(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strCostID = request.getParameter("costID");
			if (strCostID == null)
				return;
			final int costID = Integer.parseInt(strCostID);
			final CostInfo costInfo = Project.getCost(costID);
			session.setAttribute("costInfo", costInfo);
			Fms1Servlet.callPage("costDetails.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	public static final void doAddCost(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String act = request.getParameter("txtActivity").trim();
			final String strType = request.getParameter("txtType").trim();
			final String strEffort = request.getParameter("txtEffort").trim();
			final String strCost = request.getParameter("txtCost").trim();

			final CostInfo costInfo = new CostInfo();
			costInfo.prjID = Integer.parseInt(prjID);
			costInfo.costID = 0;
			costInfo.act = act;
			costInfo.type = Integer.parseInt(strType);
			if (strEffort.compareTo("") == 0) {
				costInfo.effort = 0;
			}
			else {
				costInfo.effort = Double.parseDouble(strEffort);
			}
			if (strCost.compareTo("") == 0) {
				costInfo.cost = 0;
			}
			else {
				costInfo.cost = Double.parseDouble(strCost);
			}
			if (!Project.addCost(costInfo)) {
				//System.out.println("error when add Cost!");
				Fms1Servlet.callPage("costAdd.jsp",request,response);
			}
			else {
				ProjectPlanCaller.addChangeAuto(
					Long.parseLong(prjID),
					Constants.ACTION_ADD,
					Constants.PL_FINANCE,
					"Project cost>Activity>" + costInfo.act,
					null,
					null);
			}
			doGetFinanList(request, response,"");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUppdateCost(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String strCostID = request.getParameter("txtCostID").trim();
			final String act = request.getParameter("txtActivity").trim();
			final String strType = request.getParameter("txtType").trim();
			final String strEffort = request.getParameter("txtEffort").trim();
			final String strCost = request.getParameter("txtCost").trim();

			final CostInfo costInfo = new CostInfo();
			costInfo.prjID = Long.parseLong(prjID);
			costInfo.costID = Long.parseLong(strCostID);
			costInfo.act = act;
			costInfo.type = Integer.parseInt(strType);
			if (strEffort.compareTo("") == 0) {
				costInfo.effort = 0;
			}
			else if (costInfo.type == 0) {
				costInfo.effort = 0;
			}
			else {
				costInfo.effort = Double.parseDouble(strEffort);
			}
			if (strCost.compareTo("") == 0) {
				costInfo.cost = 0;
			}
			else {
				costInfo.cost = Double.parseDouble(strCost);
			}
			if (!Project.updateCost(costInfo)) {
				//System.out.println("error when do Cost update!");
				Fms1Servlet.callPage("costUpdate.jsp",request,response);
			}
			else {
				CostInfo oldInfo = (CostInfo) session.getAttribute("costInfo");
				String oldValue, newValue;

				oldValue = oldInfo.act;
				newValue = costInfo.act;
				if (!oldValue.equalsIgnoreCase(newValue))
					ProjectPlanCaller.addChangeAuto(
						Long.parseLong(prjID),
						Constants.ACTION_UPDATE,
						Constants.PL_FINANCE,
						"Project cost>Activity",
						newValue,
						oldValue);
			}
			doGetFinanList(request, response,"");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteCost(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");

			final String costIDstr = request.getParameter("txtCostID");
			final int costID = Integer.parseInt(costIDstr);
			if (!Project.deleteCost(costID)) {
				//System.out.println("FinanCaller.DeleteCost :Can't delete the Cost");
			}
			else {
				CostInfo oldInfo = (CostInfo) session.getAttribute("costInfo");
				ProjectPlanCaller.addChangeAuto(
					Long.parseLong(prjID),
					Constants.ACTION_DELETE,
					Constants.PL_FINANCE,
					"Project cost>Activity" + oldInfo.act,
					null,
					null);
			}

			doGetFinanList(request, response,"");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static final void addFinan(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String strDueD = request.getParameter("txtDueD").trim();
			final String strValue = request.getParameter("txtValue").trim();
			final String cond = request.getParameter("txtCondition").trim();
			final String item = request.getParameter("txtItem").trim();
			final String strActualD = request.getParameter("txtActualD").trim();
			final FinancialInfo finanInfo = new FinancialInfo();
			finanInfo.prjID = Integer.parseInt(prjID);
			finanInfo.finanID = 0;
			if (strValue.compareTo("") == 0) {
				finanInfo.value = 0;
			}
			else {
				finanInfo.value = Double.parseDouble(strValue);
			}
			finanInfo.condition = cond;
			finanInfo.item = item;
			finanInfo.type = 0;
			if (strDueD.compareTo("") == 0) {
				finanInfo.dueD = null;
			}
			else {
				finanInfo.dueD = CommonTools.parseSQLDate(strDueD);
			}
			if (strActualD.compareTo("") == 0) {
				finanInfo.actualD = null;
			}
			else {
				finanInfo.actualD = CommonTools.parseSQLDate(strActualD);
			}
			if (!Project.addFinan(finanInfo)) {
				//System.out.println("making error when add finan");
				Fms1Servlet.callPage("finanAdd.jsp",request,response);
			}
			else //Add successfully
				{
				ProjectPlanCaller.addChangeAuto(
					Long.parseLong(prjID),
					Constants.ACTION_ADD,
					Constants.PL_FINANCE,
					"Financial plan list>Item>" + finanInfo.item,
					null,
					null);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void updateFinan(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String strFinanID = request.getParameter("txtFinan").trim();
			final String strDueD = request.getParameter("txtDueD").trim(); ////////////
			final String strValue = request.getParameter("txtValue").trim(); ////////////
			final String cond = request.getParameter("txtCondition").trim();
			final String item = request.getParameter("txtItem").trim(); ////////////
			final String strActualD = request.getParameter("txtActualD").trim();

			String vtIDstr = request.getParameter("vtIDstr");
			Vector finanVt = (Vector) session.getAttribute("finanVector");
			int vtID = Integer.parseInt(vtIDstr);
			FinancialInfo oldInfo = (FinancialInfo) finanVt.get(vtID);

			final FinancialInfo finanInfo = new FinancialInfo();
			finanInfo.prjID = Integer.parseInt(prjID);
			finanInfo.finanID = Integer.parseInt(strFinanID);
			if (strValue.compareTo("") == 0) {
				finanInfo.value = 0;
			}
			else {
				finanInfo.value = Double.parseDouble(strValue);
			}
			finanInfo.condition = cond;
			finanInfo.item = item;
			finanInfo.type = 0;
			if (strDueD.compareTo("") == 0) {
				finanInfo.dueD = null;
			}
			else {
				finanInfo.dueD = CommonTools.parseSQLDate(strDueD);
			}
			if (strActualD.compareTo("") == 0) {
				finanInfo.actualD = null;
			}
			else {
				finanInfo.actualD = CommonTools.parseSQLDate(strActualD);
			}
			if (!Project.updateFinan(finanInfo)) {
				Fms1Servlet.callPage("finanUpdate.jsp",request,response);
			}
			else {
				String oldValue, newValue;
				//Compare Item
				if (!oldInfo.item.equalsIgnoreCase(finanInfo.item)) {
					oldValue = oldInfo.item;
					newValue = finanInfo.item;
					ProjectPlanCaller.addChangeAuto(
						Long.parseLong(prjID),
						Constants.ACTION_UPDATE,
						Constants.PL_FINANCE,
						"Financial plan list>Item",
						newValue,
						oldValue);
				}
				//Compare Due date
				if (!oldInfo.dueD.equals(finanInfo.dueD)) {
					oldValue = CommonTools.dateFormat(oldInfo.dueD);
					newValue = CommonTools.dateFormat(finanInfo.dueD);
					ProjectPlanCaller.addChangeAuto(
						Long.parseLong(prjID),
						Constants.ACTION_UPDATE,
						Constants.PL_FINANCE,
						"Financial plan list>Due date",
						newValue,
						oldValue);
				}
				//Compare amount (value)
				if (oldInfo.value != finanInfo.value) {
					oldValue = CommonTools.formatDouble(oldInfo.value);
					newValue = CommonTools.formatDouble(finanInfo.value);
					ProjectPlanCaller.addChangeAuto(
						Long.parseLong(prjID),
						Constants.ACTION_UPDATE,
						Constants.PL_FINANCE,
						"Financial plan list>Amount",
						newValue,
						oldValue);
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Add by HaiMM
	public static final void getFinance(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long pid = Long.parseLong((String) session.getAttribute("projectID"));
		final ProjectInfo pinf = Project.getProjectInfo(pid);

		Vector durationList = Finance.getDurationList(pinf);
		Vector financePlanList = Finance.getFinancePlan(pid, durationList);
		
		// get all Finance including calculate Total - Start
		Vector financeNameList = Finance.getPlanFinanceName(pid);
		Vector financePlanTotalList = new Vector();
		
		FinanceInfo financeInf;
		String financeName = "";
		double totalPd = 0;
		double sumTotal = 0;
		
		for (int i = 0; i < financeNameList.size(); i++) {
			FinanceTotalInfo fTotalInf = new FinanceTotalInfo();
			fTotalInf = (FinanceTotalInfo)financeNameList.elementAt(i);
			financeName=fTotalInf.financeName;
			totalPd = 0;
			for (int j = 0; j < financePlanList.size(); j++) {
				financeInf=(FinanceInfo)financePlanList.elementAt(j);
				if (financeInf.financeName.equals(financeName)) {
					if (!Double.isNaN(financeInf.plannedValue)) {
						totalPd += financeInf.plannedValue;
					}
				}
			}
			sumTotal += totalPd;
			fTotalInf.setTotalBg(totalPd);
			
			financePlanTotalList.add(fTotalInf);
		}
		// get all Process including calculate Total - End
		
		boolean oneIsClosed = false;
		
		request.setAttribute("oneClosed", oneIsClosed ? "1" : "0");
		session.setAttribute("durationList", durationList);
		session.setAttribute("financePlanList", financePlanList);
		session.setAttribute("financePlanTotalList", financePlanTotalList);
		session.setAttribute("replan", "");
		session.setAttribute("sumTotal", Double.toString(sumTotal));
		Fms1Servlet.callPage("financePlanView.jsp", request, response);
	}

	public static final void deleteFinancePlan(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		Vector financePlanList=(Vector)session.getAttribute("financePlanList");
		int elementIdx=Integer.parseInt((String)request.getParameter("elementIdx"));
	
		FinanceInfo inf;
		inf = (FinanceInfo)financePlanList.elementAt(elementIdx);
		if (inf != null) {
			boolean isSaved = Finance.delFinanceDB(inf.financeName + StringConstants.pattern + inf.note);
			if (isSaved) {
				getFinance(request, response);
			}
		} else {
			Fms1Servlet.callPage("error.jsp?error=Internal server error !",request,response);
			return;
		}
	}
	
	public static final void updateFinancePlan(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		Vector financePlanList=(Vector)session.getAttribute("financePlanList");
		long pid=Long.parseLong((String)session.getAttribute("projectID"));
		FinanceInfo inf;
		int k=0;
		String param;
		Vector plan=new Vector();
		for (k=0;k<financePlanList.size();k++){
			inf=(FinanceInfo)financePlanList.elementAt(k);
			String financeName = inf.financeName;
			inf.financeName = financeName + StringConstants.pattern + inf.note;
			param=request.getParameter("plan"+k);
			if (param !=null){
				if (param !=null && !"".equals(param)){
					inf.plannedValue=CommonTools.parseDouble(param);
					plan.add(inf);
				}
			}
		}
		Finance.updateFinancePlan(plan, pid);
		
		// Update financeName
		Vector financePlanTotalList=(Vector)session.getAttribute("financePlanTotalList");
		int i=0;
		String financeNameNote;
		Vector vFinaneNameNote=new Vector();
		String noteTmp = "";
		FinanceTotalInfo fInf;
		for (i=0;i<financePlanTotalList.size();i++){
			fInf=(FinanceTotalInfo)financePlanTotalList.elementAt(i);
			String financeName = fInf.financeName;
			if (request.getParameter("note"+i).equalsIgnoreCase("") || request.getParameter("note"+i) == null) {
				noteTmp = "N/A";	
			}
			else {
				noteTmp = request.getParameter("note"+i);
			}

			financeNameNote=financeName + StringConstants.pattern + noteTmp;
			vFinaneNameNote.add(financeNameNote);
		}

		Finance.updateFinanceNote(vFinaneNameNote, financePlanTotalList, pid);
		
		getFinance(request, response);
	}

	public static final void financeAddPreCaller(
		final HttpServletRequest request, final HttpServletResponse response)
		{
		try {
			final HttpSession session = request.getSession();
			final long projectId = Long.parseLong((String) session.getAttribute("projectID"));
		
			final ProjectInfo pinf = Project.getProjectInfo(projectId);

			Vector durationList = Finance.getDurationList(pinf);

			session.setAttribute("durationList", durationList);
		
			Fms1Servlet.callPage("financePlanAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void financePlanAdd(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long pid=Long.parseLong((String)session.getAttribute("projectID"));
		final ProjectInfo pinf = Project.getProjectInfo(pid);
		
		Vector durationList = Finance.getDurationList(pinf);
		DurationInfo dInf;
	
		int k=0;
		int maxRow = 10; // Fixed max Row (sync with maxRow of estEffProcessAdd.jsp)
		String param;
		String[] financeTmp;
		String[] note;
		String[] finance = new String[maxRow];
		Vector plan=new Vector();
	
		financeTmp = request.getParameterValues("process");
		note = request.getParameterValues("note");
		String noteTmp = "";
		
		for (k=0;k<maxRow;k++){
			if (financeTmp[k]!= null && !financeTmp[k].equalsIgnoreCase("")) {
			// Merge Finance & Note before save to DB:
				if (note[k].equalsIgnoreCase("") || note[k] == null) {
					noteTmp = "N/A";	
				}
				else {
					noteTmp = note[k];
				}
				finance[k] = financeTmp[k] + StringConstants.pattern + noteTmp;
				for (int j=0;j<durationList.size();j++ ) {
					FinanceInfo inf = new FinanceInfo();
					inf.financeName = finance[k].trim();
					dInf = (DurationInfo)durationList.elementAt(j);
					inf.week = dInf.week;
					inf.month = dInf.month;
					inf.year = dInf.year;
					param = request.getParameter("plan"+k+j);
					if (param !=null && !"".equals(param)){
						inf.plannedValue=CommonTools.parseDouble(param);
						plan.add(inf);
					}
				}
			}
		}
	
		Finance.updateFinancePlan(plan, pid);
	
		// Get data and forward to list page
		getFinance(request, response);

	}

}