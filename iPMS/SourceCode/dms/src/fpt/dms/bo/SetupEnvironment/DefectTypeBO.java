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
 * @Title:        DefectTypeBO.java
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

public class DefectTypeBO
{
	private WSConnectionPooling conPool = new WSConnectionPooling();
	//private Connection con = null;
	
	private static Logger logger = Logger.getLogger(DefectTypeBO.class.getName());
	
	public DefectTypeBO()
	{
	}
	
	/**
	 * Get DefectType list
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public StringMatrix getDefectTypeList() throws SQLException, Exception
	{
		StringMatrix smResult = new StringMatrix();
        Connection con = null;		
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			strSQL.append("SELECT * FROM Defect_Type ORDER BY DT_ID");
   
            ds = conPool.getDS();
			con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
			StringVector tmpVector= new StringVector(2);
			while (rs.next())
			{
				tmpVector.setCell(0,rs.getString("DT_ID"));
				tmpVector.setCell(1,CommonUtil.correctHTMLError(rs.getString("Name")));
				smResult.addRow(tmpVector);
			}

			rs.close();
            prepStmt.close();
		}
		catch (SQLException ex)
		{
			logger.debug("SQLException occurs in DefectTypeBO.getDefectTypeList(): " + ex.getMessage());
		}
		catch(Exception e)
		{
			logger.debug("Exception occurs in DefectTypeBO.getDefectTypeList(): " + e.getMessage());
		}
		finally
		{
            conPool.releaseResource(con, prepStmt, rs,
                            "DefectTypeBO.getDefectTypeList(): ");
      	}
		
		return smResult;
	}
	
	/**
	 * Add a new DefectType
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int addDefectType(StringVector svDefectType) throws SQLException, Exception
	{
		int nResult = -1;
        Connection con = null;		
        DataSource ds = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			String strID = svDefectType.getCell(0);
            String strName = CommonUtil.stringConvert(svDefectType.getCell(1).trim());
            
            strSQL.append("INSERT INTO Defect_Type(DT_ID, Name) VALUES("+strID+",'" + strName + "')");
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
			logger.debug("SQLException occurs in DefectTypeBO.addDefectType(): " + ex.getMessage());
		}
		catch(Exception e)
		{
			con.rollback();
			logger.debug("Exception occurs in DefectTypeBO.addDefectType(): " + e.getMessage());
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "DefectTypeBO.addDefectType(): ");
      	}
		
		return nResult;
	}
	
	/**
	 * Delete a DefectType
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int deleteDefectType(String[] strDefectType) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			int nCount = strDefectType.length;
            String strIDList = "", strValue = "";
            for (int i= 0; i< nCount; i++)
            {
                strValue = strValue +"'" + strDefectType[i] + "'" + ",";
            }
            if (strValue.length() >0 )
              strIDList = strValue.substring(0, strValue.length() -1);
              
            strSQL.append("DELETE FROM Defect_Type WHERE DT_ID IN ("+strIDList+")");
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
			logger.debug("SQLException occurs in DefectTypeBO.deleteDefectType(): " + ex.getMessage());
		}
		catch(Exception e)
		{
			con.rollback();
			logger.debug("Exception occurs in DefectTypeBO.deleteDefectType(): " + e.getMessage());
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "DefectTypeBO.deleteDefectType(): ");
      	}
		
		return nResult;
	}
	
	/**
	 * Update a DefectType
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int updateDefectType(StringVector svDefectType) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			String strID = svDefectType.getCell(0);
            String strName = svDefectType.getCell(1);
              
            strSQL.append("UPDATE Defect_Type SET Name = '" + strName + "' WHERE DT_ID IN (" + strID + ")");
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
			logger.debug("SQLException occurs in DefectTypeBO.updateDefectType(): " + ex.getMessage());
		}
		catch(Exception e)
		{
			con.rollback();
			logger.debug("Exception occurs in DefectTypeBO.updateDefectType(): " + e.getMessage());
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "DefectTypeBO.updateDefectType(): ");
      	}
		
		return nResult;
	}
}