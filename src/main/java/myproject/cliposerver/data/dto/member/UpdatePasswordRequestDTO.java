package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;
}
