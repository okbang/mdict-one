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
package openones.gae.session;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import openones.gae.group.GoogleGroup;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Thach Le
 *
 */
public class SessionCounter implements HttpSessionListener {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());
    static final String SESSION_COUNTER = "SessionCounter";
    private static int nmSession = 0;
    private static long nmHit = 0;
    private static int nmGGroupMembers = 0;
    
    private Key sessionKey;
    private Entity entityKey;
    final static String K_NMHIT = "NHHIT";
    private static final String K_NMMEMBER_GGROUP = "NMMEMBER_GROUP";
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    private String groupUrl = "http://groups.google.com/group/open-ones";
    /**
     * 
     */
    public SessionCounter() {
        super();
        initEntity();
    }

    /**
     * [Give the description for method].
     */
    private void initEntity() {
        sessionKey = KeyFactory.createKey(this.getClass().getName(), this.getClass().getName());
        entityKey = new Entity(this.getClass().getName(), this.getClass().getName());
        
        try {
            entityKey = ds.get(sessionKey);
            // Create entity to count session 
            if ((entityKey == null || entityKey.getProperty(K_NMHIT) == null)) {
               entityKey.setProperty(K_NMHIT, Long.valueOf(nmHit));
               ds.put(entityKey);
            } else { // The session count is already
                nmHit = (Long) entityKey.getProperty(K_NMHIT);
            }
        } catch (Throwable th) {
            LOG.log(Level.WARNING, "Create new a session instance", th);
        }
    }

    public SessionCounter(String groupUrl) {
        super();
        initEntity();
        this.groupUrl = groupUrl;
    }
    
    
    /*
     * Explain the description for this method here
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        nmSession++;
        nmHit++;
        LOG.info("Number of sessions: " + nmSession);
        
        // Save the number of hits
        try {
            entityKey.setProperty(K_NMHIT, nmHit);
            ds.put(entityKey);
        } catch (Throwable th) {
            LOG.log(Level.WARNING, "Save the number of his: " + nmHit, th);
        }
        
        // Get the number of members
        GoogleGroup gGroup = new GoogleGroup(groupUrl);
        int numOfMembers = gGroup.getNumOfMembers();
        if (numOfMembers > -1) {
            nmGGroupMembers = numOfMembers;
            try {
                entityKey.setProperty(K_NMMEMBER_GGROUP, nmGGroupMembers);
                ds.put(entityKey);
            } catch (Throwable th) {
                LOG.log(Level.WARNING, "Save the number of members of group '" + groupUrl + "'", th);
            }
        }
    }

    /*
     * Explain the description for this method here
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        nmSession--;
    }

    public static int getNumberOfGGroupMembers() {
        return nmGGroupMembers;
    }
    
    public static int getNumberOfSessions() {
        return nmSession;
    }
    
    public static long getNmHits() {
        return nmHit;
    }
}
