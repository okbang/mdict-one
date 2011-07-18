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
 * @author Thach Le
 *
 */
public interface ISVNVersion {

    /**
     * [Give the description for method].
     * @param parentDir
     */
    void setDir(IDir parentDir);

    /**
     * [Give the description for method].
     * @param added
     */
    void setSVNAction(char added);

    /**
     * [Give the description for method].
     * @param revision
     */
    void setRevision(IRevision revision);

    /**
     * [Give the description for method].
     * @param valueOf
     */
    void setSize(BigInteger valueOf);

    /**
     * [Give the description for method].
     * @param valueOf
     */
    void setNMComment(BigInteger valueOf);

    /**
     * [Give the description for method].
     * @param string
     */
    void setUnit(String string);

    /**
     * [Give the description for method].
     * @param copyPath
     */
    void setCopyFromPath(String copyPath);

    /**
     * [Give the description for method].
     * @param valueOf
     */
    void setCopyRevision(BigInteger valueOf);

    /**
     * [Give the description for method].
     * @param valueOf
     */
    void setNmStaticBug(BigInteger valueOf);

}
