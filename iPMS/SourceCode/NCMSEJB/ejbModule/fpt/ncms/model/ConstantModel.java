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
 
 package fpt.ncms.model;

import java.io.Serializable;

/**
 * @author trungtn
 * @since 2003-December-27
 */
public class ConstantModel implements Serializable {
    private int n_ID;
    private String strType;
    private String strDescription;
    private int n_SystemType;
    private int n_Usage;
    /**
     * Returns the ID.
     * @return int
     */
    public int getID() {
        return n_ID;
    }

    /**
     * Returns the usage.
     * @return int
     */
    public int getUsage() {
        return n_Usage;
    }

    /**
     * Returns the description.
     * @return String
     */
    public String getDescription() {
        return strDescription;
    }

    /**
     * Returns the systemType.
     * @return String
     */
    public int getSystemType() {
        return n_SystemType;
    }

    /**
     * Returns the type.
     * @return String
     */
    public String getType() {
        return strType;
    }

    /**
     * Sets the iD.
     * @param iD The iD to set
     */
    public void setID(int nID) {
        n_ID = nID;
    }

    /**
     * Sets the usage.
     * @param usage The usage to set
     */
    public void setUsage(int usage) {
        n_Usage = usage;
    }

    /**
     * Sets the description.
     * @param description The description to set
     */
    public void setDescription(String description) {
        strDescription = description;
    }

    /**
     * Sets the systemType.
     * @param systemType The systemType to set
     */
    public void setSystemType(int systemType) {
        n_SystemType = systemType;
    }

    /**
     * Sets the type.
     * @param type The type to set
     */
    public void setType(String type) {
        strType = type;
    }

}
