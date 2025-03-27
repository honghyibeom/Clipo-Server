package myproject.cliposerver.data.dto.tag;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchTagResponseDTO {
    private String[] tags;
}
