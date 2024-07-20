package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.*;
import myproject.cliposerver.data.enumerate.Role;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;
    @Column(nullable = true)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = true)
    private String password;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = true)
    private String profileImage;
    @Column(nullable = true)
    private Boolean isSocial;
    @Column(nullable = false)
    private Role role;
    @Column(nullable = true, unique = true)
    private String accessToken;
    @Column(nullable = true, unique = true)
    private String refreshToken;
    @ColumnDefault("false")
    private Boolean isValidate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "fromMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followFromList = new ArrayList<>();

    @OneToMany(mappedBy = "toMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followToList = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeValidate(Boolean validate) {
        this.isValidate = validate;
    }

    public void changeToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void changeAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
