package myproject.cliposerver.service.bookmark;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LittleUserInfoResponseDTO;
import myproject.cliposerver.data.entity.*;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookMarkServiceImpl implements BookmarkService {
    private final BoardRepository boardRepository;
    private final BookMarkRepository bookmarkRepository;
    private final FollowRepository followRepository;

    @Transactional
    public ResponseDTO bookmark(Long bno, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        Bookmark bookmark = Bookmark.builder()
                .board(board)
                .member(userDetails.getMember())
                .build();
        bookmarkRepository.save(bookmark);

        return ResponseDTO.builder()
                .message("북마크 완료")
                .build();
    }

    @Transactional
    public ResponseDTO unBookmark(Long bno, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        Bookmark bookmark = bookmarkRepository.findByMemberAndBoard(userDetails.getMember(), board)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        bookmarkRepository.delete(bookmark);

        return ResponseDTO.builder()
                .message("북마크 취소 완료")
                .build();
    }
    @Override
    public ResponseDTO bookmarkMemberList(Long bno, int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Bookmark> boardLikePage = bookmarkRepository.getBookmarkByBoard_Bno(bno, pageRequest);

        List<Bookmark> result = boardLikePage.getContent();
        List<LittleUserInfoResponseDTO> responseDTOS = result.stream().map(Bookmark ->
                LittleUserInfoResponseDTO.builder()
                        .profilePicture(Bookmark.getMember().getProfileImage())
                        .nickName(Bookmark.getMember().getName())
                        .email(Bookmark.getMember().getEmail())
                        .isFollowing(followRepository
                                .existsByFromMemberAndToMember(userDetails.getMember(), Bookmark.getMember()))
                        .build()
        ).toList();

        PageResponseDTO<LittleUserInfoResponseDTO> response = PageResponseDTO.<LittleUserInfoResponseDTO>builder()
                .data(responseDTOS)
                .page(boardLikePage.getNumber())
                .hasNext(boardLikePage.hasNext())
                .hasPrev(boardLikePage.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .message("Bookmark 유저들 목록 확인")
                .body(response)
                .build();
    }


}
