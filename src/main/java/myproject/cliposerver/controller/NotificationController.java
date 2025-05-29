package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "NotificationAPI", description = "알림 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification/activity")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 기록 조회",description = "알림 기록 조회기능")
    @GetMapping(value = "/get/all/{pages}")
    public ResponseEntity<ResponseDTO> getNotification(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable int pages) {
        return ResponseEntity.ok(notificationService.getNotification(userDetails, pages));
    }
    @GetMapping(value = "/subscribe")
    public SseEmitter ringNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.subscribe(userDetails);
    }

    @Operation(summary = "최초 isRead 배열",description = "로그인시 읽지 않은 알림의 갯수 전달")
    @GetMapping(value = "/get/unRead")
    public ResponseEntity<ResponseDTO> getUnRead(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getUnRead(userDetails));
    }

    @Operation(summary = "isRead 배열 수정",description = "읽었을때 읽지 않은 알림의 갯수 전달 및 업데이트")
    @PatchMapping(value = "/patch/unRead/")
    public ResponseEntity<ResponseDTO> updateUnRead(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestParam Long nno) {
        return ResponseEntity.ok(notificationService.updateUnRead(userDetails, nno));
    }
}
