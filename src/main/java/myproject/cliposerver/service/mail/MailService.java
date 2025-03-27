package myproject.cliposerver.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface MailService {
    // 메일 생성
    MimeMessage createMassage(String to) throws MessagingException, UnsupportedEncodingException;
    // 키 생성
    String createKey();
    // 메일 전송
    String sendSimpleMessage(String to) throws MessagingException, UnsupportedEncodingException;
}
