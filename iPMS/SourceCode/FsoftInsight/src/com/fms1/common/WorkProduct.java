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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.fms1.infoclass.*;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.tools.Db;
import com.fms1.tools.ProductLocComparator;
import com.fms1.web.*;
/**
 * Products with related sizing and schedule logic
 *
 */
public class WorkProduct {
    /**
     * get list of work product
     */
    public static Vector getWPList() {
        return getWPList(false);
    }
    /**
     * Get workproduct list. Filter by has_loc is used for product LOC add new
     * @param hasLoc
     * @return
     */
    public static Vector getWPList(boolean hasLoc) {
        String sql = null;
        Connection conn = null;
        PreparedStatement prepStmt = null;
        final Vector list = new Vector();
        ResultSet rs = null;
        try {
            String hasLocFilter = "";
            if (hasLoc) {
                hasLocFilter = " WHERE HAS_LOC=?";
            }
            sql = "SELECT WORKPRODUCT.WP_ID,WORKPRODUCT.NAME" +
                " FROM WORKPRODUCT" +
                hasLocFilter +
                " ORDER BY NAME";
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            if (hasLoc) {
                prepStmt.setInt(1, 1);
            }
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                final WorkProductInfo info = new WorkProductInfo();
                info.workProductID = rs.getInt("WP_ID");
                info.workProductName = rs.getString("NAME");
                list.addElement(info);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return list;
        }
    }
    /**
     * Get work product and size list
     * @param projectID
     * @return
     */
    public static final Vector getWPAndSizeList(long projectID) {
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        final Vector list = new Vector();
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql =
                "SELECT WORKPRODUCT.WP_ID,WORKPRODUCT.NAME,"
                    + " WP_SIZE.PLANNED_DEFECT,WP_SIZE.REPLANNED_DEFECT"
                    + " FROM WORKPRODUCT,WP_SIZE"
                    + " WHERE WP_SIZE.project_id(+)=? "
                    + " AND WORKPRODUCT.WP_ID=WP_SIZE.WP_ID(+)"
                    + " ORDER BY WORKPRODUCT.NAME";
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setLong(1, projectID);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                final WorkProductInfo info = new WorkProductInfo();
                info.workProductID = rs.getInt("WP_ID");
                info.workProductName = rs.getString("NAME");
                info.plannedDefects = Db.getDouble(rs, "PLANNED_DEFECT");
                info.rePlannedDefects = Db.getDouble(rs, "REPLANNED_DEFECT");
                list.addElement(info);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return list;
        }
    }
    /**
     * @return Vector of WPSizeInfo
     */
    public static Vector getModuleListSize(final long prjID) {
        return getModuleSize(prjID, 0);
    }
    
	public static Vector getDefectModuleListSize(final long prjID) {
		return getDefectModuleSize(prjID);
	}
    public static Vector getModuleSize(final long prjID, long moduleID) {
        Vector modules = getModuleList(prjID, moduleID);
        int vsize = modules.size();
        WPSizeInfo info;
        for (int i = 0; i < vsize; i++) {
            info = (WPSizeInfo)modules.elementAt(i);
            info.reestimatedSizeConv = convertToUCP(info.estimatedSizeUnitID, info.reestimatedSize, info.methodBasedSize);
            info.estimatedSizeConv = convertToUCP(info.estimatedSizeUnitID, info.estimatedSize, info.methodBasedSize);
            info.actualSizeConv = convertToUCP((int)info.actualSizeUnitID, info.actualSize, (int)info.acMethodBasedSize);
            if (info.actualSizeConv >= 0) {
                if (info.reusePercentage >= 0)
                    info.createdSize = (double) (1d - info.reusePercentage / 100d) * info.actualSizeConv;
                info.deviation = CommonTools.metricDeviation(info.estimatedSizeConv, info.reestimatedSizeConv, info.actualSizeConv);
            }
			if (info.actualSize >= 0) {
				if (info.reusePercentage >= 0)
					info.createdSizeOrigin = (double) (1d - info.reusePercentage / 100d) * info.actualSize;
			}
        }
        return modules;
    }
    
	public static Vector getDefectModuleSize(final long prjID) {
	   Vector modules = getDefectModuleList(prjID);
	   int vsize = modules.size();
	   WPSizeInfo info;
	   for (int i = 0; i < vsize; i++) {
		   info = (WPSizeInfo)modules.elementAt(i);
		   info.reestimatedSizeConv = convertToUCP(info.estimatedSizeUnitID, info.reestimatedSize, info.methodBasedSize);
		   info.estimatedSizeConv = convertToUCP(info.estimatedSizeUnitID, info.estimatedSize, info.methodBasedSize);
		   info.actualSizeConv = convertToUCP((int)info.actualSizeUnitID, info.actualSize, (int)info.acMethodBasedSize);
		   if (info.actualSizeConv >= 0) {
			   if (info.reusePercentage >= 0)
				   info.createdSize = (double) (1d - info.reusePercentage / 100d) * info.actualSizeConv;
			   info.deviation = CommonTools.metricDeviation(info.estimatedSizeConv, info.reestimatedSizeConv, info.actualSizeConv);
		   }
		   if (info.actualSize >= 0) {
			   if (info.reusePercentage >= 0)
				   info.createdSizeOrigin = (double) (1d - info.reusePercentage / 100d) * info.actualSize;
		   }
		   // Add by HaiMM: Canculate actual defect review/test by module
		info.actualDefReview = getActDefByReviewQC(prjID, info.moduleID, "Review"); 
		info.actualDefTest = getActDefByReviewQC(prjID, info.moduleID, "Test"); 
		   
	   }
	   return modules;
   }
    
    public static Vector getModuleList(final long prjID) {
        return getModuleList(prjID, 0);
    }

	private static double getActDefByReviewQC(final long prjID, final long moduleID, String activity) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		double result = Double.NaN;
		String defectTable = "DEFECT";
		try {
			sql =
					" SELECT SUM(DEFECT_SEVERITY.WEIGHT) WEIGHTED_DEFECT"
						+ " FROM "+ defectTable +" , DEFECT_SEVERITY,PROCESS,ACTIVITY_TYPE, QC_ACTIVITY,WORKPRODUCT"
						+ " WHERE "+ defectTable +".DEFS_ID = DEFECT_SEVERITY.DEFS_ID "
						+ " AND "+ defectTable +".WP_ID=WORKPRODUCT.WP_ID"
						+ " AND WORKPRODUCT.PROCESS=PROCESS.PROCESS_ID"
						+ " AND "+ defectTable +".module_id = ?"
						+ "	AND "+ defectTable +".PROJECT_ID = ?"
						+ " AND "+ defectTable +".DS_ID <>6" //not cancelled
						+ " AND "+ defectTable +".QA_ID <>30" //Quality_Gate_Inspection
						+ " AND "+ defectTable +".QA_ID <>40" //BaseLine_Audit
						+ " AND "+ defectTable +".AT_ID=ACTIVITY_TYPE.AT_ID"
						+ " AND UPPER(ACTIVITY_TYPE.NAME)=?"
						+ " AND QC_ACTIVITY.QA_ID = "+ defectTable +".QA_ID"
						+ " AND NOT(QC_ACTIVITY.QA_ID = 13 OR QC_ACTIVITY.QA_ID = 15 OR QC_ACTIVITY.QA_ID=22)";

			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, moduleID);
			stm.setLong(2, prjID);
			stm.setString(3, activity.trim().toUpperCase());
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getLong("WEIGHTED_DEFECT");
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
     * Get project's product list. Can be used to get only one product
     * @param prjID
     * @param moduleID
     * @return
     */
    public static Vector getModuleList(final long projectId, long moduleId) {
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        final Vector list = new Vector();
        ResultSet rs = null;
        String moduleConst = (moduleId > 0) ? " AND M.MODULE_ID=?" + " " : "";
        try {
            conn = ServerHelper.instance().getConnection();
            sql =
                "SELECT M.MODULE_ID, M.NAME MODULE_NAME, W.NAME WP_NAME,"
                    + " M.PLANNED_SIZE_UNIT_ID, M.PLANNED_SIZE, M.REPLANNED_SIZE,"
                    + " M.ACTUAL_SIZE_UNIT_ID, M.ACTUAL_SIZE, M.REUSE,"
                    + " M.PLANNED_SIZE_TYPE, M.ACTUAL_SIZE_TYPE, M.NOTE MODULE_DESCRIPTION,"
                    + " M.IS_DELIVERABLE,W.WP_ID PARENTID,M.PLANNED_DEFECT,M.REPLANNED_DEFECT,M.PLANNED_RELEASE_DATE,M.REPLANNED_RELEASE_DATE, w.HAS_LOC"
                    + " FROM MODULE M, WORKPRODUCT W "
                    + " WHERE M.PROJECT_ID = ? AND M.WP_ID = W.WP_ID"
                    + moduleConst
                    + " ORDER BY WP_NAME, M.NAME";            
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setLong(1, projectId);
            if (moduleId > 0){
            	prepStmt.setLong(2, moduleId);
            }
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                final WPSizeInfo info = new WPSizeInfo();
                info.moduleID = rs.getLong("MODULE_ID");
                info.workProductID = rs.getLong("PARENTID");
                info.name = rs.getString("MODULE_NAME");
                info.categoryName = rs.getString("WP_NAME");
                info.categoryID = (int)info.workProductID;
                info.estimatedSize = rs.getDouble("PLANNED_SIZE");
                info.methodBasedSize = rs.getInt("PLANNED_SIZE_TYPE");
                info.estimatedSizeUnitID = rs.getInt("PLANNED_SIZE_UNIT_ID");
                info.reestimatedSize = Db.getDouble(rs, "REPLANNED_SIZE");
                info.actualSize = Db.getDouble(rs, "ACTUAL_SIZE");
                info.description = rs.getString("MODULE_DESCRIPTION");
                info.reusePercentage = Db.getDouble(rs, "REUSE");                
                info.acMethodBasedSize = Db.getDouble(rs, "ACTUAL_SIZE_TYPE");
                info.actualSizeUnitID = Db.getDouble(rs, "ACTUAL_SIZE_UNIT_ID");
                info.isDel = rs.getInt("IS_DELIVERABLE");
                info.plannedDefects = Db.getDouble(rs, "PLANNED_DEFECT");
                info.rePlannedDefects = Db.getDouble(rs, "REPLANNED_DEFECT");
                info.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
                info.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
                if (rs.getString("HAS_LOC") != null){
                	info.setHasLoc(rs.getInt("HAS_LOC"));
                }
                else{
                	info.setHasLoc(0);
                }
                if (info.methodBasedSize == 0) {
                    final LanguageInfo lang = Param.getLanguage(info.estimatedSizeUnitID);
	                info.estimatedSizeUnitName = lang.name + " " + lang.sizeUnit;
                    info.isDocument = !"LOC".equals(lang.sizeUnit);
                }
                else {
                    //TODO The following function need to query from Database, should consider to remove it.
                    // Solution: store estimation methodes in Hashtable with keys are estimation_method_id
                    final EstimationMethodInfo method = Param.getEstimationMethod(info.estimatedSizeUnitID);
	                info.estimatedSizeUnitName = method.name;
                    info.isDocument = false;
                }
                if (!Double.isNaN(info.actualSizeUnitID)) {
                    if (info.acMethodBasedSize == 0) {
                        final LanguageInfo lang = Param.getLanguage((int)info.actualSizeUnitID);
                        if (lang.name != null)
                        	info.actualSizeUnitName = lang.name + " " + lang.sizeUnit;
                        info.isDocument = !"LOC".equals(lang.sizeUnit);
                    }
                    else {
                        //TODO The following function need to query from Database, should consider to remove it.
                        // Solution: store estimation methodes in Hashtable with keys are estimation_method_id
                        final EstimationMethodInfo method = Param.getEstimationMethod((int)info.actualSizeUnitID);
	                    info.actualSizeUnitName = method.name;
                    }
                }
                list.addElement(info);
            }
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
        }
    }
    /**
     * convert languageLOC to UCP
     * @author Hoang My Duc
     * @param size, unitID, methodBased
     * @return ucp
     */
    public static final double convertToUCP(final int unitID, final double size, final int methodBased) {
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        ResultSet rs = null;
        double ucp = Double.NaN;
        try {
            if (Double.isNaN(size))
                return Double.NaN;
            conn = ServerHelper.instance().getConnection();
            if (methodBased == 0) {
                sql = "SELECT ?/SLOC UCP FROM CONVERSION WHERE LANGUAGE_ID = ? AND METHOD_ID = 3";
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setDouble(1, size);
                prepStmt.setInt(2, unitID);
                rs = prepStmt.executeQuery();
                if (rs == null)
                    return -1;
                if (rs.next()) {
                    ucp = rs.getDouble("UCP");
                }
            }
            else {
                sql =
                    "SELECT ?*(SELECT SLOC FROM CONVERSION WHERE LANGUAGE_ID = 6 AND METHOD_ID = 3)/SLOC UCP "
                        + "FROM CONVERSION WHERE LANGUAGE_ID = 6 AND METHOD_ID = ?";
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setDouble(1, size);
                prepStmt.setInt(2, unitID);
                rs = prepStmt.executeQuery();
                if (rs == null)
                    return -1;
                if (rs.next()) {
                    ucp = rs.getDouble("UCP");
                }
            }
            return ucp;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
        }
    }
    public static final long addModule(final WPSizeInfo wpsi) {
        String sql = null;
        Connection conn = null;
        PreparedStatement prepStmt = null;
        long retVal = 0;		
        ResultSet rs = null;
        int i = 1;
        try {
        	long moduleId = ServerHelper.getNextSeq("module_seq");
            sql =
                "INSERT INTO MODULE ("
					+ "MODULE_ID, "
                    + "PROJECT_ID, "
                    + "WP_ID, "
                    + "NAME, "
                    + "PLANNED_SIZE_UNIT_ID, "
                    + "PLANNED_SIZE, "
                    + "PLANNED_SIZE_TYPE, "
                    + "REPLANNED_SIZE, "
                    + "ACTUAL_SIZE_UNIT_ID, "
                    + "ACTUAL_SIZE, "
                    + "ACTUAL_SIZE_TYPE, "
                    + "REUSE, "
                    + "NOTE, "
                    + "BASELINE_STATUS, "
					+ "WORKPRODUCT_CODE "
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, ?)";
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            //mandatory fields
			prepStmt.setLong(i++, moduleId);
            prepStmt.setLong(i++, wpsi.projectID);
            prepStmt.setInt(i++, wpsi.categoryID);
            prepStmt.setString(i++, wpsi.name);
            prepStmt.setInt(i++, wpsi.estimatedSizeUnitID);
            prepStmt.setDouble(i++, wpsi.estimatedSize);
            prepStmt.setInt(i++, wpsi.methodBasedSize);
            //optional fields
            Db.setDouble(prepStmt, i++, wpsi.reestimatedSize);
            Db.setDouble(prepStmt, i++, wpsi.actualSizeUnitID);
            Db.setDouble(prepStmt, i++, wpsi.actualSize);
            Db.setDouble(prepStmt, i++, wpsi.acMethodBasedSize);
            Db.setDouble(prepStmt, i++, wpsi.reusePercentage);
            prepStmt.setString(i++, wpsi.description);
			prepStmt.setString(i++, wpsi.workProductCode);
            //return the moduleID
            if (prepStmt.executeUpdate() > 0) {
                sql = "SELECT MAX(MODULE_ID) FROM MODULE WHERE PROJECT_ID=?";
                prepStmt.close();
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setLong(1, wpsi.projectID);
                rs = prepStmt.executeQuery();
                if (rs.next())
                    retVal = rs.getLong(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return retVal;
        }
    }
    public static final boolean setModule(final WPSizeInfo wpsi) {
        String sql = null;
        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            int sizeType, sizeUnit;
            if (Double.isNaN(wpsi.acMethodBasedSize) || wpsi.acMethodBasedSize == -1) {
                //actual size type not defined
                sizeType = (int)wpsi.methodBasedSize;
                sizeUnit = (int)wpsi.estimatedSizeUnitID;
            }
            else {
                sizeType = (int)wpsi.acMethodBasedSize;
                sizeUnit = (int)wpsi.actualSizeUnitID;
            }
            String strDoc = "";
            if ((sizeType == 0) && !isNormal(sizeUnit))
                strDoc = "PLANNED_TEST_END_DATE=null,ACTUAL_TEST_END_DATE=null, ";
            sql =
                "UPDATE MODULE SET "
                    + "PROJECT_ID = ?, "
                    + "WP_ID = ?, "
                    + "NAME = ?, "
                    + "PLANNED_SIZE_UNIT_ID = ?, "
                    + "PLANNED_SIZE = ?, "
                    + "PLANNED_SIZE_TYPE = ?, "
                    + "REPLANNED_SIZE = ?, "
                    + "ACTUAL_SIZE_UNIT_ID = ?, "
                    + "ACTUAL_SIZE = ?, "
                    + "ACTUAL_SIZE_TYPE = ?, "
                    + "REUSE = ?, "
                    + strDoc
                    + "NOTE = ?, "
					+ "WORKPRODUCT_CODE = ? "
                    + "WHERE MODULE_ID = ?";
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            //mandatory fields
            prepStmt.setLong(1, wpsi.projectID);
            prepStmt.setInt(2, wpsi.categoryID);
            prepStmt.setString(3, wpsi.name);
            prepStmt.setInt(4, wpsi.estimatedSizeUnitID);
            prepStmt.setDouble(5, wpsi.estimatedSize);
            prepStmt.setInt(6, wpsi.methodBasedSize);
            //optional fields
            Db.setDouble(prepStmt, 7, wpsi.reestimatedSize);
            Db.setDouble(prepStmt, 8, wpsi.actualSizeUnitID);
            Db.setDouble(prepStmt, 9, wpsi.actualSize);
            Db.setDouble(prepStmt, 10, wpsi.acMethodBasedSize);
            Db.setDouble(prepStmt, 11, wpsi.reusePercentage);
            prepStmt.setString(12, wpsi.description);
			prepStmt.setString(13, wpsi.workProductCode);
			prepStmt.setLong(14, wpsi.moduleID);
            return (prepStmt.executeUpdate() > 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
        }
    }
    public static final boolean delModule(final long moduleID) {
        return Db.delete(moduleID, "MODULE_ID", "MODULE");
    }
    
	public static final boolean delBatchModule(final String listModuleID) {
		return Db.deleteBatch(listModuleID, "MODULE_ID", "MODULE");
	}
		
    public static final Vector getProcessList() {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        Vector vt = null;
        ResultSet rs = null;
        try {
            vt = new Vector();
            final ProcessInfo proc1 = new ProcessInfo();
            proc1.processId = 0;
            proc1.name = Parameters.ORGNAME + " SLC";
            vt.add(proc1);
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            sql = "SELECT * FROM PROCESS ORDER BY NAME";
            rs = stm.executeQuery(sql);
            if (rs != null) {
                while (rs.next()) {
                    final ProcessInfo proc = new ProcessInfo();
                    proc.processId = rs.getInt("PROCESS_ID");
                    proc.name = rs.getString("NAME");
                    vt.add(proc);
                }
            }
            sql = "SELECT * FROM WORKPRODUCT ORDER BY NAME";
            rs = stm.executeQuery(sql);
            if (rs != null) {
                while (rs.next()) {
                    final ProcessInfo proc = new ProcessInfo();
                    proc.processId = rs.getInt("WP_ID");
                    proc.name = rs.getString("NAME");
                    vt.add(proc);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    private static String getLanguageName(final long languageID) {
        String lang = "N/A";
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            sql = "SELECT NAME, SIZE_UNIT FROM LANGUAGE WHERE LANGUAGE_ID = " + languageID;
            rs = stm.executeQuery(sql);
            if (rs.next())
                lang = rs.getString("NAME") + " " + rs.getString("SIZE_UNIT");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return lang;
        }
    }
    //Get language name of language table
    private static boolean isNormal(final long languageID) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        boolean bl = false;
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT SIZE_UNIT FROM LANGUAGE WHERE LANGUAGE_ID = ? AND ( TRIM(SIZE_UNIT)='LOC')";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, languageID);
            rs = stm.executeQuery();
            bl = rs.next();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return bl;
        }
    }
    public static final String ORDER_BY_NAME = " ORDER BY MODULE.NAME";
    public static final String ORDER_BY_DELIVERABLE = " ORDER BY MODULE.IS_DELIVERABLE, MODULE.STATUS";
    public static final String ORDER_BY_PRELEASE = " ORDER BY PL_RELEASE_DATE";
    public static final String ORDER_BY_ARELEASE = " ORDER BY MODULE.ACTUAL_RELEASE_DATE";
    public static final String GETMODULE = " AND MODULE.MODULE_ID=";
    public static final Vector getModuleSchedule(final long prjID, long moduleID) {
        return getModuleListSchedule(prjID, GETMODULE + moduleID, new java.sql.Date(new java.util.Date().getTime()));
    }
    public static final Vector getModuleListSchedule(final long prjID, final String orderByWhat) {
        return getModuleListSchedule(prjID, orderByWhat, new java.sql.Date(new java.util.Date().getTime()));
    }
    public static final Vector getModuleListSimple(final long prjID, final String orderByWhat) {
        return getModuleListSchedule(prjID, orderByWhat, new java.sql.Date(new java.util.Date().getTime()), true);
    }
    public static final Vector getModuleListSchedule(final long prjID, final String orderByWhat, java.sql.Date nowD) {
        return getModuleListSchedule(prjID, orderByWhat, nowD, false);
    }
    /**
     * in order to save CPU we add the simple param
     * 
     */
    public static final Vector getModuleListSchedule(final long prjID, final String orderByWhat, java.sql.Date nowD, boolean simple) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        Vector vt = new Vector();
        ResultSet rs = null;
        try {
            Vector estimMethodList = null;
            if (!simple)
                estimMethodList = Param.getEstimationMethodList();
            conn = ServerHelper.instance().getConnection();
            sql =
                "SELECT MODULE.*, NVL(ACTUAL_SIZE_UNIT_ID,PLANNED_SIZE_UNIT_ID) SUNIT,WORKPRODUCT.NAME WPNAME,"
                    + "NVL(MODULE.REPLANNED_RELEASE_DATE,MODULE.PLANNED_RELEASE_DATE) PL_RELEASE_DATE"
                    + " FROM MODULE,WORKPRODUCT WHERE PROJECT_ID = ?"
                    + " AND WORKPRODUCT.WP_ID=MODULE.WP_ID "
                    + orderByWhat;
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjID);
            rs = stm.executeQuery();
            //get stage list in order to give stage name for modules,  
            Vector stageVt = null;
            if (!simple)
                stageVt = Schedule.getStageList(prjID);
            while (rs.next()) {
                final ModuleInfo mInfo = new ModuleInfo();
                mInfo.baselineStatus = rs.getInt("BASELINE_STATUS");
                mInfo.baseline = rs.getString("BASELINE");
                mInfo.baselineNote = rs.getString("BASELINE_NOTE");
                mInfo.plannedTestEndDate = rs.getDate("PLANNED_TEST_END_DATE");
                mInfo.actualTestEndDate = rs.getDate("ACTUAL_TEST_END_DATE");
                mInfo.plannedReviewDate = rs.getDate("PLANNED_REVIEW_DATE");
                mInfo.actualReviewDate = rs.getDate("ACTUAL_REVIEW_DATE");
                mInfo.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
                mInfo.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
                mInfo.actualSize = Db.getDouble(rs, "ACTUAL_SIZE"); //we need to force user to enter size when filling actual end date
                mInfo.note = rs.getString("NOTE");
                mInfo.thePlanReleaseDate = (mInfo.rePlannedReleaseDate == null) ? mInfo.plannedReleaseDate : mInfo.rePlannedReleaseDate;
                mInfo.actualReleaseDate = rs.getDate("ACTUAL_RElEASE_DATE");
                mInfo.moduleID = rs.getLong("MODULE_ID");
                String type = rs.getString("ACTUAL_SIZE_TYPE");
                if (type == null || "-1".equals(type)) //actual size type not defined
                    mInfo.sizeType = rs.getInt("PLANNED_SIZE_TYPE");
                else
                    mInfo.sizeType = Integer.parseInt(type);
                if (rs.getString("NAME") != null)
                    mInfo.name = rs.getString("NAME");
                if (rs.getString("CONDUCTOR") != null)
                    mInfo.conductor = rs.getString("CONDUCTOR");
                if (rs.getString("REVIEWERS") != null)
                    mInfo.reviewer = rs.getString("REVIEWERS");
                if (rs.getString("APPROVERS") != null)
                    mInfo.approver = rs.getString("APPROVERS");
                mInfo.wpName = rs.getString("WPNAME");
                final long langID = rs.getLong("SUNIT");
                if (!simple) {
                    if (mInfo.sizeType == 1) {
                        for (int i = 0; i < estimMethodList.size(); i++) {
                            EstimationMethodInfo info = (EstimationMethodInfo)estimMethodList.elementAt(i);
                            if (info.methodID == langID) {
                                mInfo.language = info.name;
                                break;
                            }
                        }
                    }
                    else
                        mInfo.language = getLanguageName(langID);
                }
                //isNormal true means that the module can be tested
                //isNormal false means that the module can't be tested (documents)
                // size type : 1 for estimation methods (can be tested)
                //				0 for programming language
                mInfo.isNormal = (mInfo.sizeType == 0) ? isNormal(langID) : true;
                mInfo.isDel = (rs.getInt("IS_DELIVERABLE") == 1);
                //only deliverables have status
                if (mInfo.isDel)
                    mInfo.status = rs.getInt("STATUS");
                //get Stage Name
                if (!simple) {
                    mInfo.stage = "Undefined";
                    if (mInfo.plannedReleaseDate != null)
                        for (int i = 0; i < stageVt.size(); i++) { //the stages are already sorted by planned en date
                            final StageInfo stageInfo = (StageInfo)stageVt.get(i);
                            if (stageInfo.plannedEndDate.compareTo(mInfo.thePlanReleaseDate) >= 0) {
                                mInfo.stage = stageInfo.stage;
                                //we only calc deviation for events that occurs in the past
                                if (mInfo.actualReleaseDate != null && mInfo.actualReleaseDate.getTime() <= nowD.getTime()) {
                                    final long s1 = mInfo.actualReleaseDate.getTime() - mInfo.thePlanReleaseDate.getTime();
                                    final long s2 = mInfo.thePlanReleaseDate.getTime() - stageInfo.plannedBeginDate.getTime();
                                    if (s2 != 0) {
                                        mInfo.deviation = (s1 == 0) ? 0 : s1 * 100d / s2;
                                    }
                                }
                                break;
                            }
                        }
                    if (mInfo.plannedReleaseDate != null) {
                        if (mInfo.actualReleaseDate != null) {
                            if (mInfo.actualReleaseDate.compareTo(mInfo.plannedReleaseDate) > 0)
                                mInfo.isRelease = "No";
                            else
                                mInfo.isRelease = "Yes";
                        }
                        else {
                            if (mInfo.plannedReleaseDate.compareTo(nowD) > 0)
                                mInfo.isRelease = "Not yet";
                            else
                                mInfo.isRelease = "No";
                        }
                    }
                    if (mInfo.plannedReviewDate != null) {
                        if (mInfo.actualReviewDate != null) {
                            if (mInfo.actualReviewDate.compareTo(mInfo.plannedReviewDate) > 0)
                                mInfo.isReview = "No";
                            else
                                mInfo.isReview = "Yes";
                        }
                        else {
                            if (mInfo.plannedReviewDate.compareTo(nowD) > 0)
                                mInfo.isReview = "Not yet";
                            else
                                mInfo.isReview = "No";
                        }
                    }
                    if (mInfo.plannedTestEndDate != null) {
                        if (mInfo.actualTestEndDate != null) {
                            if (mInfo.actualTestEndDate.compareTo(mInfo.plannedTestEndDate) > 0)
                                mInfo.isTest = "No";
                            else
                                mInfo.isTest = "Yes";
                        }
                        else {
                            if (mInfo.plannedTestEndDate.compareTo(nowD) > 0)
                                mInfo.isTest = "Not yet";
                            else
                                mInfo.isTest = "No";
                        }
                    }
                }
                vt.add(mInfo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    public static final Vector getModuleListSQA(long[] prjIDs, Date fromDate, Date toDate) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        Vector vt = new Vector();
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            String projectFilter = (prjIDs == null) ? "" : " MODULE.PROJECT_ID in (" + ConvertString.arrayToString(prjIDs, ",") + ") AND";
            sql =
                "SELECT R.ACTUAL_EFFORT,MODULE.*, NVL(ACTUAL_SIZE_UNIT_ID,PLANNED_SIZE_UNIT_ID) SUNIT,WORKPRODUCT.NAME WPNAME,"
                    + "NVL(MODULE.REPLANNED_RELEASE_DATE,MODULE.PLANNED_RELEASE_DATE) PL_RELEASE_DATE"
                    + " FROM MODULE,WORKPRODUCT,REVIEW_EFFORT R"
                    + " WHERE "
                    + projectFilter
                    + " WORKPRODUCT.WP_ID=MODULE.WP_ID "
                    + " AND MODULE.PLANNED_REVIEW_DATE >=? AND MODULE.PLANNED_REVIEW_DATE <=? "
                    + " AND (MODULE.STATUS <>"
                    + ModuleInfo.STATUS_CANCELLED
                    + " OR MODULE.STATUS IS NULL)"
                    + " AND  R.MODULE_ID (+) = MODULE.MODULE_ID";
            stm = conn.prepareStatement(sql);
            stm.setDate(1, fromDate);
            stm.setDate(2, toDate);
            rs = stm.executeQuery();
            while (rs.next()) {
                final ModuleInfo mInfo = new ModuleInfo();
                mInfo.projectID = rs.getInt("PROJECT_ID");
                mInfo.baselineStatus = rs.getInt("BASELINE_STATUS");
                mInfo.baseline = rs.getString("BASELINE");
                mInfo.baselineNote = rs.getString("BASELINE_NOTE");
                mInfo.plannedTestEndDate = rs.getDate("PLANNED_TEST_END_DATE");
                mInfo.actualTestEndDate = rs.getDate("ACTUAL_TEST_END_DATE");
                mInfo.plannedReviewDate = rs.getDate("PLANNED_REVIEW_DATE");
                mInfo.actualReviewDate = rs.getDate("ACTUAL_REVIEW_DATE");
                mInfo.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
                mInfo.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
                mInfo.actualSize = Db.getDouble(rs, "ACTUAL_SIZE"); //we need to force user to enter size when filling actual end date
                mInfo.note = rs.getString("NOTE");
                mInfo.thePlanReleaseDate = (mInfo.rePlannedReleaseDate == null) ? mInfo.plannedReleaseDate : mInfo.rePlannedReleaseDate;
                mInfo.actualReleaseDate = rs.getDate("ACTUAL_RElEASE_DATE");
                mInfo.moduleID = rs.getLong("MODULE_ID");
                mInfo.actualEffort = Db.getDouble(rs, "ACTUAL_EFFORT");
                String type = rs.getString("ACTUAL_SIZE_TYPE");
                if (type == null || "-1".equals(type)) //actual size type not defined
                    mInfo.sizeType = rs.getInt("PLANNED_SIZE_TYPE");
                else
                    mInfo.sizeType = Integer.parseInt(type);
                if (rs.getString("NAME") != null)
                    mInfo.name = rs.getString("NAME");
                if (rs.getString("CONDUCTOR") != null)
                    mInfo.conductor = rs.getString("CONDUCTOR");
                if (rs.getString("REVIEWERS") != null)
                    mInfo.reviewer = rs.getString("REVIEWERS");
                if (rs.getString("APPROVERS") != null)
                    mInfo.approver = rs.getString("APPROVERS");
                mInfo.wpName = rs.getString("WPNAME");
                final long langID = rs.getLong("SUNIT");
                //isNormal true means that the module can be tested
                //isNormal false means that the module can't be tested (documents)
                // size type : 1 for estimation methods (can be tested)
                //				0 for programming language
                mInfo.isNormal = (mInfo.sizeType == 0) ? isNormal(langID) : true;
                mInfo.isDel = (rs.getInt("IS_DELIVERABLE") == 1);
                vt.add(mInfo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    public static final Vector getDeliverableList(final long prjID) {
        final Vector resultVector = new Vector();
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        final float projectDuration;
        try {
            conn = ServerHelper.instance().getConnection();
            sql =
                "SELECT (NVL(PLAN_FINISH_DATE,BASE_FINISH_DATE) - PLAN_START_DATE) PROJECT_DURATION FROM " + "PROJECT WHERE PROJECT_ID = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjID);
            rs = stm.executeQuery();
            rs.next();
            projectDuration = rs.getLong("PROJECT_DURATION");
            rs.close();
            stm.close();
            sql =
                "SELECT MODULE.*,(ACTUAL_RELEASE_DATE - NVL(REPLANNED_RELEASE_DATE,PLANNED_RELEASE_DATE)) MODULE_DURATION FROM MODULE "
                    + "WHERE PROJECT_ID = ? AND IS_DELIVERABLE  = 1 ORDER BY MODULE.PLANNED_RELEASE_DATE";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjID);
            rs = stm.executeQuery();
            while (rs.next()) {
                final ModuleInfo moduleInfo = new ModuleInfo();
                moduleInfo.moduleID = rs.getLong("MODULE_ID");
                moduleInfo.projectID = rs.getInt("PROJECT_ID");
                moduleInfo.name = rs.getString("NAME");
                moduleInfo.wpID = rs.getInt("WP_ID");
                moduleInfo.plannedDefect = rs.getInt("PLANNED_DEFECT");
                moduleInfo.rePlannedDefect = rs.getInt("REPLANNED_DEFECT");
                moduleInfo.plannedReviewDate = rs.getDate("PLANNED_REVIEW_DATE");
                moduleInfo.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
                moduleInfo.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
                moduleInfo.thePlanReleaseDate =
                    (moduleInfo.rePlannedReleaseDate == null) ? moduleInfo.plannedReleaseDate : moduleInfo.rePlannedReleaseDate;
                moduleInfo.plannedTestEndDate = rs.getDate("PLANNED_TEST_END_DATE");
                moduleInfo.actualReleaseDate = rs.getDate("ACTUAL_RELEASE_DATE");
                moduleInfo.actualTestEndDate = rs.getDate("ACTUAL_TEST_END_DATE");
                moduleInfo.isDeliverable = rs.getInt("IS_DELIVERABLE");
                moduleInfo.deliveryLocation = rs.getString("DELIVERY_LOCATION");
                moduleInfo.note = rs.getString("NOTE");
                moduleInfo.status = rs.getInt("STATUS");
                final float moduleDuration;
                moduleDuration = rs.getLong("MODULE_DURATION");
                if ((projectDuration != 0) && (moduleInfo.thePlanReleaseDate != null) && (moduleInfo.actualReleaseDate != null)) {
                    moduleInfo.deviation = moduleDuration / projectDuration * 100d;
                }
                resultVector.addElement(moduleInfo);
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
    public static final Vector getRemainingDeliverableList(final long prjID) {
        final Vector resultVector = new Vector();
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT * FROM MODULE  WHERE PROJECT_ID = ? AND NVL(IS_DELIVERABLE, 0)  != 1 ORDER BY NAME";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, prjID);
            rs = stm.executeQuery();
            while (rs.next()) {
                final ModuleInfo moduleInfo = new ModuleInfo();
                moduleInfo.moduleID = rs.getLong("MODULE_ID");
                moduleInfo.name = rs.getString("NAME");
                moduleInfo.plannedReviewDate = rs.getDate("PLANNED_REVIEW_DATE");
                moduleInfo.plannedTestEndDate = rs.getDate("PLANNED_TEST_END_DATE");
                moduleInfo.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
                moduleInfo.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
                moduleInfo.actualReleaseDate = rs.getDate("ACTUAL_RELEASE_DATE");
                moduleInfo.baselineNote = rs.getString("NOTE");
                resultVector.addElement(moduleInfo);
            }
            return (resultVector.isEmpty()) ? null : resultVector;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }
    public static final boolean updateDeliverable(final ModuleInfo moduleInfo) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        try {
            sql =
                "UPDATE MODULE SET IS_DELIVERABLE = ?, "
                    + "DELIVERY_LOCATION = ?, "
                    + "NOTE = ? , "
                    + "PLANNED_RELEASE_DATE = ?, "
                    + "REPLANNED_RELEASE_DATE = ?, "
                    + "ACTUAL_RELEASE_DATE = ?, "
                    + "STATUS = ? "
                    + "WHERE MODULE_ID = ?";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setInt(1, moduleInfo.isDeliverable);
            stm.setString(2, moduleInfo.deliveryLocation);
            stm.setString(3, moduleInfo.note);
            stm.setDate(4, moduleInfo.plannedReleaseDate);
            stm.setDate(5, moduleInfo.rePlannedReleaseDate);
            stm.setDate(6, moduleInfo.actualReleaseDate);
            stm.setInt(7, moduleInfo.status);
            stm.setLong(8, moduleInfo.moduleID);
            if (stm.executeUpdate() == 0)
                return false;
            // Update Requirement and CI Register here
            if ((moduleInfo.actualReleaseDate != null) && (moduleInfo.status == 2)) {
                // Accepted
                sql = "UPDATE MODULE SET BASELINE_STATUS = 4 WHERE MODULE_ID = ?";
                stm = conn.prepareStatement(sql);
                stm.setLong(1, moduleInfo.moduleID);
                stm.executeUpdate();
                stm.close();
                sql = "UPDATE REQUIREMENTS SET ACCEPTED_DATE = ? WHERE MODULE_ID = ?";
                stm = conn.prepareStatement(sql);
                stm.setDate(1, moduleInfo.actualReleaseDate);
                stm.setLong(2, moduleInfo.moduleID);
                stm.executeUpdate();
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
        }
    }
    /**
     * uppdate MODULE
     */
    public static final boolean updateModule(final ModuleUpdateInfo info) {
        String sql = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = true;
        try {
            sql =
                " UPDATE MODULE "
                    + " SET PLANNED_TEST_END_DATE = ?,"
                    + " ACTUAL_TEST_END_DATE = ? ,"
                    + " PLANNED_RELEASE_DATE = ?,"
                    + " REPLANNED_RELEASE_DATE = ?,"
                    + " ACTUAL_RELEASE_DATE = ?,"
                    + " PLANNED_REVIEW_DATE = ?,"
                    + " ACTUAL_REVIEW_DATE = ?,"
                    + " REVIEWERS = ?,"
                    + " APPROVERS = ?,"
                    + ((info.status >= 1) ? " STATUS = " + info.status + "," : "")
                    + " CONDUCTOR = ?"
                    + " WHERE MODULE_ID = ?";
            //status is only available for deliverables
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setDate(1, info.pTestD);
            prepStmt.setDate(2, info.aTestD);
            prepStmt.setDate(3, info.pReleaseD);
            prepStmt.setDate(4, info.rpReleaseD);
            prepStmt.setDate(5, info.aReleaseD);
            prepStmt.setDate(6, info.pReviewD);
            prepStmt.setDate(7, info.aReviewD);
            prepStmt.setString(8, info.reviewer);
            prepStmt.setString(9, info.approver);
            prepStmt.setString(10, info.conductor);
            prepStmt.setLong(11, info.moduleID);
            final int rowCount = prepStmt.executeUpdate();
            if (rowCount == 0) {
                System.err.println("Update Module " + info.moduleID + " failed.");
                bl = false;
            }
            prepStmt.close();
            // Update Requirement and CI Register here
            if (info.aReviewD != null) {
                sql = "UPDATE MODULE SET BASELINE_STATUS = 2 WHERE MODULE_ID = ?";
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setLong(1, info.moduleID);
                prepStmt.executeUpdate();
                prepStmt.close();
            }
            if (info.aTestD != null) {
                sql = "UPDATE MODULE SET BASELINE_STATUS = 3 WHERE MODULE_ID = ?";
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setLong(1, info.moduleID);
                prepStmt.executeUpdate();
                prepStmt.close();
                sql = "UPDATE REQUIREMENTS SET TESTED_DATE = ? WHERE MODULE_ID = ?";
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setDate(1, info.aReleaseD);
                prepStmt.setLong(2, info.moduleID);
                prepStmt.executeUpdate();
                prepStmt.close();
            }
            if (info.aReleaseD != null) {
                sql = "UPDATE MODULE SET BASELINE_STATUS = 4 WHERE MODULE_ID = ?";
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setLong(1, info.moduleID);
                prepStmt.executeUpdate();
                prepStmt.close();
                sql = "UPDATE REQUIREMENTS SET DEPLOYED_DATE = ? WHERE MODULE_ID = ?";
                prepStmt = conn.prepareStatement(sql);
                prepStmt.setDate(1, info.aReleaseD);
                prepStmt.setLong(2, info.moduleID);
                prepStmt.executeUpdate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    /**
     * actual start date and actual finish date of project uppdate
     */
    public static final boolean updatePrjDate(final ScheduleHeaderInfo info) {
        String updateStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = true;
        try {
            conn = ServerHelper.instance().getConnection();
            updateStatement = " UPDATE PROJECT SET START_DATE = ? , ACTUAL_FINISH_DATE = ? WHERE PROJECT_ID = ?";
            prepStmt = conn.prepareStatement(updateStatement);
            prepStmt.setDate(1, info.aStartD);
            prepStmt.setDate(2, info.aEndD);
            prepStmt.setLong(3, info.prjID);
            final int rowCount = prepStmt.executeUpdate();
            bl = (rowCount == 0);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    public static final boolean updateStage(final StageInfo info) {
        String updateStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = true;
        try {
            conn = ServerHelper.instance().getConnection();
            updateStatement =
                " UPDATE MILESTONE "
                    + " SET BASE_FINISH_DATE = ?,"
                    + " PLAN_FINISH_DATE = ? ,"
                    + " ACTUAL_FINISH_DATE = ?,"
                    + " NAME = ?,"
                    + " DESCRIPTION = ?,"
                    + " MILESTONE = ?,"
                    + " COMPLETE = ?,"
                    + " STANDARDSTAGE = ?"
                    + " WHERE MILESTONE_ID = ?";
            prepStmt = conn.prepareStatement(updateStatement);
            prepStmt.setDate(1, info.bEndD);
            prepStmt.setDate(2, info.pEndD);
            prepStmt.setDate(3, info.aEndD);
            prepStmt.setString(4, info.stage);
            prepStmt.setString(5, info.description);
            prepStmt.setString(6, info.milestone);
            prepStmt.setInt(7, ((info.aEndD == null) ? 0 : 1));
            prepStmt.setInt(8, info.StandardStage);
            prepStmt.setLong(9, info.milestoneID);
            final int rowCount = prepStmt.executeUpdate();
            bl = (rowCount == 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    /**
     *Add new stageInfo to MILESTONE
     *@param:StageInfo
     *@return boolean
     */
    public static final boolean addStage(final StageInfo info) {
        String insertStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = false;
        try {
        	long milestoneId = ServerHelper.getNextSeq("milestone_seq");
            conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
            insertStatement =
                " INSERT INTO MILESTONE("
                    + " MILESTONE_ID,"
                    + " PROJECT_ID,"
                    + " BASE_FINISH_DATE,"
                    + " PLAN_FINISH_DATE,"
                    + " ACTUAL_FINISH_DATE,"
                    + " NAME,"
                    + " DESCRIPTION,"
                    + " COMPLETE,"
                    + " MILESTONE,STANDARDSTAGE)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)";
            prepStmt = conn.prepareStatement(insertStatement);
            prepStmt.setLong(1, milestoneId);
            prepStmt.setLong(2, info.prjID);
            prepStmt.setDate(3, info.bEndD);
            prepStmt.setDate(4, info.pEndD);
            prepStmt.setDate(5, info.aEndD);
            prepStmt.setString(6, info.stage);
            prepStmt.setString(7, info.description);
            prepStmt.setInt(8, ((info.aEndD == null) ? 0 : 1));
            prepStmt.setString(9, info.milestone);
            prepStmt.setInt(10, info.StandardStage);
            final int rowCount = prepStmt.executeUpdate();

			if (rowCount != 0) {
				Vector cusMetricList = new Vector();
				cusMetricList = Project.getWOCustomeMetricList(info.prjID);
				for(int i=0;i<cusMetricList.size();i++){
					WOCustomeMetricInfo cmInfo = new WOCustomeMetricInfo();  
					cmInfo = (WOCustomeMetricInfo)cusMetricList.elementAt(i);
					prepStmt.close();
					insertStatement =
						"INSERT INTO CUS_POINT (CUS_POINT_ID, MILESTONE_ID, CODE, POINT) "
							+ " VALUES (NVL((SELECT MAX(CUS_POINT_ID)+1 FROM CUS_POINT),1), ?, ?, null)";
					       
					prepStmt = conn.prepareStatement(insertStatement);
					Db.setDouble(prepStmt, 1, milestoneId);
					Db.setDouble(prepStmt, 2, cmInfo.cusMetricID);
					prepStmt.executeUpdate();
				}
				prepStmt.close();
				
				Vector dpList = new Vector();
				dpList= Defect.getDPTask(info.prjID);
				for(int i=0;i<cusMetricList.size();i++){
					WOCustomeMetricInfo cmInfo = new WOCustomeMetricInfo();  
					cmInfo = (WOCustomeMetricInfo)cusMetricList.elementAt(i);
					prepStmt.close();
					insertStatement =
						"INSERT INTO DP_MILESTONE (DP_MILESTONE_ID, MILESTONE_ID, DPTASKID, ACTUAL) "
							+ " VALUES (NVL((SELECT MAX(DP_MILESTONE_ID)+1 FROM DP_MILESTONE),1), ?, ?, null)";
		   
					prepStmt = conn.prepareStatement(insertStatement);
					Db.setDouble(prepStmt, 1, milestoneId);
					Db.setDouble(prepStmt, 2, cmInfo.cusMetricID);
					prepStmt.executeUpdate();
				}
				prepStmt.close();
			}
			bl=true;
			conn.commit();
			conn.setAutoCommit(true);
        }
        catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
            e.printStackTrace();
			bl=false;
        }
        catch (Exception ex){
        	ex.printStackTrace();
        	return false;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    /**
     * delete one row from MILESTONE
     * @param:MILESTONE_ID
     * @return boolean
     */
    public static final boolean deleteStage(final long id) {
        if(Db.delete(id, "MILESTONE_ID", "CUS_POINT") && Db.delete(id, "MILESTONE_ID", "MILESTONE")){
        	return true;
        }else{
        	return false;
        }
    }
    /******************************Further WORK*********************************************************/
    /**
     *@return FurtherWork vector
     *@param project ID
     */
    public static final Vector getFurtherWorkList(final long prjID) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        Vector vt = null;
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            sql = "SELECT *  FROM FURTHER_WORK WHERE PROJECT_ID = " + prjID + " ORDER BY NAME";
            rs = stm.executeQuery(sql);
            if (rs != null) {
                vt = new Vector();
                while (rs.next()) {
                    final FurtherWorkInfo info = new FurtherWorkInfo();
                    info.fwID = rs.getLong("FURTHER_WORK_ID");
                    info.prjID = rs.getLong("PROJECT_ID");
                    info.name = rs.getString("NAME");
                    info.result = rs.getString("RESULT");
                    info.time = rs.getDouble("TIME");
                    info.note = rs.getString("NOTE");
                    if (info.note == null) {
                        info.note = "N/A";
                    }
                    info.responsibility = rs.getString("RESPONSIBILITY");
                    vt.add(info);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    public static final boolean updateFurtherWork(final FurtherWorkInfo info) {
        String updateStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = true;
        try {
            conn = ServerHelper.instance().getConnection();
            updateStatement =
                " UPDATE FURTHER_WORK "
                    + " SET NAME = ?,"
                    + " RESULT = ? ,"
                    + " TIME = ?,"
                    + " NOTE = ?,"
                    + " RESPONSIBILITY = ?"
                    + " WHERE FURTHER_WORK_ID = ?";
            prepStmt = conn.prepareStatement(updateStatement);
            prepStmt.setString(1, info.name);
            prepStmt.setString(2, info.result);
            prepStmt.setDouble(3, info.time);
            prepStmt.setString(4, info.note);
            prepStmt.setString(5, info.responsibility);
            prepStmt.setLong(6, info.fwID);
            final int rowCount = prepStmt.executeUpdate();
            if (rowCount == 0) {
                System.out.println("Update Further work " + info.fwID + " failed.");
                bl = false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    /**
     *Add new FurtherWorkInfo to FURTHER_WORK
     */
    public static final boolean addFurtherWork(final FurtherWorkInfo info) {
        String insertStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = true;
        try {
            conn = ServerHelper.instance().getConnection();
            long lFurtherWorkID = ServerHelper.getNextSeq("FURTHER_WORK_SEQ");
            insertStatement =
                " INSERT INTO FURTHER_WORK("
                    + " FURTHER_WORK_ID,"
                    + " PROJECT_ID,"
                    + " NAME,"
                    + " RESULT,"
                    + " TIME,"
                    + " NOTE,"
                    + " RESPONSIBILITY)"
                    + " VALUES (?,?,?,?,?,?,?)";
            prepStmt = conn.prepareStatement(insertStatement);
			prepStmt.setLong(1, lFurtherWorkID);
            prepStmt.setLong(2, info.prjID);
            prepStmt.setString(3, info.name);
            prepStmt.setString(4, info.result);
            prepStmt.setDouble(5, info.time);
            prepStmt.setString(6, info.note);
            prepStmt.setString(7, info.responsibility);
            final int rowCount = prepStmt.executeUpdate();
            if (rowCount == 0) {
                bl = false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    /**
     * delete one row from FURTHER_WORK
     */
    public static final boolean deleteFurther(final long id) {
            return Db.delete(id, "FURTHER_WORK_ID", "FURTHER_WORK");
    }
    /**get work product size summarization by stage
     * Param vector of WPsizeInfo from WorkProduct.getModuleListSize()
     */
    public static WPSizeInfo getWPSizeSumByStage(final long projectID, final StageInfo infoStage, Vector modulelist) {
        final WPSizeInfo info = new WPSizeInfo();
        long start = infoStage.plannedBeginDate.getTime();
        long end = infoStage.plannedEndDate.getTime();
        info.name = infoStage.stage;
        info.estimatedSize = 0;
        WPSizeInfo sizeInfo;
        long release;
        for (int i = 0; i < modulelist.size(); i++) {
            sizeInfo = (WPSizeInfo)modulelist.elementAt(i);
            if (sizeInfo.rePlannedReleaseDate != null)
                release = sizeInfo.rePlannedReleaseDate.getTime();
            else if (sizeInfo.plannedReleaseDate != null)
                release = sizeInfo.plannedReleaseDate.getTime();
            else
                continue;
            if (release <= end && release >= start) {

                if (sizeInfo.estimatedSizeConv >= 0) {
                    if (Double.isNaN(info.estimatedSize))
                        info.estimatedSize = 0;
                    info.estimatedSize += sizeInfo.estimatedSizeConv;
                }
                if (sizeInfo.reestimatedSizeConv >= 0) {
                    if (Double.isNaN(info.reestimatedSize))
                        info.reestimatedSize = 0;
                    info.reestimatedSize += sizeInfo.reestimatedSizeConv;
                }
                if (sizeInfo.actualSizeConv >= 0) {
                    if (Double.isNaN(info.actualSize))
                        info.actualSize = 0;
                    info.actualSize += sizeInfo.actualSizeConv;
                    if (sizeInfo.reusePercentage >= 0) {
                        if (Double.isNaN(info.reusePercentage))
                            info.reusePercentage = 0;
                        info.reusePercentage += sizeInfo.actualSizeConv * sizeInfo.reusePercentage;
                    }
                }
            }
        }
        if (info.actualSize >= 0) {
            if (info.reusePercentage >= 0) {
                info.reusePercentage = (info.reusePercentage / info.actualSize);
                info.createdSize = (1d - info.reusePercentage / 100d) * info.actualSize;
            }
            info.deviation = CommonTools.metricDeviation(info.estimatedSize, info.reestimatedSize, info.actualSize);
        }
        return info;
    }
    /** get WPSizeSumList
     * @return Vector of WPSizeInfo
     */
    public static final Vector getWPSizeSumList(final long projectID, Vector modulelist) {
        final Vector WPSizeSumList = new Vector();
        try {
            Vector stageList = Schedule.getStageList(projectID);
            for (int i = 0; i < stageList.size(); i++) {
                StageInfo stageInfo = (StageInfo)stageList.elementAt(i);
                final WPSizeInfo info = WorkProduct.getWPSizeSumByStage(projectID, stageInfo, modulelist);
                WPSizeSumList.addElement(info);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return WPSizeSumList;
        }
    }
    public static final Vector getModuleByLanguage(final long langID) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        Vector vt = new Vector();
        ResultSet rs = null;
        String[] info;
        try {
            conn = ServerHelper.instance().getConnection();

            sql =
                "SELECT MODULE.NAME,Project.CODE  FROM MODULE, PROJECT"
                    + " WHERE ((PLANNED_SIZE_TYPE=0 AND PLANNED_SIZE_UNIT_ID=?)OR(ACTUAL_SIZE_TYPE=0 AND ACTUAL_SIZE_UNIT_ID=?))"
                    + " AND MODULE.PROJECT_ID =PROJECT.PROJECT_ID ORDER BY PROJECT.CODE,MODULE.NAME";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, langID);
            stm.setLong(2, langID);
            rs = stm.executeQuery();

            while (rs.next()) {
                info = new String[2];
                info[0] = rs.getString("NAME");
                info[1] = rs.getString("CODE");
                vt.add(info);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    
   /**
     * Get project's products that have LOC
     * @param projectId
     * @return
     */
    public static Vector getProductsHaveLoc(long projectId) {
        final Vector list = new Vector();
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        ResultSet rs = null;
        try {
            sql =
                "SELECT M.MODULE_ID, M.NAME MODULE_NAME, W.NAME WP_NAME,"
                + " M.PLANNED_SIZE_UNIT_ID, M.PLANNED_SIZE, M.REPLANNED_SIZE,"
                + " M.ACTUAL_SIZE_UNIT_ID, M.ACTUAL_SIZE, M.REUSE,"
                + " M.PLANNED_SIZE_TYPE, M.ACTUAL_SIZE_TYPE,"
                + " M.NOTE MODULE_DESCRIPTION, M.PLANNED_RELEASE_DATE,"
                + " M.REPLANNED_RELEASE_DATE,W.WP_ID PARENTID "
                + " FROM MODULE M, WORKPRODUCT W"
                + " WHERE M.WP_ID = W.WP_ID"
                + " AND EXISTS ("
                +       "SELECT PRODUCT_LOC_ID FROM PRODUCT_LOC_ACTUAL"
                +      " WHERE PROJECT_ID = ?"
                +      " UNION "
                +       "SELECT PRODUCT_LOC_ID FROM PRODUCT_LOC_PLAN"
                +      " WHERE PROJECT_ID = ?)"
                + " AND M.PROJECT_ID = ?"
                + " ORDER BY WP_NAME, M.NAME";

            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setLong(1, projectId);
            prepStmt.setLong(2, projectId);
            prepStmt.setLong(3, projectId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                WPSizeInfo info = new WPSizeInfo();
                info.moduleID = rs.getLong("MODULE_ID");
                info.workProductID = rs.getLong("PARENTID");
                info.name = rs.getString("MODULE_NAME");
                info.categoryName = rs.getString("WP_NAME");
                info.categoryID = (int) info.workProductID;
                info.estimatedSize = rs.getDouble("PLANNED_SIZE");
                info.methodBasedSize = rs.getInt("PLANNED_SIZE_TYPE");
                info.estimatedSizeUnitID = rs.getInt("PLANNED_SIZE_UNIT_ID");
                info.reestimatedSize = Db.getDouble(rs, "REPLANNED_SIZE");
                info.actualSize = Db.getDouble(rs, "ACTUAL_SIZE");
                info.description = rs.getString("MODULE_DESCRIPTION");
                info.reusePercentage = Db.getDouble(rs, "REUSE");
                info.acMethodBasedSize = Db.getDouble(rs, "ACTUAL_SIZE_TYPE");
                info.actualSizeUnitID = Db.getDouble(rs, "ACTUAL_SIZE_UNIT_ID");
                info.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
                info.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
                if (info.methodBasedSize == 0) {
                    final LanguageInfo lang =
                        Param.getLanguage(info.estimatedSizeUnitID);
                    info.estimatedSizeUnitName = lang.name;
                    info.isDocument = !"LOC".equals(lang.sizeUnit);
                }
                else {
                    //TODO The following function need to query from Database, should consider to remove it.
                    // Solution: store estimation methodes in Hashtable with keys are estimation_method_id
                    final EstimationMethodInfo method =
                        Param.getEstimationMethod(info.estimatedSizeUnitID);
                    info.estimatedSizeUnitName = method.name;
                    info.isDocument = false;
                }
                if (!Double.isNaN(info.actualSizeUnitID)) {
                    if (info.acMethodBasedSize == 0) {
                        final LanguageInfo lang =
                            Param.getLanguage((int) info.actualSizeUnitID);
                        if (lang.name != null)
                            info.actualSizeUnitName =
                                lang.name + " " + lang.sizeUnit;
                        info.isDocument = !"LOC".equals(lang.sizeUnit);
                    }
                    else {
                        //TODO The following function need to query from Database, should consider to remove it.
                        // Solution: store estimation methodes in Hashtable with keys are estimation_method_id
                        final EstimationMethodInfo method =
                            Param.getEstimationMethod(
                                (int) info.actualSizeUnitID);
                        info.actualSizeUnitName = method.name;
                    }
                }
                list.addElement(info);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return list;
        }
    }
    
    /**
     * Get products that does not have LOC (planned/actual).
     * Used for add new product LOC
     * @param projectId
     * @return
     */
    public static Vector getProductsWithoutLOC(long projectId) {
        final Vector list = new Vector();
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        ResultSet rs = null;
        try {
            sql =
                "SELECT M.MODULE_ID, M.NAME MODULE_NAME, W.NAME WP_NAME,"
                + " M.PLANNED_SIZE_UNIT_ID, M.PLANNED_SIZE, M.REPLANNED_SIZE,"
                + " M.ACTUAL_SIZE_UNIT_ID, M.ACTUAL_SIZE, M.REUSE,"
                + " M.PLANNED_SIZE_TYPE, M.ACTUAL_SIZE_TYPE,"
                + " M.NOTE MODULE_DESCRIPTION,"
                + " M.NOTE MODULE_DESCRIPTION, M.PLANNED_RELEASE_DATE,"
                + " M.REPLANNED_RELEASE_DATE,W.WP_ID PARENTID "
                + " FROM MODULE M, WORKPRODUCT W"
                + " WHERE M.WP_ID = W.WP_ID"
                + " AND W.HAS_LOC=1"    // Only get products marked as have LOC
                + " AND MODULE_ID NOT IN ("
                +       "SELECT MODULE_ID FROM PRODUCT_LOC_ACTUAL"
                +      " WHERE PROJECT_ID = ?"
                +      " UNION "
                +       "SELECT MODULE_ID FROM PRODUCT_LOC_PLAN"
                +      " WHERE PROJECT_ID = ?)"
                + " AND M.PROJECT_ID = ?"
                + " ORDER BY WP_NAME, M.NAME";

            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setLong(1, projectId);
            prepStmt.setLong(2, projectId);
            prepStmt.setLong(3, projectId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                WPSizeInfo info = new WPSizeInfo();
                info.moduleID = rs.getLong("MODULE_ID");
                info.workProductID = rs.getLong("PARENTID");
                info.name = rs.getString("MODULE_NAME");
                info.categoryName = rs.getString("WP_NAME");
                info.categoryID = (int) info.workProductID;
                info.estimatedSize = rs.getDouble("PLANNED_SIZE");
                info.methodBasedSize = rs.getInt("PLANNED_SIZE_TYPE");
                info.estimatedSizeUnitID = rs.getInt("PLANNED_SIZE_UNIT_ID");
                info.reestimatedSize = Db.getDouble(rs, "REPLANNED_SIZE");
                info.actualSize = Db.getDouble(rs, "ACTUAL_SIZE");
                info.description = rs.getString("MODULE_DESCRIPTION");
                info.reusePercentage = Db.getDouble(rs, "REUSE");
                info.acMethodBasedSize = Db.getDouble(rs, "ACTUAL_SIZE_TYPE");
                info.actualSizeUnitID = Db.getDouble(rs, "ACTUAL_SIZE_UNIT_ID");
                info.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
                info.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
                list.addElement(info);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return list;
        }
    }
    
    /**
     * Get products numbers that does not have LOC (planned/actual).
     * Used for product LOC listing, determine it can Add new LOC
     * @param projectId
     * @return
     */
    public static Integer getProductsNumbersWithoutLOC(long projectId) {
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        ResultSet rs = null;
        Integer products = new Integer(0);
        try {
            sql =
                "SELECT COUNT(M.MODULE_ID) AS Products"
                + " FROM MODULE M, WORKPRODUCT W"
                + " WHERE M.WP_ID = W.WP_ID"
                + " AND W.HAS_LOC=1"    // Only get products marked as have LOC
                + " AND MODULE_ID NOT IN ("
                +       "SELECT MODULE_ID FROM PRODUCT_LOC_ACTUAL"
                +      " WHERE PROJECT_ID = ?"
                +      " UNION "
                +       "SELECT MODULE_ID FROM PRODUCT_LOC_PLAN"
                +      " WHERE PROJECT_ID = ?)"
                + " AND M.PROJECT_ID = ?";
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setLong(1, projectId);
            prepStmt.setLong(2, projectId);
            prepStmt.setLong(3, projectId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                products = new Integer(rs.getInt("Products"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return products;
        }
    }
    
    /**
     * Get actual LOC list
     * @param projectId
     * @param moduleId
     * @return
     */
    public static Vector getActualLocList(long projectId, long moduleId) {
        return getProjectLocList(projectId, moduleId, "PRODUCT_LOC_ACTUAL");
    }
    /**
     * Get plan LOC list
     * @param projectId
     * @param moduleId
     * @return
     */
    public static Vector getPlanLocList(long projectId, long moduleId) {
        return getProjectLocList(projectId, moduleId, "PRODUCT_LOC_PLAN");
    }

    /**
     * calculate all product Loc (either plan or actual)
     * @param projectId
     * @param table: table Either PRODUCT_LOC_PLAN or PRODUCT_LOC_ACTUAL
     * @return
     */
    public static ProductLocInfo getTotalLocProductivityQuality(
        final long projectId, String table)
    {
		ProductLocInfo productLocInfo = new ProductLocInfo();
    	Connection conn = null;
    	PreparedStatement stm = null;
    	ResultSet rs = null;
    	String sql = null;
    	try {
			sql = "SELECT SUM (p.LOC_PRODUCTIVITY) LOC_PRODUCTIVITY,"
                + " SUM (p.LOC_QUALITY) LOC_QUALITY "
				+ " FROM " + table + " p "
				+ " WHERE p.project_id = ?";
    		conn = ServerHelper.instance().getConnection();
    		stm = conn.prepareStatement(sql);
            stm.setLong(1, projectId);
    		rs = stm.executeQuery();
    		if (rs.next()){
    			productLocInfo.setLocProductivity(rs.getDouble("LOC_PRODUCTIVITY"));
    			productLocInfo.setLocQuality(rs.getDouble("LOC_QUALITY"));
    		}
    	}
    	catch (SQLException ex){
    		ex.printStackTrace();
    	}
    	finally{
    		ServerHelper.closeConnection(conn, stm, rs);
    		return productLocInfo;
    	}
    }
    
    /**
     * Get summary of LOC productivity, LOC quality of project
     * @param projectId
     * @param startDate
     * @param endDate
     * @param projectType Project.INPROGRESS_PROJECTS (0),
     *          Project.CLOSEDPROJECTS (1), Project.ALLPROJECTS (2) 
     * @param isAccumulate
     * @param table     Either "PRODUCT_LOC_ACTUAL" or "PRODUCT_LOC_PLAN"
     * @return
     */
    public static ProductLocInfo getTotalLocProductivityQuality(
        long projectId,
        java.sql.Date startDate, java.sql.Date endDate,
        int projectType, boolean isAccumulate,
        String table)
    {
        ProductLocInfo productLocInfo = new ProductLocInfo();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = null;
        try {
            String moduleDateFilter = "";
            if (startDate != null && !isAccumulate) {
                moduleDateFilter += " AND NVL(M.ACTUAL_RELEASE_DATE," +
                                        " M.PLANNED_RELEASE_DATE) >=?";
            }
            if (endDate != null) {
                moduleDateFilter += " AND NVL(M.ACTUAL_RELEASE_DATE," +
                                        " M.PLANNED_RELEASE_DATE) <=?";
            }
            sql = "SELECT SUM (LOC.LOC_PRODUCTIVITY) LOC_PRODUCTIVITY,"
                + " SUM (LOC.LOC_QUALITY) LOC_QUALITY "
                + " FROM " + table + " LOC, project P, module M "
                + " WHERE LOC.module_id=M.module_id"
                + " AND LOC.project_id=P.project_id"
                + " AND p.project_id=?"
                + moduleDateFilter;
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, projectId);
            int i = 2;
            if (startDate != null && !isAccumulate) {
                stm.setDate(i++, startDate);
            }
            if (endDate != null) {
                stm.setDate(i++, endDate);
            }
            rs = stm.executeQuery();
            if (rs.next()) {
                productLocInfo.setLocProductivity(
                        rs.getDouble("LOC_PRODUCTIVITY"));
                productLocInfo.setLocQuality(rs.getDouble("LOC_QUALITY"));
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        finally{
            ServerHelper.closeConnection(conn, stm, rs);
            return productLocInfo;
        }
    }
    /**
     * Get summary of LOC productivity, LOC quality of project/group/org
     * @param wu
     * @param startDate
     * @param endDate
     * @param projectType Project.INPROGRESS_PROJECTS (0),
     *  Project.CLOSEDPROJECTS (1), Project.ALLPROJECTS (2) 
     * @param table     Either "PRODUCT_LOC_ACTUAL" or "PRODUCT_LOC_PLAN"
     * @return
     */
    public static ProductLocInfo getTotalLocProductivityQuality(
        WorkUnitInfo wu,
        java.sql.Date startDate, java.sql.Date endDate,
        int projectType, boolean isAccumulate,
        String table)
    {
        // If calculate for project
        if (wu.type == WorkUnitInfo.TYPE_PROJECT) {
            return getTotalLocProductivityQuality(
                    wu.tableID, startDate, endDate,
                    projectType, isAccumulate,table);
        }
        
        ProductLocInfo productLocInfo = new ProductLocInfo();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = null;
        try {
            String moduleDateFilter = "";
            String projectDateFilter = "";
            if (startDate != null && !isAccumulate) {
                moduleDateFilter += " AND NVL(M.ACTUAL_RELEASE_DATE," +
                                        " M.PLANNED_RELEASE_DATE) >=?";
            }
            if (endDate != null) {
                moduleDateFilter += " AND NVL(M.ACTUAL_RELEASE_DATE," +
                                        " M.PLANNED_RELEASE_DATE) <=?";
            }
            
            if (projectType == Project.INPROGRESS_PROJECTS) {
                projectDateFilter =
                    " AND P.STATUS <> " + ProjectInfo.STATUS_CANCELLED
                    + " AND P.STATUS <> " + ProjectInfo.STATUS_TENTATIVE;
                if (startDate != null) {
                    projectDateFilter += " AND P.START_DATE <=?";/* To_date('"
                            + CommonTools.dateFormatLong(startDate)
                            + "', 'dd-Mon-yyyy')";*/
                }
                if (endDate != null) {
                    projectDateFilter += " AND (P.ACTUAL_FINISH_DATE IS NULL"
                        + " OR P.ACTUAL_FINISH_DATE >=?)";/*
                        + " To_date('" + CommonTools.dateFormatLong(endDate)
                        + "','dd-Mon-yyyy'))";*/
                }
            }
            else if (projectType == Project.CLOSEDPROJECTS) {
                projectDateFilter =
                    " AND P.STATUS <> " + ProjectInfo.STATUS_CANCELLED
                    + " AND P.STATUS <> " + ProjectInfo.STATUS_TENTATIVE;
                if (startDate != null) {
                    projectDateFilter += " AND P.ACTUAL_FINISH_DATE >=?;";/* To_date('"
                        + CommonTools.dateFormatLong(startDate)
                        + "', 'dd-Mon-yyyy')";*/
                }
                if (endDate != null) {
                    projectDateFilter += " P.START_DATE <=?";/* To_date('"
                        + CommonTools.dateFormatLong(endDate)
                        + "', 'dd-Mon-yyyy')";*/
                }
            }
            
            switch (wu.type) {
                case WorkUnitInfo.TYPE_ORGANIZATION:
                    //TODO To optimize the following SQL statement, create SQL
                    // for organization independently because for organization
                    // there is not needed to add PROJECT IN condition
                    // ...
                    // Fall through
                case WorkUnitInfo.TYPE_GROUP:
                    sql = "SELECT SUM (LOC.LOC_PRODUCTIVITY) LOC_PRODUCTIVITY,"
                        + " SUM (LOC.LOC_QUALITY) LOC_QUALITY "
                        + " FROM " + table + " LOC, project P, module M,"
                        + " WORKUNIT wupj,WORKUNIT wugr"
                        + " WHERE wupj.type(+)= 2"
                        + " AND P.PROJECT_ID = wupj.TABLEID (+)"
                        + " AND wupj.PARENTWORKUNITID=wugr.WORKUNITID (+)"
                        + " AND (wupj.PARENTWORKUNITID=?"
                        +       " OR wugr.PARENTWORKUNITID=?)"
                        + " AND LOC.module_id=M.module_id"
                        + " AND LOC.project_id=P.project_id"
                        + moduleDateFilter
                        + projectDateFilter
                        ;
                    break;
            }
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, wu.workUnitID);
            stm.setLong(2, wu.workUnitID);
            int i = 3;
            // Date filter by Product release dates
            if (startDate != null && !isAccumulate) {
                stm.setDate(i++, startDate);
            }
            if (endDate != null) {
                stm.setDate(i++, endDate);
            }

            // Date filter by Project dates
            if (projectType == Project.INPROGRESS_PROJECTS ||
                projectType == Project.CLOSEDPROJECTS)
            {
                if (startDate != null) {
                    stm.setDate(i++, startDate);
                }
                if (endDate != null) {
                    stm.setDate(i++, endDate);
                }
            }
            rs = stm.executeQuery();
            if (rs.next()) {
                productLocInfo.setLocProductivity(
                        rs.getDouble("LOC_PRODUCTIVITY"));
                productLocInfo.setLocQuality(rs.getDouble("LOC_QUALITY"));
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        finally{
            ServerHelper.closeConnection(conn, stm, rs);
            return productLocInfo;
        }
    }
    /**
     * Get project's product LOC (either plan or actual)
     * @param projectId
     * @param table Either PRODUCT_LOC_PLAN or PRODUCT_LOC_ACTUAL
     * @return
     */
    private static Vector getProjectLocList(
        long projectId, long moduleId, String table)
    {
        Vector locList = new Vector();
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            sql = "SELECT P.PRODUCT_LOC_ID,P.MODULE_ID,P.LANGUAGE_ID,"
                + "P.LOC_PRODUCTIVITY,P.LOC_QUALITY,P.MOTHER_BODY,P.ADDED,"
                + "P.MODIFIED, P.TOTAL,P.REUSED,P.GENERATED,P.NOTE,"
                + "L.name AS language_name"
                + " FROM language L," + table + " P"
                + " WHERE L.language_id=P.language_id AND P.project_id=?";
            if (moduleId > 0) {
                sql += " AND P.MODULE_ID=?";
            }
            sql += " ORDER BY PRODUCT_LOC_ID";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, projectId);
            if (moduleId > 0) {
                stm.setLong(2, moduleId);
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                ProductLocInfo loc = new ProductLocInfo();
                loc.setProductLocId(rs.getLong("PRODUCT_LOC_ID"));
                loc.setModuleId(rs.getLong("MODULE_ID"));
                loc.setLanguageId(rs.getLong("LANGUAGE_ID"));
                loc.setLocProductivity(Db.getDouble(rs, "LOC_PRODUCTIVITY"));
                loc.setLocQuality(Db.getDouble(rs, "LOC_QUALITY"));
                loc.setMotherBody(Db.getDouble(rs, "MOTHER_BODY"));
                loc.setAdded(Db.getDouble(rs, "ADDED"));
                loc.setModified(Db.getDouble(rs, "MODIFIED"));
                loc.setTotal(Db.getDouble(rs, "TOTAL"));
                loc.setReused(Db.getDouble(rs, "REUSED"));
                loc.setGenerated(Db.getDouble(rs, "GENERATED"));
                loc.setNote(rs.getString("NOTE"));
                loc.setLanguageName(rs.getString("language_name"));
                locList.add(loc);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return locList;
        }
    }
    
    /**
     * Build product LOC by stage list
     * @param projectLocByStage
     * @param stages
     * @param actualLocs
     * @param productSizeMap products stored in hash table with keys are moduleId
     */
    private static void buildLocByStageList(
        Vector projectLocByStage, Vector stages,
        Vector actualLocs, HashMap productSizeMap)
    {
        ProductLocComparator.LocByLanguage sorter =
            ProductLocComparator.LocByLanguage.getInstance();
        // Re-used checking method from getWPSizeSumByStage() function
        // (using plan,re-plan release date of product compare with
        // stage begin date, stage end date
        long releaseTime, startTime, endTime;
        for (int iStage = 0; iStage < stages.size(); iStage++) {
            StageInfo stageInfo = (StageInfo) stages.get(iStage);
            ProductLocOfStageInfo stageLoc = new ProductLocOfStageInfo();
            //stageLoc.getStageSummary().resetToZero();
            // Used for unique language LOC for this stage, one stage may has
            // more than one product LOC for a specific language
            HashMap locByLanguage = new HashMap();
            
            // Used language name as stage name
            stageLoc.getStageSummary().setLanguageName(stageInfo.stage);
            startTime = stageInfo.plannedBeginDate.getTime();
            endTime = stageInfo.plannedEndDate.getTime();
            for (int iLoc = 0; iLoc < actualLocs.size(); iLoc++) {
                ProductLocInfo loc = (ProductLocInfo) actualLocs.get(iLoc);
                WPSizeInfo product = (WPSizeInfo)
                        productSizeMap.get(new Long(loc.getModuleId()));
                if (product.rePlannedReleaseDate != null) {
                    releaseTime = product.rePlannedReleaseDate.getTime();
                }
                else if (product.plannedReleaseDate != null) {
                    releaseTime = product.plannedReleaseDate.getTime();
                }
                else {
                    releaseTime = Long.MIN_VALUE;
                }
                // If this LOC belong to a product that released in this stage
                if (releaseTime >= startTime && releaseTime <= endTime) {
                    // Used Hash Map to store unique languages (for this stage)
                    Long languageId = new Long(loc.getLanguageId());
                    ProductLocInfo locNew = (ProductLocInfo)
                        locByLanguage.get(languageId);
                    if (locNew == null) {
                        locNew = (ProductLocInfo) loc.clone();
                        locByLanguage.put(languageId,locNew);
                        stageLoc.getLanguageLocs().add(locNew);
                    }
                    else {
                        locNew.plus(loc);
                    }
                    stageLoc.getStageSummary().plus(loc);
                }
            }
            java.util.Collections.sort(stageLoc.getLanguageLocs(), sorter);
            projectLocByStage.add(stageLoc);
        }
    }
    
    /**
     * Insert LOC information into LOC by language list, used for Product LOC listing page
     * @param loc
     * @param languageMap
     */
    private static void putLocByLanguage(
        ProductLocInfo loc, HashMap languageMap)
    {
        if (loc != null && languageMap != null) {
            Long langId = new Long(loc.getLanguageId());
            ProductLocInfo oldLoc = (ProductLocInfo) languageMap.get(langId);
            if (oldLoc != null) {   // This language is already existed
                oldLoc.plus(loc);
            }
            else {  // This is new language
                languageMap.put(langId, loc.clone());
            }
        }
    }
    
    /**
     * Build LOC by product listing, fills actual column,
     * used for Product LOC listing by product
     * @param loc
     * @param productSizeMap products get from module table
     * @param productLocMap product LOCs by product
     */
    private static void putActualLocByProduct(
        ProductLocInfo loc, HashMap productSizeMap, HashMap productLocMap)
    {
        if (loc != null && productSizeMap != null && productLocMap != null) {
            Long moduleId = new Long(loc.getModuleId());
            WPSizeInfo productLoc = (WPSizeInfo) productLocMap.get(moduleId);
            WPSizeInfo productInfo = (WPSizeInfo) productSizeMap.get(moduleId);
            if (productLoc != null) {   // This product is already existed
                productLoc.reusePercentage = CommonTools.addDouble(
                        productLoc.reusePercentage, loc.getReused());
                // If not have total => total is the productivity size
                if (!Double.isNaN(loc.getAdded()) ||
                    !Double.isNaN(loc.getModified()) ||
                    !Double.isNaN(loc.getMotherBody()))
                {
                    productLoc.actualSize = CommonTools.addDouble(
                            productLoc.actualSize, loc.getLocProductivity());
                }
                // This language LOC has Total and Reused data
                else {
                    productLoc.actualSize = CommonTools.addDouble(
                            productLoc.actualSize,
                            CommonTools.addDouble(
                                loc.getTotal(), -loc.getReused()));
                }
                productLoc.createdSize =
                        productLoc.actualSize - productLoc.reusePercentage;
            }
            else if (productInfo != null) {  // This is new product
                productLoc = new WPSizeInfo();
                productLoc.moduleID = loc.getModuleId();
                // If not have total => total is the productivity size
                if (!Double.isNaN(loc.getAdded()) ||
                    !Double.isNaN(loc.getModified()) ||
                    !Double.isNaN(loc.getMotherBody()))
                {
                    productLoc.actualSize = loc.getLocProductivity();
                }
                // This language LOC has Total and Reused data
                else {
                    productLoc.actualSize =
                        CommonTools.addDouble(loc.getTotal(), -loc.getReused());
                }
                productLoc.reusePercentage = loc.getReused();
                productLoc.createdSize = productLoc.actualSize -
                        CommonTools.nanToZero(productLoc.reusePercentage);
                productLoc.name = productInfo.name;
                productLoc.categoryName = productInfo.categoryName;
                
                productLocMap.put(moduleId, productLoc);
            }
        }
    }
    
    /**
     * Build LOC by product listing, fills plan column,
     * used for Product LOC listing by product
     * @param loc
     * @param productSizeMap products get from module table
     * @param productLocMap product LOCs by product
     */
    private static void putPlanLocByProduct(
        ProductLocInfo loc, HashMap productSizeMap, HashMap productLocMap)
    {
        if (loc != null && productSizeMap != null && productLocMap != null) {
            Long moduleId = new Long(loc.getModuleId());
            WPSizeInfo productLoc = (WPSizeInfo) productLocMap.get(moduleId);
            WPSizeInfo productInfo = (WPSizeInfo) productSizeMap.get(moduleId);
            if (productLoc != null) {   // This product is already existed
                productLoc.estimatedSize = CommonTools.addDouble(
                        productLoc.estimatedSize, loc.getLocProductivity());
            }
            else if (productInfo != null) {  // This is new product
                productLoc = new WPSizeInfo();
                productLoc.moduleID = loc.getModuleId();
                productLoc.estimatedSize = loc.getLocProductivity();
                productLoc.name = productInfo.name;
                productLoc.categoryName = productInfo.categoryName;

                productLocMap.put(moduleId, productLoc);
            }
        }
    }
    /**
     * Put products to HashMap with keys are ModuleId,
     * used for Product LOC listing by product
     * @param products
     * @param productMap
     */
    private static HashMap putProductsToHashMap(Vector products) {
        HashMap productMap = new HashMap(1);    // Set to smallest size in case
                                                // there are no products
        if (products != null) {
            // Constructs an HashMap with the initial capacity is two times
            // of list size
            productMap = new HashMap(products.size() * 2);
            for (int i = 0; i < products.size(); i++) {
                WPSizeInfo info = (WPSizeInfo) products.get(i);
                productMap.put(new Long(info.moduleID), info);
            }
        }
        return productMap;
    }
    /**
     * Create product LOC list by language that used with LOC listing page
     * @param projectLocByLanguage
     * @param languageMap
     */
    private static void buildLocByLanguage(Vector projectLocByLanguage,
        HashMap languageMap)
    {
        ProductLocComparator.LocByLanguage sorter =
            ProductLocComparator.LocByLanguage.getInstance();
        java.util.Set languageKeys = languageMap.keySet();
        Iterator it = languageKeys.iterator();
        while (it.hasNext()) {
            // Get LOC information from HashMap
            ProductLocInfo loc =
                (ProductLocInfo) languageMap.get((Long) it.next());
            // Put to listing
            projectLocByLanguage.add(loc);
        }
        java.util.Collections.sort(projectLocByLanguage, sorter);
    }
    /**
     * Create product LOC list by product that used with LOC listing page
     * @param projectLocByLanguage
     * @param languageMap
     */
    private static void buildLocByProduct(Vector projectLocByProduct,
        HashMap productLocMap)
    {
        ProductLocComparator.LocByProduct sorter =
            ProductLocComparator.LocByProduct.getInstance();
        java.util.Set productLocKeys = productLocMap.keySet();
        Iterator it = productLocKeys.iterator();
        while (it.hasNext()) {
            WPSizeInfo productLoc = (WPSizeInfo) productLocMap.get((Long) it.next());
            projectLocByProduct.add(productLoc);
        }
        java.util.Collections.sort(projectLocByProduct, sorter);
    }
    /**
     * Create data structure for product LOC listing page
     * @param projectLocSummary: project LOC summary
     * @param projectLocByStage: project LOC by stage
     * @param projectLanguages: project LOC by language (list of ProductLocInfo)
     * @param productSizeList: project LOC by product (list of WPSizeInfo,
     *      used the same as Product page but the unit is LOC instead of UCP)
     */
    public static void createProductLocListing(ProjectInfo projectInfo,
        WPSizeInfo projectLocSummary, Vector projectLocByStage,
        Vector projectLocByLanguage, Vector projectLocByProduct)
    {
        long prjId = projectInfo.getProjectId();
        Vector planLocs = getProjectLocList(prjId, 0, "PRODUCT_LOC_PLAN");
        Vector actualLocs = getProjectLocList(prjId, 0, "PRODUCT_LOC_ACTUAL");
        Vector stages = Schedule.getStageList(projectInfo);
        Vector products = getProductsHaveLoc(prjId);
        // Used to store unique languages, used default initial capacity
        HashMap languageMap = new HashMap();
        // Used to store unique product size information     
        HashMap productSizeMap = putProductsToHashMap(products);
        // Used for product LOC list by product     
        HashMap productLocMap = new HashMap();  // used default initial capacity
        
        //ProductLocInfo actualLocSummary = new ProductLocInfo();
        //ProductLocInfo planLocSummary = new ProductLocInfo();
        
        // Create LOC by stage listing
        buildLocByStageList(projectLocByStage, stages, actualLocs, productSizeMap);
        
        // Get all information for actual lists,
        // build LOC by language, LOC by product maps, create LOC summary info
        for (int i = 0; i < actualLocs.size(); i++) {
            ProductLocInfo loc = (ProductLocInfo) actualLocs.get(i);
            putLocByLanguage(loc, languageMap);
            putActualLocByProduct(loc, productSizeMap, productLocMap);
            //actualLocSummary.plus(loc);
            
        }
        //projectLocSummary.actualSize = actualLocSummary.getLocProductivity();
        //projectLocSummary.reusePercentage = actualLocSummary.getReused();
        
        // reusePercentage is used as numbers of LOCs, not percentage mean
        projectLocSummary.createdSize =
            projectLocSummary.actualSize - projectLocSummary.reusePercentage;
        // Fill planned values for Product LOC by product list
        for (int i = 0; i < planLocs.size(); i++) {
            ProductLocInfo loc = (ProductLocInfo) planLocs.get(i);
            putPlanLocByProduct(loc, productSizeMap, productLocMap);
            
            //planLocSummary.plus(loc);
        }
        //projectLocSummary.estimatedSize = planLocSummary.getLocProductivity();
        
        // Create LOC by product listing, LOC by language listing
        buildLocByProduct(projectLocByProduct, productLocMap);
        buildLocByLanguage(projectLocByLanguage, languageMap);
        // Summary for project LOC information
        for (int i = 0; i < projectLocByProduct.size(); i++) {
            WPSizeInfo locByProduct = (WPSizeInfo) projectLocByProduct.get(i);
            projectLocSummary.actualSize = CommonTools.addDouble(
                projectLocSummary.actualSize, locByProduct.actualSize);
            projectLocSummary.reusePercentage = CommonTools.addDouble(
                projectLocSummary.reusePercentage, locByProduct.reusePercentage);
            projectLocSummary.estimatedSize = CommonTools.addDouble(
                projectLocSummary.estimatedSize, locByProduct.estimatedSize);
            projectLocSummary.createdSize = CommonTools.addDouble(
                projectLocSummary.createdSize, locByProduct.createdSize);
        }
    }
    
    /**
     * Get product LOC detail information, used for Product LOC detail page
     * @param projectId
     * @param moduleId
     * @return
     */
    public static final ProductLocDetailInfo getProductLocDetail(
        long projectId, long moduleId)
    {
        ProductLocDetailInfo locDetail = new ProductLocDetailInfo();
        
        // Get product detail information,
        // the following call returned exactly one or zero member
        Vector products = WorkProduct.getModuleList(projectId, moduleId);
        WPSizeInfo moduleInfo = new WPSizeInfo();
        if (products.size() > 0) {
            moduleInfo = (WPSizeInfo) products.get(0);
        }
        Vector actualLocList = WorkProduct.getActualLocList(projectId, moduleId);
        Vector planLocList = WorkProduct.getPlanLocList(projectId, moduleId);
        
        locDetail.setProductDetail(moduleInfo);
        locDetail.setActualLocs(actualLocList);
        locDetail.setPlanLocs(planLocList);
        return locDetail;
    }

    /**
     * Set data for prepared statement, used for Insert new LOC
     * @param stm
     * @param id
     * @param loc
     * @throws SQLException
     */
    private static void setProductLocInsertFields(PreparedStatement stm,
        long id, ProductLocInfo loc) throws SQLException
    {
        stm.setLong(1,id);
        stm.setLong(2, loc.getProjectId());
        stm.setLong(3, loc.getModuleId());
        stm.setLong(4, loc.getLanguageId());
        Db.setDouble(stm, 5, loc.getLocProductivity());
        Db.setDouble(stm, 6, loc.getLocQuality());
        Db.setDouble(stm, 7, loc.getMotherBody());
        Db.setDouble(stm, 8, loc.getAdded());
        Db.setDouble(stm, 9, loc.getModified());
        Db.setDouble(stm, 10, loc.getTotal());
        Db.setDouble(stm, 11, loc.getReused());
        Db.setDouble(stm, 12, loc.getGenerated());
        stm.setString(13, loc.getNote());
    }
    /**
     * Set data for prepared statement, used for Update LOC
     * @param stm
     * @param loc
     * @throws SQLException
     */
    private static void setProductLocUpdateFields(PreparedStatement stm,
        ProductLocInfo loc) throws SQLException
    {
        stm.setLong(1, loc.getLanguageId());
        Db.setDouble(stm, 2, loc.getLocProductivity());
        Db.setDouble(stm, 3, loc.getLocQuality());
        Db.setDouble(stm, 4, loc.getMotherBody());
        Db.setDouble(stm, 5, loc.getAdded());
        Db.setDouble(stm, 6, loc.getModified());
        Db.setDouble(stm, 7, loc.getTotal());
        Db.setDouble(stm, 8, loc.getReused());
        Db.setDouble(stm, 9, loc.getGenerated());
        stm.setString(10, loc.getNote());
        stm.setLong(11, loc.getProductLocId());
        stm.setLong(12, loc.getProjectId());
        stm.setLong(13, loc.getModuleId());
    }
    /**
     * Set data for prepared statement, used for Delete LOC
     * @param stm
     * @param loc
     * @throws SQLException
     */
    private static void setProductLocDeleteFields(PreparedStatement stm,
        ProductLocInfo loc) throws SQLException
    {
        stm.setLong(1, loc.getProductLocId());
        stm.setLong(2, loc.getProjectId());
        stm.setLong(3, loc.getModuleId());
    }
    /**
     * Save Product LOC add new form
     * @param projectId
     * @param moduleId
     * @param planLocs
     * @param actualLocs
     * @return
     */
    public static boolean saveProductLocAddNew(
        Vector planLocs, Vector actualLocs)
    {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stm = null;
        String sqlPlan =  "INSERT INTO PRODUCT_LOC_PLAN"
            + " (PRODUCT_LOC_ID,PROJECT_ID,MODULE_ID,LANGUAGE_ID"
            + ",LOC_PRODUCTIVITY,LOC_QUALITY,MOTHER_BODY,ADDED,MODIFIED,TOTAL"
            + ",REUSED,GENERATED,NOTE)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlActual = "INSERT INTO PRODUCT_LOC_ACTUAL"
            + " (PRODUCT_LOC_ID,PROJECT_ID,MODULE_ID,LANGUAGE_ID"
            + ",LOC_PRODUCTIVITY,LOC_QUALITY,MOTHER_BODY,ADDED,MODIFIED,TOTAL"
            + ",REUSED,GENERATED,NOTE)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);  // Multi-records => enable transaction
            stm = conn.prepareStatement(sqlPlan);
            for (int i = 0; i < planLocs.size(); i++) {
                ProductLocInfo loc = (ProductLocInfo) planLocs.get(i);
                long id = ServerHelper.getNextSeq("PRODUCT_LOC_PLAN_SEQ");
                setProductLocInsertFields(stm, id, loc);
                stm.executeUpdate();
            }
            stm.close();
            stm = conn.prepareStatement(sqlActual);
            for (int i = 0; i < actualLocs.size(); i++) {
                ProductLocInfo loc = (ProductLocInfo) actualLocs.get(i);
                long id = ServerHelper.getNextSeq("PRODUCT_LOC_ACTUAL_SEQ");
                setProductLocInsertFields(stm, id, loc);
                stm.executeUpdate();
            }
            stm.close();
            conn.commit();
            conn.setAutoCommit(true);
            result = true;
        }
        catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
            return result;
        }
    }
    /**
     * Save Product LOC update form (INSERT, UPDATE, DELETE)
     * @param planLocs
     * @param actualLocs
     * @return
     */
    public static boolean saveProductLocUpdate(
        Vector planLocs, Vector actualLocs)
    {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stm = null;
        String sqlInsertPlan = "INSERT INTO PRODUCT_LOC_PLAN"
            + " (PRODUCT_LOC_ID,PROJECT_ID,MODULE_ID,LANGUAGE_ID"
            + ",LOC_PRODUCTIVITY,LOC_QUALITY,MOTHER_BODY,ADDED,MODIFIED,TOTAL"
            + ",REUSED,GENERATED,NOTE)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlInsertActual = "INSERT INTO PRODUCT_LOC_ACTUAL"
            + " (PRODUCT_LOC_ID,PROJECT_ID,MODULE_ID,LANGUAGE_ID"
            + ",LOC_PRODUCTIVITY,LOC_QUALITY,MOTHER_BODY,ADDED,MODIFIED,TOTAL"
            + ",REUSED,GENERATED,NOTE)"
            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        // For records being updated, add more conditions to sure the exact data
        String sqlUpdatePlan = "UPDATE PRODUCT_LOC_PLAN SET"
            + " LANGUAGE_ID=?,LOC_PRODUCTIVITY=?,LOC_QUALITY=?,MOTHER_BODY=?"
            + ",ADDED=?,MODIFIED=?,TOTAL=?,REUSED=?,GENERATED=?,NOTE=?"
            + " WHERE PRODUCT_LOC_ID=? AND PROJECT_ID=? AND MODULE_ID=?";
        String sqlUpdateActual = "UPDATE PRODUCT_LOC_ACTUAL SET"
            + " LANGUAGE_ID=?,LOC_PRODUCTIVITY=?,LOC_QUALITY=?,MOTHER_BODY=?"
            + ",ADDED=?,MODIFIED=?,TOTAL=?,REUSED=?,GENERATED=?,NOTE=?"
            + " WHERE PRODUCT_LOC_ID=? AND PROJECT_ID=? AND MODULE_ID=?";
        
        // For records being deleted, add more conditions to sure the exact data
        String sqlDeletePlan = "DELETE PRODUCT_LOC_PLAN"
            + " WHERE PRODUCT_LOC_ID=? AND PROJECT_ID=? AND MODULE_ID=?";
        String sqlDeleteActual = "DELETE PRODUCT_LOC_ACTUAL"
            + " WHERE PRODUCT_LOC_ID=? AND PROJECT_ID=? AND MODULE_ID=?";
        try {
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);  // Multi-records => enable transaction
            // Plan product LOC
            for (int i = 0; i < planLocs.size(); i++) {
                ProductLocInfo loc = (ProductLocInfo) planLocs.get(i);
                // This record marked as being updated
                if (loc.getDmlType() == ProductLocInfo.DML_UPDATE) {
                    stm = conn.prepareStatement(sqlUpdatePlan);
                    setProductLocUpdateFields(stm, loc);
                    stm.executeUpdate();
                }
                // This record marked as being deleted
                else if (loc.getDmlType() == ProductLocInfo.DML_DELETE) {
                    stm = conn.prepareStatement(sqlDeletePlan);
                    setProductLocDeleteFields(stm, loc);
                    stm.executeUpdate();
                }
                // Otherwise, this record marked as being inserted
                else {
                    long id = ServerHelper.getNextSeq("PRODUCT_LOC_PLAN_SEQ");
                    stm = conn.prepareStatement(sqlInsertPlan);
                    setProductLocInsertFields(stm, id, loc);
                    stm.executeUpdate();
                }
                // Close before prepare to open another statement
                stm.close();
            }
            // Actual product LOC
            for (int i = 0; i < actualLocs.size(); i++) {
                ProductLocInfo loc = (ProductLocInfo) actualLocs.get(i);
                // This record marked as being updated
                if (loc.getDmlType() == ProductLocInfo.DML_UPDATE) {
                    stm = conn.prepareStatement(sqlUpdateActual);
                    setProductLocUpdateFields(stm, loc);
                    stm.executeUpdate();
                }
                // This record marked as being deleted
                else if (loc.getDmlType() == ProductLocInfo.DML_DELETE) {
                    stm = conn.prepareStatement(sqlDeleteActual);
                    setProductLocDeleteFields(stm, loc);
                    stm.executeUpdate();
                }
                // Otherwise, this record marked as being inserted
                else {
                    long id = ServerHelper.getNextSeq("PRODUCT_LOC_ACTUAL_SEQ");
                    stm = conn.prepareStatement(sqlInsertActual);
                    setProductLocInsertFields(stm, id, loc);
                    stm.executeUpdate();
                }
                // Close before prepare to open another statement
                stm.close();
            }
            conn.commit();
            conn.setAutoCommit(true);
            result = true;
        }
        catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
            return result;
        }
    }
    /**
     * 
     * @param lProjectId
     * @param productId: is array of productID
     * @return return
     */
    public static int doSumNumberOfTestCaseProduct(final long lProjectId, int[] productId){
    	int numberTestCase = 0;
    	Connection conn = null;
    	PreparedStatement preStm = null;
    	ResultSet rs =  null;
    	String sql = null;
    	try{
			String constraint = ConvertString.arrayToString(productId,",");
    		sql = "SELECT SUM (ACTUAL_SIZE) ACTUAL_SIZE"
  					+ " FROM MODULE m "
 					+ " WHERE m.PROJECT_ID=?"
 					+ " AND m.wp_id IN (" 
 					+ constraint
 					+ ") AND m.actual_size_unit_id = 656"; // 656 is Id of language: "Number Of" which has sizeUnit is TestCase
 			conn = ServerHelper.instance().getConnection();
 			preStm = conn.prepareStatement(sql);
 			preStm.setLong(1, lProjectId);
 			rs = preStm.executeQuery();
 			if (rs.next()){
 				numberTestCase = rs.getInt("ACTUAL_SIZE");
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        finally{
            ServerHelper.closeConnection(conn, preStm, rs);
            return numberTestCase;
        }
    }
    /**
     * check module which added loc for Languages?
     * @param lProjectId
     * @param lModuleId
     * @return
     */
    public static boolean doCheckHasLoc(
        final long lProjectId, final long lModuleId, String table)
    {
        boolean checkResult = false;
        Connection conn = null;
        PreparedStatement preStm = null;
        ResultSet rs = null;
        String sql = null;
        try{
            sql = "SELECT COUNT(PRODUCT_LOC_ID) COUNT"
            +" FROM " + table
            + " WHERE PROJECT_ID = ?"
            + " AND MODULE_ID = ?";
            conn = ServerHelper.instance().getConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setLong(1, lProjectId);
            preStm.setLong(2, lModuleId);
            rs = preStm.executeQuery();
            if (rs.next()){
                checkResult = rs.getInt("COUNT") > 0;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        finally{
            ServerHelper.closeConnection(conn, preStm, rs);
            return checkResult;
        }
    }
	public static String getWPCode(final int wp_id)
		{
			Connection conn = null;
			PreparedStatement preStm = null;
			ResultSet rs = null;
			String sql = null;
			String result = "";
			try{
				sql = "select CODE from workproduct where WP_ID = ?";
				conn = ServerHelper.instance().getConnection();
				preStm = conn.prepareStatement(sql);
				preStm.setInt(1, wp_id);
				rs = preStm.executeQuery();
				if (rs.next()){
					result = rs.getString("CODE");
				}
			}
			catch (SQLException ex){
				ex.printStackTrace();
			}
			finally{
				ServerHelper.closeConnection(conn, preStm, rs);
				return result;
			}
		}
		
	public static final Hashtable getProcessWorkProductList() {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		ResultSet rs = null;
		Hashtable process_work = new Hashtable();
		try {			
			
			conn = ServerHelper.instance().getConnection();			
			sql =      "SELECT B.PROCESS_ID AS PROCESS_ID, B.NAME AS PROCESS_NAME, "
					+ 		 " A.WP_ID as WORK_PRODUCT_ID, A.name AS WORK_PRODUCT_NAME "
					+ " FROM WORKPRODUCT A, PROCESS B"
					+ " WHERE A.PROCESS = B.PROCESS_ID" 
					+ " AND B.NAME IN ('Design','Requirement','Coding','Other')"
					+ " AND A.DISABLED  = 0"
					+ " ORDER BY PROCESS_ID";
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				process_work.put(rs.getString("WORK_PRODUCT_NAME"),rs.getString("PROCESS_NAME")) ;				
			}			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return process_work;
		}
	}
	
	public static Vector getDefectModuleList(final long projectId) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;		
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT M.MODULE_ID, M.NAME MODULE_NAME, W.NAME WP_NAME,"
					+ " M.PLANNED_SIZE_UNIT_ID, M.PLANNED_SIZE, M.REPLANNED_SIZE,"
					+ " M.ACTUAL_SIZE_UNIT_ID, M.ACTUAL_SIZE, M.REUSE, M.NEW_PLAN_SIZE_REV, M.NEW_PLAN_SIZE_TEST, M.IS_DEFECT_REVIEW, M.IS_DEFECT_TEST,"
					+ " M.PLANNED_SIZE_TYPE, M.ACTUAL_SIZE_TYPE, M.NOTE MODULE_DESCRIPTION,"
					+ " M.IS_DELIVERABLE,W.WP_ID PARENTID,M.PLANNED_DEFECT,M.REPLANNED_DEFECT,M.PLANNED_RELEASE_DATE,M.REPLANNED_RELEASE_DATE, w.HAS_LOC"
					+ " FROM MODULE M, WORKPRODUCT W "
					+ " WHERE M.PROJECT_ID = ? AND M.WP_ID = W.WP_ID "					
					+ " ORDER BY WP_NAME, M.NAME";            
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectId);			
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final WPSizeInfo info = new WPSizeInfo();
				info.moduleID = rs.getLong("MODULE_ID");
				info.workProductID = rs.getLong("PARENTID");
				info.name = rs.getString("MODULE_NAME");
				info.categoryName = rs.getString("WP_NAME");
				info.categoryID = (int)info.workProductID;
				info.estimatedSize = rs.getDouble("PLANNED_SIZE");
				info.methodBasedSize = rs.getInt("PLANNED_SIZE_TYPE");
				info.estimatedSizeUnitID = rs.getInt("PLANNED_SIZE_UNIT_ID");
				info.reestimatedSize = Db.getDouble(rs, "REPLANNED_SIZE");
				info.actualSize = Db.getDouble(rs, "ACTUAL_SIZE");
				info.description = rs.getString("MODULE_DESCRIPTION");
				info.reusePercentage = Db.getDouble(rs, "REUSE");
				info.newPlanSizeReview = Db.getDouble(rs, "NEW_PLAN_SIZE_REV");
				info.newPlanSizeTest = Db.getDouble(rs, "NEW_PLAN_SIZE_TEST");
				info.isDefectReview = rs.getInt("IS_DEFECT_REVIEW");
				info.isDefectTest = rs.getInt("IS_DEFECT_TEST");
				info.acMethodBasedSize = Db.getDouble(rs, "ACTUAL_SIZE_TYPE");
				info.actualSizeUnitID = Db.getDouble(rs, "ACTUAL_SIZE_UNIT_ID");
				info.isDel = rs.getInt("IS_DELIVERABLE");
				info.plannedDefects = Db.getDouble(rs, "PLANNED_DEFECT");
				info.rePlannedDefects = Db.getDouble(rs, "REPLANNED_DEFECT");
				info.plannedReleaseDate = rs.getDate("PLANNED_RELEASE_DATE");
				info.rePlannedReleaseDate = rs.getDate("REPLANNED_RELEASE_DATE");
				if (rs.getString("HAS_LOC") != null){
					info.setHasLoc(rs.getInt("HAS_LOC"));
				}
				else{
					info.setHasLoc(0);
				}
				if (info.methodBasedSize == 0) {
					final LanguageInfo lang = Param.getLanguage(info.estimatedSizeUnitID);
					info.estimatedSizeUnitName = lang.name + " " + lang.sizeUnit;
					info.isDocument = !"LOC".equals(lang.sizeUnit);
				}
				else {
					//TODO The following function need to query from Database, should consider to remove it.
					// Solution: store estimation methodes in Hashtable with keys are estimation_method_id
					final EstimationMethodInfo method = Param.getEstimationMethod(info.estimatedSizeUnitID);
					info.estimatedSizeUnitName = method.name;
					info.isDocument = false;
				}
				if (!Double.isNaN(info.actualSizeUnitID)) {
					if (info.acMethodBasedSize == 0) {
						final LanguageInfo lang = Param.getLanguage((int)info.actualSizeUnitID);
						if (lang.name != null)
							info.actualSizeUnitName = lang.name + " " + lang.sizeUnit;
						info.isDocument = !"LOC".equals(lang.sizeUnit);
					}
					else {
						//TODO The following function need to query from Database, should consider to remove it.
						// Solution: store estimation methodes in Hashtable with keys are estimation_method_id
						final EstimationMethodInfo method = Param.getEstimationMethod((int)info.actualSizeUnitID);
						info.actualSizeUnitName = method.name;
					}
				}
				list.addElement(info);
			}
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return list;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
}