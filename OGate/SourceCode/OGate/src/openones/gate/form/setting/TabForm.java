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
package openones.gate.form.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thach Le
 */
public class TabForm implements Serializable {
    private String name;

    /** List of email address can manage the tab. */
    private List<String> emailMangers = new ArrayList<String>();

    private Long key;

    /**
     * @param tabName
     */
    public TabForm(String tabName) {
        this.name = tabName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * [Give the description for method].
     * 
     * @param emailAddr
     * @return true if the emailAddr has not existed in the list.
     */
    public boolean addManager(String emailAddr) {
        if (emailMangers.contains(emailAddr)) {
            return false;
        } else {
            emailMangers.add(emailAddr);
            return true;
        }
    }

    public List<String> getEmailMangers() {
        return emailMangers;
    }

    public void setEmailMangers(List<String> emailMangers) {
        this.emailMangers = emailMangers;
    }

    /**
     * [Give the description for method].
     * @param key
     */
    public void setKey(Long key) {
        this.key = key;
    }

    public Long getKey() {
        return key;
    }
}
