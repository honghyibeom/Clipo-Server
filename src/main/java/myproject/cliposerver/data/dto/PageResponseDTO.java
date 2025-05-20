package myproject.cliposerver.data.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageResponseDTO<T> {
    private List<T> data;
    private int page;
    private boolean hasNext;
    private boolean hasPrev;
}
