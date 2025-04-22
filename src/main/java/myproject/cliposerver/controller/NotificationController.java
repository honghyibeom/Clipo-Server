package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 기록 조회",description = "알림 기록 조회기능")
    @GetMapping(value = "/get")
    public ResponseEntity<ResponseDTO> getNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getNotification(userDetails));
    }

    @GetMapping(value = "/subscribe")
    public SseEmitter ringNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.subscribe(userDetails);
    }
}
