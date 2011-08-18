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

 package fpt.timesheet.bo.Approval;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

//import com.jspsmart.upload.SmartUpload;

import fpt.timesheet.InputTran.ejb.DateUtils;
//import fpt.timesheet.InputTran.ejb.Input;
//import fpt.timesheet.InputTran.ejb.InputHome;
import fpt.timesheet.InputTran.ejb.InputEJBLocal;
import fpt.timesheet.InputTran.ejb.InputEJBLocalHome;
import fpt.timesheet.InputTran.ejb.TimeSheetInfo;
import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.bean.Approval.ChangePasswordBean;
import fpt.timesheet.bean.Approval.TSAddBean;
import fpt.timesheet.bean.Approval.TSListBean;
import fpt.timesheet.bean.Approval.TSUpdateBean;
import fpt.timesheet.bean.Report.InquiryReportBean;
import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.bo.ComboBox.ProjectComboBO;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.util.CommonUtil.CommonUtil;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;

public class TimesheetBO {
    private InputEJBLocalHome homeInput = null;
    private InputEJBLocal objInput = null;

    private static Logger logger = Logger.getLogger(TimesheetBO.class.getName());

    /**
     * @see java.lang.Object#Object()
     */
    public TimesheetBO() {
    }

    /**
     * Method getInputEJB.
     * @throws NamingException
     */
    private void getInputEJB() throws NamingException {
        try {
            if (homeInput == null) {
                Context ic = new InitialContext();
                java.lang.Object objref = ic.lookup(JNDI.INPUT);

//              homeInput = (InputHome) javax.rmi.PortableRemoteObject.narrow(objref, InputHome.class);
				homeInput = (InputEJBLocalHome)(objref);
                if (objInput == null)
                    objInput = homeInput.create();
            }
        }
        catch (NamingException ex) {
            logger.debug("TimesheetBO.getInputEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
            throw ex;
        }
        catch (Exception ex) {
            logger.debug("TimesheetBO.getInputEJB() error! -- " + ex.getMessage());
            ex.printStackTrace();
        }
    } //getLoginEJB

    /**
     * Get all user timesheet.
     * @author  Nguyen Thai Son.
     * @version  November 19, 2002.
     * @param   beanTSList TSListBean: input data
     * @param	beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public TSListBean getTimesheetList(UserInfoBean beanUserInfo, TSListBean beanTSList) throws Exception {
        try {
            getInputEJB();

            Collection arrListing = null;
            if (beanTSList.getSelectedTS() != null && beanTSList.getSelectedTS().length() > 0) {
                // Get List for Update..
                arrListing = objInput.getTimeSheetList(beanTSList.getSelectedTS());
            }
            else {
                // Get List for View..
                int nUserID = beanUserInfo.getUserId();
                int nProjectID = beanTSList.getSearchProjectID();
                int nStatus = beanTSList.getSearchStatus();
                String strFromDate = beanTSList.getSearchFromDate();
                String strToDate = beanTSList.getSearchToDate();
                int nSortBy = beanTSList.getSearchSortBy();
                int nCurrentPage = beanTSList.getCurrentPage();

                arrListing = objInput.getTimeSheetList(nUserID, nProjectID, nStatus, strFromDate, strToDate, nSortBy, nCurrentPage);

                int nTotalPage = objInput.getTotalPage();
                int nTotalTimesheet = objInput.getTotalTimesheet();
                beanTSList.setRejectedTimesheets(objInput.getRejectedTimesheets(nUserID, nProjectID, strFromDate, strToDate));
                beanTSList.setTotalPage(nTotalPage);
                beanTSList.setTotalTimesheet(nTotalTimesheet);
                //added by MinhPT 03Oct13
                //for check nCurrentPage > (nTotalPage-1)
                if (nCurrentPage > (nTotalPage - 1))
                    beanTSList.setCurrentPage(0);
                else
                    beanTSList.setCurrentPage(nCurrentPage);
            }

            beanTSList.setTimesheetList(arrListing);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in TimesheetBO.getTimesheetList(): " + ex.toString());
            logger.error(ex);
        }

        return beanTSList;
    }

    /**
     * Insert new timesheet into database
     * @author  Nguyen Thai Son.
     * @version  November 19, 2002.
     * @param   beanTSList TSListBean: input data
     * @param	beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public int addTimesheet(TSAddBean beanTSAdd, UserInfoBean beanUserInfo) throws Exception {
        int nResult = -1;
        if (beanTSAdd == null)
            return nResult;

        try {
            getInputEJB();

            int nUserID = beanUserInfo.getUserId();
            Collection colTimeSheetList = beanTSAdd.getTimesheetList();
			if (colTimeSheetList!=null && !colTimeSheetList.isEmpty()) {
				Iterator it = colTimeSheetList.iterator();
				while (it.hasNext()) {
					TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
					objInput.addTimeSheetLine(
						nUserID,
						timeSheetInfo.getProject(),
						timeSheetInfo.getProcess(),
						timeSheetInfo.getTypeofWork(),
						timeSheetInfo.getWorkProduct(),
						timeSheetInfo.getDuration(),
						timeSheetInfo.getDate(),
						timeSheetInfo.getDescription()
					);
				}
			}
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in TimesheetBO.addTimesheet(): " + ex.toString());
            logger.error(ex);
        }

        return nResult;
    }

    /**
     * Update timesheet
     * @author  Nguyen Thai Son.
     * @version  November 19, 2002.
     * @param   beanTSUpdate TSUpdateBean: input data
     * @exception   Exception    If an exception occurred.
     */
    public int updateTimesheet(TSUpdateBean beanTSUpdate) throws Exception {
        int nResult = -1;
        if (beanTSUpdate == null)
            return nResult;

        try {
            getInputEJB();

            Collection colTimeSheetList = beanTSUpdate.getTimesheetList();
			if (colTimeSheetList!=null && !colTimeSheetList.isEmpty()) {
				Iterator it = colTimeSheetList.iterator();
				while (it.hasNext()) {
					TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
					objInput.updateTimeSheetLine(
						timeSheetInfo.getTimeSheetID(),
						timeSheetInfo.getProject(),
						timeSheetInfo.getProcess(),
						timeSheetInfo.getTypeofWork(),
						timeSheetInfo.getWorkProduct(),
						timeSheetInfo.getDuration(),
						timeSheetInfo.getDate(),
						timeSheetInfo.getDescription()
					);
				}
			}
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in TimesheetBO.addTimesheet(): " + ex.toString());
            logger.error(ex);
        }

        return nResult;
    }

	/**
	 * Delete timesheet
	 * @author  Nguyen Thai Son.
	 * @version  November 19, 2002.
	 * @param   strIDs String: a string of IDs separated by commas
	 * @exception   Exception    If an exception occurred.
	 */
	public int deleteTimesheet(String strIDs) throws Exception {
		int nResult = -1;
		try {
			getInputEJB();
			objInput.deleteTimeSheet(strIDs);
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in TimesheetBO.deleteTimesheet(): " + ex.toString());
			logger.error(ex);
		}
		return nResult;
	}

	public int getNewTSid(String strID) throws Exception {
			try {
				getInputEJB();
				return objInput.getTimeSheetsById(strID);
			}
			catch (javax.naming.NamingException e) {
				logger.debug("Couldn't locate " + e.getResolvedName());
				e.printStackTrace();
			}
			catch (Exception ex) {
				logger.debug("Exception occurs in TimesheetBO.deleteTimesheet(): " + ex.toString());
				logger.error(ex);
			}
			return 0 ;
		}

    /**
     * Save password
     * @author  Nguyen Thai Son.
     * @version  November 23, 2002.
     * @param   beanChangePassword ChangePasswordBean: input data
     * @param	beanUserInfo UserInfoBean: user information
     * @exception   Exception    If an exception occurred.
     */
    public ChangePasswordBean saveNewPassword(UserInfoBean beanUserInfo, ChangePasswordBean beanChangePassword) throws Exception {
        try {
            getInputEJB();

            int nResult = objInput.changePassword(beanUserInfo.getUserId(), beanChangePassword.getOldPassword(), beanChangePassword.getNewPassword());
            String strMessage = "Your password changed successfully.";
            if (nResult == 0)
                strMessage = "Change password failed. Invalid old password.";
            beanChangePassword.setMessage(strMessage);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in TimesheetBO.saveNewPassword(): " + ex.toString());
            logger.error(ex);
        }

        return beanChangePassword;
    }

    /**
     * checkUserAccount
     * Check User Account Existance, return Developer ID if exist
     * @author  Tu Ngoc Trung
     * @version  12 2003-11-15
     * @throws Exception
     */
    public ArrayList checkUserAccount(String strUserAccount) throws Exception {
        ArrayList alsUser = null;
        try {
            getInputEJB();
            alsUser = objInput.checkUserAccount(strUserAccount);
        }
        catch (javax.naming.NamingException e) {
            logger.debug("Couldn't locate " + e.getResolvedName());
            e.printStackTrace();
        }
        catch (Exception ex) {
            logger.debug("Exception occurs in TimesheetBO.checkUserAccount(): " + ex.toString());
            logger.error(ex);
        }

        return alsUser;
    }

    /**
     * uploadTimesheet
     * Import timesheet by QA.
     * @author  Tu Ngoc Trung
     * @version November 14, 2003
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @param   servlet javax.servlet.HttpServlet: the servlet object that has called.
     * @throws Exception
     * @throws Throwable
     */
    public void uploadTimesheet(HttpServletRequest request,
                                HttpServletResponse response,
                                HttpServlet servlet)
                                throws Exception, Throwable {
        InquiryReportBean beanInquiryReport = new InquiryReportBean();
        //Init InquiryReportBean
        request.setAttribute("beanInquiryReport", beanInquiryReport);

        //Reset information
        beanInquiryReport.setTotalPage(0);
        beanInquiryReport.setTotalTimesheet(0);
        beanInquiryReport.setCurrentPage(0);
        request.removeAttribute("uploadMessage");
        try {
			ArrayList files = new ArrayList();
			HashMap fields = new HashMap();
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(256 * 1024);
			List items = upload.parseRequest(request);

			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					fields.put(item.getFieldName(), item.getString());
				} else {
					files.add(item);
				}
			}
            /*SmartUpload myUpload = new SmartUpload();
            myUpload.initialize(servlet.getServletConfig(), request, response);
            myUpload.setMaxFileSize(256 * 1024);
            myUpload.upload();
            com.jspsmart.upload.Request jspRequest = myUpload.getRequest();
            String strUserAccount = jspRequest.getParameter("userAccount");*/
			String strUserAccount = fields.get("userAccount") == null ? "" : fields.get("userAccount").toString();
            strUserAccount = strUserAccount.toUpperCase();
            ArrayList alsUser = checkUserAccount(strUserAccount);
            UserInfoBean impUserInfoBean = new UserInfoBean();
            if (alsUser.size() > 0) {
                impUserInfoBean.setUserId(Integer.parseInt((String) alsUser.get(0)));
                impUserInfoBean.setLoginName((String) alsUser.get(1));
                impUserInfoBean.setRole((String) alsUser.get(2));
                impUserInfoBean.setStatus((String) alsUser.get(3));
                impUserInfoBean.setGroupName((String) alsUser.get(4));
            }

            if ((strUserAccount != null) && (strUserAccount.length() > 0) && (alsUser.size() > 0)) {
                //Clock milli seconds
                String strMillis = String.valueOf(System.currentTimeMillis());
                String strExt = ".xls";
                String strFile_name = strMillis + strExt;

                String strRealPath = servlet.getServletContext().getRealPath("\\upload");
                String strExcelFile = strRealPath + "\\" + strFile_name;

                //com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);
                if (files.size() > 0 /*!myFile.isMissing()*/) {
                    if (TIMESHEET.DEBUG) {
                        logger.debug("strExcelFile=" + strExcelFile);
                    }
                    java.io.File mFile = new java.io.File(strRealPath);
                    if (!mFile.exists()) {
                        mFile.mkdir();
                    }
                    //myFile.saveAs(strExcelFile, SmartUpload.SAVE_PHYSICAL);
					((FileItem)files.get(0)).write(new File(strExcelFile));
                    StringMatrix smImport = getUploadedTimesheet(strExcelFile);
                    int nImported = -1;
                    if (smImport.getNumberOfRows() > 0) {   // At least one line
                        nImported = importTSList(impUserInfoBean, smImport);
                    }
                    showUploadResult(request, impUserInfoBean, smImport, nImported);
                }
            }
            else {
                request.setAttribute("uploadMessage", "<FONT color=yellow class=\"label1\">Incorrect Account</FONT>");
            }
        }
        catch (Exception e) {
            request.setAttribute("uploadMessage", "<FONT color=yellow class=\"label1\">Cannot import timesheet list</FONT>");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * getUploadedTimesheet
     * Get list of Timesheet from Excel file
     * @param strExcelFile - MS Excel file name
     * @return list of imported Timesheet
     * @throws Exception
     * @throws Throwable
     */
    private StringMatrix getUploadedTimesheet(String strExcelFile) throws Exception, Throwable {
        StringMatrix smTSList = new StringMatrix();
        java.io.File fExcelFile = null;
        try {
            fExcelFile = new java.io.File(strExcelFile);
            Workbook wb = Workbook.getWorkbook(fExcelFile);
            Sheet sh = wb.getSheet(TIMESHEET.IMPORT_SHEET);
            if (sh == null) {
                logger.info(TIMESHEET.IMPORT_SHEET + " sheet does not exist.");
                //return smTSList;
            }
            else {
                //Import data plus additional informations
                StringVector svRow = new StringVector(TIMESHEET.MAX_COL_IMPORT);
                for (int i = 0; i < TIMESHEET.MAX_ROW_IMPORT; i++) {
                    NumberCell c = (NumberCell) sh.getCell(0, i);
                    if (c.getValue() > 0) {
                        for (int j = 1; j < TIMESHEET.MAX_COL_IMPORT; j++) {
                            String strTmp = "";
                            if (sh.getCell(j, i).getType() == CellType.STRING_FORMULA ||
                                sh.getCell(j, i).getType() == CellType.NUMBER_FORMULA)
                            {
                                strTmp = sh.getCell(j, i).getContents();
                            }
                            svRow.setCell(j - 1, strTmp);
                        }
                        smTSList.addRow(svRow);
                    }
                }
            }
            wb.close();
        }
        catch (Exception e) {
            //e.printStackTrace();
            //throw e;
        }
        finally {
            if (fExcelFile != null) {
                fExcelFile.delete();
            }
        }
        return smTSList;
    }

    /**
     * importTSList
     * Import Timesheet from Imported List
     * @param beanUserInfo - User Informations
     * @param smImported - Imported List
     * @throws NumberFormatException
     * @throws Exception
     * @throws Throwable
     */
    private int importTSList(UserInfoBean beanUserInfo, StringMatrix smImported) throws NumberFormatException, Exception, Throwable {
        int nImportCount = 0;
        try {
            ProjectComboBO prjComboBO = new ProjectComboBO();
            //Get project list which this user can assign for.
            StringMatrix smProject = prjComboBO.getProjectComboList(beanUserInfo.getRole(), beanUserInfo.getUserId(), 0, -1);

            String strCell = "";
            // Check Excel file
            int nFound;
            String strMMDDYY = "";  //Store converted date from DD-MMM-YY to mm/dd/yy

            ArrayList arrListing = new ArrayList();
            TSAddBean beanTSAdd = new TSAddBean();
            for (int i = 0; i < smImported.getNumberOfRows(); i++) {
                //Get project code from imported list to check existance
                strCell = smImported.getCell(i, 1);
                nFound = smProject.indexOf(strCell, 1);
                if (nFound < 0) {    // Project Code is not existed
                    return nImportCount;
                }
                else {
                    // Get Project ID
                    smImported.setCell(i, 7, smProject.getCell(nFound, 0));
                    strCell = smImported.getCell(i, 0);     //Date
                    //Convert date from DD-MMM-YY to mm/dd/yy
                    strMMDDYY = DateUtils.DateToString(DateUtils.StringToDate(strCell, "dd-MMM-yy"), "MM/dd/yy");
                    smImported.setCell(i, 0, strMMDDYY);
                }
				if (strMMDDYY != null) {
				    TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
					//HanhTN set info to timeSheetInfo
					timeSheetInfo.setDate(smImported.getCell(i, 0));
					timeSheetInfo.setProject(Integer.parseInt(smImported.getCell(i, 7)));
					timeSheetInfo.setProcess(Integer.parseInt(smImported.getCell(i, 2)));
					timeSheetInfo.setTypeofWork(Integer.parseInt(smImported.getCell(i, 3)));
					timeSheetInfo.setWorkProduct(Integer.parseInt(smImported.getCell(i, 4)));
					timeSheetInfo.setDuration(Float.parseFloat(smImported.getCell(i, 5)));
					timeSheetInfo.setDescription(CommonUtil.stringConvert(smImported.getCell(i, 6)));

	                arrListing.add(timeSheetInfo);
	                nImportCount++;
				}
            }
            beanTSAdd.setTimesheetList(arrListing);
            addTimesheet(beanTSAdd, beanUserInfo);

			// Added by HaiMM
			TimesheetBO boTimeSheet = new TimesheetBO();
			boTimeSheet.addTimesheetDummy(beanUserInfo, TIMESHEET.INSERT);
			boTimeSheet.updateTimesheetDummy(beanUserInfo);
        }
        catch (NumberFormatException e) {   //Test number has failed
            logger.error(e);
        }
        catch (Exception e) {
            logger.error(e);
            throw e;
        }
        return nImportCount;
    }

    /**
     * showUploadResult
     * Show imported timesheet
     * @author Tu Ngoc Trung
     * @version November 18, 2003
     * @param request javax.servlet.HttpServletRequest: the request object.
     * @param impUserInfoBean UserInfoBean:   User info bean for import.
     * @param smImport StringMatrix:   Imported timesheet.
     * @param nImported: Number of imported timesheet.
     * @throws Exception
     * @throws Throwable
     */
    private void showUploadResult(HttpServletRequest request, UserInfoBean impUserInfoBean, StringMatrix smImport, int nImported) throws Exception, Throwable {
        InquiryReportBean beanInquiryReport = new InquiryReportBean();
        if ((nImported > 0) && (nImported == smImport.getNumberOfRows())) {
            //Set filter parameters
            //////////////////////////////////////
            /*
            StringBuffer strProjectIDs = new StringBuffer();   //beanInquiryReport.getProjectList().getCell(0, 0);
            String strStatus = impUserInfoBean.getStatus();
            //String strGroup = "All";
            String strGroup = impUserInfoBean.getGroupName();
            String strCurrentPage = "0";
            String strAccount = impUserInfoBean.getLoginName();
            String strApprover = "";
            String strSortby = "1";
            String strFromDate = null;
            String strToDate = null;

            //Compare and retrieve from date, to date, project list
            Date dFromDate = DateUtils.StringToDate(smImport.getCell(0, 0), "MM/dd/yy");
            Date dToDate = (Date)dFromDate.clone();
            for (int i = 0; i < smImport.getNumberOfRows(); i++) {
                Date dDate = DateUtils.StringToDate(smImport.getCell(i, 0), "MM/dd/yy");
                if (dFromDate.after(dDate)) {
                    dFromDate = dDate;
                }
                if (dToDate.before(dDate)) {
                    dToDate = dDate;
                }
                strProjectIDs.append(",").append(smImport.getCell(i, 7));
            }
            strProjectIDs.deleteCharAt(0);  //Remove first semicolon (,)

            strFromDate = DateUtils.DateToString(dFromDate, "MM/dd/yy");
            strToDate = DateUtils.DateToString(dToDate, "MM/dd/yy");

            beanInquiryReport.setProject(0);
            beanInquiryReport.setProjectID(strProjectIDs.toString());

            beanInquiryReport.setStatus(CommonUtil.StrToInt(strStatus));
            beanInquiryReport.setGroup(strGroup);
            beanInquiryReport.setFromDate(strFromDate);
            beanInquiryReport.setToDate(strToDate);
            beanInquiryReport.setAccount(strAccount);
            beanInquiryReport.setApprover(strApprover);
            beanInquiryReport.setSortby(CommonUtil.StrToInt(strSortby));
            beanInquiryReport.setCurrentPage(CommonUtil.StrToInt(strCurrentPage));
            //////////////////////////////////////////////////////////////////////////////////////////////////////

            //STEP 3 - Get all timesheet from database by filter parameters
            ReportBO boReport = new ReportBO();
            beanInquiryReport = boReport.getInquiryReport(beanInquiryReport);
            */

            //--------Set output display--------//
            //Get Process,Work,Product (ID, Code)
            CommonListBO cmlBO = new CommonListBO();
            StringMatrix smProcess = cmlBO.getProcessList();
            StringMatrix smWork = cmlBO.getTypeOfWorkList();
            StringMatrix smProduct = cmlBO.getProductList();

            StringMatrix smResult = new StringMatrix();
            int nFound = 0;

            for (int iImport = 0; iImport < smImport.getNumberOfRows(); iImport++) {
                StringVector vecRecord = new StringVector(14);

                vecRecord.setCell(0, smImport.getCell(iImport, 1));     //Project Code
                vecRecord.setCell(1, impUserInfoBean.getLoginName());   //Account for import
                vecRecord.setCell(2, smImport.getCell(iImport, 0));     //Date
                vecRecord.setCell(3, smImport.getCell(iImport, 6));     //Description
                vecRecord.setCell(4, smImport.getCell(iImport, 5));     //Time

                String strProcessID = smImport.getCell(iImport, 2);     //Process
                String strWorkID = smImport.getCell(iImport, 3);        //Work
                String strProductID = smImport.getCell(iImport, 4);     //Product

                nFound = smProcess.indexOf(strProcessID, 0);        //Index of ID
                vecRecord.setCell(5, smProcess.getCell(nFound, 1)); //Set Code

                nFound = smWork.indexOf(strWorkID, 0);              //Index of ID
                vecRecord.setCell(6, smWork.getCell(nFound, 1));    //Set Code

                nFound = smProduct.indexOf(strProductID, 0);        //Index of ID
                vecRecord.setCell(7, smProduct.getCell(nFound, 1)); //Set Code

                vecRecord.setCell(8, "");   //KPA
                vecRecord.setCell(9, "");   //Approved by Leader
                vecRecord.setCell(10, Integer.toString(iImport));    //.Timesheet_ID
                vecRecord.setCell(11, "");    //.ProjectType
                vecRecord.setCell(12, "");    //.ProjectStatus

                smResult.addRow(vecRecord);
            }
            beanInquiryReport.setTimesheetList(smResult);
            beanInquiryReport.setTotalPage(1);
            beanInquiryReport.setTotalTimesheet(nImported);
            beanInquiryReport.setCurrentPage(0);

            request.setAttribute("uploadMessage", "<FONT color=\"#ddffff\" class=\"label1\">Imported succesfully</FONT>");
        }
        else if (nImported < 0) {
            request.setAttribute("uploadMessage", "<FONT color=yellow class=\"label1\">Cannot import timesheet list</FONT>");
        }
        else if (nImported < smImport.getNumberOfRows()) {
            request.setAttribute("uploadMessage", "<FONT color=yellow class=\"label1\">Cannot import timesheet line " + (nImported + 1) + ", check again please.</FONT>");
        }

        request.setAttribute("beanInquiryReport", beanInquiryReport);
    }
	public int addTimesheetDummy(UserInfoBean beanUserInfo, String status)
			throws Exception {
			int nResult = -1;

			try {
				getInputEJB();

				int nUserID = beanUserInfo.getUserId();
				// Added by HaiMM for migration - 08/Aug/08
				Collection colTimeSheetDummyList = getTimesheetDummyList(nUserID);
				if (colTimeSheetDummyList != null && !colTimeSheetDummyList.isEmpty()) {
					Iterator it = colTimeSheetDummyList.iterator();
					while (it.hasNext()) {
						TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
						objInput.addTimeSheetDummyLine(
						timeSheetInfo.getTimeSheetID(), status);
					}
				}
				// End ------------------

			} catch (javax.naming.NamingException e) {
				logger.debug("Couldn't locate " + e.getResolvedName());
				e.printStackTrace();
			} catch (Exception ex) {
				logger.debug(
					"Exception occurs in TimesheetBO.addTimesheet(): "
						+ ex.toString());
				logger.error(ex);
			}

			return nResult;
		}

		// Added by HaiMM ================================
		public Collection getTimesheetDummyList(int nDevID)throws Exception {
			Collection arrListing = null;
			try {
				getInputEJB();

				arrListing = objInput.getTimeSheetDummyList(nDevID);

			} catch (javax.naming.NamingException e) {
				logger.debug("Couldn't locate " + e.getResolvedName());
				e.printStackTrace();
			} catch (Exception ex) {
				logger.debug(
					"Exception occurs in TimesheetBO.getTimesheetDummyList(): "
						+ ex.toString());
				logger.error(ex);
			}
			if (arrListing != null && !arrListing.isEmpty()) {
				return arrListing;
			}
			return arrListing;
		}

		public int updateTimesheetDummy(UserInfoBean beanUserInfo) throws Exception {
			int nResult = -1;

			try {
				getInputEJB();
				int nUserID = beanUserInfo.getUserId();
				// Added by HaiMM for migration - 08/Aug/08
				Collection colTimeSheetDummyList = getTimesheetDummyList(nUserID);

				if (colTimeSheetDummyList != null && !colTimeSheetDummyList.isEmpty()) {
					Iterator it = colTimeSheetDummyList.iterator();
					while (it.hasNext()) {
						TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
						objInput.updateTimeSheetDummyLine(
							timeSheetInfo.getTimeSheetID());
					}
				}
			} catch (javax.naming.NamingException e) {
				logger.debug("Couldn't locate " + e.getResolvedName());
				e.printStackTrace();
			} catch (Exception ex) {
				logger.debug(
					"Exception occurs in TimesheetBO.addTimesheet(): "
						+ ex.toString());
				logger.error(ex);
			}

			return nResult;
		}

		public int addTimesheetByUpdateStatus(Collection colUpdateList, String status)
			throws Exception {
			int nResult = -1;

			try {
				getInputEJB();

				if (colUpdateList != null && !colUpdateList.isEmpty()) {
					Iterator it = colUpdateList.iterator();
					while (it.hasNext()) {
						TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
						objInput.addTimeSheetDummyLine(
						timeSheetInfo.getTimeSheetID(), status);
					}
				}

			} catch (javax.naming.NamingException e) {
				logger.debug("Couldn't locate " + e.getResolvedName());
				e.printStackTrace();
			} catch (Exception ex) {
				logger.debug(
					"Exception occurs in TimesheetBO.addTimesheet(): "
						+ ex.toString());
				logger.error(ex);
			}

			return nResult;
		}

		public Collection getTimesheetMigrateList()throws Exception {
				Collection arrListing = null;
				try {
					getInputEJB();

					arrListing = objInput.getTimesheetMigrateList();

				} catch (javax.naming.NamingException e) {
					logger.debug("Couldn't locate " + e.getResolvedName());
					e.printStackTrace();
				} catch (Exception ex) {
					logger.debug(
						"Exception occurs in TimesheetBO.getTimesheetMigrateList(): "
							+ ex.toString());
					logger.error(ex);
				}
				if (arrListing != null && !arrListing.isEmpty()) {
					return arrListing;
				}
				return arrListing;
			}

		public int updateTimesheetMigrate(Collection colUpdateList, String status) throws Exception {
			int nResult = -1;

			try {
				getInputEJB();

				if (colUpdateList != null && !colUpdateList.isEmpty()) {
					Iterator it = colUpdateList.iterator();
					while (it.hasNext()) {
						TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
						objInput.updateTimeSheetMigrateLine(timeSheetInfo.getTimeSheetID(), status);
					}
				}
			} catch (javax.naming.NamingException e) {
				logger.debug("Couldn't locate " + e.getResolvedName());
				e.printStackTrace();
			} catch (Exception ex) {
				logger.debug(
					"Exception occurs in TimesheetBO.addTimesheet(): "
						+ ex.toString());
				logger.error(ex);
			}

			return nResult;
		}

		// End ===============================

}