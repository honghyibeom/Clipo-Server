package myproject.cliposerver.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoRequestDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Notification;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.BoardRepository;
import myproject.cliposerver.repository.NotificationRepository;
import myproject.cliposerver.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    @Override
    public ResponseDTO insertNotification(NoteInfoRequestDTO noteInfoRequestDTO, UserDetailsImpl userDetails) {
        if (noteInfoRequestDTO.getBoardId() == null && noteInfoRequestDTO.getReplyId() == null) {
            throw new CustomException(ErrorCode.WRONG_REQUEST);
        }

        if (noteInfoRequestDTO.getBoardId() == null) {
            Optional<Reply> replyResult = replyRepository.findByRno(noteInfoRequestDTO.getReplyId());
            if(replyResult == null) {
                throw new CustomException(ErrorCode.NOT_EXIST_REPLY);
            }
            Notification notification = Notification.builder()
                    .reply(replyResult.get())
                    .type(noteInfoRequestDTO.getType())
                    .createdAt(LocalDateTime.now())
                    .receiverMno(replyResult.get().getWriter())
                    .senderMno(userDetails.getMember())
                    .build();
            notificationRepository.save(notification);
        }

        else if (noteInfoRequestDTO.getReplyId() == null) {
            Optional<Board> boardResult = boardRepository.findByBno(noteInfoRequestDTO.getBoardId());
            if(boardResult == null) {
                throw new CustomException(ErrorCode.NOT_EXIST_REPLY);
            }
            Notification notification = Notification.builder()
                    .board(boardResult.get())
                    .type(noteInfoRequestDTO.getType())
                    .createdAt(LocalDateTime.now())
                    .receiverMno(boardResult.get().getMember())
                    .senderMno(userDetails.getMember())
                    .build();
            notificationRepository.save(notification);
        }

        return ResponseDTO.builder()
                .message("알림 생성완료")
                .build();
    }

//    public SseEmitter subscribe(String email) {
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//        emitters.put(email, emitter);
//
//        emitter.onCompletion(() -> emitters.remove(email));
//        emitter.onTimeout(() -> emitters.remove(email));
//
//        return emitter;
//    }

//    public void sendNotification(UserDetailsImpl userDetails) {
//        SseEmitter emitter = emitters.get(userDetails.getMember().getEmail());
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("notification")
//                        .data();
//            } catch (IOException e) {
//                emitters.remove(userDetails.getMember().getEmail());
//            }
//        }
//    }
}
