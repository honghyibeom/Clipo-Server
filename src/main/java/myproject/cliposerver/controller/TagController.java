package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "테그 검색", description = "테그를 검색하는 api")
    @GetMapping("/get/{page}/")
    public ResponseEntity<ResponseDTO> getTagForSearch(@PathVariable("page") int page,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @RequestParam(required = false, defaultValue = "") String search) {

        return ResponseEntity.ok(tagService.getTagForSearch(page, userDetails, search));
    }
}
