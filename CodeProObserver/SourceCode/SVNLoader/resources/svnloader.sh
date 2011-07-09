#!/bin/bash

export CLASSPATH=$CLASSSPATH:../lib/*
java -Xmx1028M svn2rdb.ConsoleSVN2RDB $0 $1 $2 $3 $4 $5 $6
read -p "Press any key to continue"

exit 0
