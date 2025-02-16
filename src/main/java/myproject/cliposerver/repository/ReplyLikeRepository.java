package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.data.entity.ReplyLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    void deleteByMemberAndReply(Member member, Reply reply);
    Boolean existsByReplyAndMember(Reply reply, Member member);
    Long countByReply(Reply reply);
    Page<ReplyLike> getReplyLikeByReply_Rno(Long reply, Pageable pageable);
}
