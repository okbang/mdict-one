package rocky.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

import rocky.common.Constant;
import rocky.common.LogService;
import rocky.common.PropertiesManager;
import rocky.sql.RockySql;

public class SqlConfigurationUtil {
    
    /**
     * Parse report configuration.
     * @param resourcePath
     * @param props
     * @return null if the error occurs
     */
    public static List<ReportItemConfigurationInfo> parse(String resourcePath) {
        List<ReportItemConfigurationInfo> rptItemCfgInfoList = new ArrayList<ReportItemConfigurationInfo>();
        ReportItemConfigurationInfo rptItemCfgInfo;
        Map<String, String> queryMap;
        try {
            PropertiesManager propsManager = new PropertiesManager(resourcePath);
            RockySql rockySql = new RockySql(propsManager.getProperties());
            
            // Get list of report identifier
            String[] reportIds = propsManager.getProperties("RPTLIST", Constant.COMMA);
            String key;
            String reportName;
            String query;
            for (String reportId : reportIds) {
                // Get report name
                key = "RPTENTRY." + reportId;
                reportName = propsManager.getProperty(key);
                //query = rockySql.getCombineSql(reportId + ".SQL");
                queryMap = rockySql.getQueryMap(reportId + ".SQL");
                query = RockySql.getCombineSql(reportId + ".SQL", queryMap);
                
                rptItemCfgInfo = new ReportItemConfigurationInfo(reportId, reportName, query, queryMap);
                rptItemCfgInfoList.add(rptItemCfgInfo);
            }
        } catch (InvalidPropertiesFormatException ipfex) {
            LogService.logError(ipfex);
            return null;
        } catch (IOException ioex) {
            LogService.logError(ioex);
            return null;
        }
        
        return rptItemCfgInfoList;
    }
}
