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
 
 package com.fms1.infoclass.group;

/**
 * Info class for storing defect origin in SQA report
 * @version 1.0
 * @author PhuongNT
 */
public class DefectOriginInfo {
    private long nProjectId;
    private String strCode;
    private String strGroupName;
    private double dProjectSize;
    private int nRequirementDefect;
    private int nDesignDefect;
    private int nCodingDefect;
    private int nOtherDefect;
    private double dDPEffort;

    /**
     * Returns DP Effort.
     * @return double
     */
    public double getDPEffort() {
        return dDPEffort;
    }

    /**
     * Returns Project Size.
     * @return double
     */
    public double getProjectSize() {
        return dProjectSize;
    }

    /**
     * Returns Defect related to Coding.
     * @return int
     */
    public int getCodingDefect() {
        return nCodingDefect;
    }

    /**
     * Returns Defect related to Design.
     * @return int
     */
    public int getDesignDefect() {
        return nDesignDefect;
    }

    /**
     * Returns Defect related to other processes.
     * @return int
     */
    public int getOtherDefect() {
        return nOtherDefect;
    }

    /**
     * Returns Project Id.
     * @return long
     */
    public long getProjectId() {
        return nProjectId;
    }

    /**
     * Returns Defect related to requirement.
     * @return int
     */
    public int getRequirementDefect() {
        return nRequirementDefect;
    }

    /**
     * Returns Code of project.
     * @return String
     */
    public String getCode() {
        return strCode;
    }

    /**
     * Sets DP Effort.
     * @param dDPEffort The dDPEffort to set
     */
    public void setDPEffort(double dDPEffort) {
        this.dDPEffort = dDPEffort;
    }

    /**
     * Sets the Project Size.
     * @param dProjectSize The dProjectSize to set
     */
    public void setProjectSize(double dProjectSize) {
        this.dProjectSize = dProjectSize;
    }

    /**
     * Sets the Coding Defect.
     * @param nCodingDefect The Coding Defect to set
     */
    public void setCodingDefect(int nCodingDefect) {
        this.nCodingDefect = nCodingDefect;
    }

    /**
     * Sets the Design Defect.
     * @param nDesignDefect The Design Defect to set
     */
    public void setDesignDefect(int nDesignDefect) {
        this.nDesignDefect = nDesignDefect;
    }

    /**
     * Sets the Other Defect.
     * @param nOtherDefect The Other Defect to set
     */
    public void setOtherDefect(int nOtherDefect) {
        this.nOtherDefect = nOtherDefect;
    }

    /**
     * Sets the ProjectId.
     * @param nProjectId The ProjectId to set
     */
    public void setProjectId(long nProjectId) {
        this.nProjectId = nProjectId;
    }

    /**
     * Sets the Requirement Defect.
     * @param nRequirementDefect The Requirement Defect to set
     */
    public void setRequirementDefect(int nRequirementDefect) {
        this.nRequirementDefect = nRequirementDefect;
    }

    /**
     * Sets the Code.
     * @param strCode The Code to set
     */
    public void setCode(String strCode) {
        this.strCode = strCode;
    }

    /**
     * Returns Group Name.
     * @return String
     */
    public String getGroupName() {
        return strGroupName;
    }

    /**
     * Sets the Group Name.
     * @param strGroupName The Group Name to set
     */
    public void setGroupName(String strGroupName) {
        this.strGroupName = strGroupName;
    }

    /**
     * Return total weighted defect
     * @return int
     */
    public int getTotalWeightedDefect() {
        return nRequirementDefect + nDesignDefect + nCodingDefect + nOtherDefect;
    }
}
