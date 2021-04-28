#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    # echo 'set \$service_url http://127.0.0.1:${IDLE_PORT};'
    # 하나의 문장을 만들어 파이프라인(|)으로 넘겨주기 위해 echo 를사용합니다.
    # 엔진엑스가 변경할 프록시 주소를 생성합니다.
    # 홑따옴표(')를 사용해야 합니다.
    # 사용하지 않으면 $service_url 을 그대로 인식하지 못하고 변수를 찾게 됩니다.

    # |sudo tee /etc/nginx/conf.d/service-url.inc
    # 앞에서 넘겨준 문장을 service-url.inc 에 덮어씁니다.
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> 엔진엑스 Reload"
    # sudo service nginx reload
    # 엔진엑스설정을다시불러옵니다
    # restart 와는다릅니다
    # restart 는 잠시 끊기는 현상이있지만, reload 는 끊김없이 다시 불러옵니다.
    # 다만, 중요한설정들은 반영되지않으므로, restart 를 사용해야 합니다.
    # 여기선 외부의 설정 파일인 service-url 을 다시 불러오는 거라 reload 로 가능합니다.
    sudo service nginx reload
}

