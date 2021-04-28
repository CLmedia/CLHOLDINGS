#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
# 여기서도 IDLE_PROFILE 을사용하니 profile.sh 을 가져와야합니다.
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ubuntu/app/step3
PROJECT_NAME=pass_shoot

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."
# 기본적인스크립트는 step2 의 deploy.sh 와유사합니다.
# 다른점이라면 IDLE_PROFILE 을 통해 properties 파일을 가져오고(application-$IDLE_PROFILE.properties),active profile 을지정하는것
# (-Dspring.profiles.active=$IDLE_PROFILE) 뿐입니다.
nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties,/home/ubuntu/app/application-oauth.properties,/home/ubuntu/app/application-real-db.properties \
    -Dspring.profiles.active=$IDLE_PROFILE \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
