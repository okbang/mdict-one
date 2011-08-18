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
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.web.*;
import com.fms1.infoclass.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Products and size pages
 *
 */
public final class ModuleCaller{
	public static final int NUMBER_OF_ROW_ADDABLE = 10;
	
	public static final void moduleListCaller(final HttpServletRequest request, final HttpServletResponse response) {
		moduleListCaller( request,  response,"");
	}
	public static final void moduleListCaller(final HttpServletRequest request, final HttpServletResponse response,String index) {
		final HttpSession session = request.getSession();
		try {
			final long projectID = Long.parseLong(session.getAttribute("projectID").toString());
			final Vector moduleList = WorkProduct.getModuleListSize(projectID);
			final Vector WPSizeSumList = WorkProduct.getWPSizeSumList(projectID,moduleList);
			final ProjectSizeInfo sizeInfo = new ProjectSizeInfo(projectID);
            final Vector workproductList = WorkProduct.getWPList();

            session.setAttribute("workproductList", workproductList);
			session.setAttribute("moduleList", moduleList);
			session.setAttribute("projectSizeInfo", sizeInfo);
			session.setAttribute("WPSizeSumList", WPSizeSumList);
			
			if ("qual".equals(index)) {
				final Vector vtModule = WorkProduct.getModuleListSchedule(projectID, WorkProduct.ORDER_BY_PRELEASE);
				session.setAttribute("moduleVector", vtModule);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} 			
			else Fms1Servlet.callPage("moduleList.jsp"+index,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void moduleDetailCaller(final HttpServletRequest request, final HttpServletResponse response){
		final HttpSession session = request.getSession();
		try{
			final long projectID = Long.parseLong(session.getAttribute("projectID").toString());
			final int index = Integer.parseInt(request.getParameter("vtID"));
			Vector moduleList = (Vector)session.getAttribute("moduleList");
			WPSizeInfo moduleInfo = (WPSizeInfo)moduleList.elementAt(index);
			boolean hasLoc = false;
			if (moduleInfo.getHasLoc() == 1){ 
				hasLoc = WorkProduct.doCheckHasLoc(projectID, moduleInfo.moduleID, "PRODUCT_LOC_PLAN");
				if (!hasLoc){
					hasLoc = WorkProduct.doCheckHasLoc(projectID, moduleInfo.moduleID, "PRODUCT_LOC_ACTUAL"); 
				}
			}
			if (hasLoc){
				request.setAttribute("HasLoc", "1");
			}
			else{
				request.setAttribute("HasLoc", "0");
			}
			Fms1Servlet.callPage("moduleView.jsp?vtID=" + index, request, response);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public static final void modulePrep(HttpSession session){
			final Vector methodList = Param.getEstimationMethodList();
			final Vector languageList = Param.getLanguageList();
			final Vector workProductList = WorkProduct.getWPList();
			final Vector relevantMethodList = Param.getRelevantEstimationMethodList();
			final Vector relevantLanguageList = Param.getRelevantLanguageList();
			session.setAttribute("relevantMethodList", relevantMethodList);
			session.setAttribute("relevantLanguageList", relevantLanguageList);
			session.setAttribute("methodList", methodList);
			session.setAttribute("languageList", languageList);
			session.setAttribute("workProductList", workProductList);

	}
	public static final void moduleAddnewPrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			modulePrep(session);
			Fms1Servlet.callPage("moduleAddnew.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final long moduleAddnew(final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		try {
			final WPSizeInfo wpSize = new WPSizeInfo();
			wpSize.projectID = Integer.parseInt((String) session.getAttribute("projectID"));
			wpSize.name = request.getParameter("name");
			wpSize.categoryID = Integer.parseInt(request.getParameter("category"));
			String s = request.getParameter("estUnitID");
			wpSize.estimatedSizeUnitID = Integer.parseInt(s.substring(1, s.length()));
			wpSize.methodBasedSize = Integer.parseInt(s.substring(0, 1));
			wpSize.estimatedSize = Double.parseDouble(request.getParameter("estSize"));
			if (!request.getParameter("reestSize").equals(""))
				wpSize.reestimatedSize = Double.parseDouble(request.getParameter("reestSize"));
			s = request.getParameter("actUnitID");
			if (!"N/A".equals(s)) {
				wpSize.actualSizeUnitID = Integer.parseInt(s.substring(1, s.length()));
				wpSize.acMethodBasedSize = Integer.parseInt(s.substring(0, 1));
			}

			if (!request.getParameter("actSize").equals(""))
				wpSize.actualSize = Double.parseDouble(request.getParameter("actSize"));
			if (!request.getParameter("reuse").equals(""))
				wpSize.reusePercentage = Double.parseDouble(request.getParameter("reuse"));
			wpSize.description = request.getParameter("desc");
			wpSize.workProductCode = WorkProduct.getWPCode(wpSize.categoryID);
			return WorkProduct.addModule(wpSize);
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static final Vector moduleBatchAddnew(final HttpServletRequest request, Vector errVector) {		
		try {
			final HttpSession session = request.getSession();
			WPSizeInfo wpSize = null;
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			
			long iRet = 0;
			Vector vErrModule = new Vector();
			Vector result = new Vector();
			
			//String moduleID[] 		= request.getParameterValues("moduleID");
			String aName[] 			= request.getParameterValues("name");						
			String aCategory[] 		= request.getParameterValues("category");
			String aEstUnitID[]		= request.getParameterValues("estUnitID");
			String aEstSize[] 		= request.getParameterValues("estSize");
			String aReestSize[] 	= request.getParameterValues("reestSize");
			String aActUnitID[] 	= request.getParameterValues("actUnitID");
			String aActSize[] 		= request.getParameterValues("actSize"); 
			String aReuse[] 		= request.getParameterValues("reuse");			
			String aDesc[]			= request.getParameterValues("desc");
			
			int inputSize = aName.length;
			for(int i = 0; i < inputSize; i++) {
				if (aName[i] == null || "".equals(aName[i].trim())) continue;
				
				wpSize = new WPSizeInfo();
				wpSize.projectID = prjID;
				wpSize.name = ConvertString.toStandardizeString(aName[i]);
				wpSize.categoryID = Integer.parseInt(aCategory[i]);
				
				wpSize.estimatedSizeUnitID = Integer.parseInt(aEstUnitID[i].substring(1, aEstUnitID[i].length()));
				wpSize.methodBasedSize = Integer.parseInt(aEstUnitID[i].substring(0, 1));
				wpSize.estimatedSize = Double.parseDouble(aEstSize[i]);
				if (!"".equals(aReestSize[i]))
					wpSize.reestimatedSize = Double.parseDouble(aReestSize[i]);				
				if (!"N/A".equals(aActUnitID[i])) {
					wpSize.actualSizeUnitID = Integer.parseInt(aActUnitID[i].substring(1, aActUnitID[i].length()));
					wpSize.acMethodBasedSize = Integer.parseInt(aActUnitID[i].substring(0, 1));
				}
	
				if (!"".equals(aActSize[i]))
					wpSize.actualSize = Double.parseDouble(aActSize[i]);
				if (!"".equals(aReuse[i]))
					wpSize.reusePercentage = Double.parseDouble(aReuse[i]);
				wpSize.description = ConvertString.toStandardizeString(aDesc[i]);
				wpSize.workProductCode = WorkProduct.getWPCode(wpSize.categoryID);
				iRet = WorkProduct.addModule(wpSize);
				if (iRet == 0) {
					wpSize.updateOK = false;
					vErrModule.addElement(wpSize);
				} 
				result.addElement(wpSize);
			}
			if (errVector != null)	errVector = vErrModule;			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static final void moduleAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		String red = "";
		moduleAddnew(request);
		String fromPage = (String) request.getParameter("fromPage");
		if (fromPage != null) red = fromPage;
		moduleListCaller(request, response,red);
	}
	
	public static final void doBatchModuleAdd(final HttpServletRequest request, final HttpServletResponse response) {
		String red = "";
		Vector errModule = new Vector();
		Vector result = moduleBatchAddnew(request,errModule);
		if (errModule != null && errModule.size() == 0) { // incase no error			
			String fromPage = (String) request.getParameter("fromPage");
			if (fromPage != null) red = fromPage;
			moduleListCaller(request, response,red);
		}else {
			request.setAttribute("lastModule", errModule);
			request.setAttribute("ErrType","1");
			Fms1Servlet.callPage("moduleBatchAddnew.jsp",request,response);
		}
	}
	
	public static final void moduleUpdatePrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			modulePrep(session);
			Fms1Servlet.callPage("moduleNSizeUpdate.jsp?vtID="+request.getParameter("vtID"),request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final Vector moduleBatchUpdate(final HttpServletRequest request, Vector errVector) {
		
		try {
			final HttpSession session = request.getSession();
			WPSizeInfo wpSize = null;
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));

			boolean bRet = false;
			Vector vErrModule = new Vector();
			Vector vResult = new Vector();
			
			String moduleID[] 		= request.getParameterValues("moduleID");
			String aName[] 			= request.getParameterValues("name");						
			String aCategory[] 		= request.getParameterValues("category");
			String aEstUnitID[]		= request.getParameterValues("estUnitID");
			String aEstSize[] 		= request.getParameterValues("estSize");
			String aReestSize[] 	= request.getParameterValues("reestSize");
			String aActUnitID[] 	= request.getParameterValues("actUnitID");
			String aActSize[] 		= request.getParameterValues("actSize"); 
			String aReuse[] 		= request.getParameterValues("reuse");			
			String aDesc[]			= request.getParameterValues("desc");

			int inputSize = aName.length-1;
			for(int i = 0; i < inputSize; i++) {
				if (aName[i] == null || "".equals(aName[i].trim())) continue;
	
				wpSize = new WPSizeInfo();
				wpSize.moduleID = Long.parseLong(moduleID[i]);
				wpSize.projectID = prjID;
				wpSize.name = ConvertString.toStandardizeString(aName[i]);
				wpSize.categoryID = Integer.parseInt(aCategory[i]);
				
				wpSize.estimatedSizeUnitID = Integer.parseInt(aEstUnitID[i].substring(1, aEstUnitID[i].length()));
				wpSize.methodBasedSize = Integer.parseInt(aEstUnitID[i].substring(0, 1));
				wpSize.estimatedSize = Double.parseDouble(aEstSize[i]);
				if (!"".equals(aReestSize[i]))
					wpSize.reestimatedSize = Double.parseDouble(aReestSize[i]);				
				if (!"N/A".equals(aActUnitID[i])) {
					wpSize.actualSizeUnitID = Integer.parseInt(aActUnitID[i].substring(1, aActUnitID[i].length()));
					wpSize.acMethodBasedSize = Integer.parseInt(aActUnitID[i].substring(0, 1));
				}

				if (!"".equals(aActSize[i]))
					wpSize.actualSize = Double.parseDouble(aActSize[i]);
				if (!"".equals(aReuse[i]))
					wpSize.reusePercentage = Double.parseDouble(aReuse[i]);
				wpSize.description = ConvertString.toStandardizeString(aDesc[i]);
				wpSize.workProductCode = WorkProduct.getWPCode(wpSize.categoryID);
				bRet = WorkProduct.setModule(wpSize);
				if (!bRet) {
					wpSize.updateOK = false;
					vErrModule.addElement(wpSize);
				}
				vResult.addElement(wpSize);
			}
			if (errVector != null)	errVector = vErrModule;
			return vResult;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final long moduleUpdate(final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		try {
			final WPSizeInfo wpSize = new WPSizeInfo();
			wpSize.projectID = Integer.parseInt((String) session.getAttribute("projectID"));
			wpSize.name = request.getParameter("name");
			wpSize.categoryID = Integer.parseInt(request.getParameter("category"));
			String s = request.getParameter("estUnitID");
			wpSize.estimatedSizeUnitID = Integer.parseInt(s.substring(1, s.length()));
			wpSize.methodBasedSize = Integer.parseInt(s.substring(0, 1));
			wpSize.estimatedSize = Double.parseDouble(request.getParameter("estSize"));
			String reestSize =request.getParameter("reestSize");
			String actSize=request.getParameter("actSize");
			String reuse=request.getParameter("reuse");
			if (!"".equals(reestSize))
				wpSize.reestimatedSize = Double.parseDouble(reestSize);
			s = request.getParameter("actUnitID");
			if (!s.equals("null")) {
				wpSize.actualSizeUnitID = Integer.parseInt(s.substring(1, s.length()));
				wpSize.acMethodBasedSize = Integer.parseInt(s.substring(0, 1));
			}
			if (!"".equals(actSize))
				wpSize.actualSize = Double.parseDouble(actSize);
			if (!"".equals(reuse))
				wpSize.reusePercentage = Double.parseDouble(reuse);
			wpSize.description = request.getParameter("desc");


			Vector moduleList = (Vector)session.getAttribute("moduleList");
			WPSizeInfo oldModuleInfo = (WPSizeInfo)moduleList.elementAt(Integer.parseInt(request.getParameter("vtID")));
			wpSize.moduleID = oldModuleInfo.moduleID;
			wpSize.workProductCode = WorkProduct.getWPCode(wpSize.categoryID);
			WorkProduct.setModule(wpSize);
			return wpSize.moduleID;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	public static final void moduleUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		moduleUpdate(request);
		//if it is the popup from sched page dont forward
		if (!"1".equals(request.getParameter("fromsched")))
			moduleListCaller(request, response,"");
		else
			try {
				Fms1Servlet.callPage("closeMe.html",request,response);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public static final void doBatchModuleUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Vector errVector = new Vector();
			Vector result = moduleBatchUpdate(request, errVector);
			//if it is the popup from sched page dont forward
			if (!"1".equals(request.getParameter("fromsched")))
				moduleListCaller(request, response,"");
			else {
				if (errVector.size() > 0) {
					request.setAttribute("lastModule", errVector);
					request.setAttribute("ErrType", "1");
					Fms1Servlet.callPage("moduleBatchUpdate.jsp",request,response);
				} else moduleListCaller(request, response,"");			
			}	
				
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// batch delete module
	public static final void doBatchModuleDelete(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int moduleID = 0;			
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			String listUpdate = (String) request.getParameter("listChecked");
			
			final StringTokenizer strModuleUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			
			/*
			WPSizeInfo moduleInfo = null;
			if (WorkProduct.delBatchModule("("+listUpdate+")")) {
				WorkOrderCaller.addChangeAuto(
				prjID,
				Constants.ACTION_DELETE,
				Constants.BATCH_MODULE_DELETE,
				"ModuleID "+listUpdate,
				null,
				null);
			}
			
			final Vector moduleList = WorkProduct.getModuleListSize(prjID);
			final Vector WPSizeSumList = WorkProduct.getWPSizeSumList(prjID,moduleList);
			final ProjectSizeInfo sizeInfo = new ProjectSizeInfo(prjID);
			final Vector workproductList = WorkProduct.getWPList();

			session.setAttribute("workproductList", workproductList);
			session.setAttribute("moduleList", moduleList);
			session.setAttribute("projectSizeInfo", sizeInfo);
			session.setAttribute("WPSizeSumList", WPSizeSumList);
			Fms1Servlet.callPage("moduleList.jsp",request,response);
			*/
			
			while (strModuleUpdateIDList.hasMoreElements()) {
				moduleID = Integer.parseInt(strModuleUpdateIDList.nextToken());				
				if (WorkProduct.delModule(moduleID)) {
					WorkOrderCaller.addChangeAuto(
						prjID,
						Constants.ACTION_DELETE,
						Constants.BATCH_MODULE_DELETE,
						"ModuleID "+moduleID,
						null,
						null);
				}
			}
			
			moduleListCaller(request, response,"");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void moduleDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			Vector moduleList = (Vector)session.getAttribute("moduleList");
			int vtID=Integer.parseInt(request.getParameter("vtID"));
			WPSizeInfo moduleInfo = (WPSizeInfo)moduleList.elementAt(vtID);
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			WorkProduct.delModule(moduleInfo.moduleID);
			if (moduleInfo.isDel == 1) {
				WorkOrderCaller.addChangeAuto(
					prjID,
					Constants.ACTION_DELETE,
					Constants.MODULE_MODULE,
					moduleInfo.name,
					null,
					null);
			}
			moduleListCaller(request, response,"");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Get project's products that have LOC
     * @param request
     * @param response
     */
    public static final void productLocListingCaller(
        final HttpServletRequest request, final HttpServletResponse response)
    {
        final HttpSession session = request.getSession();
        final long projectId =
                Long.parseLong((String) session.getAttribute("projectID"));
        ProjectInfo projectInfo = Project.getProjectInfo(projectId);
        // Get project LOC summary
        WPSizeInfo projectLocSummary = new WPSizeInfo();
        // Get project LOC by stage
        Vector projectLocByStage = new Vector();
        // Get project LOC by language (list of ProductLocInfo)
        Vector projectLanguages = new Vector();
        // Get project LOC by product (list of NormInfo, normName is for
        // product name, normUnit is for work product name)
        Vector productSizeList = new Vector();

        // Generate structure of product LOC listing page
        WorkProduct.createProductLocListing(projectInfo, projectLocSummary,
            projectLocByStage, projectLanguages, productSizeList);
        request.setAttribute("projectLocSummary", projectLocSummary);
        request.setAttribute("projectLocByStage", projectLocByStage);
        request.setAttribute("projectLanguages", projectLanguages);
        request.setAttribute("productSizeList", productSizeList);
        // Get numbers of products does not have LOC, if there are no product
        // found then disable Add new LOC
        Integer productsWithoutLoc =
            WorkProduct.getProductsNumbersWithoutLOC(projectId);
        request.setAttribute("productsWithoutLoc", productsWithoutLoc);
        Fms1Servlet.callPage("productLocList.jsp",request,response);
    }
    
    /**
     * Get product LOC detail information
     * @param request
     * @param response
     */
    public static final void productLocDetailCaller(
        final HttpServletRequest request, final HttpServletResponse response)
    {
        final HttpSession session = request.getSession();
        final long projectId =
                Long.parseLong((String) session.getAttribute("projectID"));
        final long moduleId = Integer.parseInt(request.getParameter("product"));
        
        ProductLocDetailInfo locDetail =
            WorkProduct.getProductLocDetail(projectId, moduleId);
        request.setAttribute("locDetail", locDetail);
        Fms1Servlet.callPage("productLocDetail.jsp",request,response);
    }
    
    /**
     * Get product LOC update screen
     * @param request
     * @param response
     */
    public static final void productLocUpdateCaller(
        final HttpServletRequest request, final HttpServletResponse response)
    {
        final HttpSession session = request.getSession();
        final long projectId =
                Long.parseLong((String) session.getAttribute("projectID"));
        final long moduleId =
                Long.parseLong(request.getParameter("productId"));
        ProductLocDetailInfo locDetail =
            WorkProduct.getProductLocDetail(projectId, moduleId);
        request.setAttribute("locDetail", locDetail);
        Fms1Servlet.callPage("productLocUpdate.jsp",request,response);
    }
    
    /**
     * Save product LOC failed handler
     * @param request
     * @param response
     * @param planLocs
     * @param actualLocs
     */
    private static final void saveProductLocErrorHandler(
        final HttpServletRequest request, final HttpServletResponse response,
        Vector planLocs, Vector actualLocs, String pageName)
    {
        final HttpSession session = request.getSession();
        final long projectId =
                Long.parseLong((String) session.getAttribute("projectID"));
        final long moduleId =
                Long.parseLong(request.getParameter("product"));
        Vector products = WorkProduct.getModuleList(projectId, moduleId);
        WPSizeInfo moduleInfo = new WPSizeInfo();
        if (products.size() > 0) {
            moduleInfo = (WPSizeInfo) products.get(0);
        }
        ProductLocDetailInfo locDetail = new ProductLocDetailInfo();
        locDetail.setProductDetail(moduleInfo);
        // Get language names
        for (int i = 0; i < planLocs.size(); i++) {
            ProductLocInfo loc = (ProductLocInfo)planLocs.get(i);
            loc.setLanguageName(Param.getLanguageName((int)loc.getLanguageId()));
        }
        for (int i = 0; i < actualLocs.size(); i++) {
            ProductLocInfo loc = (ProductLocInfo)actualLocs.get(i);
            loc.setLanguageName(Param.getLanguageName((int)loc.getLanguageId()));
        }
        locDetail.setActualLocs(actualLocs);
        locDetail.setPlanLocs(planLocs);
        request.setAttribute("locDetail", locDetail);
        request.setAttribute("isSaved", new Boolean(false));
        // Put information for add product LOC page
        if ("productLocAdd.jsp".equals(pageName)) {
            Vector workproductList = WorkProduct.getWPList(true);
            Vector productList = WorkProduct.getProductsWithoutLOC(projectId);
            request.setAttribute("workproductList", workproductList);
            request.setAttribute("productList", productList);
        }
        Fms1Servlet.callPage(pageName,request,response);
    }
    /**
     * Save product LOC (plan, actual)
     * @param request
     * @param response
     */
    public static final void productLocSaveUpdateCaller(
        final HttpServletRequest request, final HttpServletResponse response)
    {
        Vector planLocs = new Vector();
        Vector actualLocs = new Vector();
        getUpdateLocData(request, planLocs, actualLocs);
        // Save new LOC information
        boolean isSaved =
            WorkProduct.saveProductLocUpdate(planLocs, actualLocs);
        if (isSaved) { // If saved sucessful then return listing page
            productLocListingCaller(request,response);
        }
        else { //Error, forward to update LOC page with warning message
            saveProductLocErrorHandler(request, response, planLocs, actualLocs,
                    "productLocUpdate.jsp");
        }
    }
    
    /**
     * Get product LOC add new screen
     * @param request
     * @param response
     */
    public static final void productLocAddCaller(
        final HttpServletRequest request, final HttpServletResponse response)
    {
        final HttpSession session = request.getSession();
        final long projectId =
                Long.parseLong((String) session.getAttribute("projectID"));
        Vector workproductList = WorkProduct.getWPList(true);
        Vector productList = WorkProduct.getProductsWithoutLOC(projectId);
        request.setAttribute("workproductList", workproductList);
        request.setAttribute("productList", productList);
        Fms1Servlet.callPage("productLocAdd.jsp",request,response);
    }
    
    /**
     * Save product LOC add new
     * @param request
     * @param response
     */
    public static final void productLocSaveNewCaller(
        HttpServletRequest request, HttpServletResponse response)
    {
        Vector planLocs = new Vector();
        Vector actualLocs = new Vector();
        getAddLocData(request, planLocs, actualLocs);
        // Save new LOC information
        boolean isSaved =
            WorkProduct.saveProductLocAddNew(planLocs, actualLocs);
        if (isSaved) {  // If saved sucessful then return listing page
            productLocListingCaller(request,response);
        }
        else { //Error, forward to add LOC page with warning message
            saveProductLocErrorHandler(request, response, planLocs, actualLocs,
                    "productLocAdd.jsp");
        }
    }

    /**
     * Put data of plan/actual LOC to a vector for Add product LOC form
     * @param locList
     * @param projectId
     * @param moduleId
     * @param languageList
     * @param motherBodyList
     * @param addedList
     * @param modifiedList
     * @param totalLocList
     * @param reusedLocList
     * @param generatedLocList
     * @param noteList
     */
    private static void putToAddLocList(Vector locList,
        long projectId, long moduleId,
        String[] languageList, String[] motherBodyList, String[] addedList,
        String[] modifiedList, String[] totalLocList, String[] reusedLocList,
        String[] generatedLocList, String[] noteList)
    {
        for (int i = 0; i < languageList.length; i++) {
            // If selected a language in this line
            if (Integer.parseInt(languageList[i]) != -1) {
                ProductLocInfo loc = new ProductLocInfo();
                loc.setProjectId(projectId);
                loc.setModuleId(moduleId);
                loc.setLanguageId(Integer.parseInt(languageList[i]));
                loc.setMotherBody(CommonTools.parseDouble(motherBodyList[i]));
                loc.setAdded(CommonTools.parseDouble(addedList[i]));
                loc.setModified(CommonTools.parseDouble(modifiedList[i]));
                loc.setTotal(CommonTools.parseDouble(totalLocList[i]));
                loc.setReused(CommonTools.parseDouble(reusedLocList[i]));
                loc.setGenerated(CommonTools.parseDouble(generatedLocList[i]));
                loc.setNote(noteList[i]);
                loc.calculateLocForProductivityQuality();
                if (! loc.inputDataIsEmpty()) {
                    locList.add(loc);
                }
            }
        }
    }
    /**
     * Get planned/actual LOC form data posted from client (Add product LOC)
     * @param request
     * @param planLocs
     * @param actualLocs
     */
    private static void getAddLocData(HttpServletRequest request,
        Vector planLocs, Vector actualLocs)
    {
        final HttpSession session = request.getSession();
        final long projectId =
                Long.parseLong((String) session.getAttribute("projectID"));
        /*
         * Because product Id is used both for Add product LOC and
         * Update product LOC forms so both of them should have an element
         * named "product" 
         * */
        final long moduleId = Integer.parseInt(request.getParameter("product"));
        try {
            String[] planLanguage = request.getParameterValues("plan_language");
            String[] planMotherBody = request.getParameterValues("plan_motherBody");
            String[] planAdded = request.getParameterValues("plan_added");
            String[] planModified = request.getParameterValues("plan_modified");
            String[] planTotalLoc = request.getParameterValues("plan_actualLOC");
            String[] planReusedLoc = request.getParameterValues("plan_reused");
            String[] planGeneratedLoc = request.getParameterValues("plan_generatedLOC");
            String[] planNote = request.getParameterValues("plan_note");
            putToAddLocList(planLocs, projectId, moduleId,
                    planLanguage, planMotherBody, planAdded,
                    planModified, planTotalLoc, planReusedLoc,
                    planGeneratedLoc, planNote);

            String[] actualLanguage = request.getParameterValues("language");
            String[] actualMotherBody = request.getParameterValues("motherBody");
            String[] actualAdded = request.getParameterValues("added");
            String[] actualModified = request.getParameterValues("modified");
            String[] actualTotalLoc = request.getParameterValues("actualLOC");
            String[] actualReusedLoc = request.getParameterValues("reused");
            String[] actualGeneratedLoc = request.getParameterValues("generatedLOC");
            String[] actualNote = request.getParameterValues("note");
            putToAddLocList(actualLocs, projectId, moduleId,
                    actualLanguage, actualMotherBody, actualAdded,
                    actualModified, actualTotalLoc, actualReusedLoc,
                    actualGeneratedLoc, actualNote);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Put data of plan/actual LOC to a vector for Update product LOC form
     * @param locList
     * @param projectId
     * @param moduleId
     * @param productLocIdList
     * @param languageList
     * @param motherBodyList
     * @param addedList
     * @param modifiedList
     * @param totalLocList
     * @param reusedLocList
     * @param generatedLocList
     * @param noteList
     */
    private static void putToUpdateLocList(Vector locList,
        long projectId, long moduleId, String[] productLocIdList,
        String[] languageList, String[] motherBodyList, String[] addedList,
        String[] modifiedList, String[] totalLocList, String[] reusedLocList,
        String[] generatedLocList, String[] noteList)
    {
        int productLocId = 0;
        for (int i = 0; i < languageList.length; i++) {
            productLocId = Integer.parseInt(productLocIdList[i]);
            // If selected a language in this line
            if (Integer.parseInt(languageList[i]) != -1) {
                ProductLocInfo loc = new ProductLocInfo();
                loc.setProductLocId(productLocId);
                loc.setProjectId(projectId);
                loc.setModuleId(moduleId);
                loc.setLanguageId(Integer.parseInt(languageList[i]));
                loc.setMotherBody(CommonTools.parseDouble(motherBodyList[i]));
                loc.setAdded(CommonTools.parseDouble(addedList[i]));
                loc.setModified(CommonTools.parseDouble(modifiedList[i]));
                loc.setTotal(CommonTools.parseDouble(totalLocList[i]));
                loc.setReused(CommonTools.parseDouble(reusedLocList[i]));
                loc.setGenerated(CommonTools.parseDouble(generatedLocList[i]));
                loc.setNote(noteList[i]);
                loc.calculateLocForProductivityQuality();
                if (productLocId <= 0) {    // Selected a new language
                    loc.setDmlType(ProductLocInfo.DML_INSERT);
                }
                else {  // Updated an existing line
                    loc.setDmlType(ProductLocInfo.DML_UPDATE);
                }
                // If reset all fields to blank => delete this language LOC
                if (loc.inputDataIsEmpty()) {
                    loc.setDmlType(ProductLocInfo.DML_DELETE);
                }
                locList.add(loc);
            }
            // De-selected a language => delete this language LOC information
            else if (productLocId > 0) {
                ProductLocInfo loc = new ProductLocInfo();
                loc.setProductLocId(productLocId);
                loc.setProjectId(projectId);
                loc.setModuleId(moduleId);
                loc.setDmlType(ProductLocInfo.DML_DELETE);
                locList.add(loc);
            }
        }
    }

    /**
     * Get planned/actual LOC form data posted from client (Update product LOC)
     * @param request
     * @param planLocs
     * @param actualLocs
     */
    private static void getUpdateLocData(HttpServletRequest request,
        Vector planLocs, Vector actualLocs)
    {
        final HttpSession session = request.getSession();
        final long projectId =
                Long.parseLong((String) session.getAttribute("projectID"));
        /*
         * Because product Id is used both for Add product LOC and
         * Update product LOC forms so both of them should have an element
         * named "product" 
         * */
        final long moduleId = Integer.parseInt(request.getParameter("product"));
        try {
            String[] planProductLocId = request.getParameterValues("plan_product_loc_id");
            String[] planLanguage = request.getParameterValues("plan_language");
            String[] planMotherBody = request.getParameterValues("plan_motherBody");
            String[] planAdded = request.getParameterValues("plan_added");
            String[] planModified = request.getParameterValues("plan_modified");
            String[] planTotalLoc = request.getParameterValues("plan_actualLOC");
            String[] planReusedLoc = request.getParameterValues("plan_reused");
            String[] planGeneratedLoc = request.getParameterValues("plan_generatedLOC");
            String[] planNote = request.getParameterValues("plan_note");
            putToUpdateLocList(planLocs, projectId, moduleId,
                    planProductLocId,
                    planLanguage, planMotherBody, planAdded,
                    planModified, planTotalLoc, planReusedLoc,
                    planGeneratedLoc, planNote);

            String[] actualProductLocId = request.getParameterValues("product_loc_id");
            String[] actualLanguage = request.getParameterValues("language");
            String[] actualMotherBody = request.getParameterValues("motherBody");
            String[] actualAdded = request.getParameterValues("added");
            String[] actualModified = request.getParameterValues("modified");
            String[] actualTotalLoc = request.getParameterValues("actualLOC");
            String[] actualReusedLoc = request.getParameterValues("reused");
            String[] actualGeneratedLoc = request.getParameterValues("generatedLOC");
            String[] actualNote = request.getParameterValues("note");
            putToUpdateLocList(actualLocs, projectId, moduleId,
                    actualProductLocId,
                    actualLanguage, actualMotherBody, actualAdded,
                    actualModified, actualTotalLoc, actualReusedLoc,
                    actualGeneratedLoc, actualNote);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
	public static final void moduleImport(final HttpServletRequest request,final HttpServletResponse response) throws BiffException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
	
			try {
				FileItem formFileItem = processFormFieldTeam(request, upload);
				InputStream inStream = formFileItem.getInputStream();
				readImportFileTeam(request, inStream);
				moduleListCaller(request, response,"");
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
	}
	private static final FileItem processFormFieldTeam(HttpServletRequest request, ServletFileUpload upload) throws FileUploadException {
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			FileItem item = null;
			while (iter.hasNext()) {
				FileItem fileItem = (FileItem) iter.next();

				if (fileItem.isFormField()) {
				//	processFormField(item);
				} else {
					item = fileItem;
				}
			}
			return item;
		}
	private static final void readImportFileTeam(HttpServletRequest request, InputStream inStream) throws BiffException, IOException {
		final HttpSession session = request.getSession();
		Workbook workbook = Workbook.getWorkbook(inStream);

		Sheet sheet0 = workbook.getSheet(0);
		try{
			if(!sheet0.getCell(1, 79).getContents().trim().equalsIgnoreCase("Import Product")){
				session.setAttribute("ImportFail","fail");
				return;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			session.setAttribute("ImportFail","fail");
			return;
		}	
		//	import Team
		Sheet sheet = workbook.getSheet(1);
		
		  int[] added = new int[50];
		  for(int i=0;i<50;i++){
			  added[i] = 0;
		  }
		  
		  final WPSizeInfo wpSize = new WPSizeInfo();
		  wpSize.projectID = Integer.parseInt((String) session.getAttribute("projectID"));
		 
		  int k=0;
		  for(int i=1;i<51;i++){
			  final ModuleInfo moduleInfo = new ModuleInfo();
			  NumberCell check = (NumberCell) sheet.getCell(0, i); 

			  if(check.getValue() > 0 ){
				  wpSize.categoryID = Integer.parseInt(sheet.getCell(1, i).getContents());
				  wpSize.name = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(2, i).getContents());
				  wpSize.estimatedSizeUnitID = Integer.parseInt(sheet.getCell(3, i).getContents());
				  if(wpSize.estimatedSizeUnitID <= 7){
					wpSize.methodBasedSize = 1;
				  }else{
					wpSize.methodBasedSize = 0;
				  }
				  wpSize.estimatedSize = Double.parseDouble(sheet.getCell(4, i).getContents());
				  if (!sheet.getCell(5, i).getContents().equals("0"))
					  wpSize.reestimatedSize = Double.parseDouble(sheet.getCell(5, i).getContents());
				  if (!"0".equals(sheet.getCell(6, i).getContents())) {
					  wpSize.actualSizeUnitID = Integer.parseInt(sheet.getCell(6, i).getContents());
					  if(wpSize.actualSizeUnitID <= 7){
						wpSize.acMethodBasedSize = 1;
					  }else{
						wpSize.acMethodBasedSize = 0;
					  }
				  }
		
				  if (!sheet.getCell(7, i).getContents().equals("0"))
					  wpSize.actualSize = Double.parseDouble(sheet.getCell(7, i).getContents());
				  if (!sheet.getCell(8, i).getContents().equals("0"))
					  wpSize.reusePercentage = Double.parseDouble(sheet.getCell(8, i).getContents());
				  wpSize.description = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(9, i).getContents());
				  wpSize.workProductCode = WorkProduct.getWPCode(wpSize.categoryID);
				  
				  if(WorkProduct.addModule(wpSize) > 0){
					  added[k] = i;
				  }else{
					  added[k] = -i;
				  }
				  k++;
			  }
		  }
		  session.setAttribute("AddedRecord",added);				
		  session.setAttribute("Imported","true");
	}
	
	public static final void doPrepareBatchModuleAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			modulePrep(session);
			Fms1Servlet.callPage("moduleBatchAddnew.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareBatchModuleUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			String listUpdate = (String) request.getParameter("listChecked");
			Vector moduleList = (Vector) session.getAttribute("moduleList");
			Vector vUpdate = new Vector();
			modulePrep(session);
			
			
			final StringTokenizer strModuleUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			Vector vUpdateListId = new Vector();			
			while (strModuleUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strModuleUpdateIDList.nextToken());				
			}

			int mSize = moduleList.size();
			for(int i = 0; i < mSize; i++){				
				WPSizeInfo info = (WPSizeInfo) moduleList.elementAt(i);
				if ( vUpdateListId.contains(info.moduleID+"")) {
					vUpdate.addElement(info);
				}
			}
			request.setAttribute("ModuleBatchUpdateList",vUpdate);
			Fms1Servlet.callPage("moduleBatchUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
