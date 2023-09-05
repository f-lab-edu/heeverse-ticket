#!/bin/bash

BASE_PATH='/home/ubuntu/workspace/heeverse-ticket'
BUILD_JAR=$(ls $BASE_PATH/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo ">>> build 파일명: $JAR_NAME" >> $BASE_PATH/deploy.log

echo ">>> build 파일 복사" >> $BASE_PATH/deploy.log
DEPLOY_PATH=$BASE_PATH
cp $BUILD_JAR $DEPLOY_PATH

echo ">>> 현재 실행중인 애플리케이션 pid 확인" >> $BASE_PATH/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> $BASE_PATH/deploy.log
else
  echo ">>> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH/$JAR_NAME
echo ">>> DEPLOY_JAR 배포"    >> $BASE_PATH/deploy.log
nohup java -jar \
        -Dspring.profiles.active=dev \
        $DEPLOY_JAR >> $BASE_PATH/deploy.log 2>$BASE_PATH/deploy_err.log &