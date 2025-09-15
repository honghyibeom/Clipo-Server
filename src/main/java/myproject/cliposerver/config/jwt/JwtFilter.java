package myproject.cliposerver.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.CustomExceptionDTO;
import myproject.cliposerver.exception.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


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
            String status = jwtTokenUtil.validateToken(token);
            if (!status.equals("pass")) {
                switch (status) {
                    case "expire" -> {
                        returnErrorResponse(ErrorCode.EXPIRED_TOKEN, response);
                        return;
                    }
                    case "잘못된 JWT 서명입니다." -> {
                        returnErrorResponse(ErrorCode.INVALID_SIGNATURE_TOKEN, response);
                        return;
                    }
                    case "지원되지 않는 JWT 토큰입니다." -> {
                        returnErrorResponse(ErrorCode.UNSUPPORTED_TOKEN, response);
                        return;
                    }
                    case "JWT 토큰이 잘못되었습니다." -> {
                        returnErrorResponse(ErrorCode.MALFORMED_TOKEN, response);
                        return;
                    }
                }
                returnErrorResponse(ErrorCode.NOT_VALIDATE_TOKEN, response);
                return;
            }
            if (!request.getRequestURI().equals("/api/auth/recreate/accessToken")) {
                //token 이 검증이 완료 되었으면 권한 세팅을 하고(user정보를 token에서 받아온 이후 권한 설정.)
                Claims info = jwtTokenUtil.getUserInfoFromToken(token);
                try {
                    setAuthentication(info.getSubject(), token);
                } catch (Exception e) {
                    throw new CustomException(ErrorCode.FAIL_TO_CERTIFICATE);
                }
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

    public void returnErrorResponse(ErrorCode errorCode, HttpServletResponse response) {
        log.error("spring security context 설정중 에러 발생 errorMessage: {}", errorCode.getErrorMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        //status 는 errorCode 의 HttpStatus.val();
        response.setStatus(errorCode.getHttpStatus().value());
        //미디어 타입은 json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //인코딩 utf8
        response.setCharacterEncoding("utf-8");
        //errorMessage 를 "data" 값으로 하는 ResponseDto 객제 생성.
        CustomExceptionDTO responseDto = new CustomExceptionDTO(errorCode);
        //response 출력.
        try {
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
        } catch (IOException ioException) {
            log.error("servletResponse 사용중 에러 발생: {}", Arrays.toString(ioException.getStackTrace()));
        }
    }
}
