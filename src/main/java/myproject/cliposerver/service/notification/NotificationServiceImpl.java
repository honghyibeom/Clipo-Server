package myproject.cliposerver.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoSSEDTO;
import myproject.cliposerver.data.entity.Notification;
import myproject.cliposerver.data.enumerate.NoteEnum;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.MemberRepository;
import myproject.cliposerver.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final FollowRepository followRepository;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final MemberRepository memberRepository;

    @Override
    public ResponseDTO getNotification(UserDetailsImpl userDetails, int pages) {
        //알림 기록을 가져옴
        PageRequest pageRequest = PageRequest.of(pages, 7);
        Page<Notification> pageResult = notificationRepository.getNotificationsByReceiverOrderByCreatedAtDesc(userDetails.getMember(), pageRequest);
        List<Notification> result = pageResult.getContent();

        List<NoteInfoResponseDTO> noteInfoResponseDTOList = result.stream().map(noti ->

                NoteInfoResponseDTO.builder()
                        .nno(noti.getNno())
                        .type(noti.getType().name())
                        .bno(noti.getBoard() != null ? noti.getBoard().getBno() : null)
                        .boardOneImage( noti.getBoard() != null &&
                                noti.getBoard().getBoardImageList() != null &&
                                !noti.getBoard().getBoardImageList().isEmpty()
                                ? noti.getBoard().getBoardImageList().get(0).getSrc()
                                : null)
                        .rno(noti.getReply() != null ? noti.getReply().getRno() : null)
                        .email(noti.getSender().getEmail())
                        .from(noti.getSender().getName())
                        .userProfileImage(noti.getSender().getProfileImage())
                        .isFollowing(followRepository.existsByFromMemberAndToMember(userDetails.getMember(), noti.getSender()))
                        .createAt(LocalDateTime.now())
                        .isRead(noti.getIsRead())
                        .build()).toList();

        PageResponseDTO<NoteInfoResponseDTO> response = PageResponseDTO.<NoteInfoResponseDTO>builder()
                .data(noteInfoResponseDTOList)
                .page(pageResult.getNumber())
                .hasNext(pageResult.hasNext())
                .hasPrev(pageResult.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .message("활동기록 조회")
                .body(response)
                .build();
    }

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시에 실행
    @Transactional
    public void deleteOldNotifications() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteByCreatedAtBefore(threshold);
    }

    public SseEmitter subscribe(String email) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 무한 유지
        emitters.put(email, emitter);

        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));
        emitter.onError((e) -> emitters.remove(email));

        // 연결 확인을 위한 더미 데이터
        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    // 알림을 언제 줘야하는지 정해야됨.
    public void sendNotification(String email, NoteInfoResponseDTO noteInfoResponseDTO) {
        SseEmitter emitter = emitters.get(email);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .data(noteInfoResponseDTO));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(email);
            }
        }
    }

    @Override
    public ResponseDTO getUnRead(UserDetailsImpl userDetails) {
        Long unReadNumber = notificationRepository.countUnreadNotifications(userDetails.getMember());

        return ResponseDTO.builder()
                .message("isRead 하지않은 결과")
                .body(unReadNumber)
                .build();
    }

    @Override
    public ResponseDTO updateUnRead(UserDetailsImpl userDetails, Long nno) {
        Notification notification = notificationRepository.findByNno(nno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_NOTIFICATION));
        notification.changeRead(true);
        notificationRepository.save(notification);

        Long unReadNumber = notificationRepository.countUnreadNotifications(userDetails.getMember());

        return ResponseDTO.builder()
                .message("해당 nno isRead 수정완료")
                .body(unReadNumber)
                .build();
    }

    @Override
    public ResponseDTO disconnect(String email) {
        SseEmitter emitter = emitters.remove(email);
        if (emitter != null) {
            emitter.complete();
            return ResponseDTO.builder()
                    .message("SSE 연결이 해제되었습니다.")
                    .build();
        } else {
            return ResponseDTO.builder()
                    .message("이미 해제되었거나 존재하지 않는 SSE 연결입니다.")
                    .build();
        }
    }
}
