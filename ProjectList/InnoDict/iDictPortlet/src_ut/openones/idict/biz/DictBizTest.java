package openones.idict.biz;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import openones.idict.portlet.form.DictInfo;
import openones.stardictcore.StarDict;

import org.junit.Test;

public class DictBizTest {

    static String dictRepo;
    static {
        File file = new File(".");
        try {
            dictRepo = file.getCanonicalPath() + "/src_ut/dict-repo";
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        System.out.println("Testing dictionary repository:" + dictRepo);
    }

    @Test
    public void testGetDictFolders() {

        DictBiz dictBiz = new DictBiz(dictRepo);

        List<String> folders = dictBiz.getDictFolders();

        assertEquals(2, folders.size());
        assertEquals("stardict-dictd_viet-anh-2.4.2", folders.get(1));
    }

    @Test
    public void testGetDicts() {
        DictBiz dictBiz = new DictBiz(dictRepo);

        Collection<StarDict> dicts = dictBiz.getDicts();
        Iterator<StarDict> it = dicts.iterator();

        assertEquals(2, dicts.size());

        StarDict dict0 = it.next();
        StarDict dict1 = it.next();

        assertEquals("Anh Việt", dict0.getDictName());
        assertEquals("Việt Anh", dict1.getDictName());
    }

    @Test
    public void testGetDictInfo() {
        DictBiz dictBiz = new DictBiz(dictRepo);

        Collection<DictInfo> dicts = dictBiz.getDictInfoList();

        assertEquals(2, dicts.size());

        Iterator<DictInfo> it = dicts.iterator();

        DictInfo dict0 = it.next();
        DictInfo dict1 = it.next();

        assertEquals("0", dict0.getCd());
        assertEquals("Anh Việt", dict0.getName());

        assertEquals("1", dict1.getCd());
        assertEquals("Việt Anh", dict1.getName());
    }

    @Test
    public void testGetMeaning() {
        DictBiz dictBiz = new DictBiz(dictRepo);

        Collection<DictInfo> dicts = dictBiz.getMeaning("đi");

        assertEquals(2, dicts.size());

        Iterator<DictInfo> it = dicts.iterator();

        DictInfo dict0 = it.next();
        DictInfo dict1 = it.next();

        assertEquals("0", dict0.getCd());
        assertEquals("Anh Việt", dict0.getName());
        assertEquals("not found", dict0.getMeaning());

        assertEquals("1", dict1.getCd());
        assertEquals("Việt Anh", dict1.getName());
        String expected = "@đi" + "\n" + "* verb" + "\n" + "- to go; to walk; to depart" + "\n"
                + "=đi đến một nơi nào+to go to a place" + "\n" + "-To lead; to march; to play" + "\n"
                + "=đường này đi đâu+Where does this road lead?";
        assertEquals(expected, dict1.getMeaning());
    }
}
