package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    //@EntityGraph(attributePaths = {"member", "boardImageList", "tagMapList.tag"})
    Page<Board> findAllByOrderByRegDateDesc(Pageable pageable);
    // 팔로잉한 게시글만 나오도록
    @Query("SELECT b " +
            "FROM board b " +
            "WHERE b.member IN (select f.toMember from follow f where f.fromMember = :me )" +
            "ORDER BY b.regDate DESC ")
    Page<Board> findAllByFollowing(@Param("me") Member me, Pageable pageable);

    // 가장 최근에 게시글을 작성한 글조회
    Optional<Board> findTopByMemberOrderByRegDateDesc(Member member);

}
