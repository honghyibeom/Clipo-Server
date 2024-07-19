package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "reply")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    @ManyToOne
    @JoinColumn(name = "member")
    private Member writer;
    @ManyToOne
    @JoinColumn(name = "boardInfo")
    private Board board;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Integer likes;
    @Column(nullable = true)
    private String replyImage;
    @ManyToOne
    @JoinColumn(name = "parent_rno")
    private Reply parent; // 부모 댓글
}
