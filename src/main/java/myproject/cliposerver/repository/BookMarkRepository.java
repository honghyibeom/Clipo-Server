package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.BoardLike;
import myproject.cliposerver.data.entity.Bookmark;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberAndBoard(Member member, Board board);

    Page<Bookmark> findByMember(Member member, Pageable pageable);

    Page<Bookmark> getBookmarkByBoard_Bno(Long bno, Pageable pageable);
    // 북마크 찾기
    Optional<Bookmark> findByMemberAndBoard(Member member, Board board);
}
