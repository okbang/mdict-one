package rocky.jrockyexport.gui.form;

import java.util.Map;

public class ReportConfigForm {
    private String name;
    
    private String rptEntry; // Output Excel sheet
    private Map<String, String> queryMap;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRptEntry() {
        return rptEntry;
    }
    public void setRptEntry(String rptEntry) {
        this.rptEntry = rptEntry;
    }
    public Map<String, String> getQueryMap() {
        return queryMap;
    }
    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }
}
