package rocky.util;

import java.io.FileNotFoundException;
import java.io.InputStream;

import rocky.common.CommonUtil;
import rocky.common.LogService;
import rocky.jdbc.AliasConfig;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ConfigUtil {
    
    public static AliasConfig loadConfiguration(String fileCfgPath) {
        XStream streamer = new XStream(new DomDriver());
        InputStream input = null;
        AliasConfig retCfg = null;
        try {
            input = CommonUtil.loadResource(fileCfgPath);
            retCfg = (AliasConfig)streamer.fromXML(input);
        } catch (FileNotFoundException fnfex) {
            LogService.logError(fnfex);
        }
        return retCfg;
    }
}
