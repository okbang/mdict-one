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
 * @Title:        TypeOfWorkBO.java
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

public class TypeOfWorkBO
{
	private WSConnectionPooling conPool = new WSConnectionPooling();
	//private Connection con = null;
	
	private static Logger logger = Logger.getLogger(TypeOfWorkBO.class.getName());
	
	public TypeOfWorkBO()
	{
	}
	
	/**
	 * Get TypeOfWork list
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public StringMatrix getTypeOfWorkList() throws SQLException, Exception
	{
		StringMatrix smResult = new StringMatrix();
        Connection con = null;		
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			strSQL.append("SELECT * FROM TypeOfWork ORDER BY TOW_ID");
   
            ds = conPool.getDS();
			con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
			StringVector tmpVector= new StringVector(2);
			while (rs.next())
			{
				tmpVector.setCell(0,rs.getString("TOW_ID"));
				tmpVector.setCell(1,CommonUtil.correctHTMLError(rs.getString("Name")));
				smResult.addRow(tmpVector);
			}

			rs.close();
            prepStmt.close();
		}
		catch (SQLException ex)
		{
			logger.error("TypeOfWorkBO.getTypeOfWorkList(): ", ex);
		}
		catch(Exception e)
		{
			logger.error("TypeOfWorkBO.getTypeOfWorkList(): ", e);
		}
		finally
		{
            conPool.releaseResource(con, prepStmt, rs,
                            "TypeOfWorkBO.getTypeOfWorkList(): ");
      	}
		
		return smResult;
	}
	
	/**
	 * Add a new TypeOfWork
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int addTypeOfWork(StringVector svTypeOfWork) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;        
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			String strID = svTypeOfWork.getCell(0);
            String strName = CommonUtil.stringConvert(svTypeOfWork.getCell(1).trim());
            
            strSQL.append("INSERT INTO TypeOfWork(TOW_ID, Name) VALUES("+strID+",'" + strName + "')");
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
			logger.error("TypeOfWorkBO.addTypeOfWork(): ", ex);
		}
		catch(Exception e)
		{
			con.rollback();
			logger.error("TypeOfWorkBO.addTypeOfWork(): ", e);
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "TypeOfWorkBO.addTypeOfWork(): ");
      	}
		
		return nResult;
	}
	
	/**
	 * Delete a TypeOfWork
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int deleteTypeOfWork(String[] strTypeOfWork) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;        
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			int nCount = strTypeOfWork.length;
            String strIDList = "", strValue = "";
            for (int i= 0; i< nCount; i++)
            {
                strValue = strValue +"'" + strTypeOfWork[i] + "'" + ",";
            }
            if (strValue.length() >0 )
              strIDList = strValue.substring(0, strValue.length() -1);
              
            strSQL.append("DELETE FROM TypeOfWork WHERE TOW_ID IN ("+strIDList+")");
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
			logger.error("TypeOfWorkBO.deleteTypeOfWork(): ", ex);
		}
		catch(Exception e)
		{
			con.rollback();
			logger.error("TypeOfWorkBO.deleteTypeOfWork(): ", e);
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "TypeOfWorkBO.deleteTypeOfWork(): ");
      	}
		
		return nResult;
	}
	
	/**
	 * Update a TypeOfWork
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @exception   Exception    If an exception occurred.
	 */	
	public int updateTypeOfWork(StringVector svTypeOfWork) throws SQLException, Exception
	{
		int nResult = -1;
		DataSource ds = null;
        Connection con = null;        
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try
		{
			String strID = svTypeOfWork.getCell(0);
            String strName = svTypeOfWork.getCell(1);
              
            strSQL.append("UPDATE TypeOfWork SET Name = '" + strName + "' WHERE TOW_ID IN (" + strID + ")");
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
			logger.error("TypeOfWorkBO.updateTypeOfWork(): ", ex);
		}
		catch(Exception e)
		{
			con.rollback();
			logger.error("TypeOfWorkBO.updateTypeOfWork(): ", e);
		}
		finally
		{
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "TypeOfWorkBO.updateTypeOfWork(): ");
      	}
		
		return nResult;
	}
}