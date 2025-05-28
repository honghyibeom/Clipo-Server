package myproject.cliposerver.service.tag;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Tag;
import myproject.cliposerver.data.entity.TagMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface TagService {
    // 테그 검색 기능
    ResponseDTO getTagForSearch(int page, UserDetailsImpl userDetails, String search);
    // 태그맵 생성
    List<TagMap> createTagMaps(List<String> tagWords, Board board);
    // 테그맵 수정
    List<TagMap> updateTagMaps(List<String> tagWords, Board board);
    // 테그맵 삭제
    void deleteTagMaps(Board board);
    // 고아태그 제거
    void cleanupUnusedTags(Set<Tag> tags);

}
