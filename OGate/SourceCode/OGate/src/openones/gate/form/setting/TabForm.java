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

import openones.gate.Cons;
import rocky.common.CommonUtil;

/**
 * @author Thach Le
 */
public class TabForm implements Serializable {
    private String code;
    private String name;

    /** List of email address can manage the tab. */
    private List<String> emailManagers = new ArrayList<String>();

    private Long key;

    /**
     * @param key
     */
    public TabForm(Long key) {
        super();
        this.key = key;
    }

    /**
     * @param tabName
     */
    public TabForm(String code, String tabName) {
        this.code = code;
        this.name = tabName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get displayed title of the tab.
     * @return <code>-<name>
     */
    public String getTitle() {
        return code + "-" + name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * [Give the description for method].
     * 
     * @param emailAddr
     * @return true if the emailAddr has not existed in the list.
     */
    public boolean addManager(String emailAddr) {
        if (emailManagers.contains(emailAddr)) {
            return false;
        } else {
            emailManagers.add(emailAddr);
            return true;
        }
    }

    public List<String> getEmailManagers() {
        return emailManagers;
    }

    public void setEmailManagers(List<String> emailManagers) {
        this.emailManagers = emailManagers;
    }

    public String getEmailManagersByString() {
        StringBuffer buffer = null;
        if (CommonUtil.isNNandNB(emailManagers)) {
            for (String emailManager : emailManagers) {
                if (buffer == null) {
                    buffer = new StringBuffer(emailManager);
                } else {
                    buffer.append(Cons.EMAIL_MANAGER_SEPARATOR).append(emailManager);
                }
            }
        } else {
            buffer = new StringBuffer();
        }

        return buffer.toString();
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
