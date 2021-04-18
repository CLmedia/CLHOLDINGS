package com.clholdings.pass_shoot.springboot.config.auth;

import com.clholdings.pass_shoot.springboot.config.auth.dto.OAuthAttributes;
import com.clholdings.pass_shoot.springboot.config.auth.dto.SessionUser;
import com.clholdings.pass_shoot.springboot.domain.user.User;
import com.clholdings.pass_shoot.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

// 구글로그인이후 가져온 사용자의 정보(email, name, picture 등)들을 기반으로
// 가입및정보수정,세션 저장등의기능을지원합니다

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // registrationId
        // 현재 로그인진행중인서비스를구분하는코드입니다
        // 지금은구글만사용하는불필요한값이지만, 이후네이버로그인연동시에네이버로그인인지, 구글로그인인지구분하기위해사용합니다
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // userNameAttributeName
        // OAuth2 로그인진행시 키가 되는 필드값을 이야기합니다
        // Primary Key 와같은의미입니다
        // 구글의경우 기본적으로 코드를지원하지만, 네이버카카오등은 기본지원하지않습니다 구글의기본코드는 "sub"입니다
        // 이후 네이버로그인과구글로그인을 동시 지원할때 사용됩니다
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes
        // OAuth2UserService 를 통해 가져온 OAuth2User 의 attribute 를 담을클래스입니다
        // 이후네이버 등다른소셜로그인도이클래스를사용합니다
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // SessionUser
        // 세션에 사용자정보를저장하기위한 Dto 클래스입니다
        // 왜 User 클래스를쓰지않고 새로만들어서쓰는지 설명하겠습니다
        // 만약 User 클래스를 그대로 사용했다면 에러가발생합니다
        // Failed to convert from type [java.lang.Object] to type
        // [byte[]] for value 'com.jojoldu.book.springboot.domain.user.User@4a43d6'
        // 이는세션에저장하기 위해 User 클래스를 세션에 저장하려고 하니,
        // User 클래스에 직렬화를 구현하지 않았다는 의미의 에러입니다
        // 그럼 오류를 해결하기 위해 User 클래스에 직렬화 코드를 넣으면 될까요?
        // User 클래스가 엔티티이기 때문에 생각해볼것이많습니다
        // 엔티티 클래스에는 언제 다른 엔티티와 관계가 형성될지 모릅니다
        // 예를 들어 @OneToMany, @ManyToMany 등 자식 엔티티를갖고있다면 직렬화 대상에 자식들까지 포함되니
        // 성능이슈, 부수효과가 발생합니다. 그래서 직렬화 기능을 가진 세션 Dto 를 하나추가로 만드는것이 이후 운영
        // 및 유지보수 때 많은 도움이 됩니다
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
