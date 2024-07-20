package myproject.cliposerver.service.Oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.cliposerver.config.jwt.JwtTokenUtil;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LoginResponseDTO;
import myproject.cliposerver.data.dto.Oauth2.SocialLoginDTO;
import myproject.cliposerver.data.dto.Oauth2.SocialUserInfoDTO;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.enumerate.Role;
import myproject.cliposerver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleLoginService implements SocialLoginInter {
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirect_uri;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String client_secret;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String token_url;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String user_info_url;

    public ResponseDTO login(SocialLoginDTO socialLoginDTO) throws JsonProcessingException {
        //토큰 발급
        String accessToken = getAccessToken(socialLoginDTO);
        //유저 정보 수집
        SocialUserInfoDTO socialUserInfoDto = getUserInfo(accessToken);

        Optional<Member> existData = memberRepository.findByEmail(socialUserInfoDto.getEmail());

        String createToken = jwtUtil.createToken(socialUserInfoDto.getEmail(), Role.USER);
        String refreshToken = jwtUtil.createRefreshToken();

        Member member;
        if (existData.isEmpty()) {
            member = socialUserInfoDto.toEntity();
            member.changeToken(createToken, refreshToken);
            memberRepository.save(member);
        }
        else {
            existData.get().changeToken(createToken, refreshToken);
            memberRepository.save(existData.get());
        }

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .accessToken(createToken)
                .refreshToken(refreshToken)
                .validateTime(ZonedDateTime.now(ZoneId.of("UTC"))
                        .plusHours(1L)
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .build();

        // Map 을 사용하여 여러 데이터를 반환
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("loginResponse", responseDTO);
        responseBody.put("socialUserInfo", socialUserInfoDto);

        return ResponseDTO.builder()
                .message("로그인 완료")
                .body(responseBody)
                .build();
    }


    public String getAccessToken(SocialLoginDTO socialLoginDTO) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        String code = socialLoginDTO.getCode();

        // 시작 부분의 큰따옴표 제거
        if (code.startsWith("\"")) {
            code = code.substring(1);
        }

        // 끝 부분의 큰따옴표 제거
        if (code.endsWith("\"")) {
            code = code.substring(0, code.length() - 1);
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);
        body.add("client_secret", client_secret);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> tokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                token_url,
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        log.info(response.toString());

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SocialUserInfoDTO getUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> TokenRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                user_info_url,
                HttpMethod.POST,
                TokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String nickname = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String profileImage = jsonNode.get("picture").asText();

        log.info(nickname);
        log.info(email);
        log.info(profileImage);

        return new SocialUserInfoDTO(nickname, email, null, profileImage);
    }
}
