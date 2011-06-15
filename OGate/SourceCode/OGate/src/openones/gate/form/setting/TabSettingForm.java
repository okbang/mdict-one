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
import java.util.List;

import openones.corewa.BaseInForm;
import openones.gate.Cons;
import rocky.common.Constant;

/**
 * @author Thach Le
 */
public class TabSettingForm extends BaseInForm {
    private String selectedTab;
    /** List of email of editor. */
    private String emailManagers;

    private String newTab;

    List<TabForm> tabFormList = null;
    private String tabKeys;
    
    /** 
     * managersOfTab contains all email mangers of all tabs.
     * Ex:: m1@tab1;m2@tab1 : m1@tab2;m2@tab2
     */
    private String managersOfTab;

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
        for (String tabName : allTabs) {
            tabForm = new TabForm(tabName);
            tabFormList.add(tabForm);
        }
    }

    /**
     * List of tab key with delim is semi common.
     * 
     * @return
     */
    public String getTabKeys() {
        return tabKeys;
    }

    public void setTabKeys(String tabKeys) {
        this.tabKeys = tabKeys;
    }
    public String getManagersOfTab() {
        return managersOfTab;
    }
    public void setManagersOfTab(String managersOfTab) {
        this.managersOfTab = managersOfTab;
    }

    /**
     * Get list of email managers of tab.
     * @param tabIndex order no of the tab
     * @return String of email with separator of comma or semi-comma 
     */
    public  String getManagerAtTab(int tabIndex) {
        String[] managers = managersOfTab.split(Cons.TAB_MANAGER_SEPARATOR);

        if (managers != null && (tabIndex < managers.length)) {
            return managers[tabIndex];
        } else {
            return Constant.BLANK;
        }
    }
}
