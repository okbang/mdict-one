ECHO Export for project
SET OUTPUT_FOLDER=../output
REM Report for project
SET SQL_PROPERTY=/conf/ExportProgNewsHSQL.properties
SET REPORT_FILE=CodeReport_${CURRENT_DATE.yyyyMMddHHmmss}.xls
CALL JRockyExport.bat "-conf=/conf/AliasConfigProgNews.xml" "-a=SVNReport" "-r=%SQL_PROPERTY%" "-t=/templates/Template_CodeReport01.xls" "-o=%OUTPUT_FOLDER%/%REPORT_FILE%"