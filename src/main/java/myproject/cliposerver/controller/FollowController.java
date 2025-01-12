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

    @PostMapping("/following/{username}")
    public ResponseEntity<ResponseDTO> following(@PathVariable("username") String toMemberUsername,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.follow(toMemberUsername, userDetails));
    }
    @PostMapping("/unFollowing/{username}")
    public ResponseEntity<ResponseDTO> unfollow(@PathVariable("username") String toMemberUsername,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.unfollow(toMemberUsername, userDetails));
    }

    @GetMapping("/get/users/{username}/follower/{page}")
    public ResponseEntity<ResponseDTO> getFollow(@PathVariable("username") String username,
                                                 @PathVariable("page") int page,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.getUserFollower(username,page,userDetails));
    }

    @GetMapping("/get/users/{username}/following/{page}")
    public ResponseEntity<ResponseDTO> getFollowing(@PathVariable("username") String username,
                                                    @PathVariable("page") int page,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.getUserFollowing(username,page,userDetails));
    }

}
