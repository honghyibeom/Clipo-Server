package myproject.cliposerver.service.follow;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface FollowService {
    ResponseDTO follow(String toMemberEmail, UserDetailsImpl userDetails);

    ResponseDTO unfollow(String toMemberEmail, UserDetailsImpl userDetails);
}
