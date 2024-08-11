package myproject.cliposerver.service;

import myproject.cliposerver.service.oAuth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    AuthService authService;

}