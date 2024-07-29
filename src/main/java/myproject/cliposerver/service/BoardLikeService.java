package myproject.cliposerver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.BoardLike;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.BoardLikeRepository;
import myproject.cliposerver.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardLikeService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    @Transactional
    public ResponseDTO like(Long bno, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(userDetails.getMember())
                .build();
        boardLikeRepository.save(boardLike);

        return ResponseDTO.builder()
                .message("좋아요 완료")
                .build();
    }

    @Transactional
    public ResponseDTO unlike(Long bno, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));
        boardLikeRepository.deleteByBoardAndMember(board, userDetails.getMember());

        return ResponseDTO.builder()
                .message("좋아요 취소 완료")
                .build();
    }
}
