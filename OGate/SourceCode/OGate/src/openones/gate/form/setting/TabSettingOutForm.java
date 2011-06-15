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

import openones.gate.Cons;

/**
 * @author Thach Le
 */
public class TabSettingOutForm extends TabSettingForm {
    public void addTabForm(TabForm tabForm) {
        if (tabFormList == null) {
            tabFormList = new ArrayList<TabForm>();
        }

        tabFormList.add(tabForm);
    }

    
    /** 
     * Build string of all email managers of all tabs.
     * Ex: m1@tab1;m2@tab1 : m1@tab2;m2@tab2
     * @return String of all email managers of all tabs. 
     */
    @Override
    public String getManagersOfTab() {
        StringBuffer buffer = null;
        for (TabForm tabForm : this.tabFormList) {
            if (buffer == null) {
                buffer = new StringBuffer(tabForm.getEmailManagersByString());
            } else {
                buffer.append(Cons.TAB_MANAGER_SEPARATOR).append(tabForm.getEmailManagersByString());
            }
        }
        
        return buffer.toString();
    }

}
