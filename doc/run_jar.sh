#!/bin/bash

appName=kxmall-admin-0.0.1-RELEASE.jar

set -e

pid=`ps auxw | grep $appName | grep -v grep | awk '{print $2}'`

if ! [ -z "$pid" ]; then
  kill -n 9 $pid
fi

JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED -XX:+UseG1GC -Xlog:gc*:file=./gclogs:time,tags,level"

nohup java $JAVA_OPTS -jar $appName --spring.profiles.active=dev1>nohup.out 2>&1 &

tail -500f nohup.out
