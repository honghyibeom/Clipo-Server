package myproject.cliposerver.service;

import myproject.cliposerver.repository.MemberRepository;
import myproject.cliposerver.service.follow.FollowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FollowServiceTest {
    @Autowired
    private FollowServiceImpl followService;
    @Autowired
    private MemberRepository memberRepository;
}