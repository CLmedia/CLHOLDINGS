package com.clholdings.pass_shoot.springboot.config.auth;

import com.clholdings.pass_shoot.springboot.config.auth.LoginUser;
import com.clholdings.pass_shoot.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    //supportsParameter()
    //컨트롤러 메서드의 특정 파라미터를 지원하는지 판단합니다
    //여기서는 파라미터에 @LoginUser 어노테이션이 붙어있고, 파라미터 클래스타입이 SessionUser.class 인경우 true 를반환합니다
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    //resolveArgument()
    //파라미터에 전달할 객체를 생성합니다
    //여기서는 세션에서 객체를 가져옵니다
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws Exception {
        return httpSession.getAttribute("user");
    }
}

// @LoginUser 를사용하기위한 환경을 구성합니다
