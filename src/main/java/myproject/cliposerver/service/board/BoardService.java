package myproject.cliposerver.service.board;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BoardService {
    ResponseDTO createBoard(BoardRequestDTO boardRequestDTO, List<MultipartFile> boardImages, UserDetailsImpl userDetails);

    ResponseDTO update(BoardRequestDTO boardRequestDTO,List<MultipartFile> boardImages , UserDetailsImpl userDetails);

    ResponseDTO delete(Long bno, UserDetailsImpl userDetails);

    ResponseDTO getMyBoardResponse(int page,String username,UserDetailsImpl userDetails);

    ResponseDTO getMyReplyResponse(int page, String username,UserDetailsImpl userDetails);

    ResponseDTO getMyLikesResponse(int page, String username,UserDetailsImpl userDetails);

    ResponseDTO getRandomBoard(int page,UserDetailsImpl userDetails);

    ResponseDTO getDetailBoard(Long bno,UserDetailsImpl userDetails);

}
