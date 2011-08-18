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
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fms1.infoclass.*;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.web.Security;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.StringConstants;
import com.fms1.html.ResponsibilityCbo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

/**
 * Work order pages.
 *
 */

public final class WorkOrderCaller {
	public static final void doLoadDeliverableList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			Vector deliverableList = WorkProduct.getDeliverableList(prjID);
			Vector moduleVector =
				WorkProduct.getRemainingDeliverableList(prjID);
			if (moduleVector == null) {
				moduleVector = new Vector();
			}
			//landd start			
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);

			session.setAttribute("caller", String.valueOf(Constants.WO_CALLER));
			final Vector stageVt = Schedule.getStageList(prjID);
			session.setAttribute("stageVector", stageVt);

			final ProjectInfo prjInfo = Project.getProjectInfo(prjID);
			MetricInfo durationMetric = prjInfo.getDurationMetric();
			session.setAttribute("durationMetric", durationMetric);
			// landd end

			session.setAttribute("woModuleList", moduleVector);
			session.setAttribute("deliverableList", deliverableList);
			Fms1Servlet.callPage("woDeliverable.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareDeliverableBatchUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);

			Vector deliverableList =
				(Vector) session.getAttribute("deliverableList");
			String listUpdate = (String) request.getParameter("listUpdate");

			Vector vUpdate = new Vector();
			final StringTokenizer strDeliveryUpdateList =
				new StringTokenizer(listUpdate == null ? "" : listUpdate, ",");

			while (strDeliveryUpdateList.hasMoreElements()) {
				ModuleInfo info =
					(ModuleInfo) deliverableList.elementAt(
						Integer.parseInt(strDeliveryUpdateList.nextToken()));
				vUpdate.addElement(info);
			}

			session.setAttribute("DeliveryBatchUpdateList", vUpdate);
			Fms1Servlet.callPage(
				"woDeliveryBatchUpdate.jsp",
				request,
				response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareDeliverableUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			Fms1Servlet.callPage(
				"woDeliverableUpdate.jsp?vtid=" + request.getParameter("vtid"),
				request,
				response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareDeliverableBatchAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			Fms1Servlet.callPage("woDeliveryBatchAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareDeliverableAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			Fms1Servlet.callPage("woDeliveryAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doBatchUpdateDeliverable(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int size = 0;
			boolean updateStatus = true;
			boolean isError = false;
			Vector vNewDeliver = new Vector();
			Vector vErrDeliver = new Vector();

			ModuleInfo newModuleInfo = null;
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			final int reqType =
				Integer.parseInt(request.getParameter("reqType"));

			String[] deliverable_ID =
				request.getParameterValues("deliverable_ID");

			//String[] deliverable_location = request.getParameterValues("deliverable_location");
			String[] deliverable_note =
				request.getParameterValues("deliverable_note");
			String[] txtCommitedDate =
				request.getParameterValues("txtCommitedDate");
			String[] txtReCommitedDate =
				request.getParameterValues("txtReCommitedDate");
			String[] txtActualDate =
				request.getParameterValues("txtActualDate");
			String[] cmbStatus = request.getParameterValues("cmbStatus");
			String nRow = request.getParameter("vRow");
			if (nRow != null)
				size = Integer.parseInt(nRow);
			else
				size = deliverable_ID.length;

			for (int i = 0; i < size; i++) {
				newModuleInfo = new ModuleInfo();

				final long moduleID = Long.parseLong(deliverable_ID[i]);
				newModuleInfo.moduleID = moduleID;
				// Comment by HaiMM
				// newModuleInfo.deliveryLocation =  deliverable_location[i];
				newModuleInfo.deliveryLocation = "";
				newModuleInfo.note = deliverable_note[i];
				newModuleInfo.plannedReleaseDate =
					CommonTools.parseSQLDate(txtCommitedDate[i]);
				newModuleInfo.rePlannedReleaseDate =
					CommonTools.parseSQLDate(txtReCommitedDate[i]);
				newModuleInfo.actualReleaseDate =
					CommonTools.parseSQLDate(txtActualDate[i]);
				newModuleInfo.status = Integer.parseInt(cmbStatus[i]);
				newModuleInfo.isDeliverable = 1;
				updateStatus = WorkProduct.updateDeliverable(newModuleInfo);
				if (!updateStatus) {
					isError = true;
					vErrDeliver.addElement(newModuleInfo);
				}
				if (reqType == Constants.WO_DELIVERABLE_BATCH_UPDATE) {
					if (updateStatus)
						addChangeAuto(
							prjID,
							Constants.ACTION_UPDATE,
							Constants.WO_DELIVERABLE,
							"Delivery Location",
							newModuleInfo.deliveryLocation,
							"");
				} else if (reqType == Constants.WO_DELIVERABLE_BATCH_ADD) {
					if (updateStatus)
						addChangeAuto(
							prjID,
							Constants.ACTION_ADD,
							Constants.WO_DELIVERABLE,
							newModuleInfo.name,
							null,
							null);
				}

			}

			if (!isError) {
				doLoadDeliverableList(request, response);
			} else {
				request.setAttribute("ErrDeliveryBatchList", vErrDeliver);
				request.setAttribute("ErrType", "1");
				if (reqType == Constants.WO_DELIVERABLE_BATCH_UPDATE) {
					Fms1Servlet.callPage(
						"woDeliveryBatchUpdate.jsp",
						request,
						response);
				} else if (reqType == Constants.WO_DELIVERABLE_BATCH_ADD) {
					Fms1Servlet.callPage(
						"woDeliveryBatchAdd.jsp",
						request,
						response);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdateDeliverable(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final ModuleInfo moduleInfo = new ModuleInfo();
			final long moduleID =
				Long.parseLong(request.getParameter("deliverable_ID"));
			moduleInfo.moduleID = moduleID;
			// Comment by HaiMM
			// moduleInfo.deliveryLocation =  request.getParameter("deliverable_location");
			moduleInfo.deliveryLocation = "";
			moduleInfo.note = request.getParameter("deliverable_note");
			moduleInfo.plannedReleaseDate =
				CommonTools.parseSQLDate(
					request.getParameter("txtCommitedDate"));
			moduleInfo.rePlannedReleaseDate =
				CommonTools.parseSQLDate(
					request.getParameter("txtReCommitedDate"));
			moduleInfo.actualReleaseDate =
				CommonTools.parseSQLDate(request.getParameter("txtActualDate"));
			moduleInfo.status =
				Integer.parseInt(request.getParameter("cmbStatus"));
			moduleInfo.isDeliverable = 1;
			WorkProduct.updateDeliverable(moduleInfo);
			//------------ monitor Update Deliverable ----------
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			final int reqType =
				Integer.parseInt(request.getParameter("reqType"));
			if (reqType == Constants.WO_DELIVERABLE_UPDATE) {
				Vector deliverableList =
					(Vector) session.getAttribute("deliverableList");
				ModuleInfo oldInfo =
					(ModuleInfo) deliverableList.elementAt(
						Integer.parseInt(request.getParameter("vtid")));
				// deliveryLocation
				String oldValue = oldInfo.deliveryLocation;
				if (oldValue == null)
					oldValue = "";
				String newValue = moduleInfo.deliveryLocation;
				if (!oldValue.equalsIgnoreCase(newValue)) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_DELIVERABLE,
						"Delivery Location",
						newValue,
						oldValue);
				}
			} else if (reqType == Constants.WO_DELIVERABLE_ADD) {
				Vector moduleList =
					(Vector) session.getAttribute("woModuleList");
				for (int i = 0; i < moduleList.size(); i++) {
					ModuleInfo info = (ModuleInfo) moduleList.elementAt(i);
					if (info.moduleID == moduleID) {
						addChangeAuto(
							prjID,
							Constants.ACTION_ADD,
							Constants.WO_DELIVERABLE,
							info.name,
							null,
							null);
						break;
					}
				}
			}
			doLoadDeliverableList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doBatchDeleteDeliverable(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {

			final HttpSession session = request.getSession();
			int vtModuleID = 0;
			int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			String listUpdate = (String) request.getParameter("listUpdate");
			Vector deliverableList =
				(Vector) session.getAttribute("deliverableList");

			final StringTokenizer strModuleDeleteIDList =
				new StringTokenizer(listUpdate == null ? "" : listUpdate, ",");
			while (strModuleDeleteIDList.hasMoreElements()) {
				vtModuleID =
					Integer.parseInt(strModuleDeleteIDList.nextToken());
				ModuleInfo moduleInfoOld =
					(ModuleInfo) deliverableList.elementAt(vtModuleID);
				final ModuleInfo moduleInfo = new ModuleInfo();
				moduleInfo.moduleID = moduleInfoOld.moduleID;
				//check for requirements referencing the deliverable
				//lifecycle doesn't matter
				Vector reqlist =
					Requirement.getRequirementListByDeliverable(
						Project.getProjectInfo(prjID),
						moduleInfo.moduleID);
				if (reqlist.size() > 0) {
					String errMessage =
						"&error= Can't delete the deliverable because some requirement(s) references it,<br>please update requirement(s) below first:<br>";
					for (int i = 0; i < reqlist.size(); i++) {
						RequirementInfo info =
							(RequirementInfo) reqlist.elementAt(i);
						errMessage += "- " + info.name + "<br>";
					}
					Fms1Servlet.callPage(
						"woDeliverableUpdate.jsp?vtid="
							+ vtModuleID
							+ errMessage,
						request,
						response);
					return;
				}
				moduleInfo.deliveryLocation = null;
				moduleInfo.plannedReleaseDate =
					moduleInfoOld.plannedReleaseDate;
				moduleInfo.rePlannedReleaseDate =
					moduleInfoOld.rePlannedReleaseDate;
				moduleInfo.actualReleaseDate = moduleInfoOld.actualReleaseDate;
				moduleInfo.status = 0;
				moduleInfo.note = null;
				moduleInfo.isDeliverable = 0;
				WorkProduct.updateDeliverable(moduleInfo);
				addChangeAuto(
					prjID,
					Constants.ACTION_DELETE,
					Constants.WO_DELIVERABLE,
					moduleInfoOld.name,
					null,
					null);

			}
			doLoadDeliverableList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteDeliverable(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			Vector deliverableList =
				(Vector) session.getAttribute("deliverableList");
			String vtid = request.getParameter("vtid");
			ModuleInfo moduleInfoOld =
				(ModuleInfo) deliverableList.elementAt(Integer.parseInt(vtid));
			final ModuleInfo moduleInfo = new ModuleInfo();
			moduleInfo.moduleID = moduleInfoOld.moduleID;
			//check for requirements referencing the deliverable
			//lifecycle doesn't matter
			Vector reqlist =
				Requirement.getRequirementListByDeliverable(
					Project.getProjectInfo(prjID),
					moduleInfo.moduleID);
			if (reqlist.size() > 0) {
				String errMessage =
					"&error= Can't delete the deliverable because some requirement(s) references it,<br>please update requirement(s) below first:<br>";
				for (int i = 0; i < reqlist.size(); i++) {
					RequirementInfo info =
						(RequirementInfo) reqlist.elementAt(i);
					errMessage += "- " + info.name + "<br>";
				}
				Fms1Servlet.callPage(
					"woDeliverableUpdate.jsp?vtid=" + vtid + errMessage,
					request,
					response);
				return;
			}
			moduleInfo.deliveryLocation = null;
			moduleInfo.plannedReleaseDate = moduleInfoOld.plannedReleaseDate;
			moduleInfo.rePlannedReleaseDate =
				moduleInfoOld.rePlannedReleaseDate;
			moduleInfo.actualReleaseDate = moduleInfoOld.actualReleaseDate;
			moduleInfo.status = 0;
			moduleInfo.note = null;
			moduleInfo.isDeliverable = 0;
			WorkProduct.updateDeliverable(moduleInfo);
			addChangeAuto(
				prjID,
				Constants.ACTION_DELETE,
				Constants.WO_DELIVERABLE,
				moduleInfoOld.name,
				null,
				null);
			doLoadDeliverableList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAutoUpdateCustomerMetric(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {
			final HttpSession session = request.getSession();
			long prjID =Long.parseLong((String) session.getAttribute("projectID"));	
			long mileStoneID;
			String stStageID=(String)request.getParameter("stageID");
			if ((String) request.getParameter("stageID") == null) {
				mileStoneID =((StageInfo) session.getAttribute("StageInfo")).milestoneID;
			} else {
				mileStoneID =
					Long.parseLong((String) request.getParameter("stageID"));
			}
			StageInfo stageInfo = Schedule.getStageByID(mileStoneID);
			Vector cusMetric =(Vector) Project.getCustomerMetric(prjID, mileStoneID);
			// reset current stage	
			session.setAttribute("StageInfo", stageInfo);
			session.setAttribute("WOCustomeMetricList", cusMetric);
			String sourcePage = (String)session.getAttribute("SourcePage");
			if (sourcePage != null) {
				if (sourcePage.equalsIgnoreCase("0")) {
					Fms1Servlet.callPage(
						"woPerformanceView.jsp" + index,
						request,
						response);

				} else {
					Fms1Servlet.callPage(
						"qualityObjective.jsp" + index,
						request,
						response);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static final void doAutoUpdateDefect(
			final HttpServletRequest request,
			final HttpServletResponse response
			) {
			try {
				final HttpSession session = request.getSession();
				long prjID =Long.parseLong((String) session.getAttribute("projectID"));	
				long mileStoneID;
				String stStageID=(String)request.getParameter("stageID_DP");
				if ((String) request.getParameter("stageID_DP") == null) {
					mileStoneID =
						((StageInfo) session.getAttribute("StageInfoDP")).milestoneID;
				} else {
					mileStoneID =
						Long.parseLong((String) request.getParameter("stageID_DP"));
				}
				StageInfo stageInfo = Schedule.getStageByID(mileStoneID);
				
				Vector defectList= Defect.getDPTask(prjID,mileStoneID);	
				// reset current stage	
				session.setAttribute("StageInfoDP", stageInfo);
				session.setAttribute("defectPrevention", defectList);
				String sourcePage = (String)session.getAttribute("SourcePage");
				if (sourcePage != null) {
					if (sourcePage.equalsIgnoreCase("0")) {
						Fms1Servlet.callPage(
							"woPerformanceView.jsp",
							request,
							response);

					} else {
						Fms1Servlet.callPage(
							"qualityObjective.jsp",
							request,
							response);
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	// anhtv08-start
	//date:14/07/2009
	public static void doUpdateWODefectMetric( final HttpServletRequest request,
											   final HttpServletResponse response,
											   String index)
	{
			try {
				final HttpSession session = request.getSession();
				long prjID =Long.parseLong((String) session.getAttribute("projectID"));	
				long mileStoneID;
				String stStageID=(String)request.getParameter("stageID");
				if ((String) request.getParameter("stageID") == null) {
					mileStoneID =
						((StageInfo) session.getAttribute("StageInfo")).milestoneID;
				} else {
					mileStoneID =
						Long.parseLong((String) request.getParameter("stageID"));
				}
				StageInfo stageInfo = Schedule.getStageByID(mileStoneID);
				Vector cusMetric =
					(Vector) Project.getCustomerMetric(prjID, mileStoneID);
					
				// reset current stage	
				session.setAttribute("StageInfo", stageInfo);
				session.setAttribute("WOCustomeMetricList", cusMetric);
				String sourcePage = (String)session.getAttribute("SourcePage");
				if (sourcePage != null) {
					if (sourcePage.equalsIgnoreCase("0")) {
						Fms1Servlet.callPage(
							"woPerformanceView.jsp" + index,
							request,
							response);
	
					} else {
						Fms1Servlet.callPage(
							"qualityObjective.jsp" + index,
							request,
							response);
					}
				}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	//end
	public static final void doAddWOCustomeMetric(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final String name = request.getParameter("woCM_name");
			final String unit = request.getParameter("woCM_unit");
			//int mileStoneID = Integer.parseInt(request.getParameter("stageID"));
			String strPlannedValue = request.getParameter("woCM_plannedValue");
			String strUCL = request.getParameter("woCM_UCL");
			String strLCL = request.getParameter("woCM_LCL");
			
			final double plannedValue =
				CommonTools.parseDouble(strPlannedValue);
			// Modify by HaiMM - Start
			final double UCL =
				strUCL.equalsIgnoreCase("")
					? plannedValue
					: CommonTools.parseDouble(strUCL);
			final double LCL =
				strLCL.equalsIgnoreCase("")
					? plannedValue
					: CommonTools.parseDouble(strLCL);
			// Modify by HaiMM - End			
			
			String note = request.getParameter("woCM_note");
			// set current stage
			//StageInfo stageInfo = Schedule.getStageByID(mileStoneID);
			//session.setAttribute("StageInfo", stageInfo);
			if (note == null) {
				note = "";
			}
			// Monitor add new CustomerMetric
			addChangeAuto(
				(int) prjID,
				Constants.ACTION_ADD,
				Constants.WO_METRICS_CUSTOM_METRIC,
				name,
				null,
				null);
			final WOCustomeMetricInfo woCMInfo =
				new WOCustomeMetricInfo(
					0,
					prjID,
					name,
					unit,
					plannedValue,
					LCL,
					UCL,
					note);
			Project.addCM(woCMInfo);

			String source = "";
			if (request.getParameter("source") != null) {
				source = request.getParameter("source");
			}
			if (source.equalsIgnoreCase("1")) {

				// Edited by anhtv08
				// date: 7/7/2009
				// comment
				Vector cmList = Project.getWOCustomeMetricList(prjID);
				if(cmList==null)
				{
					cmList= new Vector();
				}
				session.setAttribute("WOCustomeMetricList", cmList);
				Fms1Servlet.callPage("qualityObjective.jsp", request, response);
			} else {
				doAutoUpdateCustomerMetric(request, response, "");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateWOCustomeMetric(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			// edited by anhtv08- start..
			
			// end
			Vector cmList =
				(Vector) session.getAttribute("WOCustomeMetricList");

			// get stagelist

			String id = request.getParameter("id");
			int vectId = 0;
			if (id != null) {
				vectId = Integer.parseInt(id);
				if ((vectId > cmList.size() - 1) || (vectId < 0))
					Fms1Servlet.callPage(
						"error.jsp?error=Bad parameters",
						request,
						response);
			} else
				Fms1Servlet.callPage(
					"error.jsp?error=Bad parameters",
					request,
					response);
			WOCustomeMetricInfo info =
				(WOCustomeMetricInfo) cmList.elementAt(vectId);

			double oldPlannedValue = info.plannedValue;
			info.name = request.getParameter("woCM_name");
			info.unit = request.getParameter("woCM_unit");
			info.plannedValue =
				CommonTools.parseDouble(
					request.getParameter("woCM_plannedValue"));
			// Modify by HaiMM - Start
			info.LCL =
				(request.getParameter("woCM_LCL").equalsIgnoreCase(""))
					? CommonTools.parseDouble(
						request.getParameter("woCM_plannedValue"))
					: CommonTools.parseDouble(request.getParameter("woCM_LCL"));
			info.UCL =
				(request.getParameter("woCM_UCL").equalsIgnoreCase(""))
					? CommonTools.parseDouble(
						request.getParameter("woCM_plannedValue"))
					: CommonTools.parseDouble(request.getParameter("woCM_UCL"));
			// Modify by HaiMM - End
			info.actualValue =
				CommonTools.parseDouble(
					request.getParameter("woCM_actualValue"));
			info.note = request.getParameter("woCM_note");
			if (info.note == null)
				info.note = "";
			Project.updateCM(info);
			if (oldPlannedValue != info.plannedValue) {
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.WO_METRICS_CUSTOM_METRIC,
					info.name + ">Targeted value",
					CommonTools.formatDouble(info.plannedValue),
					CommonTools.formatDouble(oldPlannedValue));
			}
			String source = (String)session.getAttribute("SourcePage");
			if (source.equalsIgnoreCase("1")) {
				final Vector cmListQuality =
					Project.getWOCustomeMetricList(prjID);
				session.setAttribute("WOCustomeMetricList", cmListQuality);
				Fms1Servlet.callPage("qualityObjective.jsp", request, response);

			} else {
				doAutoUpdateCustomerMetric(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteWOCustomeMetric(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			if (request.getParameter("StageID") == null) {

			}
			final HttpSession session = request.getSession();
			//ng MileStoneID=Integer.parseInt(request.getParameter("StageID"));
			final int prjID =Integer.parseInt((String) session.getAttribute("projectID"));
			Vector cmList =(Vector) session.getAttribute("WOCustomeMetricList");
			String id = request.getParameter("id");
			int vectId = 0;
			if (id != null) {
				vectId = Integer.parseInt(id);
				if ((vectId > cmList.size() - 1) || (vectId < 0))
					Fms1Servlet.callPage(
						"error.jsp?error=Bad parameters",
						request,
						response);
			} else
				Fms1Servlet.callPage(
					"error.jsp?error=Bad parameters",
					request,
					response);
			WOCustomeMetricInfo info =
				(WOCustomeMetricInfo) cmList.elementAt(vectId);
			addChangeAuto(
				(int) prjID,
				Constants.ACTION_DELETE,
				Constants.WO_METRICS_CUSTOM_METRIC,
				info.name,
				null,
				null);
			Project.deleteCM(info.cusMetricID);
			String source = (String)session.getAttribute("SourcePage");
			if (source.equalsIgnoreCase("1")) {
				final Vector cmListQuality =
					Project.getWOCustomeMetricList(prjID);
				session.setAttribute("WOCustomeMetricList", cmListQuality);

				Fms1Servlet.callPage("qualityObjective.jsp", request, response);
			} else {
				//LoadWOPerformanceList(request, response,"");
				doAutoUpdateCustomerMetric(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateStandardMetricList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector vectorResult = new Vector();
			final StandardMetricInfo customerSatisfactionInfo =
				new StandardMetricInfo();
			final StandardMetricInfo leakageInfo = new StandardMetricInfo();
			final StandardMetricInfo processComplianceInfo =
				new StandardMetricInfo();
			final StandardMetricInfo EffortEfficiencyInfo =
				new StandardMetricInfo();
			final StandardMetricInfo correctionCostInfo =
				new StandardMetricInfo();
			final StandardMetricInfo timelinessInfo = new StandardMetricInfo();
			final StandardMetricInfo requirementCompletenessInfo =
				new StandardMetricInfo();
			//			  final StandardMetricInfo responseTimeInfo = new StandardMetricInfo();
			//			  final StandardMetricInfo translationCostInfo = new StandardMetricInfo();
			//			  monitor the change of Standard Metric
			final Vector stdMetrics =
				(Vector) session.getAttribute("WOStandardMetricMatrix");
			NormInfo normInfo = null;
			String newValue = null;
			String oldValue = null;
			String metricName = null;

			for (int k = 0; k < 7; k++) {
				switch (k) {
					case 0 :
						metricName = "Customer Satisfaction";
						break;
					case 1 :
						metricName = "Leakage";
						break;
					case 2 :
						metricName = "Process Compliance";
						break;
					case 3 :
						metricName = "Effort Efficiency";
						break;
					case 4 :
						metricName = "Correction Cost";
						break;
					case 5 :
						metricName = "Timeliness";
						break;
					case 6 :
						metricName = "Requirement Completeness";
						break;
				}
				for (int j = 0; j < stdMetrics.size(); j++) {
					normInfo = (NormInfo) stdMetrics.elementAt(j);
					if (normInfo.normName.equals(metricName))
						break;
				}

				newValue = request.getParameter("sm" + k + "0");
				oldValue = CommonTools.formatDouble(normInfo.plannedValue);

				if (!newValue.equals(oldValue)) {
					addChangeAuto(
						(int) prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_STANDARD_METRIC,
						metricName,
						newValue,
						oldValue);
				}
			}
			//----------------------------------------------
			//Customer Satisfication
			customerSatisfactionInfo.setUslValue(request.getParameter("sm03"));
			customerSatisfactionInfo.setLslValue(request.getParameter("sm04"));

			customerSatisfactionInfo.setTargetedValue(
				request.getParameter("sm00"));
			customerSatisfactionInfo.setActualValue(
				request.getParameter("sm01"));
			customerSatisfactionInfo.setNote(request.getParameter("sm02"));
			//Leakage
			leakageInfo.setUslValue(request.getParameter("sm13"));
			leakageInfo.setLslValue(request.getParameter("sm14"));

			leakageInfo.setTargetedValue(request.getParameter("sm10"));
			leakageInfo.setNote(request.getParameter("sm12"));
			//Process Compliance
			processComplianceInfo.setUslValue(request.getParameter("sm23"));
			processComplianceInfo.setLslValue(request.getParameter("sm24"));

			processComplianceInfo.setTargetedValue(
				request.getParameter("sm20"));
			processComplianceInfo.setNote(request.getParameter("sm22"));
			//Effort Efficiency
			EffortEfficiencyInfo.setUslValue(request.getParameter("sm33"));
			EffortEfficiencyInfo.setLslValue(request.getParameter("sm34"));

			EffortEfficiencyInfo.setTargetedValue(request.getParameter("sm30"));
			EffortEfficiencyInfo.setNote(request.getParameter("sm32"));
			//Correction Cost
			correctionCostInfo.setUslValue(request.getParameter("sm43"));
			correctionCostInfo.setLslValue(request.getParameter("sm44"));

			correctionCostInfo.setTargetedValue(request.getParameter("sm40"));
			correctionCostInfo.setNote(request.getParameter("sm42"));
			//Timeliness
			timelinessInfo.setUslValue(request.getParameter("sm53"));
			timelinessInfo.setLslValue(request.getParameter("sm54"));

			timelinessInfo.setTargetedValue(request.getParameter("sm50"));
			timelinessInfo.setNote(request.getParameter("sm52"));
			//Requirement Completion
			requirementCompletenessInfo.setUslValue(
				request.getParameter("sm63"));
			requirementCompletenessInfo.setLslValue(
				request.getParameter("sm64"));

			requirementCompletenessInfo.setTargetedValue(
				request.getParameter("sm60"));
			requirementCompletenessInfo.setNote(request.getParameter("sm62"));
			//			//Response Time
			//			  responseTimeInfo.setTargetedValue(request.getParameter("sm60"));
			//			  responseTimeInfo.setNote(request.getParameter("sm62"));
			//			//Translation Cost
			//			  translationCostInfo.setTargetedValue(request.getParameter("sm70"));
			//			  translationCostInfo.setNote(request.getParameter("sm72"));

			vectorResult.add(customerSatisfactionInfo);
			vectorResult.add(leakageInfo);
			vectorResult.add(processComplianceInfo);
			vectorResult.add(EffortEfficiencyInfo);
			vectorResult.add(correctionCostInfo);
			vectorResult.add(timelinessInfo);
			vectorResult.add(requirementCompletenessInfo);
			//			  vectorResult.add(responseTimeInfo);
			//			  vectorResult.add(translationCostInfo);            

			Metrics.updateStandardMetricList(prjID, vectorResult);
			String source = "";
			if (request.getParameter("source") != null) {
				source = request.getParameter("source");
			}
			if (source.equalsIgnoreCase("1")) {
				final Vector stdMetricsUpdated =
					Project.getStandardMetricList(prjID);
				session.setAttribute(
					"WOStandardMetricMatrix",
					stdMetricsUpdated);

				Fms1Servlet.callPage("qualityObjective.jsp", request, response);
			} else {
				doLoadWOPerformanceList(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAutoUpdateStandardMetricList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector vectorResult = new Vector();
			final StandardMetricInfo customerSatisfactionInfo =
				new StandardMetricInfo();
			final StandardMetricInfo leakageInfo = new StandardMetricInfo();
			final StandardMetricInfo processComplianceInfo =
				new StandardMetricInfo();
			final StandardMetricInfo EffortEfficiencyInfo =
				new StandardMetricInfo();
			final StandardMetricInfo correctionCostInfo =
				new StandardMetricInfo();
			final StandardMetricInfo timelinessInfo = new StandardMetricInfo();
			final StandardMetricInfo requirementCompletenessInfo =
				new StandardMetricInfo();
			final Vector stdMetrics =
				(Vector) session.getAttribute("WOStandardMetricMatrix");
			NormInfo normInfo = null;
			String newValue = null;
			String oldValue = null;
			String metricName = null;

			for (int k = 0; k < 7; k++) {
				switch (k) {
					case 0 :
						metricName = "Customer Satisfaction";
						break;
					case 1 :
						metricName = "Leakage";
						break;
					case 2 :
						metricName = "Process Compliance";
						break;
					case 3 :
						metricName = "Effort Efficiency";
						break;
					case 4 :
						metricName = "Correction Cost";
						break;
					case 5 :
						metricName = "Timeliness";
						break;
					case 6 :
						metricName = "Requirement Completeness";
						break;
				}
				for (int j = 0; j < stdMetrics.size(); j++) {
					normInfo = (NormInfo) stdMetrics.elementAt(j);
					if (normInfo.normName.equals(metricName)) {
						break;
					}
				}

				if (CommonTools
					.formatDouble(normInfo.plannedValue)
					.equals("N/A")) {
					if (!CommonTools
						.formatDouble(normInfo.average)
						.equals("N/A")) {
						newValue = CommonTools.formatDouble(normInfo.average);
						oldValue =
							CommonTools.formatDouble(normInfo.plannedValue);
						if (!newValue.equals(oldValue)) {
							addChangeAuto(
								(int) prjID,
								Constants.ACTION_UPDATE,
								Constants.WO_METRICS_STANDARD_METRIC,
								metricName,
								newValue,
								oldValue);
						}
					}
				}

				switch (k) {
					case 0 :
						//Customer Satisfication
						if (CommonTools
							.formatDouble(normInfo.usl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.lcl)
								.equals("N/A")) {
								customerSatisfactionInfo.setUslValue(
									CommonTools.formatDouble(normInfo.lcl));
							}
						} else {
							customerSatisfactionInfo.setUslValue(
								CommonTools.formatDouble(normInfo.usl));
						}
						if (CommonTools
							.formatDouble(normInfo.lsl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.ucl)
								.equals("N/A")) {
								customerSatisfactionInfo.setLslValue(
									CommonTools.formatDouble(normInfo.ucl));
							}
						} else {
							customerSatisfactionInfo.setLslValue(
								CommonTools.formatDouble(normInfo.lsl));
						}
						if (CommonTools
							.formatDouble(normInfo.plannedValue)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.average)
								.equals("N/A")) {
								customerSatisfactionInfo.setTargetedValue(
									CommonTools.formatDouble(normInfo.average));
							}
						} else {
							customerSatisfactionInfo.setTargetedValue(
								CommonTools.formatDouble(
									normInfo.plannedValue));
						}
						if (!CommonTools
							.formatDouble(normInfo.actualValue)
							.equals("N/A")) {
							customerSatisfactionInfo.setActualValue(
								CommonTools.formatDouble(normInfo.actualValue));
						}
						customerSatisfactionInfo.setNote(normInfo.note);
						break;
					case 1 :
						//Leakage
						if (CommonTools
							.formatDouble(normInfo.usl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.lcl)
								.equals("N/A")) {
								leakageInfo.setUslValue(
									CommonTools.formatDouble(normInfo.lcl));
							}
						} else {
							leakageInfo.setUslValue(
								CommonTools.formatDouble(normInfo.usl));
						}
						if (CommonTools
							.formatDouble(normInfo.lsl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.ucl)
								.equals("N/A")) {
								leakageInfo.setLslValue(
									CommonTools.formatDouble(normInfo.ucl));
							}
						} else {
							leakageInfo.setLslValue(
								CommonTools.formatDouble(normInfo.lsl));
						}
						if (CommonTools
							.formatDouble(normInfo.plannedValue)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.average)
								.equals("N/A")) {
								leakageInfo.setTargetedValue(
									CommonTools.formatDouble(normInfo.average));
							}
						} else {
							leakageInfo.setTargetedValue(
								CommonTools.formatDouble(
									normInfo.plannedValue));
						}
						leakageInfo.setNote(normInfo.note);
						break;
					case 2 :
						//Process Compliance
						if (CommonTools
							.formatDouble(normInfo.usl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.lcl)
								.equals("N/A")) {
								processComplianceInfo.setUslValue(
									CommonTools.formatDouble(normInfo.lcl));
							}
						} else {
							processComplianceInfo.setUslValue(
								CommonTools.formatDouble(normInfo.usl));
						}
						if (CommonTools
							.formatDouble(normInfo.lsl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.ucl)
								.equals("N/A")) {
								processComplianceInfo.setLslValue(
									CommonTools.formatDouble(normInfo.ucl));
							}
						} else {
							processComplianceInfo.setLslValue(
								CommonTools.formatDouble(normInfo.lsl));
						}
						if (CommonTools
							.formatDouble(normInfo.plannedValue)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.average)
								.equals("N/A")) {
								processComplianceInfo.setTargetedValue(
									CommonTools.formatDouble(normInfo.average));
							}
						} else {
							processComplianceInfo.setTargetedValue(
								CommonTools.formatDouble(
									normInfo.plannedValue));
						}
						processComplianceInfo.setNote(normInfo.note);
						break;
					case 3 :
						//Effort Efficiency
						if (CommonTools
							.formatDouble(normInfo.usl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.lcl)
								.equals("N/A")) {

								EffortEfficiencyInfo.setUslValue(
									CommonTools.formatDouble(normInfo.lcl));
							}
						} else {
							EffortEfficiencyInfo.setUslValue(
								CommonTools.formatDouble(normInfo.usl));
						}
						if (CommonTools
							.formatDouble(normInfo.lsl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.ucl)
								.equals("N/A")) {
								EffortEfficiencyInfo.setLslValue(
									CommonTools.formatDouble(normInfo.ucl));
							}
						} else {
							EffortEfficiencyInfo.setLslValue(
								CommonTools.formatDouble(normInfo.lsl));
						}
						if (CommonTools
							.formatDouble(normInfo.plannedValue)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.average)
								.equals("N/A")) {
								EffortEfficiencyInfo.setTargetedValue(
									CommonTools.formatDouble(normInfo.average));
							}
						} else {
							EffortEfficiencyInfo.setTargetedValue(
								CommonTools.formatDouble(
									normInfo.plannedValue));
						}
						EffortEfficiencyInfo.setNote(normInfo.note);
						break;
					case 4 :
						//Correction Cost
						if (CommonTools
							.formatDouble(normInfo.usl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.lcl)
								.equals("N/A")) {
								correctionCostInfo.setUslValue(
									CommonTools.formatDouble(normInfo.lcl));
							}
						} else {
							correctionCostInfo.setUslValue(
								CommonTools.formatDouble(normInfo.usl));
						}
						if (CommonTools
							.formatDouble(normInfo.lsl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.ucl)
								.equals("N/A")) {
								correctionCostInfo.setLslValue(
									CommonTools.formatDouble(normInfo.ucl));
							}
						} else {
							correctionCostInfo.setLslValue(
								CommonTools.formatDouble(normInfo.lsl));
						}
						if (CommonTools
							.formatDouble(normInfo.plannedValue)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.average)
								.equals("N/A")) {
								correctionCostInfo.setTargetedValue(
									CommonTools.formatDouble(normInfo.average));
							}
						} else {
							correctionCostInfo.setTargetedValue(
								CommonTools.formatDouble(
									normInfo.plannedValue));
						}
						correctionCostInfo.setNote(normInfo.note);
						break;
					case 5 :
						//Timeliness
						if (CommonTools
							.formatDouble(normInfo.usl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.lcl)
								.equals("N/A")) {
								timelinessInfo.setUslValue(
									CommonTools.formatDouble(normInfo.lcl));
							}
						} else {
							timelinessInfo.setUslValue(
								CommonTools.formatDouble(normInfo.usl));
						}
						if (CommonTools
							.formatDouble(normInfo.lsl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.ucl)
								.equals("N/A")) {
								timelinessInfo.setLslValue(
									CommonTools.formatDouble(normInfo.ucl));
							}
						} else {
							timelinessInfo.setLslValue(
								CommonTools.formatDouble(normInfo.lsl));
						}
						if (CommonTools
							.formatDouble(normInfo.plannedValue)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.average)
								.equals("N/A")) {
								timelinessInfo.setTargetedValue(
									CommonTools.formatDouble(normInfo.average));
							}
						} else {
							timelinessInfo.setTargetedValue(
								CommonTools.formatDouble(
									normInfo.plannedValue));
						}
						timelinessInfo.setNote(normInfo.note);
						break;
					case 6 :
						//Requirement Completion
						if (CommonTools
							.formatDouble(normInfo.usl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.lcl)
								.equals("N/A")) {
								requirementCompletenessInfo.setUslValue(
									CommonTools.formatDouble(normInfo.lcl));
							}
						} else {
							requirementCompletenessInfo.setUslValue(
								CommonTools.formatDouble(normInfo.usl));
						}
						if (CommonTools
							.formatDouble(normInfo.lsl)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.ucl)
								.equals("N/A")) {
								requirementCompletenessInfo.setLslValue(
									CommonTools.formatDouble(normInfo.ucl));
							}
						} else {
							requirementCompletenessInfo.setLslValue(
								CommonTools.formatDouble(normInfo.lsl));
						}
						if (CommonTools
							.formatDouble(normInfo.plannedValue)
							.equals("N/A")) {
							if (!CommonTools
								.formatDouble(normInfo.average)
								.equals("N/A")) {
								requirementCompletenessInfo.setTargetedValue(
									CommonTools.formatDouble(normInfo.average));
							}
						} else {
							requirementCompletenessInfo.setTargetedValue(
								CommonTools.formatDouble(
									normInfo.plannedValue));
						}
						requirementCompletenessInfo.setNote(normInfo.note);
						break;
				}
			}

			vectorResult.add(customerSatisfactionInfo);
			vectorResult.add(leakageInfo);
			vectorResult.add(processComplianceInfo);
			vectorResult.add(EffortEfficiencyInfo);
			vectorResult.add(correctionCostInfo);
			vectorResult.add(timelinessInfo);
			vectorResult.add(requirementCompletenessInfo);

			Metrics.updateStandardMetricList1(prjID, vectorResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//End of Standard Metric-----------------------------------------------------------
	//Performance Metric
	public static final void doLoadWOPerformanceList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		doLoadWOPerformanceList(request, response, "");
	}
	public static final void doLoadWOPerformanceList(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {

			Vector cmList;
			final HttpSession session = request.getSession();
			final long prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));

			//Added by anhtv08

			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			final Vector stageList = Schedule.getStageList(prjID);

			// get current Stage..
			StageInfo stageInfo = null;
			for (int i = stageList.size() - 1; i >= 0; i--) {
				stageInfo = (StageInfo) stageList.elementAt(i);
				if ((stageInfo.aEndD != null)
					&& (stageInfo.actualBeginDate != null)
					&& (stageInfo.plannedEndDate != null)
					&& (stageInfo.plannedBeginDate != null)) {
					break;
				}
			}
			if(stageInfo!=null)
			{
				cmList = Project.getCustomerMetric(prjID,stageInfo.milestoneID);
				session.setAttribute("StageInfo", stageInfo);
				session.setAttribute("StageInfoDP", stageInfo);
				session.setAttribute("WOCustomeMetricList", cmList);
			}
			else 
			{
				session.setAttribute("StageInfo", null);
				session.setAttribute("StageInfoDP", null);
				session.setAttribute("WOCustomeMetricList", null);
			}

			final Vector performanceVector =
				Project.getPerformanceMetrics(prjID);
			session.setAttribute("WOPerformanceVector", performanceVector);

			final ProjectInfo pinf = Project.getProjectInfo(prjID);
			// added anhtv08-start
			session.setAttribute(
				"projectCatalog",
				String.valueOf(pinf.getLifecycleId()));
			//end
			final EffortInfo effortHeader = Effort.getEffortInfo(pinf);
			session.setAttribute("effortHeaderInfo", effortHeader);
			// Add by HaiMM - Start
			//final Vector dpVt = Defect.getDPTask(prjID);
			// anhtv08-start
			if(stageInfo!=null){
				final Vector dpVt = Defect.getDPTask(prjID,stageInfo.milestoneID);
				session.setAttribute("defectPrevention", dpVt);	
			}
			else 
			{
				session.setAttribute("defectPrevention",null);
			}
			// end
			// Add by HaiMM - End

			//			Added by LamNT3
			Vector stdMetrics = Project.getStandardMetricList(prjID);
			session.setAttribute("WOStandardMetricMatrix", stdMetrics);
			doAutoUpdateStandardMetricList(request, response);
			stdMetrics = Project.getStandardMetricList(prjID);
			session.setAttribute("WOStandardMetricMatrix", stdMetrics);

			session.setAttribute("WOMetricsStageList", stageList);
			session.setAttribute("SourcePage","0");
			// end
			Fms1Servlet.callPage(
				"woPerformanceView.jsp" + index,
				request,
				response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdatePerformanceList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			Project.updateWOPerformanceInfo newValues =
				new Project.updateWOPerformanceInfo();
			newValues.planStartDate =
				CommonTools.parseSQLDate(request.getParameter("planStartDate"));
			newValues.actualStartDate =
				CommonTools.parseSQLDate(
					request.getParameter("actualStartDate"));
			newValues.planEndDate =
				CommonTools.parseSQLDate(request.getParameter("planEndDate"));
			newValues.rePlanEndDate =
				CommonTools.parseSQLDate(request.getParameter("rePlanEndDate"));
			newValues.actualEndDate =
				CommonTools.parseSQLDate(request.getParameter("actualEndDate"));
			newValues.planTeamSize =
				CommonTools.parseDouble(request.getParameter("planTeamSize"));
			newValues.effortUsage =
				CommonTools.parseDouble(request.getParameter("planEffort"));
			newValues.reEffortUsage =
				CommonTools.parseDouble(request.getParameter("rePlanEffort"));
			//HUYNH2 add billable effort
			newValues.billableEffortUsage =
				CommonTools.parseDouble(
					request.getParameter("planBillableEffort"));
			newValues.reBillableEffortUsage =
				CommonTools.parseDouble(
					request.getParameter("rePlanBillableEffort"));
			newValues.billableActual =
				CommonTools.parseDouble(request.getParameter("billableActual"));
			//end 
			newValues.planCalendarEffort =
				CommonTools.parseDouble(
					request.getParameter("planCalendarEffort"));
			newValues.replanCalendarEffort =
				CommonTools.parseDouble(
					request.getParameter("rePlanCalendarEffort"));
			newValues.devEffort =
				CommonTools.parseDouble(request.getParameter("devEffort"));
			newValues.manEffort =
				CommonTools.parseDouble(request.getParameter("manEffort"));

			if (Double.isNaN(newValues.devEffort)
				|| Double.isNaN(newValues.manEffort)) {
				//if one is null then all are null
				newValues.devEffort = Double.NaN;
				newValues.manEffort = Double.NaN;
				newValues.quaEffort = Double.NaN;
			} else
				newValues.quaEffort =
					100d - newValues.devEffort - newValues.manEffort;
			//save WOPerformance.
			if (Project.updateWOPerformanceList(prjID, newValues)) {
				final Vector oldPerfList =
					(Vector) session.getAttribute("WOPerformanceVector");
				MetricInfo startDateInfo =
					(MetricInfo) oldPerfList.elementAt(0);
				if (newValues.planStartDate.getTime()
					!= startDateInfo.plannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						startDateInfo.name,
						CommonTools.dateFormat(newValues.planStartDate),
						CommonTools.dateFormat(startDateInfo.plannedValue));
				}
				MetricInfo endDateInfo = (MetricInfo) oldPerfList.elementAt(1);
				if (newValues.planEndDate.getTime()
					!= endDateInfo.plannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						endDateInfo.name,
						CommonTools.dateFormat(newValues.planEndDate),
						CommonTools.dateFormat(endDateInfo.plannedValue));
				}
				if (newValues.rePlanEndDate != null
					&& (newValues.rePlanEndDate.getTime()
						!= endDateInfo.rePlannedValue)) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						endDateInfo.name,
						CommonTools.dateFormat(newValues.rePlanEndDate),
						CommonTools.dateFormat(endDateInfo.rePlannedValue));
				}
				MetricInfo maxTeam = (MetricInfo) oldPerfList.elementAt(3);
				if (newValues.planTeamSize != maxTeam.plannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						maxTeam.name,
						CommonTools.formatDouble(newValues.planTeamSize),
						CommonTools.formatDouble(maxTeam.plannedValue));
				}
				MetricInfo effortUsage = (MetricInfo) oldPerfList.elementAt(4);
				if (newValues.effortUsage != effortUsage.plannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						effortUsage.name,
						CommonTools.formatDouble(newValues.effortUsage),
						CommonTools.formatDouble(effortUsage.plannedValue));
				}
				if (newValues.reEffortUsage != effortUsage.rePlannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						effortUsage.name,
						CommonTools.formatDouble(newValues.reEffortUsage),
						CommonTools.formatDouble(effortUsage.rePlannedValue));
				}
				MetricInfo devEffort = (MetricInfo) oldPerfList.elementAt(5);
				if (newValues.devEffort != devEffort.plannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						devEffort.name,
						CommonTools.formatDouble(newValues.devEffort),
						CommonTools.formatDouble(devEffort.plannedValue));
				}
				MetricInfo manEffort = (MetricInfo) oldPerfList.elementAt(6);
				if (newValues.manEffort != manEffort.plannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						manEffort.name,
						CommonTools.formatDouble(newValues.manEffort),
						CommonTools.formatDouble(manEffort.plannedValue));
				}
				MetricInfo qualityEffort =
					(MetricInfo) oldPerfList.elementAt(7);
				if (newValues.quaEffort != qualityEffort.plannedValue) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_METRICS_PERFORMANCE,
						qualityEffort.name,
						CommonTools.formatDouble(newValues.quaEffort),
						CommonTools.formatDouble(qualityEffort.plannedValue));
				}
				// if the user has selected a project, should we display menus ?
				// synchronize with code in  WorkUnitCaller.workUnitHomeCaller
				String disableMnu =
					Project.isWOConsistent(prjID)
						? "?mnuDisable=0"
						: "?mnuDisable=1";
				String source = "";
				if (request.getParameter("source") != null) {
					source = request.getParameter("source");
				}
				if (source.equalsIgnoreCase("1")) {
					final Vector performanceVector =
						Project.getPerformanceMetrics(prjID);
					session.setAttribute(
						"WOPerformanceVector",
						performanceVector);

					Fms1Servlet.callPage(
						"qualityObjective.jsp",
						request,
						response);
				} else {
					doLoadWOPerformanceList(request, response, disableMnu);
				}
			} else {
				Fms1Servlet.callPage(
					"error.jsp?error=Bad data input",
					request,
					response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Fms1Servlet.callPage(
					"error.jsp?error=Bad data input",
					request,
					response);
			} catch (Exception f) {
				f.printStackTrace();
			}
		}
	}

	// General Information
	public static final void doLoadGeneralInfo(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		doLoadGeneralInfo(request, response, "");
	}
	public static final void doLoadGeneralInfo(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {
			request.getSession().setAttribute("messageCustomer1", "true");
			request.getSession().setAttribute("messageCustomer2", "true");
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			session.setAttribute("WOGeneralInfo", projectInfo);
			session.setAttribute("caller", String.valueOf(Constants.WO_CALLER));
			session.setAttribute(
				"PLConstraintList",
				Project.getConstraintList(prjID));
			session.setAttribute(
				"PLAssumptionList",
				Project.getAssumptionList(prjID));
			Fms1Servlet.callPage(
				"woGeneralInfoView.jsp" + index,
				request,
				response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// huynh2 add some function
	public static final void doLoadGeneralInfoUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		doLoadGeneralInfoUpdate(request, response, "");
		Fms1Servlet.callPage("woGeneralInfo.jsp", request, response);
	}
	public static final void doLoadGeneralInfoUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {
			request.getSession().setAttribute("messageCustomer1", "true");
			request.getSession().setAttribute("messageCustomer2", "true");
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			final Vector devList =
				Assignments.getAllUserAssignment(prjID, "", "", 0);
			final Vector groupList = WorkUnit.getOperationGroupList();
			final Vector apptypeList = Param.getAppTypeList();
			final Vector bizdomainList = Param.getBizDomainList();
			final Vector contracttypeList = Param.getContractTypeList();
			request.getSession().setAttribute("messageCustomer1", "true");
			request.getSession().setAttribute("messageCustomer2", "true");
			session.setAttribute("ContractTypeList", contracttypeList);
			session.setAttribute("BizDomainList", bizdomainList);
			session.setAttribute("ApplicationTypeList", apptypeList);
			session.setAttribute("WOGeneralInfo", projectInfo);
			session.setAttribute("WODevList", devList);
			session.setAttribute("WOGroupList", groupList);
			//Fms1Servlet.callPage("woGeneralInfoView.jsp "+index,request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Perform close a project
	 * @param request
	 * @param response
	 */
	public static final void doLoadGeneralInfoProjectCloseCancel(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			ProjectInfo projectInfo = new ProjectInfo();
			final long lProjectID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			projectInfo.setProjectId(lProjectID);
			projectInfo.setStatus(
				Integer.parseInt(request.getParameter("cboStatus")));
			projectInfo.setDescription(request.getParameter("txtDescription"));
			projectInfo.setActualFinishDate(
				CommonTools.parseSQLDate(
					request.getParameter("txtActualFinish")));
			if (Schedule.doUpdateMilestoneBeforeCloseProject(projectInfo)) {
				if (Project.doCloseCanCelProject(projectInfo)) {
					final ProjectInfo woGeneralInfo =
						Project.getProjectInfo(lProjectID);
					session.setAttribute("WOGeneralInfoMatrix", woGeneralInfo);
				}
			}
			doLoadGeneralInfo(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static final void doLoadGeneralInfoProjectReOpen(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		final long lProjectID =
			Long.parseLong((String) session.getAttribute("projectID"));
		Project.reOpenProject(lProjectID);
		final ProjectInfo projectInfo = Project.getProjectInfo(lProjectID);
		session.setAttribute("WOGeneralInfoMatrix", projectInfo);
		doLoadGeneralInfo(request, response);
	}
	public static final void doGeneralInfoUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));

			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			String prjCode = request.getParameter("gi_prj_code");
			String prjName = request.getParameter("gi_prj_name");
			String cstName = request.getParameter("gi_cst_name");
			String cstName2nd = request.getParameter("gi_cst_name_2nd");
			String contract = request.getParameter("gi_contract");
			String domain = request.getParameter("gi_domain");
			String apptype = request.getParameter("gi_apptype");
			String grpName = request.getParameter("gi_grp_name");
			String divName = request.getParameter("gi_division_name");
			String prjDtr = request.getParameter("gi_prj_dtr");
			String prjType = request.getParameter("gi_prj_type");
			String prjCat = request.getParameter("gi_prj_cat");
			String prjObjective = request.getParameter("gi_prj_objective");
			String prjRank = request.getParameter("gi_grp_rank");
			String prjLevel = request.getParameter("gi_prj_level");
			String prjStatus = request.getParameter("cboStatus");
			String typeCustomer1 = request.getParameter("customerType1");
			String typeCustomer2 = request.getParameter("customer2ndType1");
			String typeNameCustomer1 = request.getParameter("rdCustomerName");
			String typeNameCustomer2 =
				request.getParameter("rdCustomerName2nd");
			String messageCustomer1 = "false";
			String messageCustomer2 = "false";
			if (typeCustomer1.equalsIgnoreCase("INTERNAL"))
				typeCustomer1 = "1";
			else
				typeCustomer1 = "0";
			if (typeCustomer2.equalsIgnoreCase("INTERNAL"))
				typeCustomer2 = "1";
			else
				typeCustomer2 = "0";

			if (typeCustomer1.equalsIgnoreCase("0")) {
				Vector cus = Requirement.getCustomerList();

				if (typeNameCustomer1.equalsIgnoreCase("StandardName")) {
					for (int i = 0; i < cus.size(); i++) {
						CustomerInfo temp1 = (CustomerInfo) cus.get(i);
						if (cstName.equalsIgnoreCase(temp1.standardName))
							messageCustomer1 = "true";
					}
				} else {
					for (int i = 0; i < cus.size(); i++) {
						CustomerInfo temp1 = (CustomerInfo) cus.get(i);
						if (cstName.equalsIgnoreCase(temp1.fullName))
							messageCustomer1 = "true";
					}
				}
			} else {
				messageCustomer1 = "true";
			}
			if (typeCustomer2.equalsIgnoreCase("0")) {
				Vector cus = Requirement.getCustomerList();

				if (typeNameCustomer2.equalsIgnoreCase("StandardName")) {
					for (int i = 0; i < cus.size(); i++) {
						CustomerInfo temp1 = (CustomerInfo) cus.get(i);
						if (cstName2nd.equalsIgnoreCase(temp1.standardName))
							messageCustomer2 = "true";
					}
				} else {
					for (int i = 0; i < cus.size(); i++) {
						CustomerInfo temp1 = (CustomerInfo) cus.get(i);
						if (cstName2nd.equalsIgnoreCase(temp1.fullName))
							messageCustomer2 = "true";
					}
				}
			} else {
				messageCustomer2 = "true";
			}

			// Project Code
			projectInfo.setProjectCode(prjCode);
			// Project Name
			projectInfo.setProjectName(prjName);
			// Customer
			projectInfo.setCustomer(cstName);
			// 2nd Customer
			projectInfo.setSecondCustomer(cstName2nd);
			// Business Domain
			projectInfo.setBusinessDomain(domain);
			// Application Type
			projectInfo.setApplicationType(apptype);
			// Contract Type
			projectInfo.setContractType(contract);
			// Group
			projectInfo.setGroupName(grpName);
			// Division
			projectInfo.setDivisionName(divName);
			// Project Level
			projectInfo.setProjectLevel(prjLevel);
			// Project Manager
			projectInfo.setLeader(prjDtr);
			// Project Type
			projectInfo.setProjectType(prjType);
			// Project Category (Life Cycle)
			projectInfo.setLifecycle(prjCat);
			// Rank
			projectInfo.setProjectRank(prjRank);
			// Scope and Objective
			projectInfo.setScopeAndObjective(prjObjective);
			// Status
			projectInfo.setStatus(Integer.parseInt(prjStatus));
			//////////////////////////////////////////////////////////////////
			projectInfo.setTypeCustomer(Integer.parseInt(typeCustomer1));
			projectInfo.setTypeCustomer2(Integer.parseInt(typeCustomer2));
			if (messageCustomer2.equalsIgnoreCase("true")
				&& messageCustomer1.equals("true")) {
				session.setAttribute("WOGeneralInfo", projectInfo);
				if (Project.updateGeneralInfo(prjID, projectInfo)) {
					doLoadGeneralInfo(request, response);
				} else {
					request.setAttribute(
						StringConstants.WORK_UNIT_ERROR_MESSAGE,
						"Update Project failed");
					Fms1Servlet.callPage(
						"woGeneralInfo.jsp",
						request,
						response);
				}
			} else {
				request.getSession().setAttribute(
					"messageCustomer1",
					messageCustomer1);
				request.getSession().setAttribute(
					"messageCustomer2",
					messageCustomer2);
				final Vector devList =
					Assignments.getAllUserAssignment(prjID, "", "", 0);
				final Vector groupList = WorkUnit.getOperationGroupList();
				final Vector apptypeList = Param.getAppTypeList();
				final Vector bizdomainList = Param.getBizDomainList();
				final Vector contracttypeList = Param.getContractTypeList();
				session.setAttribute("ContractTypeList", contracttypeList);
				session.setAttribute("BizDomainList", bizdomainList);
				session.setAttribute("ApplicationTypeList", apptypeList);
				session.setAttribute("WOGeneralInfo", projectInfo);
				session.setAttribute("WODevList", devList);
				session.setAttribute("WOGroupList", groupList);
				Fms1Servlet.callPage("woGeneralInfo.jsp", request, response);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// End of General information
	// Team----------------------------------------------------------------------------------
	public static final void doLoadWOTeamList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long projectId = 0;
			if (request.getParameter("projectID") != null) {
				projectId = Long.parseLong(request.getParameter("projectID"));
				session.setAttribute("projectID", Long.toString(projectId));
				if (request.getParameter("projectCode") != null) {
					session.setAttribute(
						"GroupName",
						session.getAttribute("workUnitName"));
					session.setAttribute(
						"workUnitName",
						request.getParameter("projectCode"));
				}
			} else {
				projectId =
					Long.parseLong((String) session.getAttribute("projectID"));
			}
			// landd add sub team information start
			Vector subTeamList = SubTeams.getWOSubTeamList(projectId);
			if (subTeamList == null) {
				subTeamList = new Vector();
			}
			session.setAttribute("WOSubTeamList", subTeamList);
			// landd add sub team information end

			Vector teamList = Assignments.getWOTeamList(projectId, null, null);
			if (teamList == null) {
				teamList = new Vector();
			}
			Double totalCalendarEffort =
				new Double(Assignments.getTotalCalendarEffort(projectId));
			session.setAttribute("WOTeamList", teamList);
			session.setAttribute("totalCalendarEffort", totalCalendarEffort);
			Fms1Servlet.callPage("woTeam.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrepareTeamUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final ProjectDateInfo projectDateInfo =
				Project.getProjectDate(prjID);
			session.setAttribute("projectDateInfo", projectDateInfo);
			Fms1Servlet.callPage(
				"woTeamUpdate.jsp?assID="
					+ request.getParameter("woTeam_assID"),
				request,
				response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// landd add/update subteam start
	public static final void doPrepareSubTeamAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("woSubTeamAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareSubTeamUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("woSubTeamUpdate.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doSubTeamAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			SubTeamInfo subTeamInfo = null;
			int iRet = 0;
			boolean isExist = false;

			String[] aSubTeamName = request.getParameterValues("subteam_name");
			String[] aSubTeamNote = request.getParameterValues("subteam_note");

			int size = aSubTeamName.length;
			for (int i = 0; i < size; i++) {
				if (aSubTeamName[i] == null
					|| "".equals(aSubTeamName[i].trim()))
					continue;
				subTeamInfo = new SubTeamInfo();

				subTeamInfo.teamName =
					ConvertString.toStandardizeString(aSubTeamName[i]).trim();

				if (aSubTeamNote[i] == null)
					aSubTeamNote[i] = "";
				subTeamInfo.teamNote =
					ConvertString.toStandardizeString(aSubTeamNote[i].trim());

				// If not system error then call add sub team
				if (iRet != 1)
					iRet = SubTeams.doAddSubTeam(subTeamInfo, prjID);

				// If error then add to vector
				if (iRet != 0) {
					if (iRet == 2)
						isExist = true;
					errDataVector.addElement(subTeamInfo);
				} else {
					addChangeAuto(
						prjID,
						Constants.ACTION_ADD,
						Constants.WO_SUB_TEAM_ADD,
						"TeamName",
						subTeamInfo.teamName,
						null);
				}
			}

			if (isExist)
				iRet = 2;

			if (iRet == 0 && !isExist) {
				Vector subTeamList = SubTeams.getWOSubTeamList(prjID);
				if (subTeamList == null) {
					subTeamList = new Vector();
				}
				session.setAttribute("WOSubTeamList", subTeamList);
				Fms1Servlet.callPage("woTeam.jsp", request, response);
			} else {
				request.setAttribute("WOErrSubTeamList", errDataVector);
				request.setAttribute("ErrType", iRet + "");
				Fms1Servlet.callPage("woSubTeamAdd.jsp", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doSubTeamUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;
			Vector vNewSubTeam = new Vector();
			Vector vErrSubTeam = new Vector();
			Vector vOldSubTeam = (Vector) session.getAttribute("WOSubTeamList");

			SubTeamInfo newSubTeamInfo = null;
			SubTeamInfo oldSubTeamInfo = null;
			String[] aSubTeamName = request.getParameterValues("subteam_name");
			String[] aSubTeamNote = request.getParameterValues("subteam_note");
			String[] aSubTeamID = request.getParameterValues("subteam_id");

			if (aSubTeamName != null)
				size = aSubTeamName.length;
			for (int i = 0; i < size; i++) {

				newSubTeamInfo = new SubTeamInfo();
				oldSubTeamInfo = (SubTeamInfo) vOldSubTeam.elementAt(i);
				if (aSubTeamName[i] == null)
					aSubTeamName[i] = "";
				else
					aSubTeamName[i] = aSubTeamName[i].trim();

				if (aSubTeamNote[i] == null)
					aSubTeamNote[i] = "";
				else
					aSubTeamNote[i] = aSubTeamNote[i].trim();

				if (oldSubTeamInfo.teamName == null)
					oldSubTeamInfo.teamName = "";
				else
					oldSubTeamInfo.teamName = oldSubTeamInfo.teamName.trim();

				if (oldSubTeamInfo.teamNote == null)
					oldSubTeamInfo.teamNote = "";
				else
					oldSubTeamInfo.teamNote = oldSubTeamInfo.teamNote.trim();

				newSubTeamInfo.teamName = aSubTeamName[i];
				newSubTeamInfo.teamNote = aSubTeamNote[i];
				newSubTeamInfo.teamID = Long.parseLong(aSubTeamID[i]);

				if (aSubTeamName[i]
					.compareToIgnoreCase(oldSubTeamInfo.teamName)
					!= 0
					|| aSubTeamNote[i].compareToIgnoreCase(
						oldSubTeamInfo.teamNote)
						!= 0) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_SUB_TEAM_UPDATE,
						"TeamName",
						aSubTeamName[i],
						oldSubTeamInfo.teamName);

					vNewSubTeam.addElement(newSubTeamInfo);
				}
				vErrSubTeam.addElement(newSubTeamInfo);
			}

			if (SubTeams.doUpdateSubTeam(vNewSubTeam) == 0) {
				Vector subTeamList = SubTeams.getWOSubTeamList(prjID);
				if (subTeamList == null) {
					subTeamList = new Vector();
				}
				session.setAttribute("WOSubTeamList", subTeamList);
				Fms1Servlet.callPage("woTeam.jsp", request, response);
			} else {
				request.setAttribute("WOErrSubTeamList", vErrSubTeam);
				request.setAttribute("ErrType", "1");
				Fms1Servlet.callPage("woSubTeamUpdate.jsp", request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doSubTeamDelete(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			long subTeamID = Long.parseLong(request.getParameter("subTeamID"));
			if (!SubTeams.checkRefSubTeam(subTeamID)) {
				if (SubTeams.doDeleteSubTeam(subTeamID)) {
					addChangeAuto(
						prjID,
						Constants.ACTION_DELETE,
						Constants.WO_SUB_TEAM_DELETE,
						"SubTeamID" + subTeamID,
						null,
						null);
					Vector subTeamList = SubTeams.getWOSubTeamList(prjID);
					if (subTeamList == null) {
						subTeamList = new Vector();
					}
					session.setAttribute("WOSubTeamList", subTeamList);

					Fms1Servlet.callPage(
						"woSubTeamUpdate.jsp",
						request,
						response);
				} else {
					request.setAttribute("ErrType", "1");
					Fms1Servlet.callPage(
						"woSubTeamUpdate.jsp",
						request,
						response);
				}
			} else {
				// process show exist data in workorder sub team update screen				
				Vector vExistAssignmentData =
					Assignments.getAssignSubTeam(subTeamID);
				if (vExistAssignmentData == null) {
					vExistAssignmentData = new Vector();
				}
				request.setAttribute("WOAssSubTeam", vExistAssignmentData);
				request.setAttribute("ErrType", "2");
				Fms1Servlet.callPage("woSubTeamUpdate.jsp", request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//	landd add/update subteam end

	public static final void doPrepareTeamAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final ProjectDateInfo projectDateInfo =
				Project.getProjectDate(prjID);
			session.setAttribute("projectDateInfo", projectDateInfo);
			Fms1Servlet.callPage("woTeamAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddAssignment(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			Vector resultVector = new Vector();
			AssignmentInfo assInfo = new AssignmentInfo();
			String projectCode = "";
			long projectID = 0;
			boolean userFilterErr = false;

			// standardize input data: remove space: beginning, end, redundance space
			// ConvertString.toStandardizeString
			String strUserAccount[] =
				request.getParameterValues("strAccountName");
			String type[] = request.getParameterValues("woTeam_type");
			String workingTime[] =
				request.getParameterValues("woTeam_workingTime");
			String startDate[] = request.getParameterValues("woTeam_startDate");
			String endDate[] = request.getParameterValues("woTeam_endDate");
			String role[] = request.getParameterValues("cboRole");
			String teamNote[] = request.getParameterValues("woTeam_note");
			String teamID[] = request.getParameterValues("woTeam_TeamName");
			String qualification[] =
				request.getParameterValues("woTeam_qualification");

			// Find Name of Assignment
			// TODO Avoid to use Vector to store user reference by Id,
			// should use HashMap (hash table) with key = devId
			projectID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			ProjectInfo pinf = Project.getProjectInfo(projectID);
			projectCode = pinf.getProjectCode();

			// get Inputed data			
			Vector vConFlict = new Vector();
			for (int i = 0; i < strUserAccount.length; i++) {
				if ((strUserAccount[i] == null)
					|| ("".equals(strUserAccount[i])))
					continue;

				assInfo = new AssignmentInfo();
				assInfo.projectID = projectID;
				assInfo.account =
					ConvertString.toStandardizeString(strUserAccount[i]);
				assInfo.projectCode = projectCode;

				assInfo.type = Integer.parseInt(type[i]);
				assInfo.beginDate = CommonTools.parseSQLDate(startDate[i]);
				assInfo.endDate = CommonTools.parseSQLDate(endDate[i]);
				assInfo.teamID = Long.parseLong(teamID[i]);

				if ((workingTime[i] == null) || ("".equals(workingTime[i])))
					assInfo.workingTime = 0;
				else {
					DecimalFormat formater = new DecimalFormat("#.#");
					double tmpWorkingTime = Double.parseDouble(workingTime[i]);
					assInfo.workingTime =
						Double.parseDouble(formater.format(tmpWorkingTime));
				}

				assInfo.responsibilityID = Long.parseLong(role[i]);
				assInfo.qualification =
					ConvertString.toStandardizeString(qualification[i]);
				assInfo.note = ConvertString.toStandardizeString(teamNote[i]);

				// check if strUserAccount is correct account 
				UserInfo userInfo =
					UserProfileCaller.checkUserFilter(
						request,
						strUserAccount[i],
						null);
				if (userInfo != null) {
					assInfo.devName = userInfo.Name;
					assInfo.devID = userInfo.developerID;

					addChangeAuto(
						assInfo.projectID,
						Constants.ACTION_ADD,
						Constants.WO_TEAM,
						userInfo.Name,
						null,
						null);

					// Check if user allocation on this project and other projects > 100%
					// in some periods then return list of conflicts
					Vector vtProjectAllocationConflict =
						Assignments.checkProjectAllocationAdd(assInfo);
					if (vtProjectAllocationConflict == null) {
						Vector vtAllProjectAllocationConflict =
							Assignments.checkAllProjectAllocationAdd(assInfo);
						if (vtAllProjectAllocationConflict != null)
							vConFlict.addAll(vtAllProjectAllocationConflict);
					} else {
						vConFlict.addAll(vtProjectAllocationConflict);
					}

					Assignments.addAssignment(assInfo);

					//Check if the user has rights on the project, if not, add :
					// Hieunv1: We don't check for rights because a user has right for in all Projects but 
					// in others project this user can only view if user has assigned role for view.
					final long workUnitID =
						Long.parseLong(
							(String) session.getAttribute("workUnitID"));
					// if (Security.getUserRights("Project home", assInfo.devID, workUnitID) < RightForPage.RIGHT_VIEW){
					ResponsibilityInfo resInfo =
						Assignments.getAssignmentInfoByID(
							assInfo.responsibilityID);
					RolesInfo newInfo =
						new RolesInfo(
							assInfo.devID,
							workUnitID,
							resInfo.rightGroup,
							"");
					Roles.addRightOfUserByWorkUnit(newInfo);

				} else {
					userFilterErr = true;
					resultVector.addElement(assInfo);
				}
			}

			if (userFilterErr) {
				request.setAttribute("lastAssignment", resultVector);
				Fms1Servlet.callPage("woTeamAdd.jsp", request, response);
				// Return add new with error Account
			} else {
				if (vConFlict != null) {
					if (vConFlict.size() > 0) {
						request.setAttribute("allocationConflict", vConFlict);
					}
				}

				doLoadWOTeamList(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareBatchUpdateAssignment(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector teamList = (Vector) session.getAttribute("WOTeamList");
			String listUpdate = (String) request.getParameter("listUpdate");
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final ProjectDateInfo projectDateInfo =
				Project.getProjectDate(prjID);
			session.setAttribute("projectDateInfo", projectDateInfo);
			Vector vUpdate = new Vector();
			final StringTokenizer strAssUpdateIDList =
				new StringTokenizer(listUpdate == null ? "" : listUpdate, ",");
			Vector vUpdateListId = new Vector();
			int i = 0;
			while (strAssUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strAssUpdateIDList.nextToken());
			}

			int tSize = teamList.size();
			for (i = 0; i < tSize; i++) {
				AssignmentInfo info = (AssignmentInfo) teamList.elementAt(i);
				if (vUpdateListId.contains(info.assID + "")) {
					vUpdate.addElement(info);
				}
			}
			request.setAttribute("AssBatchUpdateList", vUpdate);
			Fms1Servlet.callPage("woTeamBatchUpdate.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doBatchUpdateAssignment(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			Vector resultVector = new Vector();
			AssignmentInfo assInfo = new AssignmentInfo();
			String projectCode = "";
			long projectID = 0;
			boolean userFilterErr = false;

			String assID[] = request.getParameterValues("assID");
			String strUserAccount[] =
				request.getParameterValues("strAccountName");
			String type[] = request.getParameterValues("woTeam_type");
			String workingTime[] =
				request.getParameterValues("woTeam_workingTime");
			String startDate[] = request.getParameterValues("woTeam_startDate");
			String endDate[] = request.getParameterValues("woTeam_endDate");
			String role[] = request.getParameterValues("cboRole");
			String teamNote[] = request.getParameterValues("woTeam_note");
			String teamID[] = request.getParameterValues("woTeam_TeamName");
			String qualification[] =
				request.getParameterValues("woTeam_qualification");

			projectID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			ProjectInfo pinf = Project.getProjectInfo(projectID);
			projectCode = pinf.getProjectCode();

			int inputSize = strUserAccount.length - 1;
			Vector vConFlict = new Vector();

			// get Inputed data
			for (int i = 0; i < inputSize; i++) {

				assInfo = new AssignmentInfo();
				assInfo.projectID = projectID;
				assInfo.assID = Long.parseLong(assID[i]);
				assInfo.account =
					ConvertString.toStandardizeString(strUserAccount[i]);
				assInfo.projectCode = projectCode;

				assInfo.type = Integer.parseInt(type[i]);
				assInfo.beginDate = CommonTools.parseSQLDate(startDate[i]);
				assInfo.endDate = CommonTools.parseSQLDate(endDate[i]);
				assInfo.teamID = Long.parseLong(teamID[i]);

				if ((workingTime[i] == null) || ("".equals(workingTime[i])))
					assInfo.workingTime = 0;
				else {
					DecimalFormat formater = new DecimalFormat("#.#");
					double tmpWorkingTime = Double.parseDouble(workingTime[i]);
					assInfo.workingTime =
						Double.parseDouble(formater.format(tmpWorkingTime));
				}

				assInfo.responsibilityID = Long.parseLong(role[i]);
				assInfo.qualification =
					ConvertString.toStandardizeString(qualification[i]);
				assInfo.note = ConvertString.toStandardizeString(teamNote[i]);

				// check if strUserAccount is correct account 
				UserInfo userInfo =
					UserProfileCaller.checkUserFilter(
						request,
						strUserAccount[i],
						null);
				if (userInfo != null) {
					assInfo.devName = userInfo.Name;
					assInfo.devID = userInfo.developerID;

					addChangeAuto(
						assInfo.projectID,
						Constants.ACTION_ADD,
						Constants.WO_TEAM,
						userInfo.Name,
						null,
						null);

					Vector vtProjectAllocationConflict =
						Assignments.checkProjectAllocationUpdate(assInfo);
					if (vtProjectAllocationConflict == null) {
						Vector vtAllProjectAllocationConflict =
							Assignments.checkAllProjectAllocationUpdate(
								assInfo);
						if (vtAllProjectAllocationConflict != null)
							vConFlict.addAll(vtAllProjectAllocationConflict);
					} else {
						vConFlict.addAll(vtProjectAllocationConflict);
					}

					Assignments.updateBatchAssignment(assInfo);

					final long workUnitID =
						Long.parseLong(
							(String) session.getAttribute("workUnitID"));

					ResponsibilityInfo resInfo =
						Assignments.getAssignmentInfoByID(
							assInfo.responsibilityID);
					RolesInfo newInfo =
						new RolesInfo(
							assInfo.devID,
							workUnitID,
							resInfo.rightGroup,
							"");
					Roles.addRightOfUserByWorkUnit(newInfo);

				} else {
					userFilterErr = true;
					resultVector.addElement(assInfo);
				}
			}

			if (userFilterErr) {
				request.setAttribute("lastAssignment", resultVector);
				Fms1Servlet.callPage(
					"woTeamBatchUpdate.jsp",
					request,
					response);
			} else {
				if (vConFlict != null) {
					if (vConFlict.size() > 0) {
						request.setAttribute("allocationConflict", vConFlict);
					}
				}

				doLoadWOTeamList(request, response);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdateAssignment(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final SimpleDateFormat formatter =
				new SimpleDateFormat("dd-MMM-yy");
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final long assID =
				Long.parseLong(request.getParameter("woTeam_assID"));
			final long workUnitID =
				Long.parseLong((String) session.getAttribute("workUnitID"));
			final String devId = request.getParameter("woTeam_devID");
			final String type = request.getParameter("woTeam_type");
			final String startDate = request.getParameter("woTeam_startDate");
			//
			final String endDate = request.getParameter("woTeam_endDate"); //
			final String qualification =
				request.getParameter("woTeam_qualification");
			final String teamID = request.getParameter("woTeam_TeamName");
			final String note = request.getParameter("woTeam_note");
			final String workingTime =
				request.getParameter("woTeam_workingTime");
			//
			final AssignmentInfo assInfo = new AssignmentInfo();
			Vector teamList = (Vector) session.getAttribute("WOTeamList");
			AssignmentInfo oldInfo = null;
			for (int i = 0; i < teamList.size(); i++) {
				oldInfo = (AssignmentInfo) teamList.elementAt(i);
				if (oldInfo.assID == assID)
					break;
			}
			assInfo.assID = assID;
			assInfo.projectID = prjID;
			assInfo.workUnitID = workUnitID;
			assInfo.type = Integer.parseInt(type);
			assInfo.teamID = Long.parseLong(teamID);
			assInfo.qualification = qualification;
			if ((startDate != "") && (startDate != null)) {
				final java.util.Date sDate = formatter.parse(startDate);
				final java.sql.Date sSqlDate =
					new java.sql.Date(sDate.getTime());
				assInfo.beginDate = sSqlDate;
			}
			if ((endDate != "") && (endDate != null)) {
				final java.util.Date eDate = formatter.parse(endDate);
				final java.sql.Date eSqlDate =
					new java.sql.Date(eDate.getTime());
				assInfo.endDate = eSqlDate;
			}
			if ((workingTime.equals("")) || (workingTime == null)) {
				assInfo.workingTime = 0;
			} else {
				DecimalFormat formater = new DecimalFormat("#.#");
				double tmpWorkingTime = Double.parseDouble(workingTime);
				assInfo.workingTime =
					Double.parseDouble(formater.format(tmpWorkingTime));
			}
			assInfo.responsibilityID = ResponsibilityCbo.parse(request);
			assInfo.note = note;
			assInfo.devID = Long.parseLong(devId);

			// TODO Avoid to use Vector to store user reference by Id,
			// should use HashMap (hash table) with key = devId
			// ProjectInfo pinf = new ProjectInfo(assInfo.projectID);
			ProjectInfo pinf = Project.getProjectInfo(assInfo.projectID);
			assInfo.projectCode = pinf.getProjectCode();
			UserInfo userInfo = UserHelper.getUser(assInfo.devID);
			assInfo.account = userInfo.account;
			assInfo.devName = userInfo.Name;
			//Update Priority into assInfo 
			assInfo.priority =
				Assignments.getPriorityOfResponsibility(
					assInfo.responsibilityID);

			// Check conflict of resource allocation
			Vector vtProjectAllocationConflict =
				Assignments.checkProjectAllocationUpdate(assInfo);
			if (vtProjectAllocationConflict != null) {
				//Error, return add new page with list of conflict periods
				request.setAttribute(
					"allocationConflict",
					vtProjectAllocationConflict);
				request.setAttribute("lastAssignment", assInfo);
				doPrepareTeamUpdate(request, response);
				// Return update with conflicts
			} else {
				// Check if user allocation on this project and other projects > 100%
				// in some periods then return list of conflicts
				Vector vtAllProjectAllocationConflict =
					Assignments.checkAllProjectAllocationUpdate(assInfo);

				// Modified from 12-Jan-08, include old assignment information
				// for integration with RMS
				Assignments.updateAssignment(assInfo, oldInfo);
				request.setAttribute(
					"allocationConflict",
					vtAllProjectAllocationConflict);

				String oldValue, newValue;
				oldValue = oldInfo.workingTime + "";
				newValue = assInfo.workingTime + "";
				if (oldInfo.workingTime != assInfo.workingTime) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_TEAM,
						oldInfo.devName + ">Working time",
						newValue,
						oldValue);
				}
				oldValue =
					formatter.format(
						new java.util.Date(oldInfo.beginDate.getTime()));
				newValue =
					formatter.format(
						new java.util.Date(assInfo.beginDate.getTime()));
				if (!oldInfo.beginDate.equals(assInfo.beginDate)) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_TEAM,
						oldInfo.devName + ">Start date",
						newValue,
						oldValue);
				}
				oldValue =
					formatter.format(
						new java.util.Date(oldInfo.endDate.getTime()));
				newValue =
					formatter.format(
						new java.util.Date(assInfo.endDate.getTime()));
				if (!oldInfo.endDate.equals(assInfo.endDate)) {
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_TEAM,
						oldInfo.devName + ">End date",
						newValue,
						oldValue);
				}
				doLoadWOTeamList(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doBatchDeleteAssignment(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long assID = 0;
			int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			long lDeveloperID = 0;

			String listUpdate = (String) request.getParameter("listUpdate");
			String devListUpdate =
				(String) request.getParameter("devListUpdate");

			final StringTokenizer strAssUpdateIDList =
				new StringTokenizer(listUpdate == null ? "" : listUpdate, ",");
			final StringTokenizer strDevUpdateIDList =
				new StringTokenizer(
					devListUpdate == null ? "" : devListUpdate,
					",");

			int i = 0;
			while (strAssUpdateIDList.hasMoreElements()) {
				assID = Long.parseLong(strAssUpdateIDList.nextToken());
				lDeveloperID = Long.parseLong(strDevUpdateIDList.nextToken());

				final long lWorkUnitID = WorkUnit.getWorkUnitByProjectId(prjID);
				// Find Name of Assignment
				Vector teamList = (Vector) session.getAttribute("WOTeamList");
				AssignmentInfo assignmentInfo = null;
				for (i = 0; i < teamList.size(); i++) {
					assignmentInfo = (AssignmentInfo) teamList.elementAt(i);
					if (assignmentInfo.assID == assID)
						break;
				}
				addChangeAuto(
					(int) prjID,
					Constants.ACTION_DELETE,
					Constants.WO_METRICS_PERFORMANCE,
					assignmentInfo.devName,
					null,
					null);
				//----------------
				// Assignments.deleteAssignment(assID, lDeveloperID, lWorkUnitID);
				Assignments.deleteAssignment(
					assignmentInfo,
					lDeveloperID,
					lWorkUnitID);
			}

			doLoadWOTeamList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteAssignment(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long assID = Long.parseLong(request.getParameter("woTeam_assID"));
			int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			long lDeveloperID =
				Long.parseLong(request.getParameter("Developer_id"));

			final long lWorkUnitID = WorkUnit.getWorkUnitByProjectId(prjID);
			// Find Name of Assignment
			Vector teamList = (Vector) session.getAttribute("WOTeamList");
			AssignmentInfo assignmentInfo = null;
			for (int i = 0; i < teamList.size(); i++) {
				assignmentInfo = (AssignmentInfo) teamList.elementAt(i);
				if (assignmentInfo.assID == assID)
					break;
			}
			addChangeAuto(
				(int) prjID,
				Constants.ACTION_DELETE,
				Constants.WO_METRICS_PERFORMANCE,
				assignmentInfo.devName,
				null,
				null);
			//----------------
			// Assignments.deleteAssignment(assID, lDeveloperID, lWorkUnitID);
			Assignments.deleteAssignment(
				assignmentInfo,
				lDeveloperID,
				lWorkUnitID);
			doLoadWOTeamList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//End of team---------------------------------------------------------------------------
	//WO Change-----------------------------------------------------------------------

	public static final void doLoadWOChangeList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			String change_source_page =
				request.getParameter("change_source_page");
			if (change_source_page != null) {
				session.setAttribute("change_source_page", change_source_page);
			} else {
				change_source_page =
					(String) session.getAttribute("change_source_page");
			}
			Vector changeList = null;
			if (change_source_page.equals("0")) {
				changeList = Project.getWOChangeList(prjID);
			} else if (change_source_page.equals("1")) {
				changeList = Project.getPLChangeList(prjID);
			}
			if (changeList == null) {
				changeList = new Vector();
			}
			session.setAttribute("WOChangeList", changeList);
			Fms1Servlet.callPage("woChange.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrepareChangeUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long changeID =
				Long.parseLong(request.getParameter("woChange_ID"));
			final String change_source_page =
				(String) session.getAttribute("change_source_page");
			WOChangeInfo info = null;
			if (change_source_page.equals("0")) {
				info = Project.getWOChange(changeID);
			} else if (change_source_page.equals("1")) {
				info = Project.getPLChange(changeID);
			}
			session.setAttribute("woChangeInfo", info);
			Fms1Servlet.callPage("woChangeUpdate.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrepareChangeAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("woChangeAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddChange(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final String item = request.getParameter("woChange_item");
			final String changes = request.getParameter("woChange_changes");
			final String reason = request.getParameter("woChange_reason");
			final String version = request.getParameter("woChange_version");
			final String action = request.getParameter("action");
			final WOChangeInfo info = new WOChangeInfo();
			info.projectID = prjID;
			info.item = item;
			info.changes = changes;
			info.reason = reason;
			info.version = version;
			info.action = action;
			final String change_source_page =
				(String) session.getAttribute("change_source_page");
			if (change_source_page.equals("0")) {
				Project.addWOChange(info);
			} else if (change_source_page.equals("1")) {
				Project.addPLChange(info);
			}
			doLoadWOChangeList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateChange(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final String changeID = request.getParameter("woChange_ID");
			final String item = request.getParameter("woChange_item");
			final String changes = request.getParameter("woChange_changes");
			final String reason = request.getParameter("woChange_reason");
			final String version = request.getParameter("woChange_version");
			final String action = request.getParameter("action");
			final WOChangeInfo info = new WOChangeInfo();
			info.changeID = Long.parseLong(changeID);
			info.projectID = prjID;
			info.item = item;
			info.changes = changes;
			info.reason = reason;
			info.version = version;
			info.action = action;
			final String change_source_page =
				(String) session.getAttribute("change_source_page");
			if (change_source_page.equals("0")) {
				Project.updateWOChange(info);
			} else if (change_source_page.equals("1")) {
				Project.updatePLChange(info);
			}
			doLoadWOChangeList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteChange(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long changeID =
				Long.parseLong((String) request.getParameter("woChange_ID"));
			final String change_source_page =
				(String) session.getAttribute("change_source_page");
			if (change_source_page.equals("0")) {
				Project.deleteWOChange(changeID);
			} else if (change_source_page.equals("1")) {
				Project.deletePLChange(changeID);
			}
			doLoadWOChangeList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//End of WO change----------------------------------------------------------------
	//WOSignature---------------------------------------------------------------------
	public static final void doLoadWOSignatureList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		doLoadWOSignatureList(request, response, "");
	}
	public static final void doLoadWOSignatureList(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			Vector sigList = Project.getApprovalList(prjID, 0);
			if (sigList == null) {
				sigList = new Vector();
			}
			request.setAttribute("WOSigList", sigList);
			Vector changeSigList = Project.getApprovalList(prjID, 1);
			if (changeSigList == null) {
				changeSigList = new Vector();
			}
			request.setAttribute("WOChangeSigList", changeSigList);
			Vector internalSigList = Project.getApprovalList(prjID, 2);
			if (internalSigList == null) {
				internalSigList = new Vector();
			}
			request.setAttribute("WOInternalSigList", internalSigList);
			Fms1Servlet.callPage("woSignature.jsp" + index, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateWOSignature(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			//security flaw Id should come from session
			final long lDeveloperId =
				Long.parseLong(request.getParameter("woSig_devID"));
			final int iSigApp =
				Integer.parseInt(request.getParameter("woSig_app"));
			final SignatureInfo signatureInfo = new SignatureInfo();
			signatureInfo.setProjectId(prjID);
			signatureInfo.setDeveloperId(lDeveloperId);
			signatureInfo.setApprovalStatus(iSigApp);
			Project.updateApproval(signatureInfo, 0);
			doLoadWOSignatureList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddWOApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long lProjectID =
				Long.parseLong((String) session.getAttribute("projectID"));
			String strAccountName = request.getParameter("strAccountName");
			String strType = request.getParameter("rdAccountName");
			strAccountName = ConvertString.toStandardizeString(strAccountName);
			UserInfo userInfo =
				UserHelper.doCheckUserAssigned(
					0,
					lProjectID,
					strAccountName,
					strType);
			if (userInfo == null
				|| !Project.addApproval(0, userInfo.developerID, lProjectID)) {
				request.setAttribute(StringConstants.FILLTER_USER_ERROR, "0");
			}
			doLoadWOSignatureList(
				request,
				response,
				"?Index=" + strAccountName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doResetWOApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			Project.resetApproval(0, prjID);
			doLoadWOSignatureList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteWOApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final String[] idArray =
				request.getParameterValues("delWOApprovalID");
			if (idArray != null) {
				for (int i = 0; i < idArray.length; i++) {
					final long appID = Long.parseLong(idArray[i]);
					Project.deleteApproval(appID);
				}
			}
			doLoadWOSignatureList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateChangeSignature(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final long lDeveloperId =
				Long.parseLong(request.getParameter("woChangeSig_devID"));
			final int iSigApp =
				Integer.parseInt(request.getParameter("woChangeSig_app"));
			final SignatureInfo signatureInfo = new SignatureInfo();
			signatureInfo.setProjectId(prjID);
			signatureInfo.setDeveloperId(lDeveloperId);
			signatureInfo.setApprovalStatus(iSigApp);
			Project.updateApproval(signatureInfo, 1);
			doLoadWOSignatureList(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddChangeApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long lProjectID =
				Long.parseLong((String) session.getAttribute("projectID"));
			String strAccountName = request.getParameter("strAccountName");
			String strType = request.getParameter("rdAccountName");
			strAccountName = ConvertString.toStandardizeString(strAccountName);
			UserInfo userInfo =
				UserHelper.doCheckUserAssigned(
					1,
					lProjectID,
					strAccountName,
					strType);
			if (userInfo == null
				|| !Project.addApproval(1, userInfo.developerID, lProjectID)) {
				request.setAttribute(StringConstants.FILLTER_USER_ERROR, "1");
			}
			doLoadWOSignatureList(
				request,
				response,
				"?Index=" + strAccountName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteChangeApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final String[] idArray =
				request.getParameterValues("delWOApprovalID2");
			if (idArray != null) {
				for (int i = 0; i < idArray.length; i++) {
					final long appID = Long.parseLong(idArray[i]);
					Project.deleteApproval(appID);
				}
			}
			doLoadWOSignatureList(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doResetChangeApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			Project.resetApproval(1, prjID);
			doLoadWOSignatureList(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateInternalSignature(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final long lDeveloperId =
				Long.parseLong(request.getParameter("woInternalSig_devID"));
			final int iSigApp =
				Integer.parseInt(request.getParameter("woInternalSig_app"));
			final SignatureInfo signatureInfo = new SignatureInfo();
			signatureInfo.setProjectId(prjID);
			signatureInfo.setDeveloperId(lDeveloperId);
			signatureInfo.setApprovalStatus(iSigApp);
			Project.updateApproval(signatureInfo, 2);
			doLoadWOSignatureList(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddInternalApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long lProjectID =
				Long.parseLong((String) session.getAttribute("projectID"));
			String strAccountName = request.getParameter("strAccountName");
			String strType = request.getParameter("rdAccountName");
			strAccountName = ConvertString.toStandardizeString(strAccountName);
			UserInfo userInfo =
				UserHelper.doCheckUserAssigned(
					2,
					lProjectID,
					strAccountName,
					strType);
			if (userInfo == null
				|| !Project.addApproval(2, userInfo.developerID, lProjectID)) {
				request.setAttribute(StringConstants.FILLTER_USER_ERROR, "2");
			}
			doLoadWOSignatureList(
				request,
				response,
				"?Index=" + strAccountName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteInternalApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final String[] idArray =
				request.getParameterValues("delWOApprovalID3");
			if (idArray != null)
				for (int i = 0; i < idArray.length; i++) {
					final long appID = Long.parseLong(idArray[i]);
					Project.deleteApproval(appID);
				}
			doLoadWOSignatureList(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doResetInternalApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			Project.resetApproval(2, prjID);
			doLoadWOSignatureList(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//End of WOSignature---------------------------------------------------------------------
	//Acceptance
	public static final void doLoadWOAcceptanceList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			final String[] acceptanceMatrix = Project.getWOAcceptance(prjID);
			final List teamEvaluationMatric =
				Evaluations.getTeamEvaluation(prjID);
			session.setAttribute("WOAcceptanceMatrix", acceptanceMatrix);
			session.setAttribute("teamEvaluationMatric", teamEvaluationMatric);
			Fms1Servlet.callPage("woAcceptanceView.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateTeamEvaluation(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage(
				"woTeamEvaluationUpdate.jsp",
				request,
				response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void TeamEvaluationSave(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			String temp = request.getParameter("def");
			final HttpSession session = request.getSession();
			final int prjID =
				Integer.parseInt((String) session.getAttribute("projectID"));
			String[] devId = request.getParameterValues("devID");
			String[] arrayHp = request.getParameterValues("HP");
			String[] arrayRole = request.getParameterValues("role");

			String[] arrayPC = request.getParameterValues("PC");

			String[] arrayNote = request.getParameterValues("Note");

			String[] fullArrayPC = new String[devId.length];

			if (arrayPC.length < devId.length) {
				int position = 0;
				for (int i = 0; i < devId.length - 1; i++) {
					if (devId[i]
						.toString()
						.equalsIgnoreCase(devId[i + 1].toString()))
						fullArrayPC[i] = arrayPC[position];
					else
						fullArrayPC[i] = arrayPC[position++];
				}
				fullArrayPC[devId.length - 1] = arrayPC[arrayPC.length - 1];
				for (int i = 0; i < devId.length; i++) {

					Evaluations.updateTeamEva(
						prjID,
						devId[i],
						arrayHp[i],
						fullArrayPC[i],
						arrayNote[i],
						arrayRole[i]);

				}
			} else {
				for (int i = 0; i < devId.length; i++) {

					Evaluations.updateTeamEva(
						prjID,
						devId[i],
						arrayHp[i],
						arrayPC[i],
						arrayNote[i],
						arrayRole[i]);

				}
			}

			final List teamEvaluationMatric =
				Evaluations.getTeamEvaluation(prjID);
			session.setAttribute("teamEvaluationMatric", teamEvaluationMatric);
			Fms1Servlet.callPage("woAcceptanceView.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateWOAcceptanceList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));
			String[] newValues = new String[4];
			//Start date
			String strAsset = request.getParameter("acceptance00");
			if (strAsset == null)
				strAsset = "";
			newValues[0] = strAsset;
			String strProblem = request.getParameter("acceptance10");
			if (strProblem == null)
				strProblem = "";
			newValues[1] = strProblem;
			String strRP = request.getParameter("acceptance20");
			if (strRP == null)
				strRP = "";
			newValues[2] = strRP;
			String strPro = request.getParameter("acceptance30");
			if (strPro == null)
				strPro = "";
			newValues[3] = strPro;

			Project.updateWOAcceptance(prjID, newValues);
			String[] oldValues =
				(String[]) session.getAttribute("WOAcceptanceMatrix");
			String name = null;
			for (int i = 0; i < 3; i++) {
				if (i == 0)
					name = "Assets";
				else if (i == 1)
					name = "Problems";
				else if (i == 2)
					name = "Reward and Penalty";
				if (newValues[i].trim().compareToIgnoreCase(oldValues[i]) != 0)
					addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.WO_ACCEPTANCE,
						name,
						newValues[i],
						oldValues[i]);
			}
			doLoadWOAcceptanceList(request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//End of Acceptance
	public static boolean addChangeAuto(
		long prjID,
	//all table ids are long (application standart)
	int ActionType,
		int Location,
		String name,
		Object newValue,
		Object oldValue) {
		try {
			String item = "";
			String changes = "";
			String oldVl = (String) oldValue;
			String newVl = (String) newValue;
			///////////check if every one is approved
			Vector sigList = Project.getApprovalList(prjID, 0);
			SignatureInfo signatureInfo = new SignatureInfo();
			if (sigList.size() == 0) {
				return false;
			}
			for (int i = 0; i < sigList.size(); i++) {
				signatureInfo = (SignatureInfo) sigList.elementAt(i);
				if (signatureInfo.getApprovalStatus() != 1) {
					return false;
				}
			}
			//////////////////////////////////////////
			if (oldVl == null)
				oldVl = "";
			if (newVl == null)
				newVl = "";
			final String reason = "";
			final String version = "";
			final String note = "";
			final WOChangeInfo info = new WOChangeInfo();
			switch (Location) {
				case Constants.WO_INFORMATION :
					item = "WO>Information>";
					break;
				case Constants.WO_DELIVERABLE :
					item = "WO>Deliverable>";
					break;
				case Constants.SCHEDULE_REVIEW_AND_TEST :
					item =
						"Schedule>Review and test activities list>Planned release date>";
					break;
				case Constants.MODULE_MODULE :
					item = "Module>Module list>";
					break;
				case Constants.WO_METRICS_PERFORMANCE :
					item = "WO>Metrics>Performance>";
					break;
				case Constants.WO_METRICS_STANDARD_METRIC :
					item = "WO>Standard metrics list>";
					break;
				case Constants.WO_METRICS_CUSTOM_METRIC :
					item = "WO>Custom metric list>";
					break;
				case Constants.WO_TEAM :
					item = "WO>Team>";
					break;
				case Constants.WO_ACCEPTANCE :
					item = "WO>Acceptance>";
					break;
			}
			item += name;
			item = ConvertString.trunc(item, 50);
			switch (ActionType) {
				case Constants.ACTION_ADD :
					changes = "Added new";
					break;
				case Constants.ACTION_DELETE :
					changes = "Deleted";
					break;
				case Constants.ACTION_UPDATE :
					changes =
						"Previous value \""
							+ oldVl
							+ "\" changed to \""
							+ newVl
							+ "\"";
					if (changes.length() > 200) {
						oldVl = ConvertString.trunc(oldVl, 75);
						newVl = ConvertString.trunc(newVl, 75);
						changes =
							"Previous value \""
								+ oldVl
								+ "\" changed to \""
								+ newVl
								+ "\"";
					}
					break;
			}
			info.projectID = prjID;
			info.item = item;
			info.changes = changes;
			info.reason = reason;
			info.version = version;
			info.note = note;
			Project.addWOChange(info);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static final void doExportWO(
		final HttpServletRequest request,
		final HttpServletResponse response,
		boolean isWO) {
		try {
			final HttpSession session = request.getSession();
			final long prjID =
				Long.parseLong((String) session.getAttribute("projectID"));

			// Added by anhtv08

			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			session.setAttribute("WOGeneralInfo", projectInfo);
			session.setAttribute(
				"PLConstraintList",
				Project.getConstraintList(prjID));
			session.setAttribute("PLAssumptionList",Project.getAssumptionList(prjID));
			Vector deliverableList = WorkProduct.getDeliverableList(prjID);
			Vector moduleVector =WorkProduct.getRemainingDeliverableList(prjID);
			if (moduleVector == null) {
				moduleVector = new Vector();
			}
			session.setAttribute("woModuleList", moduleVector);
			session.setAttribute("deliverableList", deliverableList);

			Vector performanceVector = Project.getPerformanceMetrics(prjID);
			session.setAttribute("WOPerformanceVector", performanceVector);
			Vector stdMetrics = Project.getStandardMetricList(prjID);
			session.setAttribute("WOStandardMetricMatrix", stdMetrics);

			// added anhtv08-start
			Vector stageList = Schedule.getStageList(prjID);
			StageInfo stageInfo = null;
			if (stageList != null) {
				for (int i = stageList.size() - 1; i >= 0; i--) {
					stageInfo = (StageInfo) stageList.elementAt(i);
					if ((stageInfo.aEndD != null)
						&& (stageInfo.actualBeginDate != null)
						&& (stageInfo.plannedEndDate != null)
						&& (stageInfo.plannedBeginDate != null)) {
						break;
					}
				}
			}
			if (stageInfo!=null) {
				Vector cmList =Project.getCustomerMetric(prjID, stageInfo.milestoneID);
				if(cmList==null)
				{
					cmList= new Vector();
				}
				session.setAttribute("WOCustomeMetricList", cmList);
			} 
			//end

			//			Vector cmList = Project.getWOCustomeMetricList(prjID);
			//			session.setAttribute("WOCustomeMetricList", cmList);
			// Team List
			Vector teamList = Assignments.getWOTeamList(prjID, null, null);
			if (teamList == null) {
				teamList = new Vector();
			}
			session.setAttribute("WOTeamList", teamList);

			Vector subTeamList = SubTeams.getWOSubTeamList(prjID);
			if (subTeamList == null) {
				subTeamList = new Vector();
			}
			session.setAttribute("WOSubTeamList", subTeamList);

			final Vector stageVt = Schedule.getStageList(prjID);
			session.setAttribute("stageVector", stageVt);

			// Signature List
			Vector sigList = Project.getApprovalList(prjID, 0);
			if (sigList == null) {
				sigList = new Vector();
			}
			session.setAttribute("WOSigList", sigList);
			// Change Signature List
			Vector changeSigList = Project.getApprovalList(prjID, 1);
			if (changeSigList == null) {
				changeSigList = new Vector();
			}
			session.setAttribute("WOChangeSigList", changeSigList);
			// Internal Signature List
			Vector internalSigList = Project.getApprovalList(prjID, 2);
			if (internalSigList == null) {
				internalSigList = new Vector();
			}
			session.setAttribute("WOInternalSigList", internalSigList);

			Vector changeList = Project.getWOChangeList(prjID);
			Vector changeVersionList = Project.getWOChangeVersionList(prjID);
			session.setAttribute("WOChangeList", changeList);
			session.setAttribute("WOChangeVersionList", changeVersionList);

			// ProjectInfo pnf = new ProjectInfo(prjID);
			ProjectInfo pnf = Project.getProjectInfo(prjID);
			final EffortInfo effortHeader = Effort.getEffortInfo(pnf);
			session.setAttribute("effortHeaderInfo", effortHeader);

			//Add by HaiMM - Start
			if(stageInfo!=null)
			{
				 Vector dpVt = Defect.getDPTask(prjID,stageInfo.milestoneID);
				if(dpVt== null)
				{
					dpVt = new Vector();
				}
				session.setAttribute("defectPrevention", dpVt);	
			}
			// Add by HaiMM - End
			if (isWO) {
				Fms1Servlet.callPage(
					"WO.jsp",
					request,
					response,
					Fms1Servlet.CONTENT_TYPE_WORD);
			} else {
				String[] acceptanceMatrix = Project.getWOAcceptance(prjID);
				session.setAttribute("WOAcceptanceMatrix", acceptanceMatrix);
				Fms1Servlet.callPage(
					"AN.jsp",
					request,
					response,
					Fms1Servlet.CONTENT_TYPE_WORD);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doImportDeliverable(
		final HttpServletRequest request,
		final HttpServletResponse response)
		throws BiffException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				FileItem formFileItem = processFormField(request, upload);
				InputStream inStream = formFileItem.getInputStream();
				readImportFile(request, inStream);
				doLoadDeliverableList(request, response);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
	}
	private static final FileItem processFormField(
		HttpServletRequest request,
		ServletFileUpload upload)
		throws FileUploadException {
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
	private static final void readImportFile(
		HttpServletRequest request,
		InputStream inStream)
		throws BiffException, IOException {
		final HttpSession session = request.getSession();
		Workbook workbook = Workbook.getWorkbook(inStream);

		Sheet sheet0 = workbook.getSheet(0);
		try {
			if (!sheet0
				.getCell(1, 68)
				.getContents()
				.trim()
				.equalsIgnoreCase("Import Deliverable")) {
				session.setAttribute("ImportFail", "fail");
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			session.setAttribute("ImportFail", "fail");
			return;
		}

		Sheet sheet = workbook.getSheet(1);

		for (int i = 1; i < 51; i++) {
			long id = Long.parseLong(sheet.getCell(1, i).getContents());
			if (id == -1) {
				session.setAttribute("ImportFail", "fail");
				return;
			}
		}
		int sessionPrjID =
			Integer.parseInt((String) session.getAttribute("projectID"));
		int prjID = Integer.parseInt(sheet.getCell(9, 1).getContents());
		if (sessionPrjID != prjID) {
			session.setAttribute("ImportFail", "fail");
			return;
		}

		//Vector IssueList = new Vector();
		int[] deliverableAdded = new int[50];
		for (int i = 0; i < 50; i++) {
			deliverableAdded[i] = -1;
		}

		java.util.Date currentDate = new java.util.Date();
		String sDate = CommonTools.dateFormat(currentDate);

		int k = 0;
		String name = "";
		for (int i = 1; i < 51; i++) {
			final ModuleInfo moduleInfo = new ModuleInfo();
			NumberCell check = (NumberCell) sheet.getCell(0, i);

			if (check.getValue() > 0) {
				moduleInfo.moduleID =
					Long.parseLong(sheet.getCell(1, i).getContents());
				moduleInfo.deliveryLocation =
					CommonTools.parseAllForImportFromExcelFile(
						sheet.getCell(2, i).getContents());
				moduleInfo.plannedReleaseDate =
					CommonTools.parseSQLDate(sheet.getCell(3, i).getContents());
				moduleInfo.rePlannedReleaseDate =
					CommonTools.parseSQLDate(sheet.getCell(4, i).getContents());
				moduleInfo.actualReleaseDate =
					CommonTools.parseSQLDate(sheet.getCell(5, i).getContents());
				moduleInfo.status =
					Integer.parseInt(sheet.getCell(6, i).getContents());
				moduleInfo.note =
					CommonTools.parseAllForImportFromExcelFile(
						sheet.getCell(7, i).getContents());
				moduleInfo.isDeliverable = 1;
				if (WorkProduct.updateDeliverable(moduleInfo) == true) {
					deliverableAdded[k] = i;
				} else {
					deliverableAdded[k] = -i;
				}
				k++;
				name =
					CommonTools.parseAllForImportFromExcelFile(
						sheet0.getCell(1, 72 + i).getContents());

				addChangeAuto(
					prjID,
					Constants.ACTION_ADD,
					Constants.WO_DELIVERABLE,
					name,
					null,
					null);
			}
		}
		session.setAttribute("AddedRecord", deliverableAdded);
		session.setAttribute("Imported", "true");
	}

	public static final void doImportTeam(
		final HttpServletRequest request,
		final HttpServletResponse response)
		throws BiffException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				FileItem formFileItem = processFormFieldTeam(request, upload);
				InputStream inStream = formFileItem.getInputStream();
				readImportFileTeam(request, inStream);
				doLoadWOTeamList(request, response);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
	}
	private static final FileItem processFormFieldTeam(
		HttpServletRequest request,
		ServletFileUpload upload)
		throws FileUploadException {
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
	private static final void readImportFileTeam(
		HttpServletRequest request,
		InputStream inStream)
		throws BiffException, IOException {
		final HttpSession session = request.getSession();
		Workbook workbook = Workbook.getWorkbook(inStream);

		Sheet sheet0 = workbook.getSheet(0);
		try {
			if (!sheet0
				.getCell(1, 124)
				.getContents()
				.trim()
				.equalsIgnoreCase("Import Team")) {
				session.setAttribute("ImportFail", "fail");
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			session.setAttribute("ImportFail", "fail");
			return;
		}
		//	import Team
		Sheet sheet = workbook.getSheet(1);

		int[] teamAdded = new int[50];
		for (int i = 0; i < 50; i++) {
			teamAdded[i] = 0;
		}
		final AssignmentInfo assInfo = new AssignmentInfo();
		assInfo.projectID =
			Integer.parseInt((String) session.getAttribute("projectID"));

		ProjectInfo pinf = Project.getProjectInfo(assInfo.projectID);
		assInfo.projectCode = pinf.getProjectCode();
		int k = 0;
		for (int i = 53; i < 103; i++) {
			final ModuleInfo moduleInfo = new ModuleInfo();
			NumberCell check = (NumberCell) sheet.getCell(0, i);

			if (check.getValue() > 0) {
				assInfo.account =
					ConvertString.toStandardizeString(
						sheet.getCell(1, i).getContents());
				assInfo.devName = sheet0.getCell(10, i + 73).getContents();
				assInfo.type =
					Integer.parseInt(sheet.getCell(2, i).getContents());
				String workingTime =
					CommonTools.parseAllForImportFromExcelFile(
						sheet.getCell(3, i).getContents());
				if ((workingTime.equals("N/A")) || (workingTime == null))
					assInfo.workingTime = 0;
				else
					assInfo.workingTime = Double.parseDouble(workingTime);

				assInfo.beginDate =
					CommonTools.parseSQLDate(sheet.getCell(4, i).getContents());
				assInfo.endDate =
					CommonTools.parseSQLDate(sheet.getCell(5, i).getContents());
				assInfo.responsibilityID =
					Long.parseLong(sheet.getCell(6, i).getContents());
				assInfo.note =
					CommonTools.parseAllForImportFromExcelFile(
						sheet.getCell(7, i).getContents());
				assInfo.devID =
					Long.parseLong(sheet.getCell(8, i).getContents());

				if (WorkOrderCaller.doAddAssignmentForImport(assInfo, request)
					== 3) {
					teamAdded[k] = i - 52;
				} else {
					teamAdded[k] = - (i - 52);
				}
				k++;
			}
		}
		session.setAttribute("AddedRecord", teamAdded);
		session.setAttribute("Imported", "true");
	}

	public static final int doAddAssignmentForImport(
		final AssignmentInfo assInfo,
		final HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();

			addChangeAuto(
				assInfo.projectID,
				Constants.ACTION_ADD,
				Constants.WO_TEAM,
				assInfo.devName,
				null,
				null);

			// Check user allocation on this project not > 100% in some periods
			Vector vtProjectAllocationConflict =
				Assignments.checkProjectAllocationAdd(assInfo);
			if (vtProjectAllocationConflict != null) {
				//Error, return add new page with list of conflict periods
				if (vtProjectAllocationConflict.size() > 0) {
					//request.setAttribute("allocationConflict", vtProjectAllocationConflict);
					return 1;
				} else {
					//request.setAttribute(StringConstants.WO_TEAM_ADD, "This assignment have been exist! please update it in assignments list");
					return 2;
				}
			} else {
				Vector vtAllProjectAllocationConflict =
					Assignments.checkAllProjectAllocationAdd(assInfo);
				Assignments.addAssignment(assInfo);

				final long workUnitID =
					Long.parseLong((String) session.getAttribute("workUnitID"));
				ResponsibilityInfo resInfo =
					Assignments.getAssignmentInfoByID(assInfo.responsibilityID);
				RolesInfo newInfo =
					new RolesInfo(
						assInfo.devID,
						workUnitID,
						resInfo.rightGroup,
						"");
				Roles.addRightOfUserByWorkUnit(newInfo);
				return 3;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// added by anhtv08-start
	// date
	public static final void doMigrateCustomerMetricsTable(
		HttpServletRequest request,
		HttpServletResponse response) {
		try {
			Project.doMigrateCustomerMetricsData();
		} catch (Exception ex) {

		}
		String isFinish = "1";
		HttpSession session = request.getSession();
		session.setAttribute("isFinish", isFinish);
		Fms1Servlet.callPage("projectHome.jsp", request, response);
	}
}