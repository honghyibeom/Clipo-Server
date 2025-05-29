package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.service.follow.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FollowAPI", description = "팔로우 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "팔로우",description = "팔로잉 api")
    @PostMapping("/following/{username}")
    public ResponseEntity<ResponseDTO> following(@PathVariable("username") String toMemberUsername,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.follow(toMemberUsername, userDetails));
    }

    @Operation(summary = "팔로우 취소",description = "언팔로우 api")
    @PostMapping("/unFollowing/{username}")
    public ResponseEntity<ResponseDTO> unfollow(@PathVariable("username") String toMemberUsername,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.unfollow(toMemberUsername, userDetails));
    }

    @Operation(summary = "팔로우 조회 리스트",description = "팔로우 조회 리스트 api")
    @GetMapping("/get/users/{username}/follower/{page}")
    public ResponseEntity<ResponseDTO> getFollow(@PathVariable("username") String username,
                                                 @PathVariable("page") int page,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.getUserFollower(username,page,userDetails));
    }

    @Operation(summary = "팔로잉 조회 리스트",description = "팔로잉 조회 리스트 api")
    @GetMapping("/get/users/{username}/following/{page}")
    public ResponseEntity<ResponseDTO> getFollowing(@PathVariable("username") String username,
                                                    @PathVariable("page") int page,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(followService.getUserFollowing(username,page,userDetails));
    }
}
