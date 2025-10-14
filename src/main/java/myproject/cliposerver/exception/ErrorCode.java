package myproject.cliposerver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_EXIST_USER("사용자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_EQUALS_USER("사용자가 일지하지 않습니다.", HttpStatus.BAD_REQUEST),
    EXIST_USER("중복된 사용자가 존재합니다.", HttpStatus.BAD_REQUEST),
    NOT_EQUALS_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    SMS_CERTIFICATION_NUMBER_MISMATCH("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_VALIDATE_USER("인증되지 않은 유저입니다.", HttpStatus.UNAUTHORIZED),
    NOT_VALIDATE_TOKEN("유효한 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),
    NOT_VALIDATE_REFRESH_TOKEN("유효한 리프레시 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),
    EXIST_NICKNAME("같은 닉네임이 존재 합니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_BOARD("존재하지 않는 게시글",HttpStatus.BAD_REQUEST),
    NOT_EXIST_REPLY("존재하지 않는 댓글",HttpStatus.BAD_REQUEST),
    NOT_EXIST_NOTIFICATION("존재하지 않는 알림",HttpStatus.BAD_REQUEST),
    NOT_EXIST_FOLLOW("팔로우가 되어있지 않습니다.",HttpStatus.BAD_REQUEST),
    NOT_FOLLOW_SELF("자신을 팔로우 할 수 없습니다.",HttpStatus.BAD_REQUEST),
    EMPTY_FILE_EXCEPTION("파일이 없습니다.",HttpStatus.BAD_REQUEST),
    IO_EXCEPTION_ON_IMAGE_UPLOAD("S3 이미지 업로드 에러.",HttpStatus.BAD_REQUEST),
    NO_FILE_EXTENTION("파일 형식이 아닙니다.",HttpStatus.BAD_REQUEST),
    INVALID_FILE_EXTENTION("유효하지 않은 파일 형식입니다.",HttpStatus.BAD_REQUEST),
    PUT_OBJECT_EXCEPTION("S3 이미지 업로드중 에러.",HttpStatus.BAD_REQUEST),
    IO_EXCEPTION_ON_IMAGE_DELETE("S3 이미지 삭제중 에러.",HttpStatus.BAD_REQUEST),
    ALREADY_CHANGED_TOKEN("이미 재발급이 완료된 토큰입니다.",HttpStatus.UNAUTHORIZED),
    FAIL_TO_CERTIFICATE("인증 요청이 정상적으로 실행되지 않았습니다.",HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", HttpStatus.FORBIDDEN),
    INVALID_SIGNATURE_TOKEN("jwt 서명 오류", HttpStatus.FORBIDDEN),
    UNSUPPORTED_TOKEN("지원하지 않는 토큰", HttpStatus.FORBIDDEN),
    MALFORMED_TOKEN("형식 오류 등 일반적인 잘못된 토큰", HttpStatus.FORBIDDEN),
    S3_CHECK_FILE_EXISTENCE_EXCEPTION("S3_CHECK_FILE_EXISTENCE_EXCEPTION", HttpStatus.BAD_REQUEST),
    S3_DELETE_EXCEPTION("S3 이미지 삭제 실패...", HttpStatus.BAD_REQUEST),
    EXIST_IMAGE("기존이미지가 존재합니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_TAG("테그가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    WRONG_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UPLOAD_LIMIT_EXCEEDED("업로드한 이미지 갯수 초과", HttpStatus.BAD_REQUEST),
    BUCKET_FULL("버킷 100GB 초과 관리자 문의", HttpStatus.BAD_REQUEST);



    private final HttpStatus httpStatus;
    private final String errorMessage;
    ErrorCode(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
