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

import javax.persistence.EntityManager;

import openones.svnloader.dao.DaoManager;
import openones.svnloader.dao.IDirManager;
import openones.svnloader.dao.IRevisionManager;
import openones.svnloader.dao.ISVNFileManager;
import openones.svnloader.dao.ISVNRepoManager;
import openones.svnloader.dao.ISVNVersionManager;
import openones.svnloader.daoimpl.store.PersistentManager;

/**
 * @author Thach Le
 *
 */
public class JpaDaoManager extends DaoManager {
    EntityManager em = PersistentManager.getEntityManager();

    /**
     * [Explain the description for this method here].
     * @see openones.svnloader.dao.DaoManager#beginTransaction()
     */
    @Override
    public void beginTransaction() {
        em.getTransaction().begin();
    }

    /**
     * [Explain the description for this method here].
     * @see openones.svnloader.dao.DaoManager#commitTransaction()
     */
    @Override
    public void commitTransaction() {
        em.getTransaction().commit();
    }

    /**
     * [Explain the description for this method here].
     * @see openones.svnloader.dao.DaoManager#rollbackTransaction()
     */
    @Override
    public void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    /**
     * [Explain the description for this method here].
     * @see openones.svnloader.dao.DaoManager#close()
     */
    @Override
    public void close() {
        em.close();
    }

    /**
     * [Explain the description for this method here].
     * @return
     * @see openones.svnloader.dao.DaoManager#newDirManagerInst()
     */
    @Override
    public IDirManager newDirManagerInst() {
        return new DirManager();
    }

    /**
     * [Explain the description for this method here].
     * @return
     * @see openones.svnloader.dao.DaoManager#newRevisionManagerInst()
     */
    @Override
    public IRevisionManager newRevisionManagerInst() {
        return new RevisionManager();
    }

    /**
     * [Explain the description for this method here].
     * @return
     * @see openones.svnloader.dao.DaoManager#newSVNFileManagerInst()
     */
    @Override
    public ISVNFileManager newSVNFileManagerInst() {
        return new SVNFileManager();
    }

    /**
     * [Explain the description for this method here].
     * @return
     * @see openones.svnloader.dao.DaoManager#newSVNRepoManagerInst()
     */
    @Override
    public ISVNRepoManager newSVNRepoManagerInst() {
        return new SVNRepoManager();
    }

    /**
     * [Explain the description for this method here].
     * @return
     * @see openones.svnloader.dao.DaoManager#newSVNVersionManagerInst()
     */
    @Override
    public ISVNVersionManager newSVNVersionManagerInst() {
        return new SVNVersionManager();
    }
}
