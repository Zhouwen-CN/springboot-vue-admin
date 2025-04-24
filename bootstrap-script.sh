#!/bin/bash

set -euo pipefail
set -x
app_name='admin-backend-1.0.0-SNAPSHOT.jar'
active_profile='prod'

function start_app(){
    nohup java -jar ${app_name} --spring.profiles.active=${active_profile} > /dev/null 2>&1 &
    echo '------------ APP Start ------------'
}

function stop_app(){
    pid=$(pgrep -f ${app_name})
    if [[ -n ${pid} ]]; then
        kill -9 ${pid}
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