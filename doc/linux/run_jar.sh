#!/bin/bash

appName=kxmall-admin.jar

set -e

pid=`ps auxw | grep $appName | grep -v grep | awk '{print $2}'`

if ! [ -z "$pid" ]; then
  kill -n 9 $pid
fi

nohup java -XX:SurvivorRatio=8 -XX:+UseParallelGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./gclogs -jar $appName >nohup.out 2>&1 &

tail -500f nohup.out
