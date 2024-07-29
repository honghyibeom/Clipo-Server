package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.enumerate.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class SignupRequestDTO {
    @Email(message = "do not match email form")
    @NotBlank(message = "email cannot be blank")
    private String email;
    private String password;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(null)
                .phone(null)
                .profileImage(null)
                .isValidate(false)
                .isSocial(false)
                .role(Role.USER)
                .build();
    }
}
