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
import java.util.List;

import openones.idict.portlet.form.DictInfo;
import openones.stardictcore.StarDict;

public class DictBiz {
    private String dictRepo;

    public DictBiz(String dictRepo) {
        this.dictRepo = dictRepo;
    }

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

//    public List<String> getDictNames() {
//        List<StarDict> dicts = getDicts();
//        List<String> dictNames = new ArrayList<String>();
//
//        for (StarDict dict : dicts) {
//            dictNames.add(dict.getDictName());
//        }
//
//        return dictNames;
//    }

    public List<DictInfo> getDictInfoList() {
        List<StarDict> dicts = getDicts();
        List<DictInfo> dictInfos = new ArrayList<DictInfo>();

        DictInfo dictInfo;
        int i = 0;
        for (StarDict dict : dicts) {            
            dictInfo = new DictInfo(String.valueOf(i), dict.getDictName());
            dictInfos.add(dictInfo);
            i++;
        }

        return dictInfos;
    }
}
