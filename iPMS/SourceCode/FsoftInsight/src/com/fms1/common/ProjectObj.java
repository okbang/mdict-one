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
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.infoclass.*;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Language;
/**
 * Is to be merged with Project object
 *
 */
public final class ProjectObj implements Constants {
	/**
	 * Get all trainings for one project.
	 */
	public static final void doGetTrainingList(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			session.setAttribute("caller", String.valueOf(TRAINING_CALLER));
			final Vector vtTrain = Project.getTrainingList(prjID);
			int i = (vtTrain.size() + 19) / 20;
			if (i == 0)
				i = 1;
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			session.setAttribute("trainingVector", vtTrain);
			session.setAttribute("trainPageTotal", String.valueOf(i));
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
// --Recycle Bin START (2/17/04 3:08 PM):
//	/**
//	 * Get one trainings for project.
//	 */
//	public static final void doGetTraining(final HttpServletRequest request, final HttpServletResponse response) {
//		try {
//			final String trainIdStr = request.getParameter("trainingID");
//			if (trainIdStr == null)
//				return;
//			final int trainID = Integer.parseInt(trainIdStr);
//			final TrainingInfo trainInfo = Project.getTraining(trainID);
//			final HttpSession session = request.getSession();
//			session.setAttribute("trainingInfo", trainInfo);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//	}
// --Recycle Bin STOP (2/17/04 3:08 PM)
	public static final void doAddTraining(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final TrainingInfo trainInfo = new TrainingInfo();
			trainInfo.prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			trainInfo.duration = request.getParameter("txtDuration").trim();
			trainInfo.participant = request.getParameter("txtParticipant").trim();
			trainInfo.waiver = request.getParameter("txtWaiver").trim();
			trainInfo.topic = request.getParameter("txtTopic").trim();
			/*
			final String startD = request.getParameter("txtStartD").trim();
			final String endD = request.getParameter("txtEndD").trim();
			final String actualD = request.getParameter("txtActualD").trim();
			trainInfo.setVerifyBy(request.getParameter("verifyBy"));
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			trainInfo.trainingID = 0; //Integer.parseInt(trainID);
			trainInfo.startD = new java.sql.Date(formatter.parse(startD).getTime());
			trainInfo.endD = new java.sql.Date(formatter.parse(endD).getTime());
			if (actualD.compareTo("") == 0) {
				trainInfo.actualEndD = null;
			}
			else {
				trainInfo.actualEndD = new java.sql.Date(formatter.parse(actualD).getTime());
			}
			*/
			if (Project.addTraining(trainInfo) ) {
				ProjectPlanCaller.addChangeAuto(
					trainInfo.prjID,
					Constants.ACTION_ADD,
					Constants.PL_TRAINING,
					"Training plan list>" + trainInfo.topic,
					null,
					null);
				return;
			}
			final Language lang = (Language) session.getAttribute("lang");
			session.setAttribute("trainingError", lang.errTrain);
			Fms1Servlet.callPage("trainingAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUppdateTraining(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final TrainingInfo trainInfo = new TrainingInfo();
			final HttpSession session = request.getSession();
			trainInfo.prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			trainInfo.trainingID = Integer.parseInt(request.getParameter("trainingID").trim());
			trainInfo.duration = request.getParameter("txtDuration").trim();
			trainInfo.participant = request.getParameter("txtParticipant").trim();
			trainInfo.waiver = request.getParameter("txtWaiver").trim();
			trainInfo.topic = request.getParameter("txtTopic").trim();

			/*
			final String startD = request.getParameter("txtStartD").trim();
			final String endD = request.getParameter("txtEndD").trim();
			final String actualD = request.getParameter("txtActualD").trim();
			trainInfo.setVerifyBy(request.getParameter("verifyBy"));
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			trainInfo.startD = new java.sql.Date(formatter.parse(startD).getTime());
			trainInfo.endD = new java.sql.Date(formatter.parse(endD).getTime());
			if (actualD.compareTo("") == 0) {
				trainInfo.actualEndD = null;
			}
			else {
				trainInfo.actualEndD = new java.sql.Date(formatter.parse(actualD).getTime());
			}
			*/
			if (Project.updateTraining(trainInfo)) {

				Vector vt=(Vector)session.getAttribute("trainingVector");
				TrainingInfo oldTrainInfo=null;
				for (int i=0;i<vt.size();i++){
					oldTrainInfo=(TrainingInfo)vt.elementAt(i);
					if (oldTrainInfo.trainingID==trainInfo.trainingID)
						break;
				}


				String newValue, oldValue;
				newValue = trainInfo.topic;
				oldValue = oldTrainInfo.topic;
				if (!newValue.equalsIgnoreCase(oldValue))
					ProjectPlanCaller.addChangeAuto(
						trainInfo.prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_TRAINING,
						"Training list>Topic",
						newValue,
						oldValue);
				newValue = trainInfo.description;
				oldValue = oldTrainInfo.description;
				if (!newValue.equalsIgnoreCase(oldValue))
					ProjectPlanCaller.addChangeAuto(
						trainInfo.prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_TRAINING,
						"Training list>Description",
						newValue,
						oldValue);
				newValue = trainInfo.participant;
				oldValue = oldTrainInfo.participant;
				if (!newValue.equalsIgnoreCase(oldValue))
					ProjectPlanCaller.addChangeAuto(
						trainInfo.prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_TRAINING,
						"Training list>Participant",
						newValue,
						oldValue);
				newValue = trainInfo.waiver;
				oldValue = oldTrainInfo.waiver;
				if (!newValue.equalsIgnoreCase(oldValue))
					ProjectPlanCaller.addChangeAuto(
						trainInfo.prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_TRAINING,
						"Training list>waiver",
						newValue,
						oldValue);

				return;
			}
			final Language lang = (Language) session.getAttribute("lang");
			session.setAttribute("trainingError", lang.errTrain);
			Fms1Servlet.callPage("trainingUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *
	 * Delete Training
	 *
	 */
	public static final void doDeleteTraining(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String trainIDstr = request.getParameter("txtTrainingID");
			final int trainID = Integer.parseInt(trainIDstr);
			if (Project.deleteTraining(trainID)) {
				Vector vtTraining = (Vector)session.getAttribute("trainingVector");
				TrainingInfo trainingInfo = new TrainingInfo();
				for(int i = 0; i < vtTraining.size(); i++){
					trainingInfo = (TrainingInfo) vtTraining.get(i);
					if (trainingInfo.trainingID == trainID){
						break;
					}
				}
				ProjectPlanCaller.addChangeAuto(
					Integer.parseInt(prjID),
					Constants.ACTION_DELETE,
					Constants.PL_TRAINING,
					"Training plan list>" + trainingInfo.topic,
					null,
					null);
				return;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
