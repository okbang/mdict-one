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

public final class RequirementBatchUpdate{
	private String[] requirementId;
	private String[] deliverable;
	private String[] requirementType; //1: New; 2: Change Request; 3: Defect
	private String[] requirementSize;
	private String[] requirementSection;
	private String[] designSection;
	private String[] codeModule;
	private String[] testCaseSection;
	private String[] releaseNote;
	
	public RequirementBatchUpdate(){
	}
	
	public String[] getRequirementId(){
		return this.requirementId;
	}
	public void setRequirementId(String[] strrequirementId){
		requirementId = strrequirementId;
	}

	public String[] getDeliverable(){
		return this.deliverable;
	}
	public void setDeliverable(String[] strDeliverable){
		this.deliverable = strDeliverable;
	}

	public String[] getRequirementType(){
		return this.requirementType;
	}
	public void setRequirementType(String[] strRequirementType){
		this.requirementType = strRequirementType;
	}
	
	public String[] getRequirementSize(){
		return this.requirementSize;
	}
	public void setRequirementSize(String[] strRequirementSize){
		this.requirementSize = strRequirementSize;
	}
	
	public String[] getRequirementSection(){
		return this.requirementSection;
	}
	public void setRequirementSection(String[] strRequirementSection){
		this.requirementSection = strRequirementSection;
	}
	
	public String[] getDesignSection(){
		return this.designSection;
	}
	public void setDesignSection(String[] strDesignSection){
		this.designSection = strDesignSection;
	}
	
	public String[] getCodeModule(){
		return this.codeModule;
	}
	public void setCodeModule(String[] strCodeModule){
		this.codeModule = strCodeModule;
	}
	
	public String[] getTestCaseSection(){
		return this.testCaseSection;
	}
	public void setTestCaseSection(String[] strTestCaseSection){
		this.testCaseSection = strTestCaseSection;
	}
	
	public String[] getReleaseNote(){
		return this.releaseNote;
	}
	public void setReleaseNote(String[] strReleaseNote){
		this.releaseNote = strReleaseNote;
	}
}