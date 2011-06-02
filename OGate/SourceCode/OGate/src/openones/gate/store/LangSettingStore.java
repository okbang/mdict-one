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
package openones.gate.store;

import java.util.List;

import openones.gaecore.PMF;
import openones.gate.store.dto.LangDTO;

/**
 * @author ThachLN
 */
public class LangSettingStore {
    public static List<LangDTO> getLangs() {
        List<LangDTO> langObjList = (List<LangDTO>) PMF.getObjects(LangDTO.class, PMF.NO_FILTER, PMF.NO_IMPORT,
                                                                   PMF.NO_PARAM, "name desc", PMF.NO_PARAMVALUE);

        return langObjList;
    }

    /**
     * [Give the description for method].
     * @param langDto
     */
    public static void save(LangDTO langDto) {
        PMF.save(langDto);

    }

    /**
     * [Give the description for method].
     * @param key
     * @return
     */
    public static boolean delete(Long key) {
        return PMF.delete(key, LangDTO.class);
    }
}
