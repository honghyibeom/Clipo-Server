package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    //테그 검색
    Optional<Tag> findFirstByWord(String word);
    // 검색한 태그들
    @Query("select distinct t.word from tag t where t.word like :search")
    Page<String> findDistinctWords(@Param("search") String search, Pageable pageable);
}
