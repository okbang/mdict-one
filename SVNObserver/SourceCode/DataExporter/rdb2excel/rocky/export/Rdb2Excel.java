package rocky.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.w3c.dom.Document;

import rocky.common.CommonUtil;
import rocky.common.LogService;
import rocky.common.PropertiesManager;
import rocky.common.XMLUtil;
import rocky.jdbc.AliasConfig;
import rocky.jdbc.AliasInfo;
import rocky.jdbcwrapper.DaoService;
import rocky.sql.RockySql;
import rocky.util.ConfigUtil;

public class Rdb2Excel {
    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPasswd;
    static final private int START_ROW = 0;
    static final private int START_COL = 0;
    
    public Rdb2Excel(String jdbcDriver, String jdbcUrl, String jdbcUser, String jdbcPasswd) {
        this.jdbcDriver = jdbcDriver;
        this.jdbcUrl = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPasswd = jdbcPasswd;
    }
    
    /**
     * The entry point of Report Engine.
     * 
     * @param args The format of arguments is: [-conf=<Configuration File Path>] -a=<alias> -r=<ResourceBaseName> -t=<TemplateFilePath> -o=<output file>
     *    <Configuration File Path>: the xml configuration file of databases. The "aliasName" is the identifier of the database.
     *    <alias>: selected alias
     *    <ResourceBaseName>: the resource property which define reports, query to get data
     *    <TemplateFilePath>: Excel template file to contain report
     *    <output file>: output report
     */
    public static void main(String[] args) {
    	
    	final String AT_CONFTYPE = "-conftype";
        final String AT_CONF = "-conf";
        final String AT_ALIAS = "-a";
        final String AT_RESOURCE = "-r";
        final String AT_TEMPLATE = "-t";
        final String AT_OUTPUT = "-o";
        
        String confType = "alias"; // value of confType: alias | jpa
        String configFilePath = "/persistence.xml"; // Default configuration
                                                    // file
        String alias = null;
        String resourceBaseName = null;
        String templateFilePath = null;
        String outputFilePath = null;
        
        boolean isValidArg = true;

        // Parsing the arguments
        if (args != null && args.length > 0) {

            String[] argValue;
            String key;
            String value;
            for (int i = 0; (i < args.length) && isValidArg; i++) {
                argValue = args[i].split("=");                
                if (argValue.length == 2) {
                    key = argValue[0];
                    value = argValue[1];
                    if (AT_CONF.equals(key)) {
                        // Get the configuration file in the next argument
                        configFilePath = value;
                    } else if (AT_ALIAS.equals(key)) {
                        alias = value;
                    } else if (AT_RESOURCE.equals(key)) {
                        resourceBaseName = value;
                    } else if (AT_TEMPLATE.equals(key)) {
                        templateFilePath = value;
                    } else if (AT_OUTPUT.equals(key)) {
                        outputFilePath = CommonUtil.formatPattern(value);
                    } else if (AT_CONFTYPE.equals(key)) {
                    	confType = value;
                    } else {
                        isValidArg = false;
                    }
                } else {
                    isValidArg = false;
                }
            }
        }
        if (isValidArg) {
			Rdb2Excel exporter = null;
			String jdbcDriver;
			String jdbcUrl;
			String jdbcUsername;
			String jdbcPassword;
			// Parse the application configuration
			if (confType.equalsIgnoreCase("jpa")) {
				InputStream is;
				try {
					is = CommonUtil.loadResource(configFilePath);
					Document doc = XMLUtil.parse(is);

					XPathFactory xpf = XPathFactory.newInstance();
					XPath xp = xpf.newXPath();

					// Lookup jdbc driver
					jdbcDriver = xp.evaluate(
							"//persistence-unit/properties/property[@name=\"javax.persistence.jdbc.driver\"]/@value", doc);
					
					jdbcUrl = xp.evaluate(
							"//persistence-unit/properties/property[@name=\"javax.persistence.jdbc.url\"]/@value", doc);
					
					jdbcUsername = xp.evaluate(
							"//persistence-unit/properties/property[@name=\"javax.persistence.jdbc.user\"]/@value", doc);
					
					jdbcPassword = xp.evaluate(
							"//persistence-unit/properties/property[@name=\"javax.persistence.jdbc.password\"]/@value", doc);
					exporter = new Rdb2Excel(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
					exporter.process("", resourceBaseName, templateFilePath, outputFilePath);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else { // alias configuration
				AliasConfig aliasCfg = ConfigUtil.loadConfiguration(configFilePath);
				AliasInfo aliasInfo = aliasCfg.getAliasByName(alias);
				String jdbcUrlPattern = RockySql.getJdbcUrlPattern(aliasInfo.getServerType());
				jdbcDriver = RockySql.getJdbcDriver(aliasInfo.getServerType());
				jdbcUrl = RockySql.buildJdbcUrl(jdbcUrlPattern, aliasInfo.getServerName(), aliasInfo.getPort(),
						aliasInfo.getDatabaseName());
				exporter = new Rdb2Excel(jdbcDriver, jdbcUrl, aliasInfo.getUserName(), aliasInfo.getPassword());								
				exporter.process(alias, resourceBaseName, templateFilePath, outputFilePath);
			}
            //exporter.process(alias, resourceBaseName, templateFilePath, outputFilePath);
        } else {
            // Invalid arguments
            usage();
            System.exit(1);

        }
    }
    
    private static void usage() {
        System.out.println("Rdb2Excel [-conftype= alias | jpa][-conf=<Configuration File Path>] -a=<alias> -r=<ResourceBaseName> -t=<TemplateFilePath> -o=<output file>");
    }
    
    public void process(String alias, String resourceBaseName, String templateFilePath, String outputFilePath) {
        RockySql expEngine;
        
        try {
            expEngine = new RockySql(new PropertiesManager(resourceBaseName).getProperties());
            
            // Parse SQL Configuration
            List<ReportItemConfigurationInfo> rptItemCfgInfoList = SqlConfigurationUtil.parse(resourceBaseName);
            HSSFWorkbook workbook = new HSSFWorkbook(CommonUtil.loadResource(templateFilePath));
            for (ReportItemConfigurationInfo rptItemCfgInfo : rptItemCfgInfoList) {
                export(rptItemCfgInfo.getQuery(), null, workbook , rptItemCfgInfo.getName(), outputFilePath, START_ROW, START_COL);
            }
            
//            expEngine.getCombineSql("");
        } catch (InvalidPropertiesFormatException ipfex) {
            LogService.logError(ipfex);
        } catch (IOException ioex) {
            LogService.logError(ioex);
        }

    }
    /**
     * Export data of the query to the output stream.
     * @param sqlTemplate input
     * @param sqlValueMap input
     * @param os output
     * @param outFilePaht Excel output file
     * @return
     */
    public int export(String sqlTemplate, Map<String, Object> sqlValueMap, HSSFWorkbook workbook, String sheetName,
            String outFilePath, int startRow, int startCol) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        OutputStream os = null;
        
        try {
            DaoService dao = new DaoService(jdbcDriver, jdbcUrl, jdbcUser, jdbcPasswd);
            conn = dao.getConnection();
            pstmt = DaoService.buildSQL(conn, sqlTemplate, sqlValueMap);
            rs = pstmt.executeQuery();            
            
            os = new FileOutputStream(outFilePath);
            
            exportToExcel(rs, workbook, os, sheetName, startRow, startCol);
        } catch (ClassNotFoundException cnfex) {
            LogService.logDebug(cnfex);
        } catch (SQLException sqlex) {
            LogService.logDebug(sqlex);
        } catch (FileNotFoundException fnfex) {
            LogService.logDebug(fnfex);
        } catch (IOException ioex) {
            LogService.logDebug(ioex);
        } finally {
            DaoService.close(rs);
            DaoService.close(pstmt);
            DaoService.close(conn);

            CommonUtil.close(os);
        }
        
        return 0;
    }
    
    private void exportToExcel(ResultSet rs, HSSFWorkbook workbook, OutputStream os, String sheetName, int startRow,
            int startCol) throws SQLException, IOException {
        ResultSetMetaData rsMD = rs.getMetaData();
        

        // Write the header
        int nCol = rsMD.getColumnCount();
        HSSFSheet sheet = workbook.getSheet(sheetName);
        
        if (sheet == null) {
            workbook.createSheet(sheetName);
        }

        HSSFRow row = HSSFCellUtil.getRow(startRow, sheet);
        HSSFCell cell;
        int colIdx;
        for (colIdx = 1; colIdx <= nCol; colIdx++) {
            cell = HSSFCellUtil.getCell(row, startCol + colIdx - 1);
            cell.setCellValue(new HSSFRichTextString(rsMD.getColumnName(colIdx)));
        }
        // Write the result set
        int rowPhyIdx = startRow + 1;
        java.sql.Date sqlDte;
        java.sql.Timestamp sqlTime;
        while (rs.next()) {
            // Write the record
            for (colIdx = 1; colIdx <= nCol; colIdx++) {
                row = HSSFCellUtil.getRow(rowPhyIdx, sheet);
                cell = HSSFCellUtil.getCell(row, startCol + colIdx - 1  );

                // Write cell by type of value
                switch (rsMD.getColumnType(colIdx)) {
                case Types.INTEGER:
                case Types.BIGINT:
                case Types.NUMERIC:
                    cell.setCellValue(rs.getInt(colIdx));
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.NVARCHAR:
                    cell.setCellValue(new HSSFRichTextString(rs.getString(colIdx)));
                    break;
                case Types.DATE:
                    sqlDte = rs.getDate(colIdx);
                    cell.setCellValue(new Date(sqlDte.getTime()));
                    break;
                case Types.TIMESTAMP:
                    sqlTime = rs.getTimestamp(colIdx);
                    cell.setCellValue(new Date(sqlTime.getTime()));
                    break;
                case Types.DECIMAL:
                case Types.FLOAT:
                case Types.DOUBLE:
                    cell.setCellValue(rs.getDouble(colIdx));
                    break;
                    
                }
            }

            rowPhyIdx++;
        }
        workbook.write(os);
    }
}
