package myproject.cliposerver.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseDTO {
    private String message;
    private Object body;
}
