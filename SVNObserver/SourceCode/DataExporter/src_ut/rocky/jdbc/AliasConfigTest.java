package rocky.jdbc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import rocky.sql.RockySql;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import junit.framework.TestCase;

public class AliasConfigTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetListAliasInfo() {
        //final String outPath = "D:/FSOFT/RAI/InitialTraining/DEV/InitialTraining/Utility/JRockyReport/Wip/Project/JRockyExport/AliasConfig.xml";
        final String outPath = "AliasConfig.xml";
        XStream streamer = new XStream(new DomDriver());
        AliasConfig aliasConf = new AliasConfig();
        
        List<AliasInfo> listAliases = new ArrayList<AliasInfo>();
        
        AliasInfo ai;
        for (int i = 0; i < 1; i++) {
            ai = new AliasInfo();
            ai.setAliasName("CodeReportCenter");
            ai.setDatabaseName("JRockyReporter");
            ai.setPassword("");
            //ai.setPort(-1);
            ai.setServerName("localhost");
            ai.setServerType(RockySql.ST_HSQL);
            ai.setUserName("sa");
            listAliases.add(ai);
        }
        aliasConf.setListAliasInfo(listAliases);
        
        try {
            streamer.toXML(aliasConf, new FileOutputStream(outPath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }

}
