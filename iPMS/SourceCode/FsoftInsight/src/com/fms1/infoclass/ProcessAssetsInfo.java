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

public final class ProcessAssetsInfo {

    /**
    * Get all info for Process Assets
    */
    private String team;
    private String osList;
    private String dbmsList;
    private String languageList;
    private String swToolList;
    private String hardwareList;
    private String year;
    
    public ProcessAssetsInfo() {
        team = "";
        osList = "";
        dbmsList = "";
        languageList = "";
        swToolList = "";
        hardwareList = "";
        year = "";
    }

    /**
    * Get and Show All Properties
    */
    public String getTeam() {
        return this.team;
    }
    public void setTeam(String strTeam) {
        if (strTeam == null) {
            this.team = "";
        } else {
            this.team = strTeam;
        }
    }
    
    public String getOsList() {
        return this.osList;
    }
    public void setOsList(String strOsList) {
        if (strOsList == null) {
            this.osList = "";
        } else {
            this.osList = strOsList;
        }
    }

    public String getDbmsList() {
        return this.dbmsList;
    }
    public void setDbmsList(String strDbmsList) {
        if (strDbmsList == null) {
            this.dbmsList = "";
        } else {
            this.dbmsList = strDbmsList;
        }
    }
    
    public String getLanguageList() {
        return this.languageList;
    }
    public void setLanguageList(String strLanguageList) {
        if (strLanguageList == null) {
            this.languageList = "";
        } else {
            this.languageList = strLanguageList;
        }
    }
    
    public String getSwToolList() {
        return this.swToolList;
    }
    public void setSwToolList(String strSwToolList) {
        if (strSwToolList == null) {
            this.swToolList = "";
        } else {
            this.swToolList = strSwToolList;
        }
    }
    
    public String getHardwareList() {
        return this.hardwareList;
    }
    public void setHardwareList(String strHardwareList) {
        if (strHardwareList == null) {
            this.hardwareList = "";
        } else {
            this.hardwareList = strHardwareList;
        }
    }

    public String getYear() {
        return this.year;
    }
    public void setYear(String strYear) {
        if (strYear == null) {
            this.year = "";
        } else {
            this.year = strYear;
        }
    }
}