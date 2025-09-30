package myproject.cliposerver.service.bookmark;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;

public interface BookmarkService {
    // 북마크
    ResponseDTO bookmark(Long bno, UserDetailsImpl userDetails);
    // 북마크 취소
    ResponseDTO unBookmark(Long bno, UserDetailsImpl userDetails);
    // 북마크한 회원목록 조회
    ResponseDTO bookmarkMemberList(Long bno, int page, UserDetailsImpl userDetails);

}
