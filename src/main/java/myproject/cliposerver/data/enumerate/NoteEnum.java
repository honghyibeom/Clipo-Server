package myproject.cliposerver.data.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoteEnum {
    board("board"),
    reply("reply"),
    nestRe("nestRe"),
    like("like"),
    longtime("longtime"),
    follow("follow"),
    mention("mention");

    private final String type;

}
