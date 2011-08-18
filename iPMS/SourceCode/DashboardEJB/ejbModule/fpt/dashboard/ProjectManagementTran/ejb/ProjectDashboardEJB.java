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
 
 package fpt.dashboard.ProjectManagementTran.ejb;

import java.rmi.RemoteException;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;

import fpt.dashboard.constant.*;
import fpt.dashboard.framework.connection.*;

public class ProjectDashboardEJB implements SessionBean {
	//*******************************************
	// Internal properties
	private DataSource ds = null;
	private Connection con = null;
	private SessionContext sc;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	//*******************************************
	// Exposed methods
	private int ProjectID;
	private String Name;
	private String Code;
	private String dt_start;
	private String dt_base_finish;
	private String dt_plan_finish;
	private String dt_actual_finish;
	private int int_base_effort;
	private int int_plan_effort;
	private int int_actual_effort;
	private int per_complete;
	private String txt_description;
	private int totalRequirement;
	private int committedRequirement;
	private int designedRequirement;
	private int codedRequirement;
	private int testedRequirement;
	private int deployedRequirement;
	private int acceptedRequirement;
	private int totalDefect;
	private int totalWeightedDefect;
	private int fatalPendingDefect;
	private int seriousPendingDefect;
	private int mediumPendingDefect;
	private int cosmeticPendingDefect;
	private int totalFatalDefect;
	private int totalSeriousDefect;
	private int totalMediumDefect;
	private int totalCosmeticDefect;
	private float schedule_status;
	private float effort_status;
	public ProjectDashboardEJB() {
	}
	public String getID() {
		return "" + ProjectID;
	}
	public String getName() {
		return Name;
	}
	public String getDescription() {
		return txt_description;
	}
	public String getCode() {
		return Code;
	}
	public String getStart_date() {
		return dt_start;
	}
	public String getBase_finish() {
		return dt_base_finish;
	}
	public String getPlan_finish() {
		return dt_plan_finish;
	}
	public String getActual_finish() {
		return dt_actual_finish;
	}
	public int getTotalRequirement() {
		return totalRequirement;
	}
	public int getCommittedRequirement() {
		return committedRequirement;
	}
	public int getDesignedRequirement() {
		return designedRequirement;
	}
	public int getCodedRequirement() {
		return codedRequirement;
	}
	public int getTestedRequirement() {
		return testedRequirement;
	}
	public int getDeployedRequirement() {
		return deployedRequirement;
	}
	public int getAcceptedRequirement() {
		return acceptedRequirement;
	}
	public int getTotalDefect() {
		return totalDefect;
	}
	public int getTotalWeightedDefect() {
		return totalWeightedDefect;
	}
	public int getFatalPendingDefect() {
		return fatalPendingDefect;
	}
	public int getSeriousPendingDefect() {
		return seriousPendingDefect;
	}
	public int getMediumPendingDefect() {
		return mediumPendingDefect;
	}
	public int getCosmeticPendingDefect() {
		return cosmeticPendingDefect;
	}
	public int getTotalFatalDefect() {
		return totalFatalDefect;
	}
	public int getTotalSeriousDefect() {
		return totalSeriousDefect;
	}
	public int getTotalMediumDefect() {
		return totalMediumDefect;
	}
	public int getTotalCosmeticDefect() {
		return totalCosmeticDefect;
	}
	public int getBase_effort() {
		return int_base_effort;
	}
	public int getPlan_effort() {
		return int_plan_effort;
	}
	public int getActual_effort() {
		return int_actual_effort;
	}
	public int getPer_complete() {
		return per_complete;
	}
	public float getSchedule_status() {
		return schedule_status;
	}
	public float getEffort_status() {
		return effort_status;
	}
	public void ejbCreate() throws CreateException {
		if (con == null) {
			try {
				System.out.println("ProjectDashboardEJB.ejbCreate(): get JNDI connection.");
				ds = conPool.getDS();
				con = ds.getConnection();
			} catch (Exception e) {
				System.out.println("Exception in ProjectDashboardEJB: ejbCreate(): " + e.toString());
				throw new CreateException(e.getMessage());
			}
		} else {
			System.out.println("connection exists");
		}
	}
	public void updateProject(
		int per_complete,
		String dt_plan_finish,
		String dt_actual_finish,
		int int_plan_effort,
		int int_actual_effort,
		String txt_description,
		int totalRequirement,
		int committedRequirement,
		int designedRequirement,
		int codedRequirement,
		int testedRequirement,
		int deployedRequirement,
		int acceptedRequirement,
		int totalDefect,
		int totalWeightedDefect,
		int fatalPendingDefect,
		int seriousPendingDefect,
		int mediumPendingDefect,
		int cosmeticPendingDefect,
		int totalFatalDefect,
		int totalSeriousDefect,
		int totalMediumDefect,
		int totalCosmeticDefect)
		throws SQLException {
		PreparedStatement prepStmt = null;
		try {
			String strSQL =
				"UPDATE Project set per_complete = ?, "
					+ "plan_finish_date = TO_DATE(?,'dd/mm/yy'),actual_finish_date = TO_DATE(?,'dd/mm/yy'), "
					+ "plan_effort = ?, actual_effort = ?, description = ? ,"
					+ "totalRequirement = ?,"
					+ "committedRequirement = ?,"
					+ "designedRequirement = ?,"
					+ "codedRequirement = ?,"
					+ "testedRequirement = ?,"
					+ "deployedRequirement   = ?,"
					+ "acceptedRequirement   = ?,"
					+ "totalDefect = ?,"
					+ "totalWeightedDefect = ?,"
					+ "fatalPendingDefect = ?,"
					+ "seriousPendingDefect = ?,"
					+ "mediumPendingDefect = ?,"
					+ "cosmeticPendingDefect = ?,"
					+ "totalFatalDefect = ?,"
					+ "totalSeriousDefect    = ?,"
					+ "totalMediumDefect = ?,"
					+ "totalCosmeticDefect = ? "
					+ " WHERE Project_ID = ?";

			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);
			prepStmt.setInt(1, per_complete);
			prepStmt.setString(2, dt_plan_finish);
			prepStmt.setString(3, dt_actual_finish);
			prepStmt.setInt(4, int_plan_effort);
			prepStmt.setInt(5, int_actual_effort);
			prepStmt.setString(6, txt_description);
			prepStmt.setInt(7, totalRequirement);
			prepStmt.setInt(8, committedRequirement);
			prepStmt.setInt(9, designedRequirement);
			prepStmt.setInt(10, codedRequirement);
			prepStmt.setInt(11, testedRequirement);
			prepStmt.setInt(12, deployedRequirement);
			prepStmt.setInt(13, acceptedRequirement);
			prepStmt.setInt(14, totalDefect);
			prepStmt.setInt(15, totalWeightedDefect);
			prepStmt.setInt(16, fatalPendingDefect);
			prepStmt.setInt(17, seriousPendingDefect);
			prepStmt.setInt(18, mediumPendingDefect);
			prepStmt.setInt(19, cosmeticPendingDefect);
			prepStmt.setInt(20, totalFatalDefect);
			prepStmt.setInt(21, totalSeriousDefect);
			prepStmt.setInt(22, totalMediumDefect);
			prepStmt.setInt(23, totalCosmeticDefect);
			prepStmt.setInt(24, ProjectID);
			prepStmt.executeQuery();
		} catch (SQLException e) {
			throw new SQLException(
				"SQLException occurs in ProjectDashboardEJB.updateProject() - "
					+ e.toString());
		} finally {
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Friday, August 16, 2002 - Thaison
	public Collection getProjectDashboard(
		String strType,
		String strGroup,
		String strStatus,
        String strCategory,
        String strDeveloperId,
		int nOrderBy)
		throws SQLException {
		ArrayList listResult = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		String strNotArchiveStatus = " archive_status <> 4 ";
		String strSQL =
			"SELECT Project_ID, Code, Name, per_complete, "
				+ "TO_CHAR(start_date,'dd/mm/yy') AS Start_date, TO_CHAR(Base_Finish_date,'dd/mm/yy') AS Base_Finish_date, "
				+ "TO_CHAR(plan_finish_date,'dd/mm/yy') AS plan_finish_date,TO_CHAR(actual_finish_date,'dd/mm/yy') AS actual_finish_date,"
				+ "base_effort,plan_effort, actual_effort,schedule_status,effort_status,schedule_status*effort_status AS byOrder,start_date AS OrDate,Base_Finish_date AS OrBaseDate "
				+ "FROM Project WHERE type ";
		//ThaiLH                    
		if (Integer.parseInt(strType) != -1) {
			strSQL += "=" + strType;
		} else {
			strSQL += "in  (0,8) ";
		}
		if (strGroup != null) {
			if ((!Constants.GROUP_ALL.equals(strGroup))
				&& !strGroup.equals("")) {
				strSQL += " AND Group_name = '" + strGroup + "'";
			}
		}
        //added by MinhPT 
        //for select in category
        if (strCategory.trim().length() > 0) {
            if (Integer.parseInt(strCategory) != -1)
                strSQL += " AND Category ='" + strCategory + "'";
            else {
                 // 0,1,2.
                strSQL += " AND Category in ('0','1','2')";
            }
        }

		if (strStatus != null) {
			if ((Integer.parseInt(strStatus) != -1) && !strStatus.equals("")) {
				strSQL += " AND status =" + strStatus;
			}
		}
        
        // Get only projects that this developer has been assigned to
        if (strDeveloperId != null) {
            strSQL += " AND project_id IN (" +
                    "SELECT project_id FROM assignment WHERE developer_id=" +
                    strDeveloperId + ")";
        }
        strSQL += " AND ";
        strSQL += strNotArchiveStatus;
		switch (nOrderBy) {
			case 0 :
				strSQL += " ORDER BY name ";
				break;
			case 1 :
				strSQL += " ORDER BY Code ";
				break;
			case 2 :
				strSQL += " ORDER BY Name ";
				break;
			case 3 :
				strSQL += " ORDER BY OrDate DESC ";
				break;
			case 4 :
				strSQL += " ORDER BY OrBaseDate ";
				break;
		}
        if (DB.DEBUG) {
            System.out.println("ProjectDashboardEJB.getProjectDashboard(): " +strSQL);
        }
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);
			while (rs.next()) {
				ProjectDashboardInfo project = new ProjectDashboardInfo();
				project.setID(rs.getInt("Project_ID"));
				project.setCode(rs.getString("Code"));
				project.setName(rs.getString("Name"));
				project.setPer_complete(
					(rs.getString("per_complete") != null)
						? rs.getInt("per_complete")
						: 0);
				project.setStart_date(rs.getString("Start_date"));
				project.setBase_finish(rs.getString("Base_Finish_date"));
				project.setPlan_finish(rs.getString("plan_finish_date"));
				project.setActual_finish(rs.getString("actual_finish_date"));
				project.setBase_effort(
					(rs.getString("base_effort") != null)
						? rs.getInt("base_effort")
						: 0);
				project.setPlan_effort(
					(rs.getString("plan_effort") != null)
						? rs.getInt("plan_effort")
						: 0);
				project.setActual_effort(
					(rs.getString("actual_effort") != null)
						? rs.getInt("actual_effort")
						: 0);
				project.setEffort_status(
					(rs.getString("effort_status") != null)
						? rs.getFloat("effort_status")
						: 0);
				project.setSchedule_status(
					(rs.getString("schedule_status") != null)
						? rs.getFloat("schedule_status")
						: 0);

				listResult.add(project);
			} //end while

			System.out.println(
				"ProjectDashboardEJB.getProjectDashboard(): listResult.size() = "
					+ listResult.size());
		} catch (SQLException ex) {
			System.out.println(
				"SQLException occurs in ProjectDashoardEJB.getProjectDashboard(). -- "
					+ ex.getMessage());
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}

