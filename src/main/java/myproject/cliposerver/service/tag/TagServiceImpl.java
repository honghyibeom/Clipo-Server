package myproject.cliposerver.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.tag.SearchTagResponseDTO;
import myproject.cliposerver.data.entity.Tag;
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
        Page<Tag> tagPages = tagRepository.findBySearch(search,pageRequest);

        if (tagPages.isEmpty()) {
            return ResponseDTO.builder()
                    .message("테그가 없습니다.")
                    .build();
        }
        List<Tag> result = tagPages.getContent();

        // 객체 리스트를 String[]로 만드는 작업
        String[] tags = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            tags[i] = result.get(i).getWord();
        }
        SearchTagResponseDTO searchTagResponseDTO = SearchTagResponseDTO.builder()
                .tags(tags)
                .build();

        return ResponseDTO.builder()
                .body(searchTagResponseDTO)
                .message("테그 검색 결과")
                .build();
    }
}
