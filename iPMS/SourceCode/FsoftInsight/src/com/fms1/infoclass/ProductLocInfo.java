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

import java.io.Serializable;

import com.fms1.tools.CommonTools;

public class ProductLocInfo implements Serializable, Cloneable {
    // Data ManipuLation marks 
	public static final int DML_INSERT = 1;
    public static final int DML_UPDATE = 2;
    public static final int DML_DELETE = 3;
    
    /*
	 * get all information in product_loc_plan/product_loc_actual table
	 */
    private long    productLocId;
    private long    projectId;
    private long    moduleId;
    private long    languageId;
    private long    milestoneId; // Used for product LOC by stage listing
    private double  locProductivity = Double.NaN;
    private double  locQuality = Double.NaN;
    private double  motherBody = Double.NaN;
	private double 	added = Double.NaN;
    private double  modified = Double.NaN;
    private double  total = Double.NaN;
	private double 	reused = Double.NaN;
	private double 	generated = Double.NaN;
	private String	note = "";
    private String  languageName = "";
    // Data ManipuLation marks (INSERT, UPDATE OR DELETE), used for product LOC update 
    private int     dmlType = DML_INSERT;
    
    public ProductLocInfo() {
    }

    /*
     * Initialize instance with default values. Temporary removed
     * @param defaultValue
    public ProductLocInfo(double defaultValue) {
        locProductivity = defaultValue;
        locQuality = defaultValue;
        motherBody = defaultValue;
        added = defaultValue;
        modified = defaultValue;
        total = defaultValue;
        reused = defaultValue;
        generated = defaultValue;
    }
     */
    
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String text = "productLocId:" + productLocId +
            "  projectId:" + projectId + "  moduleId:" + moduleId +
            "  languageId:" + languageId + "  milestoneId:" + milestoneId +
            "  locProductivity:" + locProductivity +
            "  locQuality:" + locQuality +
            "  motherBody:" + motherBody + "  added:" + added +
            "  modified:" + modified + 
            "  total:" + total + "  reused:" + reused +
            "  generated:" + generated;
        return text;
    }
    
    /**
     * Set zero values for all fields:
     * total,added,reused,modified,generated,motherBody,
     * locProductivity,locQuality.
     */
    public void resetToZero() {
        locProductivity = 0;
        locQuality = 0;
        motherBody = 0;
        added = 0;
        modified = 0;
        total = 0;
        reused = 0;
        generated = 0;
    }
    
    /**
     * Plus all fields with other LOC object
     * @param newLoc
     */
    public void plus(ProductLocInfo newLoc) {
        this.motherBody = CommonTools.addDouble(
                this.motherBody, newLoc.motherBody);
        this.added = CommonTools.addDouble(this.added, newLoc.added);
        this.modified = CommonTools.addDouble(this.modified, newLoc.modified);
        this.total = CommonTools.addDouble(this.total, newLoc.total);
        this.reused = CommonTools.addDouble(this.reused, newLoc.reused);
        this.generated = CommonTools.addDouble(
                this.generated, newLoc.generated);
        this.locProductivity = CommonTools.addDouble(
                this.locProductivity, newLoc.locProductivity);
        this.locQuality = CommonTools.addDouble(
                this.locQuality, newLoc.locQuality);
    }
    
    /**
     * Calculate LOC for Productivity, LOC for Quality, based on
     * {added, modified, mother body} OR {total, reused}
     */
    public void calculateLocForProductivityQuality() {
        if (added > 0 || modified > 0 || motherBody > 0) {
            locProductivity = CommonTools.addDouble(
                    CommonTools.addDouble(added, 2 * modified),
                    0.06 * motherBody);
            locQuality = CommonTools.addDouble(added, modified);
        }
        else {
            locProductivity = CommonTools.addDouble(total, -reused);
            locQuality = total;
        }
    }
    
    /**
     * Check input data (add/update functions) is empty
     * @return
     */
    public boolean inputDataIsEmpty() {
        // Not selected language or not filled other data fields
        if (languageId <= 0) {
            return true;
        }
        else {
            return (Double.isNaN(motherBody) && Double.isNaN(added) &&
                    Double.isNaN(modified) && Double.isNaN(total) &&
                    Double.isNaN(reused) && Double.isNaN(generated));
        }
    }
    
	public void setProductLocId(long lProductLocId){
	 	this.productLocId = lProductLocId;
	}
	public long getProductLocId(){
	 	return this.productLocId;
	}

	public void setProjectId(long lProjectId){
		this.projectId = lProjectId;	 
	}
	public long getProjectId(){
		return this.projectId;
	}

	public void setModuleId(long lModuleId){
		this.moduleId = lModuleId;
	}
	public long getModuleId(){
		return this.moduleId;
	}

	public void setLanguageId(long lLanguageId){
		this.languageId = lLanguageId;
	}
	public long getLanguageId(){
		return this.languageId;
	}

    public long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(long lMilestoneId) {
        milestoneId = lMilestoneId;
    }

	public void setMotherBody(double dMotherBody){
		this.motherBody = dMotherBody;
	}
	public double getMotherBody(){
		return this.motherBody;
	}

	public void setAdded(double dAdded){
		this.added = dAdded;
	}
	public double getAdded(){
		return this.added;
	}

	public void setModified(double dModified){
		this.modified = dModified;
	}
	public double getModified(){
		return this.modified;
	}

	public void setTotal(double dTotal){
		this.total = dTotal;
	}
	public double getTotal(){
		return this.total;
	}

	public void setReused(double dResused){
		this.reused = dResused;
	}
	public double getReused(){
		return this.reused;
	}

	public void setGenerated(double dGenerated){
		this.generated = dGenerated;
	}
	public double getGenerated(){
		return this.generated;
	}

	public void setNote(String strNote){
		this.note = (strNote == null)?"":strNote;
	}
	public String getNote(){
		return this.note;
	}
    /**
     * @return
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * @param string
     */
    public void setLanguageName(String langName) {
        languageName = langName;
    }

    /**
     * @return
     */
    public double getLocProductivity() {
        return locProductivity;
    }

    /**
     * @param d
     */
    public void setLocProductivity(double d) {
        locProductivity = d;
    }

    /**
     * @return
     */
    public double getLocQuality() {
        return locQuality;
    }

    /**
     * @param d
     */
    public void setLocQuality(double d) {
        locQuality = d;
    }

    /**
     * @return
     */
    public int getDmlType() {
        return dmlType;
    }

    /**
     * @param i
     */
    public void setDmlType(int type) {
        dmlType = type;
    }

}