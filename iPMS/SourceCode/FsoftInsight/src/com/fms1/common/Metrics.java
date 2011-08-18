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
import java.util.Hashtable;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.tools.Db;
import com.fms1.tools.ConvertString;
import com.fms1.web.ServerHelper;

/**
 * Implements logic for retreiving metrics description 
 * and manage user defined metric data 
 * @author manu
 */

public class Metrics {
	public static final Vector getAllMetricDesc() {
		ResultSet rs = null;
		Statement stmt = null;
		String sql = null;
		Connection conn = null;
		Vector metricList = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT ID,METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY,"
					+ "COLOR_TYPE, MGROUP_ID,METRIC_DESC FROM METRIC_DES ORDER BY DISPLAY_INDEX";
			stmt=conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MetricDescInfo metricDesc = new MetricDescInfo();
				metricDesc.metricID = rs.getString("METRIC_ID").trim();
				metricDesc.metricName = rs.getString("METRIC_NAME");
				metricDesc.unit = rs.getString("UNIT");
				metricDesc.displayIndex = rs.getInt("DISPLAY_INDEX");
				metricDesc.hotPriority = rs.getInt("HOT_PRIORITY");
				metricDesc.colorType = rs.getInt("COLOR_TYPE");
				metricDesc.mgroupID = rs.getLong("MGROUP_ID");
				metricDesc.definition=rs.getString("METRIC_DESC");
				metricDesc.metricConstant = rs.getInt("ID");
				metricList.add(metricDesc);
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
			return metricList;
		}
	}
    public static Hashtable constructMetricHashTb(Vector metricDescList) {
        Hashtable metricHashTb = new Hashtable(); // The default hashtable size is 16
        if (metricDescList != null) {
            // Optimize hashtable for large numbers of elements
            if (metricDescList.size() >= 10) {
                metricHashTb = new Hashtable(metricDescList.size() * 2);
            }
            for (int i = 0; i < metricDescList.size(); i++) {
                MetricDescInfo metricDesc = (MetricDescInfo) metricDescList.get(i);
                metricHashTb.put(new Integer(metricDesc.metricConstant), metricDesc);
            }
        }
        return metricHashTb;
    }
    
    /**
     * Get metric information by using above constructed hash table of metrics
     * @param metricConstant
     * @return
     */
    public static final MetricDescInfo getMetricDesc(int metricConstant) {
        return MetricDescInfo.getMetricDesc(metricConstant);
    }
	
    /**
     * Get metric ID by metric constant using above constructed hash table of metrics
     * @param metricConstant
     * @return
     */
    public static String getMetricID(int metricConstant) {
        return MetricDescInfo.getMetricID(metricConstant);
    }
	public static final Vector getMetricDescs() {
		return MetricDescInfo.metricDescList;
	}
	public static final Vector getChildrenMetrics(int metricConstant) {
		int [] artificialGroup=MetricDescInfo.getArtificialGroup(metricConstant);
		if (artificialGroup!=null)
			java.util.Arrays.sort(artificialGroup);
		Vector childrenMetrics = new Vector();
		Vector metricDescList = MetricDescInfo.metricDescList;
		MetricDescInfo metricDescInfo = null;
		for (int i = 0; i < metricDescList.size(); i++) {
			metricDescInfo = (MetricDescInfo) metricDescList.elementAt(i);
			if (artificialGroup==null){
				if (metricDescInfo.mgroupID ==metricConstant) {
					childrenMetrics.add(metricDescInfo);
				}
			}
			else{
				if (java.util.Arrays.binarySearch(artificialGroup,metricDescInfo.metricConstant)>=0)
					childrenMetrics.add(metricDescInfo);
			}
		}
		return childrenMetrics;
	}
	/**
	 * this function should only be called from Parameter object 
	 * and the result should be cached.
	 * Further access to this info should be made through getMetricGroups
	 */
	public static final Vector getDbMetricGroups() {
		Vector metricGroups = new Vector();
		ResultSet rs = null;
		Statement stmt = null;
		String sql = null;
		Connection conn = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT MGROUP_ID, GROUPNAME, PARENT_ID "
					+ "FROM METRICGROUP";
			stmt=conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MetricGroupInfo metricGroup = new MetricGroupInfo();
				metricGroup.mgroup_id= rs.getInt("MGROUP_ID");
				metricGroup.groupName= rs.getString("GROUPNAME");
				metricGroup.parentID = rs.getInt("PARENT_ID");
				metricGroups.add(metricGroup);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
			return metricGroups;
		}
	}
	public static final MetricGroupInfo getMetricGroup(int groupID) {
		Vector metricGroups = MetricGroupInfo.metricGroupList;
		MetricGroupInfo metricGroup=new MetricGroupInfo();
		for (int i = 0; i < metricGroups.size(); i++) {
			metricGroup=(MetricGroupInfo)metricGroups.elementAt(i);
			if (metricGroup.mgroup_id==groupID) {
				return metricGroup;
			}
		}
		return metricGroup;
	}

	/**
	 * Post-Mortem Report
	 * uppdate Causal analysis
	 * @param:projectID,code;
	 * return boolean
	 */
	public static boolean updateCausal(long prjID, String[] mtricIndex, String[] cause) {
		String updateStatement = "";
		String sql = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean bl = true;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			String prjCode = "";
			updateStatement = " SELECT CODE FROM PROJECT WHERE PROJECT_ID = ?";
			prepStmt = conn.prepareStatement(updateStatement);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				prjCode = rs.getString("CODE").trim();
			}
			rs.close();
			prepStmt.close();
			conn.setAutoCommit(false);
			updateStatement =
				"UPDATE METRICS"
					+ " SET CAUSAL = ?"
					+ " WHERE TRIM(PROJECT_CODE) = ?"
					+ " AND METRIC_INDEX = ?"
					+ " AND CODE = (SELECT MAX(CODE) "
					+ " FROM METRICS"
					+ " WHERE TRIM(PROJECT_CODE) = ?"
					+ " AND METRIC_INDEX = ?)";
			sql =
				"INSERT INTO METRICS("
					+ " CODE, PROJECT_CODE, METRIC_INDEX, CAUSAL, REPORT_DATE)"
					+ " VALUES((SELECT MAX(CODE)+1 FROM METRICS), ?, ?, ?, SYSDATE)";

			for (int i = 0; i < mtricIndex.length; i++){
				int metricConstant = MetricDescInfo.getMetricType(mtricIndex[i].trim());
				prepStmt = conn.prepareStatement(updateStatement);
				prepStmt.setString(1, cause[i]);
				prepStmt.setString(2, prjCode);
				prepStmt.setInt(3, metricConstant );
				prepStmt.setString(4, prjCode);
				prepStmt.setInt(5, metricConstant);
				int count = prepStmt.executeUpdate();
				if (count < 1) {
					prepStmt = conn.prepareStatement(sql);
					prepStmt.setString(1, prjCode);
					prepStmt.setInt(2, metricConstant);
					prepStmt.setString(3, cause[i]);
					count = prepStmt.executeUpdate();
					if (count < 1) {
						bl = false;
						break;
					}
				}
			}
			if (bl){
				conn.commit();
			}
			else {
				conn.rollback();
			}
			conn.setAutoCommit(true);
		}
		catch (SQLException ex){
			ex.printStackTrace();
			conn.rollback();
		}
		catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
			return bl;
		}
	}
	
	public static boolean updateCusMetricNote(long prjID, String[] arrMetricCode, String[] note) {
			String updateStatement = "";
			String sql = "";
			Connection conn = null;
			PreparedStatement prepStmt = null;
			boolean b = true;
			int count;
			ResultSet rs = null;
			try {
				conn = ServerHelper.instance().getConnection();
				conn.setAutoCommit(false);
				
				if (arrMetricCode == null)
					System.out.println("Errors");
				
				for (int i = 0; i < arrMetricCode.length; i++){
					System.out.println(arrMetricCode[i]);
					updateStatement = "UPDATE cus_metrics SET CAUSAL = ? WHERE CODE = " + arrMetricCode[i];
					prepStmt = conn.prepareStatement(updateStatement);
					prepStmt.setString(1, note[i]);
					count = prepStmt.executeUpdate();
					if (count < 1) {
						b = false;
						break;
					}
				}
				if (b){
					conn.commit();
				}
				else {
					conn.rollback();
				}
				conn.setAutoCommit(true);
			}
			catch (SQLException ex){
				ex.printStackTrace();
				conn.rollback();
			}
			catch (Exception e) {
				e.printStackTrace();
				conn.rollback();
			}
			finally {
				ServerHelper.closeConnection(conn, prepStmt, null);
				return b;
			}
		}
	public static MetricInfo getMetric(long prjID, int metricConstant) {
		return getMetric(prjID, null, metricConstant, null);
	}
	public static MetricInfo getMetric(String code,int metricConstant,Date date) {
		return getMetric(0, code,metricConstant,date);
	}
	public static MetricInfo getMetric(long prjID, String code,int metricConstant,Date date) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		//MetricDescInfo mdesc=Metrics.getMetricDesc(metricConstant);
		MetricInfo inf = new MetricInfo();
		try {
			String constraint= (date !=null)?" AND REPORT_DATE <= ? ":"";
				
			String sql;
			if (code==null){
				sql="SELECT METRICS.* "
					+ " FROM METRICS,PROJECT "
					+ " WHERE METRIC_INDEX= ?"
					+ " AND PROJECT.PROJECT_ID = ?"
					+ " AND TRIM(PROJECT.CODE) = TRIM(METRICS.PROJECT_CODE)"
					+ constraint
					+ " ORDER BY REPORT_DATE DESC";
			}
			else{
				sql="SELECT METRICS.* "
					+ " FROM METRICS "
					+ " WHERE METRIC_INDEX= ?"
					+ " AND METRICS.PROJECT_CODE =? "
					+ constraint
					+ " ORDER BY REPORT_DATE DESC";
			}
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, metricConstant);
			
			if (code!=null)
				prepStmt.setString(2, code);
			else
				prepStmt.setLong(2, prjID);
				
			if (date !=null)
				prepStmt.setDate(3,date);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				inf.actualValue=Db.getDouble(rs, "ACTUAL_VALUE");
				inf.causal= rs.getString("CAUSAL");
				if (inf.causal == null)
					inf.causal= "";
				inf.note = rs.getString("NOTE");
				if (inf.note == null)
					inf.note = "";
				inf.plannedValue = Db.getDouble(rs, "PLANNED_VALUE");
				// Add by HaiMM - Start
				inf.usl = Db.getDouble(rs, "USL");
				inf.lsl = Db.getDouble(rs, "LSL");
				// Add by HaiMM - End
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return inf;
		}
	}	
	public static boolean updateMetricValue(String code, int metricConstant, double value, Date date) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				"UPDATE METRICS"
					+ " SET  ACTUAL_VALUE=?"
					+ " WHERE TRIM(PROJECT_CODE) = ?"
					+ " AND METRIC_INDEX = ?"
					+ " AND REPORT_DATE=?";
			prepStmt = conn.prepareStatement(updateStatement);
			Db.setDouble(prepStmt,1, value);
			prepStmt.setString(2, code);
			prepStmt.setInt(3, metricConstant);
			prepStmt.setDate(4, date);
			if (prepStmt.executeUpdate() < 1) {
				updateStatement =
					"INSERT INTO METRICS("
						+ " CODE,ACTUAL_VALUE,PROJECT_CODE,METRIC_INDEX,REPORT_DATE)"
						+ " VALUES((SELECT MAX(CODE)+1 FROM METRICS),?,?,?,?)";
				prepStmt = conn.prepareStatement(updateStatement);
				Db.setDouble(prepStmt,1, value);
				prepStmt.setString(2, code);
				prepStmt.setInt(3, metricConstant);
				prepStmt.setDate(4, date);
				if (prepStmt.executeUpdate() == 0) {
					bl = false;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
			return bl;
		}
	}
	public static final boolean updateStandardMetricList(final long prjID, final Vector vectorInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		PreparedStatement update = null;
		PreparedStatement insert = null;
		PreparedStatement updateActual = null;
		String sql = null;
        ResultSet rs = null;
        String projectCode;
		try {
			// get project code
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT CODE FROM PROJECT WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			rs.next();
            projectCode = rs.getString("CODE");
			ServerHelper.closeConnection(null,stm,rs);
			
			int metricConstant=0;
			// update metric value
			sql =
				"UPDATE METRICS SET"
					+ " USL=?," // Modify by HaiMM: Add USL, LSL
					+ " LSL=?,"
					+ " PLANNED_VALUE=?,"
					+ " REPORT_DATE=SYSDATE,"
					+ " NOTE=? "
					+ " WHERE PROJECT_CODE=?"
					+ " AND METRIC_INDEX=?";
			update = conn.prepareStatement(sql);
			sql =
				" INSERT INTO METRICS ("	// Modify by HaiMM: Add USL, LSL
					+ "CODE, PROJECT_CODE, USL, LSL, PLANNED_VALUE, METRIC_INDEX, REPORT_DATE, NOTE"
					+ ") VALUES ((SELECT NVL(MAX(CODE)+1,1) FROM METRICS), ?, ?, ?, ?, ?, SYSDATE, ?)";
			insert = conn.prepareStatement(sql);
			sql = "UPDATE METRICS SET ACTUAL_VALUE = ? WHERE PROJECT_CODE=? AND METRIC_INDEX=?";
			updateActual = conn.prepareStatement(sql);
			for (int k = 0; k < 7; k++) {
				switch (k) {
					case 0 :
					metricConstant = MetricDescInfo.CUSTOMER_SATISFACTION;
						break;
					case 1 :
					metricConstant = MetricDescInfo.LEAKAGE;
						break;
					case 2 :
					metricConstant = MetricDescInfo.PROCESS_COMPLIANCE;
						break;
					case 3 :
					metricConstant = MetricDescInfo.EFFORT_EFFICIENCY;
						break;
					case 4 :
					metricConstant = MetricDescInfo.CORRECTION_COST;
						break;
					case 5 :
					metricConstant = MetricDescInfo.TIMELINESS;
						break;
					case 6 :
					metricConstant = MetricDescInfo.REQUIREMENT_COMPLETENESS;
						break;
				}
                
				// try update                 
                StandardMetricInfo standardMetricInfo = (StandardMetricInfo)vectorInfo.elementAt(k);
				
				// Add by HaiMM - Start
				if (!Float.isNaN(standardMetricInfo.getUslValue())) {
					update.setFloat(1, standardMetricInfo.getUslValue());
				} else {
					update.setNull(1, Types.DOUBLE);
				}
				if (!Float.isNaN(standardMetricInfo.getLslValue())) {
					update.setFloat(2, standardMetricInfo.getLslValue());
				} else {
					update.setNull(2, Types.DOUBLE);
				}
				// Add by HaiMM - End
                
                if (!Float.isNaN(standardMetricInfo.getTargetedValue())) {
                    update.setFloat(3, standardMetricInfo.getTargetedValue());
                } else {
                    update.setNull(3, Types.DOUBLE);
                }
                update.setString(4, standardMetricInfo.getNote());
                update.setString(5, projectCode);
                update.setInt(6, metricConstant);
                update.executeUpdate();
                if (update.executeUpdate() == 0) {
                    // update failed, try insert
                    insert.setString(1, projectCode);
					
					// Add by HaiMM - Start
					if (!Float.isNaN(standardMetricInfo.getUslValue())) {
						insert.setFloat(2, standardMetricInfo.getUslValue());
					} else {
						insert.setNull(2, Types.DOUBLE);
					}
					if (!Float.isNaN(standardMetricInfo.getLslValue())) {
						insert.setFloat(3, standardMetricInfo.getLslValue());
					} else {
						insert.setNull(3, Types.DOUBLE);
					}
					// Add by HaiMM - End
					
                    if (!Float.isNaN(standardMetricInfo.getTargetedValue())) {
                        insert.setFloat(4, standardMetricInfo.getTargetedValue());
                    } else {
                        insert.setNull(4, Types.DOUBLE);
                    }
                    insert.setInt(5, metricConstant);
                    insert.setString(6, standardMetricInfo.getNote());
                    if (insert.executeUpdate() == 0) {
                        return false;
                    }
                }
                // We also store the actual value for customer satisfaction
                if (k == 0) {
                    if (!Float.isNaN(standardMetricInfo.getActualValue())) {
                        updateActual.setFloat(1, standardMetricInfo.getActualValue());
                    } else { 
                        updateActual.setNull(1, Types.DOUBLE);
                    }
                    updateActual.setString(2, projectCode);
                    updateActual.setInt(3, metricConstant);
                    if (updateActual.executeUpdate() == 0) {
                        return false;
                    }
                }
            }
            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final boolean updateStandardMetricList1(final long prjID, final Vector vectorInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		PreparedStatement update = null;
		PreparedStatement insert = null;
		PreparedStatement updateActual = null;
		String sql = null;
		ResultSet rs = null;
		String projectCode;
		try {
			// get project code
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT CODE FROM PROJECT WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			rs.next();
			projectCode = rs.getString("CODE");
			ServerHelper.closeConnection(null,stm,rs);
		
			int metricConstant=0;
			// update metric value
			sql =
				"UPDATE METRICS SET"
					+ " USL=?," // Modify by HaiMM: Add USL, LSL
					+ " LSL=?,"
					+ " PLANNED_VALUE=?,"
					+ " NOTE=? "
					+ " WHERE PROJECT_CODE=?"
					+ " AND METRIC_INDEX=?";
			update = conn.prepareStatement(sql);
			sql =
				" INSERT INTO METRICS ("	// Modify by HaiMM: Add USL, LSL
					+ "CODE, PROJECT_CODE, USL, LSL, PLANNED_VALUE, METRIC_INDEX, NOTE"
					+ ") VALUES ((SELECT NVL(MAX(CODE)+1,1) FROM METRICS), ?, ?, ?, ?, ?, ?)";
			insert = conn.prepareStatement(sql);
			sql = "UPDATE METRICS SET ACTUAL_VALUE = ? WHERE PROJECT_CODE=? AND METRIC_INDEX=?";
			updateActual = conn.prepareStatement(sql);
			for (int k = 0; k < 7; k++) {
				switch (k) {
					case 0 :
					metricConstant = MetricDescInfo.CUSTOMER_SATISFACTION;
						break;
					case 1 :
					metricConstant = MetricDescInfo.LEAKAGE;
						break;
					case 2 :
					metricConstant = MetricDescInfo.PROCESS_COMPLIANCE;
						break;
					case 3 :
					metricConstant = MetricDescInfo.EFFORT_EFFICIENCY;
						break;
					case 4 :
					metricConstant = MetricDescInfo.CORRECTION_COST;
						break;
					case 5 :
					metricConstant = MetricDescInfo.TIMELINESS;
						break;
					case 6 :
					metricConstant = MetricDescInfo.REQUIREMENT_COMPLETENESS;
						break;
				}
               
				// try update                 
				StandardMetricInfo standardMetricInfo = (StandardMetricInfo)vectorInfo.elementAt(k);
			
				// Add by HaiMM - Start
				if (!Float.isNaN(standardMetricInfo.getUslValue())) {
					update.setFloat(1, standardMetricInfo.getUslValue());
				} else {
					update.setNull(1, Types.DOUBLE);
				}
				if (!Float.isNaN(standardMetricInfo.getLslValue())) {
					update.setFloat(2, standardMetricInfo.getLslValue());
				} else {
					update.setNull(2, Types.DOUBLE);
				}
				// Add by HaiMM - End
               
				if (!Float.isNaN(standardMetricInfo.getTargetedValue())) {
					update.setFloat(3, standardMetricInfo.getTargetedValue());
				} else {
					update.setNull(3, Types.DOUBLE);
				}
				update.setString(4, standardMetricInfo.getNote());
				update.setString(5, projectCode);
				update.setInt(6, metricConstant);
				update.executeUpdate();
				if (update.executeUpdate() == 0) {
					// update failed, try insert
					insert.setString(1, projectCode);
				
					// Add by HaiMM - Start
					if (!Float.isNaN(standardMetricInfo.getUslValue())) {
						insert.setFloat(2, standardMetricInfo.getUslValue());
					} else {
						insert.setNull(2, Types.DOUBLE);
					}
					if (!Float.isNaN(standardMetricInfo.getLslValue())) {
						insert.setFloat(3, standardMetricInfo.getLslValue());
					} else {
						insert.setNull(3, Types.DOUBLE);
					}
					// Add by HaiMM - End
				
					if (!Float.isNaN(standardMetricInfo.getTargetedValue())) {
						insert.setFloat(4, standardMetricInfo.getTargetedValue());
					} else {
						insert.setNull(4, Types.DOUBLE);
					}
					insert.setInt(5, metricConstant);
					insert.setString(6, standardMetricInfo.getNote());
					if (insert.executeUpdate() == 0) {
						return false;
					}
				}
				// We also store the actual value for customer satisfaction
				if (k == 0) {
					if (!Float.isNaN(standardMetricInfo.getActualValue())) {
						updateActual.setFloat(1, standardMetricInfo.getActualValue());
					} else { 
						updateActual.setNull(1, Types.DOUBLE);
					}
					updateActual.setString(2, projectCode);
					updateActual.setInt(3, metricConstant);
					if (updateActual.executeUpdate() == 0) {
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final Vector getCusMetric(long milestoneID) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = null;
		Connection conn = null;
		Vector vectorCusMetric = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT cm.code as code, cm.NAME as name, cp.actual_value as actualValue, cp.point as point, cp.MILESTONE_ID as milestone_ID FROM cus_metrics cm, cus_point cp WHERE cm.code = cp.code AND cp.milestone_id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, milestoneID);
			rs = stmt.executeQuery();
			while (rs.next()) {
				final CusMetricInfo cusMetric = new CusMetricInfo();
				cusMetric.code= rs.getLong("code");
				cusMetric.name= rs.getString("name");
				cusMetric.actualValue = rs.getDouble("actualValue");
				cusMetric.point = rs.getDouble("point");
				cusMetric.milestone_ID = rs.getLong("milestone_ID");
				vectorCusMetric.add(cusMetric);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
			return vectorCusMetric;
		}
	} 
	public static final Vector insertCusPoint(Vector vectorCusMetric) {
			ResultSet rs = null;
			PreparedStatement stmt = null;
			String sql = null;
			Connection conn = null;
			try {
				for(int i=0;i<vectorCusMetric.size();i++){
					CusMetricInfo cusMetricInfo = new CusMetricInfo();
					cusMetricInfo = (CusMetricInfo)vectorCusMetric.elementAt(i);
					conn = ServerHelper.instance().getConnection();
					sql = "update cus_point set point = ? where code = ? and milestone_ID = ?";
					stmt = conn.prepareStatement(sql);
					stmt.setDouble(1, cusMetricInfo.point);
					stmt.setDouble(2, cusMetricInfo.code);
					stmt.setDouble(3, cusMetricInfo.milestone_ID);
					rs = stmt.executeQuery();
					stmt.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, stmt, rs);
				return vectorCusMetric;
			}
	}
	// anhtv08- start
	public static void updateCusMetricActualValue(Vector cusMetricList)
	{
		Connection conn=null;
		PreparedStatement stm=null;
		String sql= null;
		ResultSet rs= null;
		try
		{
				conn= ServerHelper.instance().getConnection();
				for(int i=0;i<cusMetricList.size();i++)
				{
					WOCustomeMetricInfo woCusMetricInfo= (WOCustomeMetricInfo)cusMetricList.elementAt(i);
					sql= "update cus_point set actual_value=? where code=? and milestone_ID = ?";
					stm= conn.prepareStatement(sql);
					stm.setDouble(1,woCusMetricInfo.actualValue);
					stm.setDouble(2,woCusMetricInfo.cusMetricID);
					stm.setDouble(3,woCusMetricInfo.mileStoneID);
					stm.executeUpdate();
					stm.close();
				}
			}catch(Exception ex)
			{
				
			}
			finally
			{
				ServerHelper.closeConnection(conn,stm,rs);
			}
		}
		// end
}