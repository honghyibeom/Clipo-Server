package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.data.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/wakeUp")
public class ConnectController {
    @Operation(summary = "서버 연결", description = "서버연결")
    @GetMapping("")
    public ResponseEntity<ResponseDTO> wakeUp() {
        return ResponseEntity.ok(ResponseDTO.builder().message("서버 일어남").build());
    }
}
