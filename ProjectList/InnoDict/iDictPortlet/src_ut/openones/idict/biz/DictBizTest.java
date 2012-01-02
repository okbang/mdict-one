package openones.idict.biz;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import openones.idict.portlet.form.DictInfo;
import openones.stardictcore.StarDict;

import org.junit.Test;

public class DictBizTest {

    @Test
    public void testGetDictFolders() {
        DictBiz dictBiz = new DictBiz(
                "/media/Data1/FSoft/OOG/IDictionary/trunk/SourceCode/Stardict-Core/src_ut/testdata");

        List<String> folders = dictBiz.getDictFolders();

        assertEquals(3, folders.size());
        assertEquals("stardict-dictd-easton-2.4.2", folders.get(1));
    }

    @Test
    public void testGetDicts() {
        DictBiz dictBiz = new DictBiz(
                "/media/Data1/FSoft/OOG/IDictionary/trunk/SourceCode/Stardict-Core/src_ut/testdata");

        List<StarDict> dicts = dictBiz.getDicts();

        assertEquals(2, dicts.size());

        StarDict dict0 = dicts.get(0);
        StarDict dict1 = dicts.get(1);

        assertEquals("Easton's 1897 Bible Dictionary", dict0.getDictName());
        assertEquals("Việt Anh", dict1.getDictName());
    }

    @Test
    public void testGetDictInfo() {
        DictBiz dictBiz = new DictBiz(
                "/media/Data1/FSoft/OOG/IDictionary/trunk/SourceCode/Stardict-Core/src_ut/testdata");

        Collection<DictInfo> dicts = dictBiz.getDictInfoList();

        assertEquals(2, dicts.size());

        Iterator<DictInfo> it = dicts.iterator();

        DictInfo dict0 = it.next();
        DictInfo dict1 = it.next();

        assertEquals("0", dict0.getCd());
        assertEquals("Easton's 1897 Bible Dictionary", dict0.getName());

        assertEquals("1", dict1.getCd());
        assertEquals("Việt Anh", dict1.getName());
    }

}
