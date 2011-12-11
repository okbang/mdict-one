package rocky.util;

import rocky.jdbc.AliasConfig;
import rocky.jdbc.AliasInfo;
import rocky.sql.RockySql;
import junit.framework.TestCase;

public class ConfigUtilTest extends TestCase {

	public void testLoadConfiguration() {
		String fileCfgPath = "/AliasConfig02.xml";
		AliasConfig aCfg = ConfigUtil.loadConfiguration(fileCfgPath);
		
		assertNotNull(aCfg);
		AliasInfo aInfo = aCfg.getAlias(0);
		
		assertEquals("CodeReportCenter", aInfo.getAliasName());
		assertEquals(1433, aInfo.getPort().intValue());
	}

	public void testLoadConfiguration02() {
		String fileCfgPath = "/AliasConfig02.xml";
		AliasConfig aliasCfg = ConfigUtil.loadConfiguration(fileCfgPath);
		
		assertNotNull(aliasCfg);
		AliasInfo aInfo = aliasCfg.getAlias(0);
		
		assertEquals("CodeReportCenter", aInfo.getAliasName());
		assertEquals(1433, aInfo.getPort().intValue());
		
		
	    AliasInfo aliasInfo = aliasCfg.getAliasByName("CodeReportCenter");
	    
	    String jdbcUrlPattern = RockySql.getJdbcUrlPattern(aliasInfo.getServerType());
	    assertEquals("jdbc:sqlserver://$serverName:$port;databaseName=$databaseName", jdbcUrlPattern);
	    
	    String jdbcDriver = RockySql.getJdbcDriver(aliasInfo.getServerType());
	    assertEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver", jdbcDriver);
	    
	    String jdbcUrl = RockySql.buildJdbcUrl(jdbcUrlPattern, aliasInfo.getServerName(), aliasInfo.getPort(), aliasInfo.getDatabaseName());
	    assertEquals("jdbc:sqlserver://rai-server:1433;databaseName=DrCenter", jdbcUrl);
	}
	
	   public void testLoadConfiguration03() {
	        String fileCfgPath = "/prognews/AliasConfigProgNews.xml";
	        AliasConfig aliasCfg = ConfigUtil.loadConfiguration(fileCfgPath);
	        
	        assertNotNull(aliasCfg);
	        AliasInfo aInfo = aliasCfg.getAlias(1);
	        
	        assertEquals("SVNReport", aInfo.getAliasName());
	        
	        
	        AliasInfo aliasInfo = aliasCfg.getAliasByName("SVNReport");
	        
	        String jdbcUrlPattern = RockySql.getJdbcUrlPattern(aliasInfo.getServerType());
	        assertEquals("jdbc:hsqldb:hsql://$serverName/$databaseName", jdbcUrlPattern);
	        
	        String jdbcDriver = RockySql.getJdbcDriver(aliasInfo.getServerType());
	        assertEquals("org.hsqldb.jdbcDriver", jdbcDriver);
	        
	        String jdbcUrl = RockySql.buildJdbcUrl(jdbcUrlPattern, aliasInfo.getServerName(), aliasInfo.getPort(), aliasInfo.getDatabaseName());
	        assertEquals("jdbc:hsqldb:hsql://localhost/svnreport", jdbcUrl);
	    }
}
