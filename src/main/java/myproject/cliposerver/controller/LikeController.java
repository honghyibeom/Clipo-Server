package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.service.boardlike.BoardLikeService;
import myproject.cliposerver.service.replylike.ReplyLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final BoardLikeService boardLikeService;
    private final ReplyLikeService replyLikeService;

    @PostMapping("/api/boardLike/like/")
    public ResponseEntity<ResponseDTO> boardLike(@RequestParam Long bno,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardLikeService.like(bno, userDetails));
    }
    @PostMapping("/api/boardLike/unlike/")
    public ResponseEntity<ResponseDTO> boardUnlike(@RequestParam Long bno,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardLikeService.unlike(bno, userDetails));
    }
    @PostMapping("/api/replyLike/like/")
    public ResponseEntity<ResponseDTO> ReplyLike(@RequestParam Long rno,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(replyLikeService.like(rno, userDetails));
    }
//    @PostMapping("/api/replyLike/unlike/")
//    public ResponseEntity<ResponseDTO> ReplyUnlike(@RequestParam Long rno,
//                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ResponseEntity.ok(replyLikeService.unlike(rno, userDetails));
//    }
//
//    @GetMapping("/api/boardLike/list")
//    public ResponseEntity<ResponseDTO> ReplyUnlike(@RequestParam Long rno,
//                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ResponseEntity.ok(replyLikeService.unlike(rno, userDetails));
//    }


}
