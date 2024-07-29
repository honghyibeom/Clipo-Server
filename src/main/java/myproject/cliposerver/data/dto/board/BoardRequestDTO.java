package myproject.cliposerver.data.dto.board;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.BoardImage;
import myproject.cliposerver.data.entity.Member;

import java.util.List;

@Getter
@Builder
public class BoardRequestDTO {
    private Long bno;
    private String title;
    private String content;
    private String tag;
    private List<String> boardImageList;

    public Board toEntity(Member member){
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .member(member)
                .likes(0)
                .tag(this.tag)
                .build();
    }

    public BoardImage toEntity(Board board,String src){
        return BoardImage.builder()
                .board(board)
                .src(src)
                .build();
    }
}


