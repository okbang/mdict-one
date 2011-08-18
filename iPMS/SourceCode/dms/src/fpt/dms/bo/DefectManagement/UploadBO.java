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
 
 /*
 * Created on Jan 7, 2005
 */
package fpt.dms.bo.DefectManagement;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import fpt.dms.bean.UserInfoBean;
import fpt.dms.bean.DefectManagement.DefectAddBean;
import fpt.dms.bean.DefectManagement.DefectAttachBean;
import fpt.dms.bean.DefectManagement.DefectAttachDataBean;
import fpt.dms.bean.DefectManagement.DefectAttachTempBean;
import fpt.dms.bean.DefectManagement.DefectUpdateBean;
import fpt.dms.constant.DMS;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.CommonUtil.CommonUtil;

//import com.jspsmart.upload.*;

/**
 * Process attach files
 * @author trungtn
 * @since Jan 7, 2005
 */
public class UploadBO {
    private WSConnectionPooling conPool = new WSConnectionPooling();
    private static Logger logger = Logger.getLogger(QueryBO.class.getName());
    private DataSource ds = null;
    
    /**
     * Attach file from update defect
     * @param request
     * @param response
     * @param servlet
     * @return
     * @throws Exception
     * @throws Throwable
     */
    public DefectAttachBean attachUpdate(/*SmartUpload myUpload,*/
    							List files,
                                HttpServletRequest request,
                                HttpServlet servlet)
                                throws java.io.IOException {
        HttpSession session = request.getSession();
        DefectAttachBean beanDefectAttach =
            (DefectAttachBean) session.getAttribute("beanDefectAttach");
        int n_NewFiles = 0;
        int n_NewSize = 0;
        if (beanDefectAttach != null) {
            try {
                // Check available space
                //if (isAvailableUpload(myUpload, beanDefectAttach)) {
				if (isAvailableUpload(files, beanDefectAttach)) {
                    //com.jspsmart.upload.Files uploadFiles = myUpload.getFiles();
                    for (int i = 0; i < files.size()/*uploadFiles.getCount()*/; i++) {
                        DefectAttachTempBean attachTemp = saveTemp(/*uploadFiles.getFile(i)*/(FileItem)files.get(i), servlet);
                        if (attachTemp != null) {
                            beanDefectAttach.pushTempAttach(attachTemp);
                            n_NewFiles++;
                            n_NewSize += attachTemp.getSize();
                        }
                        if (n_NewFiles > 0) {
                            setAttachMsg(beanDefectAttach, session,
                                    DefectAttachBean.UP_STATUS_NORMAL);
                        }
                    }
                }
                else {
                    setAttachMsg(beanDefectAttach, session, DefectAttachBean.UP_STATUS_OVER);
                }
            }
            catch (Exception e) {
                setAttachMsg(beanDefectAttach, session, DefectAttachBean.UP_STATUS_FAILED);
            }
        }
        // Remember attachment informations
        session.setAttribute(DMS.UPLOAD_NEW_FILES, Integer.toString(n_NewFiles));
        session.setAttribute(DMS.UPLOAD_NEW_SIZE,
                CommonUtil.roundFormat(n_NewSize / (float) 1024, 2));
        
        return beanDefectAttach;
    }
    /**
     * Set attach message and upload attach status flag
     * @param beanDefectAttach
     * @param session
     * @param nStatus
     */
    private void setAttachMsg(DefectAttachBean beanDefectAttach,
                                    HttpSession session,
                                    int nStatus) {
        if (nStatus == DefectAttachBean.UP_STATUS_NORMAL) {
            beanDefectAttach.setLastStatus(DefectAttachBean.UP_STATUS_NORMAL);
            session.setAttribute(DMS.MSG_ATTACH_MESSAGE,
                    DMS.MSG_ATTACH_SUCCESSFUL);
        }
        else if (nStatus == DefectAttachBean.UP_STATUS_OVER) {
            beanDefectAttach.setLastStatus(DefectAttachBean.UP_STATUS_OVER);
            session.setAttribute(DMS.MSG_ATTACH_MESSAGE,
                    DMS.MSG_ATTACH_SPACE_OVER);
        }
        else if (nStatus == DefectAttachBean.UP_STATUS_FAILED) {
            beanDefectAttach.setLastStatus(DefectAttachBean.UP_STATUS_FAILED);
            session.setAttribute(DMS.MSG_ATTACH_MESSAGE,
                    DMS.MSG_ATTACH_FAILED);
        }
        else {
            session.removeAttribute(DMS.MSG_ATTACH_MESSAGE);
        }
    }
    
