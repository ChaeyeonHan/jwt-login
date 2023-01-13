package com.study.jwtlogin.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class User extends BaseTimeEntity implements UserDetails, OAuth2User {
    // UserDetails : 스프링 시큐리티에서 사용자의 정보를 담는 인터페이스

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "pw")
    private String pw;

    @Column(name = "activated")
    private boolean activated;

    // OAuth2User //

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    // OAuth2User를 사용한 소셜 로그인시 사용하는 생성자


    // UserDetails 상속 //

    // 유저의 권한 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> {
            return getRole().toString();
        });
        return authorities;
    }

    // 유저의 패스워드 리턴
    @Override
    public String getPassword() {
        return this.getPw();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    // 계정이 만료되었는지
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    // 계정이 잠겼는지
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 계정 정보를 변경해야하는지
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 계정이 활성화 되어있는지
    @Override
    public boolean isEnabled() {
        // 휴면 계정으로 전환하는 규정과 같은 것들이 있을 때 사용
        // (현재 시간 - 마지막 로그인시간) => 1년 초과시 return false
        return false;
    }
}
