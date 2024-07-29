package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "replyLike")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long replyLno;
    @ManyToOne
    @JoinColumn(name = "reply")
    Reply reply;
    @ManyToOne
    @JoinColumn(name = "member")
    Member member;
}
