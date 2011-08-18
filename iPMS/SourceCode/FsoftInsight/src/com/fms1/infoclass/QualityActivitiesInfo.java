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

public final class QualityActivitiesInfo {

	/**
	* Get all info for Quality Activities
	*/
	private String activity;
    private double weightedDefectsPlannedToBeFound;
	private double weightedDefectsDetected;
	private double plannedEffort;
	private double actualEffort;
	private double completeness;
	private double timeliness;

    public QualityActivitiesInfo() {
        this.activity = "";
    }

	/**
	* Get and Show All Properties
	*/
	public String getActivity() {
		return this.activity;
	}
	public void setActivity(String strActivity) {
		if (strActivity == null) {
			this.activity = "";
		} else {
			this.activity = strActivity;
		}
	}
    
    public double getWeightedDefectsPlannedToBeFound() {
        return this.weightedDefectsPlannedToBeFound;
    }
    public void setWeightedDefectsPlannedToBeFound(double doubleWeightedDefectsPlannedToBeFound) {
        this.weightedDefectsPlannedToBeFound = doubleWeightedDefectsPlannedToBeFound;
    }
    
    public double getWeightedDefectsDetected() {
        return this.weightedDefectsDetected;
    }
    public void setWeightedDefectsDetected(double doubleWeightedDefectsDetected) {
        this.weightedDefectsDetected = doubleWeightedDefectsDetected;
    }
    
    public double getPlannedEffort() {
        return this.plannedEffort;
    }    
    public void setPlannedEffort(double doublePlannedEffort) {
        this.plannedEffort = doublePlannedEffort;
    }
    
    public double getActualEffort() {
        return this.actualEffort;
    }        
    public void setActualEffort(double doubleActualEffort) {
        this.actualEffort = doubleActualEffort;
    }

	public double getCompleteness() {
		return this.completeness;
	}
	public void setCompleteness(double doubleCompleteness) {
        this.completeness = doubleCompleteness;
    }
    
	public double getTimeliness() {
		return this.timeliness;
	}	
    public void setTimeliness(double doubleTimeliness) {
        this.timeliness = doubleTimeliness;
    }	
}