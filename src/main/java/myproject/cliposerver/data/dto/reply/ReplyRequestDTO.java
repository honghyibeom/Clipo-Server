package myproject.cliposerver.data.dto.reply;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Reply;

@Getter
@Builder
public class ReplyRequestDTO {
    private Long rno; // 수정 할 때 필요
    private Long bno;
    private String text;
    private String replyImage;
    private Long parentRno; // 자식댓글 생성 할 때 필요

    public Reply toEntity(Board board, Member member) {
        return Reply.builder()
                .board(board)
                .writer(member)
                .likes(0)
                .replyImage(this.replyImage)
                .text(this.text)
                .parent(null)
                .build();
    }
    public Reply toEntity(Board board, Member member, Reply parent) {
        return Reply.builder()
                .board(board)
                .writer(member)
                .likes(0)
                .replyImage(this.replyImage)
                .text(this.text)
                .parent(parent)
                .build();
    }
}
