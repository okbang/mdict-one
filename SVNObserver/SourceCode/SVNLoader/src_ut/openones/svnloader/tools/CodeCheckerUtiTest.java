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
package openones.svnloader.tools;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import openones.svnloader.tools.CodeCheckerUtil;

import org.junit.Test;

import rocky.sizecounter.ISizeCounter;
import rocky.sizecounter.SizeCounterFactory;
import rocky.sizecounter.SizeCounterImpl;
import rocky.sizecounter.SizeMetaData;
import rocky.sizecounter.UnsupportedFileType;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

/**
 * @author ThachLN
 *
 */
public class CodeCheckerUtiTest {
    CodeCheckerUtil codeChecker = new CodeCheckerUtil();
    @Test
    public void testCodeChecker0001() {
        //String xmlFilePath = "D://Fsoft//4PSuite//CodeChecker//OpenOnes Checks_v1.0.xml";
        String sourcePath = "./s/src/openones/svnloader/ConsoleSVN2RDB.java";
        String xmlFilePath = "./src/Open-Ones Checks_v1.0.xml";
        
        //assertEquals(1, codeChecker.check(sourcePath).size());
        List<AuditEvent> errorList = codeChecker.check(sourcePath).get(sourcePath);
        assertEquals(16, errorList.size());
    }

    @Test
    public void testCodeCheckerSaveDB() {
        String sourcePath = "H:/FSoft/OOG/4PSuite/trunk/SourceCode/SVN2RDB/src/svn2rdb/ConsoleSVN2RDB.java";
        String xmlFilePath = "H:/FSoft/OOG/4PSuite/trunk/SourceCode/SVN2RDB/src/OpenOnes Checks_v1.0.xml";
        List<AuditEvent> errorList = codeChecker.check(sourcePath).get(sourcePath);
        assertNotNull(errorList);
        assertEquals(12, errorList.size());
    }
    
    @Test
    public void testCodeChecker0002() {
        String sourcePath = "E:\\ReviewSVN\\SourceCode\\SizeCounter\\src\\csdl\\locc\\measures\\java\\javaline\\LoccUtil.java";
        //String xmlFilePath = "E:/4PSuite/SourceCode/SVN2RDB/src/OpenOnes Checks_v1.0.xml";
        Map<String, List<AuditEvent>> errorMap = codeChecker.check(sourcePath);
//        assertNotNull(errorMap);
        for (String key : errorMap.keySet()) {
            System.out.println("key=" + key);
            Assert.assertEquals(sourcePath, key);
        }
        
        List<AuditEvent> errorList = codeChecker.check(sourcePath).get(sourcePath);
        assertNotNull(errorList);
        assertEquals(12, errorList.size());
    }
    
    @Test
    public void testCountSize() throws UnsupportedFileType{
        String sourcePath = "E:\\ReviewSVN\\SourceCode\\SizeCounter\\src\\csdl\\locc\\measures\\java\\javaline\\LoccUtil.java";
        ISizeCounter sc = SizeCounterFactory.getDefaultInstance();
        SizeMetaData smd = sc.countSize(sourcePath);
        System.out.println(sc.toString());
        System.out.println(smd.getSize());
    }
}
