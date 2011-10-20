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
package openones.gaecore;

import static org.junit.Assert.*;

import java.util.Date;

import openones.gaecore.dto.StudentDTO;

import org.junit.Test;

/**
 * @author ThachLN
 *
 */
public class PMFTest {

    /**
     * Test method for {@link openones.gaecore.PMF#save(java.lang.Object)}.
     */
    @Test
    public void testSave() {
        StudentDTO student = new StudentDTO();
        
        student.setId("0001");
        student.setFirstName("Long");
        student.setBirthDay(new Date());
        student.setToelfMark(500);
        PMF.save(student);
    }

    /**
     * Test method for {@link openones.gaecore.PMF#getObjectByKey(java.lang.Long, java.lang.Class)}.
     */
    @Test
    public void testGetObjectByKey() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link openones.gaecore.PMF#getObject(java.lang.Class, java.lang.String[], java.lang.String[], java.lang.Object[])}.
     */
    @Test
    public void testGetObject() {
        fail("Not yet implemented");
    }

}
