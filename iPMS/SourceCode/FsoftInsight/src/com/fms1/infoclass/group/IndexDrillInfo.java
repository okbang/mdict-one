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
 * @(#) IndexDrillInfo.java 6-Oct-03
 */
package com.fms1.infoclass.group;

/**
 * Infoclass for storing index drill
 * @version 1.0
 * @author PhuongNT
 */
public final class IndexDrillInfo implements java.io.Serializable {
    private long workUnitID;
    private String workUnitName;
    private double plannedValue = Double.NaN;
    private double actualValue = Double.NaN;
    private double lastValue = Double.NaN;
    private boolean metricType;

	/**
	 * Returns the achievementValue.
	 * @return double
	 */
	public double getAchievementValue() {
		if (Double.isNaN(actualValue) || Double.isNaN(plannedValue)) {
			return Double.NaN;	
		}
		else {
			if (metricType) {
				if (plannedValue == 0) {
					return Double.NaN;
				}
				else {
					return (100 * actualValue / plannedValue);
				}
			}
			else {
				if (actualValue == 0) {
					return Double.NaN;
				}
				else {
					return (100 * plannedValue / actualValue);
				}
			}
		}
	}

	/**
	 * Returns the actualValue.
	 * @return double
	 */
	public double getActualValue() {
		return actualValue;
	}

	/**
	 * Returns the improvementValue.
	 * @return double
	 */
	public double getImprovementValue() {
		if ((plannedValue == 0) || Double.isNaN(actualValue) 
				|| Double.isNaN(plannedValue) || Double.isNaN(lastValue)) {
			return Double.NaN;	
		}
		else {
			if (metricType) {
				return 100 * (actualValue - lastValue) / plannedValue;
			}
			else {
				return 100 * (lastValue - actualValue) / plannedValue;
			}
		}
	}

	/**
	 * Returns the lastValue.
	 * @return double
	 */
	public double getLastValue() {
		return lastValue;
	}

	/**
	 * Returns the plannedValue.
	 * @return double
	 */
	public double getPlannedValue() {
		return plannedValue;
	}

	/**
	 * Returns the workUnitID.
	 * @return long
	 */
	public long getWorkUnitID() {
		return workUnitID;
	}

	/**
	 * Returns the workUnitName.
	 * @return String
	 */
	public String getWorkUnitName() {
		return workUnitName;
	}

	/**
	 * Sets the actualValue.
	 * @param actualValue The actualValue to set
	 */
	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}

	/**
	 * Sets the lastValue.
	 * @param lastValue The lastValue to set
	 */
	public void setLastValue(double lastValue) {
		this.lastValue = lastValue;
	}

	/**
	 * Sets the plannedValue.
	 * @param plannedValue The plannedValue to set
	 */
	public void setPlannedValue(double plannedValue) {
		this.plannedValue = plannedValue;
	}

	/**
	 * Sets the workUnitID.
	 * @param workUnitID The workUnitID to set
	 */
	public void setWorkUnitID(long workUnitID) {
		this.workUnitID = workUnitID;
	}

	/**
	 * Sets the workUnitName.
	 * @param workUnitName The workUnitName to set
	 */
	public void setWorkUnitName(String workUnitName) {
		this.workUnitName = workUnitName;
	}

	/**
	 * Sets the metricType.
	 * @param metricType The metricType to set
	 */
	public void setMetricType(boolean metricType) {
		this.metricType = metricType;
	}
    /**
     * @return
     */
    public boolean getMetricType() {
        return metricType;
    }

}