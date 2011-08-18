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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.fms1.infoclass.*;
import com.fms1.web.*;
/**
 * Page list logic (the page list is used for role administration)
 * @author NgaHT
 */
final class Page {

	/**
	 * return List of Page
	 * param:none
	 */
	public static final Vector getPageVector() {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		final Vector vt = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM PAGE ORDER BY ORDR";
			final ResultSet rs = stm.executeQuery(sql);
			if (rs == null)
				return null;
			while (rs.next()) {
				final PageInfor pi = new PageInfor();
				pi.pageID = rs.getLong("PAGEID");
				pi.name = rs.getString("PAGENAME");
				pi.ordr = rs.getInt("ORDR");
				pi.type=rs.getInt("TYPE");
				vt.add(pi);
			}
			return vt;
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + sql);
		}
		finally {
			try {
				if (stm != null)
					stm.close();
			}
			catch (SQLException e2) {
				System.err.println(e2.toString());
			}
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e2) {
				System.err.println(e2.toString());
			}
		}
		return null;
	}
}