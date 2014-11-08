#!/bin/sh

# paths on the jenkins machine dslab-worf.epfl.ch
SDK_PATH="/home/jenkins/tools/android-sdk/"
PROJECT_PATH="/home/jenkins/workspace/2014-sweng-team-boh-domp/"

# config file specifying the android sdk path for gradle
PROPERTIES_FILE="${PROJECT_PATH}local.properties"

touch ${PROPERTIES_FILE}
echo "sdk.dir=${SDK_PATH}" > ${PROPERTIES_FILE}

exec "${PROJECT_PATH}gradlew" "connectedAndroidTest"

