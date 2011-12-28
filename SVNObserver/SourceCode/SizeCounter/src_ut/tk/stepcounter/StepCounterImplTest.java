package tk.stepcounter;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StepCounterImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIsCountable() {
        String extFiles = "C:\\Users\\boyfree2712\\Desktop\\qwe.cpp";
        StepCounterImpl impl = new StepCounterImpl();
        assertTrue(impl.isCountable(extFiles));
    }

    @Test
    public void testIsCountable02() {
        String extFiles = "qwe.cpp";
        StepCounterImpl impl = new StepCounterImpl();
        assertTrue(impl.isCountable(extFiles));
    }

    @Test
    public void testIsCountable03() {
        String extFiles = ".cpp";
        StepCounterImpl impl = new StepCounterImpl();
        assertTrue (impl.isCountable(extFiles));
    }
    
    @Test
    public void testIsCountable04() {
        String extFiles = "cpp";
        StepCounterImpl impl = new StepCounterImpl();
        assertTrue (impl.isCountable(extFiles));
    }
    
    @Test
    public void testIsCountable05() {
        String extFiles = ".c";
        StepCounterImpl impl = new StepCounterImpl();
        assertTrue (impl.isCountable(extFiles));
    }
    
    @Test
    public void testIsCountable06() {
        StepCounterImpl impl = new StepCounterImpl();
        assertFalse(impl.isCountable(".pp"));
        assertFalse(impl.isCountable(".ava"));
        
        assertFalse(impl.isCountable("pp"));
        assertFalse(impl.isCountable("ava"));
    }

    @Test
    public void testIsCountable07() {
        StepCounterImpl impl = new StepCounterImpl();
        assertFalse(impl.isCountable("cpp h"));
    }
}
