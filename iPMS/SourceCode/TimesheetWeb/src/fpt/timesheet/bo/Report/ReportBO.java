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
 
 package fpt.timesheet.bo.Report;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.Logger;

import fpt.timesheet.ApproveTran.ejb.report.InquiryEJBLocal;
import fpt.timesheet.ApproveTran.ejb.report.InquiryEJBLocalHome;
//import fpt.timesheet.ApproveTran.ejb.report.InquiryHome;
//import fpt.timesheet.ApproveTran.ejb.report.InquiryRemote;
import fpt.timesheet.ApproveTran.ejb.report.InquiryRow;
import fpt.timesheet.ApproveTran.ejb.report.ReportWeeklyModel;
import fpt.timesheet.ApproveTran.ejb.report.SummaryModel;

//import fpt.timesheet.Exemption.ejb.Exemption;
//import fpt.timesheet.Exemption.ejb.ExemptionHome;
import fpt.timesheet.Exemption.ejb.ExemptionEJBLocal;
import fpt.timesheet.Exemption.ejb.ExemptionEJBLocalHome;

import fpt.timesheet.InputTran.ejb.DateUtils;
//import fpt.timesheet.InputTran.ejb.Input;
//import fpt.timesheet.InputTran.ejb.InputHome;
import fpt.timesheet.InputTran.ejb.InputEJBLocal;
import fpt.timesheet.InputTran.ejb.InputEJBLocalHome;

import fpt.timesheet.InputTran.ejb.AssignmentInfo;
import fpt.timesheet.bean.Report.InquiryReportBean;
import fpt.timesheet.bean.Report.SummaryReportBean;
import fpt.timesheet.bean.Report.WeeklyReportBean;
import fpt.timesheet.bo.Approval.TimesheetBO;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;
import fpt.timesheet.util.CommonFunction;
import fpt.timesheet.vo.*;

public class ReportBO {
	
	private InquiryEJBLocalHome homeInquiry = null; // InquiryHome --> InquiryEJBLocalHome  
	private InquiryEJBLocal objInquiry = null; //InquiryRemote --> InquiryEJBLocal
	
	private ExemptionEJBLocalHome homeExemption = null;
	private ExemptionEJBLocal objExemption = null;
	
	private InputEJBLocalHome homeInput = null; //InputHome --> InputEJBLocalHome
	private InputEJBLocal objInput = null; //Input --> InputEJBLocal
	
	private static Logger logger = Logger.getLogger(ReportBO.class.getName());
    
