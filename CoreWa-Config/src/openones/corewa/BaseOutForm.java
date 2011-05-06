package openones.corewa;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import openones.corewa.config.Event;
import openones.corewa.validate.config.ErrorField;

public class BaseOutForm implements Serializable {
    private Map<String, Object> requestMap = new HashMap<String, Object>();
    private Map<String, Object> sessionMap = new HashMap<String, Object>();
    private BaseInForm inForm;
    
    /** Support specified next jsp page. */
    private String nextScreen;
    
    /** Support multi forwards. */
    private String nextResult;

    /** transType: FORWARE, INCLUDE. Null means redirect */
    private Event.DispType dispType;

    /** Processed request dispatch in sub control. */
    private boolean isDispatched;
    
    public void putRequest(String key, Object value) {
        requestMap.put(key, value);
    }

    public Event.DispType getDispType() {
        return dispType;
    }

    public void setDispType(Event.DispType dispType) {
        this.dispType = dispType;
    }

    public boolean isDispatched() {
        return isDispatched;
    }

    public void setDispatched(boolean isDispatched) {
        this.isDispatched = isDispatched;
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
    public String getNextResult() {
        return nextResult;
    }

    public void setNextResult(String nextResult) {
        this.nextResult = nextResult;
    }
    
    public String getNextScreen() {
        return nextScreen;
    }

    public void setNextScreen(String nextScreen) {
        this.nextScreen = nextScreen;
    }
}
