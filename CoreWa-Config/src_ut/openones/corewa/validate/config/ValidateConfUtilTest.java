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
package openones.corewa.validate.config;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author ThachLN
 *
 */
public class ValidateConfUtilTest extends TestCase {

    /**
     * Test method for {@link openones.corewa.config.validate.ValidateConfUtil#parse(java.lang.String)}.
     */
    @Test
    public void testParseValidation() {
        ValidationConf validator = ValidateConfUtil.parse("/validate/AddForm.xml");
        
        assertEquals(1, validator.getVarMap().size());
        
        FormValidation form = validator.getFormValidation();
        assertEquals("AddFormId", form.getId());
        
        assertEquals(1, validator.getVarMap().size());
        
        Field field01 = form.getField("01");
        assertEquals("name", field01.getName());
        assertEquals(CheckType.mandatory, field01.getCheckType());
        assertEquals("${error.require}", field01.getError());
        
        Field field = form.getField("02");
        assertEquals("phone", field.getName());
        assertEquals(CheckType.pattern, field.getCheckType());
        
        field = form.getField("03");
        assertEquals("birthday", field.getName());
        assertEquals(CheckType.datefmt, field.getCheckType());
        
        field = form.getField("04");
        assertEquals("address", field.getName());
        assertEquals(CheckType.length, field.getCheckType());
        assertEquals(300.0, field.getMax());
        assertEquals(0.0, field.getMin());
    }

}

