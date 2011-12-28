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
package openones.svnloader.dao.entity;

import java.math.BigInteger;


/**
 * @author ThachLN
 *
 */
public interface IDir {

    /**
     * [Give the description for method].
     * @param svnRepoRoot
     */
    void setSVNRepo(ISVNRepo svnRepoRoot);

    /**
     * [Give the description for method].
     * @param rootname
     */
    void setDirName(String rootname);

    /**
     * [Give the description for method].
     * @param currentRevision
     */
    void setRevision(IRevision currentRevision);

    /**
     * [Give the description for method].
     * @return
     */
    String getParentPath();

    /**
     * [Give the description for method].
     * @return
     */
    Integer getDirID();

    /**
     * [Give the description for method].
     * @param dirID
     */
    void setParentDirID(Integer dirID);

    /**
     * [Give the description for method].
     * @param ordinal
     */
    void setStatus(Integer ordinal);

    /**
     * [Give the description for method].
     * @param valueOf
     */
    void setDeletedRevisionID(BigInteger valueOf);

    /**
     * [Give the description for method].
     * @return
     */
    String getDirName();

    /**
     * [Give the description for method].
     * @param string
     */
    void setParentPath(String string);

    /**
     * [Give the description for method].
     * @param copyPath
     */
    void setCopyFormPath(String copyPath);

    /**
     * [Give the description for method].
     * @param valueOf
     */
    void setCopyRevision(BigInteger valueOf);

    /**
     * [Give the description for method].
     * @param dirId
     */
    void setDirID(Integer dirId);

}
