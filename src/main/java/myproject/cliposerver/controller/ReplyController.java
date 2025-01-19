package myproject.cliposerver.controller;

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

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> insertReply(@ModelAttribute ReplyRequestDTO replyRequestDTO,
                                                   @RequestPart(value = "commentImage", required = false) MultipartFile commentImage,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.createReply(replyRequestDTO, userDetails, commentImage));
    }
    @PatchMapping("/update")
    public ResponseEntity<ResponseDTO> updateReply(@ModelAttribute ReplyRequestDTO replyRequestDTO,
                                                   @RequestPart(value = "commentImage", required = false) MultipartFile commentImage,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.updateReply(replyRequestDTO, userDetails, commentImage));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteReply(@RequestParam Long rno,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.deleteReply(rno, userDetails));
    }

    @GetMapping("/detail/{page}")
    public ResponseEntity<ResponseDTO> getDetailReply(@RequestParam("bno") Long bno,
                                                      @PathVariable("page") int page,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getDetailReply(bno,page,userDetails));
    }

    @GetMapping("/detail/nest/{page}")
    public ResponseEntity<ResponseDTO> getDetailChildReply(@RequestParam("rno") Long rno,
                                                           @PathVariable("page") int page,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getDetailChildReply(rno,page, userDetails));
    }

    @GetMapping("/one/")
    public ResponseEntity<ResponseDTO> getDetailOneReply(@RequestParam("rno") Long rno,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.getDetailOneReply(rno, userDetails));
    }
}