    /**
     * Check if new upload files is fit with available spaces
     * @param myUpload
     * @param beanDefectAttach
     * @return
     */
    private boolean isAvailableUpload(/*SmartUpload myUpload,*/
    					List files,
                        DefectAttachBean beanDefectAttach)
                        throws java.io.IOException {
        boolean bResult = true;
        int nFiles = files.size();
		/*for (int i = 0; i < myUpload.getFiles().getCount(); i++) {
		    if (! myUpload.getFiles().getFile(i).isMissing()) {
		        nFiles++;
		    }
		}*/
		// If new upload files NOT fit in current available space for attach size, attach files
		int totalSize = 0;
		for (int i=0; i<files.size(); i++)
			totalSize += ((FileItem)files.get(i)).getSize();
		
		return !(((beanDefectAttach.getCurrentSize() + totalSize) > DMS.MAX_ATTACH_SIZE) ||
		    ((beanDefectAttach.getFilesNumber() + nFiles) > DMS.MAX_UPLOAD_FILES));

    }
    
    private String getFileExt(String filename) {
		if (filename == null || filename.lastIndexOf('.') < 0) return "";
		return filename.substring(filename.lastIndexOf('.') + 1);
    }
    /**
     * Save attach file to upload temp directory with temporary name
     * @param myFile
     * @param servlet
     * @return
     */
    private DefectAttachTempBean saveTemp(/*com.jspsmart.upload.File myFile,*/
    									FileItem myFile,
                                        HttpServlet servlet)
                                        throws Exception {
        DefectAttachTempBean beanTempFile = null;
        try {
            //if (! myFile.isMissing()) {
                beanTempFile = new DefectAttachTempBean();
                beanTempFile.setFileName(myFile.getName() /*myFile.getFileName()*/);
                // Generate temporary file
                String strTempName =
                    String.valueOf(System.currentTimeMillis() + "_" +
                    Math.round(Math.random() * 1000000)) +
                    "." + getFileExt(myFile.getName())/*myFile.getFileExt()*/;
                // Locate and create temp directory if not existed
                String strUploadPath =
                    servlet.getServletContext().getRealPath(DMS.DIRECTORY_UPLOAD);
                java.io.File sysFile = new java.io.File(strUploadPath);
                if (!sysFile.exists()) {
                    sysFile.mkdir();
                }
                String strTempPath =
                    servlet.getServletContext().getRealPath(DMS.DIRECTORY_UPLOAD_TEMP);
                sysFile = new java.io.File(strTempPath);
                if (!sysFile.exists()) {
                    sysFile.mkdir();
                }
                //myFile.saveAs(strTempPath + strTempName, SmartUpload.SAVE_PHYSICAL);
				myFile.write(new File(strTempPath + strTempName));
			
                beanTempFile.setTempName(strTempName);
                beanTempFile.setSize((int)myFile.getSize());
            //}
        }
        catch (IOException  e) {
            logger.error(e);
            throw e;
        }
        catch (Exception e) {
            logger.error(e);
            throw e;
        }
        
        return beanTempFile;
    }
    
