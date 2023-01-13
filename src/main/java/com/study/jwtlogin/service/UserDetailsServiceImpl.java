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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
    // 로그인 시 DB에서 유저정보를 가져오고. 이를 기반으로 Userdetails 객체 생성

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
//        System.out.println("====================loadUserByUsername================");
//        System.out.println(userRepository.findByEmail(email).get().getEmail());
        return userRepository.findByEmail(email)
                .map(user -> createUserDetails(email, user))
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 해당하는 유저를 찾을 수 없습니다."));

    }

    // DB에 해당 이메일로 가입한 유저가 있다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(String username, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }
//        System.out.println("createUserDetails 메서드 실행");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.getRole().value());
        grantedAuthorities.add(role);
//        System.out.println("권한 생성");

        try {
//            System.out.println("try안으로 들어옴.");
            org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);

//            System.out.println(user1.getUsername());
//            System.out.println(user1.getPassword());
//            System.out.println("UserDetails 객체 생성 완료");
            return user1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
