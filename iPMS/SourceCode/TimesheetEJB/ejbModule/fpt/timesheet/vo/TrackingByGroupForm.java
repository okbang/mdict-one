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
 * Created on Jul 3, 2007
 * @author trungtn
 *
 */
package fpt.timesheet.vo;

import java.io.Serializable;

/**
 * @author trungtn
 * Store request parameters of Tracking timesheet by group form
 */
public class TrackingByGroupForm implements Serializable {
    String strGroup;
    String strFromDate;
    String strToDate;
    String strLogDate;
    String strLogTime;
    String strLogDateTime;

    String arrProjectIds;

    /**
     * @return
     */
    public String getArrProjectIds() {
        return arrProjectIds;
    }

    /**
     * @return
     */
    public String getFromDate() {
        return strFromDate;
    }

    /**
     * @return
     */
    public String getGroup() {
        return strGroup;
    }

    /**
     * @return
     */
    public String getLogDate() {
        return strLogDate;
    }

    /**
     * @return
     */
    public String getLogDateTime() {
        return strLogDateTime;
    }

    /**
     * @return
     */
    public String getLogTime() {
        return strLogTime;
    }

    /**
     * @return
     */
    public String getToDate() {
        return strToDate;
    }

    /**
     * @param string
     */
    public void setArrProjectIds(String string) {
        arrProjectIds = string;
    }

    /**
     * @param string
     */
    public void setFromDate(String string) {
        strFromDate = string;
    }

    /**
     * @param string
     */
    public void setGroup(String string) {
        strGroup = string;
    }

    /**
     * @param string
     */
    public void setLogDate(String string) {
        strLogDate = string;
    }

    /**
     * @param string
     */
    public void setLogDateTime(String string) {
        strLogDateTime = string;
    }

    /**
     * @param string
     */
    public void setLogTime(String string) {
        strLogTime = string;
    }

    /**
     * @param string
     */
    public void setToDate(String string) {
        strToDate = string;
    }

}
