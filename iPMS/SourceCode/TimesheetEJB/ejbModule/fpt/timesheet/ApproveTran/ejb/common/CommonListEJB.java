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
 * Bean implementation class for Enterprise Bean: CommonList
 * @version 1.0 23-April-03
 * @author
 */
public class CommonListEJB implements SessionBean {
	private static final Logger logger = Logger.getLogger(CommonListEJB.class);

	private Connection con;
	private SessionContext mySessionCtx;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	private static final String strClassName = "CommonListEJB";

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
	 * Method getCommonList
	 * get common record list
	 * @return list of work type, product, process, KPA and group
	 */
	public Collection getCommonList() {
		ArrayList arCommonList = new ArrayList();
		try {
			arCommonList = getPQANameList(arCommonList);
			arCommonList = getWorkTypeList(arCommonList);
			arCommonList = getProductList(arCommonList);
			arCommonList = getProcessList(arCommonList);
			arCommonList = getKpaList(arCommonList);
			arCommonList = getGroupList(arCommonList);
		}
		catch (Exception e) {
			logger.error("getCommonList() - error:" + e.toString());
		}
		return arCommonList;
	}

	/**
	 * Method getPQANameList
	 * Add list of PQA Name to current list
	 * @author Truong Ngoc Hanh
	 * @param arList
	 * @return arList
	 * @throws SQLException
	 */
	private ArrayList getPQANameList(ArrayList arList) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		String tmpKey = "PQA";

		StringBuffer strSQL  = new StringBuffer();
		strSQL.append(" SELECT DISTINCT d.account AS Code ");
		strSQL.append(" FROM developer d, assignment a, project p ");
		strSQL.append(" WHERE d.developer_id = a.developer_id ");
		strSQL.append(" AND p.project_id = a.project_id ");
		strSQL.append(" AND a.response =" + Timesheet.PQA_ROLE + " AND d.status != " + Timesheet.USER_QUIT);		
//		strSQL += " FROM developer d, assignment a, project p ";
//		strSQL += " WHERE d.developer_id = a.developer_id ";
//		strSQL += " AND p.project_id = a.project_id ";
//		strSQL += " AND a.response =" + Timesheet.PQA_ROLE + " AND d.status != " + Timesheet.USER_QUIT;

		//logger.debug("EJB.InquiryEJB.getPQANameList(): strSQL = " + strSQL.toString());
		try {
			// All PQA Name
			CommonListModel clmAllData = new CommonListModel();
			clmAllData.setKey(tmpKey);
			clmAllData.setCode("All");
			clmAllData.setName("All");
			arList.add(clmAllData);

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				CommonListModel clmData = new CommonListModel();
				clmData.setKey(tmpKey);
				clmData.setCode(rs.getString("Code").trim());
				clmData.setName(rs.getString("Code").trim());
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getPQANameList():" + ex.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getPQANameList():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getPQANameList():");
		}//finally
		return arList;
	}

	/**
	 * Method getWorkTypeList
	 * Add list of work type to current list
	 * @param arList - current common list
	 * @return new common list
	 * @throws SQLException
	 */
	private ArrayList getWorkTypeList(ArrayList arList) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		String tmpKey = "WorkType";
		String sqlCommand = "SELECT TOW_ID as Code, Name FROM TypeOfWork ORDER by Name";

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(sqlCommand);
			rs = stmt.executeQuery();

			while (rs.next()) {
				CommonListModel clmData = new CommonListModel();
				clmData.setKey(tmpKey);
				clmData.setCode(rs.getString("Code").trim());
				clmData.setName(rs.getString("Name").trim());
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getWorkType():" + ex.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getWorkType():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getWorkType():");
		}//finally
		return arList;
	}

	/**
	 * Method getProcessList
	 * Add list of process to current list
	 * @param arList - current common list
	 * @return new common list
	 * @throws SQLException
	 */
	private ArrayList getProcessList(ArrayList arList) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		String tmpKey = "Process";
		String sqlCommand = "SELECT PROCESS_ID as Code, Name FROM PROCESS ORDER by Name";

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(sqlCommand);
			rs = stmt.executeQuery();

			while (rs.next()) {
				CommonListModel clmData = new CommonListModel();
				clmData.setKey(tmpKey);
				clmData.setCode(rs.getString("Code").trim());
				clmData.setName(rs.getString("Name").trim());
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getProcessList():" + ex.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getProcessList():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getProcessList():");
		}//finally
		return arList;
	}

	/**
	 * Method getProductList
	 * Add list of product to current list
	 * @param arList - current common list
	 * @return new common list
	 * @throws SQLException
	 */
	private ArrayList getProductList(ArrayList arList) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		String tmpKey = "Product";
		String sqlCommand = "SELECT WP_ID as Code, Name FROM WorkProduct ORDER by Name";

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(sqlCommand);
			rs = stmt.executeQuery();

			while (rs.next()) {
				CommonListModel clmData = new CommonListModel();
				clmData.setKey(tmpKey);
				clmData.setCode(rs.getString("Code").trim());
				clmData.setName(rs.getString("Name").trim());
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getProductList():" + ex.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getProductList():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getProductList():");
		}//finally
		return arList;
	}

	/**
	 * Method getKpaList
	 * Add list of KPA to current list
	 * @param arList - current common list
	 * @return new common list
	 * @throws SQLException
	 */
	private ArrayList getKpaList(ArrayList arList) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		String tmpKey = "Kpa";
		String sqlCommand = "SELECT KPA_ID as Code, Name FROM KPA ORDER by Name";

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(sqlCommand);
			rs = stmt.executeQuery();

			while (rs.next()) {
				CommonListModel clmData = new CommonListModel();
				clmData.setKey(tmpKey);
				clmData.setCode(rs.getString("Code").trim());
				clmData.setName(rs.getString("Name").trim());
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getKpaList():" + ex.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getKpaList():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getKpaList():");
		}//finally
		return arList;
	}

	/**
	 * Method getGroupList
	 * Add list of group to current list
	 * @param arList - current common list
	 * @return new common list
	 * @throws SQLException
	 */
	private ArrayList getGroupList(ArrayList arList) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		String tmpKey = "Group";
		String sqlCommand = "SELECT DISTINCT groupname as Code FROM groups, workunit WHERE groups.group_id = workunit.tableid ORDER BY groupname";

		try {
			// All group
			CommonListModel clmAllData = new CommonListModel();
			clmAllData.setKey(tmpKey);
			clmAllData.setCode("All");
			clmAllData.setName("All");
			arList.add(clmAllData);

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(sqlCommand);
			rs = stmt.executeQuery();

			while (rs.next()) {
				CommonListModel clmData = new CommonListModel();
				clmData.setKey(tmpKey);
				clmData.setCode(rs.getString("Code").trim());
				clmData.setName(rs.getString("Code").trim());
				arList.add(clmData);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getGroupList():" + ex.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getGroupList():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getGroupList():");
		}//finally
		return arList;
	}
}