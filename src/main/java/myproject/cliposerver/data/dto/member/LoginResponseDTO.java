package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String validateTime;

}
