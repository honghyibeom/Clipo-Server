package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "follow")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;
    @ManyToOne
    @JoinColumn(name = "userInfo")
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "follower")
    private Member toMember;
}
