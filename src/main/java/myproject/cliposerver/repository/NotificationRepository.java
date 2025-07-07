package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Notification;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.data.enumerate.NoteEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 활동기록 조회
    Page<Notification> getNotificationsByReceiver(Member receiver, Pageable pageable);

    // 오전 3시가 되면 알림기록 삭제
    @Modifying
    @Query("delete from notification n where n.createdAt < :createdAtBefore ")
    void deleteByCreatedAtBefore(@Param("createdAtBefore") LocalDateTime createdAtBefore);

    // 알림 갯수
    @Query("SELECT COUNT(n) FROM notification n WHERE n.isRead = false AND n.receiver =:receiver ")
    Long countUnreadNotifications(Member receiver);

    // 알림 정보 조회
    Optional<Notification> findByNno(Long nno);

    //게시글 언급 알림 조회
    List<Notification> findByBoardAndType(Board board, NoteEnum type);
    // 댓글 언급 알림 조회
    List<Notification> findByReplyAndType(Reply reply, NoteEnum type);

}
