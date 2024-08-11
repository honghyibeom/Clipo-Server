package myproject.cliposerver.data.dto.board;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.*;

import java.util.List;

@Getter
@Builder
public class BoardRequestDTO {
    private Long bno;
    private String content;
    private List<String> boardImageList;
    private List<String> tag;

    public Board toEntity(Member member){
        return Board.builder()
                .content(this.content)
                .member(member)
                .likes(0)
                .build();
    }

    public BoardImage toEntity(Board board,String src){
        return BoardImage.builder()
                .board(board)
                .src(src)
                .build();
    }
    public Tag toEntity(String word){
        return Tag.builder()
                .word(word)
                .build();
    }

    public TagMap toEntity(Board board, Tag tag){
        return TagMap.builder()
                .board(board)
                .tag(tag)
                .build();
    }
}


