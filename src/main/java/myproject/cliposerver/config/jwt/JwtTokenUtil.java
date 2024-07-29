package myproject.cliposerver.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.config.security.UserDetailsServiceImpl;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.enumerate.Role;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {
    private final UserDetailsServiceImpl userDetailsService;
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private final SecretKey secretKey;

    public JwtTokenUtil(@Value("${jwt.secret.key}") String secretKey, UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    //JWT Token 발급
    public String createToken(Member member) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        //"sub" 값에 id 값이 들어감.
                        .subject(member.getEmail())
                        .expiration(new Date(date.getTime() + 60 * 60 * 1000L))
                        //"Authorization" 칸 안에 role 값이 들어감 (https://jwt.io/) 에서 토큰 decode 가능.
                        .claim(AUTHORIZATION_KEY, member.getRole())
                        .issuedAt(date)
                        .signWith(secretKey)
                        .compact();
    }

    //Refresh Token 생성
    public String createRefreshToken() {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        //토큰 타임 설정
                        .expiration(new Date(date.getTime() + 60 * 24 * 60 * 60 * 1000L))
                        .issuedAt(date)
                        .signWith(secretKey)
                        .compact();
    }

    //토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    //토큰정보 parsing 메서드
    public Claims getUserInfoFromToken(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    //사용자의 인증 정보를 생성
    public Authentication createAuthentication(String email, String accessToken) {
        // 주어진 이메일을 사용하여 사용자 세부 정보를 로드
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(email);

        // 액세스 토큰이 유효한지 확인
        if (!userDetails.getMember().getAccessToken().equals(BEARER_PREFIX + accessToken)) {
            // 토큰이 유효하지 않은 경우 오류 로그를 기록하고 CustomException을 발생시킴
            ErrorCode errorCode = ErrorCode.ALREADY_CHANGED_TOKEN;
            log.error(errorCode.getErrorMessage());
            throw new CustomException(errorCode);
        }

        // 유효한 토큰인 경우, Authentication 객체를 생성하여 반환
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //HTTP 요청에서 JWT 토큰을 추출하는 역할을 합니다.
    //클라이언트가 서버에 요청을 보낼 때 HTTP 헤더에 포함된 인증 정보를 파싱하여 실제 토큰을 반환합니다.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
