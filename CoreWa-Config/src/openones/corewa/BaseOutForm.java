package openones.corewa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseOutForm implements Serializable {
    private Map<String, Object> requestMap = new HashMap<String, Object>();
    private Map<String, Object> sessionMap = new HashMap<String, Object>();
    private BaseInForm inForm;
    
    public void putRequest(String key, Object value) {
        requestMap.put(key, value);
    }
    
    public void putSession(String key, Object value) {
        sessionMap.put(key, value);
    }
    
    public Map<String, Object> getRequestMap() {
        return requestMap;
    }
    
    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public BaseInForm getInForm() {
        return inForm;
    }

    public void setInForm(BaseInForm inForm) {
        this.inForm = inForm;
    }

}
