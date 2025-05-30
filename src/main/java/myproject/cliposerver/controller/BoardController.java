package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import myproject.cliposerver.service.board.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "게시글 생성",description = "게시글 생성 api")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> posting(@ModelAttribute BoardRequestDTO boardRequestDTO,
                                               @RequestPart(value = "boardImages", required = false) List<MultipartFile> boardImages,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.createBoard(boardRequestDTO,boardImages ,userDetails));
    }
    @Operation(summary = "게시글 수정",description = "게시글 수정 api")
    @PatchMapping("/update")
    public ResponseEntity<ResponseDTO> update(@ModelAttribute BoardRequestDTO boardRequestDTO,
                                              @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.update(boardRequestDTO,newImages,userDetails));
    }
    @Operation(summary = "게시글 삭제",description = "게시글 삭제 api")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> delete(@RequestParam Long bno,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.delete(bno, userDetails));
    }
    @Operation(summary = "사용자의 게시글 목록 조회", description = "특정 사용자가 작성한 게시글 목록을 페이지별로 조회하는 API")
    @GetMapping("/postInfo/{username}/post/{page}")
    public ResponseEntity<ResponseDTO> getMyBoardList(@PathVariable("page") int page,
                                                      @PathVariable String username,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getMyBoardResponse(page,username,userDetails));
    }
    @Operation(summary = "사용자의 댓글 목록 조회", description = "특정 사용자가 작성한 댓글 목록을 페이지별로 조회하는 API")
    @GetMapping("/postInfo/{username}/replies/{page}")
    public ResponseEntity<ResponseDTO> getMyRepliesList(@PathVariable("page") int page,
                                                        @PathVariable String username,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getMyReplyResponse(page,username,userDetails));
    }

    @Operation(summary = "사용자가 좋아요한 게시글 목록 조회", description = "특정 사용자가 좋아요한 게시글 목록을 페이지별로 조회하는 API")
    @GetMapping("/postInfo/{username}/likes/{page}")
    public ResponseEntity<ResponseDTO> getMyLikesList(@PathVariable("page") int page,
                                                      @PathVariable String username,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getMyLikesResponse(page,username,userDetails));
    }

    @Operation(summary = "랜덤 게시글 목록 조회", description = "메인페이지에서 게시글 목록을 페이지별로 조회하는 API")
    @GetMapping("/randomBoard/{page}")
    public ResponseEntity<ResponseDTO> getBoardRandomList(@PathVariable("page") int page,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getRandomBoard(page,userDetails));
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글의 상세 내용을 조회하는 API")
    @GetMapping("/detail/")
    public ResponseEntity<ResponseDTO> getDetailBoard(@RequestParam Long bno,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getDetailBoard(bno, userDetails));
    }
    @Operation(summary = "태그 기반 게시글 검색", description = "특정 태그가 포함된 게시글 목록을 페이지별로 조회하는 API")
    @GetMapping("/get/tag/{page}/")
    public ResponseEntity<ResponseDTO> getBoardForTag(@PathVariable("page") int page,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestParam(required = false, defaultValue = "") String search) {
        return ResponseEntity.ok(boardService.getBoardForTag(page, userDetails, search));
    }

    @Operation(summary = "팔로잉 기반 게시글", description = "팔로잉한 게시글만 나오도록 함")
    @GetMapping("/get/postInfo/follow/{page}/")
    public ResponseEntity<ResponseDTO> getBoardForFollowing(@PathVariable("page") int page,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(boardService.getFollowingBoard(page, userDetails));
    }

}
