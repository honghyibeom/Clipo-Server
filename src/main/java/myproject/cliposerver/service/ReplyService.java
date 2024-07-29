package myproject.cliposerver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyRequestDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.BoardRepository;
import myproject.cliposerver.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseDTO createReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(replyRequestDTO.getBno())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));

            Reply reply = replyRequestDTO.toEntity(board, userDetails.getMember());
            replyRepository.save(reply);

        return ResponseDTO.builder()
                .message("댓글 생성 완료")
                .build();
    }
    @Transactional
    public ResponseDTO createChildReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(replyRequestDTO.getBno())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));
        Reply reply = replyRepository.findById(replyRequestDTO.getParentRno())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        Reply childReply = replyRequestDTO.toEntity(board, userDetails.getMember(), reply);
        replyRepository.save(childReply);

        return ResponseDTO.builder()
                .message("대댓글 생성 완료")
                .build();
    }

    @Transactional
    public ResponseDTO updateReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(replyRequestDTO.getRno())
                        .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        identification(reply.getWriter().getEmail(), userDetails.getEmail());
        reply.changeText(replyRequestDTO.getText());
        reply.changeReplyImage(replyRequestDTO.getReplyImage());
        replyRepository.save(reply);

        return ResponseDTO.builder()
                .message("댓글 수정 완료")
                .build();
    }

    @Transactional
    public ResponseDTO deleteReply(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));
        identification(reply.getWriter().getEmail(), userDetails.getEmail());

        replyRepository.delete(reply);
        return ResponseDTO.builder()
                .message("댓글 삭제 완료")
                .build();
    }

    private void identification(String memberEmail, String userDetailsEmail) {
        if (!memberEmail.equals(userDetailsEmail)){
            throw new CustomException(ErrorCode.NOT_EQUALS_USER);
        }
    }
}
