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

import java.sql.*;
import java.util.Vector;
import java.util.Calendar;
import com.fms1.infoclass.*;
import com.fms1.tools.Db;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.web.ServerHelper;

/**
 * Project team logic
 * @author manu
 * @date Jul 6, 2004
 * Modify by HUYNH2
 */

public class Assignments {
    private static final long MS_PER_DAY = 86400000;// 24 * 60 * 60 * 1000; // Miliseconds per day
    
	public static final int NUMBER_OF_ROW_ADDABLE = 10;
	
    /*
     * HUYNH2 add some function for assign permission to defect when assignment in FsoftInsight.
     */
    public static final boolean checkForUpdateDefectPermission(long prjId, long dvlId) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        boolean returnValue = false;
        sql =
            "SELECT *  FROM defect_permission WHERE project_id = ? AND developer_id = ?";
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjId);
            stm.setLong(2, dvlId);
            rs = stm.executeQuery();
            if (rs.next()) {
                returnValue = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return returnValue;
        }
    }

    public static final boolean updateAssignForDefectPermission(long prjId, long dvlId, String position) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        boolean returnValue = true;
        try {
            conn = ServerHelper.instance().getConnection();
            sql =
                "UPDATE Defect_Permission SET  Position = ?, Status = 0 WHERE Developer_ID = ? AND Project_ID =?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, position);
            stm.setLong(2, dvlId);
            stm.setLong(3, prjId);
            if (stm.executeUpdate() == 0) {
                returnValue = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
            returnValue = false;
        } finally {
            ServerHelper.closeConnection(conn, stm, null);
            return returnValue;
        }
    }

    /**
     * Delete defect permission following deleting of user from project assignment
     * @param assID
     * @param lDeveloperID
     * @param lWorkUnitID
     * @return
     */
    public static final boolean deleteAssignForDefectPermission(
        long assID, long lDeveloperID, long lWorkUnitID)
    {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            int assignments = 0;
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
            sql = "SELECT COUNT(*) AS assignments " +
                " FROM assignment" +
                " WHERE (project_id, developer_id) IN (" +
                " SELECT project_id, developer_id FROM assignment" +
                " WHERE assignment_id = ?)";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, assID);
            rs = stm.executeQuery();
            if (rs.next()) {
                assignments = rs.getInt("assignments");
            }
            rs.close();
            stm.close();
            if (assignments == 1) {
                sql =
                    "DELETE Defect_Permission" +
                    " WHERE (Developer_ID,Project_ID) IN" +
                    " (SELECT Developer_ID,Project_ID" +
                    " FROM Assignment WHERE assignment_id = ?) ";
                stm = conn.prepareStatement(sql);
                stm.setLong(1, assID);
                stm.executeUpdate();
                stm.close();
            }
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }

    public static final boolean addAssignForDefectPermission(long prjId, long dvlId, String position) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        boolean returnValue = true;
        try {
            conn = ServerHelper.instance().getConnection();
            sql =
                "INSERT INTO Defect_Permission(Developer_ID, Position, Status, Project_ID) "
                    + "VALUES (?,?,?,?)";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, dvlId);
            stm.setString(2, position);
            stm.setString(3, "0");
            stm.setLong(4, prjId);
            if (stm.executeUpdate() <= 0) {
                conn.rollback();
                returnValue = false;
            }
            return returnValue;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            ServerHelper.closeConnection(conn, stm, null);
        }
    }

    public static TeamSizeInfo getTeamSizeProgress(long prjID) {
		Vector vectorTeamList = (Vector) Assignments.getWOTeamList(prjID, null, null);
		AssignmentInfo info;
		int teamlistsize = vectorTeamList.size();
		TeamSizeInfo teamsizeinfo = new TeamSizeInfo();
		
        // Get begin dates and end dates in an Vector
		// The first half of the vector is begin dates and the second half of the vector is end dates
        
		for (int i = 0; i < teamlistsize; i++) {
			info = (AssignmentInfo) vectorTeamList.elementAt(i);
			teamsizeinfo.periods.add(info.beginDate);
			teamsizeinfo.periods.add(info.endDate);
		}

		// Sort Date
		CommonTools.sortVector(teamsizeinfo.periods);

		// Eleminate the same date
		Date date1;
		Date date2;
		for (int i = 0; i < teamsizeinfo.periods.size(); i++) {
			if (teamsizeinfo.periods.elementAt(i) != null) {
				for (int j = i + 1; j < teamsizeinfo.periods.size(); j++) {
					date1 = (Date) teamsizeinfo.periods.elementAt(i);
					date2 = (Date) teamsizeinfo.periods.elementAt(j);
					if (date1.compareTo(date2) == 0) {
						teamsizeinfo.periods.setElementAt(null, j);
					}
				}
			}
		}
		for (int i = 0; i < teamsizeinfo.periods.size(); i++) {
			if (teamsizeinfo.periods.elementAt(i) == null) {
				teamsizeinfo.periods.removeElementAt(i);
				if (i != 0) {
					i--;
				}
			}
		}

		// Get team size for each period
		if (teamsizeinfo.periods.size() > 0) {

			int count1;
			int count2 = 0;
			long d1, d2, date3, date4;

			for (int l = 0; l < teamsizeinfo.periods.size(); l++) {
				count1 = 0;
				if (l < teamsizeinfo.periods.size() - 1) {
					d1 = ((Date) teamsizeinfo.periods.elementAt(l)).getTime();
					d2 = ((Date) teamsizeinfo.periods.elementAt(l + 1)).getTime();
					for (int i = 0; i < vectorTeamList.size(); i++) {
						info = (AssignmentInfo) vectorTeamList.elementAt(i);
						date3 = info.beginDate.getTime();
						date4 = info.endDate.getTime();

						if ((d1 >= date3) && (d2 <= date4) &&
                            (info.responsibilityID != 5) && // not count External into team size
                            (info.responsibilityID != 7) && // not count SQA into team size
                            (info.responsibilityID != 8)) { // not count PQA into team size
							count1++;
						}
					}
					teamsizeinfo.teamSize.add(count2, Integer.toString(count1));
					count2++;
				}
			}
		}
		return teamsizeinfo;
	}

	public static int getActualTeamSize(long prjID) {
		TeamSizeInfo info = new TeamSizeInfo();

		info = getTeamSizeProgress(prjID);

		//get max teamsize
		int teamSize;
		int teamSizeMax = 0;
		for (int i = 0; i < info.teamSize.size(); i++) {
			teamSize = Integer.parseInt((String) info.teamSize.elementAt(i));
			if (teamSize > teamSizeMax) {
                teamSizeMax = teamSize;
            }
		}
		return teamSizeMax;
	}

	public static final Vector getDevListByProject(final long prjID) {
		final Vector vectorResult = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql =
				"SELECT A.DEVELOPER_ID F1, B.NAME F2, B.ACCOUNT F3 FROM ASSIGNMENT A, DEVELOPER B WHERE A.PROJECT_ID = ? AND "
					+ "(A.DEVELOPER_ID = B.DEVELOPER_ID) AND (SYSDATE < A.END_DATE) AND (SYSDATE > A.BEGIN_DATE) ORDER BY B.ACCOUNT";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
                final PlanInterfacesInfo planInterfacesInfo = new PlanInterfacesInfo();
                planInterfacesInfo.setDeveloperId(rs.getInt("F1"));
                planInterfacesInfo.setDeveloperName(rs.getString("F2"));
                planInterfacesInfo.setDeveloperAccount(rs.getString("F3"));
                vectorResult.addElement(planInterfacesInfo);
			}
			return vectorResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
    public static final Vector getProjectRole(final long prjID, int roleID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql =
				"SELECT A.DEVELOPER_ID F1, B.NAME F2, B.ACCOUNT F3,A.END_DATE F4"
					+ " FROM ASSIGNMENT A, DEVELOPER B"
					+ " WHERE A.PROJECT_ID = ? "
					+ " AND A.DEVELOPER_ID = B.DEVELOPER_ID"
					+ " AND RESPONSE = ?"
					+ " ORDER BY A.END_DATE";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setInt(2, roleID);
			rs = stm.executeQuery();
			AssignmentInfo info;
			while (rs.next()) {
				info = new AssignmentInfo();
				info.devID = rs.getLong("F1");
				info.account = rs.getString("F3");
				info.devName = rs.getString("F2");
				info.endDate = rs.getDate("F4");
				resultVector.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	/**
	 * The vector must come from the function getProjectRole
	 * 
	 */
	public static final AssignmentInfo getProjectRoleAtDate(
		Vector roles,
		Date date) {
		AssignmentInfo info = null;
		AssignmentInfo infoBK = null;
		int size = roles.size() - 1;
		for (int i = size; i >= 0; i--) {
			info = (AssignmentInfo) roles.elementAt(i);
			if (info.endDate.compareTo(date) <= 0)
				return (infoBK != null) ? infoBK : info;
			infoBK = info;
		}
		return info;
	}

	public static final Vector getWOTeamList(final long prjID, String strAccount_Name, String strType) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT A.ASSIGNMENT_ID, A.PROJECT_ID PROJECT_ID, A.DEVELOPER_ID DEVELOPER_ID, D.NAME DEV_NAME, "
					+ " A.TYPE, A.BEGIN_DATE, A.END_DATE, R.NAME RES_NAME, A.NOTE, A.USAGE, D.ACCOUNT,R.RESPONSIBILITY_ID, D.GROUP_NAME,"
					+ " A.QUALIFICATION, A.TEAM_ID, T.TEAM_NAME"
					+ " FROM ASSIGNMENT A, DEVELOPER D,RESPONSIBILITY R, TEAM T"
					+ " WHERE PROJECT_ID = ? "
					+ " AND R.RESPONSIBILITY_ID=A.RESPONSE"
					+ " AND A.DEVELOPER_ID = D.DEVELOPER_ID"
					+ " AND A.TEAM_ID = T.TEAM_ID(+)"
					+ " ORDER BY R.ORDER_NUMBER, D.ACCOUNT";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final AssignmentInfo assInfo = new AssignmentInfo();
				assInfo.assID = rs.getLong("ASSIGNMENT_ID");
				assInfo.projectID = rs.getInt("PROJECT_ID");
				assInfo.devID = rs.getLong("DEVELOPER_ID");
				assInfo.devName = rs.getString("DEV_NAME");
				assInfo.type = rs.getInt("TYPE");
				assInfo.beginDate = rs.getDate("BEGIN_DATE");
				assInfo.endDate = rs.getDate("END_DATE");
				assInfo.roleID = rs.getString("RES_NAME");
				assInfo.note = rs.getString("NOTE");
				assInfo.workingTime = rs.getDouble("USAGE");
				assInfo.account = rs.getString("ACCOUNT");
				assInfo.responsibilityID = rs.getLong("RESPONSIBILITY_ID");
				assInfo.groupName = rs.getString("GROUP_NAME");
				assInfo.qualification = rs.getString("QUALIFICATION");
				assInfo.teamID = rs.getLong("TEAM_ID");
				assInfo.teamName = rs.getString("TEAM_NAME");
				
				// Modify by HaiMM: Canculate calendar effort
				double workingDays = CommonTools.getWorkingDays(assInfo.beginDate, assInfo.endDate);
				assInfo.total = workingDays * assInfo.workingTime / 100.0;
				 
				resultVector.addElement(assInfo);
			}
			return resultVector;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    
    // HaiMM add for Org Plan
	public static final Vector getOrgTotalValueList(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT *"
					+ " FROM org_plan_total"
					+ " WHERE PROJECT_ID = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final OrgTotalInfo orgTotalInfo = new OrgTotalInfo();
				orgTotalInfo.assID = rs.getLong("assignment_id");
				orgTotalInfo.projectID = rs.getInt("project_id");
				orgTotalInfo.totalValue = rs.getDouble("total_plan");
			 
				resultVector.addElement(orgTotalInfo);
			}
			return resultVector;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    
    
	/**
     * Add new assignment in FI/WO/Team
     * @param newInfo
     * @return
     */
    public static final boolean addAssignment(final AssignmentInfo newInfo) {
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		boolean returnValue = true;
        
    	try {
			conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
			final long assignment_id = ServerHelper.getNextSeq("ASSIGNMENT_SEQ");
            newInfo.assID = assignment_id;
			sql =
				"INSERT INTO ASSIGNMENT (ASSIGNMENT_ID, PROJECT_ID, DEVELOPER_ID, "
					+ " TYPE, BEGIN_DATE, END_DATE, RESPONSE, NOTE, USAGE, TEAM_ID, QUALIFICATION,"
                    + " PROJECT_POSITION_CODE) "
					+ " VALUES ( "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, assignment_id);
			preStm.setLong(2, newInfo.projectID);
			preStm.setLong(3, newInfo.devID);
			preStm.setInt(4, newInfo.type);
			preStm.setDate(5, newInfo.beginDate);
			preStm.setDate(6, newInfo.endDate);
			preStm.setLong(7, newInfo.responsibilityID);
			preStm.setString(8, newInfo.note);
			preStm.setDouble(9, newInfo.workingTime);
			if (newInfo.teamID != 0) preStm.setLong(10, newInfo.teamID);
			else preStm.setNull(10, Types.LONGVARCHAR);
			preStm.setString(11, newInfo.qualification);

            //-----------------------------------------------------------------
            // Update for compatible with RMS: Project position code
            //-----------------------------------------------------------------
            String positionCode =
                com.fms1.integrate.Project.getProjectPositionCodeById(
                    newInfo.responsibilityID);
            preStm.setString(12, positionCode);
            //-----------------------------------------------------------------
            // END: Update for compatible with RMS: Project position code
            //-----------------------------------------------------------------

            if (preStm.executeUpdate() <= 0) {
				returnValue = false;
			} else {
			    // Update defect_permission
			    if (!CommonTools
					.getDefectPermission(newInfo.responsibilityID)
					.equals("0000000000")) {
					//add permission to defect_permission
					if (checkForUpdateDefectPermission(newInfo.projectID, newInfo.devID)) {
						returnValue =
							updateAssignForDefectPermission(
								newInfo.projectID,
								newInfo.devID,
								CommonTools.getDefectPermission(
									newInfo.responsibilityID));
					} else {
						returnValue =
							addAssignForDefectPermission(
								newInfo.projectID,
								newInfo.devID,
								CommonTools.getDefectPermission(
									newInfo.responsibilityID));
					}
				}
				
                //-----------------------------------------------------------------
                // Update for compatible with RMS: User role
                //-----------------------------------------------------------------
                returnValue =
                    com.fms1.integrate.Project.updateRoleByAddAssignment(
                        newInfo);
                if (!returnValue) {
                    conn.rollback();
                }
                
			}
            conn.setAutoCommit(true);
            conn.commit();
            
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		} finally {
			ServerHelper.closeConnection(conn, preStm, null);
		}
	}
	
	public static final boolean updateBatchAssignment(final AssignmentInfo newInfo) {
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		boolean returnValue = true;
        
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql =
				"UPDATE ASSIGNMENT SET "
					+ " DEVELOPER_ID=?, "					
					+ " TYPE=?, "
					+ " BEGIN_DATE=?, "
					+ " END_DATE=?, "
					+ " RESPONSE=?, "
					+ " NOTE=?, "
					+ " USAGE=?, "
					+ " TEAM_ID=?, "
					+ " QUALIFICATION=?,"
					+ " PROJECT_POSITION_CODE=?"
					+ " WHERE ASSIGNMENT_ID=? ";
					 					
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, newInfo.devID);
			preStm.setInt(2, newInfo.type);
			
			preStm.setDate(3, newInfo.beginDate);
			preStm.setDate(4, newInfo.endDate);
			preStm.setLong(5, newInfo.responsibilityID);
			preStm.setString(6, newInfo.note);
			preStm.setDouble(7, newInfo.workingTime);
			if (newInfo.teamID != 0) preStm.setLong(8, newInfo.teamID);
			else preStm.setNull(8, Types.LONGVARCHAR);
			
			preStm.setString(9, newInfo.qualification);
			
			String positionCode = com.fms1.integrate.Project.getProjectPositionCodeById(newInfo.responsibilityID);
			preStm.setString(10, positionCode);
			preStm.setLong(11, newInfo.assID);			

			if (preStm.executeUpdate() <= 0) {
				returnValue = false;
			} else {
				// Update defect_permission
				if (!CommonTools
					.getDefectPermission(newInfo.responsibilityID)
					.equals("0000000000")) {
					//add permission to defect_permission
					if (checkForUpdateDefectPermission(newInfo.projectID, newInfo.devID)) {
						returnValue =
							updateAssignForDefectPermission(
								newInfo.projectID,
								newInfo.devID,
								CommonTools.getDefectPermission(
									newInfo.responsibilityID));
					} else {
						returnValue =
							addAssignForDefectPermission(
								newInfo.projectID,
								newInfo.devID,
								CommonTools.getDefectPermission(
									newInfo.responsibilityID));
					}
				}
				
				//-----------------------------------------------------------------
				// Update for compatible with RMS: User role
				//-----------------------------------------------------------------
				returnValue =
					com.fms1.integrate.Project.updateRoleByAddAssignment(
						newInfo);
				if (!returnValue) {
					conn.rollback();
				}
                
			}
			conn.setAutoCommit(true);
			conn.commit();
            
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		} finally {
			ServerHelper.closeConnection(conn, preStm, null);
		}
	}
	
	public static final boolean updateAssignment(
        AssignmentInfo newInfo,
        AssignmentInfo oldInfo)
    {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		long developer_id;
		long project_id;
		boolean returnValue = true;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql =
				"UPDATE RIGHTGROUPOFUSERBYWORKUNIT SET RIGHTGROUPID = "
				+ " (SELECT RIGHTGROUP FROM RESPONSIBILITY WHERE RESPONSIBILITY_ID = ?)"
				+ " WHERE WORKUNITID =? AND DEVELOPERID =?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.responsibilityID);
			stm.setLong(2, newInfo.workUnitID);
			stm.setLong(3, newInfo.devID);
			stm.executeUpdate();
			stm.close();

			sql =
				"UPDATE ASSIGNMENT 	SET  TYPE 	= ?"
					+ ",BEGIN_DATE	= ?"
					+ ",END_DATE	= ?"
					+ ",RESPONSE	= ?"
					+ ",NOTE	= ?"
					+ ",USAGE	= ?"
					+ ",TEAM_ID   = ?"
					+ ",QUALIFICATION   = ?"
            //-----------------------------------------------------------------
            // Update for compatible with RMS: Project position code
            //-----------------------------------------------------------------
                    + ",PROJECT_POSITION_CODE   = ?"					
            //-----------------------------------------------------------------
            // END: Update for compatible with RMS: Project position code
            //-----------------------------------------------------------------
					+ " WHERE ASSIGNMENT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setInt(1, newInfo.type);
			stm.setDate(2, newInfo.beginDate);
			stm.setDate(3, newInfo.endDate);
			stm.setLong(4, newInfo.responsibilityID);
			stm.setString(5, newInfo.note);
			stm.setDouble(6, newInfo.workingTime);
			if (newInfo.teamID != 0) stm.setLong(7, newInfo.teamID);
			else stm.setNull(7, Types.LONGVARCHAR);
			stm.setString(8, newInfo.qualification);
            //-----------------------------------------------------------------
            // Update for compatible with RMS: Project position code
            //-----------------------------------------------------------------
            String positionCode =
                com.fms1.integrate.Project.getProjectPositionCodeById(
                    newInfo.responsibilityID);
            stm.setString(9, positionCode);
            //-----------------------------------------------------------------
            // END: Update for compatible with RMS: Project position code
            //-----------------------------------------------------------------

            stm.setLong(10, newInfo.assID);
			if (stm.executeUpdate() != 0) {
                //--------------------------------------------------------------
                // Update for compatible with RMS: User role
                //--------------------------------------------------------------
                returnValue = com.fms1.integrate.Project.updateRoleByUpdateAssignment(
                        newInfo, oldInfo);
                if (!returnValue) {
                    conn.rollback();
                    returnValue = false;
                }
                //--------------------------------------------------------------
                // END: Update for compatible with RMS: User role
                //--------------------------------------------------------------
                else if (!CommonTools
					.getDefectPermission(newInfo.responsibilityID)
					.equals("0000000000")) {
					//get projectId and developerId from assignment by AssignId
					sql =
						"SELECT project_id, developer_id FROM assignment WHERE assignment_id = ? AND rownum = 1 ";
					stm = conn.prepareStatement(sql);
					stm.setLong(1, newInfo.assID);
					rs = stm.executeQuery();
					if (rs.next()) {
						project_id = rs.getLong("project_id");
						developer_id = rs.getLong("developer_id");
						if (checkForUpdateDefectPermission(project_id, developer_id)) {
							returnValue =
								updateAssignForDefectPermission(
									project_id,
									developer_id,
									CommonTools.getDefectPermission(
										newInfo.responsibilityID));
						} else {
							returnValue =
								addAssignForDefectPermission(
									project_id,
									developer_id,
									CommonTools.getDefectPermission(
										newInfo.responsibilityID));
						}
					}
				} else {
					//Delete from defect
					deleteAssignForDefectPermission(newInfo.assID, newInfo.devID, WorkUnit.getWorkUnitByProjectId((long)newInfo.projectID));
				}
			}else {
				conn.rollback();
				returnValue = false;
			}

			conn.commit();
			conn.setAutoCommit(true);
			return returnValue;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	 * get Priority follows responsibilityID of Responsibility table
     * @param responsibilityID
     * @return Priority
     */
    public static final int getPriorityOfResponsibility(long responsibilityID){
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT re.priority FROM responsibility re WHERE re.responsibility_id =?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, responsibilityID);
			rs = stm.executeQuery();
			int priority = 0;
			if (rs.next()) {
				priority = rs.getInt(1);
			}
			return priority;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}

    public static final boolean updateAssignment(final AssignmentInfo newInfo) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        long developerId;
        long projectId;
        boolean returnValue = true;
        try {
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);			
			//Update RIGHTGROUPOFUSERBYWORKUNIT table follows max priority of Assignment which the user have
			sql =
				" SELECT res.priority, res.responsibility_id FROM assignment a, responsibility res "
				+ "WHERE a.response = res.responsibility_id AND a.assignment_id <> ? "
				+ "AND a.project_id = ? AND a.developer_id = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.assID);
			stm.setLong(2, newInfo.projectID);
			stm.setLong(3, newInfo.devID);
			rs = stm.executeQuery();			
			int priorityOld = 0;
			int priorityNew = newInfo.priority;
			long responsibilityId = newInfo.responsibilityID;
			long assID = 0;
			while (rs.next()) {
				priorityOld = rs.getInt(1);
				if (priorityNew < priorityOld) {
					priorityNew = priorityOld;
					responsibilityId = rs.getLong(2);
				}
			}
			stm.close();
			rs.close();
			                
            sql =
                "UPDATE RIGHTGROUPOFUSERBYWORKUNIT SET RIGHTGROUPID = "
                    + " (SELECT RIGHTGROUP FROM RESPONSIBILITY WHERE RESPONSIBILITY_ID = ?)"
                    + " WHERE WORKUNITID =? AND DEVELOPERID =?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, responsibilityId);
            stm.setLong(2, newInfo.workUnitID);
            stm.setLong(3, newInfo.devID);
            stm.executeUpdate();
            stm.close();

			sql = "UPDATE ASSIGNMENT SET TYPE = ?"
				+ ",BEGIN_DATE	= ?"
				+ ",END_DATE	= ?"
				+ ",RESPONSE	= ?"
				+ ",NOTE		= ?"
				+ ",USAGE		= ?"
				+ " WHERE ASSIGNMENT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setInt(1, newInfo.type);
			stm.setDate(2, newInfo.beginDate);
			stm.setDate(3, newInfo.endDate);
			stm.setLong(4, newInfo.responsibilityID);
			stm.setString(5, newInfo.note);
			stm.setDouble(6, newInfo.workingTime);
			stm.setLong(7, newInfo.assID);
			if (stm.executeUpdate() != 0) {
				if (!CommonTools
					.getDefectPermission(newInfo.responsibilityID)
					.equals("0000000000")) {
					//get projectId and developerId from assignment by AssignId
					sql =
						"SELECT project_id, developer_id FROM assignment WHERE assignment_id = ? AND rownum = 1 ";
					stm = conn.prepareStatement(sql);
					stm.setLong(1, newInfo.assID);
					rs = stm.executeQuery();
					if (rs.next()) {
						projectId = rs.getLong("project_id");
						developerId = rs.getLong("developer_id");
						if (checkForUpdateDefectPermission(projectId, developerId)) {
							returnValue =
								updateAssignForDefectPermission(
									projectId,
									developerId,
									CommonTools.getDefectPermission(
										newInfo.responsibilityID));
						} else {
							returnValue =
								addAssignForDefectPermission(
									projectId,
									developerId,
									CommonTools.getDefectPermission(
										newInfo.responsibilityID));
						}
					}
				} else {
					//Delete from defect
					deleteAssignForDefectPermission(newInfo.assID, newInfo.devID, WorkUnit.getWorkUnitByProjectId((long)newInfo.projectID));
				}
			}else {
				conn.rollback();
				returnValue = false;
			}

			conn.commit();
			conn.setAutoCommit(true);
			return returnValue;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}

	public static final boolean deleteAssignment(AssignmentInfo assignmentInfo,
        long lDeveloperID, long lWorkUnitID)
    {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            int assignments = 0;
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
            sql = "SELECT COUNT(*) AS assignments " +
                " FROM assignment" +
                " WHERE (project_id, developer_id) IN (" +
                " SELECT project_id, developer_id FROM assignment" +
                " WHERE assignment_id = ?)";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, assignmentInfo.assID);
            rs = stm.executeQuery();
            if (rs.next()) {
                assignments = rs.getInt("assignments");
            }
            rs.close();
            stm.close();
            // Delete user role associated with project (granted from FI/Admin)
            if (assignments == 1) {
                sql =
                    " DELETE RIGHTGROUPOFUSERBYWORKUNIT " +
                    " WHERE DEVELOPERID = ? AND WORKUNITID = ?";
                stm = conn.prepareStatement(sql);
                stm.setLong(1, lDeveloperID);
                stm.setLong(2, lWorkUnitID);
                stm.executeUpdate();
                stm.close();
            }
            // Delete defect permission, used for old DMS, DMS2 not needed.
            if (!deleteAssignForDefectPermission(
                assignmentInfo.assID, lDeveloperID, lWorkUnitID)) {
                return false;
            }

            //------------------------------------------------------------------
            // RMS integrate: Remove user role at project 
            //------------------------------------------------------------------
            if (!com.fms1.integrate.Project.updateRoleByDeleteAssignment(
                assignmentInfo))
            {
                conn.rollback();
                return false;
            }
            //------------------------------------------------------------------
            // END - RMS integrate: Remove user role at project 
            //------------------------------------------------------------------

            sql = 
                "DELETE ASSIGNMENT " +
                " WHERE ASSIGNMENT_ID = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, assignmentInfo.assID);
            stm.executeUpdate();
            stm.close();

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
//        try {
//            if (deleteAssignForDefectPermission(assignmentInfo.assID, lDeveloperID, lWorkUnitID)) {
//				return true;
//            }
//			return false;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return false;
//		}
	}
    
	public static String getAssignmentOfUser(final long developerID, final long projectID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		String strRoleID = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();

			sql =
				"SELECT  RESPONSIBILITY.NAME"
					+ " FROM ASSIGNMENT,RESPONSIBILITY"
					+ " WHERE DEVELOPER_ID = ? AND PROJECT_ID = ?"
					+ " AND RESPONSIBILITY.RESPONSIBILITY_ID = ASSIGNMENT.RESPONSE";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, developerID);
			stm.setLong(2, projectID);
			rs = stm.executeQuery();
			while (rs.next()) {
				strRoleID = rs.getString("NAME");
			}            
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
            return strRoleID;
		}
	}
    
    /**
     * Check project allocation conflict with result returned from
     * checkProjectAllocationAdd(), checkProjectAllocationUpdate()
     * checkAllProjectAllocationAdd(), checkAllProjectAllocationUpdate()
     * @param vtAssignment
     * @param beginDate
     * @param endDate
     * @param workingTime
     * @return If conflicts found then return original vector (vtAssignment)
     * plus new record with first conflict found
     * (example: from 01-Jan-07 to 05-Jan-07 working time 110%).
     * Otherwise return null
     */
    public static Vector checkAllcationConflict(Vector vtAssignment, AssignmentInfo newAssignment) {
        Vector result = null;
        try {
            if (vtAssignment != null) {
                Date minDate = (Date) newAssignment.beginDate.clone();
                Date maxDate = (Date) newAssignment.endDate.clone();
                for (int i = 0; i < vtAssignment.size(); i++) {
                    AssignmentInfo assInfo = (AssignmentInfo)vtAssignment.get(i);
                    if (minDate.after(assInfo.beginDate)) {
                        minDate = assInfo.beginDate;
                    }
                    if (maxDate.before(assInfo.endDate)) {
                        maxDate = assInfo.endDate;
                    }
                }
                int days = CommonTools.daysBetween(minDate, maxDate) + 1;
                if (days > 0) {
                    // First: Create array to calculate user working time (%)
                    // for all days between minDate and maxDate
                    double arrWorkingTime[] = new double[days];
                    java.util.Arrays.fill(arrWorkingTime, 0.00);
                    // Next: Calculate working time of each day based on all assignments
                    int iDay = 0;
                    for (int i = 0; i < vtAssignment.size(); i++) {
                        AssignmentInfo assInfo = (AssignmentInfo) vtAssignment.get(i);
                        int daysMinDate_beginDate = CommonTools.daysBetween(minDate, assInfo.beginDate);
                        int daysMinDate_endDate = CommonTools.daysBetween(minDate, assInfo.endDate);
                        // Increase each day working time by this assignment
                        // working time between begin date, end date
                        for (iDay = daysMinDate_beginDate;
                            iDay < (daysMinDate_endDate + 1);// Include endDate => plus 1
                            iDay++) {
                            arrWorkingTime[iDay] += assInfo.workingTime;
                        }
                    }
                    // Next: Include calculate for assignment that try to add/update
                    int daysMinDate_beginDate = CommonTools.daysBetween(minDate, newAssignment.beginDate);
                    int daysMinDate_endDate = CommonTools.daysBetween(minDate, newAssignment.endDate);
                    // Increase each day working time following by
                    // assignment working time between begin date, end date
                    for (iDay = daysMinDate_beginDate;
                        iDay < (daysMinDate_endDate + 1);// Include endDate => plus 1
                        iDay++) {
                        arrWorkingTime[iDay] += newAssignment.workingTime;
                    }

                    // Find the first day that working time assigned in project(s) > 100%
                    // If not found then iDay passed the last index of array (iDay = array lengtn)
                    for (iDay = 0; iDay < arrWorkingTime.length; iDay++) {
                        if (arrWorkingTime[iDay] > 100) {
                            break;
                        }
                    }
                    // Next: Found the day that working time assigned in project(s) > 100%
                    // => return all assignments and the conflict period
                    if (iDay < arrWorkingTime.length) {
                        result = vtAssignment;
                        result.add(newAssignment);  // Include new assignment
                        AssignmentInfo conflictAssignment = new AssignmentInfo();
                        conflictAssignment.beginDate =
                            new Date(minDate.getTime() + iDay * MS_PER_DAY);
                        conflictAssignment.workingTime = arrWorkingTime[iDay];
                        // Search the longest conflict period that has
                        // working time the same with the first found
                        for (/*iDay = iDay*/; iDay < arrWorkingTime.length; iDay++) {
                            if (arrWorkingTime[iDay] != conflictAssignment.workingTime) {
                                break;
                            }
                        }
                        conflictAssignment.endDate =
                            new Date(minDate.getTime() + (iDay - 1) * MS_PER_DAY);
                        conflictAssignment.devName = newAssignment.devName;
                        conflictAssignment.account = newAssignment.account;
                        conflictAssignment.projectCode = newAssignment.projectCode;
                        result.add(conflictAssignment);  // Conflict information stored as normal assignment
                    }
                }
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }
    
    /**
     * The first one check this user in this project who have been assigned. 
     * If existed aler to user should update information insteed add new data.
     * else: 
     * Check a project try to allocate a member with more than 100% effort in some periods
     * Example: if this member already assigned to project X from 01-Jan-07 to
     * 31-Jan-07 with working time 70% then cannot assign this user again in
     * project X from 15-Dec-06 to 05-Jan-07 with working time 40% because
     * from 01-Jan-07 to 05-Jan-07 this member effort for project X is 110%
     * @param assInfo
     * @return List of assignments with conflict period(s) or null if not conflict
     */
    public static Vector checkProjectAllocationAdd(AssignmentInfo newAssignment) {
		Vector vtAssignments = new Vector();
		Vector vtProjectAllocationConflict = null;
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            sql =
                "SELECT  p.CODE, a.BEGIN_DATE, a.END_DATE, a.USAGE, d.NAME DEV_NAME, d.ACCOUNT DEV_ACCOUNT, a.TYPE, a.RESPONSE"
                    + " FROM ASSIGNMENT a, PROJECT p, DEVELOPER d"
                    + " WHERE a.PROJECT_ID = p.PROJECT_ID"
                    + " AND a.DEVELOPER_ID = d.DEVELOPER_ID"
                    + " AND a.PROJECT_ID = ? AND a.DEVELOPER_ID = ?"
                    + " AND a.END_DATE >= ? AND a.BEGIN_DATE <= ?";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, newAssignment.projectID);
            stm.setLong(2, newAssignment.devID);
            stm.setDate(3, newAssignment.beginDate);
            stm.setDate(4, newAssignment.endDate);
            rs = stm.executeQuery();
            while (rs.next()) {
                AssignmentInfo assignment = new AssignmentInfo();
                assignment.projectCode = rs.getString("CODE");
                assignment.devName = rs.getString("DEV_NAME");
                assignment.account = rs.getString("DEV_ACCOUNT");
                assignment.beginDate = rs.getDate("BEGIN_DATE");
                assignment.endDate = rs.getDate("END_DATE");
                assignment.workingTime = rs.getLong("USAGE");
                assignment.type = rs.getInt("TYPE");
                assignment.responsibilityID = rs.getInt("RESPONSE");
                vtAssignments.add(assignment);
            }
			if (vtAssignments.size() > 0) {
				//Check this assignment have been existed with: account, role, type.
				/*
				for (int i = 0; i < vtAssignments.size(); i++){
					AssignmentInfo assignmentInfo = (AssignmentInfo)vtAssignments.get(i);
					if (assignmentInfo.type == newAssignment.type && 
							assignmentInfo.responsibilityID == newAssignment.responsibilityID){
						vtProjectAllocationConflict = new Vector();
						break;
					}
				}
				*/
				/* if (vtProjectAllocationConflict == null){ */
					// Check user allocation on this project not > 100% in some periods
					vtProjectAllocationConflict = Assignments.checkAllcationConflict(vtAssignments, newAssignment);
				/*} */
			}
           
        } 
        catch (SQLException ex){
        	ex.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vtProjectAllocationConflict;
        }
    }
    
    /**
     * Check to try to allocate a member with more than 100% effort in some period for all projects
     * Example: if this member already assigned to project X from 01-Jan-07 to
     * 31-Jan-07 with working time 70% then should warn about assignment of this user
     * for project Y from 15-Dec-06 to 05-Jan-07 with working time 40% because
     * from 01-Jan-07 to 05-Jan-07 this member effort for projects X,Y is 110%
     * @param devID
     * @param beginDate
     * @param endDate
     * @param workingTime
     * @return List of assignments with conflict period(s) or null if not conflict
     */
    public static Vector checkAllProjectAllocationAdd(AssignmentInfo newAssignment) {
        Vector result = null;
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            Vector vtAssignments = new Vector();
            sql =
                "SELECT  p.CODE, a.BEGIN_DATE, a.END_DATE, a.USAGE, d.NAME DEV_NAME, d.ACCOUNT DEV_ACCOUNT"
                    + " FROM ASSIGNMENT a, PROJECT p, DEVELOPER d"
                    + " WHERE a.PROJECT_ID = p.PROJECT_ID"
                    + " AND a.DEVELOPER_ID = d.DEVELOPER_ID"
                    + " AND a.DEVELOPER_ID = ?"
                    + " AND a.END_DATE >= ? AND a.BEGIN_DATE <= ?";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, newAssignment.devID);
            stm.setDate(2, newAssignment.beginDate);
            stm.setDate(3, newAssignment.endDate);
            rs = stm.executeQuery();
            while (rs.next()) {
                AssignmentInfo assignment = new AssignmentInfo();
                assignment.projectCode = rs.getString("CODE"); 
                assignment.devName = rs.getString("DEV_NAME");
                assignment.account = rs.getString("DEV_ACCOUNT");
                assignment.beginDate = rs.getDate("BEGIN_DATE");
                assignment.endDate = rs.getDate("END_DATE");
                assignment.workingTime = rs.getLong("USAGE");
                vtAssignments.add(assignment);
            }
            if (vtAssignments.size() > 0) {
                result = checkAllcationConflict(vtAssignments, newAssignment);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return result;
        }
    }
    
    /**
     * Check a project try to allocate a member with more than 100% effort in some periods
     * Example: if this member already assigned to project X from 01-Jan-07 to
     * 31-Jan-07 with working time 70% then cannot assign this user again in
     * project X from 15-Dec-06 to 05-Jan-07 with working time 40% because
     * from 01-Jan-07 to 05-Jan-07 this member effort for project X is 110%
     * @param assInfo
     * @return
     * @return List of assignments with conflict period(s) or null if not conflict
     */
    public static Vector checkProjectAllocationUpdate(AssignmentInfo newAssignment) {
        Vector result = null;
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            Vector vtAssignments = new Vector();
            sql =
                "SELECT  p.CODE, a.BEGIN_DATE, a.END_DATE, a.USAGE, d.NAME DEV_NAME, d.ACCOUNT DEV_ACCOUNT"
                    + " FROM ASSIGNMENT a, PROJECT p, DEVELOPER d"
                    + " WHERE a.PROJECT_ID = p.PROJECT_ID"
                    + " AND a.DEVELOPER_ID = d.DEVELOPER_ID"
                    + " AND a.PROJECT_ID = ? AND a.DEVELOPER_ID = ?"
                    + " AND a.END_DATE >= ? AND a.BEGIN_DATE <= ?"
                    + " AND a.ASSIGNMENT_ID <> ?";  // Exclude updating assignment
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, newAssignment.projectID);
            stm.setLong(2, newAssignment.devID);
            stm.setDate(3, newAssignment.beginDate);
            stm.setDate(4, newAssignment.endDate);
            stm.setLong(5, newAssignment.assID);
            rs = stm.executeQuery();
            while (rs.next()) {
                AssignmentInfo assignment = new AssignmentInfo();
                assignment.projectCode = rs.getString("CODE"); 
                assignment.devName = rs.getString("DEV_NAME");
                assignment.account = rs.getString("DEV_ACCOUNT");
                assignment.beginDate = rs.getDate("BEGIN_DATE"); 
                assignment.endDate = rs.getDate("END_DATE");
                assignment.workingTime = rs.getLong("USAGE");
                vtAssignments.add(assignment);
            }
            if (vtAssignments.size() > 0) {
                result = checkAllcationConflict(vtAssignments, newAssignment);
            }
        }
        catch (SQLException ex){
        	ex.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return result;
        }
    }
    
    /**
     * Check to try to allocate a member with more than 100% effort in some period for all projects
     * Example: if this member already assigned to project X from 01-Jan-07 to
     * 31-Jan-07 with working time 70% then should warn about assignment of this user
     * for project Y from 15-Dec-06 to 05-Jan-07 with working time 40% because
     * from 01-Jan-07 to 05-Jan-07 this member effort for projects X,Y is 110%
     * @param devID
     * @param beginDate
     * @param endDate
     * @param workingTime
     * @return List of assignments with conflict period(s) or null if not conflict
     */
    public static Vector checkAllProjectAllocationUpdate(AssignmentInfo newAssignment) {
        Vector result = null;
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            Vector vtAssignments = new Vector();
            sql =
                "SELECT  p.CODE, a.BEGIN_DATE, a.END_DATE, a.USAGE, d.NAME DEV_NAME, d.ACCOUNT DEV_ACCOUNT"
                    + " FROM ASSIGNMENT a, PROJECT p, DEVELOPER d"
                    + " WHERE a.PROJECT_ID = p.PROJECT_ID"
                    + " AND a.DEVELOPER_ID = d.DEVELOPER_ID"
                    + " AND a.DEVELOPER_ID = ?"
                    + " AND a.END_DATE >= ? AND a.BEGIN_DATE <= ?"
                    + " AND a.ASSIGNMENT_ID <> ?";  // Exclude updating assignment
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, newAssignment.devID);
            stm.setDate(2, newAssignment.beginDate);
            stm.setDate(3, newAssignment.endDate);
            stm.setLong(4, newAssignment.assID);
            rs = stm.executeQuery();
            while (rs.next()) {
                AssignmentInfo assignment = new AssignmentInfo();
                assignment.projectCode = rs.getString("CODE");
                assignment.devName = rs.getString("DEV_NAME");
                assignment.account = rs.getString("DEV_ACCOUNT");
                assignment.beginDate = rs.getDate("BEGIN_DATE");
                assignment.endDate = rs.getDate("END_DATE");
                assignment.workingTime = rs.getLong("USAGE");
                vtAssignments.add(assignment);
            }
            if (vtAssignments.size() > 0) {
                result = checkAllcationConflict(vtAssignments, newAssignment);
            }
        }
        catch (SQLException ex){
        	ex.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return result;
        }
    }
    
    /**
     * Get project actual calendar effort up to current date and following by
     * FSoft working time (exclude Sunday, half of Saturday)
     * Calculate result returned from getActualCalendarEffort(ProjectInfo,Date)
     * @param vtAssignments
     * @return
     */
    public static double getActualCalendarEffort(Vector vtAssignments) {
        double calendarEffort = 0.0;
        try {
            double workingDays = 0;
            for (int i = 0; i < vtAssignments.size(); i++) {
                AssignmentInfo assignment =
                    (AssignmentInfo) vtAssignments.elementAt(i);
                workingDays = CommonTools.getWorkingDays(assignment.beginDate,
                    assignment.endDate);
                calendarEffort += workingDays * assignment.workingTime / 100.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return calendarEffort;
        }
    }
    
    /**
     * Get project actual calendar effort up to specified date exclude Sunday, half of Saturday
     * Formula: Actual Duration of team members (include Comtors, PM, exclude QA
     *  & external user) up to specified date * % Effort allocated of team members
     * @param prjInfo
     * @param untilDate Get actual calendar up to this date
     * @return
     */
    public static double getActualCalendarEffort(ProjectInfo prjInfo, java.util.Date uptoDate) {
        double calendarEffort = 0.0;
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            java.util.Date milestone;   // Calculate up to this date
            if (uptoDate != null) {
                milestone = uptoDate;
            }
            // If project is closed then the milestone is project closed date
            // 03-Mar-07: temporary disable status closed condition
            else if (/* (prjInfo.status == ProjectInfo.STATUS_CLOSED) && */
                        (prjInfo.getActualFinishDate() != null)) {
                milestone = prjInfo.getActualFinishDate();
            }
            else {  // Otherwise, the milestone is current system date
                milestone = new java.util.Date();
            }
            Vector vtAssignments = new Vector();
            // Select all resource allocations exclude External(5),SQA(7),PQA(8)
            sql =
                "SELECT  a.begin_date, a.end_date, a.usage"
                    + " FROM ASSIGNMENT a"
                    + " WHERE a.project_id = ? AND a.begin_date <= ?"
                    + " AND a.response NOT IN (5,7,8)";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjInfo.getProjectId());
            stm.setDate(2, new java.sql.Date(milestone.getTime()));
            rs = stm.executeQuery();
            while (rs.next()) {
                AssignmentInfo assignment = new AssignmentInfo();
                assignment.beginDate = rs.getDate("begin_date"); 
                assignment.endDate = rs.getDate("end_date");
                assignment.workingTime = rs.getLong("usage");
                // Get resource allocation until milestone, not the whole assignment
                if (assignment.endDate.after(milestone)) {
                    assignment.endDate.setTime(milestone.getTime());
                }
                vtAssignments.add(assignment);
            }
            calendarEffort = getActualCalendarEffort(vtAssignments);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return calendarEffort;
        }
    }
    
    /**
     * Get project total calendar effort exclude Sunday, half of Saturday
     * Formula: Actual Duration of team members (include Comtors, PM, exclude QA
     *  & external user) * % Effort allocated of team members
     * @param prjInfo
     * @return
     */
    public static double getTotalCalendarEffort(ProjectInfo prjInfo) {
        // Return total project calendar effort. Until 9999, this tool is used or not? 
        return getActualCalendarEffort(prjInfo, new java.util.Date("31-Dec-9999"));
    }
    
    /**
     * Get project total calendar effort exclude Sunday, half of Saturday
     * Formula: Actual Duration of team members (include Comtors, PM, exclude QA
     *  & external user) * % Effort allocated of team members
     * @param prjInfo
     * @return
     */
    public static double getTotalCalendarEffort(long projectId) {
        // Return total project calendar effort. Until 9999, this tool is used or not?
		return getActualCalendarEffort(Project.getProjectInfo(projectId), new java.util.Date("31-Dec-9999"));
    }
    
    /**
     * Get ResponsibilityInfo following 
     * @param lResponseInfo
     * @return ResponsibilityInfo
     */
    public static ResponsibilityInfo getAssignmentInfoByID(long lResponseInfo) {
    	Connection conn = null;
    	PreparedStatement preStm = null;
    	String sql = null;
    	ResultSet rs = null;
    	ResponsibilityInfo resInfo = new ResponsibilityInfo();
    	try{
    		conn = ServerHelper.instance().getConnection();
    		sql = "SELECT RESPONSIBILITY_ID, NAME, RIGHTGROUP FROM RESPONSIBILITY WHERE RESPONSIBILITY_ID = ?";
    		preStm = conn.prepareStatement(sql);
    		preStm.setLong(1, lResponseInfo);
    		rs = preStm.executeQuery();
    		if (rs.next()) {
    			resInfo.id = rs.getLong("RESPONSIBILITY_ID");
    			resInfo.name = rs.getString("NAME");
    			resInfo.rightGroup = rs.getString("RIGHTGROUP");
    		}
    		return resInfo;
    	} catch (SQLException ex) {
    		ex.printStackTrace();
    		return resInfo;
    	} finally {
    		ServerHelper.closeConnection(conn, preStm, rs);
    	}
    }
	
    public static Vector getResponsibilityList() {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector retVal = new Vector();
		try {
			sql = "SELECT  RESPONSIBILITY_ID,NAME, RIGHTGROUP"
                + " FROM RESPONSIBILITY"
            
            //------------------------------------------------------------------
            // 14-Jan-08: Added this line of code for compatible with RMS, avoid
            // records inserted by RMS, all new inserted data will not has ID
            // (null)
            //------------------------------------------------------------------
                + " WHERE responsibility_id IS NOT NULL" 
                + " AND ISDROP IS NULL"; // HaiMM: Get Roles is not droped only
				
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			ResponsibilityInfo inf;
			while (rs.next()) {
				inf = new ResponsibilityInfo();
				inf.name = rs.getString("NAME");
				inf.id = rs.getLong("RESPONSIBILITY_ID");
				inf.rightGroup = rs.getString("RIGHTGROUP");
				retVal.add(inf);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return retVal;
		}
	}
    
	/**
	 * get User infomation assigned into the project whose account or name contain strAccount_Name
	 * @param lProjectID
	 * @param strAccount_Name
	 * @param strType
	 * @return
	 */
	public static Vector getAllUserAssignment(final long lProjectID, String strAccount_Name, String strType, int typeQuery) {
		Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		String strCondition = "";
		try {
            if (strAccount_Name != null) {
				strAccount_Name = ConvertString.toSql(strAccount_Name.trim().toUpperCase(), ConvertString.ad_Search_);
            }
            else {
                strAccount_Name = "";
            }
			if ("Name".equals(strType)){
				strCondition += CommonTools.doCreateSQLCondition("D.NAME", strAccount_Name, typeQuery);
			}
			else if ("Account".equals(strType)) {
				strCondition += CommonTools.doCreateSQLCondition("D.ACCOUNT", strAccount_Name, typeQuery);
			}
            sql =
                " SELECT DISTINCT D.DEVELOPER_ID, D.NAME DEV_NAME, D.ACCOUNT, D.GROUP_NAME"
				    + " FROM ASSIGNMENT A, DEVELOPER D"
				    + " WHERE A.DEVELOPER_ID = D.DEVELOPER_ID "
				    + " AND PROJECT_ID = " + lProjectID
				    + strCondition
				    + " AND D.STATUS != 4 "
				    + " ORDER BY D.ACCOUNT";

			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				AssignmentInfo assInfo = new AssignmentInfo();
				assInfo.devID = rs.getLong("DEVELOPER_ID");
				assInfo.devName = rs.getString("DEV_NAME");
				assInfo.account = rs.getString("ACCOUNT");
				assInfo.groupName = rs.getString("GROUP_NAME");
				resultVector.addElement(assInfo);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
    
	/**
	 * get User infomation assigned into the project follow extractly account or Name
	 * @param lProjectID
	 * @param strAccount_Name
	 * @param strType
	 * @return
	 */
	public static AssignmentInfo getUserAssignment(final long lProjectID, String strAccount_Name, String strType) {
		AssignmentInfo resultVector = null;
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		String strCondition = "";
		try {
			if (strAccount_Name != null) {
				if ("Name".equals(strType)) {
					strCondition += CommonTools.doCreateSQLCondition("D.NAME", strAccount_Name, ConvertString.adCheck);
				} else {
					strCondition += CommonTools.doCreateSQLCondition("D.ACCOUNT", strAccount_Name, ConvertString.adCheck);
				}
			}
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT A.ASSIGNMENT_ID, A.PROJECT_ID Project_id, A.DEVELOPER_ID DEVELOPER_ID, D.NAME Dev_name, "
					+ " A.TYPE, A.BEGIN_DATE, A.END_DATE, R.NAME RES_NAME, A.NOTE, A.USAGE, D.ACCOUNT,R.RESPONSIBILITY_ID, D.GROUP_NAME"
					+ " FROM ASSIGNMENT A, DEVELOPER D,RESPONSIBILITY R "
					+ " WHERE PROJECT_ID = " + lProjectID
					+ " AND R.RESPONSIBILITY_ID=A.RESPONSE"
					+ " AND A.DEVELOPER_ID = D.DEVELOPER_ID"
					+ strCondition
					+ " AND D.STATUS != 4 "
					+ " ORDER BY R.ORDER_NUMBER, D.ACCOUNT";
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				resultVector = new AssignmentInfo();
				resultVector.assID = rs.getLong("ASSIGNMENT_ID");
				resultVector.projectID = rs.getInt("Project_id");
				resultVector.devID = rs.getLong("DEVELOPER_ID");
				resultVector.devName = rs.getString("Dev_name");
				resultVector.type = rs.getInt("TYPE");
				resultVector.beginDate = rs.getDate("BEGIN_DATE");
				resultVector.endDate = rs.getDate("END_DATE");
				resultVector.roleID = rs.getString("RES_NAME");
				resultVector.note = rs.getString("NOTE");
				resultVector.workingTime = rs.getDouble("USAGE");
				resultVector.account = rs.getString("ACCOUNT");
				resultVector.responsibilityID = rs.getLong("RESPONSIBILITY_ID");
				resultVector.groupName = rs.getString("GROUP_NAME");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}	

	public static Vector getAssignSubTeam(final long subTeamID) {
		final Vector resultVector = new Vector();		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;		
		try {
			conn = ServerHelper.instance().getConnection();
			sql =     " SELECT B.TEAM_NAME AS NAME, C.ACCOUNT AS ACCOUNT, A.ASSIGNMENT_ID AS ASS_ID"
					+ " FROM TEAM B, ASSIGNMENT A, DEVELOPER C"
					+ " WHERE B.TEAM_ID = A.TEAM_ID"
					+ " AND A.DEVELOPER_ID = C.DEVELOPER_ID"
					+ " AND B.TEAM_ID = ?";			
					
			stm = conn.prepareStatement(sql);
			stm.setLong(1, subTeamID);			
			rs = stm.executeQuery();
			while (rs.next()) {
				final AssignSubTeamInfo assignSubTeamInfo = new AssignSubTeamInfo();
				assignSubTeamInfo.subTeamName = rs.getString("NAME");
				assignSubTeamInfo.account = rs.getString("ACCOUNT");
				assignSubTeamInfo.assID = rs.getLong("ASS_ID");								
				resultVector.addElement(assignSubTeamInfo); 
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
}