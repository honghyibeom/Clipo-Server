package myproject.cliposerver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import myproject.cliposerver.data.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //이메일으로 유저 정보 찾기
    Optional<Member> findByEmail(String email);
    //리프레시 토큰으로 유저 정보 찾기
    Optional<Member> findByRefreshToken(String refreshToken);
    //닉네임으로 유저 정보 찾기
    Optional<Member> findByName(String nickName);
    //핸드폰으로 유저 정보 찾기
    Optional<Member> findByPhone(String phone);
    // 유저가 맞는지 확인
    boolean existsByEmail(String email);
    // 검색으로 유저 정보 찾기
    @Query("select m from members m where m.name like :search ")
    Page<Member> findBySearch(@Param("search") String search, Pageable pageable);

    // 인증이 안된 유저 제거
    void deleteByIsValidate(Boolean isValidate);

}
