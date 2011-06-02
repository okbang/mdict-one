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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import openones.gaecore.PMF;
import openones.gate.biz.SessionBiz;
import openones.gate.store.dto.ModuleDTO;
import rocky.common.CommonUtil;

import com.google.appengine.api.datastore.Text;

/**
 * @author ThachLN
 */
public class ModuleStore {
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
     * [Give the description for method].
     * 
     * @return
     */
    public static ModuleDTO getLastModuleContent(String moduleId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        // Get top 5 newest contents
        String query = "select from " + ModuleDTO.class.getName() + " order by created desc range 0,5";

        // Query query = pm.newQuery(ModuleDTO.class);
        // query.setOrdering("created descending");
        // List<ModuleDTO> introList = (List<ModuleDTO>) query.execute();

        List<ModuleDTO> modList = (List<ModuleDTO>) pm.newQuery(query).execute();

//        if (CommonUtil.isNNandNB(modList)) {
//            //System.out.println("content=" + modList.get(0).getContent());
//        }
        return (CommonUtil.isNNandNB(modList) ? modList.get(0) : null);
        //return (CommonUtil.isNNandNB(modList) ? modList.get(0).getStringContent() : Constant.BLANK);
    }

    /**
     * Get all modules of tab.
     * @return
     */
    public static List<ModuleDTO> getTabModules() {
        List<ModuleDTO> moduleList = new ArrayList<ModuleDTO>();
        
        ModuleDTO intro = new ModuleDTO("intro", "Intro", new Text("This is the content of Introduction tab"));
        intro.setOrder(1);
        
        ModuleDTO product = new ModuleDTO("product", "Product", new Text("This is the content of Product tab"));
        product.setOrder(2);
        
        ModuleDTO service = new ModuleDTO("service", "Service", new Text("This is the content of service tab"));
        service.setOrder(3);
        
        ModuleDTO member = new ModuleDTO("member", "Member", new Text("This is the content of member tab"));
        member.setOrder(4);
        
        ModuleDTO forum = new ModuleDTO("forum", "Forum", new Text("This is the content of forum tab"));
        forum.setOrder(5);
        
        ModuleDTO career = new ModuleDTO("career", "Career", new Text("This is the content of career tab"));
        career.setOrder(6);
        
        ModuleDTO activity = new ModuleDTO("activity", "Activity", new Text("This is the content of activity tab"));
        activity.setOrder(7);
        
        ModuleDTO project = new ModuleDTO("project", "Project", new Text("This is the content of project tab"));
        project.setOrder(8);
        
        moduleList.add(intro);
        moduleList.add(product);
        moduleList.add(service);
        moduleList.add(member);
        moduleList.add(forum);
        moduleList.add(career);
        moduleList.add(activity);
        moduleList.add(project);
        
        return moduleList;
    }
    
    /**
     * Get all modules.
     * @return
     */
    public static List<ModuleDTO> getModules() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        String query = "select from " + ModuleDTO.class.getName() + " order by created desc";

        List<ModuleDTO> modList = (List<ModuleDTO>) pm.newQuery(query).execute();

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


}
