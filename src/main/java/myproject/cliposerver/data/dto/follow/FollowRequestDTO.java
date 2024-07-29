package myproject.cliposerver.data.dto.follow;

import lombok.Builder;
import lombok.Getter;
import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;

@Getter
@Builder
public class FollowRequestDTO {
    private String fromMemberEmail ;
    private String toMemberEmail;

    public Follow toEntity(Member fromMember, Member toMember){
        return Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
    }
}


