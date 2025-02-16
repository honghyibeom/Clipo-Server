package myproject.cliposerver.service.replylike;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.member.LittleUserInfoResponseDTO;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.data.entity.ReplyLike;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.FollowRepository;
import myproject.cliposerver.repository.ReplyLikeRepository;
import myproject.cliposerver.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyLikeServiceImpl implements ReplyLikeService {
    private final ReplyRepository replyRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final FollowRepository followRepository;

    @Transactional
    public ResponseDTO like(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        ReplyLike replyLike = ReplyLike.builder()
                .member(userDetails.getMember())
                .reply(reply)
                .build();
        replyLikeRepository.save(replyLike);

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

        return ResponseDTO.builder()
                .message("boardLike 유저들 목록 확인")
                .body(responseDTOS)
                .build();
    }
}


