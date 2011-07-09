SVNLoader loads statistical data from the Subversion (SVN) repository to the relational database.

Current version: 0.0.1

How to run the demo from the source code?
=========================================
The demo version uses built-in database HSQL.

Step 1: Run script dist.bat to compile and make distribution. The binary package is generated at ./dist

Step 2: Start database by running the batch script "hsqldb-8.1\CodeProObserver\svnreport.bat" from binary package.
  Information to connect this database by Java:
    JDBC Driver: org.hsqldb.jdbcDriver
	JDBC URL: jdbc:hsqldb:hsql://localhost/svnreport
	JDBC User: sa
	JDBC Password: blank (no password)
  
Step 3: Check configuration of "bin/persistence.xml"
  Check the configuration of database (JDBC) to connect to the database HSQL in step 2

Step 4: Run bin/SVNLoader-4Project.bat

How to run the binary?
=========================================

How to run the Test Case?
=========================================

How to develop?
=========================================
- Eclipse
- Support tools:
  + Database client: Squirrel (http://squirrel-sql.sourceforge.net/)
  + HSQL: http://hsqldb.org/
  
Bugs/Changes tracking
==============================
