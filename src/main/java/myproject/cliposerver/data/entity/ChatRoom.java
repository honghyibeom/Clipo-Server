package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "chat_room")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false, length = 100)
    private String roomName;

    @Column(nullable = false)
    private boolean isGroup; // true: 그룹채팅, false: 1:1채팅

    @Column(nullable = false)
    private Long createdBy; // 생성자 userId (FK)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 메시지 관계 (채팅방 삭제 시 메시지도 함께 제거)
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;

}
