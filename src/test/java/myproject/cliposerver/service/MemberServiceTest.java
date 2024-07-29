package myproject.cliposerver.service;

import myproject.cliposerver.data.dto.member.SignupRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void signupTest() {
        memberService.signup(
        SignupRequestDTO.builder()
                .email("ghdgmlqja1@naver.com")
                .password("1234")
                .build());
    }

    @Test
    void createPassword() throws Exception {
        memberService.forgotPassword("hh46318@gmail.com");
    }


}