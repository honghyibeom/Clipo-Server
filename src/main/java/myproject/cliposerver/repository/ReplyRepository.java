package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 댓글 갯수
    Long countByBoard(Board board);
    // 부모 댓글 갯수
    Long countByParent(Reply reply);
    // 특정 유저의 댓글 조회
    Page<Reply> findByWriterOrderByRegDateDesc(Member member, Pageable page);
    // 특정 board의 댓글 조회
    Page<Reply> findByBoardAndParentIsNullOrderByRegDateAsc(Board board, Pageable page);
    // 특정 댓글의 대댓글 조회
    Page<Reply> findByParentRnoOrderByRegDateAsc(Long rno, Pageable page);

    // 댓글 찾기
    Optional<Reply> findByRno(Long rno);

    // 해당 댓글보다 regDate가 빠른 댓글의 개수 구하기
    @Query("select count(r) " +
            "from reply r " +
            "where r.board.bno = :bno " +
            "and r.parent is null " +
            "and r.regDate < :regDate ")
    long countByReplyBefore(@Param("bno") Long bno, @Param("regDate") LocalDateTime regDate);

    //해당 대댓글보다 regDate가 빠른 대댓의 개수 구하기
    @Query("SELECT COUNT(r)" +
            " FROM reply r" +
            " WHERE r.parent.rno = :parentRno" +
            " AND r.regDate < :regDate")
    long countNestedRepliesBefore(@Param("parentRno") Long parentRno, @Param("regDate") LocalDateTime regDate);

}
