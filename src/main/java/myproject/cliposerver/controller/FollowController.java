package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.service.follow.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/following")
    public ResponseEntity<ResponseDTO> following(@RequestParam String toMemberEmail,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.follow(toMemberEmail, userDetails));
    }
    @PostMapping("/unfollow")
    public ResponseEntity<ResponseDTO> unfollow(@RequestParam String toMemberEmail,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.unfollow(toMemberEmail, userDetails));
    }

}
