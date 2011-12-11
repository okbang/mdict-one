package tk.stepcounter;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import rocky.common.CommonUtil;
import rocky.sizecounter.ISizeCounter;
import rocky.sizecounter.SourceMetaData;
import rocky.sizecounter.UnsupportedFileType;

public class StepCounterImpl implements ISizeCounter {
	static final Logger LOG = Logger.getLogger("StepCounterImpl"); 
    @Override
    public SourceMetaData countLOC(String filePath, String csName) throws UnsupportedFileType {
        SourceMetaData srcMd = new SourceMetaData();
        StepCounter stepCounter = StepCounterFactory.getCounter(filePath);
        CountResult cntRes;
        
        if (stepCounter != null) {

            try {
                cntRes = stepCounter.count(new File(filePath));
                if (cntRes != null) {
                    srcMd.setLoc(cntRes.getStep());
                    srcMd.setComment(cntRes.getComment());
                } else {
                    LOG.warn("Could not count file extension:" + CommonUtil.getExtension(filePath));
                }

                return srcMd;

            } catch (IOException ioex) {
                LOG.error("Error in counting size of file '" + filePath + "' csName=" + csName, ioex);
            }
        } else {
            LOG.warn("Don't support counter for file extension:" + CommonUtil.getExtension(filePath));
            throw new UnsupportedFileType("Don't support counter for file extension:" + CommonUtil.getExtension(filePath));
        }
        
        return null;
    }
    
    @Override
    public SourceMetaData countLOC(String filePath) throws UnsupportedFileType {
        SourceMetaData srcMd = new SourceMetaData();
        StepCounter stepCounter = StepCounterFactory.getCounter(filePath);
        CountResult cntRes;
        
        if (stepCounter != null) {

            try {
                cntRes = stepCounter.count(new File(filePath));
                if (cntRes != null) {
                    srcMd.setLoc(cntRes.getStep());
                    srcMd.setComment(cntRes.getComment());
                } else {
                    LOG.warn("Could not count file extension:" + CommonUtil.getExtension(filePath));
                }

                return srcMd;

            } catch (IOException ioex) {
                LOG.error("Error in counting size of file '" + filePath + "'", ioex);
            }
        } else {
            LOG.warn("Don't support counter for file extension:" + CommonUtil.getExtension(filePath));
            throw new UnsupportedFileType("Don't support counter for file extension:" + CommonUtil.getExtension(filePath));
        }
        
        return null;
    }
}
