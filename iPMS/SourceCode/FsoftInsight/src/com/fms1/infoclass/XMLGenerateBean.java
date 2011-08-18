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
 
 package com.fms1.infoclass;

public class XMLGenerateBean{
	private long 	lProjectID;
	private String	strProjectName;
	private String	strTagProjectName; //Store tag name of ProjectName
	private String	strCustomer;
	private String	strTagCustomer;// Store tag name of customer
	private String	strProjectManager;
	private String	strTagProjectManager;
	private double	dProjectPoint;
	private	String	strTagProjectPoint;
	
	public XMLGenerateBean(){
		this.strCustomer = "N/A";
		this.strTagCustomer = "Customer";
		this.strProjectName = "";
		this.strTagProjectName = "Name";
		this.strProjectManager = "N/A";
		this.strTagProjectManager = "PM";
		this.dProjectPoint = Double.NaN;
		this.strTagProjectPoint = "Point";
	}

	public void setProjectID(long lProjectID){
		this.lProjectID = lProjectID;
	}
	public long getProjectID(){
		return this.lProjectID;
	}

	public void setProjectName(String strProjectName){
		if (strProjectName != null){
			this.strProjectName = strProjectName;
		}
	}
	public String getProjectName(){
		return this.strProjectName;
	}
	public String getTagProjectName(){
		return this.strTagProjectName;
	}

	public void setCustomer(String strCustomer){
		if (strCustomer != null){
			this.strCustomer = strCustomer;
		}
	}
	public String getCustomer(){
		return this.strCustomer;
	}
	public String getTagCustomer(){
		return this.strTagCustomer;
	}

	public void setProjectManager(String strProjectManager){
		if (strProjectManager != null){
			this.strProjectManager = strProjectManager;
		}
	}
	public String getProjectManager(){
		return this.strProjectManager;
	}
	public String getTagProjectManager(){
		return this.strTagProjectManager;
	}
	
	public void setProjectPoint(double dProjectPoint){
		this.dProjectPoint = dProjectPoint;
	}
	public double getProjectPoint(){
		return this.dProjectPoint;
	}
	public String getTagProjectPoint(){
		return strTagProjectPoint;
	}
}