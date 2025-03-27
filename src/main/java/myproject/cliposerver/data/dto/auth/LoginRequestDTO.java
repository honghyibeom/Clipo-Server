package myproject.cliposerver.data.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDTO {
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;
    @Schema(description = "사용자 비밀번호", example = "securePassword123@")
    private String password;
}
