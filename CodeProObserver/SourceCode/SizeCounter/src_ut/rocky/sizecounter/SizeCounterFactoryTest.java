package rocky.sizecounter;

import junit.framework.TestCase;

public class SizeCounterFactoryTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetDefaultInstance() {
		ISizeCounter counter = SizeCounterFactory.getDefaultInstance();
		SourceMetaData sizeMd;
        try {
	        // Count LOC of file "TestStepCounterImpl.java" in the current folder.
        	// In the Utility eclipse project,the current folder is the project folder.
        	sizeMd = counter.countLOC("testdata/TestStepCounterImpl.java", ".java");
	        assertNotNull(sizeMd);
	        assertEquals(0, sizeMd.getComment());
	        assertEquals(59, sizeMd.getLoc());
        } catch (UnsupportedFileType e) {
	        e.printStackTrace();
	        fail(e.getMessage());
        }
		
	}

}
