package myproject.cliposerver.service.board;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {
    ResponseDTO createBoard(BoardRequestDTO boardRequestDTO, UserDetailsImpl userDetails);

    ResponseDTO update(BoardRequestDTO boardRequestDTO, UserDetailsImpl userDetails);

    ResponseDTO delete(Long bno, UserDetailsImpl userDetails);

    ResponseDTO getMyBoardResponse(int page, UserDetailsImpl userDetails);

    ResponseDTO getMyReplyResponse(int page, UserDetailsImpl userDetails);

    ResponseDTO getMyLikesResponse(int page, UserDetailsImpl userDetails);

    ResponseDTO getRandomBoard(int page);

}
