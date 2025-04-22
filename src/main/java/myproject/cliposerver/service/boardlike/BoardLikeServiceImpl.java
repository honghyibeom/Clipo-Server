package myproject.cliposerver.service.boardlike;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.BoardLike;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Notification;
import myproject.cliposerver.data.enumerate.NoteEnum;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.BoardLikeRepository;
import myproject.cliposerver.repository.BoardRepository;
import myproject.cliposerver.repository.NotificationRepository;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

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

        return ResponseDTO.builder()
                .message("boardLike 유저들 목록 확인")
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
