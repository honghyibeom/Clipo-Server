package myproject.cliposerver.service;

import myproject.cliposerver.service.board.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {
    @Autowired
    BoardServiceImpl boardServiceImpl;


}