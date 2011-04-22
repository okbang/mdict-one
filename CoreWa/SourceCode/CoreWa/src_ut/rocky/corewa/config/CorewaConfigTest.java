package rocky.corewa.config;

import java.io.FileNotFoundException;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;
import openones.corewa.config.ConfigUtil;
import openones.corewa.config.CoreWa;
import openones.corewa.config.Event;
import openones.corewa.config.Layout;
import openones.corewa.config.Part;
import openones.corewa.config.Screen;
import rocky.common.CommonUtil;

public class CorewaConfigTest extends TestCase {

    public void testParse01() {
        String file = "/conf/corewa.xml";
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

    public void testParse02() {
        String file = "/conf/corewa02.xml";
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
            
            Assert.assertNull(conf.getLayout());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }
    
    public void testParseLayout01() {
        String file = "/conf/sample_layout.xml";
        try {
            CoreWa conf = ConfigUtil.parse(CommonUtil.loadResource(file));
            Assert.assertEquals("Layout", conf.getHomeScreenId());
            
            Layout layout = conf.getLayout();

            Assert.assertEquals("Layout", layout.getId());
            Assert.assertEquals(6, layout.getPartMap().size());

            Part main = layout.getPartMap().get("main.do");
            
            Assert.assertEquals("main.do", main.getId());
            Assert.assertEquals("Main", main.getScreenId());

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }

    
}
