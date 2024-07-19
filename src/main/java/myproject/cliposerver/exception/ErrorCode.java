package myproject.cliposerver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_EXIST_USER("사용자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    EXIST_USER("중복된 사용자가 존재합니다.", HttpStatus.BAD_REQUEST),
    NOT_EQUALS_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    SMS_CERTIFICATION_NUMBER_MISMATCH("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_VALIDATE_USER("인증되지 않은 유저입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_CHANGED_TOKEN("이미 재발급이 완료된 토큰입니다.",HttpStatus.UNAUTHORIZED),
    FAIL_TO_CERTIFICATE("인증 요청이 정상적으로 실행되지 않았습니다.\n다시 로그인 해주세요.",HttpStatus.UNAUTHORIZED);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    ErrorCode(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
