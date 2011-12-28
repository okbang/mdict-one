package openones.svnloader.engine;

import static org.junit.Assert.*;

import openones.svnloader.engine.SVNUtility;

import org.junit.Assert;
import org.junit.Test;

public class SVNUtilityTest {

    @Test
    public void testBuildSVNRepository() {
        fail("Not yet implemented");
    }

    @Test
    public void testReplace() {
        fail("Not yet implemented");
    }

    @Test
    public void testChangeToRelativePath() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetSubStringFromEnd() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteDir() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeletePath() {
        fail("Not yet implemented");
    }

    /**
     */
    @Test
    public void testTrimCharAtEnd() {
        String path = "http://www.thefreedictionary.com/latest////////";
        String result = SVNUtility.trimCharAtEnd(path, '/');
        System.out.println(result);
        Assert.assertEquals("http://www.thefreedictionary.com/latest", result);        
    }

}
