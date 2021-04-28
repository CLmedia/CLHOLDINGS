#!/usr/bin/env bash

# 쉬고 있는 profile 찾기: real1이 사용 중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음
function find_idle_profile() {

  # $(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)
  # 현재 엔진엑스가 바라보고 있는 스프링 부트가 정상적으로 수행 중인지 확인합니다.
  # 응답값을 HttpStatus 로 받습니다.
  # 정상이면 200, 오류가발생한다면 400~503 사이로 발생하니 400 이상은 모두 예외로 보고 real2를현재 profile 로 사용합니다.
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ]; then # 400 보다크면(즉, 40x/50x에러모두포함)
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

  # IDLE_PROFILE
  # 엔진엑스와 연결되지않은 프로파일입니다
  # 스프링부트프로젝트를 이 프로파일로 연결하기위해반환합니다
    if [ ${CURRENT_PROFILE} == real1 ]; then
        IDLE_PROFILE=real2
    else
        IDLE_PROFILE=real1
    fi

  # echo "${IDLE_PROFILE}"
  # bash 라는 스크립트는 값을 반환하는 기능이 없습니다
  # 그래서 제일 마지막 줄에 echo 로 결과를출력 후, 클라이언트에서 그 값을 잡아서 ($(find_idle_profile)) 사용합니다.
  # 중간에 echo 를 사용해선안됩니다.
    echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile 의 port 찾기
function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]; then
        echo "8081"
    else
        echo "8082"
    fi
}