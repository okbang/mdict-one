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

import openones.svnloader.dao.entity.ISVNFilePK;

/**
 * @author ThachLN
 *
 */
public interface ISVNFilePKManager {

    public abstract void createFile(Object svnFile) throws Exception;

    /**
     * [Give the description for method].
     * @param dirID
     * @param name
     * @param revisionID
     * @return
     */
    public abstract ISVNFilePK newSVNFilePKInst(Integer dirID, String name, long revisionID);

}