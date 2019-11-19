#!/bin/bash  
set -x
set -e

cmd="s?:1?:$2?"
echo cmd

msg=$( cat $1 | sed "$cmd" )
echo $msg

mosquitto_pub -h 127.0.0.1 -p 1883 -t "hzyw/001/gateway/1000-f82d132f9bb018ca-2001-ffff-d28a" -m "$msg" 

