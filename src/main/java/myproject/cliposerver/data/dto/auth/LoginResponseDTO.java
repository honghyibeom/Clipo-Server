package myproject.cliposerver.data.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String validateTime;

}
