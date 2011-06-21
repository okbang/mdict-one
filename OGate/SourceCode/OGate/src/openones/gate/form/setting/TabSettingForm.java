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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    Map<String, TabForm> tabFormMap = null;
    private String tabKeys;

    /**
     * managersOfTab contains all email mangers of all tabs. Ex:: m1@tab1;m2@tab1 : m1@tab2;m2@tab2
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

    public Map<String, TabForm> getTabFormMap() {
        return tabFormMap;
    }

    /**
     * Get list of tab with order by "orderNo".
     * @return
     */
    public Collection<TabForm> getTabFormList() {
        Map<Integer, TabForm> sortTabMap = new TreeMap<Integer, TabForm>();

        if (tabFormMap != null) {
            for (TabForm tab : tabFormMap.values()) {
                sortTabMap.put(tab.getOrderNo(), tab);
            }
        }

        return sortTabMap.values();
    }

    public TabForm getTabByCode(String tabCode) {
        return (tabFormMap != null) ? tabFormMap.get(tabCode) : null;
    }

    public void setTabFormMap(Map<String, TabForm> tabFormMap) {
        this.tabFormMap = tabFormMap;
    }
    /**
     * [Give the description for method].
     * 
     * @param allTabs list of tab string. Tab string is fommated "<tab code>-<tab name>".
     */
    public void setTabForms(String[] allTabs) {
        if (tabFormMap == null) {
            tabFormMap = new HashMap<String, TabForm>();
        }

        TabForm tabForm;
        String[] tabStrings;
        int len = (allTabs != null) ? allTabs.length: 0;
        String tabName;
        for (int i = 0; i < len; i++) {
            tabName = allTabs[i];
            tabStrings = tabName.split(Cons.HYPHEN);
            if (tabStrings.length == 2) { // Not new tab
                tabForm = new TabForm(tabStrings[0], tabStrings[1]);
                tabForm.setOrderNo(i);
                tabFormMap.put(tabForm.getCode(), tabForm);
            } else { // Existed tab
                tabForm = new TabForm(Long.valueOf(tabName));
                tabForm.setOrderNo(i);
                tabFormMap.put(tabForm.getKey().toString(), tabForm);
            }

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
     * 
     * @param tabIndex order no of the tab
     * @return String of email with separator of comma or semi-comma
     */
    public String getManagerAtTab(int tabIndex) {
        String[] managers = managersOfTab.split(Cons.TAB_MANAGER_SEPARATOR);

        if (managers != null && (tabIndex < managers.length)) {
            return managers[tabIndex];
        } else {
            return Constant.BLANK;
        }
    }
}
