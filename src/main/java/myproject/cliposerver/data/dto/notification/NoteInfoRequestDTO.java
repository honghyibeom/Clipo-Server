package myproject.cliposerver.data.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoteInfoRequestDTO {
    @Schema(description = "댓글, 좋아요 구분",example = "REPLY or LIKE")
    private String type;
    @Schema(description = "좋아요 및 댓글일 경우",example = "비밀번호123@")
    private Long boardId;
    @Schema(description = "좋아요 및 대댓글일 경우",example = "비밀번호123@")
    private Long replyId;
}
