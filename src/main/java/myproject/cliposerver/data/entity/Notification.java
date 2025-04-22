package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "notification")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;
    @ManyToOne
    @JoinColumn(name = "receiverMember")
    private Member receiver;
    @ManyToOne
    @JoinColumn(name = "senderMember")
    private Member sender;
    @ManyToOne
    @JoinColumn(name = "board")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "reply")
    private Reply reply;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    LocalDateTime createdAt;
    @Column(nullable = false)
    private Boolean isRead;
}
