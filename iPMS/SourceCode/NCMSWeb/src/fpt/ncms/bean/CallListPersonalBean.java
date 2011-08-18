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
 * @(#)NCListPersonalBean.java 01-Apr-03
 */


package fpt.ncms.bean;

import fpt.ncms.util.StringUtil.StringMatrix;

/**
 * Class NCListPersonalBean
 * Bean object for storing list of NC
 * @version 1.0 01-Apr-03
 * @author
 */
public final class CallListPersonalBean extends NCListBean {
    /** Ident.number of used view */
    private String strViewID;
    /** List of personal views */
    private StringMatrix smComboView;

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
     * getComboView
     * Get combo view
     * @return  combo view
     */
    public final StringMatrix getComboView() {
        return smComboView;
    }

    /**
     * setComboView
     * Set combo view
     * @param   inData - combo view
     */
    public final void setComboView(StringMatrix inData) {
        smComboView = inData;
    }

}