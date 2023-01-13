package com.study.jwtlogin.oauth;

import com.study.jwtlogin.domain.User;
import com.study.jwtlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service  // 소셜로그인 구분, 소셜로그인 후 회원가입등의 후처리의 기능
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    // oauth2로 로그인시 loadUser 메서드가 실행된다
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("ClientRegistration : " + userRequest.getClientRegistration()); // ClientRegistration 정보
        System.out.println("AccessToken : " + userRequest.getAccessToken().getTokenValue());  // accessToken 가져오기

        // DefaultOAuth2UserService는 OAuth2UserService의 구현체
        // 해당 클래스를 통해 userRequest의 정보를 가져올 수 있다
       OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 서비스 구분(구글, 카카오, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행시 키가 되는 필드 값 (PK). 구글은 sub라는 필드가 유니크 필드, 네이버는 id 필드가 유니크 필드이다.
        // 소셜 서비스들간에 유니크 필드명이 다르기에, 필드명을 찾아 userNameAttributeName에 저장
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        // attributes.getAttributes() : {sub=XXX, name=XXX, given_name=XX(이름), family_name=X(성), picture=XXX, email=XXXX@gmail.com, email_verified=true, locale=ko}

        User user = saveUser(attributes);

        return (OAuth2User) new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), null);

    }

    private User saveUser(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .orElse(attributes.toEntity(attributes.getRegistrationId()));

        return userRepository.save(user);
    }


}
