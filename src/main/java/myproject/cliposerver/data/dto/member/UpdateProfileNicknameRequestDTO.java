package myproject.cliposerver.data.dto.member;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UpdateProfileNicknameRequestDTO {
    private String username;
    private MultipartFile profileImage;
}
