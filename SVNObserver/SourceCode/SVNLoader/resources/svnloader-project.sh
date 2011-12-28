#!/bin/sh

set +v
# Get the current date with format YYYYMMDD
export YYYYMMDD=$(date +"%Y")$(date +"%m")$(date +"%d")
export LOG=Log_$YYYYMMDD
export TEMPATH=./TMP/SVN2RDB
export CLASSPATH=$CLASSSPATH:../conf:../lib/*
export STARREV=-1

export URL=https://rai-server/svn/iDictionary/trunk/SourceCode/IDictionary_Android
export USERNAME=fsofter
export PASSWORD=fsofter12345
export PROJECTCODE=iDictionary

echo Execute command: _svnloader.sh "-url=$URL" "-u=$USERNAME" "-p=$PASSWORD" -t="$TEMPATH" "-pcode=$PROJECTCODE" "-start=$STARREV"

./_svnloader.sh "-url=$URL" "-u=$USERNAME" "-p=$PASSWORD" "-t=$TEMPATH" "-pcode=$PROJECTCODE" "-start=$STARREV"

exit 0
