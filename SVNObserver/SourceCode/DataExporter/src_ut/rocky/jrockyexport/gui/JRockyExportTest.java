package rocky.jrockyexport.gui;

import java.util.List;

import rocky.jdbc.AliasConfig;
import rocky.jdbc.AliasInfo;
import rocky.util.ConfigUtil;
import junit.framework.Assert;
import junit.framework.TestCase;

public class JRockyExportTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLoadConfiguration() {
        JRockyExporter exporter = new JRockyExporter();
        
        AliasConfig aliasCfg = ConfigUtil.loadConfiguration("AliasConfig.xml");
        
        Assert.assertNotNull(aliasCfg);
        List<AliasInfo> listAliasInfos = aliasCfg.getListAliasInfo();
        
        assertNotNull(listAliasInfos);
        
        AliasInfo alias1 = listAliasInfos.get(0);
        assertEquals("DBAlias1", alias1.getAliasName());
    }

}
