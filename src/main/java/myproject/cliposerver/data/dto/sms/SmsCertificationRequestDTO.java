package myproject.cliposerver.data.dto.sms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsCertificationRequestDTO {
        @Schema(description = "사용자 번호", example = "01012345678")
        private String phone;
        @Schema(description = "인증번호", example = "0000")
        private String validateSMSCode;
        @Schema(description = "유저 이메일", example = "example@naver.com")
        private String email;
}
