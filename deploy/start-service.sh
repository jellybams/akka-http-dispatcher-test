#!/bin/bash

# the "~APP_VER~" portion of the command gets sed-ed
# with the current build version of the artifact
# see <prohect root>/project/Build.scala for details
java -Xms200m -Xmx400m \
    -Dorg.slf4j.simpleLogger.defaultLogLevel=debug \
    -cp /opt/services/akka-http-dispatcher-test-assembly-~APP_VER~.jar \
     me.spoolphiz.service.Main
