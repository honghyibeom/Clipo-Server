package myproject.cliposerver.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService  {
    private final JavaMailSender javaMailSender;
    private String ePw; // 인증번호

    public MimeMessage createMassage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to); // 메일 받을 사용자
        message.setSubject("[Clipo] 비밀번호 변경을 위한 이메일 인증코드 입니다.");// 메일 제목
        String form = msgg();
        message.setText(form, "utf-8", "html"); // 메일 내용, charset타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("ghdgmlqja1@naver.com", "Clipo_Admin"));
        return message;
    }

    public String createKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String key = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println("생성된 랜덤 인증코드"+ key);
        return key;
    }

    // 메일 발송
    // sendSimpleMessage 의 매개변수 to는 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다
    // bean으로 등록해둔 javaMail 객체를 사용하여 이메일을 발송한다
    public String sendSimpleMessage(String to) throws Exception {
        ePw = createKey(); // 랜덤 인증코드 생성
        MimeMessage message = createMassage(to); // "to" 로 메일 발송

        try { // 예외처리
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw; // 메일로 사용자에게 보낸 인증코드를 서버로 반환! 인증코드 일치여부를 확인하기 위함
    }

    private String msgg() {
        String msgg ="";
        msgg += "<h1>안녕하세요</h1>";
        msgg += "<h1>Clipo SNS 입니다</h1>";
        msgg += "<br>";
        msgg += "<p>임시 비밀번호 입니다. 아래 비밀번호로 로그인 하시길 바랍니다.</p>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>변경될 비밀번호 입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + ePw + "</strong></div><br/>" ; // 메일에 인증번호 ePw 넣기
        msgg += "</div>";
        return msgg;
    }
}
