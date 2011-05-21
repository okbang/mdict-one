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

import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Text;

/**
 * @author ThachLN
 *
 */
@PersistenceCapable
public class IntroDTO extends ModuleDTO {

    /**
     * @param introContent
     */
    public IntroDTO(Text introContent) {
        super(introContent);
    }


}
