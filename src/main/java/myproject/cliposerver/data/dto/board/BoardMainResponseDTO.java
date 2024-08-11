package myproject.cliposerver.data.dto.board;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardMainResponseDTO {
    private String nickName;
    private String profilePicture;
    private Integer numberOfLike;
    private Long numberOfComments;
    private String contents;
    private List<String> tags;
    private String regData;
}
