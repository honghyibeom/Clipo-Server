package myproject.cliposerver.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface MailService {
    MimeMessage createMassage(String to) throws MessagingException, UnsupportedEncodingException;
    String createKey();
    String sendSimpleMessage(String to) throws MessagingException, UnsupportedEncodingException;
}
