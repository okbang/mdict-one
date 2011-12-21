package openones.stardictcore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
/**
 * this class is used for testing Dictionary class.
 * @author kien
 * @author LongNX
 * @author Thach Le
 */
public class StarDictTest {
    /*** prjPath, dictPaht used for test case xxx */
    String prjPathWin = "J:/FSoft/OOG/IDictionary/trunk/SourceCode/Stardict-Core/";
    String dictPathWin = prjPathWin + "src_ut/testdata/stardict-dictd-easton-2.4.2";

    /*** prjPath, dictPaht used for test case xxx02 */
    // String prjPath = "/media/Data1/FSoft/OOG/IDictionary/trunk/SourceCode/Stardict-Core/";
    String prjPath = prjPathWin;
    String dictPath = prjPath + "src_ut/testdata/stardict-dictd_viet-anh-2.4.2";

    /**
     * test look up word.
     */
    @Test
    public void testLookupWordString() {
        StarDict dict = new StarDict(dictPathWin);
        String actual = dict.lookupWord("Abagtha");
        String expected = "   one of the seven eunuchs in Ahasuerus's court (Esther 1:10;\n   2:21).";
        assertEquals(expected, actual);
    }
    /**
     * test get dictName.
     */
    @Test
    public void testGetDictName() {
        StarDict dict = new StarDict(dictPathWin);
        String actual = dict.getDictName();
        String expected = "Easton's 1897 Bible Dictionary";
        assertEquals("Result: ", expected, actual);
    }
    @Test
    public void testGetDictVersion() {
        StarDict dict = new StarDict(dictPathWin);
        String actual = dict.getDictVersion();
        String expected = "2.4.2";
        assertEquals(expected, actual);
    }
    @Test
    public void testGetWordByIndex() {
        StarDict dict = new StarDict(dictPathWin);
        String actual = dict.getWordByIndex(1000);
        String expected = "diana";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTotalWords() {
        StarDict dict = new StarDict(dictPathWin);
        int actual = dict.getTotalWords();
        int expected = 3843;
        assertEquals(expected, actual);
    }

    /**
     * test get dictName.
     */
    @Test
    public void testGetDictName02() {

        StarDict dict = new StarDict(dictPath);
        String actual = dict.getDictName();
        String expected = "Việt Anh";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWordByIndex02() {
        StarDict dict = new StarDict(dictPath);
        String actual = dict.getWordByIndex(1);
        String expected = "a";
        assertEquals(expected, actual);

        actual = dict.getWordByIndex(1000);
        expected = "bóc lột";
        assertEquals(expected, actual);

        actual = dict.getWordByIndex(2000);
        expected = "bộ cánh";
        assertEquals(expected, actual);
    }

}
