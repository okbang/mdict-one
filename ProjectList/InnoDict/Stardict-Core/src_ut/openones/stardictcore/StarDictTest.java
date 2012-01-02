package openones.stardictcore;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;
/**
 * this class is used for testing Dictionary class.
 * @author kien
 * @author LongNX
 * @author Thach Le
 */
public class StarDictTest {
    /*** prjPath, dictPaht used for test case xxx */
    // String prjPathWin = "J:/OOG/IDictionary/trunk/SourceCode/Stardict-Core/";
    // String dictPathWin = prjPathWin + "src_ut/testdata/stardict-dictd-easton-2.4.2";

    /*** prjPath, dictPaht used for test case xxx02 */
    String prjPath = "/media/Data1/Projects/Open-OnesGroup/GoogleCode_RapidSVN/trunk/ProjectList/InnoDict/Stardict-Core/";
    // String prjPath = prjPathWin;
    String dictPathVE = prjPath + "src_ut/testdata/stardict-dictd_viet-anh-2.4.2";
    String dictEastonPath = prjPath + "src_ut/testdata/stardict-dictd-easton-2.4.2";

    /**
     * test look up word.
     */
    @Test
    public void testLookupWordString() {
        StarDict dict = new StarDict(dictEastonPath);
        String actual = dict.lookupWord("Abagtha");
        String expected = "   one of the seven eunuchs in Ahasuerus's court (Esther 1:10;\n   2:21).";
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWordVE02() {
        StarDict dict = StarDict.loadDict(dictPathVE);
        String actual = dict.lookupWord("đi");
        String expected = "@đi" + "\n" + "* verb" + "\n" + "- to go; to walk; to depart" + "\n"
                + "=đi đến một nơi nào+to go to a place" + "\n" + "-To lead; to march; to play" + "\n"
                + "=đường này đi đâu+Where does this road lead?";

        assertEquals(expected, actual);
    }
    /**
     * test get dictName.
     */
    @Test
    public void testGetDictName() {
        StarDict dict = new StarDict(dictEastonPath);
        String actual = dict.getDictName();
        String expected = "Easton's 1897 Bible Dictionary";
        assertEquals("Result: ", expected, actual);
    }

    @Test
    public void testGetDictName03() {
        StarDict dict = StarDict.loadDict(dictEastonPath);
        String actual = dict.getDictName();
        String expected = "Easton's 1897 Bible Dictionary";
        assertEquals("Result: ", expected, actual);
    }

    @Test
    public void testGetDictName04() {
        StarDict dict = StarDict.loadDict("/folder is not existed");
        Assert.assertNull(dict);
    }

    @Test
    public void testGetDictVersion() {
        StarDict dict = new StarDict(dictEastonPath);
        String actual = dict.getDictVersion();
        String expected = "2.4.2";
        assertEquals(expected, actual);
    }
    @Test
    public void testGetWordByIndex() {
        StarDict dict = new StarDict(dictEastonPath);
        String actual = dict.getWordByIndex(1000);
        String expected = "diana";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTotalWords() {
        StarDict dict = new StarDict(dictEastonPath);
        int actual = dict.getTotalWords();
        int expected = 3843;
        assertEquals(expected, actual);
    }

    /**
     * test get dictName.
     */
    @Test
    public void testGetDictName02() {

        StarDict dict = new StarDict(dictPathVE);
        String actual = dict.getDictName();
        String expected = "Việt Anh";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWordByIndex02() {
        StarDict dict = new StarDict(dictPathVE);
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
