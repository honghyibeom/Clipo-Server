package myproject.cliposerver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.auth.LoginRequestDTO;
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

    @Operation(summary = "회원 가입", description = "유저 정보를 저장")
    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody @Validated SignupRequestDTO userSignupRequestDTO) {
        return ResponseEntity.ok(authService.signup(userSignupRequestDTO));
    }

    @PostMapping("/auth/send/phone")
    public ResponseEntity<ResponseDTO> sendSms(@RequestBody SmsCertificationRequestDTO smsCertificationRequestDTO) {
      return ResponseEntity.ok(smsService.sendSms(smsCertificationRequestDTO));
    }

    @PostMapping("/auth/send/verification")
    public ResponseEntity<ResponseDTO> SmsVerification(@RequestBody SmsCertificationRequestDTO smsCertificationRequestDTO) {
        return ResponseEntity.ok(smsService.verifySms(smsCertificationRequestDTO));
    }
    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Validated LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/auth/recreatePassword/{phone}")
    public ResponseEntity<ResponseDTO> recreatePassword(@PathVariable("phone") String phone) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(authService.forgetPassword(phone));
    }

    @PostMapping("/auth/recreate/accessToken")
    public ResponseEntity<ResponseDTO> recreateAccessToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.recreateAccessToken(request));
    }

    @PostMapping("/auth/socialLogin")
    public ResponseEntity<ResponseDTO> socialLogin(@RequestBody SocialLoginDTO socialLoginDTO) throws JsonProcessingException {
        SocialLoginService socialLoginService = socialLoginInterMap.get(socialLoginDTO.getTypeOfPlatform());
        return ResponseEntity.ok(socialLoginService.login(socialLoginDTO));
    }

}
