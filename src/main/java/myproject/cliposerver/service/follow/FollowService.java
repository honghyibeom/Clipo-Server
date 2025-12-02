package myproject.cliposerver.service.follow;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.enumerate.FollowEnum;
import org.springframework.stereotype.Service;

@Service
public interface FollowService {
    // 팔로우
    ResponseDTO follow(String toMemberEmail, UserDetailsImpl userDetails);
    // 팔로우 취소
    ResponseDTO unfollow(String toMemberEmail, UserDetailsImpl userDetails);
//    // 팔로워 조회
//    ResponseDTO getUserFollower(String username, int page, UserDetailsImpl userDetails);
//    // 팔로잉 조회
//    ResponseDTO getUserFollowing(String username, int page, UserDetailsImpl userDetails);
    // 팔로워 & 팔로잉 조회
    ResponseDTO getUserFollow(String username, int page, UserDetailsImpl userDetails, FollowEnum followEnum);
}
