package myproject.cliposerver.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.jwt.JwtTokenUtil;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.*;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.MemberRepository;
import org.springframework.http.HttpHeaders;
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
    public ResponseDTO forgotPassword(String phone) throws Exception {
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
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_VALIDATE_TOKEN));

        String accessToken = jwtTokenUtil.createToken(member);
        member.changeAccessToken(accessToken);

        return ResponseDTO.builder()
                .message("토큰 재발급 완료")
                .body(accessToken)
                .build();
    }

    @Transactional
    public ResponseDTO updateProfileNickname(UpdateProfileNicknameRequestDTO profileNicknameRequestDTO,
                                             UserDetailsImpl userDetails){
        Optional<Member> optionalUser = memberRepository.findByName(profileNicknameRequestDTO.getNickName());
        if (optionalUser.isPresent()) {
            throw new CustomException(ErrorCode.EXIST_NICKNAME);
        }

        Member member = getUser(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
        member.changeProfileImage(profileNicknameRequestDTO.getProfileImage());
        member.changeName(profileNicknameRequestDTO.getNickName());

        return ResponseDTO.builder()
                .message("프로필 이미지 및 닉네임 변경 완료")
                .build();
    }

    @Transactional
    public ResponseDTO updatePassword(UpdatePasswordRequestDTO updatePasswordRequestDTO,
                                      UserDetailsImpl userDetails) {

        if (!passwordEncoder.matches(updatePasswordRequestDTO.getOldPassword(), userDetails.getMember().getPassword())) {
            throw new CustomException(ErrorCode.NOT_EQUALS_PASSWORD);
        }
        Member member = getUser(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        member.changePassword(passwordEncoder.encode(updatePasswordRequestDTO.getNewPassword()));
        memberRepository.save(member);
        return ResponseDTO.builder()
                .message("비밀번호 수정 완료")
                .build();
    }

    private Optional<Member> getUser(String email) {
        return memberRepository.findByEmail(email);
    }
}
