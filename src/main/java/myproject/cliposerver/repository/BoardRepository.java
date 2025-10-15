package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    // 모든 게시글 h2 버전
    @Query(value = "SELECT b.*, " +
            "       (0.5 * (1.0 / (1 + TIMESTAMPDIFF(HOUR, b.regdate, NOW())))) + " +
            "       (0.25 * LOG(1 + b.like_count)) + " +
            "       (0.25 * LOG(1 + b.reply_count)) + " +
            "       (CASE WHEN b.user_info IN ( " +
            "            SELECT f.to_Member " +
            "            FROM follow f " +
            "            WHERE f.from_Member = (SELECT m.email FROM members m WHERE m.email = :email) " +
            "       ) THEN 0.1 ELSE 0 END) AS rankingScore " +
            "FROM board b " +
            "ORDER BY rankingScore DESC",
            countQuery = "SELECT count(*) FROM board",
            nativeQuery = true)
    Page<Board> findBoardsByRankingH2(@Param("email") String email, Pageable pageable);

    @Query(value = """
    SELECT b.*, 
           (0.5 * (1.0 / (1 + ((SYSDATE - CAST(b.regdate AS DATE)) * 24)))) +
           (0.25 * LN(1 + b.like_count)) +
           (0.25 * LN(1 + b.reply_count)) +
           (CASE 
                WHEN b.user_info IN (
                    SELECT f.to_member
                    FROM follow f
                    WHERE f.from_member = :email
                )
                THEN 0.1 ELSE 0
            END) AS rankingScore
    FROM board b
    ORDER BY rankingScore DESC
    """,
            countQuery = "SELECT count(*) FROM board",
            nativeQuery = true)
    Page<Board> findBoardsByRanking(@Param("email") String email, Pageable pageable);

    // 팔로잉한 게시글만 나오도록
    @Query("SELECT b " +
            "FROM board b " +
            "WHERE b.member IN (select f.toMember from follow f where f.fromMember = :me )" +
            "ORDER BY b.regDate DESC ")
    Page<Board> findAllByFollowing(@Param("me") Member me, Pageable pageable);

    // 가장 최근에 게시글을 작성한 글조회
    Optional<Board> findTopByMemberOrderByRegDateDesc(Member member);

}
