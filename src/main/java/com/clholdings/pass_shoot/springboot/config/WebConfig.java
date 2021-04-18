package com.clholdings.pass_shoot.springboot.config;

import com.clholdings.pass_shoot.springboot.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    // HandlerMethodArgumentResolver 는항상 WebMvcConfigurer 의 addArgumentResolvers() 를통해 추가해야합니다
    // 다른 HandlerMethodArgumentResolver 가 필요하다면 같은 방식으로추가해주면됩니다
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }
}
