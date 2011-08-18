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
 
 package fpt.dms.bean.DefectManagement;

import fpt.dms.framework.util.StringUtil.StringMatrix;

public class DefectAddBean {
	private String strDefectID = "";
	private int nStatus = 0;
	private String strCreateUser = "";
	private String strCreateDate = "";
	private int nWorkProduct = 9; // default Work Product is Software Package
	private String strTitle = "";
	private int nSeverity = 0;
	private int nModuleCode = 0;
	private int nTypeofActivity = 1;
	private int nPriority = 2;
	// default activity is Testprivate int nPriority = 2;   // default=2 - High priority
	private int nType = 0;
	private String strDescription = "";
	private String strAssignTo = "";
	private String strDueDate = "";
	private String strCloseDate = "";
	private int nStageDetected = 0;
	private int nQCActivity = 0;
	private int nStageInjected = 0;
	private int nDefectOrigin = 4; //4: CODING Process
	private String strCorrectiveAction = "";
	private String strCauseAnalysis = ""; //add by HUYNH2
	private String strTestCase = ""; // add by HUYNH2
	private String strImage = "";
	private String strProject_Origin = "";
	private String Reference = "";
	private String strFixedDate = null;
	private String defectOwner = ""; // add by MINHVQ

	///////////////////////////////////////////////
	public DefectAddBean() {
	}

	public String getDefectID() {
		return strDefectID;
	}

	public void setDefectID(String strDefectID) {
		if (strDefectID != null) {
			this.strDefectID = strDefectID;
		}
	}

	public int getStatus() {
		return nStatus;
	}

	public void setStatus(int iStatus) {
		this.nStatus = iStatus;
	}

	public String getCreateUser() {
		return strCreateUser;
	}

	public void setCreateUser(String strData) {
		if (strData != null) {
			strCreateUser = strData;
		}
	}

	public String getCreateDate() {
		return strCreateDate;
	}
	
	public void setCreateDate(String strCreateDate) {
		if (strCreateDate != null) {
			this.strCreateDate = strCreateDate;
		}
	}

	public int getWorkProduct() {
		return nWorkProduct;
	}
  
	public void setWorkProduct(int iWorkProduct) {
		nWorkProduct = iWorkProduct;
	}

	public String getTitle() {
		return strTitle;
	}
  	
	public void setTitle(String strTitle) {
		if (strTitle != null) {
			this.strTitle = strTitle;
		}
	}

	public int getSeverity() {
		return nSeverity;
	}
  	
	public void setSeverity(int iSeverity) {
		this.nSeverity = iSeverity;
	}

	public int getModuleCode() {
		return nModuleCode;
	}
  
	public void setModuleCode(int iModuleCode) {
		this.nModuleCode = iModuleCode;
	}

	public int getTypeofActivity() {
		return nTypeofActivity;
	}
  
	public void setTypeofActivity(int iTypeofActivity) {
		this.nTypeofActivity = iTypeofActivity;
	}

	public int getPriority() {
		return nPriority;
	}
	
	public void setPriority(int iPriority) {
		this.nPriority = iPriority;
	}

	public int getType() {
		return nType;
	}

	public void setType(int iType) {
		this.nType = iType;
	}

	public String getDescription() {
		return strDescription;
	}

	public void setDescription(String strDescription) {
		if (strDescription != null) {
			this.strDescription = strDescription;
		}
	}

	public String getAssignTo() {
		return strAssignTo;
	}

	public void setAssignTo(String strAssignTo) {
		if (strAssignTo != null) {
			this.strAssignTo = strAssignTo;
		}
	}

	public String getDueDate() {
		return strDueDate;
	}

	public void setDueDate(String strDueDate) {
		if (strDueDate != null) {
			this.strDueDate = strDueDate;
		}
	}

	public String getCloseDate() {
		return strCloseDate;
	}

	public void setCloseDate(String strCloseDate) {
		if (strCloseDate != null) {
			this.strCloseDate = strCloseDate;
		}
	}

	public int getStageDetected() {
		return nStageDetected;
	}

	public void setStageDetected(int iStageDetected) {
		this.nStageDetected = iStageDetected;
	}

	public int getQCActivity() {
		return nQCActivity;
	}

	public void setQCActivity(int iQCActivity) {
		this.nQCActivity = iQCActivity;
	}

	public int getStageInjected() {
		return nStageInjected;
	}

	public void setStageInjected(int iStageInjected) {
		this.nStageInjected = iStageInjected;
	}

	public int getDefectOrigin() {
		return nDefectOrigin;
	}

	public void setDefectOrigin(int iDefectOrigin) {
		this.nDefectOrigin = iDefectOrigin;
	}

	public String getCorrectiveAction() {
		return strCorrectiveAction;
	}

	public void setCorrectiveAction(String strCorrectiveAction) {
		if (strCorrectiveAction != null) {
			this.strCorrectiveAction = strCorrectiveAction;
		}
	}

	// Add By HUYNH2
	public String getCauseAnalysis() {
		return strCauseAnalysis;
	}

	public void setCauseAnalysis(String strCauseAnalysis) {
		if (strCauseAnalysis != null) {
			this.strCauseAnalysis = strCauseAnalysis.trim();
		} else {
			this.strCauseAnalysis = "";
		}
	}

