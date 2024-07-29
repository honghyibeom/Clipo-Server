package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import myproject.cliposerver.service.BoardLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boardLike")
public class BoardLikeController {
    private final BoardLikeService boardLikeService;

    @PostMapping("/like")
    public ResponseEntity<ResponseDTO> like(@RequestParam Long bno,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardLikeService.like(bno, userDetails));
    }
    @PostMapping("/unlike")
    public ResponseEntity<ResponseDTO> unlike(@RequestParam Long bno,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardLikeService.unlike(bno, userDetails));
    }
}
