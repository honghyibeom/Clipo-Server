package myproject.cliposerver.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import myproject.cliposerver.exception.CustomExceptionDTO;
import myproject.cliposerver.exception.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    private static final ErrorCode errorCode = ErrorCode.FAIL_TO_CERTIFICATE;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //출력하는 타입은 map 타입. ObjectMapper 생성.
        //status 는 errorCode 의 HttpStatus.val();
        response.setStatus(errorCode.getHttpStatus().value());
        //미디어 타입은 json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //인코딩 utf8
        response.setCharacterEncoding("utf-8");
        //errorMessage 를 "data" 값으로 하는 ResponseDto 객제 생성.
        CustomExceptionDTO exceptionDTO = new CustomExceptionDTO(errorCode);
        //response 출력.
        try {
            response.getWriter().write(objectMapper.writeValueAsString(exceptionDTO));
        } catch (IOException e) {
            log.error("security filter exception 처리중 error발생 requestURL: {}", request.getRequestURI(), e);
        }
    }
}
//필요한 권한이 존재하지 않은 경우에러를 리턴하기 위한 클래스이다.
//ex) User권한을 가진 사람이 Admin 권한이 있는 곳에 접근하려 할 때 발생