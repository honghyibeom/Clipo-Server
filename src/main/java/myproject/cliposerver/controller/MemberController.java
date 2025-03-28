package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.UpdatePasswordRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateUserInfoRequestDTO;
import myproject.cliposerver.service.member.MemberService;
import myproject.cliposerver.service.tag.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "프로필, 닉네임 변경",description = "프로필과 닉네임이 없으면 나타나는 api")
    @PostMapping(value = "/update/profileNickname")
    public ResponseEntity<ResponseDTO> updateProfileNickname(@RequestPart("nickName") String username,
                                                             @RequestPart(value = "files", required = false) MultipartFile file,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updateProfileNickname(username,file,userDetails));
    }

    @Operation(summary = "페스워드 변경",description = "페스워드 update api")
    @PostMapping("/update/password")
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody UpdatePasswordRequestDTO updatePasswordRequestDTO,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updatePassword(updatePasswordRequestDTO, userDetails));
    }

    @Operation(summary = "사용자 정보 조회", description = "특정 사용자의 상세 정보를 조회하는 API")
    @GetMapping("/get/userInformation/{username}")
    public ResponseEntity<ResponseDTO> userInformationDetails(@PathVariable String username,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.getUserDetailsInformation(username, userDetails));
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회하는 API")
    @GetMapping("/get/user/information")
    public ResponseEntity<ResponseDTO> userInformation(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.getUserInformation(userDetails));
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자의 프로필 정보를 수정하는 API. 프로필 이미지 및 배경 이미지 변경 가능.")
    @PatchMapping("/update/userInformation")
    public ResponseEntity<ResponseDTO> updateUserInformation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @ModelAttribute UpdateUserInfoRequestDTO requestDTO,
                                                             @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                             @RequestPart(value = "backgroundImage", required = false) MultipartFile bgImage
                                                             ) {
        return ResponseEntity.ok(memberService.updateUserInfo(userDetails, requestDTO, profileImage, bgImage));
    }

    @Operation(summary = "사용자 검색", description = "검색어를 기반으로 사용자 목록을 조회하는 API")
    @GetMapping("/search/get/users/{page}/")
    public ResponseEntity<ResponseDTO> getUserForSearch(@PathVariable("page") int page,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam(required = false, defaultValue = "") String search) {
            return ResponseEntity.ok(memberService.getUserForSearch(page, userDetails, search));
        }
}
