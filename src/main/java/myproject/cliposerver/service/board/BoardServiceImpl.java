package myproject.cliposerver.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardInfoResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import myproject.cliposerver.data.dto.reply.ReplyInfoResponseDTO;
import myproject.cliposerver.data.entity.*;

import myproject.cliposerver.data.enumerate.TypeOfPost;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.*;
import myproject.cliposerver.service.Image.S3ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final TagRepository tagRepository;
    private final FollowRepository followRepository;
    private final BoardImageRepository boardImageRepository;
    private final TagMapRepository tagMapRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final S3ImageService imageService;

    @Transactional
    public ResponseDTO createBoard(BoardRequestDTO boardRequestDTO, List<MultipartFile> boardImages , UserDetailsImpl userDetails){
        Board board = boardRequestDTO.toEntity(userDetails.getMember());

        //이미지 생성
        if (boardImages != null) {
            List<BoardImage> boardImageList = new ArrayList<>();
            List<String> imageUrl = imageService.uploadFileList(boardImages);
            for (String boardImage: imageUrl) {
                boardImageList.add(boardRequestDTO.toEntity(board,boardImage));
            }
            board.changeBoardImageList(boardImageList);
        }
        //tag 생성
        if (boardRequestDTO.getTag() != null) {
            List<Tag> boardTagList = new ArrayList<>();
            for (String tag : boardRequestDTO.getTag()) {
                boardTagList.add(boardRequestDTO.toEntity(tag));
            }

            tagRepository.saveAll(boardTagList);
            //tagMap 생성
            List<TagMap> tagMapList = new ArrayList<>();
            for (Tag tag : boardTagList) {
                tagMapList.add(boardRequestDTO.toEntity(board, tag));
            }
            board.changeTagMapList(tagMapList);
        }
        boardRepository.save(board);

        return ResponseDTO.builder()
                .message("게시글 생성")
                .body(board.getBno())
                .build();
    }

    @Transactional
    public ResponseDTO update(BoardRequestDTO boardRequestDTO, List<MultipartFile> boardImages, UserDetailsImpl userDetails) {
        // 게시글 조회
        Board board = boardRepository.findByBno(boardRequestDTO.getBno())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_BOARD));

        // 권한 확인
        identification(board.getMember().getEmail(), userDetails.getEmail());

        // 유지할 기존 이미지
        Set<String> originImage = Optional.ofNullable(boardRequestDTO.getOriginImages())
                .map(HashSet::new)
                .orElse(new HashSet<>());

        // 삭제 대상 이미지 추출
        List<BoardImage> deleteImages = board.getBoardImageList().stream()
                .filter(image -> !originImage.contains(image.getSrc())) // 유지되지 않는 이미지를 필터링
                .toList();

        // 삭제 대상 이미지 처리
        if (!deleteImages.isEmpty()) {
            deleteImages.forEach(image -> {
                imageService.deleteFile(image.getSrc()); // S3에서 삭제
            });
            board.getBoardImageList().removeAll(deleteImages); // 리스트에서 제거
        }

        // 새 이미지 업로드 및 저장
        if (boardImages != null && !boardImages.isEmpty()) {
            List<String> uploadedUrls = imageService.uploadFileList(boardImages);
            List<BoardImage> newImages = uploadedUrls.stream()
                    .map(url -> boardRequestDTO.toEntity(board, url))
                    .toList();
            board.getBoardImageList().addAll(newImages); // 새 이미지를 엔티티에 추가
        }

        // 태그 삭제 후 추가
        if (boardRequestDTO.getTag() != null) {
            List<TagMap> tagMaps = processTags(boardRequestDTO, board);
            board.changeTagMapList(tagMaps);
        }

        // 게시글 수정
        if (boardRequestDTO.getContent() != null) {
            board.changeContent(boardRequestDTO.getContent());
        }

        if (boardRequestDTO.getIsLikeVisible() != null) {
            board.changeLikeVisible(boardRequestDTO.getIsLikeVisible());
        }
        if (boardRequestDTO.getIsReplyAllowed() != null) {
            board.changeReplyAllowed(boardRequestDTO.getIsReplyAllowed());
        }

        // 변경 사항 저장
        boardRepository.save(board);

        return ResponseDTO.builder()
                .message("게시글 수정 완료")
                .build();
    }

    @Transactional
    public ResponseDTO delete(Long bno, UserDetailsImpl userDetails){
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));
        tagMapRepository.deleteByBoard(board);

        identification(board.getMember().getEmail(), userDetails.getEmail());

        for (String fileName : board.getBoardImageList().stream().map(BoardImage :: getSrc).toList()) {
            imageService.deleteFile(fileName);
        }

        boardRepository.delete(board);
        return ResponseDTO.builder()
                .message("삭제 완료")
                .body(bno)
                .build();
    }

    public ResponseDTO getMyBoardResponse(int page, String username,UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        Page<Board> pages = boardRepository.findByMemberOrderByRegDateDesc(member, pageRequest);
        List<Board> result = pages.getContent();
        List<BoardInfoResponseDTO> responseList = new ArrayList<>();
        for (Board board : result) {
            BoardInfoResponseDTO boardInfoResponseDTO = getBoardInfoResponseDTO(board, userDetails);
            responseList.add(boardInfoResponseDTO);
        }
        return ResponseDTO.builder()
                .message("포스트 확인했습니다.")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getMyReplyResponse(int page, String username, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
        Page<Reply> pages = replyRepository.findByWriterOrderByRegDateDesc(member, pageRequest);
        List<Reply> result = pages.getContent();

        List<ReplyInfoResponseDTO> responseList = new ArrayList<>();
        for (Reply reply : result) {
            ReplyInfoResponseDTO replyInfoResponseDTO = ReplyInfoResponseDTO.builder()
                    .rno(reply.getRno())
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
            responseList.add(replyInfoResponseDTO);
        }

        return ResponseDTO.builder()
                .message("작성한 댓글을 확인했습니다.")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getMyLikesResponse(int page, String username,UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        Page<Board> boardPages = boardRepository.findByBoardLikeListMemberOrderByRegDateDesc(member, pageRequest);
        List<Board> result = boardPages.getContent();
        List<BoardInfoResponseDTO> responseList = new ArrayList<>();

        for (Board board : result) {
            BoardInfoResponseDTO boardInfoResponseDTO = getBoardInfoResponseDTO(board, userDetails);
            responseList.add(boardInfoResponseDTO);
        }
        return ResponseDTO.builder()
                .message("좋아요한 포스트를 확인했습니다.")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getRandomBoard(int page,UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Board> boardPages = boardRepository.findAllByOrderByRegDateDesc(pageRequest);

        List<Board> result = boardPages.getContent();
        List<BoardInfoResponseDTO> responseList = new ArrayList<>();

        for(Board board: result) {
            BoardInfoResponseDTO boardInfoResponseDTO = getBoardInfoResponseDTO(board, userDetails);
            responseList.add(boardInfoResponseDTO);
        }

        return ResponseDTO.builder()
                .message("메인페이지 조회")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getDetailBoard(Long bno, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(bno)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));
        BoardInfoResponseDTO boardInfoResponseDTO = getBoardInfoResponseDTO(board, userDetails);

        return ResponseDTO.builder()
                .message("board 상세 조회")
                .body(boardInfoResponseDTO)
                .build();
    }

    private BoardInfoResponseDTO getBoardInfoResponseDTO(Board board, UserDetailsImpl userDetails) {

        return BoardInfoResponseDTO.builder()
                .bno(board.getBno())
                .typeOfPost(TypeOfPost.board.name())
                .nickName(board.getMember().getName())
                .profilePicture(board.getMember().getProfileImage())
                .numberOfLike(boardLikeRepository.countByBoard(board))
                .numberOfComments(replyRepository.countByBoard(board))
                .contents(board.getContent())
                .tag(board.getTagMapList().stream().map(tagMap -> tagMap.getTag().getWord()).toList())
                .regDate(String.valueOf(board.getRegDate()))
                .boardImages(board.getBoardImageList().stream().map(BoardImage::getSrc).toList())
                .isLike(boardLikeRepository.existsByBoardAndMember(board, userDetails.getMember()))
                .isFollowing(followRepository.existsByFromMemberAndToMember(userDetails.getMember(), board.getMember()))
                .isReplyAllowed(board.getIsReplyAllowed())
                .isLikeVisible(board.getIsLikeVisible())
                .build();
    }

    private List<TagMap> processTags(BoardRequestDTO boardRequestDTO, Board board) {
        tagMapRepository.deleteByBoard(board);

        List<Tag> tags = boardRequestDTO.getTag().stream()
                .map(boardRequestDTO::toEntity)
                .toList();
        tagRepository.saveAll(tags);

        return tags.stream()
                .map(tag -> boardRequestDTO.toEntity(board, tag))
                .toList();
    }

    private void identification(String memberEmail, String userDetailsEmail) {
        if (!memberEmail.equals(userDetailsEmail)){
            throw new CustomException(ErrorCode.NOT_EQUALS_USER);
        }
    }
}
