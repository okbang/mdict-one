/*
 * Screen.java 0.1 June 30, 2010
 * 
 * Copyright (c) 2010, Open-Ones Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package openones.corewa.config;

import java.util.HashMap;
import java.util.Map;

/**
 * This class presents the bean of tag screen in configuration file. 
 * @author Thach Le
 *
 */
public class Screen {

    private String id;
    private String ctrlClass;
    private String inputPage;
    private String formId;
    private Map<String, Event> events = new HashMap<String, Event>();

    public Screen() {
    }
    
    public Screen(String screenId, String controlClass, String inputPage, String formId) {
        this.id = screenId;
        this.ctrlClass = controlClass;
        this.inputPage = inputPage;
        this.formId = formId;
    }

    public String getCtrlClass() {
        return ctrlClass;
    }

    public void setCtrlClass(String ctrlClass) {
        this.ctrlClass = ctrlClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public void addEvent(Event evt) {
        events.put(evt.getId(), evt);
    }

    public Event getEvent(String id) {
        return events.get(id);
    }

    public String getInputPage() {
        return inputPage;
    }

    public void setInputPage(String inputPage) {
        this.inputPage = inputPage;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
