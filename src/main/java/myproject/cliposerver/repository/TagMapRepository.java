package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.TagMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapRepository extends JpaRepository<TagMap, Long> {
    void deleteByBoard(Board board);
}
