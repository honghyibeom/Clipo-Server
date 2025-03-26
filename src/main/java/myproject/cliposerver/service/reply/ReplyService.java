package myproject.cliposerver.service.reply;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ReplyService {
    // 댓글 생성
    ResponseDTO createReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails, MultipartFile commentImage);
    // 댓글 수정
    ResponseDTO updateReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails, MultipartFile commentImage);
    // 댓글 삭제
    ResponseDTO deleteReply(Long rno, UserDetailsImpl userDetails);
    // 댓글 조회
    ResponseDTO getDetailReply(Long bno, int page, UserDetailsImpl userDetails);
    // 대댓글 조회
    ResponseDTO getDetailChildReply(Long rno, int page, UserDetailsImpl userDetails);
    // 댓글 하나 조회
    ResponseDTO getDetailOneReply(Long rno, UserDetailsImpl userDetails);


}
