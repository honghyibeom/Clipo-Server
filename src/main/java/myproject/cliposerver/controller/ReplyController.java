package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyRequestDTO;
import myproject.cliposerver.service.reply.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reply")
public class ReplyController {
    private final ReplyService replyService;

    @Operation(summary = "댓글 생성", description = "게시글에 새로운 댓글을 작성하는 API. 댓글 내용과 함께 이미지 첨부 가능.")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> insertReply(@ModelAttribute ReplyRequestDTO replyRequestDTO,
                                                   @RequestPart(value = "commentImage", required = false) MultipartFile commentImage,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.createReply(replyRequestDTO, userDetails, commentImage));
    }

    @Operation(summary = "댓글 수정", description = "기존 댓글의 내용을 수정하는 API. 새로운 이미지로 변경 가능.")
    @PatchMapping("/update")
    public ResponseEntity<ResponseDTO> updateReply(@ModelAttribute ReplyRequestDTO replyRequestDTO,
                                                   @RequestPart(value = "commentImage", required = false) MultipartFile commentImage,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.updateReply(replyRequestDTO, userDetails, commentImage));
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제하는 API.")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteReply(@RequestParam Long rno,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.deleteReply(rno, userDetails));
    }

    @Operation(summary = "게시글의 댓글 조회", description = "특정 게시글의 댓글 목록을 페이지네이션하여 조회하는 API.")
    @GetMapping("/detail/{page}")
    public ResponseEntity<ResponseDTO> getDetailReply(@RequestParam("bno") Long bno,
                                                      @PathVariable("page") int page,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getDetailReply(bno, page, userDetails));
    }

    @Operation(summary = "대댓글(답글) 조회", description = "특정 댓글의 대댓글(답글) 목록을 페이지네이션하여 조회하는 API.")
    @GetMapping("/detail/nest/{page}")
    public ResponseEntity<ResponseDTO> getDetailChildReply(@RequestParam("rno") Long rno,
                                                           @PathVariable("page") int page,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getDetailChildReply(rno, page, userDetails));
    }

    @Operation(summary = "단일 댓글 조회", description = "특정 댓글의 상세 정보를 조회하는 API.")
    @GetMapping("/one/")
    public ResponseEntity<ResponseDTO> getDetailOneReply(@RequestParam("rno") Long rno,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getDetailOneReply(rno, userDetails));
    }

    @Operation(summary = "댓글 페이지 번호 조회", description = "특정 댓글의 페이징 정보를 조회하는 API.")
    @GetMapping("/get/pageNumber/")
    public ResponseEntity<ResponseDTO> getPageNumberReply(@RequestParam("bno") Long bno,
                                                          @RequestParam("rno") Long rno,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getPageReply(bno, rno, userDetails));
    }

    @Operation(summary = "대댓글 페이지 번호 조회", description = "특정 대댓글의 페이징 정보를 조회하는 API.")
    @GetMapping("/get/pageNumber/nest/")
    public ResponseEntity<ResponseDTO> getPageNumberNestReply(@RequestParam("parentId") Long rno,
                                                              @RequestParam("targetId") Long nestRno,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getPageNestReply(rno, nestRno, userDetails));
    }

}
