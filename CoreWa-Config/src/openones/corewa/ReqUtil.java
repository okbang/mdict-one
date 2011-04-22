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
import java.util.Map;

import org.apache.log4j.Logger;

import rocky.common.BeanUtil;

/**
 * @author ThachLN
 *
 */
public class ReqUtil {
    final public static Logger LOG = Logger.getLogger("ReqUtil");
    
    public static BaseInForm getData(Map<String, Object> mapReq, Class clazz) {
        LOG.debug("getData.START:clazz=" + clazz);
        Object bean = null;

        try {
            bean = clazz.newInstance();
            // 1. Get all method setters
            Map<String, Method> beanSetterMap = BeanUtil.getWriteMethodMap(bean);

            // Scan all properties of the bean
            Method method = null;
            Object value;

            for (String propName : beanSetterMap.keySet()) {
                // get value from the request basing property
                value = mapReq.get(propName);

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
}
