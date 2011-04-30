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

import java.util.HashMap;
import java.util.Map;

/**
 * @author MKhang, Thach Le
 *
 */
public class ValidationConf {
    
    private Map<String, Var> varMap = new HashMap<String, Var>();
    private FormValidation formValidation;

    /**
     * 
     */
    public ValidationConf() {
        // TODO Auto-generated constructor stub
    }
        
    /**
     * Get value of varMap.
     * @return the varMap
     */
    public Map<String, Var> getVarMap() {
        return varMap;
    }
    /**
     * Set the value for varMap.
     * @param varMap the varMap to set
     */
    public void setVarMap(Map<String, Var> varMap) {
        this.varMap = varMap;
    }

    
    public void addVar(Var var) {
        varMap.put(var.getName(), var);
    }
    
    public Var getVar(String name) {
        return varMap.get(name);
    }

    
    public FormValidation getFormValidation() {
        return formValidation;
    }

    public void setFormValidation(FormValidation formValidation) {
        this.formValidation = formValidation;
    }

}
