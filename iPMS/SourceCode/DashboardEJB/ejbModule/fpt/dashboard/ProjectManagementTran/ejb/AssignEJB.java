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
 
 /*
 *
 * Copyright 2001 FPT. All Rights Reserved.
 * Author: Duong Thanh Nhan
 *
 */
package fpt.dashboard.ProjectManagementTran.ejb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import fpt.dashboard.framework.util.SqlUtil;
import fpt.dashboard.framework.connection.WSConnectionPooling;
public class AssignEJB implements SessionBean {
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private SessionContext context;
	private DataSource ds = null;
	private Connection con = null;

	private Integer assignID;
	private Integer projectID;
	private Integer developerID;
	private Integer type;
	private Integer startWeek;
	private Integer endWeek;
	private String projectName;
	private String projectLeader;
	private String projectCode;
	private String projectStart;
	private String projectFinish;
	private String developerName;
	private String begin;
	private String end;
	private String desc;
	int usage = 0;
	String response = "";

	public Integer getProjectID() {
		return projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public String getProjectLeader() {
		return projectLeader;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public Integer getDeveloperID() {
		return developerID;
	}
	public Integer getAssignID() {
		return assignID;
	}
	public String getDeveloperName() {
		return developerName;
	}
	public String getBegin() {
		if (begin != null) {
			return begin;
		} else {
			return "";
		}
	}
	public String getEnd() {
		if (end != null) {
			return end;
		} else {
			return "";
		}
	}
	public String getDesc() {
		return desc;
	}
	public String getProjectStart() {
		return projectStart;
	}
	public String getProjectFinish() {
		return projectFinish;
	}
	public Integer getType() {
		return type;
	}
	public Integer getStartWeek() {
		return startWeek;
	}
	public Integer getEndWeek() {
		return endWeek;
	}
	public int getUsage() {
		return usage;
	}
	public void setUsage(int Usage) {
		this.usage = Usage;
	}
	public String getRes() {
		return response;
	}
	public void setRes(String res) {
		this.response = res;
	}
	public void setProjectID(Integer ID) {
		this.projectID = ID;
	}
	public void setProjectName(String Name) {
		this.projectName = Name;
	}
	public void setProjectLeader(String Name) {
		this.projectLeader = Name;
	}
	public void setProjectCode(String Code) {
		this.projectCode = Code;
	}
	public void setDeveloperID(Integer ID) {
		this.developerID = ID;
	}
	public void setDeveloperName(String Name) {
		this.developerName = Name;
	}
	public void setBegin(String Begin) {
		this.begin = Begin;
	}
	public void setEnd(String End) {
		this.end = End;
	}
	public void setProjectStart(String End) {
		this.end = End;
	}
	public void setProjectFinish(String End) {
		this.end = End;
	}
	public void setDesc(String Desc) {
		this.desc = Desc;
	}
	public void setType(Integer Type) {
		this.type = Type;
	}

	public void ejbCreate() throws CreateException {
		try {
			makeConnection();
		} catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	public void ejbRemove() {
		try {
			if (con != null)
				con.close();
		} catch (Exception ex) {
			throw new EJBException(
				"ejbRemove SQLException: " + ex.getMessage());
		}
	}
	public void setSessionContext(SessionContext context) {
		this.context = context;
	}
	public void ejbActivate() {
		try {
			makeConnection();
		} catch (Exception ex) {
			throw new EJBException("ejbActivate Exception: " + ex.getMessage());
		}
	}
	public void ejbPassivate() {
		try {
			if (con != null)
				con.close();
		} catch (Exception ex) {
			throw new EJBException(
				"ejbPassivate Exception: " + ex.getMessage());
		}
	}
	/* 
	 * *********************** Database Routines *************************
	 * HUYNH2 add some function for assign permission to defect when assignment in Dashboard.
	 */

	/**
	 * if the row containt projec_id and develop Id existing in defect table return true, othe return false
	 * @param prjId
	 * @param dvlId
	 * @return
	 * @throws SQLException
	 */
	public final boolean checkForUpdateDefectPermission(long prjId, long dvlId)
		throws SQLException {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs1 = null;
		boolean returnValue = false;
		sql =
			"SELECT *  FROM defect_permission WHERE project_id = ? AND developer_id = ?";
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(sql);
			prepStmt.setLong(1, prjId);
			prepStmt.setLong(2, dvlId);
			rs1 = prepStmt.executeQuery();
			if (rs1.next()) {
				returnValue = true;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
			return returnValue;
		}
	}

	public final boolean updateAssignForDefectPermission(
		long prjId,
		long dvlId,
		String position)
		throws SQLException {
		PreparedStatement prepStmt = null;
		String sql = null;
		boolean returnValue = true;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			sql =
				"UPDATE Defect_Permission SET  Position = ?, Status = 0 WHERE Developer_ID = ? AND Project_ID =?";
			prepStmt = con.prepareStatement(sql);
			prepStmt.setString(1, position);
			prepStmt.setLong(2, dvlId);
			prepStmt.setLong(3, prjId);
			if (prepStmt.executeUpdate() == 0) {
				returnValue = false;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
			return returnValue;
		}

	}

	public final boolean deleteAssignForDefectPermission(final long assID)
		throws SQLException {
		PreparedStatement prepStmt = null;
		String sql = null;
		boolean returnValue = true;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			sql =
				"DELETE FROM  Defect_Permission WHERE (Developer_ID,Project_ID) IN (SELECT Developer_ID,Project_ID FROM Assignment WHERE assignment_id = ?) ";
			prepStmt = con.prepareStatement(sql);
			prepStmt.setLong(1, assID);
			if (prepStmt.executeUpdate() <= 0) {
				returnValue = false;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
			return returnValue;
		}

	}

	public final boolean addAssignForDefectPermission(
		long prjId,
		long dvlId,
		String position)
		throws SQLException {
		PreparedStatement prepStmt = null;
		String sql = null;
		boolean returnValue = true;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			sql =
				"INSERT INTO Defect_Permission(Developer_ID, Position, Status, Project_ID) "
					+ "VALUES (?,?,?,?)";
			prepStmt = con.prepareStatement(sql);
			prepStmt.setLong(1, dvlId);
			prepStmt.setString(2, position);
			prepStmt.setString(3, "0");
			prepStmt.setLong(4, prjId);
			if (prepStmt.executeUpdate() <= 0) {
				returnValue = false;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
		return returnValue;
	}

	// ENd HUYNH2    

	private void makeConnection() throws NamingException, SQLException {
		ds = conPool.getDS();
		con = ds.getConnection();
	}
    private final boolean checkDevJoinProject(long developer_id, long project_id) throws SQLException{
        Connection conn =  null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = null;
        sql = "SELECT * FROM ASSIGNMENT WHERE PROJECT_ID = ? AND DEVELOPER_ID = ?";
        boolean returnValue =  false;
        try{
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            stm = con.prepareStatement(sql);
            stm.setLong(1,project_id);
            stm.setLong(2,developer_id);
            rs = stm.executeQuery();
            if (rs.next()) {
                returnValue = true;
            }            
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            if (stm != null)
                stm.close();
            if (con != null)
                con.close();
            if (rs != null)
                rs.close();
            return returnValue;
        }        
    }    

	/**
	 * method: void insertRow(Integer ProjectID, Integer DeveloperID, String Begin, String End, Integer Type)
	 * updated by: Nguyen Thai Son
	 * date: October 11, 2002
	 * description: assign new member to project with his responsibily default as Developer
	 * */
	public void insertRow(
		Integer ProjectID,
		Integer DeveloperID,
		String Begin,
		String End,
		Integer Type)
		throws SQLException {
		// Insert new assignment with default value of Usage=100% and Response=0 (Developer)
		String insertStatement =
			"insert into assignment(ASSIGNMENT_ID, project_id, developer_id,begin_date,end_date,type,usage,response)"
				+ " values (?, ? , ?, TO_DATE(?,'dd/mm/yy'),TO_DATE(?,'dd/mm/yy'), ?,100, '0')";
		PreparedStatement prepStmt = null;
        //if(!checkDevJoinProject(DeveloperID.intValue(),ProjectID.intValue())){            
    		try {
    			if (ds == null)
    				ds = conPool.getDS();
    			con = ds.getConnection();
    			long lAssignmentID = conPool.getNextSeq("ASSIGNMENT_SEQ");
    			prepStmt = con.prepareStatement(insertStatement);
    			prepStmt.setLong(1,lAssignmentID);
    			prepStmt.setInt(2, ProjectID.intValue());
    			prepStmt.setInt(3, DeveloperID.intValue());
    			prepStmt.setString(4, Begin);
    			prepStmt.setString(5, End);
    			prepStmt.setInt(6, Type.intValue());
    			if (prepStmt.executeUpdate() > 0) {
    				if (!SqlUtil.getDefectPermission(0).equals("0000000000")) {
    					//add permission to defect_permission
    					if (checkForUpdateDefectPermission(ProjectID.longValue(),
    						DeveloperID.longValue())) {
    						updateAssignForDefectPermission(
    							ProjectID.longValue(),
    							DeveloperID.longValue(),
                                SqlUtil.getDefectPermission(0));
    					} else {
    						addAssignForDefectPermission(
    							ProjectID.longValue(),
    							DeveloperID.longValue(),
                                SqlUtil.getDefectPermission(0));
    					}
    				}   
    			}
    			prepStmt.close();
    		}
    		catch (SQLException ex) {
    			ex.printStackTrace();
    		}
    		catch (Exception ex) {
    			ex.printStackTrace();
    		}
    		finally {
    			if (prepStmt != null)
    				prepStmt.close();
    			if (con != null)
    				con.close();
    		}
	}

	/**
	 * Delete an assignment
	 * @param AssignID
	 * @throws SQLException
	 */
	public void deleteRow(Integer AssignID) throws SQLException {
		PreparedStatement prepStmt = null;

		try {
			String deleteStatement =
				"delete from assignment where (assignment_id= ? ) ";
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(deleteStatement);
			prepStmt.setInt(1, AssignID.intValue());
			//delete from defect_permission
			deleteAssignForDefectPermission(AssignID.intValue());
			prepStmt.executeUpdate();
			prepStmt.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * Get project's detail informaion
	 * @param ProjectID
	 * @throws SQLException
	 */
	public void getProjectInfor(Integer ProjectID) throws SQLException {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			String selectStatement =
				"select project.name,code,leader,TO_CHAR(start_date,'dd/mm/yy'),TO_CHAR(plan_finish_date,'dd/mm/yy'),TO_CHAR(base_finish_date,'dd/mm/yy') "
					+ "from project where project_id= ?";

			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, ProjectID.intValue());
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				this.projectName = rs.getString(1);
				this.projectCode = rs.getString(2);
				this.projectLeader = rs.getString(3);
				this.projectStart = rs.getString(4);
				if (rs.getString(5) != null) {
					this.projectFinish = rs.getString(5);
				} else {
					this.projectFinish = rs.getString(6);
				}
				rs.close();
				prepStmt.close();
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * Get assingment detail from non-daily projects
	 * @param AssignID
	 * @throws SQLException
	 */
	public void loadRow(Integer AssignID) throws SQLException {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(
			"SELECT D.name, TO_CHAR(A.begin_date, 'dd/mm/yy'), TO_CHAR(A.end_date, 'dd/mm/yy')");
		strSQL.append(", A.project_id, D.developer_id, A.type");
		strSQL.append(", TO_NUMBER(TO_CHAR(A.begin_date, 'iW'))");
		strSQL.append(", TO_NUMBER(TO_CHAR(A.end_date, 'iW'))");
		strSQL.append(", A.usage, A.response ");
		strSQL.append(" FROM developer D, assignment A, project P");
		strSQL.append(" WHERE A.assignment_id = ?");
		strSQL.append(" AND A.developer_id = D.developer_id");
		strSQL.append(" AND A.project_id = P.project_id ");
		strSQL.append(
			" AND not((P.code like 'Daily_%') and (D.group_name like 'G%')) ");

		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL.toString());

			prepStmt.setInt(1, AssignID.intValue());
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				this.developerName = rs.getString(1);
				this.begin = rs.getString(2);
				this.end = rs.getString(3);
				this.assignID = AssignID;
				this.projectID = new Integer(rs.getString(4));
				this.developerID = new Integer(rs.getString(5));
				this.type = new Integer(rs.getInt(6));
				if (rs.getString(7) != null) {
					this.startWeek = new Integer(rs.getString(7));
				} else {
					this.startWeek = new Integer(0);
				}
				if (rs.getString(8) != null) {
					this.endWeek = new Integer(rs.getString(8));
				} else {
					this.endWeek = this.startWeek;
				}
				this.usage = rs.getInt(9);
				if (rs.getString(10) != null) {
					this.response = rs.getString(10);
				} else {
					this.response = "0";
				}
				prepStmt.close();
			} else {
				prepStmt.close();
				//  throw new NoSuchEntityException(
				//      "Row for ProjectID " + AssignID + " not found in database.");
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * Get other assingment detail
	 * @param AssignID
	 * @throws SQLException
	 */
	public void loadOtherRow(Integer AssignID) throws SQLException {
		String selectStatement =
			"select developer.name, TO_CHAR(from_date,'dd/mm/yy'),TO_CHAR(end_date,'dd/mm/yy'),other_assignment.developer_id, other_assignment.type,description,to_number(to_char(from_date,'iW')),to_number(to_char(end_date,'iW')),usage "
				+ "from developer,other_assignment where "
				+ "(developer.developer_id =other_assignment.developer_id)"
				+ "and(other_assignment.oa_id= ? )";

		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, AssignID.intValue());
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				this.developerName = rs.getString(1);
				this.begin = rs.getString(2);
				this.end = rs.getString(3);
				this.assignID = AssignID;
				this.developerID = new Integer(rs.getString(4));
				this.type = new Integer(rs.getInt(5));
				this.desc = rs.getString(6);
				this.startWeek = new Integer(rs.getString(7));
				this.endWeek = new Integer(rs.getString(8));
				this.usage = rs.getInt(9);
			} else {
				rs.close();
				prepStmt.close();
				throw new NoSuchEntityException(
					"Row for ProjectID "
						+ AssignID
						+ " not found in database.");
			}
			prepStmt.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * Update assingment informations
	 * @param AssignID
	 * @throws SQLException
	 */
	public void storeRow(Integer AssignID) throws SQLException {
		String updateStatement =
			"update assignment set begin_date =  TO_DATE(?,'dd/mm/yy') ,"
				+ "end_date =  TO_DATE(?,'dd/mm/yy') ,"
				+ "type= ?, "
				+ "usage= ?, "
				+ "response= ? "
				+ "where (assignment_id= ? ) ";
		PreparedStatement prepStmt = null;
		long project_id;
		long developer_id;
		String sql;
		ResultSet rs = null;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(updateStatement);
			prepStmt.setString(1, begin);
			prepStmt.setString(2, end);
			prepStmt.setInt(3, type.intValue());
			prepStmt.setInt(4, usage);
			prepStmt.setString(5, response);
			prepStmt.setInt(6, AssignID.intValue());
			int rowCount = prepStmt.executeUpdate();
			prepStmt.close();
			if (rowCount == 0) {
				throw new EJBException(
					"Storing row for ProjectID " + AssignID + " failed.");
			} else {

				//update defect permission					
				if (!(SqlUtil.getDefectPermission(Long.parseLong(response))
					.equals("0000000000"))) {
					//get projectId and developerId from assignment by AssignId
					sql =
						"SELECT project_id, developer_id FROM assignment WHERE assignment_id = ? AND rownum = 1 ";
					prepStmt = con.prepareStatement(sql);
					prepStmt.setLong(1, AssignID.longValue());
					rs = prepStmt.executeQuery();
					if (rs.next()) {
						project_id = rs.getLong("project_id");
						developer_id = rs.getLong("developer_id");
						if (checkForUpdateDefectPermission(project_id,
							developer_id)) {
							updateAssignForDefectPermission(
								project_id,
								developer_id,
                                SqlUtil.getDefectPermission(Long.parseLong(response)));
						} else {
							addAssignForDefectPermission(
								project_id,
								developer_id,
                                SqlUtil.getDefectPermission(Long.parseLong(response)));
						}
					}
				} else {
					//Delete from defect
					deleteAssignForDefectPermission(AssignID.longValue());
				}

			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * Get all assignments from non-daily projects
	 * @return
	 * @throws SQLException
	 */
	public Collection selectAllKey() throws SQLException {
		ArrayList a = new ArrayList();

		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT A.assignment_id");
		strSQL.append(" FROM assignment A, developer D, project P");
		strSQL.append(" WHERE A.project_id = ?");
		strSQL.append(" AND A.developer_id = D.developer_id");
		strSQL.append(" AND A.project_id = P.project_id ");
		strSQL.append(
			" AND not((P.code like 'Daily_%') and (D.group_name like 'G%'))");
		strSQL.append(" ORDER BY A.begin_date, D.name");

		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL.toString());
			prepStmt.setInt(1, this.projectID.intValue());
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				Integer tid = new Integer(rs.getInt(1));
				a.add(tid);
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
		return a;
	}

	/**
	 * Get all assignment_IDs of a developer in non-daily projects
	 * @return
	 * @throws SQLException
	 */
	public Collection selectByDeveloper() throws SQLException {
		String selectStatement =
			"select A.assignment_id from assignment A, project P, developer D where A.developer_id= ? "
				+ " AND A.project_id = P.project_id "
				+ " AND A.developer_id = D.developer_id "
				+ " AND not((P.code like 'Daily_%') and (D.group_name like 'G%')) ";
		ArrayList a = new ArrayList();
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, this.developerID.intValue());
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				Integer tid = new Integer(rs.getInt(1));
				a.add(tid);
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
		return a;
	}

	/**
	 * Get all Other assignment_IDs of a developer in non-daily projects
	 * @return
	 * @throws SQLException
	 */
	public Collection selectOtherByDeveloper() throws SQLException {
		ArrayList a = new ArrayList();
		String selectStatement =
			"select oa_id " + "from other_assignment where developer_id= ? ";
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, this.developerID.intValue());
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				Integer tid = new Integer(rs.getInt(1));
				a.add(tid);
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
		return a;
	}

	/**
	 * Get all developer_IDs had assigned in a specific period
	 * @param startPeriod
	 * @param endPeriod
	 * @param Condi
	 * @return
	 * @throws SQLException
	 */
	public Collection selectBussyDeveloper(
		String startPeriod,
		String endPeriod,
		String Condi)
		throws SQLException {
		ArrayList a = new ArrayList();
		String strCondi = "";
		if (!Condi.equals(Constants.ALL)) {
			strCondi = " AND (group_name= ? ) ";
		}
		String selectStatement =
			"select developer_id from developer where (developer_id IN (select distinct A.developer_id "
				+ "from assignment A, developer D, project P where ((TO_DATE(? ,'dd/mm/yy' ) between A.begin_date and A.end_date ) or (A.begin_date between TO_DATE(? ,'dd/mm/yy') and TO_DATE(? ,'dd/mm/yy'))) "
				+ " AND A.project_id = P.project_id "
				+ " AND A.developer_id = D.developer_id "
				+ " AND not((P.code like 'Daily_%') and (D.group_name like 'G%'))"
				+ "union select distinct developer_id from other_assignment where (TO_DATE(? ,'dd/mm/yy' ) between from_date and end_date ) or (from_date between TO_DATE(? ,'dd/mm/yy') and TO_DATE(? ,'dd/mm/yy'))"
				+ "))"
				+ strCondi
				+ " AND status<>3 "
				+ " AND status<>4 "
				+ " ORDER BY Name";

		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, startPeriod);
			prepStmt.setString(2, startPeriod);
			prepStmt.setString(3, endPeriod);
			prepStmt.setString(4, startPeriod);
			prepStmt.setString(5, startPeriod);
			prepStmt.setString(6, endPeriod);
			if (!Condi.equals(Constants.ALL)) {
				prepStmt.setString(7, Condi);
			}
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				Integer tid = new Integer(rs.getInt(1));
				a.add(tid);
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
		return a;
	}

	/**
	 * Get all developer_IDs that not in bussy list (get from bussy developer list)
	 * @param strID
	 * @param Condi
	 * @return
	 * @throws SQLException
	 */
	public Collection selectFreeDeveloper(String strID, String Condi)
		throws SQLException {
		String strCondi = "";
		if (!Condi.equals(Constants.ALL)) {
			strCondi = " AND (group_name= ? )";
		}
		String selectStatement = "";
		if (!strID.equals("")) {
			selectStatement =
				"select developer_id "
					+ "from developer where ( (role<>'0000000000') AND developer_id not in ("
					+ strID
					+ "))"
					+ strCondi
					+ " AND status<>3 "
					+ " AND status<>4 "
					+ " ORDER BY Name";
		} else {
			if (!Condi.equals(Constants.ALL)) {
				selectStatement =
					"select developer_id "
						+ "from developer where (role<>'0000000000') AND (group_name= ?) AND status<>4 ORDER BY Name";
			} else {
				selectStatement =
					"select developer_id "
						+ "from developer where (role<>'0000000000') + AND status<>4 ORDER BY Name";
			}
		}
		ArrayList a = new ArrayList();
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(selectStatement);
			if (!Condi.equals(Constants.ALL)) {
				prepStmt.setString(1, Condi);
			}
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				Integer tid = new Integer(rs.getInt(1));
				a.add(tid);
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
		return a;
	}
}