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
 * @(#)ViewAddBean.java 08-Apr-03
 */


package fpt.ncms.bean;

import fpt.ncms.util.StringUtil.StringMatrix;


/**
 * Class ViewAddBean
 * Bean object for storing view information
 * @version 1.0 08-Apr-03
 * @author
 */
public final class ViewAddBean {
    private String strViewID = "";
    private String strTitle = "";
    private String strField = "";
    private String strOrderBy = "";
    /** Combo for sorting condition */
    private StringMatrix smComboOrderBy;

    /**
     * getViewID
     * Get view ident.number
     * @return  view ident.number
     */
    public final String getViewID() {
        return strViewID;
    }

    /**
     * setViewID
     * Set view ident.numbers
     * @param   inData - view ident.number
     */
    public final void setViewID(String inData) {
        strViewID = inData;
    }

    /**
     * getTitle
     * Get view title
     * @return  view title
     */
    public final String getTitle() {
        return strTitle;
    }

    /**
     * setTitle
     * Set view title
     * @param   inData - view title
     */
    public final void setTitle(String inData) {
        strTitle = inData;
    }

    /**
     * getField
     * Get view field names
     * @return  view field names
     */
    public final String getField() {
        return strField;
    }

    /**
     * setField
     * Set view field names
     * @param   inData - field names
     */
    public final void setField(String inData) {
        strField = inData;
    }

    /**
     * getOrderBy
     * Get sorting condition
     * @return  sort condition
     */
    public final String getOrderBy() {
        return strOrderBy;
    }

    /**
     * setOrderBy
     * Set sorting condition
     * @param   inData - sorting condition
     */
    public final void setOrderBy(String inData) {
        strOrderBy = inData;
    }

    /**
     * getComboOrderBy
     * Get combo order by
     * @return  combo order by
     */
    public final StringMatrix getComboOrderBy() {
        return smComboOrderBy;
    }

    /**
     * setComboOrderBy
     * Set combo order by
     * @param   inData - combo order by
     */
    public final void setComboOrderBy(StringMatrix inData) {
        smComboOrderBy = inData;
    }
}