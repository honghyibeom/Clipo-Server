package myproject.cliposerver.data.dto.notification;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoteInfoResponseDTO {
    private String type;
    private String email;
    private Long bno;
    private Long rno;
    private Boolean isFollowing;
    private LocalDateTime createAt;
    private String boardOneImage;
    private String userProfileImage;
}
