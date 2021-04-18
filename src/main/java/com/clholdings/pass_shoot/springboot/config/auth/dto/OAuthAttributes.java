package com.clholdings.pass_shoot.springboot.config.auth.dto;

import com.clholdings.pass_shoot.springboot.domain.user.Role;
import com.clholdings.pass_shoot.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

// 스프링시큐리티 설정등록
// 구글로그인을등록하면서 대부분 코드가 확장성있게 작성되었다보니
// 네이버는쉽게 등록가능합니다.
// 네이버인지판단하는코드와 네이버생성자만 OAuthAttributes 에 추가해 주면 됩니다

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profileImage"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // User 엔티티를생성합니다
    // OAuthAttributes 에서 엔티티를생성하는시점은처음가입할때입니다
    // 가입할때의기본권한을 GUEST 로주기위해서 role 빌더값에는 Role.GUEST 를 사용합니다

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
