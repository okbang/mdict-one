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
 * Created on Jan 5, 2005
 *
 */
package fpt.dms.bean.DefectManagement;

import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.bo.DefectManagement.UploadBO;
import fpt.dms.constant.DMS;

/**
 * @author trungtn
 */
public class DefectAttachBean implements HttpSessionBindingListener {
    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(DefectAttachBean.class);
    public static int UP_STATUS_NORMAL = 0;
    public static int UP_STATUS_FAILED = 1;
    public static int UP_STATUS_OVER = 2;
    private int n_LastStatus = 0;    // Last upload status : 0-success, 1-failed, 2-overflow
    
    public static int MODE_ADD_NEW = 0;
    public static int MODE_UPDATE = 1;
    private int n_AttachMode = -1;      // Attach from add new (0) or update(1)
    private String strTempDirectory;

    private Vector vt_DbAttachList = new Vector(); // Contain list of attach stored in database (DefectAttachDataBean)
    private Vector vt_TempAttachList = new Vector(); // Contain list of temporary attach file names (DefectAttachTempBean)
    private int n_FilesNumber;      // Number of current attached files
    private int n_CurrentSize;      // Current attach size total (in bytes) 
    
    public DefectAttachBean(String strDirectory) {
        strTempDirectory = strDirectory;
    }
    
    /**
     * Identify for attach from Add new defect
     */
    public void setModeAdd() {
        n_AttachMode = MODE_ADD_NEW;
    }
    
    /**
     * Identify for attach from Update defect
     */
    public void setModeUpdate() {
        n_AttachMode = MODE_UPDATE;
    }
    
    /**
     * Push new attach data to list
     * @param attach
     */
    public void pushAttachData(DefectAttachDataBean attach) {
        vt_DbAttachList.add(attach);
        n_FilesNumber++;
        n_CurrentSize += attach.getSize();
    }
    
    /**
     * Remove an attach data from list
     * @param strAttachId
     */
    public void removeAttachData(String strAttachId) {
        DefectAttachDataBean attach;
        for (int i = 0; i < vt_DbAttachList.size(); i++) {
            attach = (DefectAttachDataBean) vt_DbAttachList.get(i);
            if (attach.getAttachId().equals(strAttachId)) {
                removeAttachData(i);
                break;
            }
        }
    }
    
    /**
     * Remove an attach data from list
     * @param strAttachId
     */
    public void removeAttachData(int nIndex) {
        DefectAttachDataBean beanAttachData =
                (DefectAttachDataBean) vt_DbAttachList.get(nIndex);
        try {
            // Remove attachment from database
            UploadBO boUpload = new UploadBO();
            boUpload.removeAttachDb(beanAttachData.getAttachId());
            int nSize =  beanAttachData.getSize();
            vt_DbAttachList.remove(nIndex);
            n_FilesNumber--;
            n_CurrentSize -= nSize;
        }
        catch (SQLException e) {
        }
        catch (NamingException e) {
        }
    }
    
    /**
     * Push new temporary file to current temporary list
     * @param strFileName
     */
    public void pushTempAttach(DefectAttachTempBean attachTemp) {
        vt_TempAttachList.add(attachTemp);
        n_FilesNumber++;
        n_CurrentSize += attachTemp.getSize();
    }
    
    /**
     * Remove temporary attach file from temporary list
     * @param strRemoveName
     */
    public void removeTempAttach(String strRemoveName) {
        DefectAttachTempBean attachTemp;
        int nSize;
        for (int i = 0; i < vt_TempAttachList.size(); i++) {
            attachTemp = (DefectAttachTempBean) vt_TempAttachList.get(i);
            nSize = attachTemp.getSize();
            // Remove system file and file name from temporary list
            // Reduce attachment count, attachment size
            if (attachTemp.getTempName().equals(strRemoveName)) {
                CommonUtil.deleteFile(strTempDirectory + strRemoveName);
                vt_TempAttachList.remove(i);
                n_FilesNumber--;
                n_CurrentSize -= nSize;
                break;
            }
        }
    }
    
    /**
     * Remove temporary attach file (index) from temporary list
     * @param strRemoveName
     */
    public void removeTempAttach(int nIndex) {
        // Remove system file and file name from temporary list
        // Reduce attachment count, attachment size
        DefectAttachTempBean attachTemp = (DefectAttachTempBean) vt_TempAttachList.get(nIndex);
        CommonUtil.deleteFile(strTempDirectory + attachTemp.getTempName());
        int nSize = attachTemp.getSize();
        vt_TempAttachList.remove(nIndex);
        n_FilesNumber--;
        n_CurrentSize -= nSize;
    }
    
