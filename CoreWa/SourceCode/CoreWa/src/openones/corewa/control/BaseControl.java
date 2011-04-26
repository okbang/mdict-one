/*
 * BaseControl.java 0.1 June 30, 2010
 * 
 * Copyright (c) 2010, LunarCal4U
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package openones.corewa.control;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.corewa.BaseOutForm;
import openones.corewa.form.BaseForm;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import rocky.common.BeanUtil;

public class BaseControl {
    final static Logger LOG = Logger.getLogger("BaseControl");

    
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.log(Level.INFO, "procInit.START");
        LOG.log(Level.INFO, "procInit.END");
        
        return null;
    }
    

    /**
     * Get data from web request to form bean.
     * @param req
     * @param clazz
     * @return
     */
    protected BaseForm getData(HttpServletRequest req, Class clazz) {
        LOG.fine("getData.START");
        Object bean = null;
        
        try {
           bean = clazz.newInstance();
           // 1. Get all method setters
           Map<String, Method> beanSetterMap = BeanUtil.getWriteMethodMap(bean);
           
           // Scan all property
           Object value;
           Method method;
           for (String propName : beanSetterMap.keySet()) {
               // get value from the request basing property
               value = req.getParameter(propName);
               if (value == null) {
                   value = req.getAttribute(propName);
               }
                if (value != null) {
                    method = beanSetterMap.get(propName);
                    // Call method setter of property
                    LOG.fine("Set " + propName + "=" + value);
                    method.invoke(bean, value);
                }
           }
        } catch (Exception ex) {
            LOG.log(Level.FINE, "Get data form web request to form bean", ex);
        }
        LOG.fine("getData.END");
        
        return (BaseForm) bean;
    }
    
    /**
     * This method is invoked by CoreWa.
     * @param req
     * @return
     */
    public static Map<String, Object> getMapData(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<String, Object>();
        LOG.log(Level.FINEST, "getMapData.START:");
        Object bean = null;

        if (!ServletFileUpload.isMultipartContent(req)) { // Normal form data
            String fieldName;
            for (Enumeration<String> keyEnum = req.getParameterNames(); keyEnum.hasMoreElements();) {
                fieldName = keyEnum.nextElement();
                result.put(fieldName, req.getParameter(fieldName));
            }
            return result;
        } else { // Upload form data
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload portletFU = new ServletFileUpload(fileItemFactory);
            List items;
            try {
                items = portletFU.parseRequest(req);
                FileItem fit;
                String fieldName;
                String value;
                for (Iterator it = items.iterator(); it.hasNext();) {
                    fit = (FileItem) it.next();
                    fieldName = fit.getFieldName();

                    if (fit.isFormField()) { // Normal field
                        value = fit.getString();
                        result.put(fieldName, value);
                    } else {
                        result.put(fieldName, fit);
                    }
                }
            } catch (FileUploadException ex) {
                LOG.log(Level.FINE, "Parsing the request", ex);
            }

        }
        LOG.log(Level.FINEST, "getMapData.END");

        return result;
    }

}
