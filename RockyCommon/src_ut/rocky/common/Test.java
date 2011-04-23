package rocky.common;


import junit.framework.Assert;
import junit.framework.TestCase;

public class Test extends TestCase {
    public void test01() {
        Employee intVal = new Employee();
        
        boolean isSimple = intVal.getClass().getName().startsWith("java.lang");
        Assert.assertEquals(true, isSimple);
    }
}
