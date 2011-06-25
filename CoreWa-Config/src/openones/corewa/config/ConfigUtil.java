package openones.corewa.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rocky.common.CommonUtil;

public class ConfigUtil {
    static final Logger LOG = Logger.getLogger("ConfigUtil");

    public static CoreWa parse(InputStream is) {
        CoreWa confInfo = new CoreWa();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            confInfo = parse(doc);
        } catch (Exception ex) {
            LOG.error("Parse InputStream of CoreWa XML configuration file.", ex);
        }

        return confInfo;
    }

    /**
     * Parse xml configuration file.
     * @param doc
     * @return
     */
    private static CoreWa parse(Document doc) {
        CoreWa cwInfo = new CoreWa();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        try {
            // Lookup view-screen, edit-screen, help-screen for Portlet only
            String value = xp.evaluate("//view-screen/text()", doc);
            cwInfo.setViewScreen(value);
            
            value = xp.evaluate("//edit-screen/text()", doc);
            cwInfo.setEditScreen(value);
            
            value = xp.evaluate("//help-screen/text()", doc);
            cwInfo.setHelpScreen(value);
            
            // Lookup home-screen for Servlet only
            value = xp.evaluate("//home-screen/text()", doc);
            cwInfo.setHomeScreenId(value);
            
            cwInfo.setLayout(parseLayout(doc));
            cwInfo.setForms(parseForm(doc));
            cwInfo.setScreens(parseScreen(doc));
        } catch (XPathExpressionException e) {
            LOG.error("Parse configuration file", e);
        } 
        
        return cwInfo; 
    }
    /**
     * Parse node
          <layout id="Layout">
              <part id="header.do" screen="Header"/>
              <part id="left.do" screen="Left"/>
              <part id="main.do" screen="Main"/>
              <part id="right.do" screen="Right"/>
              <part id="footer.do" screen="Footer"/>
          </layout>
     * @param doc
     * @return
     */
    private static Layout parseLayout(Document doc) {
        Map<String, Form> formBeanMap = new HashMap<String, Form>();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        Layout layout = new Layout();
        
        try {
            Node layoutNode = (Node) xp.evaluate("//layout", doc, XPathConstants.NODE);
            if (layoutNode == null) {
                return null;
            }

            layout.setId(xp.evaluate("@id", layoutNode));
            
            NodeList partNodeList = (NodeList) xp.evaluate("//layout/part", doc, XPathConstants.NODESET);
            
            int len = (partNodeList != null)? partNodeList.getLength():0;
            Node partNode;
            Part part;
            String key;
            for (int i = 0; i < len; i++) {
                partNode = partNodeList.item(i);
                
                part = new Part();
                part.setId(xp.evaluate("@id", partNode));
                part.setScreenId(xp.evaluate("@screen", partNode));
                
                layout.add(part);
            }
            
        } catch (XPathExpressionException e) {
            LOG.error("Parse configuration file: lookup form", e);
        }
        return layout;
    }
    /**
     * Parse node <form>
     * @param doc
     * @return
     */
    private static Map<String, Form> parseForm(Document doc) {
        Map<String, Form> formBeanMap = new HashMap<String, Form>();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        
        try {
            NodeList formBeanNodeList = (NodeList) xp.evaluate("//form", doc, XPathConstants.NODESET);
            
            int len = (formBeanNodeList != null)? formBeanNodeList.getLength():0;
            Node formBeanNode;
            Form formBeanInfo;
            String key;
            for (int i = 0; i < len; i++) {
                formBeanNode = formBeanNodeList.item(i);
                
                formBeanInfo = new Form();
                formBeanInfo.setId(xp.evaluate("@id", formBeanNode));
                formBeanInfo.setClassName(xp.evaluate("@class", formBeanNode));
                formBeanInfo.setValidateFile(xp.evaluate("@validate-file", formBeanNode));
                
                if (CommonUtil.isNNandNB(formBeanInfo.getId())) {
                    formBeanMap.put(formBeanInfo.getId(), formBeanInfo);
                }
            }
            
        } catch (XPathExpressionException e) {
            LOG.error("Parse configuration file: lookup form", e);
        }
        return formBeanMap;
    }

    private static Map<String, Screen> parseScreen(Document doc) {
        Map<String, Screen> screenMap = new HashMap<String, Screen>();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        
        try {
            NodeList screenNodeList = (NodeList) xp.evaluate("//screen", doc, XPathConstants.NODESET);
            
            int len = (screenNodeList != null)? screenNodeList.getLength():0;
            Node screenNode;
            Screen screenInfo;
            String dispType;
            for (int i = 0; i < len; i++) {
                screenNode = screenNodeList.item(i);
                
                screenInfo = new Screen();
                screenInfo.setId(xp.evaluate("@id", screenNode));
                screenInfo.setCtrlClass(xp.evaluate("@control", screenNode));
                screenInfo.setInputPage(xp.evaluate("@input", screenNode));
                screenInfo.setFormId(xp.evaluate("@form", screenNode));
                dispType = xp.evaluate("@disp-type", screenNode);
                if (CommonUtil.isNNandNB(dispType)) {
                    screenInfo.setDispType(dispType);
                }
                
                screenInfo.setEvents(parseEvent(screenNode));
                
                screenMap.put(screenInfo.getId(), screenInfo);
            }
            
        } catch (XPathExpressionException e) {
            LOG.error("Parse configuration file: lookup screen", e);
        }
        
        return screenMap;
    }

    private static Map<String, Event> parseEvent(Node screenNode) {
        Map<String, Event> eventMap = new HashMap<String, Event>();
        
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        
        try {
            NodeList eventNodeList = (NodeList) xp.evaluate("event", screenNode, XPathConstants.NODESET);
            int len = (eventNodeList != null) ? eventNodeList.getLength() : 0;
            
            Node eventNode;
            Event eventInfo;
            for (int i = 0; i < len; i++) {
                eventNode = eventNodeList.item(i);
                
                eventInfo = new Event();
                eventInfo.setId(xp.evaluate("@id", eventNode));
                eventInfo.setProcId(xp.evaluate("@procId", eventNode));
                eventInfo.setRedirect((Boolean) xp.evaluate("@redirect", eventNode, XPathConstants.BOOLEAN));
                eventInfo.setNextScrId(xp.evaluate("@nextScreen", eventNode));
                eventInfo.setFormBean(xp.evaluate("@form", eventNode));
                eventInfo.setScope(xp.evaluate("@scope", eventNode));
                
                eventInfo.setDispType(xp.evaluate("@disp-type", eventNode).toUpperCase());
                
                eventMap.put(eventInfo.getId(), eventInfo);
                eventInfo.setResults(parseResult(eventNode));
            }
        } catch (XPathExpressionException e) {
            LOG.error("Parse configuration file: lookup event", e);
        }
        
        return eventMap;
    }

    /**
     * Parse node <Result> within node <Event>
     * @param screenNode
     * @return
     */
    private static Map<String, Result> parseResult(Node eventNode) {
        Map<String, Result> resultMap = new HashMap<String, Result>();
        
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        
        try {
            NodeList resultNodeList = (NodeList) xp.evaluate("result", eventNode, XPathConstants.NODESET);
            int len = (resultNodeList != null) ? resultNodeList.getLength() : 0;
            
            Node resultNode;
            Result resultInfo;
            for (int i = 0; i < len; i++) {
                eventNode = resultNodeList.item(i);
                
                resultInfo = new Result();
                resultInfo.setId(xp.evaluate("@id", eventNode));
                resultInfo.setNextScrId(xp.evaluate("@nextScreen", eventNode));
                
                resultMap.put(resultInfo.getId(), resultInfo);
            }
        } catch (XPathExpressionException e) {
            LOG.error("Parse node configuration file: lookup result of node event", e);
        }
        
        return resultMap;
    }

    public static CoreWa parse(String resource) {
        InputStream fileCfgIs = null;
        try {
            fileCfgIs = CommonUtil.loadResource(resource);
            return ConfigUtil.parse(fileCfgIs);
        } catch (FileNotFoundException fnfEx) {
            LOG.error("", fnfEx);
            fnfEx.printStackTrace();
        }
        return null;
    }
}
