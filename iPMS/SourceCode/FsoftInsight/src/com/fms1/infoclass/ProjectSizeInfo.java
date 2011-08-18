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
 
 package com.fms1.infoclass;
import java.sql.*;
import com.fms1.web.*;
import com.fms1.tools.CommonTools;
import com.fms1.tools.Db;
/**
 * as it says
 * @author manu
 * Created on Sep 16, 2003
 * Used for project product page and product LOC page
 */
public final class ProjectSizeInfo {
	
	public double totalEstimatedSize = 0;
	public double totalReestimatedSize = 0;
	public double totalActualSize = 0;
	public double totalCreatedSize = 0;
	public double totalSizeDeviation = 0;
	public double totalPlannedSize =Double.NaN;
	public ProjectSizeInfo(long prjID){
		setProjectSizeInfo(prjID,null);
	}
	public ProjectSizeInfo(long prjID,java.sql.Date date){
		setProjectSizeInfo(prjID,date);
	}
	private void setProjectSizeInfo(long prjID,java.sql.Date date)
	{
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		
		try {
			//language non method based
			sql =
				"SELECT  SUM(NVL(M.PLANNED_SIZE/C.SLOC,0)) SUMPLANNED,SUM(NVL(M.REPLANNED_SIZE/C.SLOC,(NVL(M.PLANNED_SIZE/C.SLOC,0)))) SUMREPLANNED"
					+ " FROM MODULE M ,CONVERSION C"
					+ " WHERE  M.PROJECT_ID = ? "
					+ " AND M.PLANNED_SIZE_TYPE=0 "
					+ " AND C.LANGUAGE_ID=M.PLANNED_SIZE_UNIT_ID"
					+ " AND C.METHOD_ID = 3";
			//ucp
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			rs.next();
			totalEstimatedSize = Db.getDouble(rs, "SUMPLANNED");
			totalReestimatedSize = Db.getDouble(rs, "SUMREPLANNED");
			rs.close();
			prepStmt.close();
			if (Double.isNaN(totalEstimatedSize))
				totalEstimatedSize = 0;
			if (Double.isNaN(totalReestimatedSize))
				totalReestimatedSize = 0;
			//get conversion factor and misc data
			sql = "select (SELECT SLOC FROM CONVERSION WHERE LANGUAGE_ID = 6 AND METHOD_ID = 3)SLOC, (SELECT COUNT(*) FROM MODULE M WHERE M.PROJECT_ID = ? AND M.REPLANNED_SIZE IS NOT NULL ) THECOUNT FROM DUAL";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			rs.next();
			double conversionFactor = Db.getDouble(rs, "SLOC");
			boolean isReplan=(rs.getInt("THECOUNT")>0);
			rs.close();
			prepStmt.close();
			//same with method based estimation
			sql =
				"SELECT  SUM(NVL(M.PLANNED_SIZE/C.SLOC,0))*? SUMPLANNED,SUM(NVL(M.REPLANNED_SIZE/C.SLOC,(NVL(M.PLANNED_SIZE/C.SLOC,0))))*? SUMREPLANNED"
					+ " FROM MODULE M ,CONVERSION C"
					+ " WHERE  M.PROJECT_ID = ? "
					+ " AND M.PLANNED_SIZE_TYPE="+ModuleInfo.SIZE_TYPE_ESTIM_METHOD
					+ " AND C.LANGUAGE_ID="+LanguageInfo.FOURTH_GENERATION_ID
					+ " AND C.METHOD_ID = M.PLANNED_SIZE_UNIT_ID";
			//ucp
			prepStmt = conn.prepareStatement(sql);
			Db.setDouble(prepStmt, 1, conversionFactor);
			Db.setDouble(prepStmt, 2, conversionFactor);
			prepStmt.setLong(3, prjID);
			rs = prepStmt.executeQuery();
			rs.next();
			double tmptotalEstimatedSize = Db.getDouble(rs, "SUMPLANNED");
			double tmptotalReestimatedSize = Db.getDouble(rs, "SUMREPLANNED");
			if (!Double.isNaN(tmptotalEstimatedSize))
				totalEstimatedSize += tmptotalEstimatedSize;
			if (!Double.isNaN(tmptotalReestimatedSize))
				totalReestimatedSize += tmptotalReestimatedSize;
			rs.close();
			prepStmt.close();
			if (!isReplan){
				totalReestimatedSize=Double.NaN;
				totalPlannedSize=totalEstimatedSize;
			}
			else
				totalPlannedSize=totalReestimatedSize;
				
			String dateConstraint=(date ==null)?"":" AND M.ACTUAL_RELEASE_DATE <='"+CommonTools.dateFormat(date)+"' ";
			//urgh ! but needed for performance
			sql =
				"SELECT SUM(SUMACTUAL) SUMSUMACTUAL, SUM(CREATEDSIZE) SUMCREATEDSIZE FROM("
					+ " SELECT  SUM(NVL(M.ACTUAL_SIZE/C.SLOC,0))*? SUMACTUAL"
					+ " , SUM(NVL((1 - (NVL(M.REUSE,0) / 100))*M.ACTUAL_SIZE/C.SLOC,0))*? CREATEDSIZE, 1"
					+ " FROM MODULE M ,CONVERSION C"
					+ " WHERE  M.PROJECT_ID = ?"
					+ " AND M.ACTUAL_SIZE_TYPE="+ModuleInfo.SIZE_TYPE_ESTIM_METHOD
					+ dateConstraint
					+ " AND C.LANGUAGE_ID="+LanguageInfo.FOURTH_GENERATION_ID
					+ " AND C.METHOD_ID = M.ACTUAL_SIZE_UNIT_ID"
					+ " UNION"
					+ " SELECT  SUM(NVL(M.ACTUAL_SIZE/C.SLOC,0)) SUMACTUAL"
					+ " , SUM(NVL((1 - (NVL(M.REUSE,0) / 100))*M.ACTUAL_SIZE/C.SLOC,0)) CREATEDSIZE, 2"
					+ " FROM MODULE M ,CONVERSION C"
					+ " WHERE  M.PROJECT_ID = ? "
					+ " AND M.ACTUAL_SIZE_TYPE="+ModuleInfo.SIZE_TYPE_LANGUAGE
					+ dateConstraint
					+ " AND C.LANGUAGE_ID=M.ACTUAL_SIZE_UNIT_ID"
					+ " AND C.METHOD_ID ="+EstimationMethodInfo.METHOD_UCP

					+ " )";
			prepStmt = conn.prepareStatement(sql);
			Db.setDouble(prepStmt, 1, conversionFactor);
			Db.setDouble(prepStmt, 2, conversionFactor);
			prepStmt.setLong(3, prjID);
			prepStmt.setLong(4, prjID);
			rs = prepStmt.executeQuery();
			rs.next();
			totalActualSize = Db.getDouble(rs, "SUMSUMACTUAL");
			totalCreatedSize = Db.getDouble(rs, "SUMCREATEDSIZE");
			totalSizeDeviation = CommonTools.metricDeviation(totalEstimatedSize, totalReestimatedSize, totalActualSize);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
}
