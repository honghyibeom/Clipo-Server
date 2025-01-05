package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.TagMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapRepository extends JpaRepository<TagMap, Long> {
    List<TagMap> findByBoard(Board board);
    void deleteByBoard(Board board);
}
