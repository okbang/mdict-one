SVNLoader loads statistical data from the Subversion (SVN) repository to the relational database.

Current version: 0.1.0

How to make the binary package and run the demo from the source code?
=========================================
The demo version uses built-in database HSQL.

Step 1: Run script dist.bat to compile and make distribution. The binary package is generated at ./dist/svnloader-x.y.z (This folder is called $SVNLOADER_HOME)

Step 2: Start database by change current folder into "$SVNLOADER_HOME\bin" and running the batch script "start-hsql.bat".
  Information to connect this database by Java:
    JDBC Driver: org.hsqldb.jdbcDriver
	JDBC URL: jdbc:hsqldb:hsql://localhost/svnreport
	JDBC User: sa
	JDBC Password: blank (no password)
  
Step 3: Check the SVN configuration and database configuration 
  + Check the configuration of database (JDBC) in file "bin/persistence.xml" to connect to the database HSQL  which is mentioned in step 2
  + Fill the arguments for SVN: URL, Username, Password
  + Fill the value for property PROJECTCODE. This project alias will be named for the URL SVN in the database.

Step 4: Run by change current folder into $SVNLOADER_HOME/bin and running script SVNLoader-4Project.bat

How to run the binary?
=========================================

How to run the Test Case?
=========================================

How to develop?
=========================================
- Eclipse. Refer http://code.google.com/p/open-ones/wiki/PrepareIDEEclipse
- Support tools:
  + Database client: Squirrel (http://squirrel-sql.sourceforge.net/)
  + HSQL: http://hsqldb.org/
  A built-in HSQL is in folder "hsqldb-8.1/svnreport".
Step 1) Start the database HSQL by execute $SVNLOADER_SOURCE/hsqldb-8.1/svnreport/StartDB.bat (Working folder is $SVNLOADER_SOURCE/hsqldb-8.1/svnreport)
Step 2) Import the project into the Eclipse, revise the database configuration in /SVNLoader/resources/META-INF/persistence.xml
Step 3) Explore the feature of SVNLoader by execute the test case "testTransfer0002_GoogleCode" in /SVNLoader/src_ut/openones/svnloader/engine/SVNLoaderBizTest.java
Step 4) Start HSQL client by execute "SVNLoader/hsqldb-8.1/svnreport/runManager.bat" to explore the database.
  Input information for connection:
   Setting Name: svnreport
   Type: HSQL Database Engine Standalone
   Driver: keep value "org.hsqldb.jdbcDriver"
   URL: jdbc:hsqldb:hsql://localhost/svnreport
   User: sa
   Password: <blank>

Bugs/Changes tracking
==============================
