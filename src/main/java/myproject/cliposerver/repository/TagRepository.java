package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    //테그 검색
    Optional<Tag> findByWord(String word);
}
