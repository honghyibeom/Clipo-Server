package myproject.cliposerver.service.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LittleUserInfoResponseDTO;
import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.MemberRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Log4j2
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDTO follow(String toMemberEmail, UserDetailsImpl userDetails) {
        Member Member = memberRepository.findByName(toMemberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        Follow follow = Follow.builder()
                .fromMember(userDetails.getMember())
                .toMember(Member)
                .build();
        followRepository.save(follow);

        return ResponseDTO.builder()
                .message("팔로우 완료")
                .build();
    }

    @Transactional
    public ResponseDTO unfollow(String toMemberEmail, UserDetailsImpl userDetails) {
        Member Member = memberRepository.findByName(toMemberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        Follow follow = followRepository.findByFromMemberAndToMember(userDetails.getMember(), Member)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_FOLLOW));

        followRepository.delete(follow);

        return ResponseDTO.builder()
                .message("언팔로우 완료")
                .build();
    }

    @Override
    public ResponseDTO getUserFollower(String username, int page, UserDetailsImpl userDetails) {
        String gbn = "follower";
        List<LittleUserInfoResponseDTO> responseList = getFollowInfoResponseDTOS(username, page, userDetails, gbn);

        return ResponseDTO.builder()
                .message("팔로워 조회")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getUserFollowing(String username, int page, UserDetailsImpl userDetails) {
        String gbn = "following";
        List<LittleUserInfoResponseDTO> responseList = getFollowInfoResponseDTOS(username, page, userDetails, gbn);

        return ResponseDTO.builder()
                .message("팔로잉 조회")
                .body(responseList)
                .build();
    }

    @NotNull
    private List<LittleUserInfoResponseDTO> getFollowInfoResponseDTOS(String username, int page, UserDetailsImpl userDetails,
                                                                      String gbn) {
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Follow> followerPage = followRepository.findByFromMember(member,pageRequest);
        Page<Follow> followingPage = followRepository.findByToMember(member,pageRequest);

        List<LittleUserInfoResponseDTO> responseList = new ArrayList<>();

        if (gbn.equals("follower")){
            List<Follow> result = followingPage.getContent();
            for (Follow follow: result) {
                LittleUserInfoResponseDTO littleUserInfoResponseDTO = LittleUserInfoResponseDTO.builder()
                        .profilePicture(follow.getFromMember().getProfileImage())
                        .nickName(follow.getFromMember().getName())
                        .email(follow.getFromMember().getEmail())
                        .isFollowing(followRepository.existsByFromMemberAndToMember(userDetails.getMember(),member))
                        .build();
                responseList.add(littleUserInfoResponseDTO);
            }
        }
        else if (gbn.equals("following")) {
            List<Follow> result = followerPage.getContent();
            for (Follow follow: result) {
                LittleUserInfoResponseDTO littleUserInfoResponseDTO = LittleUserInfoResponseDTO.builder()
                        .profilePicture(follow.getToMember().getProfileImage())
                        .nickName(follow.getToMember().getName())
                        .email(follow.getToMember().getEmail())
                        .isFollowing(followRepository.existsByFromMemberAndToMember(userDetails.getMember(),member))
                        .build();
                responseList.add(littleUserInfoResponseDTO);
            }
        }
        return responseList;
    }
}
