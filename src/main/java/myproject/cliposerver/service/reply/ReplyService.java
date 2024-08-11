package myproject.cliposerver.service.reply;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReplyService {
    ResponseDTO createReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails);
    ResponseDTO createChildReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails);
    ResponseDTO updateReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails);
    ResponseDTO deleteReply(Long rno, UserDetailsImpl userDetails);

}
