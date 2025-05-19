package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Tag;
import myproject.cliposerver.data.entity.TagMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapRepository extends JpaRepository<TagMap, Long> {
    // 태그맵 찾기
    Page<TagMap> findByTag(Tag tag, Pageable pageable);
    //  태그맵 제거
    void deleteByBoard(Board board);
}
