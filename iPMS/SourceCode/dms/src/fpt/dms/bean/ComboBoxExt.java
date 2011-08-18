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
 
 package fpt.dms.bean;

/**
 * Title:        Project
 * Description:
 * Copyright:    Copyright (C) 2001 Cogita FPTSoft
 * Company:      FPT Corporation
 * @author
 * @version 1.0.0
 */
import fpt.dms.framework.util.StringUtil.StringMatrix;

public class ComboBoxExt {
    /**
     * Added by Tu Ngoc Trung
     * Date: 2003-10-22
     * Purpose: Set default display strings for combo box*/
    public static final String STR_ALL_VALUE = "-1";
    public static final String STR_ALL_STRING = "(All)";
    public static final String STR_NONE_VALUE = "0";
    public static final String STR_NONE_STRING = "(None)";

    
    //Combo box types:
    /**     Normal combo box*/
    public static final int COMBO_NORMAL = 0;
    /**     Combo box with empty option*/
    public static final int COMBO_EMPTY = 1;
    /**     Combo box with [ALL] option*/
    public static final int COMBO_ALL = 2;
    /**     Combo box with [ALL] option and [NONE] option*/
    public static final int COMBO_ALL_NONE = 3;
    //End

    private StringMatrix strListing = new StringMatrix();

    public void setListing(StringMatrix list) {
        strListing = list;
    }

    public StringMatrix getListing() {
        return strListing;
    }

    public ComboBoxExt() {

    }
}