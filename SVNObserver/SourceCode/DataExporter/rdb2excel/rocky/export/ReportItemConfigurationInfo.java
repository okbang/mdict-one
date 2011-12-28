package rocky.export;

import java.util.Map;

public class ReportItemConfigurationInfo {
    private String id;
    private String name;
    private String query;
    private Map<String, String> queryMap;
    
    public ReportItemConfigurationInfo(String reportId, String reportName, String query, Map<String, String> queryMap) {
        this.id = reportId;
        this.name = reportName;
        this.query = query;
        this.queryMap = queryMap;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Map<String, String> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }
}
