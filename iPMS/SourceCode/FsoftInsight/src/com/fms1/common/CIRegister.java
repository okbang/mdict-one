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
import com.fms1.infoclass.ModuleInfo;
import com.fms1.web.*;
/**
 * CI register logic
 * @author manu
 * @date 14 Jan 2003
 */
public final class CIRegister {
	public static final Vector GetCIRegister(final long projectid) {
		Vector resultVector = new Vector();
		try {
			Vector moduleList = WorkProduct.getModuleListSchedule(projectid, WorkProduct.ORDER_BY_PRELEASE);
			while (moduleList.size() > 0) {
				ModuleInfo moduleInfoBackup = null;
				ModuleInfo moduleInfo = null;
				for (int i = 0; i < moduleList.size(); i++) {
					moduleInfo = (ModuleInfo) moduleList.elementAt(i);
					//remove the modules without planned release date
					if (moduleInfo.thePlanReleaseDate == null) {
						moduleList.removeElementAt(i);
						//the index and size of function have changed so: 
						if (i < (moduleList.size() - 1))
							i--;
					}
					else if (moduleInfoBackup == null) { //first iteration
						moduleInfoBackup = moduleInfo;
					}
					else if (moduleInfoBackup.thePlanReleaseDate.compareTo(moduleInfo.thePlanReleaseDate) > 0)
						moduleInfoBackup = moduleInfo;
				}
				if (moduleInfoBackup != null) {
					final ModuleInfo moduleInfoToAdd = moduleInfoBackup;
					resultVector.add(moduleInfoToAdd);
					moduleList.removeElement(moduleInfoBackup);
				}
			}
			return resultVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return resultVector;
		}
	}
	public static final void SetCIRegister(final long projectid, final javax.servlet.http.HttpServletRequest request) {
		Connection conn = null;
		PreparedStatement sql = null;
		try {
			final int nRows = Integer.parseInt((String) request.getParameter("nRows"));
			String strBaseline;
			String strBaselineNote;
			String strModuleName;
			String strRequest;
			String strStatus;
			//**get stage list 
			conn = ServerHelper.instance().getConnection();
			strRequest =
				"UPDATE MODULE SET BASELINE= ?,"
					+ " BASELINE_NOTE= ?,"
					+ " BASELINE_STATUS=?"
					+ " WHERE MODULE.PROJECT_ID=? AND MODULE.NAME=?";
			sql = conn.prepareStatement(strRequest);
			for (int i = 0; i < nRows; i++) {
				strBaseline = (String) request.getParameter("baseline" + i);
				strBaselineNote = (String) request.getParameter("baseline_note" + i);
				strModuleName = (String) request.getParameter("moduleName" + i);
				strStatus = (String) request.getParameter("GROUPRADIO" + i);

				sql.setString(1, strBaseline);
				sql.setString(2, strBaselineNote);
				sql.setString(3, strStatus);
				sql.setLong(4, projectid);
				sql.setString(5, strModuleName);
				sql.execute();
			}
			//get the number of rows in order to size the array
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, sql, null);
		}
	}
}