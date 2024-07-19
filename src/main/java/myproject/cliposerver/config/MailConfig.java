package myproject.cliposerver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender naverMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.naver.com"); // SMTP 서버명
        javaMailSender.setUsername("ghdgmlqja1"); // 네이버 아이디
        javaMailSender.setPassword("apcarry123@"); // 네이버 비밀번호
        javaMailSender.setPort(465);

        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    // 메일 인증서버 정보 가져오기
    private Properties getMailProperties(){
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true"); // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", "true"); // smtp strattles 사용
        properties.setProperty("mail.debug", "true"); // 디버그 사용
        properties.setProperty("mail.smtp.ssl.trust", "smtp.naver.com"); // ssl 인증 서버 (smtp 서버명)
        properties.setProperty("mail.smtp.ssl.enable", "true"); // ssl 사용
        return properties;
    }
}
