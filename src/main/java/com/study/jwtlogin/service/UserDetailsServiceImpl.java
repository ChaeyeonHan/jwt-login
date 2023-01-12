package com.study.jwtlogin.service;

import com.study.jwtlogin.domain.User;
import com.study.jwtlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
    // 로그인 시 DB에서 유저정보를 가져오고. 이를 기반으로 Userdetails 객체 생성

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        System.out.println("loadUserByUsername 실행되니??");
        return userRepository.findByEmail(email)
                .map(user -> createUserDetails(email, user))
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 해당하는 유저를 찾을 수 없습니다."));

    }

    // DB에 해당 이메일로 가입한 유저가 있다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(String email, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(email + " -> 활성화되어 있지 않은 유저입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.getRole().value());
        grantedAuthorities.add(role);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
