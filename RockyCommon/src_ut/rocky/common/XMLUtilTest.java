package rocky.common;

import junit.framework.TestCase;

public class XMLUtilTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testToXML1() {
        Employee emp = new Employee();
        String xml;
        try {
            xml = XMLUtil.toXML(emp);
            System.out.println(xml);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public void testToXML2() {
        Employee[] emp = {new Employee() ,new Employee()};
        String xml;
        try {
            xml = XMLUtil.toXML(emp);
            System.out.println(xml);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
