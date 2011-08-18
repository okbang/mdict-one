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
 
 package fpt.timesheet.bean;

import java.util.Collection;

import fpt.timesheet.bo.ComboBox.CommonListBO;
import fpt.timesheet.bo.Exemption.ExemptionBO;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

public class ExemptionListBean implements java.io.Serializable {

	private int intCurrentPage 	  = 0;
	private int intTotalPage 	  = 0;
	private int intTotalExemption = 0;

	private StringMatrix smtDevList = null;
	private StringMatrix smtGroupList = null;
	private Collection colExemption = null;

	/**
	 * Method getDevList.
	 * @return smtDevList
	 */
	public StringMatrix getDevList() {
		return smtDevList;
	}
	
	/**
	 * Method setDevList.
	 * @param smtDevList
	 */
	public void setDevList(StringMatrix smtDevList) {
		this.smtDevList = smtDevList;
	}

	/**
	 * Method setDevList.
	 */
	public void setDevList() {
		try {
			ExemptionBO boExemption = new ExemptionBO();
			this.smtDevList = boExemption.getDeveloperList();
		}
		catch (Exception ex){
			System.out.println("Error: " + ex.toString());
		}
	}

	/**
	 * Method getGroupList.
	 * @return smtGroupList
	 */
	public StringMatrix getGroupList() {
		return smtGroupList;
	}

	/**
	 * Method setGroupList.
	 * @param smtGroupList
	 */
	public void setGroupList(StringMatrix smtGroupList) {
		this.smtGroupList = smtGroupList;
	}

	/**
	  * Method setGroupList.
	  */
	public void setGroupList() {
		CommonListBO cmlRef = new CommonListBO();
		this.smtGroupList = cmlRef.getGroupList();
	}
	
	/**
	 * Method getTimesheetList.
	 * @return Collection
	 */
	public Collection getExemptionList() {
		return colExemption;
	}

	/**
	 * Method setTimesheetList.
	 * @param inData
	 */
	public void setExemptionList(Collection colExemption) {
		this.colExemption = colExemption;
	}

	/**
	 * Method getCurrentPage.
	 * @return intCurrentPage
	 */
	public int getCurrentPage() {
		return intCurrentPage;
	}

	/**
	 * Method setCurrentPage.
	 * @param intCurrentPage
	 */
	public void setCurrentPage(int intCurrentPage) {
		this.intCurrentPage = intCurrentPage;
	}

	/**
	 * Method getTotalPage.
	 * @return intTotalPage
	 */
	public int getTotalPage() {
		return intTotalPage;
	}

	/**
	 * Method setTotalPage.
	 * @param intTotalPage
	 */
	public void setTotalPage(int intTotalPage) {
		this.intTotalPage = intTotalPage;
	}

	/**
	 * Method getTotalExemption.
	 * @return intTotalExemption
	 */
	public int getTotalExemption() {
		return intTotalExemption;
	}

	/**
	 * Method setTotalExemption
	 * @param intTotalExemption
	 */
	public void setTotalExemption(int intTotalExemption) {
		this.intTotalExemption = intTotalExemption;
	}

}
