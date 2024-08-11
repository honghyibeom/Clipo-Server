package myproject.cliposerver.data.dto.board;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class BoardInfoResponseDTO {
    private String email;
    private String nickName;
    private String profilePicture;
    private List<String> boardImage;
    private String replyImage;
    private Integer numberOfLike;
    private Long numberOfComments;
    private String contents;
    private List<String> tag;
    private String regDate;
}
