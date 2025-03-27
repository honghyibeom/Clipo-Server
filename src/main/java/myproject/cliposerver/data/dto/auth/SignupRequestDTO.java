package myproject.cliposerver.data.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.enumerate.DefaultProfile;
import myproject.cliposerver.data.enumerate.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class SignupRequestDTO {
    @Email(message = "do not match email form")
    @NotBlank(message = "email cannot be blank")
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;
    @Schema(description = "사용자 비밀번호", example = "securePassword123@")
    private String password;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(null)
                .phone(null)
                .profileImage(DefaultProfile.getRandomProfileImage())
                .isValidate(false)
                .isSocial(false)
                .role(Role.USER)
                .build();
    }
}
