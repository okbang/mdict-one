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
 * @(#)CommonListModel.java 23-April-03
 */

package fpt.timesheet.ApproveTran.ejb.common;

/**
 * Class CommonListModel
 * Description: Common List Model
 * @version 1.0 23-April-03
 * @author
 */
public class CommonListModel implements java.io.Serializable {

    private String Key;
    private String Code;
    private String Name;

    /**
     * CommonListModel
     * Class constructor
     */
    public CommonListModel() {
        this.Key = "";
        this.Code = "";
        this.Name = "";
    }

    /**
     * CommonListModel
     * Class constructor
     * @param strKey - init.key
     * @param strCode - init.code
     * @param strName - init.name
     */
    public CommonListModel(String strKey, String strCode, String strName) {
        this.Key = strKey;
        this.Code = strCode;
        this.Name = strName;
    }

    /**
     * setCode
     * Setter method for Code
     * @param str - input data for setting
     */
    public void setCode(String str) {
        this.Code = str;
    }

    /**
     * getCode
     * Getter method for code
     * @return Code
     */
    public String getCode() {
        return this.Code;
    }

    /**
     * setName
     * Setter method for Name
     * @param str - input data for setting
     */
    public void setName(String str) {
        this.Name = str;
    }

    /**
     * getName
     * Getter method for Name
     * @return Name
     */
    public String getName() {
        return this.Name;
    }

    /**
     * setKey
     * Setter method for Key
     * @param str - input data for setting
     */
    public void setKey(String str) {
        this.Key = str;
    }

    /**
     * getKey
     * Getter method for Key
     * @return Key
     */
    public String getKey() {
        return this.Key;
    }

}

