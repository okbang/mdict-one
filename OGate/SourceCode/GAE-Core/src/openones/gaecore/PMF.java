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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public final class PMF {
    final static Logger LOG = Logger.getLogger("PMF");
    private static final PersistenceManagerFactory pmfInstance = 
                          JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public static final String NO_FILTER = null;
    public static final String NO_PARAM = null;
    public static final String NO_IMPORT = null;
    public static final String NO_ORDERING = null;
    public static final Object[] NO_PARAMVALUE = null;
    
    private PMF() {
    }

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }

    public static boolean save(Object obj) {
        PersistenceManager pm = get().getPersistenceManager();
        try {
            pm.makePersistent(obj);
        } finally {
            pm.close();
        }
        return true;
    }


    public static Object getObjectByKey(Long key, Class clazz) {
        PersistenceManager pm = get().getPersistenceManager();

        Query query = pm.newQuery(clazz);
        query.setFilter("key == keyParam");
        query.declareParameters("Long keyParam");

        List<Object> eventDtoList = (List<Object>) query.execute(key);

        return (eventDtoList.iterator().hasNext()) ? eventDtoList.get(0) : null;
    }
    
    public static boolean delete(Long key, Class clazz) {
        PersistenceManager pm = get().getPersistenceManager();
        Query query = pm.newQuery(clazz);
        query.setFilter("key == keyParam");
        query.declareParameters("Long keyParam");

        try {
            query.deletePersistentAll(key);
        } catch (Exception ex) {
            LOG.log(Level.FINE, "delete Object '" + clazz + "' by key " + key, ex);
            return false;
        }
        return true;
    }
    
    public static boolean deleteAll(Class clazz) {
        PersistenceManager pm = get().getPersistenceManager();
        Query query = pm.newQuery(clazz);

        try {
            query.deletePersistentAll();
        } catch (Exception ex) {
            LOG.log(Level.FINE, "delete all Objects '" + clazz + "", ex);
            return false;
        }
        return true;
    }
    
    /**
     * Common query object with given condition.
     * @param clazz class of DTO object
     * @param filters the String array of the filters
     * @param imports the String array of the imports. The import statements separated by semicolons
     * @param parameters the String array of the parameters. The list of parameters separated by commas
     * @param ordering the ordering specification
     * @param values the Object Value array with all of the parameters
     * @return the filtered Collection.
     * Example:
     * String[] filters = {};
     * getObject
     */
//    public static Object getObjects(Class clazz, String[] filters, String[] imports, String[] parameters, String ordering, Object[] values) {
//        PersistenceManager pm = get().getPersistenceManager();
//        Query query = pm.newQuery(clazz);
//        
//        if (filters != null) {
//            for (String filter : filters) {
//                query.setFilter(filter);
//            }
//        }
//        
//        if (imports != null) {
//            for (String impo : imports) {
//                query.declareParameters(impo);
//            }
//        }
//        
//        if (parameters != null) {
//            for (String param : parameters) {
//                query.declareParameters(param);
//            }
//        }
//        
//        if (ordering != null) {
//            query.setOrdering(ordering);
//        }
//        
//        if ((values != null) && (values.length > 0)) {
//            return query.executeWithArray(values);
//        } else {
//            return query.execute();
//        }
//    }
    
    /**
     * Common query object with given condition.
     * @param clazz class of DTO object
     * @param filters the String array of the filters
     * @param imports the String array of the imports. The import statements separated by semicolons
     * @param parameters the String array of the parameters. The list of parameters separated by commas
     * @param ordering the ordering specification
     * @param values the Object Value array with all of the parameters
     * @return the filtered Collection.
     * Example:
     * String[] filters = {};
     * getObject
     */
    public static Object getObjects(Class clazz, String filters, String imports, String parameters, String ordering, Object[] values) {
        PersistenceManager pm = get().getPersistenceManager();
        Query query = pm.newQuery(clazz);
        
        if (filters != null) {
            query.setFilter(filters);
        }
        
        if (imports != null) {
            query.declareParameters(imports);
        }
        
        if (parameters != null) {
            query.declareParameters(parameters);
        }
        
        if (ordering != null) {
            query.setOrdering(ordering);
        }
        
        if ((values != null) && (values.length > 0)) {
            return query.executeWithArray(values);
        } else {
            return query.execute();
        }
    }
}
