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
package openones.idict.biz;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import openones.idict.portlet.form.DictInfo;
import openones.stardictcore.StarDict;

/**
 * Dictionary Business Layer.
 * @author Thach Le
 */
public class DictBiz {
    /** Path of dictionary repository. */
    private String dictRepo;

    /**
     * Create instance of DictBiz with given path of dictionary repository.
     * @param dictRepo path of dictionary repository
     */
    public DictBiz(String dictRepo) {
        this.dictRepo = dictRepo;
    }

    /**
     * Get folders contain dictionaries of StarDict.
     * @return List of folder which contain dictionaries of StarDict
     */
    public List<String> getDictFolders() {
        File dictF = new File(dictRepo);
        List<String> dictFolders = new ArrayList<String>();

        for (File subFile : dictF.listFiles()) {
            if (subFile.isDirectory()) {
                dictFolders.add(subFile.getName());
            }
        }

        return dictFolders;
    }

    /**
     * Get instances of StarDicts.
     * @return list of instances of StarDicts
     */
    public List<StarDict> getDicts() {
        List<String> dictFolders = getDictFolders();
        List<StarDict> dicts = new ArrayList<StarDict>();

        StarDict dict;
        for (String dictFolderName : dictFolders) {
            dict = StarDict.loadDict(dictRepo + File.separator + dictFolderName);
            if (dict != null) {
                dicts.add(dict);
            }
        }

        return dicts;
    }

    /**
     * Get list of dictionary information. Dictionaries are shorted by name with increasing order.
     * @return list of dictionary information
     */
    public Collection<DictInfo> getDictInfoList() {
        List<StarDict> dicts = getDicts();
        // Use TreeMap to add the item with increasing order by key (name of dict)
        Map<String, DictInfo> dictMap = new TreeMap<String, DictInfo>();

        DictInfo dictInfo;
        int i = 0;
        for (StarDict dict : dicts) {
            dictInfo = new DictInfo(String.valueOf(i), dict.getDictName());
            dictMap.put(dict.getDictName(), dictInfo);
            i++;
        }

        return dictMap.values();
    }
}
