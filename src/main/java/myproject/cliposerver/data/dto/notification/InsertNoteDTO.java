package myproject.cliposerver.data.dto.notification;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Reply;

import java.time.LocalDateTime;

@Getter
@Builder
public class InsertNoteDTO {
    private String type;
    private Member from;
    private Member to;
    private Board board;
    private Reply reply;
    private LocalDateTime time;
    private Boolean isRead;
}
