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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openones.gate.form.setting.LangSettingForm;
import openones.gate.store.LangSettingStore;
import openones.gate.store.dto.LangDTO;
import rocky.common.Constant;

/**
 * @author ThachLN
 *
 */
public class LangSettingBiz {

    /**  . */
    private static final String HYPHEN = "-";
    Map<String, LangDTO> langMap = new HashMap<String, LangDTO>();
    
    /**
     * Get list of languages.
     * @return String of line of language.
     * Lines are separated by \n
     * Format of line:
     * <Language code>-<Language name>
     * Ex:
     * vn-Việt Nam
     */
    public String getLangs() {
        // Update the map of lang
        langMap = new HashMap<String, LangDTO>();
        
        LangSettingStore store = new LangSettingStore();

        List<LangDTO> langList = store.getLangs();

        StringBuffer buff = new StringBuffer();
        for (LangDTO langDto : langList) {
            buff.append(langDto.getCd()).append(HYPHEN).append(langDto.getName()).append(Constant.LF);
            
            // Store lang into the map
            langMap.put(langDto.toString(), langDto);
        }
        return buff.toString();
    }
    
    public int save(LangSettingForm form) {
        int nCount = 0;
        String[] langs = form.getLanguages().split(Constant.LF);
        
        // Parse lines of languages;
        String[] langTokens;
        String langCd;
        String langName;
        LangDTO langDto;
        for (String lang: langs) {
            langTokens = lang.split(HYPHEN);
            if ((langTokens != null) && (langTokens.length != 2)) {
                langCd = langTokens[0];
                langName = langTokens[1];
                
                if (!langMap.containsKey(langCd + HYPHEN + langName)) {
                    langDto = new LangDTO(langCd, langName);
                    LangSettingStore.save(langDto);
                    nCount++;
                }
                
            }
        }
        
        return nCount;
    }
}
