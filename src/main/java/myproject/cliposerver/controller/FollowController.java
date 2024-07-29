package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.follow.FollowRequestDTO;
import myproject.cliposerver.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/following")
    public ResponseEntity<ResponseDTO> following(@RequestBody FollowRequestDTO followRequestDTO,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.follow(followRequestDTO, userDetails));
    }
    @PostMapping("/unfollow")
    public ResponseEntity<ResponseDTO> unfollow(@RequestBody FollowRequestDTO followRequestDTO,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.unfollow(followRequestDTO, userDetails));
    }
    @GetMapping("/followList") // 구현중
    public ResponseEntity<ResponseDTO> getFollowList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.getFollowerList(userDetails));
    }

}
