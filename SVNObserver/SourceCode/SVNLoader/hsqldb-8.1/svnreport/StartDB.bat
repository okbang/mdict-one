@ECHO OFF
cd data
java -cp ../../lib/hsqldb.jar org.hsqldb.Server -database.0 file:svnreport -dbname.0 svnreport