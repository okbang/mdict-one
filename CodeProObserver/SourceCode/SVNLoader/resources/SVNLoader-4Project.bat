@ECHO OFF
REM Get the current date with format YYYYMMDD
for /f "tokens=1,2,3,4 delims=/ " %%a in ('date /t') do set YYYYMMDD=%%d%%c%%b
set LOG=Log_%YYYYMMDD%.txt
SET TEMPATH=%TEMP%/SVNLoader
SET CLASSPATH=%CLASSSPATH%;../resources

SET URL=
SET USERNAME=
SET PASSWORD=
SET PROJECTCODE=
SET STARREV=-1
SET CMD=svnloader.bat "-url=%URL%" "-u=%USERNAME%" "-p=%PASSWORD%" "-t=%TEMPATH%" "-pcode=%PROJECTCODE%" "-start=%STARREV%"
ECHO Execute command: %CMD%
%CMD%


