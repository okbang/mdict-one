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

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import openones.gaecore.PMF;
import openones.gate.store.dto.IntroDTO;
import rocky.common.CommonUtil;
import rocky.common.Constant;

/**
 * @author ThachLN
 *
 */
public class IntroStore {
    final static Logger LOG = Logger.getLogger("IntroStore");
    
    public static boolean save(IntroDTO intro) {
        LOG.info("intro.getContent()=" + intro.getContent());
        return PMF.save(intro);
    }

    /**
     * [Give the description for method].
     * @return
     */
    public static String getLastContent() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        //String query = "select from " + IntroDTO.class.getName();
        Query query = pm.newQuery(IntroDTO.class);

        query.setOrdering("modified descending");

        List<IntroDTO> introList = (List<IntroDTO>) query.execute();

        if (CommonUtil.isNNandNB(introList)) {
            System.out.println("content=" + introList.get(0).getContent());
        }
        return (CommonUtil.isNNandNB(introList) ? introList.get(0).getStringContent() : Constant.BLANK);
    }

    /**
     * @return
     */
    public static List<IntroDTO> getContents() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        String query = "select from " + IntroDTO.class.getName();
        
        List<IntroDTO> introList = (List<IntroDTO>) pm.newQuery(query).execute();
        
        return introList;
    }

    /**
     * @param contentKey
     * @return
     */
    public static boolean delete(Long contentKey) {
        if (contentKey == -1) { //Delete all
            return PMF.deleteAll(IntroDTO.class);
        } else {
            return PMF.delete(contentKey, IntroDTO.class);
        }
    }
}
