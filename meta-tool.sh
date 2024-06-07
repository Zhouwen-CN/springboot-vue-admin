#!/bin/bash

set -eu
app_name='meta-tool-app'

function start_app(){
    nohup java -jar ${app_name}*.jar > /dev/null 2>&1 &
    echo '------------ APP Start ------------'
}

function stop_app(){
    pid=$(ps -ef | grep -v grep | grep "${app_name}*" | awk '{print $2}')
    if [[ -n ${pid} ]]; then
        kill -9 $pid
        echo '------------- APP Stop -------------'
    fi
}

if [[ $# -ne 1 ]]; then
    echo 'Usage: [ start | stop | restart ]'
    exit 1
fi

case $1 in
'start' )
    start_app
;;
'stop' )
    stop_app
;;
'restart' )
    stop_app
    start_app
;;
* )
    echo 'Error Parameter!'
;;
esac