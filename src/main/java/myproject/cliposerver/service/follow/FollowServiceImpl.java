package myproject.cliposerver.service.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LittleUserInfoResponseDTO;
import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Notification;
import myproject.cliposerver.data.enumerate.NoteEnum;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.MemberRepository;
import myproject.cliposerver.repository.NotificationRepository;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@Log4j2
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Transactional
    public ResponseDTO follow(String toMemberEmail, UserDetailsImpl userDetails) {
        Member member = memberRepository.findByName(toMemberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        Follow follow = Follow.builder()
                .fromMember(userDetails.getMember())
                .toMember(member)
                .build();
        followRepository.save(follow);

        //알림테이블에 추가
        insertNotification(member, userDetails);

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

        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Follow> followingPage = followRepository.findByToMember(member,pageRequest);
        PageResponseDTO<LittleUserInfoResponseDTO> response = getUserInfo(userDetails, member, followingPage);

        return ResponseDTO.builder()
                .message("팔로워 조회")
                .body(response)
                .build();
    }

    @Override
    public ResponseDTO getUserFollowing(String username, int page, UserDetailsImpl userDetails) {

        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Follow> followerPage = followRepository.findByFromMember(member,pageRequest);
        PageResponseDTO<LittleUserInfoResponseDTO> response = getUserInfo(userDetails, member, followerPage);

        return ResponseDTO.builder()
                .message("팔로잉 조회")
                .body(response)
                .build();
    }

    private PageResponseDTO<LittleUserInfoResponseDTO> getUserInfo(UserDetailsImpl userDetails, Member member, Page<Follow> followerPage) {
        List<Follow> result = followerPage.getContent();

        List<LittleUserInfoResponseDTO> responseList = result.stream().map(follow ->
                LittleUserInfoResponseDTO.builder()
                        .profilePicture(follow.getFromMember().getProfileImage())
                        .nickName(follow.getFromMember().getName())
                        .email(follow.getFromMember().getEmail())
                        .isFollowing(followRepository.existsByFromMemberAndToMember(userDetails.getMember(),member))
                        .build()
        ).toList();

        return PageResponseDTO.<LittleUserInfoResponseDTO>builder()
                .data(responseList)
                .page(followerPage.getNumber())
                .hasNext(followerPage.hasNext())
                .hasPrev(followerPage.hasPrevious())
                .build();
    }

    private void insertNotification(Member toMember, UserDetailsImpl userDetails) {
        Notification notification = Notification.builder()
                .type(NoteEnum.follow.name())
                .sender(userDetails.getMember())
                .receiver(toMember)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

        // 알림 전달
        notificationService.sendNotification(toMember.getEmail(), "notification");
    }
}
