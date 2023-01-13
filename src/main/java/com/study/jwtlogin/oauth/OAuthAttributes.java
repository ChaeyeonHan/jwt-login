package com.study.jwtlogin.oauth;

import com.study.jwtlogin.domain.LoginType;
import com.study.jwtlogin.domain.Role;
import com.study.jwtlogin.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String registrationId;
    private String name;
    private String email;
//    private String picture;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        return ofKakao(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)  // sub
                .registrationId("google")
                .attributes(attributes).build();
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)  // 카카오는 지원하지 X
                .registrationId("kakao")
                .attributes(attributes).build();
    }

    public User toEntity(String registrationId) {
        if (registrationId.equals("google")) {  // 구글로 가입한 회원
            return User.builder()
                    .role(Role.ROLE_USER)
                    .loginType(LoginType.GOOLGE)
                    .email(email)
                    .activated(true)
                    .pw(null).build();
        }
        // 카카오로 가입한 회원
        return User.builder()
                .role(Role.ROLE_USER)
                .loginType(LoginType.KAKAO)
                .email(email)
                .activated(true)
                .pw(null).build();
    }


}
