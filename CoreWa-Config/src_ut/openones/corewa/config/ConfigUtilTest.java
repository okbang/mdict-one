package openones.corewa.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import openones.corewa.config.Event.DispType;

import junit.framework.Assert;
import junit.framework.TestCase;
import rocky.common.CommonUtil;

public class ConfigUtilTest extends TestCase {

    String fileCfgPath = "/test-conf/mvcportlet-config.xml";
    String fileCfgPath1 = "/test-conf/mvcportlet-config1.xml";
    String fileCfgPath2 = "/test-conf/mvcportlet-config2.xml";
    String fileCfgPath3 = "/test-conf/userprofileportlet-config.xml";
    String fileCfgPath5 = "/test-conf/corewa.xml";

    InputStream fileCfgIs = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testParse0001CoreWa() {
        String file = "/test-conf/corewa.xml";
        try {
            CoreWa conf = ConfigUtil.parse(CommonUtil.loadResource(file));
            Map<String, Screen> screens = conf.getScreens();

            Assert.assertEquals(3, screens.size());

            Screen menuScr = screens.get("Menu");
            Assert.assertEquals("Menu", menuScr.getId());
            Assert.assertEquals("rocky.corewademo.control.MenuControl", menuScr.getCtrlClass());

            Map<String, Event> eventMap = menuScr.getEvents();
            Event addEvent = eventMap.get("Add");
            Assert.assertEquals("procAdd", addEvent.getProcId());

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }

    public void testParse0001MVCConfig() {
        String file = "/test-conf/mvc-config.xml";
        try {
            CoreWa conf = ConfigUtil.parse(CommonUtil.loadResource(file));
            Map<String, Screen> screens = conf.getScreens();

            Assert.assertEquals(3, screens.size());

            Screen screen = screens.get("c");
            Assert.assertEquals("c", screen.getId());

            Map<String, Event> eventMap = screen.getEvents();
            Assert.assertEquals(1, eventMap.size());
            
            Event event = eventMap.get("gotoa");
            Assert.assertEquals("clickAProc", event.getProcId());

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }
    public void testParse() {

        try {
            fileCfgIs = CommonUtil.loadResource(fileCfgPath);
        } catch (FileNotFoundException fnfEx) {
            fnfEx.printStackTrace();
            fail(fileCfgPath + " not found!");
            return;
        }
        CoreWa conf = ConfigUtil.parse(fileCfgIs);
        Map<String, Screen> screens = conf.getScreens();

        Assert.assertEquals(3, screens.size());

        Screen menuScr = screens.get("Menu");
        Assert.assertEquals("Menu", menuScr.getId());
        Assert.assertEquals("rocky.corewademo.control.MenuControl", menuScr.getCtrlClass());

        Map<String, Event> eventMap = menuScr.getEvents();
        Event addEvent = eventMap.get("Add");
        Assert.assertEquals("procAdd", addEvent.getProcId());

        Assert.assertEquals("Menu", conf.getViewScreen());
    }

    public void testParseMVCConfig02() {

        try {
            fileCfgIs = CommonUtil.loadResource("/test-conf/mvc-config02.xml");
        } catch (FileNotFoundException fnfEx) {
            fnfEx.printStackTrace();
            fail(fileCfgPath + " not found!");
            return;
        }
        CoreWa conf = ConfigUtil.parse(fileCfgIs);
        Map<String, Screen> screens = conf.getScreens();

        Assert.assertEquals(1, screens.size());

        Screen searchScr = screens.get("Search");
        Assert.assertEquals("Search", searchScr.getId());
        
        Map<String, Event> eventMap = searchScr.getEvents();
        Event searchEvent = eventMap.get("search");
        Assert.assertEquals("clickSearch", searchEvent.getProcId());
        Assert.assertEquals(DispType.INCLUDE, searchEvent.getDispType());

        Event addEvent = eventMap.get("add");
        Assert.assertEquals("clickAdd", addEvent.getProcId());
        Assert.assertEquals(DispType.FORWARD, addEvent.getDispType());
    }
    
    public void testParse1() {

        try {
            fileCfgIs = CommonUtil.loadResource(fileCfgPath1);
        } catch (FileNotFoundException fnfEx) {
            fnfEx.printStackTrace();
            fail(fileCfgPath + " not found!");
            return;
        }
        CoreWa conf = ConfigUtil.parse(fileCfgIs);
        Map<String, Screen> screens = conf.getScreens();

        Assert.assertEquals(3, screens.size());

        Screen menuScr = screens.get("Menu");
        Assert.assertEquals("Menu", menuScr.getId());
        Assert.assertEquals("rocky.corewademo.control.MenuControl", menuScr.getCtrlClass());

        Map<String, Event> eventMap = menuScr.getEvents();
        Event addEvent = eventMap.get("add");
        Assert.assertEquals("procAdd", addEvent.getProcId());
        Assert.assertEquals("/pages/add.jsp", addEvent.getNextScrId());

        Event listEvent = eventMap.get("list");
        // Assert.assertNull(listEvent.getProcId());
        Assert.assertEquals("", listEvent.getProcId());

        Screen listScr = screens.get("List");
        Assert.assertEquals("List", listScr.getId());
        Assert.assertEquals("rocky.corewademo.control.ListControl", listScr.getCtrlClass());
        Event nextEvent = listScr.getEvent("Next");
        assertNotNull(nextEvent);
        // assertNull(nextEvent.getNextScrId());
        assertEquals("", nextEvent.getNextScrId());

    }

    public void testParse2() {

        try {
            fileCfgIs = CommonUtil.loadResource(fileCfgPath2);
        } catch (FileNotFoundException fnfEx) {
            fnfEx.printStackTrace();
            fail(fileCfgPath + " not found!");
            return;
        }
        CoreWa conf = ConfigUtil.parse(fileCfgIs);
        Map<String, Screen> screens = conf.getScreens();

        Assert.assertEquals(3, screens.size());

        Screen menuScr = screens.get("Menu");
        Assert.assertEquals("Menu", menuScr.getId());
        // Assert.assertNull(menuScr.getCtrlClass());
        Assert.assertEquals("", menuScr.getCtrlClass());

        Map<String, Event> eventMap = menuScr.getEvents();
        Event addEvent = eventMap.get("add");
        Assert.assertEquals("procAdd", addEvent.getProcId());
        Assert.assertEquals("/WEB-INF/jsp/add.jsp", addEvent.getNextScrId());

        Event listEvent = eventMap.get("list");
        // Assert.assertNull(listEvent.getProcId());
        Assert.assertEquals("", listEvent.getProcId());

        Screen listScr = screens.get("List");
        Assert.assertEquals("List", listScr.getId());
        Assert.assertEquals("rocky.corewademo.control.ListControl", listScr.getCtrlClass());
        Event nextEvent = listScr.getEvent("next");
        assertNotNull(nextEvent);
        // assertNull(nextEvent.getNextScrId());
        assertEquals("", nextEvent.getNextScrId());

    }

    public void testParse3() {

        try {
            fileCfgIs = CommonUtil.loadResource(fileCfgPath3);
        } catch (FileNotFoundException fnfEx) {
            fnfEx.printStackTrace();
            fail(fileCfgPath + " not found!");
            return;
        }
        CoreWa conf = ConfigUtil.parse(fileCfgIs);
        Map<String, Screen> screens = conf.getScreens();

        Assert.assertEquals(3, screens.size());

        Screen addScr = screens.get("Add");
        Assert.assertEquals("Add", addScr.getId());
        Assert.assertEquals("rocky.mvcportlet.userprofile.control.AddControl", addScr.getCtrlClass());

        Map<String, Form> formBeanMap = conf.getForms();
        assertEquals(1, formBeanMap.size());
        Form addFormBean = formBeanMap.get("addForm");
        assertEquals("addForm", addFormBean.getId());
        assertEquals("rocky.mvcportlet.userprofile.form.AddForm", addFormBean.getClassName());

        Map<String, Event> eventMap = addScr.getEvents();
        Event addEvent = eventMap.get("save");
        Assert.assertEquals("procSave", addEvent.getProcId());
        Assert.assertEquals("/WEB-INF/userprofile/add.jsp", addEvent.getNextScrId());

        assertEquals("addForm", addEvent.getFormBean());

    }

    public void testParseLayout() {

        try {
            fileCfgIs = CommonUtil.loadResource("/test-conf/sample_layout.xml");
        } catch (FileNotFoundException fnfEx) {
            fnfEx.printStackTrace();
            fail(fileCfgPath + " not found!");
            return;
        }
        CoreWa conf = ConfigUtil.parse(fileCfgIs);
        Layout layout = conf.getLayout();

        Assert.assertEquals("Layout", layout.getId());
        Assert.assertEquals(6, layout.getPartMap().size());

        Part main = layout.getPartMap().get("main.do");
        
        Assert.assertEquals("main.do", main.getId());
        Assert.assertEquals("Main", main.getScreenId());
    }
}
