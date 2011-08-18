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

import fpt.dms.bean.ComboBoxExt;
import fpt.dms.framework.util.StringUtil.StringMatrix;

public class DefectListingBean
{
    StringMatrix DefectList = null;
    String[] arrDeleteDefect = null;
    
    //Tu Ngoc Trung, 2003-10-23
    //Default values for first display
    String SortBy = "";
    int n_Direction = 0;
    String DefectOwner = "";
    String AssignTo = "";
    String CreatedBy = "";
    String StageDefected = ComboBoxExt.STR_ALL_VALUE;
    String StageInjected = ComboBoxExt.STR_ALL_VALUE;
    String Priority = ComboBoxExt.STR_ALL_VALUE;
    String ModuleCode = ComboBoxExt.STR_ALL_VALUE;
    String Status = ComboBoxExt.STR_ALL_VALUE;
    String WorkProduct = ComboBoxExt.STR_ALL_VALUE;
    String Severity = ComboBoxExt.STR_ALL_VALUE;
    String QCActivity = ComboBoxExt.STR_ALL_VALUE;
    String DefectOrigin = ComboBoxExt.STR_ALL_VALUE;
    String Type = ComboBoxExt.STR_ALL_VALUE;
    String Ref = ComboBoxExt.STR_ALL_VALUE;
    String FromDate = "";
    String ToDate = "";
    String Numpage = "0";
    String Totalpage = "0";
    String TotalRecord = "";
//Removed this solution
//  String strExportMode = null;
    //Thaison - Sep 18, 2002
    private boolean bIsExportAll = false; //true: export all defects
    private StringMatrix strProjectList; //project combo box
    String strFixedFrom = "";
    String strFixedTo = "";
    String strDefectID = "";
    String strTitle = "";
    String strTestCase = "";

    public StringMatrix getDefectList()
    {
        return DefectList;
    }
    public void setDefectList(StringMatrix DefectList)
    {
        this.DefectList = DefectList;
    }
    public String[] getArrDeleteDefect()
    {
        return arrDeleteDefect;
    }
    public void setArrDeleteDefect(String[] arrDeleteDefect)
    {
        this.arrDeleteDefect = arrDeleteDefect;
    }
    // ****************************** Extend Code ******************************
    // WorkProduct Combo:
    private StringMatrix smxComboWorkProduct;
    public StringMatrix getComboWorkProduct()
    {
        return smxComboWorkProduct;
    }
    public void setComboWorkProduct(StringMatrix inCombo)
    {
        smxComboWorkProduct = inCombo;
    }
    // Severity Combo:
    private StringMatrix smxComboSeverity;
    public StringMatrix getComboSeverity()
    {
        return smxComboSeverity;
    }
    public void setComboSeverity(StringMatrix inCombo)
    {
        smxComboSeverity = inCombo;
    }
    // ModuleCode Combo:
    private StringMatrix smxComboModuleCode;
    public StringMatrix getComboModuleCode()
    {
        return smxComboModuleCode;
    }
    public void setComboModuleCode(StringMatrix inCombo)
    {
        smxComboModuleCode = inCombo;
    }
    // TypeofActivity Combo:
    private StringMatrix smxComboTypeofActivity;
    public StringMatrix getComboTypeofActivity()
    {
        return smxComboTypeofActivity;
    }
    public void setComboTypeofActivity(StringMatrix inCombo)
    {
        smxComboTypeofActivity = inCombo;
    }
    // Priority Combo:
    private StringMatrix smxComboPriority;
    public StringMatrix getComboPriority()
    {
        return smxComboPriority;
    }
    public void setComboPriority(StringMatrix inCombo)
    {
        smxComboPriority = inCombo;
    }
    // Type Combo:
    private StringMatrix smxComboType;
    public StringMatrix getComboType()
    {
        return smxComboType;
    }
    public void setComboType(StringMatrix inCombo)
    {
        smxComboType = inCombo;
    }
	// defect owner
	private StringMatrix smxDefectOwner;
	public StringMatrix getComboDefectOwner()
	{
		return smxDefectOwner;
	}
	public void setComboDefectOwner(StringMatrix inCombo)
	{
		smxDefectOwner = inCombo;
	}
    
    // AssignTo Combo:
    private StringMatrix smxComboAssignTo;
    public StringMatrix getComboAssignTo()
    {
        return smxComboAssignTo;
    }
    public void setComboAssignTo(StringMatrix inCombo)
    {
        smxComboAssignTo = inCombo;
    }
    
