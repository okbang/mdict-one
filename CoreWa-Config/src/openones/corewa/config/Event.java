package openones.corewa.config;

import java.util.HashMap;
import java.util.Map;

import rocky.common.CommonUtil;

public class Event {
    public static enum DispType { INCLUDE, FORWARD };
    private String id;
    private String procId;
    
    private String nextScrId;
    private DispType dispType = DispType.INCLUDE;
    
    /** If attribute "redirect" is declared, the procId is skipped. */
    private boolean isRedirect = false;

    private String formBean;
    private String scope;
    
    private Map<String, Result> results = new HashMap<String, Result>();
    
    public Event() {
    }
    
    public Event(String id, String procId, String nextScrId) {
        this.id = id;
        this.procId = procId;
        this.nextScrId = nextScrId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getNextScrId() {
        return nextScrId;
    }

    public void setNextScrId(String nextScrId) {
        this.nextScrId = nextScrId;
    }

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

    public String getFormBean() {
        return formBean;
    }

    public void setFormBean(String formBean) {
        this.formBean = formBean;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public DispType getDispType() {
        return dispType;
    }

    public void setDispType(DispType dispType) {
        this.dispType = dispType;
    }

    public void setDispType(String dispType) {
        if (CommonUtil.isNNandNB(dispType)) {
            this.dispType = DispType.valueOf(dispType);
        }
    }

    public Map<String, Result> getResults() {
        return results;
    }

    public void setResults(Map<String, Result> results) {
        this.results = results;
    }
    
    public Result getResult(String resultId) {
        return results.get(resultId);
    }
}
