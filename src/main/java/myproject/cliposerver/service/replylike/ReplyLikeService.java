package myproject.cliposerver.service.replylike;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
public interface ReplyLikeService {
    // 댓글 좋아요
    ResponseDTO like(Long rno, UserDetailsImpl userDetails);
    // 댓글 좋아요 취소
    ResponseDTO unlike(Long rno, UserDetailsImpl userDetails);
    // 댓글 좋아요 리스트
    ResponseDTO replyLikeList(Long rno, int page, UserDetailsImpl userDetails);

}
