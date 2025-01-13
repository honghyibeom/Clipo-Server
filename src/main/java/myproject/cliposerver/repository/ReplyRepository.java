package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.data.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Long countByBoard(Board board);
    Long countByParent(Reply reply);
    Page<Reply> findByWriterOrderByRegDateDesc(Member member, Pageable page);
    Page<Reply> findByBoardOrderByRegDateDesc(Board board, Pageable page);
    Page<Reply> findByParentRnoOrderByRegDateDesc(Long rno, Pageable page);

    Optional<Reply> findByRno(Long rno);


}
