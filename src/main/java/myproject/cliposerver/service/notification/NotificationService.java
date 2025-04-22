package myproject.cliposerver.service.notification;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public interface NotificationService {
    //활동기록 조회
    ResponseDTO getNotification(UserDetailsImpl userDetails);

    //sse연결
    SseEmitter subscribe(UserDetailsImpl userDetails);

    //sse전송
    void sendNotification(String email, String message);
}
