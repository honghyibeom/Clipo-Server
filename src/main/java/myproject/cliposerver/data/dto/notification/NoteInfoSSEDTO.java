package myproject.cliposerver.data.dto.notification;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.enumerate.NoteEnum;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoteInfoSSEDTO {
    private NoteEnum type;
    private String from;
    private Long bno;
    private Long rno;
    private Long nestRe;
    private LocalDateTime createAt;
}