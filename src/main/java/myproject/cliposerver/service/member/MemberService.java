package myproject.cliposerver.service.member;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.UpdatePasswordRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateProfileNicknameRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateUserInfoRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MemberService {
    ResponseDTO updateProfileNickname(String username, MultipartFile multipartFile, UserDetailsImpl userDetails);
    ResponseDTO updatePassword(UpdatePasswordRequestDTO updatePasswordRequestDTO, UserDetailsImpl userDetails);
    ResponseDTO getUserInformation(UserDetailsImpl userDetails);
    ResponseDTO getUserDetailsInformation(String email);
    ResponseDTO updateUserInfo(UserDetailsImpl userDetails, UpdateUserInfoRequestDTO requestDTO, MultipartFile profileImage, MultipartFile bgImage);

}
