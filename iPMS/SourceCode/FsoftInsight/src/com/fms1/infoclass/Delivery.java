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
 * Created on May 25, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.fms1.infoclass;

import java.util.Date;

/**
 * @author TienHM08
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Delivery {
	private String projectCode;
	private String customerName;
	private String deliverable;
	private String  firstCommitDate;
	private String reCommitDate;
	private String actualDate;
	private String status;
	private String note;
	/**
	 * @return
	 */
	public String getActualDate() {
		return actualDate;
	}

	/**
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @return
	 */
	public String getDeliverable() {
		return deliverable;
	}

	/**
	 * @return
	 */
	public String getFirstCommitDate() {
		return firstCommitDate;
	}

	/**
	 * @return
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return
	 */
	public String getProjectCode() {
		return projectCode;
	}

	/**
	 * @return
	 */
	public String getReCommitDate() {
		return reCommitDate;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param date
	 */
	public void setActualDate(String date) {
		actualDate = date;
	}

	/**
	 * @param string
	 */
	public void setCustomerName(String string) {
		customerName = string;
	}

	/**
	 * @param string
	 */
	public void setDeliverable(String string) {
		deliverable = string;
	}

	/**
	 * @param date
	 */
	public void setFirstCommitDate(String date) {
		firstCommitDate = date;
	}

	/**
	 * @param string
	 */
	public void setNote(String string) {
		note = string;
	}

	/**
	 * @param string
	 */
	public void setProjectCode(String string) {
		projectCode = string;
	}

	/**
	 * @param date
	 */
	public void setReCommitDate(String date) {
		reCommitDate = date;
	}

	/**
	 * @param i
	 */
	public void setStatus(String i) {
		status = i;
	}

}
