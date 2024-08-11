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
    @Column(nullable = false)
    private String content;
    @Column(nullable = true)
    private Integer likes;

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

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeBoardImageList(List<BoardImage> boardImageList) {
        this.boardImageList = boardImageList;
    }

    public void changeTagMapList(List<TagMap> tagMapList) {
        this.tagMapList = tagMapList;
    }


}
