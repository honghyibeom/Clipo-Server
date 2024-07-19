package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDTO {
    private String email;
    private String password;
}
