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
package openones.svnloader.tools;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import rocky.common.Constant;
import rocky.common.PropertiesManager;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

/**
 * 
 * 
 */
public class CodeCheckerUtil {    
    final static Logger LOG = Logger.getLogger("CodeCheckerUtil");
    static String codeCheckerImpl = null;
    static String xmlCheckStyle = null;
    static String className = null;
    static String methodName = null;

    static Object checkCheckerObj = null;
    static Method checkMethod = null;
    static boolean isImplemented;
    static {
        PropertiesManager props;
        try {
            props = new PropertiesManager("/conf.properties");

            codeCheckerImpl = props.getProperty("CodeCheckerImpl");
            xmlCheckStyle = props.getProperty("XMLCheckStyle");
            // get class name, method
            if (codeCheckerImpl != null) {
                int lastDot = codeCheckerImpl.lastIndexOf(Constant.STR_DOT);
                className = codeCheckerImpl.substring(0, lastDot);
                methodName = codeCheckerImpl.substring(lastDot + 1);
                createCodeCheckerImpl();
            } else {
                isImplemented = false;
            }
        } catch (Exception ex) {
            LOG.error("Load configuration file conf.properties", ex);
        }

    }

    static void createCodeCheckerImpl() {
        try {
            checkCheckerObj = Class.forName(className).newInstance();
            checkMethod = checkCheckerObj.getClass().getMethod(methodName, String.class, String.class);
        } catch (Exception ex) {
            LOG.error("New class " + className, ex);
        }
    }

    public Map<String, List<AuditEvent>> check(String sourcePath) {
        Map<String, List<AuditEvent>> errorMap = null;
        try {
        	if (codeCheckerImpl != null) {
        		errorMap = (Map<String, List<AuditEvent>>) checkMethod.invoke(checkCheckerObj, xmlCheckStyle, sourcePath);
        	}
        } catch (Exception ex) {
            LOG.error("Invoke method '" + methodName + "' of class '" + className, ex);
        }
        return errorMap;
    }

    public static boolean isImplemented() {
        return isImplemented;
    }
}
