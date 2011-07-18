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

import openones.svnloader.dao.IRevisionManager;
import openones.svnloader.dao.entity.IRevision;
import openones.svnloader.daoimpl.entity.Revision;
import openones.svnloader.daoimpl.store.RevisionJpaController;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;

public class RevisionManager implements IRevisionManager {
    private RevisionJpaController jpaController;

    public RevisionManager() {
        jpaController = new RevisionJpaController();
    }

    @Override
    public void createRevision(Object revisionObj) throws Exception {
        Revision revision = (Revision) revisionObj;
        long nextID = jpaController.getNextId();
        revision.setRevisionID(nextID);
        jpaController.create(revision);
    }

    /**
     * [Explain the description for this method here].
     * @param id
     * @return
     * @see openones.svnloader.engine.manager.IRevisionManager#findRevision(long)
     */
    @Override
    public Revision findRevision(long id) {
        return jpaController.findRevision(id);
    }

    /**
     * [Explain the description for this method here].
     * @return
     * @see openones.svnloader.dao.IRevisionManager#newRevisionInst()
     */
    @Override
    public IRevision newRevisionInst() {
        return new Revision();
    }
}
