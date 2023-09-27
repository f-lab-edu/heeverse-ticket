#!/bin/sh


# log 경로
LOG_PATH=./logs
mkdir -p $LOG_PATH

# jar 경로
BUILD_JAR=$(ls ./*.jar)
JAR_NAME=$(basename $BUILD_JAR)

# scouter 경로
SCOUTER_BASE_PATH=./scouter
mkdir -p $SCOUTER_BASE_PATH

DEPLOY_JAR=$JAR_NAME
echo ">>> DEPLOY_JAR 배포 시작"    >> $LOG_PATH/deploy.log
echo ">>> DEPLOY_JAR :: $DEPLOY_JAR "  >> $LOG_PATH/deploy.log

nohup java -jar \
        -Dspring.profiles.active=${PROFILE} \
        -javaagent:$SCOUTER_BASE_PATH/scouter/agent.java/scouter.agent.jar \
        -Dscouter.config=$SCOUTER_BASE_PATH/scouter/agent.java/conf/scouter.conf \
        -Dadd-opensjava.base/java.lang=ALL-UNNAMED \
        "$DEPLOY_JAR"
