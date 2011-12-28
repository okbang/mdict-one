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
package rocky.sizecounter;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import rocky.common.CommonUtil;


/**
 * @author linh
 *
 */
public class SizeCounterImplTest {
    @Test
    public void testIsWord() {
        String path = "E:\\Test0.3\\Document\\SCT2UTC\\ADD\\4PSuite_SCT2UTC_ADD.doc";
        SizeCounterImpl impl = new SizeCounterImpl();
        SizeMetaData smd = new SizeMetaData();
        smd.setSize(impl.countSize(path).getSize());
        System.out.println(smd.getSize());
    }
    
    @Test
    public void testIsSource() {
        String path = "E:\\Test0.3\\SourceCode\\DBExporter\\rdb2excel\\rocky\\export\\Rdb2Excel.java";
        SizeCounterImpl impl = new SizeCounterImpl();
        SizeMetaData smd = new SizeMetaData();
        smd.setSize(impl.countSize(path).getSize());
        System.out.println(smd.getSize());
    }
    
    @Test
    public void testIsPP() {
        String path = "C:\\Users\\linh\\Desktop\\s.pptx";
        SizeCounterUtil impl = new SizeCounterUtil();
        int i = impl.countSlide(path);
        System.out.println(i);
    }
    
 /**
  * @author DuanND   
  */
    @Test
    public void testIsWord1() {
        String path = "D:\\Test1.doc";
        SizeCounterImpl impl = new SizeCounterImpl();
        SizeMetaData smd = new SizeMetaData();
        smd.setSize(impl.countSize(path).getSize());
        System.out.println(smd.getSize());
    }
    @Test
    public void testIsSource1() {
        String path = "D:\\Test1.java";
        SizeCounterImpl impl = new SizeCounterImpl();
        SizeMetaData smd = new SizeMetaData();
        smd.setSize(impl.countSize(path).getSize());
        System.out.println(smd.getSize());
    }
    @Test
    public void testIsPP1() throws IOException {
        String path = "C:\\Users\\linhln\\Desktop\\Template_UnitTest Case_v0.1.xls";
        SizeCounterUtil impl = new SizeCounterUtil();
        boolean i = impl.checkIsUnitTestFile(path);
        System.out.println(i);
    }
    @Test
    public void getResources(){
        SizeCounterUtil impl = new SizeCounterUtil();
        ArrayList<String> a = impl.getResources("ExcelFiles");
        System.out.println(a.toString());
        System.out.println(impl.checkFileType("PowerPointFiles", "xls"));
    }
}
