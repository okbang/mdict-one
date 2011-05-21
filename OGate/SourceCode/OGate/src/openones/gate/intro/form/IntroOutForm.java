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
package openones.gate.intro.form;

import java.io.Serializable;

import openones.gate.Cons;
import rocky.common.Constant;

/**
 * @author ThachLN
 *
 */
public class IntroOutForm implements Serializable {
    private Cons.ActResult saveResult;
    
    /** IntroSaveFail | IntroSaveOk. */
    private String key;
    
    private String content = Constant.BLANK;
    
    private String created = Constant.BLANK;
    
    public Cons.ActResult getSaveResult() {
        return saveResult;
    }
    public void setSaveResult(Cons.ActResult saveResult) {
        this.saveResult = saveResult;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String msgKey) {
        this.key = msgKey;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
}
