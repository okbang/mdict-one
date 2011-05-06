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
package openones.corewa;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import openones.corewa.validate.CheckUtil;
import openones.corewa.validate.config.ErrorField;
import openones.corewa.validate.config.Field;
import openones.corewa.validate.config.FormValidation;
import openones.corewa.validate.config.ValidationConf;
import openones.corewa.validate.config.Var;

import org.apache.log4j.Logger;

import rocky.common.BeanUtil;

/**
 * @author ThachLN
 */
public class ReqUtil {
    final public static Logger LOG = Logger.getLogger("ReqUtil");

    /**
     * Parse data from request map into form bean.
     * 
     * @param reqMap HashMap contain user data
     * @param clazz class of form bean
     * @param validationConf contain the data validation configuration
     * @return form bean
     */
    public static BaseInForm getData(Map<String, Object> reqMap, Class clazz, ValidationConf validationConf) {
        LOG.debug("getData.START:clazz=" + clazz + ";validationConf=" + validationConf);
        Object bean = null;

        try {
            bean = clazz.newInstance();
            // 1. Get all method setters
            Map<String, Method> beanSetterMap = BeanUtil.getWriteMethodMap(bean);

            // Scan all properties of the bean
            Method method = null;
            Object value;

            FormValidation formVd = null;
            if (validationConf != null) {
                // Evaluate variables
                Map<String, Var> evaluatedVarMap = CheckUtil.evaluateVar(validationConf.getVarMap(), reqMap);
                validationConf.setVarMap(evaluatedVarMap);

                formVd = validationConf.getFormValidation();
            }
            List<Field> fieldList; 
            ErrorField errorField = null;
            BaseInForm inForm;
            for (String propName : beanSetterMap.keySet()) {
                // get value from the request basing property
                value = reqMap.get(propName);
                if (validationConf != null) { // Check input
                    // Get configuration field of property
                    fieldList = formVd.getFieldByName(propName);
                    for (Field field : fieldList) {
                        LOG.debug("Check field '" + field.getName() + ";value=" + value);

                        // If a validation is a depend check, verify 
                        inForm = (BaseInForm) bean;
                        if (!inForm.isError(field.getDependIdList())) { // Depended checks have no error
                            errorField = CheckUtil.checkInput(field, value, reqMap);
                            LOG.debug(";result=" + errorField);
                        
                            if (errorField != null) { // Add error into form bean
                                ((BaseInForm) bean).putError(errorField);
                            }
                        }
                        
                    }
                }
                method = beanSetterMap.get(propName);
                // Call method setter of property
                LOG.debug("Set " + propName + "=" + value);

                if (value != null) {
                    method.invoke(bean, value);
                }
            }
        } catch (Exception ex) {
            LOG.error("Get data form web request to form bean", ex);
        }
        LOG.debug("getData.END");

        return (BaseInForm) bean;

    }

    /**
     * Parse data from request map into form bean.
     * 
     * @param reqMap HashMap contain user data
     * @param clazz class of form bean
     * @return form bean
     */
    public static BaseInForm getData(Map<String, Object> reqMap, Class clazz) {
        LOG.debug("getData.START:clazz=" + clazz);
        return getData(reqMap, clazz, null);
    }
}
