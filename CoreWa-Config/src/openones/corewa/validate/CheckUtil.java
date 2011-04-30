/**
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package openones.corewa.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.log4j.Logger;

import openones.corewa.validate.config.CheckType;
import openones.corewa.validate.config.ErrorField;
import openones.corewa.validate.config.Field;
import openones.corewa.validate.config.Var;
import rocky.common.CommonUtil;

/**
 * @author Thach Le
 */
public class CheckUtil {
    final static Logger LOG = Logger.getLogger("CheckUtil");
    /**
     * Evaluate value of var ${XXX}.
     * 
     * @param varMap
     * @return
     */
    public static Map<String, Var> evaluateVar(Map<String, Var> varMap, Map<String, Object> reqMap) {

        // Scan value of variable. If is contain ${XXX}, ${XXX} will be replaced by value of XXX in request
        Var var;
        String varValue;
        String evalValue;
        for (String key : varMap.keySet()) {
            var = varMap.get(key);

            if (var.getValue() instanceof String) {
                varValue = (String) var.getValue();
                if (CommonUtil.isNNandNB(varValue) && (varValue.indexOf("${") > -1)) {
                    evalValue = CommonUtil.formatPattern(varValue, reqMap);
                    var.setValue(evalValue);
                }
                // Update evaluated value of var
                varMap.put(key, var);
            }
        }

        return varMap;
    }
    
    /**
     * Check error of input field
     * 
     * @param field data validation configuration
     * @param value value of field
     * @return ErrorField contains the error. null if no error.
     */
    public static ErrorField checkInput(Field field, Object value, Map<String, Object> valueMap) {
        ErrorField result = null;
        String patternValue = null;
        
        if (field == null) {
            return null;
        }
        
        LOG.debug("checkInput:fieldName=" + field.getName() + "';type=" + field.getCheckType());
        
        CheckType typeOfChk = field.getCheckType();
        boolean hasError = false;
        
        switch (typeOfChk) {
            case mandatory :
                hasError = (!CommonUtil.isNNandNB(value));
                break;
            case length :
                Double min = field.getMin();
                Double max = field.getMax();
                Double dblValue = null;
                if ((value instanceof Double) || (value instanceof Float)) {
                    dblValue = (Double) value;
                } else { // compare size if it is String
                    dblValue = Double.valueOf(((String) value).length());
                }
                
                if ((min != null) && (max != null)) { // Has low limit and upper limit
                    hasError = ((dblValue < min) || (dblValue > max));
                } else if (max == null) { // Only low limit, no upper limit
                    hasError = (dblValue < min);
                } else if (min == null) { // No low limit, only upper limit
                    hasError = (dblValue > max);
                }
                break;
            case pattern :
                if (CommonUtil.isNNandNB(value) && CommonUtil.isNNandNB(field.getPattern())) {
                    patternValue = CommonUtil.formatPattern(field.getPattern(), valueMap);
                    hasError = !value.toString().matches(patternValue);
                }
                break;
            case datefmt :
                if (CommonUtil.isNNandNB(value) && CommonUtil.isNNandNB(field.getPattern())) {
                    patternValue = CommonUtil.formatPattern(field.getPattern(), valueMap);
                    SimpleDateFormat sdf = new SimpleDateFormat(patternValue);
                    try {
                        sdf.parse(value.toString());
                    } catch (ParseException ex) {
                        hasError = true;
                    }
                }
                break;
            case decfmt :
                break;
        }

        LOG.debug("Check field '" + field.getName() + "';type=" + field.getCheckType() + ";pattern" + field.getPattern() + ";error=" + hasError);
        
        if (hasError) {
            result = new ErrorField(field.getId(), field.getName());
            result.setErrorMessage(CommonUtil.formatPattern(field.getError(), valueMap));
        }

        return result;
    }

}
