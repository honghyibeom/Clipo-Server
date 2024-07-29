package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyRequestDTO;
import myproject.cliposerver.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reply")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/insert")
    public ResponseEntity<ResponseDTO> insertReply(@RequestBody ReplyRequestDTO replyRequestDTO,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.createReply(replyRequestDTO, userDetails));
    }
    @PostMapping("/insert/child")
    public ResponseEntity<ResponseDTO> insertChildReply(@RequestBody ReplyRequestDTO replyRequestDTO,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.createChildReply(replyRequestDTO, userDetails));
    }
    @PostMapping("/update")
    public ResponseEntity<ResponseDTO> updateReply(@RequestBody ReplyRequestDTO replyRequestDTO,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.updateReply(replyRequestDTO, userDetails));
    }
    @PostMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteReply(@RequestParam Long rno,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(replyService.deleteReply(rno, userDetails));
    }
}
