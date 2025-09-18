package myproject.cliposerver.service.oAuth;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.jwt.JwtTokenUtil;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.auth.LoginRequestDTO;
import myproject.cliposerver.data.dto.auth.LoginResponseDTO;
import myproject.cliposerver.data.dto.auth.SignupRequestDTO;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.enumerate.DefaultProfile;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.MemberRepository;
import myproject.cliposerver.service.mail.MailServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MailServiceImpl mailService;


    @Transactional
    public ResponseDTO signup(SignupRequestDTO signupRequestDTO){
        if (getUser(signupRequestDTO.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EXIST_USER);
        }
        Member member = signupRequestDTO.toEntity();
        member.changePassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

       return ResponseDTO.builder()
               .message("핸드폰 인증이 필요합니다.")
               .build();
    }
    @Transactional
    public ResponseDTO guestLogin(SignupRequestDTO signupRequestDTO){
        if (getUser(signupRequestDTO.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EXIST_USER);
        }
        Member member = signupRequestDTO.toEntity();
        member.changePassword(passwordEncoder.encode(member.getPassword()));
        member.changeValidate(true);
        memberRepository.save(member);

        return login(LoginRequestDTO.builder().email(member.getEmail()).password(signupRequestDTO.getPassword()).build());
    }

    @Transactional
    public ResponseDTO login(LoginRequestDTO loginRequestDTO) {
       Member member = getUser(loginRequestDTO.getEmail())
               .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_USER));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.NOT_EQUALS_PASSWORD);
        }
        if (!member.getIsValidate()){
            throw new CustomException(ErrorCode.NOT_VALIDATE_USER);
        }

        String accessToken = jwtTokenUtil.createToken(member);
        String refreshToken = jwtTokenUtil.createRefreshToken();

        member.changeToken(accessToken, refreshToken);
        member.changeLastLoginAt(LocalDateTime.now());

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .validateTime(ZonedDateTime.now(ZoneId.of("UTC"))
                        .plusHours(1L)
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .email(member.getEmail())
                .build();

        return ResponseDTO.builder()
                .message("로그인 성공")
                .body(responseDTO)
                .build();
    }
    @Transactional
    public ResponseDTO forgetPassword(String phone) throws MessagingException, UnsupportedEncodingException {
        Member member = memberRepository.findByPhone(phone)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_USER));
        String code = mailService.sendSimpleMessage(member.getEmail());
        member.changePassword(code);
        memberRepository.save(member);

        return ResponseDTO.builder()
                .message("임시 비밀번호 발급")
                .build();
    }

    @Transactional
    public ResponseDTO recreateAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_VALIDATE_REFRESH_TOKEN));

        String accessToken = jwtTokenUtil.createToken(member);
        member.changeAccessToken(accessToken);

        return ResponseDTO.builder()
                .message("토큰 재발급 완료")
                .body(accessToken)
                .build();
    }

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시에 실행
    @Transactional
    public void deleteNotValidUsers() {
        memberRepository.deleteByIsValidate(false);
    }

    private Optional<Member> getUser(String email) {
        return memberRepository.findByEmail(email);
    }
}
