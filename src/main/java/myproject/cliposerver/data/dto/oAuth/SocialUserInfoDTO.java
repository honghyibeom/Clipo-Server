package myproject.cliposerver.data.dto.oAuth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.enumerate.DefaultBackGround;
import myproject.cliposerver.data.enumerate.DefaultProfile;
import myproject.cliposerver.data.enumerate.Role;

@Getter
@NoArgsConstructor
public class SocialUserInfoDTO {
    private String userNickname;
    private String email;
    private String mobile;
    private String profileImage;

    public SocialUserInfoDTO(String userNickname, String email, String mobile, String profileImage) {
        this.userNickname = userNickname;
        this.email = email;
        this.mobile = mobile;
        this.profileImage = profileImage;
    }

    public Member toEntity(SocialUserInfoDTO socialUserInfoDto){
        return Member.builder()
                .email(socialUserInfoDto.getEmail())
                .password(null)
                .profileImage(DefaultProfile.getRandomProfileImage())
                .backgroundImage(DefaultBackGround.getRandomBackGroundImage())
                .name(null)
                .phone(socialUserInfoDto.getMobile())
                .isValidate(true)
                .isSocial(true)
                .role(Role.USER)
                .build();
    }
}