		System.out.println("ProjectDashboardEJB.getProjectDashboard() - END");
		return (Collection) listResult;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void ejbRemove() throws RemoteException {
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			throw new RemoteException(
				"projectEJB-ejbRemove error: " + e.toString());
		}
	}
	public void ejbActivate() throws RemoteException {
		try {
			ds = conPool.getDS();
			con = ds.getConnection();
		} catch (Exception e) {
			throw new RemoteException(
				"projectEJB-ejbActivate error: " + e.toString());
		}
	}
	public void ejbPassivate() throws RemoteException {
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			throw new RemoteException(
				"projectEJB-ejbPassivate error: " + e.toString());
		}
	}
	public void setSessionContext(SessionContext sc) {
		this.sc = sc;
	} //Get Group List
	public String[] getGroup() throws SQLException {
		ResultSet rs2 = null;
		Vector vec = new Vector();
		String strSQL = "select groupname FROM groups,workunit" +
            " where groups.group_id=workunit.tableid" +
            " and workunit.type=? and workunit.parentworkunitid=?" +
            " ORDER BY groupname";
		PreparedStatement prepStmt = null;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);
            prepStmt.setInt(1, DB.WORKUNIT_TYPE_GROUP);
            prepStmt.setInt(2, DB.WORKUNIT_FSOFT);
            if (DB.DEBUG) {
                System.out.println(
                    "ProjectDashboardEJB.getGroup(): strSQL = " + strSQL);
            }
			rs2 = prepStmt.executeQuery();
			while (rs2.next()) {
				vec.add(rs2.getString(1));
			}
			rs2.close();
			prepStmt.close();

