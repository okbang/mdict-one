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
 
 package fpt.dms.bo.SetupEnvironment;

/**
 * @Title:        SeverityBO.java
 * @Description:  
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 30, 2002
 * @Modified date:
 */

import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeverityBO
{
	private WSConnectionPooling conPool = new WSConnectionPooling();
	//private Connection con = null;
	
	private static Logger logger = Logger.getLogger(SeverityBO.class.getName());
	
	public SeverityBO()
	{
	}
	
	/**
	 * Get Severity list
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public StringMatrix getSeverityList() throws SQLException, Exception
	{
		StringMatrix smResult = new StringMatrix();
        Connection con = null;		
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			strSQL.append("SELECT * FROM Defect_Severity ORDER BY DEFS_ID");
   
            ds = conPool.getDS();
			con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
			StringVector tmpVector= new StringVector(2);
			while (rs.next())
			{
				tmpVector.setCell(0,rs.getString("DEFS_ID"));
				tmpVector.setCell(1,CommonUtil.correctHTMLError(rs.getString("Name")));
				smResult.addRow(tmpVector);
			}

			rs.close();
            prepStmt.close();
		}
		catch (SQLException ex)
		{
			logger.error("SeverityBO.getSeverityList(): ", ex);
		}
		catch(Exception e)
		{
			logger.error("SeverityBO.getSeverityList(): ", e);
		}
		finally
		{
            conPool.releaseResource(con, prepStmt, rs,
                            "SeverityBO.getSeverityList(): ");
      	}
		
		return smResult;
	}
	
	/**
	 * Add a new Severity
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int addSeverity(StringVector svSeverity) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;        
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			String strID = svSeverity.getCell(0);
            String strName = CommonUtil.stringConvert(svSeverity.getCell(1).trim());
            
            strSQL.append("INSERT INTO Defect_Severity(DEFS_ID, Name) VALUES("+strID+",'" + strName + "')");
            ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
            prepStmt = con.prepareStatement(strSQL.toString());
            prepStmt.executeQuery();
			prepStmt.close();
			con.commit();
		}
		catch (SQLException ex)
		{
			con.rollback();
			logger.error("SeverityBO.addSeverity(): ", ex);
		}
		catch(Exception e)
		{
			con.rollback();
			logger.error("SeverityBO.addSeverity(): ", e);
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "SeverityBO.addSeverity(): ");
      	}
		
		return nResult;
	}
	
	/**
	 * Delete a Severity
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int deleteSeverity(String[] strSeverity) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;        
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			int nCount = strSeverity.length;
            String strIDList = "", strValue = "";
            for (int i= 0; i< nCount; i++)
            {
                strValue = strValue +"'" + strSeverity[i] + "'" + ",";
            }
            if (strValue.length() >0 )
              strIDList = strValue.substring(0, strValue.length() -1);
              
            strSQL.append("DELETE FROM Defect_Severity WHERE DEFS_ID IN ("+strIDList+")");
            ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
            prepStmt = con.prepareStatement(strSQL.toString());
            prepStmt.executeQuery();
			prepStmt.close();
			con.commit();
		}
		catch (SQLException ex)
		{
			con.rollback();
			logger.error("SeverityBO.deleteSeverity(): ", ex);
		}
		catch(Exception e)
		{
			con.rollback();
			logger.error("SeverityBO.deleteSeverity(): ", e);
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "SeverityBO.deleteSeverity(): ");
      	}
		
		return nResult;
	}
	
	/**
	 * Update a Severity
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int updateSeverity(StringVector svSeverity) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;        
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			String strID = svSeverity.getCell(0);
            String strName = svSeverity.getCell(1);
              
            strSQL.append("UPDATE Defect_Severity SET Name = '" + strName + "' WHERE DEFS_ID IN (" + strID + ")");
            ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
            prepStmt = con.prepareStatement(strSQL.toString());
            prepStmt.executeQuery();
			prepStmt.close();
			con.commit();
		}
		catch (SQLException ex)
		{
			con.rollback();
			logger.error("SeverityBO.updateSeverity(): ", ex);
		}
		catch(Exception e)
		{
			con.rollback();
			logger.error("SeverityBO.updateSeverity(): ", e);
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "SeverityBO.updateSeverity(): ");
      	}
		
		return nResult;
	}
}