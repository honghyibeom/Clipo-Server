package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "게시글 좋아요 기능",description = "게시글 좋아요 api")
    @PostMapping("/api/boardLike/like/")
    public ResponseEntity<ResponseDTO> boardLike(@RequestParam Long bno,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardLikeService.like(bno, userDetails));
    }
    @Operation(summary = "게시글 좋아요 취소 기능",description = "게시글 좋아요 취소 api")
    @PostMapping("/api/boardLike/unlike/")
    public ResponseEntity<ResponseDTO> boardUnlike(@RequestParam Long bno,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardLikeService.unlike(bno, userDetails));
    }
    @Operation(summary = "댓글 좋아요 기능",description = "댓글 좋아요 api")
    @PostMapping("/api/replyLike/like/")
    public ResponseEntity<ResponseDTO> ReplyLike(@RequestParam Long rno,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(replyLikeService.like(rno, userDetails));
    }
    @Operation(summary = "댓글 좋아요 취소 기능",description = "댓글 좋아요 취소 api")
    @PostMapping("/api/replyLike/unlike/")
    public ResponseEntity<ResponseDTO> ReplyUnlike(@RequestParam Long rno,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(replyLikeService.unlike(rno, userDetails));
    }

    @Operation(summary = "좋아요 유저들 조회",description = "게시글 좋아요 누른 유저들 조회 api")
    @GetMapping("/api/boardLike/get/users/{page}/")
    public ResponseEntity<ResponseDTO> getBoardLikeList(@RequestParam("bno") Long bno,
                                                        @PathVariable("page") int page,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardLikeService.boardLikeList(bno,page,userDetails));
    }

    @Operation(summary = "좋아요 유저들 조회",description = "댓글 좋아요 누른 유저들 조회 api")
    @GetMapping("/api/replyLike/get/users/{page}")
    public ResponseEntity<ResponseDTO> getReplyLikeList(@RequestParam("rno") Long rno,
                                                        @PathVariable("page") int page,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(replyLikeService.replyLikeList(rno,page,userDetails));
    }


}