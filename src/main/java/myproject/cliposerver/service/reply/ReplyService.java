package myproject.cliposerver.service.reply;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ReplyService {
    ResponseDTO createReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails, MultipartFile commentImage);
    ResponseDTO updateReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails, MultipartFile commentImage);
    ResponseDTO deleteReply(Long rno, UserDetailsImpl userDetails);
    ResponseDTO getDetailReply(Long bno, int page, UserDetailsImpl userDetails);
    ResponseDTO getDetailChildReply(Long rno, int page, UserDetailsImpl userDetails);


}
