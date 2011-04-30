package openones.corewa;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import openones.corewa.validate.config.ErrorField;

public class BaseInForm implements Serializable {
    private Map<String, ErrorField> errorFieldMap = new TreeMap<String, ErrorField>();
    
    public void putError(ErrorField errorField) {
        errorFieldMap.put(errorField.getId(), errorField);
    }
    
    public Collection<ErrorField> getErrors() {
        return errorFieldMap.values();
    }
}
