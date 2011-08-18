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
 
 package fpt.timesheet.constant;

public interface DefinitionList {
    // For list project in Project combo
    int DEV_PROJECT_TYPE = 0x00; // for Developer add,view,update timesheet
    int BILLABLE_PROJECT_TYPE = 0x01; // Approved by PL - Billable
    int QA_PROJECT_TYPE = 0x02; // Approved by QA
    int OTHER_PROJECT_TYPE = 0x03; // Approved by GL - Other
    int RP_PROJECT_TYPE = 0x04; // View all projects
    int EXTERNAL_PROJECT_TYPE = 0x05; // View all projects in user's group
    int INQUIRY_REPORT_TYPE = 0x06;   // For inquiry report

    int LIST_ALL_PROJECT = -1;
    int LIST_ON_GOING_PROJECT = 0;
    int LIST_CLOSED_PROJECT = 1;

    int TS_UNAPPROVED = 1;
    int GL_APPROVED = 3;

    // For change status in Approve Listing, Update
    int LD_APPROVE_STATUS = 2;
    int LD_REJECT_STATUS = 5;

    int QA_APPROVE_STATUS = 4;
    int QA_REJECT_STATUS = 5;

    //Project Status
    public static final int PROJECT_STATUS_NULL         = -2;
    public static final int PROJECT_STATUS_ALL          = -1;
    public static final int PROJECT_STATUS_ONGOING      = 0;
    public static final int PROJECT_STATUS_CLOSED       = 1;
    public static final int PROJECT_STATUS_CANCELLED    = 2;
    public static final int PROJECT_STATUS_TENTATIVE    = 3;
    
}