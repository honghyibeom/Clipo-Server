package myproject.cliposerver.service;

import myproject.cliposerver.data.dto.follow.FollowRequestDTO;
import myproject.cliposerver.data.entity.Follow;
import myproject.cliposerver.data.entity.Member;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FollowServiceTest {
    @Autowired
    private FollowService followService;
    @Autowired
    private MemberRepository memberRepository;
    @Test
    void followTest() {
    }
    @Test
    void unfollowTest() {
    }
    @Test
    void getFollowList() {
        Member member = memberRepository.findByEmail("hh46318@gmail.com")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
        List<Follow> followList = member.getFollowToList();
        for (Object result :followList) {
            System.out.println(result.toString());
        }
    }


}