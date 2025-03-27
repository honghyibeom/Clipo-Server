package myproject.cliposerver.data.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhoneNumberRequestDTO {
    @Schema(description = "사용자 번호", example = "01012345678")
    private String phoneNumber;
}
