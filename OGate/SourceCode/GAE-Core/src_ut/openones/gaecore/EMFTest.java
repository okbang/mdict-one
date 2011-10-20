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
package openones.gaecore;

import static org.junit.Assert.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import openones.gaecore.entity.SessionEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author ThachLN
 */
public class EMFTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    /**
     * Test method for {@link openones.gaecore.EMF#get()}.
     */
    @Test
    public void testPersist() {
        EntityManagerFactory emf = EMF.get();

        EntityManager em = emf.createEntityManager();
        try {
            SessionEntity entity = new SessionEntity();
            // entity.setId(Long.valueOf(1));
            entity.setIp("10.1.1.1");
            em.persist(entity);
            
            Long id = entity.getId();

            entity = em.find(SessionEntity.class, id);

            assertEquals("10.1.1.1", entity.getIp());
        } finally {
            em.close();
        }
    }

    @Test
    public void testPersistService() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity tom = new Entity("Person", "tom");
        tom.setProperty("age", 32);
        
        Key tomKey = ds.put(tom);
        
        tomKey = KeyFactory.createKey("Person", "tom");
        System.out.println("Tom key=" + tomKey);

        Entity lucy = new Entity("Person", "lucy");
        ds.put(lucy);

        try {
            tom = ds.get(tomKey);
            assertEquals(Long.valueOf(32), tom.getProperty("age"));
        } catch (EntityNotFoundException ex) {
            fail(ex.getMessage());
            ex.printStackTrace();
        }

    }

    @Test
    public void testFind() {
        EntityManagerFactory emf = EMF.get();

        EntityManager em = emf.createEntityManager();
        try {
            SessionEntity entity = em.find(SessionEntity.class, 1);

            assertEquals("10.1.1.1", entity.getIp());
        } finally {
            em.clear();
        }
    }

}
