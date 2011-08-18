/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */


package com.fms1.tools;

import java.io.File;
import java.io.*;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;

/**
 * File for HSSF testing/examples
 *
 * THIS IS NOT THE MAIN HSSF FILE!!  This is a util for testing functionality.
 * It does contain sample API usage that may be educational to regular API users.
 *
 * @see #main
 * @author Andrew Oliver (acoliver at apache dot org)
 */

public class HSSF
{
    public static void Start(String args){
		java.io.File fExcelFile = null;
		try
        {
			fExcelFile = new java.io.File(args);
			Workbook workbook = Workbook.getWorkbook(fExcelFile);
			Sheet sheet = workbook.getSheet(1);
			int k=0;
			if (!StringUtils.isBlank(sheet.getCell(0, k).getContents())) {
                k++;   
        	}
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
