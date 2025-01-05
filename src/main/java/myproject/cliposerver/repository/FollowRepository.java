package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);

    //팔로잉 수
    Long countByFromMember(Member fromMember);

    //팔로워 수
    Long countByToMember(Member toMember);

    //팔로워 조회
    Page<Follow> findByToMember(Member toMember, Pageable pageable);

    //팔로잉 조회
    Page<Follow> findByFromMember(Member fromMember,Pageable pageable);

    Boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);
}
