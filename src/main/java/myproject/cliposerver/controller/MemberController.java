package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.UpdatePasswordRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateProfileNicknameRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateUserInfoRequestDTO;
import myproject.cliposerver.service.member.MemberService;
import org.springframework.http.MediaType;
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
    public ResponseEntity<ResponseDTO> updateProfileNickname(@RequestPart("username") String username,
                                                             @RequestPart(value = "file", required = false) MultipartFile file,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updateProfileNickname(username,file,userDetails));
    }

    @PostMapping("/update/password")
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody UpdatePasswordRequestDTO updatePasswordRequestDTO,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.updatePassword(updatePasswordRequestDTO, userDetails));
    }
    @GetMapping("/get/userInformation")
    public ResponseEntity<ResponseDTO> userInformationDetails(@RequestParam String email) {
        return ResponseEntity.ok(memberService.getUserDetailsInformation(email));
    }
    @GetMapping("/get/user/information")
    public ResponseEntity<ResponseDTO> userInformation(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(memberService.getUserInformation(userDetails));
    }
    @PostMapping("/update/userInformation")
    public ResponseEntity<ResponseDTO> updateUserInformation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @ModelAttribute UpdateUserInfoRequestDTO requestDTO,
                                                             @RequestPart(value = "profilePicture", required = false) MultipartFile profileImage,
                                                             @RequestPart(value = "backgroundPicture", required = false) MultipartFile bgImage
                                                             ) {
        return ResponseEntity.ok(memberService.updateUserInfo(userDetails, requestDTO, profileImage, bgImage));
    }


}
