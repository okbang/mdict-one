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

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import rocky.common.PropertiesManager;

/**
 * @author ThachLN
 *
 */
public abstract class DaoManager {
    final static Logger LOG = Logger.getLogger("DaoManager");
    
    static DaoManager daoInstance = null;
    
    /**
     * [Give the description for method].
     * @return
     */
    public static DaoManager getInstance() {
        if (daoInstance == null) {
            try {
                Properties props = PropertiesManager.newInstanceFromProps("/conf.properties");
                // get class of DaoManager implementation.
                String daoManagerImplClassName = props.getProperty("DaoManagerImpl", "openones.svnloader.daoimpl.DefaultDaoManager");
                Class daoManagerClass = Class.forName(daoManagerImplClassName);
                daoInstance = (DaoManager) daoManagerClass.newInstance();
            } catch (IOException ex) {
                LOG.error("Load configuration resource file /conf.properties" , ex);
            } catch (ClassNotFoundException ex) {
                LOG.error("Create class for DaoManagerImpl in /conf.properties" , ex);
            } catch (InstantiationException ex) {
                LOG.error("Create instance for DaoManagerImpl in /conf.properties" , ex);
            } catch (IllegalAccessException ex) {
                LOG.error("Create instance for DaoManagerImpl in /conf.properties" , ex);
            }
        }

        return daoInstance;
    }


    /**
     * [Give the description for method].
     */
    public abstract void beginTransaction();
    /**
     * [Give the description for method].
     */
    public abstract void commitTransaction();

    /**
     * [Give the description for method].
     */
    public abstract void rollbackTransaction();

    /**
     * [Give the description for method].
     */
    public abstract void close();

    public abstract IDirManager newDirManagerInst();
    public abstract IRevisionManager newRevisionManagerInst();
    public abstract ISVNFileManager newSVNFileManagerInst();
    public abstract ISVNRepoManager newSVNRepoManagerInst();
    public abstract  ISVNVersionManager newSVNVersionManagerInst();
}
