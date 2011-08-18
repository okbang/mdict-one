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

public class DefectBatchUpdateBean {

	private StringMatrix smxBatchUpdateList = null;
	private StringMatrix smxComboStatus;
	private StringMatrix smxComboSeverity;
	private StringMatrix smxComboPriority;
	private StringMatrix smxComboAssignTo;
	private StringMatrix smxComboDefectOwner;

	public StringMatrix getBatchUpdateList() {
		return smxBatchUpdateList;
	}
	public void setBatchUpdateList(StringMatrix inData) {
		this.smxBatchUpdateList = inData;
	}
	// ****************************** Extended Code ******************************
	// Status Combo:
	public StringMatrix getComboStatus() {
		return smxComboStatus;
	}
	public void setComboStatus(StringMatrix inCombo) {
		smxComboStatus = inCombo;
	}
	// Severity Combo:
	public StringMatrix getComboSeverity() {
		return smxComboSeverity;
	}
	public void setComboSeverity(StringMatrix inCombo) {
		smxComboSeverity = inCombo;
	}
	// Priority Combo:
	public StringMatrix getComboPriority() {
		return smxComboPriority;
	}
	public void setComboPriority(StringMatrix inCombo) {
		smxComboPriority = inCombo;
	}
	// AssignTo Combo:
	public StringMatrix getComboAssignTo() {
		return smxComboAssignTo;
	}
	public void setComboAssignTo(StringMatrix inCombo) {
		smxComboAssignTo = inCombo;
	}
	// Defect Owner Combo:
	public StringMatrix getComboDefectOwner(){
		return smxComboDefectOwner;
	}
	public void setComboDefectOwner(StringMatrix inCombo){
		smxComboDefectOwner = inCombo;
	}
}