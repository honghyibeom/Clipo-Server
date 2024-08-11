package myproject.cliposerver.service.replylike;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.data.entity.ReplyLike;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.ReplyLikeRepository;
import myproject.cliposerver.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyLikeServiceImpl implements ReplyLikeService {
    private final ReplyRepository replyRepository;
    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    public ResponseDTO like(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        ReplyLike replyLike = ReplyLike.builder()
                .member(userDetails.getMember())
                .reply(reply)
                .build();
        replyLikeRepository.save(replyLike);

        return ResponseDTO.builder()
                .message("좋아요 완료")
                .build();
    }

    @Transactional
    public ResponseDTO unlike(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));
        replyLikeRepository.deleteByMemberAndReply(userDetails.getMember(), reply);

        return ResponseDTO.builder()
                .message("좋아요 취소 완료")
                .build();
    }
}
