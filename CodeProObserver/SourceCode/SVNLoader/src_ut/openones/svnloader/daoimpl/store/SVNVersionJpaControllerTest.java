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
package openones.svnloader.daoimpl.store;

import static org.junit.Assert.fail;
import openones.svnloader.daoimpl.entity.Dir;
import openones.svnloader.daoimpl.entity.Revision;
import openones.svnloader.daoimpl.entity.SVNVersion;
import openones.svnloader.daoimpl.store.DirJpaController;
import openones.svnloader.daoimpl.store.RevisionJpaController;
import openones.svnloader.daoimpl.store.SVNVersionJpaController;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;

import org.junit.Test;


/**
 * @author ThachLN
 *
 */
public class SVNVersionJpaControllerTest {

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#getEntityManager()}.
     */
    @Test
    public void testGetEntityManager() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#create(openones.svnloader.daoimpl.entity.SVNVersion)}.
     */
    @Test
    public void testCreate() {
        SVNVersionJpaController jpa = new SVNVersionJpaController();
        // Create dir
        DirJpaController dirJpa = new DirJpaController();
        RevisionJpaController revisionJpa = new RevisionJpaController();
        Dir dir = new Dir(0);
        try {
            dirJpa.create(dir);
            Revision revision = new Revision(Long.valueOf(0), 0);
            revisionJpa.create(revision );
            
            SVNVersion svnVersion = new SVNVersion(0, "FileCheckedCode.java", 0);
            svnVersion.setEffort(2.0);
            svnVersion.setNmStaticBug(10);
            svnVersion.setNmUTBug(15);

            
            jpa.create(svnVersion);
        } catch (PreexistingEntityException e) {
           
            e.printStackTrace();
            fail();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#edit(openones.svnloader.daoimpl.entity.SVNVersion)}.
     */
    @Test
    public void testEdit() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#destroy(openones.svnloader.daoimpl.entity.SVNVersionPK)}.
     */
    @Test
    public void testDestroy() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#findSVNVersionEntities()}.
     */
    @Test
    public void testFindSVNVersionEntities() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#findSVNVersionEntities(int, int)}.
     */
    @Test
    public void testFindSVNVersionEntitiesIntInt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#findSVNVersion(openones.svnloader.daoimpl.entity.SVNVersionPK)}.
     */
    @Test
    public void testFindSVNVersion() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.daoimpl.store.SVNVersionJpaController#getSVNVersionCount()}.
     */
    @Test
    public void testGetSVNVersionCount() {
        fail("Not yet implemented");
    }

}