    /**
     * Remove all temporary attach files from temporary list (when user does not save)
     * @param nIndex
     */
    public synchronized void discardTempAttach() {
        // Remove system file and file name from temporary list
        // Reduce attachment count, attachment size
        for (int nIndex = vt_TempAttachList.size() - 1; nIndex >= 0; nIndex--) {
            DefectAttachTempBean attachTemp = (DefectAttachTempBean) vt_TempAttachList.get(nIndex);
            CommonUtil.deleteFile(strTempDirectory + attachTemp.getTempName());
            n_FilesNumber--;
            n_CurrentSize -= attachTemp.getSize();
            vt_TempAttachList.remove(nIndex);
        }
    }
    
    /**
     * @return
     */
    public int getAvailableSize() {
        return DMS.MAX_ATTACH_SIZE - n_CurrentSize;
    }

    /**
     * @return
     */
    public int getCurrentSize() {
        return n_CurrentSize;
    }

    /**
     * @return
     */
    public int getFilesNumber() {
        return n_FilesNumber;
    }

    /**
     * @param f
     */
    public void setCurrentSize(int i) {
        n_CurrentSize = i;
    }

    /**
     * @param i
     */
    public void setFilesNumber(int i) {
        n_FilesNumber = i;
    }

    /*
     * @param bean
    public void setUpdateBean(DefectUpdateBean bean) {
        updateBean = bean;
    }
     */
    /**
     * @return
     */
    public int getAttachMode() {
        return n_AttachMode;
    }

    /**
     * @param i
     */
    public void setAttachMode(int i) {
        n_AttachMode = i;
    }

    /**
     * @return
     */
    public int getLastStatus() {
        return n_LastStatus;
    }

    /**
     * @param i
     */
    public void setLastStatus(int i) {
        n_LastStatus = i;
    }
    
    /**
     * Write link to show popup window for permanent attach file (in DB).<BR>
     * Html page must implement javascript popup() and remove() functions
     * to view attach file.
     * @param attachTemp
     * @return
     */
    private static String getDataLink(DefectAttachDataBean attachData, int nIndex) {
        String strHtml = "";
        // Popup link
        strHtml += "<A href=\"javascript:showDbFile(" + nIndex + ")\">";
        strHtml += attachData.getFileName() + "</A>&nbsp;(";
        strHtml += CommonUtil.roundFormat(attachData.getSize() / (float) 1024, 2) + "Kb)";
        // Remove link
        strHtml += "&nbsp;<A href=\"javascript:removeDbFile(";
        strHtml += nIndex + ")\">Remove</A><BR>\n";
        return strHtml;
    }
    /**
     * Write link to show popup window for temporary attach file.<BR>
     * Html page must implement javascript popup() and remove() functions
     * to view attach file.
     * @param attachTemp
     * @return
     */
    private static String getTempLink(DefectAttachTempBean attachTemp, int nIndex) {
        String strHtml = "";
        // Popup link
        strHtml += "<A href=\"javascript:showTempFile(" + nIndex + ")\">";
        strHtml += attachTemp.getFileName() + ".!!!</A>&nbsp;(";
        strHtml += CommonUtil.roundFormat(attachTemp.getSize() / (float) 1024, 2) + "Kb)";
        // Remove link
        strHtml += "&nbsp;<A href=\"javascript:removeTempFile(";
        strHtml += nIndex + ")\">Remove</A><BR>\n";
        return strHtml;
    }
    /**
     * Write out Html links for attachments (temp attach file and database attach)
     */
    public String writeHtmlLink() {
        DefectAttachDataBean attachData;
        DefectAttachTempBean attachTemp;
        String strDataLink = "";
        String strTempLink = "";
        for (int i = 0; i < vt_DbAttachList.size(); i++) {
            // Write database attachment links
            attachData = (DefectAttachDataBean) vt_DbAttachList.get(i);
            strDataLink += getDataLink(attachData, i);
        }
        for (int i = 0; i < vt_TempAttachList.size(); i++) {
            // Write temporary file attachment links
            attachTemp = (DefectAttachTempBean) vt_TempAttachList.get(i);
            strTempLink += getTempLink(attachTemp, i);
        }
        return strDataLink + strTempLink;
    }

    /**
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0) {
    }

    /**
     * Delete temporary attach files when session is going to invalidated
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        if (vt_TempAttachList.size() > 0) {
            discardTempAttach();
            logger.info("Attach files are being deleted due to unbounding...");
        }
    }
    /**
     * @return
     */
    public String getTempDirectory() {
        return strTempDirectory;
    }

    /**
     * @param string
     */
    public void setTempDirectory(String string) {
        strTempDirectory = string;
    }

    /**
     * @return
     */
    public Vector getDbAttachList() {
        return vt_DbAttachList;
    }

    /**
     * @return
     */
    public Vector getTempAttachList() {
        return vt_TempAttachList;
    }

    /**
     * @param vector
     */
    public void setDbAttachList(Vector vector) {
        vt_DbAttachList = vector;
    }

    /**
     * @param vector
     */
    public void setTempAttachList(Vector vector) {
        vt_TempAttachList = vector;
    }
}
