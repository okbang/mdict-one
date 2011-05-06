package openones.corewa;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import openones.corewa.validate.config.ErrorField;
import rocky.common.CommonUtil;

public class BaseInForm extends BaseForm {
    private Map<String, ErrorField> errorFieldMap = new TreeMap<String, ErrorField>();
    
    public void putError(ErrorField errorField) {
        errorFieldMap.put(errorField.getId(), errorField);
    }
    
    public Collection<ErrorField> getErrors() {
        return errorFieldMap.values();
    }
    
    public boolean isError(String checkId) {
        return errorFieldMap.containsKey(checkId);
    }
    
    public boolean isError(List<String> checkIdList) {
        if (CommonUtil.isNNandNB(checkIdList)) {
            for (String checkId : checkIdList) {
                if (errorFieldMap.containsKey(checkId)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
