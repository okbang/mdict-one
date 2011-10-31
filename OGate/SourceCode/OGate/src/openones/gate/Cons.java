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
package openones.gate;

/**
 * @author Thach Le
 */
public final class Cons {
    final static public String SK_USER = "user";
    public static final String SK_NMLOGON_USER = "nmLogonUser";
    public static final String SK_NEXTPAGE = "nextPage";
    public static final String SK_LANG = "lang";

    public static enum ActResult {
        OK, FAIL
    };

    public static enum Screens {
        TabSetting, AccSetting, LangSetting
    }

    public static final String TAB_MANAGER_SEPARATOR = ":";
    public static final Object EMAIL_MANAGER_SEPARATOR = ";";
    public static final String UNDER_SCORE = "_";
    public static final String DEF_LANGCD = "vn";
    public static final String HYPHEN = "-";
    
    public static enum ModuleType {
        Tab, Layout
    }
}
