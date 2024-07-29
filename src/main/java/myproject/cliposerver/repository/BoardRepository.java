package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBno(Long bno);
}
