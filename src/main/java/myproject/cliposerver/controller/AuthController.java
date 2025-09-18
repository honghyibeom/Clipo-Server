package myproject.cliposerver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.auth.LoginRequestDTO;
import myproject.cliposerver.data.dto.auth.PhoneNumberRequestDTO;
import myproject.cliposerver.data.dto.auth.SignupRequestDTO;
import myproject.cliposerver.data.dto.oAuth.SocialLoginDTO;
import myproject.cliposerver.data.dto.sms.SmsCertificationRequestDTO;
import myproject.cliposerver.service.oAuth.AuthService;
import myproject.cliposerver.service.oAuth.SocialLoginService;
import myproject.cliposerver.service.sms.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;
    private final Map<String, SocialLoginService> socialLoginInterMap;

    @Operation(summary = "회원 가입 api", description = "유저 정보를 저장")
    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody @Validated SignupRequestDTO userSignupRequestDTO) {
        return ResponseEntity.ok(authService.signup(userSignupRequestDTO));
    }

    @Operation(summary = "문자 전송 api", description = "회원가입 시 문자인증을 하기 위해 6자리 숫자 전송")
    @PostMapping("/auth/send/phone")
    public ResponseEntity<ResponseDTO> sendSms(@RequestBody PhoneNumberRequestDTO PhoneNumberRequestDTO) {
      return ResponseEntity.ok(smsService.sendSms(PhoneNumberRequestDTO));
    }

    @Operation(summary = "문자 인증 api", description = "문자 인증 전송")
    @PostMapping("/auth/send/verification")
    public ResponseEntity<ResponseDTO> SmsVerification(@RequestBody SmsCertificationRequestDTO smsCertificationRequestDTO) {
        return ResponseEntity.ok(smsService.verifySms(smsCertificationRequestDTO));
    }
    @Operation(summary = "로그인 api", description = "로그인")
    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Validated LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @Operation(summary = "비밀번호 재생성", description = "임시 비밀번호 발급")
    @PostMapping("/auth/recreatePassword/{phone}")
    public ResponseEntity<ResponseDTO> recreatePassword(@PathVariable("phone") String phone) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(authService.forgetPassword(phone));
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급, refreshToken 입력")
    @PostMapping("/auth/recreate/accessToken")
    public ResponseEntity<ResponseDTO> recreateAccessToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.recreateAccessToken(request));
    }

    @PostMapping("/auth/socialLogin")
    public ResponseEntity<ResponseDTO> socialLogin(@RequestBody SocialLoginDTO socialLoginDTO) throws JsonProcessingException {
        SocialLoginService socialLoginService = socialLoginInterMap.get(socialLoginDTO.getTypeOfPlatform());
        return ResponseEntity.ok(socialLoginService.login(socialLoginDTO));
    }

    @Operation(summary = "서버 연결", description = "서버연결")
    @PostMapping("/auth/wakeUp")
    public ResponseEntity<ResponseDTO> wakeUp() {
        return ResponseEntity.ok(ResponseDTO.builder().message("서버 일어남").build());
    }

    @Operation(summary = "게스트 로그인 api", description = "로그인")
    @PostMapping("/auth/guest/login")
    public ResponseEntity<ResponseDTO> guestLogin(@RequestBody @Validated SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(authService.guestLogin(signupRequestDTO));
    }

}
