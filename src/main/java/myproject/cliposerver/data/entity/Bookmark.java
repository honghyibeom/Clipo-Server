package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "bookmark")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long bookmarkId;
    @ManyToOne
    @JoinColumn(name = "board")
    Board board;
    @ManyToOne
    @JoinColumn(name = "member")
    Member member;

}
