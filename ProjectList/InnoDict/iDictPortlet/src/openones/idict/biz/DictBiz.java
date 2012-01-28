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

import org.apache.log4j.Logger;

import openones.idict.portlet.form.DictInfo;
import openones.stardictcore.StarDict;

/**
 * Dictionary Business Layer.
 * @author Thach Le
 */
public class DictBiz {
    /** Logger. */
    private static Logger log = Logger.getLogger(DictBiz.class);

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

        if (dictF != null) {
            for (File subFile : dictF.listFiles()) {
                if (subFile.isDirectory()) {
                    dictFolders.add(subFile.getName());
                }
            }
        } else {
            log.warn("No dictionary found at '" + dictRepo + "'");
        }

        return dictFolders;
    }

    /**
     * Get instances of StarDicts.
     * @param dictNames filter dictionaries. Null mean no filter
     * @return list of instances of StarDicts
     */
    public Collection<StarDict> getDicts(List<String> dictNames) {
        List<String> dictFolders = getDictFolders();
        Map<String, StarDict> dictMap = new TreeMap<String, StarDict>();

        StarDict dict;
        for (String dictFolderName : dictFolders) {
            dict = StarDict.loadDict(dictRepo + File.separator + dictFolderName);
            if (dict != null) {
                if ((dictNames == null) || (dictNames.contains(dict.getDictName()))) {
                    dictMap.put(dict.getDictName(), dict);
                }
            }
        }

        return dictMap.values();
    }

    /**
     * Get all dictionaries from the repository.
     * @return list of instances of StarDicts
     */
    public Collection<StarDict> getDicts() {
        return getDicts(null);
    }

    /**
     * Get list of dictionary information. Dictionaries are shorted by name with increasing order.
     * @return list of dictionary information
     */
    public Collection<DictInfo> getDictInfoList() {
        Collection<StarDict> dicts = getDicts();
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

    /**
     * Get meaning of the work in all dictionaries.
     * @param word word to be searched.
     * @return list of DictInfo contain meanings of word.
     */
    public Collection<DictInfo> getMeaning(String word) {
        Collection<StarDict> dicts = getDicts();
        // Use TreeMap to add the item with increasing order by key (name of dict)
        Map<String, DictInfo> dictMap = new TreeMap<String, DictInfo>();

        DictInfo dictInfo;
        int i = 0;
        for (StarDict dict : dicts) {
            dictInfo = new DictInfo(String.valueOf(i), dict.getDictName());
            dictInfo.setMeaning(dict.lookupWord(word));
            dictMap.put(dict.getDictName(), dictInfo);
            i++;
        }

        return dictMap.values();
    }

    /**
     * Get meanings of a word in dictionaries.
     * @param word word to be lookup
     * @param dictNames Names of dictionaries (preserved)
     * @return list of meaning, dictionary name, dictionary code in DictInfo
     */
    public Collection<DictInfo> getMeaningByDict(String word, List<String> dictNames) {
        Collection<StarDict> dicts = getDicts(dictNames);
        // Use TreeMap to add the item with increasing order by key (name of dict)
        Map<String, DictInfo> dictMap = new TreeMap<String, DictInfo>();

        DictInfo dictInfo;
        int i = 0; // use for code of dictionary
        String meaning;
        for (StarDict dict : dicts) {
            dictInfo = new DictInfo(String.valueOf(i), dict.getDictName());
            meaning = dict.lookupWord(word);
            // replace newline character in Java by break line tag in HTML
            meaning = meaning.replaceAll("\n", "<br/>");
            dictInfo.setMeaning(meaning);
            dictMap.put(dict.getDictName(), dictInfo);
            i++;
        }

        return dictMap.values();
    }
}
