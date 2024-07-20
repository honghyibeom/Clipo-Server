package myproject.cliposerver.data.dto.member;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfileNicknameRequestDTO {
    private String nickName;
    private String profileImage;
}
