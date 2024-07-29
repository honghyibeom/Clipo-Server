package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import myproject.cliposerver.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/posting")
    public ResponseEntity<ResponseDTO> posting(@RequestBody BoardRequestDTO boardRequestDTO,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.createBoard(boardRequestDTO, userDetails));
    }
    @PostMapping("/update")
    public ResponseEntity<ResponseDTO> update(@RequestBody BoardRequestDTO boardRequestDTO,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.update(boardRequestDTO,userDetails));
    }
    @PostMapping("/delete")
    public ResponseEntity<ResponseDTO> delete(@RequestParam Long bno,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(boardService.delete(bno, userDetails));
    }

}
