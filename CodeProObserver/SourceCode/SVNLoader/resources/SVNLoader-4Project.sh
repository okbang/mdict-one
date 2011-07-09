#!/bin/sh

set +v
# Get the current date with format YYYYMMDD
export YYYYMMDD=$(date +"%Y")$(date +"%m")$(date +"%d")
export LOG=Log_$YYYYMMDD

export CLASSPATH=$CLASSSPATH:../conf:../lib/*
export URL=https://rai-server/svn/iDictionary/trunk/SourceCode/IDictionary_Android
export USERNAME=fsofter
export PASSWORD=fsofter12345
export TEMPATH=./TMP/SVN2RDB
export PROJECTCODE=iDictionary
export STARREV=-1
echo Execute command: SVN2RDB.sh "-url=$URL" "-u=$USERNAME" "-p=$PASSWORD" -t="$TEMPATH" "-pcode=$PROJECTCODE" "-start=$STARREV"

./SVN2RDB.sh "-url=$URL" "-u=$USERNAME" "-p=$PASSWORD" "-t=$TEMPATH" "-pcode=$PROJECTCODE" "-start=$STARREV"

exit 0
