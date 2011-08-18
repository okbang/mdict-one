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
 * @(#)ConstantListBean.java 31-Mar-03
 */


package fpt.ncms.bean;

import fpt.ncms.util.StringUtil.StringMatrix;

/**
 * Class ConstantListBean
 * Bean object for storing list of NC constant
 * @version 1.0 31-Mar-03
 * @author
 */
public final class ConstantListBean {
    /** list of NC constant */
    private StringMatrix arrConstantList = null;
    /** sorting condition */
    private String strSortBy = "All";
    /** default page is the first page, no doubt */
    private int nNumPage = 1;
    /** number of record */
    private int nTotal = 0;

    private StringMatrix smComboSortBy;

    /**
     * getConstantList
     * Get list of NC constants
     * @return  list of NC constants
     */
    public final StringMatrix getConstantList() {
        return arrConstantList;
    }

    /**
     * setConstantList
     * Set list of constants
     * @param   inData - list of constants
     */
    public final void setConstantList(StringMatrix inData) {
        arrConstantList = inData;
    }

    /**
     * getSortBy
     * Get sorting condition
     * @return  sort condition
     */
    public final String getSortBy() {
        return strSortBy;
    }

    /**
     * setSortBy
     * Set sorting condition
     * @param   inData - sorting condition
     */
    public final void setSortBy(String inData) {
        strSortBy = inData;
    }

    /**
     * getNumPage
     * Get number of current page
     * @return  number of current page
     */
    public final int getNumPage() {
        return nNumPage;
    }

    /**
     * setNumPage
     * Set number of current page
     * @param   inData - number of page
     */
    public final void setNumPage(int inData) {
        nNumPage = inData;
    }

    /**
     * getTotal
     * Get number of records
     * @return  number of records
     */
    public final int getTotal() {
        return nTotal;
    }

    /**
     * setTotal
     * Set number of records
     * @param   inData - number of records
     */
    public final void setTotal(int inData) {
        nTotal = inData;
    }

    /**
     * getComboSortBy
     * Get combo order by
     * @return  combo order by
     */
    public final StringMatrix getComboSortBy() {
        return smComboSortBy;
    }

    /**
     * setComboSortBy
     * Set combo order by
     * @param   inData - combo order by
     */
    public final void setComboSortBy(StringMatrix inData) {
        smComboSortBy = inData;
    }
}