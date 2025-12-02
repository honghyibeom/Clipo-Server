package myproject.cliposerver.data.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FollowEnum {
    FOLLOWER("FOLLOWER"),
    FOLLOWING("FOLLOWING"),;
    private final String type;

}
