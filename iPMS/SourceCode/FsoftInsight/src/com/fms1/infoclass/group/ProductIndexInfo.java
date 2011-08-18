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
 * @(#) ProductIndexeInfo.java 6-Oct-03
 */
package com.fms1.infoclass.group;

import com.fms1.common.group.ProductIndexes;
import com.fms1.tools.Color;

/**
 * Infoclass for storing product index
 * @version 1.0
 * @author PhuongNT
 */
public class ProductIndexInfo {
    private int metricConstant;
    private String metricID;
    private String metricName;
    private String metricUnit;
    private double actualValue;
    private double lastValue;
    private double actualPlannedValue;
    private double nextPlannedValue;
    private double next2monthsPlannedValue;
    private boolean metricType;
	public ProductIndexInfo(
            int metricConstant,
            String metricID,
            String metricName,
            String metricUnit, 
			double lastValue,
            double actualValue,
            double actualPlannedValue,
			double nextPlannedValue,
            double next2monthsPlannedValue,
            int metricType) {
		this.metricConstant = metricConstant;
        this.metricID = metricID;
		this.metricName = metricName;
		this.metricUnit = metricUnit;
		this.actualValue = actualValue;
		this.lastValue = lastValue;
		this.actualPlannedValue = actualPlannedValue;
		this.nextPlannedValue = nextPlannedValue;
		this.next2monthsPlannedValue = next2monthsPlannedValue;
		this.metricType = (metricType==Color.HIGHBAD)?ProductIndexes.HIGH_BAD:ProductIndexes.HIGH_GOOD;
	}

	/**
	 * Returns the actualPlannedValue.
	 * @return double
	 */
	public double getActualPlannedValue() {
		return actualPlannedValue;
	}

	/**
	 * Returns the actualValue.
	 * @return double
	 */
	public double getActualValue() {
		return actualValue;
	}

	/**
	 * Returns the lastValue.
	 * @return double
	 */
	public double getLastValue() {
		return lastValue;
	}

	/**
	 * Returns the metricID.
	 * @return String
	 */
	public String getMetricID() {
		return metricID;
	}

	/**
	 * Returns the metricName.
	 * @return String
	 */
	public String getMetricName() {
		return metricName;
	}

	/**
	 * Returns the metricUnit.
	 * @return String
	 */
	public String getMetricUnit() {
		return metricUnit;
	}

	/**
	 * Returns the next2monthsPlannedValue.
	 * @return double
	 */
	public double getNext2monthsPlannedValue() {
		return next2monthsPlannedValue;
	}

	/**
	 * Returns the nextPlannedValue.
	 * @return double
	 */
	public double getNextPlannedValue() {
		return nextPlannedValue;
	}

	/**
	 * Sets the actualPlannedValue.
	 * @param actualPlannedValue The actualPlannedValue to set
	 */
	public void setActualPlannedValue(double actualPlannedValue) {
		this.actualPlannedValue = actualPlannedValue;
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
	 * Sets the metricID.
	 * @param metricID The metricID to set
	 */
	public void setMetricID(String metricID) {
		this.metricID = metricID;
	}

	/**
	 * Sets the metricName.
	 * @param metricName The metricName to set
	 */
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	/**
	 * Sets the metricUnit.
	 * @param metricUnit The metricUnit to set
	 */
	public void setMetricUnit(String metricUnit) {
		this.metricUnit = metricUnit;
	}

	/**
	 * Sets the next2monthsPlannedValue.
	 * @param next2monthsPlannedValue The next2monthsPlannedValue to set
	 */
	public void setNext2monthsPlannedValue(double next2monthsPlannedValue) {
		this.next2monthsPlannedValue = next2monthsPlannedValue;
	}

	/**
	 * Sets the nextPlannedValue.
	 * @param nextPlannedValue The nextPlannedValue to set
	 */
	public void setNextPlannedValue(double nextPlannedValue) {
		this.nextPlannedValue = nextPlannedValue;
	}

	/**
	 * Returns the achievementValue.
	 * @return double
	 */
	public double getAchievementValue() {
		if (Double.isNaN(actualValue) || Double.isNaN(actualPlannedValue)) {
			return Double.NaN;	
		}
		else {
			if (metricType) {
				if (actualPlannedValue == 0) {
					return Double.NaN;
				}
				else {
					return (100 * actualValue / actualPlannedValue);
				}
			}
			else {
				if (actualValue == 0) {
					return Double.NaN;
				}
				else {
					return (100 * actualPlannedValue / actualValue);
				}
			}
		}
	}
    /**
     * @return
     */
    public int getMetricConstant() {
        return metricConstant;
    }

    /**
     * @param i
     */
    public void setMetricConstant(int i) {
        metricConstant = i;
    }

}
