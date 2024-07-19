package myproject.cliposerver.service;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.SmsCertificationRequestDTO;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.MemberRepository;
import myproject.cliposerver.repository.SmsCertificationDao;
import myproject.cliposerver.util.SmsUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;
    private final MemberRepository memberRepository;

    public ResponseDTO sendSms(SmsCertificationRequestDTO requestDto){
        String to = requestDto.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsUtil.sendOne(to, certificationNumber);
        smsCertificationDao.createSmsCertification(to,certificationNumber);

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
        memberRepository.save(member);

        return ResponseDTO.builder()
                .message("검증 완료! 회원가입 완료되었습니다.")
                .build();
    }

    public boolean isVerify(SmsCertificationRequestDTO requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getCertificationNumber()));
    }
}
