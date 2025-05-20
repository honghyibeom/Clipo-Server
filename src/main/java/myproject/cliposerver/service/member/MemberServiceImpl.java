package myproject.cliposerver.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.UpdatePasswordRequestDTO;
import myproject.cliposerver.data.dto.member.UpdateUserInfoRequestDTO;
import myproject.cliposerver.data.dto.member.UserInfoDetailsResponseDTO;
import myproject.cliposerver.data.dto.member.UserInfoResponseDTO;
import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.MemberRepository;
import myproject.cliposerver.service.Image.S3ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final S3ImageService imageService;

    @Transactional
    public ResponseDTO updateProfileNickname(String username,
                                             MultipartFile multipartFile,
                                             UserDetailsImpl userDetails){
        Optional<Member> optionalUser = memberRepository.findByName(username);
        if (optionalUser.isPresent()) {
            throw new CustomException(ErrorCode.EXIST_NICKNAME);
        }
        Member member = getUser(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        if (multipartFile != null && !multipartFile.isEmpty()){
            String result = imageService.uploadFile(multipartFile);
            member.changeProfileImage(result);
        }
        member.changeName(username);
        memberRepository.save(member);

        return ResponseDTO.builder()
                .message("프로필 이미지 및 닉네임 변경 완료")
                .build();
    }

    @Transactional
    public ResponseDTO updatePassword(UpdatePasswordRequestDTO updatePasswordRequestDTO,
                                      UserDetailsImpl userDetails) {

        if (!passwordEncoder.matches(updatePasswordRequestDTO.getOldPassword(), userDetails.getMember().getPassword())) {
            throw new CustomException(ErrorCode.NOT_EQUALS_PASSWORD);
        }
        Member member = getUser(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        member.changePassword(passwordEncoder.encode(updatePasswordRequestDTO.getNewPassword()));
        memberRepository.save(member);
        return ResponseDTO.builder()
                .message("비밀번호 수정 완료")
                .build();
    }

    public ResponseDTO getUserInformation(UserDetailsImpl userDetails) {
        Member member = getUser(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.builder()
                .email(member.getEmail())
                .nickName(member.getName())
                .profilePicture(member.getProfileImage())
                .build();

        return ResponseDTO.builder()
                .message("유저정보를 확인했습니다.")
                .body(userInfoResponseDTO)
                .build();
    }

    public ResponseDTO getUserDetailsInformation(String username, UserDetailsImpl userDetails) {
        // 상대의 정보
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        Optional<Follow> follow = followRepository.findByFromMemberAndToMember(userDetails.getMember(), member);
        //팔로우가 되어있다면 true, 되어있지 않다면 false
        boolean isFollow = follow.isPresent();

        UserInfoDetailsResponseDTO userResponseDTO = UserInfoDetailsResponseDTO.builder()
                .email(member.getEmail())
                .nickName(member.getName())
                .profilePicture(member.getProfileImage())
                .backgroundPicture(member.getBackgroundImage())
                .location(member.getLocation())
                .description(member.getDescription())
                .followingNumber(followRepository.countByFromMember(member))
                .followerNumber(followRepository.countByToMember(member))
                .brithDay(member.getBirth())
                .isFollowing(isFollow)
                .build();

        return ResponseDTO.builder()
                .message("유저정보를 확인했습니다.")
                .body(userResponseDTO)
                .build();
    }

    @Override
    @Transactional
    public ResponseDTO updateUserInfo(UserDetailsImpl userDetails, UpdateUserInfoRequestDTO requestDTO,
                                      MultipartFile profileImage, MultipartFile bgImage) {
        Member member = getUser(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        // 빈 문자열도 null로 처리해서 무시하도록 변환
        String nickName = convertBlankToNull(requestDTO.getNickName());
        String location = convertBlankToNull(requestDTO.getLocation());
        String description = convertBlankToNull(requestDTO.getDescription());
        String birthday = convertBlankToNull(requestDTO.getBirthday());

        if (nickName != null) {
            member.changeName(nickName);
        }
        if (location != null) {
            member.changeLocation(location);
        }
        if (description != null) {
            member.changeDescription(description);
        }
        if (birthday != null) {
            member.changeBirth(birthday);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            if (member.getProfileImage() != null) {
                imageService.deleteFile(member.getProfileImage());
            }
            String result = imageService.uploadFile(profileImage);
            member.changeProfileImage(result);
        }
        if (bgImage != null && !bgImage.isEmpty()) {
            if (member.getBackgroundImage() != null) {
                imageService.deleteFile(member.getBackgroundImage());
            }
            String result = imageService.uploadFile(bgImage);
            member.changeBackgroundImage(result);
        }
        memberRepository.save(member);

        return ResponseDTO.builder()
                .message("유저편집 완료")
                .build();
    }

    @Override
    public ResponseDTO getUserForSearch(int page, UserDetailsImpl userDetails, String search) {
        //유저 가져오기
        PageRequest pageRequest = PageRequest.of(page, 6);
        Page<Member> memberPages = memberRepository.findBySearch(search + "%", pageRequest);

//        if (memberPages.isEmpty()) {
//            return ResponseDTO.builder()
//                    .message("유저가 없습니다.")
//                    .body(Collections.emptyList()) // 무한 스크롤할 때 array의 갯수로 판단을 한다해서 빈 배열을 보냄
//                    .build();
//        }
        List<Member> result = memberPages.getContent();

        List<UserInfoResponseDTO> responseList = new ArrayList<>();
        for (Member member : result) {
            UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.builder()
                    .email(member.getEmail())
                    .nickName(member.getName())
                    .profilePicture(member.getProfileImage())
                    .build();
            responseList.add(userInfoResponseDTO);
        }

        PageResponseDTO<UserInfoResponseDTO> response = PageResponseDTO.<UserInfoResponseDTO>builder()
                .data(responseList)
                .page(memberPages.getNumber())
                .hasNext(memberPages.hasNext())
                .hasPrev(memberPages.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .body(response)
                .message("유저 검색 결과")
                .build();
    }

    private String convertBlankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }

    private Optional<Member> getUser(String email) {
        return memberRepository.findByEmail(email);
    }

}
