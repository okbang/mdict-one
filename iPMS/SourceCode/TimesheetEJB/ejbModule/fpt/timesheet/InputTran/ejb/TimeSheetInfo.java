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
 
 //***************************************************************************
// TimeSheetInfo.java
//
// Created by	: ThanhSKID
// Created date	: August 01, 2001
//				  HanhTN change constructor TimeSheetInfo() to set(), get()
//***************************************************************************
package fpt.timesheet.InputTran.ejb;

public class TimeSheetInfo implements java.io.Serializable {
	private int intTimeSheetID;
    private int intProject;
	private int intProcess;
	private int intTypeofWork;
	private int intWorkProduct;
	private int intStatus;
	private float fDuration;
	private String strDate;
	private String strDescription;
	private String strProjectName;
	private String strProcessName;
    private String strTypeofWorkName;
    private String strWorkProductName;
    private String strStatusName;
    private String strComment = "";

	public TimeSheetInfo() {
	}

	/**
	 * Method getTimeSheetID
	 * @return intTimeSheetID
	 */
	public int getTimeSheetID() {
		return intTimeSheetID;
	}

	/**
	 * Method setTimeSheetID
	 * @param intTimeSheetID
	 */
	public void setTimeSheetID(int intTimeSheetID) {
		this.intTimeSheetID = intTimeSheetID;
	}

	/**
	 * Method setProject
	 * @param intProject
	 */
	public void setProject(int intProject) {
		this.intProject = intProject;
	}

	/**
	 * Method getProject
	 * @return intProject
	 */
	public int getProject() {
		return intProject;
	}

	/**
	 * Method setProcess
	 * @param intProcess
	 */
	public void setProcess(int intProcess) {
		this.intProcess = intProcess;
	}

	/**
	 * Method getProcess
	 * @return intProcess
	 */
	public int getProcess() {
		return intProcess;
	}

	/**
	 * Method setTypeofWork
	 * @param intTypeofWork
	 */
	public void setTypeofWork(int intTypeofWork) {
		this.intTypeofWork = intTypeofWork;
	}

	/**
	 * Method getTypeofWork
	 * @return intTypeofWork
	 */
	public int getTypeofWork() {
		return intTypeofWork;
	}

	/**
	 * Method setWorkProduct
	 * @param intWorkProduct
	 */
	public void setWorkProduct(int intWorkProduct) {
		this.intWorkProduct = intWorkProduct;
	}

	/**
	 * Method getWorkProduct
	 * @return intWorkProduct
	 */
	public int getWorkProduct() {
		return intWorkProduct;
	}

	/**
	 * Method setStatus
	 * @param intStatus
	 */
	public void setStatus(int intStatus) {
		this.intStatus = intStatus;
	}

	/**
	 * Method getStatus
	 * @return intStatus
	 */
	public int getStatus() {
		return intStatus;
	}

	/**
	 * Method setDuration
	 * @param fDuration
	 */
	public void setDuration(float fDuration) {
		this.fDuration = fDuration;
	}

	/**
	 * Method getDuration
	 * @return fDuration
	 */
	public float getDuration() {
		return fDuration;
	}

	/**
	 * Method setDate
	 * @param strDate
	 */
	public void setDate(String strDate) {
		this.strDate = strDate;
	}

	/**
	 * Method getDate
	 * @return strDate
	 */
	public String getDate() {
		return strDate;
	}

	/**
	 * Method setDescription
	 * @param strDescription
	 */
	public void setDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	/**
	 * Method getDescription
	 * @return strDescription
	 */
	public String getDescription() {
		return strDescription;
	}

	/**
	 * Method setProjectName
	 * @param strProjectName
	 */
	public void setProjectName(String strProjectName) {
		this.strProjectName = strProjectName;
	}

	/**
	 * Method getProjectName
	 * @return strProjectName
	 */
	public String getProjectName() {
		return strProjectName;
	}

	/**
	 * Method setProcessName
	 * @param strProcessName
	 */
	public void setProcessName(String strProcessName) {
		this.strProcessName = strProcessName;
	}

	/**
	 * Method getProcessName
	 * @return strProcessName
	 */
	public String getProcessName() {
		return strProcessName;
	}

	/**
	 * Method setTypeofWorkName
	 * @param strTypeofWorkName
	 */
	public void setTypeofWorkName(String strTypeofWorkName) {
		this.strTypeofWorkName = strTypeofWorkName;
	}

	/**
	 * Method getTypeofWorkName
	 * @return strTypeofWorkName
	 */
	public String getTypeofWorkName() {
		return strTypeofWorkName;
	}

	/**
	 * Method setWorkProductName
	 * @param strWorkProductName
	 */
	public void setWorkProductName(String strWorkProductName) {
		this.strWorkProductName = strWorkProductName;
	}

	/**
	 * Method getWorkProductName
	 * @return strWorkProductName
	 */
	public String getWorkProductName() {
		return strWorkProductName;
	}

	/**
	 * Method setStatusName
	 * @param strStatusName
	 */
	public void setStatusName(String strStatusName) {
		this.strStatusName = strStatusName;
	}

	/**
	 * Method getStatusName
	 * @return strStatusName
	 */
	public String getStatusName() {
		return strStatusName;
	}

	/**
	 * Method setComment
	 * @param strComment
	 */
	public void setComment(String strComment) {
		this.strComment = strComment;
	}

	/**
	 * Method getComment
	 * @return strComment
	 */
	public String getComment() {
		return strComment;
	}

}