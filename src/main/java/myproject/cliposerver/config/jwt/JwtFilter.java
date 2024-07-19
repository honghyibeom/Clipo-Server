package myproject.cliposerver.config.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header 에서 토큰을 꺼냄
        String token = jwtTokenUtil.resolveToken(request);

        if (token != null) {
            if (!jwtTokenUtil.validateToken(token)) {
                throw new CustomException(ErrorCode.FAIL_TO_CERTIFICATE);
            }
            if (!request.getRequestURI().equals("/api/auth/recreate/accessToken")) {
                //token 이 검증이 완료 되었으면 권한 세팅을 하고(user정보를 token에서 받아온 이후 권한 설정.)
                Claims info = jwtTokenUtil.getUserInfoFromToken(token);
                setAuthentication(info.getSubject(), token);
            }
        }
        //원래 필터 체인으로 되돌리기
        filterChain.doFilter(request, response);

    }

    //jwtUtil에서 만든 권한을 security에 set.
    private void setAuthentication(String email, String accessToken) {
        try {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = jwtTokenUtil.createAuthentication(email, accessToken);
            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
        } catch (Exception e) {
            log.error("spring security context 설정중 에러 발생", e);
        }
    }
}
