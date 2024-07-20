package myproject.cliposerver.data.dto.sms;

import lombok.Getter;

@Getter
public class SmsCertificationRequestDTO {
        private String phone;
        private String certificationNumber;
        private String email;
}
