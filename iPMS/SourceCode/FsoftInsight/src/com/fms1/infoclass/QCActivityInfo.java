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
import java.util.Vector;

import com.fms1.web.ServerHelper;

/**
 * @author manu
 * @date Apr 3, 2004
 */
public class QCActivityInfo {
	public static final int SYSTEM_TEST=12;
	public static final int ACCEPTANCE_TEST=13;
	public static final int REGRESSION_TEST=14;
	public static final int UNIT_TEST=10;
	public static final int DOCUMENT_REVIEW=20;
	public static final int CODE_REVIEW=21;
	public static final int AFTER_RELEASE_REVIEW=22;
	public static final int PROTOTYPE_REVIEW=23;
	public static final int OTHER_REVIEW=24;
	public static final int AFTER_RELEASE_TEST=15;
	public static final int QUALITY_GATE_INSPECTION=30;
	public static final int BASELINE_AUDIT=40;
	public static final int OTHER_AUDIT=41;
	public static final int FINAL_INSPECTION=31;
	public static final int OTHER_TEST=16;
	public static final int INTEGRATION_TEST=11;
	public static final int OTHER_INSPECTION=32;
	public static final int PROTOTYPE_TEST=17;
	/*not matching QC_activity table IDs*/
	public static final int INTERNAL_AUDIT=42;
	
    // QC Activities,for PPM,RREVIEW and DREVIEW not belong to QC Activity table
    public static final int QC_RREVIEW = 1;
    public static final int QC_DREVIEW = 2;
    public static final int QC_CREVIEW = 3;
    public static final int QC_UTEST = 4;
    public static final int QC_ITEST = 5;
    public static final int QC_STEST = 6;
    public static final int QC_OREVIEW = 7;
    public static final int QC_OTEST = 8;
    public static final int QC_OTHER = 9;
    public static final String[] qcNames =
        { "Requirement Review", "Design Review", "Code Review", "Unit Test",
            "Integration Test", "System Test", "Other Review", "Other Test", "Others"};
    public static final int[] qcList = { QC_RREVIEW, QC_DREVIEW, QC_CREVIEW,
            QC_UTEST, QC_ITEST, QC_STEST, QC_OREVIEW, QC_OTEST, QC_OTHER};
	
	public static Vector qcActivitiesList=getActivities();
	public int id;
	public String name;    
	             
  	private static Vector getActivities() {
		Connection conn = null;
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector result = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT QA_ID, NAME  FROM QC_ACTIVITY ORDER BY NAME";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			QCActivityInfo info;
			while (rs.next()) {
				info=new QCActivityInfo();
				info.id=rs.getInt("QA_ID");
				info.name=rs.getString("NAME").substring(3);
				result.add(info);
			}
			info=new QCActivityInfo();
			info.id=INTERNAL_AUDIT;
			info.name="Internal audit";
			//insert it just before the "other audit"
			result.insertElementAt(info,result.size()-1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
			return result;
		}
	}  
	public static QCActivityInfo getActivity(int id) {
		int size=qcActivitiesList.size();
		QCActivityInfo info;
		for (int i=0;i<size;i++){
			info=(QCActivityInfo)qcActivitiesList.elementAt(i);
			if (info.id==id)
				return info;
		}
		info=new QCActivityInfo();
		info.id=0;
		info.name="N/A";
		return info;
	
	}                                      
                                                 
}
