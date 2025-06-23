package myproject.cliposerver.data.dto.reply;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Reply;

import java.util.List;

@Getter
@Builder
public class ReplyRequestDTO {
    private Long rno; // 수정 할 때 필요
    private Long bno;
    private String content;
    private String originImage;
    private Long parentRno; // 자식댓글 생성 할 때 필요
    private List<String> mentions;

    public Reply toEntity(Board board, Member member) {
        return Reply.builder()
                .board(board)
                .writer(member)
                .likes(0)
                .text(this.content)
                .parent(null)
                .build();
    }
    public Reply toEntity(Board board, Member member, Reply parent) {
        return Reply.builder()
                .board(board)
                .writer(member)
                .likes(0)
                .text(this.content)
                .parent(parent)
                .build();
    }
}
