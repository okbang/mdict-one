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

/**
 * @author ThachLN
 */
public class FilterCond {
    private Class clazz;
    private String langCd;
    private String[] filters;
    private String[] imports;
    private String[] parameters;
    private String ordering;

    public FilterCond(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * @param clazz
     * @param langCd
     */
    public FilterCond(Class clazz, String langCd) {
        super();
        this.clazz = clazz;
        this.langCd = langCd;
    }

    public Class getClazz() {
        return clazz;
    }
    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
    public String getLangCd() {
        return langCd;
    }
    public void setLangCd(String langCd) {
        this.langCd = langCd;
    }
    public String[] getFilters() {
        return filters;
    }
    public void setFilters(String[] filters) {
        this.filters = filters;
    }
    public String[] getImports() {
        return imports;
    }

    public void setImports(String[] imports) {
        this.imports = imports;
    }

    public String[] getParameters() {
        return parameters;
    }
    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
    public String getOrdering() {
        return ordering;
    }
    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }
}
