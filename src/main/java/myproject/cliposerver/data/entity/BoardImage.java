package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "boardImage")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;
    @ManyToOne
    @JoinColumn(name = "boardInfo")
    private Board board;
    @Column(nullable = false)
    private String src;
}
