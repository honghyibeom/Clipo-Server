package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.UpdatePasswordRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateUserInfoRequestDTO;
import myproject.cliposerver.service.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/update/profileNickname")
    public ResponseEntity<ResponseDTO> updateProfileNickname(@RequestPart("nickName") String username,
                                                             @RequestPart(value = "files", required = false) MultipartFile file,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updateProfileNickname(username,file,userDetails));
    }

    @PostMapping("/update/password")
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody UpdatePasswordRequestDTO updatePasswordRequestDTO,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updatePassword(updatePasswordRequestDTO, userDetails));
    }
    @GetMapping("/get/userInformation/{username}")
    public ResponseEntity<ResponseDTO> userInformationDetails(@PathVariable String username,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.getUserDetailsInformation(username, userDetails));
    }
    @GetMapping("/get/user/information")
    public ResponseEntity<ResponseDTO> userInformation(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.getUserInformation(userDetails));
    }
    @PatchMapping("/update/userInformation")
    public ResponseEntity<ResponseDTO> updateUserInformation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @ModelAttribute UpdateUserInfoRequestDTO requestDTO,
                                                             @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                             @RequestPart(value = "backgroundImage", required = false) MultipartFile bgImage
                                                             ) {
        return ResponseEntity.ok(memberService.updateUserInfo(userDetails, requestDTO, profileImage, bgImage));
    }

    @GetMapping("/search/get/users/{page}/")
    public ResponseEntity<ResponseDTO> getUserForSearch(@PathVariable("page") int page,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam(required = false, defaultValue = "") String search) {
        return ResponseEntity.ok(memberService.getUserForSearch(page,userDetails,search));
    }




}
