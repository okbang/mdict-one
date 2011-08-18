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

import java.sql.Date;

public final class WPSizeInfo{
	public long moduleID;
	public long workProductID;
	public long projectID;
	public String name = "";
	public int categoryID;
	public String categoryName = "";
	public int estimatedSizeUnitID;
	public String estimatedSizeUnitName;
	public double estimatedSize = Double.NaN;
	public double reestimatedSize = Double.NaN;
	public double plannedDefects = Double.NaN;
	public double actualSize = Double.NaN;

    public Date plannedReleaseDate = null;
    public Date rePlannedReleaseDate = null;

	public double reestimatedSizeConv = Double.NaN;
	public double estimatedSizeConv = Double.NaN;
	public double actualSizeConv = Double.NaN;

	public double rePlannedDefects = Double.NaN;
	public double actualSizeUnitID = Double.NaN;
	public String actualSizeUnitName = null;

	public double reusePercentage = 0;
	public String description;
	public double createdSize = Double.NaN;
	public double createdSizeOrigin = Double.NaN;
	public double deviation = Double.NaN;
	public double newPlanSizeReview = Double.NaN;
	public double newPlanSizeTest = Double.NaN;
	public int methodBasedSize;
	public double acMethodBasedSize = Double.NaN;
	public int isDel = 0;
	public boolean isDocument = false;
	public String workProductCode = null;

    private long milestoneId;
    private int hasLoc;
    public boolean updateOK = true;
    public int isDefectReview = 0;
	public int isDefectTest = 0;
	public int processType = 0;
	
	public double actualDefReview = Double.NaN;
	public double actualDefTest = Double.NaN;
	
    /**
     * @return
     */
    public long getMilestoneId() {
        return milestoneId;
    }

    /**
     * @param l
     */
    public void setMilestoneId(long id) {
        milestoneId = id;
    }

    public void setHasLoc(int iHasLoc){
        this.hasLoc = iHasLoc;
    }
    public int getHasLoc(){
        return this.hasLoc;
    }
}