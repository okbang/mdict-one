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
package openones.gate.biz;

import java.util.List;

import openones.gae.users.OUser;
import openones.gate.Cons;
import openones.gate.Cons.ModuleType;
import openones.gate.store.ModuleStore;
import openones.gate.store.dto.ModuleDTO;
import rocky.common.Constant;

import com.google.appengine.api.datastore.Text;

/**
 * @author ThachLN
 *
 */
public class ModuleBiz {
    private OUser loginUser;
    private String langCd;
    /**
     * @param logonUser
     */
    public ModuleBiz(OUser logonUser, String langCd) {
        this.loginUser = loginUser;
        this.langCd = langCd;
    }

    /**
     * Get list of module from persistence layer.
     * @return
     */
    public List<ModuleDTO> getModules(ModuleType moduleType, String langCd) {
        return ModuleStore.getModules(moduleType.toString(), langCd);
//        FilterCond filterCond = new FilterCond(ModuleDTO.class, langCd);
//        return ModuleStore.getModules(filterCond, null);
    }

    public String getModuleConentById(String moduleId) {
        Text tabModuleContent = ModuleStore.getLastModuleContent(moduleId, langCd);
        String content = (tabModuleContent != null ? tabModuleContent.getValue() : Constant.BLANK);

        return content;
    }
}
