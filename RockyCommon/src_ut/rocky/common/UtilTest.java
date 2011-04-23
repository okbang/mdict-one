package rocky.common;

import java.util.ArrayList;

import junit.framework.TestCase;

public class UtilTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIsPrimitive01() {

        assertTrue(Util.isPrimitive(true));
        assertTrue(Util.isPrimitive(1));
        assertTrue(Util.isPrimitive(1.0));
        assertTrue(Util.isPrimitive(new ArrayList()));
    }
    
    public void testIsPrimitive02() {

        assertEquals(false, Util.isPrimitive(new ArrayList()));
    }

}
