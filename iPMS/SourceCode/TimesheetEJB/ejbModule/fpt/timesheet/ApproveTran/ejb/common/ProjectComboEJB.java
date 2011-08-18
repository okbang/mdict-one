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
 
 package fpt.timesheet.ApproveTran.ejb.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.connection.WSConnectionPooling;

/**
 * Bean implementation class for Enterprise Bean: ProjectCombo
 * @version 1.0 25-Apr-03
 * @author
 */
public class ProjectComboEJB implements SessionBean {

	private static final Logger logger = Logger.getLogger(ProjectComboEJB.class);

	//*******************************************
	// Internal properties
	private Connection con;
	private SessionContext mySessionCtx;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;

	private String strClassName = "ProjectComboEJB";
	//*******************************************
	/**
	 * getSessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}

	/**
	 * setSessionContext
	 */
	public void setSessionContext(javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws CreateException {
		try {
			ds = conPool.getDS();
		}
		catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}

	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}

	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}
    
	/**
	 * Method genSQLForTracking
	 * generate WHERE condition due to project status
	 * @param intProjectStatus - status of watched project
	 * @return condition string
	 */
	private String genSQLForTracking(int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		strTemp.append(" SELECT DISTINCT P.status ");
		strTemp.append(" FROM project P ");
		strTemp.append(" WHERE p.archive_status != 4 AND P.status IN (");
		strTemp.append(Timesheet.PROJECT_STATUS_ONGOING).append(",");
		strTemp.append(Timesheet.PROJECT_STATUS_CLOSED).append(")");
		
		//logger.debug(strClassName + ".genSQLForTracking() = " + strTemp.toString());
		return strTemp.toString();
	}

	/**
	 * Method genSQLForSupporter
	 * generate SQL statement for querying list of projects a supporter can log timesheet to
	 * @param intProjectStatus - status of watched project
	 * @return SQL statement
	 */
	private String genSQLForSupporter(int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		if (intProjectStatus != Timesheet.PROJECT_STATUS_NULL) {
			// Supporter (member of support group) can log timesheet to every project                                          
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P ");
			strTemp.append(" WHERE P.archive_status != 4 ");
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			strTemp.append(" ORDER BY ordercode ");
		}
		else {
			strTemp.append(" SELECT DISTINCT P.status FROM project P WHERE p.archive_status != 4 ");
		}
		return strTemp.toString();
	}

	/**
	 * Method genSQLStatusCondition
	 * generate WHERE condition due to project status
	 * @param intProjectStatus - status of watched project
	 * @return condition string
	 */
	private static String genSQLStatusCondition(int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		if (intProjectStatus == Timesheet.PROJECT_STATUS_RUNNING) {
			strTemp.append(" AND P.status IN (");
			strTemp.append(Timesheet.PROJECT_STATUS_ONGOING).append(",");
			strTemp.append(Timesheet.PROJECT_STATUS_TENTATIVE).append(")");
		}
		else if (intProjectStatus == Timesheet.PROJECT_STATUS_ONGOING_CLOSED) {
			strTemp.append(" AND P.status IN (");
			strTemp.append(Timesheet.PROJECT_STATUS_ONGOING).append(",");
			strTemp.append(Timesheet.PROJECT_STATUS_CLOSED).append(")");
		}
		else if (intProjectStatus == Timesheet.PROJECT_STATUS_ONGOING_TENTATIVE_CLOSED) {
			strTemp.append(" AND P.status IN (");
			strTemp.append(Timesheet.PROJECT_STATUS_ONGOING).append(",");
			strTemp.append(Timesheet.PROJECT_STATUS_TENTATIVE).append(",");
			strTemp.append(Timesheet.PROJECT_STATUS_CLOSED).append(")");
		}
		else if (intProjectStatus == Timesheet.PROJECT_STATUS_ONGOING) {
			strTemp.append(" AND P.status IN (");
			strTemp.append(Timesheet.PROJECT_STATUS_ONGOING).append(")");
		}
		else if (intProjectStatus != Timesheet.PROJECT_STATUS_ALL) {
			strTemp.append(" AND P.status=");
			strTemp.append(intProjectStatus);
		}
		return strTemp.toString();
	}

	/**
	 * Method genSQLForDeveloper
	 * generate SQL statement for querying list of projects a developer can log timesheet to
	 * @param intDeveloperId - ident.number of developer
	 * @param intProjectStatus - status of watched project
	 * @return SQL statement
	 */
	private String genSQLForDeveloper(int intDeveloperID, int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		if (intProjectStatus != Timesheet.PROJECT_STATUS_NULL) {
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P, assignment A ");
            
			// Normal developer can access to project he's assigned to, if project is not closed
			strTemp.append(" WHERE ");
			strTemp.append(" P.archive_status != 4 ");
			strTemp.append(" AND A.project_id = P.project_id ");
			strTemp.append(" AND A.developer_id = ").append(intDeveloperID);
			strTemp.append(genSQLStatusCondition(intProjectStatus));

			strTemp.append(" UNION ");
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name,P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P");
			// Developer can access to MISC project approved by group leader
			strTemp.append(" WHERE p.archive_status != 4 AND (( P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			strTemp.append(" AND p.group_name = (select d.group_name from developer d where d.developer_id= ").append(intDeveloperID);
			strTemp.append(genSQLStatusCondition(intProjectStatus));

			// Developer can access to internal project of his group
			strTemp.append(")) OR (P.type = ").append(Timesheet.PROJECTTYPE_INTERNAL);
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			strTemp.append(" AND P.group_name = ");
			strTemp.append(" (SELECT group_name FROM developer ");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperID).append(")) ");

			// Finally, developer can access to all SEPG & FIST projects
			strTemp.append(" OR  P.code = 'MISC' ");
			strTemp.append(" OR (P.group_name IN ('SEPG','FIST') ");
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			strTemp.append(" )) ");

			strTemp.append(" ORDER BY ordercode");
		}
		else {
			strTemp.append(" SELECT DISTINCT P.status ");
			strTemp.append(" FROM project P, assignment A");

			// Normal developer has access to project he's assigned to, if project is not closed
			strTemp.append(" WHERE ");
			strTemp.append(" P.archive_status != 4 ");
			strTemp.append(" AND A.project_id = P.project_id ");
			strTemp.append(" AND A.developer_id = ").append(intDeveloperID);

			strTemp.append(" UNION ");
			strTemp.append(" SELECT DISTINCT P.status ");
			strTemp.append(" FROM project P ");
			// Developer has access to MISC project approved by group leader
			strTemp.append(" WHERE p.archive_status != 4 AND (( P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			strTemp.append(" AND p.group_name = (select d.group_name from developer d where d.developer_id= ").append(intDeveloperID);
			// Developer has access to internal project of his group
			strTemp.append(" )) OR ((P.type = ").append(Timesheet.PROJECTTYPE_INTERNAL);
			strTemp.append(" OR P.type = ").append(Timesheet.PROJECTTYPE_EXTERNAL);
			strTemp.append(") AND P.group_name = ");
			strTemp.append(" (SELECT group_name FROM developer ");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperID).append(" )) ");
			// Finally, developer has access to all SEPG & FIST projects
			strTemp.append(" OR  P.code = 'MISC' ");
			strTemp.append(" OR P.group_name IN ('SEPG','FIST') ");
			strTemp.append(" ) ");
		}
		logger.debug(strClassName + ".genSQLForDeveloper() = " + strTemp.toString());
		return strTemp.toString();
	}

	/**
	 * Method genSQLForProjectLeader
	 * generate SQL statement for querying list of projects a project leader can approve
	 * @param intDeveloperID - ident.number of leader
	 * @param intProjectStatus - status of watched project
	 * @param intProjectType - type of watched project
	 * @return SQL statement
	 */
	private String genSQLForProjectLeader(int intDeveloperID, int intProjectStatus, int intProjectType) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		if (intProjectStatus != Timesheet.PROJECT_STATUS_NULL) {
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P,developer D, assignment A ");
			strTemp.append(" WHERE ");
			// PM or PTL can approve timesheet for project
			strTemp.append(" p.archive_status != 4 AND a.project_id = p.project_id AND a.developer_id = d.developer_id AND ((a.response = 2) OR (a.response = 3)) AND D.developer_id = ").append(intDeveloperID);
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			//Modified 2003-11-26
			if (intProjectType == Timesheet.PROJECT_STATUS_ALL) {
				//Do nothing
			}else if (intProjectType == Timesheet.PROJECTTYPE_INTERNAL || intProjectType == Timesheet.PROJECTTYPE_EXTERNAL){
				strTemp.append(" AND P.type IN ( ").append(Timesheet.PROJECTTYPE_INTERNAL);
				strTemp.append(" , ").append(Timesheet.PROJECTTYPE_EXTERNAL);
				strTemp.append(" )");
			}else if(intProjectType == Timesheet.PROJECTTYPE_PUBLIC) {
				strTemp.append(" AND P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			}
			strTemp.append(" ORDER BY ordercode");
		}
		else {
			strTemp.append("SELECT DISTINCT P.status");
			strTemp.append(" FROM project P,developer D, assignment A");
			strTemp.append(" WHERE");
			// PM or PTL can approve timesheet for project
			strTemp.append(" P.archive_status != 4 AND a.project_id = p.project_id AND a.developer_id = d.developer_id AND ((a.response = 2) OR (a.response = 3)) AND D.developer_id = ").append(intDeveloperID);
			//Modified 2003-11-26
			if (intProjectType == Timesheet.PROJECT_STATUS_ALL) {
				//Do nothing
			}else if (intProjectType == Timesheet.PROJECTTYPE_INTERNAL || intProjectType == Timesheet.PROJECTTYPE_EXTERNAL){
				strTemp.append(" AND P.type IN ( ").append(Timesheet.PROJECTTYPE_INTERNAL);
				strTemp.append(" , ").append(Timesheet.PROJECTTYPE_EXTERNAL);
				strTemp.append(" )");
			}else if(intProjectType == Timesheet.PROJECTTYPE_PUBLIC) {
				strTemp.append(" AND P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			}
		}
		//logger.debug(strClassName + ".genSQLForProjectLeader() = " + strTemp.toString());
		return strTemp.toString();
	}

	/**
	 * Method genSQLForGroupLeader
	 * generate SQL statement for querying list of projects a group leader can approve
	 * @param intDeveloperId - ident.number of leader
	 * @param intProjectStatus - status of watched project
	 * @param intProjectType - type of watched project
	 * @return SQL statement
	 */
	private String genSQLForGroupLeader(int intDeveloperID, int intProjectStatus, int intProjectType) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		if (intProjectStatus != Timesheet.PROJECT_STATUS_NULL) {
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P ");

			// Group leader can approve timesheet for project of group he's leading
			strTemp.append(" WHERE P.archive_status != 4 AND P.group_name = (SELECT group_name FROM developer ");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperID).append(" ) ");
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			//Modified 2008-06-20
			if (intProjectType == Timesheet.PROJECT_STATUS_ALL) {
				//Do nothing
			}else if (intProjectType == Timesheet.PROJECTTYPE_PUBLIC){
				strTemp.append(" AND P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			}else{
				strTemp.append(" AND P.type IN ( ").append(Timesheet.PROJECTTYPE_INTERNAL);
				strTemp.append(" , ").append(Timesheet.PROJECTTYPE_EXTERNAL);
				strTemp.append(" )");
			}
			// Group leader may be a project leader of other group's project
			strTemp.append(" UNION ");
			strTemp.append(" SELECT DISTINCT P.project_id, '(' || P.group_name || ')' || P.code as code, ");
			strTemp.append(" P.name, '(' || P.group_name || ')' || UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P, developer D ");
			strTemp.append(" WHERE ");
			// This user lead projects of other groups 
			strTemp.append(" P.archive_status != 4 AND P.leader = D.account AND P.group_name <> D.group_name AND D.developer_id = ").append(intDeveloperID);
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			
			if (intProjectType == Timesheet.PROJECT_STATUS_ALL) {
				//Do nothing
			}else if (intProjectType == Timesheet.PROJECTTYPE_PUBLIC){
				strTemp.append(" AND P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			}else{
				strTemp.append(" AND P.type IN ( ").append(Timesheet.PROJECTTYPE_INTERNAL);
				strTemp.append(" , ").append(Timesheet.PROJECTTYPE_EXTERNAL);
				strTemp.append(" )");
			}
		}
		else {
			strTemp.append("SELECT DISTINCT P.status");
			strTemp.append(" FROM project P");
			// Group leader may be a project leader of other group's project
			strTemp.append(" WHERE p.archive_status != 4 AND P.group_name = (SELECT group_name FROM developer");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperID).append(")");
			if (intProjectType == Timesheet.PROJECT_STATUS_ALL) {
				//Do nothing
			}else if (intProjectType == Timesheet.PROJECTTYPE_PUBLIC){
				strTemp.append(" AND P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			}else{
				strTemp.append(" AND P.type IN ( ").append(Timesheet.PROJECTTYPE_INTERNAL);
				strTemp.append(" , ").append(Timesheet.PROJECTTYPE_EXTERNAL);
				strTemp.append(" )");
			}
			// Group leader may be a project leader of other group's project
			strTemp.append(" UNION ");
			strTemp.append(" SELECT DISTINCT P.status ");
			strTemp.append(" FROM project P,developer D ");
			strTemp.append(" WHERE ");
			// This user lead projects of other groups 
			strTemp.append(" P.archive_status != 4 AND P.leader = D.account AND P.group_name <> D.group_name AND D.developer_id = ").append(intDeveloperID);
			
			if (intProjectType == Timesheet.PROJECT_STATUS_ALL) {
				//Do nothing
			}else if (intProjectType == Timesheet.PROJECTTYPE_PUBLIC){
				strTemp.append(" AND P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			}else{
				strTemp.append(" AND P.type IN ( ").append(Timesheet.PROJECTTYPE_INTERNAL);
				strTemp.append(" , ").append(Timesheet.PROJECTTYPE_EXTERNAL);
				strTemp.append(" )");
			}
		}
		//logger.debug(strClassName + ".genSQLForGroupLeader() = " + strTemp.toString());
		
		System.out.println(strTemp.toString());
		return strTemp.toString();
	}

	/**
	 * genSQLForQA
	 * generate SQL statement for querying list of projects a QA can approve
	 * @param intDeveloperId - ident.number of QA
	 * @param intProjectStatus - status of watched project
	 * @return SQL statement
	 */
	private String genSQLForQA(int intDeveloperID, int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		if (intProjectStatus != Timesheet.PROJECT_STATUS_NULL) {
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P");

			// QA can approve for all projects, except MISC
			strTemp.append(" WHERE P.archive_status != 4 AND P.type IN (");
			strTemp.append(Timesheet.PROJECTTYPE_EXTERNAL).append(",");
			strTemp.append(Timesheet.PROJECTTYPE_INTERNAL).append(")");

			strTemp.append(genSQLStatusCondition(intProjectStatus));
			strTemp.append(" ORDER BY ordercode");
		}
		else {
			strTemp.append(" SELECT DISTINCT P.status");
			strTemp.append(" FROM project P");
			// QA can approve for all projects, except MISC
			strTemp.append(" WHERE P.archive_status != 4 AND P.type IN (");
			strTemp.append(Timesheet.PROJECTTYPE_EXTERNAL).append(",");
			strTemp.append(Timesheet.PROJECTTYPE_INTERNAL).append(")");
		}
		//logger.debug(strClassName + ".genSQLForQA() = " + strTemp.toString());
		return strTemp.toString();
	}

	/**
	 * genSQLForManager
	 * generate SQL statement for querying list of projects a manager can view
	 * @param intProjectStatus - status of watched project
	 * @return SQL statement
	 */
	private String genSQLForManager(int intProjectStatus) {
		// manager has access to all projects like supporter
		return genSQLForSupporter(intProjectStatus);
	}

	private String genSQLForExternalUser(int intDeveloperId, int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		strTemp.append(" SELECT DISTINCT p.project_id, p.code, p.NAME, UPPER (p.code) AS ordercode, p.group_name, p.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
		strTemp.append(" FROM project p, assignment a ");
		strTemp.append(" WHERE p.archive_status != 4 AND ");
		strTemp.append(" AND a.project_id = p.project_id ");
		strTemp.append(" AND a.developer_id  = " + intDeveloperId);
		strTemp.append(genSQLStatusCondition(intProjectStatus));
		strTemp.append(" ORDER BY ordercode");
		//logger.debug(strClassName + ".genSQLForExternalUser() = " + strTemp.toString());
		return strTemp.toString();
	}

	/**
	 * genSQLForExternalGroupManager
	 * generate SQL statement for querying list of projects an external manager can view
	 * @param intDeveloperId - ident.number of manager
	 * @param intProjectStatus - status of watched project
	 * @return SQL statement
	 */
	private String genSQLForExternalGroupManager(int intDeveloperId, int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		if (intProjectStatus != Timesheet.PROJECT_STATUS_NULL) {
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P ");

			// external group manager has access to project of his group
			strTemp.append(" WHERE P.archive_status != 4 AND ( P.group_name IN (SELECT group_name FROM developer ");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperId).append(" ) ");
			strTemp.append(genSQLStatusCondition(intProjectStatus));

			// external group manager has access to SEPG & FIST projects
			strTemp.append(" OR (P.group_name IN ('SEPG','FIST') ");
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			strTemp.append(" ) ");

			// external group manager has access to MISC project
			strTemp.append(" OR (P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			strTemp.append(" ) ) ");

			strTemp.append(" ORDER BY ordercode ");
		}
		else {
			strTemp.append(" SELECT DISTINCT P.status ");
			strTemp.append(" FROM project P ");
			// external group manager has access to project of his group
			strTemp.append(" WHERE p.archive_status != 4 AND ( P.group_name IN (SELECT group_name FROM developer ");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperId).append(" ) ");
			// external group manager has access to SEPG & FIST projects
			strTemp.append(" OR P.group_name IN ('SEPG','FIST') ");
			// external group manager has access to MISC project
			strTemp.append(" OR P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			strTemp.append(" ) ");
		}
		//logger.debug(strClassName + ".genSQLForExternalGroupManager() = " + strTemp.toString());        
		return strTemp.toString();
	}

	/**
	 * genSQLForExternalProjectManager
	 * generate SQL statement for querying list of projects an external manager can view
	 * @param intDeveloperID - ident.number of manager
	 * @param intProjectStatus - status of watched project
	 * @return SQL statement
	 */
	private String genSQLForExternalProjectManager(int intDeveloperID, int intProjectStatus) {
		// external project manager has read-only access to project like developer
		return genSQLForDeveloper(intDeveloperID, intProjectStatus);
	}

	/**
	 * genSQLForCommunicator
	 * generate SQL statement for querying list of projects an communicator can view
	 * @param intDeveloperId - ident.number of communicator
	 * @param intProjectStatus - status of watched project
	 * @return SQL statement
	 */
	private String genSQLForCommunicator(int intDeveloperId, int intProjectStatus) {
		StringBuffer strTemp = new StringBuffer();
		// Get project status or get project data
		if (intProjectStatus != Timesheet.PROJECT_STATUS_NULL) {
			strTemp.append(" SELECT DISTINCT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P, assignment A ");

			// Normal communicator can access to project he/she 's assigned to, if project is not closed
			strTemp.append(" WHERE ");
			strTemp.append(" p.archive_status != 4 ");
			strTemp.append(" AND A.project_id = P.project_id AND A.developer_id = ").append(intDeveloperId);
			strTemp.append(genSQLStatusCondition(intProjectStatus));

			strTemp.append(" UNION ");
			strTemp.append(" SELECT P.project_id, P.code, P.name, UPPER(P.code) AS ordercode, P.group_name, P.status, TO_CHAR (p.start_date, 'mm/dd/yy') AS start_date, TO_CHAR (p.plan_start_date, 'mm/dd/yy') AS plan_start_date "); // Modified by PhuNT
			strTemp.append(" FROM project P ");
			// Communicator has access to project of his/her group
			strTemp.append(" WHERE p.archive_status != 4 AND ( P.group_name IN (SELECT group_name FROM developer ");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperId).append(" ) ");
			strTemp.append(genSQLStatusCondition(intProjectStatus));

			// Communicator has access to SEPG & FIST projects
			//strTemp.append(" OR (P.group_name IN ('SEPG','FIST') ");
			//strTemp.append(genSQLStatusCondition(intProjectStatus));
			//strTemp.append(" ) ");

			// Communicator has access to MISC project
			strTemp.append(" OR (P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			strTemp.append(genSQLStatusCondition(intProjectStatus));
			strTemp.append(" ) ) ");

			strTemp.append(" ORDER BY ordercode ");
		}
		else {
			strTemp.append(" SELECT DISTINCT P.status ");
			strTemp.append(" FROM project P, assignment A ");

			// Normal communicator can access to project he/she 's assigned to, if project is not closed
			strTemp.append(" WHERE ");
			strTemp.append(" P.archive_status != 4 ");
			strTemp.append(" AND A.project_id = P.project_id AND A.developer_id = ").append(intDeveloperId);

			strTemp.append(" UNION ");
			strTemp.append(" SELECT P.status ");
			strTemp.append(" FROM project P ");
			// Communicator has access to project of his/her group
			strTemp.append(" WHERE p.archive_status != 4 AND ( P.group_name IN (SELECT group_name FROM developer ");
			strTemp.append(" WHERE developer_id = ").append(intDeveloperId).append(" ) ");
			// Communicator has access to SEPG & FIST projects
			//strTemp.append(" OR P.group_name IN ('SEPG','FIST') ");
			// Communicator has access to MISC project
			strTemp.append(" OR P.type = ").append(Timesheet.PROJECTTYPE_PUBLIC);
			strTemp.append(" ) ");
		}
		//logger.debug(strClassName + ".genSQLForCommunicator() = " + strTemp.toString());        
		return strTemp.toString();
	}

	/**
	 * Method getProjectList
	 * @param strRole
	 * @param intDeveloperId
	 * @param intPageType
	 * @param intProjectStatus -> -1~All | 0~On-going | 1~Closed | 2~Cancelled | 3~Tentative 
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getProjectList(String strRole, int intDeveloperId, int intPageType, int intProjectStatus) throws SQLException {

		ResultSet rs = null;
		PreparedStatement stmt = null;
		ArrayList arList = new ArrayList();
		String strSQL = "";

		switch (intPageType) {
			case 0x00: // for Developer add,view,update timesheet
				if (isDev(strRole)) {
					if (isQA(strRole) || (isSupporter(intDeveloperId))) {
						strSQL = genSQLForSupporter(intProjectStatus);
					}
					else {
						strSQL = genSQLForDeveloper(intDeveloperId, intProjectStatus);
					}
				}
				// HanhTN addded for ExternalAtGroup and ExternalAtProject -- 06/02/2007
				else if (isExternalAtGroup(strRole)) {
					strSQL = genSQLForExternalGroupManager(intDeveloperId, intProjectStatus);
				}
				else if (isExternalAtProject(strRole)) {
					strSQL = genSQLForExternalUser(intDeveloperId, intProjectStatus);
				}
				// PhuNT addded for Communicator -- 30/11/2007
				else if (isCommunicator(strRole)) {
					strSQL = genSQLForCommunicator(intDeveloperId, intProjectStatus);
				}
				break;
			case 0x01: // Approved by PL
				if (isGL(strRole)) {
					strSQL = genSQLForGroupLeader(intDeveloperId, intProjectStatus, Timesheet.PROJECTTYPE_EXTERNAL);
				}
				else if (isPL(strRole)) {
					strSQL = genSQLForProjectLeader(intDeveloperId, intProjectStatus, Timesheet.PROJECTTYPE_EXTERNAL);
				}
				break;
			case 0x02: // Approved by QA
				if (isQA(strRole)) {
					strSQL = genSQLForQA(intDeveloperId, intProjectStatus);
				}
				break;
			case 0x03: // Approved by GL
				if (isGL(strRole)) {
					strSQL = genSQLForGroupLeader(intDeveloperId, intProjectStatus, Timesheet.PROJECTTYPE_PUBLIC);
				}
				else if (isPL(strRole)) {
					strSQL = genSQLForProjectLeader(intDeveloperId, intProjectStatus, Timesheet.PROJECTTYPE_PUBLIC);
				}
				break;
			case 0x04: // View by Manager
				strSQL = genSQLForManager(intProjectStatus);
				break;
			case 0x05:
				if (isExternalAtGroup(strRole)) {
					strSQL = genSQLForExternalGroupManager(intDeveloperId, intProjectStatus);
				}
				else if (isExternalAtProject(strRole)) {
					strSQL = genSQLForExternalProjectManager(intDeveloperId, intProjectStatus);
				}
				break;
			case 0x06: // Other, example: For Inquiry Report
				if (isQA(strRole) || (isSupporter(intDeveloperId))) {
					strSQL = genSQLForSupporter(intProjectStatus);
				}
				else if (isGL(strRole)) {
					strSQL = genSQLForGroupLeader(intDeveloperId, intProjectStatus, Timesheet.PROJECT_STATUS_ALL);
				}
				else if (isExternalAtGroup(strRole)) {
					strSQL = genSQLForExternalGroupManager(intDeveloperId, intProjectStatus);
				}
				else if (isExternalAtProject(strRole)) {
					strSQL = genSQLForExternalProjectManager(intDeveloperId, intProjectStatus);
				}
				else if (isCommunicator(strRole)) {
					strSQL = genSQLForCommunicator(intDeveloperId, intProjectStatus);
				}
				else {
					strSQL = genSQLForDeveloper(intDeveloperId, intProjectStatus);
				}
				break;

			default :
				throw new SQLException("Invalid page type with Type = " + intPageType + " maybe error in Database");
		}
		if (strSQL == null || strSQL.length() <= 0) {
			throw new SQLException("You don't have permision to enter this page OR error in Database (Please contact to Administrator for more detail)");
		}
		//logger.debug(strClassName + ".getProjectList(strRole:"+strRole+ ", intDeveloperId: " +intDeveloperId + ", intPageType: " +intPageType + ", intProjectStatus: " +intProjectStatus+ "): strSQL = " + strSQL);

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL);
			rs = stmt.executeQuery();

			while (rs.next()) {
				ProjectComboModel clmData = new ProjectComboModel();
				clmData.setID(rs.getString("Project_ID"));
				clmData.setCode(rs.getString("Code"));
				clmData.setName(rs.getString("Name"));
				clmData.setGroup(rs.getString("group_name"));
				clmData.setStatus(rs.getString("status"));
				//Modified by HaiMM				
				clmData.setStartdate(rs.getString("start_date"));
				clmData.setPlanStartdate(rs.getString("plan_start_date"));
				arList.add(clmData);
			}
			System.out.println(strSQL.toString());
			
		}
		catch (SQLException ex) {
			logger.error("SQLException occurs in ProjectComboEJB.getProjectList(). " + ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Exception occurs in ProjectComboEJB.getProjectList(). " + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getProjectList(). ");
		}
		return arList;
	}

	/**
	 * Method getProjectList
	 * @param strGroup
	 * @param strRole
	 * @param intDeveloperID
	 * @param intProjectStatus -> -1~All | 0~On-going | 1~Closed | 2~Cancelled | 3~Tentative
	 * @return arList
	 * @throws SQLException
	 */
	public Collection getProjectList(String strRole, int intDeveloperId, int intProjectStatus) throws SQLException {

		ResultSet rs = null;
		PreparedStatement stmt = null;

		ArrayList arList = new ArrayList();
		String strSQL = "";

		if ( isDev(strRole) || isQA(strRole) || isSupporter(intDeveloperId) ||
			 isGL(strRole) || isPL(strRole) || isExternalAtGroup(strRole) ||
			 isExternalAtProject(strRole) || isCommunicator(strRole) ) {
			strSQL = this.genSQLForSupporter(intProjectStatus);
		}
		if (strSQL == null || strSQL.length() <= 0) {
			throw new SQLException("You don't have permision to enter this page OR error in Database (Please contact to Administrator for more detail)");
		}
		//logger.debug(strClassName + ".getProjectList(strRole:"+strRole+ ", intDeveloperId: " +intDeveloperId + ", intProjectStatus: " +intProjectStatus+ "): strSQL = " + strSQL);

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL);
			rs = stmt.executeQuery();

			while (rs.next()) {
				ProjectComboModel clmData = new ProjectComboModel();
				clmData.setID(rs.getString("Project_ID"));
				clmData.setCode(rs.getString("Code"));
				clmData.setName(rs.getString("Name"));
				clmData.setGroup(rs.getString("group_name"));
				clmData.setStatus(rs.getString("status"));
				//Modified by HaiMM
				clmData.setStartdate(rs.getString("start_date"));
				clmData.setPlanStartdate(rs.getString("plan_start_date"));
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error("SQLException occurs in ProjectComboEJB.getProjectListTracking(). " + ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Exception occurs in ProjectComboEJB.getProjectListTracking(). " + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getProjectListTracking(). ");
		}
		return arList;
	}

	/**
	 * Method getStatusString
	 * Get project status title by project status value
	 * @param nStatus
	 * @return strStatus
	 */
	private String getStatusString(int nStatus) {
		String strStatus = null;
		switch (nStatus) {
			case Timesheet.PROJECT_STATUS_ONGOING:
				strStatus = Timesheet.PROJECT_STATUS_ONGOING_STR;
				break;
			case Timesheet.PROJECT_STATUS_CLOSED:
				strStatus = Timesheet.PROJECT_STATUS_CLOSED_STR;
				break;
			case Timesheet.PROJECT_STATUS_CANCELLED:
				strStatus = Timesheet.PROJECT_STATUS_CANCELLED_STR;
				break;
			case Timesheet.PROJECT_STATUS_TENTATIVE:
				strStatus = Timesheet.PROJECT_STATUS_TENTATIVE_STR;
				break;
			default:
				strStatus = "";
		}
		return strStatus;
	}

	/**
	 * Method getProjectStatusList
	 * Get project statuses
	 * @param strRole
	 * @param intDeveloperId
	 * @param nPageType
	 * @param intProjectStatus
	 * @return
	 * @throws SQLException
	 */
	public Collection getProjectStatusList(String strRole, int intDeveloperId) throws SQLException {

		ResultSet rs = null;
		PreparedStatement stmt = null;
		ArrayList arList = new ArrayList();
		String strSQL = "";

		if (isQA(strRole) || (isSupporter(intDeveloperId))) {
			strSQL = genSQLForSupporter(Timesheet.PROJECT_STATUS_NULL);
		}
		else if (isExternalAtGroup(strRole)) {
			strSQL = genSQLForExternalGroupManager(intDeveloperId, Timesheet.PROJECT_STATUS_NULL);
		}
		else if (isExternalAtProject(strRole)) {
			strSQL = genSQLForExternalProjectManager(intDeveloperId, Timesheet.PROJECT_STATUS_NULL);
		}
		else if (isCommunicator(strRole)) {
			strSQL = genSQLForCommunicator(intDeveloperId, Timesheet.PROJECT_STATUS_NULL);
		}
		else {
			strSQL = genSQLForDeveloper(intDeveloperId, Timesheet.PROJECT_STATUS_NULL);
		}

		if (strSQL == null || strSQL.length() <= 0) {
			throw new SQLException("You don't have permision to enter this page OR error in Database (Please contact to Administrator for more detail)");
		}
		//logger.debug(strClassName + ".getProjectList(strRole:"+strRole+ ", intDeveloperId: " +intDeveloperId+"): strSQL = " + strSQL);

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL);
			rs = stmt.executeQuery();

			while (rs.next()) {
				ProjectComboModel clmData = new ProjectComboModel();
				clmData.setID(rs.getString("Status").trim());
				clmData.setName(getStatusString(rs.getInt("Status")));
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error("SQLException occurs in ProjectComboEJB.getProjectStatusList(). " + ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Exception occurs in ProjectComboEJB.getProjectStatusList(). " + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getProjectStatusList(). ");
		}
		return arList;
	}

	/**
	 * Method isSupporter
	 * isSupporter
	 * identify if developer belong to support group or not
	 * @param intDeveloperId - ident.number of developer
	 * @return true if developer is a suppoter, otherwise return false
	 */
	private boolean isSupporter(int intDeveloperId) throws SQLException {

		ResultSet rs = null;
		PreparedStatement pStmt = null;
		String strSQL = "SELECT COUNT(developer_id) FROM developer " +
						"WHERE group_name NOT IN (SELECT groupname FROM groups,workunit WHERE " + 
						"group_id = tableid AND isoperationgroup = 1 AND type = 1) " +
						"AND developer_id = ?";
		boolean isOK = true;
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			pStmt = con.prepareStatement(strSQL);
			pStmt.setInt(1, intDeveloperId);
			rs = pStmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) == 0) {
					isOK = false;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			conPool.releaseResource(con, pStmt, rs, strClassName + ".isSupporter(). ");
			return isOK;
		}
	}

	/**
	 * Method isDev.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isDev(String sRole) {
		return sRole.substring(0, 1).equals("1");
	}

	/**
	 * Method isPL.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isPL(String sRole) {
		return sRole.substring(1, 2).equals("1");
	}

	/**
	 * Method isGL.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isGL(String sRole) {
		return sRole.substring(2, 3).equals("1");
	}

	/**
	 * Method isMisc.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isMisc(String sRole) {
		return sRole.substring(3, 4).equals("1");
	}

	/**
	 * Method isQA.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isQA(String sRole) {
		return sRole.substring(4, 5).equals("1");
	}

	/**
	 * Method isManager.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isManager(String sRole) {
		return sRole.substring(5, 6).equals("1");
	}

	/**
	 * Method isExternalAtGroup.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isExternalAtGroup(String sRole) {
		return (sRole.substring(6, 7).equals("1") && sRole.substring(7, 8).equals("1"));
	}

	/**
	 * Method isExternalAtProject.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isExternalAtProject(String sRole) {
		return (sRole.substring(6, 7).equals("1") && sRole.substring(7, 8).equals("0"));
	}
    
	/**
	 * Method isCommunicator.
	 * @param sRole
	 * @return boolean
	 */
	private static boolean isCommunicator(String sRole) {
		return (sRole.substring(8, 9).equals("1"));
	}
}