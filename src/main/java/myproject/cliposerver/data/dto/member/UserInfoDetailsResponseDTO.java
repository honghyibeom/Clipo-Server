package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDetailsResponseDTO {
    private String email;
    private String nickName;
    private String profilePicture;
    private String backgroundPicture;
    private String location;
    private String description;
    private Long followingNumber;
    private Long followerNumber;
    private String brithDay;


}
