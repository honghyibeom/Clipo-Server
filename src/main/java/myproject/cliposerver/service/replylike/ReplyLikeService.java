package myproject.cliposerver.service.replylike;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
public interface ReplyLikeService {
    ResponseDTO like(Long rno, UserDetailsImpl userDetails);
    ResponseDTO unlike(Long rno, UserDetailsImpl userDetails);
    ResponseDTO replyLikeList(Long rno, int page, UserDetailsImpl userDetails);

}
