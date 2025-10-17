package myproject.cliposerver.data.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    TEXT("TEXT"),
    IMAGE("IMAGE"),
    SYSTEM("SYSTEM");

    private final String type;
}
