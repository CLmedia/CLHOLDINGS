package com.clholdings.pass_shoot.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("profile")
    public String profile() {
        // env.getActiveProfiles()
        // 현재 실행중인 ActiveProfile을모두가져옵니다.
        // 즉,real,oauth,real-db 등이활성화되어있다면(active)3개가모두담겨있습니다.
        // 여기서 real,real1,real2는 모두배포에사용될 profile이라이중하나라도있으면 그값을반환하도록 합니다
        // 실제로 이번무중단 배포에서는 real1과real2만 사용되지만, step2를 다시 사용해 볼수도있으니 real도 남겨둡니다.
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        String defaultProfile = profiles.isEmpty()? "default" : profiles.get(0);

        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
