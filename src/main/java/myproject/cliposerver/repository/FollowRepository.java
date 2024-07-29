package myproject.cliposerver.repository;

import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFromMemberAndToMember(Member fromMember, Member toMember);

}
