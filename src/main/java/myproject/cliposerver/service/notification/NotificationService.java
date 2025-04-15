package myproject.cliposerver.service.notification;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoRequestDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    ResponseDTO insertNotification(NoteInfoRequestDTO noteInfoRequestDTO, UserDetailsImpl userDetails);
}
