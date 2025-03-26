package myproject.cliposerver.service.boardlike;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;

public interface BoardLikeService {
    //좋아요
    ResponseDTO like(Long bno, UserDetailsImpl userDetails);
    // 좋아요 취소
    ResponseDTO unlike(Long bno, UserDetailsImpl userDetails);
    // 좋아요한 게시글 리스트 조회
    ResponseDTO boardLikeList(Long bno, int page, UserDetailsImpl userDetails);

}
