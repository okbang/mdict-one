/**
 * Licensed to OpenOnes under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * OpenOnes licenses this file to you under the Apache License,
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
package openones.corewa.validate.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rocky.common.CommonUtil;

/**
 * @author MKhang
 *
 */
public class FormValidation {

    private String id;
    private Map<String, Field> fieldMap = new HashMap<String, Field>();
    
    /**
     * 
     */
    public FormValidation() {
    }
    
    public FormValidation(String id) {
        this.id = id;
    }
    
    /**
     * Get value of id.
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * Set the value for id.
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Get value of fieldMap.
     * @return the fieldMap
     */
    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }
    /**
     * Set the value for fieldMap.
     * @param fieldMap the fieldMap to set
     */
    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;   
    }
   
    public void addField(Field field) {
        fieldMap.put(field.getName(), field);
    }
    
    public Field getField(String name) {
        return fieldMap.get(name);
    }
    
    
    public void add(Field field) {
        fieldMap.put(field.getId(), field);
    }

    /**
     * @param propName
     * @return
     */
    public List<Field> getFieldByName(String propName) {
        List<Field> fieldList = new ArrayList<Field>();
        
        for (Field field : fieldMap.values()) {
            if (field.getName().equalsIgnoreCase(propName)) {
                fieldList.add(field);
            }
        }
        
        return fieldList;
    }
}
