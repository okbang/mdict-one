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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
     * [Give the description for method].
     * 
     * @param form
     * @return
     */
    public static boolean save(TabSettingForm form) {
        // Get all module of tab
        List<ModuleDTO> tabModules = ModuleStore.getTabModules();
        
        // Convert string of key "N,N,..." into the list
        String[] tabKeys = form.getTabKeys().split(",");
        List<String> tabKeyList = Arrays.asList(tabKeys);
        
     // If tab form the form is not existed in tabModules. It means the tab will be deleted.
        for (ModuleDTO tabModule : tabModules) {
            if (!tabKeyList.contains(tabModule.getKey().toString())) {
                LOG.finest("Delete Tab Module " + tabModule.getKey());
                // Delete tabForm from the tabModules
                ModuleStore.delete(tabModule);
            }
        }

        boolean isNew;
        // Insert new tab
        for (String tabKey : tabKeys) {
            isNew = true;
            for (ModuleDTO tabModule : tabModules) {
                LOG.finest("tabKey=" + tabKey + ";tabModule key=" + tabModule.getKey());
                
                if (tabModule.getKey().toString().equals(tabKey)) {
                    isNew = false;
                    break;
                }
            }

            if (isNew) {
                LOG.finest("tabKey=" + tabKey + " is new.");
                // Add new tab form
                ModuleDTO module = new ModuleDTO(tabKey, tabKey, "None");
                module.setType("Tab");
                ModuleStore.save(module);
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
     * 
     * @return
     */
    public static TabSettingOutForm getTabs() {
        List<ModuleDTO> moduleList = ModuleStore.getTabModules();
        TabSettingOutForm outForm = new TabSettingOutForm();
        List<TabForm> tabFormList = new ArrayList<TabForm>();
        TabForm tabForm;
        for (ModuleDTO module : moduleList) {
            tabForm = new TabForm(module.getName());
            tabForm.setEmailMangers(module.getManagers());
            tabForm.setKey(module.getKey());

            tabFormList.add(tabForm);
        }

        outForm.setTabFormList(tabFormList);
        return outForm;
    }

}
