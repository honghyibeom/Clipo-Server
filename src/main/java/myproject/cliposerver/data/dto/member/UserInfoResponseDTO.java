package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponseDTO {
    private String email;
    private String nickName;
    private String profilePicture;
    private Boolean following;
}
