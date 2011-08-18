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
 
 /**
 * Description  :  Timesheet <p>
 * Author       :  HaTH<p>
 * Created date :  12/21/2001
 */

package fpt.timesheet.ApproveTran.ejb.report;

import java.io.Serializable;

public class InquiryRow implements Serializable {

    public String Timesheet_ID;
    public String ProjectCode;
    public String ProjectType;
    public String ProjectStatus;
    public String UserName;
    public String Create_Date;
    public String Occur_Date;
    public String Description;
    public String Duration;
    public String ProcessName;
    public String TypeOfWorkName;
    public String WorkProductName;
    public String KPAName;
    public String Approved_By_Leader;

    public String Process_ID;
    public String TOW_ID;
    public String WP_ID;
    public String KPA_ID;

    public String GroupName;
    public String PLapprovedtime;
	public String QAapprovedtime;

    /**
     * @see java.lang.Object#Object()
     */
    public InquiryRow() {

    }

    /**
     * Method InquiryRow.
     * @param Timesheet_ID
     * @param ProjectCode
     * @param UserName
     * @param Occur_Date
     * @param Description
     * @param Duration
     * @param ProcessName
     * @param TypeOfWorkName
     * @param WorkProductName
     * @param KPAName
     * @param Approved_By_Leader
     * @param Process_ID
     * @param TOW_ID
     * @param WP_ID
     * @param KPA_ID
     * @param GroupName
     */
    public InquiryRow(String Timesheet_ID, String ProjectCode, String UserName, String Occur_Date, String Description, String Duration, String ProcessName, String TypeOfWorkName, String WorkProductName, String KPAName, String Approved_By_Leader, String Process_ID, String TOW_ID, String WP_ID, String KPA_ID, String GroupName) {
        this.Timesheet_ID = Timesheet_ID;
        this.ProjectCode = ProjectCode;
        this.UserName = UserName;
        this.Occur_Date = Occur_Date;
        this.Description = Description;
        this.Duration = Duration;
        this.ProcessName = ProcessName;
        this.TypeOfWorkName = TypeOfWorkName;
        this.WorkProductName = WorkProductName;
        this.KPAName = KPAName;
        this.Approved_By_Leader = Approved_By_Leader;
        this.Process_ID = Process_ID;
        this.TOW_ID = TOW_ID;
        this.WP_ID = WP_ID;
        this.KPA_ID = KPA_ID;
        this.GroupName = GroupName;
    }
}