//			System.out.println(
//				"ProjectDashboardEJB.getGroup(): vec.size() = " + vec.size());
		} catch (SQLException ex) {
			throw new SQLException(
				"SQLException occurs in ProjectDashboard.getGroup(): "
					+ ex.toString());
		} finally {
			if (rs2 != null)
				rs2.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}

		String[] str_ret = new String[vec.size()];
		vec.copyInto(str_ret);

//		System.out.println(
//			"ProjectDashboardEJB.getGroup(): con.isClosed() = "
//				+ con.isClosed());
		return str_ret;
	}
	//////////////////////////////////////////////////////////////////////////////////
	//Thaison added here (12 Nov 01 - 6.00pm)

	public void getProjectInfo(int ProjectID)
		throws SQLException {
		String strSQL =
			"SELECT Name, Code, per_complete, "
				+ " TO_CHAR(plan_finish_date,'dd/mm/yy'),"
				+ " TO_CHAR(actual_finish_date,'dd/mm/yy'),"
				+ " plan_effort, actual_effort, description,"
				+ " TO_CHAR(start_date,'dd/mm/yy'),"
				+ " TO_CHAR(base_finish_date,'dd/mm/yy'), "
				+ "totalRequirement, "
				+ "committedRequirement, "
				+ "designedRequirement, "
				+ "codedRequirement, "
				+ "testedRequirement, "
				+ "deployedRequirement, "
				+ "acceptedRequirement, "
				+ "totalDefect, "
				+ "totalWeightedDefect, "
				+ "fatalPendingDefect, "
				+ "seriousPendingDefect, "
				+ "mediumPendingDefect, "
				+ "cosmeticPendingDefect, "
				+ "totalFatalDefect, "
				+ "totalSeriousDefect, "
				+ "totalMediumDefect, "
				+ "totalCosmeticDefect "
				+ " from project where Project_ID = ?";
		PreparedStatement prepStmt = null;
		ResultSet rs = null;

		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);
			prepStmt.setInt(1, ProjectID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				Name = rs.getString(1);
				Code = rs.getString(2);
				if (rs.getString(3) != null)
					per_complete = rs.getInt(3);
				else
					per_complete = 0;
				dt_plan_finish = rs.getString(4);
				dt_actual_finish = rs.getString(5);
				dt_start = rs.getString(9);
				dt_base_finish = rs.getString(10);
				if (rs.getString(6) != null)
					int_plan_effort = rs.getInt(6);
				else
					int_plan_effort = 0;
				if (rs.getString(7) != null)
					int_actual_effort = rs.getInt(7);
				else
					int_actual_effort = 0;
				txt_description = rs.getString(8);
				totalRequirement = rs.getInt(11);
				committedRequirement = rs.getInt(12);
				designedRequirement = rs.getInt(13);
				codedRequirement = rs.getInt(14);
				testedRequirement = rs.getInt(15);
				deployedRequirement = rs.getInt(16);
				acceptedRequirement = rs.getInt(17);
				totalDefect = rs.getInt(18);
				totalWeightedDefect = rs.getInt(19);
				fatalPendingDefect = rs.getInt(20);
				seriousPendingDefect = rs.getInt(21);
				mediumPendingDefect = rs.getInt(22);
				cosmeticPendingDefect = rs.getInt(23);
				totalFatalDefect = rs.getInt(24);
				totalSeriousDefect = rs.getInt(25);
				totalMediumDefect = rs.getInt(26);
				totalCosmeticDefect = rs.getInt(27);

			} //end if
			else {
				Name = "";
				Code = "";
				per_complete = 0;
				dt_plan_finish = "";
				dt_actual_finish = "";
				int_plan_effort = 0;
				int_actual_effort = 0;
				txt_description = "";
			} //end else
			rs.close();
			prepStmt.close();
		} //end try
		catch (SQLException es) {
			throw es;
		} finally {
			if (rs != null)
				rs.close();
			if (prepStmt != null)
				prepStmt.close();
			if (con != null)
				con.close();
		}
	} //end function

	public void setProjectID(int value) {
		ProjectID = value;
	}
}