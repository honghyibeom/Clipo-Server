package myproject.cliposerver.data.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.*;

import java.util.List;

@Getter
@Builder
public class BoardRequestDTO {
    @Schema(description = "게시글Id(업데이트시 입력)", example = "1")
    private Long bno;
    @Schema(description = "게시글 내용", example = "1")
    private String content;
    @Schema(description = "테그", example = "#테그")
    private List<String> tags;
    @Schema(description = "이미지(업데이트시 입력)", example = "imageUrl")
    private List<String> originImages;
    @Schema(description = "좋아요 허용 여부", example = "true")
    private Boolean isLikeVisible;
    @Schema(description = "댓글 허용 여부", example = "true")
    private Boolean isReplyAllowed;
    @Schema(description = "언급", example = "@username")
    private List<String> mentions;

    public Board toEntity(Member member){
        return Board.builder()
                .content(this.content)
                .member(member)
                .likeCount(0)
                .replyCount(0)
                .isLikeVisible(this.isLikeVisible)
                .isReplyAllowed(this.isReplyAllowed)
                .build();
    }

    public BoardImage toEntity(Board board,String src){
        return BoardImage.builder()
                .board(board)
                .src(src)
                .build();
    }
}


