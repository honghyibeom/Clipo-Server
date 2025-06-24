package myproject.cliposerver.service.reply;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoResponseDTO;
import myproject.cliposerver.data.dto.notification.NoteInfoSSEDTO;
import myproject.cliposerver.data.dto.reply.PageNumberResponseDTO;
import myproject.cliposerver.data.dto.PageResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyInfoResponseDTO;
import myproject.cliposerver.data.dto.reply.ReplyRequestDTO;
import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Notification;
import myproject.cliposerver.data.entity.Reply;
import myproject.cliposerver.data.enumerate.NoteEnum;
import myproject.cliposerver.data.enumerate.TypeOfPost;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.*;
import myproject.cliposerver.service.Image.S3ImageService;
import myproject.cliposerver.service.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final S3ImageService imageService;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public ResponseDTO createReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails, MultipartFile commentImage) {
        Board board = boardRepository.findByBno(replyRequestDTO.getBno())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        // 자식댓
        if (replyRequestDTO.getParentRno() != null) {
            Reply parentReply = replyRepository.findById(replyRequestDTO.getParentRno())
                    .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));

            Reply reply = replyRequestDTO.toEntity(board, userDetails.getMember(), parentReply);

            if (commentImage != null) {
                String getImage = imageService.uploadFile(commentImage);
                reply.changeReplyImage(getImage);
            }
            replyRepository.save(reply);

            //알림 생성
            insertReplyCreateNotification(userDetails.getMember(), reply, replyRequestDTO.getMentions());

            return ResponseDTO.builder()
                    .message("자식 댓글 생성 완료")
                    .body(reply.getRno())
                    .build();
        }

        // 부모댓
        else {
            Reply reply = replyRequestDTO.toEntity(board, userDetails.getMember());
            if (commentImage != null) {
                String getImage = imageService.uploadFile(commentImage);
                reply.changeReplyImage(getImage);
            }
            replyRepository.save(reply);

            //알림 생성
            insertReplyCreateNotification(userDetails.getMember(), reply, replyRequestDTO.getMentions());

            return ResponseDTO.builder()
                    .message("댓글 생성 완료")
                    .body(reply.getRno())
                    .build();
        }
    }

    @Transactional
    public ResponseDTO updateReply(ReplyRequestDTO replyRequestDTO, UserDetailsImpl userDetails, MultipartFile commentImage) {
        Reply reply = replyRepository.findById(replyRequestDTO.getRno())
                        .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        identification(reply.getWriter().getEmail(), userDetails.getEmail());

        // 이미지 삭제 및 추가
        // 파일이 존재하면 삭제 후 추가
        if (commentImage != null) {
            if(replyRequestDTO.getOriginImage() != null) {
                imageService.deleteFile(reply.getReplyImage());
                String newImage = imageService.uploadFile(commentImage);
                reply.changeReplyImage(newImage);
            }
            else {
                //기존이미지가 존재하는 경우 예외처리
                 throw new CustomException(ErrorCode.EXIST_IMAGE);
            }
        }
        //파일이 존재하지 않는경우
        else {
            //기존이미지가 없는경우
            if(replyRequestDTO.getOriginImage() != null) {
                imageService.deleteFile(reply.getReplyImage());
                reply.changeReplyImage(null);
            }
        }

        //댓글 수정
        reply.changeText(replyRequestDTO.getContent());
        replyRepository.save(reply);

        return ResponseDTO.builder()
                .message("댓글 수정 완료")
                .build();
    }

    @Transactional
    public ResponseDTO deleteReply(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_REPLY));
        identification(reply.getWriter().getEmail(), userDetails.getEmail());

        if (reply.getReplyImage() != null) {
            imageService.deleteFile(reply.getReplyImage());
        }
        replyRepository.delete(reply);
        return ResponseDTO.builder()
                .message("댓글 삭제 완료")
                .build();
    }

    @Override
    public ResponseDTO getDetailReply(Long bno, int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Board board = boardRepository.findByBno(bno)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        Page<Reply> pages = replyRepository.findByBoardAndParentIsNullOrderByRegDateAsc(board, pageRequest);
        List<Reply> result = pages.getContent();

        List<ReplyInfoResponseDTO> responseList = new ArrayList<>();
        for (Reply reply : result) {
            ReplyInfoResponseDTO replyInfoResponseDTO = getReplyInfoResponseDTO(userDetails,reply);
            responseList.add(replyInfoResponseDTO);
        }

        PageResponseDTO<ReplyInfoResponseDTO> response = PageResponseDTO.<ReplyInfoResponseDTO>builder()
                .data(responseList)
                .page(pages.getNumber())
                .hasNext(pages.hasNext())
                .hasPrev(pages.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .message("댓글을 확인했습니다.")
                .body(response)
                .build();
    }

    @Override
    public ResponseDTO getDetailChildReply(Long rno, int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Reply> pages = replyRepository.findByParentRnoOrderByRegDateAsc(rno, pageRequest);
        List<Reply> result = pages.getContent();

        List<ReplyInfoResponseDTO> responseList = new ArrayList<>();
        for (Reply reply : result) {
            ReplyInfoResponseDTO replyInfoResponseDTO = ReplyInfoResponseDTO.builder()
                    .parentRno(reply.getParent().getRno())
                    .bno(reply.getBoard().getBno())
                    .rno(reply.getRno())
                    .typeOfPost(TypeOfPost.nestRe.name())
                    .email(reply.getWriter().getEmail())
                    .nickName(reply.getWriter().getName())
                    .profilePicture(reply.getWriter().getProfileImage())
                    .commentImage(reply.getReplyImage())
                    .numberOfLike(replyLikeRepository.countByReply(reply))
                    .numberOfComments(replyRepository.countByParent(reply))
                    .contents(reply.getText())
                    .regDate(String.valueOf(reply.getRegDate()))
                    .isLike(replyLikeRepository.existsByReplyAndMember(reply, userDetails.getMember()))
                    .build();
            responseList.add(replyInfoResponseDTO);
        }

        PageResponseDTO<ReplyInfoResponseDTO> response = PageResponseDTO.<ReplyInfoResponseDTO>builder()
                .data(responseList)
                .page(pages.getNumber())
                .hasNext(pages.hasNext())
                .hasPrev(pages.hasPrevious())
                .build();

        return ResponseDTO.builder()
                .message("대댓글을 확인했습니다.")
                .body(response)
                .build();
    }

    @Override
    public ResponseDTO getDetailOneReply(Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(rno)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        ReplyInfoResponseDTO replyInfoResponseDTO = getReplyInfoResponseDTO(userDetails, reply);

        return ResponseDTO.builder()
                .body(replyInfoResponseDTO)
                .message("댓글정보 단일 조회")
                .build();
    }

    @Override
    public ResponseDTO getPageReply(Long bno, Long rno, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findByRno(rno)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        // 해당 댓글이 몇 번쨰인지 계산(작성시간 기준)
        long index = replyRepository.countByReplyBefore(bno, reply.getRegDate());

        int pageSize = 10;
        long pageNumber = index / pageSize;
        PageNumberResponseDTO response = PageNumberResponseDTO.builder()
                .pageNumber(pageNumber)
                .build();

        return ResponseDTO.builder()
                .message("댓글 pageNumber 결과")
                .body(response)
                .build();
    }

    @Override
    public ResponseDTO getPageNestReply(Long rno, Long nestRno, UserDetailsImpl userDetails) {
        Reply nestReply = replyRepository.findByRno(nestRno)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_REPLY));

        // 해당 댓글이 몇 번쨰인지 계산(작성시간 기준)
        long index = replyRepository.countNestedRepliesBefore(rno, nestReply.getRegDate());

        int pageSize = 10;
        long pageNumber = index / pageSize;
        PageNumberResponseDTO response = PageNumberResponseDTO.builder()
                .pageNumber(pageNumber)
                .build();

        return ResponseDTO.builder()
                .message("댓글 pageNumber 결과")
                .body(response)
                .build();
    }

    private ReplyInfoResponseDTO getReplyInfoResponseDTO(UserDetailsImpl userDetails, Reply reply) {

        return ReplyInfoResponseDTO.builder()
                .rno(reply.getRno())
                .bno(reply.getBoard().getBno())
                .typeOfPost(TypeOfPost.reply.name())
                .email(reply.getWriter().getEmail())
                .nickName(reply.getWriter().getName())
                .profilePicture(reply.getWriter().getProfileImage())
                .commentImage(reply.getReplyImage())
                .numberOfLike(replyLikeRepository.countByReply(reply))
                .numberOfComments(replyRepository.countByParent(reply))
                .contents(reply.getText())
                .regDate(String.valueOf(reply.getRegDate()))
                .isLike(replyLikeRepository.existsByReplyAndMember(reply, userDetails.getMember()))
                .build();
    }

    private void identification(String memberEmail, String userDetailsEmail) {
        if (!memberEmail.equals(userDetailsEmail)){
            throw new CustomException(ErrorCode.NOT_EQUALS_USER);
        }
    }

    private void insertReplyCreateNotification(Member sender, Reply reply, List<String> mentions) {
        List<Notification> notifications = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // [1] 게시글 작성자에게 알림 (댓글일 때)
        if (reply.getParent() == null) { // 일반 댓글
            Member boardWriter = reply.getBoard().getMember();
            if (!boardWriter.getEmail().equals(sender.getEmail())) {
                notifications.add(Notification.builder()
                        .type(NoteEnum.reply)
                        .sender(sender)
                        .receiver(boardWriter)
                        .board(reply.getBoard())
                        .reply(reply)
                        .isRead(false)
                        .createdAt(now)
                        .build());
            }
        }

        // [2] 멘션된 사용자
        if (mentions != null) {
            insertReplyEditNotification(sender, reply, mentions);
        }

        // 저장 및 SSE 전송
        notificationRepository.saveAll(notifications);
        notifications.forEach(note -> {
            notificationService.sendNotification(note.getReceiver().getEmail(),
                    NoteInfoResponseDTO.builder()
                            .type(note.getType().name())
                            .bno(note.getBoard().getBno())
                            .boardOneImage(null)
                            .rno(reply.getRno())
                            .email(sender.getEmail())
                            .from(sender.getEmail())
                            .userProfileImage(sender.getProfileImage())
                            .isFollowing(followRepository.existsByFromMemberAndToMember(note.getReceiver(), sender))
                            .createAt(now)
                            .isRead(false)
                            .build());
        });
    }

    private void insertReplyEditNotification(Member sender, Reply reply, List<String> mentions) {
        if (mentions == null || mentions.isEmpty()) return;

        List<Notification> notifications = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (String email : mentions) {
            memberRepository.findByEmail(email).ifPresent(mentionedUser -> {
                if (!mentionedUser.getEmail().equals(sender.getEmail())) {
                    notifications.add(Notification.builder()
                            .type(NoteEnum.mention)
                            .sender(sender)
                            .receiver(mentionedUser)
                            .board(reply.getBoard())
                            .reply(reply)
                            .isRead(false)
                            .createdAt(now)
                            .build());
                }
            });
        }

        notificationRepository.saveAll(notifications);
        notifications.forEach(note -> {
            notificationService.sendNotification(note.getReceiver().getEmail(),
                    NoteInfoResponseDTO.builder()
                            .type(note.getType().name())
                            .bno(note.getBoard().getBno())
                            .boardOneImage(null)
                            .rno(reply.getRno())
                            .email(sender.getEmail())
                            .from(sender.getEmail())
                            .userProfileImage(sender.getProfileImage())
                            .isFollowing(followRepository.existsByFromMemberAndToMember(note.getReceiver(), sender))
                            .createAt(now)
                            .isRead(false)
                            .build());
        });
    }



}
