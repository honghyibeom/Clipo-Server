package myproject.cliposerver.service.boardlike;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LittleUserInfoResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyInfoResponseDTO;
import myproject.cliposerver.data.entity.*;
import myproject.cliposerver.data.enumerate.NoteEnum;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.BoardLikeRepository;
import myproject.cliposerver.repository.BoardRepository;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.NotificationRepository;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final FollowRepository followRepository;

    @Transactional
    public ResponseDTO like(Long bno, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(userDetails.getMember())
                .build();
        boardLikeRepository.save(boardLike);

        //알림테이블에 추가
        insertNotification(userDetails.getMember(), board);

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
    @Override
    public ResponseDTO boardLikeList(Long bno, int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<BoardLike> boardLikePage = boardLikeRepository.getBoardLikesByBoard_Bno(bno, pageRequest);

        List<BoardLike> result = boardLikePage.getContent();
        List<LittleUserInfoResponseDTO> responseDTOS = result.stream().map(boardlike ->
                LittleUserInfoResponseDTO.builder()
                        .profilePicture(boardlike.getMember().getProfileImage())
                        .nickName(boardlike.getMember().getName())
                        .email(boardlike.getMember().getEmail())
                        .isFollowing(followRepository
                                .existsByFromMemberAndToMember(userDetails.getMember(), boardlike.getMember()))
                        .build()
                ).toList();

        PageResponseDTO<LittleUserInfoResponseDTO> response = PageResponseDTO.<LittleUserInfoResponseDTO>builder()
                .data(responseDTOS)
                .page(boardLikePage.getNumber())
                .hasNext(boardLikePage.hasNext())
                .hasPrev(boardLikePage.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .message("boardLike 유저들 목록 확인")
                .body(response)
                .build();
    }
    private void insertNotification(Member sender, Board board) {
        Notification notification = Notification.builder()
                .type(NoteEnum.like.name())
                .sender(sender)
                .board(board)
                .receiver(board.getMember())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

        // 알림 전달
        notificationService.sendNotification(board.getMember().getEmail(), "notification");
    }
}
