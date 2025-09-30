package myproject.cliposerver.data.entity;

import jakarta.persistence.*;
import lombok.*;
import myproject.cliposerver.data.enumerate.Role;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    private String email;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String password;
    @Column(nullable = true)
    private String phone;
    @Column(nullable = true)
    private String profileImage;
    @Column(nullable = true)
    private Boolean isSocial;
    @Column(nullable = false)
    private Role role;
    @Column(nullable = true)
    private String backgroundImage;
    @Column (nullable = true)
    private String description;
    @Column(nullable = true)
    private String birth;
    @Column(nullable = true)
    private String location;
    @Column(nullable = true, unique = true)
    private String accessToken;
    @Column(nullable = true, unique = true)
    private String refreshToken;
    @ColumnDefault("false")
    private Boolean isValidate;
    @Column(nullable = true)
    private LocalDateTime lastLoginAt;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "fromMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followFromList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "toMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followToList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplyLike> replyLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> receiverList  = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> senderList  = new ArrayList<>();


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

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeBirth(String birth) {
        this.birth = birth;
    }

    public void changeLocation(String location) {
        this.location = location;
    }

    public void changeBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void changeLastLoginAt(LocalDateTime lastLoginAt) {this.lastLoginAt = lastLoginAt;}
}
