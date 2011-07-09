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
package openones.svnloader.daoimpl;

import openones.svnloader.dao.ISVNVersionManager;
import openones.svnloader.daoimpl.entity.SVNVersion;
import openones.svnloader.daoimpl.store.SVNVersionJpaController;

public class SVNVersionManager implements ISVNVersionManager {
    private SVNVersionJpaController jpaController;

    public SVNVersionManager() {
        jpaController = new SVNVersionJpaController();
    }

    /**
     * [Explain the description for this method here].
     * @param svnVersion
     * @throws Exception
     * @see openones.svnloader.engine.manager.ISVNVersionManager#createVersion(openones.svnloader.daoimpl.entity.SVNVersion)
     */
    @Override
    public void createVersion(SVNVersion svnVersion) throws Exception {
        try {
            jpaController.create(svnVersion);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