	public String getTestCase() {
		return strTestCase;
	}

	public void setTestCase(String strTestCase) {
		if (strTestCase != null) {
			this.strTestCase = strTestCase;
		}
	}

	// written by minhvq
	public String getDefectOwner() {
		return defectOwner;
	}

	public void setDefectOwner(String strDefectOwner) {
		if (strDefectOwner != null) {
			this.defectOwner = strDefectOwner;
		}
		else {
			this.defectOwner = "";
		}
	}
  
  	// ****************************** Extend Code ******************************
  	// WorkProduct Combo:
  	
	private StringMatrix smxComboWorkProduct;
	public StringMatrix getComboWorkProduct() {
		return smxComboWorkProduct;
	}

	public void setComboWorkProduct(StringMatrix smxComboWorkProduct) {
		this.smxComboWorkProduct = smxComboWorkProduct;
	}

	// Severity Combo:
	private StringMatrix smxComboSeverity;
	public StringMatrix getComboSeverity() {
		return smxComboSeverity;
	}

	public void setComboSeverity(StringMatrix smxComboSeverity) {
		this.smxComboSeverity = smxComboSeverity;
	}

	// ModuleCode Combo:
	private StringMatrix smxComboModuleCode;
	public StringMatrix getComboModuleCode() {
		return smxComboModuleCode;
	}

	public void setComboModuleCode(StringMatrix smxComboModuleCode) {
		this.smxComboModuleCode = smxComboModuleCode;
	}

	// ModuleCode Combo:
	private StringMatrix smxComboTypeofActivity;
	public StringMatrix getComboTypeofActivity() {
		return smxComboTypeofActivity;
	}

	public void setComboTypeofActivity(StringMatrix smxComboTypeofActivity) {
		this.smxComboTypeofActivity = smxComboTypeofActivity;
	}

	// ModuleCode Combo:
	private StringMatrix smxComboPriority;
	public StringMatrix getComboPriority() {
		return smxComboPriority;
	}

	public void setComboPriority(StringMatrix smxComboPriority) {
		this.smxComboPriority = smxComboPriority;
	}

	// ModuleCode Combo:
	private StringMatrix smxComboType;
	public StringMatrix getComboType() {
		return smxComboType;
	}

	public void setComboType(StringMatrix smxComboType) {
		this.smxComboType = smxComboType;
	}

	// ModuleCode Combo:
	private StringMatrix smxComboAssignTo;
	public StringMatrix getComboAssignTo() {
		return smxComboAssignTo;
	}

	public void setComboAssignTo(StringMatrix smxComboAssignTo) {
		this.smxComboAssignTo = smxComboAssignTo;
	}
	//ModuleCode Combo:
	private StringMatrix smxComboDefectOwner;
	public StringMatrix getComboDefectOwner() {
		return smxComboDefectOwner;
	}

	public void setComboDefectOwner(StringMatrix smxComboDefectOwner) {
		this.smxComboDefectOwner = smxComboDefectOwner;
	}
	// StageDetected Combo:

	private StringMatrix smxComboStageDetected;
	public StringMatrix getComboStageDetected() {
		return smxComboStageDetected;
	}

	public void setComboStageDetected(StringMatrix smxComboStageDetected) {
		this.smxComboStageDetected = smxComboStageDetected;
	}

	// ModuleCode Combo:
	private StringMatrix smxComboQCActivity;
	public StringMatrix getComboQCActivity() {
		return smxComboQCActivity;
	}

	public void setComboQCActivity(StringMatrix smxComboQCActivity) {
		this.smxComboQCActivity = smxComboQCActivity;
	}

	// ModuleCode Combo:
	private StringMatrix smxComboStageInjected;
	public StringMatrix getComboStageInjected() {
		return smxComboStageInjected;
	}

	public void setComboStageInjected(StringMatrix smxComboStageInjected) {
		this.smxComboStageInjected = smxComboStageInjected;
	}

	// DefectOrigin Combo:
	private StringMatrix smxComboDefectOrigin;
	public StringMatrix getComboDefectOrigin() {
		return smxComboDefectOrigin;
	}
	public void setComboDefectOrigin(StringMatrix smxComboDefectOrigin) {
		this.smxComboDefectOrigin = smxComboDefectOrigin;
	}

	public String getStrProject_Origin() {
		return strProject_Origin;
	}

	public String getStrImage() {
		return strImage;
	}
	
	public void setStrImage(String inData) {
		if (inData != null) {
			this.strImage = inData.trim();
		}
		else {
			this.strImage = "";
		}
	}

	public void setStrProject_Origin(String inData) {
		if (inData != null) {
			this.strProject_Origin = inData.trim();
		}
		else {
			this.strProject_Origin = "";
		}
	}
	
	public String getReference() {
		return Reference;
	}

	public void setReference(String inData) {
		if (inData != null) {
			this.Reference = inData.trim();
		}
		else {
			this.Reference = "";
		}
	}
	
	//Thaison - Sep 24, 2002
	public String getFixedDate() {
		return strFixedDate;
	}

	public void setFixedDate(String inData) {
		if (inData != null) {
			this.strFixedDate = inData.trim();
		}
		else {
			this.strFixedDate = "";
		}
	}
}
