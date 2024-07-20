package myproject.cliposerver.data.dto.Oauth2;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginDTO {
    private String code;
    private String typeOfPlatform;


}
