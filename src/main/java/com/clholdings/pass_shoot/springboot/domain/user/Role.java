package com.clholdings.pass_shoot.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

}
// 각 사용자의 권한을 관리할 Enum 클래스 Role 을생성합니다
// 스프링시큐리티에서는 권한코드에 항상 ROLE_이 앞에 있어야만합니다
// 그래서코드별 키값을 ROLE_GUEST, ROLE_USER 등으로지정합니다