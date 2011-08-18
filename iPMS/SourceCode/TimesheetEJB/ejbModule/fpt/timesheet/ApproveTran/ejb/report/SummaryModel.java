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

public class SummaryModel implements Serializable {

    private String strEffortName;
    private String strSelectedPeriod;
    private String strUptoCurrentDate;

    /**
     * @see java.lang.Object#Object()
     */
    public SummaryModel() {

    }

    /**
     * Method setAll.
     * @param str1
     * @param str2
     * @param str3
     */
    public void setAll(String str1, String str2, String str3) {
        this.strEffortName = str1;
        this.strSelectedPeriod = str2;
        this.strUptoCurrentDate = str3;
    }

    /**
     * Method getEffortName.
     * @return String
     */
    public String getEffortName() {
        return this.strEffortName;
    }

    /**
     * Method getSelectedPeriod.
     * @return String
     */
    public String getSelectedPeriod() {
        return this.strSelectedPeriod;
    }

    /**
     * Method getUptoCurrentDate.
     * @return String
     */
    public String getUptoCurrentDate() {
        return this.strUptoCurrentDate;
    }

}