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
package openones.svnloader.daoimpl;

import openones.svnloader.dao.ISVNFilePKManager;
import openones.svnloader.dao.entity.ISVNFilePK;

/**
 * @author ThachLN
 *
 */
public class SVNFilePKManager implements ISVNFilePKManager {

    /**
     * [Explain the description for this method here].
     * @param svnFile
     * @throws Exception
     * @see openones.svnloader.dao.ISVNFilePKManager#createFile(java.lang.Object)
     */
    @Override
    public void createFile(Object svnFile) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * [Explain the description for this method here].
     * @param dirID
     * @param name
     * @param revisionID
     * @return
     * @see openones.svnloader.dao.ISVNFilePKManager#newSVNFilePKInst(java.lang.Integer, java.lang.String, long)
     */
    @Override
    public ISVNFilePK newSVNFilePKInst(Integer dirID, String name, long revisionID) {
        // TODO Auto-generated method stub
        return null;
    }

}
