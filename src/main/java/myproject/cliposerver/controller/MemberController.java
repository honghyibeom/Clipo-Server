package myproject.cliposerver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LoginRequestDTO;
import myproject.cliposerver.data.dto.member.SignupRequestDTO;
import myproject.cliposerver.data.dto.member.SmsCertificationRequestDTO;
import myproject.cliposerver.service.MemberService;
import myproject.cliposerver.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final SmsService smsService;

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
}
