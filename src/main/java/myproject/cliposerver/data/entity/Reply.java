package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "boardInfo")
    private Board board;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Integer likes;
    @Column(nullable = true)
    private String replyImage;

    @ManyToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_rno")
    private Reply parent; // 부모 댓글

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplyLike> ReplyLikeList  = new ArrayList<>();

    public void changeText(String text) {
        this.text = text;
    }

    public void changeReplyImage(String replyImage) {
        this.replyImage = replyImage;
    }
}
