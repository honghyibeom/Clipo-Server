package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 활동기록 조회
    List<Notification> getNotificationsByReceiver(Member receiver);

    @Modifying
    @Query("delete from notification n where n.createdAt < :createdAtBefore ")
    void deleteByCreatedAtBefore(@Param("createdAtBefore") LocalDateTime createdAtBefore);

}
