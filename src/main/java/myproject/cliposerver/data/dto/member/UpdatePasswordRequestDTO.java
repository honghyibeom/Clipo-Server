package myproject.cliposerver.data.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePasswordRequestDTO {
    @Schema(description = "기존 비밀번호 입력",example = "비밀번호123@")
    private String oldPassword;
    @Schema(description = "새 비밀번호 입력",example = "비밀번호123#")
    private String newPassword;
}
