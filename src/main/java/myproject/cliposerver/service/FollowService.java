package myproject.cliposerver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.follow.FollowRequestDTO;
import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDTO follow(FollowRequestDTO followRequestDTO, UserDetailsImpl userDetails) {
        Member fromMember = getUser(followRequestDTO.getFromMemberEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
        Member toMember = getUser(followRequestDTO.getToMemberEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
        identification(fromMember.getEmail(), userDetails.getEmail());

        Follow follow = followRequestDTO.toEntity(fromMember, toMember);
        followRepository.save(follow);

        return ResponseDTO.builder()
                .message("팔로우 완료")
                .build();
    }

    @Transactional
    public ResponseDTO unfollow(FollowRequestDTO followRequestDTO, UserDetailsImpl userDetails) {
        Member fromMember = getUser(followRequestDTO.getFromMemberEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
        Member toMember = getUser(followRequestDTO.getToMemberEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        identification(fromMember.getEmail(), userDetails.getEmail());

        followRepository.deleteByFromMemberAndToMember(fromMember, toMember);

        return ResponseDTO.builder()
                .message("언팔로우 완료")
                .build();
    }

    public ResponseDTO getFollowerList(UserDetailsImpl userDetails){
        Member member = memberRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
//        List<Follow> followList =

        return ResponseDTO.builder()
                .message("팔로워 리스트 조회")
//                .body()
                .build();
    }

    public ResponseDTO getFollowingList(UserDetailsImpl userDetails){
        Member member = memberRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
//        List<Follow> followFromList =
        return ResponseDTO.builder()
                .message("팔로잉 리스트 조회")
//                .body()
                .build();
    }

    private void identification(String memberEmail, String userDetailsEmail) {
        if (!memberEmail.equals(userDetailsEmail)){
            throw new CustomException(ErrorCode.NOT_EQUALS_USER);
        }
    }

    private Optional<Member> getUser(String email) {
        return memberRepository.findByEmail(email);
    }

}
