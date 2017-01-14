#!/bin/bash
host=$1
echo "running against host $host:9004"

servicesBridgeUrl="http://$host:9004/api/v1/health-check"

numCallsPerMinute=6
sleepTime=$[60/$numCallsPerMinute]

# Call health check endpoint
function callHealthCheck() {
i=0
  while [ 1 ]
  do
    echo "calling health-check endpoint.."
    curl -X GET $servicesBridgeUrl
    i=$[$i+1]
    sleep $sleepTime
  done
}

### MAIN CALLS ###
callHealthCheck
