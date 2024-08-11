package myproject.cliposerver.service.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
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
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDTO follow(String toMemberEmail, UserDetailsImpl userDetails) {
        Member toMember = getUser(toMemberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        Follow follow = Follow.builder()
                .fromMember(userDetails.getMember())
                .toMember(toMember)
                .build();
        followRepository.save(follow);

        return ResponseDTO.builder()
                .message("팔로우 완료")
                .build();
    }

    @Transactional
    public ResponseDTO unfollow(String toMemberEmail, UserDetailsImpl userDetails) {
        Member toMember = getUser(toMemberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
        Follow follow = followRepository.findByFromMemberAndToMember(userDetails.getMember(), toMember)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_FOLLOW));

        followRepository.delete(follow);

        return ResponseDTO.builder()
                .message("언팔로우 완료")
                .build();
    }


    private Optional<Member> getUser(String email) {
        return memberRepository.findByEmail(email);
    }

}
