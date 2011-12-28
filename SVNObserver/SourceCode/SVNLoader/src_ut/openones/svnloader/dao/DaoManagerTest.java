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
package openones.svnloader.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Thach Le
 *
 */
public class DaoManagerTest {

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#getInstance()}.
     */
    @Test
    public void testGetInstance0001() {
        DaoManager daoManager = DaoManager.getInstance();
        assertNotNull(daoManager);
        assertEquals("openones.svnloader.daoimpl.JpaDaoManager", daoManager.getClass().getName());

    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#beginTransaction()}.
     */
    @Test
    public void testBeginTransaction() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#commitTransaction()}.
     */
    @Test
    public void testCommitTransaction() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#rollbackTransaction()}.
     */
    @Test
    public void testRollbackTransaction() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#close()}.
     */
    @Test
    public void testClose() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#newDirManagerInst()}.
     */
    @Test
    public void testNewDirManagerInst() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#newRevisionManagerInst()}.
     */
    @Test
    public void testNewRevisionManager() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#newSVNFileManagerInst()}.
     */
    @Test
    public void testNewSVNFileManager() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#newSVNRepoManagerInst()}.
     */
    @Test
    public void testNewSVNRepoManager() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.svnloader.dao.DaoManager#newSVNVersionManagerInst()}.
     */
    @Test
    public void testNewSVNVersionManager() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#Object()}.
     */
    @Test
    public void testObject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#getClass()}.
     */
    @Test
    public void testGetClass() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#hashCode()}.
     */
    @Test
    public void testHashCode() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
     */
    @Test
    public void testEquals() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#clone()}.
     */
    @Test
    public void testClone() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#toString()}.
     */
    @Test
    public void testToString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#notify()}.
     */
    @Test
    public void testNotify() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#notifyAll()}.
     */
    @Test
    public void testNotifyAll() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#wait(long)}.
     */
    @Test
    public void testWaitLong() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#wait(long, int)}.
     */
    @Test
    public void testWaitLongInt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#wait()}.
     */
    @Test
    public void testWait() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link java.lang.Object#finalize()}.
     */
    @Test
    public void testFinalize() {
        fail("Not yet implemented");
    }

}
