package myproject.cliposerver.data.dto.member;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateUserInfoRequestDTO {
    private String nickName;
    private String location;
    private String description;
    private String brithDay;
}
