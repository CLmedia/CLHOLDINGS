package com.clholdings.pass_shoot.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Target(ElementType.PARAMETER)
// 이어노테이션이 생성될수있는 위치를 지정합니다
// PARAMETER 로지정했으니 메소드의 파라미터로 선언된 객체에서만 사용할수있습니다
// 이외에도 클래스 선언문에 쓸수있는 TYPE 등이있습니다
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
    // @interface
    // 이 파일을 어노테이션 클래스로 지정합니다
    // LoginUser 라는이름을 가진 어노테이션이 생성되었다고보면됩니다
}
