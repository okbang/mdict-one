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
package openones.corewa;

/**
 * @author MKhang
 *
 */
public class ErrorMessage {
    private String fieldName;
    private String message;
    /**
     * Get value of fieldName.
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }
    /**
     * Set the value for fieldName.
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    /**
     * Get value of message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Set the value for message.
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
}