    /* Common formats that used for Excel cells */
    WritableFont writeFont = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD);
    WritableCellFormat cellFormat = new WritableCellFormat(writeFont);
    jxl.write.NumberFormat fmNumber = new jxl.write.NumberFormat("0.0#"); 
    WritableCellFormat numberCell = new WritableCellFormat(fmNumber);

	/**
	 * @see java.lang.Object#Object()
	 */
	public ReportBO() {
        try {
            // Set wrapping format for label (text) cells
            cellFormat.setWrap(true);
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Method getInquiryEJB.
	 * @throws NamingException
	 */
	private void getInquiryEJB() throws NamingException {
		try {

			if (homeInquiry == null) {
				
				Context ic = new InitialContext();
				java.lang.Object objref = ic.lookup(JNDI.INQUIRY);

				homeInquiry = (InquiryEJBLocalHome)(objref);

				if (objInquiry == null)
					objInquiry = homeInquiry.create();
			}
		}
		catch (NamingException ex) {
			logger.debug("ReportBO.getInquiryEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
			throw ex;
		}
		catch (Exception ex) {
			logger.debug("ReportBO.getInquiryEJB() error! -- " + ex.getMessage());
			ex.printStackTrace();
		}
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

//				homeInput = (InputHome) javax.rmi.PortableRemoteObject.narrow(objref, InputHome.class);
				homeInput = (InputEJBLocalHome)(objref);
				if (objInput == null)
					objInput = homeInput.create();
			}
		}
		catch (NamingException ex) {
			logger.debug("ReportBO.getInputEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
			throw ex;
		}
		catch (Exception ex) {
			logger.debug("ReportBO.getInputEJB() error! -- " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Method getInputEJB.
	 * @throws NamingException
	 */
	private void getExemptionEJB() throws NamingException {
		try {
			if (homeExemption == null) {
				Context ic = new InitialContext();
				java.lang.Object objref = ic.lookup(JNDI.EXEMPTION);

				homeExemption = (ExemptionEJBLocalHome)(objref);
				if (objExemption == null)
					objExemption = homeExemption.create();
			}
		}
		catch (NamingException ex) {
			logger.debug("ReportBO.getExemptionEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
			throw ex;
		}
		catch (Exception ex) {
			logger.debug("ReportBO.getExemptionEJB() error! -- " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Method getInquiryReport
	 * Get all timesheet
	 * @author  Nguyen Thai Son.
	 * @version  November 22, 2002.
	 * @param   beanInquiryReport InquiryReportBean: input data
	 * @exception   Exception    If an exception occurred.
	 */
	public InquiryReportBean getInquiryReport(InquiryReportBean beanInquiryReport, 
											  boolean bIsForExport,
											  HttpServletResponse response) throws Exception {
		try {
			getInquiryEJB();

			if (bIsForExport) {
				doExportInquiryFile(beanInquiryReport, bIsForExport, response);
				CommonFunction.deleteFile(beanInquiryReport.getExcelFile());
				CommonFunction.deleteFile(beanInquiryReport.getZipFile());
			}
			else {
				int nProjectID = beanInquiryReport.getProject();
				String strProjectID = beanInquiryReport.getProjectID();
				int nStatus = beanInquiryReport.getStatus();
				String strGroup = beanInquiryReport.getGroup();

				String strDateFrom = beanInquiryReport.getFromDate();
				String strDateTo = beanInquiryReport.getToDate();
				String strAccount = beanInquiryReport.getAccount();

				String strApprover = beanInquiryReport.getApprover();
				int nSort = beanInquiryReport.getSortby();
				int nCurrentPage = beanInquiryReport.getCurrentPage();

				Collection dbList;
				dbList = objInquiry.ViewTimesheet(strAccount, strApprover, nProjectID, strProjectID, nStatus, strDateFrom, strDateTo, nSort, strGroup, nCurrentPage, bIsForExport);
				StringMatrix smResult = new StringMatrix();

				if (dbList == null || dbList.isEmpty()) {
					beanInquiryReport.setTimesheetList(smResult);
				}

				Iterator it = dbList.iterator();
				while (it.hasNext()) {
					InquiryRow tsData = (InquiryRow) it.next();
					StringVector vecRecord = new StringVector(17);

					vecRecord.setCell(0, tsData.ProjectCode);
					vecRecord.setCell(1, tsData.UserName);
					vecRecord.setCell(2, tsData.Occur_Date);
					vecRecord.setCell(3, tsData.Description);
					vecRecord.setCell(4, tsData.Duration);
					vecRecord.setCell(5, tsData.ProcessName);
					vecRecord.setCell(6, tsData.TypeOfWorkName);
					vecRecord.setCell(7, tsData.WorkProductName);
					vecRecord.setCell(8, tsData.KPAName);
					vecRecord.setCell(9, tsData.Approved_By_Leader);

					vecRecord.setCell(10, tsData.Timesheet_ID);
					vecRecord.setCell(11, tsData.ProjectType);
					vecRecord.setCell(12, tsData.ProjectStatus);
					vecRecord.setCell(13, tsData.GroupName);
					vecRecord.setCell(14, tsData.Create_Date);
					vecRecord.setCell(15, tsData.PLapprovedtime);
					vecRecord.setCell(16, tsData.QAapprovedtime);

					smResult.addRow(vecRecord);
				}
				int nTotalPage = objInquiry.getTotalPage();
				int nTotalTimesheet = objInquiry.getTotalTimesheet();

				beanInquiryReport.setTimesheetList(smResult);
				beanInquiryReport.setTotalPage(nTotalPage);
				beanInquiryReport.setTotalTimesheet(nTotalTimesheet);
				if (nCurrentPage > (nTotalPage - 1)) {
					beanInquiryReport.setCurrentPage(0);
				}
				else {
					beanInquiryReport.setCurrentPage(nCurrentPage);
				}
			}
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getInquiryReport(): " + ex.toString());
			logger.error(ex);
		}
		return beanInquiryReport;
	}

	/**
	 * Export Inquiry File
	 * Method doExportInquiryFile
	 * @param beanInquiryReport
	 * @param bIsForExport
	 * @param request
	 * @param response
	 * @param servlet
	 * @return
	 * @throws Exception
	 */
	public InquiryReportBean doExportInquiryFile(InquiryReportBean beanInquiryReport, 
												 boolean bIsForExport, 
												 HttpServletResponse response) throws Exception {
		try {
			//Export Inquiry File
            CommonFunction.checkFolderExistCreateNew(InquiryReportBean.getRealPath());
			String strFileName = CommonFunction.doGenerateFile("InquiryTS_", ".xls");
			String strExcelFile = InquiryReportBean.getRealPath() + strFileName;
			beanInquiryReport.setExcelFile(strExcelFile);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook = Workbook.createWorkbook(new File(strExcelFile), ws);
			WritableSheet sheetInquiry = workbook.createSheet("Sheet1", 0);

			writeInquirySheet(beanInquiryReport, bIsForExport, sheetInquiry);
			workbook.write();
			workbook.close();

			//Zip Inquiry File
			CommonFunction.doZipInquiry(beanInquiryReport);

			//Download Zip Inquiry File
			if (beanInquiryReport != null) {
				response.setContentType("application/download");
				response.setHeader("Content-Disposition","attachment;filename=\"InquiryTimesheetZip.zip\"");
				byte[] fileData = CommonFunction.getFileData(beanInquiryReport.getZipFile(), TIMESHEET.SIZE_OF_FILE);
				ServletOutputStream sos = response.getOutputStream();
				sos.write(fileData);
				sos.close();
			}
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.doExportInquiryFile(): " + ex.toString());
			logger.error(ex);
		}
		return beanInquiryReport;
	}

	/**
	 * Do Export Inquiry File
	 * @param beanInquiryReport
	 * @param bIsForExport
	 * @param sheetInquiry
	 * @throws WriteException
	 * @throws Exception
	 */
	private void writeInquirySheet(InquiryReportBean beanInquiryReport, boolean bIsForExport, WritableSheet sheetInquiry) throws WriteException, Exception {
		try {
			getInquiryEJB();

			int nProjectID = beanInquiryReport.getProject();
			String strProjectID = beanInquiryReport.getProjectID();
			int nStatus = beanInquiryReport.getStatus();
			String strGroup = beanInquiryReport.getGroup();

			String strDateFrom = beanInquiryReport.getFromDate();
			String strDateTo = beanInquiryReport.getToDate();
			String strAccount = beanInquiryReport.getAccount();

			String strApprover = beanInquiryReport.getApprover();
			int nSort = beanInquiryReport.getSortby();
			int nCurrentPage = beanInquiryReport.getCurrentPage();

			Collection dbList;
			dbList = objInquiry.ViewTimesheet(strAccount, strApprover, nProjectID, strProjectID, nStatus, strDateFrom, strDateTo, nSort, strGroup, nCurrentPage, bIsForExport);
			Iterator it = dbList.iterator();

			int count = 1;
			//Inquiry Title
			writeInquiryTitle(sheetInquiry);
			//Inquiry Data
			while (it.hasNext()) {
				InquiryRow beanInquiryData = (InquiryRow) it.next();
				writeInquiryData(beanInquiryData, sheetInquiry, count);
				count++;
			}
			dbList.clear();
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.writeInquirySheet(): " + ex.toString());
			logger.error(ex);
		}
	}

	/**
	 * Make Title For Inquiry Sheet
	 * @param sheetInquiry
	 * @throws WriteException
	 */
	private void writeInquiryTitle(WritableSheet sheetInquiry) throws WriteException {
		try {
			//Format the Font Title
			WritableFont writeFontTitle = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLUE);
			WritableCellFormat cellFormatTitle = new WritableCellFormat(writeFontTitle);
			cellFormatTitle.setAlignment(Alignment.CENTRE);
			cellFormatTitle.setWrap(true);

			//Set size for Cell
			CellView cvShort = new CellView();
			CellView cvMedium = new CellView();
			CellView cvLong = new CellView();
			cvShort.setSize(10 * 256);
			cvMedium.setSize(18 * 256);
			cvLong.setSize(35 * 256);

			//Set Column for Inquiry Sheet
			cvShort.setFormat(cellFormatTitle);
			sheetInquiry.setColumnView(0, cvMedium);
			sheetInquiry.setColumnView(1, cvShort);
			sheetInquiry.setColumnView(2, cvShort);
			sheetInquiry.setColumnView(3, cvMedium);
			sheetInquiry.setColumnView(4, cvLong);
			sheetInquiry.setColumnView(5, cvShort);
			sheetInquiry.setColumnView(6, cvMedium);
			sheetInquiry.setColumnView(7, cvShort);
			sheetInquiry.setColumnView(8, cvMedium);
			sheetInquiry.setColumnView(9, cvShort);
			sheetInquiry.setColumnView(10, cvShort);
			sheetInquiry.setColumnView(11, cvMedium);
			sheetInquiry.setColumnView(12, cvMedium);
			sheetInquiry.setColumnView(13, cvMedium);

			//Label Title
			Label lblTitle_ProjectCode 	   = new Label(0,  0, "Project", cellFormatTitle);
			Label lblTitle_GroupName 	   = new Label(1,  0, "Group", cellFormatTitle);
			Label lblTitle_Account 		   = new Label(2,  0, "Account", cellFormatTitle);
			Label lblTitle_OccurDate 	   = new Label(3,  0, "Date", cellFormatTitle);
			Label lblTitle_Description 	   = new Label(4,  0, "Description", cellFormatTitle);
			Label lblTitle_Time 		   = new Label(5,  0, "Time", cellFormatTitle);
			Label lblTitle_ProcessName 	   = new Label(6,  0, "Process", cellFormatTitle);
			Label lblTitle_TypeOfWork  	   = new Label(7,  0, "Work", cellFormatTitle);
			Label lblTitle_WorkProductName = new Label(8,  0, "Product", cellFormatTitle);
			Label lblTitle_KPAName 		   = new Label(9,  0, "KPAName", cellFormatTitle);
			Label lblTitle_Leader 		   = new Label(10, 0, "Approver", cellFormatTitle);
			Label lblTitle_TimeStamp 	   = new Label(11, 0, "Time Stamp", cellFormatTitle);
			Label lblTitle_PLApprovedTime  = new Label(12, 0, "PL approved time", cellFormatTitle);
			Label lblTitle_QAApprovedTime  = new Label(13, 0, "QA approved time", cellFormatTitle);

			//Add Label Title
			sheetInquiry.addCell(lblTitle_ProjectCode);
			sheetInquiry.addCell(lblTitle_GroupName);
			sheetInquiry.addCell(lblTitle_Account);
			sheetInquiry.addCell(lblTitle_OccurDate);
			sheetInquiry.addCell(lblTitle_Description);
			sheetInquiry.addCell(lblTitle_Time);
			sheetInquiry.addCell(lblTitle_ProcessName);
			sheetInquiry.addCell(lblTitle_TypeOfWork);
			sheetInquiry.addCell(lblTitle_WorkProductName);
			sheetInquiry.addCell(lblTitle_KPAName);
			sheetInquiry.addCell(lblTitle_Leader);
			sheetInquiry.addCell(lblTitle_TimeStamp);
			sheetInquiry.addCell(lblTitle_PLApprovedTime);
			sheetInquiry.addCell(lblTitle_QAApprovedTime);
		}
		catch (Exception ex) {
			logger.debug("@HanhTN -- ReportBO -- writeInquiryTitle" + ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Fill Data Into Inquiry Sheet 
	 * @param beanInquiryData
	 * @param sheetInquiry
	 * @param count
	 * @throws WriteException
	 */
	private void writeInquiryData(InquiryRow beanInquiryData, WritableSheet sheetInquiry, int count) throws WriteException {
		try {
			//Format the Font Data
			/*
            WritableFont writeFont = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD);
			WritableCellFormat cellFormat = new WritableCellFormat(writeFont);
            jxl.write.NumberFormat fmNumber = new jxl.write.NumberFormat("0.##"); 
			WritableCellFormat numberCell = new WritableCellFormat(fmNumber);
			cellFormat.setWrap(true);*/

			//Inquiry Data
			Label lblProjectCode 	 = new Label(0,  count, beanInquiryData.ProjectCode, cellFormat);
			Label lblGroupName 		 = new Label(1,  count, beanInquiryData.GroupName, cellFormat);
			Label lblAccount 		 = new Label(2,  count, beanInquiryData.UserName, cellFormat);
			Label lblOccurDate 		 = new Label(3,  count, beanInquiryData.Occur_Date, cellFormat);
			Label lblDescription 	 = new Label(4,  count, beanInquiryData.Description, cellFormat);
			Number numTime			 = new Number(5, count, Double.parseDouble(beanInquiryData.Duration), numberCell);
			Label lblProcessName 	 = new Label(6,  count, beanInquiryData.ProcessName, cellFormat);
			Label lblTypeOfWork  	 = new Label(7,  count, beanInquiryData.TypeOfWorkName, cellFormat);
			Label lblWorkProductName = new Label(8,  count, beanInquiryData.WorkProductName, cellFormat);
			Label lblKPAName 		 = new Label(9,  count, beanInquiryData.KPAName, cellFormat);
			Label lblLeader 		 = new Label(10, count, beanInquiryData.Approved_By_Leader, cellFormat);
			Label lblTimeStamp 		 = new Label(11, count, beanInquiryData.Create_Date, cellFormat);
			Label lblPLApprovedTime  = new Label(12, count, beanInquiryData.PLapprovedtime, cellFormat);
			Label lblQAApprovedTime  = new Label(13, count, beanInquiryData.QAapprovedtime, cellFormat);

			//Add Inquiry Data
			sheetInquiry.addCell(lblProjectCode);
			sheetInquiry.addCell(lblGroupName);
			sheetInquiry.addCell(lblAccount);
			sheetInquiry.addCell(lblOccurDate);
			sheetInquiry.addCell(lblDescription);
			sheetInquiry.addCell(numTime);
			sheetInquiry.addCell(lblProcessName);
			sheetInquiry.addCell(lblTypeOfWork);
			sheetInquiry.addCell(lblWorkProductName);
			sheetInquiry.addCell(lblKPAName);
			sheetInquiry.addCell(lblLeader);
			sheetInquiry.addCell(lblTimeStamp);
			sheetInquiry.addCell(lblPLApprovedTime);
			sheetInquiry.addCell(lblQAApprovedTime);
		}
		catch (Exception ex) {
			logger.debug("@HanhTN -- ReportBO -- writeInquiryData" + ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Get summary report
	 * @author  Nguyen Thai Son.
	 * @version  November 23, 2002.
	 * @param   beanSummaryReport SummaryReportBean: input data
	 * @exception   Exception    If an exception occurred.
	 */
	public SummaryReportBean getSummaryReport(SummaryReportBean beanSummaryReport) throws Exception {
		int pType = 10;
		int nProjectID = beanSummaryReport.getProject();
		String arrProjectIDs = beanSummaryReport.getArrayOfProjectIDs();
		int nStatus = beanSummaryReport.getStatus();

		String strDateFrom = beanSummaryReport.getFromDate();
		String strDateTo = beanSummaryReport.getToDate();
		String strAccount = beanSummaryReport.getAccount();

		TimesheetBO tsBO = new TimesheetBO();
		ArrayList alsUser = tsBO.checkUserAccount(strAccount);
		String strDeveloperID = null;
		// Found this account
		if (alsUser.size() > 0) {
			strDeveloperID = (String) alsUser.get(0);
		}
		// Not found account then return empty report
		else if (strAccount.length() > 0) {
			//beanSummaryReport.setReportList(new StringMatrix());
			//return beanSummaryReport;
			strDeveloperID = "0";   //Not found
		}

		try {
			getInquiryEJB();

			if (beanSummaryReport.getProjectType() != null) {
				pType = Integer.parseInt(beanSummaryReport.getProjectType());
			}

			Collection dbList;
			dbList = objInquiry.getSummaryReportData(nProjectID, arrProjectIDs, strDateFrom, strDateTo, nStatus, pType, strDeveloperID);

			StringMatrix smResult = new StringMatrix();
			if (dbList == null || dbList.isEmpty()) {
				//logger.debug(" No data found.");
				beanSummaryReport.setReportList(smResult);
			}

			Iterator it = dbList.iterator();
			while (it.hasNext()) {
				SummaryModel tsData = (SummaryModel) it.next();
				StringVector svItem = new StringVector(3);
				svItem.setCell(0, tsData.getEffortName());
				svItem.setCell(1, tsData.getSelectedPeriod());
				svItem.setCell(2, tsData.getUptoCurrentDate());
				smResult.addRow(svItem);
			}
			beanSummaryReport.setReportList(smResult);
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			//pType = 10;
			logger.debug("NumberFormatException occurs in ReportBO.getSummaryReport(): beanSummaryReport.getProjectType() = " + beanSummaryReport.getProjectType());
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getSummaryReport(): " + ex.toString());
			logger.error(ex);
		}

		return beanSummaryReport;
	}

	/**
	 * Get effort summary
	 * @author  Nguyen Thai Son.
	 * @version  November 23, 2002.
	 * @param   beanSummaryReport SummaryReportBean: input data
	 * @exception   Exception    If an exception occurred.
	 */
	public SummaryReportBean getEffortSummary(SummaryReportBean beanSummaryReport) throws Exception {
		try {
			int nProjectID = beanSummaryReport.getProject();
			String arrProjectIDs = beanSummaryReport.getArrayOfProjectIDs();
			int nStatus = beanSummaryReport.getStatus();
			int nProjectType = 10;
			String strAccount = beanSummaryReport.getAccount();

			TimesheetBO tsBO = new TimesheetBO();
			ArrayList alsUser = tsBO.checkUserAccount(strAccount);
			String strDeveloperID = null;
			//boolean bFound = false;
			// Found this account
			if (alsUser.size() > 0) {
				strDeveloperID = (String) alsUser.get(0);
			}
			// Not found account then return empty report
			else if (strAccount.length() > 0) {
				strDeveloperID = "0";   //Not found
			}

			if (beanSummaryReport.getProjectType() != null) {
				nProjectType = Integer.parseInt(beanSummaryReport.getProjectType());
			}
			String strFromDate = beanSummaryReport.getFromDate();
			String strToDate = beanSummaryReport.getToDate();
			int nReportType = beanSummaryReport.getReportType();

			getInputEJB();

			ArrayList arrReturn;
			arrReturn = (ArrayList) objInput.getSummaryEfforts(nProjectID, arrProjectIDs, nStatus, strFromDate, strToDate, nReportType, nProjectType, strDeveloperID);

			StringMatrix smtSummaryReport = null;
			if (!arrReturn.isEmpty()) {
				if (nReportType != 4) {
					smtSummaryReport = (StringMatrix) arrReturn.get(0x00);
					if (smtSummaryReport.getNumberOfRows() > 0) {
						int nCol = smtSummaryReport.getNumberOfCols();
						int nRow = smtSummaryReport.getNumberOfRows();
						StringMatrix stmTemp = new StringMatrix(nRow + 1, nCol + 1);

						//get Header
						for (int i = 0; i < nCol; i++)
							stmTemp.setCell(0, i, smtSummaryReport.getCell(0, i));
						stmTemp.setCell(0, nCol, "Total");
						//get title
						for (int j = 1; j < nRow; j++)
							stmTemp.setCell(j, 0, smtSummaryReport.getCell(j, 0));
						stmTemp.setCell(nRow, 0, "<b>Total</b>");

						//get data
						float sumRow = 0;
						String tmp = "";
						float[] sumCol = {0, 0, 0, 0, 0, 0};
						DecimalFormat decimalFormat = new DecimalFormat("0.##");
						final String ZERO = "0.00";

						for (int i = 1; i < nRow; i++) {
							sumRow = 0;
							for (int j = 1; j < nCol; j++) {
								if (smtSummaryReport.getCell(i, j) != null) {
									float fTemp = Float.parseFloat(smtSummaryReport.getCell(i, j));
									String strItem = decimalFormat.format(fTemp);

									//Format before displaying it.
									int nDot = strItem.lastIndexOf('.');
									if (nDot == -1)
										strItem += ".00";
									else if (nDot == strItem.length() - 2)
										strItem += "0";
									stmTemp.setCell(i, j, strItem);

									sumRow += fTemp;
									sumCol[j - 1] += fTemp;
								}
								else {
									stmTemp.setCell(i, j, ZERO);
								}
							}
							if (sumRow != 0) {
								String strItem = decimalFormat.format(sumRow);

								//Format before displaying it.
								int nDot = strItem.lastIndexOf('.');
								if (nDot == -1)
									strItem += ".00";
								else if (nDot == strItem.length() - 2)
									strItem += "0";
								stmTemp.setCell(i, nCol, "<b>" + strItem + "</b>");
							}
							else {
								stmTemp.setCell(i, nCol, ZERO);
							}
						}
						float sTotal = 0;

						for (int i = 1; i < nCol; i++) {
							tmp = String.valueOf(sumCol[i - 1]) + "0";
							if (sumCol[i - 1] != 0) {
								String strItem = decimalFormat.format(sumCol[i - 1]);

								//Format before displaying it.
								int nDot = strItem.lastIndexOf('.');
								if (nDot == -1)
									strItem += ".00";
								else if (nDot == strItem.length() - 2)
									strItem += "0";

								stmTemp.setCell(nRow, i, "<b>" + strItem + "</b>");
							}
							else {
								stmTemp.setCell(nRow, i, ZERO);
							}
							sTotal += Float.parseFloat(tmp);
						}
						sTotal += 0.0001;
						String strTmp = String.valueOf(sTotal) + "0";
						if (sTotal > 0.001) {
							String strItem = decimalFormat.format(Float.parseFloat(strTmp));

							//Format before displaying it.
							int nDot = strItem.lastIndexOf('.');
							if (nDot == -1)
								strItem += ".00";
							else if (nDot == strItem.length() - 2)
								strItem += "0";

							stmTemp.setCell(nRow, nCol, "<b>" + strItem + "</b>");
						}
						else {
							stmTemp.setCell(nRow, nCol, ZERO);
						}
						beanSummaryReport.setReportList(stmTemp);
					}
				}
				else {
					smtSummaryReport = (StringMatrix) arrReturn.get(0x00);

					for (int i = 1; i < smtSummaryReport.getNumberOfRows(); i++) {
						StringVector tmpVector = new StringVector(smtSummaryReport.getNumberOfCols());
						tmpVector = smtSummaryReport.getRow(i);
						smtSummaryReport.setCell(i, 1, sumVector(tmpVector));
					}

					beanSummaryReport.setReportList(smtSummaryReport);
				}
			}
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getEffortSummary(): " + ex.toString());
			logger.error(ex);
		}

		return beanSummaryReport;
	}

	/**
	 * Method getProjectDistribution
	 * Get project effort distribution by Process, Type of work, KPA or Work product
	 * @param beanSummaryReport
	 * @return beanSummaryReport
	 * @throws Exception
	 */
	public SummaryReportBean getProjectDistribution(SummaryReportBean beanSummaryReport) throws Exception {
		int nProjectID = beanSummaryReport.getProject();
		String arrProjectIDs = beanSummaryReport.getArrayOfProjectIDs();
		int nStatus = beanSummaryReport.getStatus();
		int nProjectType = 10;
		String strAccount = beanSummaryReport.getAccount();

		// Check account is existed or not
		TimesheetBO tsBO = new TimesheetBO();
		ArrayList alsUser = tsBO.checkUserAccount(strAccount);
		String strDeveloperID = null;
		// Found this account
		if (alsUser.size() > 0) {
			strDeveloperID = (String) alsUser.get(0);
		}
		// Not found account then return empty report
		else if (strAccount.length() > 0) {
			strDeveloperID = "0";   //Not found
		}

		if (beanSummaryReport.getProjectType() != null) {
			nProjectType = Integer.parseInt(beanSummaryReport.getProjectType());
		}
		String strFromDate = beanSummaryReport.getFromDate();
		String strToDate = beanSummaryReport.getToDate();
		int nReportType = beanSummaryReport.getReportType();

		try {
			getInquiryEJB();

			ArrayList dbList;
			dbList = objInquiry.getProjectDistribution(nProjectID, arrProjectIDs, nStatus, strFromDate, strToDate, nReportType, nProjectType, strDeveloperID);
			beanSummaryReport.setProjectSummary(dbList);
		}
		catch (javax.naming.NamingException e) {
		}
		catch (NumberFormatException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return beanSummaryReport;
	}

	/**
	 * Get summary effort of projects
	 * @param beanSummaryReport
	 * @return
	 * @throws Exception
	 */
	public SummaryReportBean getProjectSummary(SummaryReportBean beanSummaryReport) throws Exception {
		int nProjectID = beanSummaryReport.getProject();
		String arrProjectIDs = beanSummaryReport.getArrayOfProjectIDs();
		int nStatus = beanSummaryReport.getStatus();
		int nProjectType = 10;
		String strAccount = beanSummaryReport.getAccount();
        
		// Check account is existed or not
		TimesheetBO tsBO = new TimesheetBO();
		ArrayList alsUser = tsBO.checkUserAccount(strAccount);
		String strDeveloperID = null;
		// Found this account
		if (alsUser.size() > 0) {
			strDeveloperID = (String) alsUser.get(0);
		}
		// Not found account then return empty report
		else if (strAccount.length() > 0) {
			strDeveloperID = "0";   //Not found
		}

		if (beanSummaryReport.getProjectType() != null) {
			nProjectType = Integer.parseInt(beanSummaryReport.getProjectType());
		}
		String strFromDate = beanSummaryReport.getFromDate();
		String strToDate = beanSummaryReport.getToDate();
		int nReportType = beanSummaryReport.getReportType();

		try {
			getInquiryEJB();

			ArrayList dbList;
			dbList = objInquiry.getProjectSummary(nProjectID, arrProjectIDs, nStatus, strFromDate, strToDate, nReportType, nProjectType, strDeveloperID);
			beanSummaryReport.setProjectSummary(dbList);
		}
		catch (javax.naming.NamingException e) {
		}
		catch (NumberFormatException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return beanSummaryReport;
	}

	/**
	 * Method sumVector.
	 * @param StringVector inVec
	 * @return String
	 */
	private static String sumVector(StringVector inVec) {
		float total = 0;
		for (int i = 2; i < inVec.getDim(); i++) {
			String ef = inVec.getCell(i) + "0";
			ef = ef.substring(0, ef.lastIndexOf('.') + 3);
			float effort = Float.parseFloat(ef);
			total += effort;
		}

		total += 0.0001;
		if (total < 0.001) {
			return "0.0";
		}
		else {
			String ret;
			ret = String.valueOf(total);
			ret = ret.substring(0, ret.lastIndexOf('.') + 2);
			return ret;
		}
	}


	/**
	 * Method getPendingTimesheet
	 * @param beanWeeklyReport
	 * @return beanReportWeekly
	 * @throws Exception
	 */
	public WeeklyReportBean getPendingTimesheet(WeeklyReportBean beanWeeklyReport) throws Exception {
		try {
			getInquiryEJB();

			String arrProjectIDs = beanWeeklyReport.getArrayOfProjectIDs();
			String strFromDate = beanWeeklyReport.getFromDate();
			String strToDate = beanWeeklyReport.getToDate();
			String strGroup = beanWeeklyReport.getGroup();

			Collection dbList;
			dbList = objInquiry.getPendingReportData(arrProjectIDs, strFromDate, strToDate, strGroup);
			StringMatrix smtReportList = new StringMatrix(dbList.size(), 3);

			Iterator it = dbList.iterator();
			int i = 0;
			while (it.hasNext()) {
				ReportWeeklyModel tsData = (ReportWeeklyModel) it.next();
				smtReportList.setCell(i, 0, tsData.getKey());
				smtReportList.setCell(i, 1, tsData.getValue());
				smtReportList.setCell(i, 2, tsData.getLeader());
				i++;
			}
			beanWeeklyReport.setReportList(smtReportList);
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getPendingTimesheet(): " + ex.toString());
			logger.error(ex);
		}

		return beanWeeklyReport;
	}

	/**
	 * Method getUnapprovedPMBO
	 * @author Truong Ngoc Hanh
	 * @param beanWeeklyReport
	 * @return beanWeeklyReport
	 * @throws Exception
	 */
	public WeeklyReportBean getUnreviewedPMBO(WeeklyReportBean beanWeeklyReport) throws Exception {
		try {
			getInquiryEJB();

			int intProjectID = beanWeeklyReport.getProject();
			String arrProjectIDs = beanWeeklyReport.getArrayOfProjectIDs();
			String strGroup = beanWeeklyReport.getGroup();
			int intProjectStatus = beanWeeklyReport.getProjectStatus();			

			String strFromDate = beanWeeklyReport.getSearchFromDate();
			String strToDate = beanWeeklyReport.getSearchToDate();
			String strLogDate = beanWeeklyReport.getLogDate();
			String strLogTime = beanWeeklyReport.getLogTime();
			String strLogDateTime = strLogDate + " " + strLogTime;

			Hashtable hashReportList = new Hashtable();
			StringMatrix smtReportList = new StringMatrix();

			Collection colReportList;
			colReportList = objInquiry.getUnreviewedPMEJB(strGroup, intProjectID, arrProjectIDs, strFromDate, strToDate, strLogDateTime, intProjectStatus);

			//GET REPORT LIST
			if (colReportList!= null && !colReportList.isEmpty()) {
				Iterator it = colReportList.iterator();
				while (it.hasNext()) {
					ReportWeeklyModel beanReport = (ReportWeeklyModel) it.next();

					StringVector vecReportList = new StringVector(7);
					vecReportList.setCell(0, Integer.toString(beanReport.getProjectId()));
					vecReportList.setCell(1, beanReport.getKey());
					vecReportList.setCell(2, beanReport.getLeader());
					vecReportList.setCell(3, Double.toString(beanReport.getUnapprovedEffort()));
					vecReportList.setCell(4, Double.toString(beanReport.getTotalEffort()));
					vecReportList.setCell(5, Double.toString(beanReport.getRatioEffort()));
					vecReportList.setCell(6, beanReport.getValue());

					hashReportList.put(new Integer(beanReport.getProjectId()), vecReportList);
					smtReportList.addRow(vecReportList);
				}

				StringMatrix smtReportResult = new StringMatrix();
				ReportWeeklyModel beanReport = new ReportWeeklyModel();
				StringVector vecProjectId = null;
				StringVector vecResult = new StringVector(7);

				//Select all projects
				if (intProjectID==0) {
					for (int row=0; row<smtReportList.getNumberOfRows(); row++) {
						vecResult.setCell(0, smtReportList.getCell(row, 0));	//Project ID
						vecResult.setCell(1, smtReportList.getCell(row, 1));	//Project Code
						vecResult.setCell(2, smtReportList.getCell(row, 2));	//Leader
						vecResult.setCell(3, smtReportList.getCell(row, 3));	//Unapproved Effort
						vecResult.setCell(4, smtReportList.getCell(row, 4));	//Total Effort
						vecResult.setCell(5, smtReportList.getCell(row, 5));	//Ratio Effort
						vecResult.setCell(6, smtReportList.getCell(row, 6));	//Value
						smtReportResult.addRow(vecResult);
					}
					getReportPMList(beanWeeklyReport, beanReport, smtReportResult);
				}
				//Select a project
				else {
					StringVector vecReport = new StringVector(7);
					vecProjectId = (StringVector) hashReportList.get(Integer.valueOf(Integer.toString(intProjectID)));

					if (vecProjectId != null) {
						vecReport.setCell(0, vecProjectId.getCell(0));	//Project Id
						vecReport.setCell(1, vecProjectId.getCell(1));	//Project Code
						vecReport.setCell(2, vecProjectId.getCell(2));	//Leader
						vecReport.setCell(3, vecProjectId.getCell(3));	//Unapproved Effort
						vecReport.setCell(4, vecProjectId.getCell(4));	//Total Effort
						vecReport.setCell(5, vecProjectId.getCell(5));	//Ratio Effort
						vecReport.setCell(6, vecProjectId.getCell(6));	//Value
						smtReportResult.addRow(vecReport);
					}
					getReportPMList(beanWeeklyReport, beanReport, smtReportResult);
				}
			}
			else {
				insertReportPMTitle(new StringMatrix());
			}
		}
		catch (javax.naming.NamingException ex) {
			logger.debug("Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getUnapprovedPMBO(): " + ex.toString());
			logger.error(ex);
			ex.printStackTrace();
		}
		return beanWeeklyReport;
	}

	/**
	 * Method insertReportPMTitle
	 * @param smtReportList
	 */
	private void insertReportPMTitle(StringMatrix smtReportList) {
		 //Add Title
		 StringVector vecTitle = null;
		 vecTitle = new StringVector(6);

		 vecTitle.setCell(0, "Project Id");
		 vecTitle.setCell(1, "Project Code");
		 vecTitle.setCell(2, "Leader");
		 vecTitle.setCell(3, "Unapproved Effort");
		 vecTitle.setCell(4, "Ratio Effort");
		 vecTitle.setCell(5, "Persons whose TS <br> has not been reviewed");

		 if (smtReportList.getNumberOfRows() > 0) {
			  smtReportList.insertRow(0, vecTitle);
		 }
		 else {
			  smtReportList.addRow(vecTitle);
		 }
	}

	/**
	 * Method getReportPMList
	 * @param beanWeeklyReport
	 * @param beanReport
	 * @param smtReportResult
	 */
	private void getReportPMList(WeeklyReportBean beanWeeklyReport, ReportWeeklyModel beanReport, StringMatrix smtReportResult) {
		StringMatrix smtList = new StringMatrix();
		StringVector vecList = new StringVector(5);
		DecimalFormat decimalFormat = new DecimalFormat("0.##");

		//Add Total
		int intCount = 0;
		double dblUnapprovedEffort = 0.0;
		double dblTotalEffort = 0.0;
		double dblRatioEffort = 0.0;

		for (int row=0; row<smtReportResult.getNumberOfRows(); row++) {
			dblUnapprovedEffort += Double.parseDouble(smtReportResult.getCell(row, 3));
			dblTotalEffort += Double.parseDouble(smtReportResult.getCell(row, 4));
			intCount++;
		}
		dblRatioEffort = (dblUnapprovedEffort/dblTotalEffort)*100.0;

		StringVector vecTotal = new StringVector(7);
		vecTotal.setCell(0, "<b>Total<b>");
		vecTotal.setCell(1, "<b>Total<b>");
		vecTotal.setCell(2, "");
		vecTotal.setCell(3, Double.toString(dblUnapprovedEffort));
		vecTotal.setCell(4, Double.toString(dblTotalEffort));
		vecTotal.setCell(5, Double.toString(dblRatioEffort));
		vecTotal.setCell(6, "");
		smtReportResult.insertRow(intCount, vecTotal);

		for (int row=0; row<smtReportResult.getNumberOfRows(); row++) {
			beanReport.setKey(smtReportResult.getCell(row, 1));
			beanReport.setLeader(smtReportResult.getCell(row, 2));
			beanReport.setUnapprovedEffort(Double.parseDouble(smtReportResult.getCell(row, 3)));
			beanReport.setRatioEffort(Double.parseDouble(smtReportResult.getCell(row, 5)));
			beanReport.setValue(smtReportResult.getCell(row, 6));

			vecList.setCell(0, beanReport.getKey());
			vecList.setCell(1, beanReport.getLeader());
			vecList.setCell(2, decimalFormat.format(beanReport.getUnapprovedEffort()));
			vecList.setCell(3, decimalFormat.format(beanReport.getRatioEffort()));
			vecList.setCell(4, beanReport.getValue());
			smtList.addRow(vecList);
		}
		int count = 0;
		for (int row=0; row<smtList.getNumberOfRows(); row++) {
			count++;
		}
		smtList.setCell(count - 1, 2, "<b>" + smtList.getCell(count - 1, 2) +"</b>");
		smtList.setCell(count - 1, 3, "<b>" + smtList.getCell(count - 1, 3) +"</b>");

		//Add Title
		StringVector vecReportTitle = null;
		vecReportTitle = new StringVector(5);

		vecReportTitle.setCell(0, "Project code");
		vecReportTitle.setCell(1, "Reviewer");
		vecReportTitle.setCell(2, "Unreviewed TS <br> (person-day)");
		vecReportTitle.setCell(3, "Ratio of Unreviewed TS <br> (%)");
		vecReportTitle.setCell(4, "Persons whose TS <br> has not been reviewed");

		if (smtList.getNumberOfRows() > 0) {
			smtList.insertRow(0, vecReportTitle);
		}
		else {
			smtList.addRow(vecReportTitle);
		}
		beanWeeklyReport.setReportList(smtList);
	}

	/**
	 * Method getUnapprovedQABO
	 * @author Truong Ngoc Hanh
	 * @param beanWeeklyReport
	 * @return beanWeeklyReport
	 * @throws Exception
	 */
	public WeeklyReportBean getUnreviewedQABO(WeeklyReportBean beanWeeklyReport) throws Exception {
		try {
			getInquiryEJB();

			int nProjectID = beanWeeklyReport.getProject();
			String arrProjectIDs = beanWeeklyReport.getArrayOfProjectIDs();

			String strGroup = beanWeeklyReport.getGroup();
			String strPQAName = beanWeeklyReport.getPQAName();
			int intProjectStatus = beanWeeklyReport.getProjectStatus();

			String strFromDate = beanWeeklyReport.getSearchFromDate();
			String strToDate = beanWeeklyReport.getSearchToDate();
			String strLogDate = beanWeeklyReport.getLogDate();
			String strLogTime = beanWeeklyReport.getLogTime();
			String strLogDateTime = strLogDate + " " + strLogTime;

			//GET DATA FROM EJB
			Collection colReportList;
			Collection colPQAList;
			colReportList = objInquiry.getUnreviewedQAEJB(strGroup, strPQAName, nProjectID, arrProjectIDs, strFromDate, strToDate, strLogDateTime, intProjectStatus);
			colPQAList = objInquiry.getPQAList(nProjectID, arrProjectIDs, strPQAName);

			//GET REPORTLIST
		    StringMatrix smtReportList = new StringMatrix();
		    StringVector vecReportList = null;

		    if (colReportList!= null && !colReportList.isEmpty()) {
			    Iterator itReportList = colReportList.iterator();
			    while (itReportList.hasNext()) {
				    ReportWeeklyModel beanReport = (ReportWeeklyModel) itReportList.next();

				    vecReportList = new StringVector(7);
				    vecReportList.setCell(0, Integer.toString(beanReport.getProjectId()));
				    vecReportList.setCell(1, beanReport.getKey());
				    vecReportList.setCell(2, beanReport.getLeader());
					vecReportList.setCell(3, Double.toString(beanReport.getUnapprovedEffort()));
					vecReportList.setCell(4, Double.toString(beanReport.getTotalEffort()));
					vecReportList.setCell(5, Double.toString(beanReport.getRatioEffort()));
				    vecReportList.setCell(6, beanReport.getValue());
				    smtReportList.addRow(vecReportList);
			   }

			   //GET PQA List
			   Hashtable hashPQAList = new Hashtable();
			   Iterator itPQAList = colPQAList.iterator();
			   while (itPQAList.hasNext()) {
				   ReportWeeklyModel beanReport = (ReportWeeklyModel) itPQAList.next();

				   StringVector vecPQAList = new StringVector(4);
				   vecPQAList.setCell(0, Integer.toString(beanReport.getProjectId()));
				   vecPQAList.setCell(1, beanReport.getProjectCode());
				   vecPQAList.setCell(2, beanReport.getPQAAccount());
				   vecPQAList.setCell(3, beanReport.getPQAName());
				   hashPQAList.put(new Integer(beanReport.getProjectId()), vecPQAList);
			   }

			   //ReportWeeklyModel beanReport = new ReportWeeklyModel();
			   StringMatrix smtReportResult = new StringMatrix();
			   StringVector vecHashPQAList = null;
			   StringVector vecResult = new StringVector(8);

			   for (int row=0; row<smtReportList.getNumberOfRows(); row++) {
				   vecHashPQAList = (StringVector) hashPQAList.get(Integer.valueOf(smtReportList.getCell(row, 0)));
				   String strName = "";
				   String strAccount = "";

				   if (vecHashPQAList != null) {
					   strAccount = vecHashPQAList.getCell(2);
					   strName = vecHashPQAList.getCell(3);
				   }
				   vecResult.setCell(0, smtReportList.getCell(row, 0)); 	//Project ID
				   vecResult.setCell(1, smtReportList.getCell(row, 1)); 	//Project Code
				   vecResult.setCell(2, strAccount);	 					//PQA Account
				   vecResult.setCell(3, strName);					 		//PQA Name	
				   vecResult.setCell(4, smtReportList.getCell(row, 3)); 	//Unapproved Effort
				   vecResult.setCell(5, smtReportList.getCell(row, 4));		//Total Effort
				   vecResult.setCell(6, smtReportList.getCell(row, 5));		//Ratio Effort
				   vecResult.setCell(7, smtReportList.getCell(row, 6));		//Value
				   smtReportResult.addRow(vecResult);
			   }
			   //Select all projects
			   if (nProjectID == 0) {
				   insertTotalUnapprovedQARow(smtReportResult);
				   getReportQAList(beanWeeklyReport, smtReportResult);
			   }
			   //Select a project
			   else {
				   getReportQAListByProjectId(beanWeeklyReport, smtReportResult, nProjectID);
			   }
		    }
		    else {
				insertTotalUnapprovedQARow(new StringMatrix());
		    }
		}
		catch (javax.naming.NamingException ex) {
			logger.debug("Couldn't locate " + ex.getResolvedName());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getUnapprovedQABO(): " + ex);
			logger.error(ex);
			ex.printStackTrace();
		}
		return beanWeeklyReport;
	}

	/**
	 * Method insertTotalUnapprovedQARow
	 * @param smtReportResult
	 */
	private void insertTotalUnapprovedQARow(StringMatrix smtReportResult) {
		 //Add Title
		 StringVector vecTitle = null;
		 vecTitle = new StringVector(8);

		 vecTitle.setCell(0, "Project Id");
		 vecTitle.setCell(1, "Project Code");
		 vecTitle.setCell(2, "QA Account");
		 vecTitle.setCell(3, "QA Name");
		 vecTitle.setCell(4, "Unapproved Effort");
		 vecTitle.setCell(5, "Total Effort");
		 vecTitle.setCell(6, "Ratio Effort");
		 vecTitle.setCell(7, "Persons whose TS <br> has not been reviewed");

		 if (smtReportResult.getNumberOfRows() > 0) {
			 smtReportResult.insertRow(0, vecTitle);
		 }
		 else {
			 smtReportResult.addRow(vecTitle);
		 }

		//Add Total
		int intCount = 0;
		double dblUnapprovedEffort = 0.0;
		double dblTotalEffort = 0.0;
		double dblRatioEffort = 0.0;

		for (int irow=1; irow<smtReportResult.getNumberOfRows(); irow++) {
			dblUnapprovedEffort += Double.parseDouble(smtReportResult.getCell(irow, 4));
			dblTotalEffort += Double.parseDouble(smtReportResult.getCell(irow, 5));
			intCount++;
		}
		dblRatioEffort = (dblUnapprovedEffort/dblTotalEffort)*100.0;

		StringVector vecTotal = new StringVector(8);
		vecTotal.setCell(0, "<b>Total<b>");
		vecTotal.setCell(1, "<b>Total<b>");
		vecTotal.setCell(2, "");
		vecTotal.setCell(3, "");
		vecTotal.setCell(4, Double.toString(dblUnapprovedEffort));
		vecTotal.setCell(5, Double.toString(dblTotalEffort));
		vecTotal.setCell(6, Double.toString(dblRatioEffort));
		vecTotal.setCell(7, "");
		smtReportResult.insertRow(intCount + 1, vecTotal);
	}

	/**
	 * Method getReportQAList
	 * @param beanWeeklyReport
	 * @param smtReportResult
	 */
	private void getReportQAList(WeeklyReportBean beanWeeklyReport, StringMatrix smtReportResult) {
		ReportWeeklyModel beanList = new ReportWeeklyModel();
		StringMatrix smtList = new StringMatrix();
		StringVector vecList = new StringVector(6);
		DecimalFormat decimalFormat = new DecimalFormat("0.##");

		for (int row=1; row<smtReportResult.getNumberOfRows(); row++) {
			beanList.setProjectCode(smtReportResult.getCell(row, 1));
			beanList.setPQAAccount(smtReportResult.getCell(row, 2));
			beanList.setPQAName(smtReportResult.getCell(row, 3));
			beanList.setUnapprovedEffort(Double.parseDouble(smtReportResult.getCell(row, 4)));
			beanList.setRatioEffort(Double.parseDouble(smtReportResult.getCell(row, 6)));
			beanList.setValue(smtReportResult.getCell(row, 7));

			vecList.setCell(0, beanList.getProjectCode());
			vecList.setCell(1, beanList.getPQAAccount());
			vecList.setCell(2, beanList.getPQAName());
			vecList.setCell(3, decimalFormat.format(beanList.getUnapprovedEffort()));
			vecList.setCell(4, decimalFormat.format(beanList.getRatioEffort()));
			vecList.setCell(5, beanList.getValue());
  
			smtList.addRow(vecList);
		}

		//Add Title
	    StringVector vecListTitle = null;
	    vecListTitle = new StringVector(6);

	    vecListTitle.setCell(0, "Project code");
	    vecListTitle.setCell(1, "QA Account");
	    vecListTitle.setCell(2, "QA Name");
	    vecListTitle.setCell(3, "Unreviewed TS <br> (person-day)");
	    vecListTitle.setCell(4, "Ratio of Unreviewed TS <br> (%)");
	    vecListTitle.setCell(5, "Persons whose TS <br> has not been reviewed");

	    if (smtList.getNumberOfRows() > 0) {
		    smtList.insertRow(0, vecListTitle);
	    }
	    else {
		    smtList.addRow(vecListTitle);
	    }
	    int count = 0;
	    for (int row=0; row<smtList.getNumberOfRows(); row++) {
			count++;
	    }
	    smtList.setCell(count - 1, 3, "<b>" + smtList.getCell(count - 1, 3) +"</b>");
	    smtList.setCell(count - 1, 4, "<b>" + smtList.getCell(count - 1, 4) +"</b>");

	    beanWeeklyReport.setReportList(smtList);
	}

	/**
	 * Method getReportQAListByProjectId
	 * @param smtReportResult
	 */
	private void getReportQAListByProjectId(WeeklyReportBean beanWeeklyReport, StringMatrix smtReportResult, int nProjectID) {
		//Add Title
		StringVector vecTitle = null;
		vecTitle = new StringVector(8);

		vecTitle.setCell(0, "Project Id");
		vecTitle.setCell(1, "Project Code");
		vecTitle.setCell(2, "QA Account");
		vecTitle.setCell(3, "QA Name");
		vecTitle.setCell(4, "Unapproved Effort");
		vecTitle.setCell(5, "Total Effort");
		vecTitle.setCell(6, "Ratio Effort");
		vecTitle.setCell(7, "Persons whose TS <br> has not been reviewed");

		if (smtReportResult.getNumberOfRows() > 0) {
			smtReportResult.insertRow(0, vecTitle);
		}
		else {
			smtReportResult.addRow(vecTitle);
		}
		
		//PUT Result of smtReportResult INTO HASHTABLE
		Hashtable hashReportResult = new Hashtable();
		StringVector vecReportResult = null;
		StringVector vecProjectId = null;
		StringVector vecProject = new StringVector(8);
		StringMatrix smtProject = new StringMatrix();

		for (int row=1; row<smtReportResult.getNumberOfRows(); row++) {
			vecReportResult = smtReportResult.getRow(row);
			Integer intProjectId = new Integer(vecReportResult.getCell(0));
			hashReportResult.put(intProjectId, vecReportResult);
		}
		vecProjectId = (StringVector) hashReportResult.get(Integer.valueOf(Integer.toString(nProjectID)));
		vecProject.setCell(0, vecProjectId.getCell(0));   //Project Id
		vecProject.setCell(1, vecProjectId.getCell(1));   //Project Code
		vecProject.setCell(2, vecProjectId.getCell(2));   //PQA Account
		vecProject.setCell(3, vecProjectId.getCell(3));	  //PQA Name
		vecProject.setCell(4, vecProjectId.getCell(4));	  //Unapproved Effort
		vecProject.setCell(5, vecProjectId.getCell(5));   //Total Effort
		vecProject.setCell(6, vecProjectId.getCell(6));	  //Ratio Effort
		vecProject.setCell(7, vecProjectId.getCell(7));	  //Value
		smtProject.addRow(vecProject);

		insertTotalUnapprovedQARow(smtProject);
		getReportQAList(beanWeeklyReport, smtProject);
	}

	/*
	 * Get Lack Timesheet
	 * @author  Nguyen Thai Son.
	 * @version  November 23, 2002.
	 * @param   beanWeeklyReport WeeklyReportBean: input data
	 * @exception   Exception    If an exception occurred.
	public WeeklyReportBean getLackTimesheet(WeeklyReportBean beanWeeklyReport) throws Exception {
		try {
			getInputEJB();

			String strFromDate = beanWeeklyReport.getFromDate();
			String strToDate = beanWeeklyReport.getToDate();
			String strGroup = beanWeeklyReport.getGroup();
			
			StringMatrix smtWeeklyReport = null;
			ArrayList arrReturn = (ArrayList) objInput.getLackedTimesheet(strGroup, strFromDate, strToDate);
			if (arrReturn != null) {
				if (!arrReturn.isEmpty())
					smtWeeklyReport = (StringMatrix) arrReturn.get(0x00);
				beanWeeklyReport.setReportList(smtWeeklyReport);
			}
		}
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getLackTimesheet(): " + ex.toString());
			logger.error(ex);
		}

		return beanWeeklyReport;
	}
     */

	/**
	 * Method getLackTimesheetGroup
	 * @param beanWeeklyReport
	 * @return beanWeeklyReport
	 * @throws Exception
	 */
	public WeeklyReportBean getTrackingReportGroupBO(WeeklyReportBean beanWeeklyReport) throws Exception {
		try {
			getExemptionEJB();
            
            TrackingByProjectForm formRequest = new TrackingByProjectForm();
            
            formRequest.setGroup(beanWeeklyReport.getGroup());
            formRequest.setFromDate(beanWeeklyReport.getSearchFromDate());
            formRequest.setToDate(beanWeeklyReport.getSearchToDate());
            formRequest.setLogDate(beanWeeklyReport.getLogDate());
            formRequest.setLogTime(beanWeeklyReport.getLogTime());
            formRequest.setLogDateTime(formRequest.getLogDate() + " " + formRequest.getLogTime());

			StringMatrix smtWeeklyReport = null;
			ArrayList arrReturn = (ArrayList) objExemption.getTrackingReportEJB(formRequest);

			if (arrReturn != null && !arrReturn.isEmpty()) {
				smtWeeklyReport = (StringMatrix) arrReturn.get(0x00);
				getTotalTS(smtWeeklyReport);
				beanWeeklyReport.setReportList(smtWeeklyReport);
			}
		}
		catch (javax.naming.NamingException e) {
			logger.debug("ReportBO.getTrackingReportGroupBO() -- Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getTrackingReportGroupBO(): " + ex.toString());
			ex.printStackTrace();
		}

		return beanWeeklyReport;
	}

	/**
	 * Method getTrackingReportProjectBO
	 * @param beanWeeklyReport
	 * @return beanReportWeekly
	 * @throws Exception
	 */
	public WeeklyReportBean getTrackingReportProjectBO(WeeklyReportBean beanWeeklyReport) throws Exception {
		try {
			getExemptionEJB();

            TrackingByProjectForm formRequest = new TrackingByProjectForm();
            formRequest.setGroup(beanWeeklyReport.getGroup());
            formRequest.setFromDate(beanWeeklyReport.getSearchFromDate());
            formRequest.setToDate(beanWeeklyReport.getSearchToDate());
            formRequest.setLogDate(beanWeeklyReport.getLogDate());
            formRequest.setLogTime(beanWeeklyReport.getLogTime());
            formRequest.setLogDateTime(formRequest.getLogDate() + " " + formRequest.getLogTime());
            
            formRequest.setProjectId(beanWeeklyReport.getProject());
            formRequest.setProjectStatus(beanWeeklyReport.getProjectStatus());
            formRequest.setArrProjectIds(beanWeeklyReport.getArrayOfProjectIDs());

			Hashtable hashLackTimesheet = new Hashtable();
			StringMatrix smtLackTimeSheet = new StringMatrix();
			StringMatrix smtAssingment = new StringMatrix();
			ArrayList arrReturn = (ArrayList) objExemption.getTrackingReportEJB(formRequest);
			Collection colAssingment = objExemption.getArrAssignment(
                formRequest.getGroup(), formRequest.getProjectId(),
                formRequest.getArrProjectIds(), formRequest.getProjectStatus(),
                formRequest.getFromDate(), formRequest.getToDate());

			//HanhTN added HashTable -- 28/08/2006
			//PUT LACK TIMESHEET INTO HASHTABLE
			if (arrReturn != null && !arrReturn.isEmpty()) {
				smtLackTimeSheet = (StringMatrix) arrReturn.get(0x00);

//				int nRows = smtLackTimeSheet.getNumberOfRows();
//				int nCols = smtLackTimeSheet.getNumberOfCols();
				StringVector vecLackTimeSheet = null;

				for (int row=1; row<smtLackTimeSheet.getNumberOfRows(); row++) {
					vecLackTimeSheet = smtLackTimeSheet.getRow(row);
					Integer intDeveloperId = new Integer(vecLackTimeSheet.getCell(3));
					hashLackTimesheet.put(intDeveloperId, vecLackTimeSheet);
				}
			}

			//GET ASSIGNMENT
			if (colAssingment!= null && !colAssingment.isEmpty()) {
				Iterator it = colAssingment.iterator();
				while (it.hasNext()) {
					AssignmentInfo lackInfo = (AssignmentInfo) it.next();
					
					StringVector vecAssingment = new StringVector(5);
					vecAssingment.setCell(0, Integer.toString(lackInfo.getProjectID()));
					vecAssingment.setCell(1, Integer.toString(lackInfo.getDeveloperID()));
					vecAssingment.setCell(2, lackInfo.getDeveloperName());
					vecAssingment.setCell(3, lackInfo.getAccount());
					vecAssingment.setCell(4, lackInfo.getProjectCode());
					smtAssingment.addRow(vecAssingment);
				}
				
				//Put Project code, Full name, Lack Timesheet into smtLackTimeSheetResult to display
				StringVector vecHashLackTimeSheet = null;
				StringVector vecWeeklyReport = null;
				StringMatrix smtLackTimeSheetResult = new StringMatrix();
				
				for (int row=0; row<smtAssingment.getNumberOfRows(); row++) {
					vecHashLackTimeSheet = (StringVector) hashLackTimesheet.get(Integer.valueOf(smtAssingment.getCell(row, 1)));
					//System.out.println("HanhTN -- vecHashLackTimeSheet == " + vecHashLackTimeSheet);
					vecWeeklyReport = new StringVector(smtLackTimeSheet.getNumberOfCols());

					if (vecHashLackTimeSheet != null) {
						beanWeeklyReport.setProjectCode(smtAssingment.getCell(row, 4));
						beanWeeklyReport.setFullName(vecHashLackTimeSheet.getCell(1));
						beanWeeklyReport.setAccount(vecHashLackTimeSheet.getCell(2));
						beanWeeklyReport.setDeveloperId(vecHashLackTimeSheet.getCell(3));

						vecWeeklyReport.setCell(0, beanWeeklyReport.getProjectCode());
						vecWeeklyReport.setCell(1, beanWeeklyReport.getFullName());
						vecWeeklyReport.setCell(2, beanWeeklyReport.getAccount());
						vecWeeklyReport.setCell(3, beanWeeklyReport.getDeveloperId());

						for (int col=4; col<smtLackTimeSheet.getNumberOfCols(); col++) {
							beanWeeklyReport.setLackTimeSheetList(vecHashLackTimeSheet.getCell(col));
							vecWeeklyReport.setCell(col, beanWeeklyReport.getLackTimeSheetList());
						}
						smtLackTimeSheetResult.addRow(vecWeeklyReport);
					}
				}

				getLackTimeSheetTitleProject(smtLackTimeSheetResult,
                        formRequest.getFromDate(), formRequest.getToDate());
				getTotalTS(smtLackTimeSheetResult);
				beanWeeklyReport.setReportList(smtLackTimeSheetResult);
			}
		} //end try
		catch (javax.naming.NamingException e) {
			logger.debug("ReportBO.getTrackingReportProjectBO() -- Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in ReportBO.getTrackingReportProjectBO(): " + ex.toString());
			logger.error(ex);
		}
		return beanWeeklyReport;
	}

	/**
	 * Method getLackTimeSheetTitleProject
	 * @param smtLackTimeSheet
	 * @param strFromDate
	 * @param strToDate
	 * @return smtLackTimeSheet
	 */
	public StringMatrix getLackTimeSheetTitleProject(StringMatrix smtLackTimeSheet, String strFromDate, String strToDate) {
	
		StringVector vecLackTimeSheetTitle = null;		
		vecLackTimeSheetTitle = new StringVector(smtLackTimeSheet.getNumberOfCols());

		vecLackTimeSheetTitle.setCell(0, "Project code");
		vecLackTimeSheetTitle.setCell(1, "Account");
		vecLackTimeSheetTitle.setCell(2, "Full name");
		vecLackTimeSheetTitle.setCell(3, "Total number of TS lacking days");

		String[] arrWorkingDates = null;
		try {
			arrWorkingDates = DateUtils.getArrayOfWorkingDates(strFromDate, strToDate);
		}
		catch (Exception eMemory) {
			  return null;
		}
		int nNumberWorkingDates = arrWorkingDates.length;

		for (int intWD=0; intWD<nNumberWorkingDates; intWD++) {
			vecLackTimeSheetTitle.setCell(intWD + 4, arrWorkingDates[intWD]);
		}

		if (smtLackTimeSheet.getNumberOfRows() > 0) {
			smtLackTimeSheet.insertRow(0, vecLackTimeSheetTitle);
		}
		else {
			smtLackTimeSheet.addRow(vecLackTimeSheetTitle);
		}
		return smtLackTimeSheet;
	}

	/**
	 * Method getTotalTS
	 * Insert sub total into result matrix at nRow
	 * @param smtWeeklyReport
	 * @param nRow
	 * @param groupName
	 * @param arrTotal
	 */
	public void getTotalTS(StringMatrix smtWeeklyReport) {
		int row, col;
		int nCols = smtWeeklyReport.getNumberOfCols();
		int[] arr_Total = new int [nCols];
		Arrays.fill(arr_Total, 0);

		String oldGroupName = "";
		if (smtWeeklyReport.getNumberOfRows() > 0) {
			oldGroupName = smtWeeklyReport.getCell(1, 0);
		}

		//row 0 --> Title (Group name, Account, Full name, Total lacking days, [Dates]) 
		for (row=1; row<smtWeeklyReport.getNumberOfRows(); row++) {
			
			String groupName = smtWeeklyReport.getCell(row, 0);
			if ((row > 0) && (! oldGroupName.equals(groupName))) {
				insertTotalRow(smtWeeklyReport, row, oldGroupName, arr_Total);

				//reset total array to zero
				Arrays.fill(arr_Total, 0);
				oldGroupName = groupName;
				row = row + 1; // increase row by one for new inserted row
			}
			int count = 0;
			for (col=4; col<nCols; col++){
				if (smtWeeklyReport.getCell(row, col).equals("X")) {
					count++;
					arr_Total[col] += 1;
				}
			}
            // Replace column 4th (developerID) by total number of lacking days
			smtWeeklyReport.setCell(row, 3, Integer.toString(count));
			arr_Total[3] += count;
		}
        
		// If number of rows is > 0 then should calculate for the last group
		if (smtWeeklyReport.getNumberOfRows() > 0) {
			insertTotalRow(smtWeeklyReport, row, oldGroupName, arr_Total);
		}
	}
	
	/**
	 * Method insertTotalRow
	 * @param smtWeeklyReport
	 * @param row
	 * @param groupName
	 * @param arr_Total
	 */
	private void insertTotalRow(StringMatrix smtWeeklyReport, int row, String groupName, int[] arr_Total) {
		int nCols = smtWeeklyReport.getNumberOfCols();
		StringVector svtTotal = new StringVector(nCols);

		svtTotal.setCell(0, "<B>Total</B>");
		svtTotal.setCell(1, "");
		svtTotal.setCell(2, "");
		for (int col = 3; col < nCols; col++) {
			svtTotal.setCell(col, "<B>" + Integer.toString(arr_Total[col]) + "</B>");
		}
		smtWeeklyReport.insertRow(row, svtTotal);
	}

}