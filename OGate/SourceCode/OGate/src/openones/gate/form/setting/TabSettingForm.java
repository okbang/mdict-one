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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import openones.corewa.BaseInForm;

/**
 * @author Thach Le
 *
 */
public class TabSettingForm extends BaseInForm {
    private String selectedTab;
    /** List of email of editor. */
    private String emailManagers;
    
    private String newTab;
    private String newEmailManager;

    List<TabForm> tabFormList = null;
    private String tabKeys;
    
    public String getSelectedTab() {
        return selectedTab;
    }
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }
    public String getEmailManagers() {
        return emailManagers;
    }
    public void setEmailManagers(String emailManagers) {
        this.emailManagers = emailManagers;
    }
    public String getNewTab() {
        return newTab;
    }
    public void setNewTab(String newTab) {
        this.newTab = newTab;
    }
    public String getNewEmailManager() {
        return newEmailManager;
    }
    public void setNewEmailManager(String newEmailManager) {
        this.newEmailManager = newEmailManager;
    }
    
    public List<TabForm> getTabFormList() {
        return tabFormList;
    }

    public void setTabFormList(List<TabForm> tabFormList) {
        this.tabFormList = tabFormList;
    }
    /**
     * [Give the description for method].
     * @param allTabs
     */
    public void setTabForms(String[] allTabs) {
        if (tabFormList == null) {
            tabFormList = new ArrayList<TabForm>();
        }
        
        TabForm tabForm;
        for (String tabName: allTabs) {
            tabForm = new TabForm(tabName);
            tabFormList.add(tabForm);
        }
    }
    
    /**
     * List of tab key with delim is semi common.
     * @return
     */
    public String getTabKeys() {
        return tabKeys;
    }
    
    public void setTabKeys(String tabKeys) {
        this.tabKeys = tabKeys;
    }
}
