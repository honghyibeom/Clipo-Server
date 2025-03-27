package myproject.cliposerver.controller;

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

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> posting(@ModelAttribute BoardRequestDTO boardRequestDTO,
                                               @RequestPart(value = "boardImages", required = false) List<MultipartFile> boardImages,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.createBoard(boardRequestDTO,boardImages ,userDetails));
    }
    @PatchMapping("/update")
    public ResponseEntity<ResponseDTO> update(@ModelAttribute BoardRequestDTO boardRequestDTO,
                                              @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.update(boardRequestDTO,newImages,userDetails));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> delete(@RequestParam Long bno,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.delete(bno, userDetails));
    }

    @GetMapping("/postInfo/{username}/post/{page}")
    public ResponseEntity<ResponseDTO> getMyBoardList(@PathVariable("page") int page,
                                                      @PathVariable String username,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getMyBoardResponse(page,username,userDetails));
    }
    @GetMapping("/postInfo/{username}/replies/{page}")
    public ResponseEntity<ResponseDTO> getMyRepliesList(@PathVariable("page") int page,
                                                        @PathVariable String username,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getMyReplyResponse(page,username,userDetails));
    }
    @GetMapping("/postInfo/{username}/likes/{page}")
    public ResponseEntity<ResponseDTO> getMyLikesList(@PathVariable("page") int page,
                                                      @PathVariable String username,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getMyLikesResponse(page,username,userDetails));
    }

    @GetMapping("/randomBoard/{page}")
    public ResponseEntity<ResponseDTO> getBoardRandomList(@PathVariable("page") int page,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getRandomBoard(page,userDetails));
    }

    @GetMapping("/detail/")
    public ResponseEntity<ResponseDTO> getDetailBoard(@RequestParam Long bno,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.getDetailBoard(bno, userDetails));
    }
    @GetMapping("/get/tag/{page}/")
    public ResponseEntity<ResponseDTO> getBoardForTag(@PathVariable("page") int page,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestParam(required = false, defaultValue = "") String search) {
        return ResponseEntity.ok(boardService.getBoardForTag(page, userDetails, search));
    }

}
