package myproject.cliposerver.data.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "tagMap")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapId;
    @ManyToOne
    @JoinColumn(name = "board")
    private Board board;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag")
    private Tag tag;

}
