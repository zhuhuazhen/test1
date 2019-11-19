#!/bin/bash  
set -x
set -e

msg=$(cat $1)
echo $msg

mosquitto_pub -h 127.0.0.1 -p 1883 -t "hzyw/001/gateway/1000-f82d132f9bb018ca-2001-ffff-d28a" -m "$msg" 

