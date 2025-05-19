package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.data.entity.ReplyLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    // 댓글 좋아요 취소
    void deleteByMemberAndReply(Member member, Reply reply);
    // 댓글 좋아요 여부
    Boolean existsByReplyAndMember(Reply reply, Member member);
    // 댓글 좋아요 갯수
    Long countByReply(Reply reply);
    // 댓글에 좋아요한 목록 조회
    Page<ReplyLike> getReplyLikeByReply_Rno(Long reply, Pageable pageable);

}
