package myproject.cliposerver.service.sms;

import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.auth.PhoneNumberRequestDTO;
import myproject.cliposerver.data.dto.sms.SmsCertificationRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SmsService {
    // 회원가입시 핸드폰 인증
    ResponseDTO verifySms(SmsCertificationRequestDTO requestDto);
    // 문자 보내기
    ResponseDTO sendSms(PhoneNumberRequestDTO PhoneNumberRequestDTO);
}
