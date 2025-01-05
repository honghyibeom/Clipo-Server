package myproject.cliposerver.data.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeOfPost {
    board("board"),
    reply("reply"),
    nestRe("nestRe");

    private final String type;

}
