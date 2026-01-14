package myproject.cliposerver.service.sms;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.jwt.JwtTokenUtil;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.auth.LoginResponseDTO;
import myproject.cliposerver.data.dto.auth.PhoneNumberRequestDTO;
import myproject.cliposerver.data.dto.sms.SmsCertificationRequestDTO;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.MemberRepository;
import myproject.cliposerver.repository.SmsCertificationDao;
import myproject.cliposerver.util.SmsUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final SmsUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public ResponseDTO sendSms(PhoneNumberRequestDTO PhoneNumberRequestDTO) {
        String phoneNumber = PhoneNumberRequestDTO.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendOne(phoneNumber, certificationNumber);
        smsCertificationDao.createSmsCertification(phoneNumber,certificationNumber);

        return ResponseDTO.builder()
                .message("메세지 발송!")
                .build();
    }

    public ResponseDTO verifySms(SmsCertificationRequestDTO requestDto) {
        if (isVerify(requestDto)) {
            throw new CustomException(ErrorCode.SMS_CERTIFICATION_NUMBER_MISMATCH);
        }
        smsCertificationDao.removeSmsCertification(requestDto.getPhone());

        Member member = memberRepository.findByEmail(requestDto.getEmail())
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        member.changeValidate(true);
        member.changePhone(requestDto.getPhone());
        memberRepository.save(member);

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
                .message("SMS Authentic Complete")
                .body(responseDTO)
                .build();
    }

    public boolean isVerify(SmsCertificationRequestDTO requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getValidateSMSCode()));
    }
}
