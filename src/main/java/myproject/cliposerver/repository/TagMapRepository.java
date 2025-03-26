package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Tag;
import myproject.cliposerver.data.entity.TagMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapRepository extends JpaRepository<TagMap, Long> {

    Page<TagMap> findByTag(Tag tag, Pageable pageable);
    void deleteByBoard(Board board);
}
