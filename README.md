# akka-http-dispatcher-test

This repository serves a means of reproducing the issue outlined here: https://github.com/akka/akka-http/issues/722

To run:

* clone this project
* in the project root, build a docker image with `sbt docker`
* start the docker container with `docker run -p 9004:9004 -it spoolphiz.test/httpservice:latest /opt/services/bin/start-service.sh`
* open a new terminal and cd into the `bin` directory
* run the `health.sh` script to start making requests against the service: `./health.sh 192.168.99.100`
    * The first and only parameter accepted by health.sh is the host on which the service is running. If running docker natively replace `192.168.99.100` with `locahost`

An interesting observation is that the dispatcher thread id only increases in the manner described in github issue when the rate of requests is slow. Bumping the rate of requests to 120 per second no longer manifests the issue.
