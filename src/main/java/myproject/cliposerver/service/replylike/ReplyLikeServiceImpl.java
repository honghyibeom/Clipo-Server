package myproject.cliposerver.service.replylike;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LittleUserInfoResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoSSEDTO;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Notification;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.data.entity.ReplyLike;
import myproject.cliposerver.data.enumerate.NoteEnum;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.NotificationRepository;
import myproject.cliposerver.repository.ReplyLikeRepository;
import myproject.cliposerver.repository.ReplyRepository;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyLikeServiceImpl implements ReplyLikeService {
    private final ReplyRepository replyRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final FollowRepository followRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Transactional
    public ResponseDTO like(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        ReplyLike replyLike = ReplyLike.builder()
                .member(userDetails.getMember())
                .reply(reply)
                .build();
        replyLikeRepository.save(replyLike);

        //알림테이블에 추가
        insertNotification(userDetails.getMember(), reply);

        return ResponseDTO.builder()
                .message("좋아요 완료")
                .build();
    }

    @Transactional
    public ResponseDTO unlike(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));
        replyLikeRepository.deleteByMemberAndReply(userDetails.getMember(), reply);

        return ResponseDTO.builder()
                .message("좋아요 취소 완료")
                .build();
    }

    @Override
    public ResponseDTO replyLikeList(Long rno, int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<ReplyLike> replyLikePage = replyLikeRepository.getReplyLikeByReply_Rno(rno, pageRequest);

        List<LittleUserInfoResponseDTO> responseDTOS = new ArrayList<>();
        List<ReplyLike> result = replyLikePage.getContent();
        for (ReplyLike replyLike : result ) {
            LittleUserInfoResponseDTO littleUserInfoResponseDTO = LittleUserInfoResponseDTO.builder()
                    .profilePicture(replyLike.getMember().getProfileImage())
                    .nickName(replyLike.getMember().getName())
                    .email(replyLike.getMember().getEmail())
                    .isFollowing(followRepository.
                            existsByFromMemberAndToMember(userDetails.getMember(), replyLike.getMember()))
                    .build();
            responseDTOS.add(littleUserInfoResponseDTO);
        }

        PageResponseDTO<LittleUserInfoResponseDTO> response = PageResponseDTO.<LittleUserInfoResponseDTO>builder()
                .data(responseDTOS)
                .page(replyLikePage.getNumber())
                .hasNext(replyLikePage.hasNext())
                .hasPrev(replyLikePage.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .message("replyLike 유저들 목록 확인")
                .body(response)
                .build();
    }
    private void insertNotification(Member sender, Reply reply) {

        if (!sender.equals(reply.getWriter())) {
            Notification notification = Notification.builder()
                    .type(NoteEnum.like)
                    .sender(sender)
                    .board(reply.getBoard())
                    .reply(reply)
                    .receiver(reply.getWriter())
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);

            NoteInfoSSEDTO sseDTO = NoteInfoSSEDTO.builder()
                    .type(notification.getType())
                    .from(sender.getName())
                    .bno(reply.getBoard().getBno())
                    .rno(reply.getRno())
                    .build();

            // 알림 전달
            notificationService.sendNotification(reply.getWriter().getEmail(),
                    NoteInfoResponseDTO.builder()
                            .type(NoteEnum.like.name())
                            .bno(reply.getBoard().getBno())
                            .boardOneImage(null)
                            .rno(reply.getRno())
                            .email(sender.getEmail())
                            .from(sender.getName())
                            .userProfileImage(sender.getProfileImage())
                            .isFollowing(notification.getType().equals(NoteEnum.follow) ?
                                    followRepository.existsByFromMemberAndToMember(sender, reply.getWriter()) : null)
                            .createAt(LocalDateTime.now())
                            .isRead(false)
                            .build()
                    );
        }
    }
}


