package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LittleUserInfoResponseDTO {
    private String email;
    private String nickName;
    private String profilePicture;
    private Boolean isFollowing;
}
