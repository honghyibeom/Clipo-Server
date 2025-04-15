package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoRequestDTO;
import myproject.cliposerver.repository.NotificationRepository;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;


    @Operation(summary = "알림 생성 요청",description = "좋아요, 댓글, 대댓글을 달 때 관련된 사람에게 알림")
    @PostMapping(value = "/create")
    public ResponseEntity<ResponseDTO> updateProfileNickname(@RequestBody NoteInfoRequestDTO noteInfoRequestDTO,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.insertNotification(noteInfoRequestDTO, userDetails));
    }
}
