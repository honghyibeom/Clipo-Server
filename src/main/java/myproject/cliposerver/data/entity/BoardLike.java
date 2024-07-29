package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "boardLike")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long boardLno;
    @ManyToOne
    @JoinColumn(name = "board")
    Board board;
    @ManyToOne
    @JoinColumn(name = "member")
    Member member;

}
