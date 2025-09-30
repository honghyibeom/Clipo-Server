package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.service.boardlike.BoardLikeService;
import myproject.cliposerver.service.bookmark.BookmarkService;
import myproject.cliposerver.service.replylike.ReplyLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookMarkController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 추가",description = "북마크 추가 api")
    @PostMapping("/insert")
    public ResponseEntity<ResponseDTO> bookmark(@RequestParam Long bno,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(bookmarkService.bookmark(bno, userDetails));
    }
    @Operation(summary = "북마크 삭제",description = "북마크 삭제 api")
    @PostMapping("/delete")
    public ResponseEntity<ResponseDTO> unBookmark(@RequestParam Long bno,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(bookmarkService.unBookmark(bno, userDetails));
    }

    @Operation(summary = "북마크 조회 리스트",description = "북마크한 유저 조회 리스트 api")
    @GetMapping("/get/users/{page}")
    public ResponseEntity<ResponseDTO> getBookmarkUser(@RequestParam("bno") Long bno,
                                                       @PathVariable("page") int page,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(bookmarkService.bookmarkMemberList(bno,page,userDetails));
    }

}