package openones.svnloader;
import org.junit.Test;

import rocky.sizecounter.ISizeCounter;
import rocky.sizecounter.SizeCounterFactory;
import rocky.sizecounter.SourceMetaData;
import rocky.sizecounter.UnsupportedFileType;
import static org.junit.Assert.*;



public class TestCounter {
    @Test
    public void testGetDefaultInstance() {
        ISizeCounter counter = SizeCounterFactory.getDefaultInstance();
        SourceMetaData sizeMd;
try {
        // Count LOC of file "TestStepCounterImpl.java" in the current folder.
        // In the Utility eclipse project,the current folder is the project folder.
        //sizeMd = counter.countLOC("testdata/TestStepCounterImpl.java", ".java");
        sizeMd = counter.countLOC("testdata/HOSQL.cs", ".cs");
        assertNotNull(sizeMd);
        assertEquals(71, sizeMd.getComment());
        assertEquals(268, sizeMd.getLoc());
} catch (UnsupportedFileType e) {
        e.printStackTrace();
        fail(e.getMessage());
}
        
}
}
