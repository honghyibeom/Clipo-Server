package myproject.cliposerver.data.dto.follow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FollowInfoResponseDTO {
    private String email;
    private String nickName;
    private String profilePicture;
    private Boolean isFollowing;
}
