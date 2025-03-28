package myproject.cliposerver.data.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateUserInfoRequestDTO {
    @Schema(description = "닉네임 입력",example = "쌉범")
    private String nickName;
    @Schema(description = "위치 입력",example = "부산")
    private String location;
    @Schema(description = "설명 입력",example = "안녕하세요 반갑습니다.")
    private String description;
    @Schema(description = "생일 입력",example = "19990717")
    private String birthday;
}
