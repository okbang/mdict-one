/**
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package openones.idict.portlet.form;

import java.io.Serializable;

/**
 * @author Thach Le
 */
public class DictInfo implements Serializable {
    /** Dictionary code. */
    private String cd;

    /** Dictionary name. */
    private String name;

    /** Contain meaning of an word in this dictionary. */
    private String meaning;

    /**
     * Create in instance of DictInfo with code and name.
     * @param code unique id of the dictionary
     * @param name short name of dictionary
     */
    public DictInfo(String code, String name) {
        this.cd = code;
        this.name = name;
    }

    /**
     * Get value of cd.
     * @return the cd
     */
    public String getCd() {
        return cd;
    }

    /**
     * Set the value for cd.
     * @param cd the cd to set
     */
    public void setCd(String cd) {
        this.cd = cd;
    }

    /**
     * Get value of name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value for name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get value of meaning.
     * @return the meaning
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * Set the value for meaning.
     * @param meaning the meaning to set
     */
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
