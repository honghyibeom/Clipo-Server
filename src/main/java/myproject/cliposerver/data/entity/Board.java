package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @ManyToOne
    @JoinColumn(name = "userInfo")
    private Member member;
    @Column
    private String content;
    @Column(nullable = false)
    private Boolean isLikeVisible;
    @Column(nullable = false)
    private Boolean isReplyAllowed;
    @Column
    private Integer likeCount;
    @Column
    private Integer replyCount;

    // 👇 JPA 테이블에는 없지만, 쿼리에서 SELECT로 가져올 수 있음
    @Transient
    private Double rankingScore;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagMap> tagMapList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardLikeList  = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookMarkList  = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notificationList  = new ArrayList<>();

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeLikeVisible(Boolean likeVisible) {
        isLikeVisible = likeVisible;
    }

    public void changeReplyAllowed(Boolean replyAllowed) {
        isReplyAllowed = replyAllowed;
    }

    public void changeLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void changeReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public void changeBoardImageList(List<BoardImage> boardImageList) {
        this.boardImageList = boardImageList;
    }

    public void changeTagMapList(List<TagMap> tagMapList) {
        this.tagMapList.clear(); // 기존 내용 제거 → orphanRemoval로 자동 DELETE 발생
        for (TagMap tagMap : tagMapList) {
            this.tagMapList.add(tagMap);
            tagMap.changeBoard(this); // 연관관계 주인 설정
        }
    }

}
