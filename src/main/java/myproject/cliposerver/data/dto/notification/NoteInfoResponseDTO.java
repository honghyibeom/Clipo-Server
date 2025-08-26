package myproject.cliposerver.data.dto.notification;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoteInfoResponseDTO {
    private Long nno;
    private String type;
    private String email;
    private String from;
    private Long bno;
    private Long rno;
    private Boolean isFollowing;
    private Boolean isRead;
    private LocalDateTime createAt;
    private String boardOneImage;
    private String userProfileImage;
}
