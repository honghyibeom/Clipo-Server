package myproject.cliposerver.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Tag;
import myproject.cliposerver.data.entity.TagMap;
import myproject.cliposerver.repository.TagMapRepository;
import myproject.cliposerver.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapRepository tagMapRepository;

    @Override
    public ResponseDTO getTagForSearch(int page, UserDetailsImpl userDetails, String search) {
        //테그를 가져오자
        PageRequest pageRequest = PageRequest.of(page, 6);
        Page<String> tagPages = tagRepository.findWords(search + "%",pageRequest);

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

    @Override
    @Transactional
    public List<TagMap> createTagMaps(List<String> tagWords, Board board) {
        if (tagWords == null || tagWords.isEmpty()) {
            return Collections.emptyList();
        }

        // 기존 태그 조회
        List<Tag> existingTags = tagRepository.findAllByWordIn(tagWords);
        Set<String> existingWords = existingTags.stream()
                .map(Tag::getWord)
                .collect(Collectors.toSet());

        // 새 태그 추출
        List<String> newTagWords = tagWords.stream()
                .filter(tag -> !existingWords.contains(tag))
                .distinct()
                .toList();

        // 새 태그 객체 생성 후 저장
        List<Tag> newTags = newTagWords.stream()
                .map(word -> Tag.builder().word(word).build())
                .toList();
        List<Tag> savedNewTags = tagRepository.saveAll(newTags);

        // 전체 태그 결합
        List<Tag> allTags = new ArrayList<>();
        allTags.addAll(existingTags);
        allTags.addAll(savedNewTags);

        // TagMap 생성
        return allTags.stream()
                .map(tag -> TagMap.builder()
                        .board(board)
                        .tag(tag)
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public List<TagMap> updateTagMaps(List<String> tagWords, Board board) {
        // 1. 기존 TagMap 목록 저장 (삭제 전)
        List<TagMap> oldTagMaps = tagMapRepository.findByBoard(board);
        Set<Tag> oldTags = oldTagMaps.stream()
                .map(TagMap::getTag)
                .collect(Collectors.toSet());

        // 2. 기존 TagMap 삭제
        tagMapRepository.deleteByBoard(board);

        // 3. 고아 Tag 있으면 제거 (아무 게시글에도 연결되지 않은 경우만)
        cleanupUnusedTags(oldTags);

        // 새로 생성
        return createTagMaps(tagWords, board);
    }


    //태그맵 삭제
    @Transactional
    public void deleteTagMaps(Board board) {
        List<TagMap> oldTagMaps = tagMapRepository.findByBoard(board);

        Set<Tag> oldTags = oldTagMaps.stream()
                .map(TagMap::getTag)
                .collect(Collectors.toSet());
        tagMapRepository.deleteAll(oldTagMaps);
        //고아태그가 있으면 삭제
        cleanupUnusedTags(oldTags);
    }

    // 고아태그 제거
    @Transactional
    public void cleanupUnusedTags(Set<Tag> tags) {
        for (Tag tag : tags) {
            if (!tagMapRepository.existsByTag(tag)) {
                tagRepository.delete(tag);
            }
        }
    }
}
