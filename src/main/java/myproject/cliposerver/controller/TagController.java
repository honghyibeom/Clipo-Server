package myproject.cliposerver.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.service.tag.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tag")
public class TagController {
    private final TagService tagService;

    @GetMapping("/get/{page}/")
    public ResponseEntity<ResponseDTO> getTagForSearch(@PathVariable("page") int page,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @RequestParam(required = false, defaultValue = "") String search) {

        return ResponseEntity.ok(tagService.getTagForSearch(page, userDetails, search));
    }
}
