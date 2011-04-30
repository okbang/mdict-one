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


/**
 * @author MKhang
 *
 */
public class Field {

    private String id;

    private String name;
    private CheckType checkType;
    
    /** pattern is applied for checkType = pattern | datefmt | decfmt. */
    private String pattern;
    
    /** min | max are applied for checkType = length. */
    private Double min = null;
    private Double max = null;
    
    private String error;
    
    /**
     * 
     */
    public Field() {
    }
    
    /**
     * @param id
     * @param name
     * @param checkType
     * @param error
     */
    public Field(String id, String name, String checkType, String error) {
        super();
        this.id = id;
        this.name = name;
        this.checkType = CheckType.valueOf(checkType);
        this.error = error;
    }
    /**
     * @param id
     * @param name
     * @param checkType
     * @param error
     */
    public Field(String id, String name, CheckType checkType, String error) {
        super();
        this.id = id;
        this.name = name;
        this.checkType = checkType;
        this.error = error;
    }

    /**
     * @param id
     * @param name
     * @param checkType
     * @param min
     * @param max
     * @param error
     */
    public Field(String id, String name, CheckType checkType, Double min, Double max, String error) {
        super();
        this.id = id;
        this.name = name;
        this.checkType = checkType;
        this.min = min;
        this.max = max;
        this.error = error;
    }
    /**
     * @param id
     * @param name
     * @param checkType
     * @param pattern
     * @param error
     */
    public Field(String id, String name, CheckType checkType, String pattern, String error) {
        super();
        this.id = id;
        this.name = name;
        this.checkType = checkType;
        this.pattern = pattern;
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getError() {
        return error;
    }

    public void setError(String errMsg) {
        this.error = errMsg;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }
    
    public void setCheckType(String checkType) {
        this.checkType = CheckType.valueOf(checkType);
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
    
}
