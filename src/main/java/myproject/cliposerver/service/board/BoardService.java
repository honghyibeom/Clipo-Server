package myproject.cliposerver.service.board;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BoardService {
    // 게시글 생성
    ResponseDTO createBoard(BoardRequestDTO boardRequestDTO, List<MultipartFile> boardImages, UserDetailsImpl userDetails);
    // 게시글 수정
    ResponseDTO update(BoardRequestDTO boardRequestDTO,List<MultipartFile> boardImages , UserDetailsImpl userDetails);
    // 게시글 삭제
    ResponseDTO delete(Long bno, UserDetailsImpl userDetails);
    // 마이페이지 게시글 조회
    ResponseDTO getMyBoardResponse(int page,String username,UserDetailsImpl userDetails);
    // 마이페이지 댓글 조회
    ResponseDTO getMyReplyResponse(int page, String username,UserDetailsImpl userDetails);
    // 마이페이지 좋아요한 게시글 조회
    ResponseDTO getMyLikesResponse(int page, String username,UserDetailsImpl userDetails);
    // 메인페이지 게시글 조회
    ResponseDTO getRandomBoard(int page,UserDetailsImpl userDetails);
    // 게시글 조회
    ResponseDTO getDetailBoard(Long bno,UserDetailsImpl userDetails);

}
