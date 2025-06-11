#! /bin/bash
export JAVA_PROGRAM_ARGS=`echo "$@"`
mvn exec:java -Dexec.mainClass="com.minimax.App" -Dexec.args="$JAVA_PROGRAM_ARGS"
