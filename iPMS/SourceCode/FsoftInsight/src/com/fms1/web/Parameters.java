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
 
 package com.fms1.web;
import java.sql.*;
import com.fms1.tools.CommonTools;
/**
 * Used to store global application parameters
 * Be carefull to use the instance() function instead of the creator 
 * 
 * @author manu
 * @date May 15, 2003
 */
public final class Parameters {
	/*DIRTY CONSTANTS:must match with workunit table ID records*/
	public static final long FSOFT_WU = 132;
	public static final long SQA_WU = 48;
	public static final long SQA_PROJECT=10377;
	public static final long PQA_WU = 10;
	
    public static final long TMG_WU = 308;
	public static final long ADMIN_WU = 305;
	public static final long DDC_WU = 262;
	public static final long FIST_WU = 309;
	public static final long FWB_WU = 306;
	public static final long JCT_WU = 249;
	public static final long SEPG_WU = 133;
	
    public static final String SQA_GROUP = "SQA";//table rightgroup
	public static final String ORGNAME = "FSOFT";//
	
	public static final String PQA_ROLE = "PQA";//table rightgroup
	public static final String SQA_ROLE = "SQA";//table rightgroup
	
    public static final String TMG_ROLE = "TMG";//table rightgroup
	public static final String ADMIN_ROLE = "Admin";//table rightgroup
	public static final String DDC_ROLE = "DDC";//table rightgroup
	public static final String FIST_ROLE = "FIST";//table rightgroup
	public static final String FWB_ROLE = "FWB";//table rightgroup
	public static final String JCT_ROLE = "JCT";//table rightgroup
	public static final String SEPG_ROLE = "SEPG";//table rightgroup
	
	/*-----------------*/
    
	private static boolean _me = getAllParameters();//do not delete this line !!!
	public  static String timesheetURL;
	public  static String dashboardURL;
	public  static String dmsURL;
	public  static String ncmsURL;
	public  static String callLogURL;
	public  static String itsURL;
	public  static String helpURL;
	public  static Date convLastUpd;
	public  static Date complLastUpd;
	public  static String FMS1version;
	public  static String NormQActivityEffort;
	public  static String NormProcessAuditEffort;
	public  static String NormProcessControlEffort;
	public  static String logRoot;
	public  static String localhost;
	public  static String connectionString;
	public  static Date dayOfValidation;
	private Parameters() { 
		getAllParameters();//from param Table
	}
	private static boolean getAllParameters() {
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String paramID;
		String value;
		try {
			conn = ServerHelper.instance().getConnection();
			String sql = "SELECT PARAMETER_ID, VALUE FROM PARAMETERS";
			preStm = conn.prepareStatement(sql);
			rs = preStm.executeQuery();
			while (rs.next()) {
				paramID = rs.getString("PARAMETER_ID").toUpperCase().trim();
				value=rs.getString("VALUE");
				if (paramID.equals("TIMESHEET"))
					timesheetURL = value;
				else if (paramID.equals("DASHBOARD"))
					dashboardURL = value;
				else if (paramID.equals("DMS"))
					dmsURL = value;
				else if (paramID.equals("NCMS"))
					ncmsURL = value;
				else if (paramID.equals("CALLLOG"))
					callLogURL = value;
				else if (paramID.equals("ITS"))
					itsURL = value;
				else if (paramID.equals("HELP"))
					helpURL = value;
				else if (paramID.equals("CONV_LAST_UPDATE")){
					if (value!=null);
						convLastUpd = CommonTools.parseSQLDate(value);
				}
				else if (paramID.equals("COMPL_RATE_LAST_UPDATE")){
					if (value!=null);
						complLastUpd = CommonTools.parseSQLDate(value);
				}
				else if (paramID.equals("VERSION")){
					if (value!=null) {
						FMS1version = value;
					}
					else{
						FMS1version = "1.0";
					}
				}
				else if (paramID.equals("NORMQACTIVITYEFFORT")){
					if (value!=null) {
						NormQActivityEffort = value;
					}
					else{
						NormQActivityEffort = "0.25";
					}
				}
				else if (paramID.equals("NORMPROCESSAUDITEFFORT")){
					if (value!=null) {
						NormProcessAuditEffort = value;
					}
					else{
						NormProcessAuditEffort = "0.5";
					}
				}
				else if (paramID.equals("NORMPROCESSCONTROLEFFORT")){
					if (value!=null) {
						NormProcessControlEffort = value;
					}
					else{
						NormProcessControlEffort = "2";
					}
				}
				else if (paramID.equals("LOGROOT")){
					logRoot = value;
				}
				else if (paramID.equals("LOCALHOST")){
					localhost = value;
				}
				else if (paramID.equals("CONNECTIONSTRING")){
					connectionString = value;
				}
				else if (paramID.equals("DAYOFVALIDATION")){
					dayOfValidation = CommonTools.parseSQLDate(value);
				}
				else
					System.err.println("Parameters.getAllParameters() : Unknown ParamID : " + paramID);
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}
	public static void updateParameter(String paramID, Date dateParam) {
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		try {
			if (paramID != null) {
				paramID = paramID.trim().toUpperCase();
				if (paramID.equals("CONV_LAST_UPDATE"))
					convLastUpd = dateParam;
				else {
					System.err.println("Parameters.updateParameter() : Bad ParamID");
					return;
				}
			}
			else {
				System.err.println("Parameters.getAllParameters() : ParamID is null");
				return;
			}
			conn = ServerHelper.instance().getConnection();
			String sql = "UPDATE PARAMETERS SET VALUE = ? WHERE PARAMETER_ID = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setDate(1, dateParam);
			preStm.setString(2, paramID); 
			if (preStm.executeUpdate()<1)
				System.err.println("Parameters.updateParameter() : ParamID is not found in database");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}
	/**
	 * Answer the singleton instance.
	 */
	/*public static Parameters instance() {
		if (_me == null) {
			_me = new Parameters();
		}
		return _me;
	}*/
}
