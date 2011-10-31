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
package openones.gate.header.form;

import java.util.ArrayList;
import java.util.List;

import openones.corewa.BaseOutForm;
import rocky.common.CommonUtil;

/**
 * @author ThachLN
 *
 */
public class HeaderOutForm extends BaseOutForm {
    public final static String[] LANGS = {"Việt", "English", "Japanese"};
    public final static String[] LANGCODES = {"vn", "en", "jp"};
    private List<LangForm> langList = new ArrayList<LangForm>(2);
    private String selectedLang = "Việt";
    
    public HeaderOutForm() {
        int i = 0;
        for (String lang : LANGS) {
            langList.add(new LangForm(LANGCODES[i], LANGS[i]));
            i++;
        }
    }
    
    public String getLangCd(String langName) {
        int idx = CommonUtil.find(LANGS, langName);
        return ((idx > -1) && idx < LANGCODES.length) ? LANGCODES[idx] : null;
    }
    public List<LangForm> getLangList() {
        return langList;
    }
    public void setLangList(List<LangForm> langList) {
        this.langList = langList;
    }
    public String getSelectedLang() {
        return selectedLang;
    }
    public void setSelectedLang(String selectedLang) {
        this.selectedLang = selectedLang;
    }
    
    /**
     * Get list of supported languages.
     * @return Array of string of language
     */
    public String[] getLanguages() {
        return LANGS;
    }
}
