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

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import openones.gaecore.PMF;
import openones.gate.biz.SessionBiz;
import openones.gate.store.dto.ModuleContentDTO;
import openones.gate.store.dto.ModuleDTO;
import rocky.common.CommonUtil;

import com.google.appengine.api.datastore.Text;

/**
 * @author ThachLN
 */
public class ModuleStore {
    /**  . */
    private static final int MAX_RECS = 5;
    final static Logger LOG = Logger.getLogger("ModuleStore");
    //final static String MOD_ID = "intro";

    public static boolean save(ModuleDTO module) {
        LOG.info("intro.getContent()=" + module.getContent());
        if (module.getCreated() == null) {
            module.setCreated(new Date()); // set current Date
        }

        if (!CommonUtil.isNNandNB(module.getCreatedBy())) {
            if (SessionBiz.getLogonUser() != null) {
                module.setCreatedBy(SessionBiz.getLogonUser().getEmail());
            }
        }
        return PMF.save(module);
    }

    /**
     * Save/Update the content of Tab Module.
     * @param key
     * @param tabContent
     * @return
     */
    public static boolean saveContent(Long key, Text tabContent, String langCd) {
        // Get instance of Module. Module Id is used
        ModuleDTO moduleDTO = (ModuleDTO) PMF.getObjectByKey(key, ModuleDTO.class);

        ModuleContentDTO moduleContentDTO = new ModuleContentDTO();
        moduleContentDTO.setModuleId(moduleDTO.getId());
        moduleContentDTO.setLang(langCd);
        moduleContentDTO.setContent(tabContent);

        moduleContentDTO.setCreated(new Date()); // set current Date
        if (SessionBiz.getLogonUser() != null) {
            moduleContentDTO.setCreatedBy(SessionBiz.getLogonUser().getEmail());
        }

        PMF.save(moduleContentDTO);

        return true;
    }

    /**
     * [Give the description for method].
     * 
     * @return
     */
    public static Text getLastModuleContent(String moduleId, String langCd) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // Get top 5 newest contents
        //String query = "select from " + ModuleDTO.class.getName() + " order by created desc range 0,5";
        
        Query query =  pm.newQuery("select content from " + ModuleContentDTO.class.getName() );
        query.setOrdering("created desc");
        query.setRange(0, MAX_RECS);
        query.declareParameters("String moduleIdParam, String langParam");
        query.setFilter("moduleId == moduleIdParam && lang == langParam");

        try {
            List<Text> modContentList = (List<Text>) query.execute(moduleId, langCd);

            if (modContentList.isEmpty()) {
                return null;
            } else {
                return modContentList.get(0);
            }
        } finally {
            query.closeAll();
        }
    }

    /**
     * Get top 5 of content of module.
     * @param type Kind of module. Ex Tab, Layout
     * @param moduleId
     * @return
     */
    public static List<Text> getModuleContent(String type, String moduleId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // Get top 5 newest contents
        //String query = "select from " + ModuleDTO.class.getName() + " order by created desc range 0,5";
        Query query =  pm.newQuery("select content from " + ModuleDTO.class.getName());
        query.setFilter("type == '" + type + "'");
        query.setOrdering("order by created desc");
        query.setRange(0, MAX_RECS);
        query.declareParameters("String moduleIdParam");
        query.setFilter("id == moduleIdParam");

        try {
            List<Text> modList = (List<Text>) query.execute(moduleId);

           return modList;
        } finally {
            query.closeAll();
        }
    }

    public static ModuleDTO getTabModuleByKey(Long key) {
        return (ModuleDTO) PMF.getObjectByKey(key, ModuleDTO.class);
    }
    
    /**
     * Get all modules of tab.
     * @param type Kind of module. Ex: Tab, Layout
     * @return
     */
    public static List<ModuleDTO> getModules(String type, String langCd) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        
