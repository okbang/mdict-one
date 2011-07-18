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

import java.util.List;

import openones.svnloader.dao.entity.IDir;


/**
 * @author Thach Le
 *
 */
public interface IDirManager {

    public IDir newDirInst();
    public abstract void createDir(IDir dir) throws Exception;

    public abstract IDir findDir(Object svnRepo, String path);

    public abstract IDir findDirRoot(Object svnRepo);

    public abstract IDir findRoot(Object svnRepo);

    /**
     * Update information in a dir
     * @exception Exception
     * @param dir
     */
    public abstract void updateDir(IDir dir) throws Exception;

    /**
     * get List Dir has the same ParentDirID
     *     
     * @param parentDir
     *            :parent of dir You want to find out
     * @return Set of Dir
     */
    public abstract List getDirs(IDir parentDir);

}