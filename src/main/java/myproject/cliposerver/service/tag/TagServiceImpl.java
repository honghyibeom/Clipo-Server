package myproject.cliposerver.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public ResponseDTO getTagForSearch(int page, UserDetailsImpl userDetails, String search) {
        //테그를 가져오자
        PageRequest pageRequest = PageRequest.of(page, 6);
        Page<String> tagPages = tagRepository.findDistinctWords(search + "%",pageRequest);

        if (tagPages.isEmpty()) {
            return ResponseDTO.builder()
                    .message("테그가 없습니다.")
                    .build();
        }
        List<String> result = tagPages.getContent();

        // 객체 리스트를 String[]로 만드는 작업
        String[] tags = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            tags[i] = result.get(i);
        }
        //프론트맨이 지금까지 모든 파싱과정을 body하고 바로 배열로 하는걸로해버려서
        //이걸 한번에 못고치겠다고 하여 tags를 없애고 배열만 전달해야댐..
//
//        SearchTagResponseDTO searchTagResponseDTO = SearchTagResponseDTO.builder()
//                        .tags(tags)
//                        .build();

        PageResponseDTO<String> response = PageResponseDTO.<String>builder()
                .data(List.of(tags))
                .page(tagPages.getNumber())
                .hasNext(tagPages.hasNext())
                .hasPrev(tagPages.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .body(response)
                .message("테그 검색 결과")
                .build();
    }
}
