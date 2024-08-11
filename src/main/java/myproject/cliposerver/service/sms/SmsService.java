package myproject.cliposerver.service.sms;

import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.sms.SmsCertificationRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SmsService {
    ResponseDTO verifySms(SmsCertificationRequestDTO requestDto);
    ResponseDTO sendSms(SmsCertificationRequestDTO requestDto);
}
