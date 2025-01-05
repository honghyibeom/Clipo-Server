package myproject.cliposerver.data.dto.reply;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReplyInfoResponseDTO {
    private Long rno;
    private String typeOfPost;
    private String email;
    private String nickName;
    private String profilePicture;
    private List<String> boardImage;
    private String replyImage;
    private Long numberOfLike;
    private Long numberOfComments;
    private String contents;
    private String regDate;
    private Boolean isLike;
}
