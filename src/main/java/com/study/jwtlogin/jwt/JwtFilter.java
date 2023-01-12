package com.study.jwtlogin.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
//    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    // 필터링 로직 수행 부분. 토큰의 인증 정보를 SecurityContext 안에 저장하는 역할을 수행!!
    // Request Header에서 Access Token을 꺼내고, 유효성 검사 뒤, 토큰의 인증 정보를 SecurityContext에 저장하는 역할
    // 가입, 로그인, 재발급을 제외한 모든 Request는 이 필터를 거치기 때문에, 토큰이 없거나 유효하지 않으면 정상수행되지 않는다.
    // 해당 단계가 정상적으로 수행되었다면 SecuriyContext에 유저 정보인(email)이 존재한다는 것이 보장된다
    // * DB에서 조회한 것이 아니라 Access Token에 있는 인증정보(email)를 가져온 것이기에, 탈퇴로 인해 회원이 존재하지 않는 경우 or 해당 사용자에 대한 다른 정보 조회는 따로 처리 필요!
   @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

//       // Request Header에서 JWT 토큰 추출하기
//       String token = resolveToken((HttpServletRequest) request);
//
//       String requestURI = ((HttpServletRequest) request).getRequestURI();
//
//       // 토큰 유효성 검사
//       if (token != null && tokenProvider.validateToken(token)) {
//
//           // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
//           Authentication authentication = tokenProvider.getAuthentication(token);
//
//           // 해당 유저 정보를 시큐리티 컨텍스트에 저장(디비 거치지 X)
//           SecurityContextHolder.getContext().setAuthentication(authentication);
//           log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
//       } else {
//           log.debug("유효한 토큰이 없습니다, uri: {}", requestURI);
//       }

       chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request){

       String bearerToken = request.getHeader(JwtProperties.AUTHORIZATION_HEADER);

       if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.BEARER_PREFIX)) {
           return bearerToken.substring(7);
       }
       return null;

    }
}
