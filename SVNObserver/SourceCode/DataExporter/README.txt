This tool exports database into Excel file.

How to make a distribution?
==============================

How to run the source code?
==============================
The entry point is class "rocky.export.Rdb2Excel"
    

Changes tracking
==============================
Version 0.0.2
 + Add build.properties to provide properties: app.version, app.name
 + Support Post execution: execute a sql query after export data by
     Declare a property in ApplicationResources.properties
	    ; For HSQL, execute SHUTDOWN command to close the database
	    post.sql=SHUTDOWN