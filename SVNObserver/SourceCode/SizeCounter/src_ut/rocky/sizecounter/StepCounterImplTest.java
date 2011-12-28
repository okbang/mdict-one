package rocky.sizecounter;

import org.junit.Assert;
import org.junit.Test;

import tk.stepcounter.StepCounterImpl;

/*
 * method junittestcase
 * 
 */
public class StepCounterImplTest {

	@Test
	public void testIsCountable() {
		String path = "C:\\Users\\boyfree2712\\Desktop\\qwe.java";
    	StepCounterImpl impl = new StepCounterImpl();
        Assert.assertEquals(true, impl.isCountable(path)); 
	}
	@Test
	public void testIsCountablenameoffile() {
		String path = "https://rai-server/svn/iProjects/trunk/SizeCounter/.classpath";
    	StepCounterImpl impl = new StepCounterImpl();
        Assert.assertEquals(true,impl.isCountable(path)); 
	}
	@Test
	public void testIsCountabledotjava() {
		String path = "https://rai-server/svn/iProjects/trunk/SizeCounter/src_ut/tk/stepcounter/StepCounterImplTest.java";
    	StepCounterImpl impl = new StepCounterImpl();
        Assert.assertEquals(true,impl.isCountable(path)); 
	}
	@Test
	public void testIsCountablePowerPoint() {
		String path = "C:\\Users\\boyfree2712\\Desktop\\qwe.cpp";
    	StepCounterImpl impl = new StepCounterImpl();
        Assert.assertEquals(true,impl.isCountable(path)); 
	}
	

}
