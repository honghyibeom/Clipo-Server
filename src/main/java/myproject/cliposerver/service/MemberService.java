package myproject.cliposerver.service;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.jwt.JwtTokenUtil;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LoginRequestDTO;
import myproject.cliposerver.data.dto.member.LoginResponseDTO;
import myproject.cliposerver.data.dto.member.SignupRequestDTO;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MailService mailService;


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

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .validateTime(ZonedDateTime.now(ZoneId.of("UTC"))
                        .plusHours(1L)
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .build();

        return ResponseDTO.builder()
                .message("로그인 성공")
                .body(responseDTO)
                .build();
    }
    @Transactional
    public ResponseDTO forgotPassword(String email) throws Exception {
        Member member = getUser(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_USER));
        String code = mailService.sendSimpleMessage(member.getEmail());
        member.changePassword(code);
        memberRepository.save(member);

        return ResponseDTO.builder()
                .message("임시 비밀번호 발급")
                .build();
    }

    private Optional<Member> getUser(String email) {
        return memberRepository.findByEmail(email);
    }
}
