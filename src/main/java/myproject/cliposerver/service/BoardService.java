package myproject.cliposerver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.BoardImage;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.BoardImageRepository;
import myproject.cliposerver.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;

    @Transactional
    public ResponseDTO createBoard(BoardRequestDTO boardRequestDTO, UserDetailsImpl userDetails){
        Board board = boardRequestDTO.toEntity(userDetails.getMember());

        List<BoardImage> boardImageList = new ArrayList<>();
        for (String boardImage: boardRequestDTO.getBoardImageList()) {
            boardImageList.add(boardRequestDTO.toEntity(board,boardImage));
        }
        board.changeBoardImageList(boardImageList);
        boardRepository.save(board);

        return ResponseDTO.builder()
                .message("게시글 생성")
                .body(board.getBno())
                .build();
    }

    @Transactional
    public ResponseDTO update(BoardRequestDTO boardRequestDTO, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(boardRequestDTO.getBno())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));
        identification(board.getMember().getEmail(),userDetails.getEmail() );
        //board 수정
        board.changeTitle(boardRequestDTO.getTitle());
        board.changeContent(boardRequestDTO.getContent());
        board.changeTag(boardRequestDTO.getTag());
        //이미지 삭제 후 추가 작업
        boardImageRepository.deleteByBoard(board);
        for (String boardImage: boardRequestDTO.getBoardImageList()) {
            boardImageRepository.save(boardRequestDTO.toEntity(board,boardImage));
        }
        boardRepository.save(board);

        return ResponseDTO.builder()
                .message("게시글 수정 완료")
                .build();
    }

    @Transactional
    public ResponseDTO delete(Long bno, UserDetailsImpl userDetails){
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));
        identification(board.getMember().getEmail(), userDetails.getEmail());

        boardRepository.delete(board);
        return ResponseDTO.builder()
                .message("삭제 완료")
                .body(bno)
                .build();
    }
    private void identification(String memberEmail, String userDetailsEmail) {
        if (!memberEmail.equals(userDetailsEmail)){
            throw new CustomException(ErrorCode.NOT_EQUALS_USER);
        }
    }
}