    /**
     * Save attach data posted from user
     * @param request
     * @param beanDefectUpdate
     * @param beanDefectAttach
     * @return
     * @throws SQLException
     */
    public int saveAttachUpdate(HttpServletRequest request,
                                DefectAddBean beanDefectAdd,
                                DefectAttachBean beanDefectAttach)
                                throws SQLException, NamingException,
                                IOException {
        int nResult = -1;
        Connection con = null;
        Statement stmt = null;
        UserInfoBean beanUserInfo =
            (UserInfoBean) request.getSession().getAttribute("beanUserInfo");
        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            Vector vtTemp = beanDefectAttach.getTempAttachList();
            Vector vtAttachData = new Vector(vtTemp.capacity());
            String strInsert = null;
            for (int i = 0; i < vtTemp.size(); i++) {
                DefectAttachTempBean attTemp = (DefectAttachTempBean) vtTemp.get(i);
                DefectAttachDataBean attData = new DefectAttachDataBean();
                long nAttachId = conPool.getNextSeq("attach_seq");  // get next
                strInsert = buildSqlInsertAttach(nAttachId,
                        beanDefectAdd.getDefectID(), attTemp, beanUserInfo);
                if (DMS.DEBUG)
                    logger.debug("saveAttachUpdate().strInsert=" + strInsert);
                stmt.addBatch(strInsert);
                attData.setAttachId(Long.toString(nAttachId));
                attData.setFileName(attTemp.getTempName());
                vtAttachData.add(attData);
            }
            stmt.executeBatch();
            con.commit();   // Must be committed before insert Blob
            int nSave = saveAttachFiles(vtAttachData, beanDefectAttach.getTempDirectory());
            if (nSave == -1) {  // Error in save database
                nResult = nSave;
                con.rollback();
            }
        }
        catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            logger.error(e);
        }
        catch (IOException e) {
            if (con != null) {
                con.rollback();
            }
            logger.error(e);
        }
        finally {
            conPool.releaseResourceAndCommit(con, stmt, null,
                                    "UploadBO.saveAttachUpdate() : ");
        }
        return nResult;
    }
    /**
     * Generate INSERT SQL statement for attach
     * @param nAttachId
     * @param attTemp
     * @param beanDefectUpdate
     * @param beanUserInfo
     * @return
     */
    private static String buildSqlInsertAttach(long nAttachId,
                        String strDefectId,
                        DefectAttachTempBean attTemp,
                        UserInfoBean beanUserInfo) {
        String strDeveloperId = beanUserInfo.getDeveloperId();
        StringBuffer sbInsert = new StringBuffer();
        sbInsert.append("INSERT INTO Defect_attachment");
        sbInsert.append("(attachment_id,defect_id,name,developer_id,content_size,content)");
        sbInsert.append("VALUES(");
        sbInsert.append(Long.toString(nAttachId)).append(",");
        sbInsert.append(strDefectId).append(",");
        sbInsert.append("'").append(attTemp.getFileName()).append("',");
        sbInsert.append(strDeveloperId).append(",");
        sbInsert.append(attTemp.getSize()).append(",");
        sbInsert.append("EMPTY_BLOB())");
        return sbInsert.toString();
    }
    /**
     * Save temporary attach files to database
     * @param vtAttachData
     * @throws SQLException
     */
    private int saveAttachFiles(Vector vtAttachData, String strPath)
                                throws SQLException, NamingException,
                                IOException {
        int nResult = -1;
        Connection con = null;
        PreparedStatement pStmtLock = null;
        PreparedStatement pStmtUpd = null;
        ResultSet rs = null;
        oracle.sql.BLOB oraBlob = null;
        String strLockRow = "SELECT content FROM Defect_attachment" +
                            " WHERE attachment_id=? FOR UPDATE";
        String strSetBlob = "UPDATE Defect_attachment SET content=?" +
                            " WHERE attachment_id=?";
        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            for (int i = 0; i < vtAttachData.size(); i++) {
                DefectAttachDataBean beanAtt =  
                        (DefectAttachDataBean) vtAttachData.get(i);
                pStmtLock = con.prepareStatement(strLockRow);   // Select and lock row
                pStmtLock.setString(1, beanAtt.getAttachId());
                rs = pStmtLock.executeQuery();
                if (rs.next()) {
                    oraBlob = (oracle.sql.BLOB) rs.getBlob(1);
                    // Get data from temp attach file
                    oraBlob.putBytes(1,
                        CommonUtil.getFileData(strPath + beanAtt.getFileName(),
                                            DMS.MAX_ATTACH_FILE_SIZE));
                    pStmtUpd = con.prepareStatement(strSetBlob);    // Update content
                    pStmtUpd.setBlob(1, oraBlob);
                    pStmtUpd.setString(2, beanAtt.getAttachId());
                    pStmtUpd.executeUpdate();
                    pStmtUpd.close();
                }
                rs.close();
                pStmtLock.close();
            }
            nResult = 1;
        }
        catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
        }
        catch (IOException e) {
            if (con != null) {
                con.rollback();
            }
        }
        finally {
            conPool.releaseResource(null, pStmtLock, null,
                                    "UploadBO.saveAttachUpdate() : ");
            conPool.releaseResourceAndCommit(con, pStmtUpd, null,
                                    "UploadBO.saveAttachUpdate() : ");
        }
        return nResult;
    }
    
    /**
     * Remove all temporary attach file references (information cached in session)
     * @param session
     */
    public static void clearTempAttach(HttpSession session) {
        DefectAttachBean beanDefectAttach =
                (DefectAttachBean) session.getAttribute("beanDefectAttach");
        if ((beanDefectAttach != null) && (beanDefectAttach.getTempAttachList().size() > 0)) {
            beanDefectAttach.discardTempAttach();
        }
        session.removeAttribute("beanDefectAttach");
    }
    
    /**
     * Show dowload dialog box for temporary attach file
     * @param request
     * @param response
     * @throws IOException
     */
    public static void showAttachFile(HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws IOException {
        DefectAttachBean beanDefectAttach =
                (DefectAttachBean) request.getSession().getAttribute("beanDefectAttach");
        String strIndex = request.getParameter("hidFileIndex");
        if ((beanDefectAttach != null) && (strIndex != null)) {
            DefectAttachTempBean beanAttachTemp =   
                    (DefectAttachTempBean)beanDefectAttach.getTempAttachList().get(
                            Integer.parseInt(strIndex));
            response.setContentType("application/download");
            response.setHeader("Content-Disposition","attachment;filename=\"" +
                    beanAttachTemp.getFileName() + "\"");
            byte[] fileData = CommonUtil.getFileData(
                    beanDefectAttach.getTempDirectory() +   
                    beanAttachTemp.getTempName(), DMS.MAX_ATTACH_FILE_SIZE);
            ServletOutputStream sos = response.getOutputStream();
            sos.write(fileData);
            sos.close();
        }
    }
    
    /**
     * Remove temporary attach file from system
     * @param request
     * @throws IOException
     */
    public static void removeAttachFile(HttpServletRequest request)
                                    throws IOException {
        DefectAttachBean beanDefectAttach =
                (DefectAttachBean) request.getSession().getAttribute("beanDefectAttach");
        String strIndex = request.getParameter("hidFileIndex");
        if ((beanDefectAttach != null) && (strIndex != null)) {
            beanDefectAttach.removeTempAttach(Integer.parseInt(strIndex));
        }
    }
    
    /**
     * Request attachment information
     * @param request
     * @param beanDefectUpdate
     * @return
     * @throws SQLException
     */
    public DefectAttachBean getAttachData(HttpServletRequest request,
                                        DefectUpdateBean beanDefectUpdate,
                                        HttpServlet servlet)
                                        throws SQLException, NamingException {
        HttpSession session = request.getSession();
        DefectAttachBean beanDefectAttach =
                (DefectAttachBean) session.getAttribute("beanDefectAttach");
        if (beanDefectAttach == null) {
            // Get directory for temporary upload files
            String strDirectory = servlet.getServletContext().getRealPath(
                                    DMS.DIRECTORY_UPLOAD_TEMP);
            beanDefectAttach = new DefectAttachBean(strDirectory);
            beanDefectAttach.setModeUpdate();
            getAttachPermanent(beanDefectUpdate, beanDefectAttach);
            session.setAttribute("beanDefectAttach", beanDefectAttach);
        }
        
        return beanDefectAttach;
    }
    
    /**
     * Get attachment information from database
     * @param beanDefectUpdate
     * @param beanDefectAttach
     * @return
     * @throws SQLException
     */
    private int getAttachPermanent(DefectUpdateBean beanDefectUpdate,
                                DefectAttachBean beanDefectAttach)
                                throws SQLException, NamingException {
        int nResult = -1;
        String strSelect = "SELECT attachment_id,name,developer_id,content_size" +
                           " FROM Defect_attachment WHERE defect_id=?";
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            pStmt = con.prepareStatement(strSelect);
            pStmt.setString(1, beanDefectUpdate.getDefectID());
            rs = pStmt.executeQuery();
            nResult = 0;
            while (rs.next()) {
                DefectAttachDataBean beanData = new DefectAttachDataBean();
                beanData.setAttachId(rs.getString("attachment_id"));
                beanData.setFileName(rs.getString("name"));
                beanData.setDeveloperId(rs.getString("developer_id"));
                beanData.setSize(rs.getInt("content_size"));
                beanDefectAttach.pushAttachData(beanData);
                nResult++;
            }
        }
        catch (SQLException e) {
            throw e;
        }
        catch (NamingException e) {
            throw e;
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "UploadBO.getAttachPermanent() : ");
        }
        return nResult;
    }

    /**
     * Show dowload dialog box for permanent attach file (Blob in database)
     * @param request
     * @param response
     * @throws IOException
     */
    public void showAttachDb(HttpServletRequest request,
                            HttpServletResponse response)
                            throws IOException, SQLException, NamingException {
        DefectAttachBean beanDefectAttach =
                (DefectAttachBean) request.getSession().getAttribute("beanDefectAttach");
        String strIndex = request.getParameter("hidDataIndex");
        if ((beanDefectAttach != null) && (strIndex != null)) {
            DefectAttachDataBean beanAttachData =   
                    (DefectAttachDataBean)beanDefectAttach.getDbAttachList().get(
                            Integer.parseInt(strIndex));
            response.setContentType("application/download");
            response.setHeader("Content-Disposition","attachment;filename=\"" +
                                beanAttachData.getFileName() + "\"");
            byte[] fileData = getAttachBlob(beanAttachData.getAttachId());
            ServletOutputStream sos = response.getOutputStream();
            sos.write(fileData);
            sos.close();
        }
    }
    
    /**
     * Get attachment Blob as byte[] array 
     * @param strAttachId
     * @return
     * @throws SQLException
     * @throws NamingException
     */
    private byte[] getAttachBlob(String strAttachId)
                throws SQLException, NamingException {
        byte[] arrData = null;
        String strSelect = "SELECT content FROM Defect_attachment" +
                           " WHERE attachment_id=?";
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            pStmt = con.prepareStatement(strSelect);
            pStmt.setString(1, strAttachId);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                java.sql.Blob blob = rs.getBlob(1);
                // Blob data begining from position 1
                arrData = blob.getBytes((long) 1, (int) blob.length());
            }
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "UploadBO.getAttachBlob() : ");
        }
        return arrData;
    }

    /**
     * Request remove permanent attach information from system
     * @param request
     * @throws IOException
     */
    public static void removeAttachData(HttpServletRequest request)
                                    throws SQLException {
        DefectAttachBean beanDefectAttach =
                (DefectAttachBean) request.getSession().getAttribute("beanDefectAttach");
        String strIndex = request.getParameter("hidDataIndex");
        if ((beanDefectAttach != null) && (strIndex != null)) {
            beanDefectAttach.removeAttachData(Integer.parseInt(strIndex));
        }
    }
    
    /**
     * Remove permanent attach from database
     * @param request
     * @throws IOException
     */
    public int removeAttachDb(String strAttachId)
                            throws SQLException, NamingException {
        int nResult = -1;
        String strSelect = "DELETE Defect_attachment WHERE attachment_id=?";
        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            pStmt = con.prepareStatement(strSelect);
            pStmt.setString(1, strAttachId);
            nResult = pStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException e) {
            con.rollback();
            throw e;
        }
        catch (NamingException e) {
            throw e;
        }
        finally {
            conPool.releaseResource(con, pStmt, null, "UploadBO.removeAttachDb() : ");
        }
        return nResult;
    }
}
