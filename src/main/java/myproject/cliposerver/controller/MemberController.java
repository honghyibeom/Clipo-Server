package myproject.cliposerver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LoginRequestDTO;
import myproject.cliposerver.data.dto.member.SignupRequestDTO;
import myproject.cliposerver.data.dto.oAuth.SocialLoginDTO;
import myproject.cliposerver.data.dto.member.UpdatePasswordRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateProfileNicknameRequestDTO;
import myproject.cliposerver.data.dto.sms.SmsCertificationRequestDTO;
import myproject.cliposerver.service.MemberService;
import myproject.cliposerver.service.oAuth.SocialLoginInter;
import myproject.cliposerver.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final SmsService smsService;
    private final Map<String, SocialLoginInter> socialLoginInterMap;

    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody @Validated SignupRequestDTO userSignupRequestDTO) {
        return ResponseEntity.ok(memberService.signup(userSignupRequestDTO));
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
        return ResponseEntity.ok(memberService.login(loginRequestDTO));
    }

    @PostMapping("/auth/recreatePassword/{email}")
    public ResponseEntity<ResponseDTO> recreatePassword(@PathVariable("email") String email) throws Exception {
        return ResponseEntity.ok(memberService.forgotPassword(email));
    }

    @PostMapping("/auth/recreate/accessToken")
    public ResponseEntity<ResponseDTO> recreateAccessToken(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.recreateAccessToken(request));
    }

    @PostMapping("/auth/socialLogin")
    public ResponseEntity<ResponseDTO> socialLogin(@RequestBody SocialLoginDTO socialLoginDTO) throws JsonProcessingException {
        SocialLoginInter socialLoginService = socialLoginInterMap.get(socialLoginDTO.getTypeOfPlatform());
        return ResponseEntity.ok(socialLoginService.login(socialLoginDTO));
    }

    @PostMapping("/update/profileNickname")
    public ResponseEntity<ResponseDTO> updateProfileNickname(@RequestBody UpdateProfileNicknameRequestDTO updateProfileRequestDTO,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updateProfileNickname(updateProfileRequestDTO, userDetails));
    }

    @PostMapping("/update/password")
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody UpdatePasswordRequestDTO updatePasswordRequestDTO,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updatePassword(updatePasswordRequestDTO, userDetails));
    }
}
