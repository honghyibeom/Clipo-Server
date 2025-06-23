package myproject.cliposerver.service.notification;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoSSEDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public interface NotificationService {
    //활동기록 조회
    ResponseDTO getNotification(UserDetailsImpl userDetails, int page);
    //sse연결
    SseEmitter subscribe(String email);
    //sse전송
    void sendNotification(String email, NoteInfoResponseDTO noteInfoSSEDTO);
    //최초 unRead 조회
    ResponseDTO getUnRead(UserDetailsImpl userDetails);
    // unRead 수정
    ResponseDTO updateUnRead(UserDetailsImpl userDetails, Long nno);


}
