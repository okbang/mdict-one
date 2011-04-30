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
package openones.corewa.validate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import openones.corewa.validate.config.CheckType;
import openones.corewa.validate.config.ErrorField;
import openones.corewa.validate.config.Field;
import openones.corewa.validate.config.Var;

import org.junit.Test;

/**
 * @author ThachLN
 *
 */
public class CheckUtilTest extends TestCase {
    /**
     * Test method for {@link openones.corewa.CheckUtil#checkInput(openones.corewa.config.validate.Field, java.lang.Object, java.util.Map)}.
     */
    @Test
    public void testCheckInput01() {
        Field field = new Field("01", "firstName", "mandatory", "Field '{?}' is mandatory");
        Object firstName = "Văn";
        Map<String, Object> valueMap = null;
        
        ErrorField errorField = CheckUtil.checkInput(field, firstName, valueMap);
        
        assertNull(errorField);
    }

    @Test
    public void testCheckInput02() {
        Field field = new Field("01", "firstName", "mandatory", "Field '{?}' is mandatory");
        Object firstName = "";
        Map<String, Object> valueMap = null;
        
        ErrorField errorField = CheckUtil.checkInput(field, firstName, valueMap);
        
        assertNotNull(errorField);
        
        assertEquals("01", errorField.getId());
        assertEquals("firstName", errorField.getFieldName());
        assertEquals("Field '{?}' is mandatory", errorField.getErrorMessage());
        
    }
    
    @Test
    public void testCheckInput03() {
        Field field = new Field("01", "birthDay", CheckType.datefmt, "dd/MM/yyyy", "Format of field '{1}' is invalid.");
        String birthDay = "abc";
        Map<String, Object> valueMap = null;
        
        ErrorField errorField = CheckUtil.checkInput(field, birthDay, valueMap);
        
        assertNotNull(errorField);
        
        assertEquals("01", errorField.getId());
        assertEquals("birthDay", errorField.getFieldName());
        assertEquals("Format of field '{1}' is invalid.", errorField.getErrorMessage());
        
    }
    @Test
    public void testCheckInput04() {
        Field field = new Field("01", "birthDay", CheckType.datefmt, "dd/MM/yyyy", "Format of field '{1}' is invalid.");
        String birthDay = "abc";
        Map<String, Object> valueMap = null;
        
        ErrorField errorField = CheckUtil.checkInput(field, birthDay, valueMap);
        
        assertNotNull(errorField);
        
        assertEquals("01", errorField.getId());
        assertEquals("birthDay", errorField.getFieldName());
        assertEquals("Format of field '{1}' is invalid.", errorField.getErrorMessage());
        
    }
    
    @Test
    public void testCheckInput05() {
        Field field = new Field("01", "birthDay", CheckType.datefmt, "dd/MM/yyyy", "Format of field '{1}' is invalid.");
        String birthDay = "32/1/1990";
        Map<String, Object> valueMap = null;
        
        ErrorField errorField = CheckUtil.checkInput(field, birthDay, valueMap);
        
        assertNull(errorField);
    }
    
    @Test
    public void testCheckInputPhone06() {
        String phonePattern = "[0-9 ]+";
        Field field = new Field("01", "phone", CheckType.pattern, phonePattern, "Format of field '{1}' is invalid.");
        String phone = "09121234";
        Map<String, Object> valueMap = null;
        
        ErrorField errorField = CheckUtil.checkInput(field, phone, valueMap);
        
        assertNull(errorField);
        
        phone = "091 212 345";
        errorField = CheckUtil.checkInput(field, phone, valueMap);
        assertNull(errorField);
        
        phone = "O91212345";
        errorField = CheckUtil.checkInput(field, phone, valueMap);
        assertNotNull(errorField);
        assertEquals("phone", errorField.getFieldName());
        assertEquals("Format of field '{1}' is invalid.", errorField.getErrorMessage());
    }
    
    @Test
    public void testCheckInputLength01() {
        String address = "Tôi yêu tiếng nước tôi!";
        Field field = new Field("01", "address", CheckType.length, "Lengh of field is not above ${1}");
        field.setMax(10);
        
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("1", 10);
        
        ErrorField errorField = CheckUtil.checkInput(field, address, valueMap);
        
        assertNotNull(errorField);
        
        assertEquals("address", errorField.getFieldName());
        assertEquals("Lengh of field is not above 10", errorField.getErrorMessage());
    }
    
    @Test
    public void testCheckInputLength02() {
        String address = "Tôi yêu tiếng nước tôi!";
        Field field = new Field("01", "address", CheckType.length, "Lengh of field is not above ${1}");
        field.setMax(100);
        
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("1", 100);
        
        ErrorField errorField = CheckUtil.checkInput(field, address, valueMap);
        
        assertNull(errorField);
    }
    
    /**
     * Test method for {@link openones.corewa.CheckUtil#evaluateVar(java.util.Map, java.util.Map)}.
     */
    @Test
    public void testEvaluateVar0001() {
        Map<String, Var> varMap = new HashMap<String, Var>();
        
        Date dteToday = new Date();
        Var todayVar = new Var("today", dteToday);
        varMap.put("today", todayVar);
        CheckUtil.evaluateVar(varMap, null);
        
        assertEquals(dteToday, varMap.get("today").getValue());
        
    }

    @Test
    public void testEvaluateVar0002() {
        Map<String, Object> reqMap = new HashMap<String, Object>();
        Map<String, Var> varMap = new HashMap<String, Var>();
        
        Date dteToday = new Date();
        Var todayVar = new Var("today", "Today is ${todayVar}");
        varMap.put("today", todayVar);
        
        reqMap.put("todayVar", dteToday);
        
        CheckUtil.evaluateVar(varMap, reqMap);
        
        assertEquals("Today is " + dteToday, varMap.get("today").getValue());
        
    }


}
