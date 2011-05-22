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

import java.util.logging.Logger;

import openones.gaecore.PMF;
import openones.gate.store.dto.AuthorizationDTO;

/**
 * @author ThachLN
 *
 */
public class AuthorizationStore {
    final static Logger LOG = Logger.getLogger("AuthorizationStore");

    public Long save(AuthorizationDTO dto) {
        return (PMF.save(dto) ? dto.getKey(): null);
    }
    
    /**
     * [Give the description for method].
     * @param emailAddr
     * @param moduleId
     * @param screenId
     * @param eventId
     * @return
     */
    public boolean isExisted(String emailAddr, String moduleId, String screenId, String eventId) {
        // TODO Auto-generated method stub
        return false;
    }
}
