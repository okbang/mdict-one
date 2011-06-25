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
package openones.corewa;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author ThachLN
 *
 */
public class ReqUtilTest extends TestCase {

    /**
     * Test method for {@link openones.corewa.ReqUtil#getData(java.util.Map, java.lang.Class, openones.corewa.validate.config.ValidationConf)}.
     */
    @Test
    public void testGetData0001() {
        Map<String, Object> reqMap = new HashMap<String, Object>();
        Object form = ReqUtil.getData(reqMap, BaseInForm.class, null);
        
        assertNotNull(form);
    }

}
