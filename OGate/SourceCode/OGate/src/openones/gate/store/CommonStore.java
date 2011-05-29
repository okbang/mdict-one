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

import java.util.Date;
import java.util.logging.Logger;

import openones.gaecore.PMF;
import openones.gate.biz.AuthorizationBiz;
import openones.gate.store.dto.ModuleDTO;
import rocky.common.CommonUtil;

/**
 * @author ThachLN
 *
 */
public class CommonStore {
    final static Logger LOG = Logger.getLogger("CommonStore");

    public static boolean save(ModuleDTO module) {
        LOG.info("intro.getContent()=" + module.getContent());
        if (module.getCreated() == null) {
            module.setCreated(new Date()); // set current Date
        }

        if (!CommonUtil.isNNandNB(module.getCreatedBy())) {
            module.setCreatedBy(AuthorizationBiz.getLogonUser().getEmail());
        }
        return PMF.save(module);
    }
}
