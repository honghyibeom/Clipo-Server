package myproject.cliposerver.service.member;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.UpdatePasswordRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateUserInfoRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MemberService {
    // 회원가입시 프로필, 닉네임 업데이트 응답
    ResponseDTO updateProfileNickname(String username, MultipartFile multipartFile, UserDetailsImpl userDetails);
    // 비밀번호 변경
    ResponseDTO updatePassword(UpdatePasswordRequestDTO updatePasswordRequestDTO, UserDetailsImpl userDetails);
    // 유저 정보 조회(호버)
    ResponseDTO getUserInformation(UserDetailsImpl userDetails);
    // 유저 페이지 상세 조회
    ResponseDTO getUserDetailsInformation(String username, UserDetailsImpl userDetails);
    // 유저 정보 변경
    ResponseDTO updateUserInfo(UserDetailsImpl userDetails, UpdateUserInfoRequestDTO requestDTO, MultipartFile profileImage, MultipartFile bgImage);
    // 유저 검색
    ResponseDTO getUserForSearch(int page, UserDetailsImpl userDetails, String search);

}
