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
package openones.gate.store;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

/**
 * @author ThachLN
 *
 */
@PersistenceCapable
public class IntroDTO extends Object implements Serializable {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
    
    @Persistent
    private Date modified;

    /** Email address. */
    @Persistent
    private Text content;
    
    
    /**
     * @param object
     */
    public IntroDTO(String content) {
        this.content = new Text(content);
        this.modified = new Date();
    }

    /**
     * @param introContent
     */
    public IntroDTO(Text introContent) {
        this.content = introContent;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modifed) {
        this.modified = modifed;
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String content) {
        this.content = new Text(content);
    }
    
    public void setContent(Text content) {
        this.content = content;
    }
}
