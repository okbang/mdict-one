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

import java.util.Date;

public final class SignatureInfo {

    /**
    * Get all info for Quality Activities
    */
    private long approvalId;
    private int approvalStatus;
    private Date approvalDate;
    private long projectId;
    private long developerId;
    private String developerName;
    private String developerPosition;
    private String developerRole;

    public SignatureInfo() {
        approvalId = 0;
        approvalStatus = -1;
        approvalDate = null;
        projectId = 0;
        developerId = 0;
        developerName = null;
        developerPosition = null;
        developerRole = null;
    }
    
    /**
    * Get and Show All Properties
    */
    public long getApprovalId() {
        return this.approvalId;
    }
    public void setApprovalId(long lApprovalId) {
        this.approvalId = lApprovalId;
    }
    
    public int getApprovalStatus() {
        return this.approvalStatus;
    }
    public void setApprovalStatus(int iApprovalStatus) {
        this.approvalStatus = iApprovalStatus;
    }
    
    public Date getApprovalDate() {
        return this.approvalDate;
    }
    public void setApprovalDate(Date dtApprovalDate) {
        this.approvalDate = dtApprovalDate;
    }

    public long getProjectId() {
        return this.projectId;
    }
    public void setProjectId(long lProjectId) {
        this.projectId = lProjectId;
    }
    
    public long getDeveloperId() {
        return this.developerId;
    }
    public void setDeveloperId(long lDeveloperId) {
        this.developerId = lDeveloperId;
    }
    
    public String getDeveloperName() {
        return this.developerName;
    }
    public void setDeveloperName(String strDeveloperName) {
        if (strDeveloperName == null) {
            this.developerName = "";
        } else {
            this.developerName = strDeveloperName;
        }
    }
    
    public String getDeveloperPosition() {
        return this.developerPosition;
    }
    public void setDeveloperPosition(String strDeveloperPosition) {
        if (strDeveloperPosition == null) {
            this.developerPosition = "";
        } else {
            this.developerPosition = strDeveloperPosition;
        }
    }
    
    public String getDeveloperRole() {
        return this.developerRole;
    }
    public void setDeveloperRole(String strDeveloperRole) {
        if (strDeveloperRole == null) {
            this.developerRole = "";
        } else {
            this.developerRole = strDeveloperRole;
        }
    }
}