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

import openones.gaecore.PMF;
import openones.gate.store.dto.ModuleDTO;
import rocky.common.CommonUtil;
import rocky.common.Constant;

/**
 * @author ThachLN
 *
 */
public class IntroStore {
    final static Logger LOG = Logger.getLogger("IntroStore");
    final static String MOD_ID = "intro";
    
    public static boolean save(ModuleDTO intro) {
        LOG.info("intro.getContent()=" + intro.getContent());
        if (intro.getCreated() == null) {
            intro.setCreated(new Date()); // set current Date
        }
        
        if (!CommonUtil.isNNandNB(intro.getCreatedBy())) {
            intro.setCreatedBy(MOD_ID);
        }
        return PMF.save(intro);
    }

    /**
     * [Give the description for method].
     * @return
     */
    public static String getLastContent() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        
        // Get top 5 newest contents
        String query = "select from " + ModuleDTO.class.getName() + " order by created desc range 0,5";
        
        //Query query = pm.newQuery(ModuleDTO.class);
        //query.setOrdering("created descending");
        //List<ModuleDTO> introList = (List<ModuleDTO>) query.execute();
        
        List<ModuleDTO> introList = (List<ModuleDTO>) pm.newQuery(query).execute();
        

        if (CommonUtil.isNNandNB(introList)) {
            System.out.println("content=" + introList.get(0).getContent());
        }
        return (CommonUtil.isNNandNB(introList) ? introList.get(0).getStringContent() : Constant.BLANK);
    }

    /**
     * @return
     */
    public static List<ModuleDTO> getContents() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        String query = "select from " + ModuleDTO.class.getName() + " order by created desc";
        
        List<ModuleDTO> introList = (List<ModuleDTO>) pm.newQuery(query).execute();
        
        return introList;
    }

    /**
     * @param contentKey
     * @return
     */
    public static boolean delete(Long contentKey) {
        if (contentKey == -1) { //Delete all
            return PMF.deleteAll(ModuleDTO.class);
        } else {
            return PMF.delete(contentKey, ModuleDTO.class);
        }
    }
}
