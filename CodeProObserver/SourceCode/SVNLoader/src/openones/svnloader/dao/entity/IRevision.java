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
import java.util.Date;

/**
 * @author Thach Le
 *
 */
public interface IRevision {

    /**
     * [Give the description for method].
     * @return
     */
    long getRevisionNum();

    /**
     * [Give the description for method].
     * @param author
     */
    void setAuthor(String author);

    /**
     * [Give the description for method].
     * @param message
     */
    void setComment(String message);

    /**
     * [Give the description for method].
     * @param revisionNum
     */
    void setRevisionNum(long revisionNum);

    /**
     * [Give the description for method].
     * @param date
     */
    void setDateLog(Date date);

    /**
     * [Give the description for method].
     * @param svnRepoRoot
     */
    void setSVNRepo(ISVNRepo svnRepoRoot);

    /**
     * [Give the description for method].
     * @return
     */
    Long getRevisionID();

}