//        String query = "select from " + ModuleDTO.class.getName() + " order by order";
//        List<ModuleDTO> moduleList = (List<ModuleDTO>) pm.newQuery(query).execute();
        String filters = "type == moduleTypeParam && lang == langParam";
        String ordering = "orderNo asc";
        List<ModuleDTO> modList = (List<ModuleDTO>) PMF.getObjects(ModuleDTO.class, filters, PMF.NO_IMPORT,
                                                                   PMF.NO_PARAM, ordering , new Object[]{type, langCd});

        return modList;
    }

    /**
     * Get all modules.
     * @return
     */
    public static List<ModuleDTO> getModules() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        String ordering = "orderNo asc";
        List<ModuleDTO> modList = (List<ModuleDTO>) PMF.getObjects(ModuleDTO.class, PMF.NO_FILTER, PMF.NO_IMPORT,
                                                                   PMF.NO_PARAM, ordering , PMF.NO_PARAMVALUE);

        return modList;
    }

    /**
     * Get all modules by language code.
     * @param langCd language code
     * @return
     */
    public static List<ModuleDTO> getModulesByLang(String langCd) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ModuleDTO.class);
        String filter = "lang == langCdParam"; 
        String parameter = "String langCdParam";
        
        query.setFilter(filter);
        query.declareParameters(parameter);
        //String query = "select from " + ModuleDTO.class.getName() + " order by created desc";
        
        //PMF.getObject(ModuleDTO.class, filters, parameters, values);

        List<ModuleDTO> modList = (List<ModuleDTO>) query.execute(langCd);

        return modList;
    }

    /**
     * [Give the description for method].
     * @param filterCond
     * @param paramValues
     * @return
     */
    public static List<ModuleDTO> getModules(FilterCond filterCond, Object[] paramValues) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ModuleDTO.class);
        
        if (filterCond != null) {
            if (CommonUtil.isNNandNB(filterCond.getFilters())) {
                for (String filter: filterCond.getFilters()) {
                    query.setFilter(filter);
                }
            }
            if (CommonUtil.isNNandNB(filterCond.getImports())) {
                for (String impo: filterCond.getImports()) {
                    query.declareImports(impo);
                }
            }
            
            if (CommonUtil.isNNandNB(filterCond.getParameters())) {
                for (String param: filterCond.getParameters()) {
                    query.declareParameters(param);
                }
            }
            
            if (CommonUtil.isNNandNB(filterCond.getOrdering())) {
                query.setOrdering(filterCond.getOrdering());
            }
        }

        List<ModuleDTO> modList = (List<ModuleDTO>) query.executeWithArray(paramValues);

        return modList;
    }

    public static boolean deleteContent(Long contentKey) {
        if (contentKey == -1) { // Delete all
            return PMF.deleteAll(ModuleContentDTO.class);
        } else {
            return PMF.delete(contentKey, ModuleContentDTO.class);
        }
    }
    /**
     * @param contentKey
     * @return
     */
    public static boolean delete(Long contentKey) {
        if (contentKey == -1) { // Delete all
            return PMF.deleteAll(ModuleDTO.class);
        } else {
            return PMF.delete(contentKey, ModuleDTO.class);
        }
    }

    public static boolean delete(ModuleDTO module) {
        return PMF.delete(module.getKey(), ModuleDTO.class);
    }

    /**
     * Update information of ModuleDTO.
     * Current support: update orderNo
     * @param updateTabModule
     */
    public static void update(ModuleDTO updateTabModule) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            ModuleDTO module = pm.getObjectById(ModuleDTO.class, updateTabModule.getKey());
            
            if (updateTabModule.getOrderNo() != module.getOrderNo()) {
                module.setOrderNo(updateTabModule.getOrderNo());
            }
            
            module.setManagers(updateTabModule.getManagers());
        } finally {
            pm.close();
        }
    }

    /**
     * [Give the description for method].
     * @param tabKey
     * @return
     */
    public static ModuleDTO getModuleByKey(Long key) {
        return (ModuleDTO) PMF.getObjectByKey(key, ModuleDTO.class);
    }

    /**
     * [Give the description for method].
     * @param moduleId
     * @return
     */
    public static List<ModuleContentDTO> getModuleContents(String moduleId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // Get top 5 newest contents
        //String query = "select from " + ModuleDTO.class.getName() + " order by created desc range 0,5";
        Query query =  pm.newQuery(ModuleContentDTO.class);
        query.setOrdering("created desc");
        query.setRange(0, MAX_RECS);
        query.declareParameters("String moduleIdParam");
        query.setFilter("moduleId == moduleIdParam");

        try {
            List<ModuleContentDTO> modContentList = (List<ModuleContentDTO>) query.execute(moduleId);

            return modContentList;
        } finally {
            query.closeAll();
        }
    }
}
