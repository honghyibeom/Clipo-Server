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

import java.util.*;
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
        List<String> result = tagPages.getContent();

        PageResponseDTO<String> response = PageResponseDTO.<String>builder()
                .data(result)
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
        // 1. 현재 Board 에 연결된 TagMap 목록 조회
        List<TagMap> oldTagMaps = tagMapRepository.findByBoard(board);
        Map<String, TagMap> oldTagMapByWord = oldTagMaps.stream()
                .collect(Collectors.toMap(tm -> tm.getTag().getWord(), tm -> tm));

        Set<String> oldWords = oldTagMapByWord.keySet();
        Set<String> newWords = new HashSet<>(tagWords);

        // 2. 삭제할 태그 (old - new)
        Set<String> wordsToRemove = new HashSet<>(oldWords);
        wordsToRemove.removeAll(newWords);

        // 3. 추가할 태그 (new - old)
        Set<String> wordsToAdd = new HashSet<>(newWords);
        wordsToAdd.removeAll(oldWords);

        // 4. 삭제 처리
        List<TagMap> removedTagMaps = oldTagMaps.stream()
                .filter(tm -> wordsToRemove.contains(tm.getTag().getWord()))
                .toList();
        tagMapRepository.deleteAll(removedTagMaps);

        // 5. 고아 태그 정리 (아무 Board 에도 연결 안 된 Tag 제거)
        cleanupUnusedTags(
                removedTagMaps.stream()
                        .map(TagMap::getTag)
                        .collect(Collectors.toSet())
        );

        // 6. 추가할 태그 준비
        List<Tag> existingTags = tagRepository.findAllByWordIn(new ArrayList<>(wordsToAdd));
        Set<String> existingWords = existingTags.stream()
                .map(Tag::getWord)
                .collect(Collectors.toSet());

        // 새로 생성해야 할 태그
        List<String> newTagWords = wordsToAdd.stream()
                .filter(word -> !existingWords.contains(word))
                .toList();

        List<Tag> newTags = newTagWords.stream()
                .map(word -> Tag.builder().word(word).build())
                .toList();

        List<Tag> savedNewTags = tagRepository.saveAll(newTags);

        // 7. TagMap insert
        List<Tag> tagsToAdd = new ArrayList<>(existingTags);
        tagsToAdd.addAll(savedNewTags);

        List<TagMap> newTagMaps = tagsToAdd.stream()
                .map(tag -> TagMap.builder()
                        .board(board)
                        .tag(tag)
                        .build())
                .toList();

        tagMapRepository.saveAll(newTagMaps);

        // 8. 최종 TagMap = (유지 + 추가) 상태
        List<TagMap> finalTagMaps = new ArrayList<>();
        finalTagMaps.addAll(
                oldTagMaps.stream()
                        .filter(tm -> !wordsToRemove.contains(tm.getTag().getWord()))
                        .toList()
        );
        finalTagMaps.addAll(newTagMaps);

        return finalTagMaps;
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
