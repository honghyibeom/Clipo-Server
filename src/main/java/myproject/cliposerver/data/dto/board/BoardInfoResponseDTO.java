package myproject.cliposerver.data.dto.board;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BoardInfoResponseDTO {
    private String typeOfPost;
    private Long bno;
    private String nickName;
    private String email;
    private String profilePicture;
    private List<String> boardImages;
    private Long numberOfLike;
    private Long numberOfComments;
    private String contents;
    private List<String> tags;
    private String regDate;
    private Boolean isLike;
    private Boolean isFollowing;
    private Boolean isLikeVisible;
    private Boolean isReplyAllowed;
}