    // StageDetected Combo:
    private StringMatrix smxComboStageDetected;
    public StringMatrix getComboStageDetected()
    {
        return smxComboStageDetected;
    }
    public void setComboStageDetected(StringMatrix inCombo)
    {
        smxComboStageDetected = inCombo;
    }
    // ModuleCode Combo:
    private StringMatrix smxComboQCActivity;
    public StringMatrix getComboQCActivity()
    {
        return smxComboQCActivity;
    }
    public void setComboQCActivity(StringMatrix inCombo)
    {
        smxComboQCActivity = inCombo;
    }
    // ModuleCode Combo:
    private StringMatrix smxComboStageInjected;
    public StringMatrix getComboStageInjected()
    {
        return smxComboStageInjected;
    }
    public void setComboStageInjected(StringMatrix inCombo)
    {
        smxComboStageInjected = inCombo;
    }
    // Ref Combo:
    private StringMatrix smxComboRef;
    public StringMatrix getComboRef()
    {
        return smxComboRef;
    }
    public void setComboRef(StringMatrix inCombo)
    {
        smxComboRef = inCombo;
    }
    // DefectOrigin Combo:
    private StringMatrix smxComboDefectOrigin;
    public StringMatrix getComboDefectOrigin()
    {
        return smxComboDefectOrigin;
    }
    public void setComboDefectOrigin(StringMatrix inCombo)
    {
        smxComboDefectOrigin = inCombo;
    }
    // DefectStatus Combo:
    private StringMatrix smxComboDefectStatus;
    public StringMatrix getComboDefectStatus()
    {
        return smxComboDefectStatus;
    }
    public void setComboDefectStatus(StringMatrix inCombo)
    {
        smxComboDefectStatus = inCombo;
    }
    public String getSortBy()
    {
        return SortBy;
    }
    public void setSortBy(String SortBy)
    {
        this.SortBy = SortBy;
    }
	public String getDefectOwner() {
			return DefectOwner;
	}
	public void setDefectOwner(String DefectOwner) {
		if(DefectOwner == null) {
			this.DefectOwner = "";
		} else
		{
			this.DefectOwner = DefectOwner;
		}
	}
    public String getAssignTo()
    {
        return AssignTo;
    }
    public void setAssignTo(String AssignTo)
    {
        this.AssignTo = AssignTo;
    }
    public String getCreatedBy()
    {
        return CreatedBy;
    }
    public void setCreatedBy(String CreatedBy)
    {
        this.CreatedBy = CreatedBy;
    }
    public String getWorkProduct()
    {
        return WorkProduct;
    }
    public void setWorkProduct(String WorkProduct)
    {
        this.WorkProduct = WorkProduct;
    }
    public String getType()
    {
        return Type;
    }
    public void setType(String Type)
    {
        this.Type = Type;
    }
    public void setToDate(String ToDate)
    {
        this.ToDate = ToDate;
    }
    public String getToDate()
    {
        return ToDate;
    }
    public String getStatus()
    {
        return Status;
    }
    public void setStatus(String Status)
    {
        this.Status = Status;
    }
    public void setStageInjected(String StageInjected)
    {
        this.StageInjected = StageInjected;
    }
    public void setStageDefected(String StageDefected)
    {
        this.StageDefected = StageDefected;
    }
    public String getStageDefected()
    {
        return StageDefected;
    }
    public String getStageInjected()
    {
        return StageInjected;
    }
    public String getSeverity()
    {
        return Severity;
    }
    public void setSeverity(String Severity)
    {
        this.Severity = Severity;
    }
    public void setQCActivity(String QCActivity)
    {
        this.QCActivity = QCActivity;
    }
    public String getQCActivity()
    {
        return QCActivity;
    }
    public String getPriority()
    {
        return Priority;
    }
    public void setPriority(String Priority)
    {
        this.Priority = Priority;
    }
    public void setModuleCode(String ModuleCode)
    {
        this.ModuleCode = ModuleCode;
    }
    public String getModuleCode()
    {
        return ModuleCode;
    }
    public String getFromDate()
    {
        return FromDate;
    }
    public void setFromDate(String FromDate)
    {
        this.FromDate = FromDate;
    }
    public void setDefectOrigin(String DefectOrigin)
    {
        this.DefectOrigin = DefectOrigin;
    }
    public String getDefectOrigin()
    {
        return DefectOrigin;
    }
    public String getNumpage()
    {
        return Numpage;
    }
    public void setNumpage(String Numpage)
    {
        this.Numpage = Numpage;
    }
    public String getTotalpage()
    {
        return Totalpage;
    }
    public void setTotalpage(String Totalpage)
    {
        this.Totalpage = Totalpage;
    }
    public String getTotalRecord()
    {
        return TotalRecord;
    }
    public void setTotalRecord(String TotalRecord)
    {
        this.TotalRecord = TotalRecord;
    }
    public String getRef()
    {
        return Ref;
    }
    public void setRef(String Ref)
    {
        this.Ref = Ref;
    }
    //Thaison - Sep 18, 2002
    public boolean IsExportAll()
    {
        return this.bIsExportAll;
    }
    public void setExportAll(boolean bAll)
    {
        this.bIsExportAll = bAll;
    }
    public StringMatrix getProjectList()
    {
        return strProjectList;
    }
    public void setProjectList(StringMatrix list)
    {
        strProjectList = list;
    }

    String strNewProjectID = "";
    public String getNewProjectID()
    {
        return this.strNewProjectID;
    }
    public void setNewProjectID(String data)
    {
        this.strNewProjectID = data;
    }

    /**
     * @return
     */
    public int getDirection() {
        return n_Direction;
    }

    /**
     * @param i
     */
    public void setDirection(int i) {
        n_Direction = i;
    }

    /**
     * @return
     */
    public String getDefectID() {
        return strDefectID;
    }

    /**
     * @return
     */
    public String getFixedFrom() {
        return strFixedFrom;
    }

    /**
     * @return
     */
    public String getFixedTo() {
        return strFixedTo;
    }

    /**
     * @return
     */
    public String getTitle() {
        return strTitle;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getTestCase(){
    	return strTestCase;
    }

    /**
     * @param string
     */
    public void setDefectID(String string) {
        strDefectID = string;
    }

    /**
     * @param string
     */
    public void setFixedFrom(String string) {
        strFixedFrom = string;
    }

    /**
     * @param string
     */
    public void setFixedTo(String string) {
        strFixedTo = string;
    }

    /**
     * @param string
     */
    public void setTitle(String string) {
        this.strTitle = string;
    }
    
    /**
     *  
     * @return
     */
    public void setTestCase(String string){
    	this.strTestCase = string;
    }
    
    

    /**
     * @return
     */
//Removed this solution    
//    public String getExportMode() {
//        return strExportMode;
//    }

    /**
     * @param string
     */
//Removed this solution    
//    public void setExportMode(String string) {
//        strExportMode = string;
//    }
//
}