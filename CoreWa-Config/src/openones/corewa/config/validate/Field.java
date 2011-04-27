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
public class Field {

    private String name;
    private String type;
    private String require;
    private String pattern;
    private String error;
    private String min;
    private String max;
    
    /**
     * 
     */
    public Field() {
        // TODO Auto-generated constructor stub
    }
    
    public Field(String name, String type, String require, String pattern, 
            String error, String min, String max) {
        this.name = name;
        this.type = type;
        this.require = require;
        this.pattern = pattern;
        this.error = error;
        this.min = min;
        this.max = max;
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
     * Get value of type.
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * Set the value for type.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Get value of require.
     * @return the require
     */
    public String getRequire() {
        return require;
    }
    /**
     * Set the value for require.
     * @param require the require to set
     */
    public void setRequire(String require) {
        this.require = require;
    }
    /**
     * Get value of pattern.
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }
    /**
     * Set the value for pattern.
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    /**
     * Get value of error.
     * @return the error
     */
    public String getError() {
        return error;
    }
    /**
     * Set the value for error.
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
    /**
     * Get value of min.
     * @return the min
     */
    public String getMin() {
        return min;
    }
    /**
     * Set the value for min.
     * @param min the min to set
     */
    public void setMin(String min) {
        this.min = min;
    }
    /**
     * Get value of max.
     * @return the max
     */
    public String getMax() {
        return max;
    }
    /**
     * Set the value for max.
     * @param max the max to set
     */
    public void setMax(String max) {
        this.max = max;
    }
    
    
}
