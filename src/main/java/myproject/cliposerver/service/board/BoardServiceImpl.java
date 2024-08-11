package myproject.cliposerver.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.board.BoardInfoResponseDTO;
import myproject.cliposerver.data.dto.board.BoardMainResponseDTO;
import myproject.cliposerver.data.dto.board.BoardRequestDTO;
import myproject.cliposerver.data.entity.*;

import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final ReplyRepository replyRepository;
    private final TagRepository tagRepository;
    private final TagMapRepository tagMapRepository;

    @Transactional
    public ResponseDTO createBoard(BoardRequestDTO boardRequestDTO, UserDetailsImpl userDetails){
        Board board = boardRequestDTO.toEntity(userDetails.getMember());

        //이미지 생성
        List<BoardImage> boardImageList = new ArrayList<>();
        for (String boardImage: boardRequestDTO.getBoardImageList()) {
            boardImageList.add(boardRequestDTO.toEntity(board,boardImage));
        }
        board.changeBoardImageList(boardImageList);
        //tag 생성
        List<Tag> boardTagList = new ArrayList<>();
        for (String tag: boardRequestDTO.getTag()){
            boardTagList.add(boardRequestDTO.toEntity(tag));
        }
        tagRepository.saveAll(boardTagList);
        //tagMap 생성
        List<TagMap> tagMapList = new ArrayList<>();
        for (Tag tag: boardTagList){
         tagMapList.add(boardRequestDTO.toEntity(board, tag));
        }
        board.changeTagMapList(tagMapList);
        boardRepository.save(board);

        return ResponseDTO.builder()
                .message("게시글 생성")
                .body(board.getBno())
                .build();
    }

    @Transactional// 태그 때문에 수정중
    public ResponseDTO update(BoardRequestDTO boardRequestDTO, UserDetailsImpl userDetails) {
        Board board = boardRepository.findByBno(boardRequestDTO.getBno())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_EXIST_BOARD));
        identification(board.getMember().getEmail(),userDetails.getEmail() );
        //board 수정
        board.changeContent(boardRequestDTO.getContent());
        //이미지 삭제 후 추가 작업
        boardImageRepository.deleteByBoard(board);
        for (String boardImage: boardRequestDTO.getBoardImageList()) {
            boardImageRepository.save(boardRequestDTO.toEntity(board,boardImage));
        }
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

        boardRepository.delete(board);
        return ResponseDTO.builder()
                .message("삭제 완료")
                .body(bno)
                .build();
    }

    public ResponseDTO getMyBoardResponse(int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Board> pages = boardRepository.findByMemberOrderByRegDateDesc(userDetails.getMember(), pageRequest);
        List<Board> result = pages.getContent();
        List<BoardInfoResponseDTO> responseList = new ArrayList<>();
        for (Board board : result) {
            BoardInfoResponseDTO boardInfoResponseDTO = BoardInfoResponseDTO.builder()
                    .email(board.getMember().getEmail())
                    .nickName(board.getMember().getName())
                    .profilePicture(board.getMember().getProfileImage())
                    .boardImage(board.getBoardImageList().stream().map(BoardImage::getSrc).toList())
                    .numberOfLike(board.getLikes())
                    .numberOfComments(replyRepository.countByBoard(board))
                    .contents(board.getContent())
                    .tag(board.getTagMapList().stream().map(tagMap -> tagMap.getTag().getWord()).toList())
                    .regDate(String.valueOf(board.getRegDate()))
                    .build();
            responseList.add(boardInfoResponseDTO);
        }
        return ResponseDTO.builder()
                .message("포스트 확인했습니다.")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getMyReplyResponse(int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Reply> pages = replyRepository.findByWriterOrderByRegDateDesc(userDetails.getMember(), pageRequest);
        List<Reply> result = pages.getContent();

        List<BoardInfoResponseDTO> responseList = new ArrayList<>();
        for (Reply reply : result) {
            BoardInfoResponseDTO boardInfoResponseDTO = BoardInfoResponseDTO.builder()
                    .email(reply.getWriter().getEmail())
                    .nickName(reply.getWriter().getName())
                    .profilePicture(reply.getWriter().getProfileImage())
                    .replyImage(reply.getReplyImage())
                    .numberOfLike(reply.getLikes())
                    .numberOfComments(replyRepository.countByParent(reply))
                    .contents(reply.getText())
                    .tag(null)
                    .regDate(String.valueOf(reply.getRegDate()))
                    .build();
            responseList.add(boardInfoResponseDTO);
        }

        return ResponseDTO.builder()
                .message("댓글을 확인했습니다.")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getMyLikesResponse(int page, UserDetailsImpl userDetails) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Board> boardPages = boardRepository.findByBoardLikeListMemberOrderByRegDateDesc(userDetails.getMember(), pageRequest);
        List<Board> result = boardPages.getContent();
        List<BoardInfoResponseDTO> responseList = new ArrayList<>();
        for (Board board : result) {
            BoardInfoResponseDTO boardInfoResponseDTO = BoardInfoResponseDTO.builder()
                    .email(board.getMember().getEmail())
                    .nickName(board.getMember().getName())
                    .profilePicture(board.getMember().getProfileImage())
                    .boardImage(board.getBoardImageList().stream().map(BoardImage::getSrc).toList())
                    .numberOfLike(board.getLikes())
                    .numberOfComments(replyRepository.countByBoard(board))
                    .contents(board.getContent())
                    .tag(board.getTagMapList().stream().map(tagMap -> tagMap.getTag().getWord()).toList())
                    .regDate(String.valueOf(board.getRegDate()))
                    .build();
            responseList.add(boardInfoResponseDTO);
        }
        return ResponseDTO.builder()
                .message("좋아요한 포스트를 확인했습니다.")
                .body(responseList)
                .build();
    }

    @Override
    public ResponseDTO getRandomBoard(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Board> boardPages = boardRepository.findAllByOrderByRegDateDesc(pageRequest);
        List<Board> result = boardPages.getContent();
        List<BoardMainResponseDTO> responseList = new ArrayList<>();
        for(Board board: result) {
            BoardMainResponseDTO boardMainResponseDTO = BoardMainResponseDTO.builder()
                    .nickName(board.getMember().getName())
                    .profilePicture(board.getMember().getProfileImage())
                    .numberOfLike(board.getLikes())
                    .numberOfComments(replyRepository.countByBoard(board))
                    .contents(board.getContent())
                    .tags(board.getTagMapList().stream().map(tagMap -> tagMap.getTag().getWord()).toList())
                    .regData(String.valueOf(board.getRegDate()))
                    .build();
            responseList.add(boardMainResponseDTO);
        }

        return ResponseDTO.builder()
                .message("메인페이지 조회")
                .body(responseList)
                .build();
    }

    private void identification(String memberEmail, String userDetailsEmail) {
        if (!memberEmail.equals(userDetailsEmail)){
            throw new CustomException(ErrorCode.NOT_EQUALS_USER);
        }
    }
}
