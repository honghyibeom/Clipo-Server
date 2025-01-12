package myproject.cliposerver.data.dto.reply;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReplyInfoResponseDTO {
    private Long bno;
    private Long rno;
    private Long parentRno;
    private String typeOfPost;
    private String email;
    private String nickName;
    private String profilePicture;
    private List<String> boardImage;
    private String commentImage;
    private Long numberOfLike;
    private Long numberOfComments;
    private String contents;
    private String regDate;
    private Boolean isLike;
}
