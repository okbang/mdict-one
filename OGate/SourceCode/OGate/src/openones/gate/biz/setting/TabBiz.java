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
package openones.gate.biz.setting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import openones.gate.Cons;
import openones.gate.form.setting.TabForm;
import openones.gate.form.setting.TabSettingForm;
import openones.gate.form.setting.TabSettingOutForm;
import openones.gate.store.ModuleStore;
import openones.gate.store.dto.ModuleDTO;

/**
 * @author Thach Le
 */
public class TabBiz {
    final static Logger LOG = Logger.getLogger("TabBiz");

    /**
     * Save the tab setting.
     * For current tabs, update the order.
     * @param form
     * @return
     */
    public static boolean save(TabSettingForm form, String langCd) {
        // Get all module of tab
        List<ModuleDTO> tabModules = ModuleStore.getModules(Cons.ModuleType.Tab.toString(), langCd);

        // Convert string of key "N,N,..." into the list
        if (form.getTabKeys() == null) { // remove all tab
            //ModuleStore.deleteAll(Cons.ModuleType.Tab.toString());
            for (ModuleDTO tabModule : tabModules) {
                LOG.finest("Delete Tab Module " + tabModule.getKey());
                ModuleStore.delete(tabModule);
            }
            
            return true;
        }
        String[] tabKeys = form.getTabKeys().split(",");
        List<String> tabKeyList = Arrays.asList(tabKeys);

        // If tab form the form is not existed in tabModules. It means the tab will be deleted.
        for (ModuleDTO tabModule : tabModules) {
            LOG.finest("Check tab key:" + tabModule.getKey().toString());
            if (!tabKeyList.contains(tabModule.getKey().toString())) {
                LOG.finest("Delete Tab Module " + tabModule.getKey());
                // Delete tabForm from the tabModules
                ModuleStore.delete(tabModule);
            }
        }

        boolean isNew;
        ModuleDTO updateTabModule = null; 
        // Insert new tab
        int len = (tabKeys != null? tabKeys.length: -1);
        String tabKey;
        TabForm tab;
        for (int i = 0; i < len; i++) {
            tabKey= tabKeys[i];
        //for (String tabKey : tabKeys) {
            isNew = true;
            for (ModuleDTO tabModule : tabModules) {
                LOG.finest("tabKey=" + tabKey + ";tabModule key=" + tabModule.getKey());

                // Keep the current tab to update if it existed.
                updateTabModule = tabModule;

                if (tabModule.getKey().toString().equals(tabKey)) {
                    isNew = false;
                    break;
                }
            }

            if (isNew) {
                LOG.finest("tabKey=" + tabKey + " is new." + ";managers=" + form.getManagerAtTab(i));
                String tabName = tabKey;
                tab = form.getTabByCode(tabKey);
                saveTabModule(tabKey, tab.getName(), form.getManagerAtTab(i), i, langCd);
            } else { // Update
                LOG.finest("Update tab '" + updateTabModule.getName() + "', key=" + updateTabModule.getKey()
                        + " with orderNo=" + i + ";managers=" + form.getManagerAtTab(i));
                updateTabModule.setOrderNo(i);
                updateTabModule.setManagers(form.getManagerAtTab(i));
                ModuleStore.update(updateTabModule);
            }
        }
        return true;
//        ModuleDTO module = new ModuleDTO(form.getSelectedTab(), form.getSelectedTab(), "None");
//        StringTokenizer tokenizer = new StringTokenizer(form.getEmailManagers(), "\n;,");
//
//        while (tokenizer.hasMoreTokens()) {
//            module.addManager(tokenizer.nextToken());
//        }
//
//        module.setType("Tab");
//
//        return ModuleStore.save(module);
    }

    /**
     * [Give the description for method].
     * @param tabKey
     */
    private static void saveTabModule(String moduleId, String name, String emailManagers, int orderNo, String langCd) {
        // Add new tab form
        ModuleDTO module = new ModuleDTO(moduleId, name, "This is the context of the tab.");
        module.setType(Cons.ModuleType.Tab.toString());
        module.setManagers(emailManagers);
        module.setOrderNo(orderNo);
        module.setLang(langCd);
        ModuleStore.save(module);
        
    }

    /**
     * [Give the description for method].
     * 
     * @return
     */
    public static TabSettingOutForm getTabs(String langCd) {
        List<ModuleDTO> moduleList = ModuleStore.getModules(Cons.ModuleType.Tab.toString(), langCd);
        TabSettingOutForm outForm = new TabSettingOutForm();
        Map<String, TabForm> tabFormMap = new HashMap<String, TabForm>();
        TabForm tabForm;
        for (ModuleDTO module : moduleList) {
            tabForm = new TabForm(module.getId(), module.getName());
            tabForm.setEmailManagers(module.getManagers());
            tabForm.setKey(module.getKey());
            tabForm.setOrderNo(module.getOrderNo());

            tabFormMap.put(tabForm.getCode(), tabForm);
        }

        outForm.setTabFormMap(tabFormMap);
        return outForm;
    }

}
