package myproject.cliposerver.data.dto.sms;

import lombok.Getter;

@Getter
public class SmsCertificationRequestDTO {
        private String phone;
        private String validateSMSCode;
        private String email;
}
