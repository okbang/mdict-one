/**
 * Licensed to OpenOnes under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * OpenOnes licenses this file to you under the Apache License,
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
package openones.corewa.validate.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rocky.common.CommonUtil;
import rocky.common.XMLUtil;

public class ValidateConfUtil {
    static final Logger LOG = Logger.getLogger("ValidateConfUtil");

    public static ValidationConf parse(InputStream is) {
        ValidationConf validator = new ValidationConf();
        
        try {
            Document doc = XMLUtil.parse(is);
            validator = parse(doc);
        } catch (Exception ex) {
            LOG.error("Parse InputStream of Validation XML configuration file.", ex);
        }

        return validator;
    }

    /**
     * Parse xml configuration file.
     * @param doc
     * @return
     */
    private static ValidationConf parse(Document doc) {
        ValidationConf validator = new ValidationConf();

        validator.setVarMap(parseVar(doc));
        validator.setFormValidation(parseForm(doc));
        
        return validator; 
    }

    /**
     * Parse node <form>
     * @param doc
     * @return
     */
    private static Map<String, Var> parseVar(Document doc) {
        Map<String, Var> varMap = new HashMap<String, Var>();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        
        try {
            NodeList varNodeList = (NodeList) xp.evaluate("//var", doc, XPathConstants.NODESET);
            
            int len = (varNodeList != null)? varNodeList.getLength():0;
            Node varNode;
            Var varInfo;
            String key;
            for (int i = 0; i < len; i++) {
                varNode = varNodeList.item(i);
                
                varInfo = new Var();
                varInfo.setName(xp.evaluate("@name", varNode));
                varInfo.setValue(xp.evaluate("@value", varNode));
                varMap.put(varInfo.getName(), varInfo);
            }
            
        } catch (XPathExpressionException e) {
            LOG.error("Parse configuration file: lookup var", e);
        }
        return varMap;
    }

    private static FormValidation parseForm(Document doc) {
        FormValidation formValidation = new FormValidation();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        
        try {
            Node formNode = (Node) xp.evaluate("//form", doc, XPathConstants.NODE);
            formValidation.setId(xp.evaluate("@id", formNode));
            
            NodeList varFieldList = (NodeList) xp.evaluate("//form/field", doc, XPathConstants.NODESET);
            
            int len = (varFieldList != null)? varFieldList.getLength():0;
            Node fieldNode;
            Field fieldInfo;
            String min;
            String max;
            for (int i = 0; i < len; i++) {
                fieldNode = varFieldList.item(i);
                
                fieldInfo = new Field();
                fieldInfo.setId(xp.evaluate("@id", fieldNode));
                fieldInfo.setName(xp.evaluate("@name", fieldNode));
                fieldInfo.setCheckType(xp.evaluate("@check-type", fieldNode));
                fieldInfo.setPattern(xp.evaluate("@pattern", fieldNode));
                
                min = xp.evaluate("@min", fieldNode);
                max = xp.evaluate("@max", fieldNode);
                if (CommonUtil.isNNandNB(min)) {
                    fieldInfo.setMin(Double.valueOf(min));
                }
                
                if (CommonUtil.isNNandNB(max)) {
                    fieldInfo.setMax(Double.valueOf(max));
                }
                
                fieldInfo.setError(xp.evaluate("@error", fieldNode));
                
                formValidation.add(fieldInfo);
            }
            
        } catch (XPathExpressionException ex) {
            LOG.error("Parse configuration file: lookup form", ex);
        }
        
        return formValidation;
    }

    public static ValidationConf parse(String resource) {
        InputStream validateFileIs = null;
        try {
            validateFileIs = CommonUtil.loadResource(resource);
            return ValidateConfUtil.parse(validateFileIs);
        } catch (FileNotFoundException fnfEx) {
            LOG.error("Load resource file '" + resource + "'", fnfEx);
            fnfEx.printStackTrace();
        }
        
        return null;
    }
}
