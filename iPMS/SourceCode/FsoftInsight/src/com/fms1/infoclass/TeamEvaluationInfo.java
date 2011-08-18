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
 * Created on Apr 24, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.fms1.infoclass;

/**
 * @author TienHM08
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TeamEvaluationInfo {
	private int projectID;
	private int developerID;
	private String name;
	private String[] role;
	private float percentAttend;
	private String[] hq;
	private float pc;
	private String[] note;
	private int[] response;
	private String staffID;
	/**
	 * @return
	 */
	public int getDeveloperID() {
		return developerID;
	}

	/**
	 * @return
	 */
	public String[] getHq() {
		return hq;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String[] getNote() {
		return note;
	}

	/**
	 * @return
	 */
	public float getPc() {
		return pc;
	}

	/**
	 * @return
	 */
	public float getPercentAttend() {
		return percentAttend;
	}

	/**
	 * @return
	 */
	public int getProjectID() {
		return projectID;
	}

	/**
	 * @return
	 */
	public int[] getResponse() {
		return response;
	}

	/**
	 * @return
	 */
	public String[] getRole() {
		return role;
	}

	/**
	 * @param i
	 */
	public void setDeveloperID(int i) {
		developerID = i;
	}

	/**
	 * @param i
	 */
	public void setHq(String[] i) {
		hq = i;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setNote(String[] string) {
		note = string;
	}

	/**
	 * @param f
	 */
	public void setPc(float f) {
		pc = f;
	}

	/**
	 * @param f
	 */
	public void setPercentAttend(float f) {
		percentAttend = f;
	}

	/**
	 * @param i
	 */
	public void setProjectID(int i) {
		projectID = i;
	}

	/**
	 * @param is
	 */
	public void setResponse(int[] is) {
		response = is;
	}

	/**
	 * @param strings
	 */
	public void setRole(String[] strings) {
		role = strings;
	}

	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}

	public String getStaffID() {
		return staffID;
	}

}
