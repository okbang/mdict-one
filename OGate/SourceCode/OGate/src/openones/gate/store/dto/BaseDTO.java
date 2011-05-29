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
package openones.gate.store.dto;

import java.util.Date;

/**
 * @author Thach Le
 *
 */
public abstract class BaseDTO {

    abstract public Date getCreated();

    abstract public void setCreated(Date created);

    abstract public String getCreatedBy();

    abstract public void setCreatedBy(String createdBy);

    abstract public Date getLastModified();

    abstract public void setLastModified(Date lastModified);

    abstract public String getLastModifiedBy();

    abstract public void setLastModifiedBy(String lastModifiedBy);
}
