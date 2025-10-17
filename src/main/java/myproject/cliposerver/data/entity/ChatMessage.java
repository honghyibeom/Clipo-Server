package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.cliposerver.data.enumerate.MessageType;

import java.time.LocalDateTime;

@Entity(name = "chat_message")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // 채팅방 (N:1)
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Long senderId; // app_user.userId (FK 대신 단순 ID로 저장)

    @Column(columnDefinition = "TEXT")
    private String content; // 텍스트 or S3 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType messageType; // TEXT / IMAGE / SYSTEM

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private boolean isDeleted;
}
