package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.BoardLike;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    // 좋아요 취소
    void deleteByBoardAndMember(Board board, Member member);
    // 좋아요 갯수
    Long countByBoard(Board board);
    // 게시글을 좋아요 했는지 여부
    Boolean  existsByBoardAndMember(Board board, Member member);
    // 게시글에 좋아요한 목록 조회
    Page<BoardLike> getBoardLikesByBoard_Bno(Long bno, Pageable pageable);
}
