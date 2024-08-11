package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);

    List<Follow> findByToMember(Member member);

    //팔로잉 수
    Long countByFromMember(Member fromMember);

    //팔로워 수
    Long countByToMember(Member toMember);
}
