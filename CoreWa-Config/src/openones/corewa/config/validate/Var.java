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
package openones.corewa.config.validate;

/**
 * @author MKhang
 *
 */
public class Var {
    
    private String name;
    private String value;
    
    /**
     * 
     */
    public Var() {
        // TODO Auto-generated constructor stub
    }
    
    public Var(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    /**
     * Get value of name.
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * Set the value for name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get value of value.
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * Set the value for value.
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }   

}
