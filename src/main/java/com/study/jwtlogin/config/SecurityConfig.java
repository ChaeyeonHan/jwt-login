package com.study.jwtlogin.config;

import com.study.jwtlogin.jwt.JwtAccessDeniedHandler;
import com.study.jwtlogin.jwt.JwtAuthenticationEntryPoint;
import com.study.jwtlogin.jwt.JwtSecurityConfig;
import com.study.jwtlogin.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity  // 웹 보안 활성화(CSRF 공격 방지하는 기능을 제공)
@EnableMethodSecurity  // 보안 작동
//@EnableGlobalMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Spring Security 5.7.0부터 구성요소 기반 보안 설정으로 변경된다
    // WebSecurityConfigureAdapter가 deprecated 되었기에 기존처럼 configure 메소드 오버라이딩 해서 구현할 수 X
    // securityFilterChain을 bean으로 등록해 사용

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 인증 방식이기 때문에 csrf는 비활성화 상태로 둔다
                // rest-api를 이용한 서버라면, 세션 기반과는 다르게 stateless하기에 서버에 인증정보를 보관하지 않기에 필요하지 X
                .csrf().disable()

                // UsernamePasswordAuthenticationFilter 전에 corsFilter가 걸린다
                // 지정된 UsernamePasswordAuthenticationFilter 앞에 커스텀 필터를 추가
               .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // JWT 예외 처리 부분 -> 예외 상황에 대해 커스텀 적용
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 토큰을 사용하므로, 세션은 사용하지 않기에 STATELESS 설정
                // 스프링 시큐리티는 기본적으로 세션방식을 사용한다
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // antMatchers()로 특정 경로 지정, permitAll()은 모든 사용자가 접근 가능
                // 이 밖의 다른 요청들은 인증이 필요하다
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()  // Preflight Request 허용
                // Preflight Request : 실질적인 요청 전 OPTIONS 메소드를 통해 사전에 요청이 안전한지 확인하는 방법

                .antMatchers("/", "/h2/**", "/auth/**").permitAll()
                .anyRequest().authenticated()


                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }



    // WebSecurityCustomizer를 통해 Spring Security를 적용하지 않을 리소스를 설정해준다
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "/favicon.ico",
                        "/error");
    }
}
