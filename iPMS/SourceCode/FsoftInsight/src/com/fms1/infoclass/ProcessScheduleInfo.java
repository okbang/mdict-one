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

public final class ProcessScheduleInfo {

    /**
    * Get all info for Process Schedule
    */
    private String product;    
    private Date plannedReleaseDate;
    private Date actualReleaseDate;
    private double deviation;
    
    public ProcessScheduleInfo() {
        this.product = "";        
    }
    
    /**
    * Get and Show All Properties
    */
    public String getProduct() {
        return this.product;
    }
    public void setProduct(String strProduct) {
        if (strProduct == null) {
            this.product = "";
        } else {
            this.product = strProduct;
        }
    }
    
    public Date getPlannedReleaseDate() {
        return this.plannedReleaseDate;
    }
    public void setPlannedReleaseDate(Date datePlannedReleaseDate) {
        this.plannedReleaseDate = datePlannedReleaseDate;
    }
    
    public Date getActualReleaseDate() {
        return this.actualReleaseDate;
    }
    public void setActualReleaseDate(Date dateActualReleaseDate) {
        this.actualReleaseDate = dateActualReleaseDate;
    }
    
    public double getDeviation() {
        return this.deviation;
    }
    public void setDeviation(double doubleDeviation) {
        this.deviation = doubleDeviation;
    }
}