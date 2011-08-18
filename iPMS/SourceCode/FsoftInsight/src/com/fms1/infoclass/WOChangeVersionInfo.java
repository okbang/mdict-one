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
 * @(#) WOChangeVersionInfo.java 6-Oct-03
 */
package com.fms1.infoclass;

import java.sql.Date;

/**
 * Infoclass for storing version change
 * @version 1.0
 * @author PhuongNT
 */
public final class WOChangeVersionInfo implements java.io.Serializable {
    private int nNumChange = 0;
    private Date changeDate;
    private String strVersion;
    /**
     * Returns the changeDate.
     * @return Date
     */
    public Date getChangeDate() {
        return changeDate;
    }

    /**
     * Returns the nNumChange.
     * @return int
     */
    public int getNumChange() {
        return nNumChange;
    }

    /**
     * Returns the strVersion.
     * @return String
     */
    public String getVersion() {
        return strVersion;
    }

    /**
     * Sets the changeDate.
     * @param changeDate The changeDate to set
     */
    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * Sets the nNumChange.
     * @param nNumChange The nNumChange to set
     */
    public void setNumChange(int nNumChange) {
        this.nNumChange = nNumChange;
    }

    /**
     * Sets the strVersion.
     * @param strVersion The strVersion to set
     */
    public void setVersion(String strVersion) {
        this.strVersion = strVersion;
    }
}
