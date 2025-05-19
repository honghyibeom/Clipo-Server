package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 게시글 조회
    Optional<Board> findByBno(Long bno);
    // 특정 유저의 게시글
    Page<Board> findByMemberOrderByRegDateDesc(Member member, Pageable pageable);
    // 좋아요한 게시글
    Page<Board> findByBoardLikeListMemberOrderByRegDateDesc(Member member, Pageable pageable);
    // 모든 게시글
    Page<Board> findAllByOrderByRegDateDesc(Pageable pageable);
}
