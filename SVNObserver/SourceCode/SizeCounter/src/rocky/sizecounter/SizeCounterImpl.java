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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import rocky.common.CommonUtil;
import rocky.common.LogService;
import rocky.common.PropertiesManager;
import tk.stepcounter.CountResult;
import tk.stepcounter.StepCounter;
import tk.stepcounter.StepCounterFactory;
import csdl.locc.measures.cpp.parser.javacc.TokenMgrError;

/**
 * @author LinhLn
 */
public class SizeCounterImpl implements ISizeCounter {
    static final SizeCounterUtil scu = new SizeCounterUtil();
    static final Logger log = Logger.getLogger("ISizeCounter");
    @Override
    public SizeMetaData countSize(String filePath) {
        // TODO Auto-generated method stub
        SizeMetaData sizeMD = new SizeMetaData();
        SourceMetaData sizeMd = new SourceMetaData();
        int sizeResult;
        String ext = CommonUtil.getExtension(filePath);
        try {
            if (scu.checkFileType("SourceFiles" , ext)) {
                sizeMd = countLOC(filePath);
                sizeMD.setSize(sizeMd.getLoc());
                sizeMD.setUnit(UnitType.LOC);
                sizeMD.setSize1(sizeMd.getComment());
                sizeMD.setUnit1(UnitType.COMMENT);
            } else if (scu.checkFileType("WordFiles" , ext)) {
                sizeResult = scu.countPage(filePath);
                sizeMD.setSize(sizeResult);
                sizeMD.setUnit(UnitType.PAGE);
            } else if (scu.checkFileType("ExcelFiles" , ext)) {
                sizeResult = scu.countSheet(filePath);
                sizeMD.setSize(sizeResult);
                sizeMD.setUnit(UnitType.SHEET);
                if (scu.checkIsUnitTestFile(filePath)) {
                    int nmbUTC = scu.getNmTC(filePath);
                    sizeMD.setSize1(nmbUTC);
                }
            } else if (scu.checkFileType("PowerPointFiles" , ext)) {
                sizeResult = scu.countSlide(filePath);
                sizeMD.setSize(sizeResult);
                sizeMD.setUnit(UnitType.SLIDE);
            }
        } catch (TokenMgrError tmex) {
            LogService.logError(this.getClass(), tmex);
        } catch (UnsupportedFileType ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        return sizeMD;
    }

    /*
     * Explain the description for this method here
     * @see rocky.sizecounter.ISizeCounter#countLOC(java.lang.String, java.lang.String)
     */
    @Override
    public SourceMetaData countLOC(String filePath, String csName) throws UnsupportedFileType {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * Explain the description for this method here
     * @see rocky.sizecounter.ISizeCounter#countLOC(java.lang.String)
     */
    @Override
    public SourceMetaData countLOC(String filePath) throws UnsupportedFileType {
        // TODO Auto-generated method stub
        SourceMetaData srcMd = new SourceMetaData();
        StepCounter stepCounter = StepCounterFactory.getCounter(filePath);
        CountResult cntRes;

        if (stepCounter != null) {

            try {
                cntRes = stepCounter.count(new File(filePath), "UTF-8");

                if (cntRes != null) {
                    srcMd.setLoc(cntRes.getStep());
                    srcMd.setComment(cntRes.getComment());
                } else {
                    log.warn("Could not count file extension:"
                            + CommonUtil.getExtension(filePath));
                }

                return srcMd;

            } catch (IOException ioex) {
                log.error("Error in counting size of file '" + filePath + "'",
                        ioex);
            }
        } else {
            log.warn("Don't support counter for file extension:"
                    + CommonUtil.getExtension(filePath));
            throw new UnsupportedFileType(
                    "Don't support counter for file extension:"
                            + CommonUtil.getExtension(filePath));
        }

        return null;
    }
    /*
     * Explain the description for this method here
     * @see rocky.sizecounter.ISizeCounter#isCountable(java.lang.String)
     */
    @Override
    public boolean isCountable(String extFile) {
        // TODO Auto-generated method stub
        return scu.checkFileType("SupportFiles", extFile);
    }

}
